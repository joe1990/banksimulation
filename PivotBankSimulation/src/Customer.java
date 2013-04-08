import java.util.ArrayList;

public class Customer
{
   private String name;
   private ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();

   Customer(String aName, BankAccount firstAccount)
   {
      name = aName;
      addBankAccount(firstAccount);
   }
   
   public void addBankAccount(BankAccount aBankAccount)
   {
      if (aBankAccount != null)
         accounts.add(aBankAccount);
   }
   
   public String toString()
   {
      return "Customer: [Name: " + name + ", Accounts: " + accounts.toString();
   }
   
   public String getName() {return name;}
   public BankAccount getAccount(int index) {return accounts.get(index);}
}
