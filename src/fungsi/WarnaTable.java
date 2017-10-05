/*
 * To change this template, choose Tools | Templates
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
 * @author Owner
 */
public class WarnaTable extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (row % 2 == 1){
            component.setBackground(GColors.Biru15);// new Color(248,253,243));
        }else{
            component.setBackground(GColors.Biru10);// new Color(255,255,255));
        } 
        
        if (value instanceof MultiLineText) 
        {
            MultiLineText mt = (MultiLineText) value;            
            table.setRowHeight(row, 22 + 18 * (mt.getLength() - 1));
        }
        
        return component;
    }

}
