import java.util.stream.Stream;

public interface IDatabase {
    void insertValuesIntoClient(String sql_query, Client client);
    void insertValuesIntoInvestor(String sql_query, Client client);
    void insertValuesIntoCreditor(String sql_query, Client client);
    Stream<Client> getInvestors(String sql_query);
    Client getClient(String sql_query, int unique_id);
    void updateInvestor(String sql_query, double newValue, int uniqueId);
    void updateCreditor(String sql_query, double lunarRate, double arpcIndex, int uniqueId);

}
