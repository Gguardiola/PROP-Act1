package com.t34.prop.act1;

import java.awt.Color;
import java.awt.Graphics2D;
import robocode.HitByBulletEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;

/**
 *
 * @author T34
 */
public class TorreRobot extends Robot {
    
    private static double bearingThreshold = 5;
    
    public void run(){
        
        turnLeft(getHeading());
        while(true){
            
            turnGunLeft(90);
            turnRadarLeft(90);
            
        }
    }
    
    double normalizeBearing(double bearing){
        
        while(bearing > 180) bearing -= 360;
        while(bearing < -180) bearing += 360;
        return bearing;
    }
    
    public void onScannedRobot(ScannedRobotEvent e){
        
        if(normalizeBearing(e.getBearing()) > bearingThreshold){
            fire(1);
            
        }
    }
    
    public void onHitByBullet(HitByBulletEvent e){
        
        //turnLeft(180);
    }
    @Override
    public void onPaint(Graphics2D g){
        
        int r = 60;
        g.setColor(Color.green);
        g.drawOval((int)getX()-1, (int) getY()-r,2*r,2*r);
    }
    
}
