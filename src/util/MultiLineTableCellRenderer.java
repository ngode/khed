/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Component;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author GrT
 */
public class MultiLineTableCellRenderer extends JList<String> implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //make multi line where the cell value is String[]
        if (value instanceof String[]) 
        {
            String[] sa = (String[]) value;
            setListData(sa);
            
            table.setRowHeight(row, 22 * sa.length);
        }
//
//        //cell backgroud color when selected
//        if (isSelected) {
//            setBackground(UIManager.getColor("Table.selectionBackground"));
//        } else {
//            setBackground(UIManager.getColor("Table.background"));
//        }

        return this;
    }
}
