/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simrskhanza;

import base.BaseDialog;
import fungsi.GQuery;
import fungsi.GStatement;
import fungsi.WarnaTable;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import util.GMessage;

/**
 *
 * @author GrT
 */
public class DlgPemeriksaanOperasi2 extends BaseDialog
{
    // Const =========
    private static final int DOK_OPERATOR = 0;
    private static final int DOK_MERAWAT = 1;
    private static final int ASS_DOK_OPERATOR = 2;
    private static final int DOK_ANESTESI = 3;
    private static final int PENATA_ANESTESI = 4;
    private static final int DOK_ANAK = 5;
    private static final int DOK_PENDAMPING = 6;
    private static final int PERAWAT_OR_BIDAN = 7;

    // Child Forms ==========
    DlgCariGroupOperasi dlgGroup = new DlgCariGroupOperasi(null, false);
    DlgCariKategoriOperasi dlgKategori = new DlgCariKategoriOperasi(null, false);
    DlgCariDetailOperasi dlgDetail = new DlgCariDetailOperasi(null, false);
    DlgCariNoRawat2 dlgCariNoRawat = new DlgCariNoRawat2(null, false);
    private DlgCariPetugas dlgPetugas = new DlgCariPetugas(null, false);
    private DlgCariDokter dlgDokter = new DlgCariDokter(null, false);

    // Funcs ====
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private Connection koneksi = koneksiDB.condb();
    
    private DefaultTableModel mdlOrder, mdlTransaksi;
    private GStatement psOrder, psTransaksi;
    private ResultSet rsTransaksi, rsOrder;

    // Vars ==========
    private int pil;
    private String kelas;
    private String status;
    private String kdOperasi;

    /**
     * Creates new form DlgOperasi
     */
    public DlgPemeriksaanOperasi2(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        
        jam();
        
        addWindowClosedListener();
        addTextChangedListener();
        
        initTblOrder();
        initTblTransaksi();
        
        tampilOrder();
        tampilTransaksi();
    }

    // Init method
    private void addWindowClosedListener()
    {
        dlgCariNoRawat.addWindowClosedListener(() -> 
        {
            JTable table = dlgCariNoRawat.getTable();
            
            if (table.getSelectedRow() != -1)
            {
                txtNoRawat.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
                txtNoRm.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
                txtNamaPasien.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
            }
        });
        
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
        
        dlgPetugas.addWindowClosedListener(() ->
        {
            if (dlgPetugas.getTable().getSelectedRow() != -1)
            {
                if (pil == DOK_OPERATOR)
                {
                    txtKdDokOperator.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaDokOperator.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdDokOperator.requestFocus();
                }
                else if (pil == DOK_MERAWAT)
                {
                    txtKdDokMerawat.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaDokMerawat.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdDokMerawat.requestFocus();
                }
                else if (pil == ASS_DOK_OPERATOR)
                {
                    txtKdAssDokOperator.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaAssDokOperator.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdAssDokOperator.requestFocus();
                }
                else if (pil == DOK_ANESTESI)
                {
                    txtKdDokAnestesi.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaDokAnestesi.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdDokAnestesi.requestFocus();
                }
                else if (pil == PENATA_ANESTESI)
                {
                    txtKdPenataAnestesi.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaPenataAnestesi.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdPenataAnestesi.requestFocus();
                }
                else if (pil == DOK_ANAK)
                {
                    txtKdDokAnak.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaDokAnak.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdDokAnak.requestFocus();
                }
                else if (pil == DOK_PENDAMPING)
                {
                    txtKdDokPendamping.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaDokPendamping.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdDokPendamping.requestFocus();
                }
                else if (pil == PERAWAT_OR_BIDAN)
                {
                    txtKdPerawatOrBidan.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaPerawatOrBidan.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdPerawatOrBidan.requestFocus();
                }
            }
        });
        
        dlgDokter.addWindowClosedListener(() ->
        {
            if (pil == DOK_OPERATOR)
            {
                txtKdDokOperator.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 0).toString());
                txtNamaDokOperator.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 1).toString());
                txtKdDokOperator.requestFocus();
            }
            else if (pil == DOK_MERAWAT)
            {
                txtKdDokMerawat.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 0).toString());
                txtNamaDokMerawat.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 1).toString());
                txtKdDokMerawat.requestFocus();
            }
            else if (pil == ASS_DOK_OPERATOR)
            {
                txtKdAssDokOperator.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 0).toString());
                txtNamaAssDokOperator.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 1).toString());
                txtKdAssDokOperator.requestFocus();
            }
            else if (pil == DOK_ANESTESI)
            {
                txtKdDokAnestesi.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 0).toString());
                txtNamaDokAnestesi.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 1).toString());
                txtKdDokAnestesi.requestFocus();
            }
            else if (pil == PENATA_ANESTESI)
            {
                txtKdPenataAnestesi.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 0).toString());
                txtNamaPenataAnestesi.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 1).toString());
                txtKdPenataAnestesi.requestFocus();
            }
            else if (pil == DOK_ANAK)
            {
                txtKdDokAnak.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 0).toString());
                txtNamaDokAnak.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 1).toString());
                txtKdDokAnak.requestFocus();
            }
            else if (pil == DOK_PENDAMPING)
            {
                txtKdDokPendamping.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 0).toString());
                txtNamaDokPendamping.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 1).toString());
                txtKdDokPendamping.requestFocus();
            }
            else if (pil == PERAWAT_OR_BIDAN)
            {
                txtKdPerawatOrBidan.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 0).toString());
                txtNamaPerawatOrBidan.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 1).toString());
                txtKdPerawatOrBidan.requestFocus();
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
        
        txtKdDokOperator.addTextChangedListener((t) -> txtNamaDokOperator.setText(""));
        txtKdDokMerawat.addTextChangedListener((t) -> txtNamaDokMerawat.setText(""));
        txtKdAssDokOperator.addTextChangedListener((t) -> txtNamaAssDokOperator.setText(""));
        txtKdDokAnestesi.addTextChangedListener((t) -> txtNamaDokAnestesi.setText(""));
        txtKdPenataAnestesi.addTextChangedListener((t) -> txtNamaPenataAnestesi.setText(""));
        txtKdDokAnak.addTextChangedListener((t) -> txtNamaDokAnak.setText(""));
        txtKdDokPendamping.addTextChangedListener((t) -> txtNamaDokPendamping.setText(""));
        txtKdPerawatOrBidan.addTextChangedListener((t) -> txtNamaPerawatOrBidan.setText(""));
    }
    
    private void initTblOrder()
    {
        Object[] row =
        {
            "Kd Order", "Kd Group", "Kd Kategori", "Kd Detail", "No Rawat", "Pasien", "Group", "Kategori", "Detail", "Tgl Operasi"
        };
        
        int[] sz = 
        {
            80, 0, 0, 0, 120, 300, 200, 200, 200, 150
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

        for (int i = 0; i < sz.length; i++)
        {
            TableColumn column = tblOrder.getColumnModel().getColumn(i);
            
            if (sz[i] == 0)
            {
                column.setMinWidth(sz[i]);
                column.setMaxWidth(sz[i]);
            }
            else
            {
                column.setPreferredWidth(sz[i]);
            }
        }
        
        tblOrder.setDefaultRenderer(Object.class, new WarnaTable());
        
        psOrder = new GStatement(koneksi)
                .a("SELECT kd_operasi, operasi.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, kamar.kd_kamar, bangsal.nm_bangsal, poliklinik.nm_poli,")
                .a("    operasi_group.kd_group, operasi_kategori.kd_kategori, operasi_detail.kd_detail, nm_group, nm_kategori, nm_detail, tgl_operasi, jam_operasi")
                .a("FROM operasi")
                .a("JOIN operasi_detail ON operasi_detail.kd_detail = operasi.kd_detail")
                .a("JOIN operasi_kategori ON operasi_kategori.kd_kategori = operasi_detail.kd_kategori")
                .a("JOIN operasi_group ON operasi_group.kd_group = operasi_kategori.kd_group")
                .a("JOIN reg_periksa ON reg_periksa.no_rawat = operasi.no_rawat")
                .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                .a("LEFT JOIN kamar_inap ON kamar_inap.no_rawat = operasi.no_rawat")
                .a("LEFT JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                .a("LEFT JOIN bangsal ON bangsal.kd_bangsal = kamar.kd_bangsal")
                .a("LEFT JOIN poliklinik ON poliklinik.kd_poli = reg_periksa.kd_poli")
                .a("WHERE DATE(tgl_operasi) BETWEEN :tgl1 AND :tgl2 AND proses = 'Belum'")
                .a("ORDER BY tgl_operasi");
    }
    
    private void initTblTransaksi()
    {
        Object[] row =
        {
            "Kd Operasi", "Kd Group", "Kd Kategori", "Kd Detail", "No Rawat", "Pasien", "Group", "Kategori", "Detail", "Tgl Operasi", "Tgl Selesai"
        };
        
        int[] sz = 
        {
            80, 0, 0, 0, 120, 300, 200, 200, 200, 150, 150
        };
        
        mdlTransaksi = new DefaultTableModel(null, row)
        {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                return false;
            }
        };
        
        tblTransaksi.setModel(mdlTransaksi);

        tblTransaksi.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tblTransaksi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < sz.length; i++)
        {
            TableColumn column = tblTransaksi.getColumnModel().getColumn(i);
            
            if (sz[i] == 0)
            {
                column.setMinWidth(sz[i]);
                column.setMaxWidth(sz[i]);
            }
            else
            {
                column.setPreferredWidth(sz[i]);
            }
        }
        
        tblTransaksi.setDefaultRenderer(Object.class, new WarnaTable());
        
        psTransaksi = new GStatement(koneksi)
                .a("SELECT kd_operasi, operasi.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, kamar.kd_kamar, bangsal.nm_bangsal, poliklinik.nm_poli,")
                .a("    operasi_group.kd_group, operasi_kategori.kd_kategori, operasi_detail.kd_detail, nm_group, nm_kategori, nm_detail, tgl_operasi, jam_operasi, tgl_selesai, jam_selesai")
                .a("FROM operasi")
                .a("JOIN operasi_detail ON operasi_detail.kd_detail = operasi.kd_detail")
                .a("JOIN operasi_kategori ON operasi_kategori.kd_kategori = operasi_detail.kd_kategori")
                .a("JOIN operasi_group ON operasi_group.kd_group = operasi_kategori.kd_group")
                .a("JOIN reg_periksa ON reg_periksa.no_rawat = operasi.no_rawat")
                .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                .a("LEFT JOIN kamar_inap ON kamar_inap.no_rawat = operasi.no_rawat")
                .a("LEFT JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                .a("LEFT JOIN bangsal ON bangsal.kd_bangsal = kamar.kd_bangsal")
                .a("LEFT JOIN poliklinik ON poliklinik.kd_poli = reg_periksa.kd_poli")
                .a("WHERE DATE(tgl_operasi) BETWEEN :tgl1 AND :tgl2 AND proses = 'Sudah'")
                .a("ORDER BY tgl_operasi");
    }
    
    // Methods ==========
    private void cariNoRawat()
    {
        dlgCariNoRawat.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        dlgCariNoRawat.setLocationRelativeTo(internalFrame1);
        dlgCariNoRawat.setVisible(true);
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
    
    private void cariPetugas(int pil)
    {
        this.pil = pil;
        
        dlgPetugas.emptTeks();
        dlgPetugas.isCek();
        dlgPetugas.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        dlgPetugas.setLocationRelativeTo(internalFrame1);
        dlgPetugas.setVisible(true);
    }

    private void cariDokter(int pil)
    {
        this.pil = pil;
        
        dlgDokter.emptTeks();
        dlgDokter.isCek();
        dlgDokter.setSize(internalFrame1.getWidth()-40,internalFrame1.getHeight()-40);
        dlgDokter.setLocationRelativeTo(internalFrame1);
        dlgDokter.setVisible(true);
    }
    
    private void tindakanFromOrder(String s)
    {
        kdOperasi = s;
        
        txtNoRawat.setText(tblOrder.getValueAt(tblOrder.getSelectedRow(), 4).toString());
        Sequel.cariIsi("SELECT no_rkm_medis FROM reg_periksa WHERE no_rawat = ?", txtNoRm, txtNoRawat.getText());
        Sequel.cariIsi("SELECT nm_pasien FROM pasien WHERE no_rkm_medis = ?", txtNamaPasien, txtNoRm.getText());
        
        txtKdGroup.setText(tblOrder.getValueAt(tblOrder.getSelectedRow(), 1).toString());
        txtKdKategori.setText(tblOrder.getValueAt(tblOrder.getSelectedRow(), 2).toString());
        txtKdDetail.setText(tblOrder.getValueAt(tblOrder.getSelectedRow(), 3).toString());
        txtNamaGroup.setText(tblOrder.getValueAt(tblOrder.getSelectedRow(), 6).toString());
        txtNamaKategori.setText(tblOrder.getValueAt(tblOrder.getSelectedRow(), 7).toString());
        txtNamaDetail.setText(tblOrder.getValueAt(tblOrder.getSelectedRow(), 8).toString());
        
        cariKelas();
    }
    
    private void tindakanFromTransaksi(String s)
    {
        kdOperasi = s;
        btnSimpan.setText("Ubah");
        
        txtNoRawat.setText(tblTransaksi.getValueAt(tblTransaksi.getSelectedRow(), 4).toString());
        Sequel.cariIsi("SELECT no_rkm_medis FROM reg_periksa WHERE no_rawat = ?", txtNoRm, txtNoRawat.getText());
        Sequel.cariIsi("SELECT nm_pasien FROM pasien WHERE no_rkm_medis = ?", txtNamaPasien, txtNoRm.getText());
        
        txtKdGroup.setText(tblTransaksi.getValueAt(tblTransaksi.getSelectedRow(), 1).toString());
        txtKdKategori.setText(tblTransaksi.getValueAt(tblTransaksi.getSelectedRow(), 2).toString());
        txtKdDetail.setText(tblTransaksi.getValueAt(tblTransaksi.getSelectedRow(), 3).toString());
        txtNamaGroup.setText(tblTransaksi.getValueAt(tblTransaksi.getSelectedRow(), 6).toString());
        txtNamaKategori.setText(tblTransaksi.getValueAt(tblTransaksi.getSelectedRow(), 7).toString());
        txtNamaDetail.setText(tblTransaksi.getValueAt(tblTransaksi.getSelectedRow(), 8).toString());
        
        HashMap<String, String> m = new GQuery()
                .a("SELECT * FROM operasi WHERE kd_operasi = {id}")
                .set("id", tblTransaksi.getValueAt(tblTransaksi.getSelectedRow(), 0).toString())
                .getRowWithName();
        
        txtJenisAnasthesia.setText(m.get("jenis_anasthesi"));
        txtKdDokOperator.setText(m.get("operator1"));
        txtKdDokMerawat.setText(m.get("dokter_pjanak"));
        txtKdAssDokOperator.setText(m.get("asisten_operator1"));
        txtKdDokAnestesi.setText(m.get("dokter_anestesi"));
        txtKdPenataAnestesi.setText(m.get("asisten_anestesi"));
        txtKdDokAnak.setText(m.get("dokter_anak"));
        txtKdDokPendamping.setText(m.get("dokter_umum"));
        txtKdPerawatOrBidan.setText(m.get("bidan"));
        
        Sequel.cariIsi("SELECT nm_dokter FROM dokter WHERE kd_dokter = ?", txtNamaDokOperator, txtKdDokOperator.getText());
        Sequel.cariIsi("SELECT nm_dokter FROM dokter WHERE kd_dokter = ?", txtNamaDokMerawat, txtKdDokMerawat.getText());
        Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", txtNamaAssDokOperator, txtKdAssDokOperator.getText());
        Sequel.cariIsi("SELECT nm_dokter FROM dokter WHERE kd_dokter = ?", txtNamaDokAnestesi, txtKdDokAnestesi.getText());
        Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", txtNamaPenataAnestesi, txtKdPenataAnestesi.getText());
        Sequel.cariIsi("SELECT nm_dokter FROM dokter WHERE kd_dokter = ?", txtNamaDokAnak, txtKdDokAnak.getText());
        Sequel.cariIsi("SELECT nm_dokter FROM dokter WHERE kd_dokter = ?", txtNamaDokPendamping, txtKdDokPendamping.getText());
        Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", txtNamaPerawatOrBidan, txtKdPerawatOrBidan.getText());
        
        cariKelas();
    }
    
    private void simpan()
    {
        if (!valid())
            return;
        
        int reply = JOptionPane.showConfirmDialog(rootPane, "Eeiiiiiits, udah bener belum data yang mau disimpan..??", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        
        if (reply != JOptionPane.YES_OPTION)
            return;
        
        HashMap<String, String> paket = getPaket();
        
        boolean success;
        
        success = new GQuery()
                .a("UPDATE operasi SET")
                .a("kd_detail = {kd_detail},")
                .a("kode_paket = {kode_paket},")
                .a("jenis_anasthesi = {jenis_anasthesi},")
                // operator yg ada
                .a("operator1 = {dok_operator},")
                .a("asisten_operator1 = {ass_dok_operator},")
                .a("dokter_anak = {dok_anak},")
                .a("dokter_anestesi = {dok_anestesi},")
                .a("asisten_anestesi = {penata_anestesi},")
                .a("bidan = {perawat_or_bidan},")
                .a("dokter_pjanak = {dok_merawat},")
                .a("dokter_umum = {dok_pendamping},")
                // operator yg gk ada
                .a("operator2 = '-',")
                .a("operator3 = '-',")
                .a("asisten_operator2 = '-',")
                .a("asisten_operator3 = '-',")
                .a("instrumen = '-',")
                .a("perawaat_resusitas = '-',")
                .a("asisten_anestesi2 = '-',")
                .a("bidan2 = '-',")
                .a("bidan3 = '-',")
                .a("perawat_luar = '-',")
                .a("omloop = '-',")
                .a("omloop2 = '-',")
                .a("omloop3 = '-',")
                .a("omloop4 = '-',")
                .a("omloop5 = '-',")
                // biaya yg ada
                .a("biayaoperator1 = {biaya_dok_operator},")
                .a("biayaasisten_operator1 = {biaya_ass_dok_operator},")
                .a("biayadokter_anak = {biaya_dok_anak},")
                .a("biayadokter_anestesi = {biaya_dok_anestesi},")
                .a("biayaasisten_anestesi = {biaya_penata_anestesi},")
                .a("biayabidan = {biaya_perawat_or_bidan},")
                .a("biaya_dokter_pjanak = {biaya_dok_merawat},")
                .a("biaya_dokter_umum = {biaya_dok_pendamping},")
                // biaya yg gk ada
                .a("biayaoperator2 = '0',")
                .a("biayaoperator3 = '0',")
                .a("biayaasisten_operator2 = '0',")
                .a("biayaasisten_operator3 = '0',")
                .a("biayainstrumen = '0',")
                .a("biayaperawaat_resusitas = '0',")
                .a("biayaasisten_anestesi2 = '0',")
                .a("biayabidan2 = '0',")
                .a("biayabidan3 = '0',")
                .a("biayaperawat_luar = '0',")
                .a("biayaalat = {biayaalat},")
                .a("biayasewaok = {biayasewaok},")
                .a("akomodasi = {akomodasi},")
                .a("bagian_rs = {bagian_rs},")
                .a("biaya_omloop = '0',")
                .a("biaya_omloop2 = '0',")
                .a("biaya_omloop3 = '0',")
                .a("biaya_omloop4 = '0',")
                .a("biaya_omloop5 = '0',")
                .a("biayasarpras = {biayasarpras},")
                // dll
                .a("status = {status},")
                .a("tgl_selesai = {tgl_selesai},")
                .a("jam_selesai = {jam_selesai},")
                .a("proses = 'Sudah'")
                .a("WHERE kd_operasi = {kd_operasi}")
                .set("kd_operasi", kdOperasi)
                .set("kd_detail", txtKdDetail.getText())
                .set("kode_paket", paket.get("kode_paket"))
                .set("jenis_anasthesi", txtJenisAnasthesia.getText())
                // operator
                .set("dok_operator", txtKdDokOperator.getText().isEmpty() ? "-" : txtKdDokOperator.getText())
                .set("dok_merawat", txtKdDokMerawat.getText().isEmpty() ? "-" : txtKdDokMerawat.getText())
                .set("ass_dok_operator", txtKdAssDokOperator.getText().isEmpty() ? "-" : txtKdAssDokOperator.getText())
                .set("penata_anestesi", txtKdPenataAnestesi.getText().isEmpty() ? "-" : txtKdPenataAnestesi.getText())
                .set("dok_anestesi", txtKdDokAnestesi.getText().isEmpty() ? "-" : txtKdDokAnestesi.getText())
                .set("dok_anak", txtKdDokAnak.getText().isEmpty() ? "-" : txtKdDokAnak.getText())
                .set("dok_pendamping", txtKdDokPendamping.getText().isEmpty() ? "-" : txtKdDokPendamping.getText())
                .set("perawat_or_bidan", txtKdPerawatOrBidan.getText().isEmpty() ? "-" : txtKdPerawatOrBidan.getText())
                // biaya
                .set("biaya_dok_operator", paket.get("operator1"))
                .set("biaya_ass_dok_operator", paket.get("asisten_operator1"))
                .set("biaya_dok_anak", paket.get("dokter_anak"))
                .set("biaya_dok_anestesi", paket.get("dokter_anestesi"))
                .set("biaya_penata_anestesi", paket.get("asisten_anestesi"))
                .set("biaya_perawat_or_bidan", paket.get("bidan"))
                .set("biaya_dok_merawat", paket.get("dokter_pjanak"))
                .set("biaya_dok_pendamping", paket.get("dokter_umum"))
                .set("biayaalat", paket.get("alat"))
                .set("biayasewaok", paket.get("sewa_ok"))
                .set("akomodasi", paket.get("akomodasi"))
                .set("bagian_rs", paket.get("bagian_rs"))
                .set("biayasarpras", paket.get("sarpras"))
                // dll
                .set("status", status)
                .set("tgl_selesai", Valid.SetTgl(DTPBeri.getSelectedItem().toString()))
                .set("jam_selesai", cmbJam.getSelectedItem() + ":" + cmbMnt.getSelectedItem() + ":" + cmbDtk.getSelectedItem())
                .write();
        
        if (success)
        {
            GMessage.i("Sukses", "Simpan data berhasil");
            
            baru();
            tampilOrder();
            tampilTransaksi();
        }
        else
        {
            GMessage.e("Error", "Error saat menyimpan data");
        }
    }
    
    private void baru()
    {
        btnSimpan.setText("Simpan");
        
        txtNoRawat.setText("");
        txtNoRm.setText("");
        txtNamaPasien.setText("");
        txtKdGroup.setText("");
        txtNamaGroup.setText("");
        txtKdKategori.setText("");
        txtNamaKategori.setText("");
        txtKdDetail.setText("");
        txtNamaDetail.setText("");
        txtJenisAnasthesia.setText("");
        
        txtKdDokOperator.setText("");
        txtNamaDokOperator.setText("");
        txtKdDokMerawat.setText("");
        txtNamaDokMerawat.setText("");
        txtKdAssDokOperator.setText("");
        txtNamaAssDokOperator.setText("");
        txtKdDokAnestesi.setText("");
        txtNamaDokAnestesi.setText("");
        txtKdPenataAnestesi.setText("");
        txtNamaPenataAnestesi.setText("");
        txtKdDokAnak.setText("");
        txtNamaDokAnak.setText("");
        txtKdDokPendamping.setText("");
        txtNamaDokPendamping.setText("");
        txtKdPerawatOrBidan.setText("");
        txtNamaPerawatOrBidan.setText("");
    }
    
    private void tampilOrder()
    {
        try 
        {
            Valid.tabelKosong(mdlOrder);
            psOrder.setString("tgl1", Valid.SetTgl(tglOrder1.getSelectedItem().toString()));
            psOrder.setString("tgl2", Valid.SetTgl(tglOrder2.getSelectedItem().toString()));
            rsOrder = psOrder.executeQuery();
            
            while (rsOrder.next())
            {
                String pas;
                
                if (rsOrder.getString("kd_kamar") != null)
                {
                    pas = rsOrder.getString("no_rkm_medis") + " " + rsOrder.getString("nm_pasien") + " (Kamar : " + 
                        rsOrder.getString("kd_kamar") + ", " + rsOrder.getString("nm_bangsal") + ")";
                }
                else
                {
                    pas = rsOrder.getString("no_rkm_medis") + " " + rsOrder.getString("nm_pasien") + " (Poli : " + 
                        rsOrder.getString("nm_poli") + ")";
                }
                
                Object[] o = new Object[]
                {
                    rsOrder.getString("kd_operasi"),
                    rsOrder.getString("kd_group"),
                    rsOrder.getString("kd_kategori"),
                    rsOrder.getString("kd_detail"),
                    rsOrder.getString("no_rawat"),
                    pas,
                    rsOrder.getString("nm_group"),
                    rsOrder.getString("nm_kategori"),
                    rsOrder.getString("nm_detail"),
                    rsOrder.getString("tgl_operasi") + " " + rsOrder.getString("jam_operasi")
                };
                
                mdlOrder.addRow(o);
            }
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
        }
    }
    
    private void tampilTransaksi()
    {
        try 
        {
            Valid.tabelKosong(mdlTransaksi);
            psTransaksi.setString("tgl1", Valid.SetTgl(tglTransaksi1.getSelectedItem().toString()));
            psTransaksi.setString("tgl2", Valid.SetTgl(tglTransaksi2.getSelectedItem().toString()));
            rsTransaksi = psTransaksi.executeQuery();
            
            while (rsTransaksi.next())
            {
                String pas;
                
                if (rsTransaksi.getString("kd_kamar") != null)
                {
                    pas = rsTransaksi.getString("no_rkm_medis") + " " + rsTransaksi.getString("nm_pasien") + " (Kamar : " + 
                        rsTransaksi.getString("kd_kamar") + ", " + rsTransaksi.getString("nm_bangsal") + ")";
                }
                else
                {
                    pas = rsTransaksi.getString("no_rkm_medis") + " " + rsTransaksi.getString("nm_pasien") + " (Poli : " + 
                        rsTransaksi.getString("nm_poli") + ")";
                }
                
                Object[] o = new Object[]
                {
                    rsTransaksi.getString("kd_operasi"),
                    rsTransaksi.getString("kd_group"),
                    rsTransaksi.getString("kd_kategori"),
                    rsTransaksi.getString("kd_detail"),
                    rsTransaksi.getString("no_rawat"),
                    pas,
                    rsTransaksi.getString("nm_group"),
                    rsTransaksi.getString("nm_kategori"),
                    rsTransaksi.getString("nm_detail"),
                    rsTransaksi.getString("tgl_operasi") + " " + rsTransaksi.getString("jam_operasi"),
                    rsTransaksi.getString("tgl_selesai") + " " + rsTransaksi.getString("jam_selesai")
                };
                
                mdlTransaksi.addRow(o);
            }
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
        }
    }

    
    private boolean valid()
    {
        if (txtNoRawat.getText().toString().isEmpty())
        {
            GMessage.w("Salah", "Pilih pasien dari order atau transaksi");
            return false;
        }
        else if (txtNamaGroup.getText().trim().isEmpty())
        {
            GMessage.w("Salah", "Pilih group dulu");
            return false;
        }
        else if (txtNamaKategori.getText().trim().isEmpty())
        {
            GMessage.w("Salah", "Pilih kategori dulu");
            return false;
        }
        else if (txtNamaDetail.getText().trim().isEmpty())
        {
            GMessage.w("Salah", "Pilih detail dulu");
            return false;
        }
        else if (txtJenisAnasthesia.getText().trim().isEmpty())
        {
            GMessage.w("Salah", "Jenis Anasthesia harus diisi");
            return false;
        }
        else if (txtNamaDokOperator.getText().trim().isEmpty())
        {
            GMessage.w("Salah", "Pilih Operator 1 dulu");
            return false;
        }
//        else if (txtNamaOperator2.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Operator 2 dulu");
//            return false;
//        }
//        else if (txtNamaOperator3.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Operator 3 dulu");
//            return false;
//        }
//        else if (txtNamaAsistenOperator1.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Asisten Operator 1 dulu");
//            return false;
//        }
//        else if (txtNamaAsistenOperator2.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Asisten Operator 2 dulu");
//            return false;
//        }
//        else if (txtNamaAsistenOperator3.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Asisten Operator 3 dulu");
//            return false;
//        }
//        else if (txtNamaAnestesia.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Dr Anestesia dulu");
//            return false;
//        }
//        else if (txtNamaAsistenAnestesia1.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Asisten Anestesia 1 dulu");
//            return false;
//        }
//        else if (txtNamaAsistenAnestesia2.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Asisten Anestesia 2 dulu");
//            return false;
//        }
//        else if (txtNamaDrAnak.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Dr Anak dulu");
//            return false;
//        }
//        else if (txtNamaBidan1.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Bidan 1 dulu");
//            return false;
//        }
//        else if (txtNamaBidan2.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Bidan 2 dulu");
//            return false;
//        }
//        else if (txtNamaBidan3.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Bidan 3 dulu");
//            return false;
//        }
//        else if (txtNamaPerawatLuar.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Perawat luar dulu");
//            return false;
//        }
//        else if (txtNamaPrResus.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Pr Resus dulu");
//            return false;
//        }
//        else if (txtNamaInstrumen.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Instrumen dulu");
//            return false;
//        }
//        else if (txtNamaPjAnak.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Dr Pj Anak dulu");
//            return false;
//        }
//        else if (txtNamaDrUmum.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Dr Umum dulu");
//            return false;
//        }
//        else if (txtNamaOnloop1.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Onloop 1 dulu");
//            return false;
//        }
//        else if (txtNamaOnloop2.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Onloop 2 dulu");
//            return false;
//        }
//        else if (txtNamaOnloop3.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Onloop 3 dulu");
//            return false;
//        }
//        else if (txtNamaOnloop4.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Onloop 4 dulu");
//            return false;
//        }
//        else if (txtNamaOnloop5.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih Onloop 5 dulu");
//            return false;
//        }
        else
        {
            return true;
        }
    }
    
    private void cariKelas()
    {
        String s = new GQuery()
            .a("SELECT kelas")
            .a("FROM kamar_inap")
            .a("JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
            .a("WHERE no_rawat = {no_rawat}")
            .set("no_rawat", txtNoRawat.getText())
            .getString();
        
        if (s.isEmpty())
        {
            kelas = "Kelas 3";
            status = "Ralan";
        }
        else
        {
            kelas = s;
            status = "Ranap";
        }
    }
    
    private HashMap<String, String> getPaket()
    {
        return new GQuery()
                .a("SELECT * FROM paket_operasi")
                .a("WHERE operasi_kategori = {kat} AND kelas = {kls}")
                .set("kat", txtKdKategori.getText())
                .set("kls", kelas)
                .getRowWithName();
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
    
    // <editor-fold defaultstate="collapsed" desc=" GENERATED ">
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        internalFrame1 = new widget.InternalFrame();
        tabPane = new widget.TabPane();
        panelBiasa1 = new widget.PanelBiasa();
        pnlInput = new widget.PanelBiasa();
        txtNamaKategori = new widget.TextBox();
        txtKdKategori = new widget.TextBox();
        label3 = new widget.Label();
        btnCariKategori = new widget.Button();
        label1 = new widget.Label();
        btnCariGroup = new widget.Button();
        txtNoRawat = new widget.TextBox();
        btnCariDetail = new widget.Button();
        txtNoRm = new widget.TextBox();
        txtNamaDetail = new widget.TextBox();
        txtNamaPasien = new widget.TextBox();
        txtKdDetail = new widget.TextBox();
        txtNamaGroup = new widget.TextBox();
        label4 = new widget.Label();
        txtKdGroup = new widget.TextBox();
        label2 = new widget.Label();
        label14 = new widget.Label();
        txtKdDokOperator = new widget.TextBox();
        txtNamaDokOperator = new widget.TextBox();
        btnDokOperator = new widget.Button();
        jLabel4 = new widget.Label();
        txtJenisAnasthesia = new widget.TextBox();
        label19 = new widget.Label();
        txtKdDokMerawat = new widget.TextBox();
        txtNamaDokMerawat = new widget.TextBox();
        btnDokMerawat = new widget.Button();
        label20 = new widget.Label();
        txtKdAssDokOperator = new widget.TextBox();
        txtNamaAssDokOperator = new widget.TextBox();
        btnAssDokOperator = new widget.Button();
        label21 = new widget.Label();
        txtKdDokAnestesi = new widget.TextBox();
        txtNamaDokAnestesi = new widget.TextBox();
        btnDokAnestesi = new widget.Button();
        label22 = new widget.Label();
        txtKdPenataAnestesi = new widget.TextBox();
        txtNamaPenataAnestesi = new widget.TextBox();
        btnPenataAnestesi = new widget.Button();
        label27 = new widget.Label();
        txtKdDokAnak = new widget.TextBox();
        txtNamaDokAnak = new widget.TextBox();
        btnDokAnak = new widget.Button();
        btnDokPendamping = new widget.Button();
        txtNamaDokPendamping = new widget.TextBox();
        txtKdDokPendamping = new widget.TextBox();
        label29 = new widget.Label();
        label30 = new widget.Label();
        txtKdPerawatOrBidan = new widget.TextBox();
        txtNamaPerawatOrBidan = new widget.TextBox();
        btnPerawatOrBidan = new widget.Button();
        jLabel9 = new widget.Label();
        DTPBeri = new widget.Tanggal();
        cmbJam = new widget.ComboBox();
        cmbMnt = new widget.ComboBox();
        cmbDtk = new widget.ComboBox();
        ChkJln = new widget.CekBox();
        btnCariNoRawat = new widget.Button();
        pnlAction = new widget.panelisi();
        btnSimpan = new widget.Button();
        btnBaru = new widget.Button();
        btnCetak = new widget.Button();
        jLabel10 = new widget.Label();
        btnKeluar = new widget.Button();
        panelBiasa2 = new widget.PanelBiasa();
        Scroll3 = new widget.ScrollPane();
        tblOrder = new widget.Table();
        jPanel6 = new javax.swing.JPanel();
        panelGlass13 = new widget.panelisi();
        jLabel37 = new widget.Label();
        tglOrder1 = new widget.Tanggal();
        jLabel38 = new widget.Label();
        tglOrder2 = new widget.Tanggal();
        btnCariOrder = new widget.Button();
        jLabel28 = new widget.Label();
        lblCountOrder = new widget.Label();
        btnHapusOrder = new widget.Button();
        panelBiasa3 = new widget.PanelBiasa();
        Scroll2 = new widget.ScrollPane();
        tblTransaksi = new widget.Table();
        jPanel5 = new javax.swing.JPanel();
        panelGlass12 = new widget.panelisi();
        jLabel35 = new widget.Label();
        tglTransaksi1 = new widget.Tanggal();
        jLabel36 = new widget.Label();
        tglTransaksi2 = new widget.Tanggal();
        btnCariTransaksi = new widget.Button();
        jLabel12 = new widget.Label();
        lblCountTransaksi = new widget.Label();
        btnHapusTransaksi = new widget.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "::[ Operasi ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame1.setPreferredSize(new java.awt.Dimension(971, 558));
        internalFrame1.setLayout(new java.awt.BorderLayout());

        panelBiasa1.setPreferredSize(new java.awt.Dimension(954, 553));
        panelBiasa1.setLayout(new java.awt.BorderLayout());

        pnlInput.setPreferredSize(new java.awt.Dimension(952, 501));

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

        label1.setText("No Rawat :");

        btnCariGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnCariGroup.setMnemonic('4');
        btnCariGroup.setToolTipText("ALt+4");
        btnCariGroup.setPreferredSize(new java.awt.Dimension(35, 22));
        btnCariGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariGroupActionPerformed(evt);
            }
        });

        txtNoRawat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNoRawatKeyPressed(evt);
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

        txtNoRm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNoRmKeyPressed(evt);
            }
        });

        txtNamaDetail.setEditable(false);

        txtNamaPasien.setEditable(false);
        txtNamaPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNamaPasienKeyPressed(evt);
            }
        });

        txtKdDetail.setEditable(false);

        txtNamaGroup.setEditable(false);

        label4.setText("Detail :");

        txtKdGroup.setEditable(false);

        label2.setText("Group :");

        label14.setText("Dokter Operator :");
        label14.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdDokOperator.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdDokOperator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKdDokOperatorActionPerformed(evt);
            }
        });
        txtKdDokOperator.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKdDokOperatorKeyPressed(evt);
            }
        });

        txtNamaDokOperator.setEditable(false);
        txtNamaDokOperator.setPreferredSize(new java.awt.Dimension(207, 23));

        btnDokOperator.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokOperator.setMnemonic('2');
        btnDokOperator.setToolTipText("Alt+2");
        btnDokOperator.setPreferredSize(new java.awt.Dimension(28, 23));
        btnDokOperator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDokOperatorActionPerformed(evt);
            }
        });

        jLabel4.setText("Jenis Anasthesi :");

        txtJenisAnasthesia.setHighlighter(null);
        txtJenisAnasthesia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtJenisAnasthesiaKeyPressed(evt);
            }
        });

        label19.setText("Dokter yg Merawat :");
        label19.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdDokMerawat.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdDokMerawat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKdDokMerawatKeyPressed(evt);
            }
        });

        txtNamaDokMerawat.setEditable(false);
        txtNamaDokMerawat.setPreferredSize(new java.awt.Dimension(207, 23));

        btnDokMerawat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokMerawat.setMnemonic('2');
        btnDokMerawat.setToolTipText("Alt+2");
        btnDokMerawat.setPreferredSize(new java.awt.Dimension(28, 23));
        btnDokMerawat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDokMerawatActionPerformed(evt);
            }
        });

        label20.setText("Ass Dokter Operator :");
        label20.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdAssDokOperator.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdAssDokOperator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKdAssDokOperatorActionPerformed(evt);
            }
        });
        txtKdAssDokOperator.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKdAssDokOperatorKeyPressed(evt);
            }
        });

        txtNamaAssDokOperator.setEditable(false);
        txtNamaAssDokOperator.setPreferredSize(new java.awt.Dimension(207, 23));

        btnAssDokOperator.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnAssDokOperator.setMnemonic('2');
        btnAssDokOperator.setToolTipText("Alt+2");
        btnAssDokOperator.setPreferredSize(new java.awt.Dimension(28, 23));
        btnAssDokOperator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAssDokOperatorActionPerformed(evt);
            }
        });

        label21.setText("Dokter Anestesi :");
        label21.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdDokAnestesi.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdDokAnestesi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKdDokAnestesiKeyPressed(evt);
            }
        });

        txtNamaDokAnestesi.setEditable(false);
        txtNamaDokAnestesi.setPreferredSize(new java.awt.Dimension(207, 23));

        btnDokAnestesi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokAnestesi.setMnemonic('2');
        btnDokAnestesi.setToolTipText("Alt+2");
        btnDokAnestesi.setPreferredSize(new java.awt.Dimension(28, 23));
        btnDokAnestesi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDokAnestesiActionPerformed(evt);
            }
        });

        label22.setText("Penata Anestesi :");
        label22.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdPenataAnestesi.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdPenataAnestesi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKdPenataAnestesiKeyPressed(evt);
            }
        });

        txtNamaPenataAnestesi.setEditable(false);
        txtNamaPenataAnestesi.setPreferredSize(new java.awt.Dimension(207, 23));

        btnPenataAnestesi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPenataAnestesi.setMnemonic('2');
        btnPenataAnestesi.setToolTipText("Alt+2");
        btnPenataAnestesi.setPreferredSize(new java.awt.Dimension(28, 23));
        btnPenataAnestesi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPenataAnestesiActionPerformed(evt);
            }
        });

        label27.setText("Dokter Anak :");
        label27.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdDokAnak.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdDokAnak.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKdDokAnakKeyPressed(evt);
            }
        });

        txtNamaDokAnak.setEditable(false);
        txtNamaDokAnak.setPreferredSize(new java.awt.Dimension(207, 23));

        btnDokAnak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokAnak.setMnemonic('2');
        btnDokAnak.setToolTipText("Alt+2");
        btnDokAnak.setPreferredSize(new java.awt.Dimension(28, 23));
        btnDokAnak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDokAnakActionPerformed(evt);
            }
        });

        btnDokPendamping.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokPendamping.setMnemonic('2');
        btnDokPendamping.setToolTipText("Alt+2");
        btnDokPendamping.setPreferredSize(new java.awt.Dimension(28, 23));
        btnDokPendamping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDokPendampingActionPerformed(evt);
            }
        });

        txtNamaDokPendamping.setEditable(false);
        txtNamaDokPendamping.setPreferredSize(new java.awt.Dimension(207, 23));

        txtKdDokPendamping.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdDokPendamping.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKdDokPendampingKeyPressed(evt);
            }
        });

        label29.setText("Dokter Pendamping :");
        label29.setPreferredSize(new java.awt.Dimension(70, 23));

        label30.setText("Perawat/Bidan :");
        label30.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdPerawatOrBidan.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdPerawatOrBidan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKdPerawatOrBidanKeyPressed(evt);
            }
        });

        txtNamaPerawatOrBidan.setEditable(false);
        txtNamaPerawatOrBidan.setPreferredSize(new java.awt.Dimension(207, 23));

        btnPerawatOrBidan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPerawatOrBidan.setMnemonic('2');
        btnPerawatOrBidan.setToolTipText("Alt+2");
        btnPerawatOrBidan.setPreferredSize(new java.awt.Dimension(28, 23));
        btnPerawatOrBidan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPerawatOrBidanActionPerformed(evt);
            }
        });

        jLabel9.setText("Tanggal :");

        DTPBeri.setEditable(false);
        DTPBeri.setForeground(new java.awt.Color(50, 70, 50));
        DTPBeri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-04-2018" }));
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

        btnCariNoRawat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnCariNoRawat.setMnemonic('4');
        btnCariNoRawat.setToolTipText("ALt+4");
        btnCariNoRawat.setPreferredSize(new java.awt.Dimension(35, 22));
        btnCariNoRawat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariNoRawatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlInputLayout = new javax.swing.GroupLayout(pnlInput);
        pnlInput.setLayout(pnlInputLayout);
        pnlInputLayout.setHorizontalGroup(
            pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInputLayout.createSequentialGroup()
                        .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInputLayout.createSequentialGroup()
                                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNoRawat, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNoRm, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNamaPasien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                                    .addComponent(txtNamaGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnCariDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnCariKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnCariGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnCariNoRawat, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlInputLayout.createSequentialGroup()
                        .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                            .addGroup(pnlInputLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(label14, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                                    .addComponent(label19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(label20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(label21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(label22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(label27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(label29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(label30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlInputLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdDokOperator, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaDokOperator, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnDokOperator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdDokMerawat, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaDokMerawat, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnDokMerawat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdAssDokOperator, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaAssDokOperator, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnAssDokOperator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdDokAnestesi, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaDokAnestesi, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnDokAnestesi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdPenataAnestesi, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaPenataAnestesi, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnPenataAnestesi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdDokAnak, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaDokAnak, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnDokAnak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdDokPendamping, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaDokPendamping, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnDokPendamping, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdPerawatOrBidan, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaPerawatOrBidan, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnPerawatOrBidan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(pnlInputLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtJenisAnasthesia, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30)
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
                .addContainerGap(224, Short.MAX_VALUE))
        );
        pnlInputLayout.setVerticalGroup(
            pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNoRawat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNoRm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNamaPasien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCariNoRawat, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addComponent(txtJenisAnasthesia, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(DTPBeri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cmbJam, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMnt, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbDtk, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ChkJln, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdDokOperator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaDokOperator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDokOperator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdDokMerawat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaDokMerawat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDokMerawat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdAssDokOperator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaAssDokOperator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAssDokOperator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdDokAnestesi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaDokAnestesi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDokAnestesi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdPenataAnestesi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaPenataAnestesi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPenataAnestesi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdDokAnak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaDokAnak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDokAnak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdDokPendamping, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaDokPendamping, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDokPendamping, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdPerawatOrBidan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaPerawatOrBidan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPerawatOrBidan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(99, Short.MAX_VALUE))
        );

        panelBiasa1.add(pnlInput, java.awt.BorderLayout.PAGE_START);

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

        panelBiasa1.add(pnlAction, java.awt.BorderLayout.PAGE_END);

        tabPane.addTab("Transaksi", panelBiasa1);

        panelBiasa2.setLayout(new java.awt.BorderLayout());

        Scroll3.setOpaque(true);

        tblOrder.setAutoCreateRowSorter(true);
        tblOrder.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tblOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOrderMouseClicked(evt);
            }
        });
        tblOrder.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblOrderKeyPressed(evt);
            }
        });
        Scroll3.setViewportView(tblOrder);

        panelBiasa2.add(Scroll3, java.awt.BorderLayout.CENTER);

        jPanel6.setOpaque(false);
        jPanel6.setPreferredSize(new java.awt.Dimension(44, 44));
        jPanel6.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass13.setMinimumSize(new java.awt.Dimension(50, 47));
        panelGlass13.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 10));

        jLabel37.setText("Tgl :");
        jLabel37.setPreferredSize(new java.awt.Dimension(58, 23));
        panelGlass13.add(jLabel37);

        tglOrder1.setEditable(false);
        tglOrder1.setForeground(new java.awt.Color(50, 70, 50));
        tglOrder1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-04-2018" }));
        tglOrder1.setDisplayFormat("dd-MM-yyyy");
        tglOrder1.setOpaque(false);
        tglOrder1.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass13.add(tglOrder1);

        jLabel38.setText("s.d");
        jLabel38.setPreferredSize(new java.awt.Dimension(18, 23));
        panelGlass13.add(jLabel38);

        tglOrder2.setEditable(false);
        tglOrder2.setForeground(new java.awt.Color(50, 70, 50));
        tglOrder2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-04-2018" }));
        tglOrder2.setDisplayFormat("dd-MM-yyyy");
        tglOrder2.setOpaque(false);
        tglOrder2.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass13.add(tglOrder2);

        btnCariOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        btnCariOrder.setMnemonic('6');
        btnCariOrder.setToolTipText("Alt+6");
        btnCariOrder.setPreferredSize(new java.awt.Dimension(28, 23));
        btnCariOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariOrderActionPerformed(evt);
            }
        });
        btnCariOrder.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnCariOrderKeyPressed(evt);
            }
        });
        panelGlass13.add(btnCariOrder);

        jLabel28.setText("Record :");
        jLabel28.setPreferredSize(new java.awt.Dimension(65, 23));
        panelGlass13.add(jLabel28);

        lblCountOrder.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCountOrder.setText("0");
        lblCountOrder.setPreferredSize(new java.awt.Dimension(45, 23));
        panelGlass13.add(lblCountOrder);

        btnHapusOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        btnHapusOrder.setMnemonic('H');
        btnHapusOrder.setText("Hapus");
        btnHapusOrder.setToolTipText("Alt+H");
        btnHapusOrder.setPreferredSize(new java.awt.Dimension(100, 30));
        btnHapusOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusOrderActionPerformed(evt);
            }
        });
        panelGlass13.add(btnHapusOrder);

        jPanel6.add(panelGlass13, java.awt.BorderLayout.PAGE_START);

        panelBiasa2.add(jPanel6, java.awt.BorderLayout.PAGE_END);

        tabPane.addTab("List Order", panelBiasa2);

        panelBiasa3.setLayout(new java.awt.BorderLayout());

        Scroll2.setOpaque(true);

        tblTransaksi.setAutoCreateRowSorter(true);
        tblTransaksi.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tblTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTransaksiMouseClicked(evt);
            }
        });
        tblTransaksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblTransaksiKeyPressed(evt);
            }
        });
        Scroll2.setViewportView(tblTransaksi);

        panelBiasa3.add(Scroll2, java.awt.BorderLayout.CENTER);

        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(44, 44));
        jPanel5.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass12.setMinimumSize(new java.awt.Dimension(50, 47));
        panelGlass12.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 10));

        jLabel35.setText("Tgl :");
        jLabel35.setPreferredSize(new java.awt.Dimension(58, 23));
        panelGlass12.add(jLabel35);

        tglTransaksi1.setEditable(false);
        tglTransaksi1.setForeground(new java.awt.Color(50, 70, 50));
        tglTransaksi1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-04-2018" }));
        tglTransaksi1.setDisplayFormat("dd-MM-yyyy");
        tglTransaksi1.setOpaque(false);
        tglTransaksi1.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass12.add(tglTransaksi1);

        jLabel36.setText("s.d");
        jLabel36.setPreferredSize(new java.awt.Dimension(18, 23));
        panelGlass12.add(jLabel36);

        tglTransaksi2.setEditable(false);
        tglTransaksi2.setForeground(new java.awt.Color(50, 70, 50));
        tglTransaksi2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-04-2018" }));
        tglTransaksi2.setDisplayFormat("dd-MM-yyyy");
        tglTransaksi2.setOpaque(false);
        tglTransaksi2.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass12.add(tglTransaksi2);

        btnCariTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        btnCariTransaksi.setMnemonic('6');
        btnCariTransaksi.setToolTipText("Alt+6");
        btnCariTransaksi.setPreferredSize(new java.awt.Dimension(28, 23));
        btnCariTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariTransaksiActionPerformed(evt);
            }
        });
        btnCariTransaksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnCariTransaksiKeyPressed(evt);
            }
        });
        panelGlass12.add(btnCariTransaksi);

        jLabel12.setText("Record :");
        jLabel12.setPreferredSize(new java.awt.Dimension(65, 23));
        panelGlass12.add(jLabel12);

        lblCountTransaksi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCountTransaksi.setText("0");
        lblCountTransaksi.setPreferredSize(new java.awt.Dimension(45, 23));
        panelGlass12.add(lblCountTransaksi);

        btnHapusTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        btnHapusTransaksi.setMnemonic('H');
        btnHapusTransaksi.setText("Hapus");
        btnHapusTransaksi.setToolTipText("Alt+H");
        btnHapusTransaksi.setPreferredSize(new java.awt.Dimension(100, 30));
        btnHapusTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusTransaksiActionPerformed(evt);
            }
        });
        panelGlass12.add(btnHapusTransaksi);

        jPanel5.add(panelGlass12, java.awt.BorderLayout.PAGE_START);

        panelBiasa3.add(jPanel5, java.awt.BorderLayout.PAGE_END);

        tabPane.addTab("List Transaksi", panelBiasa3);

        internalFrame1.add(tabPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSimpanActionPerformed
    {//GEN-HEADEREND:event_btnSimpanActionPerformed
        simpan();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnBaruActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnBaruActionPerformed
    {//GEN-HEADEREND:event_btnBaruActionPerformed
        baru();
    }//GEN-LAST:event_btnBaruActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnKeluarActionPerformed
    {//GEN-HEADEREND:event_btnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void tblTransaksiMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblTransaksiMouseClicked
    {//GEN-HEADEREND:event_tblTransaksiMouseClicked
        if (evt.getClickCount() == 2 && tblTransaksi.getSelectedRow() > -1)
        {
            tabPane.setSelectedIndex(0);

            tindakanFromTransaksi(tblTransaksi.getValueAt(tblTransaksi.getSelectedRow(), 0).toString());
        }
    }//GEN-LAST:event_tblTransaksiMouseClicked

    private void tblTransaksiKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_tblTransaksiKeyPressed
    {//GEN-HEADEREND:event_tblTransaksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTransaksiKeyPressed

    private void btnCariTransaksiActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCariTransaksiActionPerformed
    {//GEN-HEADEREND:event_btnCariTransaksiActionPerformed
        tampilTransaksi();
    }//GEN-LAST:event_btnCariTransaksiActionPerformed

    private void btnCariTransaksiKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnCariTransaksiKeyPressed
    {//GEN-HEADEREND:event_btnCariTransaksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCariTransaksiKeyPressed

    private void tblOrderMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblOrderMouseClicked
    {//GEN-HEADEREND:event_tblOrderMouseClicked
        if (evt.getClickCount() == 2 && tblOrder.getSelectedRow() > -1)
        {
            tabPane.setSelectedIndex(0);

            tindakanFromOrder(tblOrder.getValueAt(tblOrder.getSelectedRow(), 0).toString());
        }
    }//GEN-LAST:event_tblOrderMouseClicked

    private void tblOrderKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_tblOrderKeyPressed
    {//GEN-HEADEREND:event_tblOrderKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblOrderKeyPressed

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
        if (tblOrder.getSelectedRow() == -1)
        {
            GMessage.i("Pilih", "Pilih dulu data yg mau dihapus");
            return;
        }
        
        if (GMessage.q("Hapus", "Yakin mau hapus?"))
        {
            hapus(tblOrder.getValueAt(tblOrder.getSelectedRow(), 0).toString());
        }
    }//GEN-LAST:event_btnHapusOrderActionPerformed

    private void btnHapusTransaksiActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnHapusTransaksiActionPerformed
    {//GEN-HEADEREND:event_btnHapusTransaksiActionPerformed
        if (tblTransaksi.getSelectedRow() == -1)
        {
            GMessage.i("Pilih", "Pilih dulu data yg mau dihapus");
            return;
        }
        
        if (GMessage.q("Hapus", "Yakin mau hapus?"))
        {
            hapus(tblTransaksi.getValueAt(tblTransaksi.getSelectedRow(), 0).toString());
        }
    }//GEN-LAST:event_btnHapusTransaksiActionPerformed

    private void btnCariNoRawatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariNoRawatActionPerformed
        cariNoRawat();
    }//GEN-LAST:event_btnCariNoRawatActionPerformed

    private void cmbDtkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbDtkKeyPressed

    }//GEN-LAST:event_cmbDtkKeyPressed

    private void cmbMntKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMntKeyPressed
        Valid.pindah(evt, cmbJam, cmbDtk);
    }//GEN-LAST:event_cmbMntKeyPressed

    private void cmbJamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbJamKeyPressed
        Valid.pindah(evt, DTPBeri, cmbMnt);
    }//GEN-LAST:event_cmbJamKeyPressed

    private void DTPBeriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPBeriKeyPressed

    }//GEN-LAST:event_DTPBeriKeyPressed

    private void btnPerawatOrBidanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPerawatOrBidanActionPerformed
        cariPetugas(PERAWAT_OR_BIDAN);
    }//GEN-LAST:event_btnPerawatOrBidanActionPerformed

    private void txtKdPerawatOrBidanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKdPerawatOrBidanKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", txtNamaPerawatOrBidan, txtKdPerawatOrBidan.getText());

            if (txtNamaPerawatOrBidan.getText().isEmpty())
            btnPerawatOrBidanActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnPerawatOrBidanActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdDokPendamping, btnSimpan);
        }
    }//GEN-LAST:event_txtKdPerawatOrBidanKeyPressed

    private void txtKdDokPendampingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKdDokPendampingKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", txtNamaDokPendamping, txtKdDokPendamping.getText());

            if (txtNamaDokPendamping.getText().isEmpty())
            btnDokPendampingActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnDokPendampingActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdDokAnak, txtKdPerawatOrBidan);
        }
    }//GEN-LAST:event_txtKdDokPendampingKeyPressed

    private void btnDokPendampingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDokPendampingActionPerformed
        cariDokter(DOK_PENDAMPING);
    }//GEN-LAST:event_btnDokPendampingActionPerformed

    private void btnDokAnakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDokAnakActionPerformed
        cariDokter(DOK_ANAK);
    }//GEN-LAST:event_btnDokAnakActionPerformed

    private void txtKdDokAnakKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKdDokAnakKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", txtNamaDokAnak, txtKdDokAnak.getText());

            if (txtNamaDokAnak.getText().isEmpty())
            btnDokAnakActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnDokAnakActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdPenataAnestesi, txtKdDokPendamping);
        }
    }//GEN-LAST:event_txtKdDokAnakKeyPressed

    private void btnPenataAnestesiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPenataAnestesiActionPerformed
        cariPetugas(PENATA_ANESTESI);
    }//GEN-LAST:event_btnPenataAnestesiActionPerformed

    private void txtKdPenataAnestesiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKdPenataAnestesiKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", txtNamaPenataAnestesi, txtKdPenataAnestesi.getText());

            if (txtNamaPenataAnestesi.getText().isEmpty())
            btnPenataAnestesiActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnPenataAnestesiActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdDokAnestesi, txtKdDokAnak);
        }
    }//GEN-LAST:event_txtKdPenataAnestesiKeyPressed

    private void btnDokAnestesiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDokAnestesiActionPerformed
        cariDokter(DOK_ANESTESI);
    }//GEN-LAST:event_btnDokAnestesiActionPerformed

    private void txtKdDokAnestesiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKdDokAnestesiKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", txtNamaDokAnestesi, txtKdDokAnestesi.getText());

            if (txtNamaDokAnestesi.getText().isEmpty())
            btnDokAnestesiActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnDokAnestesiActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdAssDokOperator, txtKdPenataAnestesi);
        }
    }//GEN-LAST:event_txtKdDokAnestesiKeyPressed

    private void btnAssDokOperatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAssDokOperatorActionPerformed
        cariPetugas(ASS_DOK_OPERATOR);
    }//GEN-LAST:event_btnAssDokOperatorActionPerformed

    private void txtKdAssDokOperatorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKdAssDokOperatorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", txtNamaAssDokOperator, txtKdAssDokOperator.getText());

            if (txtNamaAssDokOperator.getText().isEmpty())
            btnAssDokOperatorActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnAssDokOperatorActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdDokMerawat, txtKdDokAnestesi);
        }
    }//GEN-LAST:event_txtKdAssDokOperatorKeyPressed

    private void txtJenisAnasthesiaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJenisAnasthesiaKeyPressed
        Valid.pindah(evt, txtKdDokOperator, txtKdDokOperator);
    }//GEN-LAST:event_txtJenisAnasthesiaKeyPressed

    private void btnDokOperatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDokOperatorActionPerformed
        cariDokter(DOK_OPERATOR);
    }//GEN-LAST:event_btnDokOperatorActionPerformed

    private void txtKdDokOperatorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKdDokOperatorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", txtNamaDokOperator, txtKdDokOperator.getText());

            if (txtNamaDokOperator.getText().isEmpty())
            btnDokOperatorActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnDokOperatorActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdDokOperator, txtKdDokMerawat);
        }
    }//GEN-LAST:event_txtKdDokOperatorKeyPressed

    private void txtNamaPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamaPasienKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            if (txtNamaPasien.getText().trim().isEmpty())
            {
                GMessage.e("Submit", "Field masih kosong");
            }
            else
            {
                getDataNoRmByEnter(2, txtNamaPasien.getText());
            }
        }
    }//GEN-LAST:event_txtNamaPasienKeyPressed

    private void txtNoRmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoRmKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            if (txtNoRm.getText().trim().isEmpty())
            {
                GMessage.e("Submit", "Field masih kosong");
            }
            else
            {
                getDataNoRmByEnter(1, txtNoRm.getText());
            }
        }
    }//GEN-LAST:event_txtNoRmKeyPressed

    private void btnCariDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariDetailActionPerformed
        cariDetail();
    }//GEN-LAST:event_btnCariDetailActionPerformed

    private void txtNoRawatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoRawatKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            if (txtNoRawat.getText().trim().isEmpty())
            {
                GMessage.e("Submit", "Field masih kosong");
            }
            else
            {
                getDataNoRmByEnter(0, txtNoRawat.getText());
            }
        }
    }//GEN-LAST:event_txtNoRawatKeyPressed

    private void btnCariGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariGroupActionPerformed
        cariGroup();
    }//GEN-LAST:event_btnCariGroupActionPerformed

    private void btnCariKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariKategoriActionPerformed
        cariKategori();
    }//GEN-LAST:event_btnCariKategoriActionPerformed

    private void txtKdDokMerawatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKdDokMerawatKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", txtNamaDokMerawat, txtKdDokMerawat.getText());

            if (txtNamaDokMerawat.getText().isEmpty())
            btnDokMerawatActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnDokMerawatActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdDokOperator, txtKdAssDokOperator);
        }
    }//GEN-LAST:event_txtKdDokMerawatKeyPressed

    private void btnDokMerawatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDokMerawatActionPerformed
        cariDokter(DOK_MERAWAT);
    }//GEN-LAST:event_btnDokMerawatActionPerformed

    private void txtKdAssDokOperatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKdAssDokOperatorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKdAssDokOperatorActionPerformed

    private void txtKdDokOperatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKdDokOperatorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKdDokOperatorActionPerformed

    private void getDataNoRmByEnter(int i, String s)
    {
        String col = "";
        if (i == 0) col = "kamar_inap.no_rawat";
        else if (i == 1) col = "pasien.no_rkm_medis";
        else if (i == 2) col = "pasien.nm_pasien";
        
        GQuery g = new GQuery("SELECT kamar_inap.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien "
                + " FROM kamar_inap "
                + " JOIN reg_periksa ON reg_periksa.no_rawat = kamar_inap.no_rawat "
                + " JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis "
                + " WHERE " + col + " = '" + s + "' ");
        
        List<String[]> l = g.select();
        
        if (l.isEmpty())
        {
            GMessage.e("Operasi", "Data tidak ditemukan");
            
            txtNoRawat.setText("");
            txtNoRm.setText("");
            txtNamaPasien.setText("");
        }
        else
        {
            txtNoRawat.setText(l.get(0)[0]);
            txtNoRm.setText(l.get(0)[1]);
            txtNamaPasien.setText(l.get(0)[2]);
        }
    }
    
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
            java.util.logging.Logger.getLogger(DlgPemeriksaanOperasi2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(DlgPemeriksaanOperasi2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(DlgPemeriksaanOperasi2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(DlgPemeriksaanOperasi2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                DlgPemeriksaanOperasi2 dialog = new DlgPemeriksaanOperasi2(new javax.swing.JFrame(), true);
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
    private widget.CekBox ChkJln;
    private widget.Tanggal DTPBeri;
    private widget.ScrollPane Scroll2;
    private widget.ScrollPane Scroll3;
    private widget.Button btnAssDokOperator;
    private widget.Button btnBaru;
    private widget.Button btnCariDetail;
    private widget.Button btnCariGroup;
    private widget.Button btnCariKategori;
    private widget.Button btnCariNoRawat;
    private widget.Button btnCariOrder;
    private widget.Button btnCariTransaksi;
    private widget.Button btnCetak;
    private widget.Button btnDokAnak;
    private widget.Button btnDokAnestesi;
    private widget.Button btnDokMerawat;
    private widget.Button btnDokOperator;
    private widget.Button btnDokPendamping;
    private widget.Button btnHapusOrder;
    private widget.Button btnHapusTransaksi;
    private widget.Button btnKeluar;
    private widget.Button btnPenataAnestesi;
    private widget.Button btnPerawatOrBidan;
    private widget.Button btnSimpan;
    private widget.ComboBox cmbDtk;
    private widget.ComboBox cmbJam;
    private widget.ComboBox cmbMnt;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel12;
    private widget.Label jLabel28;
    private widget.Label jLabel35;
    private widget.Label jLabel36;
    private widget.Label jLabel37;
    private widget.Label jLabel38;
    private widget.Label jLabel4;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private widget.Label label1;
    private widget.Label label14;
    private widget.Label label19;
    private widget.Label label2;
    private widget.Label label20;
    private widget.Label label21;
    private widget.Label label22;
    private widget.Label label27;
    private widget.Label label29;
    private widget.Label label3;
    private widget.Label label30;
    private widget.Label label4;
    private widget.Label lblCountOrder;
    private widget.Label lblCountTransaksi;
    private widget.PanelBiasa panelBiasa1;
    private widget.PanelBiasa panelBiasa2;
    private widget.PanelBiasa panelBiasa3;
    private widget.panelisi panelGlass12;
    private widget.panelisi panelGlass13;
    private widget.panelisi pnlAction;
    private widget.PanelBiasa pnlInput;
    private widget.TabPane tabPane;
    private widget.Table tblOrder;
    private widget.Table tblTransaksi;
    private widget.Tanggal tglOrder1;
    private widget.Tanggal tglOrder2;
    private widget.Tanggal tglTransaksi1;
    private widget.Tanggal tglTransaksi2;
    private widget.TextBox txtJenisAnasthesia;
    private widget.TextBox txtKdAssDokOperator;
    private widget.TextBox txtKdDetail;
    private widget.TextBox txtKdDokAnak;
    private widget.TextBox txtKdDokAnestesi;
    private widget.TextBox txtKdDokMerawat;
    private widget.TextBox txtKdDokOperator;
    private widget.TextBox txtKdDokPendamping;
    private widget.TextBox txtKdGroup;
    private widget.TextBox txtKdKategori;
    private widget.TextBox txtKdPenataAnestesi;
    private widget.TextBox txtKdPerawatOrBidan;
    private widget.TextBox txtNamaAssDokOperator;
    private widget.TextBox txtNamaDetail;
    private widget.TextBox txtNamaDokAnak;
    private widget.TextBox txtNamaDokAnestesi;
    private widget.TextBox txtNamaDokMerawat;
    private widget.TextBox txtNamaDokOperator;
    private widget.TextBox txtNamaDokPendamping;
    private widget.TextBox txtNamaGroup;
    private widget.TextBox txtNamaKategori;
    private widget.TextBox txtNamaPasien;
    private widget.TextBox txtNamaPenataAnestesi;
    private widget.TextBox txtNamaPerawatOrBidan;
    private widget.TextBox txtNoRawat;
    private widget.TextBox txtNoRm;
    // End of variables declaration//GEN-END:variables

    // </editor-fold>
    
    private void hapus(String id)
    {
        boolean success = new GQuery()
                .a("DELETE FROM operasi WHERE kd_operasi = {kd_operasi}")
                .set("kd_operasi", id)
                .write();
        
        if (success)
        {
            GMessage.i("Sukses", "Hapus data berhasil");
            
            baru();
            tampilOrder();
            tampilTransaksi();
        }
        else
        {
            GMessage.e("Error", "Error saat menghapus data");
        }
    }
}

