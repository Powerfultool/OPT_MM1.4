/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OPT_acq;

// Import the Micromanager core
import mmcorej.CMMCore;
// Import HCAFLIMPluginFrame for parent access
import OPT_acq.OPT_frame;
import ij.CompositeImage;
import ij.IJ;
import static ij.IJ.COMPOSITE;
import org.micromanager.MMStudio;
import ij.gui.*;
import ij.process.*;
import ij.ImagePlus;
import ij.ImageStack;
import static ij.gui.NewImage.FILL_BLACK;
import org.micromanager.utils.ImageUtils;
import ij.io.DirectoryChooser;
import static ij.plugin.RGBStackConverter.convertToRGB;
import java.awt.Color;
//Import stuff for file reading
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import mmcorej.TaggedImage;

/**
 *
 * @author Fogim
 */
public class Alignment {
    private CMMCore core_;   
    private MMStudio gui_;    
    private OPT_frame frame_;
    
    ImageProcessor improc_R;
    ImageProcessor improc_G;
    ImageProcessor improc_B;
    ImageStack imstack;
    CompositeImage CI;
    ImagePlus alignimg;
    ij.ImagePlus RGBshow;
    
    private String alignacqname;
    Object RGBimg;
    
    // Maybe only need this one for a plugin filter?
    static int NO_UNDO;

    public Alignment(OPT_frame parent) {
        // Think the getInstance means we manipulate the original, not a copy?
        gui_ = MMStudio.getInstance();
        core_ = gui_.getCore();
        frame_= OPT_frame.getInstance();
        alignimg = new ImagePlus();
        alignacqname = "Alignment Image";
        CI = null;
    }

    public ImagePlus Setupalign(){
        alignimg  = new ij.ImagePlus();
        alignimg.setDimensions(3, 1, 1);
        return alignimg;
    }
    
    private void SetupStores(){
        int width = (int) core_.getImageWidth();
        int height = (int) core_.getImageHeight();
        improc_R = new ByteProcessor(width,height);
        improc_R.setColor(Color.red);
        improc_G = new ByteProcessor(width,height);
        improc_G.setColor(Color.green);
        improc_B = new ByteProcessor(width,height);
        improc_B.setColor(Color.blue);

        imstack = new ImageStack(width,height,3);
        imstack.setProcessor(improc_R, 1);
        imstack.setProcessor(improc_G, 2);
        imstack.setProcessor(improc_B, 3);
        
        CI = new CompositeImage(new ij.ImagePlus("temp", imstack),COMPOSITE);
        
        //RGBshow = new ij.ImagePlus("RGB", imstack);
    }
    
    public void end_alignment(){
        CI.getWindow().close();
        CI = null;
    }
    
    public ImagePlus Snapandshow(ImagePlus alignimg,int target_channel){
        // Test function to check core access
        try{
            if (CI == null){
                SetupStores();
            }
            
            //Red for zero degrees
            core_.snapImage();
            if(target_channel==0){
                improc_R.setPixels(core_.getImage());
            } else {
                improc_G.setPixels(core_.getImage());
            }
            //Green for 180 degrees
            //core_.snapImage();
            //improc_G.setPixels(core_.getImage());
            //Dummy blue channel by copying green, multiply by zero
            //improc_B.setPixels(core_.getImage());
            //improc_B.multiply(0.0);

            imstack.setProcessor(improc_R, 1);
            improc_G.flipHorizontal();
            imstack.setProcessor(improc_G, 2);           
            //imstack.setProcessor(improc_B, 3);               
           
            //RGBshow = new ij.ImagePlus("RGB", imstack);
            CI.setStack(imstack);
            CI.flatten();
            //CI.setProcessor(RGBshow.getProcessor());
            
            if(!CI.isVisible()){
                CI.show();
            }
            
        } catch (Exception e) {
            System.out.println(e);
            //probably someone changed the ROI or binning...
            //calling end_alignment will force reinitialisation...
            end_alignment();
        }
        return alignimg;
    }        
}
