package titan;

import java.util.Arrays;

public class Solver implements ODESolverInterface{
    @Override
    public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double[] ts) {
        int stepSize=1000;
        StateInterface[] s = new StateInterface[ts.length];
        s[0]=step(f,ts[0],y0,ts[0]);
        for(int i=1;i<ts.length;i++){
            s[i]=step(f,ts[i],s[i-1],stepSize);
        }
        return s;
    }

    @Override
    public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double tf, double h) {
        //check if t should be the total time
        //TODO or just cast to int? does it give the correct arraysize???
        int size= (int) Math.round(tf/h+1);
        StateInterface[] s = new StateInterface[size];
        s[0]=y0;

        double t=0;
        State state1 = (State) s[0];
        Vector3d posTitan1 =  state1.getPosition()[8];
        Vector3d posProbe1 =  state1.getPosition()[11];
        double dLast = posTitan1.dist(posProbe1);

        for(int i=1;i<size-1;i++){
            t+=h;
            s[i]=step(f,t,s[i-1],h);


            //this is for the brute force
            State state = (State) s[i];
            Vector3d posTitan =  state.getPosition()[8];
            Vector3d posProbe =  state.getPosition()[11];
            double distance = posTitan.dist(posProbe);

            if(distance<2575000){
                State start = (State) s[0];
                System.out.println("Start state: ");
                System.out.println("Titan: " + start.getPosition()[8] + " vel: " + start.getVelocities()[8] );
                System.out.println("Probe: " + start.getPosition()[11] + " vel: " + start.getVelocities()[11]);
                System.out.println("End state: ");
            }

        }
        //tf or tf/h
        s[size-1]=step(f,tf,s[size-2],tf-t);
        for(int i=1;i<size;i++){
   //         System.out.println(s[i]);
        }
        return s;
    }

    @Override
    public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h) {

        RateInterface r = f.call(t, y);
        return y.addMul(h,r); //h*f(t,y)
    }
}
