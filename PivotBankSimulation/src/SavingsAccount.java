import java.util.ArrayList;

public class SavingsAccount extends BankAccount
{  
   public static ArrayList<InterestRate> interestRates = new ArrayList<InterestRate>();
   
   public SavingsAccount() 
   { 
      
   }
   
   @Override   
   public Decimal getInterestRateAtDate(DateTime date)
   {
      //System.out.println("DateAt: " + Main.calendarToString(date));
      
      for (int i=0; i<interestRates.size(); i++)
      {  if (i<interestRates.size()-1)
         {  //System.out.println("From(" +  i +    "): " + Main.calendarToString(interestRates.get(i).getValidFrom()));
            //System.out.println("From(" + (i+1) + "): " + Main.calendarToString(interestRates.get(i+1).getValidFrom()));
            boolean isAfterOrEqual = date.compareTo(interestRates.get(i  ).getValidFrom()) > 0 ||
                                     date.compareTo(interestRates.get(i  ).getValidFrom()) == 0;
            boolean isBeforeNext   = date.compareTo(interestRates.get(i+1).getValidFrom()) < 0;
            if (isAfterOrEqual && isBeforeNext)
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

   public String toString()
   {
      return super.toString() + ", SavingsAccount";
   }
}
