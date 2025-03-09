/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.event_system.level_events;

import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.levels.BaseLevel;

/**
 *
 * @author J.A
 */
public class LevelWindownEvent extends BaseEvent{
    private BaseLevel level;
    
    public LevelWindownEvent(BaseLevel level){
        this.level = level;
    }
    
    public BaseLevel getLevel(){return this.level;}
}
