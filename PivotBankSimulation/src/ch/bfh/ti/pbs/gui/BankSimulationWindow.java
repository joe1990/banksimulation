package ch.bfh.ti.pbs.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.List;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.collections.Sequence.Tree.Path;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.Alert;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.FileBrowserSheet;
import org.apache.pivot.wtk.Frame;
import org.apache.pivot.wtk.ListView;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.Sheet;
import org.apache.pivot.wtk.SheetCloseListener;
import org.apache.pivot.wtk.TablePane;
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
    @BXML private WindowContent tblWindowContent;
    @BXML private NewTransactionDialog dlgNewTransaction;

    public BankSimulationWindow() 
    {
        Action.getNamedActions().put("actOpen", new Action() 
        {
            @Override
            public void perform(Component source) 
            {
                fbsOpenFile.open(BankSimulationWindow.this, new SheetCloseListener() 
                {
                    @Override
                    public void sheetClosed(Sheet arg0)
                    {
                        if (arg0.getResult()) {
                            Sequence<File> selectedFiles = fbsOpenFile.getSelectedFiles();
                            ListView lstSelectedFiles = new ListView();
                            lstSelectedFiles.setListData((List<File>) selectedFiles);
                            
                            try {
                                File bankFile = (File)lstSelectedFiles.getListData().get(0);
                                tblWindowContent.setWindowsOutput(bankFile);
                                BankSimulationWindow.this.setTitle("Bank Simulation 3: " + bankFile.getPath());
                            } catch (Exception e) {
                                Alert.alert(MessageType.ERROR, "Wrong file format. Must be a .dat-File.", BankSimulationWindow.this);
                            }
                            
                        } else {
                            Alert.alert(MessageType.INFO, "Please select a .dat-File.", BankSimulationWindow.this);                            
                        }
                    }
                    
                
                });
            }
        });
        
        Action.getNamedActions().put("actQuit", new Action() {
            @Override
            public void perform(Component source) {
                try
                {
                    tblWindowContent.saveBankToFile();
                } catch (IOException e){
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
    }
    
    @Override
    public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2)
    {
        try
        {
            File bankFile = new File("bank.dat");
            tblWindowContent.setWindowsOutput(bankFile);
            BankSimulationWindow.this.setTitle("Bank Simulation 3: " + bankFile.getPath());
            tblWindowContent.setNewTransactionDialog(dlgNewTransaction);
            
        } catch (ClassNotFoundException | IOException e)
        {
            BankSimulationWindow.this.setTitle("Bank Simulation 3: ");
            e.printStackTrace();
        }
        setNewTransactionAction();
    }  
    
    private void setNewTransactionAction() 
    {
        tblWindowContent.getNewTransactionButton().getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button arg0)
            {
                dlgNewTransaction.open(BankSimulationWindow.this);  
            }
        });
    }
}
