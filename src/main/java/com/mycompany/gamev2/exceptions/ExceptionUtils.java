/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.exceptions;

/**
 *
 * @author J.A
 */
public abstract class ExceptionUtils {
    
    public static String get_exception_location(){
        // Get the call stack to find the caller of getComponent()
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        // stackTrace[0] = Thread.getStackTrace
        // stackTrace[1] = getComponent (this method)
        // stackTrace[2] = the method that called getComponent ← this is what we want
        StackTraceElement caller = stackTrace.length > 3 ? stackTrace[3] : null;

        String location = caller != null
            ? caller.getFileName() + "." + caller.getMethodName() + " (line " + caller.getLineNumber() + ")"
            : "Unknown location";
        
        return location;
    }
}
