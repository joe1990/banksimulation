package ch.bfh.ti.pbs.helpers;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * The Decimal class wraps the BigDecimal number class and provides an additional toString
 * method for proper number display. For comparison purpose it holds in parallel a double
 * number.
 */
public class Decimal implements Serializable, Comparable<Decimal>
{
    private static final long serialVersionUID = -7840170276980982033L;
    public double numDouble;
    public BigDecimal numBigDec;
    public RoundingMode roundingMode = RoundingMode.HALF_EVEN;
    public int divisionScale = 100;
   
    public Decimal()
    {
        numDouble = 0.0;
        numBigDec = new BigDecimal(0.0);
    }
    public Decimal(double aDouble)
    {
        numDouble = aDouble;
        numBigDec = new BigDecimal(aDouble);
    }
   
    public Decimal add(Decimal summand)
    {  
        Decimal result = new Decimal();
        result.numDouble = numDouble + summand.numDouble;
        result.numBigDec = numBigDec.add(summand.numBigDec);
        return result;
    }
    public Decimal subtract(Decimal subtractand)
    {
        Decimal result = new Decimal();
        result.numDouble = numDouble - subtractand.numDouble;
        result.numBigDec = numBigDec.subtract(subtractand.numBigDec);
        return result;
    }
    public Decimal multiply(Decimal multiplicand)
    {
        Decimal result = new Decimal();
        result.numDouble = numDouble * multiplicand.numDouble;
        result.numBigDec = numBigDec.multiply(multiplicand.numBigDec);
        return result;
    }
    public Decimal divide(Decimal divisor)
    {
        Decimal result = new Decimal();
        result.numDouble = numDouble / divisor.numDouble;
        result.numBigDec = numBigDec.divide(divisor.numBigDec, divisionScale, roundingMode);
        return result;
    }
    public Decimal negate()
    {
        Decimal result = new Decimal();
        result.numDouble = numDouble * -1.0;
        result.numBigDec = numBigDec.multiply(new BigDecimal(-1.0));
        return result;
    }
   
    public String toString()
    {
        DecimalFormat df = new DecimalFormat("#0.00");
        String formated = df.format(numBigDec);
        return formated;
    }   
    public String toString(int totalWidth, int numDigitsAfter)
    {  
        String str = numBigDec.setScale(numDigitsAfter, roundingMode).toString();
       
        if (str.length() < totalWidth){  
            String prepend = "";
            for (int i=0; i< totalWidth-str.length(); ++i) {
                prepend += " ";
            }
            str = prepend + str;
        }
        return str;
    }

    @Override
    public int compareTo(Decimal o)
    {
        return numBigDec.compareTo(o.numBigDec);
    }
   
    public boolean isNegative()
    {
        return numBigDec.signum() == -1;
    }
    public boolean isPositive()
    {
        return numBigDec.signum() == 1;
    }
   
    public boolean isZero()
    {
        return numBigDec.compareTo(new BigDecimal(0.0)) == 0;
    }
}