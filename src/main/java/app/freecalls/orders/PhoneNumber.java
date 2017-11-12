package app.freecalls.orders;

import hackphone.phone.inviteing.CallingTo;

public class PhoneNumber {

    final String phoneNumber;

    public PhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return phoneNumber.toString();
    }

    public CallingTo callingTo() {
        return new CallingTo(phoneNumber);
    }
}
