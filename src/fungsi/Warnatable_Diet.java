/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fungsi;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import util.GColors;
import util.MultiLineText;

/**
 *
 * @author ASUS
 */
public class Warnatable_Diet extends DefaultTableCellRenderer{
int printKolom;
    public Warnatable_Diet(int printKolom) {
    this.printKolom = printKolom;
    }

    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component =  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); //To change body of generated methods, choose Tools | Templates.
    if (isSelected)
        {
            component.setForeground(new Color(219, 100, 13));
        }
        else
        {
            component.setForeground(new Color(60,80,50));
        }
        
        if (column == printKolom && value.equals("PRINT"))
        {
            component.setBackground(GColors.Kuning);// new Color(248,253,243));
        }
        else
        {
            component.setBackground(GColors.Biru10);// new Color(255,255,255));
        } 
        
        if (value instanceof MultiLineText) 
        {
            MultiLineText mt = (MultiLineText) value;            
            table.setRowHeight(row, 25 + 18 * (mt.getLength() - 1));
        }
        
        return component;
    }
    
}
