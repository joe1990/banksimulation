public class InterestRate
{
   private DateTime validFrom;
   private Decimal interestRatePC;
   
   public InterestRate(DateTime dateValidFrom, Decimal anInterestRatePC)
   {
      validFrom = dateValidFrom;
      interestRatePC = anInterestRatePC;
   }
   
   public Decimal getInterestRate() {return interestRatePC;}
   public DateTime getValidFrom() {return validFrom;}
}
