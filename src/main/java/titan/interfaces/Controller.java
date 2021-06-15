package titan.interfaces;

import titan.flight.State;

public interface Controller {
    RateInterface thrust(RateInterface windRate, StateInterface y);
}
