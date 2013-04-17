package ch.bfh.ti.pbs.bankactivities;
import java.io.Serializable;

import ch.bfh.ti.pbs.helpers.DateTime;
import ch.bfh.ti.pbs.helpers.Decimal;

public class InterestRate implements Serializable
{
    private static final long serialVersionUID = -1604414275984432309L;
    private DateTime validFrom;
    private Decimal interestRatePC;
   
    public InterestRate(DateTime dateValidFrom, Decimal anInterestRatePC)
    {
        validFrom = dateValidFrom;
        interestRatePC = anInterestRatePC;
    }
   
    public Decimal getInterestRate() 
    {
        return interestRatePC;
    }
    
    public DateTime getValidFrom() 
    {
        return validFrom;
    }
}
