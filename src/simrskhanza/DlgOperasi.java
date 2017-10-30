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
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import util.GMessage;

/**
 *
 * @author GrT
 */
public class DlgOperasi extends BaseDialog
{
    // Const =========
    private static final int OPERATOR_1 = 0;
    private static final int OPERATOR_2 = 1;
    private static final int OPERATOR_3 = 2;
    private static final int ASISTEN_OPERATOR_1 = 3;
    private static final int ASISTEN_OPERATOR_2 = 4;
    private static final int ASISTEN_OPERATOR_3 = 5;
    private static final int ANESTESIA = 6;
    private static final int ASISTEN_ANESTESIA_1 = 7;
    private static final int ASISTEN_ANESTESIA_2 = 8;
    private static final int DR_ANAK = 9;
    private static final int BIDAN_1 = 10;
    private static final int BIDAN_2 = 11;
    private static final int BIDAN_3 = 12;
    private static final int PR_LUAR = 13;
    private static final int PR_RESUS = 14;
    private static final int INSTRUMEN = 15;
    private static final int DR_PJ_ANAK = 16;
    private static final int DR_UMUM = 17;
    private static final int ONLOOP_1 = 18;
    private static final int ONLOOP_2 = 19;
    private static final int ONLOOP_3 = 20;
    private static final int ONLOOP_4 = 21;
    private static final int ONLOOP_5 = 22;

    // Child Forms ==========
    DlgCariGroupOperasi dlgGroup = new DlgCariGroupOperasi(null, false);
    DlgCariKategoriOperasi dlgKategori = new DlgCariKategoriOperasi(null, false);
    DlgCariDetailOperasi dlgDetail = new DlgCariDetailOperasi(null, false);
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
    private boolean isEdit = false;
    private String kelas;
    private String status;

    /**
     * Creates new form DlgOperasi
     */
    public DlgOperasi(java.awt.Frame parent, boolean modal)
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
                if (pil == ASISTEN_OPERATOR_1)
                {
                    txtKdAsistenOperator1.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaAsistenOperator1.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdAsistenOperator1.requestFocus();
                }
                else if (pil == ASISTEN_OPERATOR_2)
                {
                    txtKdAsistenOperator2.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaAsistenOperator2.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdAsistenOperator2.requestFocus();
                }
                else if (pil == ASISTEN_OPERATOR_3)
                {
                    txtKdAsistenOperator3.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaAsistenOperator3.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdAsistenOperator3.requestFocus();
                }
                else if (pil == INSTRUMEN)
                {
                    txtKdInstrumen.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaInstrumen.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdInstrumen.requestFocus();
                }
                else if (pil == ASISTEN_ANESTESIA_1)
                {
                    txtKdAsistenAnestesia1.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaAsistenAnestesia1.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdAsistenAnestesia1.requestFocus();
                }
                else if (pil == ASISTEN_ANESTESIA_2)
                {
                    txtKdAsistenAnestesia2.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaAsistenAnestesia2.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdAsistenAnestesia2.requestFocus();
                }
                else if (pil == PR_RESUS)
                {
                    txtKdPrResus.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaPrResus.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdPrResus.requestFocus();
                }
                else if (pil == PR_LUAR)
                {
                    txtKdPerawatLuar.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaPerawatLuar.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdPerawatLuar.requestFocus();
                }
                else if (pil == BIDAN_1)
                {
                    txtKdBidan1.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaBidan1.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdBidan1.requestFocus();
                }
                else if (pil == BIDAN_2)
                {
                    txtKdBidan2.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaBidan2.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdBidan2.requestFocus();
                }
                else if (pil == BIDAN_3)
                {
                    txtKdBidan3.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaBidan3.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdBidan3.requestFocus();
                }
                else if (pil == ONLOOP_1)
                {
                    txtKdOnloop1.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaOnloop1.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdOnloop1.requestFocus();
                }
                else if (pil == ONLOOP_2)
                {
                    txtKdOnloop2.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaOnloop2.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdOnloop2.requestFocus();
                }
                else if (pil == ONLOOP_3)
                {
                    txtKdOnloop3.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaOnloop3.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdOnloop3.requestFocus();
                }
                else if (pil == ONLOOP_4)
                {
                    txtKdOnloop4.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaOnloop4.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdOnloop4.requestFocus();
                }
                else if (pil == ONLOOP_5)
                {
                    txtKdOnloop5.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 0).toString());
                    txtNamaOnloop5.setText(dlgPetugas.getTable().getValueAt(dlgPetugas.getTable().getSelectedRow(), 1).toString());
                    txtKdOnloop5.requestFocus();
                }
            }
        });
        
        dlgDokter.addWindowClosedListener(() ->
        {
            if (pil == OPERATOR_1)
            {
                txtKdOperator1.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 0).toString());
                txtNamaOperator1.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 1).toString());
                txtKdOperator1.requestFocus();
            }
            else if (pil == OPERATOR_2)
            {
                txtKdOperator2.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 0).toString());
                txtNamaOperator2.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 1).toString());
                txtKdOperator2.requestFocus();
            }
            else if (pil == OPERATOR_3)
            {
                txtKdOperator3.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 0).toString());
                txtNamaOperator3.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 1).toString());
                txtKdOperator3.requestFocus();
            }
            else if (pil == ANESTESIA)
            {
                txtKdAnestesia.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 0).toString());
                txtNamaAnestesia.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 1).toString());
                txtKdAnestesia.requestFocus();
            }
            else if (pil == DR_ANAK)
            {
                txtKdDrAnak.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 0).toString());
                txtNamaDrAnak.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 1).toString());
                txtKdDrAnak.requestFocus();
            }
            else if (pil == DR_PJ_ANAK)
            {
                txtKdPjAnak.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 0).toString());
                txtNamaPjAnak.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 1).toString());
                txtKdPjAnak.requestFocus();
            }
            else if (pil == DR_UMUM)
            {
                txtKdDrUmum.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 0).toString());
                txtNamaDrUmum.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(), 1).toString());
                txtKdDrUmum.requestFocus();
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
        
        txtKdOperator1.addTextChangedListener((t) -> txtNamaOperator1.setText(""));
        txtKdOperator2.addTextChangedListener((t) -> txtNamaOperator2.setText(""));
        txtKdOperator3.addTextChangedListener((t) -> txtNamaOperator3.setText(""));
        txtKdAsistenOperator1.addTextChangedListener((t) -> txtNamaAsistenOperator1.setText(""));
        txtKdAsistenOperator2.addTextChangedListener((t) -> txtNamaAsistenOperator2.setText(""));
        txtKdAsistenOperator3.addTextChangedListener((t) -> txtNamaAsistenOperator3.setText(""));
        txtKdAnestesia.addTextChangedListener((t) -> txtNamaAnestesia.setText(""));
        txtKdAsistenAnestesia1.addTextChangedListener((t) -> txtNamaAsistenAnestesia1.setText(""));
        txtKdAsistenAnestesia2.addTextChangedListener((t) -> txtNamaAsistenAnestesia2.setText(""));
        txtKdDrAnak.addTextChangedListener((t) -> txtNamaDrAnak.setText(""));
        txtKdBidan1.addTextChangedListener((t) -> txtNamaBidan1.setText(""));
        txtKdBidan2.addTextChangedListener((t) -> txtNamaBidan2.setText(""));
        txtKdBidan3.addTextChangedListener((t) -> txtNamaBidan3.setText(""));
        txtKdPerawatLuar.addTextChangedListener((t) -> txtNamaPerawatLuar.setText(""));
        txtKdPrResus.addTextChangedListener((t) -> txtNamaPrResus.setText(""));
        txtKdInstrumen.addTextChangedListener((t) -> txtNamaInstrumen.setText(""));
        txtKdPjAnak.addTextChangedListener((t) -> txtNamaPjAnak.setText(""));
        txtKdDrUmum.addTextChangedListener((t) -> txtNamaDrUmum.setText(""));
        txtKdOnloop1.addTextChangedListener((t) -> txtNamaOnloop1.setText(""));
        txtKdOnloop2.addTextChangedListener((t) -> txtNamaOnloop2.setText(""));
        txtKdOnloop3.addTextChangedListener((t) -> txtNamaOnloop3.setText(""));
        txtKdOnloop4.addTextChangedListener((t) -> txtNamaOnloop4.setText(""));
        txtKdOnloop5.addTextChangedListener((t) -> txtNamaOnloop5.setText(""));
    }
    
    private void initTblOrder()
    {
        Object[] row =
        {
            "Kd Order", "Kd Group", "Kd Kategori", "Kd Detail", "No Rawat", "Pasien", "Group", "Kategori", "Detail", "Tgl Operasi"
        };
        
        int[] sz = 
        {
            80, 0, 0, 0, 120, 300, 200, 200, 200, 120
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
                .a("SELECT kd_order, operasi_order.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, kamar.kd_kamar, bangsal.nm_bangsal, poliklinik.nm_poli,")
                .a("    operasi_group.kd_group, operasi_kategori.kd_kategori, operasi_detail.kd_detail, nm_group, nm_kategori, nm_detail, tgl_operasi")
                .a("FROM operasi_order")
                .a("JOIN operasi_detail ON operasi_detail.kd_detail = operasi_order.kd_detail")
                .a("JOIN operasi_kategori ON operasi_kategori.kd_kategori = operasi_detail.kd_kategori")
                .a("JOIN operasi_group ON operasi_group.kd_group = operasi_kategori.kd_group")
                .a("JOIN reg_periksa ON reg_periksa.no_rawat = operasi_order.no_rawat")
                .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                .a("LEFT JOIN kamar_inap ON kamar_inap.no_rawat = operasi_order.no_rawat")
                .a("LEFT JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                .a("LEFT JOIN bangsal ON bangsal.kd_bangsal = kamar.kd_bangsal")
                .a("LEFT JOIN poliklinik ON poliklinik.kd_poli = reg_periksa.kd_poli")
                .a("WHERE DATE(tgl_operasi) BETWEEN :tgl1 AND :tgl2")
                .a("ORDER BY tgl_operasi");
    }
    
    private void initTblTransaksi()
    {
        Object[] row =
        {
            "Kd Operasi", "Kd Group", "Kd Kategori", "Kd Detail", "No Rawat", "Pasien", "Group", "Kategori", "Detail", "Tgl Operasi"
        };
        
        int[] sz = 
        {
            80, 0, 0, 0, 120, 300, 200, 200, 200, 120
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
                .a("    operasi_group.kd_group, operasi_kategori.kd_kategori, operasi_detail.kd_detail, nm_group, nm_kategori, nm_detail, tgl_operasi")
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
                .a("WHERE DATE(tgl_operasi) BETWEEN :tgl1 AND :tgl2")
                .a("ORDER BY tgl_operasi");
    }
    
    // Methods ==========
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
        isEdit = false;
        
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
        isEdit = true;
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
        txtKdOperator1.setText(m.get("operator1"));
        txtKdOperator2.setText(m.get("operator2"));
        txtKdOperator3.setText(m.get("operator3"));
        txtKdAsistenOperator1.setText(m.get("asisten_operator1"));
        txtKdAsistenOperator2.setText(m.get("asisten_operator2"));
        txtKdAsistenOperator3.setText(m.get("asisten_operator3"));
        txtKdAnestesia.setText(m.get("dokter_anestesi"));
        txtKdAsistenAnestesia1.setText(m.get("asisten_anestesi"));
        txtKdAsistenAnestesia2.setText(m.get("asisten_anestesi2"));
        txtKdDrAnak.setText(m.get("dokter_anak"));
        txtKdBidan1.setText(m.get("bidan"));
        txtKdBidan2.setText(m.get("bidan2"));
        txtKdBidan3.setText(m.get("bidan3"));
        txtKdPerawatLuar.setText(m.get("perawat_luar"));
        txtKdPrResus.setText(m.get("perawaat_resusitas"));
        txtKdInstrumen.setText(m.get("instrumen"));
        txtKdPjAnak.setText(m.get("dokter_pjanak"));
        txtKdDrUmum.setText(m.get("dokter_umum"));
        txtKdOnloop1.setText(m.get("omloop"));
        txtKdOnloop2.setText(m.get("omloop2"));
        txtKdOnloop3.setText(m.get("omloop3"));
        txtKdOnloop4.setText(m.get("omloop4"));
        txtKdOnloop5.setText(m.get("omloop5"));
        
        Sequel.cariIsi("SELECT nm_dokter FROM dokter WHERE kd_dokter = ?", txtNamaOperator1, txtKdOperator1.getText());
        Sequel.cariIsi("SELECT nm_dokter FROM dokter WHERE kd_dokter = ?", txtNamaOperator2, txtKdOperator2.getText());
        Sequel.cariIsi("SELECT nm_dokter FROM dokter WHERE kd_dokter = ?", txtNamaOperator3, txtKdOperator3.getText());
        Sequel.cariIsi("SELECT nm_dokter FROM dokter WHERE kd_dokter = ?", txtNamaAnestesia, txtKdAnestesia.getText());
        Sequel.cariIsi("SELECT nm_dokter FROM dokter WHERE kd_dokter = ?", txtNamaDrAnak, txtKdDrAnak.getText());
        Sequel.cariIsi("SELECT nm_dokter FROM dokter WHERE kd_dokter = ?", txtNamaPjAnak, txtKdPjAnak.getText());
        Sequel.cariIsi("SELECT nm_dokter FROM dokter WHERE kd_dokter = ?", txtNamaDrUmum, txtKdDrUmum.getText());
        
        Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", txtNamaAsistenOperator1, txtKdAsistenOperator1.getText());
        Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", txtNamaAsistenOperator2, txtKdAsistenOperator2.getText());
        Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", txtNamaAsistenOperator3, txtKdAsistenOperator3.getText());
        Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", txtNamaAsistenAnestesia1, txtKdAsistenAnestesia1.getText());
        Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", txtNamaAsistenAnestesia2, txtKdAsistenAnestesia2.getText());
        Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", txtNamaBidan1, txtKdBidan1.getText());
        Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", txtNamaBidan2, txtKdBidan2.getText());
        Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", txtNamaBidan3, txtKdBidan3.getText());
        Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", txtNamaPerawatLuar, txtKdPerawatLuar.getText());
        Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", txtNamaPrResus, txtKdPrResus.getText());
        Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", txtNamaInstrumen, txtKdInstrumen.getText());
        Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", txtNamaOnloop1, txtKdOnloop1.getText());
        Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", txtNamaOnloop2, txtKdOnloop2.getText());
        Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", txtNamaOnloop3, txtKdOnloop3.getText());
        Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", txtNamaOnloop4, txtKdOnloop4.getText());
        Sequel.cariIsi("SELECT nama FROM petugas WHERE nip = ?", txtNamaOnloop5, txtKdOnloop5.getText());
        
        cariKelas();
    }
    
    private void simpan()
    {
        if (!valid())
            return;
        
        HashMap<String, String> paket = getPaket();
        
        boolean success;
        
        if (isEdit)
        {
            success = new GQuery()
                    .a("UPDATE operasi SET")
                    .a("kd_detail = {kd_detail},")
                    .a("kode_paket = {kode_paket},")
                    .a("jenis_anasthesi = {jenis_anasthesi},")
                    .a("operator1 = {operator1},")
                    .a("operator2 = {operator2},")
                    .a("operator3 = {operator3},")
                    .a("asisten_operator1 = {asisten_operator1},")
                    .a("asisten_operator2 = {asisten_operator2},")
                    .a("asisten_operator3 = {asisten_operator3},")
                    .a("instrumen = {instrumen},")
                    .a("dokter_anak = {dokter_anak},")
                    .a("perawaat_resusitas = {perawaat_resusitas},")
                    .a("dokter_anestesi = {dokter_anestesi},")
                    .a("asisten_anestesi = {asisten_anestesi},")
                    .a("asisten_anestesi2 = {asisten_anestesi2},")
                    .a("bidan = {bidan},")
                    .a("bidan2 = {bidan2},")
                    .a("bidan3 = {bidan3},")
                    .a("perawat_luar = {perawat_luar},")
                    .a("omloop = {omloop},")
                    .a("omloop2 = {omloop2},")
                    .a("omloop3 = {omloop3},")
                    .a("omloop4 = {omloop4},")
                    .a("omloop5 = {omloop5},")
                    .a("dokter_pjanak = {dokter_pjanak},")
                    .a("dokter_umum = {dokter_umum},")
                    .a("biayaoperator1 = {biayaoperator1},")
                    .a("biayaoperator2 = {biayaoperator2},")
                    .a("biayaoperator3 = {biayaoperator3},")
                    .a("biayaasisten_operator1 = {biayaasisten_operator1},")
                    .a("biayaasisten_operator2 = {biayaasisten_operator2},")
                    .a("biayaasisten_operator3 = {biayaasisten_operator3},")
                    .a("biayainstrumen = {biayainstrumen},")
                    .a("biayadokter_anak = {biayadokter_anak},")
                    .a("biayaperawaat_resusitas = {biayaperawaat_resusitas},")
                    .a("biayadokter_anestesi = {biayadokter_anestesi},")
                    .a("biayaasisten_anestesi = {biayaasisten_anestesi},")
                    .a("biayaasisten_anestesi2 = {biayaasisten_anestesi2},")
                    .a("biayabidan = {biayabidan},")
                    .a("biayabidan2 = {biayabidan2},")
                    .a("biayabidan3 = {biayabidan3},")
                    .a("biayaperawat_luar = {biayaperawat_luar},")
                    .a("biayaalat = {biayaalat},")
                    .a("biayasewaok = {biayasewaok},")
                    .a("akomodasi = {akomodasi},")
                    .a("bagian_rs = {bagian_rs},")
                    .a("biaya_omloop = {biaya_omloop},")
                    .a("biaya_omloop2 = {biaya_omloop2},")
                    .a("biaya_omloop3 = {biaya_omloop3},")
                    .a("biaya_omloop4 = {biaya_omloop4},")
                    .a("biaya_omloop5 = {biaya_omloop5},")
                    .a("biayasarpras = {biayasarpras},")
                    .a("biaya_dokter_pjanak = {biaya_dokter_pjanak},")
                    .a("biaya_dokter_umum = {biaya_dokter_umum}")
                    .a("WHERE kd_operasi = {kd_operasi}")
                    .set("kd_operasi", tblTransaksi.getValueAt(tblTransaksi.getSelectedRow(), 0).toString())
                    .set("kd_detail", txtKdDetail.getText())
                    .set("kode_paket", paket.get("kode_paket"))
                    .set("jenis_anasthesi", txtJenisAnasthesia.getText())
                    .set("operator1", txtKdOperator1.getText())
                    .setNoQuote("operator2", txtKdOperator2.getText().isEmpty() ? "NULL" : "'" + txtKdOperator2.getText() + "'")
                    .setNoQuote("operator3", txtKdOperator3.getText().isEmpty() ? "NULL" : "'" + txtKdOperator3.getText() + "'")
                    .setNoQuote("asisten_operator1", txtKdAsistenOperator1.getText().isEmpty() ? "NULL" : "'" + txtKdAsistenOperator1.getText() + "'")
                    .setNoQuote("asisten_operator2", txtKdAsistenOperator2.getText().isEmpty() ? "NULL" : "'" + txtKdAsistenOperator2.getText() + "'")
                    .setNoQuote("asisten_operator3", txtKdAsistenOperator3.getText().isEmpty() ? "NULL" : "'" + txtKdAsistenOperator3.getText() + "'")
                    .setNoQuote("instrumen", txtKdInstrumen.getText().isEmpty() ? "NULL" : "'" + txtKdInstrumen.getText() + "'")
                    .setNoQuote("dokter_anak", txtKdDrAnak.getText().isEmpty() ? "NULL" : "'" + txtKdDrAnak.getText() + "'")
                    .setNoQuote("perawaat_resusitas", txtKdPrResus.getText().isEmpty() ? "NULL" : "'" + txtKdPrResus.getText() + "'")
                    .setNoQuote("dokter_anestesi", txtKdAnestesia.getText().isEmpty() ? "NULL" : "'" + txtKdAnestesia.getText() + "'")
                    .setNoQuote("asisten_anestesi", txtKdAsistenAnestesia1.getText().isEmpty() ? "NULL" : "'" + txtKdAsistenAnestesia1.getText() + "'")
                    .setNoQuote("asisten_anestesi2", txtKdAsistenAnestesia2.getText().isEmpty() ? "NULL" : "'" + txtKdAsistenAnestesia2.getText() + "'")
                    .setNoQuote("bidan", txtKdBidan1.getText().isEmpty() ? "NULL" : "'" + txtKdBidan1.getText() + "'")
                    .setNoQuote("bidan2", txtKdBidan2.getText().isEmpty() ? "NULL" : "'" + txtKdBidan2.getText() + "'")
                    .setNoQuote("bidan3", txtKdBidan3.getText().isEmpty() ? "NULL" : "'" + txtKdBidan3.getText() + "'")
                    .setNoQuote("perawat_luar", txtKdPerawatLuar.getText().isEmpty() ? "NULL" : "'" + txtKdPerawatLuar.getText() + "'")
                    .setNoQuote("omloop", txtKdOnloop1.getText().isEmpty() ? "NULL" : "'" + txtKdOnloop1.getText() + "'")
                    .setNoQuote("omloop2", txtKdOnloop2.getText().isEmpty() ? "NULL" : "'" + txtKdOnloop2.getText() + "'")
                    .setNoQuote("omloop3", txtKdOnloop3.getText().isEmpty() ? "NULL" : "'" + txtKdOnloop3.getText() + "'")
                    .setNoQuote("omloop4", txtKdOnloop4.getText().isEmpty() ? "NULL" : "'" + txtKdOnloop4.getText() + "'")
                    .setNoQuote("omloop5", txtKdOnloop5.getText().isEmpty() ? "NULL" : "'" + txtKdOnloop5.getText() + "'")
                    .setNoQuote("dokter_pjanak", txtKdPjAnak.getText().isEmpty() ? "NULL" : "'" + txtKdPjAnak.getText() + "'")
                    .setNoQuote("dokter_umum", txtKdDrUmum.getText().isEmpty() ? "NULL" : "'" + txtKdDrUmum.getText() + "'")
                    .set("biayaoperator1", paket.get("operator1"))
                    .set("biayaoperator2", paket.get("operator2"))
                    .set("biayaoperator3", paket.get("operator3"))
                    .set("biayaasisten_operator1", paket.get("asisten_operator1"))
                    .set("biayaasisten_operator2", paket.get("asisten_operator2"))
                    .set("biayaasisten_operator3", paket.get("asisten_operator3"))
                    .set("biayainstrumen", paket.get("instrumen"))
                    .set("biayadokter_anak", paket.get("dokter_anak"))
                    .set("biayaperawaat_resusitas", paket.get("perawaat_resusitas"))
                    .set("biayadokter_anestesi", paket.get("dokter_anestesi"))
                    .set("biayaasisten_anestesi", paket.get("asisten_anestesi"))
                    .set("biayaasisten_anestesi2", paket.get("asisten_anestesi2"))
                    .set("biayabidan", paket.get("bidan"))
                    .set("biayabidan2", paket.get("bidan2"))
                    .set("biayabidan3", paket.get("bidan3"))
                    .set("biayaperawat_luar", paket.get("perawat_luar"))
                    .set("biayaalat", paket.get("sewa_ok"))
                    .set("biayasewaok", paket.get("alat"))
                    .set("akomodasi", paket.get("akomodasi"))
                    .set("bagian_rs", paket.get("bagian_rs"))
                    .set("biaya_omloop", paket.get("omloop"))
                    .set("biaya_omloop2", paket.get("omloop2"))
                    .set("biaya_omloop3", paket.get("omloop3"))
                    .set("biaya_omloop4", paket.get("omloop4"))
                    .set("biaya_omloop5", paket.get("omloop5"))
                    .set("biayasarpras", paket.get("sarpras"))
                    .set("biaya_dokter_pjanak", paket.get("dokter_pjanak"))
                    .set("biaya_dokter_umum", paket.get("dokter_umum"))
                    .write();
        }
        else
        {
            success = new GQuery()
                    .a("INSERT INTO operasi")
                    .a("(no_rawat, kd_detail, kode_paket, tgl_operasi, jenis_anasthesi, operator1, operator2, operator3,")
                    .a("asisten_operator1, asisten_operator2, asisten_operator3, instrumen, dokter_anak, perawaat_resusitas,")
                    .a("dokter_anestesi, asisten_anestesi, asisten_anestesi2, bidan, bidan2, bidan3, perawat_luar,")
                    .a("omloop, omloop2, omloop3, omloop4, omloop5, dokter_pjanak, dokter_umum,")
                    .a("biayaoperator1, biayaoperator2, biayaoperator3, biayaasisten_operator1, biayaasisten_operator2, biayaasisten_operator3,")
                    .a("biayainstrumen, biayadokter_anak, biayaperawaat_resusitas, biayadokter_anestesi, biayaasisten_anestesi, biayaasisten_anestesi2,")
                    .a("biayabidan, biayabidan2, biayabidan3, biayaperawat_luar, biayaalat, biayasewaok, akomodasi, bagian_rs,")
                    .a("biaya_omloop, biaya_omloop2, biaya_omloop3, biaya_omloop4, biaya_omloop5, biayasarpras, biaya_dokter_pjanak, biaya_dokter_umum,")
                    .a("status) VALUES")
                    .a("({no_rawat}, {kd_detail}, {kode_paket}, {tgl_operasi}, {jenis_anasthesi}, {operator1}, {operator2}, {operator3},")
                    .a("{asisten_operator1}, {asisten_operator2}, {asisten_operator3}, {instrumen}, {dokter_anak}, {perawaat_resusitas},")
                    .a("{dokter_anestesi}, {asisten_anestesi}, {asisten_anestesi2}, {bidan}, {bidan2}, {bidan3}, {perawat_luar},")
                    .a("{omloop}, {omloop2}, {omloop3}, {omloop4}, {omloop5}, {dokter_pjanak}, {dokter_umum},")
                    .a("{biayaoperator1}, {biayaoperator2}, {biayaoperator3}, {biayaasisten_operator1}, {biayaasisten_operator2}, {biayaasisten_operator3},")
                    .a("{biayainstrumen}, {biayadokter_anak}, {biayaperawaat_resusitas}, {biayadokter_anestesi}, {biayaasisten_anestesi}, {biayaasisten_anestesi2},")
                    .a("{biayabidan}, {biayabidan2}, {biayabidan3}, {biayaperawat_luar}, {biayaalat}, {biayasewaok}, {akomodasi}, {bagian_rs},")
                    .a("{biaya_omloop}, {biaya_omloop2}, {biaya_omloop3}, {biaya_omloop4}, {biaya_omloop5}, {biayasarpras}, {biaya_dokter_pjanak}, {biaya_dokter_umum},")
                    .a("{status})")
                    .set("no_rawat", txtNoRawat.getText())
                    .set("kd_detail", txtKdDetail.getText())
                    .set("kode_paket", paket.get("kode_paket"))
                    .set("tgl_operasi", Valid.SetTgl(DTPBeri.getSelectedItem().toString()) + " " 
                            + cmbJam.getSelectedItem() + ":" + cmbMnt.getSelectedItem() + ":" + cmbDtk.getSelectedItem())
                    .set("jenis_anasthesi", txtJenisAnasthesia.getText())
                    .set("operator1", txtKdOperator1.getText())
                    .setNoQuote("operator2", txtKdOperator2.getText().isEmpty() ? "NULL" : "'" + txtKdOperator2.getText() + "'")
                    .setNoQuote("operator3", txtKdOperator3.getText().isEmpty() ? "NULL" : "'" + txtKdOperator3.getText() + "'")
                    .setNoQuote("asisten_operator1", txtKdAsistenOperator1.getText().isEmpty() ? "NULL" : "'" + txtKdAsistenOperator1.getText() + "'")
                    .setNoQuote("asisten_operator2", txtKdAsistenOperator2.getText().isEmpty() ? "NULL" : "'" + txtKdAsistenOperator2.getText() + "'")
                    .setNoQuote("asisten_operator3", txtKdAsistenOperator3.getText().isEmpty() ? "NULL" : "'" + txtKdAsistenOperator3.getText() + "'")
                    .setNoQuote("instrumen", txtKdInstrumen.getText().isEmpty() ? "NULL" : "'" + txtKdInstrumen.getText() + "'")
                    .setNoQuote("dokter_anak", txtKdDrAnak.getText().isEmpty() ? "NULL" : "'" + txtKdDrAnak.getText() + "'")
                    .setNoQuote("perawaat_resusitas", txtKdPrResus.getText().isEmpty() ? "NULL" : "'" + txtKdPrResus.getText() + "'")
                    .setNoQuote("dokter_anestesi", txtKdAnestesia.getText().isEmpty() ? "NULL" : "'" + txtKdAnestesia.getText() + "'")
                    .setNoQuote("asisten_anestesi", txtKdAsistenAnestesia1.getText().isEmpty() ? "NULL" : "'" + txtKdAsistenAnestesia1.getText() + "'")
                    .setNoQuote("asisten_anestesi2", txtKdAsistenAnestesia2.getText().isEmpty() ? "NULL" : "'" + txtKdAsistenAnestesia2.getText() + "'")
                    .setNoQuote("bidan", txtKdBidan1.getText().isEmpty() ? "NULL" : "'" + txtKdBidan1.getText() + "'")
                    .setNoQuote("bidan2", txtKdBidan2.getText().isEmpty() ? "NULL" : "'" + txtKdBidan2.getText() + "'")
                    .setNoQuote("bidan3", txtKdBidan3.getText().isEmpty() ? "NULL" : "'" + txtKdBidan3.getText() + "'")
                    .setNoQuote("perawat_luar", txtKdPerawatLuar.getText().isEmpty() ? "NULL" : "'" + txtKdPerawatLuar.getText() + "'")
                    .setNoQuote("omloop", txtKdOnloop1.getText().isEmpty() ? "NULL" : "'" + txtKdOnloop1.getText() + "'")
                    .setNoQuote("omloop2", txtKdOnloop2.getText().isEmpty() ? "NULL" : "'" + txtKdOnloop2.getText() + "'")
                    .setNoQuote("omloop3", txtKdOnloop3.getText().isEmpty() ? "NULL" : "'" + txtKdOnloop3.getText() + "'")
                    .setNoQuote("omloop4", txtKdOnloop4.getText().isEmpty() ? "NULL" : "'" + txtKdOnloop4.getText() + "'")
                    .setNoQuote("omloop5", txtKdOnloop5.getText().isEmpty() ? "NULL" : "'" + txtKdOnloop5.getText() + "'")
                    .setNoQuote("dokter_pjanak", txtKdPjAnak.getText().isEmpty() ? "NULL" : "'" + txtKdPjAnak.getText() + "'")
                    .setNoQuote("dokter_umum", txtKdDrUmum.getText().isEmpty() ? "NULL" : "'" + txtKdDrUmum.getText() + "'")
                    .set("biayaoperator1", paket.get("operator1"))
                    .set("biayaoperator2", paket.get("operator2"))
                    .set("biayaoperator3", paket.get("operator3"))
                    .set("biayaasisten_operator1", paket.get("asisten_operator1"))
                    .set("biayaasisten_operator2", paket.get("asisten_operator2"))
                    .set("biayaasisten_operator3", paket.get("asisten_operator3"))
                    .set("biayainstrumen", paket.get("instrumen"))
                    .set("biayadokter_anak", paket.get("dokter_anak"))
                    .set("biayaperawaat_resusitas", paket.get("perawaat_resusitas"))
                    .set("biayadokter_anestesi", paket.get("dokter_anestesi"))
                    .set("biayaasisten_anestesi", paket.get("asisten_anestesi"))
                    .set("biayaasisten_anestesi2", paket.get("asisten_anestesi2"))
                    .set("biayabidan", paket.get("bidan"))
                    .set("biayabidan2", paket.get("bidan2"))
                    .set("biayabidan3", paket.get("bidan3"))
                    .set("biayaperawat_luar", paket.get("perawat_luar"))
                    .set("biayaalat", paket.get("sewa_ok"))
                    .set("biayasewaok", paket.get("alat"))
                    .set("akomodasi", paket.get("akomodasi"))
                    .set("bagian_rs", paket.get("bagian_rs"))
                    .set("biaya_omloop", paket.get("omloop"))
                    .set("biaya_omloop2", paket.get("omloop2"))
                    .set("biaya_omloop3", paket.get("omloop3"))
                    .set("biaya_omloop4", paket.get("omloop4"))
                    .set("biaya_omloop5", paket.get("omloop5"))
                    .set("biayasarpras", paket.get("sarpras"))
                    .set("biaya_dokter_pjanak", paket.get("dokter_pjanak"))
                    .set("biaya_dokter_umum", paket.get("dokter_umum"))
                    .set("status", status)
                    .write();
        }
        
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
        isEdit = false;
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
        
        txtKdOperator1.setText("");
        txtNamaOperator1.setText("");
        txtKdOperator2.setText("");
        txtNamaOperator2.setText("");
        txtKdOperator3.setText("");
        txtNamaOperator3.setText("");
        txtKdAsistenOperator1.setText("");
        txtNamaAsistenOperator1.setText("");
        txtKdAsistenOperator2.setText("");
        txtNamaAsistenOperator2.setText("");
        txtKdAsistenOperator3.setText("");
        txtNamaAsistenOperator3.setText("");
        txtKdAnestesia.setText("");
        txtNamaAnestesia.setText("");
        txtKdAsistenAnestesia1.setText("");
        txtNamaAsistenAnestesia1.setText("");
        txtKdAsistenAnestesia2.setText("");
        txtNamaAsistenAnestesia2.setText("");
        txtKdDrAnak.setText("");
        txtNamaDrAnak.setText("");
        txtKdBidan1.setText("");
        txtNamaBidan1.setText("");
        txtKdBidan2.setText("");
        txtNamaBidan2.setText("");
        txtKdBidan3.setText("");
        txtNamaBidan3.setText("");
        txtKdPerawatLuar.setText("");
        txtNamaPerawatLuar.setText("");
        txtKdPrResus.setText("");
        txtNamaPrResus.setText("");
        txtKdInstrumen.setText("");
        txtNamaInstrumen.setText("");
        txtKdPjAnak.setText("");
        txtNamaPjAnak.setText("");
        txtKdDrUmum.setText("");
        txtNamaDrUmum.setText("");
        txtKdOnloop1.setText("");
        txtNamaOnloop1.setText("");
        txtKdOnloop2.setText("");
        txtNamaOnloop2.setText("");
        txtKdOnloop3.setText("");
        txtNamaOnloop3.setText("");
        txtKdOnloop4.setText("");
        txtNamaOnloop4.setText("");
        txtKdOnloop5.setText("");
        txtNamaOnloop5.setText("");
    }
    
    private void hapus()
    {
        
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
                    rsOrder.getString("kd_order"),
                    rsOrder.getString("kd_group"),
                    rsOrder.getString("kd_kategori"),
                    rsOrder.getString("kd_detail"),
                    rsOrder.getString("no_rawat"),
                    pas,
                    rsOrder.getString("nm_group"),
                    rsOrder.getString("nm_kategori"),
                    rsOrder.getString("nm_detail"),
                    rsOrder.getString("tgl_operasi")
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
                    rsTransaksi.getString("tgl_operasi")
                };
                
                mdlTransaksi.addRow(o);
            }
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
        }
    }

    
    // Helper Method =======
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
        else if (txtNamaOperator1.getText().trim().isEmpty())
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
    private void initComponents()
    {

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
        txtKdOperator1 = new widget.TextBox();
        txtNamaOperator1 = new widget.TextBox();
        btnOperator1 = new widget.Button();
        jLabel4 = new widget.Label();
        txtJenisAnasthesia = new widget.TextBox();
        label17 = new widget.Label();
        txtKdAsistenOperator1 = new widget.TextBox();
        txtNamaAsistenOperator1 = new widget.TextBox();
        btnAsistenOperator1 = new widget.Button();
        label19 = new widget.Label();
        txtKdOperator2 = new widget.TextBox();
        txtNamaOperator2 = new widget.TextBox();
        btnOperator2 = new widget.Button();
        label20 = new widget.Label();
        txtKdOperator3 = new widget.TextBox();
        txtNamaOperator3 = new widget.TextBox();
        btnOperator3 = new widget.Button();
        label21 = new widget.Label();
        txtKdAnestesia = new widget.TextBox();
        txtNamaAnestesia = new widget.TextBox();
        btnAnastesi = new widget.Button();
        label22 = new widget.Label();
        txtKdDrAnak = new widget.TextBox();
        txtNamaDrAnak = new widget.TextBox();
        btnDrAnak = new widget.Button();
        btnAsistenOperator2 = new widget.Button();
        txtNamaAsistenOperator2 = new widget.TextBox();
        txtKdAsistenOperator2 = new widget.TextBox();
        label18 = new widget.Label();
        btnInstrumen = new widget.Button();
        txtNamaInstrumen = new widget.TextBox();
        txtKdInstrumen = new widget.TextBox();
        label23 = new widget.Label();
        btnPrResus = new widget.Button();
        txtNamaPrResus = new widget.TextBox();
        txtKdPrResus = new widget.TextBox();
        label24 = new widget.Label();
        label26 = new widget.Label();
        txtKdAsistenAnestesia1 = new widget.TextBox();
        txtNamaAsistenAnestesia1 = new widget.TextBox();
        btnAsistenAnestesia1 = new widget.Button();
        label27 = new widget.Label();
        txtKdBidan1 = new widget.TextBox();
        txtNamaBidan1 = new widget.TextBox();
        btnBidan1 = new widget.Button();
        label28 = new widget.Label();
        txtKdPerawatLuar = new widget.TextBox();
        txtNamaPerawatLuar = new widget.TextBox();
        btnPerawatLuar = new widget.Button();
        btnBidan2 = new widget.Button();
        txtNamaBidan2 = new widget.TextBox();
        txtKdBidan2 = new widget.TextBox();
        label29 = new widget.Label();
        label30 = new widget.Label();
        txtKdBidan3 = new widget.TextBox();
        txtNamaBidan3 = new widget.TextBox();
        btnBidan3 = new widget.Button();
        label25 = new widget.Label();
        txtKdOnloop1 = new widget.TextBox();
        txtNamaOnloop1 = new widget.TextBox();
        btnOnloop1 = new widget.Button();
        btnOnloop2 = new widget.Button();
        txtNamaOnloop2 = new widget.TextBox();
        txtKdOnloop2 = new widget.TextBox();
        label31 = new widget.Label();
        label32 = new widget.Label();
        btnOnloop3 = new widget.Button();
        txtNamaOnloop3 = new widget.TextBox();
        txtKdOnloop3 = new widget.TextBox();
        label33 = new widget.Label();
        txtKdPjAnak = new widget.TextBox();
        txtNamaPjAnak = new widget.TextBox();
        btnPjAnak = new widget.Button();
        label34 = new widget.Label();
        txtKdDrUmum = new widget.TextBox();
        txtNamaDrUmum = new widget.TextBox();
        btnDrUmum = new widget.Button();
        label35 = new widget.Label();
        txtKdAsistenOperator3 = new widget.TextBox();
        txtNamaAsistenOperator3 = new widget.TextBox();
        btnAsistenOperator3 = new widget.Button();
        label36 = new widget.Label();
        txtKdAsistenAnestesia2 = new widget.TextBox();
        txtNamaAsistenAnestesia2 = new widget.TextBox();
        btnAsistenAnestesia2 = new widget.Button();
        label37 = new widget.Label();
        txtKdOnloop4 = new widget.TextBox();
        txtNamaOnloop4 = new widget.TextBox();
        btnOnloop4 = new widget.Button();
        btnOnloop5 = new widget.Button();
        txtNamaOnloop5 = new widget.TextBox();
        txtKdOnloop5 = new widget.TextBox();
        label38 = new widget.Label();
        jLabel9 = new widget.Label();
        DTPBeri = new widget.Tanggal();
        cmbJam = new widget.ComboBox();
        cmbMnt = new widget.ComboBox();
        cmbDtk = new widget.ComboBox();
        ChkJln = new widget.CekBox();
        pnlAction = new widget.panelisi();
        btnSimpan = new widget.Button();
        btnBaru = new widget.Button();
        btnHapus = new widget.Button();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "::[ Operasi ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout());

        panelBiasa1.setLayout(new java.awt.BorderLayout());

        pnlInput.setPreferredSize(new java.awt.Dimension(952, 551));

        txtNamaKategori.setEditable(false);

        txtKdKategori.setEditable(false);

        label3.setText("Kategori :");

        btnCariKategori.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnCariKategori.setMnemonic('4');
        btnCariKategori.setToolTipText("ALt+4");
        btnCariKategori.setPreferredSize(new java.awt.Dimension(35, 22));
        btnCariKategori.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCariKategoriActionPerformed(evt);
            }
        });

        label1.setText("No Rawat :");

        btnCariGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnCariGroup.setMnemonic('4');
        btnCariGroup.setToolTipText("ALt+4");
        btnCariGroup.setPreferredSize(new java.awt.Dimension(35, 22));
        btnCariGroup.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCariGroupActionPerformed(evt);
            }
        });

        txtNoRawat.setEditable(false);

        btnCariDetail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnCariDetail.setMnemonic('4');
        btnCariDetail.setToolTipText("ALt+4");
        btnCariDetail.setPreferredSize(new java.awt.Dimension(35, 22));
        btnCariDetail.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCariDetailActionPerformed(evt);
            }
        });

        txtNoRm.setEditable(false);

        txtNamaDetail.setEditable(false);

        txtNamaPasien.setEditable(false);

        txtKdDetail.setEditable(false);

        txtNamaGroup.setEditable(false);

        label4.setText("Detail :");

        txtKdGroup.setEditable(false);

        label2.setText("Group :");

        label14.setText("Operator 1 :");
        label14.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdOperator1.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdOperator1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdOperator1KeyPressed(evt);
            }
        });

        txtNamaOperator1.setEditable(false);
        txtNamaOperator1.setPreferredSize(new java.awt.Dimension(207, 23));

        btnOperator1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnOperator1.setMnemonic('2');
        btnOperator1.setToolTipText("Alt+2");
        btnOperator1.setPreferredSize(new java.awt.Dimension(28, 23));
        btnOperator1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOperator1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Jenis Anasthesi :");

        txtJenisAnasthesia.setHighlighter(null);
        txtJenisAnasthesia.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtJenisAnasthesiaKeyPressed(evt);
            }
        });

        label17.setText("Ast. Operator 1 :");
        label17.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdAsistenOperator1.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdAsistenOperator1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdAsistenOperator1KeyPressed(evt);
            }
        });

        txtNamaAsistenOperator1.setEditable(false);
        txtNamaAsistenOperator1.setPreferredSize(new java.awt.Dimension(207, 23));

        btnAsistenOperator1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnAsistenOperator1.setMnemonic('2');
        btnAsistenOperator1.setToolTipText("Alt+2");
        btnAsistenOperator1.setPreferredSize(new java.awt.Dimension(28, 23));
        btnAsistenOperator1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAsistenOperator1ActionPerformed(evt);
            }
        });

        label19.setText("Operator 2 :");
        label19.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdOperator2.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdOperator2.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdOperator2KeyPressed(evt);
            }
        });

        txtNamaOperator2.setEditable(false);
        txtNamaOperator2.setPreferredSize(new java.awt.Dimension(207, 23));

        btnOperator2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnOperator2.setMnemonic('2');
        btnOperator2.setToolTipText("Alt+2");
        btnOperator2.setPreferredSize(new java.awt.Dimension(28, 23));
        btnOperator2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOperator2ActionPerformed(evt);
            }
        });

        label20.setText("Operator 3 :");
        label20.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdOperator3.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdOperator3.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdOperator3KeyPressed(evt);
            }
        });

        txtNamaOperator3.setEditable(false);
        txtNamaOperator3.setPreferredSize(new java.awt.Dimension(207, 23));

        btnOperator3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnOperator3.setMnemonic('2');
        btnOperator3.setToolTipText("Alt+2");
        btnOperator3.setPreferredSize(new java.awt.Dimension(28, 23));
        btnOperator3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOperator3ActionPerformed(evt);
            }
        });

        label21.setText("dr Anestesi :");
        label21.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdAnestesia.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdAnestesia.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdAnestesiaKeyPressed(evt);
            }
        });

        txtNamaAnestesia.setEditable(false);
        txtNamaAnestesia.setPreferredSize(new java.awt.Dimension(207, 23));

        btnAnastesi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnAnastesi.setMnemonic('2');
        btnAnastesi.setToolTipText("Alt+2");
        btnAnastesi.setPreferredSize(new java.awt.Dimension(28, 23));
        btnAnastesi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAnastesiActionPerformed(evt);
            }
        });

        label22.setText("dr Anak :");
        label22.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdDrAnak.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdDrAnak.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdDrAnakKeyPressed(evt);
            }
        });

        txtNamaDrAnak.setEditable(false);
        txtNamaDrAnak.setPreferredSize(new java.awt.Dimension(207, 23));

        btnDrAnak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDrAnak.setMnemonic('2');
        btnDrAnak.setToolTipText("Alt+2");
        btnDrAnak.setPreferredSize(new java.awt.Dimension(28, 23));
        btnDrAnak.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDrAnakActionPerformed(evt);
            }
        });

        btnAsistenOperator2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnAsistenOperator2.setMnemonic('2');
        btnAsistenOperator2.setToolTipText("Alt+2");
        btnAsistenOperator2.setPreferredSize(new java.awt.Dimension(28, 23));
        btnAsistenOperator2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAsistenOperator2ActionPerformed(evt);
            }
        });

        txtNamaAsistenOperator2.setEditable(false);
        txtNamaAsistenOperator2.setPreferredSize(new java.awt.Dimension(207, 23));

        txtKdAsistenOperator2.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdAsistenOperator2.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdAsistenOperator2KeyPressed(evt);
            }
        });

        label18.setText("Ast. Operator 2 :");
        label18.setPreferredSize(new java.awt.Dimension(70, 23));

        btnInstrumen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnInstrumen.setMnemonic('2');
        btnInstrumen.setToolTipText("Alt+2");
        btnInstrumen.setPreferredSize(new java.awt.Dimension(28, 23));
        btnInstrumen.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnInstrumenActionPerformed(evt);
            }
        });

        txtNamaInstrumen.setEditable(false);
        txtNamaInstrumen.setPreferredSize(new java.awt.Dimension(207, 23));

        txtKdInstrumen.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdInstrumen.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdInstrumenKeyPressed(evt);
            }
        });

        label23.setText("Instrumen :");
        label23.setPreferredSize(new java.awt.Dimension(70, 23));

        btnPrResus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPrResus.setMnemonic('2');
        btnPrResus.setToolTipText("Alt+2");
        btnPrResus.setPreferredSize(new java.awt.Dimension(28, 23));
        btnPrResus.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrResusActionPerformed(evt);
            }
        });

        txtNamaPrResus.setEditable(false);
        txtNamaPrResus.setPreferredSize(new java.awt.Dimension(207, 23));

        txtKdPrResus.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdPrResus.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdPrResusKeyPressed(evt);
            }
        });

        label24.setText("Prw.Resusitasi :");
        label24.setPreferredSize(new java.awt.Dimension(70, 23));

        label26.setText("Ast. Anestesi 1 :");
        label26.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdAsistenAnestesia1.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdAsistenAnestesia1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdAsistenAnestesia1KeyPressed(evt);
            }
        });

        txtNamaAsistenAnestesia1.setEditable(false);
        txtNamaAsistenAnestesia1.setPreferredSize(new java.awt.Dimension(207, 23));

        btnAsistenAnestesia1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnAsistenAnestesia1.setMnemonic('2');
        btnAsistenAnestesia1.setToolTipText("Alt+2");
        btnAsistenAnestesia1.setPreferredSize(new java.awt.Dimension(28, 23));
        btnAsistenAnestesia1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAsistenAnestesia1ActionPerformed(evt);
            }
        });

        label27.setText("Bidan 1 :");
        label27.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdBidan1.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdBidan1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdBidan1KeyPressed(evt);
            }
        });

        txtNamaBidan1.setEditable(false);
        txtNamaBidan1.setPreferredSize(new java.awt.Dimension(207, 23));

        btnBidan1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnBidan1.setMnemonic('2');
        btnBidan1.setToolTipText("Alt+2");
        btnBidan1.setPreferredSize(new java.awt.Dimension(28, 23));
        btnBidan1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnBidan1ActionPerformed(evt);
            }
        });

        label28.setText("Prwat Luar :");
        label28.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdPerawatLuar.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdPerawatLuar.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdPerawatLuarKeyPressed(evt);
            }
        });

        txtNamaPerawatLuar.setEditable(false);
        txtNamaPerawatLuar.setPreferredSize(new java.awt.Dimension(207, 23));

        btnPerawatLuar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPerawatLuar.setMnemonic('2');
        btnPerawatLuar.setToolTipText("Alt+2");
        btnPerawatLuar.setPreferredSize(new java.awt.Dimension(28, 23));
        btnPerawatLuar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPerawatLuarActionPerformed(evt);
            }
        });

        btnBidan2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnBidan2.setMnemonic('2');
        btnBidan2.setToolTipText("Alt+2");
        btnBidan2.setPreferredSize(new java.awt.Dimension(28, 23));
        btnBidan2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnBidan2ActionPerformed(evt);
            }
        });

        txtNamaBidan2.setEditable(false);
        txtNamaBidan2.setPreferredSize(new java.awt.Dimension(207, 23));

        txtKdBidan2.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdBidan2.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdBidan2KeyPressed(evt);
            }
        });

        label29.setText("Bidan 2 :");
        label29.setPreferredSize(new java.awt.Dimension(70, 23));

        label30.setText("Bidan 3 :");
        label30.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdBidan3.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdBidan3.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdBidan3KeyPressed(evt);
            }
        });

        txtNamaBidan3.setEditable(false);
        txtNamaBidan3.setPreferredSize(new java.awt.Dimension(207, 23));

        btnBidan3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnBidan3.setMnemonic('2');
        btnBidan3.setToolTipText("Alt+2");
        btnBidan3.setPreferredSize(new java.awt.Dimension(28, 23));
        btnBidan3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnBidan3ActionPerformed(evt);
            }
        });

        label25.setText("Onloop 1 :");
        label25.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdOnloop1.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdOnloop1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdOnloop1KeyPressed(evt);
            }
        });

        txtNamaOnloop1.setEditable(false);
        txtNamaOnloop1.setPreferredSize(new java.awt.Dimension(207, 23));

        btnOnloop1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnOnloop1.setMnemonic('2');
        btnOnloop1.setToolTipText("Alt+2");
        btnOnloop1.setPreferredSize(new java.awt.Dimension(28, 23));
        btnOnloop1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOnloop1ActionPerformed(evt);
            }
        });

        btnOnloop2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnOnloop2.setMnemonic('2');
        btnOnloop2.setToolTipText("Alt+2");
        btnOnloop2.setPreferredSize(new java.awt.Dimension(28, 23));
        btnOnloop2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOnloop2ActionPerformed(evt);
            }
        });

        txtNamaOnloop2.setEditable(false);
        txtNamaOnloop2.setPreferredSize(new java.awt.Dimension(207, 23));

        txtKdOnloop2.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdOnloop2.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdOnloop2KeyPressed(evt);
            }
        });

        label31.setText("Onloop 2 :");
        label31.setPreferredSize(new java.awt.Dimension(70, 23));

        label32.setText("Onloop 3 :");
        label32.setPreferredSize(new java.awt.Dimension(70, 23));

        btnOnloop3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnOnloop3.setMnemonic('2');
        btnOnloop3.setToolTipText("Alt+2");
        btnOnloop3.setPreferredSize(new java.awt.Dimension(28, 23));
        btnOnloop3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOnloop3ActionPerformed(evt);
            }
        });

        txtNamaOnloop3.setEditable(false);
        txtNamaOnloop3.setPreferredSize(new java.awt.Dimension(207, 23));

        txtKdOnloop3.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdOnloop3.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdOnloop3KeyPressed(evt);
            }
        });

        label33.setText("dr Pj. Anak :");
        label33.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdPjAnak.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdPjAnak.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdPjAnakKeyPressed(evt);
            }
        });

        txtNamaPjAnak.setEditable(false);
        txtNamaPjAnak.setPreferredSize(new java.awt.Dimension(207, 23));

        btnPjAnak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPjAnak.setMnemonic('2');
        btnPjAnak.setToolTipText("Alt+2");
        btnPjAnak.setPreferredSize(new java.awt.Dimension(28, 23));
        btnPjAnak.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPjAnakActionPerformed(evt);
            }
        });

        label34.setText("dr Umum :");
        label34.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdDrUmum.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdDrUmum.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdDrUmumKeyPressed(evt);
            }
        });

        txtNamaDrUmum.setEditable(false);
        txtNamaDrUmum.setPreferredSize(new java.awt.Dimension(207, 23));

        btnDrUmum.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDrUmum.setMnemonic('2');
        btnDrUmum.setToolTipText("Alt+2");
        btnDrUmum.setPreferredSize(new java.awt.Dimension(28, 23));
        btnDrUmum.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDrUmumActionPerformed(evt);
            }
        });

        label35.setText("Ast. Operator 3 :");
        label35.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdAsistenOperator3.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdAsistenOperator3.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdAsistenOperator3KeyPressed(evt);
            }
        });

        txtNamaAsistenOperator3.setEditable(false);
        txtNamaAsistenOperator3.setPreferredSize(new java.awt.Dimension(207, 23));

        btnAsistenOperator3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnAsistenOperator3.setMnemonic('2');
        btnAsistenOperator3.setToolTipText("Alt+2");
        btnAsistenOperator3.setPreferredSize(new java.awt.Dimension(28, 23));
        btnAsistenOperator3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAsistenOperator3ActionPerformed(evt);
            }
        });

        label36.setText("Ast. Anestesi 2 :");
        label36.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdAsistenAnestesia2.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdAsistenAnestesia2.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdAsistenAnestesia2KeyPressed(evt);
            }
        });

        txtNamaAsistenAnestesia2.setEditable(false);
        txtNamaAsistenAnestesia2.setPreferredSize(new java.awt.Dimension(207, 23));

        btnAsistenAnestesia2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnAsistenAnestesia2.setMnemonic('2');
        btnAsistenAnestesia2.setToolTipText("Alt+2");
        btnAsistenAnestesia2.setPreferredSize(new java.awt.Dimension(28, 23));
        btnAsistenAnestesia2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAsistenAnestesia2ActionPerformed(evt);
            }
        });

        label37.setText("Onloop 4 :");
        label37.setPreferredSize(new java.awt.Dimension(70, 23));

        txtKdOnloop4.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdOnloop4.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdOnloop4KeyPressed(evt);
            }
        });

        txtNamaOnloop4.setEditable(false);
        txtNamaOnloop4.setPreferredSize(new java.awt.Dimension(207, 23));

        btnOnloop4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnOnloop4.setMnemonic('2');
        btnOnloop4.setToolTipText("Alt+2");
        btnOnloop4.setPreferredSize(new java.awt.Dimension(28, 23));
        btnOnloop4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOnloop4ActionPerformed(evt);
            }
        });

        btnOnloop5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnOnloop5.setMnemonic('2');
        btnOnloop5.setToolTipText("Alt+2");
        btnOnloop5.setPreferredSize(new java.awt.Dimension(28, 23));
        btnOnloop5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOnloop5ActionPerformed(evt);
            }
        });

        txtNamaOnloop5.setEditable(false);
        txtNamaOnloop5.setPreferredSize(new java.awt.Dimension(207, 23));

        txtKdOnloop5.setPreferredSize(new java.awt.Dimension(80, 23));
        txtKdOnloop5.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdOnloop5KeyPressed(evt);
            }
        });

        label38.setText("Onloop 5 :");
        label38.setPreferredSize(new java.awt.Dimension(70, 23));

        jLabel9.setText("Tanggal :");

        DTPBeri.setEditable(false);
        DTPBeri.setForeground(new java.awt.Color(50, 70, 50));
        DTPBeri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "31-10-2017" }));
        DTPBeri.setDisplayFormat("dd-MM-yyyy");
        DTPBeri.setOpaque(false);
        DTPBeri.setPreferredSize(new java.awt.Dimension(100, 23));
        DTPBeri.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                DTPBeriKeyPressed(evt);
            }
        });

        cmbJam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        cmbJam.setOpaque(false);
        cmbJam.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbJamKeyPressed(evt);
            }
        });

        cmbMnt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbMnt.setOpaque(false);
        cmbMnt.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbMntKeyPressed(evt);
            }
        });

        cmbDtk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbDtk.setOpaque(false);
        cmbDtk.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
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
                                    .addComponent(txtNamaGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                        .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(pnlInputLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label14, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label19, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label20, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label21, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label22, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label27, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label29, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label30, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label28, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label23, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label33, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label34, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlInputLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdOperator1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaOperator1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnOperator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdOperator2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaOperator2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnOperator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdOperator3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaOperator3, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnOperator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdAnestesia, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaAnestesia, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnAnastesi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdDrAnak, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaDrAnak, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnDrAnak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdBidan1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaBidan1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnBidan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdBidan2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaBidan2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnBidan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdBidan3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaBidan3, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnBidan3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdPerawatLuar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaPerawatLuar, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnPerawatLuar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdInstrumen, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaInstrumen, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnInstrumen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdPjAnak, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaPjAnak, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnPjAnak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdDrUmum, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaDrUmum, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnDrUmum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(pnlInputLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtJenisAnasthesia, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlInputLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label17, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label18, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label35, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label26, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label36, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label24, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label25, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label31, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label32, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label37, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label38, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdAsistenOperator1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaAsistenOperator1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnAsistenOperator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdAsistenOperator2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaAsistenOperator2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnAsistenOperator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdAsistenOperator3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaAsistenOperator3, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnAsistenOperator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdAsistenAnestesia1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaAsistenAnestesia1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnAsistenAnestesia1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdAsistenAnestesia2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaAsistenAnestesia2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnAsistenAnestesia2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdPrResus, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaPrResus, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnPrResus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdOnloop1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaOnloop1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnOnloop1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdOnloop2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaOnloop2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnOnloop2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdOnloop3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaOnloop3, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnOnloop3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdOnloop4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaOnloop4, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnOnloop4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlInputLayout.createSequentialGroup()
                                        .addComponent(txtKdOnloop5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNamaOnloop5, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(btnOnloop5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(pnlInputLayout.createSequentialGroup()
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
                                .addComponent(ChkJln, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(203, Short.MAX_VALUE))
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
                    .addComponent(txtKdOperator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaOperator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOperator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdAsistenOperator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaAsistenOperator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAsistenOperator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdOperator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaOperator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOperator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdAsistenOperator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaAsistenOperator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAsistenOperator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdOperator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaOperator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOperator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdAsistenOperator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaAsistenOperator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAsistenOperator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdAnestesia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaAnestesia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAnastesi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdAsistenAnestesia1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaAsistenAnestesia1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAsistenAnestesia1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdDrAnak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaDrAnak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDrAnak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdAsistenAnestesia2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaAsistenAnestesia2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAsistenAnestesia2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdBidan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaBidan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBidan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdPrResus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaPrResus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrResus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdBidan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaBidan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBidan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdOnloop1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaOnloop1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOnloop1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdBidan3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaBidan3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBidan3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdOnloop2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaOnloop2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOnloop2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdPerawatLuar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaPerawatLuar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPerawatLuar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdOnloop3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaOnloop3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOnloop3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdInstrumen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaInstrumen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInstrumen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdOnloop4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaOnloop4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOnloop4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdPjAnak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaPjAnak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPjAnak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdOnloop5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaOnloop5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOnloop5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKdDrUmum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaDrUmum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDrUmum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        panelBiasa1.add(pnlInput, java.awt.BorderLayout.PAGE_START);

        pnlAction.setPreferredSize(new java.awt.Dimension(779, 50));
        pnlAction.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        btnSimpan.setMnemonic('S');
        btnSimpan.setText("Simpan");
        btnSimpan.setToolTipText("Alt+S");
        btnSimpan.setPreferredSize(new java.awt.Dimension(100, 30));
        btnSimpan.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSimpanActionPerformed(evt);
            }
        });
        pnlAction.add(btnSimpan);

        btnBaru.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        btnBaru.setMnemonic('B');
        btnBaru.setText("Baru");
        btnBaru.setToolTipText("Alt+B");
        btnBaru.setPreferredSize(new java.awt.Dimension(100, 30));
        btnBaru.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnBaruActionPerformed(evt);
            }
        });
        pnlAction.add(btnBaru);

        btnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        btnHapus.setMnemonic('H');
        btnHapus.setText("Hapus");
        btnHapus.setToolTipText("Alt+H");
        btnHapus.setPreferredSize(new java.awt.Dimension(100, 30));
        btnHapus.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
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
        btnKeluar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
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
        tglOrder1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "31-10-2017" }));
        tglOrder1.setDisplayFormat("dd-MM-yyyy");
        tglOrder1.setOpaque(false);
        tglOrder1.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass13.add(tglOrder1);

        jLabel38.setText("s.d");
        jLabel38.setPreferredSize(new java.awt.Dimension(18, 23));
        panelGlass13.add(jLabel38);

        tglOrder2.setEditable(false);
        tglOrder2.setForeground(new java.awt.Color(50, 70, 50));
        tglOrder2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "31-10-2017" }));
        tglOrder2.setDisplayFormat("dd-MM-yyyy");
        tglOrder2.setOpaque(false);
        tglOrder2.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass13.add(tglOrder2);

        btnCariOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        btnCariOrder.setMnemonic('6');
        btnCariOrder.setToolTipText("Alt+6");
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
        panelGlass13.add(btnCariOrder);

        jLabel28.setText("Record :");
        jLabel28.setPreferredSize(new java.awt.Dimension(65, 23));
        panelGlass13.add(jLabel28);

        lblCountOrder.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCountOrder.setText("0");
        lblCountOrder.setPreferredSize(new java.awt.Dimension(45, 23));
        panelGlass13.add(lblCountOrder);

        jPanel6.add(panelGlass13, java.awt.BorderLayout.PAGE_START);

        panelBiasa2.add(jPanel6, java.awt.BorderLayout.PAGE_END);

        tabPane.addTab("List Order", panelBiasa2);

        panelBiasa3.setLayout(new java.awt.BorderLayout());

        Scroll2.setOpaque(true);

        tblTransaksi.setAutoCreateRowSorter(true);
        tblTransaksi.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
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
        tglTransaksi1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "31-10-2017" }));
        tglTransaksi1.setDisplayFormat("dd-MM-yyyy");
        tglTransaksi1.setOpaque(false);
        tglTransaksi1.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass12.add(tglTransaksi1);

        jLabel36.setText("s.d");
        jLabel36.setPreferredSize(new java.awt.Dimension(18, 23));
        panelGlass12.add(jLabel36);

        tglTransaksi2.setEditable(false);
        tglTransaksi2.setForeground(new java.awt.Color(50, 70, 50));
        tglTransaksi2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "31-10-2017" }));
        tglTransaksi2.setDisplayFormat("dd-MM-yyyy");
        tglTransaksi2.setOpaque(false);
        tglTransaksi2.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass12.add(tglTransaksi2);

        btnCariTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        btnCariTransaksi.setMnemonic('6');
        btnCariTransaksi.setToolTipText("Alt+6");
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
        panelGlass12.add(btnCariTransaksi);

        jLabel12.setText("Record :");
        jLabel12.setPreferredSize(new java.awt.Dimension(65, 23));
        panelGlass12.add(jLabel12);

        lblCountTransaksi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCountTransaksi.setText("0");
        lblCountTransaksi.setPreferredSize(new java.awt.Dimension(45, 23));
        panelGlass12.add(lblCountTransaksi);

        jPanel5.add(panelGlass12, java.awt.BorderLayout.PAGE_START);

        panelBiasa3.add(jPanel5, java.awt.BorderLayout.PAGE_END);

        tabPane.addTab("List Transaksi", panelBiasa3);

        internalFrame1.add(tabPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCariKategoriActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCariKategoriActionPerformed
    {//GEN-HEADEREND:event_btnCariKategoriActionPerformed
        cariKategori();
    }//GEN-LAST:event_btnCariKategoriActionPerformed

    private void btnCariGroupActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCariGroupActionPerformed
    {//GEN-HEADEREND:event_btnCariGroupActionPerformed
        cariGroup();
    }//GEN-LAST:event_btnCariGroupActionPerformed

    private void btnCariDetailActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCariDetailActionPerformed
    {//GEN-HEADEREND:event_btnCariDetailActionPerformed
        cariDetail();
    }//GEN-LAST:event_btnCariDetailActionPerformed

    private void txtKdOperator1KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdOperator1KeyPressed
    {//GEN-HEADEREND:event_txtKdOperator1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", txtNamaOperator1, txtKdOperator1.getText());
            
            if (txtNamaOperator1.getText().isEmpty())
                btnOperator1ActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnOperator1ActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdOperator1, txtKdOperator2);
        }
    }//GEN-LAST:event_txtKdOperator1KeyPressed

    private void btnOperator1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOperator1ActionPerformed
    {//GEN-HEADEREND:event_btnOperator1ActionPerformed
        cariDokter(OPERATOR_1);
    }//GEN-LAST:event_btnOperator1ActionPerformed

    private void txtJenisAnasthesiaKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtJenisAnasthesiaKeyPressed
    {//GEN-HEADEREND:event_txtJenisAnasthesiaKeyPressed
        Valid.pindah(evt, txtKdOperator1, txtKdOperator1);
    }//GEN-LAST:event_txtJenisAnasthesiaKeyPressed

    private void txtKdAsistenOperator1KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdAsistenOperator1KeyPressed
    {//GEN-HEADEREND:event_txtKdAsistenOperator1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", txtNamaAsistenOperator1, txtKdAsistenOperator1.getText());
            
            if (txtNamaAsistenOperator1.getText().isEmpty())
                btnAsistenOperator1ActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnAsistenOperator1ActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdInstrumen, txtKdAsistenOperator2);
        }

    }//GEN-LAST:event_txtKdAsistenOperator1KeyPressed

    private void btnAsistenOperator1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAsistenOperator1ActionPerformed
    {//GEN-HEADEREND:event_btnAsistenOperator1ActionPerformed
        cariPetugas(ASISTEN_OPERATOR_1);
    }//GEN-LAST:event_btnAsistenOperator1ActionPerformed

    private void txtKdOperator2KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdOperator2KeyPressed
    {//GEN-HEADEREND:event_txtKdOperator2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", txtNamaOperator2, txtKdOperator2.getText());
            
            if (txtNamaOperator2.getText().isEmpty())
                btnOperator2ActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnOperator2ActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdOperator1, txtKdOperator3);
        }
    }//GEN-LAST:event_txtKdOperator2KeyPressed

    private void btnOperator2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOperator2ActionPerformed
    {//GEN-HEADEREND:event_btnOperator2ActionPerformed
        cariDokter(OPERATOR_2);
    }//GEN-LAST:event_btnOperator2ActionPerformed

    private void txtKdOperator3KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdOperator3KeyPressed
    {//GEN-HEADEREND:event_txtKdOperator3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", txtNamaOperator3, txtKdOperator3.getText());
            
            if (txtNamaOperator3.getText().isEmpty())
                btnOperator3ActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnOperator3ActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdOperator2, txtKdAnestesia);
        }
    }//GEN-LAST:event_txtKdOperator3KeyPressed

    private void btnOperator3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOperator3ActionPerformed
    {//GEN-HEADEREND:event_btnOperator3ActionPerformed
        cariDokter(OPERATOR_3);
    }//GEN-LAST:event_btnOperator3ActionPerformed

    private void txtKdAnestesiaKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdAnestesiaKeyPressed
    {//GEN-HEADEREND:event_txtKdAnestesiaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", txtNamaAnestesia, txtKdAnestesia.getText());
            
            if (txtNamaAnestesia.getText().isEmpty())
                btnAnastesiActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnAnastesiActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdOperator3, txtKdDrAnak);
        }
    }//GEN-LAST:event_txtKdAnestesiaKeyPressed

    private void btnAnastesiActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAnastesiActionPerformed
    {//GEN-HEADEREND:event_btnAnastesiActionPerformed
        cariDokter(ANESTESIA);
    }//GEN-LAST:event_btnAnastesiActionPerformed

    private void txtKdDrAnakKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdDrAnakKeyPressed
    {//GEN-HEADEREND:event_txtKdDrAnakKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", txtNamaDrAnak, txtKdDrAnak.getText());
            
            if (txtNamaDrAnak.getText().isEmpty())
                btnDrAnakActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnDrAnakActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdAnestesia, txtKdBidan1);
        }
    }//GEN-LAST:event_txtKdDrAnakKeyPressed

    private void btnDrAnakActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDrAnakActionPerformed
    {//GEN-HEADEREND:event_btnDrAnakActionPerformed
        cariDokter(DR_ANAK);
    }//GEN-LAST:event_btnDrAnakActionPerformed

    private void btnAsistenOperator2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAsistenOperator2ActionPerformed
    {//GEN-HEADEREND:event_btnAsistenOperator2ActionPerformed
        cariPetugas(ASISTEN_OPERATOR_2);
    }//GEN-LAST:event_btnAsistenOperator2ActionPerformed

    private void txtKdAsistenOperator2KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdAsistenOperator2KeyPressed
    {//GEN-HEADEREND:event_txtKdAsistenOperator2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", txtNamaAsistenOperator2, txtKdAsistenOperator2.getText());
            
            if (txtNamaAsistenOperator2.getText().isEmpty())
                btnAsistenOperator2ActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnAsistenOperator2ActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdAsistenOperator1, txtKdAsistenAnestesia1);
        }
    }//GEN-LAST:event_txtKdAsistenOperator2KeyPressed

    private void btnInstrumenActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnInstrumenActionPerformed
    {//GEN-HEADEREND:event_btnInstrumenActionPerformed
        cariPetugas(INSTRUMEN);
    }//GEN-LAST:event_btnInstrumenActionPerformed

    private void txtKdInstrumenKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdInstrumenKeyPressed
    {//GEN-HEADEREND:event_txtKdInstrumenKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nama from petugas where nip='" + txtKdInstrumen.getText() + "'", txtNamaInstrumen);
            
            if (txtNamaInstrumen.getText().isEmpty())
                btnInstrumenActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnInstrumenActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdPerawatLuar, txtKdAsistenOperator1);
        }
    }//GEN-LAST:event_txtKdInstrumenKeyPressed

    private void btnPrResusActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPrResusActionPerformed
    {//GEN-HEADEREND:event_btnPrResusActionPerformed
        cariPetugas(PR_RESUS);
    }//GEN-LAST:event_btnPrResusActionPerformed

    private void txtKdPrResusKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdPrResusKeyPressed
    {//GEN-HEADEREND:event_txtKdPrResusKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", txtNamaPrResus, txtKdPrResus.getText());
            
            if (txtNamaPrResus.getText().isEmpty())
                btnPrResusActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnPrResusActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdAsistenAnestesia1, txtKdOnloop1);
        }
    }//GEN-LAST:event_txtKdPrResusKeyPressed

    private void txtKdAsistenAnestesia1KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdAsistenAnestesia1KeyPressed
    {//GEN-HEADEREND:event_txtKdAsistenAnestesia1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", txtNamaAsistenAnestesia1, txtKdAsistenAnestesia1.getText());
            
            if (txtNamaAsistenAnestesia1.getText().isEmpty())
                btnAsistenAnestesia1ActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnAsistenAnestesia1ActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdAsistenOperator2, txtKdPrResus);
        }
    }//GEN-LAST:event_txtKdAsistenAnestesia1KeyPressed

    private void btnAsistenAnestesia1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAsistenAnestesia1ActionPerformed
    {//GEN-HEADEREND:event_btnAsistenAnestesia1ActionPerformed
        cariPetugas(ASISTEN_ANESTESIA_1);
    }//GEN-LAST:event_btnAsistenAnestesia1ActionPerformed

    private void txtKdBidan1KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdBidan1KeyPressed
    {//GEN-HEADEREND:event_txtKdBidan1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", txtNamaBidan1, txtKdBidan1.getText());
            
            if (txtNamaBidan1.getText().isEmpty())
                btnBidan1ActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnBidan1ActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdDrAnak, txtKdBidan2);
        }
    }//GEN-LAST:event_txtKdBidan1KeyPressed

    private void btnBidan1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnBidan1ActionPerformed
    {//GEN-HEADEREND:event_btnBidan1ActionPerformed
        cariPetugas(BIDAN_1);
    }//GEN-LAST:event_btnBidan1ActionPerformed

    private void txtKdPerawatLuarKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdPerawatLuarKeyPressed
    {//GEN-HEADEREND:event_txtKdPerawatLuarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", txtNamaPerawatLuar, txtKdPerawatLuar.getText());
            
            if (txtNamaPerawatLuar.getText().isEmpty())
                btnPerawatLuarActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnPerawatLuarActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdBidan3, txtKdInstrumen);
        }
    }//GEN-LAST:event_txtKdPerawatLuarKeyPressed

    private void btnPerawatLuarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPerawatLuarActionPerformed
    {//GEN-HEADEREND:event_btnPerawatLuarActionPerformed
        cariPetugas(PR_LUAR);
    }//GEN-LAST:event_btnPerawatLuarActionPerformed

    private void btnBidan2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnBidan2ActionPerformed
    {//GEN-HEADEREND:event_btnBidan2ActionPerformed
        cariPetugas(BIDAN_2);
    }//GEN-LAST:event_btnBidan2ActionPerformed

    private void txtKdBidan2KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdBidan2KeyPressed
    {//GEN-HEADEREND:event_txtKdBidan2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", txtNamaBidan2, txtKdBidan2.getText());
            
            if (txtNamaBidan2.getText().isEmpty())
                btnBidan2ActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnBidan2ActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdBidan1, txtKdBidan3);
        }
    }//GEN-LAST:event_txtKdBidan2KeyPressed

    private void txtKdBidan3KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdBidan3KeyPressed
    {//GEN-HEADEREND:event_txtKdBidan3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", txtNamaBidan3, txtKdBidan3.getText());
            
            if (txtNamaBidan3.getText().isEmpty())
                btnBidan3ActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnBidan3ActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdBidan2, txtKdPerawatLuar);
        }
    }//GEN-LAST:event_txtKdBidan3KeyPressed

    private void btnBidan3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnBidan3ActionPerformed
    {//GEN-HEADEREND:event_btnBidan3ActionPerformed
        cariPetugas(BIDAN_3);
    }//GEN-LAST:event_btnBidan3ActionPerformed

    private void txtKdOnloop1KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdOnloop1KeyPressed
    {//GEN-HEADEREND:event_txtKdOnloop1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", txtNamaOnloop1, txtKdOnloop1.getText());
            
            if (txtNamaOnloop1.getText().isEmpty())
                btnOnloop1ActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnOnloop1ActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdPrResus, txtKdOnloop2);
        }
    }//GEN-LAST:event_txtKdOnloop1KeyPressed

    private void btnOnloop1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOnloop1ActionPerformed
    {//GEN-HEADEREND:event_btnOnloop1ActionPerformed
        cariPetugas(ONLOOP_1);
    }//GEN-LAST:event_btnOnloop1ActionPerformed

    private void btnOnloop2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOnloop2ActionPerformed
    {//GEN-HEADEREND:event_btnOnloop2ActionPerformed
        cariPetugas(ONLOOP_2);
    }//GEN-LAST:event_btnOnloop2ActionPerformed

    private void txtKdOnloop2KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdOnloop2KeyPressed
    {//GEN-HEADEREND:event_txtKdOnloop2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", txtNamaOnloop2, txtKdOnloop2.getText());
            
            if (txtNamaOnloop2.getText().isEmpty())
                btnOnloop2ActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnOnloop1ActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdOnloop1, txtKdOnloop3);
        }
    }//GEN-LAST:event_txtKdOnloop2KeyPressed

    private void btnOnloop3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOnloop3ActionPerformed
    {//GEN-HEADEREND:event_btnOnloop3ActionPerformed
        cariPetugas(ONLOOP_3);
    }//GEN-LAST:event_btnOnloop3ActionPerformed

    private void txtKdOnloop3KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdOnloop3KeyPressed
    {//GEN-HEADEREND:event_txtKdOnloop3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", txtNamaOnloop3, txtKdOnloop3.getText());
            
            if (txtNamaOnloop3.getText().isEmpty())
                btnOnloop3ActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnOnloop3ActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdOnloop2, txtKdOnloop3);
        }
    }//GEN-LAST:event_txtKdOnloop3KeyPressed

    private void txtKdPjAnakKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdPjAnakKeyPressed
    {//GEN-HEADEREND:event_txtKdPjAnakKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", txtNamaPjAnak, txtKdPjAnak.getText());
            
            if (txtNamaPjAnak.getText().isEmpty())
                btnPjAnakActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnPjAnakActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdOnloop3, txtKdDrUmum);
        }
    }//GEN-LAST:event_txtKdPjAnakKeyPressed

    private void btnPjAnakActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPjAnakActionPerformed
    {//GEN-HEADEREND:event_btnPjAnakActionPerformed
        cariDokter(DR_PJ_ANAK);
    }//GEN-LAST:event_btnPjAnakActionPerformed

    private void txtKdDrUmumKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdDrUmumKeyPressed
    {//GEN-HEADEREND:event_txtKdDrUmumKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", txtNamaDrAnak, txtKdDrAnak.getText());
            
            if (txtNamaDrAnak.getText().isEmpty())
                btnDrAnakActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnDrUmumActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdPjAnak, btnSimpan);
        }
    }//GEN-LAST:event_txtKdDrUmumKeyPressed

    private void btnDrUmumActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDrUmumActionPerformed
    {//GEN-HEADEREND:event_btnDrUmumActionPerformed
        cariDokter(DR_UMUM);
    }//GEN-LAST:event_btnDrUmumActionPerformed

    private void txtKdAsistenOperator3KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdAsistenOperator3KeyPressed
    {//GEN-HEADEREND:event_txtKdAsistenOperator3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", txtNamaAsistenOperator3, txtKdAsistenOperator3.getText());
            
            if (txtNamaAsistenOperator3.getText().isEmpty())
                btnAsistenOperator3ActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnAsistenOperator3ActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdAsistenOperator2, txtKdAsistenAnestesia1);
        }
    }//GEN-LAST:event_txtKdAsistenOperator3KeyPressed

    private void btnAsistenOperator3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAsistenOperator3ActionPerformed
    {//GEN-HEADEREND:event_btnAsistenOperator3ActionPerformed
        cariPetugas(ASISTEN_OPERATOR_3);
    }//GEN-LAST:event_btnAsistenOperator3ActionPerformed

    private void txtKdAsistenAnestesia2KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdAsistenAnestesia2KeyPressed
    {//GEN-HEADEREND:event_txtKdAsistenAnestesia2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", txtNamaAsistenAnestesia2, txtKdAsistenAnestesia2.getText());
            
            if (txtNamaAsistenAnestesia2.getText().isEmpty())
                btnAsistenAnestesia2ActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnAsistenAnestesia2ActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdAsistenOperator2, txtKdPrResus);
        }
    }//GEN-LAST:event_txtKdAsistenAnestesia2KeyPressed

    private void btnAsistenAnestesia2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAsistenAnestesia2ActionPerformed
    {//GEN-HEADEREND:event_btnAsistenAnestesia2ActionPerformed
        cariPetugas(ASISTEN_ANESTESIA_2);
    }//GEN-LAST:event_btnAsistenAnestesia2ActionPerformed

    private void txtKdOnloop4KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdOnloop4KeyPressed
    {//GEN-HEADEREND:event_txtKdOnloop4KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", txtNamaOnloop4, txtKdOnloop4.getText());
            
            if (txtNamaOnloop4.getText().isEmpty())
                btnOnloop4ActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnOnloop4ActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdOnloop3, txtKdOnloop5);
        }
    }//GEN-LAST:event_txtKdOnloop4KeyPressed

    private void btnOnloop4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOnloop4ActionPerformed
    {//GEN-HEADEREND:event_btnOnloop4ActionPerformed
        cariPetugas(ONLOOP_4);
    }//GEN-LAST:event_btnOnloop4ActionPerformed

    private void btnOnloop5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOnloop5ActionPerformed
    {//GEN-HEADEREND:event_btnOnloop5ActionPerformed
        cariPetugas(ONLOOP_5);
    }//GEN-LAST:event_btnOnloop5ActionPerformed

    private void txtKdOnloop5KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdOnloop5KeyPressed
    {//GEN-HEADEREND:event_txtKdOnloop5KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", txtNamaOnloop5, txtKdOnloop5.getText());
            
            if (txtNamaOnloop5.getText().isEmpty())
                btnOnloop5ActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnOnloop5ActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtKdOnloop4, btnSimpan);
        }
    }//GEN-LAST:event_txtKdOnloop5KeyPressed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSimpanActionPerformed
    {//GEN-HEADEREND:event_btnSimpanActionPerformed
        simpan();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnBaruActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnBaruActionPerformed
    {//GEN-HEADEREND:event_btnBaruActionPerformed
        baru();
    }//GEN-LAST:event_btnBaruActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnHapusActionPerformed
    {//GEN-HEADEREND:event_btnHapusActionPerformed
        hapus();
    }//GEN-LAST:event_btnHapusActionPerformed

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
            java.util.logging.Logger.getLogger(DlgOperasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(DlgOperasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(DlgOperasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(DlgOperasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                DlgOperasi dialog = new DlgOperasi(new javax.swing.JFrame(), true);
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
    private widget.Button btnAnastesi;
    private widget.Button btnAsistenAnestesia1;
    private widget.Button btnAsistenAnestesia2;
    private widget.Button btnAsistenOperator1;
    private widget.Button btnAsistenOperator2;
    private widget.Button btnAsistenOperator3;
    private widget.Button btnBaru;
    private widget.Button btnBidan1;
    private widget.Button btnBidan2;
    private widget.Button btnBidan3;
    private widget.Button btnCariDetail;
    private widget.Button btnCariGroup;
    private widget.Button btnCariKategori;
    private widget.Button btnCariOrder;
    private widget.Button btnCariTransaksi;
    private widget.Button btnCetak;
    private widget.Button btnDrAnak;
    private widget.Button btnDrUmum;
    private widget.Button btnHapus;
    private widget.Button btnInstrumen;
    private widget.Button btnKeluar;
    private widget.Button btnOnloop1;
    private widget.Button btnOnloop2;
    private widget.Button btnOnloop3;
    private widget.Button btnOnloop4;
    private widget.Button btnOnloop5;
    private widget.Button btnOperator1;
    private widget.Button btnOperator2;
    private widget.Button btnOperator3;
    private widget.Button btnPerawatLuar;
    private widget.Button btnPjAnak;
    private widget.Button btnPrResus;
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
    private widget.Label label17;
    private widget.Label label18;
    private widget.Label label19;
    private widget.Label label2;
    private widget.Label label20;
    private widget.Label label21;
    private widget.Label label22;
    private widget.Label label23;
    private widget.Label label24;
    private widget.Label label25;
    private widget.Label label26;
    private widget.Label label27;
    private widget.Label label28;
    private widget.Label label29;
    private widget.Label label3;
    private widget.Label label30;
    private widget.Label label31;
    private widget.Label label32;
    private widget.Label label33;
    private widget.Label label34;
    private widget.Label label35;
    private widget.Label label36;
    private widget.Label label37;
    private widget.Label label38;
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
    private widget.TextBox txtKdAnestesia;
    private widget.TextBox txtKdAsistenAnestesia1;
    private widget.TextBox txtKdAsistenAnestesia2;
    private widget.TextBox txtKdAsistenOperator1;
    private widget.TextBox txtKdAsistenOperator2;
    private widget.TextBox txtKdAsistenOperator3;
    private widget.TextBox txtKdBidan1;
    private widget.TextBox txtKdBidan2;
    private widget.TextBox txtKdBidan3;
    private widget.TextBox txtKdDetail;
    private widget.TextBox txtKdDrAnak;
    private widget.TextBox txtKdDrUmum;
    private widget.TextBox txtKdGroup;
    private widget.TextBox txtKdInstrumen;
    private widget.TextBox txtKdKategori;
    private widget.TextBox txtKdOnloop1;
    private widget.TextBox txtKdOnloop2;
    private widget.TextBox txtKdOnloop3;
    private widget.TextBox txtKdOnloop4;
    private widget.TextBox txtKdOnloop5;
    private widget.TextBox txtKdOperator1;
    private widget.TextBox txtKdOperator2;
    private widget.TextBox txtKdOperator3;
    private widget.TextBox txtKdPerawatLuar;
    private widget.TextBox txtKdPjAnak;
    private widget.TextBox txtKdPrResus;
    private widget.TextBox txtNamaAnestesia;
    private widget.TextBox txtNamaAsistenAnestesia1;
    private widget.TextBox txtNamaAsistenAnestesia2;
    private widget.TextBox txtNamaAsistenOperator1;
    private widget.TextBox txtNamaAsistenOperator2;
    private widget.TextBox txtNamaAsistenOperator3;
    private widget.TextBox txtNamaBidan1;
    private widget.TextBox txtNamaBidan2;
    private widget.TextBox txtNamaBidan3;
    private widget.TextBox txtNamaDetail;
    private widget.TextBox txtNamaDrAnak;
    private widget.TextBox txtNamaDrUmum;
    private widget.TextBox txtNamaGroup;
    private widget.TextBox txtNamaInstrumen;
    private widget.TextBox txtNamaKategori;
    private widget.TextBox txtNamaOnloop1;
    private widget.TextBox txtNamaOnloop2;
    private widget.TextBox txtNamaOnloop3;
    private widget.TextBox txtNamaOnloop4;
    private widget.TextBox txtNamaOnloop5;
    private widget.TextBox txtNamaOperator1;
    private widget.TextBox txtNamaOperator2;
    private widget.TextBox txtNamaOperator3;
    private widget.TextBox txtNamaPasien;
    private widget.TextBox txtNamaPerawatLuar;
    private widget.TextBox txtNamaPjAnak;
    private widget.TextBox txtNamaPrResus;
    private widget.TextBox txtNoRawat;
    private widget.TextBox txtNoRm;
    // End of variables declaration//GEN-END:variables

    // </editor-fold>
}

