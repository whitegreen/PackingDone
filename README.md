# PackingDone
Packing given shapes onto a grid with 0-1 integer linear programming via [Gurobi](https://www.gurobi.com/) Python/Java API.

This repo includes a Python project and a Java project. Each Python file and its Java version always result in the same Gurobi program.

First, install [Gurobi](https://www.gurobi.com/) and get a license.

## Python project: PackGrid
install: python -m pip install gurobipy

The python programs have no visualization. 

## Java project: gurobi_m3
1. link project to gurobi.jar after installing Gurobi
2. add external lib: core.jar, PeasyCam.jar, both are included in the repo.

The Java programs display the visual results:

![results](https://github.com/whitegreen/PackingDone/blob/main/javaDisplay.png)

