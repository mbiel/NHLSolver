package nhlsolver;

/**
 * Created by IntelliJ IDEA.
 * User: Mike
 * Date: 10/26/11
 * Time: 6:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class HillClimbingSearch implements Search {

    public League search(League start) {
        League result = new League(start.teams);
        result.computeCost();
        while(true) {
            League next = result.bestSuccessor();
            next.computeCost();
            if(next.compareTo(result) < 0) result = next;
            else break;
        }
        return result;
    }

    }
