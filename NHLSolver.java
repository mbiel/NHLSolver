package nhlsolver;

import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: Mike
 * Date: 10/23/11
 * Time: 11:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class NHLSolver {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.println("Select a method: \n1 - Hill Climbing\n2 - Simulated " +
                "Annealing\n3 - Genetic Search");
        while(scan.hasNextInt()) {
            int choice = scan.nextInt();
            if (choice<1 || choice>3) System.out.println("Try again (enter 1,2, or 3)");

            else {
                switch(choice) {
                    case 1: {
                        Search hillclimb = new HillClimbingSearch();
                        double minhillcost = Double.MAX_VALUE;
                        League minhillleague = null;
                        for(int i=0; i<200; i++) {
                            if(i%100==0) System.out.println(i);
                            League next = hillclimb.search(Utilities.buildPlausible());
                            if(next.getCost()<minhillcost) {
                                minhillcost = next.getCost();
                                minhillleague = next;
                            }
                        }

                        System.out.println("Hill Climbing with Random Restarts found: \n"
                                +minhillleague.toString());
                        break;
                    }

                    case 2: {
                        League sabegin = Utilities.buildPlausible();
                        Search simanneal = new SimulatedAnnealingSearch();
                        for(int i=0; i<20; i++) {
                            League next = simanneal.search(sabegin);
                            System.out.println("Iteration "+(i+1)+" SimulatedAnnealing found \n"
                                    +next.toString());
                            sabegin = next;
                        }
                        break;
                    }
                    case 3: {
                        Search gene = new GeneticSearch();
                        League result = gene.search(Utilities.buildPlausible());
                        System.out.println("Genetic Search found \n");
                        System.out.println(result.toString());
                        break;
                    }


                }
            }
        }
    }


}
