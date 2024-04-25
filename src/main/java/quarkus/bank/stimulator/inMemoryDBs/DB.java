package quarkus.bank.stimulator.inMemoryDBs;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import quarkus.bank.stimulator.accounts.BankAccount;
import quarkus.bank.stimulator.adapters.PolymorphDeserializer;
import quarkus.bank.stimulator.transactions.Transaction;
import quarkus.bank.stimulator.users.BankAccountHolder;
import quarkus.bank.stimulator.users.BankUser;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@QuarkusMain
//public class Main {


public class DB {

    public static List<BankUser> bankUsers = new ArrayList<>();
    public static List<BankAccount> bankAccounts = new ArrayList<>();
    public static List<Transaction> transactions = new ArrayList<>();

    public static <T> List<T> fillData(String filePath, Class<T> clazz) {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer(
                            Arrays.asList(
                                    DateTimeFormatter.ISO_DATE_TIME,
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            )
                    ))
//                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializerISO())
//                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializerSQL())
//                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializerKS())
                    .registerTypeAdapter(BankAccount.class, new PolymorphDeserializer<BankAccount>())
                    .registerTypeAdapter(Transaction.class, new PolymorphDeserializer<Transaction>())
                    .create();
            FileReader reader = new FileReader(filePath);
            Type bankUsersType = TypeToken.getParameterized(ArrayList.class, clazz).getType();
            return gson.fromJson(reader, bankUsersType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class LocalDateTimeDeserializerISO implements JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String dateTimeString = json.getAsJsonPrimitive().getAsString();
            return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME);
        }
    }

    public static class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
        private final List<DateTimeFormatter> formatters;

        public LocalDateTimeDeserializer(List<DateTimeFormatter> formatters) {
            this.formatters = formatters;
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String dateTimeString = json.getAsJsonPrimitive().getAsString();
            for (DateTimeFormatter formatter : formatters) {
                try {
                    return LocalDateTime.parse(dateTimeString, formatter);
                } catch (DateTimeParseException e) {
                    // Try the next formatter
                }
            }
            throw new JsonParseException("Unable to parse LocalDateTime: " + dateTimeString);
        }
    }

    public static class LocalDateTimeDeserializerSQL implements JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String dateTimeString = json.getAsJsonPrimitive().getAsString();
            return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    public static class LocalDateTimeDeserializerKS implements JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String dateTimeString = json.getAsJsonPrimitive().getAsString();
            return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        }
    }

    public <T extends BankAccount> void fillBankAccounts(String filePath, Class<T> clazz) {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializerISO())
                    .create();
            FileReader reader = new FileReader(filePath);
            Type bankAccountListType = TypeToken.getParameterized(ArrayList.class, clazz).getType();
            List<T> bankAccountsRead = gson.fromJson(reader, bankAccountListType);
            reader.close();
            bankAccounts.addAll(bankAccountsRead);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T extends Transaction> void fillTransactions(String filePath, Class<T> clazz) {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializerISO())
                    .create();
            FileReader reader = new FileReader(filePath);
            Type transactionListType = TypeToken.getParameterized(ArrayList.class, clazz).getType();
            List<T> transactionsRead = gson.fromJson(reader, transactionListType);
            reader.close();
            transactions.addAll(transactionsRead);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BankAccount getBankAccountByAccountNumber(String accountNumber) {
        for (BankAccount bankAccount : bankAccounts) {
            if (bankAccount.getAccountNumber().equals(accountNumber)) {
                return bankAccount;
            }
        }
        return null;
    }

    public static BankAccount getBankAccountByAccountHolder(BankAccountHolder accountHolder) {
        for (BankAccount bankAccount : bankAccounts) {
            if (bankAccount.getAccountHolder().equals(accountHolder)) {
                return bankAccount;
            }
        }
        return null;
    }

    public static Object getUserByFullName(String name, String surname) {
        for (BankUser bankUser : bankUsers) {
            if (bankUser.getFirstName().equals(name) && bankUser.getLastName().equals(surname)) {
                return bankUser;
            }
        }
        return null;
    }
}

//    public static void main(String... args) {

//        List<BankUser> bankUsers = new ArrayList<>();
//        List<BankAccount> bankAccounts = new ArrayList<>();
//        List<Transaction> transactions = new ArrayList<>();
//        System.out.println("Running main method");
//        System.out.println("tash bohet run");
//        Quarkus.run(args);
//        System.out.println("after the run ");
//        List<BankAccount> bankAccountsFromJSON = DB.fillData("src/main/java/quarkus/bank/stimulator/jsonFiles/bankAccounts.json", BankAccount.class);
//        List<? extends BankUser> bankUsersFromJson = DB.fillData("src/main/java/quarkus/bank/stimulator/jsonFiles/bankAccountHolders.json", BankAccountHolder.class);
//        List<? extends BankUser> bankEmployeesFromJson = DB.fillData("src/main/java/quarkus/bank/stimulator/jsonFiles/bankEmployees.json", BankEmployee.class);
//        List<? extends BankUser> bankOwnersFromJson = DB.fillData("src/main/java/quarkus/bank/stimulator/jsonFiles/bankOwners.json", BankOwner.class);
//        List<BankUser> bankUsersFromJSON = new LinkedList<>();
//        bankUsersFromJSON.addAll(bankUsersFromJson);
//        bankUsersFromJSON.addAll(bankEmployeesFromJson);
//        bankUsersFromJSON.addAll(bankOwnersFromJson);
//        List<Transaction> transactionsFromJSON = DB.fillData("src/main/java/quarkus/bank/stimulator/jsonFiles/transactions.json", Transaction.class);
//
//        DB.bankAccounts.addAll(bankAccountsFromJSON);
//        DB.bankUsers.addAll(bankUsersFromJSON);
//        DB.transactions.addAll(transactionsFromJSON);
//        System.out.println(DB.bankAccounts);
//        System.out.println(DB.bankUsers);
//        System.out.println(DB.transactions);
//    }

//}