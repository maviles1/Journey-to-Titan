package titan.interfaces;

import titan.flight.State;
import titan.flight.Vector3d;
import titan.landing.LandingState;

public abstract class Controller {

    public final double MASS = 6000;
    public final double RADIUS = 3;
    public final double angleTolerance = 1;

    public double targetAngle;
    public double thrustUntil; //TODO: find a formula to calculate this
    public boolean isSetThrustUntil;

    public abstract RateInterface thrust(RateInterface windRate, StateInterface y);

    public abstract Controller clone();

    public double startRotation(LandingState state, double targetAngle) {
        isSetThrustUntil = false;
        this.targetAngle = targetAngle;
        if (state.getAngle() - targetAngle > 0) {
            //we need to rotate to the left
            return angularAcceleration(rightTorque(100, state));
        } else if (state.getAngle() - targetAngle < 0) {
            return angularAcceleration(leftTorque(100, state));
        } else {
            return 0;
        }
    }

    public double useMainThruster(LandingState state, double strength, double duration) {
        if (!isSetThrustUntil) {
            //we only want to update the thrustUntil variable once per "session" so we can keep track of duration without overwriting
            thrustUntil = state.getTime() + duration;
            isSetThrustUntil = true;
        }

        if (state.getTime() < thrustUntil) {
            return strength;
        } else {
            return 0;
        }
    }

    public double stabilize(LandingState state, double tolerance) {
        if (state.getAngularVelocity() < 0 - tolerance) { //spinning counter-clockwise
            //need to apply leftTorque
            //System.out.println("applying left torque");
            return angularAcceleration(leftTorque(50, state));
        } else if (state.getAngularVelocity() > 0 + tolerance) {
            //System.out.println("applying right torque");
            return angularAcceleration(rightTorque(50, state));
        } else {
            //angularVelocity is within tolerance, but maybe not exactly 0;
            return -state.getAngularVelocity(); //cancel any residual angular velocity/acceleration
        }
    }

    public boolean isStable(LandingState state, double tolerance) {
        return Math.abs(state.getAngularVelocity()) < tolerance;
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
