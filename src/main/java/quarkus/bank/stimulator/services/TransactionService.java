package quarkus.bank.stimulator.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import quarkus.bank.stimulator.inMemoryDBs.DB;
import quarkus.bank.stimulator.utils.CustomResponse;

@ApplicationScoped
public class TransactionService {

    public CustomResponse getTransactions() {
        return CustomResponse.builder()
                .data(DB.transactions)
                .status(Response.Status.OK)
                .message("Transactions fetched successfully")
                .build();
    }

}
