# Group 5
## Project 1-2

Members: Martin Aviles, Lou Feiler, Marie Picquet, Sam Glassman, Yaroslav Tymofiienko, Tjardo Neis

Overview
---

Our goal is to simulate the gravitational forces of celestial bodies and find launch parameters to launch a space probe to Titan.

### How to run:

1. Clone the repository
2. Download the JavaFX sdk
3. Use command line to compile and run the Titan probe simulation.

#### To compile: (Windows)
``cd`` to the src folder of the cloned repository

``javac --module-path "C:\Users\user\path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.resources.fxml *.java``

Be sure to replace the path with the actual path to the location of the ``lib`` folder stored on your computer.

#### To run:

``java --module-path "C:\Users\user\path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.resources.fxml Main``

Use the controls to speed up, slow down, pause, zoom or rewind the trajectory.
