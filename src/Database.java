import java.sql.*;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Database implements IDatabase {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Bank?verifyServerCertificate=false&useSSL=true";

    private static final String USER = "root";
    private static final String PASS = "k7vGktpdcbb5";

    private static Connection connection = null;

    @Override
    public void insertValuesIntoClient(String sql_query, Client client) {
        DatabaseInserter.sql_query = sql_query;
        DatabaseInserter.setStatement(
            preparedStatement -> {
                try {
                    preparedStatement.setString(1, client.getName());
                    preparedStatement.setString(2, client.getSurname());
                    preparedStatement.setString(3, client.getEmailAddress());
                    preparedStatement.setString(4, client.getPhoneNumber());

                    preparedStatement.executeUpdate();
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
        Investor investor = (Investor) client;
        DatabaseInserter.sql_query = sql_query;
        DatabaseInserter.setStatement(
            preparedStatement -> {
                try {
                    preparedStatement.setDouble(1, investor.getMoneyInvested());
                    preparedStatement.setDouble(2, investor.getInterest());

                    preparedStatement.executeUpdate();
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
    public void insertValuesIntoCreditor(String sql_query, Client client) {
        Creditor creditor = (Creditor) client;
        DatabaseInserter.sql_query = sql_query;
        DatabaseInserter.setStatement(
            preparedStatement -> {
                try {
                    preparedStatement.setDouble(1, creditor.getCredit());
                    preparedStatement.setInt(2, creditor.getPeriodOfBorrowing());

                    preparedStatement.executeUpdate();
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

    public Database() {
        try {
            Class.forName(JDBC_DRIVER);

            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (Exception e) {
            e.printStackTrace();
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
             DatabaseMetaData md = connection.getMetaData();
             ResultSet rs = md.getTables(null, null, "%", null);

             while (rs.next()) {
                 if (rs.getString(3).equals(sql_query.split(" ")[2])) {
                     System.out.println("Table " + sql_query.split(" ")[2] + " already exists");
                     return;
                 }
             }

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

    void alterTableIncrement(String sql_query, int uniqueId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql_query);
            preparedStatement.setInt(1, uniqueId);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void insertValues(String sql_query, Client client) {
        if (client instanceof Investor) {
            insertValuesIntoClient(sql_query.split(";")[0], client);
            insertValuesIntoInvestor(sql_query.split(";")[1], client);
        }
        else if (client instanceof Creditor){
            insertValuesIntoClient(sql_query.split(";")[0], client);
            insertValuesIntoCreditor(sql_query.split(";")[1], client);
        }
    }

    String[] getString(ResultSet rs) throws SQLException {
        String[] fields = new String[7];

        for (int i = 0; i < 7; i++) {
            fields[i] = rs.getString(i + 1);
        }
        return fields;
    }

    @Override
    public Stream<Client> getInvestors(String sql_query) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql_query);

            ResultSet finalResultSet = resultSet;
            Stream<String[]> stream = StreamSupport.stream(new Spliterators.AbstractSpliterator<String[]>(
                    Long.MAX_VALUE,Spliterator.ORDERED) {
                @Override
                public boolean tryAdvance(Consumer<? super String[]> action) {
                    try {
                        if(!finalResultSet.next()) return false;
                        action.accept(getString(finalResultSet));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }, false);

            return stream.map(Server::getInvestor);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Client getClient(String sql_query, int uniqueId) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql_query);
            preparedStatement.setInt(1, uniqueId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next())
                return null;

            if (sql_query.split(" ")[6].equals("investor"))
                return Server.getInvestor(getString(resultSet));

            return Server.getCreditor(getString(resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void updateInvestor(String sql_query, double newValue, int uniqueId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql_query);
            preparedStatement.setDouble(1, newValue);
            preparedStatement.setInt(2, uniqueId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCreditor(String sql_query, double lunarRate, double arpcIndex, int uniqueId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql_query);
            preparedStatement.setDouble(1, lunarRate);
            preparedStatement.setDouble(2, arpcIndex);
            preparedStatement.setInt(3, uniqueId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
