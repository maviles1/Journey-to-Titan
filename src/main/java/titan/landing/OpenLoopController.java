package titan.landing;

import titan.flight.Vector3d;
import titan.interfaces.Controller;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class OpenLoopController extends Controller {

    public OpenLoopController() {}

    public OpenLoopController(double targetAngle, double thrustUntil, boolean isSetThrustUntil) {
        this.targetAngle = targetAngle;
        this.thrustUntil = thrustUntil;
        this.isSetThrustUntil = isSetThrustUntil;
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

        double mainThrust = 0; //TODO: potential base thrust off of altitude or if we are doing course correction
        double angularAcceleration = 0;

        //This is just to give it initial angular rotation in the first state
        if (state.getTime() == 1) {
            double enterAngle = Math.atan2(state.getVelocity().getY(), state.getVelocity().getX());
            System.out.println(enterAngle);
            angularAcceleration = startRotation(state, Math.toRadians(enterAngle));
        }
        //double enterAngle = Math.atan2(state.getVelocity().getX(), state.getVelocity().getY());

        //System.out.println("difference: " + (state.getAngle() % (2 * Math.PI) - targetAngle));
        if (Math.abs(state.getAngle() % (2 * Math.PI) - targetAngle) < angleTolerance) {
            System.out.println("timestep: " + state.getTime());
            System.out.println("Reached target angle: " + targetAngle);
            //now we need to counter torque
            angularAcceleration = stabilize(state, angleTolerance, 50);

            if (isStable(state, angleTolerance) && state.getTime() > 0) {
                if (targetAngle == 0) {
                    System.out.println("UPRIGHT");
                    //the lander is upright and stable.
                    //So now we can either use our main thrusters or initiate another rotation

                    if (state.getTime() < 216 + 60) { //stable at timestep 216
                        mainThrust = useMainThruster(state, 3, 60);
                    } else if (state.getTime() == 216 + 60) {
                        angularAcceleration = startRotation(state, Math.toRadians(45));
                    }

                    if (state.getTime() > 276 && state.getTime() < 591 + 100) { //stable at timestep 591
                        mainThrust = useMainThruster(state, 3, 100);
                    } else {
                        //do nothing
                    }
                } else {
                    System.out.println("REACHED TARGET ANGLE");
                    //the lander is at its target angle and is stable
                    //now we can use main thrusters for trajectory correction
                    //or put lander back into upright position

                    if (state.getTime() < 142 + 300) { //stable at timestep 142
                        mainThrust = useMainThruster(state, 3, 300);
                    } else if (state.getTime() == 142 + 300) { //THIS KINDA DEPENDS ON THE TIME STEP
                        angularAcceleration = startRotation(state, 0);
                    }

                    if (state.getTime() > 142 + 300 && state.getTime() < 461 + 60) { //stable at 461
                        mainThrust = useMainThruster(state, 3, 60);
                    } else if (state.getTime() == 461 + 60) {
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


    @Override
    public Controller clone() {
        return new OpenLoopController(targetAngle, thrustUntil, isSetThrustUntil);
    }

}
