package titan.landing;

import titan.flight.Vector3d;
import titan.interfaces.Controller;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class OpenLoopController implements Controller {

    final double MASS = 6000;
    final double RADIUS = 3;
    double targetAngle;
    final double angleTolerance = 0.1;

    @Override
    public RateInterface thrust(RateInterface windRate, StateInterface y) {
        //return predetermined thrust
        LandingState state = (LandingState) y;
        double altitude = state.getPosition().getY();

        //where u is the thrust
        //horizontal acceleration = u * sin(theta)
        //vertical acceleration = u * cos(theta) (-g)
        //angular acceleration = torque

        double mainThrust = 1; //TODO: potential base thrust off of altitute or if we are doing course correction
        double xAccel = mainThrust * Math.sin(state.getAngle());
        double yAccel = mainThrust * Math.cos(state.getAngle());
        double angularAcceleration = 0;

        System.out.println(state.getAngle() % (2 * Math.PI));
        if (Math.abs(state.getAngle() % (2 * Math.PI)) - targetAngle < angleTolerance) {
            System.out.println("Reached target angle: " + targetAngle);
            //now we need to counter torque
            if (targetAngle == 0) {
                //if we wanted to become upright, and now we are upright
                angularAcceleration = angularAcceleration(rightTorque(10000, state));
                targetAngle = Math.toRadians(-45);
                System.out.println("New Target Angle: " + targetAngle);
            } else {
                if (state.getAngularVelocity() < 0) { //spinning counter-clockwise
                    //need to apply leftTorque
                    angularAcceleration = angularAcceleration(leftTorque(10000, state));
                }
            }
        }

        //double angularAccel = angularAcceleration(rightTorque(100, state));
        Vector3d thrust = new Vector3d(xAccel, yAccel, 0);

//        System.out.println("angular accel: " +  angularAcceleration);
//        System.out.println("angular vel: " +  state.getAngularVelocity());
//        System.out.println("angle: " +  state.getAngle());
        return new LandingRate(state.getVelocity(), thrust, state.getShuttle_direction(), state.getWind_direction(), state.getPrevWindVector(), angularAcceleration);
    }

    public double leftTorque(double strength, LandingState state) {
        Vector3d force = new Vector3d(strength, strength, 0);
        return torque(force, state.getAngle(), 45);
    }

    public double rightTorque(double strength, LandingState state) {
        Vector3d force = new Vector3d(-strength, strength, 0);
        return torque(force, state.getAngle(), -45);
    }

    public double torque(Vector3d force, double probeAngle, double thrustAngle) {
        force = rotate(probeAngle, force);
        double forceMagnitude = force.norm();
        return forceMagnitude * RADIUS * Math.sin(Math.toRadians(thrustAngle));
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
