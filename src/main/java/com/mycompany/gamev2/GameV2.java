/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.gamev2;


import com.mycompany.gamev2.levels.LevelManager;
import com.mycompany.gamev2.window.MyWindow;
import javax.swing.SwingUtilities;
/**
 *
 * @author J.A
 */
public class GameV2 {

    public static void main(String[] args) {
        
        //this is not NEEEDED but it is best to ensure we're running on the EDT (event dispatcher thread) when using SWING or AWT stuff
        SwingUtilities.invokeLater( () -> {
            Provisional_Setup();
        });

    }
    
    public static void Provisional_Setup(){
        System.out.println("Hello World!");
        
        //initialize window and canvas/buffer strategy combo
        MyWindow.Construct_BUFFER_STRATEGY();
        
       
        //double radius = 45;
        //Vector3 pos = new Vector3(50,50,0).plus(Vector3.DOWN.getScaled(1 * (radius+50)));
        //TestSphere s = new TestSphere(Color.MAGENTA, radius, pos, Vector3.RIGHT, 100);

        LevelManager levelManager = LevelManager.getInstance();
        
        //run the main loop
        GameLoopV2 loop = GameLoopV2.getInstance();
        loop.start();
    }
}
