/*
  Dilarang keras menggandakan/mengcopy/menyebarkan/membajak/mendecompile 
  Software ini dalam bentuk apapun tanpa seijin pembuat software
  (Khanza.Soft Media). Bagi yang sengaja membajak softaware ini ta
  npa ijin, kami sumpahi sial 1000 turunan, miskin sampai 500 turu
  nan. Selalu mendapat kecelakaan sampai 400 turunan. Anak pertama
  nya cacat tidak punya kaki sampai 300 turunan. Susah cari jodoh
  sampai umur 50 tahun sampai 200 turunan. Ya Alloh maafkan kami 
  karena telah berdoa buruk, semua ini kami lakukan karena kami ti
  dak pernah rela karya kami dibajak tanpa ijin.
 */

package fungsi;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import uz.ncipro.calendar.JDateTimePicker;

/**
 *
 * @author Owner
 */
public final class sekuel {
    private javax.swing.ImageIcon icon = null;
    private javax.swing.ImageIcon iconThumbnail = null;
    private String folder;    
    private final Connection connect=koneksiDB.condb();
    private PreparedStatement ps;
    private ResultSet rs;
    private int angka=0;
    private double angka2=0;
    private String dicari="";
    private Date tanggal=new Date();
    private boolean bool=false;
    private DecimalFormat df2 = new DecimalFormat("####");
    private koneksiDB konek;

    public koneksiDB Konek() {
        konek = new koneksiDB();
        konek.condb();
        return konek;
    }
    public sekuel(){
        super();
    }


    public void menyimpan(String table,String value,String sama){
        try {
            ps=connect.prepareStatement("insert into "+table+" values("+value+")");
            try{                  
                ps.executeUpdate();
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);            
                JOptionPane.showMessageDialog(null,"Maaf, gagal menyimpan data. Kemungkinan ada "+sama+" yang sama dimasukkan sebelumnya...!");
            }finally{
                if(ps != null){
                    ps.close();
                }                
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e); 
        }            
    }
    
    public void menyimpan2(String table,String value,String sama){
        try {
            ps=connect.prepareStatement("insert into "+table+" values("+value+")");
            try{                  
                ps.executeUpdate();
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);    
            }finally{
                if(ps != null){
                    ps.close();
                }                
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e); 
        }            
    }
    
    public boolean menyimpantf(String table,String value,String sama){
        try {
            ps=connect.prepareStatement("insert into "+table+" values("+value+")");
            ps.executeUpdate();
            if(ps != null){
                ps.close();
            }  
            return true;           
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e); 
            JOptionPane.showMessageDialog(null,"Maaf, gagal menyimpan data. Kemungkinan ada "+sama+" yang sama dimasukkan sebelumnya...!");
            return false;
        }            
    }
    
    public boolean menyimpantf2(String table,String value,String sama){
        try {
            ps=connect.prepareStatement("insert into "+table+" values("+value+")");
            ps.executeUpdate();
            if(ps != null){
                ps.close();
            }  
            return true;           
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e); 
            return false;
        }            
    }
    
    public void menyimpan(String table,String value,String sama,int i,String[] a){
        try {
            ps=connect.prepareStatement("insert into "+table+" values("+value+")");
            try{
                for(angka=1;angka<=i;angka++){
                    ps.setString(angka,a[angka-1]);
                }            
                ps.executeUpdate();
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);            
                JOptionPane.showMessageDialog(null,"Maaf, gagal menyimpan data. Kemungkinan ada "+sama+" yang sama dimasukkan sebelumnya...!");
            }finally{
                if(ps != null){
                    ps.close();
                }                
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e); 
        }            
    }
    
    public void menyimpan2(String table,String value,String sama,int i,String[] a){
        try {
            ps=connect.prepareStatement("insert into "+table+" values("+value+")");
            try{
                for(angka=1;angka<=i;angka++){
                    ps.setString(angka,a[angka-1]);
                }            
                ps.executeUpdate();
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);            
            }finally{
                if(ps != null){
                    ps.close();
                }                
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);            
        }    
    }
    
    public boolean menyimpantf(String table,String value,String sama,int i,String[] a){        
        try{             
            ps=connect.prepareStatement("insert into "+table+" values("+value+")");
            for(angka=1;angka<=i;angka++){
                ps.setString(angka,a[angka-1]);
            }            
            ps.executeUpdate();
            
            if(ps != null){
                ps.close();
            }
            return true;
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);  
            if(e.toString().contains("Duplicate")){
                JOptionPane.showMessageDialog(null,"Maaf, gagal menyimpan data. Kemungkinan ada "+sama+" yang sama dimasukkan sebelumnya...!");
            }else{
                JOptionPane.showMessageDialog(null,"Maaf, gagal menyimpan data. Ada kesalahan Query...!");
            }
            return false;
        }
    }
    
    public boolean menyimpantf2(String table,String value,String samra,int i,String[] a){
        bool=true;
        try{ 
            ps=connect.prepareStatement("insert into "+table+" values("+value+")");
            try {
                for(angka=1;angka<=i;angka++){
                    ps.setString(angka,a[angka-1]);
                }            
                ps.executeUpdate();
                bool=true;
            } catch (Exception e) {
                bool=false;
                System.out.println("Notifikasi : "+e.getMessage());  
            } finally{
                if(ps != null){
                    ps.close();
                }
            }
        }catch(Exception e){
            bool=false;
            System.out.println("Notifikasi : "+e);  
        }
        return bool;
    }
    
    public void menyimpan(String table,String value,int i,String[] a){
        try {
            ps=connect.prepareStatement("insert into "+table+" values("+value+")");
            try{                 
                for(angka=1;angka<=i;angka++){
                    ps.setString(angka,a[angka-1]);
                }            
                ps.executeUpdate();
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);            
            }finally{
                if(ps != null){
                    ps.close();
                }                
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);   
        }            
    }
    
    public void menyimpan(String table,String value,int i,String[] a,String acuan_field,String update,int j,String[] b){
        try{ 
            ps=connect.prepareStatement("insert into "+table+" values("+value+")");
            for(angka=1;angka<=i;angka++){
                ps.setString(angka,a[angka-1]);
            }            
            ps.executeUpdate();
            
            if(ps != null){
                ps.close();
            } 
        }catch(Exception e){
            try {
                ps=connect.prepareStatement("update "+table+" set "+update+" where "+acuan_field);
                for(angka=1;angka<=j;angka++){
                    ps.setString(angka,b[angka-1]);
                } 
                ps.executeUpdate();   
                
                if(ps != null){
                    ps.close();
                } 
            } catch (Exception e2) {
                System.out.println("Notifikasi : "+e2);
            }                         
        }
    }
    
    public void menyimpan2(String table,String value,int i,String[] a,String acuan_field,String update,int j,String[] b){
        try{ 
            dicari="";
            ps=connect.prepareStatement("insert into "+table+" values("+value+")");
            for(angka=1;angka<=i;angka++){
                dicari=dicari+", "+a[angka-1];
                ps.setString(angka,a[angka-1]);
            }            
            ps.executeUpdate();
            ps=connect.prepareStatement("insert into trackersql values( now(),'insert into "+table+" values("+dicari+")','','jadwal pegawai "+var.getuser()+"','simpan')");
            ps.executeUpdate();
            
            if(ps != null){
                  ps.close();
            } 
        }catch(Exception e){
            try {
                dicari="";
                ps=connect.prepareStatement("update "+table+" set "+update+" where "+acuan_field);
                for(angka=1;angka<=j;angka++){
                    dicari=dicari+", "+a[angka-1];
                    ps.setString(angka,b[angka-1]);
                } 
                ps.executeUpdate(); 
                ps=connect.prepareStatement("insert into trackersql values( now(),'update "+table+" set "+update.replaceAll("'","")+" where "+acuan_field.replaceAll("'","")+" "+dicari+"','"+e.toString().replaceAll("'","")+"','jadwal pegawai "+var.getuser()+"','update')");
                ps.executeUpdate();
                
                if(ps != null){
                    ps.close();
                } 
            } catch (Exception e2) {                
                System.out.println("Notifikasi : "+e2);
                dicari="";
                for(angka=1;angka<=j;angka++){
                    dicari=dicari+", "+a[angka-1];
                }
                menyimpan("trackersql","now(),'update "+table+" set "+update.replaceAll("'","")+" where "+acuan_field.replaceAll("'","")+" "+dicari+"','"+e.toString().replaceAll("'","")+"','jadwal pegawai "+var.getuser()+"','update'");
            }
                         
        }
    }
    
    public void menyimpan3(String table,String value,int i,String[] a,String acuan_field,String update,int j,String[] b){
        try{ 
            ps=connect.prepareStatement("insert into "+table+" values("+value+")");
            for(angka=1;angka<=i;angka++){
                ps.setString(angka,a[angka-1]);
            }            
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(null,"Proses simpan berhasil..!!");
            if(ps != null){
                ps.close();
            } 
        }catch(Exception e){
            try {
                ps=connect.prepareStatement("update "+table+" set "+update+" where "+acuan_field);
                for(angka=1;angka<=j;angka++){
                    ps.setString(angka,b[angka-1]);
                } 
                ps.executeUpdate();   
                
                JOptionPane.showMessageDialog(null,"Proses simpan berhasil..!!");
                if(ps != null){
                    ps.close();
                } 
            } catch (Exception e2) {
                System.out.println("Notifikasi : "+e2);
            }                         
        }
    }
    
    public void menyimpan(String table,String value){
        try {
            ps=connect.prepareStatement("insert into "+table+" values("+value+")");
            try{
                ps.executeUpdate();
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);         
            }finally{
                if(ps != null){
                    ps.close();
                }                
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);  
        }
    }
    
    public void menyimpan(String table,String isisimpan,String isiedit,String acuan_field){
        try{            
            ps=connect.prepareStatement("insert into "+table+" values("+isisimpan+")");
            ps.executeUpdate();   
            if(ps != null){
                ps.close();
            }  
        }catch(Exception e){
            try {
                ps=connect.prepareStatement("update "+table+" set "+isiedit+" where "+acuan_field);
                ps.executeUpdate();
                if(ps != null){
                    ps.close();
                }  
            } catch (Exception ex) {
                System.out.println("Notifikasi Edit : "+ex);
            }
        }
    }

    public void menyimpan(String table,String value,String sama,JTextField AlmGb){
        try {
            ps = connect.prepareStatement("insert into "+table+" values("+value+",?)");
            try{                        
                ps.setBinaryStream(1, new FileInputStream(AlmGb.getText()), new File(AlmGb.getText()).length());
                ps.executeUpdate();
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                JOptionPane.showMessageDialog(null,"Maaf, gagal menyimpan data. Kemungkinan ada "+sama+" yang sama dimasukkan sebelumnya...!");
            }finally{
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
            
    }
    
    public void menyimpan(String table,String value,String sama,JTextField AlmGb,JTextField AlmPhoto){
        try {
            ps = connect.prepareStatement("insert into "+table+" values("+value+",?,?)");
            try{                        
                ps.setBinaryStream(1, new FileInputStream(AlmGb.getText()), new File(AlmGb.getText()).length());
                ps.setBinaryStream(2, new FileInputStream(AlmPhoto.getText()), new File(AlmPhoto.getText()).length());
                ps.executeUpdate();
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                JOptionPane.showMessageDialog(null,"Maaf, gagal menyimpan data. Kemungkinan ada "+sama+" yang sama dimasukkan sebelumnya...!");
            }finally{
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
    }
    

    public void meghapus(String table,String field,String nilai_field) {
        try {
            ps=connect.prepareStatement("delete from "+table+" where "+field+"=?");
            try{       
                ps.setString(1,nilai_field);
                ps.executeUpdate(); 
             }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                JOptionPane.showMessageDialog(null,"Maaf, data gagal dihapus. Kemungkinan data tersebut masih dipakai di table lain...!!!!");
             }finally{
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
    }
    
    public void meghapus2(String table,String field,String nilai_field) {
        try {
            ps=connect.prepareStatement("delete from "+table+" where "+field+"=?");
            try{       
                ps.setString(1,nilai_field);
                ps.executeUpdate(); 
                JOptionPane.showMessageDialog(null,"Proses hapus berhasil...!!!!");
             }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                JOptionPane.showMessageDialog(null,"Maaf, data gagal dihapus. Kemungkinan data tersebut masih dipakai di table lain...!!!!");
             }finally{
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
    }
    

    public void mengedit(String table,String acuan_field,String update){
        try {
            ps=connect.prepareStatement("update "+table+" set "+update+" where "+acuan_field);
            try{                        
                ps.executeUpdate();       
             }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                JOptionPane.showMessageDialog(null,"Maaf, Gagal Mengedit. Mungkin kode sudah digunakan sebelumnya...!!!!");
             }finally{
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
    }
    
    public boolean mengedittf(String table,String acuan_field,String update){
        bool=true;
        try {
            ps=connect.prepareStatement("update "+table+" set "+update+" where "+acuan_field);
            try{                        
                ps.executeUpdate();  
                bool=true;
             }catch(Exception e){
                bool=false;
                System.out.println("Notifikasi : "+e);
                JOptionPane.showMessageDialog(null,"Maaf, Gagal Mengedit. Mungkin kode sudah digunakan sebelumnya...!!!!");
             }finally{
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            bool=false;
            System.out.println("Notifikasi : "+e);
        }
        return bool;
    }
    
    public void mengedit(String table,String acuan_field,String update,int i,String[] a){
        try {
            ps=connect.prepareStatement("update "+table+" set "+update+" where "+acuan_field);
            try{
                for(angka=1;angka<=i;angka++){
                    ps.setString(angka,a[angka-1]);
                } 
                ps.executeUpdate();       
             }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                JOptionPane.showMessageDialog(null,"Maaf, Gagal Mengedit. Periksa kembali data...!!!!" + " " + table + " " + update);
             }finally{
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }    
    }
    
    public void mengedit2(String table,String acuan_field,String update,int i,String[] a){
        try {
            ps=connect.prepareStatement("update "+table+" set "+update+" where "+acuan_field);
            try{
                for(angka=1;angka<=i;angka++){
                    ps.setString(angka,a[angka-1]);
                } 
                ps.executeUpdate();   
                JOptionPane.showMessageDialog(null,"Proses edit berhasil...!!!!");
             }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                JOptionPane.showMessageDialog(null,"Maaf, Gagal mengedit. Periksa kembali data...!!!!");
             }finally{
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }    
    }
    public void mengedit3(String table,String acuan_field,String update,int i,String[] a){
        try {
            ps=connect.prepareStatement("update "+table+" set "+update+" where "+acuan_field);
            try{
                for(angka=1;angka<=i;angka++){
                    ps.setString(angka,a[angka-1]);
                } 
                ps.executeUpdate();       
             }catch(Exception e){
                System.out.println("Notifikasi : "+e);
             }finally{
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }    
    }    
    
    public boolean mengedittf(String table,String acuan_field,String update,int i,String[] a){
        bool=true;
        try {
            ps=connect.prepareStatement("update "+table+" set "+update+" where "+acuan_field);
            try{
                for(angka=1;angka<=i;angka++){
                    ps.setString(angka,a[angka-1]);
                } 
                ps.executeUpdate();       
                bool=true;
             }catch(Exception e){
                bool=false;
                System.out.println("Notifikasi : "+e);
                JOptionPane.showMessageDialog(null,"Maaf, Gagal Mengedit. Periksa kembali data...!!!!");
             }finally{
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            bool=false;
            System.out.println("Notifikasi : "+e);
        }   
        return bool;
    }
    
    public void mengedit(String table,String acuan_field,String update,JTextField AlmGb){
        try {
            ps = connect.prepareStatement("update "+table+" set "+update+" where "+acuan_field);
            try{            
                ps.setBinaryStream(1, new FileInputStream(AlmGb.getText()), new File(AlmGb.getText()).length());
                ps.executeUpdate();
             }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                JOptionPane.showMessageDialog(null,"Maaf, Pilih dulu data yang mau anda edit...\n Klik data pada table untuk memilih...!!!!");
             }finally{
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
    }

    public void query(String qry){
        try {
            ps=connect.prepareStatement(qry);
            try{
                ps.executeQuery();
             }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                JOptionPane.showMessageDialog(null,"Maaf, Query tidak bisa dijalankan...!!!!");
             }finally{
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }            
    }

    public void queryu(String qry){
        try {
            ps=connect.prepareStatement(qry);
            try{                            
                ps.executeUpdate(); 
             }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                JOptionPane.showMessageDialog(null,"Maaf, Query tidak bisa dijalankan...!!!!");
             }finally{
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
    }
    
    public boolean queryutf(String qry){
        bool=false;
        try {
            ps=connect.prepareStatement(qry);
            try{                            
                ps.executeUpdate(); 
                bool=true;
             }catch(Exception e){
                bool=false;
                System.out.println("Notifikasi : "+e);
                JOptionPane.showMessageDialog(null,"Maaf, Query tidak bisa dijalankan...!!!!");                
             }finally{
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            bool=false;
            System.out.println("Notifikasi : "+e);
        }
        return bool;
    }
    
    public void queryu(String qry,String parameter){
        try {
            ps=connect.prepareStatement(qry);
            try{
                ps.setString(1,parameter);
                ps.executeUpdate();
             }catch(Exception e){
                System.out.println("Notifikasi : "+e);
                JOptionPane.showMessageDialog(null,"Maaf, Query tidak bisa dijalankan...!!!!");
             }finally{
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }    
    }
    
    
    public void queryu2(String qry){
        try {
            ps=connect.prepareStatement(qry);
            try{                            
                ps.executeUpdate(); 
             }catch(Exception e){
                System.out.println("Notifikasi : "+e);
             }finally{
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
    }
    
    public void queryu2(String qry,int i,String[] a){
        try {
            try{            
                ps=connect.prepareStatement(qry);
                for(angka=1;angka<=i;angka++){
                    ps.setString(angka,a[angka-1]);
                } 
                ps.executeUpdate(); 
             }catch(Exception e){
                System.out.println("Notifikasi : "+e);
             }finally{
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
    }
    
    public boolean queryu2tf(String qry,int i,String[] a){
        bool=false;
        try {
            try{            
                ps=connect.prepareStatement(qry);
                for(angka=1;angka<=i;angka++){
                    ps.setString(angka,a[angka-1]);
                } 
                ps.executeUpdate(); 
                bool=true;
             }catch(Exception e){
                bool=false;
                System.out.println("Notifikasi : "+e);
             }finally{
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
        return bool;
    }
    
    public void queryu3(String qry,int i,String[] a){
        try {
            try{            
                ps=connect.prepareStatement(qry);
                for(angka=1;angka<=i;angka++){
                    ps.setString(angka,a[angka-1]);
                } 
                ps.executeUpdate(); 
             }catch(Exception e){
                System.out.println("Notifikasi : "+e);
             }finally{
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
    }
    
    public void AutoComitFalse(){
        try {
            connect.setAutoCommit(false);
        } catch (Exception e) {
        }
    }
    
    public void AutoComitTrue(){
        try {
            connect.setAutoCommit(true);
        } catch (Exception e) {
        }
    }
     

    public void cariIsi(String sql,JComboBox cmb){
        try {
            ps=connect.prepareStatement(sql);
            try{  
                rs=ps.executeQuery();
                if(rs.next()){
                    String dicari=rs.getString(1);
                    cmb.setSelectedItem(dicari);
                }else{
                    cmb.setSelectedItem("");
                }    
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }   
    }

    public void cariIsi(String sql,JDateTimePicker dtp){
        try {
            ps=connect.prepareStatement(sql);
            try{            
                rs=ps.executeQuery();
                if(rs.next()){
                    try {
                        dtp.setDisplayFormat("yyyy-MM-dd");
                        dtp.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(1)));
                        dtp.setDisplayFormat("dd-MM-yyyy");
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }       
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
    }

    public void cariIsi(String sql,JTextField txt){
        try {
            ps=connect.prepareStatement(sql);
            try{            
                rs=ps.executeQuery();
                if(rs.next()){
                    txt.setText(rs.getString(1));
                }else{
                    txt.setText("");
                }  
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
    }
    
    public int cariRegistrasi(String norawat){
        angka=0;
        try {
            ps=connect.prepareStatement(
                    "select count(billing.no_rawat) from billing where billing.no_rawat=?");
            try {
                ps.setString(1,norawat);
                rs=ps.executeQuery();
                if(rs.next()){
                    angka=rs.getInt(1);
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return angka;
    }
    
    public void cariIsi(String sql,JTextField txt,String kunci){
        try {
            ps=connect.prepareStatement(sql);
            try{
                ps.setString(1,kunci);
                rs=ps.executeQuery();
                if(rs.next()){
                    txt.setText(rs.getString(1));
                }else{
                    txt.setText("");
                }   
            }catch(SQLException e){
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
    } 
    
    public void cariIsi(String sql,JTextArea txt,String kunci){
        try {
            ps=connect.prepareStatement(sql);
            try{
                ps.setString(1,kunci);
                rs=ps.executeQuery();
                if(rs.next()){
                    txt.setText(rs.getString(1));
                }else{
                    txt.setText("");
                }   
            }catch(SQLException e){
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
    }
    

    public void cariIsi(String sql,JLabel txt){
        try {
            ps=connect.prepareStatement(sql);
            try{
                rs=ps.executeQuery();
                if(rs.next()){
                    txt.setText(rs.getString(1));
                }else{
                    txt.setText("");
                }
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
    }
    
    public String cariIsi(String sql){
        dicari="";
        try {
            ps=connect.prepareStatement(sql);
            try{            
                rs=ps.executeQuery();            
                if(rs.next()){
                    dicari=rs.getString(1);
                }else{
                    dicari="";
                }   
            }catch(Exception e){
                dicari="";
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
        
        return dicari;
    }
    
    public ByteArrayInputStream cariGambar(String sql){
        ByteArrayInputStream inputStream=null;
        try {
            ps=connect.prepareStatement(sql);
            try{            
                rs=ps.executeQuery();            
                if(rs.next()){                
                    inputStream = new ByteArrayInputStream(rs.getBytes(1));
                }
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
            
        return inputStream;
    }
    
    public String cariIsi(String sql,String data){
        dicari="";
        try {
            ps=connect.prepareStatement(sql);
            try{                            
                ps.setString(1,data);
                rs=ps.executeQuery();            
                if(rs.next()){
                    dicari=rs.getString(1);
                }else{
                    dicari="";
                }   
            }catch(Exception e){
                dicari="";
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rs != null ){
                    rs.close();
                }

                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
            
        return dicari;
    }
    
    public Date cariIsi2(String sql){
        try {
            ps=connect.prepareStatement(sql);
            try{            
                rs=ps.executeQuery();            
                if(rs.next()){
                    tanggal=rs.getDate(1);
                }else{
                    tanggal=new Date();
                }   
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
        return tanggal;
    }

    public Integer cariInteger(String sql){
        angka=0;
        try {
            ps=connect.prepareStatement(sql);
            try{            
                rs=ps.executeQuery();            
                if(rs.next()){
                    angka=rs.getInt(1);
                }else{
                    angka=0;
                } 
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
            
        return angka;
    }
    
    public Integer cariInteger(String sql,String data){
        angka=0;
        try {
            ps=connect.prepareStatement(sql);
            try{
                ps.setString(1,data);
                rs=ps.executeQuery();            
                if(rs.next()){
                    angka=rs.getInt(1);
                }else{
                    angka=0;
                }  
            }catch(Exception e){
                angka=0;
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
            
        return angka;
    }

    public Integer cariIntegerCount(String sql){
        angka=0;
        try {
            ps=connect.prepareStatement(sql);
            try{            
                rs=ps.executeQuery();            
                while(rs.next()){
                    angka=angka+rs.getInt(1);
                }
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
            
        return angka;
    }    
    
    public Integer cariInteger2(String sql){
        angka=0;
        try {
            ps=connect.prepareStatement(sql);
            try{
                rs=ps.executeQuery();            
                rs.last();
                angka=rs.getRow();
                if(angka<1){
                    angka=0;
                }   
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
            
        return angka;
    }

    public void cariIsiAngka(String sql,JTextField txt){
        try {
            ps=connect.prepareStatement(sql);
            try{
                rs=ps.executeQuery();
                if(rs.next()){
                    txt.setText(df2.format(rs.getDouble(1)));
                }else{
                    txt.setText("0");
                }
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
    }

    public void cariIsiAngka(String sql,JLabel txt) {
        try {
            ps=connect.prepareStatement(sql);
            try{
                rs=ps.executeQuery();
                if(rs.next()){
                    txt.setText(df2.format(rs.getDouble(1)));
                }else{
                    txt.setText("0");
                }
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }            
    }
    
    public double cariIsiAngka(String sql) {
        angka2=0;
        try {
            ps=connect.prepareStatement(sql);
            try{            
                rs=ps.executeQuery();
                if(rs.next()){
                    angka2=rs.getDouble(1);
                }else{
                    angka2=0;
                }
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
            
        return angka2;
    }
    
    public double cariIsiAngka(String sql,String data) {
        angka2=0;
        try {
            ps=connect.prepareStatement(sql);
            try{            
                ps.setString(1,data);
                rs=ps.executeQuery();
                if(rs.next()){
                    angka2=rs.getDouble(1);
                }else{
                    angka2=0;
                }
                //rs.close();
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
            
        return angka2;
    }

    public void cariGambar(String sql,JLabel txt){        
        try {
            ps=connect.prepareStatement(sql);
            try{
                rs=ps.executeQuery();
                if(rs.next()){
                    icon = new javax.swing.ImageIcon(rs.getBlob(1).getBytes(1L, (int) rs.getBlob(1).length()));
                    createThumbnail();
                    txt.setIcon(icon);
                }else{
                    txt.setText(null);
                }
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
    }

    public void cariGambar(String sql,java.awt.Canvas txt,String text){
        try {
            ps=connect.prepareStatement(sql);
            try {
                rs = ps.executeQuery();
                for (int I = 0; rs.next(); I++) {
                    ((Painter) txt).setImage(gambar(text));
                    Blob blob = rs.getBlob(5);
                    ((Painter) txt).setImageIcon(new javax.swing.ImageIcon(
                        blob.getBytes(1, (int) (blob.length()))));
                }  
            } catch (Exception ex) {
                cetak(ex.toString());
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
            
    }
    
    public String cariString(String sql){
        dicari="";
        try {
            ps=connect.prepareStatement(sql);
            try{
                rs=ps.executeQuery();            
                if(rs.next()){
                    dicari=rs.getString(1);
                }else{
                    dicari="";
                }
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rs != null){
                    rs.close();
                }
                
                if(ps != null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
            
        return dicari;
    }

    private String gambar(String id) {
        return folder + File.separator + id.trim() + ".jpg";
    }
    
    public void Tabel(javax.swing.JTable tb,int lebar[]){
      tb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      angka=tb.getColumnCount();
      for(int i=0;i < angka;i++){
          javax.swing.table.TableColumn tbc = tb.getColumnModel().getColumn(i);
          tbc.setPreferredWidth(lebar[i]);
          //tb.setRowHeight(17);
      }
  }

    private void createThumbnail() {
        int maxDim = 150;
        try {
            Image inImage = icon.getImage();

            double scale = (double) maxDim / (double) inImage.getHeight(null);
            if (inImage.getWidth(null) > inImage.getHeight(null)) {
                scale = (double) maxDim / (double) inImage.getWidth(null);
            }

            int scaledW = (int) (scale * inImage.getWidth(null));
            int scaledH = (int) (scale * inImage.getHeight(null));

            BufferedImage outImage = new BufferedImage(scaledW, scaledH,
            BufferedImage.TYPE_INT_RGB);

            AffineTransform tx = new AffineTransform();

            if (scale < 1.0d) {
                tx.scale(scale, scale);
            }

            Graphics2D g2d = outImage.createGraphics();
            g2d.drawImage(inImage, tx, null);
            g2d.dispose();

            iconThumbnail = new javax.swing.ImageIcon(outImage);
        } catch (Exception e) {
        }
    }

    private void cetak(String str) {
        System.out.println(str);
    }



   public class Painter extends Canvas {

        Image image;

        private void setImage(String file) {
            URL url = null;
            try {
                url = new File(file).toURI().toURL();
            } catch (MalformedURLException ex) {
                cetak(ex.toString());
            }
            image = getToolkit().getImage(url);
            repaint();
        }
        private void setImageIcon(ImageIcon file) {
            image = file.getImage();
            repaint();
        }

        @Override
        public void paint(Graphics g) {
            double d = image.getHeight(this) / this.getHeight();
            double w = image.getWidth(this) / d;
            double x = this.getWidth() / 2 - w / 2;
            g.drawImage(image, (int) x, 0, (int) (w), this.getHeight(), this);
        }

        private void cetak(String str) {
            System.out.println(str);
        }
    }

   public class NIOCopier {
        public NIOCopier(String asal, String tujuan) throws IOException {
            FileOutputStream outFile;
            try (FileInputStream inFile = new FileInputStream(asal)) {
                outFile = new FileOutputStream(tujuan);
                FileChannel outChannel;
                try (FileChannel inChannel = inFile.getChannel()) {
                    outChannel = outFile.getChannel();
                    for (ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
                            inChannel.read(buffer) != -1;
                            buffer.clear()) {
                        buffer.flip();
                        while (buffer.hasRemaining()) {
                            outChannel.write(buffer);
                        }
                    }
                }
            outChannel.close();
            }
            outFile.close();
        }
    }
   
   public boolean exist(String sql, String val)
   {
       boolean res = false;
       
       try
       {
           ps = connect.prepareStatement(sql);
           ps.setString(1, val);
           
           try
           {
               rs = ps.executeQuery();
               if (rs.next()) res = true;
           }
           catch (Exception e)
           {
               System.out.println("Notifikasi : " + e);
           }
           finally
           {
               if (rs != null)
               {
                   rs.close();
               }

               if (ps != null)
               {
                   ps.close();
               }
           }
       }
       catch (Exception e)
       {
           System.out.println("Notifikasi : " + e);
       }
       
       return res;
   }
   
    public List<String[]> select(String q)
    {
        List<String[]> res = new ArrayList<>();
       
        PreparedStatement ps = null;
        ResultSet rs = null;
            
        try
        {
            ps = connect.prepareStatement(q);
            rs = ps.executeQuery();
            
            while (rs.next())
            {
                String[] sa = new String[rs.getMetaData().getColumnCount()];
                
                for (int a = 0; a < sa.length; a++)
                {
                    sa[a] = rs.getString(a + 1);
                }
                
                res.add(sa);
            }
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(sekuel.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if (rs != null)
                    rs.close();
                
                if (ps != null)
                    ps.close();
            } 
            catch (SQLException ex)
            {
                Logger.getLogger(sekuel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return res;
   }
    
    public List<HashMap<String, String>> selectWithName(String q)
    {
        List<HashMap<String, String>> res = new ArrayList<>();
       
        PreparedStatement ps = null;
        ResultSet rs = null;
            
        try
        {
            ps = connect.prepareStatement(q);
            rs = ps.executeQuery();
            
            while (rs.next())
            {
                HashMap<String, String> map = new HashMap<>();
                
                for (int a = 0; a < rs.getMetaData().getColumnCount(); a++)
                {
                    map.put(rs.getMetaData().getColumnLabel(a + 1), rs.getString(a + 1));
                }
                
                res.add(map);
            }
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(sekuel.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if (rs != null)
                    rs.close();
                
                if (ps != null)
                    ps.close();
            } 
            catch (SQLException ex)
            {
                Logger.getLogger(sekuel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return res;
   }
    
    public GResult selectComplete(String q)
    {
        GResult res = new GResult();
       
        PreparedStatement ps = null;
        ResultSet rs = null;
            
        try
        {
            ps = connect.prepareStatement(q);
            rs = ps.executeQuery();
            
            while (rs.next())
            {
                GRow row = new GRow();
                
                for (int a = 0; a < rs.getMetaData().getColumnCount(); a++)
                {
                    row.add(rs.getMetaData().getColumnLabel(a + 1), rs.getString(a + 1));
                    row.add(rs.getString(a + 1));
                }
                
                res.add(row);
            }
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(sekuel.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if (rs != null)
                    rs.close();
                
                if (ps != null)
                    ps.close();
            } 
            catch (SQLException ex)
            {
                Logger.getLogger(sekuel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return res;
    }

    public String[] selectRow(String q)
    {
        List<String[]> res = select(q);

        if (res.size() > 0)
            return res.get(0);
        else
            return null;
    }
   
    public HashMap<String, String> selectRowWithName(String q)
    {
        List<HashMap<String, String>> res = selectWithName(q);

        if (res.size() > 0)
            return res.get(0);
        else
            return null;
    }

    public GRow selectRowComplete(String q)
    {
        GResult res = selectComplete(q);
        
        if (res.size() > 0)
            return res.get(0);
        else
            return null;
    }

    public String selectScalar(String q)
    {
        List<String[]> res = select(q);

        if (res.size() > 0 && res.get(0).length > 0)
        {
            return res.get(0)[0];
        }
        else
        {
            return "";
        }
    }

    public boolean write(String qry)
    {
        boolean res = true;
       
        try 
        {
            ps=connect.prepareStatement(qry);

            try
            {
                ps.executeUpdate();
            }
            catch(Exception e)
            {
                System.out.println("Notifikasi : " + e.getMessage() + ". Query : " + qry);

                res = false;
            }
            finally
            {
                if(ps != null)
                {
                    ps.close();
                }
            }
        } 
        catch (Exception e) 
        {
            System.out.println("Notifikasi : " + e.getMessage() + ". Query : " + qry);
            res = false;
        } 
        
        return res;
    }
   
    public String autoNumber(String table, String column)
    {
        String q = "SELECT " + column + " FROM " + table + " ORDER BY " + column + " DESC";
        String s = selectScalar(q);
        int i = GConvert.parseInt(s) + 1;

        return String.valueOf(i);
    }
    
    public boolean executeQuery(String query, boolean status){
        try {
            konek.condb();
        ps = konek.condb().prepareStatement(query);
        if(status){
            rs = ps.executeQuery();
        }else{
            ps.executeUpdate();
        }
        return true;
        } catch (SQLException e) {
            return false;
        }
    }
    
    public List<Object[]> selectAsObject(String q)
    {
        List<Object[]> res = new ArrayList<>();
       
        PreparedStatement ps = null;
        ResultSet rs = null;
            
        try
        {
            ps = connect.prepareStatement(q);
            rs = ps.executeQuery();
            
            while (rs.next())
            {
                String[] sa = new String[rs.getMetaData().getColumnCount()];
                
                for (int a = 0; a < sa.length; a++)
                {
                    sa[a] = rs.getString(a + 1);
                }
                
                res.add(sa);
            }
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(sekuel.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if (rs != null)
                    rs.close();
                
                if (ps != null)
                    ps.close();
            } 
            catch (SQLException ex)
            {
                Logger.getLogger(sekuel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return res;
   }
}
