package ch.bfh.ti.pbs.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.URL;
import java.util.ArrayList;
//import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.*;
import org.apache.pivot.collections.Sequence.Tree.Path;
import org.apache.pivot.util.Filter;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.*;
import org.apache.pivot.wtk.content.*;

import ch.bfh.ti.pbs.bankaccounts.Bank;
import ch.bfh.ti.pbs.bankaccounts.BankAccount;
import ch.bfh.ti.pbs.bankactivities.*;
import ch.bfh.ti.pbs.customers.Customer;
import ch.bfh.ti.pbs.helpers.BankReaderWriter;

public class WindowContent extends TablePane implements Bindable
{
	@BXML
	private TextInput txtFirstName;
	@BXML
	private TextInput txtLastName;
	@BXML
	private PushButton psbSaveCustomer;
	@BXML
	private PushButton psbNewTransaction;
	@BXML
	private TreeView trvUsers;
	@BXML
	private NewTransaction alertDialog;
	@BXML
	private TableView tvTransactions;

	private Customer customer;
	private BankAccount bankAccount;
	private Bank bank;

	public WindowContent()
	{

	}

	@Override
	public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2)
	{
		
	}
	
	public void setWindowsOutput(File bankFile) throws FileNotFoundException, IOException, ClassNotFoundException, StreamCorruptedException
	{
	    BankReaderWriter.getInstance().setWorkFile(bankFile);

        this.bank = BankReaderWriter.getInstance().readFile();
        this.fillTreeView(bank.Customers);

        txtFirstName.setText("");
        txtLastName.setText("");

        setTreeViewActions();
        setTableViewActions();
        setTransactions(bankAccount);
        setSaveCustomerActions();
	}

	public void setBankAccount(BankAccount bankAccount)
	{
		this.bankAccount = bankAccount;
	}

	private void setTreeViewActions()
	{
		this.trvUsers.getTreeViewSelectionListeners().add(
				new TreeViewSelectionListener() {

					@Override
					public void selectedPathsChanged(TreeView arg0,
							Sequence<Path> arg1)
					{
					}

					@Override
					public void selectedPathRemoved(TreeView arg0, Path arg1)
					{
					}

					@Override
					public void selectedPathAdded(TreeView arg0, Path arg1)
					{
					}

					@Override
					public void selectedNodeChanged(TreeView arg0, Object arg1)
					{
						TreeNode treeNode = (TreeNode) arg0.getSelectedNode();
						if (arg0.getSelectedNode() instanceof TreeBranch) {
							customer = (Customer) treeNode.getUserData();
							txtFirstName.setText(customer.getFirstname());
							txtLastName.setText(customer.getLastname());
						} else if (arg0.getSelectedNode() instanceof TreeNode) {
							bankAccount = (BankAccount) treeNode.getUserData();
							customer = (Customer) treeNode.getParent()
									.getUserData();
							txtFirstName.setText(customer.getFirstname());
							txtLastName.setText(customer.getLastname());
							setTransactions(bankAccount);
						}
					}
				});
	}

	private void setTableViewActions()
	{
		this.tvTransactions.getTableViewSelectionListeners().add(
				new TableViewSelectionListener.Adapter() {
					public void selectedRowChanged(TableView tableView,
							Object previousSelectedRow)
					{
						//System.out.println("selectedRowChanged");
					}
				});
	};

	private void setSaveCustomerActions()
	{
		psbSaveCustomer.getButtonPressListeners().add(
				new ButtonPressListener() {
					@Override
					public void buttonPressed(Button button)
					{
						customer.setLastname(txtLastName.getText());
						customer.setFirstname(txtFirstName.getText());
						try {
							BankReaderWriter.getInstance().writeFile();
							fillTreeView(bank.Customers);
						} catch (IOException e) {

							e.printStackTrace();
						}
					}
				});
	}
	
	private void setNewTransactionActions()
	{
	    psbNewTransaction.getButtonPressListeners().add(
	            new ButtonPressListener() {
                    @Override
                    public void buttonPressed(Button arg0)
                    {
                        
                        
                    }
	            });
	}

	private void fillTreeView(ArrayList<Customer> customers)
	{

		TreeBranch rootBranch = new TreeBranch();
		for (Customer customer : customers) {
			TreeBranch customerBranch = new TreeBranch();

			customerBranch.setText(customer.getName());
			customerBranch.setIcon("/ch/bfh/ti/pbs/gui/user.png");
			customerBranch.setUserData(customer);

			for (BankAccount account : customer.getAccounts()) {
				TreeNode accountNode = new TreeNode();
				accountNode.setText(String.valueOf(account.getAccountNumber()));
				accountNode.setIcon("/ch/bfh/ti/pbs/gui/bankaccount.png");
				accountNode.setUserData(account);
				customerBranch.add(accountNode);
			}
			rootBranch.add(customerBranch);
		}

		this.trvUsers.setTreeData(rootBranch);
	}

	private void setTransactions(BankAccount account)
	{
		if (bankAccount == null)
			return;
		List<Transaction> trans = new org.apache.pivot.collections.ArrayList<Transaction>();
		for (Transaction t : account.getTransactions()) {
			trans.add(t);
		}
		tvTransactions.clear();
		if (trans != null)
			tvTransactions.setTableData(trans);
	};

}
