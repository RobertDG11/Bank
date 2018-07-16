import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Server {
    private static final int CREDITOR_PARAMETERS = 7;
    private static final String PATH = "Investors/investors.csv";

    private static int creditorNoParameter;
    private static Bank bank;
    private static Database database;
    private static int uniqueId;

    private static void uiMessages() {
        switch (creditorNoParameter % CREDITOR_PARAMETERS) {
            case 1:
                    System.out.println("Enter your name:");
                break;
            case 2:
                    System.out.println("Enter your surname:");
                break;
            case 3:
                    System.out.println("Enter your email address:");
                break;
            case 4:
                    System.out.println("Enter your phone number:");
                break;
            case 5:
                    System.out.println("Enter the amount you want to borrow:");
                break;
            case 6:
                    System.out.println("Enter the period of borrowing in months(must be multiple of 12, max 60):");
                break;
        }
    }

    private static boolean checkName(String parameter) {
        if (!parameter.matches("[A-Z][a-z]+")) {
            System.out.println("Please enter a valid name. Ex: Doe");
            System.out.println();
            return false;
        }
        creditorNoParameter++;
        return true;
    }

    private static boolean checkSurname(String parameter) {
        if (!parameter.matches("[A-Z][a-z]+")) {
            System.out.println("Please enter a valid surname. Ex: John");
            System.out.println();
            return false;
        }
        creditorNoParameter++;
        return true;
    }

    private static boolean checkEmail(String parameter) {
        if (!parameter.matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
                "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@" +
                "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+" +
                "(?:[A-Z]{2}|com|org|net|gov|mil|biz|info" +
                "|mobi|name|aero|jobs|museum)\\b")) {
            System.out.println("Please enter a valid email address. Ex: john.doe@yahoo.com");
            System.out.println();
            return false;
        }
        creditorNoParameter++;
        return true;
    }

    private static boolean checkPhoneNumber(String parameter) {
        if (!parameter.matches("\\+[0-9]{11}")) {
            System.out.println("Please enter a valid phone number. Ex: +40767524116");
            System.out.println();
            return false;
        }
        creditorNoParameter++;
        return true;
    }

    private static boolean checkAmount(String parameter) {
        if (!parameter.matches("[1-9][0-9]*(\\.[0-9]+)?")) {
            System.out.println("Please enter a valid amount. Ex: 200.22");
            System.out.println();
            return false;
        }
        creditorNoParameter++;
        return true;
    }

    private static boolean checkPeriodOfBorrowing(String parameter) {
        if (!parameter.matches("12|24|36|48|60")) {
            System.out.println("Please enter a valid period of borrowing. Ex: 24");
            System.out.println();
            return false;
        }
        creditorNoParameter++;
        return true;
    }

    private static boolean checkParameters(String parameter) {
        switch (creditorNoParameter % CREDITOR_PARAMETERS) {
            case 1:
                return checkName(parameter);
            case 2:
                return checkSurname(parameter);
            case 3:
                return checkEmail(parameter);
            case 4:
                return checkPhoneNumber(parameter);
            case 5:
                return checkAmount(parameter);
            case 6:
                return checkPeriodOfBorrowing(parameter);
        }

        return true;
    }

    static Creditor getCreditor(String[] creditorFields) {
        return Creditor.CreditorBuilder.build(
                creditor -> creditor.setUniqueId(Integer.parseInt(creditorFields[0])),
                creditor -> creditor.setName(creditorFields[1]),
                creditor -> creditor.setSurname(creditorFields[2]),
                creditor -> creditor.setEmailAddress(creditorFields[3]),
                creditor -> creditor.setPhoneNumber(creditorFields[4]),
                creditor -> creditor.setCredit(Double.parseDouble(creditorFields[5])),
                creditor -> creditor.setCreditBackup(Double.parseDouble(creditorFields[5])),
                creditor -> creditor.setPeriodOfBorrowing(Integer.parseInt(creditorFields[6]))
        );
    }

    private static void readCreditors() {
        if (bank == null) {
            System.out.println("Our server has technical issues. Please contact our support team and report" +
                    "this error. Thank you for understanding");
            return;
        }

        System.out.println("If you want to exit type q/Q");

        Scanner in = new Scanner(System.in);
        String[] tokens = new String[CREDITOR_PARAMETERS];
        boolean isQuitting = false;

        while (!isQuitting) {
            if (creditorNoParameter % CREDITOR_PARAMETERS == 0) {
                tokens[creditorNoParameter % CREDITOR_PARAMETERS] = String.valueOf(uniqueId);
                uniqueId++;
                creditorNoParameter++;
            }
            do {
                uiMessages();
                tokens[creditorNoParameter % CREDITOR_PARAMETERS] = in.nextLine();
                if (tokens[creditorNoParameter % CREDITOR_PARAMETERS].matches("[qQ]")) {
                    isQuitting = true;
                    break;
                }

            } while (!checkParameters(tokens[creditorNoParameter % CREDITOR_PARAMETERS]));

            if (creditorNoParameter % CREDITOR_PARAMETERS == 0) {
                bank.setCreditor(getCreditor(tokens));
                database.insertValues(SqlQueries.INSERT_CLIENT_TABLE +
                        SqlQueries.INSERT_CREDITOR_TABLE,
                        bank.getCreditor());

                bank.setInvestors(database.getInvestors(SqlQueries.SELECT_INVESTORS)
                        .collect(Collectors.toCollection(TreeSet::new)));

                bank.executeTransaction();
                updateInvestors();
                updateCreditor();

                System.out.println();
            }
        }
    }

     static Investor getInvestor(String[] investorFields) {
        return Investor.InvestorBuilder.build(
                investor -> investor.setUniqueId(Integer.parseInt(investorFields[0])),
                investor -> investor.setName(investorFields[1]),
                investor -> investor.setSurname(investorFields[2]),
                investor -> investor.setEmailAddress(investorFields[3]),
                investor -> investor.setPhoneNumber(investorFields[4]),
                investor -> investor.setMoneyInvested(Double.parseDouble(investorFields[5])),
                investor -> investor.setInterest(Double.parseDouble(investorFields[6]))
        );
    }

    private static void readInvestorsFromFile() throws IOException {
        if (bank == null) {
            System.out.println("Our server has technical issues. Please contact our support team and report" +
                    "this error. Thank you for understanding");
            return;
        }

        Path path = Paths.get(PATH);
        uniqueId = (int)Files.lines(path).count() + 1;
        Stream<Investor> investors = Files.lines(path)
                .map(line -> line.split(","))
                .map(Server::getInvestor);

        investors.forEach(investor -> database.insertValues(
                SqlQueries.INSERT_CLIENT_TABLE +
                        SqlQueries.INSERT_INVESTOR_TABLE,
                investor));
    }

    private static void updateInvestors() {
        bank.getInvestors().stream().forEach(investor -> database.updateInvestor(
                SqlQueries.UPDATE_INVESTOR,
                ((Investor)investor).getMoneyInvested(),
                investor.getUniqueId()));
    }

    private static void updateCreditor() {
        database.updateCreditor(
                SqlQueries.UPDATE_CREDITOR,
                bank.getCreditor().getLunarRate(),
                bank.getCreditor().getAprcIndex(),
                bank.getCreditor().getUniqueId());
    }

    public static void main(String[] args) {
        bank = Bank.getInstance();
        database = new Database();

        database.createTable(SqlQueries.CREATE_CLIENT_TABLE);
        database.createTable(SqlQueries.CREATE_INVESTOR_TABLE);
        database.createTable(SqlQueries.CREATE_CREDITOR_TABLE);

        try {
            readInvestorsFromFile();
            database.alterTableIncrement(SqlQueries.ALTER_INCREMENT, uniqueId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        readCreditors();
    }
}
