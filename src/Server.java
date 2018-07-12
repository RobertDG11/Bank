import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

public class Server {
    private static final int CREDITOR_PARAMETERS = 6;

    private static int uniqueId = 0;
    private static int creditorNoParameter;
    private static Bank bank;

    private static void uiMessages() {
        switch (creditorNoParameter % CREDITOR_PARAMETERS) {
            case 0:
                    System.out.println("Enter your name:");
                break;
            case 1:
                    System.out.println("Enter your surname:");
                break;
            case 2:
                    System.out.println("Enter your email address:");
                break;
            case 3:
                    System.out.println("Enter your phone number:");
                break;
            case 4:
                    System.out.println("Enter the amount you want to borrow:");
                break;
            case 5:
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
            case 0:
                return checkName(parameter);
            case 1:
                return checkSurname(parameter);
            case 2:
                return checkEmail(parameter);
            case 3:
                return checkPhoneNumber(parameter);
            case 4:
                return checkAmount(parameter);
            case 5:
                return checkPeriodOfBorrowing(parameter);
        }

        return true;
    }

    private static Creditor getCreditor(String[] creditorFields) {
        return Creditor.CreditorBuilder.build(
                creditor -> creditor.setName(creditorFields[0]),
                creditor -> creditor.setSurname(creditorFields[1]),
                creditor -> creditor.setEmailAddress(creditorFields[2]),
                creditor -> creditor.setPhoneNumber(creditorFields[3]),
                creditor -> creditor.setCredit(Double.parseDouble(creditorFields[4])),
                creditor -> creditor.setPeriodOfBorrowing(Integer.parseInt(creditorFields[5]))
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
            do {
                uiMessages();
                tokens[creditorNoParameter % CREDITOR_PARAMETERS] = in.nextLine();
                if (tokens[creditorNoParameter % CREDITOR_PARAMETERS].matches("q|Q")) {
                    isQuitting = true;
                    break;
                }

            } while (!checkParameters(tokens[creditorNoParameter % 6]));

            if (creditorNoParameter % CREDITOR_PARAMETERS == 0) {
                bank.setCreditor(getCreditor(tokens));
                bank.executeTransaction();
                System.out.println();
            }

        }
    }

    private static Investor getInvestor(String[] investorFields) {
        return Investor.InvestorBuilder.build(
                investor -> investor.setName(investorFields[0]),
                investor -> investor.setSurname(investorFields[1]),
                investor -> investor.setEmailAddress(investorFields[2]),
                investor -> investor.setPhoneNumber(investorFields[3]),
                investor -> investor.setMoneyInvested(Double.parseDouble(investorFields[4])),
                investor -> investor.setInterest(Double.parseDouble(investorFields[5])),
                investor -> investor.setUniqueId(uniqueId++)
        );
    }

    private static void readInvestorsFromFile() throws IOException {
        if (bank == null) {
            System.out.println("Our server has technical issues. Please contact our support team and report" +
                    "this error. Thank you for understanding");
            return;
        }

        Path path = Paths.get("C:\\Users\\user\\IdeaProjects\\Streams\\investors.csv");
        Stream<Investor> investors = Files.lines(path)
                .map(line -> line.split(","))
                .map(Server::getInvestor);

        investors.forEach(investor -> bank.addInvestor(investor));
    }

    public static void main(String[] args) {
        bank = Bank.getInstance();

        try {
            readInvestorsFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        readCreditors();
    }
}
