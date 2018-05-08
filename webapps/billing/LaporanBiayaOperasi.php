<?php
 include '../conf/conf.php';
?>
<html>
    <head>
        <link href="style.css" rel="stylesheet" type="text/css" media="screen" />
    </head>
    <body>

    <?php
    reportsqlinjection();      

        $nama = $_GET['nama'];
        $no_rm = $_GET['no_rm'];
        $umur = $_GET['umur'];
        $status = $_GET['status'];
        $kelas = $_GET['kelas'];

        $diagnosa = $_GET['diagnosa'];
        $group = $_GET['group'];
        $kategori = $_GET['kategori'];
        $detail = $_GET['detail'] == 'null' ? '-' : $_GET['detail'];
        $tanggal_operasi = $_GET['tanggal_operasi'];

        $dokter_operator = $_GET['dokter_operator'] == 'null' ? '' :
        "<tr>
             <td width='35%'><font color='000000' size='1'  face='Tahoma'>Dokter Operator</font></td>
             <td><font color='000000' size='1'  face='Tahoma'>:</font></td>
             <td width='65%'><font color='000000' size='1'  face='Tahoma'>".$_GET['dokter_operator']."</font></td>
         </tr>";
        $dokter_yang_merawat = $_GET['dokter_yang_merawat'] == 'null' ? '' :
        "<tr>
             <td width='35%'><font color='000000' size='1'  face='Tahoma'>Dokter Yang Merawat</font></td>
             <td><font color='000000' size='1'  face='Tahoma'>:</font></td>
             <td width='65%'><font color='000000' size='1'  face='Tahoma'>".$_GET['dokter_yang_merawat']."</font></td>
         </tr>";
        $ass_dokter_operator = $_GET['ass_dokter_operator'] == 'null' ? '' :
        "<tr>
             <td width='35%'><font color='000000' size='1'  face='Tahoma'>Asisten Dokter Operator</font></td>
             <td><font color='000000' size='1'  face='Tahoma'>:</font></td>
             <td width='65%'><font color='000000' size='1'  face='Tahoma'>".$_GET['ass_dokter_operator']."</font></td>
         </tr>";
        $dokter_anestesi = $_GET['dokter_anestesi'] == 'null' ? '' :
        "<tr>
             <td width='35%'><font color='000000' size='1'  face='Tahoma'>Dokter Anestesi</font></td>
             <td><font color='000000' size='1'  face='Tahoma'>:</font></td>
             <td width='65%'><font color='000000' size='1'  face='Tahoma'>".$_GET['dokter_anestesi']."</font></td>
         </tr>";
        $penata_anestesi = $_GET['penata_anestesi'] == 'null' ? '' :
        "<tr>
             <td width='35%'><font color='000000' size='1'  face='Tahoma'>Penata Anestesi</font></td>
             <td><font color='000000' size='1'  face='Tahoma'>:</font></td>
             <td width='65%'><font color='000000' size='1'  face='Tahoma'>".$_GET['penata_anestesi']."</font></td>
         </tr>";
        $dokter_anak = $_GET['dokter_anak'] == 'null' ? '' :
        "<tr>
             <td width='35%'><font color='000000' size='1'  face='Tahoma'>Dokter Anak</font></td>
             <td><font color='000000' size='1'  face='Tahoma'>:</font></td>
             <td width='65%'><font color='000000' size='1'  face='Tahoma'>".$_GET['dokter_anak']."</font></td>
         </tr>";
        $dokter_pendamping = $_GET['dokter_pendamping'] == 'null' ? '' :
        "<tr>
             <td width='35%'><font color='000000' size='1'  face='Tahoma'>Dokter Pendamping</font></td>
             <td><font color='000000' size='1'  face='Tahoma'>:</font></td>
             <td width='65%'><font color='000000' size='1'  face='Tahoma'>".$_GET['dokter_pendamping']."</font></td>
         </tr>";
        $perawat_bidan = $_GET['perawat_bidan'] == 'null' ? '' :
        "<tr>
             <td width='35%'><font color='000000' size='1'  face='Tahoma'>Perawat / Bidan</font></td>
             <td><font color='000000' size='1'  face='Tahoma'>:</font></td>
             <td width='65%'><font color='000000' size='1'  face='Tahoma'>".$_GET['perawat_bidan']."</font></td>
         </tr>";
        $admin = $_GET['admin'];

        $_sql = "select temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8, temp9, temp10, temp11, temp12, temp13, temp14 from temporary order by no asc";   
        $hasil=bukaquery($_sql);
        
        if(mysql_num_rows($hasil)!=0) { 
            $setting=  mysql_fetch_array(bukaquery("select nama_instansi,alamat_instansi,kabupaten,propinsi,kontak,email,logo from setting"));
            echo "   
            <table width='100%' bgcolor='#ffffff' align='left' border='0' padding='0' class='tbl_form' >
            <tr class='isi12' padding='7'>
				<td colspan='7' padding='0'>
					   <table width='100%' bgcolor='#ffffff' align='left' border='0' class='tbl_form' border=0>
							<tr>
								<td  width='20%'>
									<img width='60' height='60' src='data:image/jpeg;base64,". base64_encode($setting['logo']). "'/>
								</td>
								<td>
								<center>
										<font color='000000' size='3'  face='Tahoma'>".$setting["nama_instansi"]."</font><br>
										<font color='000000' size='1'  face='Tahoma'>
										".$setting["alamat_instansi"].", ".$setting["kabupaten"].", ".$setting["propinsi"]."<br/>
										".$setting["kontak"].", E-mail : ".$setting["email"]."
										</font> <br>
										<center><font color='000000' size='2'  face='Tahoma'>BILLING OPERASI</font> </center>
								</center>
								</td>
								<td  width='20%'>&nbsp;
								</td>
							</tr>
					  </table>
				</td>
            </tr>
            <tr class='isi12' padding='7'>
               <td>
                  <table width='100%' bgcolor='#ffffff' align='left' border='0' class='tbl_form'>
                     <tr>
                         <td width='35%'><font color='000000' size='1'  face='Tahoma'>Nama Pasien</font></td>
                         <td><font color='000000' size='1'  face='Tahoma'>:</font></td>
                         <td width='65%'><font color='000000' size='1'  face='Tahoma'>".str_replace("_"," ", $nama)."</font></td>
                     </tr>     
                     <tr>
                         <td width='35%'><font color='000000' size='1'  face='Tahoma'>No.RM</font></td>
                         <td><font color='000000' size='1'  face='Tahoma'>:</font></td>
                         <td width='65%'><font color='000000' size='1'  face='Tahoma'>$no_rm</font></td>
                     </tr>    
                     <tr>
                         <td width='35%'><font color='000000' size='1'  face='Tahoma'>Umur</font></td>
                         <td><font color='000000' size='1'  face='Tahoma'>:</font></td>
                         <td width='65%'><font color='000000' size='1'  face='Tahoma'>$umur</font></td>
                     </tr>    
                     <tr>
                         <td width='35%'><font color='000000' size='1'  face='Tahoma'>Status</font></td>
                         <td><font color='000000' size='1'  face='Tahoma'>:</font></td>
                         <td width='65%'><font color='000000' size='1'  face='Tahoma'>$status</font></td>
                     </tr>    
                     <tr>
                         <td width='35%'><font color='000000' size='1'  face='Tahoma'>Kelas</font></td>
                         <td><font color='000000' size='1'  face='Tahoma'>:</font></td>
                         <td width='65%'><font color='000000' size='1'  face='Tahoma'>$kelas</font></td>
                     </tr>    
                  </table>
               </td>
            </tr>
            <tr class='isi12' padding='7'>
               <td>
                  <table width='100%' bgcolor='#ffffff' align='left' border='0' class='tbl_form'>   
                     <tr>
                         <td width='35%'><font color='000000' size='1'  face='Tahoma'>Diagnosa</font></td>
                         <td><font color='000000' size='1'  face='Tahoma'>:</font></td>
                         <td width='65%'><font color='000000' size='1'  face='Tahoma'>$diagnosa</font></td>
                     </tr>    
                     <tr>
                         <td width='35%'><font color='000000' size='1'  face='Tahoma'>Group</font></td>
                         <td><font color='000000' size='1'  face='Tahoma'>:</font></td>
                         <td width='65%'><font color='000000' size='1'  face='Tahoma'>$group</font></td>
                     </tr>
                     <tr>
                         <td width='35%'><font color='000000' size='1'  face='Tahoma'>Kategori</font></td>
                         <td><font color='000000' size='1'  face='Tahoma'>:</font></td>
                         <td width='65%'><font color='000000' size='1'  face='Tahoma'>$kategori</font></td>
                     </tr>
                     <tr>
                         <td width='35%'><font color='000000' size='1'  face='Tahoma'>Detail</font></td>
                         <td><font color='000000' size='1'  face='Tahoma'>:</font></td>
                         <td width='65%'><font color='000000' size='1'  face='Tahoma'>$detail</font></td>
                     </tr>
                     <tr>
                         <td width='35%'><font color='000000' size='1'  face='Tahoma'>Tanggal Operasi</font></td>
                         <td><font color='000000' size='1'  face='Tahoma'>:</font></td>
                         <td width='65%'><font color='000000' size='1'  face='Tahoma'>$tanggal_operasi</font></td>
                     </tr>
                  </table>
               </td>
            </tr>
            <tr class='isi12' padding='7'>
               <td>
                  <table width='100%' bgcolor='#ffffff' align='left' border='0' class='tbl_form'>   
                     ".$dokter_operator.$dokter_yang_merawat.$ass_dokter_operator.$dokter_anestesi.$penata_anestesi.$dokter_anak.$dokter_pendamping.$perawat_bidan."
                  </table>
               </td>
            </tr>

            <tr class='isi12' padding='7'>
               <td>
                  <table width='100%' bgcolor='#ffffff' align='left' border='0' cellpadding='0' cellspacing='0' >
                     <tr class='head'>
                         <td width='5%'><font color='000000' size='1'  face='Tahoma'>&nbsp;No</font></td>
                         <td width='45%'><font color='000000' size='1'  face='Tahoma'>&nbsp;Uraian</font></td>
                         <td width='25%'><font color='000000' size='1'  face='Tahoma'>&nbsp;Biaya</font></td>
                         <td width='25%'><font color='000000' size='1'  face='Tahoma'>&nbsp;Biaya Dibayar</font></td>
                     </tr>
                     "; 
                        $z=1;
                        while($item = mysql_fetch_array($hasil)) 
                        {
                           if($item[0]<>"Total Biaya Operasi")
                            {
                                //var_dump($item[2]);exit();

                              echo "<tr class='isi'>
                                        <td style=\"padding: 5px;\"><font color='000000' size='1'  face='Tahoma'>&nbsp;$z</font></td>
                                        <td><font color='000000' size='1'  face='Tahoma'>&nbsp;$item[0]</font></td>
                                        <td><font color='000000' size='1'  face='Tahoma'>&nbsp;</font></td>
                                        <td align=\"right\"><font color='000000' size='1'  face='Tahoma'>&nbsp;".formatDuit($item[2])."</font></td>
                                    </tr>";  
                           }
                           else if($item[0]=="Total Biaya Operasi")
                           {//var_dump($item[2]);exit();
                               echo "
                                    <tr><td></td></tr>
                                    <tr>
                                        <td style=\"padding: 5px;\"><font color='000000' size='1'  face='Tahoma'></font></td>
                                        <td><font color='000000' size='1'  face='Tahoma'><b>&nbsp;$item[0]<b></font></td>
                                        <td><font color='000000' size='1'  face='Tahoma'>&nbsp;</font></td>
                                        <td align=\"right\"><font color='000000' size='1'  face='Tahoma'><b>&nbsp;".formatDuit($item[1])."<b></font></td>
                                    </tr>";  
                           }

                           $z++;                                   
                        } 
            
            echo "
                  </table>
               </td>
            </tr>
            <tr>
               <td>
                  <table width='100%' bgcolor='#ffffff' align='left' border='0' class='tbl_form'>
                     <tr>
                         <td width='50%' align='center'><font color='000000' size='1'  face='Tahoma'></font></td>
                         <td width='50%' align='center'><font color='000000' size='1'  face='Tahoma'>Petugas Kasir</font></td>
                     </tr>
                     <tr>
                         <td width='50%' align='center'><font color='000000' size='1'  face='Tahoma'>&nbsp</font></td>
                         <td width='50%' align='center'><font color='000000' size='1'  face='Tahoma'>&nbsp</font></td>
                     </tr>
                     <tr>
                         <td width='50%' align='center'><font color='000000' size='1'  face='Tahoma'>&nbsp</font></td>
                         <td width='50%' align='center'><font color='000000' size='1'  face='Tahoma'>&nbsp</font></td>
                     </tr>
                     <tr>
                         <td width='50%' align='center'><font color='000000' size='1'  face='Tahoma'></font></td>
                         <td width='50%' align='center'><font color='000000' size='1'  face='Tahoma'>( $admin )</font></td>
                     </tr>
                  </table>
               </td>
            </tr>
            </table>
            ";  
        } else {echo "<font color='000000' size='1'  face='Times New Roman'><b>Data Pemeriksaan Radiologi masih kosong !</b>";}

    ?>  

    </body>
</html>
