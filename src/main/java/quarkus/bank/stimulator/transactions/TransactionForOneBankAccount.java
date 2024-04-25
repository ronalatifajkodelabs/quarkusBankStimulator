package quarkus.bank.stimulator.transactions;


import quarkus.bank.stimulator.accounts.BankAccount;
import quarkus.bank.stimulator.inMemoryDBs.DB;
import quarkus.bank.stimulator.enums.TransactionStatus;

public interface TransactionForOneBankAccount {
    default void performTransaction(BankAccount bankAccount, double amount) {
        bankAccount.setBalanceAmount(bankAccount.getBalanceAmount() + amount);
        DB.transactions.add(
                DepositTransaction.builder()
                        .sourceAccount(bankAccount)
                        .amount(amount)
                        .transactionTime(java.time.LocalDateTime.now())
                        .transactionStatus(TransactionStatus.COMPLETED)
                        .build());
        System.out.println("Successful");
    }
}
