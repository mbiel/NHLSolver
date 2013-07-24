package nhlsolver;

/**
 * Created by IntelliJ IDEA.
 * User: Mike
 * Date: 10/23/11
 * Time: 7:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class Team {
    private final String name;
    private final double latitude;
    private final double longitude;

    public Team(String s, double lt, double lng) {
        name = s;
        latitude = lt;
        longitude = lng;
    }

    public double distCompute(Team other) {
        double temp;
        temp = Math.pow(latitude-other.latitude, 2)+Math.pow(longitude-other.longitude,2);
        return Math.sqrt(temp);
    }

    public String getname() {
        return name;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
