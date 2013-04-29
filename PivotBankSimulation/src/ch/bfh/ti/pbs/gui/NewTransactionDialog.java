package ch.bfh.ti.pbs.gui;

import java.net.URL;
import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TextInput;

/**
 * Dialog zum Hinzuf�gen einer neuen Transaktion.
 */
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
    
    /**
     * Gibt den "New Transaction"-Button zur�ck.
     * @return
     */
    public PushButton getSaveTransactionButton()
    {
        return psbSaveTransaction;
    }
    
    /**
     * Gibt den im "New Transaction"-Dialog eingegebenen Wert (Amount) als String zur�ck.
     * @return
     */
    public String getAmount()
    {
        return txtAmount.getText();
    }
    
    /**
     * L�scht die Textbox im "New Transaction"-Dialog.
     */
    public void reset()
    {
        txtAmount.setText("");
    }
}
