import java.util.Iterator;
import java.util.TreeSet;

public class Bank {
    private static final double COMMISSION = 0.003;
    private static Bank bank;

    private TreeSet<Investor> investors;
    private Creditor creditor;

    private Bank() {
        investors = new TreeSet<>();
    }

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

    public void removeInvestor(Investor investor) {
        if (investors.contains(investor)) {
            investors.remove(investor);
        }
    }

    public void changeInvestorInterest(int uniqueId, double interest) {
        Iterator<Investor> iterator = investors.iterator();
        while(iterator.hasNext()) {
            if (iterator.next().getUniqueId() == uniqueId) {
                investors.remove(iterator.next());
                iterator.next().setInterest(interest);
                investors.add(iterator.next());
                break;
            }
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
        double sum = 0;

        Iterator<Investor> iterator = investors.iterator();
        while(iterator.hasNext()) {
            sum += iterator.next().getMoneyInvested();
        }

        return sum;
    }

    public TreeSet<Investor> getInvestors() {
        return investors;
    }

    public void setCreditor(Creditor creditor) {
        this.creditor = creditor;
    }

    public void executeTransaction() {
        if (investors.isEmpty()) {
            System.out.println("Currently we don't have any investor! Sorry for inconvenience!");
            return;
        }

        Investor i = investors.pollFirst();
        if (i.getMoneyInvested() == 0) {
            System.out.println("Unfortunately we don't have enough funds now. Please try again later!");
            addInvestor(i);

            return;
        }

        if (i.getMoneyInvested() >= creditor.getCredit()) {
            creditor.calculateLunarRate(i.getInterest());
            creditor.addCommission(COMMISSION);
            creditor.calculateTotalToPay();
            creditor.calculateAPRC();

            i.withdrawMoney(creditor.getCredit());
            addInvestor(i);
            printSuccessfulMessage();

            return;
        }

        if (i.getMoneyInvested() < creditor.getCredit()) {
            if (calculateTotalAmount() >= creditor.getCredit()) {
                creditor.calculateLunarRate(i.getMoneyInvested(), i.getInterest());
                creditor.setCredit(creditor.getCredit() - i.getMoneyInvested());

                i.withdrawMoney(i.getMoneyInvested());
                addInvestor(i);
                executeTransaction();
            } else {
                System.out.println("Unfortunately we don't have enough funds now. Please try again later!");
                addInvestor(i);
                return;
            }
        }
    }
}
