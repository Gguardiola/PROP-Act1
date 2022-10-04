package prop.com.robocode;

// Agafem d'expemple la classe Point que utilitza Robocode en els seus robots
public class Point {
    
    private double x, y;
    
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
            return x;
    }

    public double getY() {
            return y;
    }
    
    // TODO: Especificacio, fácil
    public double distanceBetween(Point P){
        return Math.sqrt(Math.pow(Math.abs(P.getX() - this.x), 2.0) +
                         Math.pow(Math.abs(P.getY() - this.y), 2.0));
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
    
}
