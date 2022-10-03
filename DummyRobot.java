package prop.com.robocode;

import robocode.TeamRobot;
import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;

import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import robocode.MessageEvent;

public class DummyRobot extends TeamRobot{
    
    @Override
    public void run(){
        
        setColors(Color.BLACK, Color.RED, Color.RED);
        turnLeft(getHeading());
        
        double dist = distanceB(getX(), getY(), 0.0, 0.0);
        try {
            broadcastMessage(dist);
        } catch (IOException ex) {
            Logger.getLogger(DummyRobot.class.getName()).log(Level.SEVERE, null, ex);
        }
            execute();
        
        while(true){
            ahead(500);
            turnRight(90);
        }
    }
    
    public double distanceB(double xO, double yO, double xD, double yD){
        return Math.sqrt(Math.pow(Math.abs(xD - xO), 2.0) +
                         Math.pow(Math.abs(yD - yO), 2.0));
    }
    
    @Override
    public void onMessageReceived(MessageEvent event) {
        setDebugProperty("MSG [" + event.getSender() + "]", event.getMessage().toString());
    }
    
    @Override
    public void onScannedRobot(ScannedRobotEvent e){
        fire(1);
    }
    
    @Override
    public void onHitByBullet(HitByBulletEvent e){
        
    }
    
}
