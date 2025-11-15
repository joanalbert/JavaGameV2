/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.event_system.game_events;

import com.mycompany.gamev2.providers.CameraProvider;
import java.awt.Graphics2D;
import java.util.Optional;

/**
 *
 * @author J.A
 */
public class RenderEvent extends BaseEvent {
    
    private Graphics2D g;
    
    public RenderEvent(Graphics2D g){
        this.g = g;
        this.providers_bundle.add(CameraProvider.class, new CameraProvider());
    }
    
    
    public Graphics2D getGraphics(){
        return this.g;
    }
    
    public CameraProvider getCameraProvider(){
        Optional<CameraProvider> o = this.providers_bundle.tryGet(CameraProvider.class);
        return o.orElse(null);
    }
}
