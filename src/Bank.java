import java.util.TreeSet;

public class Bank {
    private static final double COMMISSION = 0.003;
    private static Bank bank;

    private TreeSet<Client> investors;
    private Creditor creditor;

    private Bank() { }

    public static Bank getInstance() {
        if (bank == null) {
            bank = new Bank();
        }

        return bank;
    }

    public void addInvestor(Investor investor) {
        if (investor != null) {
            investors.add(investor);
        }
    }

    public void printSuccessfulMessage() {
        System.out.format("Mr/Mrs %s your credit was approved. For your credit of %.2f you will " +
                "have to pay a lunar rate of %.2f for a total sum of %.2f. Your APRC index is %.2f%%" +
                ". Have a wonderful day!\n",
                creditor.getName(), creditor.getCreditBackup(), creditor.getLunarRate(),
                creditor.getTotalToPay(), creditor.getAprcIndex() * 100);

    }

    private double calculateTotalAmount() {
        return investors.stream().mapToDouble(client -> ((Investor)client).getMoneyInvested()).sum();
    }

    public TreeSet<Client> getInvestors() {
        return investors;
    }

    private void calculateLunarRate(double interest) {
        creditor.setLunarRate(creditor.getCredit() * interest / (Creditor.MONTHS * (1 -
                Math.pow(1 + (interest / Creditor.MONTHS), - creditor.getPeriodOfBorrowing()))));
    }

    private void calculateLunarRate(double amount, double interest) {
        creditor.setLunarRate(amount * interest / (Creditor.MONTHS * (1 -
                Math.pow(1 + (interest / Creditor.MONTHS), - creditor.getPeriodOfBorrowing()))));
    }

    private void calculateAPRC() {
        creditor.setAprcIndex(creditor.getLunarRate()  / creditor.getTotalToPay());
    }

    public void setCreditor(Creditor creditor) {
        this.creditor = creditor;
    }

    public Creditor getCreditor() { return creditor; }

    public void setInvestors(TreeSet<Client> investors) { this.investors = investors; }

    public void executeTransaction() {
        if (investors.isEmpty()) {
            System.out.println("Currently we don't have any investor! Sorry for inconvenience!");
            return;
        }

        Investor i = (Investor)investors.pollFirst();

        if (i.getMoneyInvested() == 0) {
            System.out.println("Unfortunately we don't have enough funds now. Please try again later!");
            addInvestor(i);

            return;
        }

        if (i.getMoneyInvested() >= creditor.getCredit()) {
            calculateLunarRate(i.getInterest());
            creditor.addCommission(COMMISSION);
            creditor.calculateTotalToPay();
            calculateAPRC();

            i.withdrawMoney(creditor.getCredit());
            addInvestor(i);
            printSuccessfulMessage();

            return;
        }

        if (i.getMoneyInvested() < creditor.getCredit()) {
            if (calculateTotalAmount() >= creditor.getCredit()) {
                calculateLunarRate(i.getMoneyInvested(), i.getInterest());
                creditor.setCredit(creditor.getCredit() - i.getMoneyInvested());

                i.withdrawMoney(i.getMoneyInvested());
                addInvestor(i);
                executeTransaction();
            } else {
                System.out.println("Unfortunately we don't have enough funds now. Please try again later!");
                addInvestor(i);
            }
        }
    }
}
