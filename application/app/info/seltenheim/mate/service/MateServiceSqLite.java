package info.seltenheim.mate.service;

import info.seltenheim.mate.service.sql.ExecutionResult;
import info.seltenheim.mate.service.sql.SqlUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.sqlite.SQLiteConfig;

import play.Configuration;
import play.Logger;
import play.Play;

import com.fasterxml.jackson.databind.JsonNode;

@Component
@Profile("mateSqLite")
public class MateServiceSqLite implements MateService {
    private static final String SELECT_ALL = "SELECT * FROM user WHERE disabled = 0";

    private static final String SELECT_BY_ID = "SELECT * FROM user WHERE id = ?";
    private static final String SELECT_BY_NAME = "SELECT * FROM user WHERE name = ?";
    private static final String SELECT_LOG_ENTRIES = "SELECT * FROM junky_log ORDER BY timestamp DESC";
    private static final String INSERT_JUNKY = "INSERT INTO user (name) VALUES (?)";
    private static final String UPDATE_JUNKY = "UPDATE user SET name = ?, total_bottles = ?, credit = ? WHERE id = ?";

    private final SqlUtils sqlUtils;
    
    public static SqlUtils getSqlUtils() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            Logger.error("jdbc error", e);
        }

        final SQLiteConfig sqliteConfig = new SQLiteConfig();
        sqliteConfig.enforceForeignKeys(true);

        final Configuration config = Play.application().configuration();
        final String connectionString = "jdbc:sqlite:" + config.getString("info.seltenheim.mate.sqlite.location");

        return new SqlUtils(connectionString, sqliteConfig.toProperties());
    }

    public MateServiceSqLite() {
        sqlUtils = getSqlUtils();
    }

    @Override
    public List<MateJunky> findAllJunkies() throws IOException {
        final List<MateJunky> junkies = new ArrayList<>();

        final List<Map<String, Object>> rows = sqlUtils.selectListFromTable(SELECT_ALL);
        for (Map<String, Object> row : rows) {
            junkies.add(rowToJunky(row));
        }
        return junkies;
    }

    @Override
    public MateJunky findJunkyById(int id) throws IOException {
        MateJunky junky = null;
        final Map<String, Object> row = sqlUtils.selectEntityFromTable(SELECT_BY_ID, id);

        if (row != null) {
            junky = rowToJunky(row);
        }

        return junky;
    }

    @Override
    public MateJunky findJunkyByName(String name) throws IOException {
        MateJunky junky = null;
        final Map<String, Object> row = sqlUtils.selectEntityFromTable(SELECT_BY_NAME, name);

        if (row != null) {
            junky = rowToJunky(row);
        }

        return junky;
    }

    @Override
    public MateJunky addJunky(String name) throws IOException {
        ExecutionResult executionResult = sqlUtils.prepareAndExecuteStatement(INSERT_JUNKY, name);
        
        return findJunkyById(executionResult.getGeneratedId().intValue());
    }

    @Override
    public boolean updateJunky(MateJunky junky) throws IOException {
        final ExecutionResult result = sqlUtils.prepareAndExecuteStatement(UPDATE_JUNKY, junky.getName(), junky.getCount(), junky.getCredit(), junky.getId());
        return result.getAffectedRows() == 1;
    }

    @Override
    public JsonNode getMetaInformationAsJson() throws IOException {
        return getMetaInformation().getJsonNode();
    }

    private MetaInformation getMetaInformation() throws IOException {
        Map<String, Object> metaRow = sqlUtils.selectEntityFromTable("SELECT * FROM meta WHERE id = ?", 1);

        final int dbVersion = Integer.parseInt(metaRow.get("version").toString());
        final int bottlesAvailable = Integer.parseInt(metaRow.get("bottles_available").toString());
        final int currentBottlePrice = Integer.parseInt(metaRow.get("bottle_price").toString());

        return new MetaInformation(dbVersion, bottlesAvailable, currentBottlePrice);
    }

    @Override
    public void addMate(int count) throws IOException {
        sqlUtils.prepareAndExecuteStatement("UPDATE meta SET bottles_available = bottles_available + ?", count);
    }

    @Override
    public List<MateLogEntry> getAllLogEntries() throws IOException {
        final List<MateLogEntry> entries = new ArrayList<MateLogEntry>();
        final List<Map<String, Object>> rows = sqlUtils.selectListFromTable(SELECT_LOG_ENTRIES);
        for (Map<String, Object> row : rows) {
            entries.add(rowToLogEntry(row));
        }
        return entries;
    }

    private MateJunky rowToJunky(Map<String, Object> row) {
        final int id = Integer.parseInt(row.get("id").toString());
        final String username = row.get("name").toString();
        final int count = Integer.parseInt(row.get("total_bottles").toString());
        final int credit = Integer.parseInt(row.get("credit").toString());

        return new MateJunky(id, username, count, credit);
    }

    private MateLogEntry rowToLogEntry(Map<String, Object> row) throws IOException {
        final int id = Integer.parseInt(row.get("id").toString());
        final int userId = Integer.parseInt(row.get("user_id").toString());
        final MateJunky junky = findJunkyById(userId);
        final MateLogEntry.Type type = MateLogEntry.Type.valueOf(row.get("type").toString());
        final String timestamp = row.get("timestamp").toString();
        final int credit_old = Integer.parseInt(row.get("credit_old").toString());
        final int credit_new = Integer.parseInt(row.get("credit_new").toString());
        final int bottles_new = Integer.parseInt(row.get("bottles_new").toString());
        final int bottles_old = Integer.parseInt(row.get("bottles_old").toString());

        return new MateLogEntry(id, junky, type, timestamp, credit_old, credit_new, bottles_old, bottles_new);
    }

    @Override
    public void setCurrentBottlePrice(double newPricePerBottle) throws IOException {
        System.out.println(newPricePerBottle);
        sqlUtils.prepareAndExecuteStatement("UPDATE meta SET bottle_price = ? WHERE id = ?", (int) Math.round(newPricePerBottle * 100), 1);
    }

    public int getCurrentBottlePrice() throws IOException {
        return getMetaInformation().getCurrentBottlePrice();
    }

    @Override
    public void drinkMate(MateJunky junky) throws IOException {
        junky.setCredit(junky.getCredit() - getCurrentBottlePrice());
        junky.setCount(junky.getCount() + 1);
        updateJunky(junky);
    }

    @Override
    public void deleteJunkie(int id) throws IOException {
        sqlUtils.prepareAndExecuteStatement("UPDATE user SET disabled = 1, credit = 0 WHERE id = ?", id);
    }
}
