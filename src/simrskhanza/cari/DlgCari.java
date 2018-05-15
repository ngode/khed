/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simrskhanza.cari;

import fungsi.GResult;
import fungsi.GRow;
import fungsi.WarnaTable;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public abstract class DlgCari extends javax.swing.JDialog
{
    public boolean RESULT = false;
    public String[] DATA;
    
    private DefaultTableModel model;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    public DlgCari(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        
        model = new DefaultTableModel(null, getColumns())
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        
        table.setModel(model);
        table.setPreferredScrollableViewportSize(new Dimension(800,800));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setDefaultRenderer(Object.class, new WarnaTable());
        
        int[] sizes = getColumnSizes();
        
        if (sizes != null)
        {
            for (int a = 0; a < model.getColumnCount(); a++)
            {
                TableColumn c = table.getColumnModel().getColumn(a);

                if (sizes[a] == 0)
                {
                    c.setMinWidth(0);
                    c.setMaxWidth(0);
                }
                else
                {
                    c.setPreferredWidth(sizes[a]);
                }
            }
        }
        
        refreshTable();
        
        txtCari.addTextChangedListener((t) -> refreshTable());
    }

    private void refreshTable()
    {
        model.getDataVector().removeAllElements();
        
        for (GRow r : getData(sdf.format(dtpDari.getDate()), sdf.format(dtpSampai.getDate()), txtCari.getText()))
        {
            model.addRow(r.toObjectArray());
        }
        
        model.fireTableDataChanged();
    }
    
    private void selectData()
    {
        if (table.getSelectedRow() != -1)
        {
            int r = table.getSelectedRow();
            
            DATA = new String[model.getColumnCount()];
            
            for (int a = 0; a < DATA.length; a++)
            {
                DATA[a] = table.getValueAt(r, a).toString();
            }
            
            RESULT = true;
            
            dispose();
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        table = new widget.Table();
        jPanel2 = new javax.swing.JPanel();
        panelGlass7 = new widget.panelisi();
        jLabel15 = new widget.Label();
        dtpDari = new widget.Tanggal();
        jLabel17 = new widget.Label();
        dtpSampai = new widget.Tanggal();
        jLabel6 = new widget.Label();
        txtCari = new widget.TextBox();
        btnCari = new widget.Button();
        btnCari1 = new widget.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "::[ Cari ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout());

        Scroll.setToolTipText("Klik data di table, kemudian klik kanan untuk memilih menu yang diinginkan");
        Scroll.setOpaque(true);

        table.setAutoCreateRowSorter(true);
        table.setToolTipText("Klik data di table, kemudian klik kanan untuk memilih menu yang diinginkan");
        table.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tableMouseClicked(evt);
            }
        });
        table.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tableKeyPressed(evt);
            }
        });
        Scroll.setViewportView(table);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass7.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel15.setText("Periode :");
        jLabel15.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass7.add(jLabel15);

        dtpDari.setEditable(false);
        dtpDari.setForeground(new java.awt.Color(50, 70, 50));
        dtpDari.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10-05-2018" }));
        dtpDari.setDisplayFormat("dd-MM-yyyy");
        dtpDari.setOpaque(false);
        dtpDari.setPreferredSize(new java.awt.Dimension(133, 23));
        panelGlass7.add(dtpDari);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("s.d");
        jLabel17.setPreferredSize(new java.awt.Dimension(24, 23));
        panelGlass7.add(jLabel17);

        dtpSampai.setEditable(false);
        dtpSampai.setForeground(new java.awt.Color(50, 70, 50));
        dtpSampai.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10-05-2018" }));
        dtpSampai.setDisplayFormat("dd-MM-yyyy");
        dtpSampai.setOpaque(false);
        dtpSampai.setPreferredSize(new java.awt.Dimension(133, 23));
        panelGlass7.add(dtpSampai);

        jLabel6.setText("Key Word :");
        jLabel6.setPreferredSize(new java.awt.Dimension(158, 23));
        panelGlass7.add(jLabel6);

        txtCari.setPreferredSize(new java.awt.Dimension(200, 23));
        panelGlass7.add(txtCari);

        btnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        btnCari.setMnemonic('7');
        btnCari.setToolTipText("Alt+7");
        btnCari.setPreferredSize(new java.awt.Dimension(28, 23));
        btnCari.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCariActionPerformed(evt);
            }
        });
        panelGlass7.add(btnCari);

        btnCari1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        btnCari1.setMnemonic('7');
        btnCari1.setToolTipText("Alt+7");
        btnCari1.setPreferredSize(new java.awt.Dimension(28, 23));
        btnCari1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCari1ActionPerformed(evt);
            }
        });
        panelGlass7.add(btnCari1);

        jPanel2.add(panelGlass7, java.awt.BorderLayout.CENTER);

        internalFrame1.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tableMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tableMouseClicked
    {//GEN-HEADEREND:event_tableMouseClicked
        if (evt.getClickCount() == 2)
        {
            selectData();
        }
    }//GEN-LAST:event_tableMouseClicked

    private void tableKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_tableKeyPressed
    {//GEN-HEADEREND:event_tableKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            selectData();
        }
    }//GEN-LAST:event_tableKeyPressed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCariActionPerformed
    {//GEN-HEADEREND:event_btnCariActionPerformed
        refreshTable();
    }//GEN-LAST:event_btnCariActionPerformed

    private void btnCari1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCari1ActionPerformed
    {//GEN-HEADEREND:event_btnCari1ActionPerformed
        dispose();
    }//GEN-LAST:event_btnCari1ActionPerformed

    protected abstract Object[] getColumns();
    protected abstract int[] getColumnSizes();
    protected abstract GResult getData(String dari, String sampai, String key);
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.ScrollPane Scroll;
    private widget.Button btnCari;
    private widget.Button btnCari1;
    private widget.Tanggal dtpDari;
    private widget.Tanggal dtpSampai;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel15;
    private widget.Label jLabel17;
    private widget.Label jLabel6;
    private javax.swing.JPanel jPanel2;
    private widget.panelisi panelGlass7;
    private widget.Table table;
    private widget.TextBox txtCari;
    // End of variables declaration//GEN-END:variables
}
