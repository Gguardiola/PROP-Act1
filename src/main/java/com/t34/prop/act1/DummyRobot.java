/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.t34.prop.act1;
import robocode.HitByBulletEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;

/**
 *
 * @author USUARIO
 */
public class DummyRobot extends Robot {

    public void run(){
        turnLeft(getHeading());
        while(true){
            ahead(500);
            turnRight(90);
        
        }
    
    }

    public void onScannedRobot(ScannedRobotEvent e){
        fire(1);
        
    }
    
    public void onHitByBullet(HitByBulletEvent e){
        
        //turnLeft(180);
    }
}
