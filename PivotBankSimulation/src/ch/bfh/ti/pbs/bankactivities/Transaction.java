package ch.bfh.ti.pbs.bankactivities;
import java.io.Serializable;

import ch.bfh.ti.pbs.helpers.DateTime;
import ch.bfh.ti.pbs.helpers.Decimal;


public class Transaction implements Serializable, Comparable<Object>
{
    private static final long serialVersionUID = -5908586149933473334L;
    private DateTime timestamp;
    private Decimal amount;
    private Decimal balance;
    private String remark;
   
    public Transaction(DateTime aTimeStamp, Decimal anAmount, Decimal aBalance, String aRemark)
    {
        timestamp = (DateTime)aTimeStamp.clone();
        amount = anAmount;
        balance = aBalance;
        remark = aRemark;
    }
   
    public DateTime getTimestamp() 
    {
        return timestamp;
    }
    public Decimal getAmount() 
    {
        return amount;
    }
    public Decimal getBalance() 
    {
        return balance;
    }
    public String getRemark() {
        return remark;
    }

    public String toString()
    {
        return timestamp.toString() + ", " + amount.toString(8,2) + ", " + balance.toString(9,2) + ", " + remark;
    }
   
    @Override   
    public int compareTo(Object o)
    {
        Transaction t = (Transaction)o;
        return timestamp.compareTo(t.getTimestamp());
    }
}
