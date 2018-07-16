public class SqlQueries {
    static final String CREATE_CLIENT_TABLE =
            "create table client " +
            "(" +
            "unique_id int auto_increment," +
            "name varchar(50) not null," +
            "surname varchar(50) not null," +
            "email_address varchar(50) not null," +
            "phone_number char(12) not null," +
            "primary key(unique_id)" +
            ");";
    static final String CREATE_INVESTOR_TABLE =
            "create table investor " +
            "(" +
            "unique_id int auto_increment," +
            "money_invested double not null," +
            "interest double not null," +
            "foreign key(unique_id) references client(unique_id)" +
            ");";

    static final String CREATE_CREDITOR_TABLE =
            "create table creditor " +
            "(" +
            "unique_id int auto_increment," +
            "credit double not null," +
            "period_borrowing double not null," +
            "lunar_rate double default 0," +
            "arpc_index double default 0," +
            "foreign key(unique_id) references client(unique_id)" +
            ");";

    static final String INSERT_CLIENT_TABLE =
            "insert into client " +
            "(name, surname, email_address, phone_number) " +
            "values (?, ?, ?, ?);";

    static final String INSERT_INVESTOR_TABLE =
            "insert into investor " +
            "(money_invested, interest) " +
            "values (?, ?);";

    static final String INSERT_CREDITOR_TABLE =
            "insert into creditor " +
            "(credit, period_borrowing) " +
            "values (?, ?);";

    static final String SELECT_INVESTORS =
            "select * from client " +
            "natural join investor;";

    static final String SELECT_INVESTOR =
            "select * from client " +
            "natural join investor " +
            "where unique_id = ?;";

    static final String SELECT_CREDITOR =
            "select * from client " +
            "natural join creditor " +
            "where unique_id = ?;";

    static final String UPDATE_INVESTOR =
            "update investor " +
            "set money_invested = ? " +
            "where unique_id = ?;";

    static final String UPDATE_CREDITOR =
            "update creditor " +
            "set lunar_rate = ?, arpc_index = ?" +
            "where unique_id = ?;";

    static final String ALTER_INCREMENT =
            "alter table creditor " +
            "auto_increment=?;";
}
