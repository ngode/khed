/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simrskhanza;

import fungsi.EntPemberianDiet;
import fungsi.impPemberianDiet;
import base.BaseDialog;
import fungsi.GQuery;
import fungsi.GStatement;
import fungsi.WarnaTable;
import fungsi.Warnatable_Diet;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.var;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.GMessage;

/**
 *
 * @author GrT
 */
public class DlgLihatDiet extends BaseDialog
{
    // Const =========
 private DefaultTableModel tabMode, tabMode_1;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps,ps1;
    private ResultSet rs;
    impPemberianDiet diet = new impPemberianDiet();
    EntPemberianDiet EntDiet = new EntPemberianDiet();


    /**
     * Creates new form DlgOperasi
     */
    public DlgLihatDiet(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
      
        this.setLocation(10,10);
        setSize(459,539);
        dietRanap();
        dietRalan();
    }
    
    private void dietRalan(){
       Object[] row={"No.Rawat","Nama Pasien","Tanggal","Waktu","Bentuk Makanan", "Jenis Diet", "Ekstra","Status Print"};
        tabMode_1=new DefaultTableModel(null,row){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };

        tbPemberianDiet_Ralan.setModel(tabMode_1);
        //tampil();
        //tbJabatan.setDefaultRenderer(Object.class, new WarnaTable(Scroll.getBackground(),Color.GREEN));
        tbPemberianDiet_Ralan.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbPemberianDiet_Ralan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < 8; i++) {
            TableColumn column = tbPemberianDiet_Ralan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(120);
            }else if(i==1){
                column.setPreferredWidth(250);
            }else if(i==2){
                column.setPreferredWidth(120);
            }else if(i==3){
                column.setPreferredWidth(120);
            }else if(i==4){
                column.setPreferredWidth(250);
            }else if(i==5){
                column.setPreferredWidth(250);
            }else if(i==6){
                column.setPreferredWidth(150);
            }else if(i==7){
                column.setPreferredWidth(150);
            }
        }

        tbPemberianDiet_Ralan.setDefaultRenderer(Object.class, new Warnatable_Diet(7));

        TNoRw_Ralan.setDocument(new batasInput((byte)17).getKata(TNoRw_Ralan));
        NmEkstra_Ralan.setDocument(new batasInput((byte)20).getKata(NmEkstra_Ralan));
        TCari_Ralan.setDocument(new batasInput((byte)100).getKata(TCari_Ralan));
        if(koneksiDB.cariCepat().equals("aktif")){
            TCari_Ralan.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {tampil_Ralan();}
                @Override
                public void removeUpdate(DocumentEvent e) {tampil_Ralan();}
                @Override
                public void changedUpdate(DocumentEvent e) {tampil_Ralan();}
            });
        } 
        
        
        jDiet.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(var.getform().equals("DlgPemberianObat")){
                    if(jDiet.getTable().getSelectedRow()!= -1){                          
                        NmJenisDiet_Ralan.setText(jDiet.getTable().getValueAt(jDiet.getTable().getSelectedRow(),1).toString());
                        NmJenisDiet_Ralan.requestFocus();                           
                    }                         
                }
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
        
        bMakanan.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(var.getform().equals("DlgPemberianObat")){
                    if(bMakanan.getTable().getSelectedRow()!= -1){                          
                        NmBentukMakanan_Ralan.setText(bMakanan.getTable().getValueAt(bMakanan.getTable().getSelectedRow(),1).toString());
                        NmBentukMakanan_Ralan.requestFocus();                           
                    }                         
                }
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
        
        try {
            ps=koneksi.prepareStatement("select detail_beri_diet_ralan.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, " +
                "detail_beri_diet_ralan.tanggal,detail_beri_diet_ralan.waktu, " +
                "detail_beri_diet_ralan.bentuk_makanan, detail_beri_diet_ralan.jenis_diet, detail_beri_diet_ralan.ekstra,detail_beri_diet_ralan.status " +
                "from detail_beri_diet_ralan inner join reg_periksa inner join pasien " +
                "on detail_beri_diet_ralan.no_rawat=reg_periksa.no_rawat  " +
                "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                "where detail_beri_diet_ralan.tanggal between ? and ? and detail_beri_diet_ralan.waktu like ? and detail_beri_diet_ralan.no_rawat like ? and detail_beri_diet_ralan.status like ? or " +
                "detail_beri_diet_ralan.tanggal between ? and ? and detail_beri_diet_ralan.waktu like ? and reg_periksa.no_rkm_medis like ? and detail_beri_diet_ralan.status like ? or " +
                "detail_beri_diet_ralan.tanggal between ? and ? and detail_beri_diet_ralan.waktu like ? and pasien.nm_pasien like ? and detail_beri_diet_ralan.status like ? " +
                "order by detail_beri_diet_ralan.bentuk_makanan, detail_beri_diet_ralan.jenis_diet");
        } catch (SQLException e) {
            System.out.println(e);
        }
        tampil_Ralan();
    }
    
    private void dietRanap(){
    
        Object[] row={"No.Rawat","Nama Pasien","Kamar","Tanggal","Waktu","Diagnosa",
            "Bentuk Makanan", "Jenis Diet", "Ekstra","Status Print", "Status Pulang"};
        tabMode=new DefaultTableModel(null,row){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };

        tbPemberianDiet_Ranap.setModel(tabMode);
        //tampil();
        //tbJabatan.setDefaultRenderer(Object.class, new WarnaTable(Scroll.getBackground(),Color.GREEN));
        tbPemberianDiet_Ranap.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbPemberianDiet_Ranap.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < 11; i++) {
            TableColumn column = tbPemberianDiet_Ranap.getColumnModel().getColumn(i);
           if(i==0){
                column.setPreferredWidth(120);
            }else if(i==1){
                column.setPreferredWidth(250);
            }else if(i==2){
                column.setPreferredWidth(120);
            }else if(i==3){
                column.setPreferredWidth(120);
            }else if(i==4){
                column.setPreferredWidth(120);
            }else if(i==5){
                column.setPreferredWidth(120);
            }else if(i==6){
                column.setPreferredWidth(250);
            }else if(i==7){
                column.setPreferredWidth(250);
            }else if(i==8){
                column.setPreferredWidth(150);
            }else if(i==9){
                column.setPreferredWidth(150);
            }else if(i==10){
                column.setPreferredWidth(150);
            }
        }

        tbPemberianDiet_Ranap.setDefaultRenderer(Object.class, new Warnatable_Diet(9));

       TNoRw_Ranap.setDocument(new batasInput((byte)17).getKata(TNoRw_Ranap));
        NmEkstra_Ranap.setDocument(new batasInput((byte)20).getKata(NmEkstra_Ranap));
        TCari_Ranap.setDocument(new batasInput((byte)100).getKata(TCari_Ranap));
        if(koneksiDB.cariCepat().equals("aktif")){
            TCari_Ranap.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {tampil_Ranap();}
                @Override
                public void removeUpdate(DocumentEvent e) {tampil_Ranap();}
                @Override
                public void changedUpdate(DocumentEvent e) {tampil_Ranap();}
            });
        } 
        
        jDiet.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(var.getform().equals("DlgPemberianObat")){
                    if(jDiet.getTable().getSelectedRow()!= -1){                          
                        NmJenisDiet_Ranap.setText(jDiet.getTable().getValueAt(jDiet.getTable().getSelectedRow(),1).toString());
                        NmJenisDiet_Ranap.requestFocus();                           
                    }                         
                }
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
        
        bMakanan.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(var.getform().equals("DlgPemberianObat")){
                    if(bMakanan.getTable().getSelectedRow()!= -1){                          
                        NmBentukMakanan_Ranap.setText(bMakanan.getTable().getValueAt(bMakanan.getTable().getSelectedRow(),1).toString());
                        NmBentukMakanan_Ranap.requestFocus();                           
                    }                         
                }
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
        
        try {
            ps1=koneksi.prepareStatement("select detail_beri_diet.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, " +
                "concat(detail_beri_diet.kd_kamar,', ',bangsal.nm_bangsal),detail_beri_diet.tanggal,detail_beri_diet.waktu, " +
                "detail_beri_diet.bentuk_makanan, detail_beri_diet.jenis_diet, detail_beri_diet.ekstra, detail_beri_diet.status, detail_beri_diet.sts_Pulang " +
                "from detail_beri_diet inner join reg_periksa inner join pasien inner join kamar inner join bangsal " +
                "on detail_beri_diet.no_rawat=reg_periksa.no_rawat " +
                "and detail_beri_diet.kd_kamar=kamar.kd_kamar "+
                "and kamar.kd_bangsal=bangsal.kd_bangsal "+
                "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                "where detail_beri_diet.tanggal between ? and ? and detail_beri_diet.waktu like ? and bangsal.nm_bangsal like ? " +
                "and detail_beri_diet.no_rawat like ? and detail_beri_diet.status like ? and detail_beri_diet.sts_Pulang = '-' or " +
                "detail_beri_diet.tanggal between ? and ? and detail_beri_diet.waktu like ? and bangsal.nm_bangsal like ? " +
                "and reg_periksa.no_rkm_medis like ? and detail_beri_diet.status like ? and detail_beri_diet.sts_Pulang = '-' or " +
                "detail_beri_diet.tanggal between ? and ? and detail_beri_diet.waktu like ? and bangsal.nm_bangsal like ? " +
                "and pasien.nm_pasien like ? and detail_beri_diet.status like ? and detail_beri_diet.sts_Pulang = '-' " +
                "order by bangsal.nm_bangsal,detail_beri_diet.bentuk_makanan, detail_beri_diet.jenis_diet");
        } catch (SQLException e) {
            System.out.println(e);
        }
        
        
        bangsal.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(var.getform().equals("DlgPemberianObat")){
                    if(bangsal.getTable().getSelectedRow()!= -1){                          
                        NmBangsalCari_Ranap.setText(bangsal.getTable().getValueAt(bangsal.getTable().getSelectedRow(),1).toString());
                        NmBangsalCari_Ranap.requestFocus();                           
                    }                         
                }
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

        tampil_Ranap();
    }
    
    
    private DlgCariBangsal bangsal=new DlgCariBangsal(null,false);
    private DlgLihatPemberianDiet_Ralan dietRalan = new DlgLihatPemberianDiet_Ralan(null, false);
    private DlgCariJenisDiet jDiet = new DlgCariJenisDiet(null, false);
    private DlgCariBentukMakanan bMakanan = new DlgCariBentukMakanan(null, false);
    // Init method
  
    
    // <editor-fold defaultstate="collapsed" desc=" GENERATED ">
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popUpMenu_Ranap = new javax.swing.JPopupMenu();
        print_Ranap = new javax.swing.JMenuItem();
        popUpMenu_Ralan = new javax.swing.JPopupMenu();
        Print_Ralan = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        tabPane = new widget.TabPane();
        panelBiasa2 = new widget.PanelBiasa();
        internalFrame2 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbPemberianDiet_Ralan = new widget.Table();
        FormInput1 = new widget.PanelBiasa();
        jLabel4 = new widget.Label();
        TNoRw_Ralan = new widget.TextBox();
        TPasien_Ralan = new widget.TextBox();
        DTPTgl_Ralan = new widget.Tanggal();
        cmbJam_Ralan = new widget.ComboBox();
        jLabel17 = new widget.Label();
        NmEkstra_Ralan = new widget.TextBox();
        BtnSeekJenisDiet_Ralan = new widget.Button();
        NmBentukMakanan_Ralan = new widget.TextBox();
        BtnSeekBentukMakanan_Ralan = new widget.Button();
        NmJenisDiet_Ralan = new widget.TextBox();
        jLabel27 = new widget.Label();
        jLabel28 = new widget.Label();
        jLabel29 = new widget.Label();
        panelGlass8 = new widget.panelisi();
        BtnHapus_Ralan = new widget.Button();
        BtnEdit_Ralan = new widget.Button();
        BtnPrint_Ralan = new widget.Button();
        BtnKeluar_Ralan = new widget.Button();
        panelGlass11 = new widget.panelisi();
        jLabel19 = new widget.Label();
        DTPCari1_Ralan = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2_Ralan = new widget.Tanggal();
        jLabel6 = new widget.Label();
        TCari_Ralan = new widget.TextBox();
        BtnCari_Ralan = new widget.Button();
        BtnAll_Ralan = new widget.Button();
        jLabel7 = new widget.Label();
        LCount_Ralan = new widget.Label();
        panelGlass10 = new widget.panelisi();
        jLabel11 = new widget.Label();
        cmbJamCari_Ralan = new widget.ComboBox();
        jLabel13 = new widget.Label();
        cmbStatus_Ralan = new widget.ComboBox();
        panelBiasa3 = new widget.PanelBiasa();
        internalFrame3 = new widget.InternalFrame();
        Scroll1 = new widget.ScrollPane();
        tbPemberianDiet_Ranap = new widget.Table();
        FormInput = new widget.PanelBiasa();
        jLabel15 = new widget.Label();
        TNoRw_Ranap = new widget.TextBox();
        TPasien_Ranap = new widget.TextBox();
        DTPTgl_Ranap = new widget.Tanggal();
        cmbJam_Ranap = new widget.ComboBox();
        jLabel10 = new widget.Label();
        Kamar_Ranap = new widget.TextBox();
        NmEkstra_Ranap = new widget.TextBox();
        BtnSeekJenisDiet_Ranap = new widget.Button();
        NmBentukMakanan_Ranap = new widget.TextBox();
        BtnSeekBentukMakanan_Ranap = new widget.Button();
        NmJenisDiet_Ranap = new widget.TextBox();
        jLabel24 = new widget.Label();
        jLabel25 = new widget.Label();
        jLabel26 = new widget.Label();
        panelGlass9 = new widget.panelisi();
        BtnHapus_Ranap = new widget.Button();
        BtnEdit_Ranap = new widget.Button();
        btnPrintRanap_All = new widget.Button();
        BtnKeluar_Ranap = new widget.Button();
        panelGlass14 = new widget.panelisi();
        jLabel20 = new widget.Label();
        DTPCari3_Ranap = new widget.Tanggal();
        jLabel22 = new widget.Label();
        DTPCari4_Ranap = new widget.Tanggal();
        jLabel8 = new widget.Label();
        TCari_Ranap = new widget.TextBox();
        BtnCari_Ranap = new widget.Button();
        BtnAll_Ranap = new widget.Button();
        jLabel9 = new widget.Label();
        LCount_Ranap = new widget.Label();
        panelGlass13 = new widget.panelisi();
        jLabel14 = new widget.Label();
        cmbJamCari_Ranap = new widget.ComboBox();
        jLabel23 = new widget.Label();
        cmbStatus_Ranap = new widget.ComboBox();
        jLabel12 = new widget.Label();
        NmBangsalCari_Ranap = new widget.TextBox();
        BtnSeek3_Ranap = new widget.Button();

        popUpMenu_Ranap.setForeground(new java.awt.Color(60, 80, 50));
        popUpMenu_Ranap.setAutoscrolls(true);
        popUpMenu_Ranap.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        popUpMenu_Ranap.setFocusTraversalPolicyProvider(true);

        print_Ranap.setBackground(new java.awt.Color(255, 255, 255));
        print_Ranap.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        print_Ranap.setForeground(new java.awt.Color(60, 80, 50));
        print_Ranap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        print_Ranap.setText("Print Label Diet Ranap");
        print_Ranap.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        print_Ranap.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        print_Ranap.setIconTextGap(5);
        print_Ranap.setPreferredSize(new java.awt.Dimension(220, 26));
        print_Ranap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print_RanapActionPerformed(evt);
            }
        });
        popUpMenu_Ranap.add(print_Ranap);

        popUpMenu_Ralan.setForeground(new java.awt.Color(60, 80, 50));
        popUpMenu_Ralan.setAutoscrolls(true);
        popUpMenu_Ralan.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        popUpMenu_Ralan.setFocusTraversalPolicyProvider(true);

        Print_Ralan.setBackground(new java.awt.Color(255, 255, 255));
        Print_Ralan.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        Print_Ralan.setForeground(new java.awt.Color(60, 80, 50));
        Print_Ralan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        Print_Ralan.setText("Print Label Diet Ralan");
        Print_Ralan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Print_Ralan.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Print_Ralan.setIconTextGap(5);
        Print_Ralan.setPreferredSize(new java.awt.Dimension(220, 26));
        Print_Ralan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Print_RalanActionPerformed(evt);
            }
        });
        popUpMenu_Ralan.add(Print_Ralan);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "::[ Operasi ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout());

        tabPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabPaneMouseClicked(evt);
            }
        });

        panelBiasa2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelBiasa2MouseClicked(evt);
            }
        });
        panelBiasa2.setLayout(new java.awt.BorderLayout());

        internalFrame2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data Pemberian Diet ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N

        Scroll.setOpaque(true);
        Scroll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ScrollMouseClicked(evt);
            }
        });

        tbPemberianDiet_Ralan.setAutoCreateRowSorter(true);
        tbPemberianDiet_Ralan.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbPemberianDiet_Ralan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbPemberianDiet_RalanMouseClicked(evt);
            }
        });
        tbPemberianDiet_Ralan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbPemberianDiet_RalanKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbPemberianDiet_Ralan);

        FormInput1.setPreferredSize(new java.awt.Dimension(160, 77));
        FormInput1.setLayout(null);

        jLabel4.setText("No.Rawat :");
        FormInput1.add(jLabel4);
        jLabel4.setBounds(0, 12, 75, 23);

        TNoRw_Ralan.setHighlighter(null);
        TNoRw_Ralan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw_RalanKeyPressed(evt);
            }
        });
        FormInput1.add(TNoRw_Ralan);
        TNoRw_Ralan.setBounds(78, 12, 150, 23);

        TPasien_Ralan.setEditable(false);
        TPasien_Ralan.setHighlighter(null);
        TPasien_Ralan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TPasien_RalanActionPerformed(evt);
            }
        });
        FormInput1.add(TPasien_Ralan);
        TPasien_Ralan.setBounds(235, 12, 260, 23);

        DTPTgl_Ralan.setEditable(false);
        DTPTgl_Ralan.setForeground(new java.awt.Color(50, 70, 50));
        DTPTgl_Ralan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-05-2018" }));
        DTPTgl_Ralan.setDisplayFormat("dd-MM-yyyy");
        DTPTgl_Ralan.setOpaque(false);
        DTPTgl_Ralan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPTgl_RalanKeyPressed(evt);
            }
        });
        FormInput1.add(DTPTgl_Ralan);
        DTPTgl_Ralan.setBounds(78, 42, 125, 23);

        cmbJam_Ralan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pagi", "Siang", "Sore" }));
        cmbJam_Ralan.setOpaque(false);
        cmbJam_Ralan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbJam_RalanKeyPressed(evt);
            }
        });
        FormInput1.add(cmbJam_Ralan);
        cmbJam_Ralan.setBounds(205, 42, 100, 23);

        jLabel17.setText("Tanggal :");
        FormInput1.add(jLabel17);
        jLabel17.setBounds(0, 42, 75, 23);

        NmEkstra_Ralan.setHighlighter(null);
        FormInput1.add(NmEkstra_Ralan);
        NmEkstra_Ralan.setBounds(930, 10, 210, 24);

        BtnSeekJenisDiet_Ralan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeekJenisDiet_Ralan.setMnemonic('X');
        BtnSeekJenisDiet_Ralan.setToolTipText("Alt+X");
        BtnSeekJenisDiet_Ralan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeekJenisDiet_RalanActionPerformed(evt);
            }
        });
        BtnSeekJenisDiet_Ralan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSeekJenisDiet_RalanKeyPressed(evt);
            }
        });
        FormInput1.add(BtnSeekJenisDiet_Ralan);
        BtnSeekJenisDiet_Ralan.setBounds(820, 40, 28, 23);

        NmBentukMakanan_Ralan.setEditable(false);
        NmBentukMakanan_Ralan.setHighlighter(null);
        FormInput1.add(NmBentukMakanan_Ralan);
        NmBentukMakanan_Ralan.setBounds(660, 10, 160, 23);

        BtnSeekBentukMakanan_Ralan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeekBentukMakanan_Ralan.setMnemonic('X');
        BtnSeekBentukMakanan_Ralan.setToolTipText("Alt+X");
        BtnSeekBentukMakanan_Ralan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeekBentukMakanan_RalanActionPerformed(evt);
            }
        });
        BtnSeekBentukMakanan_Ralan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSeekBentukMakanan_RalanKeyPressed(evt);
            }
        });
        FormInput1.add(BtnSeekBentukMakanan_Ralan);
        BtnSeekBentukMakanan_Ralan.setBounds(820, 10, 28, 23);

        NmJenisDiet_Ralan.setEditable(false);
        NmJenisDiet_Ralan.setHighlighter(null);
        FormInput1.add(NmJenisDiet_Ralan);
        NmJenisDiet_Ralan.setBounds(660, 40, 160, 24);

        jLabel27.setText("Bentuk Maknan :");
        FormInput1.add(jLabel27);
        jLabel27.setBounds(550, 10, 100, 23);

        jLabel28.setText("Jenis Diet :");
        FormInput1.add(jLabel28);
        jLabel28.setBounds(550, 40, 100, 23);

        jLabel29.setText("Ekstra :");
        FormInput1.add(jLabel29);
        jLabel29.setBounds(860, 10, 60, 23);

        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnHapus_Ralan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus_Ralan.setMnemonic('H');
        BtnHapus_Ralan.setText("Hapus");
        BtnHapus_Ralan.setToolTipText("Alt+H");
        BtnHapus_Ralan.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus_Ralan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapus_RalanActionPerformed(evt);
            }
        });
        BtnHapus_Ralan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapus_RalanKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnHapus_Ralan);

        BtnEdit_Ralan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnEdit_Ralan.setMnemonic('G');
        BtnEdit_Ralan.setText("Ganti");
        BtnEdit_Ralan.setToolTipText("Alt+G");
        BtnEdit_Ralan.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnEdit_Ralan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEdit_RalanActionPerformed(evt);
            }
        });
        BtnEdit_Ralan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnEdit_RalanKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnEdit_Ralan);

        BtnPrint_Ralan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/PrinterSettings.png"))); // NOI18N
        BtnPrint_Ralan.setMnemonic('P');
        BtnPrint_Ralan.setText("Print");
        BtnPrint_Ralan.setToolTipText("Alt+P");
        BtnPrint_Ralan.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint_Ralan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrint_RalanActionPerformed(evt);
            }
        });
        BtnPrint_Ralan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPrint_RalanKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnPrint_Ralan);

        BtnKeluar_Ralan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar_Ralan.setMnemonic('K');
        BtnKeluar_Ralan.setText("Keluar");
        BtnKeluar_Ralan.setToolTipText("Alt+K");
        BtnKeluar_Ralan.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar_Ralan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluar_RalanActionPerformed(evt);
            }
        });
        BtnKeluar_Ralan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluar_RalanKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnKeluar_Ralan);

        panelGlass11.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel19.setText("Tgl.Rawat :");
        jLabel19.setPreferredSize(new java.awt.Dimension(67, 23));
        panelGlass11.add(jLabel19);

        DTPCari1_Ralan.setEditable(false);
        DTPCari1_Ralan.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1_Ralan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-05-2018" }));
        DTPCari1_Ralan.setDisplayFormat("dd-MM-yyyy");
        DTPCari1_Ralan.setOpaque(false);
        DTPCari1_Ralan.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass11.add(DTPCari1_Ralan);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("s.d.");
        jLabel21.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass11.add(jLabel21);

        DTPCari2_Ralan.setEditable(false);
        DTPCari2_Ralan.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2_Ralan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-05-2018" }));
        DTPCari2_Ralan.setDisplayFormat("dd-MM-yyyy");
        DTPCari2_Ralan.setOpaque(false);
        DTPCari2_Ralan.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass11.add(DTPCari2_Ralan);

        jLabel6.setText("Key Word :");
        jLabel6.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass11.add(jLabel6);

        TCari_Ralan.setPreferredSize(new java.awt.Dimension(250, 23));
        TCari_Ralan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCari_RalanKeyPressed(evt);
            }
        });
        panelGlass11.add(TCari_Ralan);

        BtnCari_Ralan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari_Ralan.setMnemonic('1');
        BtnCari_Ralan.setToolTipText("Alt+1");
        BtnCari_Ralan.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari_Ralan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCari_RalanActionPerformed(evt);
            }
        });
        BtnCari_Ralan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCari_RalanKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BtnCari_RalanKeyReleased(evt);
            }
        });
        panelGlass11.add(BtnCari_Ralan);

        BtnAll_Ralan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll_Ralan.setMnemonic('2');
        BtnAll_Ralan.setToolTipText("Alt+2");
        BtnAll_Ralan.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAll_Ralan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAll_RalanActionPerformed(evt);
            }
        });
        BtnAll_Ralan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAll_RalanKeyPressed(evt);
            }
        });
        panelGlass11.add(BtnAll_Ralan);

        jLabel7.setText("Record :");
        jLabel7.setPreferredSize(new java.awt.Dimension(65, 23));
        panelGlass11.add(jLabel7);

        LCount_Ralan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount_Ralan.setText("0");
        LCount_Ralan.setPreferredSize(new java.awt.Dimension(50, 23));
        panelGlass11.add(LCount_Ralan);

        panelGlass10.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel11.setText("Waktu Diet :");
        jLabel11.setPreferredSize(new java.awt.Dimension(67, 23));
        panelGlass10.add(jLabel11);

        cmbJamCari_Ralan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Semua", "Pagi", "Siang", "Sore" }));
        cmbJamCari_Ralan.setOpaque(false);
        cmbJamCari_Ralan.setPreferredSize(new java.awt.Dimension(150, 23));
        cmbJamCari_Ralan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbJamCari_RalanKeyPressed(evt);
            }
        });
        panelGlass10.add(cmbJamCari_Ralan);

        jLabel13.setText("Status");
        jLabel13.setPreferredSize(new java.awt.Dimension(67, 23));
        panelGlass10.add(jLabel13);

        cmbStatus_Ralan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SEMUA", "BELUM", "PRINT" }));
        cmbStatus_Ralan.setOpaque(false);
        cmbStatus_Ralan.setPreferredSize(new java.awt.Dimension(150, 23));
        cmbStatus_Ralan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbStatus_RalanKeyPressed(evt);
            }
        });
        panelGlass10.add(cmbStatus_Ralan);

        javax.swing.GroupLayout internalFrame2Layout = new javax.swing.GroupLayout(internalFrame2);
        internalFrame2.setLayout(internalFrame2Layout);
        internalFrame2Layout.setHorizontalGroup(
            internalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Scroll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(internalFrame2Layout.createSequentialGroup()
                .addComponent(FormInput1, javax.swing.GroupLayout.PREFERRED_SIZE, 1355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(panelGlass8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelGlass11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelGlass10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        internalFrame2Layout.setVerticalGroup(
            internalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(internalFrame2Layout.createSequentialGroup()
                .addComponent(FormInput1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelGlass10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(panelGlass11, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(panelGlass8, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        panelBiasa2.add(internalFrame2, java.awt.BorderLayout.CENTER);

        tabPane.addTab("List Diet Ralan", panelBiasa2);

        panelBiasa3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelBiasa3MouseClicked(evt);
            }
        });
        panelBiasa3.setLayout(new java.awt.BorderLayout());

        internalFrame3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data Pemberian Diet ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame3.setPreferredSize(new java.awt.Dimension(1365, 656));

        Scroll1.setOpaque(true);
        Scroll1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Scroll1MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Scroll1MouseReleased(evt);
            }
        });

        tbPemberianDiet_Ranap.setAutoCreateRowSorter(true);
        tbPemberianDiet_Ranap.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbPemberianDiet_Ranap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbPemberianDiet_RanapMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tbPemberianDiet_RanapMouseEntered(evt);
            }
        });
        tbPemberianDiet_Ranap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbPemberianDiet_RanapKeyPressed(evt);
            }
        });
        Scroll1.setViewportView(tbPemberianDiet_Ranap);

        FormInput.setPreferredSize(new java.awt.Dimension(160, 77));
        FormInput.setLayout(null);

        jLabel15.setText("No.Rawat :");
        FormInput.add(jLabel15);
        jLabel15.setBounds(0, 12, 75, 23);

        TNoRw_Ranap.setHighlighter(null);
        TNoRw_Ranap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRw_RanapKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw_Ranap);
        TNoRw_Ranap.setBounds(78, 12, 150, 23);

        TPasien_Ranap.setEditable(false);
        TPasien_Ranap.setHighlighter(null);
        FormInput.add(TPasien_Ranap);
        TPasien_Ranap.setBounds(235, 12, 230, 23);

        DTPTgl_Ranap.setEditable(false);
        DTPTgl_Ranap.setForeground(new java.awt.Color(50, 70, 50));
        DTPTgl_Ranap.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-05-2018" }));
        DTPTgl_Ranap.setDisplayFormat("dd-MM-yyyy");
        DTPTgl_Ranap.setOpaque(false);
        DTPTgl_Ranap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPTgl_RanapKeyPressed(evt);
            }
        });
        FormInput.add(DTPTgl_Ranap);
        DTPTgl_Ranap.setBounds(78, 42, 125, 23);

        cmbJam_Ranap.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pagi", "Siang", "Sore" }));
        cmbJam_Ranap.setOpaque(false);
        cmbJam_Ranap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbJam_RanapKeyPressed(evt);
            }
        });
        FormInput.add(cmbJam_Ranap);
        cmbJam_Ranap.setBounds(205, 42, 100, 23);

        jLabel10.setText("Ekstra :");
        FormInput.add(jLabel10);
        jLabel10.setBounds(860, 10, 60, 23);

        Kamar_Ranap.setEditable(false);
        Kamar_Ranap.setHighlighter(null);
        FormInput.add(Kamar_Ranap);
        Kamar_Ranap.setBounds(470, 10, 40, 23);

        NmEkstra_Ranap.setHighlighter(null);
        FormInput.add(NmEkstra_Ranap);
        NmEkstra_Ranap.setBounds(930, 10, 210, 24);

        BtnSeekJenisDiet_Ranap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeekJenisDiet_Ranap.setMnemonic('X');
        BtnSeekJenisDiet_Ranap.setToolTipText("Alt+X");
        BtnSeekJenisDiet_Ranap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeekJenisDiet_RanapActionPerformed(evt);
            }
        });
        BtnSeekJenisDiet_Ranap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSeekJenisDiet_RanapKeyPressed(evt);
            }
        });
        FormInput.add(BtnSeekJenisDiet_Ranap);
        BtnSeekJenisDiet_Ranap.setBounds(820, 40, 28, 23);

        NmBentukMakanan_Ranap.setEditable(false);
        NmBentukMakanan_Ranap.setHighlighter(null);
        FormInput.add(NmBentukMakanan_Ranap);
        NmBentukMakanan_Ranap.setBounds(660, 10, 160, 23);

        BtnSeekBentukMakanan_Ranap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeekBentukMakanan_Ranap.setMnemonic('X');
        BtnSeekBentukMakanan_Ranap.setToolTipText("Alt+X");
        BtnSeekBentukMakanan_Ranap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeekBentukMakanan_RanapActionPerformed(evt);
            }
        });
        BtnSeekBentukMakanan_Ranap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSeekBentukMakanan_RanapKeyPressed(evt);
            }
        });
        FormInput.add(BtnSeekBentukMakanan_Ranap);
        BtnSeekBentukMakanan_Ranap.setBounds(820, 10, 28, 23);

        NmJenisDiet_Ranap.setEditable(false);
        NmJenisDiet_Ranap.setHighlighter(null);
        FormInput.add(NmJenisDiet_Ranap);
        NmJenisDiet_Ranap.setBounds(660, 40, 160, 24);

        jLabel24.setText("Tanggal :");
        FormInput.add(jLabel24);
        jLabel24.setBounds(0, 42, 75, 23);

        jLabel25.setText("Bentuk Maknan :");
        FormInput.add(jLabel25);
        jLabel25.setBounds(550, 10, 100, 23);

        jLabel26.setText("Jenis Diet :");
        FormInput.add(jLabel26);
        jLabel26.setBounds(550, 40, 100, 23);

        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnHapus_Ranap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus_Ranap.setMnemonic('H');
        BtnHapus_Ranap.setText("Hapus");
        BtnHapus_Ranap.setToolTipText("Alt+H");
        BtnHapus_Ranap.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus_Ranap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapus_RanapActionPerformed(evt);
            }
        });
        BtnHapus_Ranap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapus_RanapKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnHapus_Ranap);

        BtnEdit_Ranap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnEdit_Ranap.setMnemonic('G');
        BtnEdit_Ranap.setText("Ganti");
        BtnEdit_Ranap.setToolTipText("Alt+G");
        BtnEdit_Ranap.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnEdit_Ranap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEdit_RanapActionPerformed(evt);
            }
        });
        BtnEdit_Ranap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnEdit_RanapKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnEdit_Ranap);

        btnPrintRanap_All.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/PrinterSettings.png"))); // NOI18N
        btnPrintRanap_All.setMnemonic('P');
        btnPrintRanap_All.setText("Print");
        btnPrintRanap_All.setToolTipText("Alt+P");
        btnPrintRanap_All.setPreferredSize(new java.awt.Dimension(100, 30));
        btnPrintRanap_All.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintRanap_AllActionPerformed(evt);
            }
        });
        btnPrintRanap_All.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPrintRanap_AllKeyPressed(evt);
            }
        });
        panelGlass9.add(btnPrintRanap_All);

        BtnKeluar_Ranap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar_Ranap.setMnemonic('K');
        BtnKeluar_Ranap.setText("Keluar");
        BtnKeluar_Ranap.setToolTipText("Alt+K");
        BtnKeluar_Ranap.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar_Ranap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluar_RanapActionPerformed(evt);
            }
        });
        BtnKeluar_Ranap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluar_RanapKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnKeluar_Ranap);

        panelGlass14.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel20.setText("Tgl.Rawat :");
        jLabel20.setPreferredSize(new java.awt.Dimension(67, 23));
        panelGlass14.add(jLabel20);

        DTPCari3_Ranap.setEditable(false);
        DTPCari3_Ranap.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari3_Ranap.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-05-2018" }));
        DTPCari3_Ranap.setDisplayFormat("dd-MM-yyyy");
        DTPCari3_Ranap.setOpaque(false);
        DTPCari3_Ranap.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass14.add(DTPCari3_Ranap);

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("s.d.");
        jLabel22.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass14.add(jLabel22);

        DTPCari4_Ranap.setEditable(false);
        DTPCari4_Ranap.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari4_Ranap.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-05-2018" }));
        DTPCari4_Ranap.setDisplayFormat("dd-MM-yyyy");
        DTPCari4_Ranap.setOpaque(false);
        DTPCari4_Ranap.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass14.add(DTPCari4_Ranap);

        jLabel8.setText("Key Word :");
        jLabel8.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass14.add(jLabel8);

        TCari_Ranap.setPreferredSize(new java.awt.Dimension(250, 23));
        TCari_Ranap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCari_RanapKeyPressed(evt);
            }
        });
        panelGlass14.add(TCari_Ranap);

        BtnCari_Ranap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari_Ranap.setMnemonic('1');
        BtnCari_Ranap.setToolTipText("Alt+1");
        BtnCari_Ranap.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari_Ranap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCari_RanapActionPerformed(evt);
            }
        });
        BtnCari_Ranap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCari_RanapKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BtnCari_RanapKeyReleased(evt);
            }
        });
        panelGlass14.add(BtnCari_Ranap);

        BtnAll_Ranap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll_Ranap.setMnemonic('2');
        BtnAll_Ranap.setToolTipText("Alt+2");
        BtnAll_Ranap.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAll_Ranap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAll_RanapActionPerformed(evt);
            }
        });
        BtnAll_Ranap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAll_RanapKeyPressed(evt);
            }
        });
        panelGlass14.add(BtnAll_Ranap);

        jLabel9.setText("Record :");
        jLabel9.setPreferredSize(new java.awt.Dimension(65, 23));
        panelGlass14.add(jLabel9);

        LCount_Ranap.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount_Ranap.setText("0");
        LCount_Ranap.setPreferredSize(new java.awt.Dimension(50, 23));
        panelGlass14.add(LCount_Ranap);

        panelGlass13.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel14.setText("Waktu Diet :");
        jLabel14.setPreferredSize(new java.awt.Dimension(67, 23));
        panelGlass13.add(jLabel14);

        cmbJamCari_Ranap.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Semua", "Pagi", "Siang", "Sore" }));
        cmbJamCari_Ranap.setOpaque(false);
        cmbJamCari_Ranap.setPreferredSize(new java.awt.Dimension(150, 23));
        cmbJamCari_Ranap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbJamCari_RanapKeyPressed(evt);
            }
        });
        panelGlass13.add(cmbJamCari_Ranap);

        jLabel23.setText("Status");
        jLabel23.setPreferredSize(new java.awt.Dimension(67, 23));
        panelGlass13.add(jLabel23);

        cmbStatus_Ranap.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SEMUA", "BELUM", "PRINT" }));
        cmbStatus_Ranap.setOpaque(false);
        cmbStatus_Ranap.setPreferredSize(new java.awt.Dimension(150, 23));
        cmbStatus_Ranap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbStatus_RanapKeyPressed(evt);
            }
        });
        panelGlass13.add(cmbStatus_Ranap);

        jLabel12.setText("Bangsal :");
        jLabel12.setPreferredSize(new java.awt.Dimension(163, 23));
        panelGlass13.add(jLabel12);

        NmBangsalCari_Ranap.setEditable(false);
        NmBangsalCari_Ranap.setHighlighter(null);
        NmBangsalCari_Ranap.setPreferredSize(new java.awt.Dimension(310, 23));
        panelGlass13.add(NmBangsalCari_Ranap);

        BtnSeek3_Ranap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeek3_Ranap.setMnemonic('X');
        BtnSeek3_Ranap.setToolTipText("Alt+X");
        BtnSeek3_Ranap.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnSeek3_Ranap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeek3_RanapActionPerformed(evt);
            }
        });
        BtnSeek3_Ranap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSeek3_RanapKeyPressed(evt);
            }
        });
        panelGlass13.add(BtnSeek3_Ranap);

        javax.swing.GroupLayout internalFrame3Layout = new javax.swing.GroupLayout(internalFrame3);
        internalFrame3.setLayout(internalFrame3Layout);
        internalFrame3Layout.setHorizontalGroup(
            internalFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Scroll1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(internalFrame3Layout.createSequentialGroup()
                .addComponent(FormInput, javax.swing.GroupLayout.PREFERRED_SIZE, 1355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(panelGlass9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelGlass13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelGlass14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        internalFrame3Layout.setVerticalGroup(
            internalFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(internalFrame3Layout.createSequentialGroup()
                .addComponent(FormInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Scroll1, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelGlass13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelGlass14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelGlass9, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelBiasa3.add(internalFrame3, java.awt.BorderLayout.CENTER);

        tabPane.addTab("List Diet Ranap", panelBiasa3);

        internalFrame1.add(tabPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tbPemberianDiet_RalanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPemberianDiet_RalanMouseClicked
        if(tabMode_1.getRowCount()!=0){
            try {
                getData_Ralan();
            if(evt.getButton()==MouseEvent.BUTTON3)
            {
            popUpMenu_Ralan.show((Component) evt.getSource(), evt.getX(), evt.getY());
            }  
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbPemberianDiet_RalanMouseClicked

    private void tbPemberianDiet_RalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbPemberianDiet_RalanKeyPressed
        if(tabMode_1.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData_Ralan();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbPemberianDiet_RalanKeyPressed

    private void BtnHapus_RalanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapus_RalanActionPerformed
         if(tabMode_1.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
            DTPTgl_Ralan.requestFocus();
        }else if(TPasien_Ralan.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Gagal menghapus. Pilih dulu data yang mau dihapus.\nKlik data pada table untuk memilih...!!!!");
        }else if(!(TPasien_Ralan.getText().trim().equals(""))){
            try{
                Sequel.queryu("delete from detail_beri_diet_ralan " +
                        "where no_rawat='"+TNoRw_Ralan.getText()+"' " +
                        "and tanggal='"+Valid.SetTgl(DTPTgl_Ralan.getSelectedItem()+"")+"' " +
                        "and waktu='"+cmbJam_Ralan.getSelectedItem()+"'");
                
            tampil_Ralan();
            emptTeks_Ralan();
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih terlebih dulu data yang mau anda hapus...\n Klik data pada table untuk memilih data...!!!!");
            }
        }
    }//GEN-LAST:event_BtnHapus_RalanActionPerformed

    private void BtnHapus_RalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapus_RalanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapus_RalanActionPerformed(null);
        }
    }//GEN-LAST:event_BtnHapus_RalanKeyPressed

    private void BtnEdit_RalanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEdit_RalanActionPerformed
        if(TNoRw_Ralan.getText().trim().equals("")){
            Valid.textKosong(TNoRw_Ralan,"No Rawat");
        }else if(TPasien_Ralan.getText().trim().equals("")){
            Valid.textKosong(TPasien_Ralan,"Nama Pasien");
        }else if(NmBentukMakanan_Ralan.getText().trim().equals("")){
            Valid.textKosong(NmBentukMakanan_Ralan,"Bentuk Makanan");
        }else if(NmJenisDiet_Ralan.getText().trim().equals("")){
            Valid.textKosong(NmJenisDiet_Ralan,"Jenis Diet");
        }else if(NmEkstra_Ralan.getText().trim().equals("")){
            Valid.textKosong(NmEkstra_Ralan,"Ekstra");
        }else{
           EntDiet.setNo_rawat(TNoRw_Ralan.getText());
           EntDiet.setTanggal(Valid.SetTgl(DTPTgl_Ralan.getSelectedItem().toString()));
           EntDiet.setWaktu(cmbJam_Ralan.getSelectedItem().toString());
           EntDiet.setBentuk_makanan(NmBentukMakanan_Ralan.getText());
           EntDiet.setJenis_diet(NmJenisDiet_Ralan.getText());
           EntDiet.setEkstra(NmEkstra_Ralan.getText());  
           diet.editPemberianDiet_Ralan(EntDiet);
           
            if(tabMode_1.getRowCount()!=0){tampil_Ralan();}
            emptTeks_Ralan(); 
        }
    }//GEN-LAST:event_BtnEdit_RalanActionPerformed

    private void BtnEdit_RalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEdit_RalanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEdit_RalanActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus_Ralan, BtnKeluar_Ralan);
        }
    }//GEN-LAST:event_BtnEdit_RalanKeyPressed

    private void BtnKeluar_RalanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluar_RalanActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluar_RalanActionPerformed

    private void BtnKeluar_RalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluar_RalanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,BtnEdit_Ralan,TCari_Ralan);}
    }//GEN-LAST:event_BtnKeluar_RalanKeyPressed

    private void cmbJamCari_RalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbJamCari_RalanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbJamCari_RalanKeyPressed

    private void TCari_RalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCari_RalanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCari_RalanActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari_Ralan.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar_Ralan.requestFocus();
        }
    }//GEN-LAST:event_TCari_RalanKeyPressed

    private void BtnCari_RalanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCari_RalanActionPerformed
        tampil_Ralan();
    }//GEN-LAST:event_BtnCari_RalanActionPerformed

    private void BtnCari_RalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCari_RalanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCari_RalanActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari_Ralan, BtnAll_Ralan);
        }
    }//GEN-LAST:event_BtnCari_RalanKeyPressed

    private void BtnCari_RalanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCari_RalanKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCari_RalanKeyReleased

    private void BtnAll_RalanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAll_RalanActionPerformed
        emptTeks_Ralan();
        tampil_Ralan();
    }//GEN-LAST:event_BtnAll_RalanActionPerformed

    private void BtnAll_RalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAll_RalanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAll_RalanActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnCari_Ralan, TNoRw_Ralan);
        }
    }//GEN-LAST:event_BtnAll_RalanKeyPressed

    private void tbPemberianDiet_RanapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPemberianDiet_RanapMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData_Ranap();
            if(evt.getButton()==MouseEvent.BUTTON3){
            popUpMenu_Ranap.show((Component) evt.getSource(), evt.getX(), evt.getY());
            }
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbPemberianDiet_RanapMouseClicked

    private void tbPemberianDiet_RanapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbPemberianDiet_RanapKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData_Ranap();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbPemberianDiet_RanapKeyPressed

    private void BtnHapus_RanapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapus_RanapActionPerformed
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
            DTPTgl_Ranap.requestFocus();
        }else if(TPasien_Ranap.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Gagal menghapus. Pilih dulu data yang mau dihapus.\nKlik data pada table untuk memilih...!!!!");
        }else if(!(TPasien_Ranap.getText().trim().equals(""))){
            try{
                
                Sequel.queryu("delete from detail_beri_diet " +
                        "where no_rawat='"+TNoRw_Ranap.getText()+"' " +
                        "and tanggal='"+Valid.SetTgl(DTPTgl_Ranap.getSelectedItem()+"")+"' " +
                        "and waktu='"+cmbJam_Ranap.getSelectedItem()+"' " +
                        "and kd_kamar = '"+Kamar_Ranap.getText()+"'");
                tampil_Ranap();
                emptTeks_Ranap();
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih terlebih dulu data yang mau anda hapus...\n Klik data pada table untuk memilih data...!!!!");
            }
        }
    }//GEN-LAST:event_BtnHapus_RanapActionPerformed

    private void BtnHapus_RanapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapus_RanapKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapus_RanapActionPerformed(null);
        }
    }//GEN-LAST:event_BtnHapus_RanapKeyPressed

    private void BtnEdit_RanapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEdit_RanapActionPerformed
        if(TNoRw_Ranap.getText().trim().equals("")){
            Valid.textKosong(TNoRw_Ranap,"No Rawat");
        }else if(TPasien_Ranap.getText().trim().equals("")){
            Valid.textKosong(TPasien_Ranap,"Nama Pasien");
        }else if(NmBentukMakanan_Ranap.getText().trim().equals("")){
            Valid.textKosong(NmBentukMakanan_Ranap,"Bentuk Makanan");
        }else if(NmJenisDiet_Ranap.getText().trim().equals("")){
            Valid.textKosong(NmJenisDiet_Ranap,"Jenis Diet");
        }else if(NmEkstra_Ranap.getText().trim().equals("")){
            Valid.textKosong(NmEkstra_Ranap,"Ekstra");
        }else if(Kamar_Ranap.getText().trim().equals("")){
            Valid.textKosong(Kamar_Ranap,"Kamar");
        }else{
           EntDiet.setNo_rawat(TNoRw_Ranap.getText());
           EntDiet.setKd_kamar(Kamar_Ranap.getText());
           EntDiet.setTanggal(Valid.SetTgl(DTPTgl_Ranap.getSelectedItem().toString()));
           EntDiet.setWaktu(cmbJam_Ranap.getSelectedItem().toString());
           EntDiet.setBentuk_makanan(NmBentukMakanan_Ranap.getText());
           EntDiet.setJenis_diet(NmJenisDiet_Ranap.getText());
           EntDiet.setEkstra(NmEkstra_Ranap.getText());  
           diet.editPemberianDiet_Ranap(EntDiet);
           
            if(tabMode.getRowCount()!=0){tampil_Ranap();}
            emptTeks_Ranap(); 
        }
    }//GEN-LAST:event_BtnEdit_RanapActionPerformed

    private void BtnEdit_RanapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEdit_RanapKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEdit_RanapActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus_Ranap, BtnKeluar_Ranap);
        }
    }//GEN-LAST:event_BtnEdit_RanapKeyPressed

    private void BtnKeluar_RanapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluar_RanapActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluar_RanapActionPerformed

    private void BtnKeluar_RanapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluar_RanapKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,BtnEdit_Ranap,TCari_Ranap);}
    }//GEN-LAST:event_BtnKeluar_RanapKeyPressed

    private void cmbJamCari_RanapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbJamCari_RanapKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbJamCari_RanapKeyPressed

    private void BtnSeek3_RanapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeek3_RanapActionPerformed
        var.setform("DlgPemberianObat");
        bangsal.emptTeks();
        bangsal.isCek();
        bangsal.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        bangsal.setLocationRelativeTo(internalFrame1);
        bangsal.setVisible(true);
    }//GEN-LAST:event_BtnSeek3_RanapActionPerformed

    private void BtnSeek3_RanapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSeek3_RanapKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnSeek3_RanapKeyPressed

    private void TCari_RanapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCari_RanapKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCari_RanapActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari_Ranap.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar_Ranap.requestFocus();
        }
    }//GEN-LAST:event_TCari_RanapKeyPressed

    private void BtnCari_RanapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCari_RanapActionPerformed
        tampil_Ranap();
    }//GEN-LAST:event_BtnCari_RanapActionPerformed

    private void BtnCari_RanapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCari_RanapKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCari_RanapActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari_Ranap, BtnAll_Ralan);
        }
    }//GEN-LAST:event_BtnCari_RanapKeyPressed

    private void BtnCari_RanapKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCari_RanapKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCari_RanapKeyReleased

    private void BtnAll_RanapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAll_RanapActionPerformed
        emptTeks_Ranap();
        tampil_Ranap();
    }//GEN-LAST:event_BtnAll_RanapActionPerformed

    private void BtnAll_RanapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAll_RanapKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAll_RanapActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnCari_Ranap, TNoRw_Ranap);
        }
    }//GEN-LAST:event_BtnAll_RanapKeyPressed

    private void TNoRw_RanapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw_RanapKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat_Ranap();
        }else{
            Valid.pindah(evt,cmbJam_Ranap,NmJenisDiet_Ranap);
        }
    }//GEN-LAST:event_TNoRw_RanapKeyPressed

    private void DTPTgl_RanapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPTgl_RanapKeyPressed
        Valid.pindah(evt,TCari_Ranap,cmbJam_Ranap);
    }//GEN-LAST:event_DTPTgl_RanapKeyPressed

    private void cmbJam_RanapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbJam_RanapKeyPressed
        Valid.pindah(evt,DTPTgl_Ranap,NmJenisDiet_Ranap);
    }//GEN-LAST:event_cmbJam_RanapKeyPressed

    private void BtnSeekJenisDiet_RanapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeekJenisDiet_RanapActionPerformed
        // TODO add your handling code here:
        var.setform("DlgPemberianObat");
        jDiet.emptTeks();
        jDiet.isCek();
        jDiet.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        jDiet.setLocationRelativeTo(internalFrame1);
        jDiet.setVisible(true);
    }//GEN-LAST:event_BtnSeekJenisDiet_RanapActionPerformed

    private void BtnSeekJenisDiet_RanapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSeekJenisDiet_RanapKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnSeekJenisDiet_RanapKeyPressed

    private void BtnSeekBentukMakanan_RanapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeekBentukMakanan_RanapActionPerformed
        // TODO add your handling code here:
        var.setform("DlgPemberianObat");
        bMakanan.emptTeks();
        bMakanan.isCek();
        bMakanan.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        bMakanan.setLocationRelativeTo(internalFrame1);
        bMakanan.setVisible(true);
    }//GEN-LAST:event_BtnSeekBentukMakanan_RanapActionPerformed

    private void BtnSeekBentukMakanan_RanapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSeekBentukMakanan_RanapKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnSeekBentukMakanan_RanapKeyPressed

    private void TNoRw_RalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRw_RalanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat_Ralan();
        }else{
            Valid.pindah(evt,cmbJam_Ralan,NmJenisDiet_Ralan);
        }
    }//GEN-LAST:event_TNoRw_RalanKeyPressed

    private void DTPTgl_RalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPTgl_RalanKeyPressed
        Valid.pindah(evt,TCari_Ralan,cmbJam_Ranap);
    }//GEN-LAST:event_DTPTgl_RalanKeyPressed

    private void cmbJam_RalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbJam_RalanKeyPressed
        Valid.pindah(evt,DTPTgl_Ranap,NmJenisDiet_Ranap);
    }//GEN-LAST:event_cmbJam_RalanKeyPressed

    private void BtnSeekJenisDiet_RalanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeekJenisDiet_RalanActionPerformed
        // TODO add your handling code here:
        var.setform("DlgPemberianObat");
        jDiet.emptTeks();
        jDiet.isCek();
        jDiet.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        jDiet.setLocationRelativeTo(internalFrame1);
        jDiet.setVisible(true);
    }//GEN-LAST:event_BtnSeekJenisDiet_RalanActionPerformed

    private void BtnSeekJenisDiet_RalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSeekJenisDiet_RalanKeyPressed
        // TODO add your handling code here:
        //Valid.pindah(evt,NmJenisDiet_Ranap,BtnSimpan);
    }//GEN-LAST:event_BtnSeekJenisDiet_RalanKeyPressed

    private void BtnSeekBentukMakanan_RalanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeekBentukMakanan_RalanActionPerformed
        // TODO add your handling code here:
        var.setform("DlgPemberianObat");
        bMakanan.emptTeks();
        bMakanan.isCek();
        bMakanan.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        bMakanan.setLocationRelativeTo(internalFrame1);
        bMakanan.setVisible(true);
    }//GEN-LAST:event_BtnSeekBentukMakanan_RalanActionPerformed

    private void BtnSeekBentukMakanan_RalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSeekBentukMakanan_RalanKeyPressed
        // TODO add your handling code here:
        //Valid.pindah(evt,NmBentukMakanan_Ranap,BtnSimpan);
    }//GEN-LAST:event_BtnSeekBentukMakanan_RalanKeyPressed

    private void Scroll1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Scroll1MouseReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_Scroll1MouseReleased

    private void print_RanapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print_RanapActionPerformed
     if(TPasien_Ranap.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu pasien...!!!");
        }else{
            Map<String, Object> param = new HashMap<>();
            param.put("namars",var.getnamars());
            param.put("alamatrs",var.getalamatrs());
            param.put("kotars",var.getkabupatenrs());
            param.put("propinsirs",var.getpropinsirs());
            param.put("kontakrs",var.getkontakrs());
            param.put("emailrs",var.getemailrs());
            param.put("logo",Sequel.cariGambar("select logo from setting"));
            Valid.MyReport("rptDietRanap.jrxml","report","::[ print Label Diet Ranap ]::",
                "select detail_beri_diet.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, pasien.tgl_lahir, " +
                "concat(detail_beri_diet.kd_kamar,', ',bangsal.nm_bangsal) as kamar ,detail_beri_diet.tanggal,detail_beri_diet.waktu, " +
                "detail_beri_diet.Bentuk_makanan, detail_beri_diet.jenis_diet, detail_beri_diet.ekstra " +
                "from detail_beri_diet inner join reg_periksa inner join pasien inner join kamar inner join bangsal " +
                "on detail_beri_diet.no_rawat=reg_periksa.no_rawat " +
                "and detail_beri_diet.kd_kamar=kamar.kd_kamar " +
                "and kamar.kd_bangsal=bangsal.kd_bangsal " +
                "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                "where detail_beri_diet.tanggal='"+Valid.SetTgl(DTPTgl_Ranap.getSelectedItem()+"")+"' and detail_beri_diet.waktu='"+cmbJam_Ranap.getSelectedItem()+"' "+
                "and detail_beri_diet.no_rawat='"+TNoRw_Ranap.getText()+"' and detail_beri_diet.kd_kamar ='"+Kamar_Ranap.getText()+"' "+
                "and detail_beri_diet.jenis_diet='"+NmJenisDiet_Ranap.getText()+"'",param);
            diet.ubahStatusRanap_print(Valid.SetTgl(DTPTgl_Ranap.getSelectedItem()+""), 
                    cmbJam_Ranap.getSelectedItem().toString(), TNoRw_Ranap.getText(), Kamar_Ranap.getText(), NmJenisDiet_Ranap.getText());
            tampil_Ranap();
        }
    }//GEN-LAST:event_print_RanapActionPerformed

    private void ScrollMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ScrollMouseClicked
        // TODO add your handling code here:
        if(evt.getButton()==MouseEvent.BUTTON3){
            popUpMenu_Ralan.show((Component) evt.getSource(), evt.getX(), evt.getY());
        }        
    }//GEN-LAST:event_ScrollMouseClicked

    private void tbPemberianDiet_RanapMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPemberianDiet_RanapMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tbPemberianDiet_RanapMouseEntered

    private void Scroll1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Scroll1MouseClicked
        // TODO add your handling code here:
        if(evt.getButton()==MouseEvent.BUTTON3){
            popUpMenu_Ranap.show((Component) evt.getSource(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_Scroll1MouseClicked

    private void Print_RalanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Print_RalanActionPerformed
        // TODO add your handling code here:
        if(TPasien_Ralan.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu pasien...!!!");
        }else{
            Map<String, Object> param = new HashMap<>();
            param.put("namars",var.getnamars());
            param.put("alamatrs",var.getalamatrs());
            param.put("kotars",var.getkabupatenrs());
            param.put("propinsirs",var.getpropinsirs());
            param.put("kontakrs",var.getkontakrs());
            param.put("emailrs",var.getemailrs());
            param.put("logo",Sequel.cariGambar("select logo from setting"));
            Valid.MyReport("rptDietRalan.jrxml","report","::[ print Label Diet Ralan ]::",
                "select detail_beri_diet_ralan.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, pasien.tgl_lahir, " +
                "detail_beri_diet_ralan.tanggal,detail_beri_diet_ralan.waktu, " +
                "detail_beri_diet_ralan.bentuk_makanan, detail_beri_diet_ralan.jenis_diet, detail_beri_diet_ralan.ekstra " +
                "from detail_beri_diet_ralan inner join reg_periksa inner join pasien " +
                "on detail_beri_diet_ralan.no_rawat=reg_periksa.no_rawat  " +
                "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                "where detail_beri_diet_Ralan.tanggal='"+Valid.SetTgl(DTPTgl_Ralan.getSelectedItem()+"")+"' and detail_beri_diet_Ralan.waktu='"+cmbJam_Ralan.getSelectedItem()+"' "+
                "and detail_beri_diet_Ralan.no_rawat='"+TNoRw_Ralan.getText()+"' and detail_beri_diet_Ralan.jenis_diet='"+NmJenisDiet_Ralan.getText()+"'",param);
        diet.ubahStatusRalan_print(Valid.SetTgl(DTPTgl_Ralan.getSelectedItem()+""), cmbJam_Ralan.getSelectedItem().toString(), TNoRw_Ralan.getText(), NmJenisDiet_Ralan.getText());
        tampil_Ralan();
        }
    }//GEN-LAST:event_Print_RalanActionPerformed

    private void BtnPrint_RalanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrint_RalanActionPerformed
        // TODO add your handling code here:
        if(cmbJamCari_Ralan.getSelectedItem().toString().trim().equals("Semua") && 
           cmbStatus_Ralan.getSelectedItem().toString().trim().equals("SEMUA")){
            
            Map<String, Object> param = new HashMap<>();
            param.put("namars",var.getnamars());
            param.put("alamatrs",var.getalamatrs());
            param.put("kotars",var.getkabupatenrs());
            param.put("propinsirs",var.getpropinsirs());
            param.put("kontakrs",var.getkontakrs());
            param.put("emailrs",var.getemailrs());
            param.put("logo",Sequel.cariGambar("select logo from setting"));
            Valid.MyReport("rptDietRalan.jrxml","report","::[ print Label Diet Ralan ]::",
                "select detail_beri_diet_ralan.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, pasien.tgl_lahir, " +
                "detail_beri_diet_ralan.tanggal,detail_beri_diet_ralan.waktu, " +
                "detail_beri_diet_ralan.bentuk_makanan, detail_beri_diet_ralan.jenis_diet, detail_beri_diet_ralan.ekstra " +
                "from detail_beri_diet_ralan inner join reg_periksa inner join pasien " +
                "on detail_beri_diet_ralan.no_rawat=reg_periksa.no_rawat  " +
                "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                "where detail_beri_diet_Ralan.tanggal between '"+Valid.SetTgl(DTPCari1_Ralan.getSelectedItem()+"")+"' " +
                "and '"+Valid.SetTgl(DTPCari2_Ralan.getSelectedItem()+"")+"'",param);
        
            diet.ubahStatusRalan_All(Valid.SetTgl(DTPCari1_Ralan.getSelectedItem().toString()), Valid.SetTgl(DTPCari2_Ralan.getSelectedItem().toString()));
        
        }else if(cmbJamCari_Ralan.getSelectedItem().toString().trim().equals("Semua")){
            
            Map<String, Object> param = new HashMap<>();
            param.put("namars",var.getnamars());
            param.put("alamatrs",var.getalamatrs());
            param.put("kotars",var.getkabupatenrs());
            param.put("propinsirs",var.getpropinsirs());
            param.put("kontakrs",var.getkontakrs());
            param.put("emailrs",var.getemailrs());
            param.put("logo",Sequel.cariGambar("select logo from setting"));
            Valid.MyReport("rptDietRalan.jrxml","report","::[ print Label Diet Ralan ]::",
                "select detail_beri_diet_ralan.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, pasien.tgl_lahir, " +
                "detail_beri_diet_ralan.tanggal,detail_beri_diet_ralan.waktu, " +
                "detail_beri_diet_ralan.bentuk_makanan, detail_beri_diet_ralan.jenis_diet, detail_beri_diet_ralan.ekstra " +
                "from detail_beri_diet_ralan inner join reg_periksa inner join pasien " +
                "on detail_beri_diet_ralan.no_rawat=reg_periksa.no_rawat  " +
                "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                "where detail_beri_diet_Ralan.tanggal between '"+Valid.SetTgl(DTPCari1_Ralan.getSelectedItem()+"")+"' " +
                "and '"+Valid.SetTgl(DTPCari2_Ralan.getSelectedItem()+"")+"' " +
                "and detail_beri_diet_Ralan.status = '"+cmbStatus_Ralan.getSelectedItem().toString()+"'",param);
        
            diet.ubahStatusRalan_status(Valid.SetTgl(DTPCari1_Ralan.getSelectedItem().toString()), Valid.SetTgl(DTPCari2_Ralan.getSelectedItem().toString()), 
                    cmbStatus_Ralan.getSelectedItem().toString());
            
        }else if(cmbStatus_Ralan.getSelectedItem().toString().trim().equals("SEMUA")){
        
            Map<String, Object> param = new HashMap<>();
            param.put("namars",var.getnamars());
            param.put("alamatrs",var.getalamatrs());
            param.put("kotars",var.getkabupatenrs());
            param.put("propinsirs",var.getpropinsirs());
            param.put("kontakrs",var.getkontakrs());
            param.put("emailrs",var.getemailrs());
            param.put("logo",Sequel.cariGambar("select logo from setting"));
            Valid.MyReport("rptDietRalan.jrxml","report","::[ print Label Diet Ralan ]::",
                "select detail_beri_diet_ralan.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, pasien.tgl_lahir, " +
                "detail_beri_diet_ralan.tanggal,detail_beri_diet_ralan.waktu, " +
                "detail_beri_diet_ralan.bentuk_makanan, detail_beri_diet_ralan.jenis_diet, detail_beri_diet_ralan.ekstra " +
                "from detail_beri_diet_ralan inner join reg_periksa inner join pasien " +
                "on detail_beri_diet_ralan.no_rawat=reg_periksa.no_rawat  " +
                "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                "where detail_beri_diet_Ralan.tanggal between '"+Valid.SetTgl(DTPCari1_Ralan.getSelectedItem()+"")+"' " +
                "and '"+Valid.SetTgl(DTPCari2_Ralan.getSelectedItem()+"")+"' " +
                "and detail_beri_diet_Ralan.Waktu = '"+cmbJamCari_Ralan.getSelectedItem().toString()+"'",param);
            diet.ubahStatusRalan_Waktu(Valid.SetTgl(DTPCari1_Ralan.getSelectedItem()+""), Valid.SetTgl(DTPCari2_Ralan.getSelectedItem()+""), 
                    cmbJamCari_Ralan.getSelectedItem().toString());
  
        }else{
            
            Map<String, Object> param = new HashMap<>();
            param.put("namars",var.getnamars());
            param.put("alamatrs",var.getalamatrs());
            param.put("kotars",var.getkabupatenrs());
            param.put("propinsirs",var.getpropinsirs());
            param.put("kontakrs",var.getkontakrs());
            param.put("emailrs",var.getemailrs());
            param.put("logo",Sequel.cariGambar("select logo from setting"));
            Valid.MyReport("rptDietRalan.jrxml","report","::[ print Label Diet Ralan ]::",
                "select detail_beri_diet_ralan.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, pasien.tgl_lahir, " +
                "detail_beri_diet_ralan.tanggal,detail_beri_diet_ralan.waktu, " +
                "detail_beri_diet_ralan.bentuk_makanan, detail_beri_diet_ralan.jenis_diet, detail_beri_diet_ralan.ekstra " +
                "from detail_beri_diet_ralan inner join reg_periksa inner join pasien " +
                "on detail_beri_diet_ralan.no_rawat=reg_periksa.no_rawat  " +
                "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                "where detail_beri_diet_Ralan.tanggal between '"+Valid.SetTgl(DTPCari1_Ralan.getSelectedItem()+"")+"' " +
                "and '"+Valid.SetTgl(DTPCari2_Ralan.getSelectedItem()+"")+"' " +
                "and detail_beri_diet_Ralan.waktu='"+cmbJamCari_Ralan.getSelectedItem()+"' " +
                "and detail_beri_diet_Ralan.status = '"+cmbStatus_Ralan.getSelectedItem().toString()+"'",param);
            
            diet.ubahStatusRalan_StatusWaktu(Valid.SetTgl(DTPCari1_Ralan.getSelectedItem().toString()), 
                    Valid.SetTgl(DTPCari2_Ralan.getSelectedItem().toString()), cmbJamCari_Ralan.getSelectedItem().toString(),
                    cmbStatus_Ranap.getSelectedItem().toString());
        
        }
        tampil_Ralan();
    }//GEN-LAST:event_BtnPrint_RalanActionPerformed

    private void BtnPrint_RalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrint_RalanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnPrint_RalanKeyPressed

    private void btnPrintRanap_AllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintRanap_AllActionPerformed
        // TODO add your handling code here:
        if(cmbJamCari_Ranap.getSelectedItem().toString().trim().equals("Semua") &&
           cmbStatus_Ranap.getSelectedItem().toString().trim().equals("SEMUA")){
            Map<String, Object> param = new HashMap<>();
            param.put("namars",var.getnamars());
            param.put("alamatrs",var.getalamatrs());
            param.put("kotars",var.getkabupatenrs());
            param.put("propinsirs",var.getpropinsirs());
            param.put("kontakrs",var.getkontakrs());
            param.put("emailrs",var.getemailrs());
            param.put("logo",Sequel.cariGambar("select logo from setting"));
            Valid.MyReport("rptDietRanap.jrxml","report","::[ print Label Diet Ranap ]::",
                "select detail_beri_diet.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, pasien.tgl_lahir, " +
                "concat(detail_beri_diet.kd_kamar,', ',bangsal.nm_bangsal) as kamar ,detail_beri_diet.tanggal,detail_beri_diet.waktu, " +
                "detail_beri_diet.Bentuk_makanan, detail_beri_diet.jenis_diet, detail_beri_diet.ekstra " +
                "from detail_beri_diet inner join reg_periksa inner join pasien inner join kamar inner join bangsal " +
                "on detail_beri_diet.no_rawat=reg_periksa.no_rawat " +
                "and detail_beri_diet.kd_kamar=kamar.kd_kamar " +
                "and kamar.kd_bangsal=bangsal.kd_bangsal " +
                "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                "where detail_beri_diet.tanggal between '"+Valid.SetTgl(DTPCari3_Ranap.getSelectedItem()+"")+"' " +
                "and '"+Valid.SetTgl(DTPCari4_Ranap.getSelectedItem()+"")+"' and sts_Pulang = '-' ",param); 
            
            diet.ubahStatusRanap_All(Valid.SetTgl(DTPCari3_Ranap.getSelectedItem()+""), Valid.SetTgl(DTPCari4_Ranap.getSelectedItem()+""));
      
        }else if(cmbJamCari_Ranap.getSelectedItem().toString().trim().equals("Semua")){
            Map<String, Object> param = new HashMap<>();
            param.put("namars",var.getnamars());
            param.put("alamatrs",var.getalamatrs());
            param.put("kotars",var.getkabupatenrs());
            param.put("propinsirs",var.getpropinsirs());
            param.put("kontakrs",var.getkontakrs());
            param.put("emailrs",var.getemailrs());
            param.put("logo",Sequel.cariGambar("select logo from setting"));
            Valid.MyReport("rptDietRanap.jrxml","report","::[ print Label Diet Ranap ]::",
                "select detail_beri_diet.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, pasien.tgl_lahir, " +
                "concat(detail_beri_diet.kd_kamar,', ',bangsal.nm_bangsal) as kamar ,detail_beri_diet.tanggal,detail_beri_diet.waktu, " +
                "detail_beri_diet.Bentuk_makanan, detail_beri_diet.jenis_diet, detail_beri_diet.ekstra " +
                "from detail_beri_diet inner join reg_periksa inner join pasien inner join kamar inner join bangsal " +
                "on detail_beri_diet.no_rawat=reg_periksa.no_rawat " +
                "and detail_beri_diet.kd_kamar=kamar.kd_kamar " +
                "and kamar.kd_bangsal=bangsal.kd_bangsal " +
                "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                "where detail_beri_diet.tanggal between '"+Valid.SetTgl(DTPCari3_Ranap.getSelectedItem()+"")+"' " +
                "and '"+Valid.SetTgl(DTPCari4_Ranap.getSelectedItem()+"")+"' " +
                "and detail_beri_diet.Status = '"+cmbStatus_Ranap.getSelectedItem().toString()+"'",param); 
        
            diet.ubahStatusRanap_status(Valid.SetTgl(DTPCari3_Ranap.getSelectedItem()+""), Valid.SetTgl(DTPCari4_Ranap.getSelectedItem()+""), 
                    cmbStatus_Ranap.getSelectedItem().toString());
        }else if(cmbStatus_Ranap.getSelectedItem().toString().trim().equals("SEMUA")){
            Map<String, Object> param = new HashMap<>();
            param.put("namars",var.getnamars());
            param.put("alamatrs",var.getalamatrs());
            param.put("kotars",var.getkabupatenrs());
            param.put("propinsirs",var.getpropinsirs());
            param.put("kontakrs",var.getkontakrs());
            param.put("emailrs",var.getemailrs());
            param.put("logo",Sequel.cariGambar("select logo from setting"));
            Valid.MyReport("rptDietRanap.jrxml","report","::[ print Label Diet Ranap ]::",
                "select detail_beri_diet.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, pasien.tgl_lahir, " +
                "concat(detail_beri_diet.kd_kamar,', ',bangsal.nm_bangsal) as kamar ,detail_beri_diet.tanggal,detail_beri_diet.waktu, " +
                "detail_beri_diet.Bentuk_makanan, detail_beri_diet.jenis_diet, detail_beri_diet.ekstra " +
                "from detail_beri_diet inner join reg_periksa inner join pasien inner join kamar inner join bangsal " +
                "on detail_beri_diet.no_rawat=reg_periksa.no_rawat " +
                "and detail_beri_diet.kd_kamar=kamar.kd_kamar " +
                "and kamar.kd_bangsal=bangsal.kd_bangsal " +
                "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                "where detail_beri_diet.tanggal between '"+Valid.SetTgl(DTPCari3_Ranap.getSelectedItem()+"")+"' " +
                "and '"+Valid.SetTgl(DTPCari4_Ranap.getSelectedItem()+"")+"' " +
                "and detail_beri_diet.Waktu = '"+cmbJamCari_Ranap.getSelectedItem().toString()+"'",param); 
                    
            diet.ubahStatusRanap_Waktu(Valid.SetTgl(DTPCari3_Ranap.getSelectedItem()+""), Valid.SetTgl(DTPCari4_Ranap.getSelectedItem()+""),
                    cmbJamCari_Ranap.getSelectedItem().toString());
        }else{
            Map<String, Object> param = new HashMap<>();
            param.put("namars",var.getnamars());
            param.put("alamatrs",var.getalamatrs());
            param.put("kotars",var.getkabupatenrs());
            param.put("propinsirs",var.getpropinsirs());
            param.put("kontakrs",var.getkontakrs());
            param.put("emailrs",var.getemailrs());
            param.put("logo",Sequel.cariGambar("select logo from setting"));
            Valid.MyReport("rptDietRanap.jrxml","report","::[ print Label Diet Ranap ]::",
                "select detail_beri_diet.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, pasien.tgl_lahir, " +
                "concat(detail_beri_diet.kd_kamar,', ',bangsal.nm_bangsal) as kamar ,detail_beri_diet.tanggal,detail_beri_diet.waktu, " +
                "detail_beri_diet.Bentuk_makanan, detail_beri_diet.jenis_diet, detail_beri_diet.ekstra " +
                "from detail_beri_diet inner join reg_periksa inner join pasien inner join kamar inner join bangsal " +
                "on detail_beri_diet.no_rawat=reg_periksa.no_rawat " +
                "and detail_beri_diet.kd_kamar=kamar.kd_kamar " +
                "and kamar.kd_bangsal=bangsal.kd_bangsal " +
                "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                "where detail_beri_diet.tanggal between '"+Valid.SetTgl(DTPCari3_Ranap.getSelectedItem()+"")+"' " +
                "and '"+Valid.SetTgl(DTPCari4_Ranap.getSelectedItem()+"")+"' " +
                "and detail_beri_diet.Waktu = '"+cmbJamCari_Ranap.getSelectedItem().toString()+"' " +
                "and detail_beri_diet.Status = '"+cmbStatus_Ranap.getSelectedItem().toString()+"'",param); 
                       
            diet.ubahStatusRanap_StatusWaktu(Valid.SetTgl(DTPCari3_Ranap.getSelectedItem()+""), Valid.SetTgl(DTPCari4_Ranap.getSelectedItem()+""),
                    cmbJamCari_Ranap.getSelectedItem().toString(), cmbStatus_Ranap.getSelectedItem().toString());
        }
        
        tampil_Ranap();
    }//GEN-LAST:event_btnPrintRanap_AllActionPerformed

    private void btnPrintRanap_AllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPrintRanap_AllKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintRanap_AllKeyPressed

    private void cmbStatus_RalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbStatus_RalanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbStatus_RalanKeyPressed

    private void cmbStatus_RanapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbStatus_RanapKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbStatus_RanapKeyPressed

    private void TPasien_RalanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TPasien_RalanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TPasien_RalanActionPerformed

    private void tabPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabPaneMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_tabPaneMouseClicked

    private void panelBiasa2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelBiasa2MouseClicked
        // TODO add your handling code here:
                tampil_Ralan();
    }//GEN-LAST:event_panelBiasa2MouseClicked

    private void panelBiasa3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelBiasa3MouseClicked
        // TODO add your handling code here:
                tampil_Ranap();
    }//GEN-LAST:event_panelBiasa3MouseClicked

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
            java.util.logging.Logger.getLogger(DlgLihatDiet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(DlgLihatDiet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(DlgLihatDiet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(DlgLihatDiet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                DlgLihatDiet dialog = new DlgLihatDiet(new javax.swing.JFrame(), true);
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
    private widget.Button BtnAll_Ralan;
    private widget.Button BtnAll_Ranap;
    private widget.Button BtnCari_Ralan;
    private widget.Button BtnCari_Ranap;
    private widget.Button BtnEdit_Ralan;
    private widget.Button BtnEdit_Ranap;
    private widget.Button BtnHapus_Ralan;
    private widget.Button BtnHapus_Ranap;
    private widget.Button BtnKeluar_Ralan;
    private widget.Button BtnKeluar_Ranap;
    private widget.Button BtnPrint_Ralan;
    private widget.Button BtnSeek3_Ranap;
    private widget.Button BtnSeekBentukMakanan_Ralan;
    private widget.Button BtnSeekBentukMakanan_Ranap;
    private widget.Button BtnSeekJenisDiet_Ralan;
    private widget.Button BtnSeekJenisDiet_Ranap;
    private widget.Tanggal DTPCari1_Ralan;
    private widget.Tanggal DTPCari2_Ralan;
    private widget.Tanggal DTPCari3_Ranap;
    private widget.Tanggal DTPCari4_Ranap;
    private widget.Tanggal DTPTgl_Ralan;
    private widget.Tanggal DTPTgl_Ranap;
    private widget.PanelBiasa FormInput;
    private widget.PanelBiasa FormInput1;
    private widget.TextBox Kamar_Ranap;
    private widget.Label LCount_Ralan;
    private widget.Label LCount_Ranap;
    private widget.TextBox NmBangsalCari_Ranap;
    private widget.TextBox NmBentukMakanan_Ralan;
    private widget.TextBox NmBentukMakanan_Ranap;
    private widget.TextBox NmEkstra_Ralan;
    private widget.TextBox NmEkstra_Ranap;
    private widget.TextBox NmJenisDiet_Ralan;
    private widget.TextBox NmJenisDiet_Ranap;
    private javax.swing.JMenuItem Print_Ralan;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll1;
    private widget.TextBox TCari_Ralan;
    private widget.TextBox TCari_Ranap;
    private widget.TextBox TNoRw_Ralan;
    private widget.TextBox TNoRw_Ranap;
    private widget.TextBox TPasien_Ralan;
    private widget.TextBox TPasien_Ranap;
    private widget.Button btnPrintRanap_All;
    private widget.ComboBox cmbJamCari_Ralan;
    private widget.ComboBox cmbJamCari_Ranap;
    private widget.ComboBox cmbJam_Ralan;
    private widget.ComboBox cmbJam_Ranap;
    private widget.ComboBox cmbStatus_Ralan;
    private widget.ComboBox cmbStatus_Ranap;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame3;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel17;
    private widget.Label jLabel19;
    private widget.Label jLabel20;
    private widget.Label jLabel21;
    private widget.Label jLabel22;
    private widget.Label jLabel23;
    private widget.Label jLabel24;
    private widget.Label jLabel25;
    private widget.Label jLabel26;
    private widget.Label jLabel27;
    private widget.Label jLabel28;
    private widget.Label jLabel29;
    private widget.Label jLabel4;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private widget.Label jLabel9;
    private widget.PanelBiasa panelBiasa2;
    private widget.PanelBiasa panelBiasa3;
    private widget.panelisi panelGlass10;
    private widget.panelisi panelGlass11;
    private widget.panelisi panelGlass13;
    private widget.panelisi panelGlass14;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private javax.swing.JPopupMenu popUpMenu_Ralan;
    private javax.swing.JPopupMenu popUpMenu_Ranap;
    private javax.swing.JMenuItem print_Ranap;
    private widget.TabPane tabPane;
    private widget.Table tbPemberianDiet_Ralan;
    private widget.Table tbPemberianDiet_Ranap;
    // End of variables declaration//GEN-END:variables

    // </editor-fold>
    
     private void tampil_Ranap() {
        try{
            Valid.tabelKosong(tabMode);
            ps1.setString(1,Valid.SetTgl(DTPCari3_Ranap.getSelectedItem()+""));
            ps1.setString(2,Valid.SetTgl(DTPCari4_Ranap.getSelectedItem()+""));
            ps1.setString(3,"%"+cmbJamCari_Ranap.getSelectedItem().toString().replaceAll("Semua","").trim()+"%");
            ps1.setString(4,"%"+NmBangsalCari_Ranap.getText().trim()+"%");
            ps1.setString(5,"%"+TCari_Ranap.getText().trim()+"%");
            ps1.setString(6,"%"+cmbStatus_Ranap.getSelectedItem().toString().replaceAll("SEMUA","").trim()+"%");
            ps1.setString(7,Valid.SetTgl(DTPCari3_Ranap.getSelectedItem()+""));
            ps1.setString(8,Valid.SetTgl(DTPCari4_Ranap.getSelectedItem()+""));
            ps1.setString(9,"%"+cmbJamCari_Ranap.getSelectedItem().toString().replaceAll("Semua","").trim()+"%");
            ps1.setString(10,"%"+NmBangsalCari_Ranap.getText().trim()+"%");
            ps1.setString(11,"%"+TCari_Ranap.getText().trim()+"%");
            ps1.setString(12,"%"+cmbStatus_Ranap.getSelectedItem().toString().replaceAll("SEMUA","").trim()+"%");
            ps1.setString(13,Valid.SetTgl(DTPCari3_Ranap.getSelectedItem()+""));
            ps1.setString(14,Valid.SetTgl(DTPCari4_Ranap.getSelectedItem()+""));
            ps1.setString(15,"%"+cmbJamCari_Ranap.getSelectedItem().toString().replaceAll("Semua","").trim()+"%");
            ps1.setString(16,"%"+NmBangsalCari_Ranap.getText().trim()+"%");
            ps1.setString(17,"%"+TCari_Ranap.getText().trim()+"%");
            ps1.setString(18,"%"+cmbStatus_Ranap.getSelectedItem().toString().replaceAll("SEMUA","").trim()+"%");
            
           rs=ps1.executeQuery();
            while(rs.next()){
                tabMode.addRow(new Object[]{
                rs.getString(1),rs.getString(2)+", "+rs.getString(3),
                    rs.getString(4),rs.getString(5),rs.getString(6),
                    Sequel.cariIsi("select diagnosa_awal from kamar_inap where no_rawat=? order by tgl_masuk desc",rs.getString(1)),
                    rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10), rs.getString(11)
                });
            }
        }catch(SQLException e){
            System.out.println("Notifikasi : "+e);
        }
        LCount_Ranap.setText(""+tabMode.getRowCount());
    }

    public void emptTeks_Ranap() {
        TNoRw_Ranap.setText("");
        TPasien_Ranap.setText("");
        TCari_Ranap.setText("");
        Kamar_Ranap.setText("");
        NmBentukMakanan_Ranap.setText("");
        NmJenisDiet_Ranap.setText("");
        NmEkstra_Ranap.setText("");
        TNoRw_Ranap.requestFocus();
        TPasien_Ranap.requestFocus();
        Kamar_Ranap.requestFocus();
        NmBentukMakanan_Ranap.requestFocus();
        NmJenisDiet_Ranap.requestFocus();
        NmEkstra_Ranap.requestFocus();
    }

    private void getData_Ranap() {
        if(tbPemberianDiet_Ranap.getSelectedRow()!= -1){
            TNoRw_Ranap.setText(tbPemberianDiet_Ranap.getValueAt(tbPemberianDiet_Ranap.getSelectedRow(),0).toString()); 
            isRawat_Ranap();            
            cmbJam_Ranap.setSelectedItem(tbPemberianDiet_Ranap.getValueAt(tbPemberianDiet_Ranap.getSelectedRow(),4).toString());
            NmBentukMakanan_Ranap.setText(tbPemberianDiet_Ranap.getValueAt(tbPemberianDiet_Ranap.getSelectedRow(),6).toString());
            NmJenisDiet_Ranap.setText(tbPemberianDiet_Ranap.getValueAt(tbPemberianDiet_Ranap.getSelectedRow(),7).toString());
            NmEkstra_Ranap.setText(tbPemberianDiet_Ranap.getValueAt(tbPemberianDiet_Ranap.getSelectedRow(),8).toString());
            Valid.SetTgl(DTPTgl_Ranap,tbPemberianDiet_Ranap.getValueAt(tbPemberianDiet_Ranap.getSelectedRow(),3).toString());
        }
    }
    
    private void isRawat_Ranap() {
         Sequel.cariIsi("select pasien.nm_pasien from reg_periksa inner join pasien "+
                        "on pasien.no_rkm_medis=reg_periksa.no_rkm_medis where reg_periksa.no_rawat=? ",TPasien_Ranap,TNoRw_Ranap.getText());
         Sequel.cariIsi("select kd_kamar from kamar_inap where no_rawat=? order by tgl_masuk desc limit 1",Kamar_Ranap,TNoRw_Ranap.getText());
    }
    public JTextField getTextField_Ranap(){
        return TNoRw_Ranap;
    }

    public JButton getButton_Ranap(){
        return BtnKeluar_Ranap;
    }
    
    public void isCek_Ranap(){
        BtnHapus_Ranap.setEnabled(var.getdiet_pasien());
        BtnEdit_Ranap.setEnabled(var.getdiet_pasien());
    }

     private void tampil_Ralan() {
        try{
            Valid.tabelKosong(tabMode_1);
            ps.setString(1,Valid.SetTgl(DTPCari1_Ralan.getSelectedItem()+""));
            ps.setString(2,Valid.SetTgl(DTPCari2_Ralan.getSelectedItem()+""));
            ps.setString(3,"%"+cmbJamCari_Ralan.getSelectedItem().toString().replaceAll("Semua","").trim()+"%");
            ps.setString(4,"%"+TCari_Ralan.getText().trim()+"%");
            ps.setString(5,"%"+cmbStatus_Ralan.getSelectedItem().toString().replaceAll("SEMUA","").trim()+"%");
            ps.setString(6,Valid.SetTgl(DTPCari1_Ralan.getSelectedItem()+""));
            ps.setString(7,Valid.SetTgl(DTPCari2_Ralan.getSelectedItem()+""));
            ps.setString(8,"%"+cmbJamCari_Ralan.getSelectedItem().toString().replaceAll("Semua","").trim()+"%");
            ps.setString(9,"%"+TCari_Ralan.getText().trim()+"%");
            ps.setString(10,"%"+cmbStatus_Ralan.getSelectedItem().toString().replaceAll("SEMUA","").trim()+"%");
            ps.setString(11,Valid.SetTgl(DTPCari1_Ralan.getSelectedItem()+""));
            ps.setString(12,Valid.SetTgl(DTPCari2_Ralan.getSelectedItem()+""));
            ps.setString(13,"%"+cmbJamCari_Ralan.getSelectedItem().toString().replaceAll("Semua","").trim()+"%");
            ps.setString(14,"%"+TCari_Ralan.getText().trim()+"%");
            ps.setString(15,"%"+cmbStatus_Ralan.getSelectedItem().toString().replaceAll("SEMUA","").trim()+"%");
           rs=ps.executeQuery();
            while(rs.next()){
                tabMode_1.addRow(new Object[]{
                rs.getString(1),rs.getString(2)+", "+rs.getString(3),
                    rs.getString(4),rs.getString(5),
                    rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)
                });
            }
        }catch(SQLException e){
            System.out.println("Notifikasi : "+e);
        }
        LCount_Ralan.setText(""+tabMode_1.getRowCount());
    }

    public void emptTeks_Ralan() {
        TNoRw_Ralan.setText("");
        TPasien_Ralan.setText("");
        TCari_Ralan.setText("");
        NmBentukMakanan_Ralan.setText("");
        NmJenisDiet_Ralan.setText("");
        NmEkstra_Ralan.setText("");
        TNoRw_Ralan.requestFocus();
        TPasien_Ralan.requestFocus();
        NmBentukMakanan_Ralan.requestFocus();
        NmJenisDiet_Ralan.requestFocus();
        NmEkstra_Ralan.requestFocus();
    }
    
    private void getData_Ralan() {
        if(tbPemberianDiet_Ralan.getSelectedRow()!= -1){
            TNoRw_Ralan.setText(tbPemberianDiet_Ralan.getValueAt(tbPemberianDiet_Ralan.getSelectedRow(),0).toString()); 
            isRawat_Ralan();            
            cmbJam_Ralan.setSelectedItem(tbPemberianDiet_Ralan.getValueAt(tbPemberianDiet_Ralan.getSelectedRow(),3).toString());
            NmBentukMakanan_Ralan.setText(tbPemberianDiet_Ralan.getValueAt(tbPemberianDiet_Ralan.getSelectedRow(),4).toString());
            NmJenisDiet_Ralan.setText(tbPemberianDiet_Ralan.getValueAt(tbPemberianDiet_Ralan.getSelectedRow(),5).toString());
            NmEkstra_Ralan.setText(tbPemberianDiet_Ralan.getValueAt(tbPemberianDiet_Ralan.getSelectedRow(),6).toString());
            Valid.SetTgl(DTPTgl_Ralan,tbPemberianDiet_Ralan.getValueAt(tbPemberianDiet_Ralan.getSelectedRow(),2).toString());
         }
    }
    
    private void isRawat_Ralan() {
         Sequel.cariIsi("select pasien.nm_pasien from reg_periksa inner join pasien "+
                        "on pasien.no_rkm_medis=reg_periksa.no_rkm_medis where reg_periksa.no_rawat=? ",TPasien_Ralan,TNoRw_Ralan.getText());
    }
    
    public JTextField getTextField_Ralan(){
        return TNoRw_Ralan;
    }

    public JButton getButton_Ralan(){
        return BtnKeluar_Ralan;
    }
    
    public void isCek_Ralan(){
        BtnHapus_Ralan.setEnabled(var.getdiet_pasien());
        BtnEdit_Ralan.setEnabled(var.getdiet_pasien());
    }
}


