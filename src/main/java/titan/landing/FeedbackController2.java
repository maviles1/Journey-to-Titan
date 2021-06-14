package titan.landing;

import titan.flight.Rate;
import titan.flight.State;
import titan.flight.Vector3d;
import titan.interfaces.Controller;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class FeedbackController2 implements Controller {

    @Override
    public RateInterface thrust(RateInterface windRate, StateInterface state) {
        //calculate thrust based on wind

        double windX = ((Rate)windRate).getVelRates()[0].getX();
        double thrustX = -windX * 0.8;

        Rate rate = new Rate(state.getVelocities(), new Vector3d[]{new Vector3d(thrustX,0,0)});

        return rate;
    }
}
