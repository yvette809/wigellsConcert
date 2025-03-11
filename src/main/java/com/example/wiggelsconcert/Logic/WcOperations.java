package com.example.wiggelsconcert.Logic;

import com.example.wiggelsconcert.DAO.WcDAO;
import com.example.wiggelsconcert.Entities.Concert;
import com.example.wiggelsconcert.Entities.Customer;
import com.example.wiggelsconcert.Entities.WC;

import java.util.ArrayList;
import java.util.List;

public class WcOperations {

    public List<Customer> getCustomerByConcert(Concert concert) {
        WcDAO wcDAO = new WcDAO();
        List<WC> wclist = wcDAO.getAllWcRegistrations();
        List<Customer> customerList = new ArrayList<>();
        for (WC wc : wclist) {
            if (wc.getConcert().getConcert_id() == concert.getConcert_id()) {
                customerList.add(wc.getCustomer());
            }
        }
        return customerList;
    }


}
