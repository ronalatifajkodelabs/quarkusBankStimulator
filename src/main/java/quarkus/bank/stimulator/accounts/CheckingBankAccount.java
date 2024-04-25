package quarkus.bank.stimulator.accounts;


import lombok.*;
import quarkus.bank.stimulator.enums.AccountType;
import quarkus.bank.stimulator.enums.TransactionStatus;
import quarkus.bank.stimulator.inMemoryDBs.DB;
import quarkus.bank.stimulator.transactions.BankBillingTransaction;
import quarkus.bank.stimulator.transactions.Transaction;
import quarkus.bank.stimulator.users.BankAccountHolder;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class CheckingBankAccount extends BankAccount {

    public static final double MONTHLY_FEE = 13.5;

    @Builder
    public CheckingBankAccount(String accountNumber, BankAccountHolder accountHolder) {
        super(accountNumber, accountHolder, AccountType.CHECKING);
    }

    @Override
    public void runMonthlyUpdate() {
        if (this.getPausedMonthlyBillingsUntil() != null && this.getPausedMonthlyBillingsUntil().isAfter(LocalDateTime.now())) {
            return;
        }
        if (this.getBalanceAmount() < CheckingBankAccount.MONTHLY_FEE) {
            System.out.println("Monthly fee of " + CheckingBankAccount.MONTHLY_FEE + " for checking account " + this.getAccountNumber() + "has not been deducted due to insufficient funds");
            return;
        }
        this.setBalanceAmount(this.getBalanceAmount() - (CheckingBankAccount.MONTHLY_FEE));
        Transaction bankServicesBillingTransaction = BankBillingTransaction.builder()
                .amount(CheckingBankAccount.MONTHLY_FEE)
                .sourceAccount(this)
                .transactionTime(LocalDateTime.now())
                .transactionStatus(TransactionStatus.COMPLETED)
                .build();
        DB.transactions.add(bankServicesBillingTransaction);
        System.out.println("Monthly fee of " + CheckingBankAccount.MONTHLY_FEE + " for checking account " + this.getAccountNumber() + "has been deducted");
    }

}
