package simrskhanza;

import fungsi.GQuery;
import fungsi.GResult;
import fungsi.GRow;
import fungsi.WarnaTableColored;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.var;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import util.ColSizePair;
import util.ColorUtil;
import util.GMessage;

/**
 *
 * @author dosen
 */
public final class DlgMonitoringRm extends javax.swing.JDialog
{
    // Nama dialog ini
    private static final String NAME = "DlgMonitoringRm";
    
    // Delay timer auto refresh
    private static final int delaySec = 20;
    
    // Warna2 di table
    private static final Color colorAwal = ColorUtil.hex2Rgb("#8D0E07");
    private static final Color colorKeluar = ColorUtil.hex2Rgb("#000000");
    private static final Color colorKembali = ColorUtil.hex2Rgb("#0F6013");
    private static final Color colorTidakAda = ColorUtil.hex2Rgb("#5C218D");
    
    // Kolom kolom yang ada di table
    private static final int C_NOREG = 0;
    private static final int C_NORM = 1;
    private static final int C_NAMAPASIEN = 2;
    private static final int C_TGLREG = 3;
    private static final int C_TGLKELUAR = 4;
    private static final int C_LAMA = 5;
    private static final int C_KUNJ = 6;
    private static final int C_NOURUT = 7;
    private static final int C_PRINT = 8;
    private static final int C_STATUSBERKAS = 9;
    private static final int C_RRAWAT = 10;
    private static final int C_NAMAPOLI = 11;
    private static final int C_NAMADOKTER = 12;
    private static final int C_CBAYAR = 13;
    
    private final DefaultTableModel tableModel;
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private Connection koneksi = koneksiDB.condb();
    
    // Dialogs =======
    private final DlgCariPoli dlgCariPoli = new DlgCariPoli(null, false);
    private final DlgPilihanCetakDokumen dlgCetak = new DlgPilihanCetakDokumen(null,false);

    /**
     * Creates new form DlgReg
     *
     * @param parent
     * @param modal
     */
    public DlgMonitoringRm(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();

        this.setLocation(8, 1);
        setSize(885, 674);

        // Kelas untuk menyimpan kolom dan size nya
        ColSizePair cols = new ColSizePair();
        cols.add("No Reg", 120);
        cols.add("No RM", 90);
        cols.add("Nama Pasien", 150);
        cols.add("Tgl Reg", 130);
        cols.add("Tgl Keluar", 130);
        cols.add("Lama", 90);
        cols.add("Kunj", 90);
        cols.add("No Urut", 70);
        cols.add("Print", 70);
        cols.add("Status Berkas", 100);
        cols.add("R Rawat", 100);
        cols.add("Nama Poli", 140);
        cols.add("Nama Dokter", 150);
        cols.add("C Bayar", 100);
        
        tableModel = new DefaultTableModel(null, cols.getColArray())
        {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                return false;
            }
        };
        
        tblTable.setModel(tableModel);
        tblTable.setPreferredScrollableViewportSize(new Dimension(800, 800));
        tblTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int a = 0; a < cols.getCount(); a++)
        {
            TableColumn column = tblTable.getColumnModel().getColumn(a);
            column.setPreferredWidth(cols.getSize(a));            
        }
        
        tblTable.setDefaultRenderer(Object.class, new WarnaTableColored()
        {
            @Override
            public Color getForegroundColor(int row, int col)
            {
                String st = tblTable.getValueAt(row, C_STATUSBERKAS).toString();
                
                if (st.isEmpty())
                    return colorAwal;
                else if (st.equals("Sudah Dikirim"))
                    return colorKeluar;
                else if (st.equals("Sudah Kembali"))
                    return colorKembali;
                else if (st.equals("Tidak Ada"))
                    return colorTidakAda;
                else
                    return colorAwal;
            }
        });
        
        TCari.setDocument(new batasInput((byte) 100).getKata(TCari));
        txtPoli.setDocument(new batasInput((byte) 100).getKata(txtPoli));
        
        if (koneksiDB.cariCepat().equals("aktif"))
        {
            TCari.addTextChangedListener(t -> tampil());
        }
        
        dlgCariPoli.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosed(WindowEvent e)
            {
                if (var.getform().equals(NAME))
                {
                    if (dlgCariPoli.getTable().getSelectedRow() != -1)
                    {
                        // Ambil table dari dialog poli
                        JTable tbl = dlgCariPoli.getTable();
                        
                        // set text poli dari table beserta valuenya (kd polinya)
                        txtPoli.setTextWithValue(tbl.getValueAt(tbl.getSelectedRow(), 1).toString(), tbl.getValueAt(tbl.getSelectedRow(), 0).toString());
                        txtPoli.requestFocus();
                    }
                }
            }
        });
        
        setAutoRefresh();
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
        tblTable = new widget.Table();
        jPanel2 = new javax.swing.JPanel();
        panelGlass6 = new widget.panelisi();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        jLabel16 = new widget.Label();
        txtPoli = new widget.TextBox();
        btnCariPoli = new widget.Button();
        jLabel10 = new widget.Label();
        LCount = new widget.Label();
        btnKeluar = new widget.Button();
        panelGlass7 = new widget.panelisi();
        btnPrint = new widget.Button();
        btnKirim = new widget.Button();
        btnTidakAda = new widget.Button();
        btnKembali = new widget.Button();
        btnReset = new widget.Button();
        panelGlass8 = new widget.panelisi();
        jLabel15 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel17 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel12 = new widget.Label();
        cmbStatus = new widget.ComboBox();
        jLabel13 = new widget.Label();
        ckbBalik = new widget.CekBox();
        PanelInput = new javax.swing.JPanel();
        panelGlass9 = new widget.panelisi();
        jLabel4 = new widget.Label();
        txtNoReg = new widget.TextBox();
        jLabel5 = new widget.Label();
        txtNoUrut = new widget.TextBox();
        jLabel7 = new widget.Label();
        txtNoRm = new widget.TextBox();
        jLabel8 = new widget.Label();
        txtNamaPasien = new widget.TextBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Monitoring Berkas RM ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setToolTipText("");
        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tblTable.setToolTipText("");
        tblTable.setName("tblTable"); // NOI18N
        tblTable.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblTableMouseClicked(evt);
            }
        });
        tblTable.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblTableKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tblTable);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass6.setName("panelGlass6"); // NOI18N
        panelGlass6.setPreferredSize(new java.awt.Dimension(55, 50));
        panelGlass6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass6.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(200, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                TCariKeyPressed(evt);
            }
        });
        panelGlass6.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('6');
        BtnCari.setToolTipText("Alt+6");
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
        panelGlass6.add(BtnCari);

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
        panelGlass6.add(BtnAll);

        jLabel16.setText("Poliklinik :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass6.add(jLabel16);

        txtPoli.setEditable(false);
        txtPoli.setName("txtPoli"); // NOI18N
        txtPoli.setPreferredSize(new java.awt.Dimension(150, 23));
        panelGlass6.add(txtPoli);

        btnCariPoli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnCariPoli.setMnemonic('5');
        btnCariPoli.setToolTipText("ALt+5");
        btnCariPoli.setName("btnCariPoli"); // NOI18N
        btnCariPoli.setPreferredSize(new java.awt.Dimension(28, 23));
        btnCariPoli.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCariPoliActionPerformed(evt);
            }
        });
        panelGlass6.add(btnCariPoli);

        jLabel10.setText("Record :");
        jLabel10.setName("jLabel10"); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(100, 30));
        panelGlass6.add(jLabel10);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(50, 30));
        panelGlass6.add(LCount);

        btnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        btnKeluar.setMnemonic('K');
        btnKeluar.setText("Keluar");
        btnKeluar.setToolTipText("Alt+K");
        btnKeluar.setName("btnKeluar"); // NOI18N
        btnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        btnKeluar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnKeluarActionPerformed(evt);
            }
        });
        btnKeluar.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnKeluarKeyPressed(evt);
            }
        });
        panelGlass6.add(btnKeluar);

        jPanel2.add(panelGlass6, java.awt.BorderLayout.PAGE_END);

        panelGlass7.setName("panelGlass7"); // NOI18N
        panelGlass7.setPreferredSize(new java.awt.Dimension(44, 50));
        panelGlass7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        btnPrint.setMnemonic('K');
        btnPrint.setText("Print");
        btnPrint.setToolTipText("Alt+K");
        btnPrint.setName("btnPrint"); // NOI18N
        btnPrint.setPreferredSize(new java.awt.Dimension(100, 30));
        btnPrint.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrintActionPerformed(evt);
            }
        });
        panelGlass7.add(btnPrint);

        btnKirim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/arrow-up-1.png"))); // NOI18N
        btnKirim.setMnemonic('K');
        btnKirim.setText("Kirim");
        btnKirim.setToolTipText("Alt+K");
        btnKirim.setName("btnKirim"); // NOI18N
        btnKirim.setPreferredSize(new java.awt.Dimension(100, 30));
        btnKirim.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnKirimActionPerformed(evt);
            }
        });
        panelGlass7.add(btnKirim);

        btnTidakAda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        btnTidakAda.setMnemonic('K');
        btnTidakAda.setText("Tidak Ada");
        btnTidakAda.setToolTipText("Alt+K");
        btnTidakAda.setName("btnTidakAda"); // NOI18N
        btnTidakAda.setPreferredSize(new java.awt.Dimension(110, 30));
        btnTidakAda.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTidakAdaActionPerformed(evt);
            }
        });
        panelGlass7.add(btnTidakAda);

        btnKembali.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/arrow-up-1_2.png"))); // NOI18N
        btnKembali.setMnemonic('K');
        btnKembali.setText("Kembali");
        btnKembali.setToolTipText("Alt+K");
        btnKembali.setName("btnKembali"); // NOI18N
        btnKembali.setPreferredSize(new java.awt.Dimension(100, 30));
        btnKembali.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnKembaliActionPerformed(evt);
            }
        });
        panelGlass7.add(btnKembali);

        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/system-restart-panel.png"))); // NOI18N
        btnReset.setMnemonic('K');
        btnReset.setText("Reset");
        btnReset.setToolTipText("Alt+K");
        btnReset.setName("btnReset"); // NOI18N
        btnReset.setPreferredSize(new java.awt.Dimension(100, 30));
        btnReset.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnResetActionPerformed(evt);
            }
        });
        panelGlass7.add(btnReset);

        jPanel2.add(panelGlass7, java.awt.BorderLayout.PAGE_START);

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel15.setText("Periode :");
        jLabel15.setName("jLabel15"); // NOI18N
        jLabel15.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass8.add(jLabel15);

        DTPCari1.setEditable(false);
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "02-05-2018" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(140, 23));
        DTPCari1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                DTPCari1KeyPressed(evt);
            }
        });
        panelGlass8.add(DTPCari1);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("s.d");
        jLabel17.setName("jLabel17"); // NOI18N
        jLabel17.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass8.add(jLabel17);

        DTPCari2.setEditable(false);
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "02-05-2018" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(140, 23));
        DTPCari2.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                DTPCari2KeyPressed(evt);
            }
        });
        panelGlass8.add(DTPCari2);

        jLabel12.setText("Status :");
        jLabel12.setName("jLabel12"); // NOI18N
        jLabel12.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass8.add(jLabel12);

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Semua", "Sudah Dikirim", "Sudah Kembali", "Tidak Ada" }));
        cmbStatus.setName("cmbStatus"); // NOI18N
        cmbStatus.setOpaque(false);
        cmbStatus.setPreferredSize(new java.awt.Dimension(140, 23));
        cmbStatus.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                cmbStatusItemStateChanged(evt);
            }
        });
        panelGlass8.add(cmbStatus);

        jLabel13.setName("jLabel13"); // NOI18N
        jLabel13.setPreferredSize(new java.awt.Dimension(50, 23));
        panelGlass8.add(jLabel13);

        ckbBalik.setText("Balik Urutan");
        ckbBalik.setName("ckbBalik"); // NOI18N
        ckbBalik.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ckbBalikActionPerformed(evt);
            }
        });
        panelGlass8.add(ckbBalik);

        jPanel2.add(panelGlass8, java.awt.BorderLayout.CENTER);

        internalFrame1.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));
        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel4.setText("No. Reg:");
        jLabel4.setName("jLabel4"); // NOI18N
        jLabel4.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(jLabel4);

        txtNoReg.setEditable(false);
        txtNoReg.setHighlighter(null);
        txtNoReg.setName("txtNoReg"); // NOI18N
        txtNoReg.setPreferredSize(new java.awt.Dimension(140, 23));
        panelGlass9.add(txtNoReg);

        jLabel5.setText("No. Urut :");
        jLabel5.setName("jLabel5"); // NOI18N
        jLabel5.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(jLabel5);

        txtNoUrut.setEditable(false);
        txtNoUrut.setHighlighter(null);
        txtNoUrut.setName("txtNoUrut"); // NOI18N
        txtNoUrut.setPreferredSize(new java.awt.Dimension(50, 23));
        txtNoUrut.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtNoUrutKeyPressed(evt);
            }
        });
        panelGlass9.add(txtNoUrut);

        jLabel7.setText("No.R.M. :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(65, 23));
        panelGlass9.add(jLabel7);

        txtNoRm.setEditable(false);
        txtNoRm.setHighlighter(null);
        txtNoRm.setName("txtNoRm"); // NOI18N
        txtNoRm.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(txtNoRm);

        jLabel8.setText("Nama Pasien :");
        jLabel8.setName("jLabel8"); // NOI18N
        jLabel8.setPreferredSize(new java.awt.Dimension(85, 23));
        panelGlass9.add(jLabel8);

        txtNamaPasien.setEditable(false);
        txtNamaPasien.setHighlighter(null);
        txtNamaPasien.setName("txtNamaPasien"); // NOI18N
        txtNamaPasien.setPreferredSize(new java.awt.Dimension(250, 23));
        panelGlass9.add(txtNamaPasien);

        internalFrame1.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_btnKeluarActionPerformed

    private void btnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnKeluarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            dispose();
        }
        else
        {
            Valid.pindah(evt, cmbStatus, TCari);
        }
}//GEN-LAST:event_btnKeluarKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        txtPoli.setEmpty();
        TCari.setEmpty();
        tampil();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            tampil();
            TCari.setText("");
        }
        else
        {
            Valid.pindah(evt, TCari, btnKeluar);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
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
            btnKeluar.requestFocus();
        }
}//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        tampil();
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            BtnCariActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void DTPCari1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPCari1KeyPressed
        // TODO add your handling code here:
}//GEN-LAST:event_DTPCari1KeyPressed

    private void DTPCari2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPCari2KeyPressed
        // TODO add your handling code here:
}//GEN-LAST:event_DTPCari2KeyPressed

    private void tblTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTableMouseClicked
        if (tableModel.getRowCount() != 0)
        {
            int r = tblTable.getSelectedRow();
            
            txtNoReg.setText(tblTable.getValueAt(r, C_NOREG).toString());
            txtNoUrut.setText(tblTable.getValueAt(r, C_NOURUT).toString());
            txtNoRm.setText(tblTable.getValueAt(r, C_NORM).toString());
            txtNamaPasien.setText(tblTable.getValueAt(r, C_NAMAPASIEN).toString());
            
            String st = tblTable.getValueAt(r, C_STATUSBERKAS).toString();
            
            if (st.isEmpty())
                setButton(true, true, true, false, false);
            else if (st.equals("Sudah Dikirim"))
                setButton(true, false, false, true, true);
            else if (st.equals("Sudah Kembali"))
                setButton(true, false, false, false, true);
            else if (st.equals("Tidak Ada"))
                setButton(true, false, false, false, true);
        }
}//GEN-LAST:event_tblTableMouseClicked

    private void tblTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblTableKeyPressed
        
}//GEN-LAST:event_tblTableKeyPressed

private void btnCariPoliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariPoliActionPerformed
    var.setform(NAME);
    dlgCariPoli.isCek();
    dlgCariPoli.setSize(internalFrame1.getWidth() - 40, internalFrame1.getHeight() - 40);
    dlgCariPoli.setLocationRelativeTo(internalFrame1);
    dlgCariPoli.setVisible(true);
}//GEN-LAST:event_btnCariPoliActionPerformed

private void cmbStatusItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbStatusItemStateChanged
    tampil();
}//GEN-LAST:event_cmbStatusItemStateChanged

    private void txtNoUrutKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoUrutKeyPressed
        
    }//GEN-LAST:event_txtNoUrutKeyPressed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPrintActionPerformed
    {//GEN-HEADEREND:event_btnPrintActionPerformed
//        dlgCetak.tampil2();
//        dlgCetak.setNoRm(rs.getString("temp1"),rs.getString("temp2"),rs.getString("temp3"),rs.getString("temp4"),
//                rs.getString("temp5"),rs.getString("temp6"),rs.getString("temp7"),rs.getString("temp8"),
//                rs.getString("temp9"),rs.getString("temp10"),rs.getString("temp11"),rs.getString("temp12"));
//        dlgCetak.setSize(500,400);
//        dlgCetak.setLocationRelativeTo(internalFrame1);
//        dlgCetak.setVisible(true);
        
        // Untuk sementara
        if (tblTable.getSelectedRowCount() == 0)
            return;
        
        boolean exist = new GQuery()
                .a("SELECT COUNT(*) FROM mutasi_berkas WHERE no_rawat = {no_rawat}")
                .set("no_rawat", txtNoReg.getText())
                .getInt() > 0;
        
        boolean suc;
        
        if (!exist)
        {
            suc = new GQuery()
                    .a("INSERT INTO mutasi_berkas VALUES ({no_rawat}, {status}, {print}, NOW(), NULL, NULL, NULL)")
                    .set("no_rawat", txtNoReg.getText())
                    .set("status", "")
                    .set("print", "1")
                    .write();
        }
        else
        {
            suc = new GQuery()
                    .a("UPDATE mutasi_berkas SET print = '1' WHERE no_rawat = {no_rawat}")
                    .set("no_rawat", txtNoReg.getText())
                    .write();
        }
        
        if (suc) 
            tampil();
        else 
            GMessage.e("Error", "Error saat menyimpan data");
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnKirimActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnKirimActionPerformed
    {//GEN-HEADEREND:event_btnKirimActionPerformed
        if (tblTable.getSelectedRowCount() == 0)
            return;
        
        boolean exist = new GQuery()
                .a("SELECT COUNT(*) FROM mutasi_berkas WHERE no_rawat = {no_rawat}")
                .set("no_rawat", txtNoReg.getText())
                .getInt() > 0;
        
        boolean suc;
        
        if (!exist)
        {
            suc = new GQuery()
                    .a("INSERT INTO mutasi_berkas VALUES ({no_rawat}, {status}, {print}, NOW(), NULL, NULL, NULL)")
                    .set("no_rawat", txtNoReg.getText())
                    .set("status", "Sudah Dikirim")
                    .set("print", "0")
                    .write();
        }
        else
        {
            suc = new GQuery()
                    .a("UPDATE mutasi_berkas SET status = 'Sudah Dikirim', dikirim = NOW() WHERE no_rawat = {no_rawat}")
                    .set("no_rawat", txtNoReg.getText())
                    .write();
        }
        
        if (suc) 
            tampil();
        else 
            GMessage.e("Error", "Error saat menyimpan data");
    }//GEN-LAST:event_btnKirimActionPerformed

    private void btnTidakAdaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTidakAdaActionPerformed
    {//GEN-HEADEREND:event_btnTidakAdaActionPerformed
        if (tblTable.getSelectedRowCount() == 0)
            return;
        
        boolean exist = new GQuery()
                .a("SELECT COUNT(*) FROM mutasi_berkas WHERE no_rawat = {no_rawat}")
                .set("no_rawat", txtNoReg.getText())
                .getInt() > 0;
        
        boolean suc;
        
        if (!exist)
        {
            suc = new GQuery()
                    .a("INSERT INTO mutasi_berkas VALUES ({no_rawat}, {status}, {print}, NOW(), NULL, NULL, NULL)")
                    .set("no_rawat", txtNoReg.getText())
                    .set("status", "Tidak Ada")
                    .set("print", "0")
                    .write();
        }
        else
        {
            suc = new GQuery()
                    .a("UPDATE mutasi_berkas SET status = 'Tidak Ada', dikirim = NOW() WHERE no_rawat = {no_rawat}")
                    .set("no_rawat", txtNoReg.getText())
                    .write();
        }
        
        if (suc) 
            tampil();
        else 
            GMessage.e("Error", "Error saat menyimpan data");
    }//GEN-LAST:event_btnTidakAdaActionPerformed

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnKembaliActionPerformed
    {//GEN-HEADEREND:event_btnKembaliActionPerformed
        if (tblTable.getSelectedRowCount() == 0)
            return;
        
        boolean suc = new GQuery()
                .a("UPDATE mutasi_berkas SET status = 'Sudah Kembali' WHERE no_rawat = {no_rawat}")
                .set("no_rawat", txtNoReg.getText())
                .write();
        
        if (suc)
            tampil();
        else
            GMessage.e("Error", "Error saat menyimpan data");
    }//GEN-LAST:event_btnKembaliActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnResetActionPerformed
    {//GEN-HEADEREND:event_btnResetActionPerformed
        if (tblTable.getSelectedRowCount() == 0)
            return;
        
        if (GMessage.q("Reset", "Yakin mau reset?"))
        {
            boolean suc = new GQuery()
                    .a("DELETE FROM mutasi_berkas WHERE no_rawat = {no_rawat}")
                    .set("no_rawat", txtNoReg.getText())
                    .write();

            if (suc)
                tampil();
            else
                GMessage.e("Error", "Error saat menyimpan data");
        }
    }//GEN-LAST:event_btnResetActionPerformed

    private void ckbBalikActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ckbBalikActionPerformed
    {//GEN-HEADEREND:event_ckbBalikActionPerformed
        tampil();
    }//GEN-LAST:event_ckbBalikActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(() -> 
                {
                    DlgMonitoringRm dialog = new DlgMonitoringRm(new javax.swing.JFrame(), true);
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
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.Label LCount;
    private javax.swing.JPanel PanelInput;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.Button btnCariPoli;
    private widget.Button btnKeluar;
    private widget.Button btnKembali;
    private widget.Button btnKirim;
    private widget.Button btnPrint;
    private widget.Button btnReset;
    private widget.Button btnTidakAda;
    private widget.CekBox ckbBalik;
    private widget.ComboBox cmbStatus;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel4;
    private widget.Label jLabel5;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private javax.swing.JPanel jPanel2;
    private widget.panelisi panelGlass6;
    private widget.panelisi panelGlass7;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.Table tblTable;
    private widget.TextBox txtNamaPasien;
    private widget.TextBox txtNoReg;
    private widget.TextBox txtNoRm;
    private widget.TextBox txtNoUrut;
    private widget.TextBox txtPoli;
    // End of variables declaration//GEN-END:variables

    // Nge refresh table
    public void tampil()
    {
        // Ambil no reg yg sekarang dipilih, biar kalo refresh tetep kepilih
        String noReg = "";
        if (tblTable.getSelectedRowCount() > 0)
            noReg = tblTable.getValueAt(tblTable.getSelectedRow(), C_NOREG).toString();
        
        // Kosongin table nya dulu
        Valid.tabelKosong(tableModel);
        
        GQuery q = new GQuery()
                .a("SELECT reg_periksa.no_rawat AS no_reg, pasien.no_rkm_medis AS no_rm, pasien.nm_pasien, tgl_registrasi, jam_reg, IF (dikirim IS NULL, '', dikirim) AS dikirim,")
                .a("    stts_daftar, no_reg AS no_urut, IF (print = '1', 'Print', '') AS print, IF (mutasi_berkas.status IS NULL, '', mutasi_berkas.status) AS status_berkas,")
                .a("    IF (nm_bangsal IS NULL, '', nm_bangsal) AS nm_bangsal, nm_poli, nm_dokter, png_jawab AS cara_bayar")
                .a("FROM reg_periksa")
                .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                .a("LEFT JOIN mutasi_berkas ON mutasi_berkas.no_rawat = reg_periksa.no_rawat")
                .a("LEFT JOIN kamar_inap ON kamar_inap.no_rawat = reg_periksa.no_rawat AND stts_pulang != 'Pindah Kamar'")
                .a("LEFT JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                .a("LEFT JOIN bangsal ON bangsal.kd_bangsal = kamar.kd_bangsal")
                .a("JOIN poliklinik ON poliklinik.kd_poli = reg_periksa.kd_poli")
                .a("JOIN dokter ON dokter.kd_dokter = reg_periksa.kd_dokter")
                .a("JOIN penjab ON penjab.kd_pj = reg_periksa.kd_pj")
                .a("WHERE (reg_periksa.no_rawat LIKE {cari} OR pasien.no_rkm_medis LIKE {cari} OR no_reg LIKE {cari} OR nm_pasien LIKE {cari})")
                .a("AND tgl_registrasi BETWEEN {tgl1} AND {tgl2}")
                .setLike("cari", TCari.getText())
                .set("tgl1", Valid.SetTgl(DTPCari1.getSelectedItem().toString()))
                .set("tgl2", Valid.SetTgl(DTPCari2.getSelectedItem().toString()));
        
        // Kalo status berkasnya tidak semua
        if (cmbStatus.getSelectedIndex() > 0)
        {
            q.a("AND mutasi_berkas.status = {status_berkas}");
            q.set("status_berkas", cmbStatus.getSelectedItem().toString());
        }
        
        // Kalo ada filter poli
        if (!txtPoli.getValue().isEmpty())
        {
            q.a("AND reg_periksa.kd_poli = {kd_poli}");
            q.set("kd_poli", txtPoli.getValue());
        }
        
        String order = ckbBalik.isSelected() ? "ASC" : "DESC";
        q.a("ORDER BY tgl_registrasi {order}, jam_reg {order}");
        q.setNoQuote("order", order);
        
        GResult res = q.selectComplete();
        int rowCount = 0;
        int selRow = -1;
        
        for (GRow r : res)
        {
            tableModel.addRow(new Object[]
            {
                r.getString("no_reg"),
                r.getString("no_rm"),
                r.getString("nm_pasien"),
                r.getString("tgl_registrasi") + " " + r.getString("jam_reg"),
                r.getString("dikirim"),
                "-",
                r.getString("stts_daftar"),
                r.getString("no_urut"),
                r.getString("print"),
                r.getString("status_berkas"),
                r.getString("nm_bangsal"),
                r.getString("nm_poli"),
                r.getString("nm_dokter"),
                r.getString("cara_bayar")
            });
            
            // Kalo no reg sesuai dengan yang terakhir dipilih, set selrow
            if (noReg.equals(r.getString("no_reg"))) selRow = rowCount;
            rowCount++;
        }
        
        // Kalo ada no reg yang cocok, pilih tabelnya
        if (selRow > -1)
        {
            tblTable.setRowSelectionInterval(selRow, selRow);
            tblTableMouseClicked(null);
        }
        else
        {
            // Clear tombolnya karena gak ada yang ke select datanya
            setButton(false, false, false, false, false);
        }
        
        // Set jumlah record
        LCount.setText("" + tableModel.getRowCount());
    }
    
    private void setButton(boolean print, boolean kirim, boolean tidakAda, boolean kembali, boolean reset)
    {
        btnPrint.setVisible(print);
        btnKirim.setVisible(kirim);
        btnTidakAda.setVisible(tidakAda);
        btnKembali.setVisible(kembali);
        btnReset.setVisible(reset);
    }
    
    private void setAutoRefresh()
    {
        new Timer(delaySec * 1000, e -> tampil()).start();
    }
}
