import java.sql.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Database implements IDatabase {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/";

    private static final String USER = "root";
    private static final String PASS = "k7vGktpdcbb5";

    private static Connection connection = null;

    @Override
    public void insertValuesIntoClient(String sql_query, Client client) {
        DatabaseInserter.sql_query = sql_query;
        PreparedStatement statement = DatabaseInserter.setStatement(
                preparedStatement -> {
                    try {
                        preparedStatement.setInt(1, client.getUniqueId());
                        preparedStatement.setString(2, client.getName());
                        preparedStatement.setString(3, client.getSurname());
                        preparedStatement.setString(4, client.getEmailAddress());
                        preparedStatement.setString(5, client.getPhoneNumber());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (preparedStatement != null) {
                            try {
                                preparedStatement.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );


    }

    @Override
    public void insertValuesIntoInvestor(String sql_query, Client client) {

    }

    @Override
    public void insertValuesIntoCreditor(String sql_query, Client client) {

    }

    static class DatabaseInserter {
        static String sql_query = null;
        interface StatementsSetter extends Consumer<PreparedStatement> {}

        static PreparedStatement setStatement(StatementsSetter... statementsSetters) {
            final PreparedStatement statement;
            try {
                statement = connection.prepareStatement(sql_query);
                Stream.of(statementsSetters).forEach(
                        setter -> setter.accept(statement)
                );
                return statement;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public Database(String databaseName) {
        try {
            Class.forName(JDBC_DRIVER);

            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL + databaseName, USER, PASS);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void createDatabase(String databaseName) {
        System.out.println("Creating database...");
        Statement  statement = null;
        try {
            statement = connection.createStatement();
            String sql_query = "CREATE DATABASE " + databaseName;

            statement.executeUpdate(sql_query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Database created");
    }

     void createTable(String sql_query) {
        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql_query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void insertValues(String sql_query) {

    }
}
