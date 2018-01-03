/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * DlgLhtBiaya.java
 *
 * Created on 12 Jul 10, 16:21:34
 */
package laporan;

import fungsi.GStatement;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.var;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import simrskhanza.DlgPenanggungJawab;

/**
 *
 * @author perpustakaan
 */
public final class DlgRl38 extends javax.swing.JDialog
{
    // CHILD -----
    private DlgPenanggungJawab dlgPenanggungJawab = new DlgPenanggungJawab(null,false);
    
    private final DefaultTableModel mdlList;
    private Connection koneksi = koneksiDB.condb();
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private GStatement psMain, psDetail;
    private ResultSet rstindakan, rstindakan2;
    private int i = 0, a = 0, ttl = 0;

    /**
     * Creates new form DlgLhtBiaya
     *
     * @param parent
     * @param modal
     */
    public DlgRl38(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        this.setLocation(8, 1);
        setSize(885, 674);

        Object[] rowRwJlDr =
        {
            "No.", "Jenis Kegiatan", "Jumlah"
        };
        
        mdlList = new DefaultTableModel(null, rowRwJlDr)
        {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                return false;
            }
        };
        
        tblList.setModel(mdlList);
        //tbBangsal.setDefaultRenderer(Object.class, new WarnaTable(jPanel2.getBackground(),tbBangsal.getBackground()));
        tblList.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tblList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 3; i++)
        {
            TableColumn column = tblList.getColumnModel().getColumn(i);
            if (i == 0)
            {
                column.setPreferredWidth(35);
            }
            else if (i == 1)
            {
                column.setPreferredWidth(400);
            }
            else
            {
                column.setPreferredWidth(60);
            }
        }
        tblList.setDefaultRenderer(Object.class, new WarnaTable());

        psMain = new GStatement(koneksi)
                .a("SELECT nm_perawatan, COUNT(*) AS jml, detail_periksa_lab.kd_jenis_prw")
                .a("FROM detail_periksa_lab")
                .a("JOIN jns_perawatan_lab ON jns_perawatan_lab.kd_jenis_prw = detail_periksa_lab.kd_jenis_prw")
                .a("JOIN periksa_lab ON periksa_lab.id_periksa = detail_periksa_lab.id_periksa")
                .a("JOIN reg_periksa ON reg_periksa.no_rawat = periksa_lab.no_rawat")
                .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                .a("WHERE tgl_periksa BETWEEN {tgl1} AND {tgl2} AND nm_perawatan LIKE {cari}")
                .a("AND reg_periksa.kd_pj LIKE {kd_pj} AND kategori_pasien LIKE {kategori_pasien}")
                .a("GROUP BY detail_periksa_lab.kd_jenis_prw");
        
        psDetail = new GStatement(koneksi)
                .a("SELECT template_laboratorium.pemeriksaan, COUNT(*) AS jml")
                .a("FROM detail_periksa_lab_2")
                .a("JOIN template_laboratorium ON template_laboratorium.id_template = detail_periksa_lab_2.id_template")
                .a("JOIN detail_periksa_lab ON detail_periksa_lab.id_detail = id_detail_1")
                .a("JOIN periksa_lab ON periksa_lab.id_periksa = detail_periksa_lab.id_periksa")
                .a("JOIN reg_periksa ON reg_periksa.no_rawat = periksa_lab.no_rawat")
                .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                .a("WHERE tgl_periksa BETWEEN {tgl1} AND {tgl2} AND detail_periksa_lab.kd_jenis_prw = {kd_jenis_prw}")
                .a("AND pemeriksaan LIKE {cari}")
                .a("AND reg_periksa.kd_pj LIKE {kd_pj} AND kategori_pasien LIKE {kategori_pasien}")
                .a("GROUP BY template_laboratorium.pemeriksaan");
        
        dlgPenanggungJawab.addWindowListener(new WindowListener()
        {
            @Override
            public void windowOpened(WindowEvent e)
            {
                
            }

            @Override
            public void windowClosing(WindowEvent e)
            {
                
            }

            @Override
            public void windowClosed(WindowEvent e)
            {
                JTable tbl = dlgPenanggungJawab.getTable();
                
                if (tbl.getSelectedRow() != -1)
                {
                    txtKdBayar.setText(tbl.getValueAt(tbl.getSelectedRow(), 1).toString());
                    txtNmBayar.setText(tbl.getValueAt(tbl.getSelectedRow(), 2).toString());
                }
            }

            @Override
            public void windowIconified(WindowEvent e)
            {
                
            }

            @Override
            public void windowDeiconified(WindowEvent e)
            {
                
            }

            @Override
            public void windowActivated(WindowEvent e)
            {
                
            }

            @Override
            public void windowDeactivated(WindowEvent e)
            {
                
            }
        });
        
        if (koneksiDB.cariCepat().equals("aktif"))
        {
            txtCari.addTextChangedListener((t) -> tampil());
            txtKdBayar.addTextChangedListener((t) -> tampil());
            
            cmbKategoriPasien.addActionListener((e) -> tampil());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tblList = new widget.Table();
        internalFrame2 = new widget.InternalFrame();
        panelGlass6 = new widget.panelisi();
        btnClearBayar = new widget.Button();
        txtNmBayar = new widget.TextBox();
        txtKdBayar = new widget.TextBox();
        jLabel18 = new widget.Label();
        cmbKategoriPasien = new widget.ComboBox();
        jLabel19 = new widget.Label();
        btnBayar = new widget.Button();
        panelGlass5 = new widget.panelisi();
        label11 = new widget.Label();
        Tgl1 = new widget.Tanggal();
        label18 = new widget.Label();
        Tgl2 = new widget.Tanggal();
        jLabel6 = new widget.Label();
        txtCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        jLabel7 = new widget.Label();
        BtnPrint = new widget.Button();
        BtnKeluar = new widget.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowActivated(java.awt.event.WindowEvent evt)
            {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt)
            {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ RL 3.8 Kegiatan Laboratorium ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tblList.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tblList.setName("tblList"); // NOI18N
        Scroll.setViewportView(tblList);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(new java.awt.BorderLayout());

        panelGlass6.setName("panelGlass6"); // NOI18N
        panelGlass6.setPreferredSize(new java.awt.Dimension(55, 55));
        panelGlass6.setLayout(null);

        btnClearBayar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        btnClearBayar.setMnemonic('2');
        btnClearBayar.setToolTipText("ALt+2");
        btnClearBayar.setName("btnClearBayar"); // NOI18N
        btnClearBayar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnClearBayarActionPerformed(evt);
            }
        });
        panelGlass6.add(btnClearBayar);
        btnClearBayar.setBounds(445, 10, 28, 23);

        txtNmBayar.setEditable(false);
        txtNmBayar.setName("txtNmBayar"); // NOI18N
        panelGlass6.add(txtNmBayar);
        txtNmBayar.setBounds(170, 10, 240, 23);

        txtKdBayar.setEditable(false);
        txtKdBayar.setHighlighter(null);
        txtKdBayar.setName("txtKdBayar"); // NOI18N
        panelGlass6.add(txtKdBayar);
        txtKdBayar.setBounds(95, 10, 70, 23);

        jLabel18.setText("Kategori Pasien :");
        jLabel18.setName("jLabel18"); // NOI18N
        panelGlass6.add(jLabel18);
        jLabel18.setBounds(490, 10, 100, 23);

        cmbKategoriPasien.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " -", "UMUM", "TNI AU", "TNI AD", "TNI AL", "POLRI", "PNS", "KELUARGA" }));
        cmbKategoriPasien.setName("cmbKategoriPasien"); // NOI18N
        panelGlass6.add(cmbKategoriPasien);
        cmbKategoriPasien.setBounds(600, 10, 170, 20);

        jLabel19.setText("Jenis Bayar :");
        jLabel19.setName("jLabel19"); // NOI18N
        panelGlass6.add(jLabel19);
        jLabel19.setBounds(10, 10, 80, 23);

        btnBayar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnBayar.setMnemonic('2');
        btnBayar.setToolTipText("ALt+2");
        btnBayar.setName("btnBayar"); // NOI18N
        btnBayar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnBayarActionPerformed(evt);
            }
        });
        panelGlass6.add(btnBayar);
        btnBayar.setBounds(415, 10, 28, 23);

        internalFrame2.add(panelGlass6, java.awt.BorderLayout.CENTER);

        panelGlass5.setName("panelGlass5"); // NOI18N
        panelGlass5.setPreferredSize(new java.awt.Dimension(55, 55));
        panelGlass5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        label11.setText("Tanggal :");
        label11.setName("label11"); // NOI18N
        label11.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass5.add(label11);

        Tgl1.setBackground(new java.awt.Color(245, 250, 240));
        Tgl1.setEditable(false);
        Tgl1.setDisplayFormat("dd-MM-yyyy");
        Tgl1.setName("Tgl1"); // NOI18N
        Tgl1.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass5.add(Tgl1);

        label18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label18.setText("s.d.");
        label18.setName("label18"); // NOI18N
        label18.setPreferredSize(new java.awt.Dimension(25, 23));
        panelGlass5.add(label18);

        Tgl2.setBackground(new java.awt.Color(245, 250, 240));
        Tgl2.setEditable(false);
        Tgl2.setDisplayFormat("dd-MM-yyyy");
        Tgl2.setName("Tgl2"); // NOI18N
        Tgl2.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass5.add(Tgl2);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass5.add(jLabel6);

        txtCari.setName("txtCari"); // NOI18N
        txtCari.setPreferredSize(new java.awt.Dimension(155, 23));
        txtCari.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCariKeyPressed(evt);
            }
        });
        panelGlass5.add(txtCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('2');
        BtnCari.setToolTipText("Alt+2");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnCariActionPerformed(evt);
            }
        });
        BtnCari.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                BtnCariKeyPressed(evt);
            }
        });
        panelGlass5.add(BtnCari);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnAllActionPerformed(evt);
            }
        });
        BtnAll.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                BtnAllKeyPressed(evt);
            }
        });
        panelGlass5.add(BtnAll);

        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(30, 23));
        panelGlass5.add(jLabel7);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("Cetak");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnPrintActionPerformed(evt);
            }
        });
        BtnPrint.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                BtnPrintKeyPressed(evt);
            }
        });
        panelGlass5.add(BtnPrint);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelGlass5.add(BtnKeluar);

        internalFrame2.add(panelGlass5, java.awt.BorderLayout.PAGE_END);

        internalFrame1.add(internalFrame2, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (mdlList.getRowCount() == 0)
        {
            JOptionPane.showMessageDialog(null, "Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            //TCari.requestFocus();
        }
        else if (mdlList.getRowCount() != 0)
        {
            Sequel.AutoComitFalse();
            Map<String, Object> param = new HashMap<>();
            param.put("namars", var.getnamars());
            param.put("alamatrs", var.getalamatrs());
            param.put("kotars", var.getkabupatenrs());
            param.put("propinsirs", var.getpropinsirs());
            param.put("kontakrs", var.getkontakrs());
            param.put("emailrs", var.getemailrs());
            param.put("periode", Tgl1.getSelectedItem() + " s.d. " + Tgl2.getSelectedItem());
            param.put("tanggal", Tgl2.getDate());
            param.put("logo", Sequel.cariGambar("select logo from setting"));
            Sequel.queryu("delete from temporary");
            for (int r = 0; r < mdlList.getRowCount(); r++)
            {
                if (!tblList.getValueAt(r, 0).toString().contains(">>"))
                {
                    Sequel.menyimpan("temporary", "'0','"
                            + mdlList.getValueAt(r, 0).toString() + "','"
                            + mdlList.getValueAt(r, 1).toString() + "','"
                            + mdlList.getValueAt(r, 2).toString() + "','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',''", "Rekap Nota Pembayaran");
                }
            }
            Sequel.AutoComitTrue();
            Valid.MyReport("rptRl38.jrxml", "report", "::[ Formulir RL 3.8 ]::",
                    "select * from temporary order by no asc", param);
        }
        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            BtnPrintActionPerformed(null);
        }
        else
        {
            //Valid.pindah(evt, BtnHapus, BtnAll);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            dispose();
        }
        else
        {
            Valid.pindah(evt, BtnKeluar, txtCari);
        }
}//GEN-LAST:event_BtnKeluarKeyPressed

private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
    tampil();
}//GEN-LAST:event_BtnCariActionPerformed

private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_SPACE)
    {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        tampil();
        this.setCursor(Cursor.getDefaultCursor());
    }
    else
    {
        Valid.pindah(evt, txtCari, BtnPrint);
    }
}//GEN-LAST:event_BtnCariKeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        tampil();
    }//GEN-LAST:event_formWindowOpened

    private void txtCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            BtnCariActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
        {
            BtnCari.requestFocus();
        }
        else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP)
        {
            BtnKeluar.requestFocus();
        }
    }//GEN-LAST:event_txtCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        txtCari.setText("");
        txtKdBayar.setText("");
        txtNmBayar.setText("");
        cmbKategoriPasien.setSelectedIndex(0);
        
        tampil();
    }//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            BtnAllActionPerformed(null);
        }
        else
        {

        }
    }//GEN-LAST:event_BtnAllKeyPressed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        
    }//GEN-LAST:event_formWindowActivated

    private void btnClearBayarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnClearBayarActionPerformed
    {//GEN-HEADEREND:event_btnClearBayarActionPerformed
        txtKdBayar.setText("");
        txtNmBayar.setText("");
    }//GEN-LAST:event_btnClearBayarActionPerformed

    private void btnBayarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnBayarActionPerformed
    {//GEN-HEADEREND:event_btnBayarActionPerformed
        dlgPenanggungJawab.onCari();
        dlgPenanggungJawab.isCek();
        dlgPenanggungJawab.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        dlgPenanggungJawab.setLocationRelativeTo(internalFrame1);
        dlgPenanggungJawab.setVisible(true);
    }//GEN-LAST:event_btnBayarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(() -> 
                {
                    DlgRl38 dialog = new DlgRl38(new javax.swing.JFrame(), true);
                    dialog.addWindowListener(new java.awt.event.WindowAdapter()
                    {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e)
                        {
                            System.exit(0);
                        }
                    });
                    dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.Button BtnAll;
    private widget.Button BtnCari;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.ScrollPane Scroll;
    private widget.Tanggal Tgl1;
    private widget.Tanggal Tgl2;
    private widget.Button btnBayar;
    private widget.Button btnClearBayar;
    private widget.ComboBox cmbKategoriPasien;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label label11;
    private widget.Label label18;
    private widget.panelisi panelGlass5;
    private widget.panelisi panelGlass6;
    private widget.Table tblList;
    private widget.TextBox txtCari;
    private widget.TextBox txtKdBayar;
    private widget.TextBox txtNmBayar;
    // End of variables declaration//GEN-END:variables

    public void tampil()
    {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        try
        {
            
            Valid.tabelKosong(mdlList);
            
            psMain.setString("tgl1", Valid.SetTgl(Tgl1.getSelectedItem() + ""));
            psMain.setString("tgl2", Valid.SetTgl(Tgl2.getSelectedItem() + ""));
            psMain.setString("cari", "%" + txtCari.getText().trim() + "%");
            psMain.setString("kd_pj", "%" + txtKdBayar.getText() + "%");
            psMain.setString("kategori_pasien",  "%" + (cmbKategoriPasien.getSelectedIndex() == 0 ? "" : cmbKategoriPasien.getSelectedItem().toString()) + "%");
            
            rstindakan = psMain.executeQuery();
            
            i = 1;
            ttl = 0;
            
            while (rstindakan.next())
            {
                mdlList.addRow(new Object[]
                {
                    i, rstindakan.getString(1), rstindakan.getInt(2)
                });
                
                psDetail.setString("tgl1", Valid.SetTgl(Tgl1.getSelectedItem() + ""));
                psDetail.setString("tgl2", Valid.SetTgl(Tgl2.getSelectedItem() + ""));
                psDetail.setString("cari", "%" + txtCari.getText().trim() + "%");
                psDetail.setString("kd_jenis_prw", rstindakan.getString(3));
                psDetail.setString("kd_pj", "%" + txtKdBayar.getText() + "%");
                psDetail.setString("kategori_pasien", "%" + (cmbKategoriPasien.getSelectedIndex() == 0 ? "" : cmbKategoriPasien.getSelectedItem().toString()) + "%");
            
                rstindakan2 = psDetail.executeQuery();
                
                a = 1;
                
                while (rstindakan2.next())
                {
                    mdlList.addRow(new Object[]
                    {
                        i + "." + a, "   - " + rstindakan2.getString(1), rstindakan2.getInt(2)
                    });
                    
                    ttl = ttl + rstindakan2.getInt(2);
                    a++;
                }
                
                ttl = ttl + rstindakan.getInt(2);
                i++;
            }

            if (i > 1)
            {
                mdlList.addRow(new Object[]
                {
                    "", "TOTAL", ttl
                });
            }
        }
        catch (Exception e)
        {
            System.out.println("Notifikasi : " + e);
        }
        
        this.setCursor(Cursor.getDefaultCursor());
    }

}
