/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simrskhanza.cari;

import fungsi.GQuery;
import fungsi.GResult;
import simrskhanza.cari.DlgCari;
import java.awt.Frame;

public class DlgCariReg extends DlgCari
{
    public final int NO_RKM_MEDIS = 0;
    public final int NO_RAWAT = 1;
    public final int NAMA_PASIEN = 2;
    public final int TGL_REGISTRASI = 3;
    public final int KD_DOKTER = 4;
    public final int NAMA_DOKTER = 5;
    public final int KD_KAMAR_POLI = 6;
    public final int KAMAR_POLI = 7;
    public final int STATUS = 8;
    
    public DlgCariReg(Frame parent, boolean modal)
    {
        super(parent, modal);
    }

    @Override
    protected Object[] getColumns()
    {
        return new Object[]
        {
            "No Rkm Medis", "No Rawat", "Nama Pasien", "Tgl Registrasi", "Kd Dokter", "Dokter", "Kd Kamar/Poli", "Kamar/Poli", "Status"
        };
    }

    @Override
    protected int[] getColumnSizes()
    {
        return new int[] 
        {
            120, 120, 200, 100, 100, 200, 100, 120, 120
        };
    }

    @Override
    protected GResult getData(String dari, String sampai, String key)
    {
        return new GQuery()
                .a("SELECT reg_periksa.no_rkm_medis, reg_periksa.no_rawat, nm_pasien, tgl_registrasi, dokter.kd_dokter, nm_dokter,")
                .a("IF (nm_bangsal IS NULL, poliklinik.kd_poli, kamar.kd_kamar) AS kd_kamar_poli,")
                .a("IF (nm_bangsal IS NULL, nm_poli, CONCAT(kamar.kd_kamar, nm_bangsal)) AS kamar_poli,")
                .a("IF (nm_bangsal IS NULL, 'Ralan', 'Ranap') AS status")
                .a("FROM reg_periksa")
                .a("JOIN pasien ON pasien.no_rkm_medis = reg_periksa.no_rkm_medis")
                .a("JOIN dokter ON dokter.kd_dokter = reg_periksa.kd_dokter")
                .a("JOIN poliklinik ON poliklinik.kd_poli = reg_periksa.kd_poli")
                .a("LEFT JOIN kamar_inap ON kamar_inap.no_rawat = reg_periksa.no_rawat")
                .a("LEFT JOIN kamar ON kamar.kd_kamar = kamar_inap.kd_kamar")
                .a("LEFT JOIN bangsal ON bangsal.kd_bangsal = kamar.kd_bangsal")
                .a("WHERE (reg_periksa.no_rkm_medis LIKE {cari}")
                .a("OR reg_periksa.no_rawat LIKE {cari}")
                .a("OR nm_pasien LIKE {cari})")
                .a(" AND tgl_registrasi BETWEEN {dari} AND {sampai}")
                .set("cari", "%" + key + "%")
                .set("dari", dari)
                .set("sampai", sampai)
                .selectComplete();
    }
    
}
