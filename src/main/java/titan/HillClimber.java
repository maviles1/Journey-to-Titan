package titan;

import titan.interfaces.StateInterface;

public class HillClimber {

    StateInterface [] states;

    public HillClimber(StateInterface[]states){
        this.states = states;
    }

    public Vector3d climbHill(){
        Vector3d entryPoint = new Vector3d(3000,3000,3000);
        int thrustStep = find(entryPoint);
        Vector3d probePos = states[thrustStep].getPositions()[11];
        Vector3d thrustVec = entryPoint.sub(probePos);
        return thrustVec;
    }

    /**
     * Method to find state where the probe is the closest to the specified position in the array of states
     * @param p - position to which the distance is to be calculated
     * @return the index of the state which has the probe's position closest to the specified position
     */
    public int find(Vector3d p){
        //casting
        StateInterface[] state = ((StateInterface[]) states).clone();
        Vector3d position = ((Vector3d) p).copy();

        //assuming first state is the target (in that state probe is the closest to the specified position)
        Vector3d closest = state[0].getPositions()[11].copy();
        StateInterface target = state[0].copy();
        int index = 0;

        //looping through the rest of the array searching for the right state
        for (int i = 2; i < state.length; i++){
            //if distance in current state is shorter than the distance in, previously assumed, "target" state...
            if (state[i].getPositions()[11].dist(position) < closest.dist(position)) {
                //...then reassume and reassign according values
                closest = state[i].getPositions()[11].copy();
                target = state[i].copy();
                index = i;
            }
        }
        return index;
    }



}
