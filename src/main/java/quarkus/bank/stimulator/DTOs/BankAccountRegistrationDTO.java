package quarkus.bank.stimulator.DTOs;

import lombok.Data;
import quarkus.bank.stimulator.enums.AccountType;

import java.time.LocalDateTime;

@Data
public class BankAccountRegistrationDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private AccountType accountType;
    private LocalDateTime pausedMonthlyBillingsUntil;
    private double minimumBalance;
}
