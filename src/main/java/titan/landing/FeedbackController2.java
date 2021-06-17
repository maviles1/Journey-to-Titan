package titan.landing;

import titan.flight.Rate;
import titan.flight.State;
import titan.flight.Vector3d;
import titan.interfaces.Controller;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class FeedbackController2 implements Controller {

    @Override
    public RateInterface thrust(RateInterface windRate, StateInterface y) {
        LandingState state = (LandingState) y;

        //calculate thrust based on wind
        double windX = ((LandingRate) windRate).getVelocityRate().getX();
        double thrustX = -windX * 0.8;

        //where u is the thrust
        //horizontal acceleration = u * sin(theta)
        //vertical acceleration = u * cos(theta) (-g)
        //angular acceleration = torque

        //double angle: TODO: not really sure where theta comes from. Maybe we define it here instead of using states
        double mainThrust = 1;
        double xAccel = mainThrust * Math.sin(state.getAngle());
        double yAccel = mainThrust * Math.cos(state.getAngle());
        double angularAccel = 0; //torque TODO: calculate torque

        LandingRate rate = new LandingRate(state.getVelocity(), new Vector3d(thrustX,0,0), state.getShuttle_direction(), state.getWind_direction(), state.getPrevWindVector(), angularAccel);

        return rate;
    }
}
