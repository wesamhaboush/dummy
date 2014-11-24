package com.codebreeze.rest.server.rs;

import com.codebreeze.rest.server.model.Account;
import com.codebreeze.rest.server.services.AccountService;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.math.BigDecimal;

@Path( "/account" )
public class AccountRestService {
    @Inject
    private AccountService accountService;

    @Produces( { "text/plain" } )
    @GET
    @Path("/number/{number}")
    public Account getAccount(@PathParam("number") final String number) {
        return accountService.getAccount(number);
    }

    @Produces( {"text/plain"} )
    @POST
    public Account addAccount(@FormParam("number") final String number,
                              @FormParam("name") final String name,
                              @FormParam("balance") final String balance) {
        final Account account = new Account(number, name, new BigDecimal(balance));
        accountService.addAccount(account);
        return account;
    }
}