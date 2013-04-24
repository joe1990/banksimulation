package ch.bfh.ti.pbs.gui;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;

import ch.bfh.ti.pbs.helpers.BankReaderWriter;

public class BankSimulation implements Application
{
    private Window window = null;
    
    
    @Override
    public void startup(Display display, Map<String, String> properties) throws Exception 
    {
        java.awt.Window hostWindow = display.getHostWindow();
        hostWindow.setMinimumSize(new Dimension(600, 400));
        
        
        BXMLSerializer bxmlSerializer = new BXMLSerializer();
        window = (Window)bxmlSerializer.readObject(BankSimulation.class, "BankSimulation.bxml");

        window.open(display);
    }
 
    @Override
    public boolean shutdown(boolean optional) 
    {
        if (window != null) {
            try
            {
                BankReaderWriter.getInstance().writeFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            window.close();
        }
        return false;
    }
 
    @Override
    public void suspend() 
    {
    }
 
    @Override
    public void resume() 
    {
    }
}
