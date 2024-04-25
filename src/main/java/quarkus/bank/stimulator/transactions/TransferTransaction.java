package quarkus.bank.stimulator.transactions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import quarkus.bank.stimulator.accounts.BankAccount;
import quarkus.bank.stimulator.accounts.SavingsBankAccount;
import quarkus.bank.stimulator.enums.AccountType;
import quarkus.bank.stimulator.enums.TransactionStatus;
import quarkus.bank.stimulator.enums.TransactionType;
import quarkus.bank.stimulator.inMemoryDBs.DB;

@Getter
@Setter
public class TransferTransaction extends Transaction {

    private BankAccount destinationAccount;
    private final TransactionForTwoBankAccounts transactionForTwoBankAccounts;

    @Builder
    TransferTransaction(BankAccount sourceAccount, double amount, LocalDateTime transactionTime, TransactionStatus transactionStatus, BankAccount destinationAccount, TransactionForTwoBankAccounts transactionForTwoBankAccounts) {
        super(sourceAccount, amount, TransactionType.TRANSFER, transactionTime, transactionStatus);
        this.destinationAccount = destinationAccount;
        this.transactionForTwoBankAccounts = transactionForTwoBankAccounts;
    }


    @Override
    public void performTransaction() {
        transactionForTwoBankAccounts.performTransaction(this.getSourceAccount(), this.destinationAccount, this.getAmount());
    }

    public void performTransaction(BankAccount sourceAccount, BankAccount destinationAccount, double amount) {
        if (sourceAccount == destinationAccount) {
            System.out.println("You can't transfer money to the same account");
            return;
        }
        if (sourceAccount == null || destinationAccount == null) {
            System.out.println("At least one of the accounts was not found");
            return;
        }
        if (sourceAccount.getAccountType().equals(AccountType.SAVINGS)) {
            if (amount > ((SavingsBankAccount) sourceAccount).getMinimumBalance()) {
                System.out.println("You can't transfer more than the minimum balance of the account");
            }
        }
        if (sourceAccount.getBalanceAmount() < amount) {
            Transaction transaction = TransferTransaction.builder()
                    .sourceAccount(sourceAccount)
                    .destinationAccount(destinationAccount)
                    .amount(amount)
                    .transactionTime(LocalDateTime.now())
                    .transactionStatus(TransactionStatus.FAILED)
                    .build();
            DB.transactions.add(transaction);
            System.out.println("Insufficient funds");
        } else {
            sourceAccount.setBalanceAmount(sourceAccount.getBalanceAmount() - amount);
            destinationAccount.setBalanceAmount(destinationAccount.getBalanceAmount() + amount);
            DB.transactions.add(TransferTransaction.builder()
                    .sourceAccount(sourceAccount)
                    .destinationAccount(destinationAccount)
                    .amount(amount)
                    .transactionTime(LocalDateTime.now())
                    .transactionStatus(TransactionStatus.COMPLETED)
                    .build());
            System.out.println("Transfer successful");
        }
    }

    @Override
    public String toString() {
        return "Transaction from account of: " + this.getSourceAccount().getAccountHolder().getFirstName() + " " + this.getSourceAccount().getAccountHolder().getLastName() +
                "\n     Transaction Type: " + this.getTransactionType() +
                "\n     Transaction to account of: " + destinationAccount.getAccountHolder().getFirstName() +
                "\n     Amount: " + this.getAmount() +
                "\n     Transaction Time: " + this.getTransactionTime() +
                "\n     Transaction Status: " + this.getTransactionStatus();
    }

//    @Override
//    public void performTransaction(Object... args) {
//        if (args.length != 3 || !(args[0] instanceof BankAccount) || !(args[1] instanceof BankAccount) || !(args[2] instanceof Double)) {
//            throw new IllegalArgumentException("Invalid arguments for TransferTransaction");
//        }
//        BankAccount sourceAccount = (BankAccount) args[0];
//        BankAccount destinationAccount = (BankAccount) args[1];
//        double amount = (Double) args[2];
//        if (sourceAccount == destinationAccount) {
//            System.out.println("You can't transfer money to the same account");
//            return;
//        }
//        if (sourceAccount == null || destinationAccount == null) {
//            System.out.println("At least one of the accounts was not found");
//            return;
//        }
//        if (sourceAccount.getAccountType().equals(AccountType.SAVINGS)) {
//            if (amount > ((SavingsBankAccount) sourceAccount).getMinimumBalance()) {
//                System.out.println("You can't transfer more than the minimum balance of the account");
//            }
//        }
//        if (sourceAccount.getBalanceAmount() < amount) {
//            Transaction transaction = TransferTransaction.builder()
//                    .sourceAccount(sourceAccount)
//                    .destinationAccount(destinationAccount)
//                    .amount(amount)
//                    .transactionTime(java.time.LocalDateTime.now())
//                    .transactionStatus(TransactionStatus.FAILED)
//                    .build();
//            DB.transactions.add(transaction);
//            System.out.println("Insufficient funds");
//        } else {
//            sourceAccount.setBalanceAmount(sourceAccount.getBalanceAmount() - amount);
//            destinationAccount.setBalanceAmount(destinationAccount.getBalanceAmount() + amount);
//            DB.transactions.add(TransferTransaction.builder()
//                    .sourceAccount(sourceAccount)
//                    .destinationAccount(destinationAccount)
//                    .amount(amount)
//                    .transactionTime(java.time.LocalDateTime.now())
//                    .transactionStatus(TransactionStatus.COMPLETED)
//                    .build());
//            System.out.println("Transfer successful");
//        }
//    }

}
