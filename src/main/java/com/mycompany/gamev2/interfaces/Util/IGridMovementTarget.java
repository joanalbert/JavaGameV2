/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.gamev2.interfaces.Util;

import com.mycompany.gamev2.Utils.GridMoveTimer;
import com.mycompany.gamev2.gamemath.Vector3;

/**
 *
 * @author J.A
 */
public interface IGridMovementTarget {
   public boolean getStopped_moving();
   public boolean getIsMoving();
   public boolean is_move_completed();
   public boolean getStarted_moving();
   public Vector3 getTargetPos();
   public Vector3 getStartPos();
   public GridMoveTimer getMove_timer();
}
