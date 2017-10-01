/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package widget;

import java.awt.Color;
import javax.swing.JPanel;
import util.GColors;

/**
 *
 * @author dosen
 */
public class panelGlass extends JPanel {

    public panelGlass() {
        super();
        setBackground(GColors.BiruAgakMuda);// new Color(250,255,245));
        this.setForeground(new Color(110,120,100));
        this.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(237,242,232)));
    }

}
