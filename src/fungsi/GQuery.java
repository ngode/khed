/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fungsi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author GrT
 */
public class GQuery
{
    private static sekuel sql;
    private String query;
    
    private HashMap<String, String> map;
    
    public GQuery()
    {
        if (sql == null)
            sql = new sekuel();
        
        query = "";
        map = new HashMap<>();
    }
    
    public GQuery(String q)
    {
        if (sql == null)
            sql = new sekuel();
        
        query = q;
        map = new HashMap<>();
    }
    
    public static void setAutoCommit(boolean b)
    {
        if (sql == null)
            sql = new sekuel();
        
        if (b)
            sql.AutoComitTrue();
        else
            sql.AutoComitFalse();
    }
    
    public GQuery a(String q)
    {
        query += " " + q;
        
        return this;
    }
    
    public GQuery set(String key, String val)
    {
        return set(key, val, true);
    }
    
    public GQuery setNoQuote(String key, String val)
    {
        return set(key, val, false);
    }
    
    private GQuery set(String key, String val, boolean withQuote)
    {
        if (!query.contains("{" + key + "}"))
            throw new RuntimeException("Kunci '" + key + "' tidak ditemukan untuk query '" + query + "'");
        else
        {
            if (withQuote)
                val = "'" + val + "'";
            
            if (map.containsKey(key))
                map.remove(key);
            
            map.put(key, val);
        }
        
        return this;
    }
    
    public String compile()
    {
        Iterator it = map.entrySet().iterator();
        String qCom = query;
        
        while (it.hasNext())
        {
            Map.Entry<String, String> pair = (Map.Entry)it.next();
            
            qCom = qCom.replace("{" + pair.getKey() + "}", pair.getValue());
        }
        
        return qCom;
    }
    
    public List<String[]> select()
    {
        return sql.select(compile());
    }
    
    public String[] getRow()
    {
        return sql.selectRow(compile());
    }
    
    public HashMap<String, String> getRowWithName()
    {
        return sql.selectRowWithName(compile());
    }
    
    public String getString()
    {
        return sql.selectScalar(compile());
    }
    
    public int getInt()
    {
        return GConvert.parseInt(sql.selectScalar(compile()));
    }
    
    public boolean write()
    {
        return sql.write(compile());
    }
}
