package info.seltenheim.mate.service;

import info.seltenheim.mate.service.sql.ExecutionResult;
import info.seltenheim.mate.service.sql.SqlUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import play.Configuration;
import play.Logger;
import play.Play;
import play.libs.Json;

@Component
@Profile("mateSqLite")
public class MateServiceSqLite implements MateService {
    private static final String SELECT_ALL = "SELECT * FROM user";

    private static final String SELECT_BY_ID = "SELECT * FROM user WHERE id = ?";
    private static final String SELECT_BY_NAME = "SELECT * FROM user WHERE name = ?";
    private static final String SELECT_LOG_ENTRIES = "SELECT * FROM junky_log ORDER BY timestamp DESC";
    private static final String INSERT_JUNKY = "INSERT INTO user (name) VALUES (?)";
    private static final String UPDATE_JUNKY = "UPDATE user SET name = ?, total_bottles = ?, credit = ? WHERE id = ?";

    private final String connectionString;

    public MateServiceSqLite() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            Logger.error("jdbc error", e);
        }
        final Configuration config = Play.application().configuration();
        connectionString = "jdbc:sqlite:" + config.getString("info.seltenheim.mate.sqlite.location");
    }

    @Override
    public List<MateJunky> findAllJunkies() throws IOException {
        final List<MateJunky> junkies = new ArrayList<>();

        final List<Map<String, Object>> rows = SqlUtils.selectListFromTable(connectionString, SELECT_ALL);
        for (Map<String, Object> row : rows) {
            junkies.add(rowToJunky(row));
        }
        return junkies;
    }

    @Override
    public MateJunky findJunkyById(int id) throws IOException {
        MateJunky junky = null;
        final Map<String, Object> row = SqlUtils.selectEntityFromTable(connectionString, SELECT_BY_ID, id);

        if (row != null) {
            junky = rowToJunky(row);
        }

        return junky;
    }

    @Override
    public MateJunky findJunkyByName(String name) throws IOException {
        MateJunky junky = null;
        final Map<String, Object> row = SqlUtils.selectEntityFromTable(connectionString, SELECT_BY_NAME, name);

        if (row != null) {
            junky = rowToJunky(row);
        }

        return junky;
    }

    @Override
    public MateJunky addJunky(String name) throws IOException {
        final MateJunky junkyWithName = findJunkyByName(name);

        if (junkyWithName == null) {
            SqlUtils.prepareAndExecuteStatement(connectionString, INSERT_JUNKY, name);
            return findJunkyByName(name);
        }
        // TODO correct handling
        return null;
    }

    @Override
    public boolean updateJunky(MateJunky junky) throws IOException {
        final ExecutionResult result = SqlUtils.prepareAndExecuteStatement(connectionString, UPDATE_JUNKY, junky.getName(), junky.getCount(), junky.getCredit(), junky.getId());
        return result.getAffectedRows() == 1;
    }

    @Override
    public double getCurrentBottlePrice() throws IOException {
        // TODO get price from config file
        Logger.warn("Bottle price not implement: See https://github.com/juliusse/mate-database/issues/5");
        return 0.75;
    }

    @Override
    public JsonNode getMetaInformationAsJson() throws IOException {
        Map<String, Object> metaRow = SqlUtils.selectEntityFromTable(connectionString, "SELECT * FROM meta WHERE id = ?", 1);
        final String dbVersion = metaRow.get("version").toString();
        final String bottlesAvailable = metaRow.get("bottles_available").toString();

        return Json.parse("{\"dbVersion\":" + dbVersion + ", \"bottlesAvailable\":" + bottlesAvailable + "}");
    }

    @Override
    public void addMate(int count) throws IOException {
        SqlUtils.prepareAndExecuteStatement(connectionString, "UPDATE meta SET bottles_available = bottles_available + ?", count);
    }

    @Override
    public List<MateLogEntry> getAllLogEntries() throws IOException {
        final List<MateLogEntry> entries = new ArrayList<MateLogEntry>();
        final List<Map<String, Object>> rows = SqlUtils.selectListFromTable(connectionString, SELECT_LOG_ENTRIES);
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
}
