package ch.bfh.ti.pbs.customers;
import java.io.Serializable;
import java.util.ArrayList;

import ch.bfh.ti.pbs.bankaccounts.BankAccount;

public class Customer implements Serializable 
{
    private static final long serialVersionUID = -2930570703504077649L;
    
    private String firstname;
    private String lastname;
    private ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();

    public Customer(String firstname, String lastname, BankAccount firstAccount)
    {
        this.firstname = firstname;
        this.lastname = lastname;
        addBankAccount(firstAccount);
    }
   
    public void addBankAccount(BankAccount aBankAccount)
    {
        if (aBankAccount != null) {
            accounts.add(aBankAccount);
        }
    }
   
    public String toString()
    {
        return "Customer: [Name: " + this.firstname + " " + this.lastname + ", Accounts: " + accounts.toString();
    }
   
    public String getFirstname() 
    {
        return this.firstname;
    }
    
    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }
    
    public String getLastname()
    {
        return this.lastname;
    }
    
    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }
    
    public String getName()
    {
        return this.firstname + " " + this.lastname;
    }
   
    public BankAccount getAccount(int index) 
    {
        return accounts.get(index);
    }
    
    public ArrayList<BankAccount> getAccounts() 
    {
        return this.accounts;
    }
}
