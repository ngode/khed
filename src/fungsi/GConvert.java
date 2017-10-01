/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fungsi;

/**
 *
 * @author GrT
 */
public class GConvert
{
    public static int parseInt(String s)
    {
        int res = 0;
        
        try
        {
            res = Integer.parseInt(s);
        }
        catch (NumberFormatException e)
        {
        
        }
        
        return res;
    }
}
