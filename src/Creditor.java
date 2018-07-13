import java.util.function.Consumer;
import java.util.stream.Stream;

public class Creditor extends Client {
    private static final int MONTHS = 12;

    private int periodOfBorrowing;
    private double lunarRate;
    private double credit;
    private double totalToPay;
    private double creditBackup;
    private double aprcIndex;

    public Creditor() {
        super();
        lunarRate = 0;
        aprcIndex = 0;
    }

    public void setPeriodOfBorrowing(int periodOfBorrowing) { this.periodOfBorrowing = periodOfBorrowing; }

    public double getCredit() { return credit; }

    public void setCredit(double credit) {
        this.credit = credit;
        creditBackup = credit;
    }

    public int getPeriodOfBorrowing() { return periodOfBorrowing; }

    public double getCreditBackup() { return creditBackup; }

    public double getAprcIndex() { return aprcIndex; }

    static class CreditorBuilder {
        interface CreditorSetter extends Consumer<Creditor> {}

        public static Creditor build(CreditorSetter... creditorSetters) {
            final Creditor creditor = new Creditor();

            Stream.of(creditorSetters).forEach(
                    setter -> setter.accept(creditor)
            );

            return creditor;
        }
    }

    public void calculateLunarRate(double interest) {
        lunarRate += credit * interest / (MONTHS * (1 -
                Math.pow(1 + (interest / MONTHS), - periodOfBorrowing)));
    }

    public void calculateLunarRate(double amount, double interest) {
        lunarRate += amount * interest / (MONTHS * (1 -
                Math.pow(1 + (interest / MONTHS), - periodOfBorrowing)));
    }

    public void calculateAPRC() {
        aprcIndex = getLunarRate()  / getTotalToPay();
    }

    public void addCommission(double commission) {
        lunarRate += commission * credit;
    }

    public void calculateTotalToPay() {
        totalToPay = periodOfBorrowing * lunarRate;
    }

    public double getLunarRate() {
        return lunarRate;
    }

    public double getTotalToPay() {
        return totalToPay;
    }

    @Override
    public String toString() {
        return "Creditor{" +
                "name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", emailAddress='" + getEmailAddress() + '\'' +
                ", periodOfBorrowing=" + periodOfBorrowing +
                ", credit=" + credit +
                '}';
    }
}
