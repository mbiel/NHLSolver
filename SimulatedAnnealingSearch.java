package nhlsolver;

import org.uncommons.maths.random.MersenneTwisterRNG;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Mike
 * Date: 11/10/11
 * Time: 7:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimulatedAnnealingSearch implements Search {
    public League search(League start) {
        League current = start;
        League best = start;
        double tempstart = 5000;
        double mintemp = .00001;
        double delta = .9995;
        int iterlimit = 50000;
        int iters = 0;
        Random rand = new MersenneTwisterRNG();

        double temp = tempstart;

        while(temp>mintemp && iters < iterlimit) {
            League candidate = current.probswap();
            if(candidate.cost<current.cost) {
                current = candidate;
            }
            else {
                double prob = rand.nextDouble();
                double threshold = Math.exp((current.cost-candidate.cost)/temp);
                if(prob<threshold) {
                    current = candidate;
                }
            }
            if(current.cost<best.cost) {
                best = current;
            }
            temp = temp*delta;
            iters++;
        }
        System.out.println("iters: "+iters+"\ntemp: "+temp);
        return best;
    }
}
