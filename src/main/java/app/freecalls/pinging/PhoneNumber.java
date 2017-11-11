package app.freecalls.pinging;

public class PhoneNumber {

    final String phoneNumber;

    public PhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return phoneNumber.toString();
    }
}
