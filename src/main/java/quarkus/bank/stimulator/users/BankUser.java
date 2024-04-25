package quarkus.bank.stimulator.users;

import lombok.Data;

@Data
public abstract class BankUser {

    String firstName;
    String lastName;
    String email;
    String phoneNumber;

    public BankUser() {
    }

    public BankUser(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
