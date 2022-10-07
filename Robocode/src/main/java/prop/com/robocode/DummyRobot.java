package prop.com.robocode;

import robocode.TeamRobot;
import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;

import java.awt.Color;
import robocode.HitWallEvent;


public class DummyRobot extends TeamRobot{
    
    @Override
    public void run(){
        
        setColors(Color.WHITE, Color.WHITE, Color.WHITE);
        turnLeft(getHeading());
        
        while(true){
            ahead(500);
            turnRight(90);
        }
    }
    
    @Override
    public void onScannedRobot(ScannedRobotEvent e){
    }
    
    @Override
    public void onHitByBullet(HitByBulletEvent e){ 
    }
    
    @Override
    public void onHitWall(HitWallEvent e){
        out.println(e.getBearing());
    }
    
}
