/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fungsi;


/**
 *
 * @author ASUS
 */

public class EntPemberianDiet {
    private String no_rawat;
    private String kd_kamar;
    private String tanggal;
    private String waktu;
    private String bentuk_makanan;
    private String jenis_diet;
    private String ekstra;
    private String NamaPasien;
    private String Diagnosa;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    

    public String getNamaPasien() {
        return NamaPasien;
    }

    public void setNamaPasien(String NamaPasien) {
        this.NamaPasien = NamaPasien;
    }

    public String getDiagnosa() {
        return Diagnosa;
    }

    public void setDiagnosa(String Diagnosa) {
        this.Diagnosa = Diagnosa;
    }
    

    public String getNo_rawat() {
        return no_rawat;
    }

    public void setNo_rawat(String no_rawat) {
        this.no_rawat = no_rawat;
    }

    public String getKd_kamar() {
        return kd_kamar;
    }

    public void setKd_kamar(String kd_kamar) {
        this.kd_kamar = kd_kamar;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getBentuk_makanan() {
        return bentuk_makanan;
    }

    public void setBentuk_makanan(String bentuk_makanan) {
        this.bentuk_makanan = bentuk_makanan;
    }

    public String getJenis_diet() {
        return jenis_diet;
    }

    public void setJenis_diet(String jenis_diet) {
        this.jenis_diet = jenis_diet;
    }

    public String getEkstra() {
        return ekstra;
    }

    public void setEkstra(String ekstra) {
        this.ekstra = ekstra;
    }
    
    
}
