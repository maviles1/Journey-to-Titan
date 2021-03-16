/*
 * @author Pieter Collins, Christof Seiler, Katerina Stankova, Nico Roos, Katharina Schueller
 * @version 0.99.0
 *
 * This interface serves as the API for students in phase 1.
 */

package titan;

import titan.Vector3dInterface;

public interface ODEFunctionInterface {

    /*
     * This is an interface for the function f that represents the
     * differential equation dy/dt = f(t,y).
     * You need to implement this function to represent to the laws of physics.
     *
     * For example, consider the differential equation
     *   dy[0]/dt = y[1];  dy[1]/dt=cos(t)-sin(y[0])
     * Then this function would be
     *   f(t,y) = (y[1],cos(t)-sin(y[0])).
     *
     * @param   t   the time at which to evaluate the function
     * @param   y   the state at which to evaluate the function
     * @return  The average rate-of-change over the time-step. Has dimensions of [state]/[time].
     */
    public RateInterface call(double t, StateInterface y);

}
