/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.object_components.SpriteComponent;

import com.mycompany.gamev2.component.level_components.camera_component.GridLevelCameraComponent;
import com.mycompany.gamev2.component.level_components.grid_component.LevelGridComponent;
import com.mycompany.gamev2.component.object_components.GridMovementComponent;
import com.mycompany.gamev2.component.object_components.ObjectComponent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.exceptions.NoSuchLevelComponentException;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.gameobjects.characters.GridPlayerCharacter2D;
import com.mycompany.gamev2.interfaces.characters.ECharacter;
import com.mycompany.gamev2.io.image.ImageMaker;
import com.mycompany.gamev2.levels.LevelManager;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Optional;

/**
 *
 * @author J.A
 */
public class GridCharacterSpriteComponent<T extends GridPlayerCharacter2D> extends ObjectComponent {
    
    //owner info
    protected GridMovementComponent owner_movement;
    protected Vector3 owner_facing_direction;
    protected boolean owner_is_moving;
    
    //sprite images
    protected final String MAIN_PATH = "textures/sprites/character";
    protected ECharacter character;
    
    protected HashMap<String, SpriteImage> images;
    protected SpriteImage active_image;
    
    int walk_index = 0;
    
        
    public GridCharacterSpriteComponent(T owner){
         super(owner);
         Optional<GridMovementComponent> op = owner.tryGetComponent(GridMovementComponent.class);
         this.owner_movement = op.isPresent() ? op.get() : null;
    }
    
    public void configure(ECharacter character){
        this.character = character;
        String chara = this.character.getName();
        
 
        this.images = new HashMap<String, SpriteImage>();
        
        //walk direction
        this.images.put("up",    new SpriteImage(15 , 21, MAIN_PATH+"/"+chara+"/walking_up.png",    "walk_up"));
        this.images.put("down",  new SpriteImage(15 , 21, MAIN_PATH+"/"+chara+"/walking_down.png",  "walk_down"));
        this.images.put("left",  new SpriteImage(15 , 21, MAIN_PATH+"/"+chara+"/walking_left.png",  "walk_left"));
        this.images.put("right", new SpriteImage(15 , 21, MAIN_PATH+"/"+chara+"/walking_right.png", "walk_right"));
        
        this.active_image = this.images.get("down");
    }

    @Override
    public void render(RenderEvent e) {
      
      //get the graphics object and the buffered image to draw to the screen  
      Graphics2D g = e.getGraphics();
      String src = this.active_image.getPath();
      BufferedImage img = ImageMaker.BufferedImageFromSource(src);
      
           
      //get the location to draw to, taking into account camera offsets
      Vector3 location = this.owner.getObjectLocation();
          
      //change location based on camera
      try{
        LevelGridComponent grid = LevelManager.getInstance().getCurrentLevel().getComponent(LevelGridComponent.class);
        if(grid != null && grid.getIsCameraFollow()){
          GridLevelCameraComponent cam  = LevelManager.getInstance().getCurrentLevel().getComponent(GridLevelCameraComponent.class);
          Vector3 offset = cam.getCamOffsets();
          location = location.minus(offset);
        }
      } catch (NoSuchLevelComponentException ex){System.out.println(ex.getMessage());}
      
      
      int dest_x = (int) location.getX();
      int dest_y = (int) location.getY();
      
      //these are the dimensions to "cut out" from the character sheet
      Vector3 dimensions = this.active_image.getDimensions();
      int width  = (int) dimensions.getX();
      int height = (int) dimensions.getY();
      
      //and these are the dimensions the rendered image should be
      int r_width  = 30;
      int r_height = 40;
      
      //offsets to center the final render to the tile (vertically and horizontally)
      int x_offset = 1;
      int y_offset = -9;;
      
      //apply offsets
      int x = dest_x + x_offset;
      int y = dest_y + y_offset;
      int w = dest_x + r_width  + x_offset;
      int h = dest_y + r_height + y_offset;
      
      
      g.drawImage(img, 
            x, y, w, h, // Destination rectangle
            0, this.walk_index, width, height, // Source rectangle
            null);
    }

    @Override
    public void tick(TickEvent e) {
        
               
        this.owner_facing_direction = this.owner_movement.getFacing();
        this.owner_is_moving = this.owner_movement.getIsMoving();
        
        if(!this.owner_is_moving) this.walk_index = 0;
        
        if(this.owner_facing_direction.equals(Vector3.UP)){
            this.active_image = this.images.get("up");
        }
        
        if(this.owner_facing_direction.equals(Vector3.DOWN)){
            this.active_image = this.images.get("down");
        }
        
        if(this.owner_facing_direction.equals(Vector3.LEFT)){
            this.active_image = this.images.get("left");
        }
        
        if(this.owner_facing_direction.equals(Vector3.RIGHT)){
            this.active_image = this.images.get("right");
        }
        
    }
    
    
}
