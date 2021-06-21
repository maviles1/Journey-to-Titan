package titan.landing;

import titan.flight.Vector3d;
import titan.interfaces.Controller;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class FeedbackController extends Controller {

    private double angle_of_rotation = 90;
    private double strength = 10000;

    private static boolean flag = true;

    /**
     * Constructor for the feedback controller
     */
    public FeedbackController(){}
    public FeedbackController(double targetAngle, double thrustUntil, boolean isSetThrustUntil) {
        this.targetAngle = targetAngle;
        this.thrustUntil = thrustUntil;
        this.isSetThrustUntil = isSetThrustUntil;
    }

    @Override
    public RateInterface thrust(RateInterface wR, StateInterface y) {
        //return predetermined thrust
        LandingState state = (LandingState) y;
        LandingRate windRate = (LandingRate) wR;

        //where u is the thrust
        //horizontal acceleration = u * sin(theta)
        //vertical acceleration = u * cos(theta) (-g)
        //angular acceleration = torque

        double mainThrust = 0; //TODO: potential base thrust off of altitude or if we are doing course correction

        double angularAcceleration = 0;

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        if (state.getPosition().getX() - LandingSimulation.getLandingPosition().getX() > 10) {
            System.out.println("LM is too far from LP");

            double wind_direction = Math.signum(windRate.getVelocityRate().getX()); // -1 if left, 1 if right
            double landing_point_direction = Math.signum(state.getPosition().getX() - LandingSimulation.getLandingPosition().getX()); // -1 if left, 1 if right

            if (wind_direction == 0){
                throw new RuntimeException();
            }

            double wind_acc = windRate.getVelocityRate().norm(); // strength of acceleration
            double acc = desiredAcceleration(state) - wind_acc;
            double direction;
            if (wind_direction == landing_point_direction) {
                direction = -1 * wind_direction;
            } else {
                direction = wind_direction;
                acc *= 0.5;
            }

            strength = acc;
            targetAngle = angle_of_rotation * direction;
            System.out.println(targetAngle);
        } else {
            System.out.println("LM is above LP");
            Vector3d w = windRate.getVelocityRate();
            Vector3d g = TitanGravityODE.getGravitationalPullingForce().mul(1/8000.0);

            strength = w.add(g).norm() * 0.5;
            targetAngle = angle(w.add(g).mul(-1), new Vector3d(0, 1, 0));
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////////

//        if (flag) {
            angularAcceleration = startRotation(state, Math.toRadians(targetAngle), strength);
            //System.out.println(targetAngle);
//            flag = false;
//        }

        //System.out.println("timestep: " + state.getTime());
        //System.out.println("difference: " + (state.getAngle() % (2 * Math.PI) - targetAngle));

        System.out.println("state.getAngle()    " + state.getAngle());
        System.out.println("targetAngle         " + targetAngle);
        System.out.println("angleTolerance      " + angleTolerance);
        System.out.println("thingy              " + Math.abs(state.getAngle() % (2 * Math.PI) - targetAngle));

        if (Math.abs(state.getAngle() % (2 * Math.PI) - targetAngle) < angleTolerance) {
            System.out.println("Reached target angle: " + targetAngle);
            //now we need to counter torque
            angularAcceleration = stabilize(state, angleTolerance, strength);
            //alternatively
            //state.setAngularVelocity(0.0);

            if (isStable(state, angleTolerance)) {
                flag = true;
                ////////////////////////////////////////////////////////////////////////////////////////
                if (targetAngle == 0) {
                    System.out.println("UPRIGHT");
                    //the lander is upright and stable.
                    //So now we can either use our main thrusters or initiate another rotation

                    mainThrust = strength;

//                    if (state.getTime() < 216 + 60) { //stable at timestep 216
//                        mainThrust = useMainThruster(state, strength, 60);
//                    } else if (state.getTime() == 216 + 60) {
//                        angularAcceleration = startRotation(state, Math.toRadians(45));
//                    }

//                    if (state.getTime() > 276 && state.getTime() < 591 + 100) { //stable at timestep 591
//                        mainThrust = useMainThruster(state, strength, 100);
//                    } else {
//                        //do nothing
//                    }
                } else {
                    System.out.println("REACHED TARGET ANGLE");
                    //the lander is at its target angle and is stable
                    //now we can use main thrusters for trajectory correction
                    //or put lander back into upright position

                    mainThrust = strength;

//                    if (state.getTime() < 71 + 75) { //stable at timestep 71
//                        mainThrust = useMainThruster(state, strength, 75);
//                    } else if (state.getTime() == 146) { //THIS KINDA DEPENDS ON THE TIME STEP
//                        angularAcceleration = startRotation(state, 0);
//                    }

//                    if (state.getTime() > 146 && state.getTime() < 461 + 60) { //stable at 461
//                        mainThrust = useMainThruster(state, strength, 60);
//                    } else if (state.getTime() == 461 + 60) {
//                        angularAcceleration = startRotation(state, 0);
//                    }
                }
                //////////////////////////////////////////////////////////////////////////////////////
            }
        }

//        System.out.println(Math.sin(state.getAngle()) + " ' " + Math.cos(state.getAngle()));
        System.out.println("mainThrust          " + mainThrust);
        double xAccel = mainThrust * Math.sin(state.getAngle());
        double yAccel = mainThrust * Math.cos(state.getAngle());
//        System.out.println(xAccel + " ' " + yAccel);

        Vector3d thrust = new Vector3d(xAccel, yAccel, 0);

//        System.out.println("angular accel: " +  angularAcceleration);
//        System.out.println("angular vel: " +  state.getAngularVelocity());
//        System.out.println("angle: " +  state.getAngle());
        return new LandingRate(state.getVelocity().add(windRate.getPositionRate()), thrust, state.getShuttle_direction(), state.getWind_direction(), state.getPrevWindVector(), angularAcceleration);
    }

    @Override
    public Controller clone() {
        return new FeedbackController(targetAngle, thrustUntil, isSetThrustUntil);
    }

    public double desiredAcceleration(LandingState state){
        Vector3d dir = dist(state);
        double desired_velocity = dir.getX() / dir.getY();
        double desired_acceleration = desired_velocity - state.getVelocity().getX();

        return desired_acceleration;
    }

    public Vector3d dist(LandingState state){
        Vector3d pos = state.getPosition();
        Vector3d ll = LandingSimulation.getLandingPosition();

        return ll.sub(pos);
    }

    public boolean rotationNeeded(LandingState state){
        return !(targetAngle - state.getPosition().getY() / state.getPosition().getX() <= angleTolerance);
    }

    public double angle(Vector3d a, Vector3d b){
        double sum = a.getX()*b.getX() + a.getY()*b.getY() + a.getZ()*b.getZ();
        return  Math.acos(sum / (a.norm() * b.norm()));
    }

//    /**
//     * Method to find the adjustment for direction so it remains opposite towards Titan
//     * @return the vector that represents the geometric difference between desired direction and the actual one
//     */
//    public Vector3d directionAdjustment(){
//        Vector3d direction = landing_module.getDirection(); // direction of landing module
//        Vector3d desired_direction = landing_module.getPosition(); // direction of the vector that goes from the centre of the Titan to the position of the landing module
//        Vector3d adjustment = desired_direction.sub(direction);
//        return adjustment.copy();
//    }
//
//    /**
//     * Method to produce the vector that starts at landing module's position and ends at landing location
//     * @return the vector that represents the distance between landing module and landing location
//     */
//    public Vector3d dist(){
//        Vector3d pos = current_state.getPosition();
//        Vector3d ll = LandingSimulation.getLandingPosition();
//
//        return pos.sub(ll);
//    }
//
//    /**
//     * Method to produce thrust with the main thruster towards the landing location
//     * @return the vector that represents an acceleration applied to the landing module - its main thruster's thrust
//     */
//    public Vector3d thruster(){
//        Vector3d dir = dist();
//        double desired_velocity = dir.getX() / dir.getY();
//        double desired_acceleration = desired_velocity - current_state.getVelocity().norm();
//
//        Vector3d thrust = current_state.getShuttle_direction().normalize().mul(desired_acceleration);
//
//        return thrust;
//    }
}
