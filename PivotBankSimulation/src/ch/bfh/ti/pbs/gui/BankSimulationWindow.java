package ch.bfh.ti.pbs.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.FileBrowserSheet;
import org.apache.pivot.wtk.Frame;
import org.apache.pivot.wtk.TreeView;
import org.apache.pivot.wtk.content.TreeBranch;
import org.apache.pivot.wtk.content.TreeNode;

import ch.bfh.ti.pbs.customers.Customer;
import ch.bfh.ti.pbs.exceptions.UnderFlowException;
import ch.bfh.ti.pbs.helpers.DateTime;
import ch.bfh.ti.pbs.helpers.Decimal;
import ch.bfh.ti.pbs.bankaccounts.Bank;
import ch.bfh.ti.pbs.bankaccounts.BankAccount;
import ch.bfh.ti.pbs.bankaccounts.CheckingAccount;
import ch.bfh.ti.pbs.bankaccounts.SavingsAccount;
import ch.bfh.ti.pbs.bankaccounts.TimeDepositAccount;
import ch.bfh.ti.pbs.bankactivities.InterestRate;

public class BankSimulationWindow extends Frame implements Bindable 
{
    @BXML private FileBrowserSheet fbsOpenFile;
    @BXML private TreeView trvUsers;

    public BankSimulationWindow() 
    {
        Action.getNamedActions().put("actOpen", new Action() {
            @Override
            public void perform(Component source) {
                fbsOpenFile.open(BankSimulationWindow.this);
            }
        });
        
        Action.getNamedActions().put("actQuit", new Action() {
            @Override
            public void perform(Component source) {
                System.exit(0);
            }
        });
    }
    
    @Override
    public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2)
    {
        Bank firstBankOfJava;
        File f = new File("bank.dat");
        if (f.exists()) {
            ObjectInputStream in;
            try
            {
                in = new ObjectInputStream(new FileInputStream(f));
                firstBankOfJava = (Bank) in.readObject();
                in.close();
                
                fillTreeView(firstBankOfJava.Customers);
                
            } catch (IOException e)
            {
                e.printStackTrace();
            } catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            } 
        } else {
            createFirstBank();
        }
    }
    
    private void fillTreeView(ArrayList<Customer> customers) {
        TreeBranch rootBranch = new TreeBranch();
        
        for(Customer customer: customers)
        {
            TreeBranch customerBranch = new TreeBranch();
            customerBranch.setText(customer.getName());
            customerBranch.setIcon("/ch/bfh/ti/pbs/gui/user.png");
            
            for(BankAccount account: customer.getAccounts()) {
                TreeNode accountNode = new TreeNode();
                accountNode.setText(String.valueOf(account.getAccountNumber()));
                accountNode.setIcon("/ch/bfh/ti/pbs/gui/bankaccount.png");
                customerBranch.add(accountNode);
            }
            rootBranch.add(customerBranch);
        }
        trvUsers.setTreeData(rootBranch);
    }
    
    private void createFirstBank() {
     // Add interest rates
        CheckingAccount.interestRates.add(new InterestRate(new DateTime(2012,1,1), new Decimal(0)));
        SavingsAccount.interestRates.add(new InterestRate(new DateTime(2012,1,1), new Decimal(12).divide(new Decimal(8))));
        SavingsAccount.interestRates.add(new InterestRate(new DateTime(2013,3,1), new Decimal(13).divide(new Decimal(8))));
        SavingsAccount.interestRates.add(new InterestRate(new DateTime(2013,9,1), new Decimal(14).divide(new Decimal(8))));
                  
        Bank firstBankOfJava = null;
        File f = new File("bank.dat");

        if (f.exists()) {
            ObjectInputStream in;
            try
            {
                in = new ObjectInputStream(new FileInputStream(f));
                firstBankOfJava = (Bank) in.readObject();
                in.close();
            } catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        } else {
            firstBankOfJava = new Bank();

            // Create customers with accounts
            Customer A = new Customer("Firstname A", "Lastname A", new CheckingAccount());
            Customer B = new Customer("Firstname B", "Lastname B", new SavingsAccount());
            Customer C = new Customer("Firstname C", "Lastname C", new TimeDepositAccount());

            firstBankOfJava.Customers.add(A);
            firstBankOfJava.Customers.add(B);
            firstBankOfJava.Customers.add(C);

            // Do transactions
            DateTime startDate1 = new DateTime(2013, 1, 1, 12,00,00);
            DateTime endDate1   = new DateTime(2013, 6,30, 23,59,59);
            DateTime startDate2 = new DateTime(2013, 7, 1, 12,00,00);
            DateTime endDate2   = new DateTime(2013,12,31, 23,59,59);

            addOneTransactionPerDay(A.getAccount(0), startDate1, endDate1, new Decimal( 100.0));
            addOneTransactionPerDay(A.getAccount(0), startDate2, endDate2, new Decimal(-100.0));
            addOneTransactionPerDay(B.getAccount(0), startDate1, endDate1, new Decimal( 100.0));
            addOneTransactionPerDay(B.getAccount(0), startDate2, endDate2, new Decimal(-100.0));
            addOneTransactionPerDay(C.getAccount(0), startDate1, endDate1, new Decimal( 100.0));
            addOneTransactionPerDay(C.getAccount(0), startDate2, endDate2, new Decimal(-100.0));
              
            A.getAccount(0).applyInterest(new DateTime(2013, 12, 31), false);
            B.getAccount(0).applyInterest(new DateTime(2013, 12, 31), false);
            C.getAccount(0).applyInterest(new DateTime(2013, 12, 31), false);
        
            System.out.println(A.toString());
            System.out.println(B.toString());
            System.out.println(C.toString());
        }

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
            out.writeObject(firstBankOfJava);
            out.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Heute nicht");
        }

        try {
            firstBankOfJava.Customers.get(0).getAccount(0).deposit(new DateTime(), new Decimal(10000), "So cool");
        } catch (UnderFlowException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println(firstBankOfJava.toString());        
    }
    
    public static void addOneTransactionPerDay(BankAccount account, DateTime startDate, DateTime endDate, Decimal amount) {
        DateTime date = (DateTime) startDate.clone();

        if (amount.compareTo(new Decimal(0.0)) < 0) {
            while (date.before(endDate)) {
                try {
                    account.withdraw(date, amount.negate(), "");
                } catch (UnderFlowException e) {
                    // TODO Auto-generated catch block
                    System.out.println(e.toString());
                }
                date.addDayOfYear(1);
            }
        } else {
            while (date.before(endDate)) {
                try {
                    account.deposit(date, amount, "");
                } catch (UnderFlowException e) {
                    // TODO Auto-generated catch block
                    System.out.println(e.toString());
                }
                date.addDayOfYear(1);
            }
        }
    }
}
