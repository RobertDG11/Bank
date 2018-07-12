import java.sql.SQLException;

public interface IDatabase {
    void insertValuesIntoClient(String sql_query, Client client);
    void insertValuesIntoInvestor(String sql_query, Client client);
    void insertValuesIntoCreditor(String sql_query, Client client);
}
