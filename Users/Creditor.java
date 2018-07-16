import java.util.function.Consumer;
import java.util.stream.Stream;

public class Creditor extends Client {
    static final int MONTHS = 12;

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

    public void setCredit(double credit) { this.credit = credit; }

    public void setCreditBackup(double creditBackup) { this.creditBackup = creditBackup; }

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

    public void addCommission(double commission) {
        lunarRate += commission * creditBackup;
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

    public void setLunarRate(double lunarRate) { this.lunarRate += lunarRate; }

    public void setAprcIndex(double aprcIndex) { this.aprcIndex = aprcIndex; }

    @Override
    public String toString() {
        return "Creditor{" +
                "name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", emailAddress='" + getEmailAddress() + '\'' +
                ", periodOfBorrowing=" + periodOfBorrowing +
                ", credit=" + creditBackup +
                '}';
    }
}
