/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package widget;

import fungsi.GQuery;

/**
 *
 * @author GrT
 */
public class TableComboBox extends ComboBox
{
    public void refresh(String table, String valCol, String dispCol)
    {
        removeAllItems();
        
        new GQuery()
                .a("SELECT {valCol}, {dispCol} FROM {table}")
                .setNoQuote("valCol", valCol)
                .setNoQuote("dispCol", dispCol)
                .setNoQuote("table", table)
                .select()
                .forEach(sa ->
                {
                    addItem(new Pair(sa[0], sa[1]));
                });
    }
    
    public String getText()
    {
        return getSelectedItem() == null ? "" : ((Pair)getSelectedItem()).disp;
    }
    
    public String getValue()
    {
        return getSelectedItem() == null ? "" : ((Pair)getSelectedItem()).val;
    }
    
    public void setSelectedValue(String val)
    {
        for (int a = 0; a < getItemCount(); a++)
        {
            if (val.equals(((Pair)getItemAt(a)).val))
            {
                setSelectedIndex(a);
                break;
            }
        }
    }
    
    public void setSelectedDisp(String disp)
    {
        for (int a = 0; a < getItemCount(); a++)
        {
            if (disp.equals(((Pair)getItemAt(a)).disp))
            {
                setSelectedIndex(a);
                break;
            }
        }
    }
    
    public static class Pair
    {
        private String val, disp;
        
        public Pair(String val, String disp)
        {
            this.val = val;
            this.disp = disp;
        }

        @Override
        public String toString()
        {
            return disp;
        }
    }
}
