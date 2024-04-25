package quarkus.bank.stimulator.DTOs;

import jakarta.ws.rs.DefaultValue;
import org.jboss.resteasy.reactive.RestQuery;

public class FilterParameters {
    @RestQuery("orderBy")
    @DefaultValue("accountHolder")
    public String orderBy;
    @RestQuery("orderType")
    @DefaultValue("ASC")
    public String orderType;
    @DefaultValue("10")
    @RestQuery("limit")
    public String limit;
    @DefaultValue("SAVINGS")
    @RestQuery("accountType")
    public String accountType;

}
