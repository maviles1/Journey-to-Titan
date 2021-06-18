package titan.landing;

import titan.flight.Vector3d;
import titan.interfaces.Controller;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class OpenLoopController implements Controller {

    final double MASS = 6000;
    final double RADIUS = 3;
    double targetAngle;
    final double angleTolerance = 0.01;

    public OpenLoopController() {}
    public OpenLoopController(double targetAngle) {
        this.targetAngle = targetAngle;
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

        if (state.getTime() == 1) {
            angularAcceleration = angularAcceleration(rightTorque(50, state));
            targetAngle = Math.toRadians(-45);
            System.out.println("New Target Angle: " + targetAngle);
        }

        System.out.println("difference: " + (state.getAngle() % (2 * Math.PI) - targetAngle));
        if (Math.abs(state.getAngle() % (2 * Math.PI) - targetAngle) < angleTolerance) {
            System.out.println("Reached target angle: " + targetAngle);
            //now we need to counter torque
            if (targetAngle == 0) {
                //if we wanted to become upright, and now we are upright
                System.out.println("UPRIGHT");
                if (state.getAngularVelocity() < 0 - angleTolerance/2.0) { //spinning counter-clockwise
                    //need to apply leftTorque
                    System.out.println("applying left torque");
                    angularAcceleration = angularAcceleration(leftTorque(50, state));
                } else if (state.getAngularVelocity() > 0 + angleTolerance/2.0) {
                    System.out.println("applying right torque");
                    angularAcceleration = angularAcceleration(rightTorque(50, state));
                } else {
                    //now we should be uprightish
                    angularAcceleration = -state.getAngularVelocity();
                    mainThrust = 1.3;
                }
            } else {
                if (state.getAngularVelocity() < 0 - angleTolerance) { //spinning counter-clockwise
                    //need to apply leftTorque
                    System.out.println("applying left torque");
                    angularAcceleration = angularAcceleration(leftTorque(50, state));
                } else if (state.getAngularVelocity() > 0 + angleTolerance) {
                    System.out.println("applying right torque");
                    angularAcceleration = angularAcceleration(rightTorque(50, state));
                } else {
                    System.out.println("angular velocity is zero: " + state.getAngularVelocity());
                    //now we are at our target angle and don't have any other rotational velocity
                    //now we can thrust hard
                    targetAngle = 0;
                    mainThrust = 180;
                    angularAcceleration = angularAcceleration(leftTorque(100, state));
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

    @Override
    public OpenLoopController clone() {
        return new OpenLoopController(targetAngle);
    }

}
