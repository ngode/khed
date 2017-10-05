/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package widget;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTabbedPane;
import util.CustomTabbedPaneUI;
import util.GColors;

/**
 *
 * @author khanzamedia
 */
public class TabPane extends JTabbedPane 
{
    public TabPane()
    {
        super();
        setBackground(GColors.Biru40);// new Color(250,255,245));    
        setForeground(new Color(60,80,50));    
        //this.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(245,140,245)));     
        
        setUI(new CustomTabbedPaneUI());
    }
}
