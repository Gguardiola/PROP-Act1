package prop.com.robocode;

public class Parell implements Comparable<Parell>{
    
    private final Double distance;
    private final String robotName;

    public Parell(String robotName, Double distance) {
        this.distance = distance;
        this.robotName = robotName;
    }

    public Double getDist() {
        return distance;
    }

    public String getName() {
        return robotName;
    }

    @Override
    public int compareTo(Parell o) {
        Double D = (this.distance - o.distance);
        return D.intValue();
    }

    @Override
    public String toString() {
        return "{" + "D=" + distance + 
                   ", N=" + robotName.charAt(robotName.length()-2) + '}';
    }
    
}
