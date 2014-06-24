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
    public MateJunky updateJunky(MateJunky junky) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean removeJunky(String name) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getTotalBottleCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int countMate(String name) {
        // TODO Auto-generated method stub
        return 0;
    }

    private MateJunky rowToJunky(Map<String, Object> row) {
        final String username = row.get("user_name").toString();
        final int count = Integer.parseInt(row.get("bottle_count").toString());
        final int remaining = Integer.parseInt(row.get("bottle_remain").toString());

        return new MateJunky(username, count, remaining);
    }
}
