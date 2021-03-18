package titan;
import java.lang.reflect.Array;
import java.util.Random;
import java.util.ArrayList;

public class GeneticAlgorithm {

    Vector3d start;
    State startingState;
    ODEFunction func = new ODEFunction();
    ArrayList<Vector3d> population;
    double rangeMin = -1000000;
    double rangeMax = 1000000;
    double mutationRate = 0.1;
    int popSize = 100;

    public GeneticAlgorithm(Vector3d startingVector, State startingState){
        Random rand = new Random();
        this.start = start;
        this.startingState = startingState;
        population =  new ArrayList<>();
        for (int i = 0; i < 100; i++){
            Vector3d newVec = new Vector3d(0,0,0);
            newVec.setX(rangeMin + (rangeMax - rangeMin) * rand.nextDouble());
            newVec.setY(rangeMin + (rangeMax - rangeMin) * rand.nextDouble());
            newVec.setZ(rangeMin + (rangeMax - rangeMin) * rand.nextDouble());
            population.add(newVec);
        }
    }

    public double calculateFitness(Vector3d vec){
        Solver solver = new Solver();
        State [] states = (State []) solver.solve(func, startingState, 31556926, 1000);
        State finalState = states[states.length - 1];
        return 1/(finalState.getPosition()[finalState.getPosition().length - 1].dist(finalState.getPosition()[8]));
    }

    public Vector3d crossover(Vector3d first, Vector3d second){
        return first.addMul(0.5, second);
    }

    public void populate(){
        for (int i = 0; i < population.size(); i++){
            for (int j = 0; j < population.size(); j++){
                 Vector3d child = crossover(population.get(i), population.get(j));
                 population.add(child);
            }
        }
    }

    public Vector3d calculateTrajectory(){
        while (calculateFitness(population.get(0)) != 100){
            populate();
        }
        return population.get(0);
    }



}
