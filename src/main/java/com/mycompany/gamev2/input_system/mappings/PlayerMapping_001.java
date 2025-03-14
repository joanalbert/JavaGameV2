/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system.mappings;
import com.mycompany.gamev2.input_system.enums.EKey;
import com.mycompany.gamev2.input_system.enums.EWalkingAction;
/**
 *
 * @author J.A
 */
public class PlayerMapping_001 extends ActionMapping<EWalkingAction> {
    public PlayerMapping_001() {
        keyToAction.put(EKey.A, EWalkingAction.MOVE_LEFT);
        keyToAction.put(EKey.D, EWalkingAction.MOVE_RIGHT);
        keyToAction.put(EKey.SPACE, EWalkingAction.JUMP);
        keyToAction.put(EKey.N, EWalkingAction.NEXT_LEVEL);
        keyToAction.put(EKey.S, EWalkingAction.SOME_ACTION);
    }
}
