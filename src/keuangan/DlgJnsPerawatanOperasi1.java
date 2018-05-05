/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgJnsPerawatan.java
 *
 * Created on May 22, 2010, 11:58:21 PM
 */

package keuangan;
import fungsi.GConvert;
import fungsi.GQuery;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.var;
import interfaces.TextChangedListener;
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
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import restore.DlgRestoreTarifOperasi;
import simrskhanza.DlgPenanggungJawab;
import util.GMessage;
import widget.TextBox;

/**
 *
 * @author dosen
 */
public final class DlgJnsPerawatanOperasi1 extends javax.swing.JDialog {
    private final DefaultTableModel tabModePerawatan;
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private Connection koneksi=koneksiDB.condb();
    private DecimalFormat df2 = new DecimalFormat("####");
    private double operator = 0, drAnestesi = 0, penataAnestesi = 0, instrumentator = 0, asistenOperator = 0,
            linen = 0, onLoop = 0, mat = 0;
    private PreparedStatement pstampil;
    private ResultSet rs;
    private int i=0;
    public DlgPenanggungJawab dlgPenanggungJawab = new DlgPenanggungJawab(null,false);

    /** Creates new form DlgJnsPerawatan
     * @param parent
     * @param modal */
    public DlgJnsPerawatanOperasi1(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setLocation(8,1);
        setSize(628,674);
        
        Object[] row = {
            "P", "Kode Paket", "Jenis", "Kelas", "Nama Paket", "Tarif", "Operator", "Dokter Anestesi", 
            "Penata Anestesi", "Instrumentator", "Asisten Operator", "Linen", "On Loop", "Mat", "Kd Pj"
        };
        
        tabModePerawatan = new DefaultTableModel(null, row)
        {
             @Override public boolean isCellEditable(int rowIndex, int colIndex)
             {
                boolean a = false;
                if (colIndex == 0) 
                {
                    a=true;
                }
                return a;
             }
             
             Class[] types = new Class[] 
             {
                 java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, 
                 java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, 
                 java.lang.Integer.class, java.lang.Integer.class
             };
             
             @Override
             public Class getColumnClass(int columnIndex) 
             {
                return types [columnIndex];
             }
        };
        initTbJnsPerawatan(row);
        
        txtKodePaket.setDocument(new batasInput((byte)15).getKata(txtKodePaket));
        txtOperator.setDocument(new batasInput((byte)8).getOnlyAngka(txtOperator));
        txtDrAnestesi.setDocument(new batasInput((byte)8).getOnlyAngka(txtDrAnestesi));
        txtPenataAnestesi.setDocument(new batasInput((byte)8).getOnlyAngka(txtPenataAnestesi));
        txtInstrumentator.setDocument(new batasInput((byte)8).getOnlyAngka(txtInstrumentator));
        txtAsistenOperator.setDocument(new batasInput((byte)8).getOnlyAngka(txtAsistenOperator));
        txtLinen.setDocument(new batasInput((byte)8).getOnlyAngka(txtLinen));
        txtOnLoop.setDocument(new batasInput((byte)8).getOnlyAngka(txtOnLoop));
        txtMat.setDocument(new batasInput((byte)8).getOnlyAngka(txtMat));
        
        txtOperator.addTextChangedListener(t);
        txtDrAnestesi.addTextChangedListener(t);
        txtPenataAnestesi.addTextChangedListener(t);
        txtInstrumentator.addTextChangedListener(t);
        txtAsistenOperator.addTextChangedListener(t);
        txtLinen.addTextChangedListener(t);
        txtOnLoop.addTextChangedListener(t);
        txtMat.addTextChangedListener(t);
        
        TCari.setDocument(new batasInput((byte)100).getKata(TCari));
        if(koneksiDB.cariCepat().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {tampil();}
                @Override
                public void removeUpdate(DocumentEvent e) {tampil();}
                @Override
                public void changedUpdate(DocumentEvent e) {tampil();}
            });
        }  
        
        dlgPenanggungJawab.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dlgPenanggungJawab.getTable().getSelectedRow()!= -1){
                    txtKdPenanggungJawab.setText(dlgPenanggungJawab.getTable().getValueAt(dlgPenanggungJawab.getTable().getSelectedRow(),1).toString());
                    txtNamaPenanggungJawab.setText(dlgPenanggungJawab.getTable().getValueAt(dlgPenanggungJawab.getTable().getSelectedRow(),2).toString());
                } 
                txtKdPenanggungJawab.requestFocus();
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        dlgPenanggungJawab.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    dlgPenanggungJawab.dispose();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }
    
    private void initTbJnsPerawatan(Object[] row)
    {
        tbJnsPerawatan.setModel(tabModePerawatan);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbJnsPerawatan.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbJnsPerawatan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < row.length; i++) {
            TableColumn column = tbJnsPerawatan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(40);
            }else if(i==2){
                column.setPreferredWidth(120);
            }else if(i==3){
                column.setPreferredWidth(100);
            }else if(i==4){
                column.setPreferredWidth(200);
            }else if(i==row.length-1){
                column.setPreferredWidth(50);
            }else{
                column.setPreferredWidth(85);
            }
        }
        tbJnsPerawatan.setDefaultRenderer(Object.class, new WarnaTable());
        tbJnsPerawatan.getSelectionModel().addListSelectionListener(e -> setDataYangTampil());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        MnRestore = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        TabRawat = new javax.swing.JTabbedPane();
        internalFrame2 = new widget.InternalFrame();
        Scroll1 = new widget.ScrollPane();
        FormInput = new widget.PanelBiasa();
        jLabel3 = new widget.Label();
        txtKodePaket = new widget.TextBox();
        txtOperator = new widget.TextBox();
        jLabel9 = new widget.Label();
        txtAsistenOperator = new widget.TextBox();
        jLabel12 = new widget.Label();
        txtInstrumentator = new widget.TextBox();
        jLabel14 = new widget.Label();
        jLabel15 = new widget.Label();
        txtMat = new widget.TextBox();
        jLabel17 = new widget.Label();
        txtDrAnestesi = new widget.TextBox();
        jLabel18 = new widget.Label();
        txtPenataAnestesi = new widget.TextBox();
        jLabel19 = new widget.Label();
        txtLinen = new widget.TextBox();
        jLabel4 = new widget.Label();
        cbKelas = new widget.ComboBox();
        jLabel34 = new widget.Label();
        txtKdPenanggungJawab = new widget.TextBox();
        txtNamaPenanggungJawab = new widget.TextBox();
        btnPenanggungJawab = new widget.Button();
        jLabel35 = new widget.Label();
        txtOnLoop = new widget.TextBox();
        txtTarif = new widget.TextBox();
        jLabel5 = new widget.Label();
        txtNamaPaket = new widget.TextBox();
        jLabel8 = new widget.Label();
        cbJenis = new widget.ComboBox();
        jLabel10 = new widget.Label();
        Scroll4 = new widget.ScrollPane();
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
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        internalFrame5 = new widget.InternalFrame();
        Scroll2 = new widget.ScrollPane();
        FormInput1 = new widget.PanelBiasa();
        jLabel11 = new widget.Label();
        jLabel36 = new widget.Label();
        txtKdKategori = new widget.TextBox();
        txtNamaKategori = new widget.TextBox();
        btnKategori = new widget.Button();
        Scroll5 = new widget.ScrollPane();
        tbKategori = new widget.Table();
        txtKdGroup = new widget.TextBox();
        txtNamaGroup = new widget.TextBox();
        btnGroup = new widget.Button();
        jPanel4 = new javax.swing.JPanel();
        panelGlass10 = new widget.panelisi();
        BtnSimpanK = new widget.Button();
        BtnBatalK = new widget.Button();
        BtnHapusK = new widget.Button();
        BtnEditK = new widget.Button();
        BtnAllK = new widget.Button();
        BtnKeluarK = new widget.Button();
        panelGlass11 = new widget.panelisi();
        jLabel29 = new widget.Label();
        TCariK = new widget.TextBox();
        BtnCariK = new widget.Button();
        jLabel30 = new widget.Label();
        LCountK = new widget.Label();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnRestore.setBackground(new java.awt.Color(255, 255, 255));
        MnRestore.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnRestore.setForeground(new java.awt.Color(60, 80, 50));
        MnRestore.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnRestore.setText("Data Sampah");
        MnRestore.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnRestore.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnRestore.setName("MnRestore"); // NOI18N
        MnRestore.setPreferredSize(new java.awt.Dimension(200, 28));
        MnRestore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnRestoreActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnRestore);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data Paket Tindakan Operasi/VK ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        TabRawat.setBackground(new java.awt.Color(250, 255, 245));
        TabRawat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(230, 235, 225)));
        TabRawat.setForeground(new java.awt.Color(50, 70, 40));
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        internalFrame2.setBorder(null);
        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll1.setName("Scroll1"); // NOI18N
        Scroll1.setOpaque(true);
        Scroll1.setPreferredSize(new java.awt.Dimension(102, 400));

        FormInput.setBorder(null);
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 257));
        FormInput.setLayout(null);

        jLabel3.setText("Kode Paket :");
        jLabel3.setName("jLabel3"); // NOI18N
        FormInput.add(jLabel3);
        jLabel3.setBounds(0, 10, 96, 23);

        txtKodePaket.setHighlighter(null);
        txtKodePaket.setName("txtKodePaket"); // NOI18N
        txtKodePaket.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKodePaketKeyPressed(evt);
            }
        });
        FormInput.add(txtKodePaket);
        txtKodePaket.setBounds(100, 12, 230, 23);

        txtOperator.setText("0");
        txtOperator.setHighlighter(null);
        txtOperator.setName("txtOperator"); // NOI18N
        txtOperator.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOperatorKeyPressed(evt);
            }
        });
        FormInput.add(txtOperator);
        txtOperator.setBounds(470, 10, 180, 23);

        jLabel9.setText("Operator : Rp");
        jLabel9.setName("jLabel9"); // NOI18N
        FormInput.add(jLabel9);
        jLabel9.setBounds(370, 10, 96, 23);

        txtAsistenOperator.setText("0");
        txtAsistenOperator.setHighlighter(null);
        txtAsistenOperator.setName("txtAsistenOperator"); // NOI18N
        txtAsistenOperator.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAsistenOperatorKeyPressed(evt);
            }
        });
        FormInput.add(txtAsistenOperator);
        txtAsistenOperator.setBounds(820, 10, 160, 23);

        jLabel12.setText("Asisten Operator : Rp");
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput.add(jLabel12);
        jLabel12.setBounds(670, 10, 140, 23);

        txtInstrumentator.setText("0");
        txtInstrumentator.setHighlighter(null);
        txtInstrumentator.setName("txtInstrumentator"); // NOI18N
        txtInstrumentator.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInstrumentatorKeyPressed(evt);
            }
        });
        FormInput.add(txtInstrumentator);
        txtInstrumentator.setBounds(470, 100, 180, 23);

        jLabel14.setText("Instrumentator : Rp");
        jLabel14.setName("jLabel14"); // NOI18N
        FormInput.add(jLabel14);
        jLabel14.setBounds(346, 100, 120, 23);

        jLabel15.setText("Mat : Rp");
        jLabel15.setName("jLabel15"); // NOI18N
        FormInput.add(jLabel15);
        jLabel15.setBounds(670, 100, 140, 23);

        txtMat.setText("0");
        txtMat.setHighlighter(null);
        txtMat.setName("txtMat"); // NOI18N
        txtMat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMatKeyPressed(evt);
            }
        });
        FormInput.add(txtMat);
        txtMat.setBounds(820, 100, 160, 23);

        jLabel17.setText("Dokter Anestesi : Rp");
        jLabel17.setName("jLabel17"); // NOI18N
        FormInput.add(jLabel17);
        jLabel17.setBounds(346, 40, 120, 23);

        txtDrAnestesi.setText("0");
        txtDrAnestesi.setHighlighter(null);
        txtDrAnestesi.setName("txtDrAnestesi"); // NOI18N
        txtDrAnestesi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDrAnestesiKeyPressed(evt);
            }
        });
        FormInput.add(txtDrAnestesi);
        txtDrAnestesi.setBounds(470, 40, 180, 23);

        jLabel18.setText("Penata Anestesi : Rp");
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput.add(jLabel18);
        jLabel18.setBounds(346, 70, 120, 23);

        txtPenataAnestesi.setText("0");
        txtPenataAnestesi.setHighlighter(null);
        txtPenataAnestesi.setName("txtPenataAnestesi"); // NOI18N
        txtPenataAnestesi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPenataAnestesiKeyPressed(evt);
            }
        });
        FormInput.add(txtPenataAnestesi);
        txtPenataAnestesi.setBounds(470, 70, 180, 23);

        jLabel19.setText("Linen : Rp");
        jLabel19.setName("jLabel19"); // NOI18N
        FormInput.add(jLabel19);
        jLabel19.setBounds(670, 40, 140, 23);

        txtLinen.setText("0");
        txtLinen.setHighlighter(null);
        txtLinen.setName("txtLinen"); // NOI18N
        txtLinen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLinenKeyPressed(evt);
            }
        });
        FormInput.add(txtLinen);
        txtLinen.setBounds(820, 40, 160, 23);

        jLabel4.setText("Kelas :");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput.add(jLabel4);
        jLabel4.setBounds(0, 70, 96, 23);

        cbKelas.setForeground(new java.awt.Color(153, 0, 51));
        cbKelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "Kelas VIP", "Kelas I", "Kelas II", "Kelas III" }));
        cbKelas.setName("cbKelas"); // NOI18N
        cbKelas.setOpaque(false);
        cbKelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbKelasActionPerformed(evt);
            }
        });
        cbKelas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbKelasKeyPressed(evt);
            }
        });
        FormInput.add(cbKelas);
        cbKelas.setBounds(100, 70, 230, 23);

        jLabel34.setText("Bayar : ");
        jLabel34.setName("jLabel34"); // NOI18N
        FormInput.add(jLabel34);
        jLabel34.setBounds(20, 130, 80, 23);

        txtKdPenanggungJawab.setToolTipText("Tekan ENTER untuk lanjut ke field berikutnya, tekan PAGE UP untuk ke field sebelumnya, Tekan UP untuk menampilkan data Jenis Pembayaran");
        txtKdPenanggungJawab.setHighlighter(null);
        txtKdPenanggungJawab.setName("txtKdPenanggungJawab"); // NOI18N
        txtKdPenanggungJawab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKdPenanggungJawabKeyPressed(evt);
            }
        });
        FormInput.add(txtKdPenanggungJawab);
        txtKdPenanggungJawab.setBounds(100, 130, 60, 23);

        txtNamaPenanggungJawab.setEditable(false);
        txtNamaPenanggungJawab.setName("txtNamaPenanggungJawab"); // NOI18N
        FormInput.add(txtNamaPenanggungJawab);
        txtNamaPenanggungJawab.setBounds(160, 130, 130, 23);

        btnPenanggungJawab.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPenanggungJawab.setMnemonic('2');
        btnPenanggungJawab.setToolTipText("ALt+2");
        btnPenanggungJawab.setName("btnPenanggungJawab"); // NOI18N
        btnPenanggungJawab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPenanggungJawabActionPerformed(evt);
            }
        });
        FormInput.add(btnPenanggungJawab);
        btnPenanggungJawab.setBounds(300, 130, 28, 23);

        jLabel35.setText("On Loop : Rp");
        jLabel35.setName("jLabel35"); // NOI18N
        FormInput.add(jLabel35);
        jLabel35.setBounds(670, 70, 140, 23);

        txtOnLoop.setText("0");
        txtOnLoop.setHighlighter(null);
        txtOnLoop.setName("txtOnLoop"); // NOI18N
        txtOnLoop.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOnLoopKeyPressed(evt);
            }
        });
        FormInput.add(txtOnLoop);
        txtOnLoop.setBounds(820, 70, 160, 23);

        txtTarif.setEditable(false);
        txtTarif.setText("0");
        txtTarif.setHighlighter(null);
        txtTarif.setName("txtTarif"); // NOI18N
        txtTarif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTarifKeyPressed(evt);
            }
        });
        FormInput.add(txtTarif);
        txtTarif.setBounds(720, 130, 260, 23);

        jLabel5.setText("Tarif : Rp ");
        jLabel5.setName("jLabel5"); // NOI18N
        FormInput.add(jLabel5);
        jLabel5.setBounds(620, 130, 96, 23);

        txtNamaPaket.setEditable(false);
        txtNamaPaket.setFocusable(false);
        txtNamaPaket.setHighlighter(null);
        txtNamaPaket.setName("txtNamaPaket"); // NOI18N
        txtNamaPaket.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNamaPaketKeyPressed(evt);
            }
        });
        FormInput.add(txtNamaPaket);
        txtNamaPaket.setBounds(100, 100, 230, 23);

        jLabel8.setText("Nama Paket :");
        jLabel8.setName("jLabel8"); // NOI18N
        FormInput.add(jLabel8);
        jLabel8.setBounds(0, 100, 96, 23);

        cbJenis.setForeground(new java.awt.Color(153, 0, 51));
        cbJenis.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "Operasi Kecil", "Operasi Sedang", "Operasi Besar", "Operasi Khusus I", "Operasi Khusus II", "Operasi Canggih" }));
        cbJenis.setName("cbJenis"); // NOI18N
        cbJenis.setOpaque(false);
        cbJenis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbJenisActionPerformed(evt);
            }
        });
        cbJenis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbJenisKeyPressed(evt);
            }
        });
        FormInput.add(cbJenis);
        cbJenis.setBounds(100, 40, 230, 23);

        jLabel10.setText("Jenis :");
        jLabel10.setName("jLabel10"); // NOI18N
        FormInput.add(jLabel10);
        jLabel10.setBounds(0, 40, 96, 23);

        Scroll4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(245, 255, 235)));
        Scroll4.setName("Scroll4"); // NOI18N
        Scroll4.setOpaque(true);

        tbJnsPerawatan.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbJnsPerawatan.setName("tbJnsPerawatan"); // NOI18N
        tbJnsPerawatan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbJnsPerawatanMouseClicked(evt);
            }
        });
        tbJnsPerawatan.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tbJnsPerawatanPropertyChange(evt);
            }
        });
        tbJnsPerawatan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbJnsPerawatanKeyPressed(evt);
            }
        });
        Scroll4.setViewportView(tbJnsPerawatan);

        FormInput.add(Scroll4);
        Scroll4.setBounds(0, 182, 990, 220);

        Scroll1.setViewportView(FormInput);

        internalFrame2.add(Scroll1, java.awt.BorderLayout.CENTER);

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

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(75, 23));
        panelGlass9.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass9.add(LCount);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        internalFrame2.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        TabRawat.addTab(".: Input Paket  ", internalFrame2);

        internalFrame5.setBorder(null);
        internalFrame5.setName("internalFrame5"); // NOI18N
        internalFrame5.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll2.setName("Scroll2"); // NOI18N
        Scroll2.setOpaque(true);
        Scroll2.setPreferredSize(new java.awt.Dimension(102, 400));

        FormInput1.setBorder(null);
        FormInput1.setName("FormInput1"); // NOI18N
        FormInput1.setPreferredSize(new java.awt.Dimension(100, 257));
        FormInput1.setLayout(null);

        jLabel11.setText("Group :");
        jLabel11.setName("jLabel11"); // NOI18N
        FormInput1.add(jLabel11);
        jLabel11.setBounds(20, 10, 96, 23);

        jLabel36.setText("Kategori : ");
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput1.add(jLabel36);
        jLabel36.setBounds(400, 10, 80, 23);

        txtKdKategori.setToolTipText("Tekan ENTER untuk lanjut ke field berikutnya, tekan PAGE UP untuk ke field sebelumnya, Tekan UP untuk menampilkan data Jenis Pembayaran");
        txtKdKategori.setHighlighter(null);
        txtKdKategori.setName("txtKdKategori"); // NOI18N
        txtKdKategori.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKdKategoriKeyPressed(evt);
            }
        });
        FormInput1.add(txtKdKategori);
        txtKdKategori.setBounds(490, 10, 60, 23);

        txtNamaKategori.setEditable(false);
        txtNamaKategori.setName("txtNamaKategori"); // NOI18N
        FormInput1.add(txtNamaKategori);
        txtNamaKategori.setBounds(550, 10, 130, 23);

        btnKategori.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnKategori.setMnemonic('2');
        btnKategori.setToolTipText("ALt+2");
        btnKategori.setName("btnKategori"); // NOI18N
        btnKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKategoriActionPerformed(evt);
            }
        });
        FormInput1.add(btnKategori);
        btnKategori.setBounds(690, 10, 28, 23);

        Scroll5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(245, 255, 235)));
        Scroll5.setName("Scroll5"); // NOI18N
        Scroll5.setOpaque(true);

        tbKategori.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbKategori.setName("tbKategori"); // NOI18N
        tbKategori.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbKategoriMouseClicked(evt);
            }
        });
        tbKategori.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tbKategoriPropertyChange(evt);
            }
        });
        tbKategori.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbKategoriKeyPressed(evt);
            }
        });
        Scroll5.setViewportView(tbKategori);

        FormInput1.add(Scroll5);
        Scroll5.setBounds(0, 52, 990, 350);

        txtKdGroup.setToolTipText("Tekan ENTER untuk lanjut ke field berikutnya, tekan PAGE UP untuk ke field sebelumnya, Tekan UP untuk menampilkan data Jenis Pembayaran");
        txtKdGroup.setHighlighter(null);
        txtKdGroup.setName("txtKdGroup"); // NOI18N
        txtKdGroup.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKdGroupKeyPressed(evt);
            }
        });
        FormInput1.add(txtKdGroup);
        txtKdGroup.setBounds(130, 10, 60, 23);

        txtNamaGroup.setEditable(false);
        txtNamaGroup.setName("txtNamaGroup"); // NOI18N
        FormInput1.add(txtNamaGroup);
        txtNamaGroup.setBounds(190, 10, 130, 23);

        btnGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnGroup.setMnemonic('2');
        btnGroup.setToolTipText("ALt+2");
        btnGroup.setName("btnGroup"); // NOI18N
        btnGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGroupActionPerformed(evt);
            }
        });
        FormInput1.add(btnGroup);
        btnGroup.setBounds(330, 10, 28, 23);

        Scroll2.setViewportView(FormInput1);

        internalFrame5.add(Scroll2, java.awt.BorderLayout.CENTER);

        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.setOpaque(false);
        jPanel4.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel4.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass10.setName("panelGlass10"); // NOI18N
        panelGlass10.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnSimpanK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpanK.setMnemonic('S');
        BtnSimpanK.setText("Simpan");
        BtnSimpanK.setToolTipText("Alt+S");
        BtnSimpanK.setName("BtnSimpanK"); // NOI18N
        BtnSimpanK.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpanK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanKActionPerformed(evt);
            }
        });
        BtnSimpanK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpanKKeyPressed(evt);
            }
        });
        panelGlass10.add(BtnSimpanK);

        BtnBatalK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        BtnBatalK.setMnemonic('B');
        BtnBatalK.setText("Baru");
        BtnBatalK.setToolTipText("Alt+B");
        BtnBatalK.setName("BtnBatalK"); // NOI18N
        BtnBatalK.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnBatalK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBatalKActionPerformed(evt);
            }
        });
        BtnBatalK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnBatalKKeyPressed(evt);
            }
        });
        panelGlass10.add(BtnBatalK);

        BtnHapusK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapusK.setMnemonic('H');
        BtnHapusK.setText("Hapus");
        BtnHapusK.setToolTipText("Alt+H");
        BtnHapusK.setName("BtnHapusK"); // NOI18N
        BtnHapusK.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapusK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusKActionPerformed(evt);
            }
        });
        BtnHapusK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapusKKeyPressed(evt);
            }
        });
        panelGlass10.add(BtnHapusK);

        BtnEditK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnEditK.setMnemonic('G');
        BtnEditK.setText("Ganti");
        BtnEditK.setToolTipText("Alt+G");
        BtnEditK.setName("BtnEditK"); // NOI18N
        BtnEditK.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnEditK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditKActionPerformed(evt);
            }
        });
        BtnEditK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnEditKKeyPressed(evt);
            }
        });
        panelGlass10.add(BtnEditK);

        BtnAllK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAllK.setMnemonic('M');
        BtnAllK.setText("Semua");
        BtnAllK.setToolTipText("Alt+M");
        BtnAllK.setName("BtnAllK"); // NOI18N
        BtnAllK.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnAllK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllKActionPerformed(evt);
            }
        });
        BtnAllK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKKeyPressed(evt);
            }
        });
        panelGlass10.add(BtnAllK);

        BtnKeluarK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluarK.setMnemonic('K');
        BtnKeluarK.setText("Keluar");
        BtnKeluarK.setToolTipText("Alt+K");
        BtnKeluarK.setName("BtnKeluarK"); // NOI18N
        BtnKeluarK.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluarK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarKActionPerformed(evt);
            }
        });
        BtnKeluarK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKKeyPressed(evt);
            }
        });
        panelGlass10.add(BtnKeluarK);

        jPanel4.add(panelGlass10, java.awt.BorderLayout.CENTER);

        panelGlass11.setName("panelGlass11"); // NOI18N
        panelGlass11.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel29.setText("Key Word :");
        jLabel29.setName("jLabel29"); // NOI18N
        jLabel29.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass11.add(jLabel29);

        TCariK.setName("TCariK"); // NOI18N
        TCariK.setPreferredSize(new java.awt.Dimension(450, 23));
        TCariK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKKeyPressed(evt);
            }
        });
        panelGlass11.add(TCariK);

        BtnCariK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCariK.setMnemonic('2');
        BtnCariK.setToolTipText("Alt+2");
        BtnCariK.setName("BtnCariK"); // NOI18N
        BtnCariK.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCariK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariKActionPerformed(evt);
            }
        });
        BtnCariK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKKeyPressed(evt);
            }
        });
        panelGlass11.add(BtnCariK);

        jLabel30.setText("Record :");
        jLabel30.setName("jLabel30"); // NOI18N
        jLabel30.setPreferredSize(new java.awt.Dimension(75, 23));
        panelGlass11.add(jLabel30);

        LCountK.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCountK.setText("0");
        LCountK.setName("LCountK"); // NOI18N
        LCountK.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass11.add(LCountK);

        jPanel4.add(panelGlass11, java.awt.BorderLayout.PAGE_START);

        internalFrame5.add(jPanel4, java.awt.BorderLayout.PAGE_END);

        TabRawat.addTab(".: Edit Kategori  ", internalFrame5);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(txtKodePaket.getText().trim().equals("")){
            Valid.textKosong(txtKodePaket,"Kode Paket");
        } else if (cbJenis.getSelectedIndex() == 0) {
            Valid.textKosong(txtKodePaket,"Jenis Operasi");
        } else if (cbKelas.getSelectedIndex() == 0) {
            Valid.textKosong(txtKodePaket,"Kelas");
        } else if(txtOperator.getText().trim().equals("")){
            Valid.textKosong(txtOperator,"Operator");
        }else if(txtDrAnestesi.getText().trim().equals("")){
            Valid.textKosong(txtDrAnestesi,"Dokter Anestesi");
        }else if(txtPenataAnestesi.getText().trim().equals("")){
            Valid.textKosong(txtPenataAnestesi,"Penata Anestesi");
        }else if(txtAsistenOperator.getText().trim().equals("")){
            Valid.textKosong(txtAsistenOperator,"Asisten Operator");
        }else if(txtLinen.getText().trim().equals("")){
            Valid.textKosong(txtLinen,"Linen");
        }else if(txtOnLoop.getText().trim().equals("")){
            Valid.textKosong(txtOnLoop,"On Loop");
        }else if(txtInstrumentator.getText().trim().equals("")){
            Valid.textKosong(txtInstrumentator,"Instrumentator");
        }else if(txtMat.getText().trim().equals("")){
            Valid.textKosong(txtMat,"Mat");
        }else if(txtKdPenanggungJawab.getText().trim().equals("")||txtNamaPenanggungJawab.getText().trim().equals("")){
            Valid.textKosong(txtKdPenanggungJawab,"Jenis Bayar");
        }else{
            
            new GQuery()
                    .a("INSERT INTO paket_operasi_2 VALUES (")
                    .a("{kode_paket}, {jenis}, {kelas}, {nama_paket}, {tarif}, {operator}, {dokter_anestesi},")
                    .a("{penata_anestesi}, {instrumentator}, {asisten_operator}, {linen}, {on_loop}, {mat}, {kd_pj})")
                    .set("kode_paket", txtKodePaket.getText())
                    .set("jenis", (String) cbJenis.getSelectedItem())
                    .set("kelas", (String) cbKelas.getSelectedItem())
                    .set("nama_paket", txtNamaPaket.getText())
                    .set("tarif", txtTarif.getText())
                    .set("operator", txtOperator.getText())
                    .set("dokter_anestesi", txtDrAnestesi.getText())
                    .set("penata_anestesi", txtPenataAnestesi.getText())
                    .set("instrumentator", txtInstrumentator.getText())
                    .set("asisten_operator", txtAsistenOperator.getText())
                    .set("linen", txtLinen.getText())
                    .set("on_loop", txtOnLoop.getText())
                    .set("mat", txtMat.getText())
                    .set("kd_pj", txtKdPenanggungJawab.getText())
                    .write();
            tampil();
            emptTeks();
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
//            Valid.pindah(evt,TOmloop5,BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
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
                Sequel.mengedit("paket_operasi","kode_paket='"+tbJnsPerawatan.getValueAt(i,1).toString()+"'","status='0'");
            }
        } 
        tampil();
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
        if(txtKodePaket.getText().trim().equals("")){
            Valid.textKosong(txtKodePaket,"Kode Paket");
        } else if (cbJenis.getSelectedIndex() == 0) {
            Valid.textKosong(txtKodePaket,"Jenis Operasi");
        } else if (cbKelas.getSelectedIndex() == 0) {
            Valid.textKosong(txtKodePaket,"Kelas");
        } else if(txtOperator.getText().trim().equals("")){
            Valid.textKosong(txtOperator,"Operator");
        }else if(txtDrAnestesi.getText().trim().equals("")){
            Valid.textKosong(txtDrAnestesi,"Dokter Anestesi");
        }else if(txtPenataAnestesi.getText().trim().equals("")){
            Valid.textKosong(txtPenataAnestesi,"Penata Anestesi");
        }else if(txtAsistenOperator.getText().trim().equals("")){
            Valid.textKosong(txtAsistenOperator,"Asisten Operator");
        }else if(txtLinen.getText().trim().equals("")){
            Valid.textKosong(txtLinen,"Linen");
        }else if(txtOnLoop.getText().trim().equals("")){
            Valid.textKosong(txtOnLoop,"On Loop");
        }else if(txtInstrumentator.getText().trim().equals("")){
            Valid.textKosong(txtInstrumentator,"Instrumentator");
        }else if(txtMat.getText().trim().equals("")){
            Valid.textKosong(txtMat,"Mat");
        }else if(txtKdPenanggungJawab.getText().trim().equals("")||txtNamaPenanggungJawab.getText().trim().equals("")){
            Valid.textKosong(txtKdPenanggungJawab,"Jenis Bayar");
        }else{
            
            boolean b = new GQuery()
                    .a("UPDATE paket_operasi_2 SET ")
                    .a("jenis = {jenis}, kelas = {kelas}, nama_paket = {nama_paket},")
                    .a("tarif = {tarif}, operator = {operator}, dokter_anestesi = {dokter_anestesi},")
                    .a("penata_anestesi = {penata_anestesi}, instrumentator = {instrumentator},")
                    .a("asisten_operator = {asisten_operator}, linen = {linen}, on_loop = {on_loop},")
                    .a("mat = {mat}, kd_pj = {kd_pj}")
                    .a("WHERE kode_paket = {kode_paket}")
                    .set("kode_paket", txtKodePaket.getText())
                    .set("jenis", (String) cbJenis.getSelectedItem())
                    .set("kelas", (String) cbKelas.getSelectedItem())
                    .set("nama_paket", txtNamaPaket.getText())
                    .set("tarif", txtTarif.getText())
                    .set("operator", txtOperator.getText())
                    .set("dokter_anestesi", txtDrAnestesi.getText())
                    .set("penata_anestesi", txtPenataAnestesi.getText())
                    .set("instrumentator", txtInstrumentator.getText())
                    .set("asisten_operator", txtAsistenOperator.getText())
                    .set("linen", txtLinen.getText())
                    .set("on_loop", txtOnLoop.getText())
                    .set("mat", txtMat.getText())
                    .set("kd_pj", txtKdPenanggungJawab.getText())
                    .write();
            
            if (!b) GMessage.e("Edit Gagal", "Mungkin kode sudah digunakan sebelumnya");
            
            if(tabModePerawatan.getRowCount()!=0){tampil();}
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

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,BtnAll,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(! TCari.getText().trim().equals("")){
            BtnCariActionPerformed(evt);
        }
        if(tabModePerawatan.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            BtnBatal.requestFocus();
        }else if(tabModePerawatan.getRowCount()!=0){            
            Map<String, Object> param = new HashMap<>();    
                param.put("namars",var.getnamars());
                param.put("alamatrs",var.getalamatrs());
                param.put("kotars",var.getkabupatenrs());
                param.put("propinsirs",var.getpropinsirs());
                param.put("kontakrs",var.getkontakrs());
                param.put("emailrs",var.getemailrs());   
                param.put("logo",Sequel.cariGambar("select logo from setting")); 
            Valid.MyReport("rptPaketOperasi.jrxml","report","::[ Data Paket Operasi ]::",
                   "select paket_operasi.kode_paket, paket_operasi.nm_perawatan,(paket_operasi.operator1+paket_operasi.operator2+paket_operasi.operator3+"+
                       "paket_operasi.asisten_operator1+paket_operasi.asisten_operator2+paket_operasi.asisten_operator3+paket_operasi.instrumen+"+
                       "paket_operasi.dokter_anak+paket_operasi.perawaat_resusitas+"+
                       "paket_operasi.alat+paket_operasi.dokter_anestesi+paket_operasi.asisten_anestesi+paket_operasi.asisten_anestesi2+"+
                       "paket_operasi.bidan+paket_operasi.bidan2+paket_operasi.bidan3+paket_operasi.perawat_luar+"+
                       "paket_operasi.sewa_ok+paket_operasi.akomodasi+paket_operasi.bagian_rs+"+
                       "paket_operasi.omloop+paket_operasi.omloop2+paket_operasi.omloop3+paket_operasi.omloop4+paket_operasi.omloop5+"+
                       "paket_operasi.sarpras+paket_operasi.dokter_pjanak+paket_operasi.dokter_umum) as jumlah "+
                   "from paket_operasi inner join penjab on penjab.kd_pj=paket_operasi.kd_pj "+
                   "where paket_operasi.status='1' and paket_operasi.kode_paket like '%"+TCari.getText()+"%' or "+
                   "paket_operasi.status='1' and paket_operasi.nm_perawatan like '%"+TCari.getText()+"%' or "+
                   "paket_operasi.status='1' and penjab.png_jawab like '%"+TCari.getText()+"%' order by paket_operasi.kode_paket ",param);
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

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampil();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            tampil();
            TCari.setText("");
        }else{
            Valid.pindah(evt, BtnPrint, BtnKeluar);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        tampil();
    }//GEN-LAST:event_formWindowOpened

    private void MnRestoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnRestoreActionPerformed
        DlgRestoreTarifOperasi restore=new DlgRestoreTarifOperasi(null,true);
        restore.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        restore.setLocationRelativeTo(internalFrame1);
        restore.setVisible(true);
    }//GEN-LAST:event_MnRestoreActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        if(FormInput.getHeight()<350){   
            Scroll1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            FormInput.setPreferredSize(new Dimension(FormInput.WIDTH,350));
            if(FormInput.getWidth()<740){
                Scroll1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);                                
                FormInput.setPreferredSize(new Dimension(740,350));
            }else{
                Scroll1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);                
            }
        }else{
            Scroll1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);            
            if(FormInput.getWidth()<740){
                Scroll1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);                                
                FormInput.setPreferredSize(new Dimension(740,FormInput.WIDTH));
            }else{
                Scroll1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);                
            }
        }
    }//GEN-LAST:event_formWindowActivated

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        if(TabRawat.getSelectedIndex()==1){
            tampil();
        }
    }//GEN-LAST:event_TabRawatMouseClicked

    private void txtOnLoopKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOnLoopKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isjml();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            isjml();
            txtLinen.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isjml();
            txtMat.requestFocus();
        }
    }//GEN-LAST:event_txtOnLoopKeyPressed

    private void btnPenanggungJawabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPenanggungJawabActionPerformed
        dlgPenanggungJawab.isCek();
        dlgPenanggungJawab.onCari();
        dlgPenanggungJawab.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        dlgPenanggungJawab.setLocationRelativeTo(internalFrame1);
        dlgPenanggungJawab.setVisible(true);
    }//GEN-LAST:event_btnPenanggungJawabActionPerformed

    private void txtKdPenanggungJawabKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKdPenanggungJawabKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select png_jawab from penjab where kd_pj=?",txtNamaPenanggungJawab,txtKdPenanggungJawab.getText());
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            Sequel.cariIsi("select png_jawab from penjab where kd_pj=?",txtNamaPenanggungJawab,txtKdPenanggungJawab.getText());
            cbKelas.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            Sequel.cariIsi("select png_jawab from penjab where kd_pj=?",txtNamaPenanggungJawab,txtKdPenanggungJawab.getText());
            txtOperator.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
//            BtnPenjabActionPerformed(null);
        }
    }//GEN-LAST:event_txtKdPenanggungJawabKeyPressed

    private void cbKelasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbKelasKeyPressed
//        Valid.pindah(evt,TNm,kdpnj);
    }//GEN-LAST:event_cbKelasKeyPressed

    private void txtLinenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLinenKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isjml();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            isjml();
            txtAsistenOperator.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isjml();
            txtOnLoop.requestFocus();
        }
    }//GEN-LAST:event_txtLinenKeyPressed

    private void txtPenataAnestesiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPenataAnestesiKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isjml();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            isjml();
            txtDrAnestesi.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isjml();
            txtInstrumentator.requestFocus();
        }
    }//GEN-LAST:event_txtPenataAnestesiKeyPressed

    private void txtDrAnestesiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDrAnestesiKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isjml();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            isjml();
            txtOperator.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isjml();
            txtPenataAnestesi.requestFocus();
        }
    }//GEN-LAST:event_txtDrAnestesiKeyPressed

    private void txtMatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMatKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isjml();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            isjml();
            txtOnLoop.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isjml();
//            TAsisAnastesi1.requestFocus();
        }
    }//GEN-LAST:event_txtMatKeyPressed

    private void txtInstrumentatorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInstrumentatorKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isjml();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            isjml();
            txtPenataAnestesi.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isjml();
//            TAnak.requestFocus();
        }
    }//GEN-LAST:event_txtInstrumentatorKeyPressed

    private void txtAsistenOperatorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAsistenOperatorKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isjml();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            isjml();
//            TInstrumen.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isjml();
            txtLinen.requestFocus();
        }
    }//GEN-LAST:event_txtAsistenOperatorKeyPressed

    private void txtOperatorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOperatorKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isjml();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            isjml();
            txtKdPenanggungJawab.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isjml();
            txtDrAnestesi.requestFocus();
        }
    }//GEN-LAST:event_txtOperatorKeyPressed

    private void txtKodePaketKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKodePaketKeyPressed
//        Valid.pindah(evt,TCari,TNm);
    }//GEN-LAST:event_txtKodePaketKeyPressed

    private void txtTarifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTarifKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTarifKeyPressed

    private void txtNamaPaketKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamaPaketKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaPaketKeyPressed

    private void cbJenisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbJenisKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbJenisKeyPressed

    private void cbJenisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbJenisActionPerformed
        setNamaPaket();
    }//GEN-LAST:event_cbJenisActionPerformed

    private void cbKelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbKelasActionPerformed
        setNamaPaket();
    }//GEN-LAST:event_cbKelasActionPerformed

    private void tbJnsPerawatanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbJnsPerawatanMouseClicked
        setDataYangTampil();
    }//GEN-LAST:event_tbJnsPerawatanMouseClicked

    private void tbJnsPerawatanPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tbJnsPerawatanPropertyChange
        
    }//GEN-LAST:event_tbJnsPerawatanPropertyChange

    private void tbJnsPerawatanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbJnsPerawatanKeyPressed
        setDataYangTampil();
    }//GEN-LAST:event_tbJnsPerawatanKeyPressed

    private void txtKdKategoriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKdKategoriKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKdKategoriKeyPressed

    private void btnKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnKategoriActionPerformed

    private void tbKategoriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbKategoriMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbKategoriMouseClicked

    private void tbKategoriPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tbKategoriPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_tbKategoriPropertyChange

    private void tbKategoriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbKategoriKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbKategoriKeyPressed

    private void BtnSimpanKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnSimpanKActionPerformed

    private void BtnSimpanKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnSimpanKKeyPressed

    private void BtnBatalKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnBatalKActionPerformed

    private void BtnBatalKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnBatalKKeyPressed

    private void BtnHapusKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnHapusKActionPerformed

    private void BtnHapusKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnHapusKKeyPressed

    private void BtnEditKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnEditKActionPerformed

    private void BtnEditKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnEditKKeyPressed

    private void BtnAllKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnAllKActionPerformed

    private void BtnAllKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnAllKKeyPressed

    private void BtnKeluarKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnKeluarKActionPerformed

    private void BtnKeluarKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnKeluarKKeyPressed

    private void TCariKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TCariKKeyPressed

    private void BtnCariKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCariKActionPerformed

    private void BtnCariKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCariKKeyPressed

    private void txtKdGroupKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKdGroupKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKdGroupKeyPressed

    private void btnGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGroupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGroupActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgJnsPerawatanOperasi1 dialog = new DlgJnsPerawatanOperasi1(new javax.swing.JFrame(), true);
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
    private widget.Button BtnAll;
    private widget.Button BtnAllK;
    private widget.Button BtnBatal;
    private widget.Button BtnBatalK;
    private widget.Button BtnCari;
    private widget.Button BtnCariK;
    private widget.Button BtnEdit;
    private widget.Button BtnEditK;
    private widget.Button BtnHapus;
    private widget.Button BtnHapusK;
    private widget.Button BtnKeluar;
    private widget.Button BtnKeluarK;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.Button BtnSimpanK;
    private widget.PanelBiasa FormInput;
    private widget.PanelBiasa FormInput1;
    private widget.Label LCount;
    private widget.Label LCountK;
    private javax.swing.JMenuItem MnRestore;
    private widget.ScrollPane Scroll1;
    private widget.ScrollPane Scroll2;
    private widget.ScrollPane Scroll4;
    private widget.ScrollPane Scroll5;
    private widget.TextBox TCari;
    private widget.TextBox TCariK;
    private javax.swing.JTabbedPane TabRawat;
    private widget.Button btnGroup;
    private widget.Button btnKategori;
    private widget.Button btnPenanggungJawab;
    private widget.ComboBox cbJenis;
    private widget.ComboBox cbKelas;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame5;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel12;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel29;
    private widget.Label jLabel3;
    private widget.Label jLabel30;
    private widget.Label jLabel34;
    private widget.Label jLabel35;
    private widget.Label jLabel36;
    private widget.Label jLabel4;
    private widget.Label jLabel5;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.panelisi panelGlass10;
    private widget.panelisi panelGlass11;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.Table tbJnsPerawatan;
    private widget.Table tbKategori;
    private widget.TextBox txtAsistenOperator;
    private widget.TextBox txtDrAnestesi;
    private widget.TextBox txtInstrumentator;
    private widget.TextBox txtKdGroup;
    private widget.TextBox txtKdKategori;
    private widget.TextBox txtKdPenanggungJawab;
    private widget.TextBox txtKodePaket;
    private widget.TextBox txtLinen;
    private widget.TextBox txtMat;
    private widget.TextBox txtNamaGroup;
    private widget.TextBox txtNamaKategori;
    private widget.TextBox txtNamaPaket;
    private widget.TextBox txtNamaPenanggungJawab;
    private widget.TextBox txtOnLoop;
    private widget.TextBox txtOperator;
    private widget.TextBox txtPenataAnestesi;
    private widget.TextBox txtTarif;
    // End of variables declaration//GEN-END:variables

    private void tampil() {
        Valid.tabelKosong(tabModePerawatan);
        
        new GQuery()
                .a("SELECT * FROM paket_operasi_2")
                .a("WHERE kode_paket like {x} OR")
                .a("nama_paket like {x}")
                .set("x", "%" + TCari.getText() + "%")
                .selectAsObJectArray()
                .forEach(s -> 
                {
                    Object[] o = new Object[s.length + 1];
                    o[0] = false;
                    for (int i = 1; i <= s.length; i++)
                    {
                        o[i] = s[i - 1];
                    }
                    tabModePerawatan.addRow(o);
                });
        
        if (true) 
        {
            return;
        }
        
        try{    
            pstampil=koneksi.prepareStatement(
                       "select paket_operasi.kode_paket, paket_operasi.nm_perawatan,paket_operasi.kategori,"+
                       "paket_operasi.operator1, paket_operasi.operator2, paket_operasi.operator3, "+
                       "paket_operasi.asisten_operator1, paket_operasi.asisten_operator2,paket_operasi.asisten_operator3,"+
                       "paket_operasi.instrumen, paket_operasi.dokter_anestesi,paket_operasi.asisten_anestesi,paket_operasi.asisten_anestesi2,"+
                       "paket_operasi.dokter_anak,paket_operasi.perawaat_resusitas, paket_operasi.bidan, "+
                       "paket_operasi.bidan2, paket_operasi.bidan3, paket_operasi.perawat_luar, paket_operasi.alat,"+
                       "paket_operasi.sewa_ok,paket_operasi.akomodasi,paket_operasi.bagian_rs,"+
                       "paket_operasi.omloop,paket_operasi.omloop2,paket_operasi.omloop3,paket_operasi.omloop4,paket_operasi.omloop5,"+
                       "paket_operasi.sarpras,paket_operasi.dokter_pjanak,paket_operasi.dokter_umum, "+
                       "(paket_operasi.operator1+paket_operasi.operator2+paket_operasi.operator3+"+
                       "paket_operasi.asisten_operator1+paket_operasi.asisten_operator2+paket_operasi.asisten_operator3+paket_operasi.instrumen+"+
                       "paket_operasi.dokter_anak+paket_operasi.perawaat_resusitas+"+
                       "paket_operasi.alat+paket_operasi.dokter_anestesi+paket_operasi.asisten_anestesi+paket_operasi.asisten_anestesi2+"+
                       "paket_operasi.bidan+paket_operasi.bidan2+paket_operasi.bidan3+paket_operasi.perawat_luar+"+
                       "paket_operasi.sewa_ok+paket_operasi.akomodasi+paket_operasi.bagian_rs+"+
                       "paket_operasi.omloop+paket_operasi.omloop2+paket_operasi.omloop3+paket_operasi.omloop4+paket_operasi.omloop5+"+
                       "paket_operasi.sarpras+paket_operasi.dokter_pjanak+paket_operasi.dokter_umum) as jumlah, "+
                       "penjab.png_jawab from paket_operasi inner join penjab on penjab.kd_pj=paket_operasi.kd_pj "+
                       "where paket_operasi.status='1' and paket_operasi.kode_paket like ? or "+
                       "paket_operasi.status='1' and paket_operasi.nm_perawatan like ? or "+
                       "paket_operasi.status='1' and penjab.png_jawab like ? order by paket_operasi.kode_paket ");
            try{
                pstampil.setString(1,"%"+TCari.getText()+"%");
                pstampil.setString(2,"%"+TCari.getText()+"%");
                pstampil.setString(3,"%"+TCari.getText()+"%");
                rs=pstampil.executeQuery();
                while(rs.next()){                    
                    tabModePerawatan.addRow(new Object[]{false,rs.getString("kode_paket"),
                                   rs.getString("nm_perawatan"),
                                   rs.getString("kategori"), 
                                   rs.getDouble("operator1"), 
                                   rs.getDouble("operator2"), 
                                   rs.getDouble("operator3"), 
                                   rs.getDouble("asisten_operator1"), 
                                   rs.getDouble("asisten_operator2"), 
                                   rs.getDouble("asisten_operator3"), 
                                   rs.getDouble("instrumen"), 
                                   rs.getDouble("dokter_anestesi"), 
                                   rs.getDouble("asisten_anestesi"),
                                   rs.getDouble("asisten_anestesi2"), 
                                   rs.getDouble("dokter_anak"), 
                                   rs.getDouble("perawaat_resusitas"), 
                                   rs.getDouble("bidan"), 
                                   rs.getDouble("bidan2"), 
                                   rs.getDouble("bidan3"), 
                                   rs.getDouble("perawat_luar"), 
                                   rs.getDouble("alat"), 
                                   rs.getDouble("sewa_ok"), 
                                   rs.getDouble("akomodasi"), 
                                   rs.getDouble("bagian_rs"), 
                                   rs.getDouble("omloop"), 
                                   rs.getDouble("omloop2"), 
                                   rs.getDouble("omloop3"), 
                                   rs.getDouble("omloop4"), 
                                   rs.getDouble("omloop5"), 
                                   rs.getDouble("sarpras"), 
                                   rs.getDouble("dokter_pjanak"), 
                                   rs.getDouble("dokter_umum"), 
                                   rs.getDouble("jumlah"),
                                   rs.getString("png_jawab")
                    });
                }  
            } catch(Exception e){
                System.out.println(e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(pstampil!=null){
                    pstampil.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabModePerawatan.getRowCount());
    }

    public void emptTeks() {
        txtKodePaket.setText("");
        txtOperator.setText("0");
        txtDrAnestesi.setText("0");
        txtPenataAnestesi.setText("0");
        txtAsistenOperator.setText("0");
        txtLinen.setText("0");
        txtOnLoop.setText("0");
        txtInstrumentator.setText("0");
        txtMat.setText("0");        
        cbJenis.setSelectedIndex(0);
        cbKelas.setSelectedIndex(0);
        
        String nextId = new GQuery("SELECT kode_paket FROM paket_operasi_2 ORDER BY kode_paket DESC").getString();
        nextId = GConvert.parseInt(nextId) + 1 + "";
        
        txtKodePaket.setText(nextId);
        txtKodePaket.requestFocus();
    }

    private void getData() 
    {
        if(tbJnsPerawatan.getSelectedRow()!= -1)
        {
            txtKodePaket.setText(tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),1).toString());
            cbKelas.setSelectedItem(tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),3).toString());
            txtOperator.setText(Valid.SetAngka2(Double.parseDouble(tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),4).toString())));
            txtDrAnestesi.setText(Valid.SetAngka2(Double.parseDouble(tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),5).toString())));
            txtPenataAnestesi.setText(Valid.SetAngka2(Double.parseDouble(tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),6).toString())));
            txtAsistenOperator.setText(Valid.SetAngka2(Double.parseDouble(tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),7).toString())));
            txtLinen.setText(Valid.SetAngka2(Double.parseDouble(tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),8).toString())));
            txtOnLoop.setText(Valid.SetAngka2(Double.parseDouble(tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),9).toString())));
            txtInstrumentator.setText(Valid.SetAngka2(Double.parseDouble(tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),11).toString())));
            txtMat.setText(Valid.SetAngka2(Double.parseDouble(tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),12).toString())));
            Sequel.cariIsi("select kd_pj from paket_operasi where kode_paket=?", txtKdPenanggungJawab,tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),1).toString());
            txtNamaPenanggungJawab.setText(tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),33).toString());
        }
    }


    private void isjml(){       

//        if((!txtOperator.getText().equals(""))){
//           operator1=Double.parseDouble(txtOperator.getText().trim());            
//        }
//        if((!txtDrAnestesi.getText().equals(""))){
//           operator2=Double.parseDouble(txtDrAnestesi.getText().trim());            
//        }
//        if((!txtPenataAnestesi.getText().equals(""))){
//           operator3=Double.parseDouble(txtPenataAnestesi.getText().trim());            
//        }
//        if((!txtAsistenOperator.getText().equals(""))){
//           asistenoperator1=Double.parseDouble(txtAsistenOperator.getText().trim());            
//        }
//        if((!txtLinen.getText().equals(""))){
//           asistenoperator2=Double.parseDouble(txtLinen.getText().trim());            
//        }
//        if((!txtOnLoop.getText().equals(""))){
//           asistenoperator3=Double.parseDouble(txtOnLoop.getText().trim());            
//        }
//        if((!txtInstrumentator.getText().equals(""))){
//           anastesi=Double.parseDouble(txtInstrumentator.getText().trim());            
//        }
//        if((!txtMat.getText().equals(""))){
//           asistenanas=Double.parseDouble(txtMat.getText().trim());            
//        }
//        TTotal.setText(Valid.SetAngka2(operator1+operator2+operator3+asistenoperator1+
//                asistenoperator2+asistenoperator3+instrumen+dokteranak+perawatresusitas+bidan1+bidan2+bidan3+
//                alat+anastesi+perawatluar+asistenanas+asistenanas2+sewaok+sewavk+bagianrs+omloop1+omloop2+
//                omloop3+omloop4+omloop5+sarpras+dokterpjanak+dokterumum));
    }
    
    public JTextField getTextField(){
        return txtKodePaket;
    }

    public JButton getButton(){
        return BtnKeluar;
    }    
    
    public void isCek(){
        BtnSimpan.setEnabled(var.gettarif_operasi());
        BtnHapus.setEnabled(var.gettarif_operasi());
        BtnEdit.setEnabled(var.gettarif_operasi());
        BtnPrint.setEnabled(var.gettarif_operasi());
        if(var.getkode().equals("Admin Utama")){
            MnRestore.setEnabled(true);
        }else{
            MnRestore.setEnabled(false);
        } 
    }
    
    public JTable getTable()
    {
        return tbJnsPerawatan;
    }

    private void setNamaPaket() 
    {
        txtNamaPaket.setText(cbJenis.getSelectedItem().toString() + " " + cbKelas.getSelectedItem().toString());
    }

    private void setDataYangTampil() 
    {
        int row = tbJnsPerawatan.getSelectedRow();
        
        if (row == -1) return;
        
        txtKodePaket.setText(tbJnsPerawatan.getValueAt(row, 1).toString());
        txtNamaPaket.setText(tbJnsPerawatan.getValueAt(row, 4).toString());
        txtTarif.setText(tbJnsPerawatan.getValueAt(row, 5).toString());
        txtOperator.setText(tbJnsPerawatan.getValueAt(row, 6).toString());
        txtDrAnestesi.setText(tbJnsPerawatan.getValueAt(row, 7).toString());
        txtPenataAnestesi.setText(tbJnsPerawatan.getValueAt(row, 8).toString());
        txtInstrumentator.setText(tbJnsPerawatan.getValueAt(row, 9).toString());
        txtAsistenOperator.setText(tbJnsPerawatan.getValueAt(row, 10).toString());
        txtLinen.setText(tbJnsPerawatan.getValueAt(row, 11).toString());
        txtOnLoop.setText(tbJnsPerawatan.getValueAt(row, 12).toString());
        txtMat.setText(tbJnsPerawatan.getValueAt(row, 13).toString());
        txtKdPenanggungJawab.setText(tbJnsPerawatan.getValueAt(row, 14).toString());
        txtNamaPenanggungJawab.setText(
                new GQuery("SELECT png_jawab FROM penjab where kd_pj = {kd}")
                        .set("kd", txtKdPenanggungJawab.getText())
                        .getString()
        );
        
        for (int i = 0; i < cbJenis.getItemCount(); i++)
        {
            String j = (String) tbJnsPerawatan.getValueAt(row, 2);
            if (j.equals(cbJenis.getItemAt(i)))
            {
                cbJenis.setSelectedIndex(i);
            }
        }
        for (int i = 0; i < cbKelas.getItemCount(); i++)
        {
            String k = (String) tbJnsPerawatan.getValueAt(row, 3);
            if (k.equals(cbKelas.getItemAt(i)))
            {
                cbKelas.setSelectedIndex(i);
            }
        }
    }
    
    TextChangedListener t = new TextChangedListener() 
    {
        @Override
        public void onTextChanged(TextBox txt) 
        {
            int i = GConvert.parseInt(txtOperator.getText()) + GConvert.parseInt(txtDrAnestesi.getText()) +
                GConvert.parseInt(txtPenataAnestesi.getText()) + GConvert.parseInt(txtInstrumentator.getText()) +
                GConvert.parseInt(txtAsistenOperator.getText()) + GConvert.parseInt(txtLinen.getText()) +
                GConvert.parseInt(txtOnLoop.getText()) + GConvert.parseInt(txtMat.getText());
        
            txtTarif.setText(i + "");
        }
    };
}
