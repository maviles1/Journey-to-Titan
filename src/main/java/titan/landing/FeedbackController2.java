package titan.landing;

import titan.flight.Rate;
import titan.flight.State;
import titan.flight.Vector3d;
import titan.interfaces.Controller;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class FeedbackController2 extends Controller {

    final double MASS = 6000;
    final double RADIUS = 3;
    final double angleTolerance = 0.01;
    final double tolerance = 0.1;

    double targetAngle;
    double thrustUntil; //TODO: find a formula to calculate this
    boolean isSetThrustUntil;

    public FeedbackController2() {}
    public FeedbackController2(double targetAngle, double thrustUntil, boolean isSetThrustUntil) {
        this.targetAngle = targetAngle;
        this.thrustUntil = thrustUntil;
        this.isSetThrustUntil = isSetThrustUntil;

    }

    private LandingState current_state; // current state of the environment
    private Shuttle landing_module; // object which holds characteristics of the landing module
    private Vector3d desired_velocity;

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
    public FeedbackController2(LandingState start_state, Shuttle landing_module){
        current_state = start_state;
        this.landing_module = landing_module;
    }

    /**
     * Method to find the adjustment for direction so it remains opposite towards Titan
     * @return the vector that represents the geometric difference between desired direction and the actual one
     */
    public Vector3d directionAdjustment(){
        Vector3d direction = landing_module.getDirection(); // direction of landing module
        Vector3d desired_direction = landing_module.getPosition(); // direction of the vector that goes from the centre of the Titan to the position of the landing module
        Vector3d adjustment = desired_direction.sub(direction);
        return adjustment.copy();
    }

    /**
     * Method to produce the vector that starts at landing module's position and ends at landing location
     * @return the vector that represents the distance between landing module and landing location
     */
    public Vector3d dist(){
        Vector3d pos = current_state.getPosition();
        Vector3d ll = LandingSimulation.getLandingPosition();

        return pos.sub(ll);
    }

    /**
     * Method to produce thrust with the main thruster towards the landing location
     * @return the vector that represents an acceleration applied to the landing module - its main thruster's thrust
     */
    public Vector3d thruster(){
        Vector3d dir = dist();
        double desired_velocity = dir.getX() / dir.getY();
        double desired_acceleration = desired_velocity - current_state.getVelocity().norm();

        Vector3d thrust = current_state.getShuttle_direction().normalize().mul(desired_acceleration);

        return thrust;
    }

    @Override
    public RateInterface thrust(RateInterface windRate, StateInterface y) {
        //return predetermined thrust
        LandingState state = (LandingState) y;
        double altitude = state.getPosition().getY();

        //where u is the thrust
        //horizontal acceleration = u * sin(theta)
        //vertical acceleration = u * cos(theta) (-g)
        //angular acceleration = torque

        double mainThrust = 0; //TODO: potential base thrust off of altitute or if we are doing course correction
        double angularAcceleration = 0;

        //This is just to give it initial angular rotation in the first state
        if (state.getTime() == 1) {
            angularAcceleration = startRotation(state, Math.toRadians(-45));
        }

        System.out.println("timestep: " + state.getTime());
        //System.out.println("difference: " + (state.getAngle() % (2 * Math.PI) - targetAngle));
        if (Math.abs(state.getAngle() % (2 * Math.PI) - targetAngle) < angleTolerance) {
            System.out.println("Reached target angle: " + targetAngle);
            //now we need to counter torque
            angularAcceleration = stabilize(state, angleTolerance);

            if (isStable(state, angleTolerance) && state.getTime() > 0) {
                if (targetAngle == 0) {
                    System.out.println("UPRIGHT");
                    //the lander is upright and stable.
                    //So now we can either use our main thrusters or initiate another rotation

                    if (state.getVelocity().getX() > 0 + tolerance) {
                        //moving right
                        //initiate rotation
                        angularAcceleration = startRotation(state, -90);
                    } else if (state.getVelocity().getX() < 0 - tolerance) {
                        //moving left
                        angularAcceleration = startRotation(state, 90);
                    } else {
                        //not moving horizontally
                        System.out.println("WHYYYYYYY");
                        mainThrust = useMainThruster(state, 2, 100);
                    }



                } else {
                    System.out.println("REACHED TARGET ANGLE");
                    //the lander is at its target angle and is stable
                    //now we can use main thrusters for trajectory correction
                    //or put lander back into upright position


                    if (state.getVelocity().getX() > 0 + tolerance) {
                        //moving right
                        //counter thrust
                        mainThrust = useMainThruster(state, 2, 500);
                    } else if (state.getVelocity().getX() < 0 - tolerance) {
                        //moving left
                        //counter thrust
                        mainThrust = useMainThruster(state, 2, 100);
                    } else {
                        //not moving horizontally
                        //go back upright
                        angularAcceleration = startRotation(state, 0);
                    }



                }
            }
        }

        double xAccel = mainThrust * Math.sin(state.getAngle());
        double yAccel = mainThrust * Math.cos(state.getAngle());

        Vector3d thrust = new Vector3d(xAccel, yAccel, 0);

//        System.out.println("angular accel: " +  angularAcceleration);
        //System.out.println("angular vel: " +  state.getAngularVelocity());
//        System.out.println("angle: " +  state.getAngle());
        return new LandingRate(state.getVelocity(), thrust, state.getShuttle_direction(), state.getWind_direction(), state.getPrevWindVector(), angularAcceleration);
    }

    public double startRotation(LandingState state, double targetAngle) {
        isSetThrustUntil = false;
        this.targetAngle = targetAngle;
        if (state.getAngle() - targetAngle > 0) {
            //we need to rotate to the left
            return angularAcceleration(rightTorque(100, state));
        } else if (state.getAngle() - targetAngle < 0) {
            return angularAcceleration(leftTorque(100, state));
        } else {
            return 0;
        }
    }

    public double useMainThruster(LandingState state, double strength, double duration) {
        if (!isSetThrustUntil) {
            //we only want to update the thrustUntil variable once per "session" so we can keep track of duration without overwriting
            thrustUntil = state.getTime() + duration;
            isSetThrustUntil = true;
        }

        if (state.getTime() < thrustUntil) {
            return strength;
        } else {
            return 0;
        }
    }

    public double stabilize(LandingState state, double tolerance) {
        if (state.getAngularVelocity() < 0 - tolerance) { //spinning counter-clockwise
            //need to apply leftTorque
            //System.out.println("applying left torque");
            return angularAcceleration(leftTorque(50, state));
        } else if (state.getAngularVelocity() > 0 + tolerance) {
            //System.out.println("applying right torque");
            return angularAcceleration(rightTorque(50, state));
        } else {
            //angularVelocity is within tolerance, but maybe not exactly 0;
            return -state.getAngularVelocity(); //cancel any residual angular velocity/acceleration
        }
    }

    public boolean isStable(LandingState state, double tolerance) {
        return Math.abs(state.getAngularVelocity()) < tolerance;
    }

    public double leftTorque(double strength, LandingState state) {
        Vector3d force = new Vector3d(strength, strength, 0);
        return torque(force, state.getAngle(), 45);
    }

    public double rightTorque(double strength, LandingState state) {
        Vector3d force = new Vector3d(-strength, strength, 0);
        return torque(force, state.getAngle(), -45);
    }

    public double torque(Vector3d force, double probeAngle, double thrustAngle) {
        force = rotate(probeAngle, force);
        double forceMagnitude = force.norm();
        return forceMagnitude * RADIUS * Math.sin(Math.toRadians(thrustAngle));
    }

    public double angularAcceleration(double torque) {
        double momentInertia = 0.5 * MASS * RADIUS * RADIUS;
        return torque/momentInertia;
    }

    public Vector3d rotate(double angle, Vector3d vector) {
        double newX = vector.getX() * Math.cos(angle) - vector.getY() * Math.sin(angle);
        double newY = vector.getX() * Math.sin(angle) + vector.getY() * Math.cos(angle);
        return new Vector3d(newX, newY, 0);
    }

    @Override
    public Controller clone() {
        return new FeedbackController2(targetAngle, thrustUntil, isSetThrustUntil);
    }

}
