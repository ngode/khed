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
import fungsi.GResult;
import fungsi.GRow;
import keuangan.DlgJnsPerawatanRadiologi;
import fungsi.WarnaTable;
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
import pop.PopHasilBacaanRad;
import util.GColors;
import widget.TextBox;

/**
 *
 * @author dosen
 */
public final class DlgOrderRadiologi extends javax.swing.JDialog
{

    private DefaultTableModel mdlPemeriksaan, mdlList, mdlGroup;
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
    private PreparedStatement psMain, psDetail, psGroup;
    private ResultSet rsMain, rsDetail, rsGroup;

    // VARS ===================
    private boolean isEdit = false;
    private String kdPeriksa;
    private DefaultTableModel TemplateLab;
    private List<String> selPemeriksaan = new ArrayList<>();
    private List<String> selGroup = new ArrayList<>();
    private String curPemeriksaan;

    /**
     * Creates new form DlgPerawatan
     *
     * @param parent
     * @param modal
     */
    public DlgOrderRadiologi(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();

        initTableGroup();
        initTablePemeriksaan();
        initTableList();

        txtNoRw.setDocument(new batasInput((byte) 17).getKata(txtNoRw));
        txtKdKamar.setDocument(new batasInput((byte) 20).getKata(txtKdKamar));
        txtKdDokter.setDocument(new batasInput((byte) 20).getKata(txtKdDokter));

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
        
        // ========= Set petugas rad ===============================
        String idUser = var.getkode();
        
        // Kalo dia sbg admin utama, biarin aja
        if (!idUser.equals("Admin Utama"))
        {
            // Tes apakah id user ada di dokter
            String user = Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", idUser);

            // Kalo di dokter gak ada, berarti petugas, kalo petugas set petugas otomatis
            if(user.equals(""))
            {
                txtKdPetugas.setText(idUser);
                Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", NmPtg, idUser);
            }
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
        }

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
                            ((JPopupMenu) e.getSource()).setVisible(false);
                        }

                        int row = tblListTransaksi.getSelectedRow();

                        if (row > -1)
                        {
                            int rowMain = getListMainRow(row);

                            if (tblListTransaksi.getValueAt(rowMain, 5).toString().equals("Belum"))
                            {
                                menuUbah.setEnabled(true);
                                menuHapus.setEnabled(true);
                                menuHasil.setEnabled(false);
                            }
                            else if (tblListTransaksi.getValueAt(rowMain, 5).toString().equals("Mulai"))
                            {
                                menuUbah.setEnabled(false);
                                menuHapus.setEnabled(false);
                                menuHasil.setEnabled(false);
                            }
                            else
                            {
                                menuUbah.setEnabled(false);
                                menuHapus.setEnabled(false);
                                menuHasil.setEnabled(true);
                            }
                        }
                        else
                        {
                            menuUbah.setEnabled(false);
                            menuHapus.setEnabled(false);
                            menuHasil.setEnabled(false);
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

    private void initTableList()
    {
        // Init list order ===========================
        Object[] row =
        {
            "Kd Periksa", "No Rawat", "Pasien", "Tgl Periksa", "Jam Periksa", "Status"
        };

        mdlList = new DefaultTableModel(null, row)
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

        tblListTransaksi.setModel(mdlList);

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
        jPopupMenu1 = new javax.swing.JPopupMenu();
        menuUbah = new javax.swing.JMenuItem();
        menuHapus = new javax.swing.JMenuItem();
        menuHasil = new javax.swing.JMenuItem();
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
        jLabel7 = new widget.Label();
        jLabel13 = new widget.Label();
        txtKdPetugas = new widget.TextBox();
        btnPetugas = new widget.Button();
        NmPtg = new widget.TextBox();
        NmDokterPj = new widget.TextBox();
        txtKdDokterPj = new widget.TextBox();
        btnDokterPj = new widget.Button();
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
        panelisi2 = new widget.panelisi();
        jPanel4 = new javax.swing.JPanel();
        Scroll3 = new widget.ScrollPane();
        tblListTransaksi = new widget.Table();

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
        menuUbah.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
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
        menuHapus.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuHapusActionPerformed(evt);
            }
        });
        jPopupMenu1.add(menuHapus);

        menuHasil.setBackground(new java.awt.Color(255, 255, 255));
        menuHasil.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuHasil.setForeground(new java.awt.Color(60, 80, 50));
        menuHasil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuHasil.setText("Lihat Hasil");
        menuHasil.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuHasil.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuHasil.setIconTextGap(5);
        menuHasil.setName("menuHasil"); // NOI18N
        menuHasil.setPreferredSize(new java.awt.Dimension(220, 26));
        menuHasil.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuHasilActionPerformed(evt);
            }
        });
        jPopupMenu1.add(menuHasil);

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

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Input Order Radiologi ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
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

        jLabel3.setText("No.Rawat :");
        jLabel3.setName("jLabel3"); // NOI18N
        PanelInput.add(jLabel3);
        jLabel3.setBounds(0, 12, 92, 23);

        txtNoRw.setEditable(false);
        txtNoRw.setHighlighter(null);
        txtNoRw.setName("txtNoRw"); // NOI18N
        PanelInput.add(txtNoRw);
        txtNoRw.setBounds(100, 10, 148, 23);

        txtNoRM.setEditable(false);
        txtNoRM.setHighlighter(null);
        txtNoRM.setName("txtNoRM"); // NOI18N
        PanelInput.add(txtNoRM);
        txtNoRM.setBounds(250, 10, 125, 23);

        txtNamaPasien.setEditable(false);
        txtNamaPasien.setHighlighter(null);
        txtNamaPasien.setName("txtNamaPasien"); // NOI18N
        PanelInput.add(txtNamaPasien);
        txtNamaPasien.setBounds(380, 10, 400, 23);

        jLabel9.setText("Dokter Perujuk :");
        jLabel9.setName("jLabel9"); // NOI18N
        PanelInput.add(jLabel9);
        jLabel9.setBounds(0, 70, 92, 23);

        jLabel12.setText("Ruang :");
        jLabel12.setName("jLabel12"); // NOI18N
        PanelInput.add(jLabel12);
        jLabel12.setBounds(410, 70, 50, 23);

        txtKdKamar.setEditable(false);
        txtKdKamar.setName("txtKdKamar"); // NOI18N
        PanelInput.add(txtKdKamar);
        txtKdKamar.setBounds(465, 70, 80, 23);

        txtNamaKamar.setEditable(false);
        txtNamaKamar.setName("txtNamaKamar"); // NOI18N
        PanelInput.add(txtNamaKamar);
        txtNamaKamar.setBounds(550, 70, 230, 23);

        Tanggal.setEditable(false);
        Tanggal.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "30-04-2018" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);
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
        PanelInput.add(ChkJln);
        ChkJln.setBounds(755, 100, 23, 23);

        jLabel16.setText("Jam :");
        jLabel16.setName("jLabel16"); // NOI18N
        PanelInput.add(jLabel16);
        jLabel16.setBounds(530, 100, 78, 23);

        txtKdDokter.setEditable(false);
        txtKdDokter.setName("txtKdDokter"); // NOI18N
        PanelInput.add(txtKdDokter);
        txtKdDokter.setBounds(100, 70, 80, 23);

        txtNamaDokter.setEditable(false);
        txtNamaDokter.setHighlighter(null);
        txtNamaDokter.setName("txtNamaDokter"); // NOI18N
        txtNamaDokter.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtNamaDokterActionPerformed(evt);
            }
        });
        PanelInput.add(txtNamaDokter);
        txtNamaDokter.setBounds(185, 70, 210, 23);

        jLabel15.setText("Tgl.Periksa :");
        jLabel15.setName("jLabel15"); // NOI18N
        PanelInput.add(jLabel15);
        jLabel15.setBounds(380, 100, 87, 23);

        jLabel7.setText("Dokter P.J. :");
        jLabel7.setName("jLabel7"); // NOI18N
        PanelInput.add(jLabel7);
        jLabel7.setBounds(0, 40, 92, 23);

        jLabel13.setText("Petugas :");
        jLabel13.setName("jLabel13"); // NOI18N
        PanelInput.add(jLabel13);
        jLabel13.setBounds(400, 40, 60, 23);

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
        txtKdPetugas.setBounds(465, 40, 80, 23);

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
        btnPetugas.setBounds(750, 40, 28, 23);

        NmPtg.setEditable(false);
        NmPtg.setName("NmPtg"); // NOI18N
        PanelInput.add(NmPtg);
        NmPtg.setBounds(550, 40, 195, 23);

        NmDokterPj.setEditable(false);
        NmDokterPj.setHighlighter(null);
        NmDokterPj.setName("NmDokterPj"); // NOI18N
        PanelInput.add(NmDokterPj);
        NmDokterPj.setBounds(185, 40, 180, 23);

        txtKdDokterPj.setEditable(false);
        txtKdDokterPj.setName("txtKdDokterPj"); // NOI18N
        PanelInput.add(txtKdDokterPj);
        txtKdDokterPj.setBounds(100, 40, 80, 23);

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
        btnDokterPj.setBounds(370, 40, 28, 23);

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
        tblListTransaksi.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblListTransaksiMouseClicked(evt);
            }
        });
        tblListTransaksi.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblListTransaksiKeyPressed(evt);
            }
        });
        Scroll3.setViewportView(tblListTransaksi);

        jPanel4.add(Scroll3, java.awt.BorderLayout.CENTER);

        panelisi2.add(jPanel4, java.awt.BorderLayout.CENTER);

        tabPane.addTab("List Transaksi", panelisi2);

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

    private void menuUbahActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuUbahActionPerformed
    {//GEN-HEADEREND:event_menuUbahActionPerformed
        tabPane.setSelectedIndex(0);

        // Cari baris awal dan akhir
        int row = getListMainRow(tblListTransaksi.getSelectedRow());
        int lastRow = row;

        for (int a = row + 1; a < mdlList.getRowCount(); a++)
        {
            if (tblListTransaksi.getValueAt(a, 0).equals(""))
            {
                lastRow = a;
            }
            else
            {
                break;
            }
        }

        // Masukin data ke kdPeriksa dan selPemeriksaan dan selGroup
        selGroup.clear();
        selPemeriksaan.clear();
        
        for (int a = row; a <= lastRow; a++)
        {
            if (a == row)
            {
                kdPeriksa = tblListTransaksi.getValueAt(a, 0).toString();
            }
            else if (a > row + 1)
            {
                String kdJns = tblListTransaksi.getValueAt(a, 2).toString();
                String kdGroup = Sequel.cariIsi("SELECT kd_group FROM jns_perawatan_radiologi WHERE kd_jenis_prw = '" + kdJns + "'");
                
                selPemeriksaan.add(kdJns);
                
                if (!selGroup.contains(kdGroup))
                {
                    selGroup.add(kdGroup);
                }
            }
        }
        
        // Isi data dokter dan petugas
        Sequel.cariIsi("SELECT kd_dokter_pj FROM pemeriksaan_radiologi WHERE kd_periksa = '" + kdPeriksa + "'", txtKdDokterPj);
        Sequel.cariIsi("SELECT nm_dokter FROM dokter WHERE kd_dokter = '" + txtKdDokterPj.getText() + "'", NmDokterPj);
        Sequel.cariIsi("SELECT nip FROM pemeriksaan_radiologi WHERE kd_periksa = '" + kdPeriksa + "'", txtKdPetugas);
        Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = '" + txtKdPetugas.getText() + "'", NmPtg);
        
        // Tampil-tampil
        tampilGroup();
        tampilPemeriksaan();

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
            {
                JOptionPane.showMessageDialog(rootPane, "Hapus data gagal", "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_menuHapusActionPerformed

    private void tblListTransaksiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblListTransaksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblListTransaksiKeyPressed

    private void tblListTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListTransaksiMouseClicked
        if (evt.getClickCount() == 2)
        {
            
        }
    }//GEN-LAST:event_tblListTransaksiMouseClicked

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
        if (txtNoRw.getText().equals("") || txtNoRM.getText().equals("") || txtNamaPasien.getText().equals(""))
        {
            Valid.textKosong(txtNoRw, "Pasien");
        }
        else if (txtKdKamar.getText().equals("") || txtNamaKamar.getText().equals(""))
        {
            Valid.textKosong(txtKdKamar, "Petugas");
        }
        else if (txtKdDokterPj.getText().equals(""))
        {
            Valid.textKosong(txtKdDokterPj, "Dokter Penjab");
        }
        else if (txtKdPetugas.getText().equals(""))
        {
            Valid.textKosong(txtKdDokter, "Petugas");
        }
        else if (txtKdDokter.getText().equals("") || txtNamaDokter.getText().equals(""))
        {
            Valid.textKosong(txtKdDokter, "Dokter Perujuk");
        }
        else if (mdlPemeriksaan.getRowCount() == 0)
        {
            Valid.textKosong(txtCariGroup, "Data Pemeriksaan");
        }
        else if (Sequel.cariRegistrasi(txtNoRw.getText()) > 0)
        {
            JOptionPane.showMessageDialog(rootPane, "Data billing sudah terverifikasi, data tidak boleh dihapus.\nSilahkan hubungi bagian kasir/keuangan ..!!");
            txtCariGroup.requestFocus();
        }
        else
        {
            int reply = JOptionPane.showConfirmDialog(rootPane, "Eeiiiiiits, udah bener belum data yang mau disimpan..??", "Konfirmasi", JOptionPane.YES_NO_OPTION);

            if (reply == JOptionPane.YES_OPTION)
            {
                if (isEdit)
                {
                    GQuery.setAutoCommit(false);

                    boolean success = true;

                    success &= new GQuery()
                            .a("UPDATE pemeriksaan_radiologi SET")
                            .a("nip = {nip},")
                            .a("kd_dokter_perujuk = {kd_dokter_perujuk},")
                            .a("kd_dokter_pj = {kd_dokter_pj}")
                            .a("WHERE kd_periksa = {kd_periksa}")
                            .set("nip", txtKdPetugas.getText())
                            .set("kd_dokter_perujuk", txtKdDokter.getText())
                            .set("kd_dokter_pj", txtKdDokterPj.getText())
                            .set("kd_periksa", kdPeriksa)
                            .write();

                    success &= new GQuery()
                            .a("DELETE FROM det_pemeriksaan_radiologi WHERE kd_periksa = {kd_periksa}")
                            .set("kd_periksa", kdPeriksa)
                            .write();

                    // Perulangan menyimpan ke tabel detail periksa hd
                    for (i = 0; i < tblPemeriksaan.getRowCount(); i++)
                    {
                        if (tblPemeriksaan.getValueAt(i, 0).toString().equals("true"))
                        {
                            success &= Sequel.menyimpantf2("det_pemeriksaan_radiologi", "?,?,?,?,?,?,?,?,?,?,?", "-", 11, new String[]
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
                                ""
                            });
                        }
                    }

                    GQuery.setAutoCommit(true);

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
                    Sequel.AutoComitFalse();

                    // Ambil no otomatis
                    String kdPeriksa = Sequel.autoNumber("pemeriksaan_radiologi", "kd_periksa");
                    boolean success = true;

                    // Menyimpan ke table periksa hd (UTAMA)
                    success &= Sequel.menyimpantf2("pemeriksaan_radiologi", "?,?,?,NULL,NULL,NULL,NULL,?,?,?,?", "-", 7, new String[]
                    {
                        kdPeriksa,
                        txtNoRw.getText(),
                        txtKdPetugas.getText(),
                        txtKdDokter.getText(),
                        txtKdDokterPj.getText(),
                        status,
                        "Belum"
                    });

                    // Perulangan menyimpan ke tabel detail periksa hd
                    for (i = 0; i < tblPemeriksaan.getRowCount(); i++)
                    {
                        if (tblPemeriksaan.getValueAt(i, 0).toString().equals("true"))
                        {
                            success &= Sequel.menyimpantf2("det_pemeriksaan_radiologi", "?,?,?,?,?,?,?,?,?,?,?", "-", 11, new String[]
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
                                ""
                            });
                        }
                    }

                    Sequel.AutoComitTrue();

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

    private void menuHasilActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuHasilActionPerformed
    {//GEN-HEADEREND:event_menuHasilActionPerformed
        int mainRow = getListMainRow(tblListTransaksi.getSelectedRow());
        String kdPeriksa = tblListTransaksi.getValueAt(mainRow, 0).toString();
        
        GResult res = new GQuery()
                .a("SELECT nm_perawatan, hasil")
                .a("FROM det_pemeriksaan_radiologi")
                .a("JOIN jns_perawatan_radiologi ON jns_perawatan_radiologi.kd_jenis_prw = det_pemeriksaan_radiologi.kd_jenis_prw")
                .a("WHERE kd_periksa = {id}")
                .set("id", kdPeriksa)
                .selectComplete();
        
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        
        for (GRow r : res)
        {
            if (!first) sb.append("\n\n");
            else first = false;
            
            sb.append("Pemeriksaan :\n");
            sb.append(r.getString("nm_perawatan"));
            sb.append("\n\nHasil :\n");
            sb.append(r.getString("hasil"));
        }
        
        PopHasilBacaanRad p = new PopHasilBacaanRad(null, true);
        p.setData(sb.toString());
        p.setSize(internalFrame1.getWidth() - 40, internalFrame1.getHeight() - 40);
        p.setLocationRelativeTo(internalFrame1);
        p.setVisible(true);
    }//GEN-LAST:event_menuHasilActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(()
                -> 
                {
                    DlgOrderRadiologi dialog = new DlgOrderRadiologi(new javax.swing.JFrame(), true);
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
    private widget.Tanggal Tanggal;
    private widget.TextBox Umur;
    private widget.Button btnAllGroup;
    private widget.Button btnAllPemeriksaan;
    private widget.Button btnCariGroup;
    private widget.Button btnCariPemeriksaan;
    private widget.Button btnDokterPj;
    private widget.Button btnPetugas;
    private widget.Button btnTambahGroup;
    private widget.Button btnTambahPemeriksaan;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel3;
    private widget.Label jLabel7;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.Label label10;
    private widget.Label label11;
    private javax.swing.JMenuItem menuHapus;
    private javax.swing.JMenuItem menuHasil;
    private javax.swing.JMenuItem menuUbah;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelisi1;
    private widget.panelisi panelisi2;
    private widget.panelisi panelisi5;
    private widget.panelisi panelisi6;
    private widget.TabPane tabPane;
    private widget.Table tblGroup;
    private widget.Table tblListTransaksi;
    private widget.Table tblPemeriksaan;
    private widget.TextBox txtCariGroup;
    private widget.TextBox txtCariPemeriksaan;
    private widget.TextBox txtKdDokter;
    private widget.TextBox txtKdDokterPj;
    private widget.TextBox txtKdKamar;
    private widget.TextBox txtKdPetugas;
    private widget.TextBox txtNamaDokter;
    private widget.TextBox txtNamaKamar;
    private widget.TextBox txtNamaPasien;
    private widget.TextBox txtNoRM;
    private widget.TextBox txtNoRw;
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

    // BUta nampilin list order + transaksi
    private void tampilList()
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
                    .a("WHERE pemeriksaan_radiologi.no_rawat = {no_rawat}")
                    .set("no_rawat", txtNoRw.getText())
                    .compile();

            GQuery qDet = new GQuery()
                    .a("SELECT det_pemeriksaan_radiologi.kd_jenis_prw, nm_perawatan, det_pemeriksaan_radiologi.biaya")
                    .a("FROM det_pemeriksaan_radiologi")
                    .a("JOIN jns_perawatan_radiologi ON jns_perawatan_radiologi.kd_jenis_prw = det_pemeriksaan_radiologi.kd_jenis_prw")
                    .a("WHERE kd_periksa = {kd_periksa}");

            Valid.tabelKosong(mdlList);
            psMain = koneksi.prepareStatement(qMain);
            rsMain = psMain.executeQuery();

            while (rsMain.next())
            {
                String pas;

                if (rsMain.getString("kd_kamar") != null)
                {
                    pas = rsMain.getString("no_rkm_medis") + " " + rsMain.getString("nm_pasien") + " (Kamar : "
                            + rsMain.getString("kd_kamar") + ", " + rsMain.getString("nm_bangsal") + ")";
                }
                else
                {
                    pas = rsMain.getString("no_rkm_medis") + " " + rsMain.getString("nm_pasien") + " (Poli : "
                            + rsMain.getString("nm_poli") + ")";
                }

                Object[] o = new Object[]
                {
                    rsMain.getString("kd_periksa"),
                    rsMain.getString("no_rawat"),
                    pas,
                    rsMain.getString("tgl_periksa"),
                    rsMain.getString("jam"),
                    rsMain.getString("proses")
                };

                mdlList.addRow(o);
                mdlList.addRow(new Object[]
                {
                    "", "", "Kode Periksa", "Nama Pemeriksaan", "Biaya Pemeriksaan", ""
                });

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
                        Valid.SetAngka(rsDetail.getDouble("biaya")),
                        ""
                    };

                    mdlList.addRow(od);
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

        //txtKdDokterPj.setText("");
        //NmDokterPj.setText("");
        txtKdPetugas.setText("");
        NmPtg.setText("");
        
        selGroup.clear();
        selPemeriksaan.clear();
        
        tampilGroup();
        tampilPemeriksaan();
        tampilList();
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

    public void setNoRm(String norwt, String posisi)
    {
        txtNoRw.setText(norwt);
        this.status = posisi;

        if (posisi.equals("Ranap"))
        {
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
        }
        else
        {
            String[] sa = new GQuery()
                    .a("SELECT poliklinik.kd_poli, nm_poli")
                    .a("FROM reg_periksa")
                    .a("JOIN poliklinik ON poliklinik.kd_poli = reg_periksa.kd_poli")
                    .a("WHERE no_rawat = {no_rw}")
                    .set("no_rw", norwt)
                    .getRow();

            txtKdKamar.setText(sa[0]);
            txtNamaKamar.setText(sa[1]);
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
        if (var.getjml2() >= 1)
        {
            txtKdPetugas.setText(var.getkode());
            Sequel.cariIsi("select nama from petugas where nip=?", NmPtg, txtKdPetugas.getText());
        }
        else
        {
            txtKdPetugas.setText("");
            NmPtg.setText("");
        }
        
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
