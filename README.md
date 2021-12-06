## Getting Started

Welcome to the CSI-4124 group 4 simulation project based on the following proposal:
https://docs.google.com/document/d/14V51ohddjVlnsND_AvSgljh5nh724K56AJ-VW-dpAv0/edit?usp=sharing

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Install and run the simulations

- Download VSCode (1.44+), Java 11, Language Support for Java by Red Hat (version 0.32.0 or later).
- Clone the project and open the project in VSCode.
- Navigate to src/com/sim/proj directory and open App.java.
- Navigate to the bottom of the explorer and click on JAVA PROJECTS (Allow it time to load as well)
- Run option 1: Run App.java to run the simulations and display output in console.

- Run option 2: open runSim.sh and edit localPath field with your local project folder path. open bash (git bash for windows) in the project folder and run ./runSim.sh. runSim.sh will   - compile the project, export the jar and execute it. Outputs are printed in a file called results_XXX, and then dysplayed in the terminal with colors
- to open any results_XXX file with text colors, run: less -R ./results_XXX (This also ensure that you get all the outputs in the terminal)

- Run option 2: open runSim.sh and edit localPath field with your local project folder path.
                open bash (git bash for windows) in the project folder and run ./runSim.sh.
                runSim.sh will compile the project, export the jar and execute it
                Outputs are printed in a file called results_XXX

- https://github.com/microsoft/vscode-java-dependency#manage-dependencies for more details.
