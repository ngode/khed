/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fungsi;

import java.util.List;

/**
 *
 * @author GrT
 */
public class GQuery
{
    private static sekuel sql;
    private String query;
    
    public GQuery()
    {
        if (sql == null)
            sql = new sekuel();
        
        query = "";
    }
    
    public GQuery(String q)
    {
        if (sql == null)
            sql = new sekuel();
        
        query = q;
    }
    
    public GQuery a(String q)
    {
        query += " " + q;
        
        return this;
    }
    
    public GQuery set(String key, String val)
    {
        if (!query.contains("{" + key + "}"))
            throw new RuntimeException("Kunci '" + key + "' tidak ditemukan untuk query '" + query + "'");
        else
            query = query.replace("{" + key + "}", "'" + val + "'");
        
        return this;
    }
    
    public String compile()
    {
        return query;
    }
    
    public List<String[]> select()
    {
        return sql.select(query);
    }
    
    public String[] getRow()
    {
        return sql.selectRow(query);
    }
    
    public String getString()
    {
        return sql.selectScalar(query);
    }
    
    public int getInt()
    {
        return GConvert.parseInt(sql.selectScalar(query));
    }
    
    public boolean insert()
    {
        return sql.write(query);
    }
}
