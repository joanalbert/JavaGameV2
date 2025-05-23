/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system.InputActions;

import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.input_system.BindKey;
import com.mycompany.gamev2.input_system.InputManager;
import java.util.Set;

/**
 *
 * @author J.A
 */
public class IA_Look extends InputAction {
    public IA_Look(){
        super("IA_Look", InputAction.ActionType.AXIS_2D);
    }

    @Override
    public void evaluateAxes(Set<BindKey> activeKeys) {
        Vector3 mpos      = InputManager.getInstance().getMousePos();
        Vector3 prev_mpos = InputManager.getInstance().getMousePos_prev();
        Vector3 mouse_dir = mpos.minus(prev_mpos);
        
        double x_scale = Math.signum(mouse_dir.getX());
        double y_scale = Math.signum(mouse_dir.getY());
        
        setAxisValues(x_scale, y_scale);
    }
 
    
}
