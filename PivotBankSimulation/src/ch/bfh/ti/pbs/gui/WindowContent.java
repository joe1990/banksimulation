package ch.bfh.ti.pbs.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.collections.Sequence.Tree.Path;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TabPane;
import org.apache.pivot.wtk.TablePane;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.TreeView;
import org.apache.pivot.wtk.TreeViewSelectionListener;
import org.apache.pivot.wtk.content.TreeBranch;
import org.apache.pivot.wtk.content.TreeNode;

import ch.bfh.ti.pbs.bankaccounts.Bank;
import ch.bfh.ti.pbs.bankaccounts.BankAccount;
import ch.bfh.ti.pbs.customers.Customer;
import ch.bfh.ti.pbs.helpers.BankReaderWriter;

public class WindowContent extends TablePane implements Bindable
{
    @BXML private TextInput txtFirstName;
    @BXML private TextInput txtLastName;
    @BXML private PushButton psbSaveCustomer;
    @BXML private TreeView trvUsers;
    private Customer customer;
    private BankAccount bankAccount;
    private Bank bank;
    
    public WindowContent () {
        
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
        
        txtFirstName.setText("");
        txtLastName.setText("");
        
        setTreeViewActions();
        setSaveCustomerActions();
    }

    public void setBankAccount(BankAccount bankAccount) 
    {
        this.bankAccount = bankAccount;
    }
    
    private void setTreeViewActions()
    {
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
                    customer = (Customer) treeNode.getUserData();
                    txtFirstName.setText(customer.getFirstname());
                    txtLastName.setText(customer.getLastname());
                } else if (arg0.getSelectedNode() instanceof TreeNode) {
                    bankAccount = (BankAccount) treeNode.getUserData();
                    customer = (Customer) treeNode.getParent().getUserData();
                    txtFirstName.setText(customer.getFirstname());
                    txtLastName.setText(customer.getLastname());
                }
                
            }
        });
    }
    
    private void setSaveCustomerActions() {
        psbSaveCustomer.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
                customer.setLastname(txtLastName.getText());
                customer.setFirstname(txtFirstName.getText());
                try
                {
                    BankReaderWriter.getInstance().writeFile();
                    fillTreeView(bank.Customers);
                    
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private void fillTreeView(ArrayList<Customer> customers) {
        
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
