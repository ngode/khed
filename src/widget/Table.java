package widget;

import java.awt.Color;
import javax.swing.JTable;
import util.GColors;

/**
 *
 * @author usu
 */
public class Table extends JTable {

    /*
     * Serial version UID
     */
    private static final long serialVersionUID = 1L;

    public Table() {
        super();
        //setBackground(new Color(255,235,255));
        //setGridColor(new Color(245,170,245));
        //setForeground(new Color(90,90,90));
        super.setBackground(new Color(255,255,255));
        super.setGridColor(new Color(237,242,232));
        super.setForeground(new Color(60,80,50));
        super.setFont(new java.awt.Font("Tahoma", 0, 13));
        super.setRowHeight(25);
        super.setSelectionBackground(new Color(255,255,255));
        super.setSelectionForeground(new Color(100,100,0));
        super.getTableHeader().setForeground(new Color(60,80,50));
        super.getTableHeader().setBackground(GColors.Biru40);// new Color(248,253,243));
        super.getTableHeader().setBorder(javax.swing.BorderFactory.createLineBorder(GColors.Biru15));// new Color(237,242,232)));new Color(248,253,243)));
        super.getTableHeader().setFont(new java.awt.Font("Tahoma", 0, 11));
        super.getTableHeader().setReorderingAllowed(false);
    }
}
