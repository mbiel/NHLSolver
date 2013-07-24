package nhlsolver;

import com.google.common.collect.BiMap;
import com.google.common.collect.Lists;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Mike
 * Date: 10/23/11
 * Time: 7:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class League implements Comparable<League> {
    protected ArrayList<Integer> teams;
    protected double cost;

    public League(ArrayList<Integer> list) {
        teams = list;
        cost = computeCost();
    }


    public double getCost() {
        return cost;
    }


    public double computeCost() {
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

        this.cost = (6*samedivision+4*sameconference+1.2*otherconference)/2;
        return this.cost;

    }

    //swap two teams by id
    public League swapteams(int a, int b) {
        League result = new League(this.teams);
        Collections.swap(result.teams, result.teams.indexOf(a), result.teams.indexOf(b));
        result.computeCost();
        return result;
    }

    //swap two teams by their list position
    public League swappositions(int a, int b) {
        League result = new League(this.teams);
        Collections.swap(result.teams, a, b);
        result.computeCost();
        return result;
    }

    public League swaprandom() {
        League result = new League(this.teams);
        Random rand = new Random();
        int a = rand.nextInt(30);
        int b = a;
        while(b==a) {b = rand.nextInt(30);}
        Collections.swap(result.teams, a, b);
        result.computeCost();
        return result;
    }

    public League bestSuccessor() {
        League result = new League(this.teams);
        for(int i=0; i<30; i++) {
            for(int j=0; j<30; j++) {
                League temp = new League(result.teams);
                temp.swapteams(i, j);
                if(temp.compareTo(result)<0) result = temp;
            }
        }
        return result;
    }


/*    swap two teams in the following way:
    1% chance of swapping 3 pairs of teams at random
    2% chance of swapping 2 pairs of teams at random
    52% chance of swapping a pair of teams at random
    44% chance of swapping a pair of teams at random, except guaranteed to be in same conference
    1% chance of swapping a whole division from one conference to another*/


    public League probswap() {
        League result = new League(this.teams);
        Random rand = new Random();
        double check = rand.nextDouble();
        if(check<.01) {
            result.swaprandom();
            result.swaprandom();
            result.swaprandom();
        }
        else if(check<.03) {
            result.swaprandom();
            result.swaprandom();
        }
        else if(check<.55) {
            result.swaprandom();
        }
        else if(check<.99) {
            int to = rand.nextInt(30);
            int from;
            if(to<15) {
                do {
                    from = rand.nextInt(15);
                } while(from/5 == to/5);
            }
            else {
                do {
                    from = rand.nextInt(15)+15;
                } while(from/5 == to/5);
            }
            result.swappositions(to, from);
        }
        else {
            int divfrom = rand.nextInt(6);
            int divto;
            do {
                divto = rand.nextInt(6);
            } while(divto/3 == divfrom/3);

            int startpos = 5*divfrom;
            int endpos = 5*divto;

            for(int i=0; i<5; i++) {
                result.swappositions(startpos + i, endpos + i);
            }
        }

        return result;

    }

    public League[] bestNSuccessors(int n) {
        League[] result = new League[n];
        for(int i=0; i<result.length; i++) {
            result[i] = new League(this.teams);
        }
        for(int i=0; i<30; i++) {
            for(int j=0; j<30; j++) {
                League temp = this.swapteams(i, j);
                if(temp.compareTo(result[n-1])<0) {
                    result[n-1] = temp;
                    Arrays.sort(result);
                }
            }
        }
        return result;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        League league = (League) o;

        if (Double.compare(league.cost, cost) != 0) return false;
        if (!teams.equals(league.teams)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = teams.hashCode();
        temp = cost != +0.0d ? Double.doubleToLongBits(cost) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public int compareTo(League o) {
        League other = o;
        return (Double.compare(cost, other.cost));
    }

    public String toString() {
        BiMap teammap = Utilities.getTeamIDMap(Utilities.TEAMS);
        StringBuilder b = new StringBuilder("Conference A: \n\tDivision 1: ");
        for(int i=0; i<5; i++) {
            b.append(teammap.get(teams.get(i))+"/");
        }
        b.append("\n\tDivision 2: ");
        for(int i=5;i<10; i++) {
            b.append(teammap.get(teams.get(i))+"/");
        }
        b.append("\n\tDivision 3: ");
        for(int i=10;i<15; i++) {
            b.append(teammap.get(teams.get(i))+"/");
        }
        b.append("\nConference B: \n\tDivision 4: ");
        for(int i=15;i<20; i++) {
            b.append(teammap.get(teams.get(i))+"/");
        }
        b.append("\n\tDivision 5: ");
        for(int i=20;i<25; i++) {
            b.append(teammap.get(teams.get(i))+"/");
        }
        b.append("\n\tDivision 6: ");
        for(int i=25;i<30; i++) {
            b.append(teammap.get(teams.get(i))+"/");
        }
        b.append("\nCost: "+cost);

        return b.toString();

    }
}
