/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GrT
 */
public class ColSizePair
{
    private List<String> cols = new ArrayList<>();
    private List<Integer> sizes = new ArrayList<>();
    
    public void add(String col, int size)
    {
        cols.add(col);
        sizes.add(size);
    }
    
    public Object[] getColArray()
    {
        Object[] o = new Object[cols.size()];
        for (int a = 0; a < o.length; a++) o[a] = cols.get(a);
        
        return o;
    }
    
    public int getSize(int index)
    {
        return sizes.get(index);
    }
    
    public int getCount()
    {
        return cols.size();
    }
}
