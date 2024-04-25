package quarkus.bank.stimulator.transactions;

import lombok.*;
import quarkus.bank.stimulator.accounts.BankAccount;
import quarkus.bank.stimulator.enums.TransactionStatus;
import quarkus.bank.stimulator.enums.TransactionType;
import quarkus.bank.stimulator.inMemoryDBs.DB;

import java.time.LocalDateTime;

@Getter
@Setter
public class DepositTransaction extends Transaction {

    private final TransactionForOneBankAccount transactionForOneBankAccount;

    @Builder
    DepositTransaction(BankAccount sourceAccount, double amount, LocalDateTime transactionTime, TransactionStatus transactionStatus, TransactionForOneBankAccount transactionForOneBankAccount) {
        super(sourceAccount, amount, TransactionType.DEPOSIT, transactionTime, transactionStatus);
        this.transactionForOneBankAccount = transactionForOneBankAccount;
    }

    @Override
    public void performTransaction() {
        transactionForOneBankAccount.performTransaction(this.getSourceAccount(), this.getAmount());
    }

    public static void performTransaction(BankAccount bankAccount, double amount) {
        bankAccount.setBalanceAmount(bankAccount.getBalanceAmount() + amount);
        DB.transactions.add(
                DepositTransaction.builder()
                        .sourceAccount(bankAccount)
                        .amount(amount)
                        .transactionTime(LocalDateTime.now())
                        .transactionStatus(TransactionStatus.COMPLETED)
                        .build());
        System.out.println("Deposit successful");
    }

    @Override
    public String toString() {
        return "Deposit to account of: " + this.getSourceAccount().getAccountHolder().getFirstName() + " " + this.getSourceAccount().getAccountHolder().getLastName() +
                "\n     Transaction Type: " + this.getTransactionType() +
                "\n     Amount: " + this.getAmount() +
                "\n     Transaction Time: " + this.getTransactionTime() +
                "\n     Transaction Status: " + this.getTransactionStatus();
    }

//    @Override
//    public void performTransaction(Object... args) {
//        if (args.length != 2 || !(args[0] instanceof BankAccount) || !(args[1] instanceof Double)) {
//            throw new IllegalArgumentException("Invalid arguments for DepositTransaction");
//        }
//        BankAccount bankAccount = (BankAccount) args[0];
//        double amount = (Double) args[1];
//        bankAccount.setBalanceAmount(bankAccount.getBalanceAmount() + amount);
//        inMemoryDBs.DB.transactions.add(
//                DepositTransaction.builder()
//                        .sourceAccount(bankAccount)
//                        .amount(amount)
//                        .transactionTime(java.time.LocalDateTime.now())
//                        .transactionStatus(TransactionStatus.COMPLETED)
//                        .build());
//        System.out.println("Deposit successful");
//    }


}
