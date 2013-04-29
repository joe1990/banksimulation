package ch.bfh.ti.pbs.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.List;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
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

/**
 * Frame der Applikation.
 * Klasse gehört zum BXML-File "BankSimulation.bxml"
 *
 */
public class BankSimulationWindow extends Frame implements Bindable 
{
    @BXML private FileBrowserSheet fbsOpenFile;
    @BXML private WindowContent tblWindowContent;
    @BXML private NewTransactionDialog dlgNewTransaction;

    /**
     * Konstruktor der Klasse.
     * Erzeugt die Action Listeners für die Menüaktionen (Quit und Open).
     */
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
    
    /**
     * Initialisiert das GUI mit den Daten aus dem .dat-File oder erzeugt ein neues Dat-File aus Beispieldaten und lädt dann dieses.
     */
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
    
    /**
     * Fügt dem NewTransaction-Button den ActionListener für die Drücken-Aktion hinzu.
     */
    private void setNewTransactionAction() 
    {
        tblWindowContent.getNewTransactionButton().getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button arg0)
            {
                tblWindowContent.setNewTransactionDialog(dlgNewTransaction);
                dlgNewTransaction.open(BankSimulationWindow.this);  
            }
        });
    }
}
