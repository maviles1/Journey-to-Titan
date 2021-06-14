package titan.landing;

import titan.flight.Probe;
import titan.flight.State;
import titan.flight.Vector3d;

public class FeedbackController {
    private State current_state; // current state which holds the position and the velocity of the Titan
    private final Shuttle landing_module; // object which holds characteristics of the landing module

    // requirement is that the probe is in titan orbit
    // we check the distance between titans center and the probe and determine the direction of the vector
    // we then thrust in this direction based on the size of the distance and the probes velocity
    // related to titan ->  (to decrease the distance)
    // we repeat this step until we reach a point where ->
    // the probes distance from titan is titans radius + X (certain value)

    /**
     * Constructor for the feedback controller
     * @param start_state - the initial state consists of the position and the velocity of the Titan
     * @param landing_module - object which holds characteristics of the landing module
     */
    public FeedbackController(State start_state, Shuttle landing_module){
        current_state = start_state;
        this.landing_module = landing_module;
    }

    /**
     * Method to find the adjustment for direction so it remains opposite towards Titan
     * @return the vector that represents the geometric difference between desired direction and the actual one
     */
    public Vector3d directionAdjustment(){
        Vector3d direction = landing_module.getDirection(); // direction of landing module
        Vector3d desired_direction = landing_module.getPosition().sub(current_state.getPositions()[0]); // top is above, bottom is below
        Vector3d adjustment = desired_direction.sub(direction); //
        return adjustment.copy();
    }

    public Vector3d thruster(double dis, Vector3d probe_vel){
        return null;
    }
}
