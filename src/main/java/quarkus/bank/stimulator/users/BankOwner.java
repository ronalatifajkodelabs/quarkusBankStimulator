package quarkus.bank.stimulator.users;

import quarkus.bank.stimulator.accounts.BankAccount;
import quarkus.bank.stimulator.enums.AccountType;
import quarkus.bank.stimulator.enums.TransactionStatus;
import quarkus.bank.stimulator.enums.TransactionType;
import quarkus.bank.stimulator.inMemoryDBs.DB;
import lombok.Data;
import lombok.EqualsAndHashCode;
import quarkus.bank.stimulator.transactions.Transaction;

import java.time.LocalDateTime;
import java.util.*;

@Data
@EqualsAndHashCode(callSuper = true)
public class BankOwner extends BankUser {

    public BankOwner(String firstName, String lastName, String email, String phoneNumber) {
        super(firstName, lastName, email, phoneNumber);
    }

    /*TODO
    getTotalYearlyIncome,
    getIncomeFromCheckingAccounts,
    getIncomeFromSavingsAccounts,
    getAllAccounts
    getIncomeFromUser
    getTopFivePayingSavingsAccounts
    pauseMonthlyRates*/

    public double getTotalYearlyIncome() {
        return DB.transactions.stream()
                .filter(transaction -> transaction.getTransactionType().equals(TransactionType.BANK_BILLING) && transaction.getTransactionStatus().equals(TransactionStatus.COMPLETED))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getIncomeForYear(int year) {
        return DB.transactions.stream()
                .filter(transaction -> transaction.getTransactionType().equals(TransactionType.BANK_BILLING)&& transaction.getTransactionStatus().equals(TransactionStatus.COMPLETED))
                .filter(transaction -> transaction.getTransactionTime().getYear() == year)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    //TODO if this were generic how to change it?
    public double getIncomeFromAccountsByAccountType(AccountType accountType) {
        return DB.transactions.stream()
                .filter(transaction -> transaction.getSourceAccount().getAccountType() != null && transaction.getSourceAccount().getAccountType().equals(accountType)
                        && transaction.getTransactionType().equals(TransactionType.BANK_BILLING) && transaction.getTransactionStatus().equals(TransactionStatus.COMPLETED))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public <T extends BankAccount> double getIncomeFromAccountsByAccountTypeGeneric(Class<T> bankAccountClass) {
        return DB.transactions.stream()
                .filter(transaction -> transaction.getSourceAccount().getAccountType() != null && transaction.getSourceAccount().getClass().equals(bankAccountClass)
                        && transaction.getTransactionType().equals(TransactionType.BANK_BILLING) && transaction.getTransactionStatus().equals(TransactionStatus.COMPLETED))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public List<BankAccount> getAllAccounts() {
        return DB.bankAccounts;
    }

    public double getIncomeFromUser(BankUser bankUser) {
        return DB.transactions.stream()
                .filter(transaction -> transaction.getSourceAccount().getAccountHolder().equals(bankUser))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public List<BankAccount> getTopFivePayingSavingsAccounts() {
        Map<BankAccount, Double> bankAccountToTotalAmountBilled = new HashMap<>();

        for (BankAccount bankAccount : DB.bankAccounts) {
            if (bankAccount.getAccountType().equals(AccountType.SAVINGS)) {
                List<Transaction> transactions = DB.transactions.stream()
                        .filter(transaction -> transaction.getSourceAccount().equals(bankAccount))
                        .toList();
                double totalAmount = transactions.stream()
                        .mapToDouble(Transaction::getAmount)
                        .sum();
                bankAccountToTotalAmountBilled.put(bankAccount, totalAmount);
            }
        }
        List<Map.Entry<BankAccount, Double>> listToSortMap = new LinkedList<>(bankAccountToTotalAmountBilled.entrySet());
        listToSortMap.sort(Map.Entry.comparingByValue());
        Collections.reverse(listToSortMap);

        return listToSortMap.stream()
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();
    }

    public void pauseMonthlyRates(BankAccount bankAccount, LocalDateTime pausedUntil) {
        bankAccount.setPausedMonthlyBillingsUntil(pausedUntil);
    }

}
