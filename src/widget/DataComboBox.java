/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package widget;

import fungsi.GQuery;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GrT
 */
public class DataComboBox extends ComboBox
{
    List<String> datas = new ArrayList<>();
    
    public void refresh(String query)
    {
        removeAllItems();
        
        new GQuery()
                .a(query)
                .select()
                .forEach(sa -> 
                {
                    addItem(sa[0]);
                });
    }
    
    public void addData(String s)
    {
        datas.add(s);
    }
    
    public void showData()
    {
        removeAllItems();
        
        for (String s : datas) addItem(s);
    }
    
    public void hideData()
    {
        removeAllItems();
    }
    
    public String getText()
    {
        if (getSelectedItem() == null)
            return "";
        else
            return super.getSelectedItem().toString();
    }
}
