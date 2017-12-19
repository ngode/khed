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
import keuangan.DlgJnsPerawatanLab;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.var;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import keuangan.Jurnal;
import util.GConst;
import util.GMessage;

/**
 *
 * @author dosen
 */
public final class DlgPemeriksaanLaboratorium extends javax.swing.JDialog
{

    private DefaultTableModel mdlPemeriksaan, mdlTarif, mdlOrder, mdlTransaksi, mdlDetail;
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private Connection koneksi = koneksiDB.condb();
    private Jurnal jur = new Jurnal();
    private DlgCariPetugas petugas = new DlgCariPetugas(null, false);
    private DlgCariDokter dokter = new DlgCariDokter(null, false);
    private PreparedStatement pstindakan, pstindakan2, pstampil, pstampil2, pstampil3, pstampil4, psDetail, psTraksaksi, psTransaksiD1, psTransaksiD2,
            pssimpanperiksa, psdetailpriksa, psDetailPeriksa2, psDetailPeriksa3, psset_tarif, pssetpj;
    private ResultSet rstindakan, rstampil, rsDetail, rscari, rsset_tarif, rssetpj, rsTransksi, rsTransaksiD1, rsTransaksiD2;
    private boolean[] pilih;
    private String[] kode, nama;
    private double[] total, bagian_rs, bhp, tarif_perujuk, tarif_tindakan_dokter, tarif_tindakan_petugas, kso, menejemen;
    private int jml = 0, i = 0, index = 0;
    private String kamar, namakamar, cara_bayar_lab = "Yes", pilihan = "", status = "";
    private double ttl = 0, item = 0;
    private boolean sukses = false;

    // Data Kelas ===
    private String kelas;
    private JTextField NoRawat;

    // Vars =====
    private List<String> kdJnsPrws = new ArrayList<>();
    private List<String> idTemplates = new ArrayList<>();
    private List<String> idDetTemplates = new ArrayList<>();
    private HashMap<String, String> mapHasil = new HashMap<>();
    private HashMap<String, String> mapKet = new HashMap<>();
    private String _idPeriksa;
    
    /**
     * Creates new form DlgPerawatan
     *
     * @param parent
     * @param modal
     */
    public DlgPemeriksaanLaboratorium(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();

        this.setLocation(8, 1);
        setSize(885, 674);

        Object[] row =
        {
            "P", "Pemeriksaan", "", ""
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
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            @Override
            public Class getColumnClass(int columnIndex)
            {
                return types[columnIndex];
            }
        };

        tbPemeriksaan.setModel(mdlPemeriksaan);
        //tampilPr();

        tbPemeriksaan.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbPemeriksaan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 4; i++)
        {
            TableColumn column = tbPemeriksaan.getColumnModel().getColumn(i);
            if (i == 0)
            {
                column.setPreferredWidth(20);
            }
            else if (i == 1)
            {
                column.setPreferredWidth(192);
            }
            else if (i == 2)
            {
                column.setPreferredWidth(192);
//                column.setMinWidth(0);
//                column.setMaxWidth(0);
            }
            else if (i == 3)
            {
                column.setPreferredWidth(192);
//                column.setMinWidth(0);
//                column.setMaxWidth(0);
            }
        }

        tbPemeriksaan.setDefaultRenderer(Object.class, new WarnaTable());
        
        // Detail ====
        Object[] r =
        {
            "P", "Pemeriksaan", "Hasil", "Satuan", "Nilai Rujukan",
            "Keterangan", "", "", "Jns"
        };
        
        mdlDetail = new DefaultTableModel(null, r)
        {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                boolean a = false;
                if ((colIndex == 2) || (colIndex == 5))
                {
                    a = true;
                }
                return a;
            }
            Class[] types = new Class[]
            {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            @Override
            public Class getColumnClass(int columnIndex)
            {
                return types[columnIndex];
            }
        };

        tblDetail.setModel(mdlDetail);

        tblDetail.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tblDetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        tblDetail.getModel().addTableModelListener(new TableModelListener()
        {
            @Override
            public void tableChanged(TableModelEvent e)
            {
                if (e.getColumn() == 2)
                {
                    String jns = tblDetail.getValueAt(e.getFirstRow(), 8).toString();
                    String idDet = jns + tblDetail.getValueAt(e.getFirstRow(), 6).toString();
                    String val = tblDetail.getValueAt(e.getFirstRow(), 2).toString();
                    
                    if (mapHasil.containsKey(idDet))
                    {
                        mapHasil.remove(idDet);
                    }
                    
                    mapHasil.put(idDet, val);
                }
                else if (e.getColumn() == 5)
                {
                    String jns = tblDetail.getValueAt(e.getFirstRow(), 8).toString();
                    String idDet = jns + tblDetail.getValueAt(e.getFirstRow(), 6).toString();
                    String val = tblDetail.getValueAt(e.getFirstRow(), 5).toString();
                    
                    if (mapKet.containsKey(idDet))
                    {
                        mapKet.remove(idDet);
                    }
                    
                    mapKet.put(idDet, val);
                }
            }
        });

        for (i = 0; i < 9; i++)
        {
            TableColumn column = tblDetail.getColumnModel().getColumn(i);
            if (i == 0)
            {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
            else if (i == 1)
            {
                column.setPreferredWidth(192);
            }
            else if (i == 2)
            {
                column.setPreferredWidth(130);
            }
            else if (i == 3)
            {
                column.setPreferredWidth(70);
            }
            else if (i == 4)
            {
                column.setPreferredWidth(130);
            }
            else if (i == 5)
            {
                column.setPreferredWidth(150);
            }
            else if (i == 6)
            {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
            else if (i == 7)
            {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
            else if (i == 8)
            {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }

        tblDetail.setDefaultRenderer(Object.class, new WarnaTable());
        // ===========

        Object[] row2 =
        {
            "P", "Kode Periksa", "Nama Pemeriksaan"
        };
        
        mdlTarif = new DefaultTableModel(null, row2)
        {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                boolean a = false;
                if ((colIndex == 0) || (colIndex == 3))
                {
                    a = true;
                }
                return a;
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
        
        tbTarif.setModel(mdlTarif);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbTarif.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbTarif.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 3; i++)
        {
            TableColumn column = tbTarif.getColumnModel().getColumn(i);
            if (i == 0)
            {
                column.setPreferredWidth(20);
            }
            else if (i == 1)
            {
                column.setPreferredWidth(100);
            }
            else if (i == 2)
            {
                column.setPreferredWidth(380);
            }
        }
        tbTarif.setDefaultRenderer(Object.class, new WarnaTable());

        initTblOrder();
        initTblTransaksi();
        
        TNoRw.setDocument(new batasInput((byte) 17).getKata(TNoRw));
        Jk.setDocument(new batasInput((byte) 5).getKata(Jk));
        Umur.setDocument(new batasInput((byte) 5).getKata(Umur));
        KdPtg.setDocument(new batasInput((byte) 20).getKata(KdPtg));
        KodePerujuk.setDocument(new batasInput((byte) 20).getKata(KodePerujuk));
        Pemeriksaan.setDocument(new batasInput((byte) 100).getKata(Pemeriksaan));
        
        if (koneksiDB.cariCepat().equals("aktif"))
        {
            Pemeriksaan.addTextChangedListener((t) -> tampilTarif(true));
            TCari.addTextChangedListener((t) -> tampilOrder());
            TCari1.addTextChangedListener((t) -> tampilTransksi());
        }
        
        ChkJln.setSelected(true);
        jam();

        petugas.addWindowListener(new WindowListener()
        {
            @Override
            public void windowOpened(WindowEvent e)
            {
            }

            @Override
            public void windowClosing(WindowEvent e)
            {
            }

            @Override
            public void windowClosed(WindowEvent e)
            {
                if (var.getform().equals("DlgPeriksaLaboratorium"))
                {
                    if (petugas.getTable().getSelectedRow() != -1)
                    {
                        KdPtg.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(), 0).toString());
                        NmPtg.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(), 1).toString());
                    }
                    KdPtg.requestFocus();
                }
            }

            @Override
            public void windowIconified(WindowEvent e)
            {
            }

            @Override
            public void windowDeiconified(WindowEvent e)
            {
            }

            @Override
            public void windowActivated(WindowEvent e)
            {
            }

            @Override
            public void windowDeactivated(WindowEvent e)
            {
            }
        });

        dokter.addWindowListener(new WindowListener()
        {
            @Override
            public void windowOpened(WindowEvent e)
            {
            }

            @Override
            public void windowClosing(WindowEvent e)
            {
            }

            @Override
            public void windowClosed(WindowEvent e)
            {
                if (var.getform().equals("DlgPeriksaLaboratorium"))
                {
                    if (dokter.getTable().getSelectedRow() != -1)
                    {
                        if (pilihan.equals("perujuk"))
                        {
                            KodePerujuk.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 0).toString());
                            NmPerujuk.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 1).toString());
                            KodePerujuk.requestFocus();
                        }
                        else if (pilihan.equals("penjab"))
                        {
                            KodePj.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 0).toString());
                            NmDokterPj.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 1).toString());
                            KodePj.requestFocus();
                        }
                    }
                }
            }

            @Override
            public void windowIconified(WindowEvent e)
            {
            }

            @Override
            public void windowDeiconified(WindowEvent e)
            {
            }

            @Override
            public void windowActivated(WindowEvent e)
            {
            }

            @Override
            public void windowDeactivated(WindowEvent e)
            {
            }
        });
        
        tampilOrder();
        tampilTransksi();
    }

    private void initTblOrder()
    {
        Object[] row =
        {
            "No Rawat", "Pasien", "Petugas", "Tgl Periksa", "Jam Periksa", "Dokter Perujuk", "Penanggung Jawab", "Proses", "Id"
        };
        
        mdlOrder = new DefaultTableModel(null, row)
        {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                return false;
            }
            Class[] types = new Class[]
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                java.lang.Object.class, 
            };

            @Override
            public Class getColumnClass(int columnIndex)
            {
                return types[columnIndex];
            }
        };

        tblOrder.setModel(mdlOrder);
        //tampilPr();

        tblOrder.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tblOrder.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 9; i++)
        {
            TableColumn column = tblOrder.getColumnModel().getColumn(i);
            if (i == 0)
            {
                column.setPreferredWidth(140);
            }
            else if (i == 1)
            {
                column.setPreferredWidth(192);
            }
            else if (i == 2)
            {
                column.setPreferredWidth(160);
            }
            else if (i == 3)
            {
                column.setPreferredWidth(120);
            }
            else if (i == 4)
            {
                column.setPreferredWidth(130);
            }
            else if (i == 5)
            {
                column.setPreferredWidth(150);
            }
            else if (i == 6)
            {
                column.setPreferredWidth(150);
            }
            else if (i == 7)
            {
                column.setPreferredWidth(120);
            }
            else if (i == 8)
            {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }

        tblOrder.setDefaultRenderer(Object.class, new WarnaTable());
    }

    private void initTblTransaksi()
    {
        Object[] row =
        {
            "No Rawat", "Pasien", "Petugas", "Tgl Periksa", "Jam Periksa", "Dokter Perujuk", "Penanggung Jawab", "Proses", "Id"
        };
        
        mdlTransaksi = new DefaultTableModel(null, row)
        {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                return false;
            }
            
            Class[] types = new Class[]
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                java.lang.Object.class, 
            };

            @Override
            public Class getColumnClass(int columnIndex)
            {
                return types[columnIndex];
            }
        };

        tblTransaksi.setModel(mdlTransaksi);
        //tampilPr();

        tblTransaksi.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tblTransaksi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 9; i++)
        {
            TableColumn column = tblTransaksi.getColumnModel().getColumn(i);
            if (i == 0)
            {
                column.setPreferredWidth(140);
            }
            else if (i == 1)
            {
                column.setPreferredWidth(192);
            }
            else if (i == 2)
            {
                column.setPreferredWidth(160);
            }
            else if (i == 3)
            {
                column.setPreferredWidth(120);
            }
            else if (i == 4)
            {
                column.setPreferredWidth(130);
            }
            else if (i == 5)
            {
                column.setPreferredWidth(150);
            }
            else if (i == 6)
            {
                column.setPreferredWidth(150);
            }
            else if (i == 7)
            {
                column.setPreferredWidth(120);
            }
            else if (i == 8)
            {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }

        tblTransaksi.setDefaultRenderer(Object.class, new WarnaTable());
    }
    
    private void clearSemua()
    {
        _idPeriksa = null;
        
        TNoRw.setText("");
        TNoRM.setText("");
        TPasien.setText("");
        KodePj.setText("");
        NmDokterPj.setText("");
        KdPtg.setText("");
        NmPtg.setText("");
        KodePerujuk.setText("");
        NmPerujuk.setText("");
        
        mapHasil.clear();
        mapKet.clear();
        
        kdJnsPrws.clear();
        idTemplates.clear();
        idDetTemplates.clear();
        
        Valid.tabelKosong(mdlTarif);
        Valid.tabelKosong(mdlPemeriksaan);
        Valid.tabelKosong(mdlDetail);
        
        tampilOrder();
        tampilTransksi();
    }
    
    private void dariOrder(String idPeriksa)
    {
        _idPeriksa = idPeriksa;
        tabPane.setSelectedIndex(0);
        
        // NGambil daat Pemeriksaan LAB
        String[] periksa = new GQuery()
                .a("SELECT no_rawat, nip, dokter_perujuk, kd_dokter, status FROM periksa_lab")
                .a("WHERE id_periksa = {id_periksa}")
                .set("id_periksa", idPeriksa)
                .getRow();
        
        // Dimasukkken ke textbox
        TNoRw.setText(periksa[0]);
        KodePj.setText(periksa[3]);
        Sequel.cariIsi("select nm_dokter from dokter where kd_dokter = ?", NmDokterPj, KodePj.getText());
        KdPtg.setText(periksa[1]);
        Sequel.cariIsi("select nama from petugas where nip = ?", NmPtg, KdPtg.getText());
        
        // Ngeset dewasa/anak OTO
        int umur = new GQuery()
                .a("SELECT DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW()) - TO_DAYS(tgl_lahir)), '%Y')+0 AS age")
                .a("FROM reg_periksa")
                .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                .a("WHERE no_rawat = {no_rwt}")
                .set("no_rwt", TNoRw.getText())
                .getInt();
        
        if (umur >= GConst.DEWASA)
        {
            rbDewasa.setSelected(true);
        }
        else
        {
            rbAnak.setSelected(true);
        }
        
        this.status = periksa[4];

        if (status.equals("Ralan"))
        {
            kelas = "Kelas 3";
        }
        else
        {
            kelas = new GQuery()
                    .a("SELECT kelas")
                    .a("FROM kamar_inap")
                    .a("JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                    .a("WHERE no_rawat = {no_rawat}")
                    .set("no_rawat", TNoRw.getText())
                    .getString();
        }

        isRawat();
        isPsien();
        
        selectPeriksa(idPeriksa);
        isReset(false);
    }
    
    private void dariTransaksi(String idPeriksa)
    {
        _idPeriksa = idPeriksa;
        tabPane.setSelectedIndex(0);
        
        // NGambil daat Pemeriksaan LAB
        String[] periksa = new GQuery()
                .a("SELECT no_rawat, nip, dokter_perujuk, kd_dokter, status FROM periksa_lab")
                .a("WHERE id_periksa = {id_periksa}")
                .set("id_periksa", idPeriksa)
                .getRow();
        
        // Dimasukkken ke textbox
        TNoRw.setText(periksa[0]);
        KodePj.setText(periksa[3]);
        Sequel.cariIsi("select nm_dokter from dokter where kd_dokter = ?", NmDokterPj, KodePj.getText());
        KdPtg.setText(periksa[1]);
        Sequel.cariIsi("select nama from petugas where nip = ?", NmPtg, KdPtg.getText());
        
        // Ngeset dewasa/anak OTO
        int umur = new GQuery()
                .a("SELECT DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW()) - TO_DAYS(tgl_lahir)), '%Y')+0 AS age")
                .a("FROM reg_periksa")
                .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                .a("WHERE no_rawat = {no_rwt}")
                .set("no_rwt", TNoRw.getText())
                .getInt();
        
        if (umur >= GConst.DEWASA)
        {
            rbDewasa.setSelected(true);
        }
        else
        {
            rbAnak.setSelected(true);
        }
        
        this.status = periksa[4];

        if (status.equals("Ralan"))
        {
            kelas = "Kelas 3";
        }
        else
        {
            kelas = new GQuery()
                    .a("SELECT kelas")
                    .a("FROM kamar_inap")
                    .a("JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                    .a("WHERE no_rawat = {no_rawat}")
                    .set("no_rawat", TNoRw.getText())
                    .getString();
        }

        isRawat();
        isPsien();
        
        selectPeriksa(idPeriksa);
        selectHasil(idPeriksa);
        isReset(false);
    }
    
    // Ngelist yg kecentang
    private void selectPeriksa(String idPeriksa)
    {
        kdJnsPrws.clear();
        idTemplates.clear();
        idDetTemplates.clear();
        
        List<String[]> lKdPrw = new GQuery()
                .a("SELECT id_detail, kd_jenis_prw FROM detail_periksa_lab WHERE id_periksa = {id_periksa}")
                .set("id_periksa", idPeriksa)
                .select();
        
        for (String[] sKdPrw : lKdPrw)
        {
            kdJnsPrws.add(sKdPrw[1]);
            
            List<String[]> lIdTemp = new GQuery()
                    .a("SELECT id_detail, id_template FROM detail_periksa_lab_2 WHERE id_detail_1 = {id_detail}")
                    .set("id_detail", sKdPrw[0])
                    .select();
            
            for (String[] sIdTemp : lIdTemp)
            {
                idTemplates.add(sIdTemp[1]);
            }
        }
    }
    
    // Ngelist hasil dan keterangan
    private void selectHasil(String idPeriksa)
    {
        mapHasil.clear();
        mapKet.clear();
        
        List<String[]> lDet2 = new GQuery()
                .a("SELECT detail_periksa_lab_2.id_template, detail_periksa_lab_2.nilai, detail_periksa_lab_2.keterangan")
                .a("FROM detail_periksa_lab_2")
                .a("JOIN detail_periksa_lab ON detail_periksa_lab.id_detail = detail_periksa_lab_2.id_detail_1")
                .a("WHERE id_periksa = {id_periksa}")
                .set("id_periksa", idPeriksa)
                .select();
        
        for (String[] sa : lDet2)
        {
            mapHasil.put("Atas" + sa[0], sa[1]);
            mapKet.put("Atas" + sa[0], sa[2]);
        }
        
        List<String[]> lDet3 = new GQuery()
                .a("SELECT detail_periksa_lab_3.id_det_template, detail_periksa_lab_3.nilai, detail_periksa_lab_3.keterangan")
                .a("FROM detail_periksa_lab_3")
                .a("JOIN detail_periksa_lab_2 ON detail_periksa_lab_2.id_detail = detail_periksa_lab_3.id_detail_2")
                .a("JOIN detail_periksa_lab ON detail_periksa_lab.id_detail = detail_periksa_lab_2.id_detail_1")
                .a("WHERE id_periksa = {id_periksa}")
                .set("id_periksa", idPeriksa)
                .select();
        
        for (String[] sa : lDet3)
        {
            mapHasil.put("Bawah" + sa[0], sa[1]);
            mapKet.put("Bawah" + sa[0], sa[2]);
        }
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
        buttonGroup1 = new javax.swing.ButtonGroup();
        Alamat = new widget.TextBox();
        Popup = new javax.swing.JPopupMenu();
        ppCetakHasil = new javax.swing.JMenuItem();
        ppBersihkan = new javax.swing.JMenuItem();
        ppSemua = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        tabPane = new widget.TabPane();
        panelisi1 = new widget.panelisi();
        jPanel1 = new javax.swing.JPanel();
        Scroll = new widget.ScrollPane();
        tbPemeriksaan = new widget.Table();
        Scroll2 = new widget.ScrollPane();
        tblDetail = new widget.Table();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnPrint = new widget.Button();
        BtnNota = new widget.Button();
        jLabel10 = new widget.Label();
        BtnCari = new widget.Button();
        BtnKeluar = new widget.Button();
        FormInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        PanelInput = new widget.PanelBiasa();
        jLabel3 = new widget.Label();
        TNoRw = new widget.TextBox();
        TNoRM = new widget.TextBox();
        TPasien = new widget.TextBox();
        jLabel11 = new widget.Label();
        jLabel7 = new widget.Label();
        jLabel9 = new widget.Label();
        Pemeriksaan = new widget.TextBox();
        jLabel12 = new widget.Label();
        KdPtg = new widget.TextBox();
        btnPetugas = new widget.Button();
        NmPtg = new widget.TextBox();
        Tanggal = new widget.Tanggal();
        CmbJam = new widget.ComboBox();
        CmbMenit = new widget.ComboBox();
        CmbDetik = new widget.ComboBox();
        ChkJln = new widget.CekBox();
        jLabel16 = new widget.Label();
        BtnCari1 = new widget.Button();
        btnDokter = new widget.Button();
        Scroll1 = new widget.ScrollPane();
        tbTarif = new widget.Table();
        jLabel15 = new widget.Label();
        btnTarif = new widget.Button();
        rbAnak = new widget.RadioButton();
        rbDewasa = new widget.RadioButton();
        NmDokterPj = new widget.TextBox();
        KodePj = new widget.TextBox();
        KodePerujuk = new widget.TextBox();
        NmPerujuk = new widget.TextBox();
        btnDokter1 = new widget.Button();
        btnDokterPj = new widget.Button();
        panelisi2 = new widget.panelisi();
        internalFrame2 = new widget.InternalFrame();
        scrollPane1 = new widget.ScrollPane();
        tblOrder = new widget.Table();
        panelisi4 = new widget.panelisi();
        jLabel33 = new widget.Label();
        tglTransaksi1 = new widget.Tanggal();
        jLabel34 = new widget.Label();
        tglTransaksi2 = new widget.Tanggal();
        label10 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari2 = new widget.Button();
        label9 = new widget.Label();
        BtnHapus1 = new widget.Button();
        BtnAll = new widget.Button();
        BtnPrint1 = new widget.Button();
        BtnKeluar1 = new widget.Button();
        panelisi3 = new widget.panelisi();
        internalFrame3 = new widget.InternalFrame();
        scrollPane2 = new widget.ScrollPane();
        tblTransaksi = new widget.Table();
        panelisi5 = new widget.panelisi();
        jLabel35 = new widget.Label();
        tglTransaksi3 = new widget.Tanggal();
        jLabel36 = new widget.Label();
        tglTransaksi4 = new widget.Tanggal();
        label11 = new widget.Label();
        TCari1 = new widget.TextBox();
        BtnCari3 = new widget.Button();
        label12 = new widget.Label();
        BtnHapus2 = new widget.Button();
        BtnAll1 = new widget.Button();
        BtnPrint2 = new widget.Button();
        BtnKeluar2 = new widget.Button();

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

        Popup.setName("Popup"); // NOI18N

        ppCetakHasil.setBackground(new java.awt.Color(255, 255, 255));
        ppCetakHasil.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppCetakHasil.setForeground(new java.awt.Color(102, 51, 0));
        ppCetakHasil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppCetakHasil.setText("Cetak Hasil");
        ppCetakHasil.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppCetakHasil.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppCetakHasil.setIconTextGap(8);
        ppCetakHasil.setName("ppCetakHasil"); // NOI18N
        ppCetakHasil.setPreferredSize(new java.awt.Dimension(200, 25));
        ppCetakHasil.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ppCetakHasilActionPerformed(evt);
            }
        });
        Popup.add(ppCetakHasil);

        ppBersihkan.setBackground(new java.awt.Color(255, 255, 255));
        ppBersihkan.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppBersihkan.setForeground(new java.awt.Color(102, 51, 0));
        ppBersihkan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppBersihkan.setText("Bersihkan Pilihan");
        ppBersihkan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppBersihkan.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppBersihkan.setIconTextGap(8);
        ppBersihkan.setName("ppBersihkan"); // NOI18N
        ppBersihkan.setPreferredSize(new java.awt.Dimension(200, 25));
        ppBersihkan.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ppBersihkanActionPerformed(evt);
            }
        });
        Popup.add(ppBersihkan);

        ppSemua.setBackground(new java.awt.Color(255, 255, 255));
        ppSemua.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppSemua.setForeground(new java.awt.Color(102, 51, 0));
        ppSemua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppSemua.setText("Pilih Semua");
        ppSemua.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppSemua.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppSemua.setIconTextGap(8);
        ppSemua.setName("ppSemua"); // NOI18N
        ppSemua.setPreferredSize(new java.awt.Dimension(200, 25));
        ppSemua.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ppSemuaActionPerformed(evt);
            }
        });
        Popup.add(ppSemua);

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

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Pemeriksaan Laboratorium ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        tabPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 10))); // NOI18N
        tabPane.setName("tabPane"); // NOI18N

        panelisi1.setName("panelisi1"); // NOI18N
        panelisi1.setLayout(new java.awt.BorderLayout());

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbPemeriksaan.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbPemeriksaan.setName("tbPemeriksaan"); // NOI18N
        tbPemeriksaan.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                tbPemeriksaanPropertyChange(evt);
            }
        });
        Scroll.setViewportView(tbPemeriksaan);

        jPanel1.add(Scroll);

        Scroll2.setName("Scroll2"); // NOI18N
        Scroll2.setOpaque(true);

        tblDetail.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tblDetail.setName("tblDetail"); // NOI18N
        Scroll2.setViewportView(tblDetail);

        jPanel1.add(Scroll2);

        panelisi1.add(jPanel1, java.awt.BorderLayout.CENTER);

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

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
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
        panelGlass8.add(BtnHapus);

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

        BtnNota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Agenda-1-16x16.png"))); // NOI18N
        BtnNota.setMnemonic('N');
        BtnNota.setText("Nota");
        BtnNota.setToolTipText("Alt+N");
        BtnNota.setName("BtnNota"); // NOI18N
        BtnNota.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnNota.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnNotaActionPerformed(evt);
            }
        });
        BtnNota.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                BtnNotaKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnNota);

        jLabel10.setName("jLabel10"); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(40, 30));
        panelGlass8.add(jLabel10);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnCari.setMnemonic('C');
        BtnCari.setText("Cari");
        BtnCari.setToolTipText("Alt+C");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnCari.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnCariActionPerformed(evt);
            }
        });
        BtnCari.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                BtnCariKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnCari);

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
        FormInput.setPreferredSize(new java.awt.Dimension(560, 269));
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
        PanelInput.setPreferredSize(new java.awt.Dimension(560, 168));
        PanelInput.setLayout(null);

        jLabel3.setText("No.Rawat :");
        jLabel3.setName("jLabel3"); // NOI18N
        PanelInput.add(jLabel3);
        jLabel3.setBounds(0, 12, 92, 23);

        TNoRw.setEditable(false);
        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        PanelInput.add(TNoRw);
        TNoRw.setBounds(95, 12, 148, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        PanelInput.add(TNoRM);
        TNoRM.setBounds(245, 12, 125, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        PanelInput.add(TPasien);
        TPasien.setBounds(372, 12, 400, 23);

        jLabel11.setText("Pemeriksaan :");
        jLabel11.setName("jLabel11"); // NOI18N
        PanelInput.add(jLabel11);
        jLabel11.setBounds(0, 102, 92, 23);

        jLabel7.setText("Dokter P.J. :");
        jLabel7.setName("jLabel7"); // NOI18N
        PanelInput.add(jLabel7);
        jLabel7.setBounds(0, 42, 92, 23);

        jLabel9.setText("Dokter Perujuk :");
        jLabel9.setName("jLabel9"); // NOI18N
        PanelInput.add(jLabel9);
        jLabel9.setBounds(0, 72, 92, 23);

        Pemeriksaan.setHighlighter(null);
        Pemeriksaan.setName("Pemeriksaan"); // NOI18N
        Pemeriksaan.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                PemeriksaanKeyPressed(evt);
            }
        });
        PanelInput.add(Pemeriksaan);
        Pemeriksaan.setBounds(245, 102, 465, 23);

        jLabel12.setText("Petugas Lab :");
        jLabel12.setName("jLabel12"); // NOI18N
        PanelInput.add(jLabel12);
        jLabel12.setBounds(375, 42, 87, 23);

        KdPtg.setName("KdPtg"); // NOI18N
        KdPtg.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                KdPtgKeyPressed(evt);
            }
        });
        PanelInput.add(KdPtg);
        KdPtg.setBounds(464, 42, 80, 23);

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
        btnPetugas.setBounds(744, 42, 28, 23);

        NmPtg.setEditable(false);
        NmPtg.setName("NmPtg"); // NOI18N
        PanelInput.add(NmPtg);
        NmPtg.setBounds(546, 42, 195, 23);

        Tanggal.setEditable(false);
        Tanggal.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "19-12-2017" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);
        Tanggal.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                TanggalKeyPressed(evt);
            }
        });
        PanelInput.add(Tanggal);
        Tanggal.setBounds(464, 72, 90, 23);

        CmbJam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        CmbJam.setName("CmbJam"); // NOI18N
        CmbJam.setOpaque(false);
        PanelInput.add(CmbJam);
        CmbJam.setBounds(608, 72, 45, 23);

        CmbMenit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        CmbMenit.setName("CmbMenit"); // NOI18N
        CmbMenit.setOpaque(false);
        PanelInput.add(CmbMenit);
        CmbMenit.setBounds(655, 72, 45, 23);

        CmbDetik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        CmbDetik.setName("CmbDetik"); // NOI18N
        CmbDetik.setOpaque(false);
        PanelInput.add(CmbDetik);
        CmbDetik.setBounds(702, 72, 45, 23);

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
        ChkJln.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ChkJlnActionPerformed(evt);
            }
        });
        PanelInput.add(ChkJln);
        ChkJln.setBounds(749, 72, 23, 23);

        jLabel16.setText("Jam :");
        jLabel16.setName("jLabel16"); // NOI18N
        PanelInput.add(jLabel16);
        jLabel16.setBounds(566, 72, 40, 23);

        BtnCari1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari1.setMnemonic('1');
        BtnCari1.setToolTipText("Alt+1");
        BtnCari1.setName("BtnCari1"); // NOI18N
        BtnCari1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnCari1ActionPerformed(evt);
            }
        });
        PanelInput.add(BtnCari1);
        BtnCari1.setBounds(713, 102, 28, 23);

        btnDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokter.setMnemonic('4');
        btnDokter.setToolTipText("ALt+4");
        btnDokter.setName("btnDokter"); // NOI18N
        btnDokter.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDokterActionPerformed(evt);
            }
        });
        PanelInput.add(btnDokter);
        btnDokter.setBounds(359, 72, 28, 23);

        Scroll1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)));
        Scroll1.setName("Scroll1"); // NOI18N

        tbTarif.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbTarif.setName("tbTarif"); // NOI18N
        tbTarif.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tbTarifMouseClicked(evt);
            }
        });
        Scroll1.setViewportView(tbTarif);

        PanelInput.add(Scroll1);
        Scroll1.setBounds(95, 127, 677, 110);

        jLabel15.setText("Tgl.Periksa :");
        jLabel15.setName("jLabel15"); // NOI18N
        PanelInput.add(jLabel15);
        jLabel15.setBounds(375, 72, 87, 23);

        btnTarif.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        btnTarif.setMnemonic('2');
        btnTarif.setToolTipText("Alt+2");
        btnTarif.setName("btnTarif"); // NOI18N
        btnTarif.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTarifActionPerformed(evt);
            }
        });
        PanelInput.add(btnTarif);
        btnTarif.setBounds(744, 102, 28, 23);

        buttonGroup1.add(rbAnak);
        rbAnak.setText("Anak-Anak");
        rbAnak.setEnabled(false);
        rbAnak.setIconTextGap(1);
        rbAnak.setName("rbAnak"); // NOI18N
        rbAnak.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                rbAnakMouseClicked(evt);
            }
        });
        PanelInput.add(rbAnak);
        rbAnak.setBounds(165, 102, 80, 23);

        buttonGroup1.add(rbDewasa);
        rbDewasa.setText("Dewasa");
        rbDewasa.setEnabled(false);
        rbDewasa.setIconTextGap(1);
        rbDewasa.setName("rbDewasa"); // NOI18N
        rbDewasa.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                rbDewasaMouseClicked(evt);
            }
        });
        PanelInput.add(rbDewasa);
        rbDewasa.setBounds(95, 102, 80, 23);

        NmDokterPj.setEditable(false);
        NmDokterPj.setHighlighter(null);
        NmDokterPj.setName("NmDokterPj"); // NOI18N
        PanelInput.add(NmDokterPj);
        NmDokterPj.setBounds(177, 42, 180, 23);

        KodePj.setName("KodePj"); // NOI18N
        PanelInput.add(KodePj);
        KodePj.setBounds(95, 42, 80, 23);

        KodePerujuk.setName("KodePerujuk"); // NOI18N
        KodePerujuk.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                KodePerujukKeyPressed(evt);
            }
        });
        PanelInput.add(KodePerujuk);
        KodePerujuk.setBounds(95, 72, 80, 23);

        NmPerujuk.setEditable(false);
        NmPerujuk.setHighlighter(null);
        NmPerujuk.setName("NmPerujuk"); // NOI18N
        PanelInput.add(NmPerujuk);
        NmPerujuk.setBounds(177, 72, 180, 23);

        btnDokter1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokter1.setMnemonic('4');
        btnDokter1.setToolTipText("ALt+4");
        btnDokter1.setName("btnDokter1"); // NOI18N
        btnDokter1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDokter1ActionPerformed(evt);
            }
        });
        PanelInput.add(btnDokter1);
        btnDokter1.setBounds(359, 72, 28, 23);

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
        btnDokterPj.setBounds(359, 42, 28, 23);

        FormInput.add(PanelInput, java.awt.BorderLayout.CENTER);

        panelisi1.add(FormInput, java.awt.BorderLayout.PAGE_START);

        tabPane.addTab("Transaksi", panelisi1);

        panelisi2.setName("panelisi2"); // NOI18N
        panelisi2.setLayout(new java.awt.BorderLayout());

        internalFrame2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data Order Laboratorium ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(new java.awt.BorderLayout(1, 1));

        scrollPane1.setName("scrollPane1"); // NOI18N
        scrollPane1.setOpaque(true);

        tblOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {},
                {},
                {},
                {}
            },
            new String []
            {

            }
        ));
        tblOrder.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tblOrder.setName("tblOrder"); // NOI18N
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
        scrollPane1.setViewportView(tblOrder);

        internalFrame2.add(scrollPane1, java.awt.BorderLayout.CENTER);

        panelisi4.setName("panelisi4"); // NOI18N
        panelisi4.setPreferredSize(new java.awt.Dimension(100, 56));
        panelisi4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel33.setText("Tgl :");
        jLabel33.setName("jLabel33"); // NOI18N
        jLabel33.setPreferredSize(new java.awt.Dimension(58, 23));
        panelisi4.add(jLabel33);

        tglTransaksi1.setEditable(false);
        tglTransaksi1.setForeground(new java.awt.Color(50, 70, 50));
        tglTransaksi1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "19-12-2017" }));
        tglTransaksi1.setDisplayFormat("dd-MM-yyyy");
        tglTransaksi1.setName("tglTransaksi1"); // NOI18N
        tglTransaksi1.setOpaque(false);
        tglTransaksi1.setPreferredSize(new java.awt.Dimension(100, 23));
        panelisi4.add(tglTransaksi1);

        jLabel34.setText("s.d");
        jLabel34.setName("jLabel34"); // NOI18N
        jLabel34.setPreferredSize(new java.awt.Dimension(18, 23));
        panelisi4.add(jLabel34);

        tglTransaksi2.setEditable(false);
        tglTransaksi2.setForeground(new java.awt.Color(50, 70, 50));
        tglTransaksi2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "19-12-2017" }));
        tglTransaksi2.setDisplayFormat("dd-MM-yyyy");
        tglTransaksi2.setName("tglTransaksi2"); // NOI18N
        tglTransaksi2.setOpaque(false);
        tglTransaksi2.setPreferredSize(new java.awt.Dimension(100, 23));
        panelisi4.add(tglTransaksi2);

        label10.setText("Key Word :");
        label10.setName("label10"); // NOI18N
        label10.setPreferredSize(new java.awt.Dimension(70, 23));
        panelisi4.add(label10);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(170, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                TCariKeyPressed(evt);
            }
        });
        panelisi4.add(TCari);

        BtnCari2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari2.setMnemonic('5');
        BtnCari2.setToolTipText("Alt+5");
        BtnCari2.setName("BtnCari2"); // NOI18N
        BtnCari2.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnCari2ActionPerformed(evt);
            }
        });
        BtnCari2.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                BtnCari2KeyPressed(evt);
            }
        });
        panelisi4.add(BtnCari2);

        label9.setName("label9"); // NOI18N
        label9.setPreferredSize(new java.awt.Dimension(100, 30));
        panelisi4.add(label9);

        BtnHapus1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus1.setMnemonic('H');
        BtnHapus1.setText("Hapus");
        BtnHapus1.setToolTipText("Alt+H");
        BtnHapus1.setName("BtnHapus1"); // NOI18N
        BtnHapus1.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnHapus1ActionPerformed(evt);
            }
        });
        BtnHapus1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                BtnHapus1KeyPressed(evt);
            }
        });
        panelisi4.add(BtnHapus1);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setText("Semua");
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnAll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnAllActionPerformed(evt);
            }
        });
        BtnAll.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                BtnAllKeyPressed(evt);
            }
        });
        panelisi4.add(BtnAll);

        BtnPrint1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint1.setMnemonic('T');
        BtnPrint1.setText("Cetak");
        BtnPrint1.setToolTipText("Alt+T");
        BtnPrint1.setName("BtnPrint1"); // NOI18N
        BtnPrint1.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnPrint1ActionPerformed(evt);
            }
        });
        BtnPrint1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                BtnPrint1KeyPressed(evt);
            }
        });
        panelisi4.add(BtnPrint1);

        BtnKeluar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar1.setMnemonic('K');
        BtnKeluar1.setText("Keluar");
        BtnKeluar1.setToolTipText("Alt+K");
        BtnKeluar1.setName("BtnKeluar1"); // NOI18N
        BtnKeluar1.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnKeluar1ActionPerformed(evt);
            }
        });
        BtnKeluar1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                BtnKeluar1KeyPressed(evt);
            }
        });
        panelisi4.add(BtnKeluar1);

        internalFrame2.add(panelisi4, java.awt.BorderLayout.PAGE_END);

        panelisi2.add(internalFrame2, java.awt.BorderLayout.CENTER);

        tabPane.addTab("List Order", panelisi2);

        panelisi3.setName("panelisi3"); // NOI18N
        panelisi3.setLayout(new java.awt.BorderLayout());

        internalFrame3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data Pemeriksaan Laboratorium ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame3.setName("internalFrame3"); // NOI18N
        internalFrame3.setLayout(new java.awt.BorderLayout(1, 1));

        scrollPane2.setName("scrollPane2"); // NOI18N
        scrollPane2.setOpaque(true);

        tblTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {},
                {},
                {},
                {}
            },
            new String []
            {

            }
        ));
        tblTransaksi.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tblTransaksi.setComponentPopupMenu(Popup);
        tblTransaksi.setName("tblTransaksi"); // NOI18N
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
        scrollPane2.setViewportView(tblTransaksi);

        internalFrame3.add(scrollPane2, java.awt.BorderLayout.CENTER);

        panelisi5.setName("panelisi5"); // NOI18N
        panelisi5.setPreferredSize(new java.awt.Dimension(100, 56));
        panelisi5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel35.setText("Tgl :");
        jLabel35.setName("jLabel35"); // NOI18N
        jLabel35.setPreferredSize(new java.awt.Dimension(58, 23));
        panelisi5.add(jLabel35);

        tglTransaksi3.setEditable(false);
        tglTransaksi3.setForeground(new java.awt.Color(50, 70, 50));
        tglTransaksi3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "19-12-2017" }));
        tglTransaksi3.setDisplayFormat("dd-MM-yyyy");
        tglTransaksi3.setName("tglTransaksi3"); // NOI18N
        tglTransaksi3.setOpaque(false);
        tglTransaksi3.setPreferredSize(new java.awt.Dimension(100, 23));
        panelisi5.add(tglTransaksi3);

        jLabel36.setText("s.d");
        jLabel36.setName("jLabel36"); // NOI18N
        jLabel36.setPreferredSize(new java.awt.Dimension(18, 23));
        panelisi5.add(jLabel36);

        tglTransaksi4.setEditable(false);
        tglTransaksi4.setForeground(new java.awt.Color(50, 70, 50));
        tglTransaksi4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "19-12-2017" }));
        tglTransaksi4.setDisplayFormat("dd-MM-yyyy");
        tglTransaksi4.setName("tglTransaksi4"); // NOI18N
        tglTransaksi4.setOpaque(false);
        tglTransaksi4.setPreferredSize(new java.awt.Dimension(100, 23));
        panelisi5.add(tglTransaksi4);

        label11.setText("Key Word :");
        label11.setName("label11"); // NOI18N
        label11.setPreferredSize(new java.awt.Dimension(70, 23));
        panelisi5.add(label11);

        TCari1.setName("TCari1"); // NOI18N
        TCari1.setPreferredSize(new java.awt.Dimension(170, 23));
        TCari1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                TCari1KeyPressed(evt);
            }
        });
        panelisi5.add(TCari1);

        BtnCari3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari3.setMnemonic('5');
        BtnCari3.setToolTipText("Alt+5");
        BtnCari3.setName("BtnCari3"); // NOI18N
        BtnCari3.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnCari3ActionPerformed(evt);
            }
        });
        BtnCari3.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                BtnCari3KeyPressed(evt);
            }
        });
        panelisi5.add(BtnCari3);

        label12.setName("label12"); // NOI18N
        label12.setPreferredSize(new java.awt.Dimension(100, 30));
        panelisi5.add(label12);

        BtnHapus2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus2.setMnemonic('H');
        BtnHapus2.setText("Hapus");
        BtnHapus2.setToolTipText("Alt+H");
        BtnHapus2.setName("BtnHapus2"); // NOI18N
        BtnHapus2.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnHapus2ActionPerformed(evt);
            }
        });
        BtnHapus2.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                BtnHapus2KeyPressed(evt);
            }
        });
        panelisi5.add(BtnHapus2);

        BtnAll1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll1.setMnemonic('M');
        BtnAll1.setText("Semua");
        BtnAll1.setToolTipText("Alt+M");
        BtnAll1.setName("BtnAll1"); // NOI18N
        BtnAll1.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnAll1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnAll1ActionPerformed(evt);
            }
        });
        BtnAll1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                BtnAll1KeyPressed(evt);
            }
        });
        panelisi5.add(BtnAll1);

        BtnPrint2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint2.setMnemonic('T');
        BtnPrint2.setText("Cetak");
        BtnPrint2.setToolTipText("Alt+T");
        BtnPrint2.setName("BtnPrint2"); // NOI18N
        BtnPrint2.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnPrint2ActionPerformed(evt);
            }
        });
        BtnPrint2.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                BtnPrint2KeyPressed(evt);
            }
        });
        panelisi5.add(BtnPrint2);

        BtnKeluar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar2.setMnemonic('K');
        BtnKeluar2.setText("Keluar");
        BtnKeluar2.setToolTipText("Alt+K");
        BtnKeluar2.setName("BtnKeluar2"); // NOI18N
        BtnKeluar2.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnKeluar2ActionPerformed(evt);
            }
        });
        BtnKeluar2.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                BtnKeluar2KeyPressed(evt);
            }
        });
        panelisi5.add(BtnKeluar2);

        internalFrame3.add(panelisi5, java.awt.BorderLayout.PAGE_END);

        panelisi3.add(internalFrame3, java.awt.BorderLayout.CENTER);

        tabPane.addTab("List Transaksi", panelisi3);

        internalFrame1.add(tabPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PenjabKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PenjabKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PenjabKeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        
    }//GEN-LAST:event_formWindowOpened

    private void ppBersihkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppBersihkanActionPerformed
        for (i = 0; i < tbPemeriksaan.getRowCount(); i++)
        {
            tbPemeriksaan.setValueAt(false, i, 0);
        }
    }//GEN-LAST:event_ppBersihkanActionPerformed

    private void ppSemuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppSemuaActionPerformed
        for (i = 0; i < tbPemeriksaan.getRowCount(); i++)
        {
            tbPemeriksaan.setValueAt(true, i, 0);
        }
    }//GEN-LAST:event_ppSemuaActionPerformed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        jml = 0;

        for (i = 0; i < tbTarif.getRowCount(); i++)
        {
            if (tbTarif.getValueAt(i, 0).toString().equals("true"))
            {
                jml++;
            }
        }

        for (i = 0; i < tbPemeriksaan.getRowCount(); i++)
        {
            if (tbPemeriksaan.getValueAt(i, 0).toString().equals("true"))
            {
                jml++;
            }
        }

        if (TNoRw.getText().equals("") || TNoRM.getText().equals("") || TPasien.getText().equals(""))
        {
            Valid.textKosong(TNoRw, "Pasien");
        }
        else if (KdPtg.getText().equals("") || NmPtg.getText().equals(""))
        {
            Valid.textKosong(KdPtg, "Petugas");
        }
        else if (KodePerujuk.getText().equals("") || NmPerujuk.getText().equals(""))
        {
            Valid.textKosong(KodePerujuk, "Dokter Perujuk");
        }
        else if (KodePj.getText().equals("") || NmDokterPj.getText().equals(""))
        {
            Valid.textKosong(KodePj, "Dokter Penanggung Jawab");
        }
        else if (mdlPemeriksaan.getRowCount() == 0)
        {
            Valid.textKosong(Pemeriksaan, "Data Pemeriksaan");
        }
        else if (jml == 0)
        {
            Valid.textKosong(Pemeriksaan, "Data Pemeriksaan");
        }
        else if (Sequel.cariRegistrasi(TNoRw.getText()) > 0)
        {
            JOptionPane.showMessageDialog(rootPane, "Data billing sudah terverifikasi, data tidak boleh dihapus.\nSilahkan hubungi bagian kasir/keuangan ..!!");
            Pemeriksaan.requestFocus();
        }
        else // if (var.getkode().equals("Admin Utama")) MBOH GO PO RA GRT
        {
            int reply = JOptionPane.showConfirmDialog(rootPane, "Eeiiiiiits, udah bener belum data yang mau disimpan..??", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION)
            {
                ChkJln.setSelected(false);
                sukses = true;

                try
                {
                    koneksi.setAutoCommit(false);

                    pssimpanperiksa = koneksi.prepareStatement("UPDATE periksa_lab SET nip = ?, tgl_periksa = ?, jam = ?, dokter_perujuk = ?, kd_dokter = ?, proses = ?" + 
                            " WHERE id_periksa = ?");

                    pssimpanperiksa.setString(1, KdPtg.getText());
                    pssimpanperiksa.setString(2, Valid.SetTgl(Tanggal.getSelectedItem() + ""));
                    pssimpanperiksa.setString(3, CmbJam.getSelectedItem() + ":" + CmbMenit.getSelectedItem() + ":" + CmbDetik.getSelectedItem());
                    pssimpanperiksa.setString(4, KodePerujuk.getText());
                    pssimpanperiksa.setString(5, KodePj.getText());
                    pssimpanperiksa.setString(6, "Sudah");
                    pssimpanperiksa.setString(7, _idPeriksa);
                    pssimpanperiksa.executeUpdate();

                    // Hapus dulu semua detailnya
                    new GQuery()
                            .a("DELETE FROM detail_periksa_lab WHERE id_periksa = {id_periksa}")
                            .set("id_periksa", _idPeriksa)
                            .write();
                    
                    // Simpan detail 1
                    for (int a = 0; a < tbTarif.getRowCount(); a++)
                    {
                        if (tbTarif.getValueAt(a, 0).toString().equals("true"))
                        {
                            String idDetail = Sequel.autoNumber("detail_periksa_lab", "id_detail");
                            String kdJenisPrw = tbTarif.getValueAt(a, 1).toString();

                            psdetailpriksa = koneksi.prepareStatement("insert into detail_periksa_lab values(?,?,?)");

                            psdetailpriksa.setString(1, idDetail);
                            psdetailpriksa.setString(2, _idPeriksa);
                            psdetailpriksa.setString(3, kdJenisPrw);
                            psdetailpriksa.executeUpdate();

                            // Simpan Detail 2
                            for (int b = 0; b < tblDetail.getRowCount(); b++)
                            {
                                if (tblDetail.getValueAt(b, 7).toString().equals(kdJenisPrw) &&
                                    tblDetail.getValueAt(b, 8).toString().equals("Atas"))
                                {
                                    String idDetail2 = Sequel.autoNumber("detail_periksa_lab_2", "id_detail");
                                    String idTemplate = tblDetail.getValueAt(b, 6).toString();

                                    // Ambil tarif sesuai kelas
                                    String[] tarifs = new GQuery()
                                    .a("SELECT bagian_rs, bhp, bagian_perujuk, bagian_dokter, bagian_laborat, kso, menejemen, biaya_item")
                                    .a("FROM tarif_lab")
                                    .a("WHERE id_template = {idTemplate} AND kelas = {kelas}")
                                    .set("idTemplate", idTemplate)
                                    .set("kelas", kelas)
                                    .getRow();

                                    // Nek gak ada tarif gak usah simpen aja
                                    if (tarifs == null)
                                    {
                                        continue;
                                    }

                                    psDetailPeriksa2 = koneksi.prepareStatement("insert into detail_periksa_lab_2 values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

                                    psDetailPeriksa2.setString(1, idDetail2);
                                    psDetailPeriksa2.setString(2, idDetail);
                                    psDetailPeriksa2.setString(3, idTemplate);
                                    psDetailPeriksa2.setString(4, tblDetail.getValueAt(b, 2).toString());
                                    psDetailPeriksa2.setString(5, tblDetail.getValueAt(b, 4).toString());
                                    psDetailPeriksa2.setString(6, tblDetail.getValueAt(b, 5).toString());
                                    psDetailPeriksa2.setString(7, tarifs[0]);
                                    psDetailPeriksa2.setString(8, tarifs[1]);
                                    psDetailPeriksa2.setString(9, tarifs[2]);
                                    psDetailPeriksa2.setString(10, tarifs[3]);
                                    psDetailPeriksa2.setString(11, tarifs[4]);
                                    psDetailPeriksa2.setString(12, tarifs[5]);
                                    psDetailPeriksa2.setString(13, tarifs[6]);
                                    psDetailPeriksa2.setString(14, tarifs[7]);
                                    psDetailPeriksa2.executeUpdate();
                                    
                                    // Detail 3
                                    for (int c = 0; c < tblDetail.getRowCount(); c++)
                                    {
                                        if (tblDetail.getValueAt(c, 7).toString().equals(idTemplate) &&
                                            tblDetail.getValueAt(c, 8).toString().equals("Bawah"))
                                        {
                                            String idDetail3 = Sequel.autoNumber("detail_periksa_lab_3", "id_detail");
                                            String idDetTemplate = tblDetail.getValueAt(c, 6).toString();
                                            String hasil = tblDetail.getValueAt(c, 2).toString();
                                            String ruj = tblDetail.getValueAt(c, 4).toString();
                                            String ket = tblDetail.getValueAt(c, 5).toString();

                                            psDetailPeriksa3 = koneksi.prepareStatement("INSERT INTO detail_periksa_lab_3 VALUES(?,?,?,?,?,?)");

                                            psDetailPeriksa3.setString(1, idDetail3);
                                            psDetailPeriksa3.setString(2, idDetail2);
                                            psDetailPeriksa3.setString(3, idDetTemplate);
                                            psDetailPeriksa3.setString(4, hasil);
                                            psDetailPeriksa3.setString(5, ruj);
                                            psDetailPeriksa3.setString(6, ket);
                                            psDetailPeriksa3.executeUpdate();
                                        }
                                    }
                                }
                            }
                        }
                    }

                    koneksi.setAutoCommit(true);
                }
                catch (Exception e)
                {
                    System.out.println(e);
                    sukses = false;
                }

                if (sukses)
                {
                    JOptionPane.showMessageDialog(null, "Proses simpan selesai...!");
                    clearSemua();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Proses simpan GGL!");
                }
            }
        }
    }//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            BtnSimpanActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, Pemeriksaan, BtnBatal);
        }
    }//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        clearSemua();
    }//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            emptTeks();
        }
        else
        {
            Valid.pindah(evt, BtnSimpan, BtnHapus);
        }
    }//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        try
        {
            for (i = 0; i < tbPemeriksaan.getRowCount(); i++)
            {
                if (tbPemeriksaan.getValueAt(i, 0).toString().equals("true"))
                {
                    mdlPemeriksaan.removeRow(i);
                }
            }
        }
        catch (Exception ex)
        {
        }
    }//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            BtnHapusActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, BtnBatal, BtnPrint);
        }
    }//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        jml = 0;
        for (i = 0; i < tbTarif.getRowCount(); i++)
        {
            if (tbTarif.getValueAt(i, 0).toString().equals("true"))
            {
                jml++;
            }
        }
        for (i = 0; i < tbPemeriksaan.getRowCount(); i++)
        {
            if (tbPemeriksaan.getValueAt(i, 0).toString().equals("true"))
            {
                jml++;
            }
        }
        if (TNoRw.getText().equals("") || TNoRM.getText().equals("") || TPasien.getText().equals(""))
        {
            Valid.textKosong(TNoRw, "Pasien");
        }
        else if (KdPtg.getText().equals("") || NmPtg.getText().equals(""))
        {
            Valid.textKosong(KdPtg, "Petugas");
        }
        else if (KodePerujuk.getText().equals("") || NmPerujuk.getText().equals(""))
        {
            Valid.textKosong(KodePerujuk, "Dokter Pengirim");
        }
        else if (KodePj.getText().equals("") || NmDokterPj.getText().equals(""))
        {
            Valid.textKosong(KodePj, "Dokter Penanggung Jawab");
        }
        else if (mdlPemeriksaan.getRowCount() == 0)
        {
            Valid.textKosong(Pemeriksaan, "Data Pemeriksaan");
        }
        else if (jml == 0)
        {
            Valid.textKosong(Pemeriksaan, "Data Pemeriksaan");
        }
        else
        {
            Sequel.AutoComitFalse();
            Sequel.queryu("delete from temporary");
            for (i = 0; i < tbPemeriksaan.getRowCount(); i++)
            {
                if (tbPemeriksaan.getValueAt(i, 0).toString().equals("true"))
                {
                    Sequel.menyimpan("temporary", "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?", 38, new String[]
                        {
                            "0", tbPemeriksaan.getValueAt(i, 1).toString(),
                            tbPemeriksaan.getValueAt(i, 2).toString(),
                            tbPemeriksaan.getValueAt(i, 3).toString(),
                            tbPemeriksaan.getValueAt(i, 4).toString(),
                            tbPemeriksaan.getValueAt(i, 5).toString(), "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""
                        });
                    }
                }
                Sequel.AutoComitTrue();
                Map<String, Object> param = new HashMap<>();
                param.put("noperiksa", TNoRw.getText());
                param.put("norm", TNoRM.getText());
                param.put("namapasien", TPasien.getText());
                param.put("jkel", Jk.getText());
                param.put("umur", Umur.getText());
                param.put("lahir", Sequel.cariIsi("select DATE_FORMAT(tgl_lahir,'%d-%m-%Y') from pasien where no_rkm_medis=? ", TNoRM.getText()));
                param.put("pengirim", NmPerujuk.getText());
                param.put("tanggal", Tanggal.getSelectedItem());
                param.put("penjab", NmDokterPj.getText());
                param.put("petugas", NmPtg.getText());
                param.put("alamat", Alamat.getText());
                param.put("kamar", kamar);
                param.put("namakamar", namakamar);
                param.put("jam", CmbJam.getSelectedItem() + ":" + CmbMenit.getSelectedItem() + ":" + CmbDetik.getSelectedItem());
                param.put("namars", var.getnamars());
                param.put("alamatrs", var.getalamatrs());
                param.put("kotars", var.getkabupatenrs());
                param.put("propinsirs", var.getpropinsirs());
                param.put("kontakrs", var.getkontakrs());
                param.put("emailrs", var.getemailrs());
                param.put("logo", Sequel.cariGambar("select logo from setting"));

                pilihan = (String) JOptionPane.showInputDialog(null, "Silahkan pilih hasil pemeriksaan..!", "Hasil Pemeriksaan", JOptionPane.QUESTION_MESSAGE, null, new Object[]
                    {
                        "Model 1", "Model 2", "Model 3", "Model 4", "Model 5", "Model 6"
                    }, "Model 1");
                    switch (pilihan)
                    {
                        case "Model 1":
                        Valid.MyReport("rptPeriksaLab.jrxml", "report", "::[ Pemeriksaan Laboratorium ]::",
                            "select no, temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8, temp9, temp10, temp11, temp12, temp13, temp14, temp14, temp15, temp16 from temporary order by no asc", param);
                        break;
                        case "Model 2":
                        Valid.MyReport("rptPeriksaLab2.jrxml", "report", "::[ Pemeriksaan Laboratorium ]::",
                            "select no, temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8, temp9, temp10, temp11, temp12, temp13, temp14, temp14, temp15, temp16 from temporary order by no asc", param);
                        break;
                        case "Model 3":
                        Valid.MyReport("rptPeriksaLab3.jrxml", "report", "::[ Pemeriksaan Laboratorium ]::",
                            "select no, temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8, temp9, temp10, temp11, temp12, temp13, temp14, temp14, temp15, temp16 from temporary order by no asc", param);
                        break;
                        case "Model 4":
                        Valid.MyReport("rptPeriksaLab4.jrxml", "report", "::[ Pemeriksaan Laboratorium ]::",
                            "select no, temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8, temp9, temp10, temp11, temp12, temp13, temp14, temp14, temp15, temp16 from temporary order by no asc", param);
                        break;
                        case "Model 5":
                        Valid.MyReport("rptPeriksaLab5.jrxml", "report", "::[ Pemeriksaan Laboratorium ]::",
                            "select no, temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8, temp9, temp10, temp11, temp12, temp13, temp14, temp14, temp15, temp16 from temporary order by no asc", param);
                        break;
                        case "Model 6":
                        Valid.MyReport("rptPeriksaLab6.jrxml", "report", "::[ Pemeriksaan Laboratorium ]::",
                            "select no, temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8, temp9, temp10, temp11, temp12, temp13, temp14, temp14, temp15, temp16 from temporary order by no asc", param);
                        break;
                    }

                    ChkJln.setSelected(false);
                }
                this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            BtnPrintActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, BtnHapus, BtnCari);
        }
    }//GEN-LAST:event_BtnPrintKeyPressed

    private void BtnNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnNotaActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (mdlPemeriksaan.getRowCount() == 0)
        {
            JOptionPane.showMessageDialog(null, "Maaf, data sudah habis...!!!!");
            Pemeriksaan.requestFocus();
        }
        else
        {
            Sequel.queryu("truncate table temporary");
            ttl = 0;
            for (i = 0; i < tbTarif.getRowCount(); i++)
            {
                if (tbTarif.getValueAt(i, 0).toString().equals("true"))
                {
                    item = Double.parseDouble(tbTarif.getValueAt(i, 3).toString());
                    ttl = ttl + item;
                    Sequel.menyimpan("temporary", "'0','" + tbTarif.getValueAt(i, 1).toString() + "','" + tbTarif.getValueAt(i, 2).toString() + "','" + tbTarif.getValueAt(i, 3).toString() + "','Pemeriksaan','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',''", "Transaksi Biaya Lab");
                }
            }
            for (i = 0; i < tbPemeriksaan.getRowCount(); i++)
            {
                if (tbPemeriksaan.getValueAt(i, 0).toString().equals("true"))
                {
                    try
                    {
                        item = Double.parseDouble(tbPemeriksaan.getValueAt(i, 7).toString());
                    }
                    catch (Exception e)
                    {
                        item = 0;
                    }
                    ttl = ttl + item;
                    Sequel.menyimpan("temporary", "'0','" + Sequel.cariIsi("select kd_jenis_prw from template_laboratorium where id_template=?", tbPemeriksaan.getValueAt(i, 6).toString()) + "','" + tbPemeriksaan.getValueAt(i, 1).toString() + "','" + item + "','Detail Pemeriksaan','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',''", "Transaksi Biaya Lab");
                }
            }
            Sequel.menyimpan("temporary", "'0','','Total Biaya Pemeriksaan Lab','" + ttl + "','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',''", "Transaksi Biaya Lab");
            Valid.panggilUrl("billing/LaporanBiayaLab.php?norm=" + TNoRM.getText() + "&pasien=" + TPasien.getText().replaceAll(" ", "_") + "&tanggal=" + Tanggal.getSelectedItem() + "&jam=" + CmbJam.getSelectedItem() + ":" + CmbMenit.getSelectedItem() + ":" + CmbDetik.getSelectedItem() + "&pjlab=" + NmDokterPj.getText().replaceAll(" ", "_") + "&petugas=" + NmPtg.getText().replaceAll(" ", "_") + "&kasir=" + Sequel.cariIsi("select nama from pegawai where nik=?", var.getkode()));
        }
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnNotaActionPerformed

    private void BtnNotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnNotaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            emptTeks();
        }
        else
        {
            Valid.pindah(evt, BtnCari, BtnKeluar);
        }
    }//GEN-LAST:event_BtnNotaKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        DlgCariPeriksaLab form = new DlgCariPeriksaLab(null, false);
        form.setPasien(TNoRw.getText());
        form.setSize(this.getWidth(), this.getHeight());
        form.setLocationRelativeTo(this);
        form.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            BtnCariActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, BtnPrint, BtnNota);
        }
    }//GEN-LAST:event_BtnCariKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            dispose();
        }
        else
        {
            Valid.pindah(evt, BtnNota, Pemeriksaan);
        }
    }//GEN-LAST:event_BtnKeluarKeyPressed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void PemeriksaanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PemeriksaanKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            tampilTarif(true);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            if (var.getperiksa_lab() == true)
            {
                btnTarifActionPerformed(null);
            }
        }
        else if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
        {
            BtnSimpan.requestFocus();
        }
        else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP)
        {
            Tanggal.requestFocus();
        }
    }//GEN-LAST:event_PemeriksaanKeyPressed

    private void KdPtgKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdPtgKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", NmPtg, KdPtg.getText());
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnPetugasActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, Pemeriksaan, KodePerujuk);
        }
    }//GEN-LAST:event_KdPtgKeyPressed

    private void btnPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPetugasActionPerformed
        var.setform("DlgPeriksaLaboratorium");
        petugas.emptTeks();
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth() - 40, internalFrame1.getHeight() - 40);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setVisible(true);
    }//GEN-LAST:event_btnPetugasActionPerformed

    private void TanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalKeyPressed
        Valid.pindah(evt, KodePerujuk, Pemeriksaan);
    }//GEN-LAST:event_TanggalKeyPressed

    private void ChkJlnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkJlnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChkJlnActionPerformed

    private void BtnCari1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCari1ActionPerformed
        isReset(true);
    }//GEN-LAST:event_BtnCari1ActionPerformed

    private void btnDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDokterActionPerformed
        pilihan = "perujuk";
        var.setform("DlgPeriksaLaboratorium");
        dokter.emptTeks();
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth() - 40, internalFrame1.getHeight() - 40);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_btnDokterActionPerformed

    private void tbTarifMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbTarifMouseClicked
        if (mdlTarif.getRowCount() != 0)
        {
            try
            {
                tampilTindakan(true);
                tampilDetail();
            }
            catch (java.lang.NullPointerException e)
            {
            }
        }
    }//GEN-LAST:event_tbTarifMouseClicked

    private void btnTarifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTarifActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        DlgJnsPerawatanLab tariflab = new DlgJnsPerawatanLab(null, false);
        tariflab.emptTeks();
        tariflab.isCek();
        tariflab.setSize(internalFrame1.getWidth() - 40, internalFrame1.getHeight() - 40);
        tariflab.setLocationRelativeTo(internalFrame1);
        tariflab.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_btnTarifActionPerformed

    private void rbAnakMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbAnakMouseClicked
        tbTarifMouseClicked(evt);
    }//GEN-LAST:event_rbAnakMouseClicked

    private void rbDewasaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbDewasaMouseClicked
        tbTarifMouseClicked(evt);
    }//GEN-LAST:event_rbDewasaMouseClicked

    private void KodePerujukKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KodePerujukKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
        {
            Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", NmPerujuk, KodePerujuk.getText());
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnDokterActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, KdPtg, Tanggal);
        }
    }//GEN-LAST:event_KodePerujukKeyPressed

    private void btnDokter1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDokter1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDokter1ActionPerformed

    private void btnDokterPjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDokterPjActionPerformed
        pilihan = "penjab";
        var.setform("DlgPeriksaLaboratorium");
        dokter.emptTeks();
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth() - 40, internalFrame1.getHeight() - 40);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_btnDokterPjActionPerformed

    private void tblOrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOrderMouseClicked
        
        if (evt.getClickCount() == 2 && tblOrder.getSelectedRow() != -1)
        {
            int r = tblOrder.getSelectedRow();
            
            for (int a = r; a >= 0; a--)
            {
                if (tblOrder.getValueAt(a, 8) != null)
                {
                    dariOrder(tblOrder.getValueAt(a, 8).toString());
                    break;
                }
            }
        }
    }//GEN-LAST:event_tblOrderMouseClicked

    private void tblOrderKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblOrderKeyPressed
        if(mdlPemeriksaan.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tblOrderKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
    }//GEN-LAST:event_TCariKeyPressed

    private void BtnCari2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCari2ActionPerformed
        tampilOrder();
    }//GEN-LAST:event_BtnCari2ActionPerformed

    private void BtnCari2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCari2KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
    }//GEN-LAST:event_BtnCari2KeyPressed

    private void BtnHapus1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapus1ActionPerformed
        if (tblOrder.getSelectedRow() != -1)
        {
            int r = tblOrder.getSelectedRow();
            
            for (int a = r; a >= 0; a--)
            {
                Object p = tblOrder.getValueAt(a, 7);
                
                if (p == null || p.toString().isEmpty())
                {
                    continue;
                }
                else if (p.toString().equals("Belum"))
                {
                    if (GMessage.q("Hapus", "Yakin mau menhapsus???"))
                    {
                        boolean b = new GQuery()
                                .a("DELETE FROM periksa_lab WHERE id_periksa = {id_periksa}")
                                .set("id_periksa", tblOrder.getValueAt(a, 8).toString())
                                .write();
                        
                        if (b)
                        {
                            tampilTransksi();
                        }
                        else
                        {
                            GMessage.e("Gagal hapus", "Gagallllll");
                        }
                    }
                    
                    break;
                }
                else if (p.toString().equals("Sudah"))
                {
                    GMessage.e("Gak Bener", "Data gak bolek dihapus karena udah diproses");
                    break;
                }
            }
        }
    }//GEN-LAST:event_BtnHapus1ActionPerformed

    private void BtnHapus1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapus1KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari,BtnAll);
        }
    }//GEN-LAST:event_BtnHapus1KeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed

    }//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnKeluar);
        }
    }//GEN-LAST:event_BtnAllKeyPressed

    private void BtnPrint1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrint1ActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        BtnCariActionPerformed(evt);
        if(mdlPemeriksaan.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            TCari.requestFocus();
        }else if(mdlPemeriksaan.getRowCount()!=0){
            Sequel.AutoComitFalse();
            Sequel.queryu("delete from temporary");
            int row=mdlPemeriksaan.getRowCount();
            for(i=0;i<row;i++){
                Sequel.menyimpan("temporary","'0','"+
                    mdlPemeriksaan.getValueAt(i,0).toString()+"','"+
                    mdlPemeriksaan.getValueAt(i,1).toString()+"','"+
                    mdlPemeriksaan.getValueAt(i,2).toString()+"','"+
                    mdlPemeriksaan.getValueAt(i,3).toString()+"','"+
                    mdlPemeriksaan.getValueAt(i,4).toString()+"','"+
                    mdlPemeriksaan.getValueAt(i,5).toString()+"','"+
                    mdlPemeriksaan.getValueAt(i,6).toString()+"','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',''","Periksa Lab");
            }
            Sequel.AutoComitTrue();
            Map<String, Object> param = new HashMap<>();
            param.put("namars",var.getnamars());
            param.put("alamatrs",var.getalamatrs());
            param.put("kotars",var.getkabupatenrs());
            param.put("propinsirs",var.getpropinsirs());
            param.put("kontakrs",var.getkontakrs());
            param.put("emailrs",var.getemailrs());
            param.put("logo",Sequel.cariGambar("select logo from setting"));
            Valid.MyReport("rptDataLab.jrxml","report","::[ Data Pemeriksaan Laboratorium ]::",
                "select no, temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8, temp9, temp10, temp11, temp12, temp13, temp14, temp14, temp15, temp16 from temporary order by no asc",param);
        }
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnPrint1ActionPerformed

    private void BtnPrint1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrint1KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt,BtnAll,BtnAll);
        }
    }//GEN-LAST:event_BtnPrint1KeyPressed

    private void BtnKeluar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluar1ActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluar1ActionPerformed

    private void BtnKeluar1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluar1KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,BtnPrint,NoRawat);}
    }//GEN-LAST:event_BtnKeluar1KeyPressed

    private void tblTransaksiMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblTransaksiMouseClicked
    {//GEN-HEADEREND:event_tblTransaksiMouseClicked
        if (evt.getClickCount() == 2 && tblTransaksi.getSelectedRow() != -1)
        {
            int r = tblTransaksi.getSelectedRow();
            
            for (int a = r; a >= 0; a--)
            {
                if (tblTransaksi.getValueAt(a, 8) != null)
                {
                    dariTransaksi(tblTransaksi.getValueAt(a, 8).toString());
                    break;
                }
            }
        }
    }//GEN-LAST:event_tblTransaksiMouseClicked

    private void tblTransaksiKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_tblTransaksiKeyPressed
    {//GEN-HEADEREND:event_tblTransaksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTransaksiKeyPressed

    private void TCari1KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_TCari1KeyPressed
    {//GEN-HEADEREND:event_TCari1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TCari1KeyPressed

    private void BtnCari3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BtnCari3ActionPerformed
    {//GEN-HEADEREND:event_BtnCari3ActionPerformed
        tampilTransksi();
    }//GEN-LAST:event_BtnCari3ActionPerformed

    private void BtnCari3KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_BtnCari3KeyPressed
    {//GEN-HEADEREND:event_BtnCari3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCari3KeyPressed

    private void BtnHapus2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BtnHapus2ActionPerformed
    {//GEN-HEADEREND:event_BtnHapus2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnHapus2ActionPerformed

    private void BtnHapus2KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_BtnHapus2KeyPressed
    {//GEN-HEADEREND:event_BtnHapus2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnHapus2KeyPressed

    private void BtnAll1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BtnAll1ActionPerformed
    {//GEN-HEADEREND:event_BtnAll1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnAll1ActionPerformed

    private void BtnAll1KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_BtnAll1KeyPressed
    {//GEN-HEADEREND:event_BtnAll1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnAll1KeyPressed

    private void BtnPrint2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BtnPrint2ActionPerformed
    {//GEN-HEADEREND:event_BtnPrint2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnPrint2ActionPerformed

    private void BtnPrint2KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_BtnPrint2KeyPressed
    {//GEN-HEADEREND:event_BtnPrint2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnPrint2KeyPressed

    private void BtnKeluar2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BtnKeluar2ActionPerformed
    {//GEN-HEADEREND:event_BtnKeluar2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnKeluar2ActionPerformed

    private void BtnKeluar2KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_BtnKeluar2KeyPressed
    {//GEN-HEADEREND:event_BtnKeluar2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnKeluar2KeyPressed

    private void tbPemeriksaanPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_tbPemeriksaanPropertyChange
    {//GEN-HEADEREND:event_tbPemeriksaanPropertyChange
        if (evt.getPropertyName().equals("tableCellEditor"))
        {
            tampilDetail();
        }
    }//GEN-LAST:event_tbPemeriksaanPropertyChange

    private void ppCetakHasilActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ppCetakHasilActionPerformed
    {//GEN-HEADEREND:event_ppCetakHasilActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        if (mdlTransaksi.getRowCount() == 0)
        {
            JOptionPane.showMessageDialog(null, "Maaf, data sudah habis...!!!!");
            TCari.requestFocus();
        }
        else if (tblTransaksi.getSelectedRow() == -1)
        {
            JOptionPane.showMessageDialog(null, "Maaf, Gagal mencteak. Pilih dulu data yang mau dicetak.");
        }
        else
        {
            int r = tblTransaksi.getSelectedRow();
            String idPeriksa = null;
            
            // Mencari row utama di tiap transaksi
            for (int a = r; a >= 0; a--)
            {
                if (!tblTransaksi.getValueAt(a, 8).toString().isEmpty())
                {
                    idPeriksa = tblTransaksi.getValueAt(a, 8).toString();
                    break;
                }
            }
            
            // Kalo gak nemu id periksa
            if (idPeriksa == null)
            {
                JOptionPane.showMessageDialog(null, "Pemeriksaan tidak ditemukan");
                return;
            }
            
            HashMap<String, String> mMain = new GQuery()
                    .a("SELECT periksa_lab.no_rawat, reg_periksa.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.umur, petugas.nama,")
                    .a("    DATE_FORMAT(periksa_lab.tgl_periksa,'%d-%m-%Y') as tgl_periksa, periksa_lab.jam,")
                    .a("    dokter_perujuk.nm_dokter AS nm_dokter_perujuk, dokter_pj.nm_dokter AS nm_dokter_pj, pasien.alamat, DATE_FORMAT(pasien.tgl_lahir,'%d-%m-%Y') as tgl_lahir")
                    .a("FROM periksa_lab")
                    .a("JOIN reg_periksa ON reg_periksa.no_rawat = periksa_lab.no_rawat")
                    .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                    .a("JOIN petugas ON petugas.nip = periksa_lab.nip")
                    .a("JOIN dokter dokter_perujuk ON dokter_perujuk.kd_dokter = periksa_lab.dokter_perujuk")
                    .a("JOIN dokter dokter_pj ON dokter_pj.kd_dokter = periksa_lab.kd_dokter")
                    .a("WHERE id_periksa = {id_periksa}")
                    .set("id_periksa", idPeriksa)
                    .getRowWithName();
            
            if (mMain != null)
            {
                kamar = Sequel.cariIsi("select ifnull(kd_kamar,'') from kamar_inap where no_rawat='" + mMain.get("no_rawat") + "' order by tgl_masuk desc limit 1");
                
                if (!kamar.equals(""))
                {
                    namakamar = kamar + ", " + Sequel.cariIsi("select nm_bangsal from bangsal inner join kamar on bangsal.kd_bangsal=kamar.kd_bangsal "
                            + " where kamar.kd_kamar='" + kamar + "' ");
                    
                    kamar = "Kamar";
                }
                else if (kamar.equals(""))
                {
                    kamar = "Poli";
                    namakamar = Sequel.cariIsi("select nm_poli from poliklinik inner join reg_periksa on poliklinik.kd_poli=reg_periksa.kd_poli "
                            + "where reg_periksa.no_rawat='" + mMain.get("no_rawat") + "'");
                }
                
                // Mengisi RAPOT atas
                Map<String, Object> param = new HashMap<>();
                param.put("noperiksa", mMain.get("no_rawat"));
                param.put("norm", mMain.get("no_rkm_medis"));
                param.put("namapasien", mMain.get("nm_pasien"));
                param.put("jkel", mMain.get("jk"));
                param.put("umur", mMain.get("umur"));
                param.put("pengirim", mMain.get("nm_dokter_perujuk"));
                param.put("tanggal", mMain.get("tgl_periksa"));
                param.put("penjab", mMain.get("nm_dokter_pj"));
                param.put("petugas", mMain.get("nama"));
                param.put("jam", mMain.get("jam"));
                param.put("alamat", mMain.get("Alamat"));
                param.put("kamar", kamar);
                param.put("namakamar", namakamar);
                
                // Dari GLOBAL setting
                param.put("namars", var.getnamars());
                param.put("alamatrs", var.getalamatrs());
                param.put("kotars", var.getkabupatenrs());
                param.put("propinsirs", var.getpropinsirs());
                param.put("kontakrs", var.getkontakrs());
                param.put("emailrs", var.getemailrs());
                param.put("logo", Sequel.cariGambar("select logo from setting"));

                Sequel.AutoComitFalse();
                Sequel.queryu("delete from temporary");
                
                // Ngambil detail 1
                List<HashMap<String, String>> mDetail1 = new GQuery()
                        .a("SELECT id_detail, nm_perawatan FROM detail_periksa_lab")
                        .a("JOIN jns_perawatan_lab ON jns_perawatan_lab.kd_jenis_prw = detail_periksa_lab.kd_jenis_prw")
                        .a("WHERE id_periksa = {id_periksa}")
                        .set("id_periksa", idPeriksa)
                        .selectWithName();
                
                for (HashMap<String, String> m1 : mDetail1)
                {
                    Sequel.menyimpan("temporary", "'0','" + m1.get("nm_perawatan") + "','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',''", "Data User");
                    
                    // Ngambil detail 2
                    List<HashMap<String, String>> mDetail2 = new GQuery()
                            .a("SELECT id_detail, pemeriksaan, nilai, satuan, nilai_rujukan, keterangan")
                            .a("FROM detail_periksa_lab_2")
                            .a("JOIN template_laboratorium ON template_laboratorium.id_template = detail_periksa_lab_2.id_template")
                            .a("WHERE id_detail_1 = {id_detail}")
                            .set("id_detail", m1.get("id_detail"))
                            .selectWithName();
                    
                    for (HashMap<String, String> m2 : mDetail2)
                    {
                        Sequel.menyimpan("temporary", "'0','  + " + m2.get("pemeriksaan") + "','" + m2.get("nilai") + "','" + m2.get("satuan")
                                + "','" + m2.get("nilai_rujukan") + "','" + m2.get("keterangan") 
                                + "','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',''", "Data User");
                        
                        // Ngambil detail 3
                        List<HashMap<String, String>> mDetail3 = new GQuery()
                                .a("SELECT pemeriksaan, nilai, satuan, nilai_rujukan, keterangan")
                                .a("FROM detail_periksa_lab_3")
                                .a("JOIN template_laboratorium_det ON template_laboratorium_det.id_det_template = detail_periksa_lab_3.id_det_template")
                                .a("WHERE id_detail_2 = {id_detail}")
                                .set("id_detail", m2.get("id_detail"))
                                .selectWithName();
                        
                        for (HashMap<String, String> m3 : mDetail3)
                        {
                            Sequel.menyimpan("temporary", "'0','     - " + m3.get("pemeriksaan") + "','" + m3.get("nilai") + "','" + m3.get("satuan")
                                    + "','" + m3.get("nilai_rujukan") + "','" + m3.get("keterangan") 
                                    + "','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',''", "Data User");
                        }
                    }
                }
                
                Sequel.AutoComitTrue();
                
                
                Valid.MyReport("rptPeriksaLab.jrxml", "report", "::[ Pemeriksaan Laboratorium ]::",
                        "select no, temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8, temp9, temp10, temp11, temp12, temp13, temp14, temp14, temp15, temp16 from temporary order by no asc", param);

            }
        }
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_ppCetakHasilActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(() -> 
                {
                    DlgPemeriksaanLaboratorium dialog = new DlgPemeriksaanLaboratorium(new javax.swing.JFrame(), true);
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
    private widget.Button BtnAll;
    private widget.Button BtnAll1;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnCari1;
    private widget.Button BtnCari2;
    private widget.Button BtnCari3;
    private widget.Button BtnHapus;
    private widget.Button BtnHapus1;
    private widget.Button BtnHapus2;
    private widget.Button BtnKeluar;
    private widget.Button BtnKeluar1;
    private widget.Button BtnKeluar2;
    private widget.Button BtnNota;
    private widget.Button BtnPrint;
    private widget.Button BtnPrint1;
    private widget.Button BtnPrint2;
    private widget.Button BtnSimpan;
    private widget.CekBox ChkInput;
    private widget.CekBox ChkJln;
    private widget.ComboBox CmbDetik;
    private widget.ComboBox CmbJam;
    private widget.ComboBox CmbMenit;
    private javax.swing.JPanel FormInput;
    private widget.TextBox Jk;
    private widget.TextBox KdPtg;
    private widget.TextBox KodePerujuk;
    private widget.TextBox KodePj;
    private widget.TextBox NmDokterPj;
    private widget.TextBox NmPerujuk;
    private widget.TextBox NmPtg;
    private widget.PanelBiasa PanelInput;
    private widget.TextBox Pemeriksaan;
    private widget.TextBox Penjab;
    private javax.swing.JPopupMenu Popup;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll1;
    private widget.ScrollPane Scroll2;
    private widget.TextBox TCari;
    private widget.TextBox TCari1;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.Tanggal Tanggal;
    private widget.TextBox Umur;
    private widget.Button btnDokter;
    private widget.Button btnDokter1;
    private widget.Button btnDokterPj;
    private widget.Button btnPetugas;
    private widget.Button btnTarif;
    private javax.swing.ButtonGroup buttonGroup1;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame3;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel12;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel3;
    private widget.Label jLabel33;
    private widget.Label jLabel34;
    private widget.Label jLabel35;
    private widget.Label jLabel36;
    private widget.Label jLabel7;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel1;
    private widget.Label label10;
    private widget.Label label11;
    private widget.Label label12;
    private widget.Label label9;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelisi1;
    private widget.panelisi panelisi2;
    private widget.panelisi panelisi3;
    private widget.panelisi panelisi4;
    private widget.panelisi panelisi5;
    private javax.swing.JMenuItem ppBersihkan;
    private javax.swing.JMenuItem ppCetakHasil;
    private javax.swing.JMenuItem ppSemua;
    private widget.RadioButton rbAnak;
    private widget.RadioButton rbDewasa;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollPane2;
    private widget.TabPane tabPane;
    private widget.Table tbPemeriksaan;
    private widget.Table tbTarif;
    private widget.Table tblDetail;
    private widget.Table tblOrder;
    private widget.Table tblTransaksi;
    private widget.Tanggal tglTransaksi1;
    private widget.Tanggal tglTransaksi2;
    private widget.Tanggal tglTransaksi3;
    private widget.Tanggal tglTransaksi4;
    // End of variables declaration//GEN-END:variables

    public void tampilTindakan(boolean isAll)
    {
        Valid.tabelKosong(mdlPemeriksaan);
        
        try
        {
            for (i = 0; i < tbTarif.getRowCount(); i++)
            {
                if (tbTarif.getValueAt(i, 0).toString().equals("true"))
                {
                    mdlPemeriksaan.addRow(new Object[]
                    {
                        true, tbTarif.getValueAt(i, 2).toString(), "", "", "", "", "", ""
                    });
                    
                    pstampil = koneksi.prepareStatement("select id_template, kd_jenis_prw, Pemeriksaan "
                            + "from template_laboratorium where kd_jenis_prw=? order by urut");

                    pstampil.setString(1, tbTarif.getValueAt(i, 1).toString());
                    rstampil = pstampil.executeQuery();

                    // Ngitung jumlah pemeriksaan yg dicentang
                    int jums = 0;
                    
                    while (rstampil.next())
                    {
                        if (idTemplates.contains(rstampil.getString("id_template")))
                        {
                            jums++;
                            
                            mdlPemeriksaan.addRow(new Object[]
                            {
                                true, 
                                "   + " + rstampil.getString("Pemeriksaan"),
                                rstampil.getString("id_template"),
                                rstampil.getString("kd_jenis_prw")
                            });
                        }
                    }
                    
                    // Kalo jumlah yg dicentang gak ada hapus aja kategorinya
                    if (jums == 0)
                    {
                        mdlPemeriksaan.removeRow(mdlPemeriksaan.getRowCount() - 1);
                    }
                }
            }
            
            // Buta nanpilin semuaw
            if (isAll)
            {
                for (i = 0; i < tbTarif.getRowCount(); i++)
                {
                    if (tbTarif.getValueAt(i, 0).toString().equals("true"))
                    {
                        mdlPemeriksaan.addRow(new Object[]
                        {
                            false, tbTarif.getValueAt(i, 2).toString(), "", "", "", "", "", ""
                        });

                        pstampil = koneksi.prepareStatement("select id_template, kd_jenis_prw, Pemeriksaan "
                                + "from template_laboratorium where kd_jenis_prw=? order by urut");

                        pstampil.setString(1, tbTarif.getValueAt(i, 1).toString());
                        rstampil = pstampil.executeQuery();

                        while (rstampil.next())
                        {
                            if (!idTemplates.contains(rstampil.getString("id_template")))
                            {
                                mdlPemeriksaan.addRow(new Object[]
                                {
                                    false, 
                                    "   + " + rstampil.getString("Pemeriksaan"),
                                    rstampil.getString("id_template"),
                                    rstampil.getString("kd_jenis_prw")
                                });
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }
    
    public void tampilDetail()
    {
        Valid.tabelKosong(mdlDetail);
        
        try
        {
            for (i = 0; i < tbPemeriksaan.getRowCount(); i++)
            {
                if (tbPemeriksaan.getValueAt(i, 0).toString().equals("true") &&
                        !tbPemeriksaan.getValueAt(i, 2).toString().isEmpty())
                {
                    String idTemplate = tbPemeriksaan.getValueAt(i, 2).toString();
                    String key0 = "Atas" + idTemplate;
                    String hasil0 = mapHasil.containsKey(key0) ? mapHasil.get(key0) : "";
                    String ket0 = mapKet.containsKey(key0) ? mapKet.get(key0) : "";
                    String jnsRuj;
                    
                    if (Jk.getText().equals("L") && (rbDewasa.isSelected() == true))
                    {
                        jnsRuj = "nilai_rujukan_ld";
                    }
                    else if (Jk.getText().equals("L") && (rbAnak.isSelected() == true))
                    {
                        jnsRuj = "nilai_rujukan_la";
                    }
                    else if (Jk.getText().equals("P") && (rbDewasa.isSelected() == true))
                    {
                        jnsRuj = "nilai_rujukan_pd";
                    }
                    else
                    {
                        jnsRuj = "nilai_rujukan_pa";
                    }
                    
                    pstampil = koneksi.prepareStatement("select id_template, kd_jenis_prw, Pemeriksaan, satuan, " + jnsRuj
                            + " from template_laboratorium where id_template=?");
                    pstampil.setString(1, idTemplate);
                    rstampil = pstampil.executeQuery();

                    if (rstampil.next())
                    {
                        mdlDetail.addRow(new Object[]
                        {
                            true, 
                            "   + " + rstampil.getString("Pemeriksaan"), 
                            hasil0,
                            rstampil.getString("satuan"),
                            rstampil.getString(jnsRuj), 
                            ket0,
                            rstampil.getString("id_template"),
                            rstampil.getString("kd_jenis_prw"),
                            "Atas"
                        });
                    }
                    
                    psDetail = koneksi.prepareStatement("SELECT id_det_template, id_template, pemeriksaan, satuan, nilai_rujukan_ld " +
                            "FROM template_laboratorium_det WHERE id_template = ? ORDER BY id_det_template");
                    psDetail.setString(1, idTemplate);
                    rsDetail = psDetail.executeQuery();

                    while (rsDetail.next())
                    {
                        String idDet = rsDetail.getString("id_det_template");
                        String key = "Bawah" + idDet;
                        String hasil = mapHasil.containsKey(key) ? mapHasil.get(key) : "";
                        String ket = mapKet.containsKey(key) ? mapKet.get(key) : "";
                        
                        mdlDetail.addRow(new Object[]
                        {
                            true, 
                            "      - " + rsDetail.getString("pemeriksaan"), 
                            hasil,
                            rsDetail.getString("satuan"),
                            rsDetail.getString("nilai_rujukan_ld"), 
                            ket,
                            rsDetail.getString("id_det_template"),
                            rsDetail.getString("id_template"),
                            "Bawah"
                        });
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }
    
    public void emptTeks()
    {
        KodePerujuk.setText("");
        NmPerujuk.setText("");
        KodePj.setText("");
        NmDokterPj.setText("");
        KdPtg.setText("");
        NmPtg.setText("");
        Pemeriksaan.setText("");
        KodePj.requestFocus();
        isReset(true);
    }

    public void onCari()
    {
        KodePj.requestFocus();
    }

    private void isRawat()
    {
        Sequel.cariIsi("select no_rkm_medis from reg_periksa where no_rawat=? ", TNoRM, TNoRw.getText());
        Sequel.cariIsi("select kd_pj from reg_periksa where no_rawat=? ", Penjab, TNoRw.getText());
        Sequel.cariIsi("select kd_dokter from reg_periksa where no_rawat=? ", KodePerujuk, TNoRw.getText());
        Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=? ", NmPerujuk, KodePerujuk.getText());

        kamar = Sequel.cariIsi("select ifnull(kd_kamar,'') from kamar_inap where no_rawat=? order by tgl_masuk desc limit 1", TNoRw.getText());
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
                    + "where reg_periksa.no_rawat=?", TNoRw.getText());
        }
    }

    private void isPsien()
    {
        Sequel.cariIsi("select nm_pasien from pasien where no_rkm_medis=? ", TPasien, TNoRM.getText());
        Sequel.cariIsi("select jk from pasien where no_rkm_medis=? ", Jk, TNoRM.getText());
        Sequel.cariIsi("select umur from pasien where no_rkm_medis=?", Umur, TNoRM.getText());
        Sequel.cariIsi("select alamat from pasien where no_rkm_medis=? ", Alamat, TNoRM.getText());
    }

    public void isReset(boolean isAll)
    {
        try
        {
            psset_tarif = koneksi.prepareStatement("select * from set_tarif");
            try
            {
                rsset_tarif = psset_tarif.executeQuery();
                if (rsset_tarif.next())
                {
                    cara_bayar_lab = rsset_tarif.getString("cara_bayar_lab");
                }
                else
                {
                    cara_bayar_lab = "Yes";
                }
            }
            catch (Exception e)
            {
                System.out.println(e);
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
        
        
        tampilTarif(isAll);
        tampilTindakan(isAll);
        tampilDetail();
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
                    nilai_jam = CmbJam.getSelectedIndex();
                    nilai_menit = CmbMenit.getSelectedIndex();
                    nilai_detik = CmbDetik.getSelectedIndex();
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
                CmbJam.setSelectedItem(jam);
                CmbMenit.setSelectedItem(menit);
                CmbDetik.setSelectedItem(detik);
            }
        };
        // Timer
        new Timer(1000, taskPerformer).start();
    }

    public void setNoRm(String norwt, String posisi)
    {
        TNoRw.setText(norwt);
        this.status = posisi;

        if (posisi.equals("Ralan"))
        {
            kelas = "Kelas 3";
        }
        else
        {
            kelas = new GQuery()
                    .a("SELECT kelas")
                    .a("FROM kamar_inap")
                    .a("JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                    .a("WHERE no_rawat = {no_rawat}")
                    .set("no_rawat", norwt)
                    .getString();
        }

        try
        {
            pssetpj = koneksi.prepareStatement("select * from set_pjlab");
            try
            {
                rssetpj = pssetpj.executeQuery();
                while (rssetpj.next())
                {
                    KodePj.setText(rssetpj.getString(1));
                    NmDokterPj.setText(Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", rssetpj.getString(1)));
                }
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
            finally
            {
                if (rssetpj != null)
                {
                    rssetpj.close();
                }
                if (pssetpj != null)
                {
                    pssetpj.close();
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
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
                    cara_bayar_lab = rsset_tarif.getString("cara_bayar_lab");
                }
                else
                {
                    cara_bayar_lab = "Yes";
                }
            }
            catch (Exception e)
            {
                System.out.println(e);
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

        isReset(true);
    }

    public void isCek()
    {
        if (var.getjml2() >= 1)
        {
            KdPtg.setText(var.getkode());
            Sequel.cariIsi("select nama from petugas where nip=?", NmPtg, KdPtg.getText());
        }
        else
        {
            KdPtg.setText("");
            NmPtg.setText("");
        }
        BtnSimpan.setEnabled(var.getperiksa_lab());
        BtnPrint.setEnabled(var.getperiksa_lab());
        BtnHapus.setEnabled(var.getperiksa_lab());
        btnTarif.setEnabled(var.gettarif_lab());
    }

    private void isForm()
    {
        if (ChkInput.isSelected() == true)
        {
            ChkInput.setVisible(false);
            FormInput.setPreferredSize(new Dimension(WIDTH, 269));
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
    private void getData() {
        Kd2.setText("");
        if(tblOrder.getSelectedRow()!= -1){
            Kd2.setText(tblOrder.getValueAt(tblOrder.getSelectedRow(),0).toString());
        }
    }
    public void tampilTarif(boolean isAll)
    {
        try
        {
//            jml = 0;
//            for (i = 0; i < tbTarif.getRowCount(); i++)
//            {
//                if (tbTarif.getValueAt(i, 0).toString().equals("true"))
//                {
//                    jml++;
//                }
//            }
//
//            pilih = null;
//            pilih = new boolean[jml];
//            kode = null;
//            kode = new String[jml];
//            nama = null;
//            nama = new String[jml];
//            total = null;
//            total = new double[jml];
//            bagian_rs = null;
//            bagian_rs = new double[jml];
//            bhp = null;
//            bhp = new double[jml];
//            tarif_perujuk = null;
//            tarif_perujuk = new double[jml];
//            tarif_tindakan_dokter = null;
//            tarif_tindakan_dokter = new double[jml];
//            tarif_tindakan_petugas = null;
//            tarif_tindakan_petugas = new double[jml];
//            kso = null;
//            kso = new double[jml];
//            menejemen = null;
//            menejemen = new double[jml];
//
//            index = 0;
//            for (i = 0; i < tbTarif.getRowCount(); i++)
//            {
//                if (tbTarif.getValueAt(i, 0).toString().equals("true"))
//                {
//                    pilih[index] = true;
//                    kode[index] = tbTarif.getValueAt(i, 1).toString();
//                    nama[index] = tbTarif.getValueAt(i, 2).toString();
//                    total[index] = Double.parseDouble(tbTarif.getValueAt(i, 3).toString());
//                    bagian_rs[index] = Double.parseDouble(tbTarif.getValueAt(i, 4).toString());
//                    bhp[index] = Double.parseDouble(tbTarif.getValueAt(i, 5).toString());
//                    tarif_perujuk[index] = Double.parseDouble(tbTarif.getValueAt(i, 6).toString());
//                    tarif_tindakan_dokter[index] = Double.parseDouble(tbTarif.getValueAt(i, 7).toString());
//                    tarif_tindakan_petugas[index] = Double.parseDouble(tbTarif.getValueAt(i, 8).toString());
//                    kso[index] = Double.parseDouble(tbTarif.getValueAt(i, 9).toString());
//                    menejemen[index] = Double.parseDouble(tbTarif.getValueAt(i, 10).toString());
//                    index++;
//                }
//            }
//
            Valid.tabelKosong(mdlTarif);
//            for (i = 0; i < jml; i++)
//            {
//                tabMode2.addRow(new Object[]
//                {
//                    pilih[i], kode[i], nama[i], total[i], bagian_rs[i], bhp[i], tarif_perujuk[i], tarif_tindakan_dokter[i], tarif_tindakan_petugas[i], kso[i], menejemen[i]
//                });
//            }

            pstindakan = koneksi.prepareStatement(
                    "select jns_perawatan_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan "
                    + "from jns_perawatan_lab inner join penjab on penjab.kd_pj=jns_perawatan_lab.kd_pj where "
                    + " jns_perawatan_lab.status='1' and (jns_perawatan_lab.kd_pj=? or jns_perawatan_lab.kd_pj='-') and jns_perawatan_lab.kd_jenis_prw like ? or "
                    + " jns_perawatan_lab.status='1' and (jns_perawatan_lab.kd_pj=? or jns_perawatan_lab.kd_pj='-') and jns_perawatan_lab.nm_perawatan like ? "
                    + "order by jns_perawatan_lab.kd_jenis_prw");
            pstindakan2 = koneksi.prepareStatement(
                    "select jns_perawatan_lab.kd_jenis_prw,jns_perawatan_lab.nm_perawatan "
                    + "from jns_perawatan_lab inner join penjab on penjab.kd_pj=jns_perawatan_lab.kd_pj where "
                    + " jns_perawatan_lab.status='1' and jns_perawatan_lab.kd_jenis_prw like ? or "
                    + " jns_perawatan_lab.status='1' and jns_perawatan_lab.nm_perawatan like ?  "
                    + "order by jns_perawatan_lab.kd_jenis_prw");
            try
            {
                switch (cara_bayar_lab)
                {
                    case "Yes":
                        pstindakan.setString(1, Penjab.getText().trim());
                        pstindakan.setString(2, "%" + Pemeriksaan.getText().trim() + "%");
                        pstindakan.setString(3, Penjab.getText().trim());
                        pstindakan.setString(4, "%" + Pemeriksaan.getText().trim() + "%");
                        rstindakan = pstindakan.executeQuery();
                        break;
                    case "No":
                        pstindakan2.setString(1, "%" + Pemeriksaan.getText().trim() + "%");
                        pstindakan2.setString(2, "%" + Pemeriksaan.getText().trim() + "%");
                        rstindakan = pstindakan2.executeQuery();
                        break;
                }
                
                while (rstindakan.next())
                {
                    if (kdJnsPrws.contains(rstindakan.getString(1)))
                    {
                        mdlTarif.addRow(new Object[]
                        {
                            true, rstindakan.getString(1), rstindakan.getString(2)
                        });
                    }
                }
                
                if (isAll)
                {
                    rstindakan.beforeFirst();
                
                    while (rstindakan.next())
                    {
                        if (!kdJnsPrws.contains(rstindakan.getString(1)))
                        {
                            mdlTarif.addRow(new Object[]
                            {
                                false, rstindakan.getString(1), rstindakan.getString(2)
                            });
                        }
                    }
                }
            }
            catch (Exception e)
            {
                System.out.println("Notifikasi : " + e);
            }
            finally
            {
                if (rstindakan != null)
                {
                    rstindakan.close();
                }
                if (pstindakan != null)
                {
                    pstindakan.close();
                }
                if (pstindakan2 != null)
                {
                    pstindakan2.close();
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("Notifikasi : " + e);
        }
    }
    
    private void tampilOrder()
    {
        Valid.tabelKosong(mdlOrder);
        
        try
        {
            String s = new GQuery()
                    .a("SELECT id_periksa, reg_periksa.no_rawat AS no_rawat, CONCAT(pasien.no_rkm_medis, ' ', pasien.nm_pasien) AS pasien,")
                    .a("petugas.nama AS petugas, tgl_periksa, jam, dp.nm_dokter AS dokter_perujuk, dj.nm_dokter AS dokter_pj, proses")
                    .a("FROM periksa_lab")
                    .a("JOIN reg_periksa ON reg_periksa.no_rawat = periksa_lab.no_rawat")
                    .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                    .a("JOIN petugas ON petugas.nip = periksa_lab.nip")
                    .a("JOIN dokter dp ON dp.kd_dokter = dokter_perujuk")
                    .a("JOIN dokter dj ON dj.kd_dokter = periksa_lab.kd_dokter")
                    .a("WHERE proses = 'Belum'")
                    .a("AND tgl_periksa BETWEEN {tgl1} AND {tgl2}")
                    .a("AND (pasien.no_rkm_medis LIKE {rkm_medis} OR pasien.nm_pasien LIKE {nama})")
                    .a("ORDER BY tgl_periksa, jam")
                    .set("tgl1", Valid.SetTgl(tglTransaksi1.getSelectedItem().toString()))
                    .set("tgl2", Valid.SetTgl(tglTransaksi2.getSelectedItem().toString()))
                    .set("rkm_medis", "%" + TCari.getText() + "%")
                    .set("nama", "%" + TCari.getText() + "%")
                    .compile();
            
            psTraksaksi = koneksi.prepareStatement(s);
            rsTransksi = psTraksaksi.executeQuery();
            
            while (rsTransksi.next())
            {
                mdlOrder.addRow(new Object[] 
                {
                    rsTransksi.getString("no_rawat"),
                    rsTransksi.getString("pasien"),
                    rsTransksi.getString("petugas"),
                    rsTransksi.getString("tgl_periksa"),
                    rsTransksi.getString("jam"),
                    rsTransksi.getString("dokter_perujuk"),
                    rsTransksi.getString("dokter_pj"),
                    rsTransksi.getString("proses"),
                    rsTransksi.getString("id_periksa"),
                });
                
                String s2 = new GQuery()
                        .a("SELECT * FROM detail_periksa_lab")
                        .a("JOIN jns_perawatan_lab ON jns_perawatan_lab.kd_jenis_prw = detail_periksa_lab.kd_jenis_prw")
                        .a("WHERE id_periksa = {id_periksa}")
                        .set("id_periksa", rsTransksi.getString("id_periksa"))
                        .compile();
                
                psTransaksiD1 = koneksi.prepareStatement(s2);
                rsTransaksiD1 = psTransaksiD1.executeQuery();
                
                mdlOrder.addRow(new Object[] 
                {
                    "",
                    "",
                    "Pemeriksaan",
                    "Hasil",
                    "Satuan",
                    "Nilai Rujukan",
                    "Keterangan"
                });
                
                while (rsTransaksiD1.next())
                {
                    mdlOrder.addRow(new Object[] 
                    {
                        "",
                        "",
                        rsTransaksiD1.getString("nm_perawatan"),
                        "",
                        "",
                        "",
                        ""
                    });
                    
                    String s3 = new GQuery()
                            .a("SELECT pemeriksaan, nilai, satuan, nilai_rujukan, keterangan")
                            .a("FROM detail_periksa_lab_2")
                            .a("JOIN template_laboratorium ON template_laboratorium.id_template = detail_periksa_lab_2.id_template")
                            .a("WHERE id_detail_1 = {id_detail}")
                            .set("id_detail", rsTransaksiD1.getString("id_detail"))
                            .compile();
                    
                    psTransaksiD2 = koneksi.prepareStatement(s3);
                    rsTransaksiD2 = psTransaksiD2.executeQuery();
                    
                    while (rsTransaksiD2.next())
                    {
                        mdlOrder.addRow(new Object[] 
                        {
                            "",
                            "",
                            "  - " + rsTransaksiD2.getString("pemeriksaan"),
                            rsTransaksiD2.getString("nilai"),
                            rsTransaksiD2.getString("satuan"),
                            rsTransaksiD2.getString("nilai_rujukan"),
                            rsTransaksiD2.getString("keterangan")
                        });
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    private void tampilTransksi()
    {
        Valid.tabelKosong(mdlTransaksi);
        
        try
        {
            String s = new GQuery()
                    .a("SELECT id_periksa, reg_periksa.no_rawat AS no_rawat, CONCAT(pasien.no_rkm_medis, ' ', pasien.nm_pasien) AS pasien,")
                    .a("petugas.nama AS petugas, tgl_periksa, jam, dp.nm_dokter AS dokter_perujuk, dj.nm_dokter AS dokter_pj, proses")
                    .a("FROM periksa_lab")
                    .a("JOIN reg_periksa ON reg_periksa.no_rawat = periksa_lab.no_rawat")
                    .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                    .a("JOIN petugas ON petugas.nip = periksa_lab.nip")
                    .a("JOIN dokter dp ON dp.kd_dokter = dokter_perujuk")
                    .a("JOIN dokter dj ON dj.kd_dokter = periksa_lab.kd_dokter")
                    .a("WHERE proses = 'Sudah'")
                    .a("AND tgl_periksa BETWEEN {tgl1} AND {tgl2}")
                    .a("AND (pasien.no_rkm_medis LIKE {rkm_medis} OR pasien.nm_pasien LIKE {nama})")
                    .set("tgl1", Valid.SetTgl(tglTransaksi3.getSelectedItem().toString()))
                    .set("tgl2", Valid.SetTgl(tglTransaksi4.getSelectedItem().toString()))
                    .set("rkm_medis", "%" + TCari1.getText() + "%")
                    .set("nama", "%" + TCari1.getText() + "%")
                    .a("ORDER BY tgl_periksa, jam")
                    .compile();
            
            psTraksaksi = koneksi.prepareStatement(s);
            rsTransksi = psTraksaksi.executeQuery();
            
            while (rsTransksi.next())
            {
                mdlTransaksi.addRow(new Object[] 
                {
                    rsTransksi.getString("no_rawat"),
                    rsTransksi.getString("pasien"),
                    rsTransksi.getString("petugas"),
                    rsTransksi.getString("tgl_periksa"),
                    rsTransksi.getString("jam"),
                    rsTransksi.getString("dokter_perujuk"),
                    rsTransksi.getString("dokter_pj"),
                    rsTransksi.getString("proses"),
                    rsTransksi.getString("id_periksa"),
                });
                
                String s2 = new GQuery()
                        .a("SELECT * FROM detail_periksa_lab")
                        .a("JOIN jns_perawatan_lab ON jns_perawatan_lab.kd_jenis_prw = detail_periksa_lab.kd_jenis_prw")
                        .a("WHERE id_periksa = {id_periksa}")
                        .set("id_periksa", rsTransksi.getString("id_periksa"))
                        .compile();
                
                psTransaksiD1 = koneksi.prepareStatement(s2);
                rsTransaksiD1 = psTransaksiD1.executeQuery();
                
                mdlTransaksi.addRow(new Object[] 
                {
                    "",
                    "",
                    "Pemeriksaan",
                    "Hasil",
                    "Satuan",
                    "Nilai Rujukan",
                    "Keterangan",
                    "",
                    ""
                });
                
                while (rsTransaksiD1.next())
                {
                    mdlTransaksi.addRow(new Object[] 
                    {
                        "",
                        "",
                        rsTransaksiD1.getString("nm_perawatan"),
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""
                    });
                    
                    String s3 = new GQuery()
                            .a("SELECT pemeriksaan, nilai, satuan, nilai_rujukan, keterangan")
                            .a("FROM detail_periksa_lab_2")
                            .a("JOIN template_laboratorium ON template_laboratorium.id_template = detail_periksa_lab_2.id_template")
                            .a("WHERE id_detail_1 = {id_detail}")
                            .set("id_detail", rsTransaksiD1.getString("id_detail"))
                            .compile();
                    
                    psTransaksiD2 = koneksi.prepareStatement(s3);
                    rsTransaksiD2 = psTransaksiD2.executeQuery();
                    
                    while (rsTransaksiD2.next())
                    {
                        mdlTransaksi.addRow(new Object[] 
                        {
                            "",
                            "",
                            "  - " + rsTransaksiD2.getString("pemeriksaan"),
                            rsTransaksiD2.getString("nilai"),
                            rsTransaksiD2.getString("satuan"),
                            rsTransaksiD2.getString("nilai_rujukan"),
                            rsTransaksiD2.getString("keterangan"),
                            "",
                            ""
                        });
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}
