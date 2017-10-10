/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * DlgPerawatan.java
 *
 * Created on May 23, 2010, 6:36:30 PM
 */
package simrskhanza;

import fungsi.GQuery;
import keuangan.DlgJnsPerawatanRadiologi;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.var;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import keuangan.Jurnal;
import org.smslib.Message;

/**
 *
 * @author dosen
 */
public final class DlgPeriksaHemodialisa extends javax.swing.JDialog
{
    private DefaultTableModel tabMode, tabModelList;
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private Connection koneksi = koneksiDB.condb();
    private Jurnal jur = new Jurnal();
    
    private PreparedStatement psset_tarif, pssetpj, pspemeriksaan;
    private ResultSet rs, rsset_tarif, rssetpj, rsrekening;
    private int jml = 0, i = 0, index = 0;
    private String cara_bayar_radiologi = "Yes", pilihan = "", pemeriksaan = "", kamar, namakamar, status = "";
    private double ttl = 0, item = 0;

    // LIST =========================
    private PreparedStatement psMain, psDetail;
    private ResultSet rsMain, rsDetail;
    
    // VARS ===================
    private boolean isEdit = false;
    private String kdPeriksa;
    
    /**
     * Creates new form DlgPerawatan
     *
     * @param parent
     * @param modal
     */
    public DlgPeriksaHemodialisa(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();

        initTable();
        initTableList();

        txtNoRw.setDocument(new batasInput((byte) 17).getKata(txtNoRw));
        txtKdKamar.setDocument(new batasInput((byte) 20).getKata(txtKdKamar));
        txtKdDokter.setDocument(new batasInput((byte) 20).getKata(txtKdDokter));
        
        if (koneksiDB.cariCepat().equals("aktif"))
        {
            txtCariPeriksa.getDocument().addDocumentListener(new javax.swing.event.DocumentListener()
            {
                @Override
                public void insertUpdate(DocumentEvent e)
                {
                    tampil();
                }

                @Override
                public void removeUpdate(DocumentEvent e)
                {
                    tampil();
                }

                @Override
                public void changedUpdate(DocumentEvent e)
                {
                    tampil();
                }
            });
        }

        ChkJln.setSelected(true);
        jam();
        
        jPopupMenu1.addPopupMenuListener(new PopupMenuListener()
        {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e)
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        int rowAtPoint = tblListTransaksi.rowAtPoint(SwingUtilities.convertPoint(jPopupMenu1, new Point(0, 0), tblListTransaksi));
                        
                        if (rowAtPoint > -1)
                        {
                            tblListTransaksi.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                        }
                        else
                        {
                            tblListTransaksi.clearSelection();
                            ((JPopupMenu)e.getSource()).setVisible(false);
                        }
                        
                        int row = tblListTransaksi.getSelectedRow();
                        
                        if (row > -1)
                        {
                            int rowMain = getListMainRow(row);
                            
                            if (tblListTransaksi.getValueAt(rowMain, 5).toString().equals("Belum"))
                            {
                                menuUbah.setEnabled(true);
                                menuHapus.setEnabled(true);
                            }
                            else
                            {
                                menuUbah.setEnabled(false);
                                menuHapus.setEnabled(false);
                            }
                        }
                        else
                        {
                            menuUbah.setEnabled(false);
                            menuHapus.setEnabled(false);
                        }
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
            {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e)
            {
            }
        });
    }

    private void initTable()
    {
        // Init jenis pemeriksaan ===========================
        Object[] row =
        {
            "P", "Kode Periksa", "Nama Pemeriksaan", "Material", "Bhp", "Dokter", "Perawat", "Kso", "Manajemen", "Tarif"
        };

        tabMode = new DefaultTableModel(null, row)
        {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                return colIndex == 0;
            }
            
            Class[] types = new Class[]
            {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class,
                java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class,
                java.lang.Double.class, java.lang.Double.class
            };

            @Override
            public Class getColumnClass(int columnIndex)
            {
                return types[columnIndex];
            }
        };
        
        tblPemeriksaan.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tblPemeriksaan.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tblPemeriksaan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 10; i++)
        {
            TableColumn column = tblPemeriksaan.getColumnModel().getColumn(i);
            
            if (i == 0)
            {
                column.setPreferredWidth(20);
            }
            else if (i == 1)
            {
                column.setPreferredWidth(80);
            }
            else if (i == 2)
            {
                column.setPreferredWidth(200);
            }
            else if (i == 9)
            {
                column.setPreferredWidth(75);
            }
            else
            {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
        
        tblPemeriksaan.setDefaultRenderer(Object.class, new WarnaTable());
    }
    
    private void initTableList()
    {
        // Init list order ===========================
        Object[] row =
        {
            "Kd Periksa", "No Rawat", "Pasien", "Tgl Periksa", "Jam Periksa", "Status"
        };

        tabModelList = new DefaultTableModel(null, row)
        {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                return colIndex == 0;
            }
            
            Class[] types = new Class[]
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            @Override
            public Class getColumnClass(int columnIndex)
            {
                return types[columnIndex];
            }
        };
        
        tblListTransaksi.setModel(tabModelList);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tblListTransaksi.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tblListTransaksi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 5; i++)
        {
            TableColumn column = tblListTransaksi.getColumnModel().getColumn(i);
            
            if (i == 0)
            {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
            else if (i == 1)
            {
                column.setPreferredWidth(130);
            }
            else if (i == 2)
            {
                column.setPreferredWidth(300);
            }
            else if (i == 3)
            {
                column.setPreferredWidth(200);
            }
            else if (i == 4)
            {
                column.setPreferredWidth(170);
            }
        }
        
        tblListTransaksi.setDefaultRenderer(Object.class, new WarnaTable());
    }
    
    private int getListMainRow(int row)
    {
        int rowMain = row;
        
        if (tblListTransaksi.getValueAt(row, 0).toString().isEmpty())
        {
            for (int a = row; a >= 0; a--)
            {
                if (!tblListTransaksi.getValueAt(a, 0).toString().isEmpty())
                {
                    rowMain = a;
                    break;
                }
            }
        }
        
        return rowMain;
    }
    
    /**
     * Buat ngambil kode-kode dari table yg dipilih
     * Data pertama berupa kode transaksi utama pemeriksaan_hd
     * Data kedua dst berupa kode pemeriksaan yg ada di det_pemeriksaan_hd
     * @return 
     */
    private List<String> getSelectedKode()
    {
        int row = getListMainRow(tblListTransaksi.getSelectedRow());
        int lastRow = row;
        
        for (int a = row + 1; a < tabModelList.getRowCount(); a++)
        {
            if (tblListTransaksi.getValueAt(a, 0).equals(""))
                lastRow = a;
            else
                break;
        }
        
        List<String> res = new ArrayList<>();
        
        for (int a = row; a <= lastRow; a++)
        {
            if (a == row)
                res.add(tblListTransaksi.getValueAt(a, 0).toString());
            else if (a > row + 1)
                res.add(tblListTransaksi.getValueAt(a, 2).toString());
        }
        
        return res;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Penjab = new widget.TextBox();
        Jk = new widget.TextBox();
        Umur = new widget.TextBox();
        Alamat = new widget.TextBox();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        menuUbah = new javax.swing.JMenuItem();
        menuHapus = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        tabPane = new widget.TabPane();
        panelisi1 = new widget.panelisi();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnPrint = new widget.Button();
        jLabel10 = new widget.Label();
        BtnKeluar = new widget.Button();
        FormInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        PanelInput = new widget.PanelBiasa();
        jLabel3 = new widget.Label();
        txtNoRw = new widget.TextBox();
        txtNoRM = new widget.TextBox();
        txtNamaPasien = new widget.TextBox();
        jLabel9 = new widget.Label();
        jLabel12 = new widget.Label();
        txtKdKamar = new widget.TextBox();
        txtNamaKamar = new widget.TextBox();
        Tanggal = new widget.Tanggal();
        CmbJam = new widget.ComboBox();
        CmbMenit = new widget.ComboBox();
        CmbDetik = new widget.ComboBox();
        ChkJln = new widget.CekBox();
        jLabel16 = new widget.Label();
        txtKdDokter = new widget.TextBox();
        txtNamaDokter = new widget.TextBox();
        jLabel15 = new widget.Label();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        panelisi5 = new widget.panelisi();
        label10 = new widget.Label();
        txtCariPeriksa = new widget.TextBox();
        btnCariPeriksa = new widget.Button();
        BtnAllPeriksa = new widget.Button();
        BtnTambahPeriksa = new widget.Button();
        Scroll2 = new widget.ScrollPane();
        tblPemeriksaan = new widget.Table();
        panelisi2 = new widget.panelisi();
        jPanel4 = new javax.swing.JPanel();
        Scroll3 = new widget.ScrollPane();
        tblListTransaksi = new widget.Table();

        Penjab.setEditable(false);
        Penjab.setFocusTraversalPolicyProvider(true);
        Penjab.setName("Penjab"); // NOI18N
        Penjab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PenjabKeyPressed(evt);
            }
        });

        Jk.setEditable(false);
        Jk.setFocusTraversalPolicyProvider(true);
        Jk.setName("Jk"); // NOI18N

        Umur.setEditable(false);
        Umur.setHighlighter(null);
        Umur.setName("Umur"); // NOI18N

        Alamat.setEditable(false);
        Alamat.setHighlighter(null);
        Alamat.setName("Alamat"); // NOI18N

        jPopupMenu1.setForeground(new java.awt.Color(60, 80, 50));
        jPopupMenu1.setAutoscrolls(true);
        jPopupMenu1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPopupMenu1.setFocusTraversalPolicyProvider(true);
        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        menuUbah.setBackground(new java.awt.Color(255, 255, 255));
        menuUbah.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuUbah.setForeground(new java.awt.Color(60, 80, 50));
        menuUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuUbah.setText("Ubah");
        menuUbah.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuUbah.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuUbah.setIconTextGap(5);
        menuUbah.setName("menuUbah"); // NOI18N
        menuUbah.setPreferredSize(new java.awt.Dimension(220, 26));
        menuUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuUbahActionPerformed(evt);
            }
        });
        jPopupMenu1.add(menuUbah);

        menuHapus.setBackground(new java.awt.Color(255, 255, 255));
        menuHapus.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuHapus.setForeground(new java.awt.Color(60, 80, 50));
        menuHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuHapus.setText("Hapus");
        menuHapus.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuHapus.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuHapus.setIconTextGap(5);
        menuHapus.setName("menuHapus"); // NOI18N
        menuHapus.setPreferredSize(new java.awt.Dimension(220, 26));
        menuHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuHapusActionPerformed(evt);
            }
        });
        jPopupMenu1.add(menuHapus);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Input Data Periksa Hemodialisa ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        tabPane.setName("tabPane"); // NOI18N

        panelisi1.setName("panelisi1"); // NOI18N
        panelisi1.setLayout(new java.awt.BorderLayout());

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(55, 55));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Simpan");
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setName("BtnSimpan"); // NOI18N
        BtnSimpan.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanActionPerformed(evt);
            }
        });
        BtnSimpan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpanKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnSimpan);

        BtnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        BtnBatal.setMnemonic('B');
        BtnBatal.setText("Baru");
        BtnBatal.setToolTipText("Alt+B");
        BtnBatal.setName("BtnBatal"); // NOI18N
        BtnBatal.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBatalActionPerformed(evt);
            }
        });
        BtnBatal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnBatalKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnBatal);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("Cetak");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        BtnPrint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPrintKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnPrint);

        jLabel10.setName("jLabel10"); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(350, 30));
        panelGlass8.add(jLabel10);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnKeluar);

        panelisi1.add(panelGlass8, java.awt.BorderLayout.PAGE_END);

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setOpaque(false);
        FormInput.setPreferredSize(new java.awt.Dimension(560, 129));
        FormInput.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('I');
        ChkInput.setText(".: Input Data");
        ChkInput.setToolTipText("Alt+I");
        ChkInput.setBorderPainted(true);
        ChkInput.setBorderPaintedFlat(true);
        ChkInput.setFocusable(false);
        ChkInput.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput.setName("ChkInput"); // NOI18N
        ChkInput.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInputActionPerformed(evt);
            }
        });
        FormInput.add(ChkInput, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setPreferredSize(new java.awt.Dimension(560, 108));
        PanelInput.setLayout(null);

        jLabel3.setText("No.Rawat :");
        jLabel3.setName("jLabel3"); // NOI18N
        PanelInput.add(jLabel3);
        jLabel3.setBounds(0, 12, 92, 23);

        txtNoRw.setEditable(false);
        txtNoRw.setHighlighter(null);
        txtNoRw.setName("txtNoRw"); // NOI18N
        PanelInput.add(txtNoRw);
        txtNoRw.setBounds(95, 12, 148, 23);

        txtNoRM.setEditable(false);
        txtNoRM.setHighlighter(null);
        txtNoRM.setName("txtNoRM"); // NOI18N
        PanelInput.add(txtNoRM);
        txtNoRM.setBounds(245, 12, 125, 23);

        txtNamaPasien.setEditable(false);
        txtNamaPasien.setHighlighter(null);
        txtNamaPasien.setName("txtNamaPasien"); // NOI18N
        PanelInput.add(txtNamaPasien);
        txtNamaPasien.setBounds(372, 12, 400, 23);

        jLabel9.setText("Dokter Perujuk :");
        jLabel9.setName("jLabel9"); // NOI18N
        PanelInput.add(jLabel9);
        jLabel9.setBounds(0, 42, 92, 23);

        jLabel12.setText("Ruang :");
        jLabel12.setName("jLabel12"); // NOI18N
        PanelInput.add(jLabel12);
        jLabel12.setBounds(392, 42, 70, 23);

        txtKdKamar.setEditable(false);
        txtKdKamar.setName("txtKdKamar"); // NOI18N
        PanelInput.add(txtKdKamar);
        txtKdKamar.setBounds(464, 42, 80, 23);

        txtNamaKamar.setEditable(false);
        txtNamaKamar.setName("txtNamaKamar"); // NOI18N
        PanelInput.add(txtNamaKamar);
        txtNamaKamar.setBounds(546, 42, 225, 23);

        Tanggal.setEditable(false);
        Tanggal.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10-10-2017" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);
        Tanggal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalKeyPressed(evt);
            }
        });
        PanelInput.add(Tanggal);
        Tanggal.setBounds(464, 72, 90, 23);

        CmbJam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        CmbJam.setName("CmbJam"); // NOI18N
        CmbJam.setOpaque(false);
        PanelInput.add(CmbJam);
        CmbJam.setBounds(614, 72, 42, 23);

        CmbMenit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        CmbMenit.setName("CmbMenit"); // NOI18N
        CmbMenit.setOpaque(false);
        PanelInput.add(CmbMenit);
        CmbMenit.setBounds(659, 72, 42, 23);

        CmbDetik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        CmbDetik.setName("CmbDetik"); // NOI18N
        CmbDetik.setOpaque(false);
        PanelInput.add(CmbDetik);
        CmbDetik.setBounds(704, 72, 42, 23);

        ChkJln.setBackground(new java.awt.Color(235, 255, 235));
        ChkJln.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(195, 215, 195)));
        ChkJln.setForeground(new java.awt.Color(153, 0, 51));
        ChkJln.setSelected(true);
        ChkJln.setBorderPainted(true);
        ChkJln.setBorderPaintedFlat(true);
        ChkJln.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkJln.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkJln.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkJln.setName("ChkJln"); // NOI18N
        ChkJln.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkJlnActionPerformed(evt);
            }
        });
        PanelInput.add(ChkJln);
        ChkJln.setBounds(749, 72, 23, 23);

        jLabel16.setText("Jam :");
        jLabel16.setName("jLabel16"); // NOI18N
        PanelInput.add(jLabel16);
        jLabel16.setBounds(533, 72, 78, 23);

        txtKdDokter.setEditable(false);
        txtKdDokter.setName("txtKdDokter"); // NOI18N
        PanelInput.add(txtKdDokter);
        txtKdDokter.setBounds(95, 42, 80, 23);

        txtNamaDokter.setEditable(false);
        txtNamaDokter.setHighlighter(null);
        txtNamaDokter.setName("txtNamaDokter"); // NOI18N
        txtNamaDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaDokterActionPerformed(evt);
            }
        });
        PanelInput.add(txtNamaDokter);
        txtNamaDokter.setBounds(180, 42, 190, 23);

        jLabel15.setText("Tgl.Periksa :");
        jLabel15.setName("jLabel15"); // NOI18N
        PanelInput.add(jLabel15);
        jLabel15.setBounds(375, 72, 87, 23);

        FormInput.add(PanelInput, java.awt.BorderLayout.CENTER);

        panelisi1.add(FormInput, java.awt.BorderLayout.PAGE_START);

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(816, 102));
        jPanel1.setLayout(new java.awt.GridLayout(1, 2));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(237, 242, 232)), ".: Pemeriksaan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(300, 102));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelisi5.setBorder(null);
        panelisi5.setName("panelisi5"); // NOI18N
        panelisi5.setPreferredSize(new java.awt.Dimension(100, 43));
        panelisi5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        label10.setText("Key Word :");
        label10.setName("label10"); // NOI18N
        label10.setPreferredSize(new java.awt.Dimension(68, 23));
        panelisi5.add(label10);

        txtCariPeriksa.setToolTipText("Alt+C");
        txtCariPeriksa.setName("txtCariPeriksa"); // NOI18N
        txtCariPeriksa.setPreferredSize(new java.awt.Dimension(160, 23));
        txtCariPeriksa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariPeriksaActionPerformed(evt);
            }
        });
        txtCariPeriksa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCariPeriksaKeyPressed(evt);
            }
        });
        panelisi5.add(txtCariPeriksa);

        btnCariPeriksa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        btnCariPeriksa.setMnemonic('1');
        btnCariPeriksa.setToolTipText("Alt+1");
        btnCariPeriksa.setName("btnCariPeriksa"); // NOI18N
        btnCariPeriksa.setPreferredSize(new java.awt.Dimension(28, 23));
        btnCariPeriksa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariPeriksaActionPerformed(evt);
            }
        });
        btnCariPeriksa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnCariPeriksaKeyPressed(evt);
            }
        });
        panelisi5.add(btnCariPeriksa);

        BtnAllPeriksa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAllPeriksa.setMnemonic('2');
        BtnAllPeriksa.setToolTipText("Alt+2");
        BtnAllPeriksa.setName("BtnAllPeriksa"); // NOI18N
        BtnAllPeriksa.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAllPeriksa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllPeriksaActionPerformed(evt);
            }
        });
        BtnAllPeriksa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllPeriksaKeyPressed(evt);
            }
        });
        panelisi5.add(BtnAllPeriksa);

        BtnTambahPeriksa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        BtnTambahPeriksa.setMnemonic('3');
        BtnTambahPeriksa.setToolTipText("Alt+3");
        BtnTambahPeriksa.setName("BtnTambahPeriksa"); // NOI18N
        BtnTambahPeriksa.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnTambahPeriksa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTambahPeriksaActionPerformed(evt);
            }
        });
        BtnTambahPeriksa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnTambahPeriksaKeyPressed(evt);
            }
        });
        panelisi5.add(BtnTambahPeriksa);

        jPanel3.add(panelisi5, java.awt.BorderLayout.PAGE_END);

        Scroll2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(245, 255, 235)));
        Scroll2.setName("Scroll2"); // NOI18N
        Scroll2.setOpaque(true);

        tblPemeriksaan.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tblPemeriksaan.setName("tblPemeriksaan"); // NOI18N
        tblPemeriksaan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPemeriksaanMouseClicked(evt);
            }
        });
        tblPemeriksaan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblPemeriksaanKeyPressed(evt);
            }
        });
        Scroll2.setViewportView(tblPemeriksaan);

        jPanel3.add(Scroll2, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3);

        panelisi1.add(jPanel1, java.awt.BorderLayout.CENTER);

        tabPane.addTab("Transaksi", panelisi1);

        panelisi2.setName("panelisi2"); // NOI18N
        panelisi2.setLayout(new java.awt.BorderLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(237, 242, 232)), ".: List Transaksi", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.setOpaque(false);
        jPanel4.setPreferredSize(new java.awt.Dimension(300, 102));
        jPanel4.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(245, 255, 235)));
        Scroll3.setComponentPopupMenu(jPopupMenu1);
        Scroll3.setName("Scroll3"); // NOI18N
        Scroll3.setOpaque(true);

        tblListTransaksi.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tblListTransaksi.setComponentPopupMenu(jPopupMenu1);
        tblListTransaksi.setName("tblListTransaksi"); // NOI18N
        tblListTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblListTransaksiMouseClicked(evt);
            }
        });
        tblListTransaksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblListTransaksiKeyPressed(evt);
            }
        });
        Scroll3.setViewportView(tblListTransaksi);

        jPanel4.add(Scroll3, java.awt.BorderLayout.CENTER);

        panelisi2.add(jPanel4, java.awt.BorderLayout.CENTER);

        tabPane.addTab("List Transaksi", panelisi2);

        internalFrame1.add(tabPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        emptTeks();
        BtnSimpan.setText("Simpan");
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            dispose();
        }
}//GEN-LAST:event_BtnKeluarKeyPressed

private void ChkJlnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkJlnActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_ChkJlnActionPerformed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void PenjabKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PenjabKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PenjabKeyPressed

    private void TanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalKeyPressed
        Valid.pindah(evt, txtKdDokter, txtCariPeriksa);
    }//GEN-LAST:event_TanggalKeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        
    }//GEN-LAST:event_formWindowOpened

    private void txtCariPeriksaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariPeriksaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            btnCariPeriksaActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
        {
            btnCariPeriksa.requestFocus();
        }
        else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP)
        {
            BtnTambahPeriksa.requestFocus();
        }
    }//GEN-LAST:event_txtCariPeriksaKeyPressed

    private void btnCariPeriksaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariPeriksaActionPerformed
        tampil();
    }//GEN-LAST:event_btnCariPeriksaActionPerformed

    private void btnCariPeriksaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCariPeriksaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            tampil();
        }
        else
        {
            Valid.pindah(evt, txtCariPeriksa, BtnAllPeriksa);
        }
    }//GEN-LAST:event_btnCariPeriksaKeyPressed

    private void BtnAllPeriksaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllPeriksaActionPerformed
        txtCariPeriksa.setText("");
        tampil();
    }//GEN-LAST:event_BtnAllPeriksaActionPerformed

    private void BtnAllPeriksaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllPeriksaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            BtnAllPeriksaActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, btnCariPeriksa, BtnTambahPeriksa);
        }
    }//GEN-LAST:event_BtnAllPeriksaKeyPressed

    private void BtnTambahPeriksaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahPeriksaActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        DlgJnsPerawatanRadiologi produsen = new DlgJnsPerawatanRadiologi(null, false);
        produsen.emptTeks();
        produsen.isCek();
        produsen.setSize(internalFrame1.getWidth(), internalFrame1.getHeight());
        produsen.setLocationRelativeTo(internalFrame1);
        produsen.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnTambahPeriksaActionPerformed

    private void tblPemeriksaanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPemeriksaanMouseClicked
        if (tabMode.getRowCount() != 0)
        {
            try
            {
                // getData2();
            }
            catch (java.lang.NullPointerException e)
            {
            }
        }
    }//GEN-LAST:event_tblPemeriksaanMouseClicked

    private void tblPemeriksaanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPemeriksaanKeyPressed
        if (tblPemeriksaan.getRowCount() != 0)
        {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER)
            {
                try
                {
                    int row = tblPemeriksaan.getSelectedColumn();
                    if ((row != 0) || (row != 20))
                    {
                        if (tblPemeriksaan.getSelectedRow() > -1)
                        {
                            tblPemeriksaan.setValueAt(true, tblPemeriksaan.getSelectedRow(), 0);
                        }
                        txtCariPeriksa.setText("");
                        txtCariPeriksa.requestFocus();
                    }
                    //getData2();
                }
                catch (java.lang.NullPointerException e)
                {
                }
            }
            else if ((evt.getKeyCode() == KeyEvent.VK_UP) || (evt.getKeyCode() == KeyEvent.VK_DOWN))
            {
                try
                {
                    // getData2();
                }
                catch (java.lang.NullPointerException e)
                {
                }
            }
        }
    }//GEN-LAST:event_tblPemeriksaanKeyPressed

    private void BtnTambahPeriksaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnTambahPeriksaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            BtnTambahPeriksaActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, BtnAllPeriksa, txtCariPeriksa);
        }
    }//GEN-LAST:event_BtnTambahPeriksaKeyPressed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            BtnSimpanActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtCariPeriksa, BtnBatal);
        }
    }//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if (txtNoRw.getText().equals("") || txtNoRM.getText().equals("") || txtNamaPasien.getText().equals(""))
        {
            Valid.textKosong(txtNoRw, "Pasien");
        }
        else if (txtKdKamar.getText().equals("") || txtNamaKamar.getText().equals(""))
        {
            Valid.textKosong(txtKdKamar, "Petugas");
        }
        else if (txtKdDokter.getText().equals("") || txtNamaDokter.getText().equals(""))
        {
            Valid.textKosong(txtKdDokter, "Dokter Perujuk");
        }
        else if (tabMode.getRowCount() == 0)
        {
            Valid.textKosong(txtCariPeriksa, "Data Pemeriksaan");
        }
        else if (Sequel.cariRegistrasi(txtNoRw.getText()) > 0)
        {
            JOptionPane.showMessageDialog(rootPane, "Data billing sudah terverifikasi, data tidak boleh dihapus.\nSilahkan hubungi bagian kasir/keuangan ..!!");
            txtCariPeriksa.requestFocus();
        }
        else
        {
            int reply = JOptionPane.showConfirmDialog(rootPane, "Eeiiiiiits, udah bener belum data yang mau disimpan..??", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            
            if (reply == JOptionPane.YES_OPTION)
            {
                if (isEdit)
                {
                    ChkJln.setSelected(false);
                    GQuery.setAutoCommit(false);

                    boolean success = true;
                    
                    success &= new GQuery()
                            .a("UPDATE pemeriksaan_hd SET")
                            .a("tgl_periksa = {tgl_periksa},")
                            .a("jam_mulai = {jam_mulai}")
                            .a("WHERE kd_periksa = {kd_periksa}")
                            .set("tgl_periksa", Valid.SetTgl(Tanggal.getSelectedItem().toString()))
                            .set("jam_mulai", CmbJam.getSelectedItem() + ":" + CmbMenit.getSelectedItem() + ":" + CmbDetik.getSelectedItem())
                            .set("kd_periksa", kdPeriksa)
                            .write();
                    
                    success &= new GQuery()
                            .a("DELETE FROM det_pemeriksaan_hd WHERE kd_periksa = {kd_periksa}")
                            .set("kd_periksa", kdPeriksa)
                            .write();

                    // Perulangan menyimpan ke tabel detail periksa hd
                    for (i = 0; i < tblPemeriksaan.getRowCount(); i++)
                    {
                        if (tblPemeriksaan.getValueAt(i, 0).toString().equals("true"))
                        {
                            success &= new GQuery()
                                    .a("INSERT INTO det_pemeriksaan_hd VALUES(")
                                    .a("{kd_periksa},")
                                    .a("{kd_jns_prw},")
                                    .a("{meterial},")
                                    .a("{bhp},")
                                    .a("{dokter},")
                                    .a("{perawat},")
                                    .a("{kso},")
                                    .a("{manajemen},")
                                    .a("{total}")
                                    .a(")")
                                    .set("kd_periksa", kdPeriksa)
                                    .set("kd_jns_prw", tblPemeriksaan.getValueAt(i, 1).toString())
                                    .set("meterial", tblPemeriksaan.getValueAt(i, 3).toString())
                                    .set("bhp", tblPemeriksaan.getValueAt(i, 4).toString())
                                    .set("dokter", tblPemeriksaan.getValueAt(i, 5).toString())
                                    .set("perawat", tblPemeriksaan.getValueAt(i, 6).toString())
                                    .set("kso", tblPemeriksaan.getValueAt(i, 7).toString())
                                    .set("manajemen", tblPemeriksaan.getValueAt(i, 8).toString())
                                    .set("total", tblPemeriksaan.getValueAt(i, 9).toString())
                                    .write();
                        }
                    }

                    GQuery.setAutoCommit(true);
                    ChkJln.setSelected(true);
                    
                    if (success)
                    {
                        JOptionPane.showMessageDialog(rootPane, "Proses ubah selesai...!");
                        isReset();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(rootPane, "Ubah data gagal", "Gagal", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else
                {
                    ChkJln.setSelected(false);
                    Sequel.AutoComitFalse();

                    // Ambil no otomatis
                    String kdPeriksa = Sequel.autoNumber("pemeriksaan_hd", "kd_periksa");
                    boolean success = true;
                    
                    // Menyimpan ke table periksa hd (UTAMA)
                    success &= Sequel.menyimpantf2("pemeriksaan_hd", "?,?,?,?,?,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,?,NULL,NULL,NULL,NULL,?,?", "-", 8, new String[]
                    {
                        kdPeriksa,
                        txtNoRw.getText(),
                        txtKdKamar.getText(),
                        Valid.SetTgl(Tanggal.getSelectedItem().toString()),
                        CmbJam.getSelectedItem() + ":" + CmbMenit.getSelectedItem() + ":" + CmbDetik.getSelectedItem(),
                        txtKdDokter.getText(),
                        "0",
                        "Ranap"
                    });

                    // Perulangan menyimpan ke tabel detail periksa hd
                    for (i = 0; i < tblPemeriksaan.getRowCount(); i++)
                    {
                        if (tblPemeriksaan.getValueAt(i, 0).toString().equals("true"))
                        {
                            success &= Sequel.menyimpantf2("det_pemeriksaan_hd", "?,?,?,?,?,?,?,?,?", "-", 9, new String[]
                            {
                                kdPeriksa,
                                tblPemeriksaan.getValueAt(i, 1).toString(),
                                tblPemeriksaan.getValueAt(i, 3).toString(),
                                tblPemeriksaan.getValueAt(i, 4).toString(),
                                tblPemeriksaan.getValueAt(i, 5).toString(),
                                tblPemeriksaan.getValueAt(i, 6).toString(),
                                tblPemeriksaan.getValueAt(i, 7).toString(),
                                tblPemeriksaan.getValueAt(i, 8).toString(),
                                tblPemeriksaan.getValueAt(i, 9).toString()
                            });
                        }
                    }

                    Sequel.AutoComitTrue();
                    ChkJln.setSelected(true);
                    
                    if (success)
                    {
                        JOptionPane.showMessageDialog(rootPane, "Proses simpan selesai...!");
                        isReset();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(rootPane, "Simpan data gagal", "Gagal", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            BtnBatalActionPerformed(null);
        }
        else
        {
            
        }
    }//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
//        jml = 0;
//        for (i = 0; i < tblPemeriksaan.getRowCount(); i++)
//        {
//            if (tblPemeriksaan.getValueAt(i, 0).toString().equals("true"))
//            {
//                jml++;
//            }
//        }
//        if (txtNoRw.getText().equals("") || txtNoRM.getText().equals("") || txtNamaPasien.getText().equals(""))
//        {
//            Valid.textKosong(txtNoRw, "Pasien");
//        }
//        else if (txtKdKamar.getText().equals("") || txtNamaKamar.getText().equals(""))
//        {
//            Valid.textKosong(txtKdKamar, "Petugas");
//        }
//        else if (txtKdDokter.getText().equals("") || txtNamaDokter.getText().equals(""))
//        {
//            Valid.textKosong(txtKdDokter, "Dokter Pengirim");
//        }
//        else if (KodePj.getText().equals("") || NmDokterPj.getText().equals(""))
//        {
//            Valid.textKosong(KodePj, "Dokter Penanggung Jawab");
//        }
//        else if (tabMode.getRowCount() == 0)
//        {
//            Valid.textKosong(txtCariPeriksa, "Data Pemeriksaan");
//        }
//        else if (jml == 0)
//        {
//            Valid.textKosong(txtCariPeriksa, "Data Pemeriksaan");
//        }
//        else
//        {
//            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//            Sequel.AutoComitFalse();
//            Sequel.queryu("delete from temporary");
//            pemeriksaan = "";
//            for (i = 0; i < tblPemeriksaan.getRowCount(); i++)
//            {
//                if (tblPemeriksaan.getValueAt(i, 0).toString().equals("true"))
//                {
//                    pemeriksaan = tblPemeriksaan.getValueAt(i, 2).toString() + ", " + pemeriksaan;
//                }
//            }
//            Sequel.AutoComitTrue();
//            Map<String, Object> param = new HashMap<>();
//            param.put("noperiksa", txtNoRw.getText());
//            param.put("norm", txtNoRM.getText());
//            param.put("namapasien", txtNamaPasien.getText());
//            param.put("jkel", Jk.getText());
//            param.put("umur", Umur.getText());
//            param.put("lahir", Sequel.cariIsi("select DATE_FORMAT(tgl_lahir,'%d-%m-%Y') from pasien where no_rkm_medis=? ", txtNoRM.getText()));
//            param.put("pengirim", txtNamaDokter.getText());
//            param.put("tanggal", Tanggal.getSelectedItem());
//            param.put("penjab", NmDokterPj.getText());
//            param.put("petugas", txtNamaKamar.getText());
//            param.put("alamat", Alamat.getText());
//            param.put("kamar", kamar);
//            param.put("namakamar", namakamar);
//            param.put("pemeriksaan", pemeriksaan);
//            param.put("jam", CmbJam.getSelectedItem() + ":" + CmbMenit.getSelectedItem() + ":" + CmbDetik.getSelectedItem());
//            param.put("namars", var.getnamars());
//            param.put("alamatrs", var.getalamatrs());
//            param.put("kotars", var.getkabupatenrs());
//            param.put("propinsirs", var.getpropinsirs());
//            param.put("kontakrs", var.getkontakrs());
//            param.put("emailrs", var.getemailrs());
//            param.put("hasil", HasilPeriksa.getText());
//            param.put("logo", Sequel.cariGambar("select logo from setting"));
//
//            pilihan = (String) JOptionPane.showInputDialog(null, "Silahkan pilih hasil pemeriksaan..!", "Hasil Pemeriksaan", JOptionPane.QUESTION_MESSAGE, null, new Object[]
//            {
//                "Model 1", "Model 2", "Model 3"
//            }, "Model 1");
//            switch (pilihan)
//            {
//                case "Model 1":
//                    Valid.MyReport("rptPeriksaRadiologi.jrxml", "report", "::[ Pemeriksaan Radiologi ]::",
//                            "select current_date as tanggal", param);
//                    break;
//                case "Model 2":
//                    Valid.MyReport("rptPeriksaRadiologi2.jrxml", "report", "::[ Pemeriksaan Radiologi ]::",
//                            "select current_date as tanggal", param);
//                    break;
//                case "Model 3":
//                    Valid.MyReport("rptPeriksaRadiologi3.jrxml", "report", "::[ Pemeriksaan Radiologi ]::",
//                            "select current_date as tanggal", param);
//                    break;
//            }
//
//            this.setCursor(Cursor.getDefaultCursor());
//            ChkJln.setSelected(false);
//        }
    }//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
//        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
//        {
//            BtnPrintActionPerformed(null);
//        }
//        else
//        {
//            Valid.pindah(evt, BtnBatal, BtnCari);
//        }
    }//GEN-LAST:event_BtnPrintKeyPressed

    private void txtCariPeriksaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariPeriksaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariPeriksaActionPerformed

    private void txtNamaDokterActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtNamaDokterActionPerformed
    {//GEN-HEADEREND:event_txtNamaDokterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaDokterActionPerformed

    private void tblListTransaksiMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblListTransaksiMouseClicked
    {//GEN-HEADEREND:event_tblListTransaksiMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblListTransaksiMouseClicked

    private void tblListTransaksiKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_tblListTransaksiKeyPressed
    {//GEN-HEADEREND:event_tblListTransaksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblListTransaksiKeyPressed

    private void menuUbahActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuUbahActionPerformed
    {//GEN-HEADEREND:event_menuUbahActionPerformed
        tabPane.setSelectedIndex(0);
        
        List<String> kodes = getSelectedKode();
        kdPeriksa = kodes.get(0);
        kodes.remove(0);
        
        for (int a = 0; a < tabMode.getRowCount(); a++)
        {
            if (kodes.contains(tblPemeriksaan.getValueAt(a, 1)))
                tblPemeriksaan.setValueAt(true, a, 0);
            else
                tblPemeriksaan.setValueAt(false, a, 0);
        }
        
        isEdit = true;
        BtnSimpan.setText("Ubah");
    }//GEN-LAST:event_menuUbahActionPerformed

    private void menuHapusActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuHapusActionPerformed
    {//GEN-HEADEREND:event_menuHapusActionPerformed
        int reply = JOptionPane.showConfirmDialog(rootPane, "Eeiiiiiits, beneran mau hapus..??", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            
        if (reply == JOptionPane.YES_OPTION)
        {
            GQuery.setAutoCommit(false);
            
            boolean isSuccess = true;
            
            int mainRow = getListMainRow(tblListTransaksi.getSelectedRow());
            String kdPeriksa = tblListTransaksi.getValueAt(mainRow, 0).toString();
            
            isSuccess &= new GQuery()
                    .a("DELETE FROM det_pemeriksaan_hd WHERE kd_periksa = {kd_periksa}")
                    .set("kd_periksa", kdPeriksa)
                    .write();
            
            isSuccess &= new GQuery()
                    .a("DELETE FROM pemeriksaan_hd WHERE kd_periksa = {kd_periksa}")
                    .set("kd_periksa", kdPeriksa)
                    .write();
            
            GQuery.setAutoCommit(true);
            
            if (isSuccess)
            {
                JOptionPane.showMessageDialog(rootPane, "Hapus data berhasil", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                tampilList();
            }
            else
                JOptionPane.showMessageDialog(rootPane, "Hapus data gagal", "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_menuHapusActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(() -> 
                {
                    DlgPeriksaHemodialisa dialog = new DlgPeriksaHemodialisa(new javax.swing.JFrame(), true);
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
    private widget.TextBox Alamat;
    private widget.Button BtnAllPeriksa;
    private widget.Button BtnBatal;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.Button BtnTambahPeriksa;
    private widget.CekBox ChkInput;
    private widget.CekBox ChkJln;
    private widget.ComboBox CmbDetik;
    private widget.ComboBox CmbJam;
    private widget.ComboBox CmbMenit;
    private javax.swing.JPanel FormInput;
    private widget.TextBox Jk;
    private widget.PanelBiasa PanelInput;
    private widget.TextBox Penjab;
    private widget.ScrollPane Scroll2;
    private widget.ScrollPane Scroll3;
    private widget.Tanggal Tanggal;
    private widget.TextBox Umur;
    private widget.Button btnCariPeriksa;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel12;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel3;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.Label label10;
    private javax.swing.JMenuItem menuHapus;
    private javax.swing.JMenuItem menuUbah;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelisi1;
    private widget.panelisi panelisi2;
    private widget.panelisi panelisi5;
    private widget.TabPane tabPane;
    private widget.Table tblListTransaksi;
    private widget.Table tblPemeriksaan;
    private widget.TextBox txtCariPeriksa;
    private widget.TextBox txtKdDokter;
    private widget.TextBox txtKdKamar;
    private widget.TextBox txtNamaDokter;
    private widget.TextBox txtNamaKamar;
    private widget.TextBox txtNamaPasien;
    private widget.TextBox txtNoRM;
    private widget.TextBox txtNoRw;
    // End of variables declaration//GEN-END:variables

    public void tampil()
    {
        try
        {
            Valid.tabelKosong(tabMode);

            String q = new GQuery()
                    .a("SELECT kd_jenis_prw, nm_perawatan, material, bhp, tarif_tindakandr, tarif_tindakanpr,")
                    .a("kso, menejemen, total_byrdrpr")
                    .a("FROM jns_perawatan")
                    .a("WHERE kd_kategori = 'hd'")
                    .a("ORDER BY nm_perawatan")
                    .compile();
            
            pspemeriksaan = koneksi.prepareStatement(q);
            try
            {
                rs = pspemeriksaan.executeQuery();
                
                while (rs.next())
                {
                    tabMode.addRow(new Object[]
                    {
                        false, rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9)
                    });
                }
            }
            catch (Exception e)
            {
                System.out.println("Notifikasi 1 : " + e);
            }
            finally
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (pspemeriksaan != null)
                {
                    pspemeriksaan.close();
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("Notifikasi 2 : " + e);
        }
    }

    // BUta nampilin list order + transaksi
    public void tampilList()
    {
        // Isi statement ----------------
        try
        {
            String qMain = new GQuery()
                    .a("SELECT pemeriksaan_hd.kd_periksa, pemeriksaan_hd.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, kamar.kd_kamar, bangsal.nm_bangsal,")
                    .a("    tgl_periksa, jam_mulai, IF (pemeriksaan_hd.status = 0, 'Belum', 'Sudah') as status")
                    .a("FROM pemeriksaan_hd")
                    .a("JOIN reg_periksa ON reg_periksa.no_rawat = pemeriksaan_hd.no_rawat")
                    .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                    .a("JOIN kamar ON kamar.kd_kamar = pemeriksaan_hd.kd_kamar")
                    .a("JOIN bangsal ON bangsal.kd_bangsal = kamar.kd_bangsal")
                    .a("WHERE pemeriksaan_hd.no_rawat = {no_rawat}")
                    .set("no_rawat", txtNoRw.getText())
                    .compile();
            
            GQuery qDet = new GQuery()
                    .a("SELECT det_pemeriksaan_hd.kd_jenis_prw, nm_perawatan, det_pemeriksaan_hd.biaya_rawat")
                    .a("FROM det_pemeriksaan_hd")
                    .a("JOIN jns_perawatan ON jns_perawatan.kd_jenis_prw = det_pemeriksaan_hd.kd_jenis_prw")
                    .a("WHERE kd_periksa = {kd_periksa}");
            
            Valid.tabelKosong(tabModelList);
            psMain = koneksi.prepareStatement(qMain);
            rsMain = psMain.executeQuery();
            
            while (rsMain.next())
            {
                Object[] o = new Object[]
                {
                    rsMain.getString("kd_periksa"),
                    rsMain.getString("no_rawat"),
                    rsMain.getString("no_rkm_medis") + " " + rsMain.getString("nm_pasien") + " (Kamar : " + 
                        rsMain.getString("kd_kamar") + ", " + rsMain.getString("nm_bangsal") + ")",
                    rsMain.getString("tgl_periksa"),
                    rsMain.getString("jam_mulai"),
                    rsMain.getString("status")
                };
                
                tabModelList.addRow(o);
                tabModelList.addRow(new Object[] { "", "", "Kode Periksa", "Nama Pemeriksaan", "Biaya Pemeriksaan", ""});
                
                String q = qDet.set("kd_periksa", rsMain.getString("kd_periksa"))
                        .compile();
                
                psDetail = koneksi.prepareStatement(q);
                rsDetail = psDetail.executeQuery();
                
                while (rsDetail.next())
                {
                    Object[] od = new Object[]
                    {
                        "",
                        "",
                        rsDetail.getString("kd_jenis_prw"),
                        rsDetail.getString("nm_perawatan"),
                        Valid.SetAngka(rsDetail.getDouble("biaya_rawat")),
                        ""
                    };
                    
                    tabModelList.addRow(od);
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    public void isReset()
    {
        jml = tblPemeriksaan.getRowCount();
        for (i = 0; i < jml; i++)
        {
            tblPemeriksaan.setValueAt(false, i, 0);
        }
        
        tampilList();
    }

    public void emptTeks()
    {
        isReset();
    }

    public void onCari()
    {
        
    }

    private void isRawat()
    {
        Sequel.cariIsi("select no_rkm_medis from reg_periksa where no_rawat=? ", txtNoRM, txtNoRw.getText());
        Sequel.cariIsi("select kd_pj from reg_periksa where no_rawat=? ", Penjab, txtNoRw.getText());
        Sequel.cariIsi("select kd_dokter from reg_periksa where no_rawat=? ", txtKdDokter, txtNoRw.getText());
        Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=? ", txtNamaDokter, txtKdDokter.getText());

        kamar = Sequel.cariIsi("select ifnull(kd_kamar,'') from kamar_inap where no_rawat=? order by tgl_masuk desc limit 1", txtNoRw.getText());
        if (!kamar.equals(""))
        {
            namakamar = kamar + ", " + Sequel.cariIsi("select nm_bangsal from bangsal inner join kamar on bangsal.kd_bangsal=kamar.kd_bangsal "
                    + " where kamar.kd_kamar=? ", kamar);
            kamar = "Kamar";
        }
        else if (kamar.equals(""))
        {
            kamar = "Poli";
            namakamar = Sequel.cariIsi("select nm_poli from poliklinik inner join reg_periksa on poliklinik.kd_poli=reg_periksa.kd_poli "
                    + "where reg_periksa.no_rawat=?", txtNoRw.getText());
        }
    }

    private void isPsien()
    {
        Sequel.cariIsi("select nm_pasien from pasien where no_rkm_medis=? ", txtNamaPasien, txtNoRM.getText());
        Sequel.cariIsi("select jk from pasien where no_rkm_medis=? ", Jk, txtNoRM.getText());
        Sequel.cariIsi("select umur from pasien where no_rkm_medis=?", Umur, txtNoRM.getText());
        Sequel.cariIsi("select alamat from pasien where no_rkm_medis=? ", Alamat, txtNoRM.getText());
    }

    private void jam()
    {
        ActionListener taskPerformer = new ActionListener()
        {
            private int nilai_jam;
            private int nilai_menit;
            private int nilai_detik;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                String nol_jam = "";
                String nol_menit = "";
                String nol_detik = "";
                // Membuat Date
                //Date dt = new Date();
                Date now = Calendar.getInstance().getTime();

                // Mengambil nilaj JAM, MENIT, dan DETIK Sekarang
                if (ChkJln.isSelected() == true)
                {
                    nilai_jam = now.getHours();
                    nilai_menit = now.getMinutes();
                    nilai_detik = now.getSeconds();
                }
                else if (ChkJln.isSelected() == false)
                {
                    nilai_jam = CmbJam.getSelectedIndex();
                    nilai_menit = CmbMenit.getSelectedIndex();
                    nilai_detik = CmbDetik.getSelectedIndex();
                }

                // Jika nilai JAM lebih kecil dari 10 (hanya 1 digit)
                if (nilai_jam <= 9)
                {
                    // Tambahkan "0" didepannya
                    nol_jam = "0";
                }
                // Jika nilai MENIT lebih kecil dari 10 (hanya 1 digit)
                if (nilai_menit <= 9)
                {
                    // Tambahkan "0" didepannya
                    nol_menit = "0";
                }
                // Jika nilai DETIK lebih kecil dari 10 (hanya 1 digit)
                if (nilai_detik <= 9)
                {
                    // Tambahkan "0" didepannya
                    nol_detik = "0";
                }
                // Membuat String JAM, MENIT, DETIK
                String jam = nol_jam + Integer.toString(nilai_jam);
                String menit = nol_menit + Integer.toString(nilai_menit);
                String detik = nol_detik + Integer.toString(nilai_detik);
                // Menampilkan pada Layar
                //tampil_jam.setText("  " + jam + " : " + menit + " : " + detik + "  ");
                CmbJam.setSelectedItem(jam);
                CmbMenit.setSelectedItem(menit);
                CmbDetik.setSelectedItem(detik);
            }
        };
        // Timer
        new Timer(1000, taskPerformer).start();
    }

    public void setNoRm(String norwt, String posisi)
    {
        txtNoRw.setText(norwt);
        this.status = posisi;
     
        String[] sa = new GQuery()
                .a("SELECT kamar.kd_kamar, nm_bangsal")
                .a("FROM kamar_inap")
                .a("JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                .a("JOIN bangsal ON bangsal.kd_bangsal = kamar.kd_bangsal")
                .a("WHERE no_rawat = {no_rw}")
                .set("no_rw", norwt)
                .getRow();
                
        txtKdKamar.setText(sa[0]);
        txtNamaKamar.setText(sa[1]);
        
        isRawat();
        isPsien();
        
        try
        {
            psset_tarif = koneksi.prepareStatement("select * from set_tarif");
            try
            {
                rsset_tarif = psset_tarif.executeQuery();
                if (rsset_tarif.next())
                {
                    cara_bayar_radiologi = rsset_tarif.getString("cara_bayar_radiologi");
                }
                else
                {
                    cara_bayar_radiologi = "Yes";
                }
            }
            catch (Exception e)
            {
                System.out.println("Notifikasi : " + e);
            }
            finally
            {
                if (rsset_tarif != null)
                {
                    rsset_tarif.close();
                }
                if (psset_tarif != null)
                {
                    psset_tarif.close();
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        
        isReset();
    }

    public void isCek()
    {
        BtnSimpan.setEnabled(var.getperiksa_radiologi());
        BtnTambahPeriksa.setEnabled(var.gettarif_radiologi());
    }

    private void isForm()
    {
        if (ChkInput.isSelected() == true)
        {
            ChkInput.setVisible(false);
            FormInput.setPreferredSize(new Dimension(WIDTH, 129));
            PanelInput.setVisible(true);
            ChkInput.setVisible(true);
        }
        else if (ChkInput.isSelected() == false)
        {
            ChkInput.setVisible(false);
            FormInput.setPreferredSize(new Dimension(WIDTH, 20));
            PanelInput.setVisible(false);
            ChkInput.setVisible(true);
        }
    }

}
