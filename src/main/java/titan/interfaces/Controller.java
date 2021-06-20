package titan.interfaces;

import titan.flight.State;

public abstract class Controller {
    public abstract RateInterface thrust(RateInterface windRate, StateInterface y);
    public abstract Controller clone();
}
