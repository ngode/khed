/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.FileInputStream;
import java.util.Properties;

/**
 *
 * @author GrT
 */
public class XmlSettings
{
    private static Properties prop;
    
    static
    {
        prop = new Properties();
        
        try
        {
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
        } 
        catch (Exception ex) 
        {
            System.out.println(ex.getMessage());
        }
    }
    
    public static String getProp(String key)
    {
        return prop.getProperty(key);
    }
}
