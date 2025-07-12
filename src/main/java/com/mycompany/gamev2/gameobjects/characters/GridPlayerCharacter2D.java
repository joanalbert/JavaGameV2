/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.gameobjects.characters;

import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.input_system.InputActions.IA_Walk;
import com.mycompany.gamev2.input_system.InputBinding;
import com.mycompany.gamev2.input_system.InputContexts.GridPlayerCharacter_InputContext;
import com.mycompany.gamev2.input_system.InputManager;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author J.A
 */
public class GridPlayerCharacter2D extends PlayerCharacter {
 
    private IA_Walk ia_walk;
    
    public GridPlayerCharacter2D(){
        this.color = Color.YELLOW;
    }

    @Override
    protected void input_setup() {
        
        GridPlayerCharacter_InputContext context = new GridPlayerCharacter_InputContext();
        setInputContext(context);
        InputManager.getInstance().addContext(context);
        
        // Find Walk action and set callback
        ia_walk = (IA_Walk) context.getBindings().stream()
                .map(InputBinding::getAction)
                .filter(action -> action instanceof IA_Walk)
                .findFirst()
                .orElse(new IA_Walk());

        ia_walk.setOnTriggered(action -> {
            if (action instanceof IA_Walk walkAction) {
                Vector3 v = new Vector3(walkAction.getAxisValues()[0],
                                        walkAction.getAxisValues()[1], 0); 
                this.vel = v.normalize(); // Update velocity based on input
            }
        });
    }

    @Override
    protected void render(RenderEvent event) {
        if(!this.isActive) return;
        
        Graphics2D g = event.getGraphics();
        Vector3 location = getObjectLocation();
        
        g.setColor(this.color);
        g.fillRect((int)location.getX(), (int)location.getY(), 32, 32);
    }
    
}
