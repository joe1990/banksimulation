package ch.bfh.ti.pbs.bankaccounts;
import ch.bfh.ti.pbs.helpers.DateTime;
import ch.bfh.ti.pbs.helpers.Decimal;

public class TimeDepositAccount extends SavingsAccount
{
   // Private static variables
   private static final int ALLOWED_WITHDRAWS_PER_MONTH = 1;
   private static final Decimal MAX_TRANSACTION_AMOUNT = new Decimal(1000.0);
   private static final Decimal PENALTY_INTEREST_RATE = new Decimal(4.0);

   // Private instance variables
   private int monthlyTransactionCounter = 0;
   private int currentTransactionMonth = 0;
   
   public TimeDepositAccount()
   {
   }  
   
   @Override
   public void deposit(DateTime date, Decimal amount, String remark) 
   {  super.deposit(date, amount, remark);
   }
   
   @Override
   public void withdraw(DateTime date, Decimal amount, String remark) 
   {  
      super.withdraw(date, amount, remark); 
      deductFees(date, amount);
   }
   
   public Decimal getInterestRateAtDate(DateTime date)
   {  
      return super.getInterestRateAtDate(date).add(new Decimal(1.0));
   }

   public Decimal getMinAllowedBalance() {return new Decimal(-10000.0);}
   
   public static Decimal getInterestRate()
   {
      return SavingsAccount.getInterestRate().add(new Decimal(1.0));
   }
   
   public void deductFees(DateTime date, Decimal amount)
   {    
      if (currentTransactionMonth != date.getMonth())
      {  currentTransactionMonth = date.getMonth();
         monthlyTransactionCounter = 0;
      }
      monthlyTransactionCounter++;
      
      // Withdraw additional penalty
      if (monthlyTransactionCounter > ALLOWED_WITHDRAWS_PER_MONTH || amount.compareTo(MAX_TRANSACTION_AMOUNT) > 0)
      {  Decimal penalty = amount.multiply(PENALTY_INTEREST_RATE).multiply(new Decimal(0.01));
         super.withdraw(date, penalty, "Penalty interest");
      }
   }
   
   public String toString()
   {
      return super.toString() + ", TimeDepositAccount";
   }
}
