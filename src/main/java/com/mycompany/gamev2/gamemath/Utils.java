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
    
    public static double dlerp(double a, double b, double t){
        //double easedT = t * t * (3.0 - 2.0 * t); // Smoothstep
        return a + (b-a) * t;
    }
    
    public static Vector3 dlerp(Vector3 a, Vector3 b, double t){
        
        double x = dlerp(a.getX(), b.getX(), t);
        double y = dlerp(a.getY(), b.getY(), t);
        double z = dlerp(a.getZ(), b.getZ(), t);
        return new Vector3(x,y,z);
    }
}
