package widget;

import interfaces.TextChangedListener;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import util.GColors;
import util.RoundBorder;

/**
 *
 * @author usu
 */
public class TextBox extends JTextField
{
    private List<TextChangedListener> textChangedListeners = new ArrayList<>();
    
    private String lastText;
        
    public TextBox()
    {
        super();
        
        setFont(new java.awt.Font("Tahoma", 0, 11));
        setSelectionColor(new Color(50, 51, 0));
        setSelectedTextColor(new Color(255, 255, 0));
        setForeground(new Color(60, 80, 50));
        setBackground(GColors.Biru10);// new Color(250,255,245));
        setHorizontalAlignment(LEFT);
        setSize(WIDTH, 23);
        
        addDocumentListener();
    }
    
    @Override
    public void setEditable(boolean b)
    {
        super.setEditable(b);

        if (b)
        {
            setBackground(GColors.Biru10);
        }
        else
        {
            setBackground(GColors.Biru15);
        }
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

    public void addTextChangedListener(TextChangedListener listener)
    {
        textChangedListeners.add(listener);
    }
    
    public void removeTextChangedListener(TextChangedListener listener)
    {
        textChangedListeners.remove(listener);
    }
    
    // pirvate ===============
    private void addDocumentListener()
    {
        getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                onTextChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                onTextChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                onTextChanged();
            }
        });
    }
    
    private void onTextChanged()
    {
        if (!getText().equals(lastText))
        {
            for (TextChangedListener l : textChangedListeners)
            {
                l.onTextChanged(this);
            }
        }
        
        lastText = getText();
    }
}
