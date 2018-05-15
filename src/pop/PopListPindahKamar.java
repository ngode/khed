package pop;

import fungsi.GQuery;
import fungsi.WarnaTable;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * Kelas untuk melihat list pindah kamar pasien inap
 */
public class PopListPindahKamar extends javax.swing.JDialog
{
    private DefaultTableModel dtmKamar;
    
    public PopListPindahKamar(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        setSize(600, 400);
        
        Object[] cols = 
        {
            "Kamar", "Tgl Masuk", "Tgl Keluar", "Status"
        };
        
        int[] widths = 
        {
            150, 140, 140, 100
        };
        
        dtmKamar = new DefaultTableModel(null, cols)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        
        tblKamar.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblKamar.setModel(dtmKamar);
        tblKamar.setColWidth(widths);
        tblKamar.setDefaultRenderer(Object.class, new WarnaTable());
    }
    
    public void setNoRw(String noRw)
    {
        dtmKamar.getDataVector().removeAllElements();
        
        new GQuery()
                .a("SELECT no_kamar, no_bed, nm_bangsal, tgl_masuk, jam_masuk, tgl_keluar, jam_keluar, stts_pulang")
                .a("FROM kamar_inap")
                .a("JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                .a("JOIN bangsal ON bangsal.kd_bangsal = kamar.kd_bangsal")
                .a("WHERE no_rawat = {no_rawat}")
                .a("ORDER BY tgl_masuk, jam_masuk")
                .set("no_rawat", noRw)
                .selectComplete()
                .forEach(r ->
                {
                    dtmKamar.addRow(new Object[]
                    {
                        r.getString("no_kamar") + "-" + r.getString("no_bed") + " (" + r.getString("nm_bangsal") + ")",
                        r.getString("tgl_masuk") + " " + r.getString("jam_masuk"),
                        r.getString("tgl_keluar") == null ? " - " : r.getString("tgl_keluar") + " " + r.getString("jam_keluar"),
                        r.getString("stts_pulang").equals("-") ? "Sekarang" : r.getString("stts_pulang")
                    });
                });
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tblKamar = new widget.Table();
        panelisi1 = new widget.panelisi();
        btnKeluar = new widget.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(0, 0));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(400, 200));
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), ".:[ List Pindah Kamar ]:.", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame1.setPreferredSize(new java.awt.Dimension(500, 400));
        internalFrame1.setLayout(new java.awt.BorderLayout());

        Scroll.setToolTipText("");
        Scroll.setOpaque(true);

        tblKamar.setToolTipText("");
        Scroll.setViewportView(tblKamar);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        panelisi1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        btnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        btnKeluar.setMnemonic('K');
        btnKeluar.setText("Keluar");
        btnKeluar.setToolTipText("Alt+K");
        btnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        btnKeluar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnKeluarActionPerformed(evt);
            }
        });
        panelisi1.add(btnKeluar);

        internalFrame1.add(panelisi1, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnKeluarActionPerformed
    {//GEN-HEADEREND:event_btnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(PopListPindahKamar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(PopListPindahKamar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(PopListPindahKamar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(PopListPindahKamar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                PopListPindahKamar dialog = new PopListPindahKamar(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter()
                {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e)
                    {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.ScrollPane Scroll;
    private widget.Button btnKeluar;
    private widget.InternalFrame internalFrame1;
    private widget.panelisi panelisi1;
    private widget.Table tblKamar;
    // End of variables declaration//GEN-END:variables
}
