/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fungsi;

import fungsi.EntPemberianDiet;
import fungsi.koneksiDB;
import fungsi.sekuel;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class impPemberianDiet {
    private koneksiDB konek;
    private ResultSet rsBeriDiet;
    private String query;
    private boolean status;
    private sekuel sequel;
    List<EntPemberianDiet> listBeriDiet = new ArrayList<EntPemberianDiet>();

    public impPemberianDiet() {
    sequel = new sekuel();
    sequel.Konek();
    }
    
    public boolean editPemberianDiet_Ranap(EntPemberianDiet pd){
        status = false;
        query = "UPDATE detail_Beri_Diet Set Bentuk_Makanan = '"+pd.getBentuk_makanan()+"',"
                + "Jenis_diet = '"+pd.getJenis_diet()+"',"
                + "ekstra = '"+pd.getEkstra()+"' "
                + "WHERE no_rawat = '"+pd.getNo_rawat()+"' and kd_Kamar = '"+pd.getKd_kamar()+"' and "
                + "tanggal = '"+pd.getTanggal()+"' and waktu = '"+pd.getWaktu()+"'";
        status = sequel.executeQuery(query, false);
        System.out.println(query);
        return status;
    }
    
    public boolean editPemberianDiet_Ralan(EntPemberianDiet pd){
        status = false;
        query = "UPDATE detail_Beri_Diet_Ralan Set Bentuk_Makanan = '"+pd.getBentuk_makanan()+"',"
                + "Jenis_diet = '"+pd.getJenis_diet()+"',"
                + "ekstra = '"+pd.getEkstra()+"' "
                + "WHERE no_rawat = '"+pd.getNo_rawat()+"' and tanggal = '"+pd.getTanggal()+"' and waktu = '"+pd.getWaktu()+"'";
        status = sequel.executeQuery(query, false);
        System.out.println(query);
        return status;
    }
    
    public boolean ubahStatusRanap(String tgl1, String tgl2, String waktu){
        status = false;
        query = "UPDATE detail_Beri_Diet SET Status = 'PRINT' WHERE Tanggal between '"+tgl1+"' and '"+tgl2+"' and waktu = '"+waktu+"'";
        status = sequel.executeQuery(query, false);
        System.out.println(query);
        return status;
    }

    public boolean ubahStatusRanap_All(String tgl1, String tgl2){
        status = false;
        query = "UPDATE detail_Beri_Diet SET Status = 'PRINT' WHERE Tanggal between '"+tgl1+"' and '"+tgl2+"'";
        status = sequel.executeQuery(query, false);
        System.out.println(query);
        return status;
    }
    
    public boolean ubahStatusRalan(String tgl1, String tgl2, String waktu){
        status = false;
        query = "UPDATE detail_Beri_Diet_Ralan SET Status = 'PRINT' WHERE Tanggal between '"+tgl1+"' and '"+tgl2+"' and waktu = '"+waktu+"'";
        status = sequel.executeQuery(query, false);
        System.out.println(query);
        return status;
    }

    public boolean ubahStatusRalan_All(String tgl1, String tgl2){
        status = false;
        query = "UPDATE detail_Beri_Diet_Ralan SET Status = 'PRINT' WHERE Tanggal between '"+tgl1+"' and '"+tgl2+"'";
        status = sequel.executeQuery(query, false);
        System.out.println(query);
        return status;
    }
   
     public boolean ubahStatusRanap_status(String tgl1, String tgl2, String statusPrint){
        status = false;
        query = "UPDATE detail_Beri_Diet SET Status = 'PRINT' WHERE Tanggal between '"+tgl1+"' and '"+tgl2+"' and status = '"+statusPrint+"'";
        status = sequel.executeQuery(query, false);
        System.out.println(query);
        return status;
        
    }
    
     public boolean ubahStatusRalan_status(String tgl1, String tgl2, String statusPrint){
        status = false;
        query = "UPDATE detail_Beri_Diet_Ralan SET Status = 'PRINT' WHERE Tanggal between '"+tgl1+"' and '"+tgl2+"' and status = '"+statusPrint+"'";
        status = sequel.executeQuery(query, false);
        System.out.println(query);
        return status;
        
    }

    
     public boolean ubahStatusRalan_Waktu(String tgl1, String tgl2, String waktu){
        status = false;
        query = "UPDATE detail_Beri_Diet_Ralan SET Status = 'PRINT' WHERE Tanggal between '"+tgl1+"' and '"+tgl2+"' and waktu = '"+waktu+"'";
        status = sequel.executeQuery(query, false);
        System.out.println(query);
        return status;
        
    }

public boolean ubahStatusRanap_Waktu(String tgl1, String tgl2, String waktu){
        status = false;
        query = "UPDATE detail_Beri_Diet SET Status = 'PRINT' WHERE Tanggal between '"+tgl1+"' and '"+tgl2+"' and waktu = '"+waktu+"'";
        status = sequel.executeQuery(query, false);
        System.out.println(query);
        return status;
        
    }     

public boolean ubahStatusRanap_StatusWaktu(String tgl1, String tgl2, String waktu, String sts){
        status = false;
        query = "UPDATE detail_Beri_Diet SET Status = 'PRINT' WHERE Tanggal between '"+tgl1+"' and '"+tgl2+"'"
                + " and waktu = '"+waktu+"' and status = '"+sts+"'";
        status = sequel.executeQuery(query, false);
        System.out.println(query);
        return status;
        
    }

public boolean ubahStatusRalan_StatusWaktu(String tgl1, String tgl2, String waktu, String sts){
        status = false;
        query = "UPDATE detail_Beri_Diet_Ralan SET Status = 'PRINT' WHERE Tanggal between '"+tgl1+"' and '"+tgl2+"'"
                + " and waktu = '"+waktu+"' and status = '"+sts+"'";
        status = sequel.executeQuery(query, false);
        System.out.println(query);
        return status;
        
    }  

public boolean ubahStatusRanap_print(String tgl, String waktu, String noRw, String kd_Kamar, String jenisDiet ){
        status = false;
        query = "UPDATE detail_Beri_Diet SET Status = 'PRINT' WHERE Tanggal = '"+tgl+"' and waktu = '"+waktu+"' and "
                + "no_rawat = '"+noRw+"' and kd_Kamar = '"+kd_Kamar+"' and jenis_Diet = '"+jenisDiet+"'";
        status = sequel.executeQuery(query, false);
        System.out.println(query);
        return status;
        
    }  


public boolean ubahStatusRalan_print(String tgl, String waktu, String noRw, String jenisDiet ){
        status = false;
        query = "UPDATE detail_Beri_Diet_Ralan SET Status = 'PRINT' WHERE Tanggal = '"+tgl+"' and waktu = '"+waktu+"' and "
                + "no_rawat = '"+noRw+"' and jenis_Diet = '"+jenisDiet+"'";
        status = sequel.executeQuery(query, false);
        System.out.println(query);
        return status;
        
    }  



    public static void main(String[] ar){
        System.out.println("query");
    }
    
}
