package info.seltenheim.mate.service;

import info.seltenheim.mate.service.sql.ExecutionResult;
import info.seltenheim.mate.service.sql.SqlUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import play.Configuration;
import play.Logger;
import play.Play;

@Component
@Profile("mateSqLite")
public class MateServiceSqLite implements MateService {
    private static final String SELECT_ALL = "SELECT * FROM user";

    private static final String SELECT_BY_ID = "SELECT * FROM user WHERE id = ?";
    private static final String SELECT_BY_NAME = "SELECT * FROM user WHERE name = ?";
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

    private MateJunky rowToJunky(Map<String, Object> row) {
        final int id = Integer.parseInt(row.get("id").toString());
        final String username = row.get("name").toString();
        final int count = Integer.parseInt(row.get("total_bottles").toString());
        final int credit = Integer.parseInt(row.get("credit").toString());

        return new MateJunky(id, username, count, credit);
    }
}
