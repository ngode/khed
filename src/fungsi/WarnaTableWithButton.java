/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fungsi;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import util.GColors;

/**
 *
 * @author GrT
 */
public class WarnaTableWithButton extends DefaultTableCellRenderer
{
    //private JTable table;
    private int rowOver = -1;
    private int columnOver = -1;
    private boolean pressed = false;
    
    public WarnaTableWithButton(JTable table)
    {
        //this.table = table;
        table.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseExited(MouseEvent e)
            {
                rowOver = -1;
                columnOver = -1;

                table.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                pressed = true;
                table.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                pressed = false;
                table.repaint();
            }
        });
        
        table.addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent e)
            {
                int r = table.rowAtPoint(e.getPoint());
                int c = table.columnAtPoint(e.getPoint());
                
                if (rowOver != r || columnOver != c)
                {
                    rowOver = r;
                    columnOver = c;
                    
                    table.repaint();
                }
            }
        });
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if (isSelected)
        {
            component.setForeground(new Color(219, 100, 13));
        }
        else
        {
            component.setForeground(new Color(60, 80, 50));
        }

        if (row % 2 == 1)
        {
            component.setBackground(GColors.Biru15);
        }
        else
        {
            component.setBackground(GColors.Biru10);
        }

        if (value instanceof ButtonCell)
        {
            ButtonCell c = (ButtonCell) value;
            
            if (c.isEnabled())
            {
                if (rowOver == row && columnOver == column)
                {
                    if (pressed)
                    {
                        component.setForeground(Color.WHITE);
                        component.setBackground(GColors.Biru70);
                    }
                    else
                    {
                        component.setForeground(Color.WHITE);
                        component.setBackground(GColors.Biru50);
                    }
                }
                else
                {
                    component.setForeground(Color.WHITE);
                    component.setBackground(GColors.Biru60);
                }
            }
            else
            {
                component.setForeground(Color.WHITE);
                component.setBackground(GColors.Abu);
            }
            
            if (component instanceof JLabel)
            {
                ((JLabel)component).setHorizontalAlignment(JLabel.CENTER);
                setBold(component, true);
            }
        }
        else
        {
            if (component instanceof JLabel)
            {
                ((JLabel)component).setHorizontalAlignment(JLabel.LEFT);
                setBold(component, false);
            }
        }

        return component;
    }
    
    private void setBold(Component c, boolean b)
    {
        JLabel l = (JLabel)c;
        
        Font f = l.getFont();
        
        if (b)
            l.setFont(f.deriveFont(f.getStyle() | Font.BOLD ));
        else
            l.setFont(f.deriveFont(f.getStyle() & ~Font.BOLD ));
    }
}
