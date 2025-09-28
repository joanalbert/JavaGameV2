/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mycompany.gamev2.interfaces.characters;

/**
 *
 * @author J.A
 */
public enum ECharacter {
    BRENDAN("brendan"),
    MAY("may");

    private final String name;

    ECharacter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
