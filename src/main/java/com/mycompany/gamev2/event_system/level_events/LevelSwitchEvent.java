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
public class LevelSwitchEvent extends BaseEvent {
    private BaseLevel oldLevel;
    private BaseLevel newLevel;
    
    public LevelSwitchEvent(BaseLevel old, BaseLevel neww){
        this.oldLevel = old;
        this.newLevel = neww;
    }

    public BaseLevel getOldLevel() {
        return oldLevel;
    }

    public BaseLevel getNewLevel() {
        return newLevel;
    }
    
    
}
