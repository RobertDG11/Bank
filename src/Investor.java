import java.util.function.Consumer;
import java.util.stream.Stream;

public class Investor extends Client implements Comparable<Investor> {
    private double moneyInvested;
    private double interest;

    static class InvestorBuilder {
        interface InvestorSetter extends Consumer<Investor> {}

        static Investor build(InvestorSetter... investorSetters) {
            final Investor investor = new Investor();

            Stream.of(investorSetters).forEach(
                    setter -> setter.accept(investor)
            );

            return investor;
        }

    }

    public Investor() {
        super();
    }

    public double getMoneyInvested() { return moneyInvested; }

    public void setMoneyInvested(double moneyInvested) {
        this.moneyInvested = moneyInvested;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    @Override
    public String toString() {
        return "Investor{" +
                "uniqueId=" + getUniqueId() +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", emailAddress='" + getEmailAddress() + '\'' +
                ", moneyInvested=" + moneyInvested +
                ", interest=" + interest +
                '}';
    }

    @Override
    public int compareTo(Investor i) {
        if (moneyInvested == 0)
            return 1;
        if (i.moneyInvested == 0)
            return -1;
        if (interest > i.interest)
            return 1;
        else if (interest < i.interest)
            return -1;
        else
            return 0;
    }

    public void withdrawMoney(double amount) {
        moneyInvested -= amount;
    }

    public void addMoney(double amount) {
        moneyInvested += amount;
    }
}
