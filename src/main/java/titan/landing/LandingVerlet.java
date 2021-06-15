package titan.landing;

import titan.flight.Vector3d;
import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.StateInterface;
import titan.interfaces.StepInterface;

public class LandingVerlet implements StepInterface  {

    @Override
    public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h) {
        LandingRate rate = (LandingRate) f.call(t, y);
        LandingState state  = (LandingState) y;

        //x(t + ∆t) = x(t) * ∆t + 1/2 * a(t) * ∆t^2
        Vector3d pos = state.getPosition().addMul(h, state.getVelocity().addMul(0.5 * h * h, rate.getVelocityRate()));

        LandingState nextState = new LandingState(pos, state.getVelocity(), state.getShuttle_direction(), state.getWind_direction(), t + h);
        LandingRate newRate = (LandingRate) f.call(t + h, nextState);
        Vector3d accel = newRate.getVelocityRate(); //a(t + ∆t)

        //v(t + ∆t) = v(t) + 1/2*(a(t) + a(t + ∆t))*∆t
        Vector3d vel = state.getVelocity().addMul(0.5 * h, rate.getVelocityRate().add(accel));

        return new LandingState(pos, vel, rate.getShuttle_directionRate(), rate.getWind_directionRate(), t + h);
    }
}
