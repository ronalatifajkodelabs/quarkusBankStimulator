package quarkus.bank.stimulator.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import quarkus.bank.stimulator.DTOs.BankAccountRegistrationDTO;
import quarkus.bank.stimulator.DTOs.FilterParameters;
import quarkus.bank.stimulator.accounts.BankAccount;
import quarkus.bank.stimulator.accounts.CheckingBankAccount;
import quarkus.bank.stimulator.accounts.SavingsBankAccount;
import quarkus.bank.stimulator.enums.AccountType;
import quarkus.bank.stimulator.inMemoryDBs.DB;
import quarkus.bank.stimulator.transactions.Transaction;
import quarkus.bank.stimulator.users.BankAccountHolder;
import quarkus.bank.stimulator.utils.CustomResponse;

import java.util.*;

import static quarkus.bank.stimulator.utils.Util.validate;

@ApplicationScoped
public class BankAccountService {
    public CustomResponse getBankAccounts() {
        return CustomResponse.builder()
                .data(DB.bankAccounts)
                .status(Response.Status.OK)
                .message("Bank accounts fetched successfully")
                .build();
    }

    public CustomResponse addBankAccount(BankAccountRegistrationDTO bankAccountRegistrationDTO) {
        UUID uuid = UUID.randomUUID();
        if (bankAccountRegistrationDTO.getAccountType().equals(AccountType.CHECKING)) {
            CheckingBankAccount checkingBankAccount = CheckingBankAccount.builder()
                    .accountHolder(BankAccountHolder.builder()
                            .firstName(bankAccountRegistrationDTO.getFirstName())
                            .lastName(bankAccountRegistrationDTO.getLastName())
                            .email(bankAccountRegistrationDTO.getEmail())
                            .phoneNumber(bankAccountRegistrationDTO.getPhoneNumber())
                            .build())
                    .accountNumber(uuid.toString())
                    .build();
            DB.bankAccounts.add(checkingBankAccount);
            return CustomResponse.builder()
                    .data(checkingBankAccount)
                    .status(Response.Status.CREATED)
                    .message("Checking bank account added successfully")
                    .build();
        } else if (bankAccountRegistrationDTO.getAccountType().equals(AccountType.SAVINGS)) {
            SavingsBankAccount savingsBankAccount = SavingsBankAccount.builder()
                    .accountHolder(BankAccountHolder.builder()
                            .firstName(bankAccountRegistrationDTO.getFirstName())
                            .lastName(bankAccountRegistrationDTO.getLastName())
                            .email(bankAccountRegistrationDTO.getEmail())
                            .phoneNumber(bankAccountRegistrationDTO.getPhoneNumber())
                            .build())
                    .accountNumber(uuid.toString())
                    .minimumBalance(bankAccountRegistrationDTO.getMinimumBalance())
                    .build();
            DB.bankAccounts.add(savingsBankAccount);
            return CustomResponse.builder()
                    .data(savingsBankAccount)
                    .status(Response.Status.CREATED)
                    .message("Savings bank account added successfully")
                    .build();
        } else {
            return CustomResponse.builder()
                    .data(null)
                    .status(Response.Status.BAD_REQUEST)
                    .message("Invalid account type")
                    .build();
        }
    }

//    public CustomResponse addBankAccount(BankAccount bankAccount) {
//        if (BankAccount.getBankAccount(bankAccount.getAccountNumber()) != null) {
//            return CustomResponse.builder()
//                    .data(null)
//                    .status(Response.Status.CONFLICT)
//                    .message("Bank account already exists")
//                    .build();
//        }
//        DB.bankAccounts.add(bankAccount);
//        return CustomResponse.builder()
//                .data(bankAccount)
//                .status(Response.Status.CREATED)
//                .message("Bank account added successfully")
//                .build();
//    }

    public CustomResponse getBankAccountByAccountNumber(String accountNumber) {
        BankAccount bankAccount = BankAccount.getBankAccount(accountNumber);
        if (bankAccount == null) {
            return CustomResponse.builder()
                    .data(null)
                    .status(Response.Status.NOT_FOUND)
                    .message("Bank account not found")
                    .build();
        }
        return CustomResponse.builder()
                .data(bankAccount)
                .status(Response.Status.OK)
                .message("Bank account fetched successfully")
                .build();
    }

    public CustomResponse deleteBankAccountByAccountNumber(String accountNumber) {
        BankAccount bankAccount = BankAccount.getBankAccount(accountNumber);
        if (bankAccount == null) {
            return CustomResponse.builder()
                    .data(null)
                    .status(Response.Status.NOT_FOUND)
                    .message("Bank account not found")
                    .build();
        }
        DB.bankAccounts.remove(bankAccount);
        return CustomResponse.builder()
                .data(null)
                .status(Response.Status.OK)
                .message("Bank account deleted successfully")
                .build();
    }

    public CustomResponse updateSavingsBankAccount(String accountNumber, SavingsBankAccount bankAccountCloud) {
        SavingsBankAccount bankAccount = (SavingsBankAccount) BankAccount.getBankAccount(accountNumber);
        if (bankAccount == null) {
            return CustomResponse.builder()
                    .data(null)
                    .status(Response.Status.NOT_FOUND)
                    .message("Bank account not found")
                    .build();
        }
        bankAccount.setAccountHolder(bankAccountCloud.getAccountHolder() == null ? bankAccount.getAccountHolder() : bankAccountCloud.getAccountHolder());
        bankAccount.setAccountNumber(bankAccountCloud.getAccountNumber() == null ? bankAccount.getAccountNumber() : bankAccountCloud.getAccountNumber());
        bankAccount.setBalanceAmount(bankAccountCloud.getBalanceAmount() == 0.0 ? bankAccount.getBalanceAmount() : bankAccountCloud.getBalanceAmount());
        bankAccount.setPausedMonthlyBillingsUntil(bankAccountCloud.getPausedMonthlyBillingsUntil() == null ? bankAccount.getPausedMonthlyBillingsUntil() : bankAccountCloud.getPausedMonthlyBillingsUntil());
        bankAccount.setMinimumBalance(bankAccountCloud.getMinimumBalance() == 0.0 ? bankAccount.getMinimumBalance() : bankAccountCloud.getMinimumBalance());

        return CustomResponse.builder()
                .data(bankAccount)
                .status(Response.Status.OK)
                .message("Bank account updated successfully")
                .build();
    }

    public List<BankAccount> getTopLimitPayingAccountsByType(AccountType accountType, int limit) {
        Map<BankAccount, Double> bankAccountToTotalAmountBilled = new HashMap<>();

        for (BankAccount bankAccount : DB.bankAccounts) {
            if (bankAccount.getAccountType().equals(accountType)) {
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
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
    }

    public CustomResponse getBankAccounts(FilterParameters filterParameters) {
        FilterParameters validatedFilterParameters = validate(filterParameters);
        String orderBy = validatedFilterParameters.orderBy;
        String orderType = validatedFilterParameters.orderType;
        int limit = Integer.parseInt(validatedFilterParameters.limit);
        AccountType accountType = AccountType.valueOf(validatedFilterParameters.accountType);
        if (orderBy.equals("income")) {
            return CustomResponse.builder()
                    .data(getTopLimitPayingAccountsByType(accountType, limit))
                    .status(Response.Status.OK)
                    .message("Bank accounts fetched successfully")
                    .build();
        }
        List<BankAccount> listOfBankAccountsToBeReturned = DB.bankAccounts.stream().filter(bankAccount -> bankAccount.getAccountType().equals(accountType))
                .sorted((bankAccount1, bankAccount2) -> switch (orderBy) {
                    case "balanceAmount" ->
                            orderType.equals("ASC") ? Double.compare(bankAccount1.getBalanceAmount(), bankAccount2.getBalanceAmount()) : Double.compare(bankAccount2.getBalanceAmount(), bankAccount1.getBalanceAmount());
                    case "accountNumber" ->
                            orderType.equals("ASC") ? bankAccount1.getAccountNumber().compareTo(bankAccount2.getAccountNumber()) : bankAccount2.getAccountNumber().compareTo(bankAccount1.getAccountNumber());
                    default ->
                            orderType.equals("ASC") ? bankAccount1.getAccountHolder().getFirstName().compareTo(bankAccount2.getAccountHolder().getFirstName()) : bankAccount2.getAccountHolder().getFirstName().compareTo(bankAccount1.getAccountHolder().getFirstName());
                })
                .limit(limit)
                .toList();
        return CustomResponse.builder()
                .data(listOfBankAccountsToBeReturned)
                .status(Response.Status.OK)
                .message("Bank accounts fetched successfully")
                .build();
    }

}
