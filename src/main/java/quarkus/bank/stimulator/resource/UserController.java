package quarkus.bank.stimulator.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestQuery;
import quarkus.bank.stimulator.services.UserService;
import quarkus.bank.stimulator.users.BankAccountHolder;
import quarkus.bank.stimulator.users.BankEmployee;
import quarkus.bank.stimulator.users.BankOwner;
import quarkus.bank.stimulator.utils.CustomResponse;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    UserService userService;

    @GET
    public CustomResponse getUsers() {
        return userService.getUsers();
    }

    @GET
    @Path("/{firstName}/{lastName}")
    public CustomResponse getUser(@PathParam("firstName") String firstName, @PathParam("lastName") String lastName) {
        return userService.getUserResponse(firstName, lastName);
    }

    @POST
    @Path("/bank-account-holder")
    public CustomResponse addBankAccountHolder(BankAccountHolder bankUser) {
        return userService.addBankAccountHolder(bankUser);
    }

    @POST
    @Path("/bank-employee")
    public CustomResponse addBankEmployee(BankEmployee bankUser) {
        return userService.addBankEmployee(bankUser);
    }

    @POST
    @Path("/bank-owner")
    public CustomResponse addBankOwner(BankOwner bankUser) {
        return userService.addBankOwner(bankUser);
    }

    @DELETE
    @Path("/{firstName}/{lastName}")
    public CustomResponse deleteUser(@PathParam("firstName") String firstName, @PathParam("lastName") String lastName) {
        return userService.deleteUser(firstName, lastName);
    }

    @PUT
    public CustomResponse updateUser(@RestQuery String firstName, @RestQuery String lastName, BankAccountHolder bankUserCloud) {
        return userService.updateUser(firstName, lastName, bankUserCloud);
    }

    //    @GET
    //    public BankUser getUser(UserIdentificationDTO userIdentificationDTO) {
    //        return userService.getUser(userIdentificationDTO.getFirstName(), userIdentificationDTO.getFirstName());
    //    }

}
