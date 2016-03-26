package info.seltenheim.mate;

import java.io.IOException;

import info.seltenheim.mate.service.sql.SqlUtils;

public class DatabaseMigrator {

    public static void migrateDatabase(String dbPath) {
        final String connectionString = "jdbc:sqlite:" + dbPath;

        int version = -1;
        try {
            final String vString = SqlUtils.selectEntityFromTable(connectionString, "SELECT version FROM meta WHERE id = 1").get("version").toString();
            version = Integer.parseInt(vString);
        } catch (Exception e) {
            version = 1;
        }

        switch (version) {
        case 1:
            migrate1to2(connectionString);
        case 2:
            migrate2to3(connectionString);
        case 3:
            migrate3to4(connectionString);
        }

    }

    private static void migrate1to2(String connectionString) {
        try {
            SqlUtils.prepareAndExecuteStatement(connectionString, "DROP TABLE credit_payment_log");
            SqlUtils.prepareAndExecuteStatement(connectionString, "DROP TABLE mate_usage_log");
            SqlUtils.prepareAndExecuteStatement(connectionString, "DROP TRIGGER mate_usage_log_trigger");
            SqlUtils.prepareAndExecuteStatement(connectionString, "DROP TRIGGER credit_payment_log_trigger");

            SqlUtils.prepareAndExecuteStatement(connectionString, "CREATE TABLE `junky_log` ( `id`    INTEGER, `user_id`   INTEGER, `type`  STRING,"
                    + "`timestamp` INTEGER DEFAULT (datetime('now')), `credit_old`    INTEGER, `credit_new`    INTEGER, `bottles_old`   INTEGER, `bottles_new`   INTEGER," + "PRIMARY KEY(id) );");

            SqlUtils.prepareAndExecuteStatement(
                    connectionString,
                    "CREATE TRIGGER mate_drinking_trigger AFTER UPDATE OF total_bottles ON user FOR EACH ROW WHEN OLD.total_bottles != NEW.total_bottles BEGIN INSERT INTO junky_log (user_id, type, credit_old, credit_new, bottles_old, bottles_new) VALUES (OLD.id, \"drinking\", OLD.credit, NEW.credit, OLD.total_bottles, NEW.total_bottles); END;");
            SqlUtils.prepareAndExecuteStatement(
                    connectionString,
                    "CREATE TRIGGER payment_trigger AFTER UPDATE OF credit ON user FOR EACH ROW WHEN OLD.credit < NEW.credit BEGIN INSERT INTO junky_log (user_id, type, credit_old, credit_new, bottles_old, bottles_new) VALUES (OLD.id, \"payment\", OLD.credit, NEW.credit, OLD.total_bottles, NEW.total_bottles); END;");

            SqlUtils.prepareAndExecuteStatement(connectionString, "CREATE TABLE `meta` (`id`    INTEGER PRIMARY KEY AUTOINCREMENT, `version`   INTEGER);");
            SqlUtils.prepareAndExecuteStatement(connectionString, "INSERT INTO meta (version) VALUES (2)");
        } catch (Exception e) {

        }
    }

    private static void migrate2to3(String connectionString) {
        try {
            SqlUtils.prepareAndExecuteStatement(connectionString, "ALTER TABLE meta ADD COLUMN bottles_available INTEGER DEFAULT 0");
            SqlUtils.prepareAndExecuteStatement(connectionString, "UPDATE meta SET version = 3");
            SqlUtils.prepareAndExecuteStatement(connectionString, "DROP TRIGGER mate_drinking_trigger;");
            SqlUtils.prepareAndExecuteStatement(
                    connectionString,
                    "CREATE TRIGGER mate_drinking_trigger "
                            + "AFTER UPDATE OF total_bottles ON user FOR EACH ROW WHEN OLD.total_bottles != NEW.total_bottles "
                            + "BEGIN "
                            + "INSERT INTO junky_log (user_id, type, credit_old, credit_new, bottles_old, bottles_new) VALUES (OLD.id, \"drinking\", OLD.credit, NEW.credit, OLD.total_bottles, NEW.total_bottles); "
                            + "UPDATE meta SET bottles_available = bottles_available - 1; " + "END");
        } catch (IOException e) {
        }
    }
    
    private static void migrate3to4(String connectionString) {
        try {
            SqlUtils.prepareAndExecuteStatement(connectionString, "ALTER TABLE meta ADD COLUMN bottle_price INTEGER");
            SqlUtils.prepareAndExecuteStatement(connectionString, "UPDATE meta SET version = 4, bottle_price = 75");
        } catch (IOException e) {
        }
    }
}
