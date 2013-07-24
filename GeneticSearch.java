package nhlsolver;


import org.uncommons.maths.random.DiscreteUniformGenerator;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.PoissonGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.factories.ListPermutationFactory;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.operators.ListOrderCrossover;
import org.uncommons.watchmaker.framework.operators.ListOrderMutation;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.Stagnation;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static nhlsolver.Utilities.calculateCost;

/**
 * Created by IntelliJ IDEA.
 * User: Mike
 * Date: 10/28/11
 * Time: 11:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeneticSearch implements Search {
    public League search(League startleague) {
        ArrayList<Integer> target = startleague.teams;
        ArrayList<Integer> result = (ArrayList<Integer>) evolveList(target);
        return new League(result);
    }

    public List<Integer> evolveList(List<Integer> target) {
        Random rand = new MersenneTwisterRNG();
        ListPermutationFactory<Integer> factory = new ListPermutationFactory(target);
        List<EvolutionaryOperator<List<Integer>>> operators = new ArrayList<EvolutionaryOperator<List<Integer>>>();
        operators.add(new ListOrderMutation(new PoissonGenerator(4, rand), new DiscreteUniformGenerator(1,29, rand)));
        operators.add(new ListOrderCrossover(new Probability(.04)));
        EvolutionaryOperator<List<Integer>> pipeline = new EvolutionPipeline<List<Integer>>(operators);
        EvolutionEngine<List<Integer>> engine =
                new GenerationalEvolutionEngine<List<Integer>>(factory,
                pipeline, new ListEvaluator(),
                new RouletteWheelSelection(), rand);

        return engine.evolve(5000,5, new Stagnation(200, false),
                new TargetFitness(20283.68266258679, false));
    }

    public class ListEvaluator implements FitnessEvaluator<List<Integer>> {
        public ListEvaluator() {

        }
        public double getFitness(List<Integer> candidate, List<? extends List<Integer>> population) {
            return calculateCost(candidate);
        }

        public boolean isNatural() {
            return false;
        }
    }
}
