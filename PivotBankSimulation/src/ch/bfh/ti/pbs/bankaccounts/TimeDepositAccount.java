package ch.bfh.ti.pbs.bankaccounts;

import java.io.Serializable;
import ch.bfh.ti.pbs.exceptions.UnderFlowException;
import ch.bfh.ti.pbs.helpers.DateTime;
import ch.bfh.ti.pbs.helpers.Decimal;

public class TimeDepositAccount extends SavingsAccount implements Serializable
{

    private static final long serialVersionUID = -5215474940396999839L;
    private static final int ALLOWED_WITHDRAWS_PER_MONTH = 1;
    private static final Decimal MAX_TRANSACTION_AMOUNT = new Decimal(1000.0);
    private static final Decimal PENALTY_INTEREST_RATE = new Decimal(4.0);
    private static final Decimal MINIMUM_BALANCE = new Decimal(-10000.0);
    
    private int transactionCount;
   
    public Decimal getMinimumBalance(){
        return MINIMUM_BALANCE;
    }
    
    public TimeDepositAccount()
    {
        transactionCount = 0;
    }  
   
    public void deposit(DateTime date, Decimal amount, String remark) 
    {  
        try {
            super.deposit(date, amount, remark);
        } catch (UnderFlowException e) {
            System.out.println(e.toString());
        }
    }
   
    public void withdraw(DateTime date, Decimal amount, String remark) throws UnderFlowException 
    {  
        if (date.getDayOfMonth() == 1) {
            transactionCount = 0; 
        }
        
        transactionCount++; 
        super.withdraw(date, amount, remark);
        deductFees(date, amount);
    }
   
    public Decimal getInterestRateAtDate(DateTime date)
    {  
        return super.getInterestRateAtDate(date).add(new Decimal(1.0));
    }    
   
    public static Decimal getInterestRate()
    {
        return SavingsAccount.getInterestRate().add(new Decimal(1.0));
    }
   
    public void deductFees(DateTime date, Decimal amount)
    {    
        // Withdraw additional penalty
        if (transactionCount > ALLOWED_WITHDRAWS_PER_MONTH || amount.compareTo(MAX_TRANSACTION_AMOUNT) > 0) {  
            Decimal penalty = amount.multiply(PENALTY_INTEREST_RATE).multiply(new Decimal(0.01));
            try {
                super.withdraw(date, penalty, "Penalty interest");
            } catch (UnderFlowException e) {
                System.out.println(e.toString());
            }
        }
    }
   
   public String toString()
   {
      return super.toString() + ", TimeDepositAccount";
   }
}
