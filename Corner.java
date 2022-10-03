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
    
    private int corner = -1;
    private boolean cornerTaked = false;
    private List<String> L = new ArrayList<>();
    private List<String> blackList = new ArrayList<>();
    
    @Override
    public void run(){
        
        setPoints();
        
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
        if(!isTeammate(e.getName()))
            fire(1);
        
    }
    
    @Override
    public void onHitByBullet(HitByBulletEvent e){}
    
    
    @Override
    public void onMessageReceived(MessageEvent e){
        
        if(e.getMessage().toString().contains("Point")){
            String[] msg = e.getMessage().toString().split(",");
            L.add(e.getSender() + "," + msg[1] + "," + msg[2]);
        }
        
        if(L.size() == 4) sendRobotToCorner();

    }
    
    public void sendRobotToCorner(){
        
        double dist;
        
        String[] ini0 =  L.get(0).split(",");
        String[] ini1 =  L.get(1).split(",");
        String[] ini2 =  L.get(2).split(",");
        String[] ini3 =  L.get(3).split(",");
        
        String[] names = { ini0[0], ini1[0], ini2[0], ini3[0] };
        
        Point iniPX[] = {
            new Point(Double.parseDouble(ini0[1]), Double.parseDouble(ini0[2])),
            new Point(Double.parseDouble(ini1[1]), Double.parseDouble(ini1[2])),
            new Point(Double.parseDouble(ini2[1]), Double.parseDouble(ini2[2])),
            new Point(Double.parseDouble(ini3[1]), Double.parseDouble(ini3[2]))
        };
        
        /* ---------------------------------------------- */

        List<Parell> L0 = new ArrayList<>();
        dist = iniP.distanceBetween(Corner[0]);
        for(int i = 0; i < 4; i++) L0.add(new Parell(names[i], iniPX[i].distanceBetween(Corner[0])));
        Collections.sort(L0);
        
        if((dist < L0.get(0).getDist() && !blackList.contains(getName()))){
            cornerTaked = true;
            blackList.add(getName());
            sendRobotToCornerBL(iniP);
        }
        else if(!cornerTaked && blackList.contains(L0.get(0).getName())){
            Parell nF = nextFreeRobot(L0);
            if(dist < nF.getDist()){
                out.println("DIST: " + dist + " FREE: " + nF.getDist() + " N: " + nF.getName());
                cornerTaked = true;
                blackList.add(getName());
                sendRobotToCornerBL(iniP);
            }
            else blackList.add(nF.getName());
        }
        else blackList.add(L0.get(0).getName());
        
        /* ---------------------------------------------- */
        
        List<Parell> L1 = new ArrayList<>();
        dist = iniP.distanceBetween(Corner[1]);
        for(int i = 0; i < 4; i++) L1.add(new Parell(names[i], iniPX[i].distanceBetween(Corner[1])));
        Collections.sort(L1);
        
        if((dist < L1.get(0).getDist() && !blackList.contains(getName()))){
            cornerTaked = true;
            blackList.add(getName());
            sendRobotToCornerBR(iniP);
        }
        else if(!cornerTaked && blackList.contains(L1.get(0).getName())){
            Parell nF = nextFreeRobot(L1);
            if(dist < nF.getDist()){
                cornerTaked = true;
                blackList.add(getName());
                sendRobotToCornerBR(iniP);
            }
            else blackList.add(nF.getName());
        }
        else blackList.add(L1.get(0).getName());
        
        /* ---------------------------------------------- */
        
        List<Parell> L2 = new ArrayList<>();
        dist = iniP.distanceBetween(Corner[2]);
        for(int i = 0; i < 4; i++) L2.add(new Parell(names[i], iniPX[i].distanceBetween(Corner[2])));
        Collections.sort(L2);
        
        if((dist < L2.get(0).getDist() && !blackList.contains(getName()))){
            cornerTaked = true;
            blackList.add(getName());
            sendRobotToCornerTR(iniP);
        }
        else if(!cornerTaked && blackList.contains(L2.get(0).getName())){
            Parell nF = nextFreeRobot(L2);
            if(dist < nF.getDist()){
                cornerTaked = true;
                blackList.add(getName());
                sendRobotToCornerTR(iniP);
            }
            else blackList.add(nF.getName());

        }
        else blackList.add(L2.get(0).getName());
        
        /* ---------------------------------------------- */
        
        List<Parell> L3 = new ArrayList<>();
        dist = iniP.distanceBetween(Corner[3]);
        for(int i = 0; i < 4; i++) L3.add(new Parell(names[i], iniPX[i].distanceBetween(Corner[3])));
        Collections.sort(L3);
        
        if((dist < L3.get(0).getDist() && !blackList.contains(getName()))){
            cornerTaked = true;
            blackList.add(getName());
            sendRobotToCornerTL(iniP);
        }
        else if(!cornerTaked && blackList.contains(L3.get(0).getName())){
            Parell nF = nextFreeRobot(L3);
            if(dist < nF.getDist()){
                cornerTaked = true;
                blackList.add(getName());
                sendRobotToCornerTL(iniP);
            }
            else blackList.add(nF.getName());
        }
        else blackList.add(L3.get(0).getName());
        
    }
    
    public Parell nextFreeRobot(List<Parell> L){
        int i = 0; boolean trobat = false;
        while(i < L.size() && !trobat){
            trobat = !blackList.contains(L.get(i).getName());
            i++;
        }
        return (i > 0) ? L.get(i-1) : L.get(0);
    }
    
    public void sendRobotToCornerBL(Point actP){
        corner = 0;
        turnRight(180.0 - getHeading());
        ahead(actP.getY());
        turnRight(90);
        ahead(actP.getX());
        turnGunRight(135.0);
        setColors(Color.BLACK, Color.BLACK, Color.BLACK);
        execute();
        checkCornerPosition();
    }
    
    public void sendRobotToCornerBR(Point actP){
        corner = 1;
        turnRight(180.0 - getHeading());
        ahead(actP.getY());
        turnLeft(90);
        ahead(Corner[1].getX() - actP.getX());
        turnGunLeft(135.0);
        setColors(Color.BLACK, Color.BLACK, Color.BLACK);
        execute();
        checkCornerPosition();
    }
    
    public void sendRobotToCornerTR(Point actP){
        corner = 2;
        turnRight(-getHeading());
        ahead(Corner[2].getY() - actP.getY());
        turnRight(90);
        ahead(Corner[2].getX() - actP.getX());
        turnGunRight(135.0);
        setColors(Color.BLACK, Color.BLACK, Color.BLACK);
        execute();
        checkCornerPosition();
    }
    
    public void sendRobotToCornerTL(Point actP){
        corner = 3;
        turnRight(-getHeading());
        ahead(Corner[3].getY() - actP.getY());
        turnLeft(90);
        ahead(actP.getX());
        turnGunLeft(135.0);
        setColors(Color.BLACK, Color.BLACK, Color.BLACK);
        execute();
        checkCornerPosition();
    }
    
    public void checkCornerPosition(){
        
        Point actP = new Point(getX(), getY());
        
        switch (corner) {
            case 0 -> {
                if(getX() != 18 && getY() != 18)
                    sendRobotToCornerBL(actP);
            }
            case 1 ->{
                if(getX() != (Corner[1].getX() - 18) && getY() != 18)
                    sendRobotToCornerBR(actP);
            }
            case 2 -> {
                if(getX() != (Corner[2].getX() - 18) && getY() != (Corner[2].getY() - 18))
                    sendRobotToCornerTR(actP);
            }
            case 3 ->{
                if(getX() != 18 && getY() != (Corner[3].getY() - 18))
                    sendRobotToCornerTL(actP);
            }
        }
        execute();
    }
    
    public void setPoints(){
        iniP = new Point(getX(), getY());
        Corner[0] = new Point(0.0, 0.0);
        Corner[1] = new Point(getBattleFieldWidth(), 0.0);
        Corner[2] = new Point(getBattleFieldWidth(), getBattleFieldHeight());
        Corner[3] = new Point(0.0, getBattleFieldHeight());
    }
    
}