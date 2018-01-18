/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OPT_acq;

/**
 *
 * @author Fogim
 */
public class Alignment_thread implements Runnable {
    private OPT_frame frame;
    
    public Alignment_thread(OPT_frame frame_){
        // initialize new instance of HCAFLIMPluginFrame
        frame = frame_;
    }
    
    //@Override
    public void run() {
        //System.out.println("Started alignment thread");
        // NEED TO ADD STUFF IN THE MAIN FRAME TO GET THIS TO WORK...
        try{
            frame.alignment_procedure();
        } catch (Exception e) {
            System.out.println("Alignment threw an error!");
        }
        //frame.snapFLIMImageButton(); REPLACE WITH THE CALL TO PREFIND
    }
}

