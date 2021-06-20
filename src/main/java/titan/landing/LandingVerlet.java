package titan.landing;

import titan.flight.Vector3d;
import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.StateInterface;
import titan.interfaces.StepInterface;

public class LandingVerlet implements StepInterface  {

    @Override
    public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h) {
//        System.out.println("------");
        LandingRate rate = (LandingRate) f.call(t, y);
        LandingState state  = (LandingState) y;

        //x(t + ∆t) = x(t) * ∆t + 1/2 * a(t) * ∆t^2
        Vector3d pos = state.getPosition().addMul(h, state.getVelocity().addMul(0.5 * h * h, rate.getVelocityRate()));
        double angle = state.getAngle() + (state.getAngularVelocity() * h) + (0.5 * h * h * rate.getAngularAcceleration());

        LandingState nextState = new LandingState(pos, state.getVelocity(), state.getShuttle_direction(), state.getWind_direction(), state.getPrevWindVector(), angle, state.getAngularVelocity(), t + h);
        //LandingRate newRate = (LandingRate) f.call(t + h, nextState);

        PhysicsEngine engine = ((PhysicsEngine) f).clone();
        LandingRate newRate = (LandingRate) engine.call(t + h, nextState);

        Vector3d accel = newRate.getVelocityRate(); //a(t + ∆t)
        double angularAccel = newRate.getAngularAcceleration();

        //v(t + ∆t) = v(t) + 1/2*(a(t) + a(t + ∆t))*∆t
        Vector3d vel = state.getVelocity().addMul(0.5 * h, rate.getVelocityRate().add(accel));
        double angularVel = state.getAngularVelocity() + 0.5 * h * (rate.getAngularAcceleration() + angularAccel);

//        System.out.println("--");
//        System.out.println("angle: " + Math.toDegrees(angle));
//        System.out.println("angleVel: " + angularVel);
//        System.out.println("------");

        return new LandingState(pos, vel, rate.getShuttle_directionRate(), rate.getWind_directionRate(), state.getPrevWindVector(), angle, angularVel, t + h);
    }
}
