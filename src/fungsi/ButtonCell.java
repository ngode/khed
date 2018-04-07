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
public class ButtonCell
{
    private String text;
    private boolean enabled;
        
    public ButtonCell(String text)
    {
        this(text, true);
    }
        
    public ButtonCell(String text, boolean enabled)
    {
        this.text = text;
        this.enabled = enabled;
    }

    public String getText()
    {
        return text;
    }
    
    public boolean isEnabled()
    {
        return enabled;
    }

    @Override
    public String toString()
    {
        return text;
    }
}
