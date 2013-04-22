package ch.bfh.ti.pbs.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.List;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.ScrollPane;
import org.apache.pivot.wtk.TabPane;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.TableView.Column;
import org.apache.pivot.wtk.TableView.ColumnSequence;
import org.apache.pivot.wtk.TextInput;

import ch.bfh.ti.pbs.bankaccounts.Bank;
import ch.bfh.ti.pbs.bankactivities.Transaction;
import ch.bfh.ti.pbs.customers.Customer;
import ch.bfh.ti.pbs.helpers.DateTime;
import ch.bfh.ti.pbs.helpers.Decimal;

public class TabPanel extends TabPane implements Bindable
{
	@BXML
	private TextInput txtFirstName;
	@BXML
	private TextInput txtLastName;
	@BXML
	private TableView tvTransactions;
	
	public TabPanel()
	{

	}

	@Override
	public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2)
	{
		txtFirstName.setText("");
		txtLastName.setText("Shevchuk");


		tvTransactions = (TableView)arg0.get("tvTransactions");
		ColumnSequence cols = tvTransactions.getColumns();
		Column col = cols.get(2);
		System.out.println(col.getName());
		
		
		Bank firstBankOfJava;
		File f = new File("bank.dat");
		if (f.exists()) {
			ObjectInputStream in;
			try {
				in = new ObjectInputStream(new FileInputStream(f));
				firstBankOfJava = (Bank) in.readObject();
				in.close();

				//fillTableView(firstBankOfJava.Customers);
				fillTableView();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	private void fillTableView()
	{		
		List<Transaction> trans = new ArrayList<Transaction>();	
		
		trans.add(new Transaction(new DateTime(2013,02,02), new Decimal(100.0), new Decimal(50.0), "Account 1"));
		trans.add(new Transaction(new DateTime(2013,02,03), new Decimal(100.0), new Decimal(30.0), "Account 2"));
		trans.add(new Transaction(new DateTime(2013,02,04), new Decimal(100.0), new Decimal(20.0), "Account 3"));
		tvTransactions.setTableData(trans);
//		tvTransactions.setTableData(getName());
		
		//tvTransactions.load(new BeanAdapter(customers));
		//load(new BeanAdapter(stockQuote));
		//trvUsers.setTreeData(rootBranch);
	}
}
