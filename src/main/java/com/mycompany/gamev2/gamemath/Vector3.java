/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.gamemath;

import java.util.Objects;

/**
 *
 * @author J.A
 */
public class Vector3 {
    
    public static final Vector3 ZERO = new Vector3(0,0,0);
    public static final Vector3 UP = new Vector3(0,-1,0);
    public static final Vector3 DOWN = new Vector3(0,1,0);
    public static final Vector3 LEFT = new Vector3(-1,0,0);
    public static final Vector3 RIGHT = new Vector3(1,0,0);
    
    private double x,y,z;
    
    public Vector3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3 getScaled(double factor){
        return new Vector3(this.x*factor, this.y*factor, this.z*factor);
    }
    
    public void scale(double factor){
        this.x*=factor;
        this.y*=factor;
        this.z*=factor;
    }
    
    public double magnitude(){
        return Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
    }
    
    public double squaredMagnitude() {
        return this.x*this.x + this.y*this.y + this.z*this.z;
    }

    
    public Vector3 normalize(){
        double m = this.magnitude();
        if (m == 0) { // Prevent division by zero
            return ZERO;
        } else {
            return new Vector3(this.x / m,
                               this.y / m,
                               this.z / m);
        }
    }
    
    public Vector3 plus(Vector3 other){
        return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
    }
    
    public Vector3 minus(Vector3 other){
         return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
    }
    
    public double dot(Vector3 other){
        return this.x*other.x + this.y*other.y + this.z*other.z;
    }
    
    public Vector3 cross(Vector3 v){
        //yz
        //zx
        //xy
        return new Vector3(
            this.y * v.z - this.z * v.y,  
            this.z * v.x - this.x * v.z,  
            this.x * v.y - this.y * v.x   
        );
    }
    
    public double angleBetween(Vector3 v){
        Vector3 n2 = v.normalize();
        Vector3 n1 = this.normalize();
        double d = n1.dot(n2);
        return Math.acos(d) / Math.PI * 180d;
    }
    
    @Override
    public String toString() {
        return String.format("Vector3(%.3f, %.3f, %.3f)", x, y, z);
    }
    
   
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vector3 vector3 = (Vector3) obj;
        return Double.compare(vector3.x, x) == 0 &&
               Double.compare(vector3.y, y) == 0 &&
               Double.compare(vector3.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
