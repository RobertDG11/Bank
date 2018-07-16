public class Client {
    private int uniqueId;
    private String name;
    private String surname;
    private String phoneNumber;
    private String emailAddress;

    public Client() {
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}
