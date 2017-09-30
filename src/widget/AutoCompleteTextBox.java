/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package widget;

import fungsi.sekuel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author GrT
 */
public class AutoCompleteTextBox extends TextBox implements DocumentListener, ActionListener, KeyListener
{
    private sekuel sql;
    
    private List<String[]> datas;
    
    boolean isUser = false;
    
    public void init(String table, String idCol, String dispCol)
    {
        sql = new sekuel();
        
        String q = "SELECT " + idCol + ", " + dispCol + " FROM " + table + " ORDER BY " + dispCol;
        datas = sql.select(q);        
        
        addDocumentListener(this);
        addActionListener(this);
        addKeyListener(this);
    }
    
    private void suggest()
    {
        if (!isUser)
            return;
        
        isUser = false;
        
        if (datas == null)
            return;
        
        String s = getText();
        
        for (String[] sa : datas)
        {
            if (sa[1].toLowerCase().startsWith(s.toLowerCase()))
            {
                SwingUtilities.invokeLater(new AutoCompletion(s, s.length(), sa[1]));
                
                System.out.println(sa[1]);
                
                break;
            }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e)
    {
        //suggest(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
        //suggest(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e)
    {
        suggest();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        //suggest();
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        if (e.getKeyChar() == '\b')
            return;
        
        isUser = true;
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        suggest();
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        suggest();
    }
    
    private class AutoCompletion implements Runnable
    {
        private String text;
        private int pos;
        private String completion;

        public AutoCompletion(String text, int pos, String completion)
        {
            this.text = text;
            this.pos = pos;
            this.completion = completion;
        }
        
        @Override
        public void run()
        {
            if (!text.equalsIgnoreCase(getText()))
                return;
            
            setText(completion);
            setCaretPosition(completion.length());
            moveCaretPosition(pos);
        }
        
    }
}
