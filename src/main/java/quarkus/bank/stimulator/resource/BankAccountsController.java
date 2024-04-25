package quarkus.bank.stimulator.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import quarkus.bank.stimulator.DTOs.BankAccountRegistrationDTO;
import quarkus.bank.stimulator.DTOs.FilterParameters;
import quarkus.bank.stimulator.accounts.SavingsBankAccount;
import quarkus.bank.stimulator.services.BankAccountService;
import quarkus.bank.stimulator.utils.CustomResponse;

@Path("/bank-accounts")
@Produces(MediaType.APPLICATION_JSON)
public class BankAccountsController {

    @Inject
    BankAccountService bankAccountsService;

    @GET
    public CustomResponse getBankAccounts() {
        return bankAccountsService.getBankAccounts();
    }

    @GET
    @Path("/{accountNumber}")
    public CustomResponse getBankAccounts(@PathParam("accountNumber") String accountNumber) {
        return bankAccountsService.getBankAccountByAccountNumber(accountNumber);
    }

    @POST
    public CustomResponse addBankAccount(BankAccountRegistrationDTO bankAccountRegistrationDTO) {
        return bankAccountsService.addBankAccount(bankAccountRegistrationDTO);
    }
    //    @POST
    //    public CustomResponse addBankAccount(SavingsBankAccount savingsBankAccount) {
    //        return bankAccountsService.addBankAccount(savingsBankAccount);
    //    }

    @DELETE
    @Path("/{accountNumber}")
    public CustomResponse deleteBankAccount(@PathParam("accountNumber") String accountNumber) {
        return bankAccountsService.deleteBankAccountByAccountNumber(accountNumber);
    }

    @PUT
    @Path("/{accountNumber}")
    public CustomResponse updateSavingsBankAccount(@PathParam("accountNumber") String accountNumber, SavingsBankAccount bankAccountCloud) {
        return bankAccountsService.updateSavingsBankAccount(accountNumber, bankAccountCloud);
    }

    //    GET /bank-accounts?orderBy=...&orderType=...&limit=...&accountType=...
    @GET
    @Path("/filters")
    public CustomResponse getBankAccounts(@BeanParam FilterParameters filterParameters) {
        return bankAccountsService.getBankAccounts(filterParameters);
    }

}
