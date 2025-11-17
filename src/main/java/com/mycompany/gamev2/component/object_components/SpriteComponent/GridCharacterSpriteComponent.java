/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.object_components.SpriteComponent;

import com.mycompany.gamev2.GameLoopV2;
import com.mycompany.gamev2.component.object_components.GridMovementComponent;
import com.mycompany.gamev2.component.object_components.ObjectComponent;
import com.mycompany.gamev2.event_system.EventManager;
import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.event_system.gameplay_events.CharacterStepEvent;
import com.mycompany.gamev2.event_system.gameplay_events.CharacterStepEvent.ECharacterStepSide;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.gameobjects.characters.GridPlayerCharacter2D;
import com.mycompany.gamev2.interfaces.characters.ECharacter;
import com.mycompany.gamev2.interfaces.event_listeners.IGameplayListener;
import com.mycompany.gamev2.io.image.ImageMaker;
import com.mycompany.gamev2.providers.CameraProvider;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Optional;

/**
 *
 * @author J.A
 */
public class GridCharacterSpriteComponent<T extends GridPlayerCharacter2D> extends ObjectComponent implements IGameplayListener {
    
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
    private ECharacterStepSide current_step = ECharacterStepSide.NEUTRAL;
       
    //step flags
    /*private boolean leftFired = false;
    private boolean rightFired = false;
    private boolean neutralFired = false;*/
        
    public GridCharacterSpriteComponent(T owner){
         super(owner);
         Optional<GridMovementComponent> op = owner.tryGetComponent(GridMovementComponent.class);
         this.owner_movement = op.isPresent() ? op.get() : null;
         
         EventManager.getInstance().subscribe(this, IGameplayListener.class);
    }

    @Override
    public void onEventReceived(BaseEvent event) {
        if(event instanceof CharacterStepEvent step_event){
            
            //we only react for step events of this component's owner
            if(step_event.getCharacter().equals(this.owner)){
                //System.out.println("EVENT RECEIVED: "+step_event.getProgress());
                double t = step_event.getProgress();    
                this.animate(t);
            }
            
        }
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
    
    private boolean state = true;
    private boolean state_changed = false;
    public void animate(double t){
        double f = GameLoopV2.getInstance().getFrames();
        
        
        
        if(t > 0.5d && !state_changed){ 
            //this.current_step = ECharacterStepSide.LEFT;
            state = !state;
            state_changed = true;
            System.out.println(state+" "+f);
        }
        
        if(state) this.current_step = ECharacterStepSide.LEFT;
        else this.current_step = ECharacterStepSide.RIGHT;
        
        if(t >= .6d) this.current_step = ECharacterStepSide.NEUTRAL;
        
        if(state_changed && t >= .99d){
            state_changed = false;
        }

    }
    

    @Override
    public void render(RenderEvent e) {
      
      //get the graphics object and the buffered image to draw to the screen  
      Graphics2D g = e.getGraphics();
      String src = this.active_image.getPath();
      BufferedImage img = ImageMaker.BufferedImageFromSource(src);
      
           
      //get the location to draw to, taking into account camera offsets
      Vector3 location = this.owner.getObjectLocation();
      if(location == null) {
          System.out.println("sprite null owner location");
          return;
      }
          
      //apply camera offsets for correct render
      Vector3 offsets = Vector3.ZERO;
      CameraProvider provider = e.getCameraProvider();
      if(provider != null) offsets = provider.getCameraOffsets();
      location = location.minus(offsets);
      
            
      
      int dest_x = (int) location.getX();
      int dest_y = (int) location.getY();
      
      //these are the dimensions to "cut out" from the character sheet
      Vector3 dimensions = this.active_image.getDimensions();
      int width  = (int) dimensions.getX();
      int height = (int) dimensions.getY();
      
      //and these are the dimensions the rendered image should be
      int r_width  = 30;
      int r_height = 41;
      
      //offsets to center the final render to the tile (vertically and horizontally)
      int x_offset = 2;
      int y_offset = -12;
      
      int step_y_wobble = (this.walk_index == 0) ? 0 : 2;
      
      
      //apply offsets
      int x = dest_x + x_offset;
      int y = dest_y + y_offset + step_y_wobble;
      int w = dest_x + r_width  + x_offset;
      int h = dest_y + r_height + y_offset + step_y_wobble;
      
      
      
      
      //FINAL OPERATIONS BEFORE RENDERING
      // (this needs clarification:
      //  walk-index is both the frame index (in the atlas) as well as the 1 pixel-tall 'buffer' in between frames)
      //  top-left and bottom-right corners of origin rectangle
      int origin_x_top_left = 0;
      int origin_y_top_left = this.walk_index*height+this.walk_index;
      
      int origin_x_bottom_right = width;
      int origin_y_bottom_right = this.walk_index*height+height+this.walk_index;
      
      g.drawImage(img, 
            x, y, // top-left     corner of destination rectangle
            w, h, // bottom-right corner of destination rectangle
            origin_x_top_left    , origin_y_top_left,
            origin_x_bottom_right, origin_y_bottom_right, 
            null);
      
       
      /*g.drawImage(img, 
            x, y, w, h, // top-left and bottom-right corners of destination rectangle
            0, this.walk_index*height+this.walk_index, width, this.walk_index*height+height+this.walk_index, 
            null);*/
      
    }

    @Override
    public void tick(TickEvent e) {
        
               
        this.owner_facing_direction = this.owner_movement.getFacing();
        this.owner_is_moving = this.owner_movement.getIsMoving();
        
        //if(!this.owner_is_moving) this.walk_index = 0;
        
        if(this.owner_is_moving){
            //if(this.current_step == ECharacterStepSide.LEFT) this.walk_index = 1;
           // else if(this.current_step == ECharacterStepSide.RIGHT) this.walk_index = 2;                        
            //else this.walk_index = 0;
            
            
        }
        
        switch(this.current_step){
                case ECharacterStepSide.LEFT:
                    this.walk_index = 1;
                    break;
                case ECharacterStepSide.RIGHT:
                    this.walk_index = 2; 
                    break;
                case ECharacterStepSide.NEUTRAL:
                    this.walk_index = 0;
                    break;
                    
            }
        
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
