package info.seltenheim.mate;

import java.io.IOException;

import play.Logger;
import info.seltenheim.mate.service.MateServiceSqLite;
import info.seltenheim.mate.service.sql.SqlUtils;

public class DatabaseMigrator {

    private final SqlUtils sqlUtils;

    private DatabaseMigrator() {
        sqlUtils = MateServiceSqLite.getSqlUtils();
    }

    public static void migrateDatabase() {
        new DatabaseMigrator().doMigrateDatabase();
    }

    public void doMigrateDatabase() {
        int version = -1;
        try {
            final String vString = sqlUtils.selectEntityFromTable("SELECT version FROM meta WHERE id = 1").get("version").toString();
            version = Integer.parseInt(vString);
        } catch (Exception e) {
            version = 1;
        }

        try {
            switch (version) {
            case 1:
                Logger.debug("Migrate to database version 2");
                migrate1to2();
            case 2:
                Logger.debug("Migrate to database version 3");
                migrate2to3();
            case 3:
                Logger.debug("Migrate to database version 4");
                migrate3to4();
            case 4:
                Logger.debug("Migrate to database version 5");
                migrate4to5();
            }
        } catch (IOException e) {
            Logger.error("Failed to migrate database.", e);
        }
    }

    private void migrate1to2() throws IOException {
        sqlUtils.prepareAndExecuteStatement("DROP TABLE credit_payment_log");
        sqlUtils.prepareAndExecuteStatement("DROP TABLE mate_usage_log");
        sqlUtils.prepareAndExecuteStatement("DROP TRIGGER mate_usage_log_trigger");
        sqlUtils.prepareAndExecuteStatement("DROP TRIGGER credit_payment_log_trigger");

        sqlUtils.prepareAndExecuteStatement("CREATE TABLE `junky_log` ( `id`    INTEGER, `user_id`   INTEGER, `type`  STRING,"
                + "`timestamp` INTEGER DEFAULT (datetime('now')), `credit_old`    INTEGER, `credit_new`    INTEGER, `bottles_old`   INTEGER, `bottles_new`   INTEGER," + "PRIMARY KEY(id) );");

        sqlUtils.prepareAndExecuteStatement("CREATE TRIGGER mate_drinking_trigger AFTER UPDATE OF total_bottles ON user FOR EACH ROW WHEN OLD.total_bottles != NEW.total_bottles BEGIN INSERT INTO junky_log (user_id, type, credit_old, credit_new, bottles_old, bottles_new) VALUES (OLD.id, \"drinking\", OLD.credit, NEW.credit, OLD.total_bottles, NEW.total_bottles); END;");
        sqlUtils.prepareAndExecuteStatement("CREATE TRIGGER payment_trigger AFTER UPDATE OF credit ON user FOR EACH ROW WHEN OLD.credit < NEW.credit BEGIN INSERT INTO junky_log (user_id, type, credit_old, credit_new, bottles_old, bottles_new) VALUES (OLD.id, \"payment\", OLD.credit, NEW.credit, OLD.total_bottles, NEW.total_bottles); END;");

        sqlUtils.prepareAndExecuteStatement("CREATE TABLE `meta` (`id`    INTEGER PRIMARY KEY AUTOINCREMENT, `version`   INTEGER);");
        sqlUtils.prepareAndExecuteStatement("INSERT INTO meta (version) VALUES (2)");
    }

    private void migrate2to3() throws IOException {
        sqlUtils.prepareAndExecuteStatement("ALTER TABLE meta ADD COLUMN bottles_available INTEGER DEFAULT 0");
        sqlUtils.prepareAndExecuteStatement("UPDATE meta SET version = 3");
        sqlUtils.prepareAndExecuteStatement("DROP TRIGGER mate_drinking_trigger;");
        sqlUtils.prepareAndExecuteStatement("CREATE TRIGGER mate_drinking_trigger " + "AFTER UPDATE OF total_bottles ON user FOR EACH ROW WHEN OLD.total_bottles != NEW.total_bottles " + "BEGIN "
                + "INSERT INTO junky_log (user_id, type, credit_old, credit_new, bottles_old, bottles_new) VALUES (OLD.id, \"drinking\", OLD.credit, NEW.credit, OLD.total_bottles, NEW.total_bottles); "
                + "UPDATE meta SET bottles_available = bottles_available - 1; " + "END");
    }

    private void migrate3to4() throws IOException {
        sqlUtils.prepareAndExecuteStatement("ALTER TABLE meta ADD COLUMN bottle_price INTEGER");
        sqlUtils.prepareAndExecuteStatement("UPDATE meta SET version = 4, bottle_price = 75");
    }

    private void migrate4to5() throws IOException {
        sqlUtils.prepareAndExecuteStatement("ALTER TABLE user ADD COLUMN disabled INTEGER DEFAULT 0");
        sqlUtils.prepareAndExecuteStatement("CREATE TRIGGER mate_user_disable_trigger " + "AFTER UPDATE OF disabled ON user FOR EACH ROW WHEN OLD.disabled = 0 and NEW.disabled != 0 " + "BEGIN "
                + "INSERT INTO junky_log (user_id, type, credit_old, credit_new, bottles_old, bottles_new) VALUES (OLD.id, \"disabled\", OLD.credit, NEW.credit, OLD.total_bottles, NEW.total_bottles); " + "END");
        sqlUtils.prepareAndExecuteStatement("DROP TRIGGER payment_trigger");
        sqlUtils.prepareAndExecuteStatement("CREATE TRIGGER payment_trigger AFTER UPDATE OF credit ON user FOR EACH ROW WHEN OLD.credit != NEW.credit AND OLD.total_bottles = NEW.total_bottles AND NEW.disabled = 0 BEGIN INSERT INTO junky_log (user_id, type, credit_old, credit_new, bottles_old, bottles_new) VALUES (OLD.id, \"payment\", OLD.credit, NEW.credit, OLD.total_bottles, NEW.total_bottles); END;");
        sqlUtils.prepareAndExecuteStatement("CREATE TRIGGER user_creation AFTER INSERT ON user FOR EACH ROW BEGIN INSERT INTO junky_log (user_id, type, credit_old, credit_new, bottles_old, bottles_new) VALUES (NEW.id, \"created\", NEW.credit, NEW.credit, NEW.total_bottles, NEW.total_bottles); END;");
        
        sqlUtils.prepareAndExecuteStatement("UPDATE meta SET version = 5");
    }
}
