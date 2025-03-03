/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.gamev2;

import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.gameobjects.TestSphere;
import com.mycompany.gamev2.window.MyWindow;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author J.A
 */
public class GameV2 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        MyWindow.Construct_BUFFER_STRATEGY();
        
        int total_spheres = 1; 
        ArrayList<TestSphere> spheres = new ArrayList<>();
        
        int radius = 45;
        int count = 0;
        while(count < total_spheres){
            Vector3 pos = new Vector3(50,50,0).plus(Vector3.DOWN.getScaled(count * (radius+50)));
            TestSphere s = new TestSphere(Color.MAGENTA, radius, pos, Vector3.RIGHT, 100);
            spheres.add(s);
            count ++;
        }
        
        GameLoopV2 loop = GameLoopV2.getInstance();
        loop.start();
    }
}
