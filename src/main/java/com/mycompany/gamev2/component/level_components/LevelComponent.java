/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.level_components;

import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;

/**
 *
 * @author J.A
 */
public abstract class LevelComponent {
    
    
    
    public abstract void tick(TickEvent e);
    public abstract void render(RenderEvent e);
}
