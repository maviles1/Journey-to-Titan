package titan;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.util.HashMap;

public class SpaceObjectBuilder {

    private ArrayList<SpaceObject> objects = new ArrayList<>();
    public static ArrayList<SpaceObject> spaceObjects = new ArrayList<>();

    public SpaceObjectBuilder(String source){
        ArrayList<SpaceObject> objects = new ArrayList<>();
        File file = new File(source);
        try {
            Scanner in = new Scanner(file);
            while (in.hasNext()){
                HashMap <String, Double>arguments = new HashMap<>();
                String line = in.nextLine();
                String [] lines = line.split(":");
                String name = lines[0];
                String [] args = lines[1].split(",");
                for (int i = 0; i < args.length; i++){
                    arguments.put(args[i].split("=")[0].replaceAll("[\\W]", ""),Double.parseDouble(args[i].split("=")[1].replaceAll("}", "")));
                }
                Vector3d pos = new Vector3d(arguments.get("x"), arguments.get("y"), arguments.get("z"));
                Vector3d vel = new Vector3d(arguments.get("vx"), arguments.get("vy"), arguments.get("vz"));
                SpaceObject body = null;
                if (name.equals("Sun")) {
                    body = new Planet(name, arguments.get("mass"), pos, vel, 0);
                } else if (name.equals("Moon")) {
                    body = new Planet(name, arguments.get("mass"), pos, vel, 0);
                } else {
                    body = new Planet(name, arguments.get("mass"), pos, vel, 0);
                }

                objects.add(body);
                this.objects = objects;
                spaceObjects = objects;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static double standardize(double d){
        return d/1000000000/1000000000/7000000;
    }

    public ArrayList<SpaceObject> getSpaceObjects(){
        return objects;
    }

    public static void main(String [] args){
        SpaceObjectBuilder builder = new SpaceObjectBuilder("src/solar_system_data-2020_04_01.txt");
        System.out.println(builder.getSpaceObjects().get(1).getPosition().getY());
    }
}
