package titan.genetic;

import titan.*;
import titan.StateInterface;

import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.ArrayList;

public class GeneticAlgorithm {

    Vector3d start;
    State startingState;
    ODEFunction func = new ODEFunction();
    ArrayList<Individual> population;
    double mutationRate = 0.1;
    int popSize = 100;
    State finalState;
    public static final double MAX_VELOCITY = 60000;

    public GeneticAlgorithm(Vector3d startingVector, State startingState){
        Random rand = new Random();
        this.start = startingVector;
        this.startingState = startingState;
        population =  new ArrayList<>();
        for (int i = 0; i < popSize; i++){
            Vector3d newVec = new Vector3d(0,0,0);
            newVec.setX(rand.nextDouble()*MAX_VELOCITY);
            newVec.setY(rand.nextDouble()*Math.sqrt(MAX_VELOCITY*MAX_VELOCITY - newVec.getX()*newVec.getX()));
            newVec.setZ(Math.sqrt(MAX_VELOCITY*MAX_VELOCITY - newVec.getX()*newVec.getX() - newVec.getY()*newVec.getY()));
            population.add(new Individual(newVec));
        }
    }

    public double calculateFitness(Individual ind){
        Solver solver = new Solver();
        State state2 = startingState.copy();
        state2.getVelocities()[state2.getVelocities().length - 1] = ind.getChromosome();
        StateInterface[] states = solver.solve(func, state2, 31556926, 1000);
        finalState = (State) states[states.length - 1];
        return 1 / (finalState.getPosition()[finalState.getPosition().length - 1].dist(finalState.getPosition()[8]));
    }

    public Vector3d [] crossover(Vector3d first, Vector3d second){
        Vector3d[] children = new Vector3d[2];

        children[0] = first.addMul(0.5, first.addMul(0.5, second));
        children[1] = second.addMul(0.5, second.addMul(0.5, first));

        return children;
    }

    public Individual [] topIndividuals(){
        Individual [] top = new Individual[10];
        sort();
        for (int i = 0; i < top.length; i++){
            top[i] = population.get(i);
        }
        return top;
    }


    public void populate(){
        ArrayList<Individual> new_population = new ArrayList<>();
        for (int i = 0; i < population.size(); i+=2){
            Vector3d[] new_individuals = crossover(population.get(i).getChromosome(),(population.get(i+1).getChromosome()));
            new_population.add(new Individual(new_individuals[0]));
            new_population.add(new Individual(new_individuals[1]));
        }
        population = new_population;
        for (int i = 0; i < population.size(); i++){
            population.get(i).setFitness(calculateFitness(population.get(i)));
        }
    }

//    public Individual[]  breedTopIndividuals(Individual [] individuals){
//        Individual[] children = new Individual[individuals.length * individuals.length];
//        int index = 0;
//        for (int i = 0; i < individuals.length; i++){
//            for (int j = 0; j < individuals.length; j++){
//                if (i != j){
//                    Vector3d [] vectors = new Vector3d[];
//                    vectors = crossover(individuals[i].getChromosome(), individuals[j].getChromosome() );
//                    children[index] = new Individual(vectors[0]);
//                    index++;
//                    children[index] = new Individual(vectors[1]);
//                    index++;
//                }
//            }
//        }
//    }

//    public void mutate(){
//
//    }

    private void sort(){
        Comparator<Individual> com = new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                return (int) (o2.getFitness() - o1.getFitness());
            }
        };
        Collections.sort(population, com);
    }

    public Individual findMin(){
        Individual min = population.get(0);
        for (int i = 0; i < population.size(); i++){
            if (calculateFitness(min) < calculateFitness(population.get(i))){
                min = population.get(i);
            }
        }
        return min;
    }

    public Vector3d calculateTrajectory(){
//        while (population.get(0).getFitness() != 100){
            populate();
//            System.out.println(population.get(0).getFitness());
//        }

        System.out.println(population.get(0).getFitness());
        return population.get(0).getChromosome();
    }



}
