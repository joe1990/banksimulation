package ch.bfh.ti.pbs.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.collections.Sequence.Tree.Path;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.FileBrowserSheet;
import org.apache.pivot.wtk.Frame;
import org.apache.pivot.wtk.TreeView;
import org.apache.pivot.wtk.TreeViewSelectionListener;
import org.apache.pivot.wtk.content.TreeBranch;
import org.apache.pivot.wtk.content.TreeNode;

import ch.bfh.ti.pbs.customers.Customer;
import ch.bfh.ti.pbs.exceptions.UnderFlowException;
import ch.bfh.ti.pbs.helpers.BankReaderWriter;
import ch.bfh.ti.pbs.helpers.DateTime;
import ch.bfh.ti.pbs.helpers.Decimal;
import ch.bfh.ti.pbs.bankaccounts.Bank;
import ch.bfh.ti.pbs.bankaccounts.BankAccount;
import ch.bfh.ti.pbs.bankaccounts.CheckingAccount;
import ch.bfh.ti.pbs.bankaccounts.SavingsAccount;
import ch.bfh.ti.pbs.bankaccounts.TimeDepositAccount;
import ch.bfh.ti.pbs.bankactivities.InterestRate;

public class BankSimulationWindow extends Frame implements Bindable 
{
    @BXML private FileBrowserSheet fbsOpenFile;
    @BXML private TreeView trvUsers;
    @BXML private TabPanel tbpAccountDetails;
    private Bank bank;

    public BankSimulationWindow() 
    {
        Action.getNamedActions().put("actOpen", new Action() {
            @Override
            public void perform(Component source) {
                fbsOpenFile.open(BankSimulationWindow.this);
            }
        });
        
        Action.getNamedActions().put("actQuit", new Action() {
            @Override
            public void perform(Component source) {
                System.exit(0);
            }
        });
    }
    
    @Override
    public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2)
    {
        
        
        File workFile = new File("bank.dat");
        BankReaderWriter.getInstance().setWorkFile(workFile);
        
        try
        {
            this.bank = BankReaderWriter.getInstance().readFile();
            this.fillTreeView(bank.Customers);
        } catch (ClassNotFoundException | IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void fillTreeView(ArrayList<Customer> customers) {
        
        this.trvUsers.getTreeViewSelectionListeners().add(new TreeViewSelectionListener()
        {
            
            @Override
            public void selectedPathsChanged(TreeView arg0, Sequence<Path> arg1){}
            
            @Override
            public void selectedPathRemoved(TreeView arg0, Path arg1){}
            
            @Override
            public void selectedPathAdded(TreeView arg0, Path arg1){}
            
            @Override
            public void selectedNodeChanged(TreeView arg0, Object arg1)
            {
                TreeNode treeNode = (TreeNode) arg0.getSelectedNode();
                if (arg0.getSelectedNode()  instanceof TreeBranch) {
                    Customer customer = (Customer) treeNode.getUserData();
                    tbpAccountDetails.setCustomer(customer);
                } else {
                    BankAccount bankAccount = (BankAccount) treeNode.getUserData();
                    Customer customer = (Customer) treeNode.getParent().getUserData();
                    tbpAccountDetails.setCustomer(customer);
                    tbpAccountDetails.setBankAccount(bankAccount);
                }
                
            }
        });
        
        
        TreeBranch rootBranch = new TreeBranch();
        for(Customer customer: customers)
        {
            TreeBranch customerBranch = new TreeBranch();
                       
            customerBranch.setText(customer.getName());
            customerBranch.setIcon("/ch/bfh/ti/pbs/gui/user.png");
            customerBranch.setUserData(customer);
            
            for(BankAccount account: customer.getAccounts()) {
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
}
