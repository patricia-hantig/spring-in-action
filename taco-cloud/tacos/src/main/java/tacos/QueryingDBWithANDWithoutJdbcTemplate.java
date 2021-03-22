package tacos;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.logging.Logger;

// this class shows the difference between working with and without JDBCTemplate class
public class QueryingDBWithANDWithoutJdbcTemplate {

    public static DataSource getDataSource() {
        DataSource dataSource = new DataSource() {
            @Override
            public Connection getConnection() throws SQLException {
                return null;
            }

            @Override
            public Connection getConnection(String username, String password) throws SQLException {
                return null;
            }

            @Override
            public <T> T unwrap(Class<T> iface) throws SQLException {
                return null;
            }

            @Override
            public boolean isWrapperFor(Class<?> iface) throws SQLException {
                return false;
            }

            @Override
            public PrintWriter getLogWriter() throws SQLException {
                return null;
            }

            @Override
            public void setLogWriter(PrintWriter out) throws SQLException {

            }

            @Override
            public void setLoginTimeout(int seconds) throws SQLException {

            }

            @Override
            public int getLoginTimeout() throws SQLException {
                return 0;
            }

            @Override
            public Logger getParentLogger() throws SQLFeatureNotSupportedException {
                return null;
            }
        };
        return dataSource;
    }

    // ■■■ querying a database without JdbcTemplate
    public Ingredient findOne(String id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getDataSource().getConnection();
            statement = connection.prepareStatement("SELECT id, name, type from Ingredient");
            statement.setString(1, id);
            resultSet = statement.executeQuery();
            Ingredient ingredient = null;
            if (resultSet.next()) {
                ingredient = new Ingredient(resultSet.getString("id"), resultSet.getString("name"), Ingredient.Type.valueOf(resultSet.getString("type")));
            }
            return ingredient;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {}
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {}
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {}
            }
        }
        return null;
    }
    // the lines for query the database for ingredients are surrounded by code that creates a connection, creates a statement and cleans up by closing the connection, statement, and result set
    // a lot of things could go wrong here - so we catch a SQLException every time => we need to know how to interpret that in order to figure out which was the problem


    // ■■■ querying a database with JdbcTemplate
    // in order to use JdbcTemplate class - please make sure you add the next dependency in pom.xml
    //      <dependency>
    //			<groupId>org.springframework.boot</groupId>
    //			<artifactId>spring-boot-starter-jdbc</artifactId>
    //		</dependency>

    private JdbcTemplate jdbcTemplate;

    public Ingredient findOne2(String id) {
        return jdbcTemplate.queryForObject("SELECT id, name, type from Ingredient where id=?", this::mapRowToIngredient, id);
    }

    private Ingredient mapRowToIngredient(ResultSet resultSet, int rowNum) throws SQLException {
        return new Ingredient(resultSet.getString("id"), resultSet.getString("name"), Ingredient.Type.valueOf(resultSet.getString("type")));
    }

}
