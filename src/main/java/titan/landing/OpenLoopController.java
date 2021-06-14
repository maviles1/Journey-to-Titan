package titan.landing;

import titan.flight.State;
import titan.interfaces.Controller;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class OpenLoopController implements Controller {
    @Override
    public RateInterface thrust(RateInterface windRate, StateInterface state) {
        //return predetermined thrust
        return null;
    }
}
