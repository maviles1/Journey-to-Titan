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

        double mainThrust = 0; //TODO: potential base thrust off of altitute or if we are doing course correction
        double angularAcceleration = 0;

        //This is just to give it initial angular rotation in the first state
        if (state.getTime() == 0) {
            angularAcceleration = startRotation(state, Math.toRadians(-24.503));
        }

        //System.out.println("difference: " + (state.getAngle() % (2 * Math.PI) - targetAngle));
        if (Math.abs(state.getAngle() % (2 * Math.PI) - targetAngle) < angleTolerance) {
            System.out.println("timestep: " + state.getTime());
            System.out.println("Reached target angle: " + targetAngle);
            //now we need to counter torque
            angularAcceleration = stabilize(state, angleTolerance);

            if (isStable(state, angleTolerance) && state.getTime() > 0) {
                if (targetAngle == 0) {
                    System.out.println("UPRIGHT");
                    //the lander is upright and stable.
                    //So now we can either use our main thrusters or initiate another rotation
                    state.setAngle(0); //hacky, i know, but its gotta be

                    double t0 = 380 + 250;//259 is when its upright, 380 is when it stops rising
                    double duration = 407;

                    if (state.getVelocity().getY() > 0)
                        System.out.println("RISING");

                    if (state.getTime() > t0 && state.getTime() < t0 + duration) { //stable at timestep 216
                        mainThrust = useMainThruster(state, 2.192157, duration);
                    } else if (state.getTime() == t0 + duration) {
                        //angularAcceleration = startRotation(state, Math.toRadians(45));
                    }

                } else {
                    System.out.println("REACHED TARGET ANGLE");
                    //the lander is at its target angle and is stable
                    //now we can use main thrusters for trajectory correction
                    //or put lander back into upright position
                    state.setAngle(targetAngle);

                    //old values: duration: 145, strength: 4.907
                    double duration = 182;
                    double t0 = 76;

                    if (state.getTime() < t0 + duration) { //stable at timestep 142
                        mainThrust = useMainThruster(state, 3.907, duration);

                    } else if (state.getTime() == t0 + duration) { //THIS KINDA DEPENDS ON THE TIME STEP
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
