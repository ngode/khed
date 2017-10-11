/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class GMessage 
{
    public static void i(String title, String msg)
    {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void w(String title, String msg)
    {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.WARNING_MESSAGE);
    }
    
    public static void e(String title, String msg)
    {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
    }
    
    public static boolean q(String title, String msg)
    {
        int i = JOptionPane.showConfirmDialog(null,msg, title, JOptionPane.YES_NO_OPTION);
        
        return i == JOptionPane.YES_OPTION;
    }
}
