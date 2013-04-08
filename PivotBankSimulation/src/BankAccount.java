import java.util.ArrayList;
import java.util.Collections;

public abstract class BankAccount
{    
   private static long nextBankAccountNo = 1;
   private static Decimal penaltyRatePerYearPC = new Decimal(10.0);
   
   private long accountNumber;
   private Decimal balance;
   private ArrayList<Transaction> transactions = new ArrayList<Transaction>();

   public BankAccount()
   {  accountNumber = nextBankAccountNo;
      nextBankAccountNo++;
      balance = new Decimal(0.0);
   }
 
   public void deposit(DateTime date, Decimal amount, String remark) 
   {  balance = balance.add(amount);
      transactions.add(new Transaction(date, amount, balance, remark));
   }  
   public void withdraw(DateTime date, Decimal amount, String remark) 
   {  balance = balance.subtract(amount);
      transactions.add(new Transaction(date, amount.negate(), balance, remark));
   }
   public void transfer(DateTime date, Decimal amount, BankAccount other)
   {  this.withdraw(date, amount, "Transfer to  : #" + other.accountNumber);
      other.deposit(date, amount, "Transfer from: #" + this.accountNumber);
   }

   /**
    * Abstract method to retrieve the interest rate at a certain DateTime
    * @param date
    * @return
    */
   public abstract Decimal getInterestRateAtDate(DateTime date);
   
   /**
    * Adds interest transactions to the bank account at the last millisecond
    * of the upToDate date. Positive and negative interests are added separately.
    * The interest is summed up from the first day of the same year up to upToDate.
    * The interest is calculated every day once at the last millisecond with the
    * interest rate valid at that day.
    * @param upToDate 
    * @param logOutput prints log output if true.
    */
   public void applyInterest(DateTime upToDate, boolean logOutput)
   {  
      if (transactions.size()==0) return;
      
      // Be sure that transactions are sorted by date
      Collections.sort(transactions);
      
      // Get balance of the last day before the 1st of the year
      DateTime nextDay = new DateTime(upToDate.getYear()-1,12,31);
      nextDay.setLastMillisecond();
      int iT = 0;
      while(iT < transactions.size() && transactions.get(iT).getTimestamp().before(nextDay)) iT++;
      if (iT >= transactions.size()) iT = transactions.size()-1;
      Transaction t = transactions.get(iT);
      Decimal balance = t.getBalance().subtract(t.getAmount());
      if (logOutput) System.out.println("Start balance: " + balance + " at: " + nextDay.toString());

      // Loop through all days until upToDate
      int days = upToDate.getDayOfYear();
      Decimal interestPositiveSum = new Decimal(0.0);
      Decimal interestNegativeSum = new Decimal(0.0);
      
      // Calculate penalty interest per day
      Decimal penaltyRatePerYear = penaltyRatePerYearPC.multiply(new Decimal(0.01));
      Decimal penaltyRatePerDay  = penaltyRatePerYear.divide(new Decimal((double)days));
      
      // Loop through every day and sum up the day interest with the valid interest rate.
      for (int d = 0; d < days; d++)
      {
         nextDay.addDayOfYear(1);
         
         // Get the interest rate at the current day
         Decimal interestRatePerYear = getInterestRateAtDate(nextDay).multiply(new Decimal(0.01));
         Decimal interestRatePerDay  = interestRatePerYear.divide(new Decimal((double)days));
         
         while(iT < transactions.size() && 
               transactions.get(iT).getTimestamp().before(nextDay)) iT++;
         if (iT > 0) iT--; // go back to the last transaction of the day
         
         t = transactions.get(iT);  
         
         Decimal dayInterest;
         if (t.getBalance().isNegative())
         {  dayInterest = t.getBalance().multiply(penaltyRatePerDay);
            interestNegativeSum = interestNegativeSum.add(dayInterest);
         } else 
         {  dayInterest = t.getBalance().multiply(interestRatePerDay);
            interestPositiveSum = interestPositiveSum.add(dayInterest);
         }
         
         if (logOutput) 
         {  String log = "Date: " + nextDay.toStringDate(); 
            log += ", Last tran. at: " + t.getTimestamp().toString();
            if (dayInterest.isNegative())
                 log += ", i.rate: " + penaltyRatePerYear.toString(6,5);
            else log += ", i.rate: " + interestRatePerYear.toString(6,5);
            log += ", bal.: " + t.getBalance().toString(8,2);
            log += ", int. per day: " + dayInterest.toString(7,4);
            log += ", int.pos.sum: " + interestPositiveSum.toString(8,3);
            log += ", int.neg.sum: " + interestNegativeSum.toString(8,3);
            System.out.println(log);         
         }
      }

      // Set last millisecond of up to date
      DateTime lastMilisecond = (DateTime) upToDate.clone();
      lastMilisecond.setLastMillisecond();
      
      // Add positive & negative interests
      if (!interestPositiveSum.isZero())
         deposit(lastMilisecond, interestPositiveSum, "Positive interest.");
      if (!interestNegativeSum.isZero())
         deposit(lastMilisecond, interestNegativeSum, "Negative interest.");
   }

   public String toString()
   {
      return "BankAccount: [No: " + accountNumber + ", balance: " + balance.toString(8,2) + "]";
   }
   
   // Setters & getters 
   public Decimal getBalance() {return balance;}
   public long getAccountNumber() {return accountNumber;}
   public ArrayList<Transaction> getTransactions() {return transactions;}
}
