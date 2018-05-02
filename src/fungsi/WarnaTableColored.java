/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fungsi;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import util.ColorUtil;
import util.GColors;
import util.MultiLineText;

/**
 *
 * @author Owner
 */
public class WarnaTableColored extends DefaultTableCellRenderer
{
    private static final Color foregroundColor = new Color(60, 80, 50);
    private static final Color selectedBackgroundColor = GColors.Biru15;
    private static final Color oddBackgroundColor = GColors.Biru10;
    private static final Color evenBackgroundColor = GColors.Biru10;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        component.setForeground(getForegroundColor(row, column));

        if (isSelected)
        {
            component.setBackground(selectedBackgroundColor);
        }
        else
        {
            if (row % 2 == 1)
            {
                component.setBackground(oddBackgroundColor);
            }
            else
            {
                component.setBackground(evenBackgroundColor);
            }
        }

        return component;
    }

    public Color getForegroundColor(int row, int col)
    {
        return foregroundColor;
    }
}
