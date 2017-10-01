/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author GrT
 */
public class MultiLineText
{
    private String[] val;
    
    public MultiLineText(String[] val)
    {
        this.val = val;
    }
    
    public int getLength()
    {
        return val.length;
    }
    
    public String getFirst()
    {
        return val[0];
    }

    @Override
    public String toString()
    {
        String res = "<html>";
            
        for (int a = 0; a < val.length; a++)
        {
            res += "<nobr>" + val[a] + "</nobr>";

            if (a < val.length - 1)
                res += "<br>";
        }

        res += "</html>";
        
        return res;
    }
}
