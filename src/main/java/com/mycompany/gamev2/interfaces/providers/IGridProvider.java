/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.gamev2.interfaces.providers;

import com.mycompany.gamev2.component.level_components.grid_component.LevelGridTileV2;
import com.mycompany.gamev2.gamemath.Vector3;

/**
 *
 * @author J.A
 */
public interface IGridProvider {
    public Vector3 grid_to_screen(Vector3 grid_coords);
    public Vector3 screen_to_grid(Vector3 grid_coords);
    public int height_in_tiles();
    public int width_in_tiles();
    public LevelGridTileV2.COLISION_TYPE colision_at_tile(Vector3 grid_coords);
}
