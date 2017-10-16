/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgAdmin.java
 *
 * Created on 21 Jun 10, 20:53:44
 */

package setting;

import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import simrskhanza.DlgCariDokter;

/**
 *
 * @author perpustakaan
 */
public class DlgSetPenjabLab extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    public  DlgCariDokter dokter=new DlgCariDokter(null,false);
    private int pilihan=0,i=0;

    /** Creates new form DlgAdmin
     * @param parent
     * @param modal */
    public DlgSetPenjabLab(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(10,10);
        setSize(457,249);

        Object[] row={"P.J.Laboratorium","P.J.Radiologi","P.J.Hemodialisa","P.J.Tranfusi Darah", "Konsultan Hemodialisa", "Pelaksana Hemodialisa"};
        tabMode=new DefaultTableModel(null,row){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };

        tbAdmin.setModel(tabMode);
        tbAdmin.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbAdmin.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 6; i++) {
            TableColumn column = tbAdmin.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(200);
            }else if(i==1){
                column.setPreferredWidth(200);
            }else if(i==2){
                column.setPreferredWidth(200);
            }else if(i==3){
                column.setPreferredWidth(200);
            }else if(i==4){
                column.setPreferredWidth(200);
            }else if(i==5){
                column.setPreferredWidth(200);
            }
        }

        tbAdmin.setDefaultRenderer(Object.class, new WarnaTable());
        kddokter.setDocument(new batasInput((byte)20).getKata(kddokter));
        kddokter2.setDocument(new batasInput((byte)20).getKata(kddokter2));
        kddokter3.setDocument(new batasInput((byte)20).getKata(kddokter3));
        kddokter4.setDocument(new batasInput((byte)20).getKata(kddokter4));
        txtKdKonsHd.setDocument(new batasInput((byte)20).getKata(txtKdKonsHd));
        txtKdPelHd.setDocument(new batasInput((byte)20).getKata(txtKdPelHd));

        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {;}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dokter.getTable().getSelectedRow()!= -1){                    
                    if(pilihan==1){
                        kddokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                        TDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                        kddokter.requestFocus();
                    }else if(pilihan==2){
                        kddokter2.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                        TDokter2.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                        kddokter2.requestFocus();
                    }else if(pilihan==3){
                        kddokter3.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                        TDokter3.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                        kddokter3.requestFocus();
                    }else if(pilihan==4){
                        kddokter4.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                        TDokter4.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                        kddokter4.requestFocus();
                    }else if(pilihan==5){
                        txtKdKonsHd.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                        txtNamaKonsHd.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                        txtKdKonsHd.requestFocus();
                    }else if(pilihan==6){
                        txtKdPelHd.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                        txtNamaPelHd.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                        txtKdPelHd.requestFocus();
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

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbAdmin = new widget.Table();
        panelGlass7 = new widget.panelisi();
        jLabel13 = new widget.Label();
        kddokter = new widget.TextBox();
        TDokter = new widget.TextBox();
        BtnDokter = new widget.Button();
        jLabel14 = new widget.Label();
        kddokter2 = new widget.TextBox();
        TDokter2 = new widget.TextBox();
        BtnDokter2 = new widget.Button();
        jLabel15 = new widget.Label();
        kddokter3 = new widget.TextBox();
        TDokter3 = new widget.TextBox();
        BtnDokter3 = new widget.Button();
        jLabel16 = new widget.Label();
        kddokter4 = new widget.TextBox();
        TDokter4 = new widget.TextBox();
        BtnDokter4 = new widget.Button();
        jLabel17 = new widget.Label();
        txtKdKonsHd = new widget.TextBox();
        txtNamaKonsHd = new widget.TextBox();
        BtnDokter5 = new widget.Button();
        jLabel18 = new widget.Label();
        txtKdPelHd = new widget.TextBox();
        txtNamaPelHd = new widget.TextBox();
        BtnDokter6 = new widget.Button();
        panelGlass5 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        BtnKeluar = new widget.Button();

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

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Setup Penanggung Jawab Unit Penunjang ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbAdmin.setAutoCreateRowSorter(true);
        tbAdmin.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbAdmin.setName("tbAdmin"); // NOI18N
        tbAdmin.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbAdminMouseClicked(evt);
            }
        });
        tbAdmin.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tbAdminKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbAdmin);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        panelGlass7.setName("panelGlass7"); // NOI18N
        panelGlass7.setPreferredSize(new java.awt.Dimension(44, 190));

        jLabel13.setText("P.J.Laboratorium :");
        jLabel13.setName("jLabel13"); // NOI18N

        kddokter.setHighlighter(null);
        kddokter.setName("kddokter"); // NOI18N
        kddokter.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                kddokterKeyPressed(evt);
            }
        });

        TDokter.setEditable(false);
        TDokter.setName("TDokter"); // NOI18N

        BtnDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnDokter.setMnemonic('3');
        BtnDokter.setToolTipText("ALt+3");
        BtnDokter.setName("BtnDokter"); // NOI18N
        BtnDokter.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnDokterActionPerformed(evt);
            }
        });

        jLabel14.setText("P.J.Radiologi :");
        jLabel14.setName("jLabel14"); // NOI18N

        kddokter2.setHighlighter(null);
        kddokter2.setName("kddokter2"); // NOI18N
        kddokter2.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                kddokter2KeyPressed(evt);
            }
        });

        TDokter2.setEditable(false);
        TDokter2.setName("TDokter2"); // NOI18N

        BtnDokter2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnDokter2.setMnemonic('3');
        BtnDokter2.setToolTipText("ALt+3");
        BtnDokter2.setName("BtnDokter2"); // NOI18N
        BtnDokter2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnDokter2ActionPerformed(evt);
            }
        });

        jLabel15.setText("P.J.Hemodialisa :");
        jLabel15.setName("jLabel15"); // NOI18N

        kddokter3.setHighlighter(null);
        kddokter3.setName("kddokter3"); // NOI18N
        kddokter3.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                kddokter3KeyPressed(evt);
            }
        });

        TDokter3.setEditable(false);
        TDokter3.setName("TDokter3"); // NOI18N

        BtnDokter3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnDokter3.setMnemonic('3');
        BtnDokter3.setToolTipText("ALt+3");
        BtnDokter3.setName("BtnDokter3"); // NOI18N
        BtnDokter3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnDokter3ActionPerformed(evt);
            }
        });

        jLabel16.setText("P.J.Tranfusi Darah :");
        jLabel16.setName("jLabel16"); // NOI18N

        kddokter4.setHighlighter(null);
        kddokter4.setName("kddokter4"); // NOI18N
        kddokter4.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                kddokter4KeyPressed(evt);
            }
        });

        TDokter4.setEditable(false);
        TDokter4.setName("TDokter4"); // NOI18N

        BtnDokter4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnDokter4.setMnemonic('3');
        BtnDokter4.setToolTipText("ALt+3");
        BtnDokter4.setName("BtnDokter4"); // NOI18N
        BtnDokter4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnDokter4ActionPerformed(evt);
            }
        });

        jLabel17.setText("Konsultan Hemodialisa :");
        jLabel17.setName("jLabel17"); // NOI18N

        txtKdKonsHd.setHighlighter(null);
        txtKdKonsHd.setName("txtKdKonsHd"); // NOI18N
        txtKdKonsHd.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdKonsHdKeyPressed(evt);
            }
        });

        txtNamaKonsHd.setEditable(false);
        txtNamaKonsHd.setName("txtNamaKonsHd"); // NOI18N

        BtnDokter5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnDokter5.setMnemonic('3');
        BtnDokter5.setToolTipText("ALt+3");
        BtnDokter5.setName("BtnDokter5"); // NOI18N
        BtnDokter5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnDokter5ActionPerformed(evt);
            }
        });

        jLabel18.setText("Pelaksanan Hemodialisa :");
        jLabel18.setName("jLabel18"); // NOI18N

        txtKdPelHd.setHighlighter(null);
        txtKdPelHd.setName("txtKdPelHd"); // NOI18N
        txtKdPelHd.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdPelHdKeyPressed(evt);
            }
        });

        txtNamaPelHd.setEditable(false);
        txtNamaPelHd.setName("txtNamaPelHd"); // NOI18N

        BtnDokter6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnDokter6.setMnemonic('3');
        BtnDokter6.setToolTipText("ALt+3");
        BtnDokter6.setName("BtnDokter6"); // NOI18N
        BtnDokter6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnDokter6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelGlass7Layout = new javax.swing.GroupLayout(panelGlass7);
        panelGlass7.setLayout(panelGlass7Layout);
        panelGlass7Layout.setHorizontalGroup(
            panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass7Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGlass7Layout.createSequentialGroup()
                        .addComponent(kddokter, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(TDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BtnDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelGlass7Layout.createSequentialGroup()
                        .addComponent(kddokter3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TDokter3, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BtnDokter3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelGlass7Layout.createSequentialGroup()
                        .addComponent(kddokter4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TDokter4, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BtnDokter4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelGlass7Layout.createSequentialGroup()
                        .addComponent(txtKdKonsHd, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNamaKonsHd, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BtnDokter5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelGlass7Layout.createSequentialGroup()
                        .addComponent(txtKdPelHd, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNamaPelHd, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BtnDokter6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelGlass7Layout.createSequentialGroup()
                        .addComponent(kddokter2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TDokter2, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BtnDokter2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(134, Short.MAX_VALUE))
        );
        panelGlass7Layout.setVerticalGroup(
            panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass7Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(kddokter, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(TDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(BtnDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGlass7Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(TDokter2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(kddokter2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(BtnDokter2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelGlass7Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGlass7Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(TDokter3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(kddokter3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(BtnDokter3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelGlass7Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGlass7Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(TDokter4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(kddokter4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(BtnDokter4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelGlass7Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addGroup(panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNamaKonsHd, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtKdKonsHd, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(BtnDokter5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNamaPelHd, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtKdPelHd, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(BtnDokter6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        internalFrame1.add(panelGlass7, java.awt.BorderLayout.PAGE_START);

        panelGlass5.setName("panelGlass5"); // NOI18N
        panelGlass5.setPreferredSize(new java.awt.Dimension(55, 55));
        panelGlass5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

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
        panelGlass5.add(BtnSimpan);

        BtnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        BtnBatal.setMnemonic('B');
        BtnBatal.setText("Baru");
        BtnBatal.setToolTipText("Alt+B");
        BtnBatal.setIconTextGap(3);
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
        panelGlass5.add(BtnBatal);

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setIconTextGap(3);
        BtnHapus.setName("BtnHapus"); // NOI18N
        BtnHapus.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnHapusActionPerformed(evt);
            }
        });
        BtnHapus.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                BtnHapusKeyPressed(evt);
            }
        });
        panelGlass5.add(BtnHapus);

        BtnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnEdit.setMnemonic('G');
        BtnEdit.setText("Ganti");
        BtnEdit.setToolTipText("Alt+G");
        BtnEdit.setIconTextGap(3);
        BtnEdit.setName("BtnEdit"); // NOI18N
        BtnEdit.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnEdit.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnEditActionPerformed(evt);
            }
        });
        BtnEdit.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                BtnEditKeyPressed(evt);
            }
        });
        panelGlass5.add(BtnEdit);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setIconTextGap(3);
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

        internalFrame1.add(panelGlass5, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TDokter.getText().trim().equals("")||kddokter.getText().trim().equals("")){
            Valid.textKosong(kddokter,"Penanggung Jawab Laborat");
        }else if(TDokter2.getText().trim().equals("")||kddokter2.getText().trim().equals("")){
            Valid.textKosong(kddokter2,"Penanggung Jawab Radiologi");
        }else if(TDokter3.getText().trim().equals("")||kddokter3.getText().trim().equals("")){
            Valid.textKosong(kddokter3,"Penanggung Jawab Hemodialisa");
        }else if(TDokter4.getText().trim().equals("")||kddokter4.getText().trim().equals("")){
            Valid.textKosong(kddokter4,"Penanggung Jawab Unit Tranfusi Darah");
        }else if(txtNamaKonsHd.getText().trim().equals("")||txtKdKonsHd.getText().trim().equals("")){
            Valid.textKosong(txtKdKonsHd,"Konsultan Hemodialisa");
        }else if(txtNamaPelHd.getText().trim().equals("")||txtKdPelHd.getText().trim().equals("")){
            Valid.textKosong(txtKdPelHd,"Pelaksana Hemodialisa");
        }else if(tabMode.getRowCount()==0){
            Sequel.menyimpan("set_pjlab","?,?,?,?,?,?","Penanggung Jawab",4,new String[]{
                kddokter.getText(),kddokter2.getText(),kddokter3.getText(),kddokter4.getText(), txtKdKonsHd.getText(), txtKdPelHd.getText()
            });
            tampil();
            emptTeks();
        }else if(tabMode.getRowCount()>0){
            JOptionPane.showMessageDialog(null,"Maaf, Hanya diijinkan satu Admin Utama ...!!!!");
            kddokter.requestFocus();
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
            Valid.pindah(evt,kddokter4,BtnBatal);
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
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
            kddokter.requestFocus();
        }else{
            Sequel.queryu("delete from set_pjlab");
            tampil();
            emptTeks();
        }
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBatal, BtnEdit);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        if(TDokter.getText().trim().equals("")||kddokter.getText().trim().equals("")){
            Valid.textKosong(kddokter,"Penanggung Jawab Laborat");
        }else if(TDokter2.getText().trim().equals("")||kddokter2.getText().trim().equals("")){
            Valid.textKosong(kddokter2,"Penanggung Jawab Radiologi");
        }else if(TDokter3.getText().trim().equals("")||kddokter3.getText().trim().equals("")){
            Valid.textKosong(kddokter3,"Penanggung Jawab Hemodialisa");
        }else if(TDokter4.getText().trim().equals("")||kddokter4.getText().trim().equals("")){
            Valid.textKosong(kddokter4,"Penanggung Jawab Unit Tranfusi Darah");
        }else if(txtNamaKonsHd.getText().trim().equals("")||txtKdKonsHd.getText().trim().equals("")){
            Valid.textKosong(txtKdKonsHd,"Konsultan Hemodialisa");
        }else if(txtNamaPelHd.getText().trim().equals("")||txtKdPelHd.getText().trim().equals("")){
            Valid.textKosong(txtKdPelHd,"Pelaksana Hemodialisa");
        }else{
            Sequel.queryu("delete from set_pjlab");
            Sequel.menyimpan("set_pjlab","?,?,?,?,?,?","Penanggung Jawab",6,new String[]{
                kddokter.getText(),kddokter2.getText(),kddokter3.getText(),kddokter4.getText(), txtKdKonsHd.getText(), txtKdPelHd.getText()
            });
            tampil();
            emptTeks();
        }
}//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEditActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnKeluar);
        }
}//GEN-LAST:event_BtnEditKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data penanggung jawab lab & radiologi tidak boleh kosong ...!!!!");
            kddokter.requestFocus();
        }else if(! (tabMode.getRowCount()==0)) {
            dispose();
        }
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,BtnEdit,BtnKeluar);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void tbAdminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbAdminMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbAdminMouseClicked

    private void tbAdminKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbAdminKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbAdminKeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        tampil();
    }//GEN-LAST:event_formWindowOpened

    private void kddokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kddokterKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",TDokter,kddokter.getText());
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnDokterActionPerformed(null);
        }else{
            Valid.pindah(evt,BtnKeluar,kddokter2);
        }
    }//GEN-LAST:event_kddokterKeyPressed

    private void BtnDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokterActionPerformed
        pilihan=1;
        dokter.isCek();
        dokter.TCari.requestFocus();
        dokter.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnDokterActionPerformed

    private void kddokter2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kddokter2KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",TDokter2,kddokter2.getText());
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnDokter2ActionPerformed(null);
        }else{
            Valid.pindah(evt,kddokter,kddokter3);
        }
    }//GEN-LAST:event_kddokter2KeyPressed

    private void BtnDokter2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokter2ActionPerformed
        pilihan=2;
        dokter.isCek();
        dokter.TCari.requestFocus();
        dokter.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnDokter2ActionPerformed

    private void kddokter3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kddokter3KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",TDokter3,kddokter3.getText());
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnDokter3ActionPerformed(null);
        }else{
            Valid.pindah(evt,kddokter2,kddokter4);
        }
    }//GEN-LAST:event_kddokter3KeyPressed

    private void BtnDokter3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokter3ActionPerformed
        pilihan=3;
        dokter.isCek();
        dokter.TCari.requestFocus();
        dokter.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnDokter3ActionPerformed

    private void kddokter4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kddokter4KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",TDokter4,kddokter4.getText());
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnDokter4ActionPerformed(null);
        }else{
            Valid.pindah(evt,kddokter3,BtnSimpan);
        }
    }//GEN-LAST:event_kddokter4KeyPressed

    private void BtnDokter4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokter4ActionPerformed
        pilihan=4;
        dokter.isCek();
        dokter.TCari.requestFocus();
        dokter.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnDokter4ActionPerformed

    private void txtKdKonsHdKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdKonsHdKeyPressed
    {//GEN-HEADEREND:event_txtKdKonsHdKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",txtNamaKonsHd,txtKdKonsHd.getText());
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnDokter5ActionPerformed(null);
        }else{
            Valid.pindah(evt,kddokter4,txtKdKonsHd);
        }
    }//GEN-LAST:event_txtKdKonsHdKeyPressed

    private void BtnDokter5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BtnDokter5ActionPerformed
    {//GEN-HEADEREND:event_BtnDokter5ActionPerformed
        pilihan=5;
        dokter.isCek();
        dokter.TCari.requestFocus();
        dokter.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnDokter5ActionPerformed

    private void txtKdPelHdKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdPelHdKeyPressed
    {//GEN-HEADEREND:event_txtKdPelHdKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",txtKdPelHd,txtNamaPelHd.getText());
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnDokter6ActionPerformed(null);
        }else{
            Valid.pindah(evt,txtKdKonsHd,BtnSimpan);
        }
    }//GEN-LAST:event_txtKdPelHdKeyPressed

    private void BtnDokter6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BtnDokter6ActionPerformed
    {//GEN-HEADEREND:event_BtnDokter6ActionPerformed
        pilihan=6;
        dokter.isCek();
        dokter.TCari.requestFocus();
        dokter.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnDokter6ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgSetPenjabLab dialog = new DlgSetPenjabLab(new javax.swing.JFrame(), true);
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
    private widget.Button BtnBatal;
    private widget.Button BtnDokter;
    private widget.Button BtnDokter2;
    private widget.Button BtnDokter3;
    private widget.Button BtnDokter4;
    private widget.Button BtnDokter5;
    private widget.Button BtnDokter6;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnSimpan;
    private widget.ScrollPane Scroll;
    private widget.TextBox TDokter;
    private widget.TextBox TDokter2;
    private widget.TextBox TDokter3;
    private widget.TextBox TDokter4;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
    private widget.TextBox kddokter;
    private widget.TextBox kddokter2;
    private widget.TextBox kddokter3;
    private widget.TextBox kddokter4;
    private widget.panelisi panelGlass5;
    private widget.panelisi panelGlass7;
    private widget.Table tbAdmin;
    private widget.TextBox txtKdKonsHd;
    private widget.TextBox txtKdPelHd;
    private widget.TextBox txtNamaKonsHd;
    private widget.TextBox txtNamaPelHd;
    // End of variables declaration//GEN-END:variables

    private void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            ResultSet rs=koneksi.prepareStatement("select * from set_pjlab").executeQuery();
            while(rs.next()){
                tabMode.addRow(new Object[]{
                    rs.getString(1)+" "+Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",rs.getString(1)),
                    rs.getString(2)+" "+Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",rs.getString(2)),
                    rs.getString(3)+" "+Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",rs.getString(3)),
                    rs.getString(4)+" "+Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",rs.getString(4)),
                    rs.getString(5)+" "+Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",rs.getString(5)),
                    rs.getString(6)+" "+Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",rs.getString(6))
                });
            }
        }catch(SQLException e){
            System.out.println("Notifikasi : "+e);
        }
    }

    private void getData() {
        int row=tbAdmin.getSelectedRow();
        if(row!= -1){
            kddokter.setText(Sequel.cariIsi("select kd_dokterlab from set_pjlab"));
            kddokter2.setText(Sequel.cariIsi("select kd_dokterrad from set_pjlab"));
            kddokter3.setText(Sequel.cariIsi("select kd_dokterhemodialisa from set_pjlab"));
            kddokter4.setText(Sequel.cariIsi("select kd_dokterutd from set_pjlab"));
            txtKdKonsHd.setText(Sequel.cariIsi("select kd_kons_hd from set_pjlab"));
            txtKdPelHd.setText(Sequel.cariIsi("select kd_pel_hd from set_pjlab"));
            
            TDokter.setText(Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",kddokter.getText()));
            TDokter2.setText(Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",kddokter2.getText()));
            TDokter3.setText(Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",kddokter3.getText()));
            TDokter4.setText(Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",kddokter4.getText()));
            txtNamaKonsHd.setText(Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",txtKdKonsHd.getText()));
            txtNamaPelHd.setText(Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?",txtKdPelHd.getText()));
        }
    }

    public void emptTeks() {
        kddokter.setText("");
        TDokter.setText("");
        kddokter2.setText("");
        TDokter2.setText("");
        kddokter3.setText("");
        TDokter3.setText("");
        kddokter4.setText("");
        TDokter4.setText("");
        txtKdKonsHd.setText("");
        txtNamaKonsHd.setText("");
        txtKdPelHd.setText("");
        txtNamaPelHd.setText("");
        
        kddokter.requestFocus();
    }
}
