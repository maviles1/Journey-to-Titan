package titan;

public class ODEFunction implements ODEFunctionInterface{
    @Override
    public RateInterface call(double t, StateInterface y) {
        double dt = t - ((State)y).getTime();

        return null;
    }
}
