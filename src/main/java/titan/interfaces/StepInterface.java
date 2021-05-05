package titan.interfaces;

public interface StepInterface {
    StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h);
}
