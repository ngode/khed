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

import fungsi.ButtonCell;
import fungsi.GConvert;
import fungsi.GQuery;
import fungsi.GResult;
import fungsi.GRow;
import keuangan.DlgJnsPerawatanRadiologi;
import fungsi.WarnaTable;
import fungsi.WarnaTableWithButton;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.var;
import interfaces.TextChangedListener;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import keuangan.Jurnal;
import pop.PopHasilRad;
import simrskhanza.cari.DlgCariReg;
import util.GColors;
import util.GConst;
import util.GMessage;
import widget.Button;
import widget.TextBox;

/**
 *
 * @author dosen
 */
public final class DlgPemeriksaanRadiologi extends javax.swing.JDialog
{

    private DefaultTableModel mdlPemeriksaan, mdlTransaksi, mdlGroup, mdlOrder, mdlNonRm;
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private Connection koneksi = koneksiDB.condb();
    private Jurnal jur = new Jurnal();

    private PreparedStatement psset_tarif, pssetpj, pspemeriksaan;
    private ResultSet rs, rsset_tarif, rssetpj, rsrekening;
    private int jml = 0, i = 0, index = 0;
    private String cara_bayar_radiologi = "Yes", pilihan = "", pemeriksaan = "", kamar, namakamar, status = "";
    private double ttl = 0, item = 0;

    // Child Form ==========
    private DlgCariPetugas petugas = new DlgCariPetugas(null, false);
    private DlgCariDokter dokter = new DlgCariDokter(null, false);

    // LIST =========================
    private PreparedStatement psMainTransaksi, psDetailTransaksi, psMainOrder, psDetailOrder, psGroup;
    private ResultSet rsMainTransaksi, rsDetailTransaksi, rsMainOrder, rsDetailOrder, rsGroup;

    // VARS ===================
    private boolean isEdit = false;
    private String kdPeriksa;
    private DefaultTableModel TemplateLab;
    private List<String> selPemeriksaan = new ArrayList<>();
    private List<String> selGroup = new ArrayList<>();
    private HashMap<String, String> pemeriksaanMap = new HashMap<>();
    private String curPemeriksaan;

    /**
     * Creates new form DlgPerawatan
     *
     * @param parent
     * @param modal
     */
    public DlgPemeriksaanRadiologi(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();

        initTxtHasil();
        initTableGroup();
        initTablePemeriksaan();
        initTableOrder();
        initTableTransaksi();
        initTableNonRm();

        // RM NON RM ===============================
        showRm(true);  
        
        ckbNonRm.addItemListener((e) -> 
        {
            if (ckbNonRm.isSelected())
            {
                showNonRm(true);
            }
            else
            {
                showRm(true);
            }
        });
        
        txtNoRw.setDocument(new batasInput((byte) 17).getKata(txtNoRw));
        txtKdKamar.setDocument(new batasInput((byte) 20).getKata(txtKdKamar));
        txtKdDokter.setDocument(new batasInput((byte) 20).getKata(txtKdDokter));

        // ======= Set hak akses expertise hanya untuk dokter ======
        String idUser = var.getkode();
        
        // Kalo dia sbg admin utama, biarin aja
        if (!idUser.equals("Admin Utama"))
        {
            // Tes apakah id user ada di dokter
            String user = Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", idUser);

            // Kalo di dokter gak ada, berarti petugas
            if(user.equals(""))
            {
                // Gak kasih hak akses expertise kalo yg login petugas
                btnEditHasil.setHasAccess(false);
                
                txtKdPetugas.setText(idUser);
                Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", NmPtg, idUser);
            }
        }
        // =========================================================
        
        // ============ Set pj radiologi ===========================
        // Ambil pj_rad dari db
        String pjRad = Sequel.cariIsi("SELECT kd_dokterrad FROM set_pjlab");
        
        // Kalo di database ada pj nya
        if (!pjRad.equals(""))
        {
            txtKdDokterPj.setText(pjRad);
            Sequel.cariIsi("SELECT nm_dokter FROM dokter WHERE kd_dokter = ?", NmDokterPj, pjRad);
        }
        // =========================================================
        
        petugas.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosed(WindowEvent e)
            {
                if (var.getform().equals("DlgPeriksaRadiologi"))
                {
                    if (petugas.getTable().getSelectedRow() != -1)
                    {
                        txtKdPetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(), 0).toString());
                        NmPtg.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(), 1).toString());
                    }
                    txtKdPetugas.requestFocus();
                }
            }
        });
        
        dokter.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosed(WindowEvent e)
            {
                if (var.getform().equals("DlgPeriksaRadiologi"))
                {
                    if (dokter.getTable().getSelectedRow() != -1)
                    {
                        txtKdDokterPj.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 0).toString());
                        NmDokterPj.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 1).toString());
                        txtKdDokterPj.requestFocus();
                    }
                }
            }
        });
        
        if (koneksiDB.cariCepat().equals("aktif"))
        {
            txtCariGroup.addTextChangedListener((txt) ->
            {
                tampilGroup();
                tampilPemeriksaan();
            });
            
            txtCariPemeriksaan.addTextChangedListener((txt) ->
            {
                tampilPemeriksaan();
            });
            
            txtCariOrder.addTextChangedListener((t) -> tampilOrder());
            txtCariTransaksi.addTextChangedListener((t) -> tampilTransaksi());
        }

        ChkJln.setSelected(true);
        jam();
        
        tampilOrder();
        tampilTransaksi();
        tampilNonRm();
    }

    private void initTableGroup()
    {
        // Init jenis pemeriksaan ===========================
        Object[] row =
        {
            "P", "Kode Group", "Nama Group"
        };

        mdlGroup = new DefaultTableModel(null, row)
        {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                return colIndex == 0;
            }

            Class[] types = new Class[]
            {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };

            @Override
            public Class getColumnClass(int columnIndex)
            {
                return types[columnIndex];
            }
        };

        tblGroup.setModel(mdlGroup);

        tblGroup.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tblGroup.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 3; i++)
        {
            TableColumn column = tblGroup.getColumnModel().getColumn(i);

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
        }

        tblGroup.setDefaultRenderer(Object.class, new WarnaTable());
    }

    private void initTablePemeriksaan()
    {
        // Init jenis pemeriksaan ===========================
        Object[] row =
        {
            "P", "Kode Periksa", "Nama Pemeriksaan", "Rs", "Bhp", "Perujuk", "Dokter", "Perawat", "Kso", "Manajemen", "Tarif"
        };

        mdlPemeriksaan = new DefaultTableModel(null, row)
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
                java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };

            @Override
            public Class getColumnClass(int columnIndex)
            {
                return types[columnIndex];
            }
        };

        tblPemeriksaan.setModel(mdlPemeriksaan);

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
            else if (i == 10)
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

    private void initTableOrder()
    {
        // Init list order ===========================
        Object[] row =
        {
            "Kd Periksa", "No Rawat", "Pasien", "Tgl Periksa", "Jam Periksa", 
            "Tgl Selesai", "Jam Selesai", "Status", "", ""
        };
        
        int[] sizes = 
        {
            0, 120, 300, 100, 100,
            100, 100, 100, 100, 100
        };

        mdlOrder = new DefaultTableModel(null, row)
        {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                return false;
            }
        };

        tblOrder.setModel(mdlOrder);

        tblOrder.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tblOrder.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < sizes.length; i++)
        {
            TableColumn column = tblOrder.getColumnModel().getColumn(i);

            if (sizes[i] == 0)
            {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
            else
            {
                column.setPreferredWidth(sizes[i]);
            }
        }

        tblOrder.setDefaultRenderer(Object.class, new WarnaTableWithButton(tblOrder));
        tblOrder.setFocusable(false);
    }
    
    private void initTableTransaksi()
    {
        // Init list order ===========================
        Object[] row =
        {
            "Kd Periksa", "No Rawat", "Pasien", "Tgl Periksa", "Jam Periksa", "Status"
        };

        mdlTransaksi = new DefaultTableModel(null, row)
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

        tblTransaksi.setModel(mdlTransaksi);

        tblTransaksi.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tblTransaksi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 5; i++)
        {
            TableColumn column = tblTransaksi.getColumnModel().getColumn(i);

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

        tblTransaksi.setDefaultRenderer(Object.class, new WarnaTable());
    }
    
    private void initTableNonRm()
    {
        // Init list order ===========================
        Object[] row =
        {
            "Kd Periksa", "Pasien", "Tgl Periksa", "Jam Periksa", "Status"
        };

        mdlNonRm = new DefaultTableModel(null, row)
        {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                return false;
            }
        };

        tblNonRm.setModel(mdlNonRm);

        tblNonRm.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tblNonRm.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 5; i++)
        {
            TableColumn column = tblNonRm.getColumnModel().getColumn(i);

            if (i == 0)
            {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
            else if (i == 1)
            {
                column.setPreferredWidth(300);
            }
            else if (i == 2)
            {
                column.setPreferredWidth(200);
            }
            else if (i == 3)
            {
                column.setPreferredWidth(170);
            }
        }

        tblNonRm.setDefaultRenderer(Object.class, new WarnaTable());
    }

    private void initTxtHasil()
    {
        // Init Ghost text
        txtHasil.setFont(new java.awt.Font("Tahoma", 0, 11));
        txtHasil.setSelectionColor(new Color(50, 51, 0));
        txtHasil.setSelectedTextColor(new Color(255, 255, 0));
        txtHasil.setForeground(new Color(60, 80, 50));
        txtHasil.setBackground(GColors.Biru10);
        txtHasil.setVisible(false);
        btnEditHasil.setEnabled(false);

        txtHasil.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                saveHasil();
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                saveHasil();
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                saveHasil();
            }
        });

        //new GhostText(txtHasil, "Masukkan hasil...");
    }

    private void saveHasil()
    {
        if (pemeriksaanMap.containsKey(curPemeriksaan))
        {
            pemeriksaanMap.remove(curPemeriksaan);
        }

        pemeriksaanMap.put(curPemeriksaan, txtHasil.getText());
    }

    private void refreshSelGroup()
    {
        selGroup.clear();

        for (int a = 0; a < tblGroup.getRowCount(); a++)
        {
            if ((boolean) tblGroup.getValueAt(a, 0))
            {
                selGroup.add(tblGroup.getValueAt(a, 1).toString());
            }
        }
        
        tampilGroup();
    }
    
    private void refreshSelPemeriksaans()
    {
        selPemeriksaan.clear();

        for (int a = 0; a < tblPemeriksaan.getRowCount(); a++)
        {
            if ((boolean) tblPemeriksaan.getValueAt(a, 0))
            {
                selPemeriksaan.add(tblPemeriksaan.getValueAt(a, 1).toString());
            }
        }
        
        tampilPemeriksaan();
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

        Penjab = new widget.TextBox();
        Jk = new widget.TextBox();
        Umur = new widget.TextBox();
        Alamat = new widget.TextBox();
        jPopupMenuOrder = new javax.swing.JPopupMenu();
        menuUbahOrder = new javax.swing.JMenuItem();
        menuHapusOrder = new javax.swing.JMenuItem();
        menuCetakBillingOrder = new javax.swing.JMenuItem();
        jPopupMenuTransaksi = new javax.swing.JPopupMenu();
        menuUbahTransaksi = new javax.swing.JMenuItem();
        menuHapusTransaksi = new javax.swing.JMenuItem();
        menuCetakHasilTransaksi = new javax.swing.JMenuItem();
        menuCetakBillingTransaksi = new javax.swing.JMenuItem();
        jPopupMenuNonRm = new javax.swing.JPopupMenu();
        menuUbahNonRm = new javax.swing.JMenuItem();
        menuHapusNonRm = new javax.swing.JMenuItem();
        menuCetakHasilNonRm = new javax.swing.JMenuItem();
        menuCetakBillingNonRm = new javax.swing.JMenuItem();
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
        Tanggal = new widget.Tanggal();
        CmbJam = new widget.ComboBox();
        CmbMenit = new widget.ComboBox();
        CmbDetik = new widget.ComboBox();
        ChkJln = new widget.CekBox();
        jLabel16 = new widget.Label();
        jLabel15 = new widget.Label();
        jLabel7 = new widget.Label();
        jLabel13 = new widget.Label();
        txtKdPetugas = new widget.TextBox();
        btnPetugas = new widget.Button();
        NmPtg = new widget.TextBox();
        NmDokterPj = new widget.TextBox();
        txtKdDokterPj = new widget.TextBox();
        btnDokterPj = new widget.Button();
        ckbNonRm = new widget.CekBox();
        pnlNonRm = new javax.swing.JPanel();
        label1 = new widget.Label();
        txtNamaNonRm = new widget.TextBox();
        label2 = new widget.Label();
        txtUmurNonRm = new widget.TextBox();
        label3 = new widget.Label();
        txtAlamatNonRm = new widget.TextBox();
        pnlRm = new javax.swing.JPanel();
        jLabel3 = new widget.Label();
        txtNoRw = new widget.TextBox();
        txtNoRM = new widget.TextBox();
        txtNamaPasien = new widget.TextBox();
        btnCariPasien = new widget.Button();
        jLabel9 = new widget.Label();
        txtKdDokter = new widget.TextBox();
        txtNamaDokter = new widget.TextBox();
        jLabel12 = new widget.Label();
        txtKdKamar = new widget.TextBox();
        txtNamaKamar = new widget.TextBox();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        panelisi5 = new widget.panelisi();
        label10 = new widget.Label();
        txtCariGroup = new widget.TextBox();
        btnCariGroup = new widget.Button();
        btnAllGroup = new widget.Button();
        btnTambahGroup = new widget.Button();
        Scroll4 = new widget.ScrollPane();
        tblGroup = new widget.Table();
        jPanel5 = new javax.swing.JPanel();
        Scroll2 = new widget.ScrollPane();
        tblPemeriksaan = new widget.Table();
        panelisi6 = new widget.panelisi();
        label11 = new widget.Label();
        txtCariPemeriksaan = new widget.TextBox();
        btnCariPemeriksaan = new widget.Button();
        btnAllPemeriksaan = new widget.Button();
        btnTambahPemeriksaan = new widget.Button();
        jPanel7 = new javax.swing.JPanel();
        Scroll7 = new widget.ScrollPane();
        txtHasil = new widget.EditorPane();
        panelisi9 = new widget.panelisi();
        btnEditHasil = new widget.Button();
        panelisi3 = new widget.panelisi();
        jPanel6 = new javax.swing.JPanel();
        Scroll6 = new widget.ScrollPane();
        tblOrder = new widget.Table();
        panelisi7 = new widget.panelisi();
        jLabel35 = new widget.Label();
        tglOrder1 = new widget.Tanggal();
        jLabel36 = new widget.Label();
        tglOrder2 = new widget.Tanggal();
        label12 = new widget.Label();
        txtCariOrder = new widget.TextBox();
        btnCariOrder = new widget.Button();
        label13 = new widget.Label();
        btnHapusOrder = new widget.Button();
        btnAllOrder = new widget.Button();
        btnPrintOrder = new widget.Button();
        btnKeluarOrder = new widget.Button();
        panelisi2 = new widget.panelisi();
        jPanel4 = new javax.swing.JPanel();
        Scroll3 = new widget.ScrollPane();
        tblTransaksi = new widget.Table();
        panelisi8 = new widget.panelisi();
        jLabel37 = new widget.Label();
        tglTransaksi1 = new widget.Tanggal();
        jLabel38 = new widget.Label();
        tglTransaksi2 = new widget.Tanggal();
        label14 = new widget.Label();
        txtCariTransaksi = new widget.TextBox();
        btnCariTransaksi = new widget.Button();
        label15 = new widget.Label();
        btnHapusTransaksi = new widget.Button();
        btnAllTransaksi = new widget.Button();
        btnPrintTransaksi = new widget.Button();
        btnKeluarTransaksi = new widget.Button();
        panelisi4 = new widget.panelisi();
        jPanel8 = new javax.swing.JPanel();
        Scroll5 = new widget.ScrollPane();
        tblNonRm = new widget.Table();
        panelisi10 = new widget.panelisi();
        jLabel39 = new widget.Label();
        tglNonRm1 = new widget.Tanggal();
        jLabel40 = new widget.Label();
        tglNonRm2 = new widget.Tanggal();
        label16 = new widget.Label();
        txtCariNonRm = new widget.TextBox();
        btnCariNonRm = new widget.Button();
        label17 = new widget.Label();
        btnHapusNonRm = new widget.Button();
        btnAllNonRm = new widget.Button();
        btnPrintNonRm = new widget.Button();
        btnKeluarNonRm = new widget.Button();

        Penjab.setEditable(false);
        Penjab.setFocusTraversalPolicyProvider(true);
        Penjab.setName("Penjab"); // NOI18N
        Penjab.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
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

        jPopupMenuOrder.setForeground(new java.awt.Color(60, 80, 50));
        jPopupMenuOrder.setAutoscrolls(true);
        jPopupMenuOrder.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPopupMenuOrder.setFocusTraversalPolicyProvider(true);
        jPopupMenuOrder.setName("jPopupMenuOrder"); // NOI18N

        menuUbahOrder.setBackground(new java.awt.Color(255, 255, 255));
        menuUbahOrder.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuUbahOrder.setForeground(new java.awt.Color(60, 80, 50));
        menuUbahOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuUbahOrder.setText("Ubah");
        menuUbahOrder.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuUbahOrder.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuUbahOrder.setIconTextGap(5);
        menuUbahOrder.setName("menuUbahOrder"); // NOI18N
        menuUbahOrder.setPreferredSize(new java.awt.Dimension(220, 26));
        menuUbahOrder.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuUbahOrderActionPerformed(evt);
            }
        });
        jPopupMenuOrder.add(menuUbahOrder);

        menuHapusOrder.setBackground(new java.awt.Color(255, 255, 255));
        menuHapusOrder.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuHapusOrder.setForeground(new java.awt.Color(60, 80, 50));
        menuHapusOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuHapusOrder.setText("Hapus");
        menuHapusOrder.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuHapusOrder.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuHapusOrder.setIconTextGap(5);
        menuHapusOrder.setName("menuHapusOrder"); // NOI18N
        menuHapusOrder.setPreferredSize(new java.awt.Dimension(220, 26));
        menuHapusOrder.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuHapusOrderActionPerformed(evt);
            }
        });
        jPopupMenuOrder.add(menuHapusOrder);

        menuCetakBillingOrder.setBackground(new java.awt.Color(255, 255, 255));
        menuCetakBillingOrder.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuCetakBillingOrder.setForeground(new java.awt.Color(60, 80, 50));
        menuCetakBillingOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuCetakBillingOrder.setText("Cetak Billing");
        menuCetakBillingOrder.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuCetakBillingOrder.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuCetakBillingOrder.setIconTextGap(5);
        menuCetakBillingOrder.setName("menuCetakBillingOrder"); // NOI18N
        menuCetakBillingOrder.setPreferredSize(new java.awt.Dimension(220, 26));
        menuCetakBillingOrder.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuCetakBillingOrderActionPerformed(evt);
            }
        });
        jPopupMenuOrder.add(menuCetakBillingOrder);

        jPopupMenuTransaksi.setForeground(new java.awt.Color(60, 80, 50));
        jPopupMenuTransaksi.setAutoscrolls(true);
        jPopupMenuTransaksi.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPopupMenuTransaksi.setFocusTraversalPolicyProvider(true);
        jPopupMenuTransaksi.setName("jPopupMenuTransaksi"); // NOI18N

        menuUbahTransaksi.setBackground(new java.awt.Color(255, 255, 255));
        menuUbahTransaksi.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuUbahTransaksi.setForeground(new java.awt.Color(60, 80, 50));
        menuUbahTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuUbahTransaksi.setText("Ubah");
        menuUbahTransaksi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuUbahTransaksi.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuUbahTransaksi.setIconTextGap(5);
        menuUbahTransaksi.setName("menuUbahTransaksi"); // NOI18N
        menuUbahTransaksi.setPreferredSize(new java.awt.Dimension(220, 26));
        menuUbahTransaksi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuUbahTransaksiActionPerformed(evt);
            }
        });
        jPopupMenuTransaksi.add(menuUbahTransaksi);

        menuHapusTransaksi.setBackground(new java.awt.Color(255, 255, 255));
        menuHapusTransaksi.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuHapusTransaksi.setForeground(new java.awt.Color(60, 80, 50));
        menuHapusTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuHapusTransaksi.setText("Hapus");
        menuHapusTransaksi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuHapusTransaksi.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuHapusTransaksi.setIconTextGap(5);
        menuHapusTransaksi.setName("menuHapusTransaksi"); // NOI18N
        menuHapusTransaksi.setPreferredSize(new java.awt.Dimension(220, 26));
        menuHapusTransaksi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuHapusTransaksiActionPerformed(evt);
            }
        });
        jPopupMenuTransaksi.add(menuHapusTransaksi);

        menuCetakHasilTransaksi.setBackground(new java.awt.Color(255, 255, 255));
        menuCetakHasilTransaksi.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuCetakHasilTransaksi.setForeground(new java.awt.Color(60, 80, 50));
        menuCetakHasilTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuCetakHasilTransaksi.setText("Cetak Hasil");
        menuCetakHasilTransaksi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuCetakHasilTransaksi.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuCetakHasilTransaksi.setIconTextGap(5);
        menuCetakHasilTransaksi.setName("menuCetakHasilTransaksi"); // NOI18N
        menuCetakHasilTransaksi.setPreferredSize(new java.awt.Dimension(220, 26));
        menuCetakHasilTransaksi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuCetakHasilTransaksiActionPerformed(evt);
            }
        });
        jPopupMenuTransaksi.add(menuCetakHasilTransaksi);

        menuCetakBillingTransaksi.setBackground(new java.awt.Color(255, 255, 255));
        menuCetakBillingTransaksi.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuCetakBillingTransaksi.setForeground(new java.awt.Color(60, 80, 50));
        menuCetakBillingTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuCetakBillingTransaksi.setText("Cetak Billing");
        menuCetakBillingTransaksi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuCetakBillingTransaksi.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuCetakBillingTransaksi.setIconTextGap(5);
        menuCetakBillingTransaksi.setName("menuCetakBillingTransaksi"); // NOI18N
        menuCetakBillingTransaksi.setPreferredSize(new java.awt.Dimension(220, 26));
        menuCetakBillingTransaksi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuCetakBillingTransaksiActionPerformed(evt);
            }
        });
        jPopupMenuTransaksi.add(menuCetakBillingTransaksi);

        jPopupMenuNonRm.setForeground(new java.awt.Color(60, 80, 50));
        jPopupMenuNonRm.setAutoscrolls(true);
        jPopupMenuNonRm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPopupMenuNonRm.setFocusTraversalPolicyProvider(true);
        jPopupMenuNonRm.setName("jPopupMenuNonRm"); // NOI18N

        menuUbahNonRm.setBackground(new java.awt.Color(255, 255, 255));
        menuUbahNonRm.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuUbahNonRm.setForeground(new java.awt.Color(60, 80, 50));
        menuUbahNonRm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuUbahNonRm.setText("Ubah");
        menuUbahNonRm.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuUbahNonRm.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuUbahNonRm.setIconTextGap(5);
        menuUbahNonRm.setName("menuUbahNonRm"); // NOI18N
        menuUbahNonRm.setPreferredSize(new java.awt.Dimension(220, 26));
        menuUbahNonRm.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuUbahNonRmActionPerformed(evt);
            }
        });
        jPopupMenuNonRm.add(menuUbahNonRm);

        menuHapusNonRm.setBackground(new java.awt.Color(255, 255, 255));
        menuHapusNonRm.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuHapusNonRm.setForeground(new java.awt.Color(60, 80, 50));
        menuHapusNonRm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuHapusNonRm.setText("Hapus");
        menuHapusNonRm.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuHapusNonRm.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuHapusNonRm.setIconTextGap(5);
        menuHapusNonRm.setName("menuHapusNonRm"); // NOI18N
        menuHapusNonRm.setPreferredSize(new java.awt.Dimension(220, 26));
        menuHapusNonRm.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuHapusNonRmActionPerformed(evt);
            }
        });
        jPopupMenuNonRm.add(menuHapusNonRm);

        menuCetakHasilNonRm.setBackground(new java.awt.Color(255, 255, 255));
        menuCetakHasilNonRm.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuCetakHasilNonRm.setForeground(new java.awt.Color(60, 80, 50));
        menuCetakHasilNonRm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuCetakHasilNonRm.setText("Cetak Hasil");
        menuCetakHasilNonRm.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuCetakHasilNonRm.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuCetakHasilNonRm.setIconTextGap(5);
        menuCetakHasilNonRm.setName("menuCetakHasilNonRm"); // NOI18N
        menuCetakHasilNonRm.setPreferredSize(new java.awt.Dimension(220, 26));
        menuCetakHasilNonRm.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuCetakHasilNonRmActionPerformed(evt);
            }
        });
        jPopupMenuNonRm.add(menuCetakHasilNonRm);

        menuCetakBillingNonRm.setBackground(new java.awt.Color(255, 255, 255));
        menuCetakBillingNonRm.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuCetakBillingNonRm.setForeground(new java.awt.Color(60, 80, 50));
        menuCetakBillingNonRm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuCetakBillingNonRm.setText("Cetak Billing");
        menuCetakBillingNonRm.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuCetakBillingNonRm.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuCetakBillingNonRm.setIconTextGap(5);
        menuCetakBillingNonRm.setName("menuCetakBillingNonRm"); // NOI18N
        menuCetakBillingNonRm.setPreferredSize(new java.awt.Dimension(220, 26));
        menuCetakBillingNonRm.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuCetakBillingNonRmActionPerformed(evt);
            }
        });
        jPopupMenuNonRm.add(menuCetakBillingNonRm);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowOpened(java.awt.event.WindowEvent evt)
            {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Pemeriksaan Radiologi ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        tabPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 10))); // NOI18N
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
        BtnSimpan.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnSimpanActionPerformed(evt);
            }
        });
        BtnSimpan.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
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
        BtnBatal.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnBatalActionPerformed(evt);
            }
        });
        BtnBatal.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
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
        panelGlass8.add(BtnKeluar);

        panelisi1.add(panelGlass8, java.awt.BorderLayout.PAGE_END);

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setOpaque(false);
        FormInput.setPreferredSize(new java.awt.Dimension(560, 160));
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
        ChkInput.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ChkInputActionPerformed(evt);
            }
        });
        FormInput.add(ChkInput, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setPreferredSize(new java.awt.Dimension(560, 130));
        PanelInput.setLayout(null);

        Tanggal.setEditable(false);
        Tanggal.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "30-04-2018" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);
        Tanggal.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                TanggalKeyPressed(evt);
            }
        });
        PanelInput.add(Tanggal);
        Tanggal.setBounds(470, 100, 90, 23);

        CmbJam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        CmbJam.setName("CmbJam"); // NOI18N
        CmbJam.setOpaque(false);
        PanelInput.add(CmbJam);
        CmbJam.setBounds(620, 100, 42, 23);

        CmbMenit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        CmbMenit.setName("CmbMenit"); // NOI18N
        CmbMenit.setOpaque(false);
        PanelInput.add(CmbMenit);
        CmbMenit.setBounds(665, 100, 42, 23);

        CmbDetik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        CmbDetik.setName("CmbDetik"); // NOI18N
        CmbDetik.setOpaque(false);
        PanelInput.add(CmbDetik);
        CmbDetik.setBounds(710, 100, 42, 23);

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
        ChkJln.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ChkJlnActionPerformed(evt);
            }
        });
        PanelInput.add(ChkJln);
        ChkJln.setBounds(755, 100, 23, 23);

        jLabel16.setText("Jam :");
        jLabel16.setName("jLabel16"); // NOI18N
        PanelInput.add(jLabel16);
        jLabel16.setBounds(530, 100, 78, 23);

        jLabel15.setText("Tgl.Periksa :");
        jLabel15.setName("jLabel15"); // NOI18N
        PanelInput.add(jLabel15);
        jLabel15.setBounds(380, 100, 87, 23);

        jLabel7.setText("Dokter P.J. :");
        jLabel7.setName("jLabel7"); // NOI18N
        PanelInput.add(jLabel7);
        jLabel7.setBounds(10, 70, 92, 23);

        jLabel13.setText("Petugas :");
        jLabel13.setName("jLabel13"); // NOI18N
        PanelInput.add(jLabel13);
        jLabel13.setBounds(410, 70, 60, 23);

        txtKdPetugas.setEditable(false);
        txtKdPetugas.setName("txtKdPetugas"); // NOI18N
        txtKdPetugas.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdPetugasKeyPressed(evt);
            }
        });
        PanelInput.add(txtKdPetugas);
        txtKdPetugas.setBounds(475, 70, 80, 23);

        btnPetugas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPetugas.setMnemonic('2');
        btnPetugas.setToolTipText("Alt+2");
        btnPetugas.setName("btnPetugas"); // NOI18N
        btnPetugas.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPetugasActionPerformed(evt);
            }
        });
        PanelInput.add(btnPetugas);
        btnPetugas.setBounds(760, 70, 28, 23);

        NmPtg.setEditable(false);
        NmPtg.setName("NmPtg"); // NOI18N
        PanelInput.add(NmPtg);
        NmPtg.setBounds(560, 70, 195, 23);

        NmDokterPj.setEditable(false);
        NmDokterPj.setHighlighter(null);
        NmDokterPj.setName("NmDokterPj"); // NOI18N
        PanelInput.add(NmDokterPj);
        NmDokterPj.setBounds(195, 70, 180, 23);

        txtKdDokterPj.setEditable(false);
        txtKdDokterPj.setName("txtKdDokterPj"); // NOI18N
        PanelInput.add(txtKdDokterPj);
        txtKdDokterPj.setBounds(108, 70, 80, 23);

        btnDokterPj.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokterPj.setMnemonic('4');
        btnDokterPj.setToolTipText("ALt+4");
        btnDokterPj.setName("btnDokterPj"); // NOI18N
        btnDokterPj.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDokterPjActionPerformed(evt);
            }
        });
        PanelInput.add(btnDokterPj);
        btnDokterPj.setBounds(380, 70, 28, 23);

        ckbNonRm.setText("Non RM");
        ckbNonRm.setName("ckbNonRm"); // NOI18N
        PanelInput.add(ckbNonRm);
        ckbNonRm.setBounds(790, 10, 56, 16);

        pnlNonRm.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Non RM"));
        pnlNonRm.setName("pnlNonRm"); // NOI18N
        pnlNonRm.setOpaque(false);

        label1.setText("Nama");
        label1.setName("label1"); // NOI18N

        txtNamaNonRm.setName("txtNamaNonRm"); // NOI18N

        label2.setText("Umur");
        label2.setName("label2"); // NOI18N

        txtUmurNonRm.setName("txtUmurNonRm"); // NOI18N

        label3.setText("Alamat");
        label3.setName("label3"); // NOI18N

        txtAlamatNonRm.setName("txtAlamatNonRm"); // NOI18N

        javax.swing.GroupLayout pnlNonRmLayout = new javax.swing.GroupLayout(pnlNonRm);
        pnlNonRm.setLayout(pnlNonRmLayout);
        pnlNonRmLayout.setHorizontalGroup(
            pnlNonRmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNonRmLayout.createSequentialGroup()
                .addGroup(pnlNonRmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlNonRmLayout.createSequentialGroup()
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtNamaNonRm, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlNonRmLayout.createSequentialGroup()
                        .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtUmurNonRm, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 127, Short.MAX_VALUE))
            .addGroup(pnlNonRmLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(txtAlamatNonRm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlNonRmLayout.setVerticalGroup(
            pnlNonRmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNonRmLayout.createSequentialGroup()
                .addGroup(pnlNonRmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaNonRm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlNonRmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUmurNonRm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlNonRmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAlamatNonRm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelInput.add(pnlNonRm);
        pnlNonRm.setBounds(860, 10, 500, 110);

        pnlRm.setName("pnlRm"); // NOI18N
        pnlRm.setOpaque(false);
        pnlRm.setPreferredSize(new java.awt.Dimension(100, 40));
        pnlRm.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel3.setText("No.Rawat :");
        jLabel3.setName("jLabel3"); // NOI18N
        jLabel3.setPreferredSize(new java.awt.Dimension(93, 16));
        pnlRm.add(jLabel3);

        txtNoRw.setEditable(false);
        txtNoRw.setHighlighter(null);
        txtNoRw.setName("txtNoRw"); // NOI18N
        txtNoRw.setPreferredSize(new java.awt.Dimension(120, 24));
        pnlRm.add(txtNoRw);

        txtNoRM.setEditable(false);
        txtNoRM.setHighlighter(null);
        txtNoRM.setName("txtNoRM"); // NOI18N
        txtNoRM.setPreferredSize(new java.awt.Dimension(140, 24));
        pnlRm.add(txtNoRM);

        txtNamaPasien.setEditable(false);
        txtNamaPasien.setHighlighter(null);
        txtNamaPasien.setName("txtNamaPasien"); // NOI18N
        txtNamaPasien.setPreferredSize(new java.awt.Dimension(360, 24));
        pnlRm.add(txtNamaPasien);

        btnCariPasien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnCariPasien.setMnemonic('2');
        btnCariPasien.setToolTipText("Alt+2");
        btnCariPasien.setName("btnCariPasien"); // NOI18N
        btnCariPasien.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCariPasienActionPerformed(evt);
            }
        });
        pnlRm.add(btnCariPasien);

        jLabel9.setText("Dokter Perujuk :");
        jLabel9.setName("jLabel9"); // NOI18N
        pnlRm.add(jLabel9);

        txtKdDokter.setEditable(false);
        txtKdDokter.setName("txtKdDokter"); // NOI18N
        txtKdDokter.setPreferredSize(new java.awt.Dimension(120, 24));
        pnlRm.add(txtKdDokter);

        txtNamaDokter.setEditable(false);
        txtNamaDokter.setHighlighter(null);
        txtNamaDokter.setName("txtNamaDokter"); // NOI18N
        txtNamaDokter.setPreferredSize(new java.awt.Dimension(190, 24));
        txtNamaDokter.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtNamaDokterActionPerformed(evt);
            }
        });
        pnlRm.add(txtNamaDokter);

        jLabel12.setText("Ruang :");
        jLabel12.setName("jLabel12"); // NOI18N
        jLabel12.setPreferredSize(new java.awt.Dimension(55, 16));
        pnlRm.add(jLabel12);

        txtKdKamar.setEditable(false);
        txtKdKamar.setName("txtKdKamar"); // NOI18N
        txtKdKamar.setPreferredSize(new java.awt.Dimension(120, 24));
        pnlRm.add(txtKdKamar);

        txtNamaKamar.setEditable(false);
        txtNamaKamar.setName("txtNamaKamar"); // NOI18N
        txtNamaKamar.setPreferredSize(new java.awt.Dimension(160, 24));
        pnlRm.add(txtNamaKamar);

        PanelInput.add(pnlRm);
        pnlRm.setBounds(5, 5, 780, 60);

        FormInput.add(PanelInput, java.awt.BorderLayout.CENTER);

        panelisi1.add(FormInput, java.awt.BorderLayout.PAGE_START);

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(816, 102));
        jPanel1.setLayout(new java.awt.GridLayout(1, 3));

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

        txtCariGroup.setToolTipText("Alt+C");
        txtCariGroup.setName("txtCariGroup"); // NOI18N
        txtCariGroup.setPreferredSize(new java.awt.Dimension(160, 23));
        txtCariGroup.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCariGroupActionPerformed(evt);
            }
        });
        txtCariGroup.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCariGroupKeyPressed(evt);
            }
        });
        panelisi5.add(txtCariGroup);

        btnCariGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        btnCariGroup.setMnemonic('1');
        btnCariGroup.setToolTipText("Alt+1");
        btnCariGroup.setName("btnCariGroup"); // NOI18N
        btnCariGroup.setPreferredSize(new java.awt.Dimension(28, 23));
        btnCariGroup.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCariGroupActionPerformed(evt);
            }
        });
        btnCariGroup.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnCariGroupKeyPressed(evt);
            }
        });
        panelisi5.add(btnCariGroup);

        btnAllGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        btnAllGroup.setMnemonic('2');
        btnAllGroup.setToolTipText("Alt+2");
        btnAllGroup.setName("btnAllGroup"); // NOI18N
        btnAllGroup.setPreferredSize(new java.awt.Dimension(28, 23));
        btnAllGroup.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAllGroupActionPerformed(evt);
            }
        });
        btnAllGroup.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnAllGroupKeyPressed(evt);
            }
        });
        panelisi5.add(btnAllGroup);

        btnTambahGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        btnTambahGroup.setMnemonic('3');
        btnTambahGroup.setToolTipText("Alt+3");
        btnTambahGroup.setName("btnTambahGroup"); // NOI18N
        btnTambahGroup.setPreferredSize(new java.awt.Dimension(28, 23));
        btnTambahGroup.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTambahGroupActionPerformed(evt);
            }
        });
        btnTambahGroup.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnTambahGroupKeyPressed(evt);
            }
        });
        panelisi5.add(btnTambahGroup);

        jPanel3.add(panelisi5, java.awt.BorderLayout.PAGE_END);

        Scroll4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(245, 255, 235)));
        Scroll4.setName("Scroll4"); // NOI18N
        Scroll4.setOpaque(true);
        Scroll4.setPreferredSize(new java.awt.Dimension(452, 402));

        tblGroup.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tblGroup.setName("tblGroup"); // NOI18N
        tblGroup.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblGroupMouseClicked(evt);
            }
        });
        tblGroup.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                tblGroupPropertyChange(evt);
            }
        });
        tblGroup.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblGroupKeyPressed(evt);
            }
        });
        Scroll4.setViewportView(tblGroup);

        jPanel3.add(Scroll4, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(237, 242, 232)), ".: Pemeriksaan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        jPanel5.setName("jPanel5"); // NOI18N
        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(300, 102));
        jPanel5.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(245, 255, 235)));
        Scroll2.setName("Scroll2"); // NOI18N
        Scroll2.setOpaque(true);

        tblPemeriksaan.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tblPemeriksaan.setName("tblPemeriksaan"); // NOI18N
        tblPemeriksaan.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblPemeriksaanMouseClicked(evt);
            }
        });
        tblPemeriksaan.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                tblPemeriksaanPropertyChange(evt);
            }
        });
        tblPemeriksaan.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblPemeriksaanKeyPressed(evt);
            }
        });
        Scroll2.setViewportView(tblPemeriksaan);

        jPanel5.add(Scroll2, java.awt.BorderLayout.CENTER);

        panelisi6.setBorder(null);
        panelisi6.setName("panelisi6"); // NOI18N
        panelisi6.setPreferredSize(new java.awt.Dimension(100, 43));
        panelisi6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        label11.setText("Key Word :");
        label11.setName("label11"); // NOI18N
        label11.setPreferredSize(new java.awt.Dimension(68, 23));
        panelisi6.add(label11);

        txtCariPemeriksaan.setToolTipText("Alt+C");
        txtCariPemeriksaan.setName("txtCariPemeriksaan"); // NOI18N
        txtCariPemeriksaan.setPreferredSize(new java.awt.Dimension(160, 23));
        txtCariPemeriksaan.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCariPemeriksaanActionPerformed(evt);
            }
        });
        txtCariPemeriksaan.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCariPemeriksaanKeyPressed(evt);
            }
        });
        panelisi6.add(txtCariPemeriksaan);

        btnCariPemeriksaan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        btnCariPemeriksaan.setMnemonic('1');
        btnCariPemeriksaan.setToolTipText("Alt+1");
        btnCariPemeriksaan.setName("btnCariPemeriksaan"); // NOI18N
        btnCariPemeriksaan.setPreferredSize(new java.awt.Dimension(28, 23));
        btnCariPemeriksaan.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCariPemeriksaanActionPerformed(evt);
            }
        });
        btnCariPemeriksaan.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnCariPemeriksaanKeyPressed(evt);
            }
        });
        panelisi6.add(btnCariPemeriksaan);

        btnAllPemeriksaan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        btnAllPemeriksaan.setMnemonic('2');
        btnAllPemeriksaan.setToolTipText("Alt+2");
        btnAllPemeriksaan.setName("btnAllPemeriksaan"); // NOI18N
        btnAllPemeriksaan.setPreferredSize(new java.awt.Dimension(28, 23));
        btnAllPemeriksaan.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAllPemeriksaanActionPerformed(evt);
            }
        });
        btnAllPemeriksaan.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnAllPemeriksaanKeyPressed(evt);
            }
        });
        panelisi6.add(btnAllPemeriksaan);

        btnTambahPemeriksaan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        btnTambahPemeriksaan.setMnemonic('3');
        btnTambahPemeriksaan.setToolTipText("Alt+3");
        btnTambahPemeriksaan.setName("btnTambahPemeriksaan"); // NOI18N
        btnTambahPemeriksaan.setPreferredSize(new java.awt.Dimension(28, 23));
        btnTambahPemeriksaan.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTambahPemeriksaanActionPerformed(evt);
            }
        });
        btnTambahPemeriksaan.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnTambahPemeriksaanKeyPressed(evt);
            }
        });
        panelisi6.add(btnTambahPemeriksaan);

        jPanel5.add(panelisi6, java.awt.BorderLayout.PAGE_END);

        jPanel1.add(jPanel5);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(237, 242, 232)), ".: Hasil Pemeriksaan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        jPanel7.setName("jPanel7"); // NOI18N
        jPanel7.setOpaque(false);
        jPanel7.setPreferredSize(new java.awt.Dimension(300, 102));
        jPanel7.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(245, 255, 235)));
        Scroll7.setName("Scroll7"); // NOI18N
        Scroll7.setOpaque(true);

        txtHasil.setName("txtHasil"); // NOI18N
        Scroll7.setViewportView(txtHasil);

        jPanel7.add(Scroll7, java.awt.BorderLayout.CENTER);

        panelisi9.setBorder(null);
        panelisi9.setName("panelisi9"); // NOI18N
        panelisi9.setPreferredSize(new java.awt.Dimension(100, 43));
        panelisi9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        btnEditHasil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/011.png"))); // NOI18N
        btnEditHasil.setMnemonic('3');
        btnEditHasil.setToolTipText("Alt+3");
        btnEditHasil.setName("btnEditHasil"); // NOI18N
        btnEditHasil.setPreferredSize(new java.awt.Dimension(28, 23));
        btnEditHasil.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnEditHasilActionPerformed(evt);
            }
        });
        panelisi9.add(btnEditHasil);

        jPanel7.add(panelisi9, java.awt.BorderLayout.PAGE_END);

        jPanel1.add(jPanel7);

        panelisi1.add(jPanel1, java.awt.BorderLayout.CENTER);

        tabPane.addTab("Transaksi", panelisi1);

        panelisi3.setName("panelisi3"); // NOI18N
        panelisi3.setLayout(new java.awt.BorderLayout());

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(237, 242, 232)), ".: List Order ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        jPanel6.setName("jPanel6"); // NOI18N
        jPanel6.setOpaque(false);
        jPanel6.setPreferredSize(new java.awt.Dimension(300, 102));
        jPanel6.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(245, 255, 235)));
        Scroll6.setComponentPopupMenu(jPopupMenuOrder);
        Scroll6.setName("Scroll6"); // NOI18N
        Scroll6.setOpaque(true);

        tblOrder.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tblOrder.setComponentPopupMenu(jPopupMenuOrder);
        tblOrder.setName("tblOrder"); // NOI18N
        tblOrder.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblOrderMouseClicked(evt);
            }
        });
        tblOrder.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblOrderKeyPressed(evt);
            }
        });
        Scroll6.setViewportView(tblOrder);

        jPanel6.add(Scroll6, java.awt.BorderLayout.CENTER);

        panelisi7.setName("panelisi7"); // NOI18N
        panelisi7.setPreferredSize(new java.awt.Dimension(100, 56));
        panelisi7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel35.setText("Tgl :");
        jLabel35.setName("jLabel35"); // NOI18N
        jLabel35.setPreferredSize(new java.awt.Dimension(58, 23));
        panelisi7.add(jLabel35);

        tglOrder1.setEditable(false);
        tglOrder1.setForeground(new java.awt.Color(50, 70, 50));
        tglOrder1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "30-04-2018" }));
        tglOrder1.setDisplayFormat("dd-MM-yyyy");
        tglOrder1.setName("tglOrder1"); // NOI18N
        tglOrder1.setOpaque(false);
        tglOrder1.setPreferredSize(new java.awt.Dimension(100, 23));
        panelisi7.add(tglOrder1);

        jLabel36.setText("s.d");
        jLabel36.setName("jLabel36"); // NOI18N
        jLabel36.setPreferredSize(new java.awt.Dimension(18, 23));
        panelisi7.add(jLabel36);

        tglOrder2.setEditable(false);
        tglOrder2.setForeground(new java.awt.Color(50, 70, 50));
        tglOrder2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "30-04-2018" }));
        tglOrder2.setDisplayFormat("dd-MM-yyyy");
        tglOrder2.setName("tglOrder2"); // NOI18N
        tglOrder2.setOpaque(false);
        tglOrder2.setPreferredSize(new java.awt.Dimension(100, 23));
        panelisi7.add(tglOrder2);

        label12.setText("Key Word :");
        label12.setName("label12"); // NOI18N
        label12.setPreferredSize(new java.awt.Dimension(70, 23));
        panelisi7.add(label12);

        txtCariOrder.setName("txtCariOrder"); // NOI18N
        txtCariOrder.setPreferredSize(new java.awt.Dimension(170, 23));
        txtCariOrder.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCariOrderKeyPressed(evt);
            }
        });
        panelisi7.add(txtCariOrder);

        btnCariOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        btnCariOrder.setMnemonic('5');
        btnCariOrder.setToolTipText("Alt+5");
        btnCariOrder.setName("btnCariOrder"); // NOI18N
        btnCariOrder.setPreferredSize(new java.awt.Dimension(28, 23));
        btnCariOrder.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCariOrderActionPerformed(evt);
            }
        });
        btnCariOrder.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnCariOrderKeyPressed(evt);
            }
        });
        panelisi7.add(btnCariOrder);

        label13.setName("label13"); // NOI18N
        label13.setPreferredSize(new java.awt.Dimension(100, 30));
        panelisi7.add(label13);

        btnHapusOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        btnHapusOrder.setMnemonic('H');
        btnHapusOrder.setText("Hapus");
        btnHapusOrder.setToolTipText("Alt+H");
        btnHapusOrder.setName("btnHapusOrder"); // NOI18N
        btnHapusOrder.setPreferredSize(new java.awt.Dimension(100, 30));
        btnHapusOrder.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnHapusOrderActionPerformed(evt);
            }
        });
        btnHapusOrder.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnHapusOrderKeyPressed(evt);
            }
        });
        panelisi7.add(btnHapusOrder);

        btnAllOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        btnAllOrder.setMnemonic('M');
        btnAllOrder.setText("Semua");
        btnAllOrder.setToolTipText("Alt+M");
        btnAllOrder.setName("btnAllOrder"); // NOI18N
        btnAllOrder.setPreferredSize(new java.awt.Dimension(100, 30));
        btnAllOrder.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAllOrderActionPerformed(evt);
            }
        });
        btnAllOrder.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnAllOrderKeyPressed(evt);
            }
        });
        panelisi7.add(btnAllOrder);

        btnPrintOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        btnPrintOrder.setMnemonic('T');
        btnPrintOrder.setText("Cetak");
        btnPrintOrder.setToolTipText("Alt+T");
        btnPrintOrder.setName("btnPrintOrder"); // NOI18N
        btnPrintOrder.setPreferredSize(new java.awt.Dimension(100, 30));
        btnPrintOrder.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrintOrderActionPerformed(evt);
            }
        });
        btnPrintOrder.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnPrintOrderKeyPressed(evt);
            }
        });
        panelisi7.add(btnPrintOrder);

        btnKeluarOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        btnKeluarOrder.setMnemonic('K');
        btnKeluarOrder.setText("Keluar");
        btnKeluarOrder.setToolTipText("Alt+K");
        btnKeluarOrder.setName("btnKeluarOrder"); // NOI18N
        btnKeluarOrder.setPreferredSize(new java.awt.Dimension(100, 30));
        btnKeluarOrder.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnKeluarOrderActionPerformed(evt);
            }
        });
        btnKeluarOrder.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnKeluarOrderKeyPressed(evt);
            }
        });
        panelisi7.add(btnKeluarOrder);

        jPanel6.add(panelisi7, java.awt.BorderLayout.PAGE_END);

        panelisi3.add(jPanel6, java.awt.BorderLayout.CENTER);

        tabPane.addTab("List Order", panelisi3);

        panelisi2.setName("panelisi2"); // NOI18N
        panelisi2.setLayout(new java.awt.BorderLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(237, 242, 232)), ".: List Transaksi ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.setOpaque(false);
        jPanel4.setPreferredSize(new java.awt.Dimension(300, 102));
        jPanel4.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(245, 255, 235)));
        Scroll3.setComponentPopupMenu(jPopupMenuOrder);
        Scroll3.setName("Scroll3"); // NOI18N
        Scroll3.setOpaque(true);

        tblTransaksi.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tblTransaksi.setComponentPopupMenu(jPopupMenuTransaksi);
        tblTransaksi.setName("tblTransaksi"); // NOI18N
        tblTransaksi.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblTransaksiMouseClicked(evt);
            }
        });
        tblTransaksi.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblTransaksiKeyPressed(evt);
            }
        });
        Scroll3.setViewportView(tblTransaksi);

        jPanel4.add(Scroll3, java.awt.BorderLayout.CENTER);

        panelisi8.setName("panelisi8"); // NOI18N
        panelisi8.setPreferredSize(new java.awt.Dimension(100, 56));
        panelisi8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel37.setText("Tgl :");
        jLabel37.setName("jLabel37"); // NOI18N
        jLabel37.setPreferredSize(new java.awt.Dimension(58, 23));
        panelisi8.add(jLabel37);

        tglTransaksi1.setEditable(false);
        tglTransaksi1.setForeground(new java.awt.Color(50, 70, 50));
        tglTransaksi1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "30-04-2018" }));
        tglTransaksi1.setDisplayFormat("dd-MM-yyyy");
        tglTransaksi1.setName("tglTransaksi1"); // NOI18N
        tglTransaksi1.setOpaque(false);
        tglTransaksi1.setPreferredSize(new java.awt.Dimension(100, 23));
        panelisi8.add(tglTransaksi1);

        jLabel38.setText("s.d");
        jLabel38.setName("jLabel38"); // NOI18N
        jLabel38.setPreferredSize(new java.awt.Dimension(18, 23));
        panelisi8.add(jLabel38);

        tglTransaksi2.setEditable(false);
        tglTransaksi2.setForeground(new java.awt.Color(50, 70, 50));
        tglTransaksi2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "30-04-2018" }));
        tglTransaksi2.setDisplayFormat("dd-MM-yyyy");
        tglTransaksi2.setName("tglTransaksi2"); // NOI18N
        tglTransaksi2.setOpaque(false);
        tglTransaksi2.setPreferredSize(new java.awt.Dimension(100, 23));
        panelisi8.add(tglTransaksi2);

        label14.setText("Key Word :");
        label14.setName("label14"); // NOI18N
        label14.setPreferredSize(new java.awt.Dimension(70, 23));
        panelisi8.add(label14);

        txtCariTransaksi.setName("txtCariTransaksi"); // NOI18N
        txtCariTransaksi.setPreferredSize(new java.awt.Dimension(170, 23));
        txtCariTransaksi.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCariTransaksiKeyPressed(evt);
            }
        });
        panelisi8.add(txtCariTransaksi);

        btnCariTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        btnCariTransaksi.setMnemonic('5');
        btnCariTransaksi.setToolTipText("Alt+5");
        btnCariTransaksi.setName("btnCariTransaksi"); // NOI18N
        btnCariTransaksi.setPreferredSize(new java.awt.Dimension(28, 23));
        btnCariTransaksi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCariTransaksiActionPerformed(evt);
            }
        });
        btnCariTransaksi.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnCariTransaksiKeyPressed(evt);
            }
        });
        panelisi8.add(btnCariTransaksi);

        label15.setName("label15"); // NOI18N
        label15.setPreferredSize(new java.awt.Dimension(100, 30));
        panelisi8.add(label15);

        btnHapusTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        btnHapusTransaksi.setMnemonic('H');
        btnHapusTransaksi.setText("Hapus");
        btnHapusTransaksi.setToolTipText("Alt+H");
        btnHapusTransaksi.setName("btnHapusTransaksi"); // NOI18N
        btnHapusTransaksi.setPreferredSize(new java.awt.Dimension(100, 30));
        btnHapusTransaksi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnHapusTransaksiActionPerformed(evt);
            }
        });
        btnHapusTransaksi.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnHapusTransaksiKeyPressed(evt);
            }
        });
        panelisi8.add(btnHapusTransaksi);

        btnAllTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        btnAllTransaksi.setMnemonic('M');
        btnAllTransaksi.setText("Semua");
        btnAllTransaksi.setToolTipText("Alt+M");
        btnAllTransaksi.setName("btnAllTransaksi"); // NOI18N
        btnAllTransaksi.setPreferredSize(new java.awt.Dimension(100, 30));
        btnAllTransaksi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAllTransaksiActionPerformed(evt);
            }
        });
        btnAllTransaksi.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnAllTransaksiKeyPressed(evt);
            }
        });
        panelisi8.add(btnAllTransaksi);

        btnPrintTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        btnPrintTransaksi.setMnemonic('T');
        btnPrintTransaksi.setText("Cetak");
        btnPrintTransaksi.setToolTipText("Alt+T");
        btnPrintTransaksi.setName("btnPrintTransaksi"); // NOI18N
        btnPrintTransaksi.setPreferredSize(new java.awt.Dimension(100, 30));
        btnPrintTransaksi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrintTransaksiActionPerformed(evt);
            }
        });
        btnPrintTransaksi.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnPrintTransaksiKeyPressed(evt);
            }
        });
        panelisi8.add(btnPrintTransaksi);

        btnKeluarTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        btnKeluarTransaksi.setMnemonic('K');
        btnKeluarTransaksi.setText("Keluar");
        btnKeluarTransaksi.setToolTipText("Alt+K");
        btnKeluarTransaksi.setName("btnKeluarTransaksi"); // NOI18N
        btnKeluarTransaksi.setPreferredSize(new java.awt.Dimension(100, 30));
        btnKeluarTransaksi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnKeluarTransaksiActionPerformed(evt);
            }
        });
        btnKeluarTransaksi.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnKeluarTransaksiKeyPressed(evt);
            }
        });
        panelisi8.add(btnKeluarTransaksi);

        jPanel4.add(panelisi8, java.awt.BorderLayout.PAGE_END);

        panelisi2.add(jPanel4, java.awt.BorderLayout.CENTER);

        tabPane.addTab("List Transaksi", panelisi2);

        panelisi4.setName("panelisi4"); // NOI18N
        panelisi4.setLayout(new java.awt.BorderLayout());

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(237, 242, 232)), ".: List Transaksi ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        jPanel8.setName("jPanel8"); // NOI18N
        jPanel8.setOpaque(false);
        jPanel8.setPreferredSize(new java.awt.Dimension(300, 102));
        jPanel8.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(245, 255, 235)));
        Scroll5.setComponentPopupMenu(jPopupMenuOrder);
        Scroll5.setName("Scroll5"); // NOI18N
        Scroll5.setOpaque(true);

        tblNonRm.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tblNonRm.setComponentPopupMenu(jPopupMenuNonRm);
        tblNonRm.setName("tblNonRm"); // NOI18N
        tblNonRm.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblNonRmMouseClicked(evt);
            }
        });
        tblNonRm.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblNonRmKeyPressed(evt);
            }
        });
        Scroll5.setViewportView(tblNonRm);

        jPanel8.add(Scroll5, java.awt.BorderLayout.CENTER);

        panelisi10.setName("panelisi10"); // NOI18N
        panelisi10.setPreferredSize(new java.awt.Dimension(100, 56));
        panelisi10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel39.setText("Tgl :");
        jLabel39.setName("jLabel39"); // NOI18N
        jLabel39.setPreferredSize(new java.awt.Dimension(58, 23));
        panelisi10.add(jLabel39);

        tglNonRm1.setEditable(false);
        tglNonRm1.setForeground(new java.awt.Color(50, 70, 50));
        tglNonRm1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "30-04-2018" }));
        tglNonRm1.setDisplayFormat("dd-MM-yyyy");
        tglNonRm1.setName("tglNonRm1"); // NOI18N
        tglNonRm1.setOpaque(false);
        tglNonRm1.setPreferredSize(new java.awt.Dimension(100, 23));
        panelisi10.add(tglNonRm1);

        jLabel40.setText("s.d");
        jLabel40.setName("jLabel40"); // NOI18N
        jLabel40.setPreferredSize(new java.awt.Dimension(18, 23));
        panelisi10.add(jLabel40);

        tglNonRm2.setEditable(false);
        tglNonRm2.setForeground(new java.awt.Color(50, 70, 50));
        tglNonRm2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "30-04-2018" }));
        tglNonRm2.setDisplayFormat("dd-MM-yyyy");
        tglNonRm2.setName("tglNonRm2"); // NOI18N
        tglNonRm2.setOpaque(false);
        tglNonRm2.setPreferredSize(new java.awt.Dimension(100, 23));
        panelisi10.add(tglNonRm2);

        label16.setText("Key Word :");
        label16.setName("label16"); // NOI18N
        label16.setPreferredSize(new java.awt.Dimension(70, 23));
        panelisi10.add(label16);

        txtCariNonRm.setName("txtCariNonRm"); // NOI18N
        txtCariNonRm.setPreferredSize(new java.awt.Dimension(170, 23));
        txtCariNonRm.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCariNonRmKeyPressed(evt);
            }
        });
        panelisi10.add(txtCariNonRm);

        btnCariNonRm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        btnCariNonRm.setMnemonic('5');
        btnCariNonRm.setToolTipText("Alt+5");
        btnCariNonRm.setName("btnCariNonRm"); // NOI18N
        btnCariNonRm.setPreferredSize(new java.awt.Dimension(28, 23));
        btnCariNonRm.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCariNonRmActionPerformed(evt);
            }
        });
        btnCariNonRm.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnCariNonRmKeyPressed(evt);
            }
        });
        panelisi10.add(btnCariNonRm);

        label17.setName("label17"); // NOI18N
        label17.setPreferredSize(new java.awt.Dimension(100, 30));
        panelisi10.add(label17);

        btnHapusNonRm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        btnHapusNonRm.setMnemonic('H');
        btnHapusNonRm.setText("Hapus");
        btnHapusNonRm.setToolTipText("Alt+H");
        btnHapusNonRm.setName("btnHapusNonRm"); // NOI18N
        btnHapusNonRm.setPreferredSize(new java.awt.Dimension(100, 30));
        btnHapusNonRm.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnHapusNonRmActionPerformed(evt);
            }
        });
        btnHapusNonRm.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnHapusNonRmKeyPressed(evt);
            }
        });
        panelisi10.add(btnHapusNonRm);

        btnAllNonRm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        btnAllNonRm.setMnemonic('M');
        btnAllNonRm.setText("Semua");
        btnAllNonRm.setToolTipText("Alt+M");
        btnAllNonRm.setName("btnAllNonRm"); // NOI18N
        btnAllNonRm.setPreferredSize(new java.awt.Dimension(100, 30));
        btnAllNonRm.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAllNonRmActionPerformed(evt);
            }
        });
        btnAllNonRm.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnAllNonRmKeyPressed(evt);
            }
        });
        panelisi10.add(btnAllNonRm);

        btnPrintNonRm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        btnPrintNonRm.setMnemonic('T');
        btnPrintNonRm.setText("Cetak");
        btnPrintNonRm.setToolTipText("Alt+T");
        btnPrintNonRm.setName("btnPrintNonRm"); // NOI18N
        btnPrintNonRm.setPreferredSize(new java.awt.Dimension(100, 30));
        btnPrintNonRm.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrintNonRmActionPerformed(evt);
            }
        });
        btnPrintNonRm.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnPrintNonRmKeyPressed(evt);
            }
        });
        panelisi10.add(btnPrintNonRm);

        btnKeluarNonRm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        btnKeluarNonRm.setMnemonic('K');
        btnKeluarNonRm.setText("Keluar");
        btnKeluarNonRm.setToolTipText("Alt+K");
        btnKeluarNonRm.setName("btnKeluarNonRm"); // NOI18N
        btnKeluarNonRm.setPreferredSize(new java.awt.Dimension(100, 30));
        btnKeluarNonRm.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnKeluarNonRmActionPerformed(evt);
            }
        });
        btnKeluarNonRm.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnKeluarNonRmKeyPressed(evt);
            }
        });
        panelisi10.add(btnKeluarNonRm);

        jPanel8.add(panelisi10, java.awt.BorderLayout.PAGE_END);

        panelisi4.add(jPanel8, java.awt.BorderLayout.CENTER);

        tabPane.addTab("Non RM", panelisi4);

        internalFrame1.add(tabPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);
        internalFrame1.getAccessibleContext().setAccessibleName("::[ Input Data Order Laboratorium ]::");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PenjabKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PenjabKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PenjabKeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

    }//GEN-LAST:event_formWindowOpened

    private void menuUbahOrderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuUbahOrderActionPerformed
    {//GEN-HEADEREND:event_menuUbahOrderActionPerformed
        if (tblOrder.getSelectedRow() != -1)
            dariOrder(tblOrder.getValueAt(tblOrder.getSelectedRow(), 0).toString());
    }//GEN-LAST:event_menuUbahOrderActionPerformed

    private void menuHapusOrderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuHapusOrderActionPerformed
    {//GEN-HEADEREND:event_menuHapusOrderActionPerformed
        hapusOrder();
    }//GEN-LAST:event_menuHapusOrderActionPerformed

    private void tblTransaksiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblTransaksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTransaksiKeyPressed

    private void tblTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTransaksiMouseClicked
        if (evt.getClickCount() == 2 && tblTransaksi.getSelectedRow() != -1)
        {
            dariTransaksi(tblTransaksi.getValueAt(tblTransaksi.getSelectedRow(), 0).toString());
        }
    }//GEN-LAST:event_tblTransaksiMouseClicked

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
                        txtCariGroup.setText("");
                        txtCariGroup.requestFocus();
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

    private void tblPemeriksaanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPemeriksaanMouseClicked
        if (evt.getClickCount() == 1 && tblPemeriksaan.getSelectedRow() > -1)
        {
            if ((boolean) tblPemeriksaan.getValueAt(tblPemeriksaan.getSelectedRow(), 0))
            {
                txtHasil.setVisible(true);
                curPemeriksaan = tblPemeriksaan.getValueAt(tblPemeriksaan.getSelectedRow(), 1).toString();
                txtHasil.setText(pemeriksaanMap.get(curPemeriksaan));
                btnEditHasil.setEnabled(true);
            }
            else
            {
                txtHasil.setVisible(false);
                btnEditHasil.setEnabled(false);
            }
        }
    }//GEN-LAST:event_tblPemeriksaanMouseClicked

    private void btnTambahGroupKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnTambahGroupKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            btnTambahPemeriksaanActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, btnAllGroup, txtCariGroup);
        }
    }//GEN-LAST:event_btnTambahGroupKeyPressed

    private void btnTambahGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahGroupActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        DlgJnsPerawatanRadiologi produsen = new DlgJnsPerawatanRadiologi(null, false);
        produsen.emptTeks();
        produsen.isCek();
        produsen.setSize(internalFrame1.getWidth(), internalFrame1.getHeight());
        produsen.setLocationRelativeTo(internalFrame1);
        produsen.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_btnTambahGroupActionPerformed

    private void btnAllGroupKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAllGroupKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            btnAllPemeriksaanActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, btnCariGroup, btnTambahGroup);
        }
    }//GEN-LAST:event_btnAllGroupKeyPressed

    private void btnAllGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllGroupActionPerformed
        txtCariGroup.setText("");
        tampilGroup();
        tampilPemeriksaan();
    }//GEN-LAST:event_btnAllGroupActionPerformed

    private void btnCariGroupKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCariGroupKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            tampilPemeriksaan();
        }
        else
        {
            Valid.pindah(evt, txtCariGroup, btnAllGroup);
        }
    }//GEN-LAST:event_btnCariGroupKeyPressed

    private void btnCariGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariGroupActionPerformed
        tampilPemeriksaan();
    }//GEN-LAST:event_btnCariGroupActionPerformed

    private void txtCariGroupKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariGroupKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            btnCariPemeriksaanActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
        {
            btnCariGroup.requestFocus();
        }
        else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP)
        {
            btnTambahGroup.requestFocus();
        }
    }//GEN-LAST:event_txtCariGroupKeyPressed

    private void txtCariGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariGroupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariGroupActionPerformed

    private void txtNamaDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaDokterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaDokterActionPerformed

    private void ChkJlnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkJlnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChkJlnActionPerformed

    private void TanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalKeyPressed
        Valid.pindah(evt, txtKdDokter, txtCariGroup);
    }//GEN-LAST:event_TanggalKeyPressed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            dispose();
        }
    }//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluarActionPerformed

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

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            BtnBatalActionPerformed(null);
        }
        else
        {

        }
    }//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        isEdit = false;
        
        isReset();
        BtnSimpan.setText("Simpan");
    }//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            BtnSimpanActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtCariGroup, BtnBatal);
        }
    }//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if (!ckbNonRm.isSelected() && (txtNoRw.getText().equals("") || txtNoRM.getText().equals("") || txtNamaPasien.getText().equals("")))
        {
            Valid.textKosong(txtNoRw, "Pasien");
        }
        else if (ckbNonRm.isSelected() && txtNamaNonRm.getText().isEmpty())
        {
            Valid.textKosong(txtNamaNonRm, "Nama");
        }
        else if (txtKdDokterPj.getText().equals(""))
        {
            Valid.textKosong(txtKdDokterPj, "Dokter Penjab");
        }
        else if (txtKdPetugas.getText().equals(""))
        {
            Valid.textKosong(txtKdDokter, "Petugas");
        }
        else if (mdlPemeriksaan.getRowCount() == 0)
        {
            Valid.textKosong(txtCariGroup, "Data Pemeriksaan");
        }
        else if (!ckbNonRm.isSelected() && Sequel.cariRegistrasi(txtNoRw.getText()) > 0)
        {
            JOptionPane.showMessageDialog(rootPane, "Data billing sudah terverifikasi, data tidak boleh dihapus.\nSilahkan hubungi bagian kasir/keuangan ..!!");
            txtCariGroup.requestFocus();
        }
        else
        {
            int reply = JOptionPane.showConfirmDialog(rootPane, "Eeiiiiiits, udah bener belum data yang mau disimpan..??", "Konfirmasi", JOptionPane.YES_NO_OPTION);

            if (reply == JOptionPane.YES_OPTION)
            {
                ChkJln.setSelected(false);
                GQuery.setAutoCommit(false);

                boolean success = true;
                String detTable;

                // NON RM ========
                if (ckbNonRm.isSelected())
                {
                    if (isEdit)
                    {
                        success &= new GQuery()
                                .a("UPDATE pemeriksaan_radiologi_nonrm SET")
                                .a("nama = {nama},")
                                .a("umur = {umur},")
                                .a("alamat = {alamat},")
                                .a("nip = {nip},")
                                .a("tgl_selesai = {tgl_periksa},")
                                .a("jam_selesai = {jam},")
                                .a("kd_dokter_pj = {kd_dokter_pj},")
                                .a("proses = 'Sudah'")
                                .a("WHERE kd_periksa = {kd_periksa}")
                                .set("nama", txtNamaNonRm.getText())
                                .set("umur", txtUmurNonRm.getText())
                                .set("alamat", txtAlamatNonRm.getText())
                                .set("nip", txtKdPetugas.getText())
                                .set("tgl_periksa", Valid.SetTgl(Tanggal.getSelectedItem().toString()))
                                .set("jam", CmbJam.getSelectedItem() + ":" + CmbMenit.getSelectedItem() + ":" + CmbDetik.getSelectedItem())
                                .set("kd_dokter_pj", txtKdDokterPj.getText())
                                .set("kd_periksa", kdPeriksa)
                                .write();

                        success &= new GQuery()
                                .a("DELETE FROM det_pemeriksaan_radiologi_nonrm WHERE kd_periksa = {kd_periksa}")
                                .set("kd_periksa", kdPeriksa)
                                .write();
                    }
                    // INSERT NON RM ========
                    else
                    {
                        kdPeriksa = Sequel.autoNumber("pemeriksaan_radiologi_nonrm", "kd_periksa");

                        success &= new GQuery()
                                .a("INSERT INTO pemeriksaan_radiologi_nonrm")
                                .a("VALUES ({kd_periksa}, {nama}, {umur}, {alamat}, {nip}, {tgl_periksa}, {jam}, {tgl_periksa}, {jam},")
                                .a("{kd_dokter_pj}, {proses})")
                                .set("kd_periksa", kdPeriksa)
                                .set("nama", txtNamaNonRm.getText())
                                .set("umur", txtUmurNonRm.getText())
                                .set("alamat", txtAlamatNonRm.getText())
                                .set("nip", txtKdPetugas.getText())
                                .set("tgl_periksa", Valid.SetTgl(Tanggal.getSelectedItem().toString()))
                                .set("jam", CmbJam.getSelectedItem() + ":" + CmbMenit.getSelectedItem() + ":" + CmbDetik.getSelectedItem())
                                .set("kd_dokter_pj", txtKdDokterPj.getText())
                                .set("proses", "Sudah")
                                .write();
                    }
                    
                    detTable = "det_pemeriksaan_radiologi_nonrm";
                }
                // Dengan RM ===
                else
                {
                    // EDIT RM ============
                    if (isEdit)
                    {
                        success &= new GQuery()
                                .a("UPDATE pemeriksaan_radiologi SET")
                                .a("nip = {nip},")
                                .a("tgl_selesai = {tgl_periksa},")
                                .a("jam_selesai = {jam},")
                                .a("kd_dokter_perujuk = {kd_dokter_perujuk},")
                                .a("kd_dokter_pj = {kd_dokter_pj},")
                                .a("proses = 'Sudah'")
                                .a("WHERE kd_periksa = {kd_periksa}")
                                .set("nip", txtKdPetugas.getText())
                                .set("tgl_periksa", Valid.SetTgl(Tanggal.getSelectedItem().toString()))
                                .set("jam", CmbJam.getSelectedItem() + ":" + CmbMenit.getSelectedItem() + ":" + CmbDetik.getSelectedItem())
                                .set("kd_dokter_perujuk", txtKdDokter.getText())
                                .set("kd_dokter_pj", txtKdDokterPj.getText())
                                .set("kd_periksa", kdPeriksa)
                                .write();

                        success &= new GQuery()
                                .a("DELETE FROM det_pemeriksaan_radiologi WHERE kd_periksa = {kd_periksa}")
                                .set("kd_periksa", kdPeriksa)
                                .write();
                    }
                    // INSERT RM ===========
                    else
                    {
                        kdPeriksa = Sequel.autoNumber("pemeriksaan_radiologi", "kd_periksa");

                        success &= new GQuery()
                                .a("INSERT INTO pemeriksaan_radiologi")
                                .a("VALUES ({kd_periksa}, {no_rawat}, {nip}, {tgl_periksa}, {jam}, {tgl_periksa}, {jam},")
                                .a("{kd_dokter_perujuk}, {kd_dokter_pj}, {status}, {proses})")
                                .set("kd_periksa", kdPeriksa)
                                .set("no_rawat", txtNoRw.getText())
                                .set("nip", txtKdPetugas.getText())
                                .set("tgl_periksa", Valid.SetTgl(Tanggal.getSelectedItem().toString()))
                                .set("jam", CmbJam.getSelectedItem() + ":" + CmbMenit.getSelectedItem() + ":" + CmbDetik.getSelectedItem())
                                .set("kd_dokter_perujuk", txtKdDokter.getText())
                                .set("kd_dokter_pj", txtKdDokterPj.getText())
                                .set("status", status)
                                .set("proses", "Sudah")
                                .write();
                    }

                    detTable = "det_pemeriksaan_radiologi";
                }
                
                // Perulangan menyimpan ke tabel detail periksa hd
                for (i = 0; i < tblPemeriksaan.getRowCount(); i++)
                {
                    if (tblPemeriksaan.getValueAt(i, 0).toString().equals("true"))
                    {
                        success &= Sequel.menyimpantf2(detTable, "?,?,?,?,?,?,?,?,?,?,?", "-", 11, new String[]
                        {
                            kdPeriksa,
                            tblPemeriksaan.getValueAt(i, 1).toString(),
                            tblPemeriksaan.getValueAt(i, 3).toString(),
                            tblPemeriksaan.getValueAt(i, 4).toString(),
                            tblPemeriksaan.getValueAt(i, 5).toString(),
                            tblPemeriksaan.getValueAt(i, 6).toString(),
                            tblPemeriksaan.getValueAt(i, 7).toString(),
                            tblPemeriksaan.getValueAt(i, 8).toString(),
                            tblPemeriksaan.getValueAt(i, 9).toString(),
                            tblPemeriksaan.getValueAt(i, 10).toString(),
                            pemeriksaanMap.get(tblPemeriksaan.getValueAt(i, 1).toString()) == null
                            ? "" : pemeriksaanMap.get(tblPemeriksaan.getValueAt(i, 1).toString())
                        });
                    }
                }

                GQuery.setAutoCommit(true);
                ChkJln.setSelected(true);

                if (success)
                {
                    JOptionPane.showMessageDialog(rootPane, "Proses simpan selesai...!");
                    isReset();
                }
                else
                {
                    JOptionPane.showMessageDialog(rootPane, "Proses simpan gagal", "Gagal", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_BtnSimpanActionPerformed

    private void tblGroupMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGroupMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGroupMouseClicked

    private void tblGroupKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblGroupKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGroupKeyPressed

    private void tblGroupPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_tblGroupPropertyChange
    {//GEN-HEADEREND:event_tblGroupPropertyChange
        if (evt.getPropertyName().equals("tableCellEditor"))
        {
            refreshSelGroup();
            tampilPemeriksaan();
        }
    }//GEN-LAST:event_tblGroupPropertyChange

    private void tblPemeriksaanPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_tblPemeriksaanPropertyChange
    {//GEN-HEADEREND:event_tblPemeriksaanPropertyChange
        if (evt.getPropertyName().equals("tableCellEditor"))
        {
            refreshSelPemeriksaans();
        }
    }//GEN-LAST:event_tblPemeriksaanPropertyChange

    private void txtKdPetugasKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdPetugasKeyPressed
    {//GEN-HEADEREND:event_txtKdPetugasKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", NmPtg, txtKdPetugas.getText());
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnPetugasActionPerformed(null);
        }
        else
        {

        }
    }//GEN-LAST:event_txtKdPetugasKeyPressed

    private void btnPetugasActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPetugasActionPerformed
    {//GEN-HEADEREND:event_btnPetugasActionPerformed
        var.setform("DlgPeriksaRadiologi");
        petugas.emptTeks();
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth() - 40, internalFrame1.getHeight() - 40);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setVisible(true);
    }//GEN-LAST:event_btnPetugasActionPerformed

    private void btnDokterPjActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDokterPjActionPerformed
    {//GEN-HEADEREND:event_btnDokterPjActionPerformed
        pilihan = "penjab";
        var.setform("DlgPeriksaRadiologi");
        dokter.emptTeks();
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth() - 40, internalFrame1.getHeight() - 40);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_btnDokterPjActionPerformed

    private void txtCariPemeriksaanActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCariPemeriksaanActionPerformed
    {//GEN-HEADEREND:event_txtCariPemeriksaanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariPemeriksaanActionPerformed

    private void txtCariPemeriksaanKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtCariPemeriksaanKeyPressed
    {//GEN-HEADEREND:event_txtCariPemeriksaanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariPemeriksaanKeyPressed

    private void btnCariPemeriksaanActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCariPemeriksaanActionPerformed
    {//GEN-HEADEREND:event_btnCariPemeriksaanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCariPemeriksaanActionPerformed

    private void btnCariPemeriksaanKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnCariPemeriksaanKeyPressed
    {//GEN-HEADEREND:event_btnCariPemeriksaanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCariPemeriksaanKeyPressed

    private void btnAllPemeriksaanActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAllPemeriksaanActionPerformed
    {//GEN-HEADEREND:event_btnAllPemeriksaanActionPerformed
        txtCariPemeriksaan.setText("");
        tampilPemeriksaan();
    }//GEN-LAST:event_btnAllPemeriksaanActionPerformed

    private void btnAllPemeriksaanKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnAllPemeriksaanKeyPressed
    {//GEN-HEADEREND:event_btnAllPemeriksaanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAllPemeriksaanKeyPressed

    private void btnTambahPemeriksaanActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTambahPemeriksaanActionPerformed
    {//GEN-HEADEREND:event_btnTambahPemeriksaanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTambahPemeriksaanActionPerformed

    private void btnTambahPemeriksaanKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnTambahPemeriksaanKeyPressed
    {//GEN-HEADEREND:event_btnTambahPemeriksaanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTambahPemeriksaanKeyPressed

    private void tblOrderMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblOrderMouseClicked
    {//GEN-HEADEREND:event_tblOrderMouseClicked
        if (evt.getClickCount() == 1 && tblOrder.getSelectedRow() != -1)
        {
            if (tblOrder.getSelectedColumn() == 8)
            {
                mulaiOrder();
            }
            else if (tblOrder.getSelectedColumn() == 9 && ((ButtonCell)tblOrder.getValueAt(tblOrder.getSelectedRow() , 9)).isEnabled())
            {
                dariOrder(tblOrder.getValueAt(tblOrder.getSelectedRow(), 0).toString());
            }
        }
        
//        if (evt.getClickCount() == 2 && tblOrder.getSelectedRow() != -1)
//        {
//            dariOrder(tblOrder.getValueAt(tblOrder.getSelectedRow(), 0).toString());
//        }
    }//GEN-LAST:event_tblOrderMouseClicked

    private void tblOrderKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_tblOrderKeyPressed
    {//GEN-HEADEREND:event_tblOrderKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblOrderKeyPressed

    private void txtCariOrderKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtCariOrderKeyPressed
    {//GEN-HEADEREND:event_txtCariOrderKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariOrderKeyPressed

    private void btnCariOrderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCariOrderActionPerformed
    {//GEN-HEADEREND:event_btnCariOrderActionPerformed
        tampilOrder();
    }//GEN-LAST:event_btnCariOrderActionPerformed

    private void btnCariOrderKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnCariOrderKeyPressed
    {//GEN-HEADEREND:event_btnCariOrderKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCariOrderKeyPressed

    private void btnHapusOrderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnHapusOrderActionPerformed
    {//GEN-HEADEREND:event_btnHapusOrderActionPerformed
        hapusOrder();
    }//GEN-LAST:event_btnHapusOrderActionPerformed

    private void btnHapusOrderKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnHapusOrderKeyPressed
    {//GEN-HEADEREND:event_btnHapusOrderKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHapusOrderKeyPressed

    private void btnAllOrderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAllOrderActionPerformed
    {//GEN-HEADEREND:event_btnAllOrderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAllOrderActionPerformed

    private void btnAllOrderKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnAllOrderKeyPressed
    {//GEN-HEADEREND:event_btnAllOrderKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAllOrderKeyPressed

    private void btnPrintOrderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPrintOrderActionPerformed
    {//GEN-HEADEREND:event_btnPrintOrderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintOrderActionPerformed

    private void btnPrintOrderKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnPrintOrderKeyPressed
    {//GEN-HEADEREND:event_btnPrintOrderKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintOrderKeyPressed

    private void btnKeluarOrderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnKeluarOrderActionPerformed
    {//GEN-HEADEREND:event_btnKeluarOrderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnKeluarOrderActionPerformed

    private void btnKeluarOrderKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnKeluarOrderKeyPressed
    {//GEN-HEADEREND:event_btnKeluarOrderKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnKeluarOrderKeyPressed

    private void txtCariTransaksiKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtCariTransaksiKeyPressed
    {//GEN-HEADEREND:event_txtCariTransaksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariTransaksiKeyPressed

    private void btnCariTransaksiActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCariTransaksiActionPerformed
    {//GEN-HEADEREND:event_btnCariTransaksiActionPerformed
        tampilTransaksi();
    }//GEN-LAST:event_btnCariTransaksiActionPerformed

    private void btnCariTransaksiKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnCariTransaksiKeyPressed
    {//GEN-HEADEREND:event_btnCariTransaksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCariTransaksiKeyPressed

    private void btnHapusTransaksiActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnHapusTransaksiActionPerformed
    {//GEN-HEADEREND:event_btnHapusTransaksiActionPerformed
        hapusTransaksi();
    }//GEN-LAST:event_btnHapusTransaksiActionPerformed

    private void btnHapusTransaksiKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnHapusTransaksiKeyPressed
    {//GEN-HEADEREND:event_btnHapusTransaksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHapusTransaksiKeyPressed

    private void btnAllTransaksiActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAllTransaksiActionPerformed
    {//GEN-HEADEREND:event_btnAllTransaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAllTransaksiActionPerformed

    private void btnAllTransaksiKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnAllTransaksiKeyPressed
    {//GEN-HEADEREND:event_btnAllTransaksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAllTransaksiKeyPressed

    private void btnPrintTransaksiActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPrintTransaksiActionPerformed
    {//GEN-HEADEREND:event_btnPrintTransaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintTransaksiActionPerformed

    private void btnPrintTransaksiKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnPrintTransaksiKeyPressed
    {//GEN-HEADEREND:event_btnPrintTransaksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintTransaksiKeyPressed

    private void btnKeluarTransaksiActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnKeluarTransaksiActionPerformed
    {//GEN-HEADEREND:event_btnKeluarTransaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnKeluarTransaksiActionPerformed

    private void btnKeluarTransaksiKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnKeluarTransaksiKeyPressed
    {//GEN-HEADEREND:event_btnKeluarTransaksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnKeluarTransaksiKeyPressed

    private void menuUbahTransaksiActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuUbahTransaksiActionPerformed
    {//GEN-HEADEREND:event_menuUbahTransaksiActionPerformed
        if (tblTransaksi.getSelectedRow() != -1)
            dariTransaksi(tblTransaksi.getValueAt(tblTransaksi.getSelectedRow(), 0).toString());
    }//GEN-LAST:event_menuUbahTransaksiActionPerformed

    private void menuHapusTransaksiActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuHapusTransaksiActionPerformed
    {//GEN-HEADEREND:event_menuHapusTransaksiActionPerformed
        hapusTransaksi();
    }//GEN-LAST:event_menuHapusTransaksiActionPerformed

    private void menuCetakHasilTransaksiActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuCetakHasilTransaksiActionPerformed
    {//GEN-HEADEREND:event_menuCetakHasilTransaksiActionPerformed
         this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        if (mdlTransaksi.getRowCount() == 0)
        {
            JOptionPane.showMessageDialog(null, "Maaf, data sudah habis...!!!!");
        }
        else if (tblTransaksi.getSelectedRow() == -1)
        {
            JOptionPane.showMessageDialog(null, "Maaf, Gagal mencteak. Pilih dulu data yang mau dicetak.");
        }
        else
        {
            int r = tblTransaksi.getSelectedRow();
            String idPeriksa = null;
            
            // Mencari row utama di tiap transaksi
            for (int a = r; a >= 0; a--)
            {
                if (!tblTransaksi.getValueAt(a, 0).toString().isEmpty())
                {
                    idPeriksa = tblTransaksi.getValueAt(a, 0).toString();
                    break;
                }
            }
            
            // Kalo gak nemu id periksa
            if (idPeriksa == null)
            {
                JOptionPane.showMessageDialog(null, "Pemeriksaan tidak ditemukan");
                return;
            }
            
            HashMap<String, String> mMain = new GQuery()
                    .a("SELECT pemeriksaan_radiologi.no_rawat, reg_periksa.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.umur, petugas.nama,")
                    .a("    DATE_FORMAT(pemeriksaan_radiologi.tgl_periksa,'%d-%m-%Y') as tgl_periksa, pemeriksaan_radiologi.jam,")
                    .a("    dokter_perujuk.nm_dokter AS nm_dokter_perujuk, dokter_pj.nm_dokter AS nm_dokter_pj, pasien.alamat, DATE_FORMAT(pasien.tgl_lahir,'%d-%m-%Y') as tgl_lahir")
                    .a("FROM pemeriksaan_radiologi")
                    .a("JOIN reg_periksa ON reg_periksa.no_rawat = pemeriksaan_radiologi.no_rawat")
                    .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                    .a("JOIN petugas ON petugas.nip = pemeriksaan_radiologi.nip")
                    .a("JOIN dokter dokter_perujuk ON dokter_perujuk.kd_dokter = pemeriksaan_radiologi.kd_dokter_perujuk")
                    .a("JOIN dokter dokter_pj ON dokter_pj.kd_dokter = pemeriksaan_radiologi.kd_dokter_pj")
                    .a("WHERE kd_periksa = {id_periksa}")
                    .set("id_periksa", idPeriksa)
                    .getRowWithName();
            
            if (mMain != null)
            {
                kamar = Sequel.cariIsi("select ifnull(kd_kamar,'') from kamar_inap where no_rawat='" + mMain.get("no_rawat") + "' order by tgl_masuk desc limit 1");
                
                if (!kamar.equals(""))
                {
                    namakamar = kamar + ", " + Sequel.cariIsi("select nm_bangsal from bangsal inner join kamar on bangsal.kd_bangsal=kamar.kd_bangsal "
                            + " where kamar.kd_kamar='" + kamar + "' ");
                    
                    kamar = "Kamar";
                }
                else if (kamar.equals(""))
                {
                    kamar = "Poli";
                    namakamar = Sequel.cariIsi("select nm_poli from poliklinik inner join reg_periksa on poliklinik.kd_poli=reg_periksa.kd_poli "
                            + "where reg_periksa.no_rawat='" + mMain.get("no_rawat") + "'");
                }
                
                // Mengisi RAPOT atas
                Map<String, Object> param = new HashMap<>();
                param.put("noperiksa", mMain.get("no_rawat"));
                param.put("norm", mMain.get("no_rkm_medis"));
                param.put("namapasien", mMain.get("nm_pasien"));
                param.put("jkel", mMain.get("jk"));
                param.put("umur", mMain.get("umur"));
                param.put("pengirim", mMain.get("nm_dokter_perujuk"));
                param.put("tanggal", mMain.get("tgl_periksa"));
                param.put("penjab", mMain.get("nm_dokter_pj"));
                param.put("petugas", mMain.get("nama"));
                param.put("jam", mMain.get("jam"));
                param.put("alamat", mMain.get("Alamat"));
                param.put("kamar", kamar);
                param.put("namakamar", namakamar);
                
                // Dari GLOBAL setting
                param.put("namars", var.getnamars());
                param.put("alamatrs", var.getalamatrs());
                param.put("kotars", var.getkabupatenrs());
                param.put("propinsirs", var.getpropinsirs());
                param.put("kontakrs", var.getkontakrs());
                param.put("emailrs", var.getemailrs());
                param.put("logo", Sequel.cariGambar("select logo from setting"));
                
                // Ngambil detail 1
                List<HashMap<String, String>> mDetail1 = new GQuery()
                        .a("SELECT nm_perawatan, hasil FROM det_pemeriksaan_radiologi")
                        .a("JOIN jns_perawatan_radiologi ON jns_perawatan_radiologi.kd_jenis_prw = det_pemeriksaan_radiologi.kd_jenis_prw")
                        .a("WHERE kd_periksa = {id_periksa}")
                        .set("id_periksa", idPeriksa)
                        .selectWithName();
                
                for (HashMap<String, String> m1 : mDetail1)
                {
                    param.put("pemeriksaan", m1.get("nm_perawatan"));
                    param.put("hasil", m1.get("hasil"));
                    
                    Valid.MyReport("rptPeriksaRadiologiFix.jrxml", "report", "::[ Hasil Pemeriksaan Radiologi ]::",
                        "select no, temp1, temp2 from temporary order by no asc", param);
                }
            }
        }
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_menuCetakHasilTransaksiActionPerformed

    private void menuCetakBillingTransaksiActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuCetakBillingTransaksiActionPerformed
    {//GEN-HEADEREND:event_menuCetakBillingTransaksiActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        if (mdlTransaksi.getRowCount() == 0)
        {
            JOptionPane.showMessageDialog(null, "Maaf, data sudah habis...!!!!");
        }
        else if (tblTransaksi.getSelectedRow() == -1)
        {
            JOptionPane.showMessageDialog(null, "Maaf, Gagal mencteak. Pilih dulu data yang mau dicetak.");
        }
        else
        {
            int r = tblTransaksi.getSelectedRow();
            String idPeriksa = null;
            
            // Mencari row utama di tiap transaksi
            for (int a = r; a >= 0; a--)
            {
                if (!tblTransaksi.getValueAt(a, 0).toString().isEmpty())
                {
                    idPeriksa = tblTransaksi.getValueAt(a, 0).toString();
                    break;
                }
            }
            
            // Kalo gak nemu id periksa
            if (idPeriksa == null)
            {
                JOptionPane.showMessageDialog(null, "Pemeriksaan tidak ditemukan");
                return;
            }
            
            HashMap<String, String> mMain = new GQuery()
                    .a("SELECT pemeriksaan_radiologi.no_rawat, reg_periksa.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.umur, petugas.nama,")
                    .a("    DATE_FORMAT(pemeriksaan_radiologi.tgl_periksa,'%d-%m-%Y') as tgl_periksa, pemeriksaan_radiologi.jam,")
                    .a("    dokter_perujuk.nm_dokter AS nm_dokter_perujuk, dokter_pj.nm_dokter AS nm_dokter_pj, pasien.alamat, DATE_FORMAT(pasien.tgl_lahir,'%d-%m-%Y') as tgl_lahir")
                    .a("FROM pemeriksaan_radiologi")
                    .a("JOIN reg_periksa ON reg_periksa.no_rawat = pemeriksaan_radiologi.no_rawat")
                    .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                    .a("JOIN petugas ON petugas.nip = pemeriksaan_radiologi.nip")
                    .a("JOIN dokter dokter_perujuk ON dokter_perujuk.kd_dokter = pemeriksaan_radiologi.kd_dokter_perujuk")
                    .a("JOIN dokter dokter_pj ON dokter_pj.kd_dokter = pemeriksaan_radiologi.kd_dokter_pj")
                    .a("WHERE kd_periksa = {id_periksa}")
                    .set("id_periksa", idPeriksa)
                    .getRowWithName();
            
            if (mMain != null)
            {
                Sequel.AutoComitFalse();
                Sequel.queryu("delete from temporary");
                
                int total = 0;
                
                // Ngambil detail 1
                GResult mDetail1 = new GQuery()
                        .a("SELECT nm_perawatan, biaya FROM det_pemeriksaan_radiologi")
                        .a("JOIN jns_perawatan_radiologi ON jns_perawatan_radiologi.kd_jenis_prw = det_pemeriksaan_radiologi.kd_jenis_prw")
                        .a("WHERE kd_periksa = {id_periksa}")
                        .set("id_periksa", idPeriksa)
                        .selectComplete();
                
                for (GRow row : mDetail1)
                {
                    new GQuery()
                            .a("INSERT INTO temporary (temp1, temp2) VALUES ({temp1}, {temp2})")
                            .set("temp1", row.getString("nm_perawatan"))
                            .set("temp2", row.getString("biaya"))
                            .write();
                    
                    total += row.getInt("biaya");
                }
                
                Sequel.menyimpan("temporary", "'0', 'Total Biaya Pemeriksaan Radiologi','" + total + "','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',''", "Transaksi Biaya Lab");
                
                Valid.panggilUrl("billing/LaporanBiayaRadiologi.php?norm=" + mMain.get("no_rkm_medis") + "&pasien=" + mMain.get("nm_pasien").replaceAll(" ", "_")
                        + "&tanggal=" + mMain.get("tgl_periksa") + "&jam=" + mMain.get("jam") + "&pjlab=" + mMain.get("nm_dokter_pj").replaceAll(" ", "_")
                        + "&petugas=" + mMain.get("nama").replaceAll(" ", "_") + "&kasir=" + Sequel.cariIsi("select nama from pegawai where nik=?", var.getkode()));
                
                Sequel.AutoComitTrue();
            }
        }
        
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_menuCetakBillingTransaksiActionPerformed

    private void menuCetakBillingOrderActionPerformed(java.awt.event.ActionEvent evt)                                                      
    {                                                          
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        if (mdlOrder.getRowCount() == 0)
        {
            JOptionPane.showMessageDialog(null, "Maaf, data sudah habis...!!!!");
        }
        else if (tblOrder.getSelectedRow() == -1)
        {
            JOptionPane.showMessageDialog(null, "Maaf, Gagal mencteak. Pilih dulu data yang mau dicetak.");
        }
        else
        {
            int r = tblOrder.getSelectedRow();
            String idPeriksa = null;
            
            // Mencari row utama di tiap transaksi
            for (int a = r; a >= 0; a--)
            {
                if (!tblOrder.getValueAt(a, 0).toString().isEmpty())
                {
                    idPeriksa = tblOrder.getValueAt(a, 0).toString();
                    break;
                }
            }
            
            // Kalo gak nemu id periksa
            if (idPeriksa == null)
            {
                JOptionPane.showMessageDialog(null, "Pemeriksaan tidak ditemukan");
                return;
            }
            
            HashMap<String, String> mMain = new GQuery()
                    .a("SELECT pemeriksaan_radiologi.no_rawat, reg_periksa.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.umur, petugas.nama,")
                    .a("    DATE_FORMAT(pemeriksaan_radiologi.tgl_periksa,'%d-%m-%Y') as tgl_periksa, pemeriksaan_radiologi.jam,")
                    .a("    dokter_perujuk.nm_dokter AS nm_dokter_perujuk, dokter_pj.nm_dokter AS nm_dokter_pj, pasien.alamat, DATE_FORMAT(pasien.tgl_lahir,'%d-%m-%Y') as tgl_lahir")
                    .a("FROM pemeriksaan_radiologi")
                    .a("JOIN reg_periksa ON reg_periksa.no_rawat = pemeriksaan_radiologi.no_rawat")
                    .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                    .a("JOIN petugas ON petugas.nip = pemeriksaan_radiologi.nip")
                    .a("JOIN dokter dokter_perujuk ON dokter_perujuk.kd_dokter = pemeriksaan_radiologi.kd_dokter_perujuk")
                    .a("JOIN dokter dokter_pj ON dokter_pj.kd_dokter = pemeriksaan_radiologi.kd_dokter_pj")
                    .a("WHERE kd_periksa = {id_periksa}")
                    .set("id_periksa", idPeriksa)
                    .getRowWithName();
            
            if (mMain != null)
            {
                Sequel.AutoComitFalse();
                Sequel.queryu("delete from temporary");
                
                int total = 0;
                
                // Ngambil detail 1
                GResult mDetail1 = new GQuery()
                        .a("SELECT nm_perawatan, biaya FROM det_pemeriksaan_radiologi")
                        .a("JOIN jns_perawatan_radiologi ON jns_perawatan_radiologi.kd_jenis_prw = det_pemeriksaan_radiologi.kd_jenis_prw")
                        .a("WHERE kd_periksa = {id_periksa}")
                        .set("id_periksa", idPeriksa)
                        .selectComplete();
                
                for (GRow row : mDetail1)
                {
                    new GQuery()
                            .a("INSERT INTO temporary (temp1, temp2) VALUES ({temp1}, {temp2})")
                            .set("temp1", row.getString("nm_perawatan"))
                            .set("temp2", row.getString("biaya"))
                            .write();
                    
                    total += row.getInt("biaya");
                }
                
                Sequel.menyimpan("temporary", "'0', 'Total Biaya Pemeriksaan Radiologi','" + total + "','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',''", "Transaksi Biaya Lab");
                
                Valid.panggilUrl("billing/LaporanBiayaRadiologi.php?norm=" + mMain.get("no_rkm_medis") + "&pasien=" + mMain.get("nm_pasien").replaceAll(" ", "_")
                        + "&tanggal=" + mMain.get("tgl_periksa") + "&jam=" + mMain.get("jam") + "&pjlab=" + mMain.get("nm_dokter_pj").replaceAll(" ", "_")
                        + "&petugas=" + mMain.get("nama").replaceAll(" ", "_") + "&kasir=" + Sequel.cariIsi("select nama from pegawai where nik=?", var.getkode()));
                
                Sequel.AutoComitTrue();
            }
        }
        
        this.setCursor(Cursor.getDefaultCursor());
    }                                                                            

    private void btnCariPasienActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCariPasienActionPerformed
    {//GEN-HEADEREND:event_btnCariPasienActionPerformed
        DlgCariReg d = new DlgCariReg(null, true);
        d.setSize(internalFrame1.getWidth() - 40, internalFrame1.getHeight() - 40);
        d.setLocationRelativeTo(internalFrame1);
        d.setVisible(true);
        
        if (d.RESULT)
        {
            txtNoRw.setText(d.DATA[d.NO_RAWAT]);
            txtNoRM.setText(d.DATA[d.NO_RKM_MEDIS]);
            txtNamaPasien.setText(d.DATA[d.NAMA_PASIEN]);
            txtKdDokter.setText(d.DATA[d.KD_DOKTER]);
            txtNamaDokter.setText(d.DATA[d.NAMA_DOKTER]);
            txtKdKamar.setText(d.DATA[d.KD_KAMAR_POLI]);
            txtNamaKamar.setText(d.DATA[d.KAMAR_POLI]);
            status = d.DATA[d.STATUS];
        }
    }//GEN-LAST:event_btnCariPasienActionPerformed

    private void tblNonRmMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblNonRmMouseClicked
    {//GEN-HEADEREND:event_tblNonRmMouseClicked
        if (evt.getClickCount() == 2 && tblNonRm.getSelectedRow() != -1)
        {
            dariNonRm(tblNonRm.getValueAt(tblNonRm.getSelectedRow(), 0).toString());
        }
    }//GEN-LAST:event_tblNonRmMouseClicked

    private void tblNonRmKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_tblNonRmKeyPressed
    {//GEN-HEADEREND:event_tblNonRmKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblNonRmKeyPressed

    private void txtCariNonRmKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtCariNonRmKeyPressed
    {//GEN-HEADEREND:event_txtCariNonRmKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariNonRmKeyPressed

    private void btnCariNonRmActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCariNonRmActionPerformed
    {//GEN-HEADEREND:event_btnCariNonRmActionPerformed
        tampilNonRm();
    }//GEN-LAST:event_btnCariNonRmActionPerformed

    private void btnCariNonRmKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnCariNonRmKeyPressed
    {//GEN-HEADEREND:event_btnCariNonRmKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCariNonRmKeyPressed

    private void btnHapusNonRmActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnHapusNonRmActionPerformed
    {//GEN-HEADEREND:event_btnHapusNonRmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHapusNonRmActionPerformed

    private void btnHapusNonRmKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnHapusNonRmKeyPressed
    {//GEN-HEADEREND:event_btnHapusNonRmKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHapusNonRmKeyPressed

    private void btnAllNonRmActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAllNonRmActionPerformed
    {//GEN-HEADEREND:event_btnAllNonRmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAllNonRmActionPerformed

    private void btnAllNonRmKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnAllNonRmKeyPressed
    {//GEN-HEADEREND:event_btnAllNonRmKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAllNonRmKeyPressed

    private void btnPrintNonRmActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPrintNonRmActionPerformed
    {//GEN-HEADEREND:event_btnPrintNonRmActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        // Mengisi RAPOT atas
        Map<String, Object> param = new HashMap<>();

        // Dari GLOBAL setting
        param.put("namars", var.getnamars());
        param.put("alamatrs", var.getalamatrs());
        param.put("kotars", var.getkabupatenrs());
        param.put("propinsirs", var.getpropinsirs());
        param.put("kontakrs", var.getkontakrs());
        param.put("emailrs", var.getemailrs());
        param.put("logo", Sequel.cariGambar("select logo from setting"));
        
        GResult res = new GQuery()
                .a("SELECT nm_group AS pemeriksaan, COUNT(*) AS jumlah FROM det_pemeriksaan_radiologi_nonrm")
                .a("JOIN jns_perawatan_radiologi ON jns_perawatan_radiologi.kd_jenis_prw = det_pemeriksaan_radiologi_nonrm.kd_jenis_prw")
                .a("JOIN group_radiologi ON group_radiologi.kd_group = jns_perawatan_radiologi.kd_group")
                .a("GROUP BY group_radiologi.kd_group")
                .selectComplete();
        
        Valid.MyReport("rptLaporanNoRmRad.jrxml", "::[ Laporan Non RM Radiologi ]::", param, res);
        
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_btnPrintNonRmActionPerformed

    private void btnPrintNonRmKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnPrintNonRmKeyPressed
    {//GEN-HEADEREND:event_btnPrintNonRmKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintNonRmKeyPressed

    private void btnKeluarNonRmActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnKeluarNonRmActionPerformed
    {//GEN-HEADEREND:event_btnKeluarNonRmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnKeluarNonRmActionPerformed

    private void btnKeluarNonRmKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnKeluarNonRmKeyPressed
    {//GEN-HEADEREND:event_btnKeluarNonRmKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnKeluarNonRmKeyPressed

    private void menuUbahNonRmActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuUbahNonRmActionPerformed
    {//GEN-HEADEREND:event_menuUbahNonRmActionPerformed
        if (tblNonRm.getSelectedRow() != -1)
        {
            dariNonRm(tblNonRm.getValueAt(tblNonRm.getSelectedRow(), 0).toString());
        }
    }//GEN-LAST:event_menuUbahNonRmActionPerformed

    private void menuHapusNonRmActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuHapusNonRmActionPerformed
    {//GEN-HEADEREND:event_menuHapusNonRmActionPerformed
        hapusNonRm();
    }//GEN-LAST:event_menuHapusNonRmActionPerformed

    private void menuCetakHasilNonRmActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuCetakHasilNonRmActionPerformed
    {//GEN-HEADEREND:event_menuCetakHasilNonRmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuCetakHasilNonRmActionPerformed

    private void menuCetakBillingNonRmActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuCetakBillingNonRmActionPerformed
    {//GEN-HEADEREND:event_menuCetakBillingNonRmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuCetakBillingNonRmActionPerformed

    private void btnEditHasilActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEditHasilActionPerformed
    {//GEN-HEADEREND:event_btnEditHasilActionPerformed
        if (tblPemeriksaan.getSelectedRow() == -1)
            return;
        
        PopHasilRad p = new PopHasilRad(null, true);
        p.setData(txtHasil.getText());
        p.setSize(internalFrame1.getWidth() - 40, internalFrame1.getHeight() - 40);
        p.setLocationRelativeTo(internalFrame1);
        p.setVisible(true);
        
        if (p.isSaved)
        {
            txtHasil.setText(p.result);
        }
    }//GEN-LAST:event_btnEditHasilActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(()
                -> 
                {
                    DlgPemeriksaanRadiologi dialog = new DlgPemeriksaanRadiologi(new javax.swing.JFrame(), true);
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
    private widget.Button BtnBatal;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.CekBox ChkInput;
    private widget.CekBox ChkJln;
    private widget.ComboBox CmbDetik;
    private widget.ComboBox CmbJam;
    private widget.ComboBox CmbMenit;
    private javax.swing.JPanel FormInput;
    private widget.TextBox Jk;
    private widget.TextBox NmDokterPj;
    private widget.TextBox NmPtg;
    private widget.PanelBiasa PanelInput;
    private widget.TextBox Penjab;
    private widget.ScrollPane Scroll2;
    private widget.ScrollPane Scroll3;
    private widget.ScrollPane Scroll4;
    private widget.ScrollPane Scroll5;
    private widget.ScrollPane Scroll6;
    private widget.ScrollPane Scroll7;
    private widget.Tanggal Tanggal;
    private widget.TextBox Umur;
    private widget.Button btnAllGroup;
    private widget.Button btnAllNonRm;
    private widget.Button btnAllOrder;
    private widget.Button btnAllPemeriksaan;
    private widget.Button btnAllTransaksi;
    private widget.Button btnCariGroup;
    private widget.Button btnCariNonRm;
    private widget.Button btnCariOrder;
    private widget.Button btnCariPasien;
    private widget.Button btnCariPemeriksaan;
    private widget.Button btnCariTransaksi;
    private widget.Button btnDokterPj;
    private widget.Button btnEditHasil;
    private widget.Button btnHapusNonRm;
    private widget.Button btnHapusOrder;
    private widget.Button btnHapusTransaksi;
    private widget.Button btnKeluarNonRm;
    private widget.Button btnKeluarOrder;
    private widget.Button btnKeluarTransaksi;
    private widget.Button btnPetugas;
    private widget.Button btnPrintNonRm;
    private widget.Button btnPrintOrder;
    private widget.Button btnPrintTransaksi;
    private widget.Button btnTambahGroup;
    private widget.Button btnTambahPemeriksaan;
    private widget.CekBox ckbNonRm;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel3;
    private widget.Label jLabel35;
    private widget.Label jLabel36;
    private widget.Label jLabel37;
    private widget.Label jLabel38;
    private widget.Label jLabel39;
    private widget.Label jLabel40;
    private widget.Label jLabel7;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPopupMenu jPopupMenuNonRm;
    private javax.swing.JPopupMenu jPopupMenuOrder;
    private javax.swing.JPopupMenu jPopupMenuTransaksi;
    private widget.Label label1;
    private widget.Label label10;
    private widget.Label label11;
    private widget.Label label12;
    private widget.Label label13;
    private widget.Label label14;
    private widget.Label label15;
    private widget.Label label16;
    private widget.Label label17;
    private widget.Label label2;
    private widget.Label label3;
    private javax.swing.JMenuItem menuCetakBillingNonRm;
    private javax.swing.JMenuItem menuCetakBillingOrder;
    private javax.swing.JMenuItem menuCetakBillingTransaksi;
    private javax.swing.JMenuItem menuCetakHasilNonRm;
    private javax.swing.JMenuItem menuCetakHasilTransaksi;
    private javax.swing.JMenuItem menuHapusNonRm;
    private javax.swing.JMenuItem menuHapusOrder;
    private javax.swing.JMenuItem menuHapusTransaksi;
    private javax.swing.JMenuItem menuUbahNonRm;
    private javax.swing.JMenuItem menuUbahOrder;
    private javax.swing.JMenuItem menuUbahTransaksi;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelisi1;
    private widget.panelisi panelisi10;
    private widget.panelisi panelisi2;
    private widget.panelisi panelisi3;
    private widget.panelisi panelisi4;
    private widget.panelisi panelisi5;
    private widget.panelisi panelisi6;
    private widget.panelisi panelisi7;
    private widget.panelisi panelisi8;
    private widget.panelisi panelisi9;
    private javax.swing.JPanel pnlNonRm;
    private javax.swing.JPanel pnlRm;
    private widget.TabPane tabPane;
    private widget.Table tblGroup;
    private widget.Table tblNonRm;
    private widget.Table tblOrder;
    private widget.Table tblPemeriksaan;
    private widget.Table tblTransaksi;
    private widget.Tanggal tglNonRm1;
    private widget.Tanggal tglNonRm2;
    private widget.Tanggal tglOrder1;
    private widget.Tanggal tglOrder2;
    private widget.Tanggal tglTransaksi1;
    private widget.Tanggal tglTransaksi2;
    private widget.TextBox txtAlamatNonRm;
    private widget.TextBox txtCariGroup;
    private widget.TextBox txtCariNonRm;
    private widget.TextBox txtCariOrder;
    private widget.TextBox txtCariPemeriksaan;
    private widget.TextBox txtCariTransaksi;
    private widget.EditorPane txtHasil;
    private widget.TextBox txtKdDokter;
    private widget.TextBox txtKdDokterPj;
    private widget.TextBox txtKdKamar;
    private widget.TextBox txtKdPetugas;
    private widget.TextBox txtNamaDokter;
    private widget.TextBox txtNamaKamar;
    private widget.TextBox txtNamaNonRm;
    private widget.TextBox txtNamaPasien;
    private widget.TextBox txtNoRM;
    private widget.TextBox txtNoRw;
    private widget.TextBox txtUmurNonRm;
    // End of variables declaration//GEN-END:variables

    private void tampilGroup()
    {
        try
        {
            Valid.tabelKosong(mdlGroup);

            String q = new GQuery()
                    .a("SELECT * FROM group_radiologi")
                    .a("ORDER BY nm_group")
                    .compile();

            psGroup = koneksi.prepareStatement(q);
            try
            {
                rsGroup = psGroup.executeQuery();

                while (rsGroup.next())
                {
                    if (selGroup.contains(rsGroup.getString(1)))
                    {
                        mdlGroup.addRow(new Object[]
                        {
                            true, rsGroup.getString(1), rsGroup.getString(2)
                        });
                    }
                }
                
                rsGroup.beforeFirst();
                
                while (rsGroup.next())
                {
                    if (!selGroup.contains(rsGroup.getString(1)) && rsGroup.getString(2).toLowerCase().contains(txtCariGroup.getText().toLowerCase()))
                    {
                        mdlGroup.addRow(new Object[]
                        {
                            false, rsGroup.getString(1), rsGroup.getString(2)
                        });
                    }
                }
            }
            catch (Exception e)
            {
                System.out.println("Notifikasi 1 : " + e.getMessage());
            }
            finally
            {
                if (rsGroup != null)
                {
                    rsGroup.close();
                }

                if (psGroup != null)
                {
                    psGroup.close();
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("Notifikasi 2 : " + e.getMessage());
        }
    }

    private void tampilPemeriksaan()
    {
        try
        {
            Valid.tabelKosong(mdlPemeriksaan);

            // Diambil dari group-group yg dipilih
            String where = "";

            for (int a = 0; a < tblGroup.getRowCount(); a++)
            {
                if ((boolean) tblGroup.getValueAt(a, 0))
                {
                    if (where.isEmpty())
                    {
                        where = "WHERE";
                    }
                    else
                    {
                        where += " OR";
                    }

                    where += " kd_group = '" + tblGroup.getValueAt(a, 1) + "'";
                }
            }

            if (where.isEmpty())
            {
                return;
            }

            String q = new GQuery()
                    .a("SELECT kd_jenis_prw, nm_perawatan, bagian_rs, bhp, tarif_perujuk, tarif_tindakan_dokter, tarif_tindakan_petugas,")
                    .a("kso, menejemen, total_byr")
                    .a("FROM jns_perawatan_radiologi")
                    .a(where)
                    .a("ORDER BY nm_perawatan")
                    .compile();

            pspemeriksaan = koneksi.prepareStatement(q);
            try
            {
                rs = pspemeriksaan.executeQuery();

                while (rs.next())
                {
                    if (selPemeriksaan.contains(rs.getString(1)))
                    {
                        mdlPemeriksaan.addRow(new Object[]
                        {
                            true, rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9), rs.getDouble(10)
                        });
                    }
                }
                
                rs.beforeFirst();
                
                while (rs.next())
                {
                    if (!selPemeriksaan.contains(rs.getString(1)) && rs.getString(2).toLowerCase().contains(txtCariPemeriksaan.getText().toLowerCase()))
                    {
                        mdlPemeriksaan.addRow(new Object[]
                        {
                            false, rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9), rs.getDouble(10)
                        });
                    }
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

    // BUta nampilin list transaksi
    private void tampilOrder()
    {
        // Isi statement ----------------
        try
        {
            String qMain = new GQuery()
                    .a("SELECT pemeriksaan_radiologi.kd_periksa, pemeriksaan_radiologi.no_rawat,")
                    .a("    pasien.no_rkm_medis, pasien.nm_pasien, kamar.kd_kamar, bangsal.nm_bangsal, poliklinik.nm_poli,")
                    .a("    tgl_periksa, jam, tgl_selesai, jam_selesai, pemeriksaan_radiologi.status, pemeriksaan_radiologi.proses")
                    .a("FROM pemeriksaan_radiologi")
                    .a("JOIN reg_periksa ON reg_periksa.no_rawat = pemeriksaan_radiologi.no_rawat")
                    .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                    .a("LEFT JOIN kamar_inap ON kamar_inap.no_rawat = pemeriksaan_radiologi.no_rawat AND kamar_inap.stts_pulang = '-'")
                    .a("LEFT JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                    .a("LEFT JOIN bangsal ON bangsal.kd_bangsal = kamar.kd_bangsal")
                    .a("LEFT JOIN poliklinik ON poliklinik.kd_poli = reg_periksa.kd_poli")
                    .a("WHERE (proses = 'Belum' || proses = 'Mulai')")
                    //.a("AND tgl_periksa BETWEEN {tgl1} AND {tgl2}")
                    .a("AND (pasien.no_rkm_medis LIKE {rkm_medis} OR pasien.nm_pasien LIKE {nama})")
                    .a("ORDER BY kd_periksa")
                    //.set("tgl1", Valid.SetTgl(tglOrder1.getSelectedItem().toString()))
                    //.set("tgl2", Valid.SetTgl(tglOrder2.getSelectedItem().toString()))
                    .set("rkm_medis", "%" + txtCariOrder.getText() + "%")
                    .set("nama", "%" + txtCariOrder.getText() + "%")
                    .compile();

            GQuery qDet = new GQuery()
                    .a("SELECT det_pemeriksaan_radiologi.kd_jenis_prw, nm_perawatan, det_pemeriksaan_radiologi.biaya")
                    .a("FROM det_pemeriksaan_radiologi")
                    .a("JOIN jns_perawatan_radiologi ON jns_perawatan_radiologi.kd_jenis_prw = det_pemeriksaan_radiologi.kd_jenis_prw")
                    .a("WHERE kd_periksa = {kd_periksa}");

            Valid.tabelKosong(mdlOrder);
            psMainOrder = koneksi.prepareStatement(qMain);
            rsMainOrder = psMainOrder.executeQuery();

            while (rsMainOrder.next())
            {
                String pas;

                if (rsMainOrder.getString("kd_kamar") != null)
                {
                    pas = rsMainOrder.getString("no_rkm_medis") + " " + rsMainOrder.getString("nm_pasien") + " (Kamar : "
                            + rsMainOrder.getString("kd_kamar") + ", " + rsMainOrder.getString("nm_bangsal") + ")";
                }
                else
                {
                    pas = rsMainOrder.getString("no_rkm_medis") + " " + rsMainOrder.getString("nm_pasien") + " (Poli : "
                            + rsMainOrder.getString("nm_poli") + ")";
                }

                Object[] o = new Object[]
                {
                    rsMainOrder.getString("kd_periksa"),
                    rsMainOrder.getString("no_rawat"),
                    pas,
                    rsMainOrder.getString("tgl_periksa"),
                    rsMainOrder.getString("jam"),
                    rsMainOrder.getString("tgl_selesai"),
                    rsMainOrder.getString("jam_selesai"),
                    rsMainOrder.getString("status"),
                    new ButtonCell(rsMainOrder.getString("proses")),
                    new ButtonCell("Proses", rsMainOrder.getString("proses").equals("Mulai"))
                };

                mdlOrder.addRow(o);
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    // BUta nampilin list transaksi
    private void tampilTransaksi()
    {
        // Isi statement ----------------
        try
        {
            String qMain = new GQuery()
                    .a("SELECT pemeriksaan_radiologi.kd_periksa, pemeriksaan_radiologi.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, kamar.kd_kamar, bangsal.nm_bangsal, poliklinik.nm_poli,")
                    .a("    tgl_periksa, jam, pemeriksaan_radiologi.proses")
                    .a("FROM pemeriksaan_radiologi")
                    .a("JOIN reg_periksa ON reg_periksa.no_rawat = pemeriksaan_radiologi.no_rawat")
                    .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                    .a("LEFT JOIN kamar_inap ON kamar_inap.no_rawat = pemeriksaan_radiologi.no_rawat AND kamar_inap.stts_pulang = '-'")
                    .a("LEFT JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                    .a("LEFT JOIN bangsal ON bangsal.kd_bangsal = kamar.kd_bangsal")
                    .a("LEFT JOIN poliklinik ON poliklinik.kd_poli = reg_periksa.kd_poli")
                    .a("WHERE proses = 'Sudah'")
                    .a("AND tgl_periksa BETWEEN {tgl1} AND {tgl2}")
                    .a("AND (pasien.no_rkm_medis LIKE {rkm_medis} OR pasien.nm_pasien LIKE {nama})")
                    .a("ORDER BY tgl_periksa, jam")
                    .set("tgl1", Valid.SetTgl(tglTransaksi1.getSelectedItem().toString()))
                    .set("tgl2", Valid.SetTgl(tglTransaksi2.getSelectedItem().toString()))
                    .set("rkm_medis", "%" + txtCariTransaksi.getText() + "%")
                    .set("nama", "%" + txtCariTransaksi.getText() + "%")
                    .compile();

            Valid.tabelKosong(mdlTransaksi);
            psMainTransaksi = koneksi.prepareStatement(qMain);
            rsMainTransaksi = psMainTransaksi.executeQuery();

            while (rsMainTransaksi.next())
            {
                String pas;

                if (rsMainTransaksi.getString("kd_kamar") != null)
                {
                    pas = rsMainTransaksi.getString("no_rkm_medis") + " " + rsMainTransaksi.getString("nm_pasien") + " (Kamar : "
                            + rsMainTransaksi.getString("kd_kamar") + ", " + rsMainTransaksi.getString("nm_bangsal") + ")";
                }
                else
                {
                    pas = rsMainTransaksi.getString("no_rkm_medis") + " " + rsMainTransaksi.getString("nm_pasien") + " (Poli : "
                            + rsMainTransaksi.getString("nm_poli") + ")";
                }

                Object[] o = new Object[]
                {
                    rsMainTransaksi.getString("kd_periksa"),
                    rsMainTransaksi.getString("no_rawat"),
                    pas,
                    rsMainTransaksi.getString("tgl_periksa"),
                    rsMainTransaksi.getString("jam"),
                    rsMainTransaksi.getString("proses")
                };

                mdlTransaksi.addRow(o);
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    // BUta nampilin list transaksi
    private void tampilNonRm()
    {
        // Isi statement ----------------
        GResult rMain = new GQuery()
                .a("SELECT pemeriksaan_radiologi_nonrm.kd_periksa, pemeriksaan_radiologi_nonrm.nama,")
                .a("    tgl_periksa, jam, pemeriksaan_radiologi_nonrm.proses")
                .a("FROM pemeriksaan_radiologi_nonrm")
                .a("WHERE tgl_periksa BETWEEN {tgl1} AND {tgl2}")
                .a("AND pemeriksaan_radiologi_nonrm.nama LIKE {nama}")
                .a("ORDER BY tgl_periksa, jam")
                .set("tgl1", Valid.SetTgl(tglNonRm1.getSelectedItem().toString()))
                .set("tgl2", Valid.SetTgl(tglNonRm2.getSelectedItem().toString()))
                .set("nama", "%" + txtCariNonRm.getText() + "%")
                .selectComplete();

        Valid.tabelKosong(mdlNonRm);

        for (GRow r : rMain)
        {
            Object[] o = new Object[]
            {
                r.getString("kd_periksa"),
                r.getString("nama"),
                r.getString("tgl_periksa"),
                r.getString("jam"),
                r.getString("proses")
            };

            mdlNonRm.addRow(o);
        }
    }
    
    private void mulaiOrder()
    {
        if (!GMessage.q("Mulai", "Mulai pemeriksaan?"))
            return;
        
        String id = tblOrder.getValueAt(tblOrder.getSelectedRow(), 0).toString();
        
        boolean b = new GQuery()
                .a("UPDATE pemeriksaan_radiologi SET")
                .a("tgl_periksa = CURDATE(), jam = CURTIME(), proses = 'Mulai'")
                .a("WHERE kd_periksa = {id}")
                .set("id", id)
                .write();
        
        if (b)
        {
            GMessage.i("Sukses", "Pemeriksaan dimulai");
            tampilOrder();
        }
        else
        {
            GMessage.e("Error", "Error saat menyimpan data");
        }
    }
    
    private void hapusOrder()
    {
        int reply = JOptionPane.showConfirmDialog(rootPane, "Eeiiiiiits, beneran mau hapus..??", "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (reply == JOptionPane.YES_OPTION)
        {
            GQuery.setAutoCommit(false);

            boolean isSuccess = true;

            String kdPeriksa = tblOrder.getValueAt(tblOrder.getSelectedRow(), 0).toString();

            isSuccess &= new GQuery()
                    .a("DELETE FROM pemeriksaan_radiologi WHERE kd_periksa = {kd_periksa}")
                    .set("kd_periksa", kdPeriksa)
                    .write();

            GQuery.setAutoCommit(true);

            if (isSuccess)
            {
                JOptionPane.showMessageDialog(rootPane, "Hapus data berhasil", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                tampilOrder();
            }
            else
            {
                JOptionPane.showMessageDialog(rootPane, "Hapus data gagal", "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void hapusTransaksi()
    {
        int reply = JOptionPane.showConfirmDialog(rootPane, "Eeiiiiiits, beneran mau hapus..??", "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (reply == JOptionPane.YES_OPTION)
        {
            GQuery.setAutoCommit(false);

            boolean isSuccess = true;

            String kdPeriksa = tblTransaksi.getValueAt(tblTransaksi.getSelectedRow(), 0).toString();

            isSuccess &= new GQuery()
                    .a("DELETE FROM pemeriksaan_radiologi WHERE kd_periksa = {kd_periksa}")
                    .set("kd_periksa", kdPeriksa)
                    .write();

            GQuery.setAutoCommit(true);

            if (isSuccess)
            {
                JOptionPane.showMessageDialog(rootPane, "Hapus data berhasil", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                tampilTransaksi();
            }
            else
            {
                JOptionPane.showMessageDialog(rootPane, "Hapus data gagal", "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void hapusNonRm()
    {
        int reply = JOptionPane.showConfirmDialog(rootPane, "Eeiiiiiits, beneran mau hapus..??", "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (reply == JOptionPane.YES_OPTION)
        {
            GQuery.setAutoCommit(false);

            boolean isSuccess = true;

            String kdPeriksa = tblNonRm.getValueAt(tblNonRm.getSelectedRow(), 0).toString();

            isSuccess &= new GQuery()
                    .a("DELETE FROM pemeriksaan_radiologi_nonrm WHERE kd_periksa = {kd_periksa}")
                    .set("kd_periksa", kdPeriksa)
                    .write();

            GQuery.setAutoCommit(true);

            if (isSuccess)
            {
                JOptionPane.showMessageDialog(rootPane, "Hapus data berhasil", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                tampilNonRm();
            }
            else
            {
                JOptionPane.showMessageDialog(rootPane, "Hapus data gagal", "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void dariOrder(String kdPeriksa)
    {
        isEdit = true;
        
        this.kdPeriksa = kdPeriksa;
        tabPane.setSelectedIndex(0);
        
        showRm(false);
        txtHasil.setText("");
        
        // NGambil daat Pemeriksaan LAB
        GRow periksa = new GQuery()
                .a("SELECT pemeriksaan_radiologi.no_rawat, nip, kd_dokter_perujuk, kd_dokter_pj, pemeriksaan_radiologi.status,")
                .a("IF (kamar.kd_kamar IS NULL, '', kamar.kd_kamar) AS kd_kamar,")
                .a("'' AS nm_kamar,")
                .a("poliklinik.kd_poli, nm_poli")
                .a("FROM pemeriksaan_radiologi")
                .a("LEFT JOIN kamar_inap ON kamar_inap.no_rawat = pemeriksaan_radiologi.no_rawat")
                .a("LEFT JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                .a("LEFT JOIN reg_periksa ON reg_periksa.no_rawat = pemeriksaan_radiologi.no_rawat")
                .a("LEFT JOIN poliklinik ON poliklinik.kd_poli = reg_periksa.kd_poli")
                .a("WHERE kd_periksa = {kd_periksa}")
                .set("kd_periksa", kdPeriksa)
                .getRowComplete();
        
        // Dimasukkken ke textbox
        txtNoRw.setText(periksa.getString("no_rawat"));
        txtKdPetugas.setText(periksa.getString("nip"));
        txtKdDokterPj.setText(periksa.getString("kd_dokter_pj"));
        txtKdDokter.setText(periksa.getString("kd_dokter_perujuk"));
        
        Sequel.cariIsi("select nama from petugas where nip = ?", NmPtg, txtKdPetugas.getText());
        Sequel.cariIsi("select nm_dokter from dokter where kd_dokter = ?", NmDokterPj, txtKdDokterPj.getText());
        Sequel.cariIsi("select nm_dokter from dokter where kd_dokter = ?", txtNamaDokter, txtKdDokter.getText());
        
        txtKdKamar.setText(periksa.getString("kd_kamar").isEmpty() ? periksa.getString("kd_poli") : periksa.getString("kd_kamar"));
        txtNamaKamar.setText(periksa.getString("nm_kamar").isEmpty() ? periksa.getString("nm_poli") : periksa.getString("nm_kamar"));

        isRawat();
        isPsien();
        
        // Ambil data detail
        GResult rDetail = new GQuery()
                .a("SELECT kd_jenis_prw FROM det_pemeriksaan_radiologi")
                .a("WHERE kd_periksa = {kd_periksa}")
                .set("kd_periksa", kdPeriksa)
                .selectComplete();

        // Masukin data ke kdPeriksa dan selPemeriksaan dan selGroup
        selGroup.clear();
        selPemeriksaan.clear();
        
        for (GRow r : rDetail)
        {
            String kdJns = r.getString("kd_jenis_prw");
            String kdGroup = Sequel.cariIsi("SELECT kd_group FROM jns_perawatan_radiologi WHERE kd_jenis_prw = '" + kdJns + "'");

            selPemeriksaan.add(kdJns);

            if (!selGroup.contains(kdGroup))
            {
                selGroup.add(kdGroup);
            }
        }
        
        // Tampil-tampil
        tampilGroup();
        tampilPemeriksaan();
    }
    
    private void dariTransaksi(String kdPeriksa)
    {
        isEdit = true;
        
        this.kdPeriksa = kdPeriksa;
        tabPane.setSelectedIndex(0);
        
        showRm(false);
        txtHasil.setText("");
        
        // NGambil daat Pemeriksaan LAB
        GRow periksa = new GQuery()
                .a("SELECT pemeriksaan_radiologi.no_rawat, nip, kd_dokter_perujuk, kd_dokter_pj, pemeriksaan_radiologi.status,")
                .a("IF (kamar.kd_kamar IS NULL, '', kamar.kd_kamar) AS kd_kamar,")
                .a("'' AS nm_kamar,")
                .a("poliklinik.kd_poli, nm_poli")
                .a("FROM pemeriksaan_radiologi")
                .a("LEFT JOIN kamar_inap ON kamar_inap.no_rawat = pemeriksaan_radiologi.no_rawat")
                .a("LEFT JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                .a("LEFT JOIN reg_periksa ON reg_periksa.no_rawat = pemeriksaan_radiologi.no_rawat")
                .a("LEFT JOIN poliklinik ON poliklinik.kd_poli = reg_periksa.kd_poli")
                .a("WHERE kd_periksa = {kd_periksa}")
                .set("kd_periksa", kdPeriksa)
                .getRowComplete();
        
        // Dimasukkken ke textbox
        txtNoRw.setText(periksa.getString("no_rawat"));
        txtKdPetugas.setText(periksa.getString("nip"));
        txtKdDokterPj.setText(periksa.getString("kd_dokter_pj"));
        txtKdDokter.setText(periksa.getString("kd_dokter_perujuk"));
        
        Sequel.cariIsi("select nama from petugas where nip = ?", NmPtg, txtKdPetugas.getText());
        Sequel.cariIsi("select nm_dokter from dokter where kd_dokter = ?", NmDokterPj, txtKdDokterPj.getText());
        Sequel.cariIsi("select nm_dokter from dokter where kd_dokter = ?", txtNamaDokter, txtKdDokter.getText());
        
        txtKdKamar.setText(periksa.getString("kd_kamar").isEmpty() ? periksa.getString("kd_poli") : periksa.getString("kd_kamar"));
        txtNamaKamar.setText(periksa.getString("nm_kamar").isEmpty() ? periksa.getString("nm_poli") : periksa.getString("nm_kamar"));

        isRawat();
        isPsien();
        
        // Ambil data detail
        GResult rDetail = new GQuery()
                .a("SELECT kd_jenis_prw FROM det_pemeriksaan_radiologi")
                .a("WHERE kd_periksa = {kd_periksa}")
                .set("kd_periksa", kdPeriksa)
                .selectComplete();

        // Masukin data ke kdPeriksa dan selPemeriksaan dan selGroup
        selGroup.clear();
        selPemeriksaan.clear();
        pemeriksaanMap.clear();
        
        for (GRow r : rDetail)
        {
            String kdJns = r.getString("kd_jenis_prw");
            String kdGroup = Sequel.cariIsi("SELECT kd_group FROM jns_perawatan_radiologi WHERE kd_jenis_prw = '" + kdJns + "'");
            String hasil = Sequel.cariIsi("SELECT hasil FROM det_pemeriksaan_radiologi WHERE kd_jenis_prw = '" + kdJns + "' AND kd_periksa = '" + kdPeriksa + "'");

            selPemeriksaan.add(kdJns);
            pemeriksaanMap.put(kdJns, hasil);

            if (!selGroup.contains(kdGroup))
            {
                selGroup.add(kdGroup);
            }
        }
        
        // Tampil-tampil
        tampilGroup();
        tampilPemeriksaan();
    }
    
    private void dariNonRm(String kdPeriksa)
    {
        isEdit = true;
        
        this.kdPeriksa = kdPeriksa;
        tabPane.setSelectedIndex(0);
        
        showNonRm(false);
        txtHasil.setText("");
        
        // NGambil daat Pemeriksaan LAB
        GRow periksa = new GQuery()
                .a("SELECT nama, umur, alamat, nip, kd_dokter_pj")
                .a("FROM pemeriksaan_radiologi_nonrm")
                .a("WHERE kd_periksa = {kd_periksa}")
                .set("kd_periksa", kdPeriksa)
                .getRowComplete();
        
        // Dimasukkken ke textbox
        txtNamaNonRm.setText(periksa.getString("nama"));
        txtUmurNonRm.setText(periksa.getString("umur"));
        txtAlamatNonRm.setText(periksa.getString("alamat"));
        txtKdPetugas.setText(periksa.getString("nip"));
        txtKdDokterPj.setText(periksa.getString("kd_dokter_pj"));
        
        Sequel.cariIsi("select nama from petugas where nip = ?", NmPtg, txtKdPetugas.getText());
        Sequel.cariIsi("select nm_dokter from dokter where kd_dokter = ?", NmDokterPj, txtKdDokterPj.getText());
        
        // Ambil data detail
        GResult rDetail = new GQuery()
                .a("SELECT kd_jenis_prw FROM det_pemeriksaan_radiologi_nonrm")
                .a("WHERE kd_periksa = {kd_periksa}")
                .set("kd_periksa", kdPeriksa)
                .selectComplete();

        // Masukin data ke kdPeriksa dan selPemeriksaan dan selGroup
        selGroup.clear();
        selPemeriksaan.clear();
        pemeriksaanMap.clear();
        
        for (GRow r : rDetail)
        {
            String kdJns = r.getString("kd_jenis_prw");
            String kdGroup = Sequel.cariIsi("SELECT kd_group FROM jns_perawatan_radiologi WHERE kd_jenis_prw = '" + kdJns + "'");
            String hasil = Sequel.cariIsi("SELECT hasil FROM det_pemeriksaan_radiologi_nonrm WHERE kd_jenis_prw = '" + kdJns + "' AND kd_periksa = '" + kdPeriksa + "'");

            selPemeriksaan.add(kdJns);
            pemeriksaanMap.put(kdJns, hasil);

            if (!selGroup.contains(kdGroup))
            {
                selGroup.add(kdGroup);
            }
        }
        
        // Tampil-tampil
        tampilGroup();
        tampilPemeriksaan();
    }
    
    private void showRm(boolean editable)
    {
        pnlRm.setVisible(true);
        pnlNonRm.setVisible(false);
        
        ckbNonRm.setSelected(false);
        ckbNonRm.setEnabled(editable);
    }
    
    private void showNonRm(boolean editable)
    {
        pnlRm.setVisible(false);
        pnlNonRm.setVisible(true);
        
        ckbNonRm.setSelected(true);
        ckbNonRm.setEnabled(editable);
    }
    
    public void isReset()
    {
        jml = tblPemeriksaan.getRowCount();
        for (i = 0; i < jml; i++)
        {
            tblPemeriksaan.setValueAt(false, i, 0);
        }

        kdPeriksa = null;
        
        txtNoRw.setText("");
        txtNoRM.setText("");
        txtNamaPasien.setText("");
        
        txtKdDokterPj.setText("");
        NmDokterPj.setText("");
        txtKdPetugas.setText("");
        NmPtg.setText("");
        txtKdDokter.setText("");
        txtNamaDokter.setText("");
        txtKdKamar.setText("");
        txtNamaKamar.setText("");
        
        // NON RM ==========
        txtNamaNonRm.setText("");
        txtUmurNonRm.setText("");
        txtAlamatNonRm.setText("");
        
        selGroup.clear();
        selPemeriksaan.clear();
        
        tampilGroup();
        tampilPemeriksaan();
        tampilOrder();
        tampilTransaksi();
        tampilNonRm();
        
        showRm(true);
        txtHasil.setText("");
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

    public void setNoRm(String norwt)
    {
        txtNoRw.setText(norwt);

        String[] saRanap = new GQuery()
                    .a("SELECT kamar.kd_kamar, nm_bangsal")
                    .a("FROM kamar_inap")
                    .a("JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                    .a("JOIN bangsal ON bangsal.kd_bangsal = kamar.kd_bangsal")
                    .a("WHERE no_rawat = {no_rw}")
                    .set("no_rw", norwt)
                    .getRow();
        
        String[] saRalan = new GQuery()
                    .a("SELECT poliklinik.kd_poli, nm_poli")
                    .a("FROM reg_periksa")
                    .a("JOIN poliklinik ON poliklinik.kd_poli = reg_periksa.kd_poli")
                    .a("WHERE no_rawat = {no_rw}")
                    .set("no_rw", norwt)
                    .getRow();
        
        if (saRanap != null)
        {
            this.status = "Ranap";
            
            txtKdKamar.setText(saRanap[0]);
            txtNamaKamar.setText(saRanap[1]);
        }
        else if (saRalan != null)
        {
            this.status = "Ralan";

            txtKdKamar.setText(saRalan[0]);
            txtNamaKamar.setText(saRalan[1]);
        }

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
        btnTambahGroup.setEnabled(var.gettarif_radiologi());
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
