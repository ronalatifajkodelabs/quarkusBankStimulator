package quarkus.bank.stimulator.transactions;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import quarkus.bank.stimulator.accounts.BankAccount;
import quarkus.bank.stimulator.enums.TransactionStatus;
import quarkus.bank.stimulator.enums.TransactionType;
import quarkus.bank.stimulator.inMemoryDBs.DB;

import java.time.LocalDateTime;

@Getter
@Setter
public class WithdrawTransaction extends Transaction {

    private final TransactionForOneBankAccount transactionForOneBankAccount;

    @Builder
    WithdrawTransaction(BankAccount sourceAccount, double amount, LocalDateTime transactionTime, TransactionStatus transactionStatus, TransactionForOneBankAccount transactionForOneBankAccount) {
        super(sourceAccount, amount, TransactionType.WITHDRAWAL, transactionTime, transactionStatus);
        this.transactionForOneBankAccount = transactionForOneBankAccount;
    }

    @Override
    public void performTransaction() {
        transactionForOneBankAccount.performTransaction(this.getSourceAccount(), this.getAmount());
    }

    public static void performTransaction(BankAccount bankAccount, double amount) {
        if (bankAccount.getBalanceAmount() < amount) {
            DB.transactions.add(
                    WithdrawTransaction.builder()
                            .sourceAccount(bankAccount)
                            .amount(amount)
                            .transactionTime(LocalDateTime.now())
                            .transactionStatus(TransactionStatus.FAILED)
                            .build());
            System.out.println("Insufficient funds");
        } else {
            bankAccount.setBalanceAmount(bankAccount.getBalanceAmount() - amount);
            DB.transactions.add(
                    WithdrawTransaction.builder()
                            .sourceAccount(bankAccount)
                            .amount(amount)
                            .transactionTime(LocalDateTime.now())
                            .transactionStatus(TransactionStatus.COMPLETED)
                            .build());
            System.out.println("Withdrawal successful");
        }
    }

    @Override
    public String toString() {
        return "Withdrawal Transaction from account of: " + this.getSourceAccount().getAccountHolder().getFirstName() + " " + this.getSourceAccount().getAccountHolder().getLastName() +
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
//        if (bankAccount.getBalanceAmount() < amount) {
//            DB.transactions.add(
//                    WithdrawTransaction.builder()
//                            .sourceAccount(bankAccount)
//                            .amount(amount)
//                            .transactionTime(java.time.LocalDateTime.now())
//                            .transactionStatus(TransactionStatus.FAILED)
//                            .build());
//            System.out.println("Insufficient funds");
//        } else {
//            bankAccount.setBalanceAmount(bankAccount.getBalanceAmount() - amount);
//            DB.transactions.add(
//                    WithdrawTransaction.builder()
//                            .sourceAccount(bankAccount)
//                            .amount(amount)
//                            .transactionTime(java.time.LocalDateTime.now())
//                            .transactionStatus(TransactionStatus.COMPLETED)
//                            .build());
//            System.out.println("Withdrawal successful");
//        }
//    }

//        if (bankAccount.getBalanceAmount() < amount) {
//            DB.transactions.add(
//                    WithdrawTransaction.builder()
//                            .sourceAccount(bankAccount)
//                            .amount(amount)
//                            .transactionTime(java.time.LocalDateTime.now())
//                            .transactionStatus(TransactionStatus.FAILED)
//                            .build());
//            System.out.println("Insufficient funds");
//        } else {
//            bankAccount.setBalanceAmount(bankAccount.getBalanceAmount() - amount);
//            DB.transactions.add(
//                    WithdrawTransaction.builder()
//                            .sourceAccount(bankAccount)
//                            .amount(amount)
//                            .transactionTime(java.time.LocalDateTime.now())
//                            .transactionStatus(TransactionStatus.COMPLETED)
//                            .build());
//            System.out.println("Withdrawal successful");
//        }
//    }
}
