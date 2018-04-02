/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * DlgPenyakit.java
 *
 * Created on May 23, 2010, 12:57:16 AM
 */
package simrskhanza;

import base.BaseDialog;
import fungsi.GStatement;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author dosen
 */
public final class DlgCariNoRawat extends BaseDialog
{
    private DefaultTableModel tabMode;
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private Connection koneksi = koneksiDB.condb();
    private PreparedStatement ps;
    private ResultSet rs;

    /**
     * Creates new form DlgPenyakit
     *
     * @param parent
     * @param modal
     */
    public DlgCariNoRawat(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        this.setLocation(10, 2);
        
        initTblTransaksi();
        tampilTransaksi();
        
        if (koneksiDB.cariCepat().equals("aktif"))
        {
            txtCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener()
            {
                @Override
                public void insertUpdate(DocumentEvent e)
                {
                    tampilTransaksi();
                }

                @Override
                public void removeUpdate(DocumentEvent e)
                {
                    tampilTransaksi();
                }

                @Override
                public void changedUpdate(DocumentEvent e)
                {
                    tampilTransaksi();
                }
            });
        }
    }
    
    private void initTblTransaksi()
    {
        Object[] row={"No.Rawat","Nomer RM","Nama Pasien","Alamat Pasien","Penanggung Jawab","Hubungan P.J.","Jenis Bayar","Kamar","Tarif Kamar",
                    "Diagnosa Awal","Diagnosa Akhir","Tgl.Masuk","Jam Masuk","Tgl.Keluar","Jam Keluar",
                    "Ttl.Biaya","Stts.Pulang","Lama","Dokter P.J.","Kamar"};
        
        tabMode = new DefaultTableModel(null, row)
        {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                return false;
            }
        };
        
        tblGroup.setModel(tabMode);
        tblGroup.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tblGroup.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < 20; i++) {
            TableColumn column = tblGroup.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(110);
            }else if(i==1){
                column.setPreferredWidth(70);
            }else if(i==2){
                column.setPreferredWidth(150);
            }else if(i==3){
                column.setPreferredWidth(150);
            }else if(i==4){
                column.setPreferredWidth(120);
            }else if(i==5){
                column.setPreferredWidth(90);
            }else if(i==6){
                column.setPreferredWidth(80);
            }else if(i==7){
                column.setPreferredWidth(150);
            }else if(i==8){
                column.setPreferredWidth(85);
            }else if(i==9){
                column.setPreferredWidth(90);
            }else if(i==10){
                column.setPreferredWidth(90);
            }else if(i==11){
                column.setPreferredWidth(75);
            }else if(i==12){
                column.setPreferredWidth(65);
            }else if(i==13){
                column.setPreferredWidth(75);
            }else if(i==14){
                column.setPreferredWidth(65);
            }else if(i==15){
                column.setPreferredWidth(100);
            }else if(i==16){
                column.setPreferredWidth(80);
            }else if(i==17){
                column.setPreferredWidth(40);
            }else if(i==18){
                column.setPreferredWidth(130);
            }else if(i==19){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
        
        tblGroup.setDefaultRenderer(Object.class, new WarnaTable());
        txtCari.setDocument(new batasInput((byte) 100).getKata(txtCari));
    }
    
    public void tampilTransaksi()
    {
        Valid.tabelKosong(tabMode);
        
        try 
        {
            String q = "select kamar_inap.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab),reg_periksa.p_jawab,reg_periksa.hubunganpj,"+
                       "penjab.png_jawab,concat(kamar_inap.kd_kamar,' ',bangsal.nm_bangsal),kamar_inap.trf_kamar,kamar_inap.diagnosa_awal,kamar_inap.diagnosa_akhir," +
                       "kamar_inap.tgl_masuk,kamar_inap.jam_masuk,if(kamar_inap.tgl_keluar='0000-00-00','',kamar_inap.tgl_keluar),"+
                       "if(kamar_inap.jam_keluar='00:00:00','',kamar_inap.jam_keluar),kamar_inap.ttl_biaya,kamar_inap.stts_pulang, lama,dokter.nm_dokter,kamar_inap.kd_kamar,reg_periksa.kd_pj "+
                       "from kamar_inap inner join reg_periksa inner join pasien inner join kamar inner join bangsal inner join kelurahan inner join kecamatan inner join kabupaten inner join dokter inner join penjab " +
                       "on kamar_inap.no_rawat=reg_periksa.no_rawat " +
                       "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                       "and reg_periksa.kd_dokter=dokter.kd_dokter " +
                       "and reg_periksa.kd_pj=penjab.kd_pj " +
                       "and kamar_inap.kd_kamar=kamar.kd_kamar " +
                       "and kamar.kd_bangsal=bangsal.kd_bangsal and pasien.kd_kel=kelurahan.kd_kel "+
                       "and pasien.kd_kec=kecamatan.kd_kec and pasien.kd_kab=kabupaten.kd_kab " +
                       "WHERE kamar_inap.stts_pulang = '-'";
            
//            ps = koneksi.prepareStatement("SELECT kd_operasi, operasi.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, kamar.kd_kamar, bangsal.nm_bangsal, poliklinik.nm_poli, "
//                    + "operasi_group.kd_group, operasi_kategori.kd_kategori, operasi_detail.kd_detail, nm_group, nm_kategori, nm_detail, tgl_operasi, jam_operasi, tgl_selesai, jam_selesai "
//                    + "FROM operasi "
//                    + "JOIN operasi_detail ON operasi_detail.kd_detail = operasi.kd_detail "
//                    + "JOIN operasi_kategori ON operasi_kategori.kd_kategori = operasi_detail.kd_kategori "
//                    + "JOIN operasi_group ON operasi_group.kd_group = operasi_kategori.kd_group "
//                    + "JOIN reg_periksa ON reg_periksa.no_rawat = operasi.no_rawat "
//                    + "JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis "
//                    + "LEFT JOIN kamar_inap ON kamar_inap.no_rawat = operasi.no_rawat "
//                    + "LEFT JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar "
//                    + "LEFT JOIN bangsal ON bangsal.kd_bangsal = kamar.kd_bangsal "
//                    + "LEFT JOIN poliklinik ON poliklinik.kd_poli = reg_periksa.kd_poli "
//                    + "WHERE (pasien.nm_pasien LIKE ? OR operasi_group.nm_group LIKE ? "
//                    + "OR operasi_kategori.nm_kategori LIKE ? OR operasi.no_rawat LIKE ? "
//                    + "OR operasi_detail.nm_detail LIKE ?) "
//                    + "AND proses = 'Belum' "
//                    + "ORDER BY tgl_operasi");
            
//            ps.setString(1, "%" + txtCari.getText().trim() + "%");
//            ps.setString(2, "%" + txtCari.getText().trim() + "%");
//            ps.setString(3, "%" + txtCari.getText().trim() + "%");
//            ps.setString(4, "%" + txtCari.getText().trim() + "%");
//            ps.setString(5, "%" + txtCari.getText().trim() + "%");

            ps = koneksi.prepareStatement(q);
            rs = ps.executeQuery();
                
            while (rs.next())
            {
                String kmr;
                
                if (rs.getString("kd_kamar") != null)
                {
                    kmr = "(Kamar : " +  rs.getString("kd_kamar") + ", " + rs.getString("nm_bangsal") + ")";
                }
                else
                {
                    kmr = "(Poli : " + rs.getString("nm_poli") + ")";
                }
                
                Object[] o = new Object[]
                {
                    rs.getString("kd_operasi"),
                    rs.getString("no_rawat"),
                    rs.getString("no_rkm_medis"),
                    rs.getString("nm_pasien"),
                    kmr,
                    rs.getString("nm_group"),
                    rs.getString("nm_kategori"),
                    rs.getString("nm_detail"),
                    rs.getString("tgl_operasi") + " " + rs.getString("jam_operasi")
                };
                
                tabMode.addRow(o);
            }
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
        }
        
        lblCount.setText("" + tabMode.getRowCount());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tblGroup = new widget.Table();
        panelisi3 = new widget.panelisi();
        label9 = new widget.Label();
        txtCari = new widget.TextBox();
        btnCari = new widget.Button();
        btnAll = new widget.Button();
        label10 = new widget.Label();
        lblCount = new widget.Label();
        btnKeluar = new widget.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Group Operasi ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tblGroup.setAutoCreateRowSorter(true);
        tblGroup.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tblGroup.setName("tblGroup"); // NOI18N
        tblGroup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGroupMouseClicked(evt);
            }
        });
        tblGroup.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblGroupKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tblGroup);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        panelisi3.setName("panelisi3"); // NOI18N
        panelisi3.setPreferredSize(new java.awt.Dimension(100, 43));
        panelisi3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        label9.setText("Key Word :");
        label9.setName("label9"); // NOI18N
        label9.setPreferredSize(new java.awt.Dimension(68, 23));
        panelisi3.add(label9);

        txtCari.setName("txtCari"); // NOI18N
        txtCari.setPreferredSize(new java.awt.Dimension(312, 23));
        txtCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariActionPerformed(evt);
            }
        });
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCariKeyPressed(evt);
            }
        });
        panelisi3.add(txtCari);

        btnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        btnCari.setMnemonic('1');
        btnCari.setToolTipText("Alt+1");
        btnCari.setName("btnCari"); // NOI18N
        btnCari.setPreferredSize(new java.awt.Dimension(28, 23));
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });
        btnCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnCariKeyPressed(evt);
            }
        });
        panelisi3.add(btnCari);

        btnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        btnAll.setMnemonic('2');
        btnAll.setToolTipText("2Alt+2");
        btnAll.setName("btnAll"); // NOI18N
        btnAll.setPreferredSize(new java.awt.Dimension(28, 23));
        btnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAllActionPerformed(evt);
            }
        });
        btnAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAllKeyPressed(evt);
            }
        });
        panelisi3.add(btnAll);

        label10.setText("Record :");
        label10.setName("label10"); // NOI18N
        label10.setPreferredSize(new java.awt.Dimension(60, 23));
        panelisi3.add(label10);

        lblCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCount.setText("0");
        lblCount.setName("lblCount"); // NOI18N
        lblCount.setPreferredSize(new java.awt.Dimension(50, 23));
        panelisi3.add(lblCount);

        btnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        btnKeluar.setMnemonic('4');
        btnKeluar.setToolTipText("Alt+4");
        btnKeluar.setName("btnKeluar"); // NOI18N
        btnKeluar.setPreferredSize(new java.awt.Dimension(28, 23));
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });
        panelisi3.add(btnKeluar);

        internalFrame1.add(panelisi3, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);
        internalFrame1.getAccessibleContext().setAccessibleName("::[ No Rawat ]::");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void txtCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            btnCariActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
        {
            btnCari.requestFocus();
        }
        else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP)
        {
            btnKeluar.requestFocus();
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            tblGroup.requestFocus();
        }
}//GEN-LAST:event_txtCariKeyPressed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        tampilTransaksi();
}//GEN-LAST:event_btnCariActionPerformed

    private void btnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            btnCariActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtCari, btnAll);
        }
}//GEN-LAST:event_btnCariKeyPressed

    private void btnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllActionPerformed
        txtCari.setText("");
        tampilTransaksi();
}//GEN-LAST:event_btnAllActionPerformed

    private void btnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAllKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            btnAllActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, btnCari, txtCari);
        }
}//GEN-LAST:event_btnAllKeyPressed

    private void tblGroupMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGroupMouseClicked
        if (tabMode.getRowCount() != 0)
        {
            if (evt.getClickCount() == 2)
            {
                dispose();
            }
        }
}//GEN-LAST:event_tblGroupMouseClicked

    private void tblGroupKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblGroupKeyPressed
        if (tabMode.getRowCount() != 0)
        {
            if (evt.getKeyCode() == KeyEvent.VK_SPACE)
            {
                dispose();
            }
            else if (evt.getKeyCode() == KeyEvent.VK_SHIFT)
            {
                txtCari.setText("");
                txtCari.requestFocus();
            }
        }
}//GEN-LAST:event_tblGroupKeyPressed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        emptTeks();
    }//GEN-LAST:event_formWindowActivated

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        //tampil();
    }//GEN-LAST:event_formWindowOpened

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(() -> 
                {
                    DlgCariNoRawat dialog = new DlgCariNoRawat(new javax.swing.JFrame(), true);
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
    private widget.ScrollPane Scroll;
    private widget.Button btnAll;
    private widget.Button btnCari;
    private widget.Button btnKeluar;
    private widget.InternalFrame internalFrame1;
    private widget.Label label10;
    private widget.Label label9;
    private widget.Label lblCount;
    private widget.panelisi panelisi3;
    private widget.Table tblGroup;
    private widget.TextBox txtCari;
    // End of variables declaration//GEN-END:variables


    public void emptTeks()
    {
        txtCari.requestFocus();
    }

    public JTable getTable()
    {
        return tblGroup;
    }
}
