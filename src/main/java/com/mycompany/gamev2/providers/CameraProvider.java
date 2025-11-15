/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.providers;

import com.mycompany.gamev2.providers.base.BaseProvider;
import com.mycompany.gamev2.component.level_components.camera_component.GridLevelCameraComponent;
import com.mycompany.gamev2.component.level_components.grid_component.LevelGridComponent;
import com.mycompany.gamev2.exceptions.NoSuchLevelComponentException;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.interfaces.providers.ICameraProvider;
import com.mycompany.gamev2.levels.LevelManager;

/**
 *
 * @author J.A
 */
public class CameraProvider extends BaseProvider implements ICameraProvider {

    @Override
    public Vector3 getCameraOffsets() {
      Vector3 offset = Vector3.ZERO;
        
      try{
            LevelGridComponent grid = LevelManager.getInstance().getCurrentLevel().getComponent(LevelGridComponent.class);
            if(grid != null && grid.getIsCameraFollow()){
              GridLevelCameraComponent cam  = LevelManager.getInstance().getCurrentLevel().getComponent(GridLevelCameraComponent.class);
              offset = cam.getCamOffsets();
            }
      } catch (NoSuchLevelComponentException ex){System.out.println(ex.getMessage());}
        
       return offset;
    }
    
    
}
