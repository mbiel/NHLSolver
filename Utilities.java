package nhlsolver;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Mike
 * Date: 10/23/11
 * Time: 11:56 PM
 * To change this template use File | Settings | File Templates.
 */
public final class Utilities {
    public static final ArrayList<Team> TEAMS = getTeams();
    public static final double[][] COST_MATRIX = getCostMatrix();

    private Utilities() {}

    public static ArrayList<Team> getTeams() {
        ArrayList<Team> teams = new ArrayList<Team>();
        teams.add(new Team("ANA", 33.8078150628187, -117.87654161453247));
        teams.add(new Team("BOS", 42.366281167807934, -71.06226593255996));
        teams.add(new Team("BUF", 42.87494984499334, -78.87632131576538));
        teams.add(new Team("CGY", 51.03745752757153, -114.05205488204956));
        teams.add(new Team("CAR", 35.803320112371956, -78.72194677591324));
        teams.add(new Team("CHI", 41.88058924492546, -87.67414927482605));
        teams.add(new Team("COL", 39.74856961976832, -105.00763803720474));
        teams.add(new Team("CBJ", 39.96927220845562, -83.00610780715942));
        teams.add(new Team("DAL", 32.790420054007754, -96.81029230356216));
        teams.add(new Team("DET", 42.32514033170812, -83.05145859718323));
        teams.add(new Team("EDM", 53.571294635376056, -113.45601975917816));
        teams.add(new Team("FLA", 26.15837033358863, -80.32539546489715));
        teams.add(new Team("LAK", 34.04303865743706, -118.26711416244507));
        teams.add(new Team("MIN", 44.94488683641523, -93.10120761394501));
        teams.add(new Team("MTL", 45.49591319297404, -73.56908798217773));
        teams.add(new Team("NSH", 36.15911963214768, -86.77848190069198));
        teams.add(new Team("NJD", 40.733611, -74.171111));
        teams.add(new Team("NYI", 40.7228416889373, -73.59057247638702));
        teams.add(new Team("NYR", 40.750481718523844, -73.9935502409935));
        teams.add(new Team("OTT", 45.2968034780928, -75.92723786830902));
        teams.add(new Team("PHI", 39.90112133861436, -75.17172664403915));
        teams.add(new Team("PHX", 33.53171403984821, -112.26128339767456));
        teams.add(new Team("PIT", 40.439444, -79.989167));
        teams.add(new Team("SJS", 37.33274407376185, -121.90135449171066));
        teams.add(new Team("STL", 38.62677200283504, -90.20258724689483));
        teams.add(new Team("TBL", 27.94266136819524, -82.45185881853103));
        teams.add(new Team("TOR", 43.64329216093772, -79.37907457351684));
        teams.add(new Team("VAN", 49.276586305613904, -123.11197221279144));
        teams.add(new Team("WSH", 38.898007404102145, -77.02102392911911));
        teams.add(new Team("WPG", 49.892892, -97.143836));
        return teams;
    }

    public static double[][] getCostMatrix() {
        double[][] result = new double[30][30];
        ArrayList teams = TEAMS;
        for(int i=0; i<teams.size(); i++) {
            for(int j=0;j<teams.size(); j++) {
                Team ti = (Team) teams.get(i);
                Team tj = (Team) teams.get(j);
                result[i][j] = ti.distCompute(tj);
            }
        }
        return result;
    }

    public static BiMap<Integer, String> getTeamIDMap(ArrayList<Team> teams) {
        BiMap<Integer, String> result = HashBiMap.create();
        for(int i=0; i<teams.size(); i++) {
            result.put(i, teams.get(i).getname());
        }

        return result;
    }

    public static League buildLeagueFromTeamList(ArrayList<Team> list) {
        ArrayList<Integer> intlist = new ArrayList<Integer>();
        BiMap<Integer, String> ids = getTeamIDMap(TEAMS);
        for (Team t: list) {
            intlist.add(ids.inverse().get(t.getname()));
        }
        return new League(intlist);
    }

    public static League buildRandomLeague() {
        ArrayList<Team> teams = TEAMS;
        Collections.shuffle(teams);
        return buildLeagueFromTeamList(teams);
    }

    public static League buildPlausible() {
        ArrayList<Integer> teams = new ArrayList<Integer>();
        BiMap<Integer, String> ids = getTeamIDMap(TEAMS);
        for (Team t: TEAMS) {
            teams.add(ids.inverse().get(t.getname()));
        }
        double[][] distances = COST_MATRIX;
        Random rand = new Random();

        ArrayList<ArrayList<Integer>> divs = new ArrayList<ArrayList<Integer>>(6);
        ArrayList<Integer> teampile = teams;
        Collections.shuffle(teampile);
        ArrayList<Integer> finalleague = new ArrayList<Integer>();

        for(int i=0; i<6; i++) {
            Integer choose = teampile.remove(rand.nextInt(teampile.size()));
            int current = choose;

            divs.add(new ArrayList<Integer>());
            divs.get(divs.size()-1).add(choose);
        }

        while(!teampile.isEmpty()) {
            int next = teampile.remove(0);
            int mindiv=-1;
            double mindist = Double.MAX_VALUE;
            double[] distfromdiv = new double[6];
            for(int i=0; i<distfromdiv.length; i++) {
                if(divs.get(i).size()>=5) {
                    distfromdiv[i] = Double.MAX_VALUE;
                }
                else {
                distfromdiv[i]=TEAMS.get(next).distCompute(TEAMS.get(divs.get(i).get(0)));
                }
            }
            for(int j=0; j<distfromdiv.length; j++) {
                if(distfromdiv[j]<mindist) {
                    mindist = distfromdiv[j];
                    mindiv = j;
                }
            }

            divs.get(mindiv).add(next);
        }
        for(ArrayList<Integer> div:divs) {
            for(int team: div) {
                finalleague.add(team);
            }
        }
        return new League(finalleague);
    }

    public static int[] listToIntArray(List<Integer> l) {
        int[] result = new int[l.size()];
        for(int i=0; i<l.size(); i++) {
            result[i] = l.get(i).intValue();
        }
        return result;
    }


    //need to avoid duplicating code b/w here and League class
    public static double calculateCost(List<Integer> teams) {
        double samedivision = 0;
        double sameconference = 0;
        double otherconference = 0;
        double[][] costs = Utilities.COST_MATRIX;

        List<List<Integer>> sublists = Lists.partition(teams, 5);
        int[][] divisions = new int[6][];
        for(int i=0; i<6; i++) {
            divisions[i] = Utilities.listToIntArray(sublists.get(i));
        }

        for(int row1=0; row1<6; row1++) {
            for(int col1=0; col1<5; col1++) {
                for(int row2=0; row2<6; row2++) {
                    for(int col2=0; col2<5; col2++) {
                        if(row1==row2) {
                            samedivision += costs[divisions[row1][col1]][divisions[row2][col2]];
                        }
                        else if(row1/3==row2/3) {
                            sameconference += costs[divisions[row1][col1]][divisions[row2][col2]];
                        }
                        else otherconference += costs[divisions[row1][col1]][divisions[row2][col2]];
                    }
                }
            }
        }

        double cost = (6*samedivision+4*sameconference+1.2*otherconference)/2;
        return cost;

    }


}
