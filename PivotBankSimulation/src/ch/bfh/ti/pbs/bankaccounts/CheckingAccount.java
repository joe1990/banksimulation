package ch.bfh.ti.pbs.bankaccounts;

import java.io.Serializable;
import java.util.ArrayList;

import ch.bfh.ti.pbs.bankactivities.InterestRate;
import ch.bfh.ti.pbs.exceptions.UnderFlowException;
import ch.bfh.ti.pbs.helpers.DateTime;
import ch.bfh.ti.pbs.helpers.Decimal;

public class CheckingAccount extends BankAccount implements Serializable 
{  
    // Public static variables
    public static ArrayList<InterestRate> interestRates = new ArrayList<InterestRate>();
    public static final int FREE_TRANSACTIONS_PER_MONTH = 20;
    public static final Decimal TRANSACTION_FEE = new Decimal(0.2);
    public static final Decimal PENALTY_INTEREST_RATE = new Decimal(-10.0);
    
    //Private static variables
    private static final long serialVersionUID = 7730641722569475270L;
    private static final Decimal MINIMUM_BALANCE = new Decimal(0.0);

    private int monthlyTransactionCounter = 0;
    private int currentTransactionMonth = 0;

    public CheckingAccount() {
    }



    @Override
    public void deposit(DateTime date, Decimal amount, String remark) 
    {  
        try {
            super.deposit(date, amount, remark);
            // Only deduct fees on user transactions
            if (remark.isEmpty()) {
                deductFees(date);
            }
        } catch (UnderFlowException e) {
            System.out.println(e.toString());
        }
    }
   
    @Override
    public void withdraw(DateTime date, Decimal amount, String remark) 
    {  
        try {
            super.withdraw(date, amount, remark);
            // Only deduct fees on user transactions
            if (remark.isEmpty()) {
                deductFees(date);
            }
        } catch (UnderFlowException e) {
            System.out.println(e.toString());
        }
   }
   
   public Decimal getInterestRateAtDate(DateTime date) 
   {
       for (int i = 0; i < interestRates.size(); i++) {
           if (i < interestRates.size() - 1) {
               if (interestRates.get(i).getValidFrom().after(date) && interestRates.get(i + 1).getValidFrom().before(date)) {
                   return interestRates.get(i).getInterestRate();
               }  
           } else {
               return interestRates.get(interestRates.size() - 1).getInterestRate();
           }
       }
       System.out.println("*** Error: No valid interest rate! ***");
       return new Decimal(0.0);
   }

   public static Decimal getInterestRate() 
   {
       if (interestRates.size() > 0) {
           return interestRates.get(interestRates.size() - 1).getInterestRate();
       }
       System.out.println("*** Error: No valid interest rate! ***");
       return new Decimal(0.0);
   }
   
   public Decimal getMinimumBalance() 
   {
       return MINIMUM_BALANCE;
   }

   /**
    * deductFees withdraws the transaction fee if the no. of transactions 
    * exceeds the allowed monthly number of transactions.
    * @param date the transaction date.
    */
   public void deductFees(DateTime date) 
   {
       if (currentTransactionMonth != date.getMonth())
       {  
           currentTransactionMonth = date.getMonth();
           monthlyTransactionCounter = 0;
       }
       
       monthlyTransactionCounter++;
       if (monthlyTransactionCounter > FREE_TRANSACTIONS_PER_MONTH) {
           try {
               super.withdraw(date, TRANSACTION_FEE, "Transaction fee");
           } catch (UnderFlowException e) {
               // TODO Auto-generated catch block
               System.out.println("Your Time Deposit Account balance is too low");
           }
       }
   }

   public String toString()
   {
      return super.toString() + ", ChechingAccount: TransactionCount: " + monthlyTransactionCounter;
   }
}
