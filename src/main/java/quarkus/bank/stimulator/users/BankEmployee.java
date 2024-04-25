package quarkus.bank.stimulator.users;

import lombok.Data;
import lombok.EqualsAndHashCode;
import quarkus.bank.stimulator.accounts.BankAccount;
import quarkus.bank.stimulator.accounts.CheckingBankAccount;
import quarkus.bank.stimulator.accounts.SavingsBankAccount;
import quarkus.bank.stimulator.enums.TransactionType;
import quarkus.bank.stimulator.inMemoryDBs.DB;
import quarkus.bank.stimulator.transactions.Transaction;
import quarkus.bank.stimulator.transactions.DepositTransaction;
import quarkus.bank.stimulator.transactions.WithdrawTransaction;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class BankEmployee extends BankUser {

    public BankEmployee(String firstName, String lastName, String email, String phoneNumber) {
        super(firstName, lastName, email, phoneNumber);
    }


    /*TODO
    addBankAccount ✅
    displayInformationForBankAccount ✅
    updateBankAccountBalance ✅
    runMonthlyUpdate ✅
     */

    private SavingsBankAccount addSavingsBankAccount(String accountNumber, BankAccountHolder accountHolder, double minimumBalance) {
        if (minimumBalance < 0) {
            System.out.println("Minimum balance can't be negative");
            return null;
        }
        SavingsBankAccount account = new SavingsBankAccount(accountNumber, accountHolder, minimumBalance);
        DB.bankAccounts.add(account);
        return account;
    }

    private CheckingBankAccount addCheckingBankAccount(String accountNumber, BankAccountHolder accountHolder) {
        CheckingBankAccount account = new CheckingBankAccount(accountNumber, accountHolder);
        DB.bankAccounts.add(account);
        return account;
    }

    private void displayInformationForBankAccount(BankAccount bankAccount) {
        System.out.println(bankAccount.toString());
    }

    public void displayInformationForBankAccount(String accountNumber) {
        for (BankAccount bankAccount : DB.bankAccounts) {
            if (bankAccount.getAccountNumber().equals(accountNumber)) {
                System.out.println(bankAccount);
                return;
            }
        }
        System.out.println("Account not found");
    }

    public void updateBankAccountBalance(BankAccount bankAccount, TransactionType transactionType, double amount) {
        if (transactionType == TransactionType.DEPOSIT) {
            DepositTransaction.performTransaction(bankAccount, amount);
        } else if (transactionType == TransactionType.WITHDRAWAL) {
            WithdrawTransaction.performTransaction(bankAccount, amount);
        }
    }

    public void updateBankAccountBalanceGeneric(BankAccount bankAccount, Transaction transactionTypeClass, double amount) {
//        Transaction.performTransaction(bankAccount, amount);
    }


    //TODO check this out me Ernen
    // this is not an abstract method can I call it from superclass?
//    public <T extends Transaction> void updateBankAccountBalance(BankAccount bankAccount, T transaction, double amount) {
//        transaction.performTransaction(bankAccount, amount);
//    }


    public void runMonthlyUpdateForAllAccounts() {
        for (BankAccount bankAccount : DB.bankAccounts) {
            bankAccount.runMonthlyUpdate();
        }
    }

    public void addSavingsBankAccount(SavingsBankAccount savingsBankAccount) {
        DB.bankAccounts.add(savingsBankAccount);
    }
}

//checked if account is paused for monthly billings
//checked if account has sufficient funds
//deducted monthly fee
//added transaction to transaction history
//    private void runMonthlyUpdate(BankAccount bankAccount) {
//        if (bankAccount.getPausedMonthlyBillingsUntil() != null && bankAccount.getPausedMonthlyBillingsUntil().isAfter(LocalDateTime.now())) {
//            return;
//        }
//        if (bankAccount.getAccountType() == AccountType.CHECKING) {
//            if (bankAccount.getBalanceAmount() < CheckingBankAccount.MONTHLY_FEE) {
//                System.out.println("Monthly fee of " + CheckingBankAccount.MONTHLY_FEE + " for checking account " + bankAccount.getAccountNumber() + "has not been deducted due to insufficient funds");
//                return;
//            }
//            bankAccount.setBalanceAmount(bankAccount.getBalanceAmount() - (CheckingBankAccount.MONTHLY_FEE));
//            Transaction bankServicesBillingTransaction = Transaction.builder()
//                    .amount(CheckingBankAccount.MONTHLY_FEE)
//                    .sourceAccount(bankAccount)
//                    .transactionTime(LocalDateTime.now())
//                    .transactionType(TransactionType.BANK_BILLING)
//                    .transactionStatus(TransactionStatus.COMPLETED)
//                    .build();
//            TransactionDB.transactions.add(bankServicesBillingTransaction);
//            System.out.println("Monthly fee of " + CheckingBankAccount.MONTHLY_FEE + " for checking account " + bankAccount.getAccountNumber() + "has been deducted");
//        } else if (bankAccount.getAccountType() == AccountType.SAVINGS) {
//            if (bankAccount.getBalanceAmount() < (bankAccount.getBalanceAmount() * SavingsBankAccount.INTEREST_RATE)) {
//                System.out.println("Monthly fee of " + (bankAccount.getBalanceAmount() - (bankAccount.getBalanceAmount() * SavingsBankAccount.INTEREST_RATE)) + " for saving account " + bankAccount.getAccountNumber() + "has not been deducted due to insufficient funds");
//                return;
//            }
//            System.out.println("Monthly fee of " + formatToTwoDecimals(bankAccount.getBalanceAmount() * SavingsBankAccount.INTEREST_RATE) + " for saving account " + bankAccount.getAccountNumber() + "has been deducted");
//            Transaction bankServicesBillingTransaction = Transaction.builder()
//                    .amount(bankAccount.getBalanceAmount() * SavingsBankAccount.INTEREST_RATE)
//                    .sourceAccount(bankAccount)
//                    .transactionTime(LocalDateTime.now())
//                    .transactionType(TransactionType.BANK_BILLING)
//                    .transactionStatus(TransactionStatus.COMPLETED)
//                    .build();
//            bankAccount.setBalanceAmount(bankAccount.getBalanceAmount() - (bankAccount.getBalanceAmount() * SavingsBankAccount.INTEREST_RATE));
//            TransactionDB.transactions.add(bankServicesBillingTransaction);
//        }
//    }
