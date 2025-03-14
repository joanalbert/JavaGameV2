/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mycompany.gamev2.input_system.enums;
import java.awt.event.KeyEvent;
/**
 *
 * @author J.A
 */
public enum EKey {
    W(KeyEvent.VK_W),
    A(KeyEvent.VK_A),
    S(KeyEvent.VK_S),
    D(KeyEvent.VK_D),
    SPACE(KeyEvent.VK_SPACE),
    N(KeyEvent.VK_N),
    ESCAPE(KeyEvent.VK_ESCAPE),
    C(KeyEvent.VK_C);
    
    private final int keyCode;

    EKey(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public static EKey fromKeyCode(int keyCode) {
        for (EKey key : values()) {
            if (key.keyCode == keyCode) return key;
        }
        return null;
    }
}
