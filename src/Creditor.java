public class Creditor {
    private static final int MONTHS = 12;

    private String name;
    private String surname;
    private String phoneNumber;
    private String emailAddress;
    private int periodOfBorrowing;
    private double lunarRate;
    private double credit;
    private double totalToPay;
    private double creditBackup;
    private double aprcIndex;

    public Creditor() {
        lunarRate = 0;
        aprcIndex = 0;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }

    public void setSurname(String surname) { this.surname = surname; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmailAddress() { return emailAddress; }

    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public double getPeriodOfBorrowing() { return periodOfBorrowing; }

    public void setPeriodOfBorrowing(int periodOfBorrowing) { this.periodOfBorrowing = periodOfBorrowing; }

    public double getCredit() { return credit; }

    public void setCredit(double credit) { this.credit = credit; }

    public double getCreditBackup() { return creditBackup; }

    public void setCreditBackup(double credit) { this.creditBackup = credit; }

    public double getAprcIndex() { return aprcIndex; }

    static class CreditorBuilder {
        Creditor creditor = new Creditor();

        CreditorBuilder withName(String name) {
            creditor.setName(name);
            return this;
        }

        CreditorBuilder withSurname(String surname) {
            creditor.setSurname(surname);
            return this;
        }

        CreditorBuilder withPhoneNumber(String phoneNumber) {
            creditor.setPhoneNumber(phoneNumber);
            return this;
        }

        CreditorBuilder withEmailAddress(String emailAddress) {
            creditor.setEmailAddress(emailAddress);
            return this;
        }

        CreditorBuilder withPeriodOfBorrowing(int periodOfBorrowing) {
            creditor.setPeriodOfBorrowing(periodOfBorrowing);
            return this;
        }

        CreditorBuilder withCredit(double credit) {
            creditor.setCredit(credit);
            creditor.setCreditBackup(credit);
            return this;
        }

        Creditor build() {
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
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", periodOfBorrowing=" + periodOfBorrowing +
                ", credit=" + credit +
                '}';
    }
}
