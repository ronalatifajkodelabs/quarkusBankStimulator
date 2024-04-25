package quarkus.bank.stimulator.transactions;


import quarkus.bank.stimulator.accounts.BankAccount;
import quarkus.bank.stimulator.accounts.SavingsBankAccount;
import quarkus.bank.stimulator.enums.AccountType;
import quarkus.bank.stimulator.enums.TransactionStatus;
import quarkus.bank.stimulator.inMemoryDBs.DB;

public interface TransactionForTwoBankAccounts {
    default void performTransaction(BankAccount sourceAccount, BankAccount targetAccount, double amount) {
        if (sourceAccount == targetAccount) {
            System.out.println("You can't transfer money to the same account");
            return;
        }
        if (sourceAccount == null || targetAccount == null) {
            System.out.println("At least one of the accounts was not found");
            return;
        }
        if (sourceAccount.getAccountType().equals(AccountType.SAVINGS)) {
            if (amount > ((SavingsBankAccount) sourceAccount).getMinimumBalance()) {
                System.out.println("You can't transfer more than the minimum balance of the account");
            }
        }
        sourceAccount.setBalanceAmount(sourceAccount.getBalanceAmount() - amount);
        targetAccount.setBalanceAmount(targetAccount.getBalanceAmount() + amount);
        DB.transactions.add(
                TransferTransaction.builder()
                        .sourceAccount(sourceAccount)
                        .amount(amount)
                        .transactionTime(java.time.LocalDateTime.now())
                        .transactionStatus(TransactionStatus.COMPLETED)
                        .destinationAccount(targetAccount)
                        .build());
        System.out.println("Successful");
    }
}
