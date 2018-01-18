/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OPT_acq;

import ij.CompositeImage;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import static jdk.nashorn.internal.objects.NativeMath.max;
import mmcorej.CMMCore;
import mmcorej.StrVector;
import mmcorej.TaggedImage;
import org.micromanager.MMStudio;
import org.micromanager.api.ScriptInterface;
import org.micromanager.utils.MMScriptException;
import org.micromanager.imagedisplay.VirtualAcquisitionDisplay;
import org.micromanager.utils.ImageUtils;
import org.micromanager.utils.ReportingUtils;


/**
 *
 * @author Fogim
 */
public class OPT_frame extends javax.swing.JFrame {

    public CMMCore core_;
    public MMStudio gui_;  // Might be useful to put in at some point? ### WAS DISABLED
    public ScriptInterface si_;
    static OPT_frame frame_;    
    
    public Thread Alignment_thread;
    private Alignment Alignment_;
       
    ImageProcessor ip1;
    ImageProcessor ip2;
    ImageStack istack;
    ImagePlus retimg;
    ImagePlus iplus;    
    TaggedImage tImg;
    
    StrVector int_angles = new StrVector();
    
    CompositeImage compimg;
    private static final String ACQNAME = "OPT Alignment";
    int stop = 0;
    int width_;
    int height_;
    int imgDepth_;
    Object img;
    

    
//    ImagePlus alignraw = null;    
//    ImageStack ar_s = null;
    
    /**
     * Creates new form OPT_frame
     */
    public OPT_frame(ScriptInterface si) {
        initComponents();
        ImageIcon icon = new ImageIcon(this.getClass().getResource("../Resources/OPTicon.png"));
        this.setIconImage(icon.getImage());
        this.setTitle("OPT controller");
        core_ = si.getMMCore();
        // Leaking this in constructor may lead to trouble in multithreaded situations?

        frame_ = this;        
        rotation_control3.setParent(this);       
        Alignment_ = new Alignment(this);

        calc_angles();
        
        gui_ = (MMStudio) si;
        gui_.registerForEvents(this);               
                
        frame_.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame_.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                confirmQuit();
            }
        });        
    }

    private void confirmQuit() {
        int n = JOptionPane.showConfirmDialog(frame_,
                "Quit: are you sure?", "Quit", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
    
    public void set_angle(double angletoset){
        //core_.getDeviceName(ACQNAME)
        //Get stage
        //Set angle
        //Done!
    }
    
    public void calc_angles(){
        //String[] test = {"asdf","asdgfasd","fghgfh","zvzxcvzxc","uiouioui","fghfghfg"};
        //core_.get
        //rotation_control3.update_angles(test);
    }    
    
    public void do_alignment() throws MMScriptException, InterruptedException{ 
        Alignment_thread = new Thread(new Alignment_thread(this));
        Alignment_thread.start();
    }
    
    public void alignment_procedure(){
        
        try{
            if (gui_.isLiveModeOn()){
                gui_.enableLiveMode(false);
                gui_.closeAllAcquisitions();
            }
        } catch (Exception e) {
            System.out.println("Failed stopping Live mode:   "+e);
        }        
        //retimg = Alignment_.Snapandshow(Alignment_.Setupalign(),1);
        int framecount = 0;
        while(rotation_control3.align_status()==true){
            try {
                retimg = Alignment_.Snapandshow(retimg,framecount%2);
                //DO ROTATION HERE...
                Thread.sleep(250);
            } catch (InterruptedException ex) {
                Logger.getLogger(OPT_frame.class.getName()).log(Level.SEVERE, null, ex);
            }
            framecount++;
        }
        Alignment_.end_alignment();
    }

    
    public static OPT_frame getInstance() {
        return frame_;
    }    

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rotation_control3 = new OPT_acq.Rotation_control();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rotation_control3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rotation_control3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OPT_frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OPT_frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OPT_frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OPT_frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new OPT_frame(core_).setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private OPT_acq.Rotation_control rotation_control3;
    // End of variables declaration//GEN-END:variables
}
