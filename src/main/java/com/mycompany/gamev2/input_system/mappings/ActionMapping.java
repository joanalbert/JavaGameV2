/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system.mappings;

import com.mycompany.gamev2.input_system.interfaces.IAction;
import com.mycompany.gamev2.input_system.enums.EKey;
import java.util.HashMap;
/**
 *
 * @author J.A
 */
public class ActionMapping<T extends Enum<T> & IAction> {
    protected HashMap<EKey, T> keyToAction = new HashMap<>();

    public T getActionForKey(EKey key) {
        return keyToAction.get(key);
    }
}
