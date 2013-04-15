package ch.bfh.ti.pbs.bankaccounts;

import java.util.ArrayList;

import ch.bfh.ti.pbs.bankactivities.InterestRate;
import ch.bfh.ti.pbs.helpers.DateTime;
import ch.bfh.ti.pbs.helpers.Decimal;

public class CheckingAccount extends BankAccount
{  
   // Public static variables
   public static ArrayList<InterestRate> interestRates = new ArrayList<InterestRate>();
   public static final int FREE_TRANSACTIONS_PER_MONTH = 20;
   public static final Decimal TRANSACTION_FEE = new Decimal(0.2);
   public static final Decimal PENALTY_INTEREST_RATE = new Decimal(-10.0);

   // Private instance variables
   private int monthlyTransactionCounter = 0;
   private int currentTransactionMonth = 0;

   public CheckingAccount()
   {
   }

   @Override
   public void deposit(DateTime date, Decimal amount, String remark) 
   {  
      super.deposit(date, amount, remark);
      
      // Only deduct fees on user transactions
      if (remark.isEmpty()) deductFees(date);
   }
   
   @Override
   public void withdraw(DateTime date, Decimal amount, String remark) 
   {  
      super.withdraw(date, amount, remark);
      
      // Only deduct fees on user transactions
      if (remark.isEmpty()) deductFees(date);
   }
   
   @Override
   public Decimal getInterestRateAtDate(DateTime date)
   {
      for (int i=0; i<interestRates.size(); i++)
      {  if (i<interestRates.size()-1)
         {  if (interestRates.get(i  ).getValidFrom().after(date) &&
                interestRates.get(i+1).getValidFrom().before(date))
            return interestRates.get(i).getInterestRate();
         } else return interestRates.get(interestRates.size()-1).getInterestRate();
      }
      System.out.println("*** Error: No valid interest rate! ***");
      return new Decimal(0.0);
   }

   public static Decimal getInterestRate()
   {
      if (interestRates.size() >0 )
         return interestRates.get(interestRates.size()-1).getInterestRate();
      System.out.println("*** Error: No valid interest rate! ***");
      return new Decimal(0.0);
   }

   /**
    * deductFees withdraws the transaction fee if the no. of transactions 
    * exceeds the allowed monthly number of transactions.
    * @param date the transaction date.
    */
   public void deductFees(DateTime date)
   {  
      if (currentTransactionMonth != date.getMonth())
      {  currentTransactionMonth = date.getMonth();
         monthlyTransactionCounter = 0;
      }
      monthlyTransactionCounter++;
      if (monthlyTransactionCounter > FREE_TRANSACTIONS_PER_MONTH)
         super.withdraw(date, TRANSACTION_FEE, "Transaction fee");
   }

   public String toString()
   {
      return super.toString() + ", ChechingAccount: TransactionCount: " + monthlyTransactionCounter;
   }
}
