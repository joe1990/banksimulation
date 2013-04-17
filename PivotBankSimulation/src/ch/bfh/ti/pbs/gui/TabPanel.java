package ch.bfh.ti.pbs.gui;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.TabPane;
import org.apache.pivot.wtk.TextInput;

public class TabPanel extends TabPane implements Bindable
{
    @BXML private TextInput txtFirstName;
    @BXML private TextInput txtLastName;
    
    @Override
    public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2)
    {
        txtFirstName.setText("Vorname");
        txtLastName.setText("Nachname");
    }

}
