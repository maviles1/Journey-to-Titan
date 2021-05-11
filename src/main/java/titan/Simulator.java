package titan;

import titan.interfaces.StateInterface;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Simulator {

    private final int WIDTH = 1200*4;
    private final int HEIGHT = 800*4;

    private StateInterface[] states;
    private State currentState;
    private List<Integer[]> paths = new ArrayList<>();
    private JPanel panel;

    public Simulator(StateInterface[] states) {
        this.states = states;
        this.currentState = (State) states[0];

        JFrame frame = new JFrame();
        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                draw(g);
            }
        };

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);


        try {
            parseTitanTrajectory();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        start();
    }

    public void start() {
        for (int i = 1; i < states.length; i++) {
            panel.repaint();
            currentState = (State) states[i];
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WIDTH, HEIGHT);
        g2.setColor(Color.WHITE);
        for (int i = 0; i < currentState.getPosition().length; i++) {
//            g2.setColor(Color.gray);
//            for (Integer[] pos : paths) {
//                g2.fillOval(pos[0], pos[1], 2, 2);
//            }

            g2.setColor(Color.BLUE);
            g2.fillOval((WIDTH / 2) + (int) pos.get(i).getX(), (HEIGHT / 2) + (int) pos.get(i).getY(), 3, 3);

            g2.setColor(Color.white);

            int x = (WIDTH / 2) + (int) toScreenCoordinates(currentState.getPosition()[i].getX());
            int y = (HEIGHT / 2) + (int) toScreenCoordinates(currentState.getPosition()[i].getY());
            g2.fillOval(x, y, 5, 5);

            if (State.names.get(i).equals("Saturn") || State.names.get(i).equals("Earth"))
                g2.drawString(State.names.get(i), x - 20, y - 10);
            else
                g2.drawString(State.names.get(i), x + 10, y + 20);

            paths.add(new Integer[]{x, y});
        }
    }

    public static double toScreenCoordinates(double d) {
        return ((d / (1e12)) * 200);
    }

    ArrayList<Vector3d> pos = new ArrayList<>();

    public void parseTitanTrajectory() throws FileNotFoundException {
        String url = getClass().getResource("/titan-trajectory-3600.txt").getPath();
        File file = new File(url);
        Scanner scanner = new Scanner(file);
        int size = (int) Math.round((31556926 / 3600.0) + 1.5);

        pos.ensureCapacity(size);

        int j = 1;
        for (int i = 0; scanner.hasNextLine(); i++) {
            String next = scanner.nextLine();
            if (i == j) {
                //System.out.println(next.trim());
                next = next.replaceFirst("\\s+[XYZ]+\\s+=+[\\s]*", "");
                String[] parts = next.split("\\s+[XYZ]+\\s+=+[\\s]*");
                Vector3d posV = new Vector3d(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2].trim()));
                pos.add(posV);
                j += 4;
            }
        }


//        while (scanner.hasNext()) {
//            String next = scanner.next();
//            if (!Character.isDigit(next.charAt(0))) {
//                next = next.trim();
//                String[] xes = next.split(" ");
//            }
//        }

    }
}
