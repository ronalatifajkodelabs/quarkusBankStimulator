package quarkus.bank.stimulator.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import quarkus.bank.stimulator.services.TransactionService;
import quarkus.bank.stimulator.utils.CustomResponse;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {

    @Inject
    TransactionService transactionService;

    @GET
    public CustomResponse getTransactions() {
        return transactionService.getTransactions();
    }

}
