package ch.bfh.ti.pbs.bankactivities;


import java.io.IOException;

import org.apache.pivot.wtk.DesktopApplicationContext;

import ch.bfh.ti.pbs.bankaccounts.BankAccount;
import ch.bfh.ti.pbs.bankaccounts.CheckingAccount;
import ch.bfh.ti.pbs.bankaccounts.SavingsAccount;
import ch.bfh.ti.pbs.bankaccounts.TimeDepositAccount;
import ch.bfh.ti.pbs.customers.Customer;
import ch.bfh.ti.pbs.exceptions.UnderFlowException;
import ch.bfh.ti.pbs.gui.BankSimulation;
import ch.bfh.ti.pbs.helpers.DateTime;
import ch.bfh.ti.pbs.helpers.Decimal;

public class Main
{     
   public static void main(String[] args) throws IOException, ClassNotFoundException
   {  
      // Add interest rates
       /*
      // Add interest rates
        CheckingAccount.interestRates.add(new InterestRate(new DateTime(2012, 1, 1), new Decimal(0)));
        SavingsAccount.interestRates.add(new InterestRate(new DateTime(2012, 1, 1), new Decimal(12).divide(new Decimal(8))));
        SavingsAccount.interestRates.add(new InterestRate(new DateTime(2013, 3, 1), new Decimal(13).divide(new Decimal(8))));
        SavingsAccount.interestRates.add(new InterestRate(new DateTime(2013, 9, 1), new Decimal(14).divide(new Decimal(8))));

        Bank firstBankOfJava;
        File f = new File("bank.dat");

        if (f.exists()) {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
            firstBankOfJava = (Bank) in.readObject();
            in.close();
        } else {
            firstBankOfJava = new Bank();

            // Create customers with accounts
            Customer A = new Customer("A", new CheckingAccount());
            Customer B = new Customer("B", new SavingsAccount());
            Customer C = new Customer("C", new TimeDepositAccount());

            firstBankOfJava.Customers.add(A);
            firstBankOfJava.Customers.add(B);
            firstBankOfJava.Customers.add(C);

            System.out.println("CheckingAccount    Interest Rate: " + CheckingAccount.getInterestRate().toString(6, 3));
            System.out.println("SavingsAccount     Interest Rate: " + SavingsAccount.getInterestRate().toString(6, 3));
            System.out.println("TimeDepositAccount Interest Rate: " + TimeDepositAccount.getInterestRate().toString(6, 3));
            System.out.println("");

            // Do transactions
            DateTime startDate1 = new DateTime(2013, 1, 1, 12, 00, 00);
            DateTime endDate1 = new DateTime(2013, 6, 30, 23, 59, 59);
            DateTime startDate2 = new DateTime(2013, 7, 1, 12, 00, 00);
            DateTime endDate2 = new DateTime(2013, 12, 31, 23, 59, 59);

            addOneTransactionPerDay(A.getAccount(0), startDate1, endDate1, new Decimal(100.0));
            addOneTransactionPerDay(A.getAccount(0), startDate2, endDate2, new Decimal(-100.0));
            addOneTransactionPerDay(B.getAccount(0), startDate1, endDate1, new Decimal(100.0));
            addOneTransactionPerDay(B.getAccount(0), startDate2, endDate2, new Decimal(-100.0));
            addOneTransactionPerDay(C.getAccount(0), startDate1, endDate1, new Decimal(100.0));
            addOneTransactionPerDay(C.getAccount(0), startDate2, endDate2, new Decimal(-100.0));

            A.getAccount(0).applyInterest(new DateTime(2013, 12, 31), false);
            B.getAccount(0).applyInterest(new DateTime(2013, 12, 31), false);
            C.getAccount(0).applyInterest(new DateTime(2013, 12, 31), false);
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
        } catch (UnderFlowExeption e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println(firstBankOfJava.toString());

      //for (Transaction t : A.getAccount(0).getTransactions()) System.out.println(t.toString());
      //for (Transaction t : B.getAccount(0).getTransactions()) System.out.println(t.toString());
      //for (Transaction t : C.getAccount(0).getTransactions()) System.out.println(t.toString());
       */
       
       DesktopApplicationContext.main(BankSimulation.class, args);
   }
   
   /**
    * Adds a transaction per day of a given amount between start date and end date to the given account.
    * @param account
    * @param startDate
    * @param endDate
    * @param amount
    */
   public static void addOneTransactionPerDay(BankAccount account, DateTime startDate, DateTime endDate, Decimal amount)
   {  
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
           while (date.before(endDate))
           {  
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
