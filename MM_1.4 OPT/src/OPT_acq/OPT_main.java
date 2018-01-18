/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OPT_acq;

import org.micromanager.api.ScriptInterface;
import javax.swing.JFrame;
import mmcorej.CMMCore;
import org.micromanager.MMStudio;
import org.micromanager.acquisition.AcquisitionEngine;
import org.micromanager.acquisition.AcquisitionWrapperEngine;
import org.micromanager.api.MMPlugin;
import org.micromanager.api.ScriptInterface;

/**
 *
 * @author fogim
 */
public class OPT_main implements org.micromanager.api.MMPlugin{

    public static final String menuName = "OPT controller";
    public static final String tooltipDescription = "Does OPT acquisitions";
    
    public static JFrame frame_;
    static ScriptInterface si_;
    private CMMCore core_;
    private MMStudio gui_;
    private AcquisitionEngine acq_;    
    
    @Override
    public void dispose() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void setApp(ScriptInterface si) {
      gui_ = (MMStudio) si;
      core_ = si.getMMCore();
      acq_ = gui_.getAcquisitionEngine();
      
      frame_ = new OPT_frame(si);
      frame_.pack();
    }

    @Override
    public void show() {
//        gui_.showMessage("HELLO WORLD!");
                
        if (frame_ == null) {
            frame_ = new OPT_frame(si_);
            gui_.addMMBackgroundListener(frame_);
//            frame_.setLocation(fa.controlFrame_.FrameXpos, fa.controlFrame_.FrameYpos);
        }
        frame_.setVisible(true);
        
    }

    @Override
    public String getDescription() {
        return "For acquiring OPT data - going to assume something like a Zaber stage with theta being along the 'Z' axis";
    }

    @Override
    public String getInfo() {
        return "Info?";
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public String getCopyright() {
        return "Photonics/Physics/Imperial College, 2018";
    }
    
}
