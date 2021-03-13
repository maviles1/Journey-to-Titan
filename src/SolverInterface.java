/*
 * @author Pieter Collins, Christof Seiler, Katerina Stankova, Nico Roos, Katharina Schueller
 * @version 0.99.0
 * 
 * This interface serves as the API for students in phase 1.
 */

package titan;

import titan.Vector3dInterface;
import titan.FunctionInterface;

public interface SolverInterface {
    
    /*
     * Update rule for multiple steps. Time will start at zero.
     * 
     * @param   f       the differential equation as defined in the project manual: 
     *                  y(t) describes the position of the system at time t
     *                  f(t, y(t)) describes the derivative of y(t) with respect to time t
     * @param   x0      the starting location
     * @param   h       the step size in seconds
     * @param   nSteps  the total number of time steps
     * @return  an array of size nSteps with all intermediate locations along the path 
     *              
     */
    public Vector3dInterface[] solve(FunctionInterface f, Vector3dInterface x0, double h, int nSteps);
    
    /*
     * Update rule for one step.
     * 
     * @param   f   the differential equation as defined in the project manual: 
     *              y(t) describes the position of the system at time t
     *              f(t, y(t)) describes the derivative of y(t) with respect to time t
     * @param   t   the time
     * @param   x   the location
     * @param   h   the step size in seconds
     * @return  the new location after taking one step 
     *              
     */
    public Vector3dInterface step(FunctionInterface f, double t, Vector3dInterface x, double h);
}
