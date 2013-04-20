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
