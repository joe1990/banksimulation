package ch.bfh.ti.pbs.bankaccounts;

import java.io.Serializable;
import java.util.ArrayList;

import ch.bfh.ti.pbs.customers.*;

public class Bank implements Serializable
{
    private static final long serialVersionUID = -4642741663547984958L;
    public ArrayList<Customer> Customers = new ArrayList <Customer>();
    
    public String toString()
    {
        String out = "";
        for( int i = 0; i < Customers.size();i++) {
            out += Customers.get(i).toString() + "\n";
        }
        return out;
    }
}
