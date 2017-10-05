
package widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import util.GColors;
import util.RoundBorder;

/**
 *
 * @author usu
 */
public class TextBox extends JTextField {
    public TextBox() {
        super();
        setFont(new java.awt.Font("Tahoma", 0, 11));        
        setSelectionColor(new Color(50,51,0));
        setSelectedTextColor(new Color(255,255,0));
        setForeground(new Color(60,80,50));
        setBackground(GColors.Biru10);// new Color(250,255,245));
        setHorizontalAlignment(LEFT);
        setSize(WIDTH,23);
    }

    @Override
    public void setEditable(boolean b)
    {
        super.setEditable(b);
        
        if (b)
            setBackground(GColors.Biru10);
        else
            setBackground(GColors.Biru15);
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        if (!isOpaque() && getBorder() instanceof RoundBorder)
        {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(getBackground());
            g2.fill(((RoundBorder) getBorder()).getBorderShape(
                    0, 0, getWidth() - 1, getHeight() - 1));
            g2.dispose();
        }
        super.paintComponent(g);
    }

    @Override
    public void updateUI()
    {
        super.updateUI();
        setOpaque(false);
        setBorder(new RoundBorder());
    }
    
    public void addDocumentListener(DocumentListener listener)
    {
        getDocument().addDocumentListener(listener);
    }
}
