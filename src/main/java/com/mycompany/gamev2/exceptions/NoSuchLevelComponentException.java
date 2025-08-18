/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.exceptions;

/**
 *
 * @author J.A
 */
public class NoSuchLevelComponentException extends Exception{
    
    public NoSuchLevelComponentException(String message) {
        super("NoSuchLevelComponentException: " + message);
    }
}
