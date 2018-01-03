/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fungsi;

import java.util.ArrayList;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author GrT
 */
public class GResult extends ArrayList<GRow> implements JRDataSource
{
    int cur = -1;
    
    @Override
    public boolean next() throws JRException
    {
        if (cur < size() - 1)
        {
            cur++;
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public Object getFieldValue(JRField jrf) throws JRException
    {
        if (cur >= size())
        {
            throw new RuntimeException("Index kebesaran di GResult");
        }
        else
        {
            return get(cur).get(jrf.getName());
        }
    }
    
}
