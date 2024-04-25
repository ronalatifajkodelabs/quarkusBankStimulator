package quarkus.bank.stimulator.DTOs;

import org.jboss.resteasy.reactive.RestQuery;

public class FilterParameters {
    @RestQuery("orderBy")
    public String orderBy;
    @RestQuery("orderType")
    public String orderType;
    @RestQuery("limit")
    public String limit;
    @RestQuery("accountType")
    public String accountType;

}
