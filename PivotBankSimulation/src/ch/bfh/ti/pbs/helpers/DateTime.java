package ch.bfh.ti.pbs.helpers;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The DateTime class has a GregorianCalendar object and hides some of its
 * complexity. In addition it provides constructors with real month numbers
 * and toString methods that can be used.
 */
public class DateTime implements Cloneable, Serializable, Comparable<DateTime>
{
    private static final long serialVersionUID = 159415477973731629L;
    private GregorianCalendar gc;
 
    public DateTime() 
    {  
        gc = new GregorianCalendar();
    }
    
    public DateTime(String date)
    {
        gc = new GregorianCalendar(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5, 7)) - 1, 
                Integer.parseInt(date.substring(8, 10)), Integer.parseInt(date.substring(11, 13)), Integer.parseInt(date.substring(14, 16)), Integer.parseInt(date.substring(17, 19)));
    }
    
    public DateTime(int year, int month, int day)
    {
        gc = new GregorianCalendar(year, month-1, day);
    }
   
    public DateTime(int year, int month, int day, int hour, int minute, int second)
    {
        gc = new GregorianCalendar(year, month-1, day, hour, minute, second);
    }
   
    public void setFirstMillisecond()
    {
        gc.set(Calendar.HOUR, 0);
        gc.set(Calendar.MINUTE, 0);
        gc.set(Calendar.SECOND, 0);
        gc.set(Calendar.MILLISECOND, 0);
    }
    public void setLastMillisecond()
    {
        gc.set(Calendar.HOUR, 23);
        gc.set(Calendar.MINUTE, 59);
        gc.set(Calendar.SECOND, 59);
        gc.set(Calendar.MILLISECOND, 999);
    }
   
    public void addDayOfYear(int days) 
    {
        gc.add(Calendar.DAY_OF_YEAR, days);
    }
   
    public int getYear() 
    {
        return gc.get(Calendar.YEAR);
    }
    public int getMonth() 
    {
        return gc.get(Calendar.MONTH) + 1;
    }
    public int getDay() 
    {
        return gc.get(Calendar.DAY_OF_MONTH);
    }
    public int getDayOfYear() 
    {
        return gc.get(Calendar.DAY_OF_YEAR);
    }
    public int getDayOfWeek() 
    {
        return gc.get(Calendar.DAY_OF_WEEK);
    }
    public int getDayOfMonth() 
    {
        return gc.get(Calendar.DAY_OF_MONTH);
    }
    public int getHour() 
    {
        return gc.get(Calendar.HOUR);
    }
    public int getHourOfDay() 
    {
        return gc.get(Calendar.HOUR_OF_DAY);
    }
    public int getMinute() 
    {
        return gc.get(Calendar.MINUTE);
    }
    public int getSecond() 
    {
        return gc.get(Calendar.SECOND);
    }
    public int getMillisecond() 
    {
        return gc.get(Calendar.MILLISECOND);
    }
    public GregorianCalendar getGregorianCalendar() 
    {
        return gc;
    }
  
    public String toString()
    {
        String str = String.format("%02d-%02d-%4d %02d:%02d:%02d:%03d", 
                                 getDayOfMonth(),
                                 getMonth(),
                                 getYear(),
                                 getHourOfDay(),
                                 getMinute(),
                                 getSecond(),
                                 getMillisecond());
        return str;
    }
    public String toStringDate()
    {
        String str = String.format("%02d-%02d-%4d", 
                                 getDayOfMonth(),
                                 getMonth(),
                                 getYear());
        return str;
    }
    public String toStringTime()
    {
        String str = String.format("%02d:%02d:%02d:%03d",
                                 getHourOfDay(),
                                 getMinute(),
                                 getSecond(),
                                 getMillisecond());
        return str;
    }

    public Object clone()
    {
        DateTime dt = new DateTime();
        dt.gc = (GregorianCalendar) this.gc.clone();
        return dt;
    }
   
    @Override
    public int compareTo(DateTime o)
    {
        DateTime other = (DateTime)o;
        return gc.compareTo(other.gc);
    }
   
    public boolean before(DateTime other)
    {
        return this.gc.before(other.gc);
    }   
    public boolean after(DateTime other)
    {
        return this.gc.before(other.gc);
    }  
    public boolean eqauls(DateTime other)
    {
        return this.gc.equals(other.gc);
    }
}
