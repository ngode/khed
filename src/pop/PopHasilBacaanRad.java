/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pop;

import java.awt.Font;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;

/**
 *
 * @author GrT
 */
public class PopHasilBacaanRad extends javax.swing.JDialog
{
    public boolean isSaved = false;
    public String result;
    private String data;
    
    private static int textSize = -1;
    
    public PopHasilBacaanRad(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        
        Font f = txtHasil.getFont();
        
        if (textSize != -1)
        {
            txtHasil.setFont(new Font(f.getName(), f.getStyle(), textSize));
        }
        else
        {
            textSize = f.getSize();
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

        internalFrame = new widget.InternalFrame();
        Scroll7 = new widget.ScrollPane();
        txtHasil = new widget.editorpane();
        panelisi6 = new widget.panelisi();
        jPanel1 = new javax.swing.JPanel();
        BtnCloseIn6 = new widget.Button();
        jPanel2 = new javax.swing.JPanel();
        btnMinus = new widget.Button();
        btnPlus = new widget.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("KonsulPoli"); // NOI18N
        setUndecorated(true);
        setResizable(false);

        internalFrame.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Hasil Expertise Radiologi ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame.setWarnaBawah(new java.awt.Color(245, 250, 240));
        internalFrame.setLayout(new java.awt.BorderLayout());

        Scroll7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(245, 255, 235)));
        Scroll7.setOpaque(true);

        txtHasil.setEditable(false);
        Scroll7.setViewportView(txtHasil);

        internalFrame.add(Scroll7, java.awt.BorderLayout.CENTER);

        panelisi6.setBorder(null);
        panelisi6.setPreferredSize(new java.awt.Dimension(100, 45));
        panelisi6.setLayout(new java.awt.BorderLayout());

        jPanel1.setOpaque(false);

        BtnCloseIn6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/cross.png"))); // NOI18N
        BtnCloseIn6.setMnemonic('U');
        BtnCloseIn6.setText("Tutup");
        BtnCloseIn6.setToolTipText("Alt+U");
        BtnCloseIn6.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnCloseIn6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BtnCloseIn6ActionPerformed(evt);
            }
        });
        jPanel1.add(BtnCloseIn6);

        panelisi6.add(jPanel1, java.awt.BorderLayout.EAST);

        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(71, 40));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        btnMinus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/minus.png"))); // NOI18N
        btnMinus.setMnemonic('3');
        btnMinus.setToolTipText("Alt+3");
        btnMinus.setPreferredSize(new java.awt.Dimension(28, 23));
        btnMinus.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMinusActionPerformed(evt);
            }
        });
        jPanel2.add(btnMinus);

        btnPlus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        btnPlus.setMnemonic('3');
        btnPlus.setToolTipText("Alt+3");
        btnPlus.setPreferredSize(new java.awt.Dimension(28, 23));
        btnPlus.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPlusActionPerformed(evt);
            }
        });
        jPanel2.add(btnPlus);

        panelisi6.add(jPanel2, java.awt.BorderLayout.WEST);

        internalFrame.add(panelisi6, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(internalFrame, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnCloseIn6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BtnCloseIn6ActionPerformed
    {//GEN-HEADEREND:event_BtnCloseIn6ActionPerformed
        dispose();
    }//GEN-LAST:event_BtnCloseIn6ActionPerformed

    private void btnMinusActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnMinusActionPerformed
    {//GEN-HEADEREND:event_btnMinusActionPerformed
        Font f = txtHasil.getFont();
        textSize = f.getSize() - 1;
        txtHasil.setFont(new Font(f.getName(), f.getStyle(), textSize));
    }//GEN-LAST:event_btnMinusActionPerformed

    private void btnPlusActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPlusActionPerformed
    {//GEN-HEADEREND:event_btnPlusActionPerformed
        Font f = txtHasil.getFont();
        textSize = f.getSize() + 1;
        txtHasil.setFont(new Font(f.getName(), f.getStyle(), textSize));
    }//GEN-LAST:event_btnPlusActionPerformed

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
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(PopHasilBacaanRad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(PopHasilBacaanRad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(PopHasilBacaanRad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(PopHasilBacaanRad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                PopHasilBacaanRad dialog = new PopHasilBacaanRad(new javax.swing.JFrame(), true);
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
    private widget.Button BtnCloseIn6;
    private widget.ScrollPane Scroll7;
    private widget.Button btnMinus;
    private widget.Button btnPlus;
    private widget.InternalFrame internalFrame;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private widget.panelisi panelisi6;
    private widget.editorpane txtHasil;
    // End of variables declaration//GEN-END:variables

    public void setData(String s)
    {
        txtHasil.setText(s);
        data = s;
    }
    
    private void simpan()
    {
        isSaved = true;
        result = txtHasil.getText();
        dispose();
    }
}
