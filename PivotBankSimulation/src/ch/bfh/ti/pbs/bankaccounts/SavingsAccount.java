package ch.bfh.ti.pbs.bankaccounts;

import java.io.Serializable;
import java.util.ArrayList;

import ch.bfh.ti.pbs.bankactivities.InterestRate;
import ch.bfh.ti.pbs.customers.Customer;
import ch.bfh.ti.pbs.helpers.DateTime;
import ch.bfh.ti.pbs.helpers.Decimal;

public class SavingsAccount extends BankAccount implements Serializable
{  
    private static final long serialVersionUID = 4071923363784057955L;
    private static final Decimal MINIMUM_BALANCE = new Decimal(-1000.0);
    public static ArrayList<InterestRate> interestRates = new ArrayList<InterestRate>();
   
    public SavingsAccount() 
    { 
    }
   
    public Decimal getInterestRateAtDate(DateTime date)
    {
        for (int i=0; i<interestRates.size(); i++) {  
            if (i<interestRates.size()-1) {
                boolean isAfterOrEqual = date.compareTo(interestRates.get(i  ).getValidFrom()) > 0 ||
                                     date.compareTo(interestRates.get(i  ).getValidFrom()) == 0;
                boolean isBeforeNext   = date.compareTo(interestRates.get(i+1).getValidFrom()) < 0;
                if (isAfterOrEqual && isBeforeNext) {
                    return interestRates.get(i).getInterestRate();
                }
         } else {
             return interestRates.get(interestRates.size()-1).getInterestRate();
         }
      }
      System.out.println("*** Error: No valid interest rate! ***");
      return new Decimal(0.0);
    }
   
    public static Decimal getInterestRate()
    {
        if (interestRates.size() >0 )  {
            return interestRates.get(interestRates.size()-1).getInterestRate();
        }
        System.out.println("*** Error: No valid interest rate! ***");
        return new Decimal(0.0);
    }

    public String toString()
    {
        return super.toString() + ", SavingsAccount";
    }
   
    @Override
    public Decimal getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
}
