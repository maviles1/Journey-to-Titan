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

        LandingRate rate = new LandingRate(state.getVelocity(), new Vector3d(thrustX,0,0), state.getShuttle_direction(), state.getWind_direction());

        return rate;
    }
}
