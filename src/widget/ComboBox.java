/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package widget; 

import java.awt.Color;
import javax.swing.JComboBox;
import util.GColors;

/**
 *
 * @author dosen3
 */
public final class ComboBox extends JComboBox {

    public ComboBox(){
        setFont(new java.awt.Font("Tahoma", 0, 11));
        super.setBackground(GColors.BiruAgakMuda);// new Color(248,253,243));
        setForeground(new Color(60,80,50));
        setSize(WIDTH,23);
    } 

    @Override
    public void setBackground(Color bg)
    {
        //super.setBackground(bg);
    }
}
