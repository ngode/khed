/*
*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simrskhanza;

import base.BaseDialog;
import fungsi.GConvert;
import fungsi.GQuery;
import fungsi.GResult;
import fungsi.GStatement;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import interfaces.TextChangedListener;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
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
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import util.GMessage;
import widget.TextBox;

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
    private String kdOperasi = null;
    private String jenisAnestesi, dokterOperator, dokterYgMerawat, assDokterOperator, dokterAnestesi,
            penataAnestesi, dokterAnak, dokterPendamping, perawatBidan,
            byOperasiKamarBedah, byAlatRumahSakit, byDokterOperator, byAssDokterOperator, byDokterAnestesi,
            byPenataAnestesi, byDokterAnak, byDokterPendamping, byPerawatBidan, byAssBidan, byAlatDokter,
            byRecoveryRoom, byPemakaianObatOk, jumlahBiaya;

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
        initText();
        
        tampilOrder();
        tampilTransaksi();
        cariKelas();
    }
    
    private void initText()
    {
        txtByOperasiKamarBedah.setDocument(new batasInput((byte)8).getOnlyAngka(txtByOperasiKamarBedah));
        txtByAlatRumahSakit.setDocument(new batasInput((byte)8).getOnlyAngka(txtByAlatRumahSakit));
        txtByDokterOperator.setDocument(new batasInput((byte)8).getOnlyAngka(txtByDokterOperator));
        txtByAssDokterOperator.setDocument(new batasInput((byte)8).getOnlyAngka(txtByAssDokterOperator));
        txtByDokterAnestesi.setDocument(new batasInput((byte)8).getOnlyAngka(txtByDokterAnestesi));
        txtByPenataAnestesi.setDocument(new batasInput((byte)8).getOnlyAngka(txtByPenataAnestesi));
        txtByDokterAnak.setDocument(new batasInput((byte)8).getOnlyAngka(txtByDokterAnak));
        txtByDokterPendamping.setDocument(new batasInput((byte)8).getOnlyAngka(txtByDokterPendamping));
        txtByPerawatBidan.setDocument(new batasInput((byte)8).getOnlyAngka(txtByPerawatBidan));
        txtByAssBidan.setDocument(new batasInput((byte)8).getOnlyAngka(txtByAssBidan));
        txtByAlatDokter.setDocument(new batasInput((byte)8).getOnlyAngka(txtByAlatDokter));
        txtByRecoveryRoom.setDocument(new batasInput((byte)8).getOnlyAngka(txtByRecoveryRoom));
        txtByPemakaianObatOk.setDocument(new batasInput((byte)8).getOnlyAngka(txtByPemakaianObatOk));
        
        txtByOperasiKamarBedah.addTextChangedListener(t);
        txtByAlatRumahSakit.addTextChangedListener(t);
        txtByDokterOperator.addTextChangedListener(t);
        txtByAssDokterOperator.addTextChangedListener(t);
        txtByDokterAnestesi.addTextChangedListener(t);
        txtByPenataAnestesi.addTextChangedListener(t);
        txtByDokterAnak.addTextChangedListener(t);
        txtByDokterPendamping.addTextChangedListener(t);
        txtByPerawatBidan.addTextChangedListener(t);
        txtByAssBidan.addTextChangedListener(t);
        txtByAlatDokter.addTextChangedListener(t);
        txtByRecoveryRoom.addTextChangedListener(t);
        txtByPemakaianObatOk.addTextChangedListener(t);
        
        txtKdKategori.addTextChangedListener(t -> tampilBiaya(t.getText()));
        
        Font f = lblJumlah.getFont();
        lblJumlah.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
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
            if (dlgDokter.getTable().getSelectedRow() == -1) return;
            
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
            80, 50, 50, 50, 120, 300, 200, 200, 200, 150
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
        int i = tblOrder.getSelectedRow();
        
        txtNoRawat.setText(tblOrder.getValueAt(i, 4).toString());
        Sequel.cariIsi("SELECT no_rkm_medis FROM reg_periksa WHERE no_rawat = ?", txtNoRm, txtNoRawat.getText());
        Sequel.cariIsi("SELECT nm_pasien FROM pasien WHERE no_rkm_medis = ?", txtNamaPasien, txtNoRm.getText());
        
        txtKdGroup.setText(tblOrder.getValueAt(i, 1).toString());
        txtKdKategori.setText(tblOrder.getValueAt(i, 2).toString());
        txtKdDetail.setText(tblOrder.getValueAt(i, 3) == null ? "-" : tblOrder.getValueAt(i, 3).toString());
        txtNamaGroup.setText(tblOrder.getValueAt(i, 6).toString());
        txtNamaKategori.setText(tblOrder.getValueAt(i, 7).toString());
        txtNamaDetail.setText(tblOrder.getValueAt(i, 8) == null ? "-" : tblOrder.getValueAt(i, 8).toString());
        
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
        
        jenisAnestesi = txtJenisAnasthesia.getText().trim().isEmpty() ? "NULL" : "'" + txtJenisAnasthesia.getText().trim() + "'";
        dokterOperator = txtKdDokOperator.getText().trim().isEmpty() ? "NULL" : "'" + txtKdDokOperator.getText().trim() + "'";
        dokterYgMerawat = txtKdDokMerawat.getText().trim().isEmpty() ? "NULL" : "'" + txtKdDokMerawat.getText().trim() + "'";
        assDokterOperator = txtKdAssDokOperator.getText().trim().isEmpty() ? "NULL" : "'" + txtKdAssDokOperator.getText().trim() + "'";
        dokterAnestesi = txtKdDokAnestesi.getText().trim().isEmpty() ? "NULL" : "'" + txtKdDokAnestesi.getText().trim() + "'";
        penataAnestesi = txtKdPenataAnestesi.getText().trim().isEmpty() ? "NULL" : "'" + txtKdPenataAnestesi.getText().trim() + "'";
        dokterAnak = txtKdDokAnak.getText().trim().isEmpty() ? "NULL" : "'" + txtKdDokAnak.getText().trim() + "'";
        dokterPendamping = txtKdDokPendamping.getText().trim().isEmpty() ? "NULL" : "'" + txtKdDokPendamping.getText().trim() + "'";
        perawatBidan = txtKdPerawatOrBidan.getText().trim().isEmpty() ? "NULL" : "'" + txtKdPerawatOrBidan.getText().trim() + "'";
        
        byOperasiKamarBedah = GConvert.parseInt(txtByOperasiKamarBedah.getText()) + "";
        byAlatRumahSakit = GConvert.parseInt(txtByAlatRumahSakit.getText()) + "";
        byDokterOperator = GConvert.parseInt(txtByDokterOperator.getText()) + "";
        byAssDokterOperator = GConvert.parseInt(txtByAssDokterOperator.getText()) + "";
        byDokterAnestesi = GConvert.parseInt(txtByDokterAnestesi.getText()) + "";
        byPenataAnestesi = GConvert.parseInt(txtByPenataAnestesi.getText()) + "";
        byDokterAnak = GConvert.parseInt(txtByDokterAnak.getText()) + "";
        byDokterPendamping = GConvert.parseInt(txtByDokterPendamping.getText()) + "";
        byPerawatBidan = GConvert.parseInt(txtByPerawatBidan.getText()) + "";
        byAssBidan = GConvert.parseInt(txtByAssBidan.getText()) + "";
        byAlatDokter = GConvert.parseInt(txtByAlatDokter.getText()) + "";
        byRecoveryRoom = GConvert.parseInt(txtByRecoveryRoom.getText()) + "";
        byPemakaianObatOk = GConvert.parseInt(txtByPemakaianObatOk.getText()) + "";
        jumlahBiaya = String.valueOf(
                GConvert.parseInt(byOperasiKamarBedah) + 
                GConvert.parseInt(byAlatRumahSakit) + 
                GConvert.parseInt(byDokterOperator) + 
                GConvert.parseInt(byAssDokterOperator) + 
                GConvert.parseInt(byDokterAnestesi) + 
                GConvert.parseInt(byPenataAnestesi) + 
                GConvert.parseInt(byDokterAnak) + 
                GConvert.parseInt(byDokterPendamping) + 
                GConvert.parseInt(byPerawatBidan) + 
                GConvert.parseInt(byAssBidan) + 
                GConvert.parseInt(byAlatDokter) + 
                GConvert.parseInt(byRecoveryRoom) + 
                GConvert.parseInt(byPemakaianObatOk)
        );
        
        boolean success;
        if (kdOperasi == null) success = insertOperasi();
        else success = updateOperasi();
        
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
    
    private boolean insertOperasi()
    {
        return new GQuery()
                .a("INSERT INTO hrj_operasi VALUES (")
                .a("{kd_operasi}, {no_rawat}, {kd_group}, {kd_kategori}, {kd_detail}, {kode_paket}, {tgl_operasi}, {jam_operasi},")
                .a("{tgl_selesai}, {jam_selesai}, {jenis_anasthesi}, {dokter_operator}, {dokter_yang_merawat},")
                .a("{ass_dokter_operator}, {dokter_anestesi}, {penata_anestesi}, {dokter_anak}, {dokter_pendamping},")
                .a("{perawat_bidan}, {by_operasi_kamar_bedah}, {by_alat_rumah_sakit}, {by_dokter_operator},")
                .a("{by_ass_dokter_operator}, {by_dokter_anestesi}, {by_penata_anestesi},")
                .a("{by_dokter_anak}, {by_dokter_pendamping}, {by_perawat_bidan}, {by_ass_bidan}, {by_alat_dokter},")
                .a("{by_recovery_room}, {by_pemakaian_obat_ok}, {jumlah_biaya}, {status}, {proses})")
                .set("kd_operasi", Utilz.getNextKodeOperasi())
                .set("no_rawat", txtNoRawat.getText())
                .set("kd_group", txtKdGroup.getText())
                .set("kd_kategori", txtKdKategori.getText())
                .set("kd_detail", txtKdDetail.getText())
                .set("kode_paket", txtKdKategori.getText())
                .set("tgl_operasi", Valid.SetTgl(DTPBeri.getSelectedItem().toString()))
                .set("jam_operasi", cmbJam.getSelectedItem() + ":" + cmbMnt.getSelectedItem() + ":" + cmbDtk.getSelectedItem())
                .set("tgl_selesai", Valid.SetTgl(DTPBeri.getSelectedItem().toString()))
                .set("jam_selesai", cmbJam.getSelectedItem() + ":" + cmbMnt.getSelectedItem() + ":" + cmbDtk.getSelectedItem())
                .setNoQuote("jenis_anasthesi", jenisAnestesi)
                .setNoQuote("dokter_operator", dokterOperator)
                .setNoQuote("dokter_yang_merawat", dokterYgMerawat)
                .setNoQuote("ass_dokter_operator", assDokterOperator)
                .setNoQuote("dokter_anestesi", dokterAnestesi)
                .setNoQuote("penata_anestesi", penataAnestesi)
                .setNoQuote("dokter_anak", dokterAnak)
                .setNoQuote("dokter_pendamping", dokterPendamping)
                .setNoQuote("perawat_bidan", perawatBidan)
                .set("by_operasi_kamar_bedah", byOperasiKamarBedah)
                .set("by_alat_rumah_sakit", byAlatRumahSakit)
                .set("by_dokter_operator", byDokterOperator)
                .set("by_ass_dokter_operator", byAssDokterOperator)
                .set("by_dokter_anestesi", byDokterAnestesi)
                .set("by_penata_anestesi", byPenataAnestesi)
                .set("by_dokter_anak", byDokterAnak)
                .set("by_dokter_pendamping", byDokterPendamping)
                .set("by_perawat_bidan", byPerawatBidan)
                .set("by_ass_bidan", byAssBidan)
                .set("by_alat_dokter", byAlatDokter)
                .set("by_recovery_room", byRecoveryRoom)
                .set("by_pemakaian_obat_ok", byPemakaianObatOk)
                .set("jumlah_biaya", jumlahBiaya)
                .set("status", status)
                .set("proses", "Sudah")
                .write();
    }
    
    private boolean updateOperasi()
    {
        return new GQuery()
                .a("UPDATE hrj_operasi SET")
                .a("no_rawat = {no_rawat}, kd_group = {kd_group}, kd_kategori = {kd_kategori}, kd_detail = {kd_detail}, kode_paket = {kode_paket},")
                .a("tgl_selesai = {tgl_selesai}, jam_selesai = {jam_selesai}, jenis_anasthesi = {jenis_anasthesi},")
                .a("dokter_operator = {dokter_operator}, dokter_yang_merawat = {dokter_yang_merawat},")
                .a("ass_dokter_operator = {ass_dokter_operator}, dokter_anestesi = {dokter_anestesi},")
                .a("penata_anestesi = {penata_anestesi}, dokter_anak = {dokter_anak}, dokter_pendamping = {dokter_pendamping},")
                .a("perawat_bidan = {perawat_bidan}, by_operasi_kamar_bedah = {by_operasi_kamar_bedah},")
                .a("by_alat_rumah_sakit = {by_alat_rumah_sakit}, by_dokter_operator = {by_dokter_operator},")
                .a("by_ass_dokter_operator = {by_ass_dokter_operator}, by_dokter_anestesi = {by_dokter_anestesi},")
                .a("by_penata_anestesi = {by_penata_anestesi}, by_dokter_anak = {by_dokter_anak},")
                .a("by_dokter_pendamping = {by_dokter_pendamping}, by_perawat_bidan = {by_perawat_bidan},")
                .a("by_ass_bidan = {by_ass_bidan}, by_alat_dokter = {by_alat_dokter}, by_recovery_room = {by_recovery_room},")
                .a("by_pemakaian_obat_ok = {by_pemakaian_obat_ok}, jumlah_biaya = {jumlah_biaya}, status = {status}, proses = {proses}")
                .a("WHERE kd_operasi = {kd_operasi}")
                .set("no_rawat", txtNoRawat.getText())
                .set("kd_group", txtKdGroup.getText())
                .set("kd_kategori", txtKdKategori.getText())
                .set("kd_detail", txtKdDetail.getText())
                .set("kode_paket", txtKdKategori.getText())
                .set("tgl_selesai", Valid.SetTgl(DTPBeri.getSelectedItem().toString()))
                .set("jam_selesai", cmbJam.getSelectedItem() + ":" + cmbMnt.getSelectedItem() + ":" + cmbDtk.getSelectedItem())
                .setNoQuote("jenis_anasthesi", jenisAnestesi)
                .setNoQuote("dokter_operator", dokterOperator)
                .setNoQuote("dokter_yang_merawat", dokterYgMerawat)
                .setNoQuote("ass_dokter_operator", assDokterOperator)
                .setNoQuote("dokter_anestesi", dokterAnestesi)
                .setNoQuote("penata_anestesi", penataAnestesi)
                .setNoQuote("dokter_anak", dokterAnak)
                .setNoQuote("dokter_pendamping", dokterPendamping)
                .setNoQuote("perawat_bidan", perawatBidan)
                .set("by_operasi_kamar_bedah", byOperasiKamarBedah)
                .set("by_alat_rumah_sakit", byAlatRumahSakit)
                .set("by_dokter_operator", byDokterOperator)
                .set("by_ass_dokter_operator", byAssDokterOperator)
                .set("by_dokter_anestesi", byDokterAnestesi)
                .set("by_penata_anestesi", byPenataAnestesi)
                .set("by_dokter_anak", byDokterAnak)
                .set("by_dokter_pendamping", byDokterPendamping)
                .set("by_perawat_bidan", byPerawatBidan)
                .set("by_ass_bidan", byAssBidan)
                .set("by_alat_dokter", byAlatDokter)
                .set("by_recovery_room", byRecoveryRoom)
                .set("by_pemakaian_obat_ok", byPemakaianObatOk)
                .set("jumlah_biaya", jumlahBiaya)
                .set("status", status)
                .set("proses", "Sudah")
                .set("kd_operasi", kdOperasi)
                .write();
    }
    
    private void baru()
    {
        kdOperasi = null;
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
        Valid.tabelKosong(mdlOrder);

        new GQuery()
                .a("SELECT kd_operasi, hrj_operasi.no_rawat, paket_operasi_2.*, pasien.no_rkm_medis, pasien.nm_pasien, kamar.kd_kamar, bangsal.nm_bangsal, poliklinik.nm_poli,")
                .a("operasi_group.kd_group, operasi_detail.kd_detail, nm_group, nm_detail, tgl_operasi, jam_operasi, tgl_selesai, jam_selesai")
                .a("FROM hrj_operasi")
                .a("LEFT JOIN operasi_detail ON operasi_detail.kd_detail = hrj_operasi.kd_detail")
//                .a("JOIN operasi_kategori ON operasi_kategori.kd_kategori = hrj_operasi.kd_kategori")
                .a("JOIN paket_operasi_2 ON paket_operasi_2.kode_paket = hrj_operasi.kode_paket")
                .a("JOIN operasi_group ON operasi_group.kd_group = hrj_operasi.kd_group")
                .a("JOIN reg_periksa ON reg_periksa.no_rawat = hrj_operasi.no_rawat")
                .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                .a("LEFT JOIN kamar_inap ON kamar_inap.no_rawat = hrj_operasi.no_rawat")
                .a("LEFT JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                .a("LEFT JOIN bangsal ON bangsal.kd_bangsal = kamar.kd_bangsal")
                .a("LEFT JOIN poliklinik ON poliklinik.kd_poli = reg_periksa.kd_poli")
                .a("WHERE DATE(tgl_operasi) BETWEEN {tgl1} AND {tgl2} AND proses = 'Belum'")
                .a("ORDER BY tgl_operasi")
                .set("tgl1", Valid.SetTgl(tglOrder1.getSelectedItem().toString()))
                .set("tgl2", Valid.SetTgl(tglOrder2.getSelectedItem().toString()))
                .selectWithName()
                .forEach(s ->
                {
                    String pas;

                    if (s.get("kd_kamar") != null)
                    {
                        pas = s.get("no_rkm_medis") + " " + s.get("nm_pasien") + " (Kamar : " + 
                            s.get("kd_kamar") + ", " + s.get("nm_bangsal") + ")";
                    }
                    else
                    {
                        pas = s.get("no_rkm_medis") + " " + s.get("nm_pasien") + " (Poli : " + 
                            s.get("nm_poli") + ")";
                    }

                    Object[] o = new Object[]
                    {
                        s.get("kd_operasi"),
                        s.get("kd_group"),
                        s.get("kode_paket"),
                        s.get("kd_detail"),
                        s.get("no_rawat"),
                        pas,
                        s.get("nm_group"),
                        s.get("nama_paket"),
                        s.get("nm_detail"),
                        s.get("tgl_operasi") + " " + s.get("jam_operasi")
                    };

                    mdlOrder.addRow(o);
                });
    }
    
    private void tampilTransaksi()
    {
        Valid.tabelKosong(mdlTransaksi);

        new GQuery()
                .a("SELECT kd_operasi, hrj_operasi.no_rawat, paket_operasi_2.*, pasien.no_rkm_medis, pasien.nm_pasien, kamar.kd_kamar, bangsal.nm_bangsal, poliklinik.nm_poli,")
                .a("operasi_group.kd_group, operasi_detail.kd_detail, nm_group, nm_detail, tgl_operasi, jam_operasi, tgl_selesai, jam_selesai")
                .a("FROM hrj_operasi")
                .a("LEFT JOIN operasi_detail ON operasi_detail.kd_detail = hrj_operasi.kd_detail")
//                .a("JOIN operasi_kategori ON operasi_kategori.kd_kategori = hrj_operasi.kd_kategori")
                .a("JOIN paket_operasi_2 ON paket_operasi_2.kode_paket = hrj_operasi.kode_paket")
                .a("JOIN operasi_group ON operasi_group.kd_group = hrj_operasi.kd_group")
                .a("JOIN reg_periksa ON reg_periksa.no_rawat = hrj_operasi.no_rawat")
                .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                .a("LEFT JOIN kamar_inap ON kamar_inap.no_rawat = hrj_operasi.no_rawat")
                .a("LEFT JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                .a("LEFT JOIN bangsal ON bangsal.kd_bangsal = kamar.kd_bangsal")
                .a("LEFT JOIN poliklinik ON poliklinik.kd_poli = reg_periksa.kd_poli")
                .a("WHERE DATE(tgl_operasi) BETWEEN {tgl1} AND {tgl2} AND proses = 'Sudah'")
                .a("ORDER BY tgl_operasi")
                .set("tgl1", Valid.SetTgl(tglTransaksi1.getSelectedItem().toString()))
                .set("tgl2", Valid.SetTgl(tglTransaksi2.getSelectedItem().toString()))
                .selectWithName()
                .forEach(s ->
                {
                    String pas;

                    if (s.get("kd_kamar") != null)
                    {
                        pas = s.get("no_rkm_medis") + " " + s.get("nm_pasien") + " (Kamar : " + s.get("kd_kamar") + ", " + s.get("nm_bangsal") + ")";
                    }
                    else
                    {
                        pas = s.get("no_rkm_medis") + " " + s.get("nm_pasien") + " (Poli : " + s.get("nm_poli") + ")";
                    }

                    Object[] o = new Object[]
                    {
                        s.get("kd_operasi"),
                        s.get("kd_group"),
                        s.get("kode_paket"),
                        s.get("kd_detail"),
                        s.get("no_rawat"),
                        pas,
                        s.get("nm_group"),
                        s.get("nama_paket"),
                        s.get("nm_detail"),
                        s.get("tgl_operasi") + " " + s.get("jam_operasi"),
                        s.get("tgl_selesai") + " " + s.get("jam_selesai")
                    };

                    mdlTransaksi.addRow(o);
                });
    }

    
    private boolean valid()
    {
        if (txtNoRawat.getText().trim().isEmpty())
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
//        else if (txtNamaDetail.getText().trim().isEmpty())
//        {
//            GMessage.w("Salah", "Pilih detail dulu");
//            return false;
//        }
        
        return true;
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
    
//    private HashMap<String, String> getPaket()
//    {
//        return new GQuery()
//                .a("SELECT * FROM paket_operasi_2 WHERE jenis = {jenis} AND kelas = {kelas}")
//                .set("jenis", txtNamaKategori.getText())
//                .set("kelas", Utilz.convertKelasAngkaToRoma(kelas))
//                .getRowWithName();
//    }
    
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

        jPopupMenuTransaksi = new javax.swing.JPopupMenu();
        menuCetakBillingTransaksi = new javax.swing.JMenuItem();
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
        txtByOperasiKamarBedah = new widget.TextBox();
        jLabel5 = new widget.Label();
        txtByAlatRumahSakit = new widget.TextBox();
        jLabel6 = new widget.Label();
        txtByDokterOperator = new widget.TextBox();
        jLabel7 = new widget.Label();
        jLabel8 = new widget.Label();
        txtByAssDokterOperator = new widget.TextBox();
        jLabel11 = new widget.Label();
        txtByDokterAnestesi = new widget.TextBox();
        txtByPenataAnestesi = new widget.TextBox();
        jLabel13 = new widget.Label();
        txtByDokterAnak = new widget.TextBox();
        jLabel14 = new widget.Label();
        txtByDokterPendamping = new widget.TextBox();
        jLabel15 = new widget.Label();
        txtByPerawatBidan = new widget.TextBox();
        jLabel16 = new widget.Label();
        txtByAssBidan = new widget.TextBox();
        jLabel17 = new widget.Label();
        txtByAlatDokter = new widget.TextBox();
        jLabel18 = new widget.Label();
        txtByRecoveryRoom = new widget.TextBox();
        jLabel19 = new widget.Label();
        txtByPemakaianObatOk = new widget.TextBox();
        jLabel20 = new widget.Label();
        txtJumlahBiaya = new widget.TextBox();
        lblJumlah = new widget.Label();
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

        jPopupMenuTransaksi.setForeground(new java.awt.Color(60, 80, 50));
        jPopupMenuTransaksi.setAutoscrolls(true);
        jPopupMenuTransaksi.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPopupMenuTransaksi.setFocusTraversalPolicyProvider(true);

        menuCetakBillingTransaksi.setBackground(new java.awt.Color(255, 255, 255));
        menuCetakBillingTransaksi.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuCetakBillingTransaksi.setForeground(new java.awt.Color(60, 80, 50));
        menuCetakBillingTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuCetakBillingTransaksi.setText("Cetak Billing");
        menuCetakBillingTransaksi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuCetakBillingTransaksi.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuCetakBillingTransaksi.setIconTextGap(5);
        menuCetakBillingTransaksi.setPreferredSize(new java.awt.Dimension(220, 26));
        menuCetakBillingTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCetakBillingTransaksiActionPerformed(evt);
            }
        });
        jPopupMenuTransaksi.add(menuCetakBillingTransaksi);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "::[ Operasi ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame1.setPreferredSize(new java.awt.Dimension(971, 558));
        internalFrame1.setLayout(new java.awt.BorderLayout());

        panelBiasa1.setPreferredSize(new java.awt.Dimension(954, 553));
        panelBiasa1.setLayout(new java.awt.BorderLayout());

        pnlInput.setPreferredSize(new java.awt.Dimension(952, 501));
        pnlInput.setLayout(null);

        txtNamaKategori.setEditable(false);
        pnlInput.add(txtNamaKategori);
        txtNamaKategori.setBounds(206, 78, 394, 24);

        txtKdKategori.setEditable(false);
        pnlInput.add(txtKdKategori);
        txtKdKategori.setBounds(107, 78, 93, 24);

        label3.setText("Kategori :");
        pnlInput.add(label3);
        label3.setBounds(11, 82, 86, 16);

        btnCariKategori.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnCariKategori.setMnemonic('4');
        btnCariKategori.setToolTipText("ALt+4");
        btnCariKategori.setPreferredSize(new java.awt.Dimension(35, 22));
        btnCariKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariKategoriActionPerformed(evt);
            }
        });
        pnlInput.add(btnCariKategori);
        btnCariKategori.setBounds(600, 80, 22, 22);

        label1.setText("No Rawat :");
        pnlInput.add(label1);
        label1.setBounds(11, 22, 86, 16);

        btnCariGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnCariGroup.setMnemonic('4');
        btnCariGroup.setToolTipText("ALt+4");
        btnCariGroup.setPreferredSize(new java.awt.Dimension(35, 22));
        btnCariGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariGroupActionPerformed(evt);
            }
        });
        pnlInput.add(btnCariGroup);
        btnCariGroup.setBounds(600, 48, 22, 22);

        txtNoRawat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNoRawatKeyPressed(evt);
            }
        });
        pnlInput.add(txtNoRawat);
        txtNoRawat.setBounds(107, 18, 131, 24);

        btnCariDetail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnCariDetail.setMnemonic('4');
        btnCariDetail.setToolTipText("ALt+4");
        btnCariDetail.setPreferredSize(new java.awt.Dimension(35, 22));
        btnCariDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariDetailActionPerformed(evt);
            }
        });
        pnlInput.add(btnCariDetail);
        btnCariDetail.setBounds(600, 110, 22, 22);

        txtNoRm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNoRmKeyPressed(evt);
            }
        });
        pnlInput.add(txtNoRm);
        txtNoRm.setBounds(244, 18, 100, 24);

        txtNamaDetail.setEditable(false);
        pnlInput.add(txtNamaDetail);
        txtNamaDetail.setBounds(204, 108, 396, 24);

        txtNamaPasien.setEditable(false);
        txtNamaPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNamaPasienKeyPressed(evt);
            }
        });
        pnlInput.add(txtNamaPasien);
        txtNamaPasien.setBounds(350, 18, 250, 24);

        txtKdDetail.setEditable(false);
        pnlInput.add(txtKdDetail);
        txtKdDetail.setBounds(107, 108, 91, 24);

        txtNamaGroup.setEditable(false);
        pnlInput.add(txtNamaGroup);
        txtNamaGroup.setBounds(206, 48, 394, 24);

        label4.setText("Detail :");
        pnlInput.add(label4);
        label4.setBounds(11, 112, 86, 16);

        txtKdGroup.setEditable(false);
        pnlInput.add(txtKdGroup);
        txtKdGroup.setBounds(107, 48, 93, 24);

        label2.setText("Group :");
        pnlInput.add(label2);
        label2.setBounds(11, 52, 86, 16);

        label14.setText("Dokter Operator :");
        label14.setPreferredSize(new java.awt.Dimension(70, 23));
        pnlInput.add(label14);
        label14.setBounds(13, 236, 125, 23);

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
        pnlInput.add(txtKdDokOperator);
        txtKdDokOperator.setBounds(150, 236, 100, 23);

        txtNamaDokOperator.setEditable(false);
        txtNamaDokOperator.setPreferredSize(new java.awt.Dimension(207, 23));
        pnlInput.add(txtNamaDokOperator);
        txtNamaDokOperator.setBounds(251, 236, 190, 23);

        btnDokOperator.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokOperator.setMnemonic('2');
        btnDokOperator.setToolTipText("Alt+2");
        btnDokOperator.setPreferredSize(new java.awt.Dimension(28, 23));
        btnDokOperator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDokOperatorActionPerformed(evt);
            }
        });
        pnlInput.add(btnDokOperator);
        btnDokOperator.setBounds(442, 236, 28, 23);

        jLabel4.setText("Jenis Anasthesi :");
        pnlInput.add(jLabel4);
        jLabel4.setBounds(11, 206, 127, 23);

        txtJenisAnasthesia.setHighlighter(null);
        txtJenisAnasthesia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtJenisAnasthesiaKeyPressed(evt);
            }
        });
        pnlInput.add(txtJenisAnasthesia);
        txtJenisAnasthesia.setBounds(148, 205, 293, 23);

        label19.setText("Dokter yg Merawat :");
        label19.setPreferredSize(new java.awt.Dimension(70, 23));
        pnlInput.add(label19);
        label19.setBounds(13, 266, 125, 23);

        txtKdDokMerawat.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdDokMerawat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKdDokMerawatKeyPressed(evt);
            }
        });
        pnlInput.add(txtKdDokMerawat);
        txtKdDokMerawat.setBounds(150, 266, 100, 23);

        txtNamaDokMerawat.setEditable(false);
        txtNamaDokMerawat.setPreferredSize(new java.awt.Dimension(207, 23));
        pnlInput.add(txtNamaDokMerawat);
        txtNamaDokMerawat.setBounds(251, 266, 190, 23);

        btnDokMerawat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokMerawat.setMnemonic('2');
        btnDokMerawat.setToolTipText("Alt+2");
        btnDokMerawat.setPreferredSize(new java.awt.Dimension(28, 23));
        btnDokMerawat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDokMerawatActionPerformed(evt);
            }
        });
        pnlInput.add(btnDokMerawat);
        btnDokMerawat.setBounds(442, 266, 28, 23);

        label20.setText("Ass Dokter Operator :");
        label20.setPreferredSize(new java.awt.Dimension(70, 23));
        pnlInput.add(label20);
        label20.setBounds(13, 296, 125, 23);

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
        pnlInput.add(txtKdAssDokOperator);
        txtKdAssDokOperator.setBounds(150, 296, 100, 23);

        txtNamaAssDokOperator.setEditable(false);
        txtNamaAssDokOperator.setPreferredSize(new java.awt.Dimension(207, 23));
        pnlInput.add(txtNamaAssDokOperator);
        txtNamaAssDokOperator.setBounds(251, 296, 190, 23);

        btnAssDokOperator.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnAssDokOperator.setMnemonic('2');
        btnAssDokOperator.setToolTipText("Alt+2");
        btnAssDokOperator.setPreferredSize(new java.awt.Dimension(28, 23));
        btnAssDokOperator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAssDokOperatorActionPerformed(evt);
            }
        });
        pnlInput.add(btnAssDokOperator);
        btnAssDokOperator.setBounds(442, 296, 28, 23);

        label21.setText("Dokter Anestesi :");
        label21.setPreferredSize(new java.awt.Dimension(70, 23));
        pnlInput.add(label21);
        label21.setBounds(13, 326, 125, 23);

        txtKdDokAnestesi.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdDokAnestesi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKdDokAnestesiKeyPressed(evt);
            }
        });
        pnlInput.add(txtKdDokAnestesi);
        txtKdDokAnestesi.setBounds(150, 326, 100, 23);

        txtNamaDokAnestesi.setEditable(false);
        txtNamaDokAnestesi.setPreferredSize(new java.awt.Dimension(207, 23));
        pnlInput.add(txtNamaDokAnestesi);
        txtNamaDokAnestesi.setBounds(251, 326, 190, 23);

        btnDokAnestesi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokAnestesi.setMnemonic('2');
        btnDokAnestesi.setToolTipText("Alt+2");
        btnDokAnestesi.setPreferredSize(new java.awt.Dimension(28, 23));
        btnDokAnestesi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDokAnestesiActionPerformed(evt);
            }
        });
        pnlInput.add(btnDokAnestesi);
        btnDokAnestesi.setBounds(442, 326, 28, 23);

        label22.setText("Penata Anestesi :");
        label22.setPreferredSize(new java.awt.Dimension(70, 23));
        pnlInput.add(label22);
        label22.setBounds(13, 356, 125, 23);

        txtKdPenataAnestesi.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdPenataAnestesi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKdPenataAnestesiKeyPressed(evt);
            }
        });
        pnlInput.add(txtKdPenataAnestesi);
        txtKdPenataAnestesi.setBounds(150, 356, 100, 23);

        txtNamaPenataAnestesi.setEditable(false);
        txtNamaPenataAnestesi.setPreferredSize(new java.awt.Dimension(207, 23));
        pnlInput.add(txtNamaPenataAnestesi);
        txtNamaPenataAnestesi.setBounds(251, 356, 190, 23);

        btnPenataAnestesi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPenataAnestesi.setMnemonic('2');
        btnPenataAnestesi.setToolTipText("Alt+2");
        btnPenataAnestesi.setPreferredSize(new java.awt.Dimension(28, 23));
        btnPenataAnestesi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPenataAnestesiActionPerformed(evt);
            }
        });
        pnlInput.add(btnPenataAnestesi);
        btnPenataAnestesi.setBounds(442, 356, 28, 23);

        label27.setText("Dokter Anak :");
        label27.setPreferredSize(new java.awt.Dimension(70, 23));
        pnlInput.add(label27);
        label27.setBounds(13, 386, 125, 23);

        txtKdDokAnak.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdDokAnak.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKdDokAnakKeyPressed(evt);
            }
        });
        pnlInput.add(txtKdDokAnak);
        txtKdDokAnak.setBounds(150, 386, 100, 23);

        txtNamaDokAnak.setEditable(false);
        txtNamaDokAnak.setPreferredSize(new java.awt.Dimension(207, 23));
        pnlInput.add(txtNamaDokAnak);
        txtNamaDokAnak.setBounds(251, 386, 190, 23);

        btnDokAnak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokAnak.setMnemonic('2');
        btnDokAnak.setToolTipText("Alt+2");
        btnDokAnak.setPreferredSize(new java.awt.Dimension(28, 23));
        btnDokAnak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDokAnakActionPerformed(evt);
            }
        });
        pnlInput.add(btnDokAnak);
        btnDokAnak.setBounds(442, 386, 28, 23);

        btnDokPendamping.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokPendamping.setMnemonic('2');
        btnDokPendamping.setToolTipText("Alt+2");
        btnDokPendamping.setPreferredSize(new java.awt.Dimension(28, 23));
        btnDokPendamping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDokPendampingActionPerformed(evt);
            }
        });
        pnlInput.add(btnDokPendamping);
        btnDokPendamping.setBounds(442, 416, 28, 23);

        txtNamaDokPendamping.setEditable(false);
        txtNamaDokPendamping.setPreferredSize(new java.awt.Dimension(207, 23));
        pnlInput.add(txtNamaDokPendamping);
        txtNamaDokPendamping.setBounds(251, 416, 190, 23);

        txtKdDokPendamping.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdDokPendamping.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKdDokPendampingKeyPressed(evt);
            }
        });
        pnlInput.add(txtKdDokPendamping);
        txtKdDokPendamping.setBounds(150, 416, 100, 23);

        label29.setText("Dokter Pendamping :");
        label29.setPreferredSize(new java.awt.Dimension(70, 23));
        pnlInput.add(label29);
        label29.setBounds(13, 416, 125, 23);

        label30.setText("Perawat/Bidan :");
        label30.setPreferredSize(new java.awt.Dimension(70, 23));
        pnlInput.add(label30);
        label30.setBounds(10, 450, 125, 23);

        txtKdPerawatOrBidan.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdPerawatOrBidan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKdPerawatOrBidanKeyPressed(evt);
            }
        });
        pnlInput.add(txtKdPerawatOrBidan);
        txtKdPerawatOrBidan.setBounds(150, 450, 100, 23);

        txtNamaPerawatOrBidan.setEditable(false);
        txtNamaPerawatOrBidan.setPreferredSize(new java.awt.Dimension(207, 23));
        pnlInput.add(txtNamaPerawatOrBidan);
        txtNamaPerawatOrBidan.setBounds(250, 450, 190, 23);

        btnPerawatOrBidan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPerawatOrBidan.setMnemonic('2');
        btnPerawatOrBidan.setToolTipText("Alt+2");
        btnPerawatOrBidan.setPreferredSize(new java.awt.Dimension(28, 23));
        btnPerawatOrBidan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPerawatOrBidanActionPerformed(evt);
            }
        });
        pnlInput.add(btnPerawatOrBidan);
        btnPerawatOrBidan.setBounds(440, 450, 28, 23);

        jLabel9.setText("Tanggal : ");
        pnlInput.add(jLabel9);
        jLabel9.setBounds(21, 143, 80, 23);

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
        pnlInput.add(DTPBeri);
        DTPBeri.setBounds(111, 144, 95, 23);

        cmbJam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        cmbJam.setOpaque(false);
        cmbJam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbJamKeyPressed(evt);
            }
        });
        pnlInput.add(cmbJam);
        cmbJam.setBounds(208, 143, 45, 23);

        cmbMnt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbMnt.setOpaque(false);
        cmbMnt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbMntKeyPressed(evt);
            }
        });
        pnlInput.add(cmbMnt);
        cmbMnt.setBounds(254, 143, 45, 23);

        cmbDtk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbDtk.setOpaque(false);
        cmbDtk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbDtkKeyPressed(evt);
            }
        });
        pnlInput.add(cmbDtk);
        cmbDtk.setBounds(300, 143, 45, 23);

        ChkJln.setBackground(new java.awt.Color(235, 255, 235));
        ChkJln.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(195, 215, 195)));
        ChkJln.setForeground(new java.awt.Color(153, 0, 51));
        ChkJln.setSelected(true);
        ChkJln.setBorderPainted(true);
        ChkJln.setBorderPaintedFlat(true);
        ChkJln.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkJln.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkJln.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pnlInput.add(ChkJln);
        ChkJln.setBounds(346, 143, 23, 23);

        btnCariNoRawat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnCariNoRawat.setMnemonic('4');
        btnCariNoRawat.setToolTipText("ALt+4");
        btnCariNoRawat.setPreferredSize(new java.awt.Dimension(35, 22));
        btnCariNoRawat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariNoRawatActionPerformed(evt);
            }
        });
        pnlInput.add(btnCariNoRawat);
        btnCariNoRawat.setBounds(600, 20, 22, 22);

        txtByOperasiKamarBedah.setText("0");
        txtByOperasiKamarBedah.setHighlighter(null);
        txtByOperasiKamarBedah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtByOperasiKamarBedahKeyPressed(evt);
            }
        });
        pnlInput.add(txtByOperasiKamarBedah);
        txtByOperasiKamarBedah.setBounds(960, 60, 293, 23);

        jLabel5.setText("Biaya Operasi / Kamar Bedah : Rp");
        pnlInput.add(jLabel5);
        jLabel5.setBounds(730, 60, 204, 23);

        txtByAlatRumahSakit.setText("0");
        txtByAlatRumahSakit.setHighlighter(null);
        txtByAlatRumahSakit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtByAlatRumahSakitKeyPressed(evt);
            }
        });
        pnlInput.add(txtByAlatRumahSakit);
        txtByAlatRumahSakit.setBounds(960, 90, 293, 23);

        jLabel6.setText("Biaya Pemakaian Alat Rumah Sakit : Rp");
        pnlInput.add(jLabel6);
        jLabel6.setBounds(660, 90, 278, 23);

        txtByDokterOperator.setText("0");
        txtByDokterOperator.setHighlighter(null);
        txtByDokterOperator.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtByDokterOperatorKeyPressed(evt);
            }
        });
        pnlInput.add(txtByDokterOperator);
        txtByDokterOperator.setBounds(960, 120, 293, 23);

        jLabel7.setText("Biaya Dokter Operator : Rp");
        pnlInput.add(jLabel7);
        jLabel7.setBounds(660, 120, 278, 23);

        jLabel8.setText("Biaya Asisten Dokter Operator : Rp");
        pnlInput.add(jLabel8);
        jLabel8.setBounds(660, 150, 278, 23);

        txtByAssDokterOperator.setText("0");
        txtByAssDokterOperator.setHighlighter(null);
        txtByAssDokterOperator.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtByAssDokterOperatorKeyPressed(evt);
            }
        });
        pnlInput.add(txtByAssDokterOperator);
        txtByAssDokterOperator.setBounds(960, 150, 293, 23);

        jLabel11.setText("Biaya Dokter Anestesi : Rp");
        pnlInput.add(jLabel11);
        jLabel11.setBounds(660, 180, 278, 23);

        txtByDokterAnestesi.setText("0");
        txtByDokterAnestesi.setHighlighter(null);
        txtByDokterAnestesi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtByDokterAnestesiKeyPressed(evt);
            }
        });
        pnlInput.add(txtByDokterAnestesi);
        txtByDokterAnestesi.setBounds(960, 180, 293, 23);

        txtByPenataAnestesi.setText("0");
        txtByPenataAnestesi.setHighlighter(null);
        txtByPenataAnestesi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtByPenataAnestesiKeyPressed(evt);
            }
        });
        pnlInput.add(txtByPenataAnestesi);
        txtByPenataAnestesi.setBounds(960, 210, 293, 23);

        jLabel13.setText("Biaya Penata Anestesi : Rp");
        pnlInput.add(jLabel13);
        jLabel13.setBounds(660, 210, 278, 23);

        txtByDokterAnak.setText("0");
        txtByDokterAnak.setHighlighter(null);
        txtByDokterAnak.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtByDokterAnakKeyPressed(evt);
            }
        });
        pnlInput.add(txtByDokterAnak);
        txtByDokterAnak.setBounds(960, 240, 293, 23);

        jLabel14.setText("Biaya Dokter Anak : Rp");
        pnlInput.add(jLabel14);
        jLabel14.setBounds(660, 240, 278, 23);

        txtByDokterPendamping.setText("0");
        txtByDokterPendamping.setHighlighter(null);
        txtByDokterPendamping.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtByDokterPendampingKeyPressed(evt);
            }
        });
        pnlInput.add(txtByDokterPendamping);
        txtByDokterPendamping.setBounds(960, 270, 293, 23);

        jLabel15.setText("Biaya Dokter Pendamping : Rp");
        pnlInput.add(jLabel15);
        jLabel15.setBounds(660, 270, 278, 23);

        txtByPerawatBidan.setText("0");
        txtByPerawatBidan.setHighlighter(null);
        txtByPerawatBidan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtByPerawatBidanKeyPressed(evt);
            }
        });
        pnlInput.add(txtByPerawatBidan);
        txtByPerawatBidan.setBounds(960, 300, 293, 23);

        jLabel16.setText("Biaya Perawat / Bidan : Rp");
        pnlInput.add(jLabel16);
        jLabel16.setBounds(660, 300, 278, 23);

        txtByAssBidan.setText("0");
        txtByAssBidan.setHighlighter(null);
        txtByAssBidan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtByAssBidanKeyPressed(evt);
            }
        });
        pnlInput.add(txtByAssBidan);
        txtByAssBidan.setBounds(960, 330, 293, 23);

        jLabel17.setText("Biaya Asisten Bidan : Rp");
        pnlInput.add(jLabel17);
        jLabel17.setBounds(660, 330, 278, 23);

        txtByAlatDokter.setText("0");
        txtByAlatDokter.setHighlighter(null);
        txtByAlatDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtByAlatDokterKeyPressed(evt);
            }
        });
        pnlInput.add(txtByAlatDokter);
        txtByAlatDokter.setBounds(960, 360, 293, 23);

        jLabel18.setText("Biaya Alat Dokter : Rp");
        pnlInput.add(jLabel18);
        jLabel18.setBounds(660, 360, 278, 23);

        txtByRecoveryRoom.setText("0");
        txtByRecoveryRoom.setHighlighter(null);
        txtByRecoveryRoom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtByRecoveryRoomKeyPressed(evt);
            }
        });
        pnlInput.add(txtByRecoveryRoom);
        txtByRecoveryRoom.setBounds(960, 390, 293, 23);

        jLabel19.setText("Biaya Recovery Room : Rp");
        pnlInput.add(jLabel19);
        jLabel19.setBounds(660, 390, 278, 23);

        txtByPemakaianObatOk.setText("0");
        txtByPemakaianObatOk.setHighlighter(null);
        txtByPemakaianObatOk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtByPemakaianObatOkKeyPressed(evt);
            }
        });
        pnlInput.add(txtByPemakaianObatOk);
        txtByPemakaianObatOk.setBounds(960, 420, 293, 23);

        jLabel20.setText("Biaya Pemakaian Obat OK : Rp");
        pnlInput.add(jLabel20);
        jLabel20.setBounds(660, 420, 278, 23);

        txtJumlahBiaya.setText("0");
        txtJumlahBiaya.setHighlighter(null);
        txtJumlahBiaya.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtJumlahBiayaKeyPressed(evt);
            }
        });
        pnlInput.add(txtJumlahBiaya);
        txtJumlahBiaya.setBounds(960, 450, 293, 23);

        lblJumlah.setText("Jumlah Biaya : Rp");
        pnlInput.add(lblJumlah);
        lblJumlah.setBounds(660, 450, 278, 23);

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
        tglOrder1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-05-2018" }));
        tglOrder1.setDisplayFormat("dd-MM-yyyy");
        tglOrder1.setOpaque(false);
        tglOrder1.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass13.add(tglOrder1);

        jLabel38.setText("s.d");
        jLabel38.setPreferredSize(new java.awt.Dimension(18, 23));
        panelGlass13.add(jLabel38);

        tglOrder2.setEditable(false);
        tglOrder2.setForeground(new java.awt.Color(50, 70, 50));
        tglOrder2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-05-2018" }));
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
        tblTransaksi.setComponentPopupMenu(jPopupMenuTransaksi);
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
        tglTransaksi1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-05-2018" }));
        tglTransaksi1.setDisplayFormat("dd-MM-yyyy");
        tglTransaksi1.setOpaque(false);
        tglTransaksi1.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass12.add(tglTransaksi1);

        jLabel36.setText("s.d");
        jLabel36.setPreferredSize(new java.awt.Dimension(18, 23));
        panelGlass12.add(jLabel36);

        tglTransaksi2.setEditable(false);
        tglTransaksi2.setForeground(new java.awt.Color(50, 70, 50));
        tglTransaksi2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-05-2018" }));
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

    private void txtByOperasiKamarBedahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtByOperasiKamarBedahKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtByOperasiKamarBedahKeyPressed

    private void txtByAlatRumahSakitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtByAlatRumahSakitKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtByAlatRumahSakitKeyPressed

    private void txtByDokterOperatorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtByDokterOperatorKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtByDokterOperatorKeyPressed

    private void txtByAssDokterOperatorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtByAssDokterOperatorKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtByAssDokterOperatorKeyPressed

    private void txtByDokterAnestesiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtByDokterAnestesiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtByDokterAnestesiKeyPressed

    private void txtByPenataAnestesiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtByPenataAnestesiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtByPenataAnestesiKeyPressed

    private void txtByDokterAnakKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtByDokterAnakKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtByDokterAnakKeyPressed

    private void txtByDokterPendampingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtByDokterPendampingKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtByDokterPendampingKeyPressed

    private void txtByPerawatBidanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtByPerawatBidanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtByPerawatBidanKeyPressed

    private void txtByAssBidanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtByAssBidanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtByAssBidanKeyPressed

    private void txtByAlatDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtByAlatDokterKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtByAlatDokterKeyPressed

    private void txtByRecoveryRoomKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtByRecoveryRoomKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtByRecoveryRoomKeyPressed

    private void txtByPemakaianObatOkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtByPemakaianObatOkKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtByPemakaianObatOkKeyPressed

    private void txtJumlahBiayaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahBiayaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJumlahBiayaKeyPressed

    private void menuCetakBillingTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCetakBillingTransaksiActionPerformed
        
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
            String kdOperasi = null;

            // Mencari row utama di tiap transaksi
            for (int a = r; a >= 0; a--)
            {
                if (!tblTransaksi.getValueAt(a, 0).toString().isEmpty())
                {
                    kdOperasi = tblTransaksi.getValueAt(a, 0).toString();
                    break;
                }
            }

            // Kalo gak nemu kode operasi
            if (kdOperasi == null)
            {
                JOptionPane.showMessageDialog(null, "Operasi tidak ditemukan");
                return;
            }

            HashMap<String, String> h = new GQuery()
                    .a("SELECT kd_operasi, hrj_operasi.*, paket_operasi_2.nama_paket, pasien.no_rkm_medis, pasien.nm_pasien, pasien.group_unit, pasien.umur, kamar.kd_kamar, bangsal.nm_bangsal, poliklinik.nm_poli,")
                    .a("operasi_group.kd_group, operasi_detail.kd_detail, nm_group, nm_detail, tgl_operasi, jam_operasi, tgl_selesai, jam_selesai")
                    .a("FROM hrj_operasi")
                    .a("LEFT JOIN operasi_detail ON operasi_detail.kd_detail = hrj_operasi.kd_detail")
                    .a("JOIN paket_operasi_2 ON paket_operasi_2.kode_paket = hrj_operasi.kode_paket")
//                    .a("JOIN operasi_kategori ON operasi_kategori.kd_kategori = operasi_detail.kd_kategori")
                    .a("JOIN operasi_group ON operasi_group.kd_group = hrj_operasi.kd_group")
                    .a("JOIN reg_periksa ON reg_periksa.no_rawat = hrj_operasi.no_rawat")
                    .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                    .a("LEFT JOIN kamar_inap ON kamar_inap.no_rawat = hrj_operasi.no_rawat")
                    .a("LEFT JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                    .a("LEFT JOIN bangsal ON bangsal.kd_bangsal = kamar.kd_bangsal")
                    .a("LEFT JOIN poliklinik ON poliklinik.kd_poli = reg_periksa.kd_poli")
                    .a("WHERE kd_operasi = {kd_operasi}")
                    .set("kd_operasi", kdOperasi)
                    .getRowWithName();

            if (h != null)
            {
                Sequel.AutoComitFalse();
                Sequel.queryu("delete from temporary");
                
                Valid.panggilUrl("billing/LaporanBiayaOperasi.php?nama=" + h.get("nm_pasien").replace(" ", "_") + 
                        "&no_rm=" + h.get("no_rkm_medis") + "&umur=" + h.get("umur") + "&status=" + 
                        h.get("group_unit") + "&kelas=" + Utilz.cariKelasByNoRawat(h.get("no_rawat")) +
                        "&diagnosa=" + Utilz.cariDiagnosa(h.get("no_rawat")) + "&group=" + h.get("nm_group") +
                        "&kategori=" + h.get("nama_paket") + "&detail=" + h.get("nm_detail") + "&tanggal_operasi=" +
                        h.get("tgl_selesai") + 
                        "&dokter_operator=" + Utilz.cariNamaDokter(h.get("dokter_operator")).replace(" ", "_") + 
                        "&dokter_yang_merawat=" + Utilz.cariNamaDokter(h.get("dokter_yang_merawat")).replace(" ", "_") + 
                        "&ass_dokter_operator=" + Utilz.cariNamaDokter(h.get("ass_dokter_operator")).replace(" ", "_") + 
                        "&dokter_anestesi=" + Utilz.cariNamaDokter(h.get("dokter_anestesi")).replace(" ", "_") + 
                        "&penata_anestesi=" + Utilz.cariNamaDokter(h.get("penata_anestesi")).replace(" ", "_") + 
                        "&dokter_anak=" + Utilz.cariNamaDokter(h.get("dokter_anak")).replace(" ", "_") + 
                        "&dokter_pendamping=" + Utilz.cariNamaDokter(h.get("dokter_pendamping")).replace(" ", "_") + 
                        "&perawat_bidan=" + Utilz.cariNamaDokter(h.get("perawat_bidan")).replace(" ", "_") + 
                        "&admin=Admin_OK"
                );
                
                HashMap<String, String> m = new HashMap<>();
                m.put("Operasi Kamar Bedah", h.get("by_operasi_kamar_bedah"));
                m.put("Alat Rumah Sakit", h.get("by_alat_rumah_sakit"));
                m.put("Alat Dokter", h.get("by_alat_dokter"));
                m.put("Jasa Operator", h.get("by_dokter_operator"));
                m.put("Jasa Asisten Dr Operator", h.get("by_ass_dokter_operator"));
                m.put("Jasa Anestesi", h.get("by_dokter_anestesi"));
                m.put("Jasa Penata Anestesi", h.get("by_penata_anestesi"));
                m.put("Jasa Dr Spesialis Anak", h.get("by_dokter_anak"));
                m.put("Jasa Dr Pendamping", h.get("by_dokter_pendamping"));
                m.put("Jasa Perawat / Bidan", h.get("by_perawat_bidan"));
                m.put("Jasa Asisten Perawat / Bidan", h.get("by_ass_bidan"));
                m.put("Jasa Recovery Room", h.get("by_recovery_room"));
                m.put("Pemakaian Obat Ok", h.get("by_pemakaian_obat_ok"));
                
                m.entrySet().forEach(s -> 
                {
                    new GQuery().a("INSERT INTO temporary (temp1, temp3) VALUES ({1}, {3})")
                            .set("1", s.getKey())
                            .set("3", s.getValue())
                            .write();
                });
                
                Sequel.menyimpan("temporary", "'0', 'Total Biaya Operasi','" + h.get("jumlah_biaya") + "','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',''", "Transaksi Biaya Operasi");
                Sequel.AutoComitTrue();
            }
        }

        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_menuCetakBillingTransaksiActionPerformed

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
    private widget.Label jLabel11;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel20;
    private widget.Label jLabel28;
    private widget.Label jLabel35;
    private widget.Label jLabel36;
    private widget.Label jLabel37;
    private widget.Label jLabel38;
    private widget.Label jLabel4;
    private widget.Label jLabel5;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPopupMenu jPopupMenuTransaksi;
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
    private widget.Label lblJumlah;
    private javax.swing.JMenuItem menuCetakBillingTransaksi;
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
    private widget.TextBox txtByAlatDokter;
    private widget.TextBox txtByAlatRumahSakit;
    private widget.TextBox txtByAssBidan;
    private widget.TextBox txtByAssDokterOperator;
    private widget.TextBox txtByDokterAnak;
    private widget.TextBox txtByDokterAnestesi;
    private widget.TextBox txtByDokterOperator;
    private widget.TextBox txtByDokterPendamping;
    private widget.TextBox txtByOperasiKamarBedah;
    private widget.TextBox txtByPemakaianObatOk;
    private widget.TextBox txtByPenataAnestesi;
    private widget.TextBox txtByPerawatBidan;
    private widget.TextBox txtByRecoveryRoom;
    private widget.TextBox txtJenisAnasthesia;
    private widget.TextBox txtJumlahBiaya;
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
                .a("DELETE FROM hrj_operasi WHERE kd_operasi = {kd_operasi}")
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

    private void tampilBiaya(String kdPaket)
    {
        HashMap<String, String> h = Utilz.getNeoPaket(kdPaket);
        
        if (h == null)
        {
            txtByOperasiKamarBedah.setText("0");
            txtByAlatRumahSakit.setText("0");
            txtByDokterOperator.setText("0");
            txtByAssDokterOperator.setText("0");
            txtByDokterAnestesi.setText("0");
            txtByPenataAnestesi.setText("0");
            txtByDokterAnak.setText("0");
            txtByDokterPendamping.setText("0");
            txtByPerawatBidan.setText("0");
            txtByAssBidan.setText("0");
            txtByAlatDokter.setText("0");
            txtByRecoveryRoom.setText("0");
            txtByPemakaianObatOk.setText("0");
        }
        else
        {
            txtByOperasiKamarBedah.setText("0");
            txtByAlatRumahSakit.setText(h.get("mat"));
            txtByDokterOperator.setText(h.get("operator"));
            txtByAssDokterOperator.setText(h.get("asisten_operator"));
            txtByDokterAnestesi.setText(h.get("dokter_anestesi"));
            txtByPenataAnestesi.setText(h.get("penata_anestesi"));
            txtByDokterPendamping.setText(h.get("instrumentator"));
            txtByPerawatBidan.setText(h.get("on_loop"));
            txtByAssBidan.setText("0");
            txtByAlatDokter.setText(h.get("linen"));
            txtByDokterAnak.setText("0");
            txtByRecoveryRoom.setText("0");
            txtByPemakaianObatOk.setText("0");
            txtJumlahBiaya.setText(h.get("tarif"));
        }
    }
    
    TextChangedListener t = t -> 
    {
        int total = 
                GConvert.parseInt(txtByOperasiKamarBedah.getText()) + 
                GConvert.parseInt(txtByAlatRumahSakit.getText()) +
                GConvert.parseInt(txtByDokterOperator.getText()) +
                GConvert.parseInt(txtByAssDokterOperator.getText()) +
                GConvert.parseInt(txtByDokterAnestesi.getText()) +
                GConvert.parseInt(txtByPenataAnestesi.getText()) +
                GConvert.parseInt(txtByDokterAnak.getText()) +
                GConvert.parseInt(txtByDokterPendamping.getText()) +
                GConvert.parseInt(txtByPerawatBidan.getText()) +
                GConvert.parseInt(txtByAssBidan.getText()) +
                GConvert.parseInt(txtByAlatDokter.getText()) +
                GConvert.parseInt(txtByRecoveryRoom.getText()) +
                GConvert.parseInt(txtByPemakaianObatOk.getText());
        
        txtJumlahBiaya.setText(total + "");
    };
    
    private static class Utilz
    {
        static HashMap<String, String> getNeoPaket(String kodePaket)
        {
            return new GQuery()
                    .a("SELECT * FROM paket_operasi_2 WHERE kode_paket = '" + kodePaket + "'")
                    .getRowWithName();
        }
        
        static String cariNamaDokter(String kdDokter)
        {
            return new GQuery()
                    .a("SELECT nm_dokter FROM dokter WHERE kd_dokter = {x}")
                    .set("x", kdDokter)
                    .getString();
        }
        
        static String cariDiagnosa(String noRawat)
        {
            String s = new GQuery()
                    .a("SELECT p.nm_penyakit FROM diagnosa_pasien dp")
                    .a("JOIN penyakit p ON dp.kd_penyakit = p.kd_penyakit")
                    .a("WHERE dp.no_rawat = {no_rawat}")
                    .set("no_rawat", noRawat)
                    .getString();
            
            return s.isEmpty() ? "-" : s;
        }
    
        static String cariKelasByNoRawat(String noRawat)
        {
            String s = new GQuery()
                .a("SELECT kelas")
                .a("FROM kamar_inap")
                .a("JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                .a("WHERE no_rawat = {no_rawat}")
                .set("no_rawat", noRawat)
                .getString();

            return s.isEmpty() ? "Kelas 3" : s;
        }
        
        static String getNextKodeOperasi()
        {
            return String.valueOf(GConvert.parseInt(
                    new GQuery("SELECT kd_operasi FROM hrj_operasi ORDER BY kd_operasi DESC").getString()
            ) + 1);
        }

        static String convertKelasAngkaToRoma(String kelas) 
        {
            String[] as = kelas.split(" ");

            if (as.length == 1)
            {
                return kelas;
            }
            else if (as[1].equals("1"))
            {
                return as[0] + " I"; 
            }
            else if (as[1].equals("2"))
            {
                return as[0] + " II"; 
            }
            else if (as[1].equals("3"))
            {
                return as[0] + " III"; 
            }

            return kelas;
        }
    }
}