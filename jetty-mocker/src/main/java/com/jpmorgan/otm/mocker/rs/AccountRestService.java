package com.jpmorgan.otm.mocker.rs;

import com.jpmorgan.otm.mocker.model.Account;
import com.jpmorgan.otm.mocker.services.AccountService;

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
