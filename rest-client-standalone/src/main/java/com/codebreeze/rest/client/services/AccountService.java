package com.codebreeze.rest.client.services;

import com.google.common.collect.Maps;
import com.codebreeze.rest.client.model.Account;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.map.LazyMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.springframework.xml.xpath.NodeMapper;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.apache.commons.lang3.StringUtils.join;


@Service
public class AccountService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Jaxp13XPathTemplate xpathTemplate;

    private String host = "http://localhost:8080/";

    public String getAccount(final String number) {
        String result = restTemplate.getForObject(join(host, "api/account/number/{number}"), String.class, number);

        return result;
    }

    public void addAccount(final String name, final String number, final BigDecimal balance) {
        MultiValueMap<String, String> vars = new LinkedMultiValueMap<String, String>();
        vars.add("name", name);
        vars.add("number", number);
        vars.add("balance", balance.toString());
        String result = restTemplate.postForObject(join(host, "api/account"), vars, String.class);
    }
}
