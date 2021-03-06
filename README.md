# banking-queueing-simulations

## Getting Started

Welcome to the CSI-4124 group 4 simulation project based on the following paper:

https://drive.google.com/file/d/1SyjSH26zRqFLq5I-80k-yM5Fw2I7KHQv/view?usp=sharing

This was the modified proposal related to the paper:

https://docs.google.com/document/d/1E5DGf7JqsUPbQBhVTSvf4Xihby42ri2d/edit?usp=sharing&ouid=101360817435021877092&rtpof=true&sd=true

## Future Improvements

- I originally suggested to combine the single-queue and multi-queue classes together, but this was not feasible due to the size of the group and the deadline.

- Remove manually installed project dependencies in the project folder, and use some sort of versioning/package manager if possible.

- Better coding conventions.

- More test cases.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Installation and Run Instructions

- Download VSCode (1.44+), Java 11, Language Support for Java by Red Hat (version 0.32.0 or later).
- Clone the project and open the project in VSCode.
- Navigate to src/com/sim/proj directory and open App.java.
- Navigate to the bottom of the explorer and click on JAVA PROJECTS (Allow it time to load as well)
- Run option 1: Run App.java to run the simulations and display output in console.

- Run option 2: open runSim.sh and edit localPath field with your local project folder path. open bash (git bash for windows) in the project folder and run ./runSim.sh. runSim.sh will   - compile the project, export the jar and execute it. Outputs are printed in a file called results_XXX, and then dysplayed in the terminal with colors
- to open any results_XXX file with text colors, run: less -R ./results_XXX (This also ensure that you get all the outputs in the terminal)

- https://github.com/microsoft/vscode-java-dependency#manage-dependencies for more details.
