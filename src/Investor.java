import java.util.Iterator;
import java.util.TreeSet;

public class Investor implements Comparable<Investor> {
    private int uniqueId;
    private String name;
    private String surname;
    private String phoneNumber;
    private String emailAddress;
    private double moneyInvested;
    private double moneyInvestedBackup;
    private double interest;

    static class InvestorBuilder {
        Investor investor = new Investor();

        InvestorBuilder withName(String name) {
            investor.setName(name);
            return this;
        }

        InvestorBuilder withSurname(String surname) {
            investor.setSurname(surname);
            return this;
        }

        InvestorBuilder withPhoneNumber(String phoneNumber) {
            investor.setPhoneNumber(phoneNumber);
            return this;
        }

        InvestorBuilder withEmailAddress(String emailAddress) {
            investor.setEmailAddress(emailAddress);
            return this;
        }

        InvestorBuilder withMoneyInvested(double moneyInvested) {
            investor.setMoneyInvested(moneyInvested);
            investor.setMoneyInvestedBackup(moneyInvested);
            return this;
        }

        InvestorBuilder withInterest(double interest) {
            investor.setInterest(interest);
            return this;
        }

        InvestorBuilder withUniqueId(int id) {
            investor.setUniqueId(id);
            return this;
        }

        Investor build() {
            return investor;
        }
    }

    public Investor() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public double getMoneyInvested() {
        return moneyInvested;
    }

    public void setMoneyInvested(double moneyInvested) {
        this.moneyInvested = moneyInvested;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public void addFunds(double amount) { moneyInvested += amount; }

    public int getUniqueId() { return uniqueId; }

    public void setUniqueId(int uniqueId) { this.uniqueId = uniqueId; }

    public double getMoneyInvestedBackup() { return moneyInvestedBackup; }

    public void setMoneyInvestedBackup(double moneyInvestedBackup) { this.moneyInvestedBackup = moneyInvestedBackup; }

    @Override
    public String toString() {
        return "Investor{" +
                "uniqueId=" + uniqueId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", moneyInvested=" + moneyInvested +
                ", interest=" + interest +
                '}';
    }

    @Override
    public int compareTo(Investor i) {
        if (moneyInvested == 0) {
            return 1;
        }
        if (i.moneyInvested == 0) {
            return -1;
        }
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

    public void revertMoney() {
        moneyInvested = moneyInvestedBackup;
    }
}
