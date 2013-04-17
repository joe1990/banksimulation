package ch.bfh.ti.pbs.gui;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;

public class BankSimulation implements Application
{
    private Window window = null;
    
    
    @Override
    public void startup(Display display, Map<String, String> properties) throws Exception 
    {
        BXMLSerializer bxmlSerializer = new BXMLSerializer();
        window = (Window)bxmlSerializer.readObject(BankSimulation.class, "BankSimulation.bxml");
        
        window.open(display);
    }
 
    @Override
    public boolean shutdown(boolean optional) 
    {
        if (window != null) {
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
