/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.gamemath;

/**
 *
 * @author J.A
 */
public class Utils {
    public static boolean isInteger(double value) {
        return value == Math.floor(value) && !Double.isInfinite(value);
    }
}
