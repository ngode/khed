/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simrskhanza.cari;

import fungsi.GQuery;
import fungsi.GResult;
import java.awt.Frame;

/**
 *
 * @author GrT
 */
public class DlgCariGroupTindRanap extends DlgCari
{

    public DlgCariGroupTindRanap(Frame parent, boolean modal)
    {
        super(parent, modal);
    }

    @Override
    protected Object[] getColumns()
    {
        return new Object[]
        {
            "Kd Group", "Nama Group"
        };
    }

    @Override
    protected int[] getColumnSizes()
    {
        return new int[]
        {
            100, 300        
        };
    }

    @Override
    protected GResult getData(String dari, String sampai, String key)
    {
        return new GQuery()
                .a("SELECT * FROM hrj_group_ranap WHERE nm_group LIKE {s}")
                .setLike("s", key)
                .selectComplete();
    }
    
}
