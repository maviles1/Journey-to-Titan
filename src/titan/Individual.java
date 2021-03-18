package titan;

public class Individual {
    Vector3d chromosome;
    double fitness;

    public Individual(Vector3d chromosome){
        this.chromosome = chromosome.copy();
        fitness = 0;
    }

    public Vector3d getChromosome(){
        return chromosome.copy();
    }

    public void setChromosome(Vector3d chromosome){
        this.chromosome = chromosome.copy();
    }

    public double getFitness(){
        return fitness;
    }

    public void setFitness(double fitness){
        this.fitness = fitness;
    }

    public Individual clone(){
        return new Individual(chromosome.copy());
    }
}