/*
 * @author Pieter Collins, Christof Seiler, Katerina Stankova, Nico Roos, Katharina Schueller
 * @version 0.99.0
 *
 * This interface serves as the API for students in phase 1.
 */

package titan;

/**
 * An interface representing the time-derivative (rate-of-change) of the state of a system.
 *
 * The only uses of this interface are to be the output of the ODEFunctionInterface,
 * and to participate in the addMul method of StateInterface. A concrete State class
 * must cast the rate argument of addMul to a concrete Rate class of the expected type.
 *
 * For example, a Vector2d object might implement both StateInterface and RateInterface,
 * and define an addMul method taking and returning Vector2d object. The overriden addMul
 * from StateInterface would then be implemented by casting the rate to Vector2d, and
 * dispatching to the addMul method taking a Vector2d.
 */
public interface RateInterface {
}
