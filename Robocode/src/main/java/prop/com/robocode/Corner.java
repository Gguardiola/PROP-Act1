package prop.com.robocode;

import robocode.*;
import java.awt.Color;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Corner extends TeamRobot{

    enum type { DEFAULT, CORNER, KAMIKAZE
    } type MODEL = type.DEFAULT;
   
    enum state { START, toCORNER, CORNER, ATTACK, SENTINEL, FOCUS
    } state STATUS = state.START;
        private Point iniP = new Point();
    private Point[] Corner  = {new Point(), new Point(), new Point(), new Point()};
    private Point[] CornerX = {new Point(), new Point(), new Point(), new Point()};
    
    private int turnCounter = 1;    
    private int     corner = -1;
    private boolean cornerTaked = false;
    private List<String> L = new ArrayList<>();
    private List<String> blackList = new ArrayList<>();
    
    @Override
    public void run(){
        
        setPoints();
        
        try { broadcastMessage("Point," + iniP.toString());
        } catch (IOException ignored) {}
                
        while(true){
            
            sentinelMovement();
            
            switch(STATUS){
                case toCORNER -> {
                    while(STATUS != state.CORNER)
                        checkCornerPosition();
                }
                case CORNER -> {
                    moveRadar();
                }
                case ATTACK -> {
                    setTurnRadarRight(360);
                }
            }
            
            execute();
        }
        
    }
    
    @Override
    public void onScannedRobot(ScannedRobotEvent e){
        
        if(!isTeammate(e.getName())){
            if(STATUS == state.toCORNER) fire(1);
            else if(STATUS == state.ATTACK){
                out.println("NAME: " + e.getName() + " DIST: " + e.getDistance());
                turnRadarRight(-getRadarHeading());
                attackRobot(e.getHeading(), e.getDistance());
            }
        }
    }
    
    @Override
    public void onHitWall(HitWallEvent e){
        if(STATUS == state.toCORNER || STATUS == state.ATTACK){
            if (e.getBearing() > 0.0) turnLeft(-e.getBearing());
            else turnRight(-e.getBearing());
        }
    }
    
    @Override
    public void onMessageReceived(MessageEvent e){
        
        if(e.getMessage().toString().contains("Point")){
            String[] msg = e.getMessage().toString().split(",");
            L.add(e.getSender() + "," + msg[1] + "," + msg[2]);
        } if(L.size() == 4) decideCornerRobot();

    }
    
    public void decideCornerRobot(){
        
        STATUS = state.toCORNER;
        
        String[] ini0 =  L.get(0).split(",");
        String[] ini1 =  L.get(1).split(",");
        String[] ini2 =  L.get(2).split(",");
        String[] ini3 =  L.get(3).split(",");
        
        String[] names = {ini0[0], ini1[0], ini2[0], ini3[0]};
        
        Point iniPX[] = {
            new Point(Double.parseDouble(ini0[1]), Double.parseDouble(ini0[2])),
            new Point(Double.parseDouble(ini1[1]), Double.parseDouble(ini1[2])),
            new Point(Double.parseDouble(ini2[1]), Double.parseDouble(ini2[2])),
            new Point(Double.parseDouble(ini3[1]), Double.parseDouble(ini3[2]))
        };
        
        for(int i = 0; i < 4; i++){
            
            List<Parell> distL = new ArrayList<>();
            double dist = iniP.distanceBetween(Corner[i]);
            for(int j = 0; j < 4; j++) distL.add(new Parell(names[j], iniPX[j].distanceBetween(Corner[i])));
            Collections.sort(distL);

            if((dist < distL.get(0).getDist()) && !blackList.contains(getName())){
                cornerTaked = true; corner = i;
                blackList.add(getName());
            }
            else if(!cornerTaked && blackList.contains(distL.get(0).getName())){
                Parell nF = nextFreeRobot(distL);
                if(dist < nF.getDist()){
                    cornerTaked = true; corner = i;
                    blackList.add(getName());
                }
                else blackList.add(nF.getName());
            }
            else blackList.add(distL.get(0).getName());
            
        }
                        
        MODEL  = cornerTaked ? type.CORNER    : type.KAMIKAZE;
        STATUS = cornerTaked ? state.toCORNER : state.ATTACK;
        
        if(MODEL == type.KAMIKAZE) 
            setColors(Color.WHITE, Color.RED, Color.RED);
        
    }
    
    public Parell nextFreeRobot(List<Parell> L){
        int i = 0; boolean trobat = false;
        while(i < L.size() && !trobat){
            trobat = !blackList.contains(L.get(i).getName()); i++;
        } return (i > 0) ? L.get(i-1) : L.get(0);
    }
   
    
    public void checkCornerPosition(){
        
        Point actP = new Point(getX(), getY());
        boolean checkCorner = false;
                
        switch (corner) {
            case 0 -> {
                if(getX() > 20 || getY() > 20) 
                    sendRobotToCorner(actP);                    
                else checkCorner = true;
            }
            case 1 ->{
                if(getX() < (Corner[1].getX() - 20) || getY() > 20) 
                    sendRobotToCorner(actP);                 
                else checkCorner = true;
            }
            case 2 -> {
                if(getX() < (Corner[2].getX() - 20) || getY() < (Corner[2].getY() - 20))
                    sendRobotToCorner(actP);                  
                else checkCorner = true;
            }
            case 3 ->{
                if(getX() > 20 || getY() < (Corner[3].getY() - 20))
                    sendRobotToCorner(actP); 
                
                else checkCorner = true;
            }
        }
        
        out.println("Corner: " + corner);
        
        STATUS = checkCorner ? state.CORNER : state.toCORNER;
        
        if(STATUS == state.CORNER)
            aimCenter();
        
        execute();
    }
    
    public void sendRobotToCorner(Point P){
                                
        double phi   = 0.0;
        double beta  = 0.0;
        double alpha = getHeading();

        switch(corner){
            case 0 -> {
                phi = Math.toDegrees(Math.atan((P.getY() - 18.0)/(P.getX() - 18.0)));
                phi = Math.abs(phi); beta = (360.0 - alpha) - (90 + phi);
            } case 1 -> {
                phi = Math.toDegrees(Math.atan((P.getY() - 18.0)/(CornerX[1].getX() - P.getX()))); 
                phi = Math.abs(phi); beta = -alpha + (90 + phi);
            } case 2 -> {
                phi = Math.toDegrees(Math.atan((CornerX[2].getY() - P.getY())/(CornerX[2].getX() - P.getX())));
                phi = Math.abs(phi); beta = -alpha + (90 - phi);
            } case 3 -> {
                phi = Math.toDegrees(Math.atan((CornerX[3].getY() - P.getY())/(P.getX() - 18.0)));
                phi = Math.abs(phi); beta = (360.0 - alpha) - (90 - phi);
            }
        }
        
        out.println("BETA: " + beta);
        
        if(Math.abs(beta) > 0.01) setTurnRight(beta);
        else setAhead(P.distanceBetween(CornerX[corner]));
        
        execute();
    }
    
    public void aimCenter(){
        
        double alpha = getHeading();
        if(alpha > 180.0) alpha = (-1)*(360-alpha);
        turnRight(-alpha);
        
        switch(corner){
            case 0 -> setTurnRight(45);
            case 1 -> setTurnLeft(45);
            case 2 -> setTurnLeft(135);
            case 3 -> setTurnRight(135);
        }
        execute();
    }
    
    public void attackRobot(double alpha, double dist){
        
        execute();
        
    }
    
    public void moveRadar(){
        
        for(int i = 0; i < 5; i++) turnRadarRight(9);
        for(int i = 0; i < 5; i++) turnRadarRight(-9);
        for(int i = 0; i < 5; i++) turnRadarRight(-9);
        for(int i = 0; i < 5; i++) turnRadarRight(9);        
       
    }
    
    public void rotateRobots(){
        
        corner = (corner + 1) % 4;
        Point actP = new Point(getX(), getY());
        sendRobotToCorner(actP);
        execute();
        
    }
    
    public void sentinelMovement(){
        if(STATUS == state.CORNER && getTime() > 300*turnCounter){
            STATUS = state.toCORNER; turnCounter++;
            corner = (corner + 1) % 4; 
        }
    }
    
    public void setPoints(){
        iniP = new Point(getX(), getY());
        Corner[0] = new Point(0.0, 0.0);
        Corner[1] = new Point(getBattleFieldWidth(), 0.0);
        Corner[2] = new Point(getBattleFieldWidth(), getBattleFieldHeight());
        Corner[3] = new Point(0.0, getBattleFieldHeight());
        
        CornerX[0] = new Point(18.0, 18.0);
        CornerX[1] = new Point(getBattleFieldWidth()-18.0, 18.0);
        CornerX[2] = new Point(getBattleFieldWidth()-18.0, getBattleFieldHeight()-18.0);
        CornerX[3] = new Point(18.0, getBattleFieldHeight()-18.0);
    }
    
}