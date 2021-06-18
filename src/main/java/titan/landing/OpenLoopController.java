package titan.landing;

import titan.flight.Vector3d;
import titan.interfaces.Controller;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class OpenLoopController implements Controller {

    final double MASS = 6000;
    final double RADIUS = 3;

    @Override
    public RateInterface thrust(RateInterface windRate, StateInterface y) {
        //return predetermined thrust
        LandingState state = (LandingState) y;
        double altitude = state.getPosition().getY();

        //where u is the thrust
        //horizontal acceleration = u * sin(theta)
        //vertical acceleration = u * cos(theta) (-g)
        //angular acceleration = torque

        //double angle: TODO: not really sure where theta comes from. Maybe we define it here instead of using states
        double mainThrust = 1;
        double xAccel = mainThrust * Math.sin(state.getAngle());
        double yAccel = mainThrust * Math.cos(state.getAngle());
        double angularAccel = angularAcceleration(torque(new Vector3d(100,100,0), state));
        Vector3d thrust = new Vector3d(xAccel, yAccel, 0);
        //

        System.out.println("angular accel: " +  angularAccel);
        System.out.println("angular vel: " +  state.getAngularVelocity());
        System.out.println("angle: " +  state.getAngle());
        return new LandingRate(state.getVelocity(), thrust, state.getShuttle_direction(), state.getWind_direction(), state.getPrevWindVector(), angularAccel);
    }

    public Double torque(Vector3d force, LandingState state) {
//        //FOR LEFT THRUSTER
        double thrustAngle = 45;
        force = rotate(state.getAngle(), force);
        double forceMagnitude = force.norm();
        return forceMagnitude*RADIUS*Math.sin(thrustAngle);

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


}
