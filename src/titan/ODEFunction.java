package titan;

import java.util.Arrays;

public class ODEFunction implements ODEFunctionInterface{
    @Override
    public RateInterface call(double t, StateInterface y) {

        State state = (State) y;

        int size=state.getPosition().length;
        double dt = t - state.getTime();
  //      System.out.println("T: " + t+ " stateTime " +state.getTime());

        double G = 6.67430E-11;

        Vector3d[] aRates = new Vector3d[size];
        Vector3d[] vRates = new Vector3d[size];

        for (int i = 0; i < size; i++) {
            Vector3d a = new Vector3d(0,0,0);
            Vector3d v = new Vector3d(0,0,0);

            for (int j = 0; j < size; j++) {
                if (i != j) {

                    double p = (G * State.mass[j])/Math.pow(state.getPosition()[j].dist(state.getPosition()[i]),3);
                    a = a.addMul(p, state.getPosition()[j].sub(state.getPosition()[i]));
                }
            }
            v=state.getVelocities()[i].addMul(dt,a);
        //    System.out.println(v);

            aRates[i] = a;
            vRates[i] = v;
        }


        Rate rate = new Rate(vRates, aRates);

        return rate;
    }


}
