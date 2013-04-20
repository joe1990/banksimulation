package ch.bfh.ti.pbs.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ch.bfh.ti.pbs.bankaccounts.Bank;
import ch.bfh.ti.pbs.bankaccounts.BankAccount;
import ch.bfh.ti.pbs.bankaccounts.CheckingAccount;
import ch.bfh.ti.pbs.bankaccounts.SavingsAccount;
import ch.bfh.ti.pbs.bankaccounts.TimeDepositAccount;
import ch.bfh.ti.pbs.bankactivities.InterestRate;
import ch.bfh.ti.pbs.customers.Customer;
import ch.bfh.ti.pbs.exceptions.UnderFlowException;


public class BankReaderWriter
{
    private static BankReaderWriter myInstance = new BankReaderWriter();
    private File workFile;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Bank bank;
    
    public static BankReaderWriter getInstance()
    {
        return myInstance;
    }
    
    public Bank getBank()
    {
        return this.bank;
    }
    
    public void setWorkFile(File workFile)
    {
        this.workFile = workFile;
    }
    
    public Bank readFile() throws FileNotFoundException, IOException, ClassNotFoundException {
        
        if (this.workFile.exists()) {
            inputStream = new ObjectInputStream(new FileInputStream(this.workFile));
            bank = (Bank) inputStream.readObject();
            inputStream.close();
        } else {
            createFirstBank();
        }
        return bank;
    }
    
    public void writeFile() throws FileNotFoundException, IOException {
        outputStream = new ObjectOutputStream(new FileOutputStream(this.workFile));
        outputStream.writeObject(bank);
        outputStream.close();
    }
    
    private void createFirstBank() {
        
        CheckingAccount.interestRates.add(new InterestRate(new DateTime(2012,1,1), new Decimal(0)));
        SavingsAccount.interestRates.add(new InterestRate(new DateTime(2012,1,1), new Decimal(12).divide(new Decimal(8))));
        SavingsAccount.interestRates.add(new InterestRate(new DateTime(2013,3,1), new Decimal(13).divide(new Decimal(8))));
        SavingsAccount.interestRates.add(new InterestRate(new DateTime(2013,9,1), new Decimal(14).divide(new Decimal(8))));
                          

        // Create customers with accounts
        Customer A = new Customer("Firstname A", "Lastname A", new CheckingAccount());
        Customer B = new Customer("Firstname B", "Lastname B", new SavingsAccount());
        Customer C = new Customer("Firstname C", "Lastname C", new TimeDepositAccount());

        bank = new Bank();
        bank.Customers.add(A);
        bank.Customers.add(B);
        bank.Customers.add(C);

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

        try
        {
            this.writeFile();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private static void addOneTransactionPerDay(BankAccount account, DateTime startDate, DateTime endDate, Decimal amount) {
        DateTime date = (DateTime) startDate.clone();

        if (amount.compareTo(new Decimal(0.0)) < 0) {
            while (date.before(endDate)) {
                try {
                    account.withdraw(date, amount.negate(), "");
                } catch (UnderFlowException e) {
                    System.out.println(e.toString());
                }
                date.addDayOfYear(1);
            }
        } else {
            while (date.before(endDate)) {
                try {
                    account.deposit(date, amount, "");
                } catch (UnderFlowException e) {
                    System.out.println(e.toString());
                }
                date.addDayOfYear(1);
            }
        }
    }
}
