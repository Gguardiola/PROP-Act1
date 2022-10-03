package prop.com.robocode;

import robocode.*;
import java.awt.Color;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Corner extends TeamRobot{
    
    private Point iniP;
    private Point[] Corner = { 
        new Point(0.0, 0.0), new Point(0.0, 0.0),
        new Point(0.0, 0.0), new Point(0.0, 0.0) };
    
    private boolean cornerTaked = false;
    private List<String> L = new ArrayList<>();
    private List<String> blackList = new ArrayList<>();
    
    @Override
    public void run(){
        
        iniP = new Point(getX(), getY());
        Corner[0] = new Point(0.0, 0.0);
        Corner[1] = new Point(getBattleFieldWidth(), 0.0);
        Corner[2] = new Point(getBattleFieldWidth(), getBattleFieldHeight());
        Corner[3] = new Point(0.0, getBattleFieldHeight());
        
        try { broadcastMessage("Point," + iniP.toString());
        } catch (IOException ex) {}
                
        while(true){
            turnGunLeft(20);
            turnRadarRight(20);
            execute();
        }
        
    }
    
    @Override
    public void onScannedRobot(ScannedRobotEvent e){
    }
    
    @Override
    public void onHitByBullet(HitByBulletEvent e){}
    
    @Override
    public void onMessageReceived(MessageEvent e){
        
        if(e.getMessage().toString().contains("Point")){
            String[] msg = e.getMessage().toString().split(",");
            L.add(e.getSender() + " " + msg[1] + " " + msg[2]);
        }
        
        out.println(e.getMessage().toString());
        
        if(L.size() == 4) sendRobotToCorner();

    }
    
    public void sendRobotToCorner(){
        
        double dist;
        
        String[] ini0 =  L.get(0).split(" ");
        String[] ini1 =  L.get(1).split(" ");
        String[] ini2 =  L.get(2).split(" ");
        String[] ini3 =  L.get(3).split(" ");
        
        String[] names = { ini0[0], ini1[0], ini2[0], ini3[0] };
        
        Point iniPX[] = {
            new Point(Double.parseDouble(ini0[1]), Double.parseDouble(ini0[2])),
            new Point(Double.parseDouble(ini1[1]), Double.parseDouble(ini1[2])),
            new Point(Double.parseDouble(ini2[1]), Double.parseDouble(ini2[2])),
            new Point(Double.parseDouble(ini3[1]), Double.parseDouble(ini3[2]))
        };
        

        List<Parell> L0 = new ArrayList<>();
        dist = iniP.distanceBetween(Corner[0]);
        for(int i = 0; i < 4; i++) L0.add(new Parell(getName(), iniPX[i].distanceBetween(Corner[0])));
        Collections.sort(L0);
        
        if(dist < L0.get(0).getDist() && !blackList.contains(getName())){
            cornerTaked = true;
            blackList.add(getName());
            sendRobotToCornerBL(iniP);
        }
        else blackList.add(L0.get(0).getName());
        
        List<Double> L1 = new ArrayList<>();
        dist = iniP.distanceBetween(Corner[1]);
        for(int i = 0; i < 4; i++) L1.add(iniPX[i].distanceBetween(Corner[1]));
        Collections.sort(L1);
        
        if(dist < L1.get(0) && !cornerTaked){
            cornerTaked = true;
            sendRobotToCornerBR(iniP);
        }
        
        List<Double> L2 = new ArrayList<>();
        dist = iniP.distanceBetween(Corner[2]);
        for(int i = 0; i < 4; i++) L2.add(iniPX[i].distanceBetween(Corner[2]));
        Collections.sort(L2);
        
        if(dist < L2.get(0) && !cornerTaked){
            cornerTaked = true;
            sendRobotToCornerTR(iniP);
        }
        
        List<Double> L3 = new ArrayList<>();
        dist = iniP.distanceBetween(Corner[3]);
        for(int i = 0; i < 4; i++) L3.add(iniPX[i].distanceBetween(Corner[3]));
        Collections.sort(L3);
        
        if(dist < L3.get(0) && !cornerTaked){
            cornerTaked = true;
            sendRobotToCornerTL(iniP);
        }
        
        
    }
    
    public void sendRobotToCornerBL(Point actP){
        turnRight(180.0 - getHeading());
        ahead(actP.getY());
        turnRight(90);
        ahead(actP.getX());
        turnGunRight(135.0);
        setColors(Color.BLACK, Color.BLACK, Color.BLACK);
        execute();
    }
    
    public void sendRobotToCornerBR(Point actP){
        turnRight(180.0 - getHeading());
        ahead(actP.getY());
        turnLeft(90);
        ahead(Corner[1].getX() - actP.getX());
        turnGunLeft(135.0);
        setColors(Color.BLACK, Color.BLACK, Color.BLACK);
        execute();
    }
    
    public void sendRobotToCornerTR(Point actP){
        turnRight(-getHeading());
        ahead(Corner[2].getY() - actP.getY());
        turnRight(90);
        ahead(Corner[2].getX() - actP.getX());
        turnGunRight(135.0);
        setColors(Color.BLACK, Color.BLACK, Color.BLACK);
        execute();
    }
    
    public void sendRobotToCornerTL(Point actP){
        turnRight(-getHeading());
        ahead(Corner[3].getY() - actP.getY());
        turnLeft(90);
        ahead(actP.getX());
        turnGunLeft(135.0);
        setColors(Color.BLACK, Color.BLACK, Color.BLACK);
        execute();
    }
    
}