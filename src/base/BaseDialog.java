/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

import interfaces.WindowClosedListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;

/**
 *
 * @author GrT
 */
public class BaseDialog extends JDialog implements WindowListener
{
    List<WindowClosedListener> windowClosedListeners;
    
    public BaseDialog(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        
        windowClosedListeners = new ArrayList<>();
        
        addWindowListener(this);
    }
    
    // Window closed listener ========================
    public void addWindowClosedListener(WindowClosedListener listener)
    {
        windowClosedListeners.add(listener);
    }
    
    public void removeWindowClosedListener(WindowClosedListener listener)
    {
        windowClosedListeners.remove(listener);
    }
    
    public void onWindowClosed()
    {
        for (WindowClosedListener l : windowClosedListeners)
        {
            l.onClosed();
        }
    }

    // Implements ====================
    @Override
    public void windowOpened(WindowEvent e)
    {
        
    }

    @Override
    public void windowClosing(WindowEvent e)
    {
        
    }

    @Override
    public void windowClosed(WindowEvent e)
    {
        onWindowClosed();
    }

    @Override
    public void windowIconified(WindowEvent e)
    {
        
    }

    @Override
    public void windowDeiconified(WindowEvent e)
    {
        
    }

    @Override
    public void windowActivated(WindowEvent e)
    {
        
    }

    @Override
    public void windowDeactivated(WindowEvent e)
    {
        
    }
}
