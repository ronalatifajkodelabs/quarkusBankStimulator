package quarkus.bank.stimulator.utils;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import quarkus.bank.stimulator.accounts.BankAccount;
import quarkus.bank.stimulator.transactions.Transaction;
import quarkus.bank.stimulator.users.BankAccountHolder;
import quarkus.bank.stimulator.users.BankEmployee;
import quarkus.bank.stimulator.users.BankOwner;
import quarkus.bank.stimulator.users.BankUser;

import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class DBInitializer {

    void onStart(@Observes StartupEvent ev) {
        System.out.println("The application is starting...");
        List<BankAccount> bankAccountsFromJSON = quarkus.bank.stimulator.inMemoryDBs.DB.fillData("src/main/java/quarkus/bank/stimulator/jsonFiles/bankAccounts.json", BankAccount.class);
        List<? extends BankUser> bankUsersFromJson = quarkus.bank.stimulator.inMemoryDBs.DB.fillData("src/main/java/quarkus/bank/stimulator/jsonFiles/bankAccountHolders.json", BankAccountHolder.class);
        List<? extends BankUser> bankEmployeesFromJson = quarkus.bank.stimulator.inMemoryDBs.DB.fillData("src/main/java/quarkus/bank/stimulator/jsonFiles/bankEmployees.json", BankEmployee.class);
        List<? extends BankUser> bankOwnersFromJson = quarkus.bank.stimulator.inMemoryDBs.DB.fillData("src/main/java/quarkus/bank/stimulator/jsonFiles/bankOwners.json", BankOwner.class);
        List<BankUser> bankUsersFromJSON = new LinkedList<>();
        bankUsersFromJSON.addAll(bankUsersFromJson);
        bankUsersFromJSON.addAll(bankEmployeesFromJson);
        bankUsersFromJSON.addAll(bankOwnersFromJson);
        List<Transaction> transactionsFromJSON = quarkus.bank.stimulator.inMemoryDBs.DB.fillData("src/main/java/quarkus/bank/stimulator/jsonFiles/transactions.json", Transaction.class);

        quarkus.bank.stimulator.inMemoryDBs.DB.bankAccounts.addAll(bankAccountsFromJSON);
        quarkus.bank.stimulator.inMemoryDBs.DB.bankUsers.addAll(bankUsersFromJSON);
        quarkus.bank.stimulator.inMemoryDBs.DB.transactions.addAll(transactionsFromJSON);
        System.out.println(quarkus.bank.stimulator.inMemoryDBs.DB.bankAccounts);
        System.out.println(quarkus.bank.stimulator.inMemoryDBs.DB.bankUsers);
        System.out.println(quarkus.bank.stimulator.inMemoryDBs.DB.transactions);
    }

}
