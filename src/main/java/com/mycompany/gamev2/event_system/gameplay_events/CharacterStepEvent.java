/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.event_system.gameplay_events;

import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.gameobjects.characters.Character;
/**
 *
 * @author J.A
 */



public class CharacterStepEvent extends BaseEvent {
    
    public enum ECharacterStepSide{
        LEFT,
        RIGHT,
        NEUTRAL
    }
    
    private Character character;
    private ECharacterStepSide step;
    
    public CharacterStepEvent(Character character, ECharacterStepSide step){
        this.character = character;
        this.step = step;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public ECharacterStepSide getStep() {
        return step;
    }

    public void setStep(ECharacterStepSide step) {
        this.step = step;
    }
    
    
}
