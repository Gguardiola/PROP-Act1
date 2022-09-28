package com.t34.prop.act1;
import robocode.HitByBulletEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;

/**
 *
 * @author T34
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
