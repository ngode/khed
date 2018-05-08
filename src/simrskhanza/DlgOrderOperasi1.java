/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simrskhanza;

import fungsi.GQuery;
import fungsi.GStatement;
import fungsi.WarnaTable;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import util.GMessage;

/**
 *
 * @author GrT
 */
public class DlgOrderOperasi1 extends javax.swing.JDialog
{
    private sekuel sql = new sekuel();
    private validasi Valid = new validasi();
    private Connection koneksi = koneksiDB.condb();
    private PreparedStatement ps;
    private ResultSet rs;
    
    // Child Dialog ====
    DlgCariGroupOperasi dlgGroup = new DlgCariGroupOperasi(null, false);
    DlgCariKategoriOperasi dlgKategori = new DlgCariKategoriOperasi(null, false);
    DlgCariDetailOperasi dlgDetail = new DlgCariDetailOperasi(null, false);
    DlgCariDokter dlgCariDokter = new DlgCariDokter(null, false);
    
    // Comps ======
    DefaultTableModel mdlOrder;
    
    // Vars =====
    String status;
    boolean isEdit = false;
    String kdOperasi;

    /**
     * Creates new form DlgOrderOperasi
     * @param parent
     * @param modal
     */
    public DlgOrderOperasi1(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        
        jam();
        
        initTable();
        addWIndowListener();
        addTextChangedListener();
    }

    // Public =======================
    public void setNoRm(String noRw, String posisi)
    {
        txtNoRawat.setText(noRw);
        this.status = posisi;
        
        sql.cariIsi("select no_rkm_medis from reg_periksa where no_rawat=? ", txtNoRm, txtNoRawat.getText());
        sql.cariIsi("select nm_pasien from pasien where no_rkm_medis=? ", txtNamaPasien, txtNoRm.getText());
        
        tampil();
    }
    
    // PRivate ======================
    private void initTable()
    {
        mdlOrder = new DefaultTableModel(null, new String[]
        {
            "Kd Order",
            "Kd Group",
            "Kd Kategori",
            "Kd Detail",
            "Group",
            "Kategori",
            "Detail",
            "Tanggal"
        })
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        
        tblOrder.setModel(mdlOrder);
        tblOrder.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tblOrder.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        int[] colWidths = new int[] { 0, 100, 100, 100, 200, 200, 200, 150 };
        
        for (int i = 0; i < colWidths.length; i++)
        {
            TableColumn column = tblOrder.getColumnModel().getColumn(i);
            
            if (colWidths[i] == 0)
            {
                column.setMinWidth(colWidths[i]);
                column.setMaxWidth(colWidths[i]);
            }
            else
            {
                column.setPreferredWidth(colWidths[i]);
            }
        }
        
        tblOrder.setDefaultRenderer(Object.class, new WarnaTable());
    }
    
    private void addWIndowListener()
    {
        dlgGroup.addWindowClosedListener(() ->
        {
            if (dlgGroup.getTable().getSelectedRow() != -1)
            {
                txtKdGroup.setText(dlgGroup.getTable().getValueAt(dlgGroup.getTable().getSelectedRow(),0).toString());
                txtNamaGroup.setText(dlgGroup.getTable().getValueAt(dlgGroup.getTable().getSelectedRow(),1).toString());
            }
        });
        
        dlgKategori.addWindowClosedListener(() ->
        {
            if (dlgKategori.getTable().getSelectedRow() != -1)
            {
                txtKdKategori.setText(dlgKategori.getTable().getValueAt(dlgKategori.getTable().getSelectedRow(),0).toString());
                txtNamaKategori.setText(dlgKategori.getTable().getValueAt(dlgKategori.getTable().getSelectedRow(),1).toString());
            }
        });
        
        dlgDetail.addWindowClosedListener(() ->
        {
            if (dlgDetail.getTable().getSelectedRow() != -1)
            {
                txtKdDetail.setText(dlgDetail.getTable().getValueAt(dlgDetail.getTable().getSelectedRow(),0).toString());
                txtNamaDetail.setText(dlgDetail.getTable().getValueAt(dlgDetail.getTable().getSelectedRow(),1).toString());
            }
        });
        
        dlgCariDokter.addWindowClosedListener(() ->
        {
            if (dlgCariDokter.getTable().getSelectedRow() != -1)
            {
                txtKdDokter.setText(dlgCariDokter.getTable().getValueAt(dlgCariDokter.getTable().getSelectedRow(),0).toString());
                txtNamaDokter.setText(dlgCariDokter.getTable().getValueAt(dlgCariDokter.getTable().getSelectedRow(),1).toString());
            }
        });
    }
    
    private void addTextChangedListener()
    {
        txtKdGroup.addTextChangedListener((txt) ->
        {
            txtKdKategori.setText("");
            txtNamaKategori.setText("");
            txtKdDetail.setText("");
            txtNamaDetail.setText("");
        });
        
        txtKdKategori.addTextChangedListener((txt) ->
        {
            txtKdDetail.setText("");
            txtNamaDetail.setText("");
        });
    }
    
    private void cariGroup()
    {
        dlgGroup.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        dlgGroup.setLocationRelativeTo(internalFrame1);
        dlgGroup.tampil();
        dlgGroup.setVisible(true);
    }
    
    private void cariKategori()
    {
        dlgKategori.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        dlgKategori.setLocationRelativeTo(internalFrame1);
        dlgKategori.setKdGroup(txtKdGroup.getText());
        dlgKategori.tampil();
        dlgKategori.setVisible(true);
    }
    
    private void cariDetail()
    {
        dlgDetail.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        dlgDetail.setLocationRelativeTo(internalFrame1);
        dlgDetail.setKdKategori(txtKdKategori.getText());
        dlgDetail.tampil();
        dlgDetail.setVisible(true);
    }
    
    private void cariDokter()
    {
        dlgCariDokter.emptTeks();
        dlgCariDokter.isCek();
        dlgCariDokter.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        dlgCariDokter.setLocationRelativeTo(internalFrame1);
        dlgCariDokter.setVisible(true);
    }
    
    private void baru()
    {
        txtKdGroup.setText("");
        txtKdKategori.setText("");
        txtKdDetail.setText("");
        txtNamaGroup.setText("");
        txtNamaKategori.setText("");
        txtNamaDetail.setText("");
        
        isEdit = false;
        btnSimpan.setText("Simpan");
    }
    
    private void toEdit()
    {
        int r = tblOrder.getSelectedRow();
        
        txtKdGroup.setText(tblOrder.getValueAt(r, 1).toString());
        txtKdKategori.setText(tblOrder.getValueAt(r, 2).toString());
        txtKdDetail.setText(tblOrder.getValueAt(r, 3).toString());
        txtNamaGroup.setText(tblOrder.getValueAt(r, 4).toString());
        txtNamaKategori.setText(tblOrder.getValueAt(r, 5).toString());
        txtNamaDetail.setText(tblOrder.getValueAt(r, 6).toString());
        
        isEdit = true;
        kdOperasi = tblOrder.getValueAt(r, 0).toString();
        btnSimpan.setText("Ubah");
    }
    
    private void simpan()
    {
        if (txtNoRawat.getText().trim().isEmpty())
        {
            GMessage.e("No rawat tidak boleh kosong", "Kosong");
            return;
        }
        else if (txtKdGroup.getText().trim().isEmpty())
        {
            GMessage.e("Kosong", "Group tidak boleh kosong");
            return;
        }
        else if (txtKdKategori.getText().trim().isEmpty())
        {
            GMessage.e("Kosong", "Kategori tidak boleh kosong");
            return;
        }
        else if (txtKdDokter.getText().trim().isEmpty())
        {
            GMessage.e("Kosong", "Dokter tidak boleh kosong");
            return;
        }
        
        if (GMessage.q("Konfirmasi", "Data sudah benar?"))
        {
            boolean b;
            
            if (isEdit)
            {
                b = new GQuery()
                    .a("UPDATE hrj_operasi SET")
                    .a("kd_group = {kd_group},")
                    .a("kd_kategori = {kd_kategori},")
                    .a("kd_detail = {kd_detail},")
                    .a("kode_paket = {kode_paket},")
                    .a("tgl_operasi = {tgl_operasi},")
                    .a("jam_operasi = {jam_operasi},")
                    .a("dokter_operator = {dokter_operator}")
                    .a("WHERE kd_operasi = {kd_operasi}")
                    .set("kd_group", txtKdGroup.getText())
                    .set("kd_kategori", txtKdKategori.getText())
                    .set("kd_detail", txtKdDetail.getText())
                    .set("kode_paket", txtKdKategori.getText())
                    .set("tgl_operasi", Valid.SetTgl(DTPBeri.getSelectedItem().toString()))
                    .set("jam_operasi", cmbJam.getSelectedItem() + ":" + cmbMnt.getSelectedItem() + ":" + cmbDtk.getSelectedItem())
                    .set("dokter_operator", txtKdDokter.getText())
                    .set("kd_operasi", kdOperasi)
                    .write();
            }
            else
            {
                b = new GQuery()
                    .a("INSERT INTO hrj_operasi (no_rawat, kd_group, kd_kategori, kd_detail, kode_paket, dokter_operator,")
                    .a("tgl_operasi, jam_operasi, status, proses)")
                    .a("VALUES ({no_rw}, {kd_group}, {kd_kategori}, {kd_detail}, {kode_paket}, {dokter_operator}, {tgl_operasi}, {jam_operasi}, {status}, 'Belum')")
                    .set("no_rw", txtNoRawat.getText())
                    .set("kd_group", txtKdGroup.getText())
                    .set("kd_kategori", txtKdKategori.getText())
                    .set("kd_detail", txtKdDetail.getText())
                    .set("kode_paket", txtKdKategori.getText())
                    .set("tgl_operasi", Valid.SetTgl(DTPBeri.getSelectedItem().toString()))
                    .set("jam_operasi", cmbJam.getSelectedItem() + ":" + cmbMnt.getSelectedItem() + ":" + cmbDtk.getSelectedItem())
                    .set("status", status)
                    .set("dokter_operator", txtKdDokter.getText())
                    .write();
            }

            if (b)
            {
                if (!isEdit) GMessage.i("Sukses", "Order operasi berhasil");
                else GMessage.i("Sukses", "Edit order operasi berhasil");

                baru();
                tampil();
            }
            else
            {
                GMessage.e("Gagal", "Order operasi gagal");
            }
        }
    }
    
    private void hapus()
    {
        if (!isEdit)
            return;
        
        if (GMessage.q("Konfirmasi", "Ingin menghapus?"))
        {
            boolean b = new GQuery()
                    .a("DELETE FROM operasi")
                    .a("WHERE kd_operasi = {kd_operasi}")
                    .set("kd_operasi", kdOperasi)
                    .write();
            
            if (b)
            {
                GMessage.i("Sukses", "Hapus order operasi berhasil");

                baru();
                tampil();
            }
            else
            {
                GMessage.e("Gagal", "Hapus order operasi gagal");
            }
        }
    }
    
    private void tampil()
    {
        Valid.tabelKosong(mdlOrder);
        
        try
        {
            ps = new GStatement(koneksi)
            .a("SELECT * FROM hrj_operasi")
            .a("JOIN operasi_detail ON operasi_detail.kd_detail = hrj_operasi.kd_detail")
            .a("JOIN operasi_kategori ON operasi_detail.kd_kategori = operasi_kategori.kd_kategori")
            .a("JOIN operasi_group ON operasi_group.kd_group = operasi_kategori.kd_group")
            .a("WHERE no_rawat = :no_rawat AND proses = 'Belum'")
            .a("ORDER BY tgl_operasi, jam_operasi")
            .setString("no_rawat", txtNoRawat.getText())
            .getStatement();
            
            try
            {
                rs = ps.executeQuery();
                
                while (rs.next())
                {
                    mdlOrder.addRow(new Object[]
                    {
                        rs.getString("kd_operasi"), 
                        rs.getString("kd_group"), 
                        rs.getString("kd_kategori"),
                        rs.getString("kd_detail"),
                        rs.getString("nm_group"), 
                        rs.getString("nm_kategori"),
                        rs.getString("nm_detail"),
                        rs.getString("tgl_operasi") + " " + rs.getString("jam_operasi")
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
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        internalFrame1 = new widget.InternalFrame();
        panelisi1 = new widget.panelisi();
        pnlInput = new widget.PanelBiasa();
        label1 = new widget.Label();
        txtNoRawat = new widget.TextBox();
        txtNoRm = new widget.TextBox();
        txtNamaPasien = new widget.TextBox();
        txtNamaGroup = new widget.TextBox();
        txtKdGroup = new widget.TextBox();
        label2 = new widget.Label();
        txtNamaKategori = new widget.TextBox();
        txtKdKategori = new widget.TextBox();
        label3 = new widget.Label();
        btnCariKategori = new widget.Button();
        btnCariGroup = new widget.Button();
        btnCariDetail = new widget.Button();
        txtNamaDetail = new widget.TextBox();
        txtKdDetail = new widget.TextBox();
        label4 = new widget.Label();
        jLabel9 = new widget.Label();
        DTPBeri = new widget.Tanggal();
        cmbJam = new widget.ComboBox();
        cmbMnt = new widget.ComboBox();
        cmbDtk = new widget.ComboBox();
        ChkJln = new widget.CekBox();
        btnCariDokter = new widget.Button();
        txtNamaDokter = new widget.TextBox();
        txtKdDokter = new widget.TextBox();
        label5 = new widget.Label();
        pnlAction = new widget.panelisi();
        btnSimpan = new widget.Button();
        btnBaru = new widget.Button();
        btnHapus = new widget.Button();
        btnCetak = new widget.Button();
        jLabel10 = new widget.Label();
        btnKeluar = new widget.Button();
        jPanel2 = new javax.swing.JPanel();
        scrollPane = new widget.ScrollPane();
        tblOrder = new widget.Table();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, ":: [ Order Operasi ] ::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout());

        panelisi1.setLayout(new java.awt.BorderLayout());

        pnlInput.setPreferredSize(new java.awt.Dimension(779, 200));

        label1.setText("No Rawat :");

        txtNoRawat.setEditable(false);

        txtNoRm.setEditable(false);

        txtNamaPasien.setEditable(false);

        txtNamaGroup.setEditable(false);

        txtKdGroup.setEditable(false);

        label2.setText("Group :");

        txtNamaKategori.setEditable(false);

        txtKdKategori.setEditable(false);

        label3.setText("Kategori :");

        btnCariKategori.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnCariKategori.setMnemonic('4');
        btnCariKategori.setToolTipText("ALt+4");
        btnCariKategori.setPreferredSize(new java.awt.Dimension(35, 22));
        btnCariKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariKategoriActionPerformed(evt);
            }
        });

        btnCariGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnCariGroup.setMnemonic('4');
        btnCariGroup.setToolTipText("ALt+4");
        btnCariGroup.setPreferredSize(new java.awt.Dimension(35, 22));
        btnCariGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariGroupActionPerformed(evt);
            }
        });

        btnCariDetail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnCariDetail.setMnemonic('4');
        btnCariDetail.setToolTipText("ALt+4");
        btnCariDetail.setPreferredSize(new java.awt.Dimension(35, 22));
        btnCariDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariDetailActionPerformed(evt);
            }
        });

        txtNamaDetail.setEditable(false);

        txtKdDetail.setEditable(false);

        label4.setText("Detail :");

        jLabel9.setText("Tanggal :");

        DTPBeri.setEditable(false);
        DTPBeri.setForeground(new java.awt.Color(50, 70, 50));
        DTPBeri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-05-2018" }));
        DTPBeri.setDisplayFormat("dd-MM-yyyy");
        DTPBeri.setOpaque(false);
        DTPBeri.setPreferredSize(new java.awt.Dimension(100, 23));
        DTPBeri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPBeriKeyPressed(evt);
            }
        });

        cmbJam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        cmbJam.setOpaque(false);
        cmbJam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbJamKeyPressed(evt);
            }
        });

        cmbMnt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbMnt.setOpaque(false);
        cmbMnt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbMntKeyPressed(evt);
            }
        });

        cmbDtk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbDtk.setOpaque(false);
        cmbDtk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbDtkKeyPressed(evt);
            }
        });

        ChkJln.setBackground(new java.awt.Color(235, 255, 235));
        ChkJln.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(195, 215, 195)));
        ChkJln.setForeground(new java.awt.Color(153, 0, 51));
        ChkJln.setSelected(true);
        ChkJln.setBorderPainted(true);
        ChkJln.setBorderPaintedFlat(true);
        ChkJln.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkJln.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkJln.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        btnCariDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnCariDokter.setMnemonic('4');
        btnCariDokter.setToolTipText("ALt+4");
        btnCariDokter.setPreferredSize(new java.awt.Dimension(35, 22));
        btnCariDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariDokterActionPerformed(evt);
            }
        });

        txtNamaDokter.setEditable(false);

        txtKdDokter.setEditable(false);

        label5.setText("Dokter :");

        javax.swing.GroupLayout pnlInputLayout = new javax.swing.GroupLayout(pnlInput);
        pnlInput.setLayout(pnlInputLayout);
        pnlInputLayout.setHorizontalGroup(
            pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlInputLayout.createSequentialGroup()
                            .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtKdKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtNamaKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtKdDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtNamaDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(pnlInputLayout.createSequentialGroup()
                                    .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtKdGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtNamaGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(pnlInputLayout.createSequentialGroup()
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(DTPBeri, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(2, 2, 2)
                                    .addComponent(cmbJam, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(1, 1, 1)
                                    .addComponent(cmbMnt, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(1, 1, 1)
                                    .addComponent(cmbDtk, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(1, 1, 1)
                                    .addComponent(ChkJln, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnCariDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnCariKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnCariGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInputLayout.createSequentialGroup()
                            .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtNoRawat, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtNoRm, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtNamaPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlInputLayout.createSequentialGroup()
                        .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtKdDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNamaDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCariDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlInputLayout.setVerticalGroup(
            pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNoRawat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNoRm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaPasien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtKdGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNamaGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCariGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtKdKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNamaKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCariKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtKdDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNamaDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCariDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtKdDokter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNamaDokter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCariDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(DTPBeri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cmbJam, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMnt, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbDtk, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ChkJln, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panelisi1.add(pnlInput, java.awt.BorderLayout.PAGE_START);

        pnlAction.setPreferredSize(new java.awt.Dimension(779, 50));
        pnlAction.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        btnSimpan.setMnemonic('S');
        btnSimpan.setText("Simpan");
        btnSimpan.setToolTipText("Alt+S");
        btnSimpan.setPreferredSize(new java.awt.Dimension(100, 30));
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        pnlAction.add(btnSimpan);

        btnBaru.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        btnBaru.setMnemonic('B');
        btnBaru.setText("Baru");
        btnBaru.setToolTipText("Alt+B");
        btnBaru.setPreferredSize(new java.awt.Dimension(100, 30));
        btnBaru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBaruActionPerformed(evt);
            }
        });
        pnlAction.add(btnBaru);

        btnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        btnHapus.setMnemonic('H');
        btnHapus.setText("Hapus");
        btnHapus.setToolTipText("Alt+H");
        btnHapus.setPreferredSize(new java.awt.Dimension(100, 30));
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        pnlAction.add(btnHapus);

        btnCetak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        btnCetak.setMnemonic('T');
        btnCetak.setText("Cetak");
        btnCetak.setToolTipText("Alt+T");
        btnCetak.setPreferredSize(new java.awt.Dimension(100, 30));
        pnlAction.add(btnCetak);

        jLabel10.setPreferredSize(new java.awt.Dimension(230, 30));
        pnlAction.add(jLabel10);

        btnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        btnKeluar.setMnemonic('K');
        btnKeluar.setText("Keluar");
        btnKeluar.setToolTipText("Alt+K");
        btnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });
        pnlAction.add(btnKeluar);

        panelisi1.add(pnlAction, java.awt.BorderLayout.PAGE_END);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, ".: List Order", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.BorderLayout());

        scrollPane.setOpaque(true);

        tblOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOrderMouseClicked(evt);
            }
        });
        scrollPane.setViewportView(tblOrder);

        jPanel2.add(scrollPane, java.awt.BorderLayout.CENTER);

        panelisi1.add(jPanel2, java.awt.BorderLayout.CENTER);

        internalFrame1.add(panelisi1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(internalFrame1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(internalFrame1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCariGroupActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCariGroupActionPerformed
    {//GEN-HEADEREND:event_btnCariGroupActionPerformed
        cariGroup();
    }//GEN-LAST:event_btnCariGroupActionPerformed

    private void btnCariKategoriActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCariKategoriActionPerformed
    {//GEN-HEADEREND:event_btnCariKategoriActionPerformed
        cariKategori();
    }//GEN-LAST:event_btnCariKategoriActionPerformed

    private void btnCariDetailActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCariDetailActionPerformed
    {//GEN-HEADEREND:event_btnCariDetailActionPerformed
        cariDetail();
    }//GEN-LAST:event_btnCariDetailActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSimpanActionPerformed
    {//GEN-HEADEREND:event_btnSimpanActionPerformed
        simpan();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnKeluarActionPerformed
    {//GEN-HEADEREND:event_btnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void tblOrderMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblOrderMouseClicked
    {//GEN-HEADEREND:event_tblOrderMouseClicked
        if (tblOrder.getSelectedRow() > -1)
        {
            toEdit();
        }
    }//GEN-LAST:event_tblOrderMouseClicked

    private void btnBaruActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnBaruActionPerformed
    {//GEN-HEADEREND:event_btnBaruActionPerformed
        baru();
    }//GEN-LAST:event_btnBaruActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnHapusActionPerformed
    {//GEN-HEADEREND:event_btnHapusActionPerformed
        hapus();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void DTPBeriKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_DTPBeriKeyPressed
    {//GEN-HEADEREND:event_DTPBeriKeyPressed

    }//GEN-LAST:event_DTPBeriKeyPressed

    private void cmbJamKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbJamKeyPressed
    {//GEN-HEADEREND:event_cmbJamKeyPressed
        Valid.pindah(evt, DTPBeri, cmbMnt);
    }//GEN-LAST:event_cmbJamKeyPressed

    private void cmbMntKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbMntKeyPressed
    {//GEN-HEADEREND:event_cmbMntKeyPressed
        Valid.pindah(evt, cmbJam, cmbDtk);
    }//GEN-LAST:event_cmbMntKeyPressed

    private void cmbDtkKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_cmbDtkKeyPressed
    {//GEN-HEADEREND:event_cmbDtkKeyPressed

    }//GEN-LAST:event_cmbDtkKeyPressed

    private void btnCariDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariDokterActionPerformed
        cariDokter();
    }//GEN-LAST:event_btnCariDokterActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.CekBox ChkJln;
    private widget.Tanggal DTPBeri;
    private widget.Button btnBaru;
    private widget.Button btnCariDetail;
    private widget.Button btnCariDokter;
    private widget.Button btnCariGroup;
    private widget.Button btnCariKategori;
    private widget.Button btnCetak;
    private widget.Button btnHapus;
    private widget.Button btnKeluar;
    private widget.Button btnSimpan;
    private widget.ComboBox cmbDtk;
    private widget.ComboBox cmbJam;
    private widget.ComboBox cmbMnt;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel2;
    private widget.Label label1;
    private widget.Label label2;
    private widget.Label label3;
    private widget.Label label4;
    private widget.Label label5;
    private widget.panelisi panelisi1;
    private widget.panelisi pnlAction;
    private widget.PanelBiasa pnlInput;
    private widget.ScrollPane scrollPane;
    private widget.Table tblOrder;
    private widget.TextBox txtKdDetail;
    private widget.TextBox txtKdDokter;
    private widget.TextBox txtKdGroup;
    private widget.TextBox txtKdKategori;
    private widget.TextBox txtNamaDetail;
    private widget.TextBox txtNamaDokter;
    private widget.TextBox txtNamaGroup;
    private widget.TextBox txtNamaKategori;
    private widget.TextBox txtNamaPasien;
    private widget.TextBox txtNoRawat;
    private widget.TextBox txtNoRm;
    // End of variables declaration//GEN-END:variables

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
                    nilai_jam = cmbJam.getSelectedIndex();
                    nilai_menit = cmbMnt.getSelectedIndex();
                    nilai_detik = cmbDtk.getSelectedIndex();
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
                cmbJam.setSelectedItem(jam);
                cmbMnt.setSelectedItem(menit);
                cmbDtk.setSelectedItem(detik);
            }
        };
        // Timer
        new Timer(1000, taskPerformer).start();
    }
}