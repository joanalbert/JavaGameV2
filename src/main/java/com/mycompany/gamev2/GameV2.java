/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.gamev2;

import com.mycompany.gamev2.window.MyWindow;

/**
 *
 * @author J.A
 */
public class GameV2 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        MyWindow.Construct("test");
        
        GameLoopV2 loop = GameLoopV2.getInstance();
        loop.start();
    }
}
