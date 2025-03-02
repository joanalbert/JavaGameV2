/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.pannels;
import com.mycompany.gamev2.event_system.EventManager;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.gameobjects.TestSphere;
import com.mycompany.gamev2.interfaces.ITickListener;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author J.A
 * 
 * A BasePanel child class intended to be the testing ground for more JPanels.
 * As an Updateable it has the corresponding tick and render methods, and it also gets 
 * tick updates t
 */
public class MyPanel extends BasePanel implements ITickListener   {

    
    public ArrayList<TestSphere> spheres = new ArrayList<>();
    private final int total_spheres = 1; 
    
    public MyPanel(String name){
        super(name);
        EventManager.getInstance().subscribeTo(this, TickEvent.class);
        
        
        int radius = 45;
        int count = 0;
        while(count < this.total_spheres){
            Vector3 pos = new Vector3(50,50,0).plus(Vector3.DOWN.getScaled(count * (radius+50)));
            TestSphere s = new TestSphere(Color.MAGENTA, radius, pos, Vector3.RIGHT, 100);
            this.spheres.add(s);
            count ++;
        }
    }
       
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        Graphics2D g2d = (Graphics2D) g;
        EventManager.getInstance().postEvent(new RenderEvent(g2d));
    }
    
   @Override
    public void onTick(TickEvent event) {
        this.repaint();
        //System.out.println("world repaint");
    }

   
}
