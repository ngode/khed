/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fungsi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author GrT
 */
public class GRow
{
    private HashMap<String, Object> map;
    private List<Object> list;
    
    public GRow()
    {
        map = new HashMap<>();
        list = new ArrayList<>();
    }
    
    // Add
    public void add(String key, Object val)
    {
        map.put(key, val);
    }
    
    public void add(Object val)
    {
        list.add(val);
    }
    
    // Get
    public Object get(String key)
    {
        if (!map.containsKey(key))
        {
            throw new RuntimeException("Tak ada kunci " + key + " di GRow");
        }
        
        return map.get(key);
    }
    
    public String getString(String key)
    {
        return get(key).toString();
    }
    
    public int getInt(String key)
    {
        return GConvert.parseInt(getString(key));
    }
    
    public double getDouble(String key)
    {
        return GConvert.parseDouble(getString(key));
    }
    
    public String getString(int index)
    {
        if (index >= list.size())
        {
            throw new RuntimeException("Index salah, max " + (list.size() - 1) + " di GRow");
        }
        
        return list.get(index).toString();
    }
    
    public int getInt(int index)
    {
        return GConvert.parseInt(getString(index));
    }
    
    public double getDouble(int index)
    {
        return GConvert.parseDouble(getString(index));
    }
}
