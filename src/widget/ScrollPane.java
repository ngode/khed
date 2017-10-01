
package widget;

import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import usu.widget.glass.ViewPortGlass;
import util.GColors;

/**
 *
 * @author usu
 */
public class ScrollPane extends JScrollPane {


    public ScrollPane() {
        super();
        setViewport(new ViewPortGlass());
        setOpaque(false);
        //setBorder(new LineBorder(new Color(235,140,235)));
        //setBackground(new Color(255,235,255));
        setBorder(new LineBorder(GColors.BiruAgakMuda));// new Color(237,242,232)));new Color(237,242,232)));
        setBackground(GColors.BiruSangatMuda);// new Color(255,255,255));
    }
}
