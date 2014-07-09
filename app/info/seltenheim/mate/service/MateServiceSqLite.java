package info.seltenheim.mate.service;

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
    private static final String SELECT_BY_NAME = "SELECT * FROM user WHERE user_name = ?";
    private static final String INSERT_JUNKY = "INSERT INTO user (user_name, bottle_count,bottle_remain) VALUES (?,0,0)";
    private static final String COUNT_MATE = "UPDATE user SET bottle_count=bottle_count+1, bottle_remain=bottle_remain-1 WHERE user_name = ?";
    private static final String ADD_REMAINING_MATE = "UPDATE user SET bottle_remain=bottle_remain+? WHERE user_name = ?";
    private static final String ALL_BOTTLES = "select sum(bottle_count) as count FROM user";
    private static final String LOG_MATE_COUNT = "insert into insert_log (user_id) values (?)";

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
    public int getTotalBottleCount() throws IOException {
        final Map<String, Object> row = SqlUtils.selectEntityFromTable(connectionString, ALL_BOTTLES);
        return Integer.parseInt(row.get("count").toString());
    }

    @Override
    public int countMate(String name) throws IOException {
        SqlUtils.prepareAndExecuteStatement(connectionString, COUNT_MATE, name);
        SqlUtils.prepareAndExecuteStatement(connectionString, LOG_MATE_COUNT, name);

        final MateJunky junky = findJunkyByName(name);
        return junky.getCount();
    }

    @Override
    public int addRemainingBottles(String name, int amount) throws IOException {
        SqlUtils.prepareAndExecuteStatement(connectionString, ADD_REMAINING_MATE, amount, name);

        final MateJunky junky = findJunkyByName(name);
        return junky.getRemaining();
    }

    @Override
    public double getCurrentBottlePrice() throws IOException {
        // TODO get price from config file
        Logger.warn("Bottle price not implement: See https://github.com/juliusse/mate-database/issues/5");
        return 0.75;
    }
    
    private MateJunky rowToJunky(Map<String, Object> row) {
        final String username = row.get("user_name").toString();
        final int count = Integer.parseInt(row.get("bottle_count").toString());
        final int remaining = Integer.parseInt(row.get("bottle_remain").toString());

        return new MateJunky(username, count, remaining);
    }
    
    
}
