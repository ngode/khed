package keuangan;
import restore.DlgRestoreTarifLab;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.var;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import simrskhanza.DlgPenanggungJawab;

/**
 *
 * @author dosen
 */
public final class DlgJnsPerawatanLab extends javax.swing.JDialog {
    private final DefaultTableModel tabMode,tabMode2,tabMode3;
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private Connection koneksi=koneksiDB.condb();
    private PreparedStatement ps;
    private ResultSet rs;    
    public DlgPenanggungJawab penjab=new DlgPenanggungJawab(null,false);
    public DlgKategoriLab kategori=new DlgKategoriLab(null,false);
    public DlgTemplateLaboratorium template=new DlgTemplateLaboratorium(null,false);
    public DlgTemplateLaboratorium2 template2=new DlgTemplateLaboratorium2(null,false);  
    public DlgTarifLab tarif=new DlgTarifLab(null,false);
    private int i=0;
    
    /** Creates new form DlgJnsPerawatanRalan
     * @param parent
     * @param modal */
    public DlgJnsPerawatanLab(java.awt.Frame parent, boolean modal) {
            super(parent, modal);
        initComponents();
        this.setLocation(10, 2);
        
        tabMode = new DefaultTableModel(null, new String[]
        {
            "Kode", "Nama Pemeriksaan"
        })
        {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                return false;
            }
        };
        
        tblNamaPeriksa.setModel(tabMode);
        tblNamaPeriksa.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tblNamaPeriksa.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < 2; i++)
        {
            TableColumn column = tblNamaPeriksa.getColumnModel().getColumn(i);
            if (i == 0)
            {
                column.setPreferredWidth(50);
            }
            else if (i == 1)
            {
                column.setPreferredWidth(240);
            }
        }
        
        tblNamaPeriksa.setDefaultRenderer(Object.class, new WarnaTable());
        txtCari.setDocument(new batasInput((byte) 100).getKata(txtCari));
        
        tabMode2 = new DefaultTableModel(null, new String[]
        {
            "ID Template", "Pemeriksaan", "Satuan", "Nilai Rujukan LD", "Nilai Rujukan LA",
            "Nilai Rujukan PD", "Nilai Rujukan PA", "Urut"
        })
        {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                return false;
            }
        };
        
        tblTemplate.setModel(tabMode2);
        tblTemplate.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tblTemplate.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < 8; i++)
        {
            TableColumn column = tblTemplate.getColumnModel().getColumn(i);
            if (i == 0)
            {
                column.setPreferredWidth(90);
            }
            else if (i == 1)
            {
                column.setPreferredWidth(240);
            }
            else if (i == 2)
            {
                column.setPreferredWidth(110);
            }
            else if (i == 3)
            {
                column.setPreferredWidth(112);
            }
            else if (i == 4)
            {
                column.setPreferredWidth(113);
            }
            else if (i == 5)
            {
                column.setPreferredWidth(114);
            }
            else if (i == 6)
            {
                column.setPreferredWidth(115);
            }
            else if (i == 7)
            {
                column.setPreferredWidth(68);
            }
        }
        
        tblTemplate.setDefaultRenderer(Object.class, new WarnaTable());
        txtCari.setDocument(new batasInput((byte) 100).getKata(txtCari));
        txtCari1.setDocument(new batasInput((byte) 100).getKata(txtCari1));

        
        tabMode3 = new DefaultTableModel(null, new String[]
        {
            "ID Template", "Pemeriksaan", "Satuan", "Nilai Rujukan LD", "Nilai Rujukan LA",
            "Nilai Rujukan PD", "Nilai Rujukan PA", "Urut"
        })
        {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                return false;
            }
        };
        
        tblTemplate.setModel(tabMode2);
        tblTemplate.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tblTemplate.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < 8; i++)
        {
            TableColumn column = tblTemplate.getColumnModel().getColumn(i);
            if (i == 0)
            {
                column.setPreferredWidth(90);
            }
            else if (i == 1)
            {
                column.setPreferredWidth(240);
            }
            else if (i == 2)
            {
                column.setPreferredWidth(110);
            }
            else if (i == 3)
            {
                column.setPreferredWidth(112);
            }
            else if (i == 4)
            {
                column.setPreferredWidth(113);
            }
            else if (i == 5)
            {
                column.setPreferredWidth(114);
            }
            else if (i == 6)
            {
                column.setPreferredWidth(115);
            }
            else if (i == 7)
            {
                column.setPreferredWidth(68);
            }
        }
        
        tblTemplate.setDefaultRenderer(Object.class, new WarnaTable());
        if (koneksiDB.cariCepat().equals("aktif"))
        {
            txtCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener()                   {
                @Override
                public void insertUpdate(DocumentEvent e)
                {
                    tampil();
                 //   tampilTemplate();
                }

                @Override
                public void removeUpdate(DocumentEvent e)
                {
                    tampil();
                   // tampilTemplate();
                }

                @Override
                public void changedUpdate(DocumentEvent e)
                {
                    tampil();
                   // tampilTemplate();
                }
                
            });
            ChkInput.setSelected(false);
            isForm();
        }
        
        if (koneksiDB.cariCepat().equals("aktif"))
        {
            txtCari1.getDocument().addDocumentListener(new javax.swing.event.DocumentListener()                   {
                @Override
                public void insertUpdate(DocumentEvent e)
                {
                 //   tampil();
                    tampilTemplate();
                }

                @Override
                public void removeUpdate(DocumentEvent e)
                {
                   // tampil();
                    tampilTemplate();
                }

                @Override
                public void changedUpdate(DocumentEvent e)
                {
                   // tampil();
                    tampilTemplate();
                }
                
            });
        }        
    }
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Popup = new javax.swing.JPopupMenu();
        ppTemplate = new javax.swing.JMenuItem();
        MnRestore = new javax.swing.JMenuItem();
        Popup2 = new javax.swing.JPopupMenu();
        ppDetTemplate = new javax.swing.JMenuItem();
        MnRestore1 = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        tabPane = new widget.TabPane();
        panelisi1 = new widget.panelisi();
        Scroll1 = new widget.ScrollPane();
        tblTemplate = new widget.Table();
        panelGlass10 = new widget.panelisi();
        BtnKeluar1 = new widget.Button();
        lblCount = new widget.Label();
        lblCount1 = new widget.Label();
        txtCari = new widget.TextBox();
        txtCari1 = new widget.TextBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        Scroll = new widget.ScrollPane();
        tblNamaPeriksa = new widget.Table();
        PanelInput = new javax.swing.JPanel();
        FormInput = new widget.PanelBiasa();
        jLabel3 = new widget.Label();
        TKd = new widget.TextBox();
        jLabel8 = new widget.Label();
        TNm = new widget.TextBox();
        BtnKategori = new widget.Button();
        BtnTarif = new widget.Button();
        IDTemp = new widget.TextBox();
        NMTemp = new widget.TextBox();
        jLabel10 = new widget.Label();
        jLabel5 = new widget.Label();
        BtnTemplate1 = new widget.Button();
        ChkInput = new widget.CekBox();
        panelisi2 = new widget.panelisi();
        Scroll2 = new widget.ScrollPane();
        tbJnsPerawatan = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        BtnPrint = new widget.Button();
        BtnAll = new widget.Button();
        BtnKeluar = new widget.Button();
        panelGlass9 = new widget.panelisi();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        jLabel11 = new widget.Label();
        LCount = new widget.Label();
        PanelInput1 = new javax.swing.JPanel();
        FormInput1 = new widget.PanelBiasa();
        TKd1 = new widget.TextBox();
        TNm1 = new widget.TextBox();
        jLabel12 = new widget.Label();
        jLabel13 = new widget.Label();
        jLabel14 = new widget.Label();
        jLabel15 = new widget.Label();
        jLabel16 = new widget.Label();
        jLabel17 = new widget.Label();
        BagianRs = new widget.TextBox();
        Bhp = new widget.TextBox();
        JMDokter = new widget.TextBox();
        JMLaborat = new widget.TextBox();
        JMPerujuk = new widget.TextBox();
        TotalBiaya = new widget.TextBox();
        jLabel18 = new widget.Label();
        KSO = new widget.TextBox();
        jLabel19 = new widget.Label();
        Menejemen = new widget.TextBox();
        TKd2 = new widget.TextBox();
        jLabel4 = new widget.Label();
        Kelas = new widget.ComboBox();
        jLabel20 = new widget.Label();
        ChkInput1 = new widget.CekBox();

        Popup.setName("Popup"); // NOI18N

        ppTemplate.setBackground(new java.awt.Color(255, 255, 255));
        ppTemplate.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppTemplate.setForeground(new java.awt.Color(50, 70, 40));
        ppTemplate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppTemplate.setText("Template Laboratorium");
        ppTemplate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppTemplate.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppTemplate.setIconTextGap(5);
        ppTemplate.setName("ppTemplate"); // NOI18N
        ppTemplate.setPreferredSize(new java.awt.Dimension(150, 25));
        ppTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppTemplateActionPerformed(evt);
            }
        });
        Popup.add(ppTemplate);

        MnRestore.setBackground(new java.awt.Color(255, 255, 255));
        MnRestore.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnRestore.setForeground(new java.awt.Color(60, 80, 50));
        MnRestore.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnRestore.setText("Data Sampah");
        MnRestore.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnRestore.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnRestore.setIconTextGap(5);
        MnRestore.setName("MnRestore"); // NOI18N
        MnRestore.setPreferredSize(new java.awt.Dimension(200, 28));
        MnRestore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnRestoreActionPerformed(evt);
            }
        });
        Popup.add(MnRestore);

        Popup2.setName("Popup2"); // NOI18N

        ppDetTemplate.setBackground(new java.awt.Color(255, 255, 255));
        ppDetTemplate.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppDetTemplate.setForeground(new java.awt.Color(50, 70, 40));
        ppDetTemplate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppDetTemplate.setText(" Detail Template Laboratorium");
        ppDetTemplate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppDetTemplate.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppDetTemplate.setIconTextGap(5);
        ppDetTemplate.setName("ppDetTemplate"); // NOI18N
        ppDetTemplate.setPreferredSize(new java.awt.Dimension(150, 25));
        ppDetTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppDetTemplateActionPerformed(evt);
            }
        });
        Popup2.add(ppDetTemplate);

        MnRestore1.setBackground(new java.awt.Color(255, 255, 255));
        MnRestore1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnRestore1.setForeground(new java.awt.Color(60, 80, 50));
        MnRestore1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnRestore1.setText("Data Sampah");
        MnRestore1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnRestore1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnRestore1.setIconTextGap(5);
        MnRestore1.setName("MnRestore1"); // NOI18N
        MnRestore1.setPreferredSize(new java.awt.Dimension(200, 28));
        MnRestore1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnRestore1ActionPerformed(evt);
            }
        });
        Popup2.add(MnRestore1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Tarif Pemeriksaan Laboratorium ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        tabPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 10))); // NOI18N
        tabPane.setName("tabPane"); // NOI18N

        panelisi1.setName("panelisi1"); // NOI18N
        panelisi1.setLayout(new java.awt.BorderLayout());

        Scroll1.setComponentPopupMenu(Popup2);
        Scroll1.setName("Scroll1"); // NOI18N
        Scroll1.setOpaque(true);

        tblTemplate.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tblTemplate.setComponentPopupMenu(Popup2);
        tblTemplate.setName("tblTemplate"); // NOI18N
        tblTemplate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTemplateMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblTemplateMouseReleased(evt);
            }
        });
        Scroll1.setViewportView(tblTemplate);

        panelisi1.add(Scroll1, java.awt.BorderLayout.CENTER);

        panelGlass10.setName("panelGlass10"); // NOI18N
        panelGlass10.setPreferredSize(new java.awt.Dimension(55, 50));
        panelGlass10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BtnKeluar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar1.setMnemonic('K');
        BtnKeluar1.setText("Keluar");
        BtnKeluar1.setToolTipText("Alt+K");
        BtnKeluar1.setName("BtnKeluar1"); // NOI18N
        BtnKeluar1.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluar1ActionPerformed(evt);
            }
        });
        BtnKeluar1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluar1KeyPressed(evt);
            }
        });
        panelGlass10.add(BtnKeluar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 10, -1, -1));

        lblCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCount.setText("0");
        lblCount.setName("lblCount"); // NOI18N
        lblCount.setPreferredSize(new java.awt.Dimension(50, 23));
        panelGlass10.add(lblCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 13, 36, 30));

        lblCount1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCount1.setText("0");
        lblCount1.setName("lblCount1"); // NOI18N
        lblCount1.setPreferredSize(new java.awt.Dimension(50, 23));
        panelGlass10.add(lblCount1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 13, -1, 30));

        txtCari.setHighlighter(null);
        txtCari.setMinimumSize(new java.awt.Dimension(146, 274));
        txtCari.setName("txtCari"); // NOI18N
        txtCari.setPreferredSize(new java.awt.Dimension(87, 24));
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCariKeyPressed(evt);
            }
        });
        panelGlass10.add(txtCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 15, 140, -1));

        txtCari1.setHighlighter(null);
        txtCari1.setMinimumSize(new java.awt.Dimension(146, 274));
        txtCari1.setName("txtCari1"); // NOI18N
        txtCari1.setPreferredSize(new java.awt.Dimension(67, 24));
        txtCari1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCari1KeyPressed(evt);
            }
        });
        panelGlass10.add(txtCari1, new org.netbeans.lib.awtextra.AbsoluteConstraints(507, 15, 160, -1));

        jLabel1.setText("Record :");
        jLabel1.setName("jLabel1"); // NOI18N
        panelGlass10.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 20, -1, -1));

        jLabel2.setText("Keyword: ");
        jLabel2.setName("jLabel2"); // NOI18N
        panelGlass10.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 60, -1));

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        panelGlass10.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(348, 20, -1, -1));

        jLabel9.setText("Keyword: ");
        jLabel9.setName("jLabel9"); // NOI18N
        panelGlass10.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 20, 60, -1));

        panelisi1.add(panelGlass10, java.awt.BorderLayout.PAGE_END);

        Scroll.setComponentPopupMenu(Popup);
        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);
        Scroll.setPreferredSize(new java.awt.Dimension(310, 402));
        Scroll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ScrollMouseClicked(evt);
            }
        });

        tblNamaPeriksa.setAutoCreateRowSorter(true);
        tblNamaPeriksa.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tblNamaPeriksa.setComponentPopupMenu(Popup);
        tblNamaPeriksa.setName("tblNamaPeriksa"); // NOI18N
        tblNamaPeriksa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNamaPeriksaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblNamaPeriksaMouseEntered(evt);
            }
        });
        tblNamaPeriksa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblNamaPeriksaKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tblNamaPeriksa);

        panelisi1.add(Scroll, java.awt.BorderLayout.WEST);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 150));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(200, 125));
        FormInput.setLayout(null);

        jLabel3.setText("Kode :");
        jLabel3.setName("jLabel3"); // NOI18N
        FormInput.add(jLabel3);
        jLabel3.setBounds(-10, 70, 70, 23);

        TKd.setHighlighter(null);
        TKd.setName("TKd"); // NOI18N
        TKd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TKdKeyPressed(evt);
            }
        });
        FormInput.add(TKd);
        TKd.setBounds(10, 90, 80, 23);

        jLabel8.setText("Pemeriksaan :");
        jLabel8.setName("jLabel8"); // NOI18N
        FormInput.add(jLabel8);
        jLabel8.setBounds(110, 70, 90, 23);

        TNm.setHighlighter(null);
        TNm.setName("TNm"); // NOI18N
        TNm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNmKeyPressed(evt);
            }
        });
        FormInput.add(TNm);
        TNm.setBounds(110, 90, 200, 23);

        BtnKategori.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        BtnKategori.setMnemonic('S');
        BtnKategori.setText("Kategori");
        BtnKategori.setToolTipText("Alt+S");
        BtnKategori.setName("BtnKategori"); // NOI18N
        BtnKategori.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKategoriActionPerformed(evt);
            }
        });
        BtnKategori.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKategoriKeyPressed(evt);
            }
        });
        FormInput.add(BtnKategori);
        BtnKategori.setBounds(10, 10, 90, 30);

        BtnTarif.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        BtnTarif.setMnemonic('B');
        BtnTarif.setText("Tarif");
        BtnTarif.setToolTipText("Alt+B");
        BtnTarif.setName("BtnTarif"); // NOI18N
        BtnTarif.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnTarif.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTarifActionPerformed(evt);
            }
        });
        BtnTarif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnTarifKeyPressed(evt);
            }
        });
        FormInput.add(BtnTarif);
        BtnTarif.setBounds(220, 10, 90, 30);

        IDTemp.setHighlighter(null);
        IDTemp.setName("IDTemp"); // NOI18N
        IDTemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDTempActionPerformed(evt);
            }
        });
        IDTemp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IDTempKeyPressed(evt);
            }
        });
        FormInput.add(IDTemp);
        IDTemp.setBounds(430, 90, 100, 23);

        NMTemp.setHighlighter(null);
        NMTemp.setName("NMTemp"); // NOI18N
        NMTemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NMTempActionPerformed(evt);
            }
        });
        NMTemp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NMTempKeyPressed(evt);
            }
        });
        FormInput.add(NMTemp);
        NMTemp.setBounds(540, 90, 250, 23);

        jLabel10.setText("Nama Template :");
        jLabel10.setName("jLabel10"); // NOI18N
        FormInput.add(jLabel10);
        jLabel10.setBounds(520, 70, 130, 23);

        jLabel5.setText("Kode :");
        jLabel5.setName("jLabel5"); // NOI18N
        FormInput.add(jLabel5);
        jLabel5.setBounds(360, 70, 120, 23);

        BtnTemplate1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        BtnTemplate1.setMnemonic('B');
        BtnTemplate1.setText("Template");
        BtnTemplate1.setToolTipText("Alt+B");
        BtnTemplate1.setName("BtnTemplate1"); // NOI18N
        BtnTemplate1.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnTemplate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTemplate1ActionPerformed(evt);
            }
        });
        BtnTemplate1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnTemplate1KeyPressed(evt);
            }
        });
        FormInput.add(BtnTemplate1);
        BtnTemplate1.setBounds(110, 10, 100, 30);

        PanelInput.add(FormInput, java.awt.BorderLayout.CENTER);

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('I');
        ChkInput.setText(".: Tambah Kategori Lab");
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
        PanelInput.add(ChkInput, java.awt.BorderLayout.PAGE_END);

        panelisi1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        tabPane.addTab("Pemeriksaan", panelisi1);

        panelisi2.setName("panelisi2"); // NOI18N
        panelisi2.setLayout(new java.awt.BorderLayout());

        Scroll2.setName("Scroll2"); // NOI18N
        Scroll2.setOpaque(true);
        Scroll2.setPreferredSize(new java.awt.Dimension(200, 125));

        tbJnsPerawatan.setAutoCreateRowSorter(true);
        tbJnsPerawatan.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbJnsPerawatan.setName("tbJnsPerawatan"); // NOI18N
        tbJnsPerawatan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbJnsPerawatanMouseClicked(evt);
            }
        });
        tbJnsPerawatan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbJnsPerawatanKeyPressed(evt);
            }
        });
        Scroll2.setViewportView(tbJnsPerawatan);

        panelisi2.add(Scroll2, java.awt.BorderLayout.CENTER);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Simpan");
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setName("BtnSimpan"); // NOI18N
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

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setName("BtnHapus"); // NOI18N
        BtnHapus.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });
        BtnHapus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapusKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnHapus);

        BtnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnEdit.setMnemonic('G');
        BtnEdit.setText("Ganti");
        BtnEdit.setToolTipText("Alt+G");
        BtnEdit.setName("BtnEdit"); // NOI18N
        BtnEdit.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditActionPerformed(evt);
            }
        });
        BtnEdit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnEditKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnEdit);

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

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setText("Semua");
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllActionPerformed(evt);
            }
        });
        BtnAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnAll);

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

        jPanel3.add(panelGlass8, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(450, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass9.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('2');
        BtnCari.setToolTipText("Alt+2");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariActionPerformed(evt);
            }
        });
        BtnCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnCari);

        jLabel11.setText("Record :");
        jLabel11.setName("jLabel11"); // NOI18N
        jLabel11.setPreferredSize(new java.awt.Dimension(75, 23));
        panelGlass9.add(jLabel11);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass9.add(LCount);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        panelisi2.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        PanelInput1.setName("PanelInput1"); // NOI18N
        PanelInput1.setOpaque(false);
        PanelInput1.setPreferredSize(new java.awt.Dimension(192, 220));
        PanelInput1.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput1.setName("FormInput1"); // NOI18N
        FormInput1.setPreferredSize(new java.awt.Dimension(200, 125));
        FormInput1.setLayout(null);

        TKd1.setHighlighter(null);
        TKd1.setName("TKd1"); // NOI18N
        TKd1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TKd1KeyPressed(evt);
            }
        });
        FormInput1.add(TKd1);
        TKd1.setBounds(240, 10, 110, 23);

        TNm1.setHighlighter(null);
        TNm1.setName("TNm1"); // NOI18N
        TNm1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNm1KeyPressed(evt);
            }
        });
        FormInput1.add(TNm1);
        TNm1.setBounds(370, 10, 250, 23);

        jLabel12.setText("Jasa RS: Rp.");
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput1.add(jLabel12);
        jLabel12.setBounds(-10, 42, 135, 23);

        jLabel13.setText("Total Biaya : Rp. ");
        jLabel13.setName("jLabel13"); // NOI18N
        FormInput1.add(jLabel13);
        jLabel13.setBounds(300, 102, 153, 23);

        jLabel14.setText("J.M. Petugas : Rp.");
        jLabel14.setName("jLabel14"); // NOI18N
        FormInput1.add(jLabel14);
        jLabel14.setBounds(-10, 132, 135, 23);

        jLabel15.setText("J.M. Dokter : Rp.");
        jLabel15.setName("jLabel15"); // NOI18N
        FormInput1.add(jLabel15);
        jLabel15.setBounds(-10, 102, 135, 23);

        jLabel16.setText("J.M. Perujuk : Rp.");
        jLabel16.setName("jLabel16"); // NOI18N
        FormInput1.add(jLabel16);
        jLabel16.setBounds(-10, 162, 135, 23);

        jLabel17.setText("Paket BHP : Rp.");
        jLabel17.setName("jLabel17"); // NOI18N
        FormInput1.add(jLabel17);
        jLabel17.setBounds(-10, 72, 135, 23);

        BagianRs.setText("0");
        BagianRs.setHighlighter(null);
        BagianRs.setName("BagianRs"); // NOI18N
        BagianRs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BagianRsKeyPressed(evt);
            }
        });
        FormInput1.add(BagianRs);
        BagianRs.setBounds(127, 42, 170, 23);

        Bhp.setText("0");
        Bhp.setHighlighter(null);
        Bhp.setName("Bhp"); // NOI18N
        Bhp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BhpKeyPressed(evt);
            }
        });
        FormInput1.add(Bhp);
        Bhp.setBounds(127, 72, 170, 23);

        JMDokter.setText("0");
        JMDokter.setHighlighter(null);
        JMDokter.setName("JMDokter"); // NOI18N
        JMDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JMDokterKeyPressed(evt);
            }
        });
        FormInput1.add(JMDokter);
        JMDokter.setBounds(127, 102, 170, 23);

        JMLaborat.setText("0");
        JMLaborat.setHighlighter(null);
        JMLaborat.setName("JMLaborat"); // NOI18N
        JMLaborat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JMLaboratKeyPressed(evt);
            }
        });
        FormInput1.add(JMLaborat);
        JMLaborat.setBounds(127, 132, 170, 23);

        JMPerujuk.setText("0");
        JMPerujuk.setHighlighter(null);
        JMPerujuk.setName("JMPerujuk"); // NOI18N
        JMPerujuk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JMPerujukKeyPressed(evt);
            }
        });
        FormInput1.add(JMPerujuk);
        JMPerujuk.setBounds(127, 162, 170, 23);

        TotalBiaya.setText("0");
        TotalBiaya.setHighlighter(null);
        TotalBiaya.setName("TotalBiaya"); // NOI18N
        TotalBiaya.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TotalBiayaKeyPressed(evt);
            }
        });
        FormInput1.add(TotalBiaya);
        TotalBiaya.setBounds(453, 102, 170, 23);

        jLabel18.setText("K.S.0.: Rp. ");
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput1.add(jLabel18);
        jLabel18.setBounds(300, 42, 153, 23);

        KSO.setText("0");
        KSO.setHighlighter(null);
        KSO.setName("KSO"); // NOI18N
        KSO.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KSOKeyPressed(evt);
            }
        });
        FormInput1.add(KSO);
        KSO.setBounds(453, 42, 170, 23);

        jLabel19.setText("Manajemen : Rp. ");
        jLabel19.setName("jLabel19"); // NOI18N
        FormInput1.add(jLabel19);
        jLabel19.setBounds(300, 72, 153, 23);

        Menejemen.setText("0");
        Menejemen.setHighlighter(null);
        Menejemen.setName("Menejemen"); // NOI18N
        Menejemen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MenejemenKeyPressed(evt);
            }
        });
        FormInput1.add(Menejemen);
        Menejemen.setBounds(453, 72, 170, 23);

        TKd2.setHighlighter(null);
        TKd2.setName("TKd2"); // NOI18N
        TKd2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TKd2KeyPressed(evt);
            }
        });
        FormInput1.add(TKd2);
        TKd2.setBounds(130, 10, 90, 23);

        jLabel4.setText("Nama Periksa :");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput1.add(jLabel4);
        jLabel4.setBounds(30, 10, 90, 23);

        Kelas.setForeground(new java.awt.Color(153, 0, 51));
        Kelas.setMaximumRowCount(12);
        Kelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kelas 1", "Kelas 2", "Kelas 3", "Kelas Utama", "Kelas VIP", "Kelas VVIP", "Isolasi", "Psikiatri", "Perinatologi", "ICU", "IMC", "-" }));
        Kelas.setName("Kelas"); // NOI18N
        Kelas.setOpaque(false);
        Kelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KelasActionPerformed(evt);
            }
        });
        Kelas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KelasKeyPressed(evt);
            }
        });
        FormInput1.add(Kelas);
        Kelas.setBounds(460, 130, 160, 23);

        jLabel20.setText("Kelas :");
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput1.add(jLabel20);
        jLabel20.setBounds(360, 130, 90, 23);

        PanelInput1.add(FormInput1, java.awt.BorderLayout.CENTER);

        ChkInput1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput1.setMnemonic('I');
        ChkInput1.setText(".: Input Data");
        ChkInput1.setToolTipText("Alt+I");
        ChkInput1.setBorderPainted(true);
        ChkInput1.setBorderPaintedFlat(true);
        ChkInput1.setFocusable(false);
        ChkInput1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput1.setName("ChkInput1"); // NOI18N
        ChkInput1.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput1.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInput1ActionPerformed(evt);
            }
        });
        PanelInput1.add(ChkInput1, java.awt.BorderLayout.PAGE_END);

        panelisi2.add(PanelInput1, java.awt.BorderLayout.PAGE_START);

        tabPane.addTab("Tarif", panelisi2);

        internalFrame1.add(tabPane, java.awt.BorderLayout.CENTER);
        tabPane.getAccessibleContext().setAccessibleName("PemeriksaanLab");
        tabPane.getAccessibleContext().setAccessibleDescription("");

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ppTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppTemplateActionPerformed
        if(TNm.getText().trim().equals("")){
            Valid.textKosong(TNm,"Nama Pemeriksaan");
        }else{
            template.KdPeriksa.setText(tblNamaPeriksa.getValueAt(tblNamaPeriksa.getSelectedRow(),0).toString());
            template.NmPeriksa.setText(tblNamaPeriksa.getValueAt(tblNamaPeriksa.getSelectedRow(),1).toString());
            template.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
            template.setLocationRelativeTo(internalFrame1);
            template.tampil();
            template.setVisible(true);
        }            
    }//GEN-LAST:event_ppTemplateActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        penjab.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(penjab.getTable().getSelectedRow()!= -1){
//                    kdpnj.setText(penjab.getTable().getValueAt(penjab.getTable().getSelectedRow(),0).toString());
//                    nmpnj.setText(penjab.getTable().getValueAt(penjab.getTable().getSelectedRow(),1).toString());
                }    
                txtCari.requestFocus();
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {penjab.onCari();}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });  
        penjab.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    penjab.dispose();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }//GEN-LAST:event_formWindowActivated

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        penjab.removeWindowListener(null);
        penjab.getTable().removeKeyListener(null);
    }//GEN-LAST:event_formWindowClosed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        tampil();
        tampilTemplate();
    }//GEN-LAST:event_formWindowOpened

    private void MnRestoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnRestoreActionPerformed
        DlgRestoreTarifLab restore=new DlgRestoreTarifLab(null,true);
        restore.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        restore.setLocationRelativeTo(internalFrame1);
        restore.setVisible(true);
    }//GEN-LAST:event_MnRestoreActionPerformed

    private void tblNamaPeriksaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblNamaPeriksaKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }

        }
    }//GEN-LAST:event_tblNamaPeriksaKeyPressed

    private void tblNamaPeriksaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNamaPeriksaMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tblNamaPeriksaMouseClicked

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void txtCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyPressed
 
    }//GEN-LAST:event_txtCariKeyPressed

    private void TNmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNmKeyPressed
    
    }//GEN-LAST:event_TNmKeyPressed

    private void TKdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TKdKeyPressed
    //    Valid.pindah(evt,JMLaborat,TNm,TCari);
    }//GEN-LAST:event_TKdKeyPressed

    private void BtnKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKategoriActionPerformed
        kategori.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        kategori.setLocationRelativeTo(internalFrame1);
        kategori.setVisible(true);
    }//GEN-LAST:event_BtnKategoriActionPerformed

    private void BtnKategoriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKategoriKeyPressed

    }//GEN-LAST:event_BtnKategoriKeyPressed

    private void BtnTarifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTarifActionPerformed
        tarif.setSize(internalFrame1.getWidth()-4,internalFrame1.getHeight()-4);
        tarif.setLocationRelativeTo(internalFrame1);
        tarif.setVisible(true);
    }//GEN-LAST:event_BtnTarifActionPerformed

    private void BtnTarifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnTarifKeyPressed

    }//GEN-LAST:event_BtnTarifKeyPressed

    private void BtnKeluar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluar1ActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluar1ActionPerformed

    private void BtnKeluar1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluar1KeyPressed

    }//GEN-LAST:event_BtnKeluar1KeyPressed

    private void txtCari1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCari1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCari1KeyPressed

    private void ScrollMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ScrollMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_ScrollMouseClicked

    private void tblNamaPeriksaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNamaPeriksaMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tblNamaPeriksaMouseEntered

    private void ppDetTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppDetTemplateActionPerformed
        if(NMTemp.getText().trim().equals("")){
            Valid.textKosong(NMTemp,"Nama Pemeriksaan");
        }else{
            template2.KdPeriksa.setText(tblTemplate.getValueAt(tblTemplate.getSelectedRow(),0).toString());
            template2.NmPeriksa.setText(tblTemplate.getValueAt(tblTemplate.getSelectedRow(),1).toString());
            template2.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
            template2.setLocationRelativeTo(internalFrame1);
            template2.tampil();
            template2.setVisible(true);
        }  
    }//GEN-LAST:event_ppDetTemplateActionPerformed

    private void MnRestore1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnRestore1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MnRestore1ActionPerformed

    private void tblTemplateMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTemplateMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTemplateMouseReleased

    private void tblTemplateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTemplateMouseClicked
        if(tabMode2.getRowCount()!=0){
            try {
                getTemp();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tblTemplateMouseClicked

    private void IDTempKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IDTempKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_IDTempKeyPressed

    private void NMTempKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NMTempKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NMTempKeyPressed

    private void IDTempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDTempActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IDTempActionPerformed

    private void NMTempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NMTempActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NMTempActionPerformed

    private void BtnTemplate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTemplate1ActionPerformed
        template.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        template.setLocationRelativeTo(internalFrame1);
        template.setVisible(true);
    }//GEN-LAST:event_BtnTemplate1ActionPerformed

    private void BtnTemplate1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnTemplate1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnTemplate1KeyPressed

    private void tbJnsPerawatanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbJnsPerawatanMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbJnsPerawatanMouseClicked

    private void tbJnsPerawatanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbJnsPerawatanKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }

        }
    }//GEN-LAST:event_tbJnsPerawatanKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TKd.getText().trim().equals("")){
            Valid.textKosong(TKd,"Kode Periksa");
        }else if(TNm.getText().trim().equals("")){
            Valid.textKosong(TNm,"Nama Pemeriksaan");
            //        }else if(kdpnj.getText().trim().equals("")||nmpnj.getText().trim().equals("")){
            //            Valid.textKosong(kdpnj,"Jenis Bayar");
        }else if(BagianRs.getText().trim().equals("")){
            Valid.textKosong(BagianRs,"J.S.Rumah Sakit");
        }else if(Bhp.getText().trim().equals("")){
            Valid.textKosong(Bhp,"BHP");
        }else if(JMDokter.getText().trim().equals("")){
            Valid.textKosong(JMDokter,"J.M. Dokter");
        }else if(JMLaborat.getText().trim().equals("")){
            Valid.textKosong(JMLaborat,"J.M. Petugas");
        }else if(JMPerujuk.getText().trim().equals("")){
            Valid.textKosong(JMPerujuk,"J.M. Perujuk");
        }else if(KSO.getText().trim().equals("")){
            Valid.textKosong(KSO,"K.S.O");
        }else if(Menejemen.getText().trim().equals("")){
            Valid.textKosong(Menejemen,"Menejemen");
        }else{
            if(Sequel.menyimpantf("jns_perawatan_lab","?,?,?,?,?,?,?,?,?,?,?,?","Kode Periksa",12,new String[]{
                TKd.getText(),TNm.getText(),BagianRs.getText(),Bhp.getText(),JMPerujuk.getText(),JMDokter.getText(),
                JMLaborat.getText(),KSO.getText(),Menejemen.getText(),TotalBiaya.getText(),"1"
            })==true){
                BtnCariActionPerformed(evt);
                emptTeks();
            }
        }
    }//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
            Valid.pindah(evt,Kelas,BtnBatal);
        }
    }//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        ChkInput.setSelected(true);
        isForm();
        emptTeks();
    }//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            emptTeks();
        }else{Valid.pindah(evt, BtnSimpan, BtnHapus);}
    }//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        for(i=0;i<tbJnsPerawatan.getRowCount();i++){
            if(tbJnsPerawatan.getValueAt(i,0).toString().equals("true")){
                Sequel.mengedit("jns_perawatan_lab","kd_jenis_prw='"+tbJnsPerawatan.getValueAt(i,1).toString()+"'","status='0'");
            }
        }
        BtnCariActionPerformed(evt);
        emptTeks();
    }//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBatal, BtnEdit);
        }
    }//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        if(TKd.getText().trim().equals("")){
            Valid.textKosong(TKd,"Kode Periksa");
        }else if(TNm.getText().trim().equals("")){
            Valid.textKosong(TNm,"Nama Pemeriksaan");
            //        }else if(kdpnj.getText().trim().equals("")||nmpnj.getText().trim().equals("")){
            //            Valid.textKosong(kdpnj,"Jenis Bayar");
        }else if(BagianRs.getText().trim().equals("")){
            Valid.textKosong(BagianRs,"J.S.Rumah Sakit");
        }else if(JMDokter.getText().trim().equals("")){
            Valid.textKosong(JMDokter,"J.M. Dokter");
        }else if(JMLaborat.getText().trim().equals("")){
            Valid.textKosong(JMLaborat,"J.M. Petugas");
        }else if(JMPerujuk.getText().trim().equals("")){
            Valid.textKosong(JMPerujuk,"J.M. Perujuk");
        }else if(KSO.getText().trim().equals("")){
            Valid.textKosong(KSO,"K.S.O");
        }else if(Menejemen.getText().trim().equals("")){
            Valid.textKosong(Menejemen,"Menejemen");
        }else{
            Sequel.mengedit("jns_perawatan_lab","kd_jenis_prw=?","kd_jenis_prw=?,nm_perawatan=?,bagian_rs=?,tarif_tindakan_petugas=?,total_byr=?,kd_pj=?,tarif_tindakan_dokter=?,tarif_perujuk=?,bhp=?,kso=?,menejemen=?",12,new String[]{
                //                TKd.getText(),TNm.getText(),BagianRs.getText(),JMLaborat.getText(),TotalBiaya.getText(),kdpnj.getText(),JMDokter.getText(),JMPerujuk.getText(),Bhp.getText(),
                KSO.getText(),Menejemen.getText(),tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),1).toString()
            });
            if(tabMode.getRowCount()!=0){BtnCariActionPerformed(evt);}
            emptTeks();
        }
    }//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEditActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnPrint);
        }
    }//GEN-LAST:event_BtnEditKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(! TCari.getText().trim().equals("")){
            BtnCariActionPerformed(evt);
        }
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            BtnBatal.requestFocus();
        }else if(tabMode.getRowCount()!=0){
            Map<String, Object> param = new HashMap<>();
            param.put("namars",var.getnamars());
            param.put("alamatrs",var.getalamatrs());
            param.put("kotars",var.getkabupatenrs());
            param.put("propinsirs",var.getpropinsirs());
            param.put("kontakrs",var.getkontakrs());
            param.put("emailrs",var.getemailrs());
            param.put("logo",Sequel.cariGambar("select logo from setting"));
            Valid.MyReport("rptTarifLab.jrxml","report","::[ Data Tarif Laboratorium ]::",
                "select jns_perawatan_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan,jns_perawatan_lab.bagian_rs,"+
                "jns_perawatan_lab.bhp,jns_perawatan_lab.tarif_perujuk,jns_perawatan_lab.tarif_tindakan_dokter,"+
                "jns_perawatan_lab.tarif_tindakan_petugas,jns_perawatan_lab.kso,jns_perawatan_lab.menejemen,"+
                "jns_perawatan_lab.total_byr,penjab.png_jawab "+
                "from jns_perawatan_lab inner join penjab on penjab.kd_pj=jns_perawatan_lab.kd_pj where "+
                " jns_perawatan_lab.status='1' and jns_perawatan_lab.kd_jenis_prw like '%"+TCari.getText().trim()+"%' or  "+
                " jns_perawatan_lab.status='1' and jns_perawatan_lab.nm_perawatan like '%"+TCari.getText().trim()+"%' or "+
                " jns_perawatan_lab.status='1' and penjab.png_jawab like '%"+TCari.getText().trim()+"%' "+
                "order by jns_perawatan_lab.kd_jenis_prw",param);
        }
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnEdit, BtnAll);
        }
    }//GEN-LAST:event_BtnPrintKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampil();
    }//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            tampil();
            TCari.setText("");
        }else{
            Valid.pindah(evt, BtnPrint,BtnKeluar);
        }
    }//GEN-LAST:event_BtnAllKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,BtnAll,TCari);}
    }//GEN-LAST:event_BtnKeluarKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
    }//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        tampil();
    }//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
    }//GEN-LAST:event_BtnCariKeyPressed

    private void TKd1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TKd1KeyPressed
        Valid.pindah(evt,JMLaborat,TNm,TCari);
    }//GEN-LAST:event_TKd1KeyPressed

    private void TNm1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNm1KeyPressed
        Valid.pindah(evt,TKd,BagianRs);
    }//GEN-LAST:event_TNm1KeyPressed

    private void BagianRsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BagianRsKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isjml();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isjml();
            Bhp.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            isjml();
            TNm.requestFocus();
        }
    }//GEN-LAST:event_BagianRsKeyPressed

    private void BhpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BhpKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isjml();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isjml();
            JMDokter.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            isjml();
            BagianRs.requestFocus();
        }
    }//GEN-LAST:event_BhpKeyPressed

    private void JMDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JMDokterKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isjml();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isjml();
            JMLaborat.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            isjml();
            Bhp.requestFocus();
        }
    }//GEN-LAST:event_JMDokterKeyPressed

    private void JMLaboratKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JMLaboratKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isjml();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isjml();
            JMPerujuk.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            isjml();
            JMDokter.requestFocus();
        }
    }//GEN-LAST:event_JMLaboratKeyPressed

    private void JMPerujukKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JMPerujukKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isjml();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isjml();
            KSO.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            isjml();
            JMLaborat.requestFocus();
        }
    }//GEN-LAST:event_JMPerujukKeyPressed

    private void TotalBiayaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TotalBiayaKeyPressed
        //    Valid.pindah(evt,Menejemen,kdpnj);
    }//GEN-LAST:event_TotalBiayaKeyPressed

    private void KSOKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KSOKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isjml();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isjml();
            Menejemen.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            isjml();
            JMPerujuk.requestFocus();
        }
    }//GEN-LAST:event_KSOKeyPressed

    private void MenejemenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MenejemenKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isjml();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isjml();
            TotalBiaya.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            isjml();
            KSO.requestFocus();
        }
    }//GEN-LAST:event_MenejemenKeyPressed

    private void TKd2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TKd2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TKd2KeyPressed

    private void KelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KelasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KelasActionPerformed

    private void KelasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KelasKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KelasKeyPressed

    private void ChkInput1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInput1ActionPerformed
        isForm2();
    }//GEN-LAST:event_ChkInput1ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgJnsPerawatanLab dialog = new DlgJnsPerawatanLab(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.TextBox BagianRs;
    private widget.TextBox Bhp;
    private widget.Button BtnAll;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKategori;
    private widget.Button BtnKeluar;
    private widget.Button BtnKeluar1;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.Button BtnTarif;
    private widget.Button BtnTemplate1;
    private widget.CekBox ChkInput;
    private widget.CekBox ChkInput1;
    private widget.PanelBiasa FormInput;
    private widget.PanelBiasa FormInput1;
    private widget.TextBox IDTemp;
    private widget.TextBox JMDokter;
    private widget.TextBox JMLaborat;
    private widget.TextBox JMPerujuk;
    private widget.TextBox KSO;
    private widget.ComboBox Kelas;
    private widget.Label LCount;
    private widget.TextBox Menejemen;
    private javax.swing.JMenuItem MnRestore;
    private javax.swing.JMenuItem MnRestore1;
    private widget.TextBox NMTemp;
    private javax.swing.JPanel PanelInput;
    private javax.swing.JPanel PanelInput1;
    private javax.swing.JPopupMenu Popup;
    private javax.swing.JPopupMenu Popup2;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll1;
    private widget.ScrollPane Scroll2;
    private widget.TextBox TCari;
    private widget.TextBox TKd;
    private widget.TextBox TKd1;
    private widget.TextBox TKd2;
    private widget.TextBox TNm;
    private widget.TextBox TNm1;
    private widget.TextBox TotalBiaya;
    private widget.InternalFrame internalFrame1;
    private javax.swing.JLabel jLabel1;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private javax.swing.JLabel jLabel2;
    private widget.Label jLabel20;
    private widget.Label jLabel3;
    private widget.Label jLabel4;
    private widget.Label jLabel5;
    private widget.Label jLabel6;
    private javax.swing.JLabel jLabel7;
    private widget.Label jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private widget.Label lblCount;
    private widget.Label lblCount1;
    private widget.panelisi panelGlass10;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.panelisi panelisi1;
    private widget.panelisi panelisi2;
    private javax.swing.JMenuItem ppDetTemplate;
    private javax.swing.JMenuItem ppTemplate;
    private widget.TabPane tabPane;
    private widget.Table tbJnsPerawatan;
    private widget.Table tblNamaPeriksa;
    private widget.Table tblTemplate;
    private widget.TextBox txtCari;
    private widget.TextBox txtCari1;
    // End of variables declaration//GEN-END:variables

    private void tampil() {
            Valid.tabelKosong(tabMode);
        try
        {
            ps = koneksi.prepareStatement("select kd_jenis_prw, nm_perawatan "
                    + " from jns_perawatan_lab where kd_jenis_prw like ? OR nm_perawatan like ? order by kd_jenis_prw");
            try
            {
                ps.setString(1, "%" + txtCari.getText().trim() + "%");
                ps.setString(2, "%" + txtCari.getText().trim() + "%");
                
                rs = ps.executeQuery();
                
                while (rs.next())
                {
                    tabMode.addRow(new Object[]
                    {
                        rs.getString(1), rs.getString(2)
                    });
                }
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
            finally
            {
                if (ps != null)
                {
                    ps.close();
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Notifikasi : " + e);
        }
        
        lblCount.setText("" + tabMode.getRowCount());
   
    }
    
    private void tampilTemplate() {
            Valid.tabelKosong(tabMode2);
        try
        {
            ps = koneksi.prepareStatement("select id_template, pemeriksaan,satuan,nilai_rujukan_ld,nilai_rujukan_la,nilai_rujukan_pd,nilai_rujukan_pa,urut"
                    + " from template_laboratorium where id_template like ? OR pemeriksaan like ? order by id_template");
            try
            {
                ps.setString(1, "%" + txtCari.getText().trim() + "%");
                ps.setString(2, "%" + txtCari.getText().trim() + "%");
       
                
                rs = ps.executeQuery();
                
                while (rs.next())
                {
                    tabMode2.addRow(new Object[]
                    {
                        rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)
                    });
                }
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
            finally
            {
                if (ps != null)
                {
                    ps.close();
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Notifikasi : " + e);
        }
        
        lblCount1.setText("" + tabMode2.getRowCount());
   
    }

    private void tampilTarif() {
            Valid.tabelKosong(tabMode3);
        try
        {
            ps = koneksi.prepareStatement("select id_template, pemeriksaan,satuan,nilai_rujukan_ld,nilai_rujukan_la,nilai_rujukan_pd,nilai_rujukan_pa,urut"
                    + " from template_laboratorium where id_template like ? OR pemeriksaan like ? order by id_template");
            try
            {
                ps.setString(1, "%" + txtCari.getText().trim() + "%");
                ps.setString(2, "%" + txtCari.getText().trim() + "%");
       
                
                rs = ps.executeQuery();
                
                while (rs.next())
                {
                    tabMode3.addRow(new Object[]
                    {
                        rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)
                    });
                }
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
            finally
            {
                if (ps != null)
                {
                    ps.close();
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Notifikasi : " + e);
        }
        
    //    lblCount1.setText("" + tabMode2.getRowCount());
   
    }
    public void emptTeks() {
        TKd.setText("");
        TNm.setText("");
        txtCari.setText("");
        //kdpnj.setText("");
        //nmpnj.setText("");
        //Valid.autoNomer(" jns_perawatan_lab ","JP",6,TKd);
        Valid.autoNomer3("select ifnull(MAX(CONVERT(RIGHT(kd_jenis_prw,3),signed)),0) from jns_perawatan_lab","",2,TKd);
        TKd.requestFocus();
    }

    private void getData() {
        if(tblNamaPeriksa.getSelectedRow()!= -1){
            TKd.setText(tblNamaPeriksa.getValueAt(tblNamaPeriksa.getSelectedRow(),0).toString());
            TNm.setText(tblNamaPeriksa.getValueAt(tblNamaPeriksa.getSelectedRow(),1).toString());
//            Sequel.cariIsi("select kd_pj from jns_perawatan_lab where kd_jenis_prw=?", kdpnj,tblNamaPeriksa.getValueAt(tblNamaPeriksa.getSelectedRow(),0).toString());
//            nmpnj.setText(tblNamaPeriksa.getValueAt(tblNamaPeriksa.getSelectedRow(),1).toString());
        }
    }
    private void getTemp() {
        if(tblTemplate.getSelectedRow()!= -1){
            IDTemp.setText(tblTemplate.getValueAt(tblTemplate.getSelectedRow(),0).toString());
            NMTemp.setText(tblTemplate.getValueAt(tblTemplate.getSelectedRow(),1).toString());
//            Sequel.cariIsi("select kd_pj from jns_perawatan_lab where kd_jenis_prw=?", kdpnj,tblNamaPeriksa.getValueAt(tblNamaPeriksa.getSelectedRow(),0).toString());
//            nmpnj.setText(tblNamaPeriksa.getValueAt(tblNamaPeriksa.getSelectedRow(),1).toString());
        }
    }
    
    public JTextField getTextField(){
        return TKd;
    }
    private void isjml(){
        if((! BagianRs.getText().equals(""))&&(! JMLaborat.getText().equals(""))
                &&(! JMDokter.getText().equals(""))&&(! JMPerujuk.getText().equals(""))
                &&(! Bhp.getText().equals(""))&&(! KSO.getText().equals(""))
                &&(! Menejemen.getText().equals(""))){
            TotalBiaya.setText(Valid.SetAngka2(
                    Double.parseDouble(BagianRs.getText().trim())+
                    Double.parseDouble(JMDokter.getText().trim())+
                    Double.parseDouble(JMLaborat.getText().trim())+
                    Double.parseDouble(JMPerujuk.getText().trim())+
                    Double.parseDouble(Bhp.getText().trim())+
                    Double.parseDouble(KSO.getText().trim())+
                    Double.parseDouble(Menejemen.getText().trim())
            ));
        }
    }   
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,150));
            FormInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            FormInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    private void isForm2(){
        if(ChkInput1.isSelected()==true){
            ChkInput1.setVisible(false);
            PanelInput1.setPreferredSize(new Dimension(WIDTH,220));
            FormInput1.setVisible(true);      
            ChkInput1.setVisible(true);
        }else if(ChkInput1.isSelected()==false){           
            ChkInput1.setVisible(false);            
            PanelInput1.setPreferredSize(new Dimension(WIDTH,20));
            FormInput1.setVisible(false);      
            ChkInput1.setVisible(true);
        }
    }
    public void isCek(){

    }
    
    public JTable getTable(){
        return tblNamaPeriksa;
    }

    
}
