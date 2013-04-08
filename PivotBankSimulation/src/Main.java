public class Main
{     
   public static void main(String[] args)
   {  
      // Add interest rates
      CheckingAccount.interestRates.add(new InterestRate(new DateTime(2012,1,1), new Decimal(0)));
      SavingsAccount.interestRates.add(new InterestRate(new DateTime(2012,1,1), new Decimal(12).divide(new Decimal(8))));
      SavingsAccount.interestRates.add(new InterestRate(new DateTime(2013,3,1), new Decimal(13).divide(new Decimal(8))));
      SavingsAccount.interestRates.add(new InterestRate(new DateTime(2013,9,1), new Decimal(14).divide(new Decimal(8))));
      
      // Create customers with accounts
      Customer A = new Customer("A", new CheckingAccount());
      Customer B = new Customer("B", new SavingsAccount());
      Customer C = new Customer("C", new TimeDepositAccount());
      
      System.out.println("CheckingAccount    Interest Rate: " + CheckingAccount.getInterestRate().toString(6,3));
      System.out.println("SavingsAccount     Interest Rate: " + SavingsAccount.getInterestRate().toString(6,3));
      System.out.println("TimeDepositAccount Interest Rate: " + TimeDepositAccount.getInterestRate().toString(6,3));
      System.out.println("");
      
      // Do transactions
      DateTime startDate1 = new DateTime(2013, 1, 1, 12,00,00);
      DateTime endDate1   = new DateTime(2013, 6,30, 23,59,59);
      DateTime startDate2 = new DateTime(2013, 7, 1, 12,00,00);
      DateTime endDate2   = new DateTime(2013,12,31, 23,59,59);
      addOneTransactionPerDay(A.getAccount(0), startDate1, endDate1, new Decimal( 100.0));
      addOneTransactionPerDay(A.getAccount(0), startDate2, endDate2, new Decimal(-100.0));
      addOneTransactionPerDay(B.getAccount(0), startDate1, endDate1, new Decimal( 100.0));
      addOneTransactionPerDay(B.getAccount(0), startDate2, endDate2, new Decimal(-100.0));
      addOneTransactionPerDay(C.getAccount(0), startDate1, endDate1, new Decimal( 100.0));
      addOneTransactionPerDay(C.getAccount(0), startDate2, endDate2, new Decimal(-100.0));
      
      A.getAccount(0).applyInterest(new DateTime(2013,12,31), false);
      B.getAccount(0).applyInterest(new DateTime(2013,12,31), false);
      C.getAccount(0).applyInterest(new DateTime(2013,12,31), false);

      System.out.println(A.toString());
      System.out.println(B.toString());
      System.out.println(C.toString());
      
      //Customer: [Name: A, Accounts: [BankAccount: [No: 1, balance:  -325.19], ChechingAccount: TransactionCount: 0]
      //Customer: [Name: B, Accounts: [BankAccount: [No: 2, balance:  -152.51], SavingsAccount]
      //Customer: [Name: C, Accounts: [BankAccount: [No: 3, balance:  -780.63], SavingsAccount, TimeDepositAccount]

      //for (Transaction t : A.getAccount(0).getTransactions()) System.out.println(t.toString());
      //for (Transaction t : B.getAccount(0).getTransactions()) System.out.println(t.toString());
      //for (Transaction t : C.getAccount(0).getTransactions()) System.out.println(t.toString());
   }
   
   /**
    * Adds a transaction per day of a given amount between start date and end date to the given account.
    * @param account
    * @param startDate
    * @param endDate
    * @param amount
    */
   public static void addOneTransactionPerDay(BankAccount account, DateTime startDate, DateTime endDate, Decimal amount)
   {  
      DateTime date = (DateTime) startDate.clone();
      
      if (amount.compareTo(new Decimal(0.0)) < 0)
      {  while (date.before(endDate)) 
         {  account.withdraw(date, amount.negate(), "");
            date.addDayOfYear(1);
         }
      } else
      {  while (date.before(endDate))
         {  account.deposit(date, amount, "");
            date.addDayOfYear(1);
         }
      }   
   }
}
