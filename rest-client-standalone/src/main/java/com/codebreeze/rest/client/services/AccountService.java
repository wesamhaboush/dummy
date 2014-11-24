package com.codebreeze.rest.client.services;

import com.google.common.collect.Maps;
import com.codebreeze.rest.client.model.Account;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.map.LazyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.springframework.xml.xpath.NodeMapper;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;


@Service
public class AccountService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Jaxp13XPathTemplate xpathTemplate;

    public Account getAccount(final String number) {
        String result = restTemplate.getForObject("http://example.com/hotels/{hotel}/bookings/{booking}", String.class, "42", "21");

        return accounts.get(number);
    }

    public void addAccount(Account account) {
        Map<String, String> vars = new HashMap<String, String>();
        vars.put("hotel", "42");
        vars.put("booking", "21");
        String result = restTemplate.getForObject("http://example.com/hotels/{hotel}/bookings/{booking}", String.class, vars);

        List<BufferedImage> imageList = xpathTemplate.evaluate("//photo", photos, new NodeMapper() {
            public Object mapNode(Node node, int i) throws DOMException {
                Element photo = (Element) node;

                Map<String, String> variables = new HashMap<String, String>(3);
                variables.put("server", photo.getAttribute("server"));
                variables.put("id", photo.getAttribute("id"));
                variables.put("secret", photo.getAttribute("secret"));

                String photoUrl = "http://static.flickr.com/{server}/{id}_{secret}_m.jpg";
                return restTemplate.getForObject(photoUrl, BufferedImage.class, variables);
            }
        });

        accounts.put(account.getNumber(), account);
    }
}
