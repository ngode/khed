/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * DlgPemberianObat.java
 *
 * Created on 27 Mei 10, 14:52:31
 */
package simrskhanza;

import fungsi.GQuery;
import fungsi.GResult;
import fungsi.GRow;
import fungsi.GStatement;
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
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import util.GMessage;

/**
 *
 * @author perpustakaan
 */
public class DlgPemeriksaanHemodialisa extends javax.swing.JDialog
{
    // CHILD ====
    private DlgCariPetugas petugas = new DlgCariPetugas(null, false);
    private DlgCariDokter dlgDokter;

    private DefaultTableModel mdlTindakan, mdlTransaksi, mdlOrder;
    private Connection koneksi = koneksiDB.condb();
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private GStatement psTindakan, psTransaksi, psTransaksiDet, psOrder, psOrderDet;
    private ResultSet rsTindakan, rsTransaksi, rsTransaksiDet, rsOrder, rsOrderDet;

    private List<String> selKodes = new ArrayList<>();
    private List<String[]> selDatas = new ArrayList<>();
    private String kdPeriksa;
    
    private int pil = -1;
    
    /**
     * Creates new form DlgPemberianObat
     *
     * @param parent
     * @param modal
     */
    public DlgPemeriksaanHemodialisa(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();

        this.setLocation(8, 1);
        setSize(885, 674);

        initTblTindakan();
        initTblTransaksi();
        initTblOrder();
        
        initDokter();
        
        tampilTindakan();
        tampilTransaksi();
        tampilOrder();
        
        txtNoRw.setDocument(new batasInput((byte) 17).getKata(txtNoRw));
        txtCari.setDocument(new batasInput((byte) 100).getKata(txtCari));
        
        if (koneksiDB.cariCepat().equals("aktif"))
        {
            txtCari.addTextChangedListener((t) -> tampilTindakan());
            txtCariOrder.addTextChangedListener((t) -> tampilOrder());
            txtCariTransaksi.addTextChangedListener((t) -> tampilTransaksi());
        }
        
        //ChkInput.setSelected(false);
        isForm();
        jam();
        
        dlgDokter = new DlgCariDokter(null, false);
        dlgDokter.addWindowListener(new WindowListener()
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
                if (dlgDokter.getTable().getSelectedRow() > -1)
                {
                    if (pil == 1)
                    {
                        txtKdDokterKons.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(),0).toString());
                        txtNamaDokterKons.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(),1).toString());
                    }
                    else if (pil == 2)
                    {
                        txtKdDokterPj.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(),0).toString());
                        txtNamaDokterPj.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(),1).toString());
                    }
                    else if (pil == 3)
                    {
                        txtKdDokterPel.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(),0).toString());
                        txtNamaDokterPel.setText(dlgDokter.getTable().getValueAt(dlgDokter.getTable().getSelectedRow(),1).toString());
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
                if (var.getform().equals("DlgPeriksaRadiologi"))
                {
                    if (petugas.getTable().getSelectedRow() != -1)
                    {
                        txtKdPetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(), 0).toString());
                        NmPtg.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(), 1).toString());
                    }
                    txtKdPetugas.requestFocus();
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
    }

    private void initDokter()
    {
        txtKdDokterKons.setText(Sequel.cariIsi("select kd_kons_hd from set_pjlab"));
        txtKdDokterPj.setText(Sequel.cariIsi("select kd_dokterhemodialisa from set_pjlab"));
        txtKdDokterPel.setText(Sequel.cariIsi("select kd_pel_hd from set_pjlab"));
        
        txtNamaDokterKons.setText(Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", txtKdDokterKons.getText()));
        txtNamaDokterPj.setText(Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", txtKdDokterPj.getText()));
        txtNamaDokterPel.setText(Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", txtKdDokterPel.getText()));
    }
    
    private void initTblTindakan()
    {
        // Init jenis pemeriksaan ===========================
        Object[] row =
        {
            "P", "Kode Periksa", "Nama Pemeriksaan", "Material", "Bhp", "Dokter", "Perawat", "Kso", "Manajemen", "Tarif"
        };

        mdlTindakan = new DefaultTableModel(null, row)
        {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                return colIndex == 0;
            }
            
            Class[] types = new Class[]
            {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class,
                java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class,
                java.lang.Double.class, java.lang.Double.class
            };

            @Override
            public Class getColumnClass(int columnIndex)
            {
                return types[columnIndex];
            }
        };
        
        int[] sz = 
        {
            20, 80, 400, 0, 0, 0, 0, 0, 0, 75
        };
        
        tblTindakan.setModel(mdlTindakan);

        tblTindakan.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tblTindakan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < sz.length; i++)
        {
            TableColumn column = tblTindakan.getColumnModel().getColumn(i);
            
            if (sz[i] > 0)
                column.setPreferredWidth(sz[i]);
            else
            {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
        
        tblTindakan.setDefaultRenderer(Object.class, new WarnaTable());
        
        psTindakan = new GStatement(koneksi)
            .a("SELECT kd_jenis_prw, nm_perawatan, material, bhp, tarif_tindakandr, tarif_tindakanpr,")
            .a("kso, menejemen, total_byrdrpr")
            .a("FROM jns_perawatan")
            .a("WHERE kd_kategori = 'hd'")
            .a("AND nm_perawatan LIKE ?")
            .a("ORDER BY nm_perawatan");
    }
    
    private void initTblTransaksi()
    {
        Object[] row =
        {
            "Kd Periksa", "No Rawat", "Pasien", "Tgl Periksa", "Jam Periksa", "Status"
        };
        
        int[] sz = 
        {
            0, 120, 300, 200, 140, 90
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
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
            else
            {
                column.setPreferredWidth(sz[i]);
            }
        }
        
        tblTransaksi.setDefaultRenderer(Object.class, new WarnaTable());
        
        psTransaksi = new GStatement(koneksi)
                .a("SELECT pemeriksaan_hd.kd_periksa, pemeriksaan_hd.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, kamar.kd_kamar, bangsal.nm_bangsal, poliklinik.nm_poli,")
                .a("    tgl_periksa, jam_mulai, IF (pemeriksaan_hd.status = 0, 'Belum', 'Sudah') as status")
                .a("FROM pemeriksaan_hd")
                .a("JOIN reg_periksa ON reg_periksa.no_rawat = pemeriksaan_hd.no_rawat")
                .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                .a("LEFT JOIN kamar_inap ON kamar_inap.no_rawat = pemeriksaan_hd.no_rawat")
                .a("LEFT JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                .a("LEFT JOIN bangsal ON bangsal.kd_bangsal = kamar.kd_bangsal")
                .a("LEFT JOIN poliklinik ON poliklinik.kd_poli = reg_periksa.kd_poli")
                .a("WHERE pemeriksaan_hd.status = 1")
                .a("AND tgl_periksa BETWEEN :tgl1 AND :tgl2")
                .a("AND (pasien.no_rkm_medis LIKE :cari OR pasien.nm_pasien LIKE :cari)")
                .a("ORDER BY tgl_periksa, jam_mulai");
        
        psTransaksiDet = new GStatement(koneksi)
                .a("SELECT det_pemeriksaan_hd.kd_jenis_prw, nm_perawatan, det_pemeriksaan_hd.biaya_rawat")
                .a("FROM det_pemeriksaan_hd")
                .a("JOIN jns_perawatan ON jns_perawatan.kd_jenis_prw = det_pemeriksaan_hd.kd_jenis_prw")
                .a("WHERE kd_periksa = :kd_periksa");
    }
    
    private void initTblOrder()
    {
        Object[] row =
        {
            "Kd Periksa", "No Rawat", "Pasien", "Tgl Periksa", "Jam Periksa", "Status"
        };
        
        int[] sz = 
        {
            0, 120, 300, 200, 140, 90
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
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
            else
            {
                column.setPreferredWidth(sz[i]);
            }
        }
        
        tblOrder.setDefaultRenderer(Object.class, new WarnaTable());
        
        psOrder = new GStatement(koneksi)
                .a("SELECT pemeriksaan_hd.kd_periksa, pemeriksaan_hd.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, kamar.kd_kamar, bangsal.nm_bangsal, poliklinik.nm_poli,")
                .a("    tgl_periksa, jam_mulai, IF (pemeriksaan_hd.status = 0, 'Belum', 'Sudah') as status")
                .a("FROM pemeriksaan_hd")
                .a("JOIN reg_periksa ON reg_periksa.no_rawat = pemeriksaan_hd.no_rawat")
                .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                .a("LEFT JOIN kamar_inap ON kamar_inap.no_rawat = pemeriksaan_hd.no_rawat")
                .a("LEFT JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                .a("LEFT JOIN bangsal ON bangsal.kd_bangsal = kamar.kd_bangsal")
                .a("LEFT JOIN poliklinik ON poliklinik.kd_poli = reg_periksa.kd_poli")
                .a("WHERE pemeriksaan_hd.status = 0")
                .a("AND tgl_periksa BETWEEN :tgl1 AND :tgl2")
                .a("AND (pasien.no_rkm_medis LIKE :cari OR pasien.nm_pasien LIKE :cari)")
                .a("ORDER BY tgl_periksa, jam_mulai");
        
        psOrderDet = new GStatement(koneksi)
                .a("SELECT det_pemeriksaan_hd.kd_jenis_prw, nm_perawatan, det_pemeriksaan_hd.biaya_rawat")
                .a("FROM det_pemeriksaan_hd")
                .a("JOIN jns_perawatan ON jns_perawatan.kd_jenis_prw = det_pemeriksaan_hd.kd_jenis_prw")
                .a("WHERE kd_periksa = :kd_periksa");
    }
    
    private void tampilTindakan()
    {
        try 
        {
            Valid.tabelKosong(mdlTindakan);
            psTindakan.setString(1, "%" + txtCari.getText() + "%");
            rsTindakan = psTindakan.executeQuery();
            
            while (rsTindakan.next())
            {
                boolean b = false;
                
                if (selKodes.contains(rsTindakan.getString("kd_jenis_prw")))
                    b = true;
                
                if (!ckbChecked.isSelected() || b)
                {
                    Object[] o = new Object[]
                    {
                        b,
                        rsTindakan.getString("kd_jenis_prw"),
                        rsTindakan.getString("nm_perawatan"),
                        rsTindakan.getDouble("material"),
                        rsTindakan.getDouble("bhp"),
                        rsTindakan.getDouble("tarif_tindakandr"),
                        rsTindakan.getDouble("tarif_tindakanpr"),
                        rsTindakan.getDouble("kso"),
                        rsTindakan.getDouble("menejemen"),
                        rsTindakan.getDouble("total_byrdrpr")
                    };

                    mdlTindakan.addRow(o);
                }
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
            psTransaksi.setString("cari", "%" + txtCariTransaksi.getText() + "%");
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
                    rsTransaksi.getString("kd_periksa"),
                    rsTransaksi.getString("no_rawat"),
                    pas,
                    rsTransaksi.getString("tgl_periksa"),
                    rsTransaksi.getString("jam_mulai"),
                    rsTransaksi.getString("status")
                };
                
                mdlTransaksi.addRow(o);
                mdlTransaksi.addRow(new Object[]
                {
                    "", "", "Kode Periksa", "Nama Pemeriksaan", "Biaya Pemeriksaan", ""
                });
                
                psTransaksiDet.setString("kd_periksa", rsTransaksi.getString("kd_periksa"));
                rsTransaksiDet = psTransaksiDet.executeQuery();
                
                while (rsTransaksiDet.next())
                {
                    Object[] od = new Object[]
                    {
                        "",
                        "",
                        rsTransaksiDet.getString("kd_jenis_prw"),
                        rsTransaksiDet.getString("nm_perawatan"),
                        Valid.SetAngka(rsTransaksiDet.getDouble("biaya_rawat")),
                        ""
                    };

                    mdlTransaksi.addRow(od);
                }
            }
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
        }
    }
    
    private void tampilOrder()
    {
        try 
        {
            Valid.tabelKosong(mdlOrder);
            psOrder.setString("tgl1", Valid.SetTgl(tglOrder1.getSelectedItem().toString()));
            psOrder.setString("tgl2", Valid.SetTgl(tglOrder2.getSelectedItem().toString()));
            psOrder.setString("cari", "%" + txtCariOrder.getText() + "%");
            
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
                    rsOrder.getString("kd_periksa"),
                    rsOrder.getString("no_rawat"),
                    pas,
                    rsOrder.getString("tgl_periksa"),
                    rsOrder.getString("jam_mulai"),
                    rsOrder.getString("status")
                };
                
                mdlOrder.addRow(o);
                mdlOrder.addRow(new Object[]
                {
                    "", "", "Kode Periksa", "Nama Pemeriksaan", "Biaya Pemeriksaan", ""
                });
                
                psOrderDet.setString("kd_periksa", rsOrder.getString("kd_periksa"));
                rsOrderDet = psOrderDet.executeQuery();
                
                while (rsOrderDet.next())
                {
                    Object[] od = new Object[]
                    {
                        "",
                        "",
                        rsOrderDet.getString("kd_jenis_prw"),
                        rsOrderDet.getString("nm_perawatan"),
                        Valid.SetAngka(rsOrderDet.getDouble("biaya_rawat")),
                        ""
                    };

                    mdlOrder.addRow(od);
                }
            }
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
        }
    }
    
    private void clearAll()
    {
        kdPeriksa = null;
        
        txtNoRw.setText("");
        txtNoRm.setText("");
        txtNamaPasien.setText("");
        txtAlamat.setText("");
        txtUmur.setText("");
        txtJk.setText("");
        txtHdKe.setText("");
        txtPreTd.setText("");
        txtPreBb.setText("");
        txtPreNadi.setText("");
        txtPreRes.setText("");
        txtPostTd.setText("");
        txtPostBb.setText("");
        txtPostNadi.setText("");
        txtPostRes.setText("");
        txtKdPetugas.setText("");
        NmPtg.setText("");
        txtKdDokter.setText("");
        txtNamaDokter.setText("");
        txtKdDokterKons.setText("");
        txtNamaDokterKons.setText("");
        txtKdDokterPj.setText("");
        txtNamaDokterPj.setText("");
        txtKdDokterPel.setText("");
        txtNamaDokterPel.setText("");
        
        selKodes.clear();
        selDatas.clear();
        
        tampilTindakan();
        tampilTransaksi();
        tampilOrder();
    }
    
    //private DlgCariObatPenyakit dlgobtpny=new DlgCariObatPenyakit(null,false);
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPopupMenu2 = new javax.swing.JPopupMenu();
        menuUbah1 = new javax.swing.JMenuItem();
        menuHapus1 = new javax.swing.JMenuItem();
        menuCetakHasil = new javax.swing.JMenuItem();
        menuCetakBilling = new javax.swing.JMenuItem();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        menuUbah = new javax.swing.JMenuItem();
        menuHapus = new javax.swing.JMenuItem();
        menuCetakBilling1 = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        tabPane = new widget.TabPane();
        panelBiasa1 = new widget.PanelBiasa();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        btnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnPrint = new widget.Button();
        BtnAll = new widget.Button();
        BtnKeluar = new widget.Button();
        panelGlass9 = new widget.panelisi();
        jLabel6 = new widget.Label();
        txtCari = new widget.TextBox();
        ckbChecked = new widget.CekBox();
        BtnCari = new widget.Button();
        jLabel10 = new widget.Label();
        LCount = new widget.Label();
        PanelInput = new javax.swing.JPanel();
        FormInput = new widget.PanelBiasa();
        jLabel3 = new widget.Label();
        txtNoRw = new widget.TextBox();
        txtNoRm = new widget.TextBox();
        txtNamaPasien = new widget.TextBox();
        jLabel7 = new widget.Label();
        DTPBeri = new widget.Tanggal();
        cmbJam = new widget.ComboBox();
        cmbMnt = new widget.ComboBox();
        cmbDtk = new widget.ComboBox();
        ChkJln = new widget.CekBox();
        jLabel12 = new widget.Label();
        txtUmur = new widget.TextBox();
        jLabel16 = new widget.Label();
        txtJk = new widget.TextBox();
        jLabel4 = new widget.Label();
        txtAlamat = new widget.TextBox();
        jLabel13 = new widget.Label();
        txtPreTd = new widget.TextBox();
        jLabel9 = new widget.Label();
        jLabel14 = new widget.Label();
        txtPreBb = new widget.TextBox();
        jLabel15 = new widget.Label();
        txtPreNadi = new widget.TextBox();
        jLabel17 = new widget.Label();
        txtPreRes = new widget.TextBox();
        jLabel18 = new widget.Label();
        jLabel19 = new widget.Label();
        txtPostTd = new widget.TextBox();
        jLabel20 = new widget.Label();
        txtPostBb = new widget.TextBox();
        jLabel21 = new widget.Label();
        txtPostNadi = new widget.TextBox();
        jLabel22 = new widget.Label();
        txtPostRes = new widget.TextBox();
        jLabel23 = new widget.Label();
        txtHdKe = new widget.TextBox();
        jLabel24 = new widget.Label();
        txtKdDokter = new widget.TextBox();
        txtNamaDokter = new widget.TextBox();
        jLabel25 = new widget.Label();
        txtKdDokterKons = new widget.TextBox();
        txtNamaDokterKons = new widget.TextBox();
        jLabel26 = new widget.Label();
        txtKdDokterPj = new widget.TextBox();
        txtNamaDokterPj = new widget.TextBox();
        jLabel27 = new widget.Label();
        txtKdDokterPel = new widget.TextBox();
        txtNamaDokterPel = new widget.TextBox();
        jLabel28 = new widget.Label();
        txtKdPetugas = new widget.TextBox();
        btnPetugas = new widget.Button();
        NmPtg = new widget.TextBox();
        Scroll3 = new widget.ScrollPane();
        tblTindakan = new widget.Table();
        panelBiasa3 = new widget.PanelBiasa();
        Scroll2 = new widget.ScrollPane();
        tblOrder = new widget.Table();
        jPanel5 = new javax.swing.JPanel();
        panelGlass12 = new widget.panelisi();
        jLabel35 = new widget.Label();
        tglOrder1 = new widget.Tanggal();
        jLabel36 = new widget.Label();
        tglOrder2 = new widget.Tanggal();
        label12 = new widget.Label();
        txtCariOrder = new widget.TextBox();
        btnCariOrder = new widget.Button();
        label13 = new widget.Label();
        btnHapusOrder = new widget.Button();
        btnAllOrder = new widget.Button();
        btnPrintOrder = new widget.Button();
        btnKeluarOrder = new widget.Button();
        panelBiasa2 = new widget.PanelBiasa();
        Scroll1 = new widget.ScrollPane();
        tblTransaksi = new widget.Table();
        jPanel4 = new javax.swing.JPanel();
        panelGlass11 = new widget.panelisi();
        jLabel33 = new widget.Label();
        tglTransaksi1 = new widget.Tanggal();
        jLabel34 = new widget.Label();
        tglTransaksi2 = new widget.Tanggal();
        label14 = new widget.Label();
        txtCariTransaksi = new widget.TextBox();
        btnCariTransaksi = new widget.Button();
        label15 = new widget.Label();
        btnHapusTransaksi = new widget.Button();
        btnAllTransaksi = new widget.Button();
        btnPrintTransaksi = new widget.Button();
        btnKeluarTransaksi = new widget.Button();

        jPopupMenu2.setForeground(new java.awt.Color(60, 80, 50));
        jPopupMenu2.setAutoscrolls(true);
        jPopupMenu2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPopupMenu2.setFocusTraversalPolicyProvider(true);
        jPopupMenu2.setName("jPopupMenu2"); // NOI18N

        menuUbah1.setBackground(new java.awt.Color(255, 255, 255));
        menuUbah1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuUbah1.setForeground(new java.awt.Color(60, 80, 50));
        menuUbah1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuUbah1.setText("Ubah");
        menuUbah1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuUbah1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuUbah1.setIconTextGap(5);
        menuUbah1.setName("menuUbah1"); // NOI18N
        menuUbah1.setPreferredSize(new java.awt.Dimension(220, 26));
        menuUbah1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuUbah1ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(menuUbah1);

        menuHapus1.setBackground(new java.awt.Color(255, 255, 255));
        menuHapus1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuHapus1.setForeground(new java.awt.Color(60, 80, 50));
        menuHapus1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuHapus1.setText("Hapus");
        menuHapus1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuHapus1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuHapus1.setIconTextGap(5);
        menuHapus1.setName("menuHapus1"); // NOI18N
        menuHapus1.setPreferredSize(new java.awt.Dimension(220, 26));
        menuHapus1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuHapus1ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(menuHapus1);

        menuCetakHasil.setBackground(new java.awt.Color(255, 255, 255));
        menuCetakHasil.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuCetakHasil.setForeground(new java.awt.Color(60, 80, 50));
        menuCetakHasil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuCetakHasil.setText("Cetak Hasil");
        menuCetakHasil.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuCetakHasil.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuCetakHasil.setIconTextGap(5);
        menuCetakHasil.setName("menuCetakHasil"); // NOI18N
        menuCetakHasil.setPreferredSize(new java.awt.Dimension(220, 26));
        menuCetakHasil.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuCetakHasilActionPerformed(evt);
            }
        });
        jPopupMenu2.add(menuCetakHasil);

        menuCetakBilling.setBackground(new java.awt.Color(255, 255, 255));
        menuCetakBilling.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuCetakBilling.setForeground(new java.awt.Color(60, 80, 50));
        menuCetakBilling.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuCetakBilling.setText("Cetak Billing");
        menuCetakBilling.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuCetakBilling.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuCetakBilling.setIconTextGap(5);
        menuCetakBilling.setName("menuCetakBilling"); // NOI18N
        menuCetakBilling.setPreferredSize(new java.awt.Dimension(220, 26));
        menuCetakBilling.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuCetakBillingActionPerformed(evt);
            }
        });
        jPopupMenu2.add(menuCetakBilling);

        jPopupMenu1.setForeground(new java.awt.Color(60, 80, 50));
        jPopupMenu1.setAutoscrolls(true);
        jPopupMenu1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPopupMenu1.setFocusTraversalPolicyProvider(true);
        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        menuUbah.setBackground(new java.awt.Color(255, 255, 255));
        menuUbah.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuUbah.setForeground(new java.awt.Color(60, 80, 50));
        menuUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuUbah.setText("Ubah");
        menuUbah.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuUbah.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuUbah.setIconTextGap(5);
        menuUbah.setName("menuUbah"); // NOI18N
        menuUbah.setPreferredSize(new java.awt.Dimension(220, 26));
        menuUbah.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuUbahActionPerformed(evt);
            }
        });
        jPopupMenu1.add(menuUbah);

        menuHapus.setBackground(new java.awt.Color(255, 255, 255));
        menuHapus.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuHapus.setForeground(new java.awt.Color(60, 80, 50));
        menuHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuHapus.setText("Hapus");
        menuHapus.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuHapus.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuHapus.setIconTextGap(5);
        menuHapus.setName("menuHapus"); // NOI18N
        menuHapus.setPreferredSize(new java.awt.Dimension(220, 26));
        menuHapus.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuHapusActionPerformed(evt);
            }
        });
        jPopupMenu1.add(menuHapus);

        menuCetakBilling1.setBackground(new java.awt.Color(255, 255, 255));
        menuCetakBilling1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        menuCetakBilling1.setForeground(new java.awt.Color(60, 80, 50));
        menuCetakBilling1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        menuCetakBilling1.setText("Cetak Billing");
        menuCetakBilling1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        menuCetakBilling1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        menuCetakBilling1.setIconTextGap(5);
        menuCetakBilling1.setName("menuCetakBilling1"); // NOI18N
        menuCetakBilling1.setPreferredSize(new java.awt.Dimension(220, 26));
        menuCetakBilling1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuCetakBilling1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(menuCetakBilling1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Pemeriksaan Hemodealisa ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        tabPane.setName("tabPane"); // NOI18N

        panelBiasa1.setBorder(null);
        panelBiasa1.setName("panelBiasa1"); // NOI18N
        panelBiasa1.setLayout(new java.awt.BorderLayout());

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        btnSimpan.setMnemonic('S');
        btnSimpan.setText("Simpan");
        btnSimpan.setToolTipText("Alt+S");
        btnSimpan.setName("btnSimpan"); // NOI18N
        btnSimpan.setPreferredSize(new java.awt.Dimension(100, 30));
        btnSimpan.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSimpanActionPerformed(evt);
            }
        });
        panelGlass8.add(btnSimpan);

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
        panelGlass8.add(BtnHapus);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("Cetak");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(100, 30));
        panelGlass8.add(BtnPrint);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setText("Semua");
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(100, 30));
        panelGlass8.add(BtnAll);

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
        panelGlass8.add(BtnKeluar);

        jPanel3.add(panelGlass8, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 10));

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(jLabel6);

        txtCari.setName("txtCari"); // NOI18N
        txtCari.setPreferredSize(new java.awt.Dimension(200, 23));
        txtCari.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCariKeyPressed(evt);
            }
        });
        panelGlass9.add(txtCari);

        ckbChecked.setBackground(new java.awt.Color(235, 255, 235));
        ckbChecked.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(195, 215, 195)));
        ckbChecked.setForeground(new java.awt.Color(153, 0, 51));
        ckbChecked.setBorderPainted(true);
        ckbChecked.setBorderPaintedFlat(true);
        ckbChecked.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ckbChecked.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ckbChecked.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ckbChecked.setName("ckbChecked"); // NOI18N
        ckbChecked.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                ckbCheckedStateChanged(evt);
            }
        });
        panelGlass9.add(ckbChecked);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('6');
        BtnCari.setToolTipText("Alt+6");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(28, 23));
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
        panelGlass9.add(BtnCari);

        jLabel10.setText("Record :");
        jLabel10.setName("jLabel10"); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(65, 23));
        panelGlass9.add(jLabel10);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(45, 23));
        panelGlass9.add(LCount);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        panelBiasa1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(865, 280));
        FormInput.setLayout(null);

        jLabel3.setText("No.Rawat :");
        jLabel3.setName("jLabel3"); // NOI18N
        FormInput.add(jLabel3);
        jLabel3.setBounds(-2, 12, 80, 23);

        txtNoRw.setEditable(false);
        txtNoRw.setHighlighter(null);
        txtNoRw.setName("txtNoRw"); // NOI18N
        txtNoRw.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtNoRwKeyPressed(evt);
            }
        });
        FormInput.add(txtNoRw);
        txtNoRw.setBounds(81, 12, 175, 23);

        txtNoRm.setEditable(false);
        txtNoRm.setHighlighter(null);
        txtNoRm.setName("txtNoRm"); // NOI18N
        FormInput.add(txtNoRm);
        txtNoRm.setBounds(258, 12, 143, 23);

        txtNamaPasien.setEditable(false);
        txtNamaPasien.setHighlighter(null);
        txtNamaPasien.setName("txtNamaPasien"); // NOI18N
        txtNamaPasien.setPreferredSize(new java.awt.Dimension(25, 28));
        txtNamaPasien.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtNamaPasienActionPerformed(evt);
            }
        });
        FormInput.add(txtNamaPasien);
        txtNamaPasien.setBounds(403, 12, 320, 23);

        jLabel7.setText("Pre");
        jLabel7.setName("jLabel7"); // NOI18N
        FormInput.add(jLabel7);
        jLabel7.setBounds(30, 110, 50, 23);

        DTPBeri.setEditable(false);
        DTPBeri.setForeground(new java.awt.Color(50, 70, 50));
        DTPBeri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "28-12-2017" }));
        DTPBeri.setDisplayFormat("dd-MM-yyyy");
        DTPBeri.setName("DTPBeri"); // NOI18N
        DTPBeri.setOpaque(false);
        DTPBeri.setPreferredSize(new java.awt.Dimension(100, 23));
        DTPBeri.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                DTPBeriKeyPressed(evt);
            }
        });
        FormInput.add(DTPBeri);
        DTPBeri.setBounds(81, 72, 95, 23);

        cmbJam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        cmbJam.setName("cmbJam"); // NOI18N
        cmbJam.setOpaque(false);
        cmbJam.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbJamKeyPressed(evt);
            }
        });
        FormInput.add(cmbJam);
        cmbJam.setBounds(178, 72, 45, 23);

        cmbMnt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbMnt.setName("cmbMnt"); // NOI18N
        cmbMnt.setOpaque(false);
        cmbMnt.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbMntKeyPressed(evt);
            }
        });
        FormInput.add(cmbMnt);
        cmbMnt.setBounds(224, 72, 45, 23);

        cmbDtk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbDtk.setName("cmbDtk"); // NOI18N
        cmbDtk.setOpaque(false);
        cmbDtk.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                cmbDtkKeyPressed(evt);
            }
        });
        FormInput.add(cmbDtk);
        cmbDtk.setBounds(270, 72, 45, 23);

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
        FormInput.add(ChkJln);
        ChkJln.setBounds(316, 72, 23, 23);

        jLabel12.setText("Umur :");
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput.add(jLabel12);
        jLabel12.setBounds(507, 42, 60, 23);

        txtUmur.setEditable(false);
        txtUmur.setHighlighter(null);
        txtUmur.setName("txtUmur"); // NOI18N
        txtUmur.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtUmurKeyPressed(evt);
            }
        });
        FormInput.add(txtUmur);
        txtUmur.setBounds(580, 40, 130, 23);

        jLabel16.setText("J.K. :");
        jLabel16.setName("jLabel16"); // NOI18N
        FormInput.add(jLabel16);
        jLabel16.setBounds(700, 40, 50, 23);

        txtJk.setEditable(false);
        txtJk.setHighlighter(null);
        txtJk.setName("txtJk"); // NOI18N
        txtJk.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtJkKeyPressed(evt);
            }
        });
        FormInput.add(txtJk);
        txtJk.setBounds(760, 40, 50, 23);

        jLabel4.setText("Alamat :");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput.add(jLabel4);
        jLabel4.setBounds(-2, 42, 80, 23);

        txtAlamat.setEditable(false);
        txtAlamat.setHighlighter(null);
        txtAlamat.setName("txtAlamat"); // NOI18N
        FormInput.add(txtAlamat);
        txtAlamat.setBounds(81, 42, 426, 23);

        jLabel13.setText("TD :");
        jLabel13.setName("jLabel13"); // NOI18N
        FormInput.add(jLabel13);
        jLabel13.setBounds(30, 140, 50, 23);

        txtPreTd.setHighlighter(null);
        txtPreTd.setName("txtPreTd"); // NOI18N
        txtPreTd.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPreTdKeyPressed(evt);
            }
        });
        FormInput.add(txtPreTd);
        txtPreTd.setBounds(90, 140, 160, 23);

        jLabel9.setText("Tanggal :");
        jLabel9.setName("jLabel9"); // NOI18N
        FormInput.add(jLabel9);
        jLabel9.setBounds(-2, 72, 80, 23);

        jLabel14.setText("BB :");
        jLabel14.setName("jLabel14"); // NOI18N
        FormInput.add(jLabel14);
        jLabel14.setBounds(30, 170, 50, 23);

        txtPreBb.setHighlighter(null);
        txtPreBb.setName("txtPreBb"); // NOI18N
        txtPreBb.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPreBbKeyPressed(evt);
            }
        });
        FormInput.add(txtPreBb);
        txtPreBb.setBounds(90, 170, 160, 23);

        jLabel15.setText("NADI :");
        jLabel15.setName("jLabel15"); // NOI18N
        FormInput.add(jLabel15);
        jLabel15.setBounds(30, 200, 50, 23);

        txtPreNadi.setHighlighter(null);
        txtPreNadi.setName("txtPreNadi"); // NOI18N
        txtPreNadi.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPreNadiKeyPressed(evt);
            }
        });
        FormInput.add(txtPreNadi);
        txtPreNadi.setBounds(90, 200, 160, 23);

        jLabel17.setText("RES :");
        jLabel17.setName("jLabel17"); // NOI18N
        FormInput.add(jLabel17);
        jLabel17.setBounds(30, 230, 50, 23);

        txtPreRes.setHighlighter(null);
        txtPreRes.setName("txtPreRes"); // NOI18N
        txtPreRes.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPreResKeyPressed(evt);
            }
        });
        FormInput.add(txtPreRes);
        txtPreRes.setBounds(90, 230, 160, 23);

        jLabel18.setText("Post");
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput.add(jLabel18);
        jLabel18.setBounds(260, 110, 50, 23);

        jLabel19.setText("TD :");
        jLabel19.setName("jLabel19"); // NOI18N
        FormInput.add(jLabel19);
        jLabel19.setBounds(260, 140, 50, 23);

        txtPostTd.setHighlighter(null);
        txtPostTd.setName("txtPostTd"); // NOI18N
        txtPostTd.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPostTdKeyPressed(evt);
            }
        });
        FormInput.add(txtPostTd);
        txtPostTd.setBounds(320, 140, 160, 23);

        jLabel20.setText("BB :");
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput.add(jLabel20);
        jLabel20.setBounds(260, 170, 50, 23);

        txtPostBb.setHighlighter(null);
        txtPostBb.setName("txtPostBb"); // NOI18N
        txtPostBb.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPostBbKeyPressed(evt);
            }
        });
        FormInput.add(txtPostBb);
        txtPostBb.setBounds(320, 170, 160, 23);

        jLabel21.setText("NADI :");
        jLabel21.setName("jLabel21"); // NOI18N
        FormInput.add(jLabel21);
        jLabel21.setBounds(260, 200, 50, 23);

        txtPostNadi.setHighlighter(null);
        txtPostNadi.setName("txtPostNadi"); // NOI18N
        txtPostNadi.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPostNadiKeyPressed(evt);
            }
        });
        FormInput.add(txtPostNadi);
        txtPostNadi.setBounds(320, 200, 160, 23);

        jLabel22.setText("RES :");
        jLabel22.setName("jLabel22"); // NOI18N
        FormInput.add(jLabel22);
        jLabel22.setBounds(260, 230, 50, 23);

        txtPostRes.setHighlighter(null);
        txtPostRes.setName("txtPostRes"); // NOI18N
        txtPostRes.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtPostResKeyPressed(evt);
            }
        });
        FormInput.add(txtPostRes);
        txtPostRes.setBounds(320, 230, 160, 23);

        jLabel23.setText("HD ke :");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput.add(jLabel23);
        jLabel23.setBounds(360, 70, 50, 23);

        txtHdKe.setHighlighter(null);
        txtHdKe.setName("txtHdKe"); // NOI18N
        txtHdKe.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtHdKeKeyPressed(evt);
            }
        });
        FormInput.add(txtHdKe);
        txtHdKe.setBounds(420, 70, 70, 23);

        jLabel24.setText("Dokter Perujuk  :");
        jLabel24.setName("jLabel24"); // NOI18N
        FormInput.add(jLabel24);
        jLabel24.setBounds(510, 140, 92, 23);

        txtKdDokter.setEditable(false);
        txtKdDokter.setName("txtKdDokter"); // NOI18N
        FormInput.add(txtKdDokter);
        txtKdDokter.setBounds(610, 140, 80, 23);

        txtNamaDokter.setEditable(false);
        txtNamaDokter.setHighlighter(null);
        txtNamaDokter.setName("txtNamaDokter"); // NOI18N
        txtNamaDokter.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtNamaDokterActionPerformed(evt);
            }
        });
        FormInput.add(txtNamaDokter);
        txtNamaDokter.setBounds(700, 140, 190, 23);

        jLabel25.setText("Dokter Konsultan :");
        jLabel25.setName("jLabel25"); // NOI18N
        FormInput.add(jLabel25);
        jLabel25.setBounds(510, 170, 92, 23);

        txtKdDokterKons.setEditable(false);
        txtKdDokterKons.setName("txtKdDokterKons"); // NOI18N
        FormInput.add(txtKdDokterKons);
        txtKdDokterKons.setBounds(610, 170, 80, 23);

        txtNamaDokterKons.setEditable(false);
        txtNamaDokterKons.setHighlighter(null);
        txtNamaDokterKons.setName("txtNamaDokterKons"); // NOI18N
        txtNamaDokterKons.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtNamaDokterKonsActionPerformed(evt);
            }
        });
        FormInput.add(txtNamaDokterKons);
        txtNamaDokterKons.setBounds(700, 170, 190, 23);

        jLabel26.setText("Dokter PJ  :");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput.add(jLabel26);
        jLabel26.setBounds(510, 200, 92, 23);

        txtKdDokterPj.setEditable(false);
        txtKdDokterPj.setName("txtKdDokterPj"); // NOI18N
        FormInput.add(txtKdDokterPj);
        txtKdDokterPj.setBounds(610, 200, 80, 23);

        txtNamaDokterPj.setEditable(false);
        txtNamaDokterPj.setHighlighter(null);
        txtNamaDokterPj.setName("txtNamaDokterPj"); // NOI18N
        txtNamaDokterPj.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtNamaDokterPjActionPerformed(evt);
            }
        });
        FormInput.add(txtNamaDokterPj);
        txtNamaDokterPj.setBounds(700, 200, 190, 23);

        jLabel27.setText("Dokter Pelaksana :");
        jLabel27.setName("jLabel27"); // NOI18N
        FormInput.add(jLabel27);
        jLabel27.setBounds(510, 230, 92, 23);

        txtKdDokterPel.setEditable(false);
        txtKdDokterPel.setName("txtKdDokterPel"); // NOI18N
        FormInput.add(txtKdDokterPel);
        txtKdDokterPel.setBounds(610, 230, 80, 23);

        txtNamaDokterPel.setEditable(false);
        txtNamaDokterPel.setHighlighter(null);
        txtNamaDokterPel.setName("txtNamaDokterPel"); // NOI18N
        txtNamaDokterPel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtNamaDokterPelActionPerformed(evt);
            }
        });
        FormInput.add(txtNamaDokterPel);
        txtNamaDokterPel.setBounds(700, 230, 190, 23);

        jLabel28.setText("Petugas :");
        jLabel28.setName("jLabel28"); // NOI18N
        FormInput.add(jLabel28);
        jLabel28.setBounds(510, 70, 60, 23);

        txtKdPetugas.setEditable(false);
        txtKdPetugas.setName("txtKdPetugas"); // NOI18N
        txtKdPetugas.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtKdPetugasKeyPressed(evt);
            }
        });
        FormInput.add(txtKdPetugas);
        txtKdPetugas.setBounds(580, 70, 80, 23);

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
        FormInput.add(btnPetugas);
        btnPetugas.setBounds(870, 70, 28, 23);

        NmPtg.setEditable(false);
        NmPtg.setName("NmPtg"); // NOI18N
        FormInput.add(NmPtg);
        NmPtg.setBounds(670, 70, 195, 23);

        PanelInput.add(FormInput, java.awt.BorderLayout.PAGE_START);

        Scroll3.setName("Scroll3"); // NOI18N
        Scroll3.setOpaque(true);

        tblTindakan.setAutoCreateRowSorter(true);
        tblTindakan.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tblTindakan.setName("tblTindakan"); // NOI18N
        tblTindakan.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblTindakanMouseClicked(evt);
            }
        });
        tblTindakan.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                tblTindakanPropertyChange(evt);
            }
        });
        tblTindakan.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                tblTindakanKeyPressed(evt);
            }
        });
        Scroll3.setViewportView(tblTindakan);

        PanelInput.add(Scroll3, java.awt.BorderLayout.CENTER);

        panelBiasa1.add(PanelInput, java.awt.BorderLayout.CENTER);

        tabPane.addTab("Transaksi", panelBiasa1);

        panelBiasa3.setName("panelBiasa3"); // NOI18N
        panelBiasa3.setLayout(new java.awt.BorderLayout());

        Scroll2.setName("Scroll2"); // NOI18N
        Scroll2.setOpaque(true);

        tblOrder.setAutoCreateRowSorter(true);
        tblOrder.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tblOrder.setComponentPopupMenu(jPopupMenu1);
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
        Scroll2.setViewportView(tblOrder);

        panelBiasa3.add(Scroll2, java.awt.BorderLayout.CENTER);

        jPanel5.setName("jPanel5"); // NOI18N
        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(44, 44));
        jPanel5.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass12.setMinimumSize(new java.awt.Dimension(50, 47));
        panelGlass12.setName("panelGlass12"); // NOI18N
        panelGlass12.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 10));

        jLabel35.setText("Tgl :");
        jLabel35.setName("jLabel35"); // NOI18N
        jLabel35.setPreferredSize(new java.awt.Dimension(58, 23));
        panelGlass12.add(jLabel35);

        tglOrder1.setEditable(false);
        tglOrder1.setForeground(new java.awt.Color(50, 70, 50));
        tglOrder1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "28-12-2017" }));
        tglOrder1.setDisplayFormat("dd-MM-yyyy");
        tglOrder1.setName("tglOrder1"); // NOI18N
        tglOrder1.setOpaque(false);
        tglOrder1.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass12.add(tglOrder1);

        jLabel36.setText("s.d");
        jLabel36.setName("jLabel36"); // NOI18N
        jLabel36.setPreferredSize(new java.awt.Dimension(18, 23));
        panelGlass12.add(jLabel36);

        tglOrder2.setEditable(false);
        tglOrder2.setForeground(new java.awt.Color(50, 70, 50));
        tglOrder2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "28-12-2017" }));
        tglOrder2.setDisplayFormat("dd-MM-yyyy");
        tglOrder2.setName("tglOrder2"); // NOI18N
        tglOrder2.setOpaque(false);
        tglOrder2.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass12.add(tglOrder2);

        label12.setText("Key Word :");
        label12.setName("label12"); // NOI18N
        label12.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass12.add(label12);

        txtCariOrder.setName("txtCariOrder"); // NOI18N
        txtCariOrder.setPreferredSize(new java.awt.Dimension(170, 23));
        txtCariOrder.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCariOrderKeyPressed(evt);
            }
        });
        panelGlass12.add(txtCariOrder);

        btnCariOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        btnCariOrder.setMnemonic('6');
        btnCariOrder.setToolTipText("Alt+6");
        btnCariOrder.setName("btnCariOrder"); // NOI18N
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
        panelGlass12.add(btnCariOrder);

        label13.setName("label13"); // NOI18N
        label13.setPreferredSize(new java.awt.Dimension(100, 30));
        panelGlass12.add(label13);

        btnHapusOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        btnHapusOrder.setMnemonic('H');
        btnHapusOrder.setText("Hapus");
        btnHapusOrder.setToolTipText("Alt+H");
        btnHapusOrder.setName("btnHapusOrder"); // NOI18N
        btnHapusOrder.setPreferredSize(new java.awt.Dimension(100, 30));
        btnHapusOrder.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnHapusOrderActionPerformed(evt);
            }
        });
        btnHapusOrder.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnHapusOrderKeyPressed(evt);
            }
        });
        panelGlass12.add(btnHapusOrder);

        btnAllOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        btnAllOrder.setMnemonic('M');
        btnAllOrder.setText("Semua");
        btnAllOrder.setToolTipText("Alt+M");
        btnAllOrder.setName("btnAllOrder"); // NOI18N
        btnAllOrder.setPreferredSize(new java.awt.Dimension(100, 30));
        btnAllOrder.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAllOrderActionPerformed(evt);
            }
        });
        btnAllOrder.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnAllOrderKeyPressed(evt);
            }
        });
        panelGlass12.add(btnAllOrder);

        btnPrintOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        btnPrintOrder.setMnemonic('T');
        btnPrintOrder.setText("Cetak");
        btnPrintOrder.setToolTipText("Alt+T");
        btnPrintOrder.setName("btnPrintOrder"); // NOI18N
        btnPrintOrder.setPreferredSize(new java.awt.Dimension(100, 30));
        btnPrintOrder.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrintOrderActionPerformed(evt);
            }
        });
        btnPrintOrder.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnPrintOrderKeyPressed(evt);
            }
        });
        panelGlass12.add(btnPrintOrder);

        btnKeluarOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        btnKeluarOrder.setMnemonic('K');
        btnKeluarOrder.setText("Keluar");
        btnKeluarOrder.setToolTipText("Alt+K");
        btnKeluarOrder.setName("btnKeluarOrder"); // NOI18N
        btnKeluarOrder.setPreferredSize(new java.awt.Dimension(100, 30));
        btnKeluarOrder.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnKeluarOrderActionPerformed(evt);
            }
        });
        btnKeluarOrder.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnKeluarOrderKeyPressed(evt);
            }
        });
        panelGlass12.add(btnKeluarOrder);

        jPanel5.add(panelGlass12, java.awt.BorderLayout.PAGE_START);

        panelBiasa3.add(jPanel5, java.awt.BorderLayout.PAGE_END);

        tabPane.addTab("List Order", panelBiasa3);

        panelBiasa2.setBorder(null);
        panelBiasa2.setName("panelBiasa2"); // NOI18N
        panelBiasa2.setLayout(new java.awt.BorderLayout());

        Scroll1.setName("Scroll1"); // NOI18N
        Scroll1.setOpaque(true);

        tblTransaksi.setAutoCreateRowSorter(true);
        tblTransaksi.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tblTransaksi.setComponentPopupMenu(jPopupMenu2);
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
        Scroll1.setViewportView(tblTransaksi);

        panelBiasa2.add(Scroll1, java.awt.BorderLayout.CENTER);

        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.setOpaque(false);
        jPanel4.setPreferredSize(new java.awt.Dimension(44, 44));
        jPanel4.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass11.setMinimumSize(new java.awt.Dimension(50, 47));
        panelGlass11.setName("panelGlass11"); // NOI18N
        panelGlass11.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 10));

        jLabel33.setText("Tgl :");
        jLabel33.setName("jLabel33"); // NOI18N
        jLabel33.setPreferredSize(new java.awt.Dimension(58, 23));
        panelGlass11.add(jLabel33);

        tglTransaksi1.setEditable(false);
        tglTransaksi1.setForeground(new java.awt.Color(50, 70, 50));
        tglTransaksi1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "28-12-2017" }));
        tglTransaksi1.setDisplayFormat("dd-MM-yyyy");
        tglTransaksi1.setName("tglTransaksi1"); // NOI18N
        tglTransaksi1.setOpaque(false);
        tglTransaksi1.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass11.add(tglTransaksi1);

        jLabel34.setText("s.d");
        jLabel34.setName("jLabel34"); // NOI18N
        jLabel34.setPreferredSize(new java.awt.Dimension(18, 23));
        panelGlass11.add(jLabel34);

        tglTransaksi2.setEditable(false);
        tglTransaksi2.setForeground(new java.awt.Color(50, 70, 50));
        tglTransaksi2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "28-12-2017" }));
        tglTransaksi2.setDisplayFormat("dd-MM-yyyy");
        tglTransaksi2.setName("tglTransaksi2"); // NOI18N
        tglTransaksi2.setOpaque(false);
        tglTransaksi2.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass11.add(tglTransaksi2);

        label14.setText("Key Word :");
        label14.setName("label14"); // NOI18N
        label14.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass11.add(label14);

        txtCariTransaksi.setName("txtCariTransaksi"); // NOI18N
        txtCariTransaksi.setPreferredSize(new java.awt.Dimension(170, 23));
        txtCariTransaksi.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtCariTransaksiKeyPressed(evt);
            }
        });
        panelGlass11.add(txtCariTransaksi);

        btnCariTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        btnCariTransaksi.setMnemonic('6');
        btnCariTransaksi.setToolTipText("Alt+6");
        btnCariTransaksi.setName("btnCariTransaksi"); // NOI18N
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
        panelGlass11.add(btnCariTransaksi);

        label15.setName("label15"); // NOI18N
        label15.setPreferredSize(new java.awt.Dimension(100, 30));
        panelGlass11.add(label15);

        btnHapusTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        btnHapusTransaksi.setMnemonic('H');
        btnHapusTransaksi.setText("Hapus");
        btnHapusTransaksi.setToolTipText("Alt+H");
        btnHapusTransaksi.setName("btnHapusTransaksi"); // NOI18N
        btnHapusTransaksi.setPreferredSize(new java.awt.Dimension(100, 30));
        btnHapusTransaksi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnHapusTransaksiActionPerformed(evt);
            }
        });
        btnHapusTransaksi.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnHapusTransaksiKeyPressed(evt);
            }
        });
        panelGlass11.add(btnHapusTransaksi);

        btnAllTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        btnAllTransaksi.setMnemonic('M');
        btnAllTransaksi.setText("Semua");
        btnAllTransaksi.setToolTipText("Alt+M");
        btnAllTransaksi.setName("btnAllTransaksi"); // NOI18N
        btnAllTransaksi.setPreferredSize(new java.awt.Dimension(100, 30));
        btnAllTransaksi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAllTransaksiActionPerformed(evt);
            }
        });
        btnAllTransaksi.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnAllTransaksiKeyPressed(evt);
            }
        });
        panelGlass11.add(btnAllTransaksi);

        btnPrintTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        btnPrintTransaksi.setMnemonic('T');
        btnPrintTransaksi.setText("Cetak");
        btnPrintTransaksi.setToolTipText("Alt+T");
        btnPrintTransaksi.setName("btnPrintTransaksi"); // NOI18N
        btnPrintTransaksi.setPreferredSize(new java.awt.Dimension(100, 30));
        btnPrintTransaksi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrintTransaksiActionPerformed(evt);
            }
        });
        btnPrintTransaksi.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnPrintTransaksiKeyPressed(evt);
            }
        });
        panelGlass11.add(btnPrintTransaksi);

        btnKeluarTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        btnKeluarTransaksi.setMnemonic('K');
        btnKeluarTransaksi.setText("Keluar");
        btnKeluarTransaksi.setToolTipText("Alt+K");
        btnKeluarTransaksi.setName("btnKeluarTransaksi"); // NOI18N
        btnKeluarTransaksi.setPreferredSize(new java.awt.Dimension(100, 30));
        btnKeluarTransaksi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnKeluarTransaksiActionPerformed(evt);
            }
        });
        btnKeluarTransaksi.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                btnKeluarTransaksiKeyPressed(evt);
            }
        });
        panelGlass11.add(btnKeluarTransaksi);

        jPanel4.add(panelGlass11, java.awt.BorderLayout.PAGE_START);

        panelBiasa2.add(jPanel4, java.awt.BorderLayout.PAGE_END);

        tabPane.addTab("List Transaksi", panelBiasa2);

        internalFrame1.add(tabPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            BtnCariActionPerformed(null);
        }
        else if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
        {
            BtnCari.requestFocus();
        }
        else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP)
        {
            BtnKeluar.requestFocus();
        }
}//GEN-LAST:event_txtCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE)
        {
            BtnCariActionPerformed(null);
        }
        else
        {
            Valid.pindah(evt, txtCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void txtNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoRwKeyPressed
        
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            tindakanBaru();
        }
        else
        Valid.pindah(evt, txtCari, DTPBeri);
}//GEN-LAST:event_txtNoRwKeyPressed

    private void DTPBeriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPBeriKeyPressed
        Valid.pindah(evt, txtNoRw, cmbJam);
}//GEN-LAST:event_DTPBeriKeyPressed

    private void cmbJamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbJamKeyPressed
        Valid.pindah(evt, DTPBeri, cmbMnt);
}//GEN-LAST:event_cmbJamKeyPressed

    private void cmbMntKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMntKeyPressed
        Valid.pindah(evt, cmbJam, cmbDtk);
}//GEN-LAST:event_cmbMntKeyPressed

    private void cmbDtkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbDtkKeyPressed
        
}//GEN-LAST:event_cmbDtkKeyPressed

    private void txtUmurKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUmurKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUmurKeyPressed

    private void txtJkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJkKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJkKeyPressed

    private void tblTransaksiMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblTransaksiMouseClicked
    {//GEN-HEADEREND:event_tblTransaksiMouseClicked
        if (evt.getClickCount() == 2 && tblTransaksi.getSelectedRow() > -1)
        {
            ubahTransaksi();
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

    private void tblOrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOrderMouseClicked
        if (evt.getClickCount() == 2 && tblOrder.getSelectedRow() > -1)
        {
            ubahOrder();
        }
    }//GEN-LAST:event_tblOrderMouseClicked

    private void tblOrderKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblOrderKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblOrderKeyPressed

    private void tblTindakanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTindakanMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTindakanMouseClicked

    private void tblTindakanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblTindakanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTindakanKeyPressed

    private void txtPreTdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPreTdKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPreTdKeyPressed

    private void txtPreBbKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPreBbKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPreBbKeyPressed

    private void txtPreNadiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPreNadiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPreNadiKeyPressed

    private void txtPreResKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPreResKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPreResKeyPressed

    private void txtPostTdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPostTdKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPostTdKeyPressed

    private void txtPostBbKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPostBbKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPostBbKeyPressed

    private void txtPostNadiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPostNadiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPostNadiKeyPressed

    private void txtPostResKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPostResKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPostResKeyPressed

    private void txtHdKeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHdKeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHdKeKeyPressed

    private void txtNamaDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaDokterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaDokterActionPerformed

    private void txtNamaDokterKonsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaDokterKonsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaDokterKonsActionPerformed

    private void txtNamaDokterPjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaDokterPjActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaDokterPjActionPerformed

    private void txtNamaDokterPelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaDokterPelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaDokterPelActionPerformed

    private void tblTindakanPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblTindakanPropertyChange
        if (evt.getPropertyName().equals("tableCellEditor"))
        {
            selKodes.clear();
            selDatas.clear();
            
            for (int a = 0; a < tblTindakan.getRowCount(); a++)
            {
                if (((boolean)tblTindakan.getValueAt(a, 0)))
                {
                    selKodes.add(tblTindakan.getValueAt(a, 1).toString());
                    selDatas.add(new String[]
                    {
                        tblTindakan.getValueAt(a, 1).toString(),
                        tblTindakan.getValueAt(a, 3).toString(),
                        tblTindakan.getValueAt(a, 4).toString(),
                        tblTindakan.getValueAt(a, 5).toString(),
                        tblTindakan.getValueAt(a, 6).toString(),
                        tblTindakan.getValueAt(a, 7).toString(),
                        tblTindakan.getValueAt(a, 8).toString(),
                        tblTindakan.getValueAt(a, 9).toString()
                    });
                }
            }
        }
    }//GEN-LAST:event_tblTindakanPropertyChange

    private void txtNamaPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaPasienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaPasienActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        simpan();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void ckbCheckedStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_ckbCheckedStateChanged
    {//GEN-HEADEREND:event_ckbCheckedStateChanged
        tampilTindakan();
    }//GEN-LAST:event_ckbCheckedStateChanged

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BtnBatalActionPerformed
    {//GEN-HEADEREND:event_BtnBatalActionPerformed
        clearAll();
    }//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BtnKeluarActionPerformed
    {//GEN-HEADEREND:event_BtnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BtnHapusActionPerformed
    {//GEN-HEADEREND:event_BtnHapusActionPerformed
        if (kdPeriksa == null)
            return;
        
        if (GMessage.q("Konfirmasi", "Yakin mau hapus beneran?"))
        {
            boolean success = true;
            GQuery.setAutoCommit(false);
            
            success &= new GQuery()
                    .a("DELETE FROM det_pemeriksaan_hd WHERE kd_periksa = {kd}")
                    .set("kd", kdPeriksa)
                    .write();
            
            success &= new GQuery()
                    .a("DELETE FROM pemeriksaan_hd WHERE kd_periksa = {kd}")
                    .set("kd", kdPeriksa)
                    .write();
            
            GQuery.setAutoCommit(true);
            
            if (success)
            {
                GMessage.i("Sukses", "Hapus data berhasil");
                clearAll();
                tampilTindakan();
                tampilTransaksi();
                tampilOrder();
            }
            else
            {
                GMessage.e("Error", "Hapus data gagal");
            }
        }
    }//GEN-LAST:event_BtnHapusActionPerformed

    private void btnCariOrderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCariOrderActionPerformed
    {//GEN-HEADEREND:event_btnCariOrderActionPerformed
        tampilOrder();
    }//GEN-LAST:event_btnCariOrderActionPerformed

    private void btnCariOrderKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnCariOrderKeyPressed
    {//GEN-HEADEREND:event_btnCariOrderKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCariOrderKeyPressed

    private void txtCariTransaksiKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtCariTransaksiKeyPressed
    {//GEN-HEADEREND:event_txtCariTransaksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariTransaksiKeyPressed

    private void btnHapusTransaksiActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnHapusTransaksiActionPerformed
    {//GEN-HEADEREND:event_btnHapusTransaksiActionPerformed
        hapusTransaksi();
    }//GEN-LAST:event_btnHapusTransaksiActionPerformed

    private void btnHapusTransaksiKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnHapusTransaksiKeyPressed
    {//GEN-HEADEREND:event_btnHapusTransaksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHapusTransaksiKeyPressed

    private void btnAllTransaksiActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAllTransaksiActionPerformed
    {//GEN-HEADEREND:event_btnAllTransaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAllTransaksiActionPerformed

    private void btnAllTransaksiKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnAllTransaksiKeyPressed
    {//GEN-HEADEREND:event_btnAllTransaksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAllTransaksiKeyPressed

    private void btnPrintTransaksiActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPrintTransaksiActionPerformed
    {//GEN-HEADEREND:event_btnPrintTransaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintTransaksiActionPerformed

    private void btnPrintTransaksiKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnPrintTransaksiKeyPressed
    {//GEN-HEADEREND:event_btnPrintTransaksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintTransaksiKeyPressed

    private void btnKeluarTransaksiActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnKeluarTransaksiActionPerformed
    {//GEN-HEADEREND:event_btnKeluarTransaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnKeluarTransaksiActionPerformed

    private void btnKeluarTransaksiKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnKeluarTransaksiKeyPressed
    {//GEN-HEADEREND:event_btnKeluarTransaksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnKeluarTransaksiKeyPressed

    private void txtCariOrderKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtCariOrderKeyPressed
    {//GEN-HEADEREND:event_txtCariOrderKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariOrderKeyPressed

    private void btnHapusOrderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnHapusOrderActionPerformed
    {//GEN-HEADEREND:event_btnHapusOrderActionPerformed
        hapusOrder();
    }//GEN-LAST:event_btnHapusOrderActionPerformed

    private void btnHapusOrderKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnHapusOrderKeyPressed
    {//GEN-HEADEREND:event_btnHapusOrderKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHapusOrderKeyPressed

    private void btnAllOrderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAllOrderActionPerformed
    {//GEN-HEADEREND:event_btnAllOrderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAllOrderActionPerformed

    private void btnAllOrderKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnAllOrderKeyPressed
    {//GEN-HEADEREND:event_btnAllOrderKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAllOrderKeyPressed

    private void btnPrintOrderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPrintOrderActionPerformed
    {//GEN-HEADEREND:event_btnPrintOrderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintOrderActionPerformed

    private void btnPrintOrderKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnPrintOrderKeyPressed
    {//GEN-HEADEREND:event_btnPrintOrderKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintOrderKeyPressed

    private void btnKeluarOrderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnKeluarOrderActionPerformed
    {//GEN-HEADEREND:event_btnKeluarOrderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnKeluarOrderActionPerformed

    private void btnKeluarOrderKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_btnKeluarOrderKeyPressed
    {//GEN-HEADEREND:event_btnKeluarOrderKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnKeluarOrderKeyPressed

    private void menuUbah1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuUbah1ActionPerformed
    {//GEN-HEADEREND:event_menuUbah1ActionPerformed
        ubahTransaksi();
    }//GEN-LAST:event_menuUbah1ActionPerformed

    private void menuHapus1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuHapus1ActionPerformed
    {//GEN-HEADEREND:event_menuHapus1ActionPerformed
        hapusTransaksi();
    }//GEN-LAST:event_menuHapus1ActionPerformed

    private void menuCetakHasilActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuCetakHasilActionPerformed
    {//GEN-HEADEREND:event_menuCetakHasilActionPerformed
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
            String idPeriksa = null;

            // Mencari row utama di tiap transaksi
            for (int a = r; a >= 0; a--)
            {
                if (!tblTransaksi.getValueAt(a, 0).toString().isEmpty())
                {
                    idPeriksa = tblTransaksi.getValueAt(a, 0).toString();
                    break;
                }
            }

            // Kalo gak nemu id periksa
            if (idPeriksa == null)
            {
                JOptionPane.showMessageDialog(null, "Pemeriksaan tidak ditemukan");
                return;
            }

            GRow rMain = new GQuery()
            .a("SELECT pemeriksaan_hd.no_rawat, reg_periksa.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.umur, petugas.nama,")
            .a("    DATE_FORMAT(pemeriksaan_hd.tgl_periksa,'%d-%m-%Y') as tgl_periksa, pemeriksaan_hd.jam_mulai AS jam,")
            .a("    dokter_perujuk.nm_dokter AS nm_dokter_perujuk, dokter_pj.nm_dokter AS nm_dokter_pj, pasien.alamat, DATE_FORMAT(pasien.tgl_lahir,'%d-%m-%Y') as tgl_lahir,")
            .a("    hd_ke, pre_td, pre_bb, pre_nadi, pre_res, pos_td, pos_bb, pos_nadi, pos_res")
            .a("FROM pemeriksaan_hd")
            .a("JOIN reg_periksa ON reg_periksa.no_rawat = pemeriksaan_hd.no_rawat")
            .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
            .a("JOIN petugas ON petugas.nip = pemeriksaan_hd.nip")
            .a("JOIN dokter dokter_perujuk ON dokter_perujuk.kd_dokter = pemeriksaan_hd.kd_dokter_perujuk")
            .a("JOIN dokter dokter_pj ON dokter_pj.kd_dokter = pemeriksaan_hd.kd_dokter_pj")
            .a("WHERE kd_periksa = {id_periksa}")
            .set("id_periksa", idPeriksa)
            .getRowComplete();

            if (rMain != null)
            {
                String kamar = Sequel.cariIsi("select ifnull(kd_kamar,'') from kamar_inap where no_rawat='" + rMain.getString("no_rawat") + "' order by tgl_masuk desc limit 1");
                String namakamar = "";
                
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
                        + "where reg_periksa.no_rawat='" + rMain.getString("no_rawat") + "'");
                }

                // Mengisi RAPOT atas
                Map<String, Object> param = new HashMap<>();
                param.put("noperiksa", rMain.getString("no_rawat"));
                param.put("norm", rMain.getString("no_rkm_medis"));
                param.put("namapasien", rMain.getString("nm_pasien"));
                param.put("jkel", rMain.getString("jk"));
                param.put("umur", rMain.getString("umur"));
                param.put("pengirim", rMain.getString("nm_dokter_perujuk"));
                param.put("tanggal", rMain.getString("tgl_periksa"));
                param.put("penjab", rMain.getString("nm_dokter_pj"));
                param.put("petugas", rMain.getString("nama"));
                param.put("jam", rMain.getString("jam"));
                param.put("alamat", rMain.getString("alamat"));
                param.put("kamar", kamar);
                param.put("namakamar", namakamar);
                
                // Hasil2
                param.put("hd_ke", rMain.getString("hd_ke"));
                param.put("pre_td", rMain.getString("pre_td"));
                param.put("pre_bb", rMain.getString("pre_bb"));
                param.put("pre_nadi", rMain.getString("pre_nadi"));
                param.put("pre_res", rMain.getString("pre_res"));
                param.put("pos_td", rMain.getString("pos_td"));
                param.put("pos_bb", rMain.getString("pos_bb"));
                param.put("pos_nadi", rMain.getString("pos_nadi"));
                param.put("pos_res", rMain.getString("pos_res"));

                // Dari GLOBAL setting
                param.put("namars", var.getnamars());
                param.put("alamatrs", var.getalamatrs());
                param.put("kotars", var.getkabupatenrs());
                param.put("propinsirs", var.getpropinsirs());
                param.put("kontakrs", var.getkontakrs());
                param.put("emailrs", var.getemailrs());
                param.put("logo", Sequel.cariGambar("select logo from setting"));

                GQuery.setAutoCommit(false);
                Sequel.queryu("delete from temporary");

                // Ngambil detail
                GResult resDet = new GQuery()
                .a("SELECT nm_perawatan FROM det_pemeriksaan_hd")
                .a("JOIN jns_perawatan ON jns_perawatan.kd_jenis_prw = det_pemeriksaan_hd.kd_jenis_prw")
                .a("WHERE kd_periksa = {id_periksa}")
                .set("id_periksa", idPeriksa)
                .selectComplete();

                for (GRow row : resDet)
                {
                    new GQuery()
                    .a("INSERT INTO temporary (temp1) VALUES ({temp1})")
                    .set("temp1", row.getString("nm_perawatan"))
                    .write();
                }

                GQuery.setAutoCommit(true);

                Valid.MyReport("rptPeriksaHemodialisaFix.jrxml", "report", "::[ Hasil Pemeriksaan Radiologi ]::",
                    "select no, temp1, temp2 from temporary order by no asc", param);

            }
        }
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_menuCetakHasilActionPerformed

    private void menuCetakBillingActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuCetakBillingActionPerformed
    {//GEN-HEADEREND:event_menuCetakBillingActionPerformed
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
            String idPeriksa = null;

            // Mencari row utama di tiap transaksi
            for (int a = r; a >= 0; a--)
            {
                if (!tblTransaksi.getValueAt(a, 0).toString().isEmpty())
                {
                    idPeriksa = tblTransaksi.getValueAt(a, 0).toString();
                    break;
                }
            }

            // Kalo gak nemu id periksa
            if (idPeriksa == null)
            {
                JOptionPane.showMessageDialog(null, "Pemeriksaan tidak ditemukan");
                return;
            }

            GRow rMain = new GQuery()
            .a("SELECT pemeriksaan_hd.no_rawat, reg_periksa.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.umur, petugas.nama,")
            .a("    DATE_FORMAT(pemeriksaan_hd.tgl_periksa,'%d-%m-%Y') as tgl_periksa, pemeriksaan_hd.jam_mulai AS jam,")
            .a("    dokter_perujuk.nm_dokter AS nm_dokter_perujuk, dokter_pj.nm_dokter AS nm_dokter_pj, pasien.alamat, DATE_FORMAT(pasien.tgl_lahir,'%d-%m-%Y') as tgl_lahir")
            .a("FROM pemeriksaan_hd")
            .a("JOIN reg_periksa ON reg_periksa.no_rawat = pemeriksaan_hd.no_rawat")
            .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
            .a("JOIN petugas ON petugas.nip = pemeriksaan_hd.nip")
            .a("JOIN dokter dokter_perujuk ON dokter_perujuk.kd_dokter = pemeriksaan_hd.kd_dokter_perujuk")
            .a("JOIN dokter dokter_pj ON dokter_pj.kd_dokter = pemeriksaan_hd.kd_dokter_pj")
            .a("WHERE kd_periksa = {id_periksa}")
            .set("id_periksa", idPeriksa)
            .getRowComplete();

            if (rMain != null)
            {
                Sequel.AutoComitFalse();
                Sequel.queryu("delete from temporary");

                int total = 0;

                // Ngambil detail 1
                GResult mDetail1 = new GQuery()
                .a("SELECT nm_perawatan, biaya_rawat AS biaya FROM det_pemeriksaan_hd")
                .a("JOIN jns_perawatan ON jns_perawatan.kd_jenis_prw = det_pemeriksaan_hd.kd_jenis_prw")
                .a("WHERE kd_periksa = {id_periksa}")
                .set("id_periksa", idPeriksa)
                .selectComplete();

                for (GRow row : mDetail1)
                {
                    new GQuery()
                    .a("INSERT INTO temporary (temp1, temp2) VALUES ({temp1}, {temp2})")
                    .set("temp1", row.getString("nm_perawatan"))
                    .set("temp2", row.getString("biaya"))
                    .write();

                    total += row.getInt("biaya");
                }

                Sequel.menyimpan("temporary", "'0', 'Total Biaya Pemeriksaan Hemodialisa','" + total + "','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',''", "Transaksi Biaya Hd");

                Valid.panggilUrl("billing/LaporanBiayaHemodialisa.php?norm=" + rMain.getString("no_rkm_medis") + "&pasien=" + rMain.getString("nm_pasien").replaceAll(" ", "_")
                    + "&tanggal=" + rMain.getString("tgl_periksa") + "&jam=" + rMain.getString("jam") + "&pjlab=" + rMain.getString("nm_dokter_pj").replaceAll(" ", "_")
                    + "&petugas=" + rMain.getString("nama").replaceAll(" ", "_") + "&kasir=" + Sequel.cariIsi("select nama from pegawai where nik=?", var.getkode()));

                Sequel.AutoComitTrue();
            }
        }

        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_menuCetakBillingActionPerformed

    private void menuUbahActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuUbahActionPerformed
    {//GEN-HEADEREND:event_menuUbahActionPerformed
        ubahOrder();
    }//GEN-LAST:event_menuUbahActionPerformed

    private void menuHapusActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuHapusActionPerformed
    {//GEN-HEADEREND:event_menuHapusActionPerformed
        hapusOrder();
    }//GEN-LAST:event_menuHapusActionPerformed

    private void txtKdPetugasKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtKdPetugasKeyPressed
    {//GEN-HEADEREND:event_txtKdPetugasKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
        {
            Sequel.cariIsi("select nama from petugas where nip=?", NmPtg, txtKdPetugas.getText());
        }
        else if (evt.getKeyCode() == KeyEvent.VK_UP)
        {
            btnPetugasActionPerformed(null);
        }
        else
        {

        }
    }//GEN-LAST:event_txtKdPetugasKeyPressed

    private void btnPetugasActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPetugasActionPerformed
    {//GEN-HEADEREND:event_btnPetugasActionPerformed
        var.setform("DlgPeriksaRadiologi");
        petugas.emptTeks();
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth() - 40, internalFrame1.getHeight() - 40);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setVisible(true);
    }//GEN-LAST:event_btnPetugasActionPerformed

    private void menuCetakBilling1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuCetakBilling1ActionPerformed
    {//GEN-HEADEREND:event_menuCetakBilling1ActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        if (mdlOrder.getRowCount() == 0)
        {
            JOptionPane.showMessageDialog(null, "Maaf, data sudah habis...!!!!");
        }
        else if (tblOrder.getSelectedRow() == -1)
        {
            JOptionPane.showMessageDialog(null, "Maaf, Gagal mencteak. Pilih dulu data yang mau dicetak.");
        }
        else
        {
            int r = tblOrder.getSelectedRow();
            String idPeriksa = null;

            // Mencari row utama di tiap transaksi
            for (int a = r; a >= 0; a--)
            {
                if (!tblOrder.getValueAt(a, 0).toString().isEmpty())
                {
                    idPeriksa = tblOrder.getValueAt(a, 0).toString();
                    break;
                }
            }

            // Kalo gak nemu id periksa
            if (idPeriksa == null)
            {
                JOptionPane.showMessageDialog(null, "Pemeriksaan tidak ditemukan");
                return;
            }

            GRow rMain = new GQuery()
            .a("SELECT pemeriksaan_hd.no_rawat, reg_periksa.no_rkm_medis, pasien.nm_pasien, pasien.jk, pasien.umur, petugas.nama,")
            .a("    DATE_FORMAT(pemeriksaan_hd.tgl_periksa,'%d-%m-%Y') as tgl_periksa, pemeriksaan_hd.jam_mulai AS jam,")
            .a("    dokter_perujuk.nm_dokter AS nm_dokter_perujuk, pasien.alamat, DATE_FORMAT(pasien.tgl_lahir,'%d-%m-%Y') as tgl_lahir")
            .a("FROM pemeriksaan_hd")
            .a("JOIN reg_periksa ON reg_periksa.no_rawat = pemeriksaan_hd.no_rawat")
            .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
            .a("JOIN petugas ON petugas.nip = pemeriksaan_hd.nip")
            .a("JOIN dokter dokter_perujuk ON dokter_perujuk.kd_dokter = pemeriksaan_hd.kd_dokter_perujuk")
            .a("WHERE kd_periksa = {id_periksa}")
            .set("id_periksa", idPeriksa)
            .getRowComplete();

            if (rMain != null)
            {
                Sequel.AutoComitFalse();
                Sequel.queryu("delete from temporary");

                int total = 0;

                // Ngambil detail 1
                GResult mDetail1 = new GQuery()
                .a("SELECT nm_perawatan, biaya_rawat AS biaya FROM det_pemeriksaan_hd")
                .a("JOIN jns_perawatan ON jns_perawatan.kd_jenis_prw = det_pemeriksaan_hd.kd_jenis_prw")
                .a("WHERE kd_periksa = {id_periksa}")
                .set("id_periksa", idPeriksa)
                .selectComplete();

                for (GRow row : mDetail1)
                {
                    new GQuery()
                    .a("INSERT INTO temporary (temp1, temp2) VALUES ({temp1}, {temp2})")
                    .set("temp1", row.getString("nm_perawatan"))
                    .set("temp2", row.getString("biaya"))
                    .write();

                    total += row.getInt("biaya");
                }

                Sequel.menyimpan("temporary", "'0', 'Total Biaya Pemeriksaan Hemodialisa','" + total + "','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',''", "Transaksi Biaya Hd");

                String kdDokterPj = Sequel.cariIsi("select kd_dokterhemodialisa from set_pjlab");
                String nmDokterPj = kdDokterPj.isEmpty() ? "-" : Sequel.cariIsi("select nm_dokter from dokter where kd_dokter=?", txtKdDokterPj.getText());
                
                Valid.panggilUrl("billing/LaporanBiayaHemodialisa.php?norm=" + rMain.getString("no_rkm_medis") + "&pasien=" + rMain.getString("nm_pasien").replaceAll(" ", "_")
                    + "&tanggal=" + rMain.getString("tgl_periksa") + "&jam=" + rMain.getString("jam") + "&pjlab=" + nmDokterPj.replaceAll(" ", "_")
                    + "&petugas=" + rMain.getString("nama").replaceAll(" ", "_") + "&kasir=" + Sequel.cariIsi("select nama from pegawai where nik=?", var.getkode()));

                Sequel.AutoComitTrue();
            }
        }

        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_menuCetakBilling1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(()
                -> 
                {
                    DlgPemeriksaanHemodialisa dialog = new DlgPemeriksaanHemodialisa(new javax.swing.JFrame(), true);
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
    private widget.Button BtnAll;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.CekBox ChkJln;
    private widget.Tanggal DTPBeri;
    private widget.PanelBiasa FormInput;
    private widget.Label LCount;
    private widget.TextBox NmPtg;
    private javax.swing.JPanel PanelInput;
    private widget.ScrollPane Scroll1;
    private widget.ScrollPane Scroll2;
    private widget.ScrollPane Scroll3;
    private widget.Button btnAllOrder;
    private widget.Button btnAllTransaksi;
    private widget.Button btnCariOrder;
    private widget.Button btnCariTransaksi;
    private widget.Button btnHapusOrder;
    private widget.Button btnHapusTransaksi;
    private widget.Button btnKeluarOrder;
    private widget.Button btnKeluarTransaksi;
    private widget.Button btnPetugas;
    private widget.Button btnPrintOrder;
    private widget.Button btnPrintTransaksi;
    private widget.Button btnSimpan;
    private widget.CekBox ckbChecked;
    private widget.ComboBox cmbDtk;
    private widget.ComboBox cmbJam;
    private widget.ComboBox cmbMnt;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
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
    private widget.Label jLabel3;
    private widget.Label jLabel33;
    private widget.Label jLabel34;
    private widget.Label jLabel35;
    private widget.Label jLabel36;
    private widget.Label jLabel4;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private widget.Label label12;
    private widget.Label label13;
    private widget.Label label14;
    private widget.Label label15;
    private javax.swing.JMenuItem menuCetakBilling;
    private javax.swing.JMenuItem menuCetakBilling1;
    private javax.swing.JMenuItem menuCetakHasil;
    private javax.swing.JMenuItem menuHapus;
    private javax.swing.JMenuItem menuHapus1;
    private javax.swing.JMenuItem menuUbah;
    private javax.swing.JMenuItem menuUbah1;
    private widget.PanelBiasa panelBiasa1;
    private widget.PanelBiasa panelBiasa2;
    private widget.PanelBiasa panelBiasa3;
    private widget.panelisi panelGlass11;
    private widget.panelisi panelGlass12;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.TabPane tabPane;
    private widget.Table tblOrder;
    private widget.Table tblTindakan;
    private widget.Table tblTransaksi;
    private widget.Tanggal tglOrder1;
    private widget.Tanggal tglOrder2;
    private widget.Tanggal tglTransaksi1;
    private widget.Tanggal tglTransaksi2;
    private widget.TextBox txtAlamat;
    private widget.TextBox txtCari;
    private widget.TextBox txtCariOrder;
    private widget.TextBox txtCariTransaksi;
    private widget.TextBox txtHdKe;
    private widget.TextBox txtJk;
    private widget.TextBox txtKdDokter;
    private widget.TextBox txtKdDokterKons;
    private widget.TextBox txtKdDokterPel;
    private widget.TextBox txtKdDokterPj;
    private widget.TextBox txtKdPetugas;
    private widget.TextBox txtNamaDokter;
    private widget.TextBox txtNamaDokterKons;
    private widget.TextBox txtNamaDokterPel;
    private widget.TextBox txtNamaDokterPj;
    private widget.TextBox txtNamaPasien;
    private widget.TextBox txtNoRm;
    private widget.TextBox txtNoRw;
    private widget.TextBox txtPostBb;
    private widget.TextBox txtPostNadi;
    private widget.TextBox txtPostRes;
    private widget.TextBox txtPostTd;
    private widget.TextBox txtPreBb;
    private widget.TextBox txtPreNadi;
    private widget.TextBox txtPreRes;
    private widget.TextBox txtPreTd;
    private widget.TextBox txtUmur;
    // End of variables declaration//GEN-END:variables

    private void tindakanBaru()
    {
        ResultSet rs = new GStatement(koneksi)
                .a("SELECT reg_periksa.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien,")
                .a("CONCAT(alamat, ' ', nm_kel, ' ', nm_kec, ' ', nm_kab) AS alamat,")
                .a("umur, pasien.jk, reg_periksa.kd_dokter, dper.nm_dokter AS d_per")
                .a("FROM reg_periksa")
                .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                .a("JOIN kelurahan ON kelurahan.kd_kel = pasien.kd_kel")
                .a("JOIN kecamatan ON kecamatan.kd_kec = pasien.kd_kec")
                .a("JOIN kabupaten ON kabupaten.kd_kab = pasien.kd_kab")
                .a("JOIN dokter dper ON dper.kd_dokter = reg_periksa.kd_dokter")
                .a("WHERE reg_periksa.no_rawat = :no")
                .setString("no", txtNoRw.getText())
                .executeQuery();
        
        try
        {
            if (rs.next())
            {
                txtNoRw.setText(rs.getString("no_rawat"));
                txtNoRm.setText(rs.getString("no_rkm_medis"));
                txtNamaPasien.setText(rs.getString("nm_pasien"));
                txtAlamat.setText(rs.getString("alamat"));
                txtUmur.setText(rs.getString("umur"));
                txtJk.setText(rs.getString("jk"));
                txtKdDokter.setText(rs.getString("kd_dokter"));
                txtNamaDokter.setText(rs.getString("d_per"));
            }
            else
            {
                GMessage.w("Warning", "No rawat salah");
            }
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
        }
    }
    
    private void tindakanFromOrder(String kdPeriksa)
    {
        fillData(kdPeriksa, false);
        
        this.kdPeriksa = kdPeriksa;
    }
    
    private void tindakanFromTransaksi(String kdPeriksa)
    {
        fillData(kdPeriksa, true);
        
        this.kdPeriksa = kdPeriksa;
    }
    
    private void fillData(String kdPeriksa, boolean isTransaksi)
    {
        ResultSet rs = new GStatement(koneksi)
                .a("SELECT kd_jenis_prw, material, bhp, tarif_tindakandr, tarif_tindakanpr, kso, manajemen, biaya_rawat")
                .a("FROM det_pemeriksaan_hd")
                .a("WHERE kd_periksa = :kd")
                .setString("kd", kdPeriksa)
                .executeQuery();

        selKodes.clear();
        selDatas.clear();

        try 
        {
            while (rs.next())
            {
                selKodes.add(rs.getString(1));
                selDatas.add(new String[]
                {
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7),
                    rs.getString(8)
                });
            }
        } 
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        tampilTindakan();
        
        ResultSet rs2 = new GStatement(koneksi)
                .a("SELECT reg_periksa.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien,")
                .a("CONCAT(pasien.alamat, ' ', nm_kel, ' ', nm_kec, ' ', nm_kab) AS alamat,")
                .a("umur, pasien.jk, kd_dokter_perujuk, dper.nm_dokter AS d_per,")
                .a("kd_dokter_konsultan, dkon.nm_dokter AS d_kon,")
                .a("kd_dokter_pj, dpj.nm_dokter AS d_pj,")
                .a("kd_dokter_pelaksana, dpel.nm_dokter AS d_pel,")
                .a("pemeriksaan_hd.nip, petugas.nama AS nama_petugas,")
                .a("hd_ke, pre_td, pre_bb, pre_nadi, pre_res, pos_td, pos_bb, pos_nadi, pos_res")
                .a("FROM pemeriksaan_hd")
                .a("JOIN reg_periksa ON reg_periksa.no_rawat = pemeriksaan_hd.no_rawat")
                .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                .a("JOIN kelurahan ON kelurahan.kd_kel = pasien.kd_kel")
                .a("JOIN kecamatan ON kecamatan.kd_kec = pasien.kd_kec")
                .a("JOIN kabupaten ON kabupaten.kd_kab = pasien.kd_kab")
                .a("JOIN dokter dper ON dper.kd_dokter = kd_dokter_perujuk")
                .a("JOIN petugas ON petugas.nip = pemeriksaan_hd.nip")
                .a("LEFT JOIN dokter dkon ON dkon.kd_dokter = kd_dokter_konsultan")
                .a("LEFT JOIN dokter dpj ON dpj.kd_dokter = kd_dokter_pj")
                .a("LEFT JOIN dokter dpel ON dpel.kd_dokter = kd_dokter_pelaksana")
                .a("WHERE kd_periksa = :kd")
                .setString("kd", kdPeriksa)
                .executeQuery();
        
        try
        {
            if (rs2.next())
            {
                txtNoRw.setText(rs2.getString("no_rawat"));
                txtNoRm.setText(rs2.getString("no_rkm_medis"));
                txtNamaPasien.setText(rs2.getString("nm_pasien"));
                txtAlamat.setText(rs2.getString("alamat"));
                txtUmur.setText(rs2.getString("umur"));
                txtJk.setText(rs2.getString("jk"));
                txtKdDokter.setText(rs2.getString("kd_dokter_perujuk"));
                txtNamaDokter.setText(rs2.getString("d_per"));
                txtKdPetugas.setText(rs2.getString("nip"));
                NmPtg.setText(rs2.getString("nama_petugas"));
                
                if (isTransaksi)
                {
                    txtKdDokterKons.setText(rs2.getString("kd_dokter_konsultan"));
                    txtNamaDokterKons.setText(rs2.getString("d_kon"));
                    txtKdDokterPj.setText(rs2.getString("kd_dokter_pj"));
                    txtNamaDokterPj.setText(rs2.getString("d_pj"));
                    txtKdDokterPel.setText(rs2.getString("kd_dokter_pelaksana"));
                    txtNamaDokterPel.setText(rs2.getString("d_pel"));
                    txtHdKe.setText(rs2.getString("hd_ke"));
                    txtPreTd.setText(rs2.getString("pre_td"));
                    txtPreBb.setText(rs2.getString("pre_bb"));
                    txtPreNadi.setText(rs2.getString("pre_nadi"));
                    txtPreRes.setText(rs2.getString("pre_res"));
                    txtPostTd.setText(rs2.getString("pos_td"));
                    txtPostBb.setText(rs2.getString("pos_bb"));
                    txtPostNadi.setText(rs2.getString("pos_nadi"));
                    txtPostRes.setText(rs2.getString("pos_res"));
                }
            }
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
        }
    }
    
    // Status ralan atau ranap
    private String getStatus()
    {
        return new GQuery()
                .a("SELECT COUNT(*) FROM kamar_inap WHERE no_rawat = {no_rwt}")
                .set("no_rwt", txtNoRw.getText())
                .getInt() > 0 ? "Ranap" : "Ralan";
    }
    
    private void simpan()
    {
        if (!valid())
            return;
        
        boolean success = true;
        GQuery.setAutoCommit(false);
        
        success &= new GQuery()
                .a("UPDATE pemeriksaan_hd SET")
                .a("tgl_selesai = {tgl_selesai},")
                .a("jam_selesai = {jam_selesai},")
                .a("hd_ke = {hd_ke},")
                .a("pre_td = {pre_td},")
                .a("pre_bb = {pre_bb},")
                .a("pre_nadi = {pre_nadi},")
                .a("pre_res = {pre_res},")
                .a("pos_td = {pos_td},")
                .a("pos_bb = {pos_bb},")
                .a("pos_nadi = {pos_nadi},")
                .a("pos_res = {pos_res},")
                .a("kd_dokter_konsultan = {konsultan},")
                .a("kd_dokter_pj = {pj},")
                .a("kd_dokter_pelaksana = {pel},")
                .a("status = '1'")
                .a("WHERE kd_periksa = {kd_periksa}")
                .set("tgl_selesai", Valid.SetTgl(DTPBeri.getSelectedItem().toString()))
                .set("jam_selesai", cmbJam.getSelectedItem() + ":" + cmbMnt.getSelectedItem() + ":" + cmbDtk.getSelectedItem())
                .set("hd_ke", txtHdKe.getText())
                .set("pre_td", txtPreTd.getText())
                .set("pre_bb", txtPreBb.getText())
                .set("pre_nadi", txtPreNadi.getText())
                .set("pre_res", txtPreRes.getText())
                .set("pos_td", txtPostTd.getText())
                .set("pos_bb", txtPostBb.getText())
                .set("pos_nadi", txtPostNadi.getText())
                .set("pos_res", txtPostRes.getText())
                .set("konsultan", txtKdDokterKons.getText())
                .set("pj", txtKdDokterPj.getText())
                .set("pel", txtKdDokterPel.getText())
                .set("kd_periksa", kdPeriksa)
                .write();

        success &= new GQuery()
                .a("DELETE FROM det_pemeriksaan_hd WHERE kd_periksa = {kd}")
                .set("kd", kdPeriksa)
                .write();
        
        for (String[] s : selDatas)
        {
            success &= new GQuery()
                    .a("INSERT INTO det_pemeriksaan_hd VALUES ({kd}, {jns}, {material}, {bhp}, {dr}, {pr}, {kso}, {man}, {tot})")
                    .set("kd", kdPeriksa)
                    .set("jns", s[0])
                    .set("material", s[1])
                    .set("bhp", s[2])
                    .set("dr", s[3])
                    .set("pr", s[4])
                    .set("kso", s[5])
                    .set("man", s[6])
                    .set("tot", s[7])
                    .write();
        }
        
        GQuery.setAutoCommit(true);
        
        if (success)
        {
            GMessage.i("Sukses", "Simpan data berhasil");
            
            clearAll();
        }
        else
        {
            GMessage.e("Error", "Error saat menyimpan data");
        }
    }
    
    private boolean valid()
    {
        if (txtNoRw.getText().isEmpty())
        {
            GMessage.e("Error", "Pilih pasien dari transaksi atau order dahulu");
            return false;
        }
        else if (txtKdPetugas.getText().isEmpty() || NmPtg.getText().isEmpty())
        {
            GMessage.e("Error", "Pilih petugas dahulu");
            return false;
        }
        else if (txtKdDokterKons.getText().isEmpty())
        {
            GMessage.e("Error", "Pilih dokter konsultan dahulu");
            return false;
        }
        else if (txtKdDokterPj.getText().isEmpty())
        {
            GMessage.e("Error", "Pilih dokter PJ dahulu");
            return false;
        }
        else if (txtKdDokterPel.getText().isEmpty())
        {
            GMessage.e("Error", "Pilih dokter pelaksana dahulu");
            return false;
        }
        else if (txtHdKe.getText().isEmpty())
        {
            GMessage.e("Error", "Hd ke tidak boleh kosong");
            return false;
        }
        else if (txtPreTd.getText().isEmpty())
        {
            GMessage.e("Error", "Pre TD tidak boleh kosong");
            return false;
        }
        else if (txtPreBb.getText().isEmpty())
        {
            GMessage.e("Error", "Pre BB tidak boleh kosong");
            return false;
        }
        else if (txtPreNadi.getText().isEmpty())
        {
            GMessage.e("Error", "Pre NADI tidak boleh kosong");
            return false;
        }
        else if (txtPreRes.getText().isEmpty())
        {
            GMessage.e("Error", "Pre RES tidak boleh kosong");
            return false;
        }
        else if (txtPostTd.getText().isEmpty())
        {
            GMessage.e("Error", "Post TD tidak boleh kosong");
            return false;
        }
        else if (txtPostBb.getText().isEmpty())
        {
            GMessage.e("Error", "Post BB tidak boleh kosong");
            return false;
        }
        else if (txtPostNadi.getText().isEmpty())
        {
            GMessage.e("Error", "Post NADI tidak boleh kosong");
            return false;
        }
        else if (txtPostRes.getText().isEmpty())
        {
            GMessage.e("Error", "Post RES tidak boleh kosong");
            return false;
        }
        else if (selKodes.isEmpty())
        {
            GMessage.e("Error", "Tidak ada tindakan yang dipilih");
            return false;
        }
        else
        {
            return true;
        }
    }

    private void isForm()
    {
        
    }

    public void isCek()
    {
        btnSimpan.setEnabled(var.getberi_obat());
        BtnHapus.setEnabled(var.getberi_obat());
        BtnPrint.setEnabled(var.getberi_obat());

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

    private void ubahTransaksi()
    {
        if (tblTransaksi.getSelectedRow() == -1)
        {
            GMessage.w("Pilih", "Pilih data dahulu");
            return;
        }
        
        tabPane.setSelectedIndex(0);
        tindakanFromTransaksi(tblTransaksi.getValueAt(getTransaksiMainRow(tblTransaksi.getSelectedRow()), 0).toString());
    }

    private void hapusTransaksi()
    {
        if (tblTransaksi.getSelectedRow() == -1)
        {
            GMessage.w("Pilih", "Pilih data dahulu");
            return;
        }
        
        int reply = JOptionPane.showConfirmDialog(rootPane, "Eeiiiiiits, beneran mau hapus..??", "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (reply == JOptionPane.YES_OPTION)
        {
            GQuery.setAutoCommit(false);

            boolean isSuccess = true;

            String kdPeriksa = tblTransaksi.getValueAt(getTransaksiMainRow(tblTransaksi.getSelectedRow()), 0).toString();

            isSuccess &= new GQuery()
                    .a("DELETE FROM pemeriksaan_hd WHERE kd_periksa = {kd_periksa}")
                    .set("kd_periksa", kdPeriksa)
                    .write();

            GQuery.setAutoCommit(true);

            if (isSuccess)
            {
                JOptionPane.showMessageDialog(rootPane, "Hapus data berhasil", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                tampilTransaksi();
            }
            else
            {
                JOptionPane.showMessageDialog(rootPane, "Hapus data gagal", "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ubahOrder()
    {
        if (tblOrder.getSelectedRow() == -1)
        {
            GMessage.w("Pilih", "Pilih data dahulu");
            return;
        }
        
        tabPane.setSelectedIndex(0);
        tindakanFromOrder(tblOrder.getValueAt(getOrderMainRow(tblOrder.getSelectedRow()), 0).toString());
    }

    private void hapusOrder()
    {
        if (tblOrder.getSelectedRow() == -1)
        {
            GMessage.w("Pilih", "Pilih data dahulu");
            return;
        }
        
        int reply = JOptionPane.showConfirmDialog(rootPane, "Eeiiiiiits, beneran mau hapus..??", "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (reply == JOptionPane.YES_OPTION)
        {
            GQuery.setAutoCommit(false);

            boolean isSuccess = true;

            String kdPeriksa = tblOrder.getValueAt(getOrderMainRow(tblOrder.getSelectedRow()), 0).toString();

            isSuccess &= new GQuery()
                    .a("DELETE FROM pemeriksaan_hd WHERE kd_periksa = {kd_periksa}")
                    .set("kd_periksa", kdPeriksa)
                    .write();

            GQuery.setAutoCommit(true);

            if (isSuccess)
            {
                JOptionPane.showMessageDialog(rootPane, "Hapus data berhasil", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                tampilOrder();
            }
            else
            {
                JOptionPane.showMessageDialog(rootPane, "Hapus data gagal", "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private int getOrderMainRow(int row)
    {
        int rowMain = row;

        if (tblOrder.getValueAt(row, 0).toString().isEmpty())
        {
            for (int a = row; a >= 0; a--)
            {
                if (!tblOrder.getValueAt(a, 0).toString().isEmpty())
                {
                    rowMain = a;
                    break;
                }
            }
        }

        return rowMain;
    }
    
    private int getTransaksiMainRow(int row)
    {
        int rowMain = row;

        if (tblTransaksi.getValueAt(row, 0).toString().isEmpty())
        {
            for (int a = row; a >= 0; a--)
            {
                if (!tblTransaksi.getValueAt(a, 0).toString().isEmpty())
                {
                    rowMain = a;
                    break;
                }
            }
        }

        return rowMain;
    }
}

