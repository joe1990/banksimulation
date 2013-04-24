package ch.bfh.ti.pbs.gui;

import java.net.URL;
import java.util.Scanner;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.DialogCloseListener;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

import ch.bfh.ti.pbs.helpers.BankReaderWriter;

public class NewTransactionDialog extends Dialog implements Bindable
{
    @BXML
    private PushButton psbSaveTransaction;
    @BXML
    private TextInput txtAmount;
    
    @Override
    public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2)
    {
    }
    
    public PushButton getSaveTransactionButton()
    {
        return psbSaveTransaction;
    }
    
    public String getAmount()
    {
        return txtAmount.getText();
    }
    
    public void reset()
    {
        txtAmount.setText("");
    }
}
