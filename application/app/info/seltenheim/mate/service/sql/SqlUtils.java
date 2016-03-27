package info.seltenheim.mate.service.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbutils.DbUtils;

public class SqlUtils {

    private final String connectionString;
    private final Properties properties;
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionString, properties);
    }

    public SqlUtils(String connectionString, Properties properties) {
        this.connectionString = connectionString;
        this.properties = properties;
    }

    public List<Map<String, Object>> selectListFromTable(String preparedQuery, Object... params) throws IOException {
        final List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            stmt = connection.prepareStatement(preparedQuery);

            int index = 1;
            for (Object parameter : params) {
                stmt.setObject(index, parameter);
                index++;
            }

            stmt.execute();

            rs = stmt.getResultSet();
            rows.addAll(resultSetRowToList(rs));

        } catch (SQLException e) {
            throw new IOException("Error setting/executing select list statement", e);
        } finally {
            DbUtils.closeQuietly(connection, stmt, rs);
        }

        return rows;
    }

    public Map<String, Object> selectEntityFromTable(String preparedQuery, Object... params) throws IOException {
        Map<String, Object> row = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            stmt = connection.prepareStatement(preparedQuery);

            int count = 1;
            for (Object param : params) {
                stmt.setObject(count, param);
                count++;
            }

            stmt.execute();

            rs = stmt.getResultSet();

            List<Map<String, Object>> rows = resultSetRowToList(rs);
            if (!rows.isEmpty()) {
                row = rows.get(0);
            }
        } catch (SQLException e) {
            throw new IOException("Error setting/executing select statement", e);
        } finally {
            DbUtils.closeQuietly(connection, stmt, rs);
        }

        return row;
    }

    private static List<Map<String, Object>> resultSetRowToList(ResultSet rs) throws SQLException {
        final List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        final ResultSetMetaData resultSetMetaData = rs.getMetaData();
        final int columnCount = resultSetMetaData.getColumnCount();
        while (rs.next()) {
            HashMap<String, Object> row = new HashMap<String, Object>(columnCount);
            for (int i = 1; i <= columnCount; ++i) {
                final Object value = rs.getObject(i) == null ? "" : rs.getObject(i);
                row.put(resultSetMetaData.getColumnName(i), value);
            }
            rows.add(row);
        }

        return rows;
    }

    public ExecutionResult prepareAndExecuteStatement(final String preparedQuery, Object... params) throws IOException {
        int affectedRows = 0;
        Long insertId = null;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet generatedKeys = null;
        try {
            conn = getConnection();
            stm = conn.prepareStatement(preparedQuery);

            int index = 1;
            for (Object param : params) {
                stm.setObject(index, param);
                index++;
            }
            affectedRows = stm.executeUpdate();

            // check for generated Id in case of insert
            generatedKeys = stm.getGeneratedKeys();
            if (generatedKeys.next()) {
                insertId = generatedKeys.getLong(1);
            }
        } catch (SQLException e) {
            throw new IOException("Error setting/executing statement", e);
        } finally {
            DbUtils.closeQuietly(conn, stm, generatedKeys);
        }

        return new ExecutionResult(affectedRows, insertId);
    }

}
