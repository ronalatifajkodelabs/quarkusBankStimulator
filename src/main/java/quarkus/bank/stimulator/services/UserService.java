package quarkus.bank.stimulator.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import quarkus.bank.stimulator.inMemoryDBs.DB;
import quarkus.bank.stimulator.users.BankAccountHolder;
import quarkus.bank.stimulator.users.BankEmployee;
import quarkus.bank.stimulator.users.BankOwner;
import quarkus.bank.stimulator.users.BankUser;
import quarkus.bank.stimulator.utils.CustomResponse;

@ApplicationScoped
public class UserService {

    public CustomResponse getUsers() {
        return CustomResponse.builder()
                .data(DB.bankUsers)
                .status(Response.Status.OK)
                .message("Users fetched successfully")
                .build();
    }

    public CustomResponse getUserResponse(String firstName, String lastName) {
        for (BankUser bankUser : DB.bankUsers) {
            if (bankUser.getFirstName().equals(firstName) && bankUser.getLastName().equals(lastName)) {
                return CustomResponse.builder()
                        .data(bankUser)
                        .status(Response.Status.OK)
                        .message("User fetched successfully")
                        .build();
            }
        }
        return null;
    }

    public BankUser getUser(String firstName, String lastName) {
        for (BankUser bankUser : DB.bankUsers) {
            if (bankUser.getFirstName().equals(firstName) && bankUser.getLastName().equals(lastName)) {
                return bankUser;
            }
        }
        return null;
    }

    public CustomResponse deleteUser(String firstName, String lastName) {
        BankUser bankUser = getUser(firstName, lastName);
        if (bankUser == null) {
            return CustomResponse.builder()
                    .data(null)
                    .status(Response.Status.NOT_FOUND)
                    .message("User not found")
                    .build();
        }
        DB.bankUsers.remove(bankUser);
        return CustomResponse.builder()
                .data(bankUser)
                .status(Response.Status.OK)
                .message("User deleted successfully")
                .build();
    }

    public CustomResponse updateUser(String firstName, String lastName, BankUser bankUserCloud) {

        BankUser bankUser = getUser(firstName, lastName);
        if (bankUser == null) {
            return CustomResponse.builder()
                    .data(null)
                    .status(Response.Status.NOT_FOUND)
                    .message("User not found")
                    .build();
        }
        bankUser.setFirstName(bankUserCloud.getFirstName() == null ? bankUser.getFirstName() : bankUserCloud.getFirstName());
        bankUser.setLastName(bankUserCloud.getLastName() == null ? bankUser.getLastName() : bankUserCloud.getLastName());
        bankUser.setEmail(bankUserCloud.getEmail() == null ? bankUser.getEmail() : bankUserCloud.getEmail());
        bankUser.setPhoneNumber(bankUserCloud.getPhoneNumber() == null ? bankUser.getPhoneNumber() : bankUserCloud.getPhoneNumber());
        return CustomResponse.builder()
                .data(bankUser)
                .status(Response.Status.OK)
                .message("User updated successfully")
                .build();
    }

    public CustomResponse addBankAccountHolder(BankAccountHolder bankUser) {
        DB.bankUsers.add(bankUser);
        return CustomResponse.builder()
                .data(bankUser)
                .status(Response.Status.CREATED)
                .message("Bank account holder added successfully")
                .build();
    }

    public CustomResponse addBankEmployee(BankEmployee bankUser) {
        DB.bankUsers.add(bankUser);
        return CustomResponse.builder()
                .data(bankUser)
                .status(Response.Status.CREATED)
                .message("Bank employee added successfully")
                .build();
    }

    public CustomResponse addBankOwner(BankOwner bankUser) {
        DB.bankUsers.add(bankUser);
        return CustomResponse.builder()
                .data(bankUser)
                .status(Response.Status.CREATED)
                .message("Bank owner added successfully")
                .build();
    }
}
