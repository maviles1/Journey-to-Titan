package titan.landing;

import titan.flight.Vector3d;
import titan.interfaces.Controller;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class FeedbackController extends Controller {

    private double angle_of_rotation = 90;
    private double strength = 1;

    private double max_angular_velocity = 0.05;
    private double max_velocity = 100.0;

    private static boolean flag = true;

    private static String process = "rotating";

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

        double mainThrust = 0, angularAcceleration = 0; //TODO: potential base thrust off of altitude or if we are doing course correction

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        if (flag) {
            targetAngle = getAngle(state, windRate);
            flag = false;
        }

        System.out.println("state.getAngle()    " + state.getAngle());
        System.out.println("targetAngle         " + targetAngle);
        System.out.println("angleTolerance      " + angleTolerance);
        System.out.println("thingy              " + Math.abs(state.getAngle() % (2 * Math.PI) - targetAngle));

        switch (process) {
            case "rotating": {
                if (Math.abs(state.getAngle() % (2 * Math.PI) - targetAngle) < angleTolerance) {
                    System.out.println("Rotating. Reached target angle");

                    angularAcceleration = stabilize(state, angleTolerance);
                    process = "stabilizing";
                    flag = true;
                } else if (Math.abs(state.getAngularVelocity()) <= max_angular_velocity) {
                    System.out.println("Rotating");

                    targetAngle = getAngle(state, windRate);
                    angularAcceleration = startRotation(state, Math.toRadians(targetAngle));
                }
            } break;
            case "stabilizing": {
                if (isStable(state, angleTolerance)) {
                    System.out.println("Stabilizing. Stable");
                    angle_of_rotation *= -1;

                    mainThrust = getStrength(state, windRate);
                    process = "thrusting";
                } else if (Math.abs(state.getAngularVelocity()) <= max_angular_velocity) {
                    System.out.println("Stabilizing");

                    angularAcceleration = stabilize(state, angleTolerance);
                }
            } break;
            case "thrusting": {
                System.out.println("Thrusting");

                if (state.getVelocity().norm() > max_velocity) {
                    mainThrust = getStrength(state, windRate);
                }
                process = "rotating";
            }
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        //if (state.getPosition().getX() - LandingSimulation.getLandingPosition().getX() > 10) {
//            System.out.println("LM is too far from LP");
//
//            double wind_direction = Math.signum(windRate.getVelocityRate().getX()); // -1 if left, 1 if right
//            double landing_point_direction = Math.signum(state.getPosition().getX() - LandingSimulation.getLandingPosition().getX()); // -1 if left, 1 if right
//
//            double wind_acc = windRate.getVelocityRate().norm(); // strength of acceleration
//            double acc = desiredAcceleration(state) - wind_acc;
//            double direction;
//            if (wind_direction == landing_point_direction) {
//                direction = -1 * wind_direction;
//            } else {
//                direction = wind_direction;
//                acc *= 0.5;
//            }
//
//            strength = acc;
//            targetAngle = angle_of_rotation * direction;
//            System.out.println(targetAngle);
        //} //else {
//            System.out.println("LM is above LP");
//            Vector3d w = windRate.getVelocityRate();
//            Vector3d g = TitanGravityODE.getGravitationalPullingForce().mul(1/8000.0);
//
//            strength = w.add(g).norm() * 0.5;
//            targetAngle = angle(w.add(g).mul(-1), new Vector3d(0, 1, 0));
//        }

        //////////////////////////////////////////////////////////////////////////////////////////////////

//        if (Math.abs(state.getAngularVelocity()) <= max_angular_velocity && !flag) {
//            targetAngle = angle_of_rotation;
//            angularAcceleration = startRotation(state, Math.toRadians(targetAngle));
//            //System.out.println(targetAngle);
//        }
//
//        //System.out.println("timestep: " + state.getTime());
//        //System.out.println("difference: " + (state.getAngle() % (2 * Math.PI) - targetAngle));
//
//        System.out.println("state.getAngle()    " + state.getAngle());
//        System.out.println("targetAngle         " + targetAngle);
//        System.out.println("angleTolerance      " + angleTolerance);
//        System.out.println("thingy              " + Math.abs(state.getAngle() % (2 * Math.PI) - targetAngle));
//
//        if (Math.abs(state.getAngle() % (2 * Math.PI) - targetAngle) < angleTolerance || flag) {
//            System.out.println("Reached target angle: " + targetAngle);
//            flag = true;
//
//            if (Math.abs(state.getAngularVelocity()) <= max_angular_velocity  && flag) {
//                //now we need to counter torque
//                angularAcceleration = stabilize(state, angleTolerance);
//            }
//
//            if (isStable(state, angleTolerance)) {
//                System.out.println("REACHED TARGET ANGLE");
//                flag = false;
//                ////////////////////////////////////////////////////////////////////////////////////////
//                if (targetAngle == 0) {
//                    System.out.println("UPRIGHT");
//                    //the lander is upright and stable.
//                    //So now we can either use our main thrusters or initiate another rotation
//
//                    //mainThrust = strength;
//
//                    if (Math.abs(state.getAngularVelocity()) <= max_angular_velocity ) {
//                        targetAngle = -angle_of_rotation;
//                        angularAcceleration = startRotation(state, Math.toRadians(targetAngle));
//                        //System.out.println(targetAngle);
//                    }
//
////                    if (state.getTime() < 216 + 60) { //stable at timestep 216
////                        mainThrust = useMainThruster(state, strength, 60);
////                    } else if (state.getTime() == 216 + 60) {
////                        angularAcceleration = startRotation(state, Math.toRadians(45));
////                    }
//
////                    if (state.getTime() > 276 && state.getTime() < 591 + 100) { //stable at timestep 591
////                        mainThrust = useMainThruster(state, strength, 100);
////                    } else {
////                        //do nothing
////                    }
//                } else {
//                    System.out.println("REACHED TARGET ANGLE");
//                    //the lander is at its target angle and is stable
//                    //now we can use main thrusters for trajectory correction
//                    //or put lander back into upright position
//
//                    //mainThrust = strength;
//
////                    targetAngle = 0;
////                    angularAcceleration = 2 * 5 * startRotation(state, Math.toRadians(targetAngle));
//
////                    if (state.getTime() < 71 + 75) { //stable at timestep 71
////                        mainThrust = useMainThruster(state, strength, 75);
////                    } else if (state.getTime() == 146) { //THIS KINDA DEPENDS ON THE TIME STEP
////                        angularAcceleration = startRotation(state, 0);
////                    }
//
////                    if (state.getTime() > 146 && state.getTime() < 461 + 60) { //stable at 461
////                        mainThrust = useMainThruster(state, strength, 60);
////                    } else if (state.getTime() == 461 + 60) {
////                        angularAcceleration = startRotation(state, 0);
////                    }
//                }
//                //////////////////////////////////////////////////////////////////////////////////////
//            }
//        }

//        System.out.println(Math.sin(state.getAngle()) + " ' " + Math.cos(state.getAngle()));
//        System.out.println("mainThrust              " + mainThrust);

        mainThrust = 0;//---------------------------------------------------------------------------------

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

    public double getAngle(LandingState state, LandingRate windRate){
//        Vector3d w = windRate.getVelocityRate();
//        Vector3d g = TitanGravityODE.getGravitationalPullingForce();
//
//        //return angle(w.add(g).mul(-1), new Vector3d(0, 1, 0));
//        return angle(new Vector3d(w.add(g).getX(), Math.abs(w.add(g).getY()), w.add(g).getZ()), new Vector3d(0, 1, 0));

        double wind_direction = Math.signum(windRate.getVelocityRate().getX()); // -1 if left, 1 if right
        double landing_point_direction = Math.signum(state.getPosition().getX() - LandingSimulation.getLandingPosition().getX()); // -1 if left, 1 if right

        double direction;
        if (wind_direction == landing_point_direction) {
            direction = -1 * wind_direction;
        } else {
            direction = wind_direction;
        }

        return angle_of_rotation * direction;

    }

    public double getStrength(LandingState state, LandingRate windRate){
        Vector3d w = windRate.getVelocityRate();
        Vector3d g = TitanGravityODE.getGravitationalPullingForce();

        return w.add(g).norm();
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

    public double angle(Vector3d a, Vector3d b){
        double sum = a.getX()*b.getX() + a.getY()*b.getY() + a.getZ()*b.getZ();
        return  Math.acos(sum / (a.norm() * b.norm()));
    }
}
