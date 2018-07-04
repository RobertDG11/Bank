import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Server {
    private enum UserType{
        INVESTOR,
        CREDITOR
    }

    private static final int INVESTOR_PARAMETERS = 6;
    private static final int CREDITOR_PARAMETERS = 6;

    private static int investorsNumber;
    private static int creditorsNumber;
    private static Bank bank;

    private static void uiMessages(int messageType, UserType userType) {
        int numberParameters;
        if (userType == UserType.INVESTOR) {
            numberParameters = INVESTOR_PARAMETERS;
        }
        else {
            numberParameters = CREDITOR_PARAMETERS;
        }

        switch (messageType % numberParameters) {
            case 0:
                if (userType == UserType.INVESTOR)
                    System.out.println("Enter investor's name:");
                else
                    System.out.println("Enter your name:");
                break;
            case 1:
                if (userType == UserType.INVESTOR)
                    System.out.println("Enter investor's surname:");
                else
                    System.out.println("Enter your surname:");
                break;
            case 2:
                if (userType == UserType.INVESTOR)
                    System.out.println("Enter investor's email address:");
                else
                    System.out.println("Enter your email address:");
                break;
            case 3:
                if (userType == UserType.INVESTOR)
                    System.out.println("Enter investor's phone number:");
                else
                    System.out.println("Enter your phone number:");
                break;
            case 4:
                if (userType == UserType.INVESTOR)
                    System.out.println("Enter the amount the investor want to invest:");
                else
                    System.out.println("Enter the amount you want to borrow:");
                break;
            case 5:
                if (userType == UserType.INVESTOR)
                    System.out.println("Enter investor's desired interest. Format 0.xxx:");
                else
                    System.out.println("Enter the period of borrowing in months(must be multiple of 12, max 60):");
                break;

        }
    }

    private static boolean checkParameters(String parameter, int parameterType, UserType userType) {
        int numberParameters;
        if (userType == UserType.INVESTOR) {
            numberParameters = INVESTOR_PARAMETERS;
        }
        else {
            numberParameters = CREDITOR_PARAMETERS;
        }

        switch (parameterType % numberParameters) {
            case 0:
                if (!parameter.matches("[a-zA-Z]+")) {
                    System.out.println("Please enter a valid name. Ex: Doe");
                    System.out.println();
                    return false;
                }
                break;
            case 1:
                if (!parameter.matches("[a-zA-Z]+")) {
                    System.out.println("Please enter a valid surname. Ex: John");
                    System.out.println();
                    return false;
                }
                break;
            case 2:
                if (!parameter.matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
                        "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@" +
                        "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+" +
                        "(?:[A-Z]{2}|com|org|net|gov|mil|biz|info" +
                        "|mobi|name|aero|jobs|museum)\\b")) {
                    System.out.println("Please enter a valid email address. Ex: john.doe@yahoo.com");
                    System.out.println();
                    return false;
                }
                break;
            case 3:
                if (!parameter.matches("\\+[0-9]{11}")) {
                    System.out.println("Please enter a valid phone number. Ex: +40767524116");
                    System.out.println();
                    return false;
                }
                break;
            case 4:
                if (!parameter.matches("[1-9][0-9]*(\\.[0-9]+)?")) {
                    System.out.println("Please enter a valid amount. Ex: 200.22");
                    System.out.println();
                    return false;
                }
                break;
            case 5:
                if (userType == UserType.INVESTOR) {
                    if (!parameter.matches("0\\.[0-9]+")) {
                        System.out.println("Please enter a valid interest. Ex: 0.15");
                        System.out.println();
                        return false;
                    }
                }
                else {
                    if (!parameter.matches("12|24|36|48|60")) {
                        System.out.println("Please enter a valid period of borrowing. Ex: 24");
                        System.out.println();
                        return false;
                    }
                }

                break;
        }

        return true;
    }

    private static void readInvestorsFromKeyboard(Scanner in) {
        if (bank == null) {
            System.out.println("Our server has technical issues. Please contact our support team and report" +
                    "this error. Thank you for understanding");
            return;
        }
        while (investorsNumber <= 0) {
            System.out.println("Insert the number of investors");
            investorsNumber = Integer.parseInt(in.nextLine());
            if (investorsNumber <= 0) {
                System.out.println("Please insert a positive non-zero value!");
                System.out.println();
            }
        }
        String[] tokens = new String[investorsNumber * INVESTOR_PARAMETERS];
        int currentInvestor = 0;
        for (int i = 0; i < investorsNumber * INVESTOR_PARAMETERS; i++) {
            do {
                uiMessages(i, UserType.INVESTOR);
                tokens[i] = in.nextLine();
            } while (!checkParameters(tokens[i], i, UserType.INVESTOR));

            if (i % INVESTOR_PARAMETERS == 5) {
                bank.addInvestor(new Investor.InvestorBuilder().withName(tokens[INVESTOR_PARAMETERS * currentInvestor])
                        .withSurname(tokens[INVESTOR_PARAMETERS * currentInvestor + 1])
                        .withEmailAddress(tokens[INVESTOR_PARAMETERS * currentInvestor + 2])
                        .withPhoneNumber(tokens[INVESTOR_PARAMETERS * currentInvestor + 3])
                        .withMoneyInvested(Double.parseDouble(tokens[INVESTOR_PARAMETERS * currentInvestor + 4]))
                        .withInterest(Double.parseDouble(tokens[INVESTOR_PARAMETERS * currentInvestor + 5]))
                        .withUniqueId(bank.getInvestors().size() + 1)
                        .build());

                currentInvestor++;
                System.out.println();
            }
        }
    }

    private static void readCreditors(Scanner in) {
        if (bank == null) {
            System.out.println("Our server has technical issues. Please contact our support team and report" +
                    "this error. Thank you for understanding");
            return;
        }

        while (creditorsNumber <= 0) {
            System.out.println("Insert the number of creditors");
            creditorsNumber = Integer.parseInt(in.nextLine());
            if (creditorsNumber <= 0) {
                System.out.println("Please insert a positive non-zero value!");
                System.out.println();
            }
        }
        String[] tokens = new String[creditorsNumber * CREDITOR_PARAMETERS];

        int currentCreditor = 0;
        for (int i = 0; i < creditorsNumber * CREDITOR_PARAMETERS; i++) {
            do {
                uiMessages(i, UserType.CREDITOR);
                tokens[i] = in.nextLine();
            } while (!checkParameters(tokens[i], i, UserType.CREDITOR));

            if (i % CREDITOR_PARAMETERS == 5) {
                Creditor creditor = new Creditor.CreditorBuilder()
                        .withName(tokens[currentCreditor * CREDITOR_PARAMETERS])
                        .withSurname(tokens[currentCreditor * CREDITOR_PARAMETERS + 1])
                        .withPhoneNumber(tokens[currentCreditor * CREDITOR_PARAMETERS + 2])
                        .withEmailAddress(tokens[currentCreditor * CREDITOR_PARAMETERS + 3])
                        .withCredit(Double.parseDouble(tokens[currentCreditor * CREDITOR_PARAMETERS + 4]))
                        .withPeriodOfBorrowing(Integer.parseInt(tokens[currentCreditor * CREDITOR_PARAMETERS + 5]))
                        .build();

                currentCreditor++;

                bank.setCreditor(creditor);
                bank.executeTransaction();
                System.out.println();
            }
        }
    }

    private static void readInvestorsFromFile() {
        String filepath = ("Investors/investors.csv");
        try {
            BufferedReader in = new BufferedReader(new FileReader(filepath));

            String line;

            while((line = in.readLine()) != null) {
                String[] tokens = line.split(",");

                bank.addInvestor(new Investor.InvestorBuilder().withName(tokens[0])
                        .withSurname(tokens[1])
                        .withEmailAddress(tokens[2])
                        .withPhoneNumber(tokens[3])
                        .withMoneyInvested(Double.parseDouble(tokens[4]))
                        .withInterest(Double.parseDouble(tokens[5]))
                        .withUniqueId(bank.getInvestors().size() + 1)
                        .build());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        bank = Bank.getInstance();

        System.out.println("Please choose a method to read investors. Type 1 for keyboard and 2 for file.");

        int method = Integer.parseInt(in.nextLine());

        if (method == 1) {
            readInvestorsFromKeyboard(in);
        }
        else {
            readInvestorsFromFile();
        }

        readCreditors(in);
    }
}
