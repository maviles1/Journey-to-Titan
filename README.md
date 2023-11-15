# Journey to Titan

Members: Martin Aviles, Lou Feiler, Marie Picquet, Sam Glassman, Yaroslav Tymofiienko, Tjardo Neis

Overview
---

Our goal is to simulate the gravitational forces of celestial bodies using different types of solvers, and to launch a rocket into Titan's orbit and back to Earth again using thrusters.

### How to run:

1. Clone the repository
2. (Optional) Download and install gradle

### To run:

1. ``cd`` to the root folder of the cloned project.
2. If gradle is installed, execute ``gradle run``. If gradle is not installed, execute ``gradlew run``
3. In the launcher window, input the launch parameters and select solver(s) (for 2D simulation). Select the 3D option to execute 3D simulation (uses hardcoded parameters)

#### Controls

For 2D simulation:
Click on simulation pane to focus
- zoom out: press ``Z`` or ``Y``
- zoom in: press ``X``
- speed up simulation:  press ``=`` or ``A``
- slow down simulation: press ``-`` or ``S``
- toggle debug console visibility: press ``C``
- focus view on Probe: press ``P``
- focus view on celestial body: press ``0-9``, (where Sun is ``0``, etc)
- focus view on Titan: press ``T`` or ``8``
- Use scrolling or dragging in order to pan 
- pressing a focus view key multiple times will cycle the focus through the different solvers simulating that body. (E.g: pressing ``P`` once focuses view on Probe of first Solver. Pressing ``P`` again will focus view on next Solver's Probe)

For 3D simulation:
- Press ``SPACE`` to start simulation
- Click on ``CAM`` button to switch focus
- Use scrolling to zoom
