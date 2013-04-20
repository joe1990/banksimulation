package ch.bfh.ti.pbs.gui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TabPane;
import org.apache.pivot.wtk.TextInput;

import ch.bfh.ti.pbs.bankaccounts.BankAccount;
import ch.bfh.ti.pbs.customers.Customer;
import ch.bfh.ti.pbs.helpers.BankReaderWriter;

public class TabPanel extends TabPane implements Bindable
{
    @BXML private TextInput txtFirstName;
    @BXML private TextInput txtLastName;
    @BXML private PushButton psbSaveCustomer;
    private Customer customer;
    private BankAccount bankAccount;
    
    public TabPanel () {
        
    }
    
    @Override
    public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2)
    {
        txtFirstName.setText("");
        txtLastName.setText("");
        
        setSaveCustomerAction();
    }

    public void setCustomer(Customer customer) 
    {
        this.customer = customer;
        txtFirstName.setText(this.customer.getFirstname());
        txtLastName.setText(this.customer.getLastname());
    }
    
    public void setBankAccount(BankAccount bankAccount) 
    {
        this.bankAccount = bankAccount;
    }
    
    private void setSaveCustomerAction() {
        psbSaveCustomer.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
                customer.setLastname(txtLastName.getText());
                customer.setFirstname(txtFirstName.getText());
                try
                {
                    BankReaderWriter.getInstance().writeFile();
                    
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
    
}
