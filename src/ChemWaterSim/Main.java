package ChemWaterSim;

import ChemWaterSim.SimulateGen.Simulate;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Scanner;

/**
 * This is the main class for the Sim. First the main thread runs asking info about the simulation. The main thread then
 * calls the simulation taking over this thread. It will run to completion. After the simulation is done, it returns to
 * the main thread and it take the data from the sim and hand it off to JavaFX. JavaFX takes over this thread and
 * displays the graph.
 *
 * @author Omar Radwan
 */
public class Main extends Application {
    public static boolean[][][] crystal;
    public static int gridSize;
    public static void main(String[] args){
        boolean[] filled = new boolean[5];
        for (int i = 0; i <  filled.length; i++) {
            filled[i] = false;
        }
        double tmp = 0;
        double growth = 0;
        double nucleation = 0;
        int tickCount = 0;
        if (args.length == 0) {
            while (true) {
                System.out.println("Welcome to");
                System.out.println("__      __       _                  ___  _        ");
                System.out.println("\\ \\    / / __ _ | |_  ___  _ _     / __|(_) _ __  ");
                System.out.println(" \\ \\/\\/ / / _` ||  _|/ -_)| '_|    \\__ \\| || '  \\ ");
                System.out.println("  \\_/\\_/  \\__/_| \\__|\\___||_|      |___/|_||_|_|_|");
                System.out.println(" ___  ___   ___  ");
                System.out.println("| _ \\| _ \\ / _ \\ ");
                System.out.println("|  _/|   /| (_) |");
                System.out.println("|_|  |_|_\\ \\___/ ");

                System.out.println();


                System.out.println("What would you like to do?");
                System.out.println("1 | Start");
                System.out.println("2 | How to Use");
                System.out.println("3 | Credits");
                System.out.println("4 | Quit");

                Scanner scanner = new Scanner(System.in);
                int input;
                while (true) {
                    System.out.print(">>>");
                    try {
                        input = Integer.parseInt(scanner.nextLine());

                        if (input < 1 || input > 4) {
                            System.out.println("Error: Not a valid number!");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println("Error: Enter a number!");
                    }
                }

                if (input == 1) {
                    break;
                } else if (input == 2) {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("HOW TO USE:");
                    System.out.println("\t1. Run \"Start\" on the main menu.");

                    System.out.println("\t2. It will ask about a few variables explained below:");
                    //temperature
                    System.out.println("\t\t- Temperature (\u00B0C): This is the temperature the Simulation will start at.");
                    System.out.println("\t\t\tThe temperature must be a number.");
                    //growth rate
                    System.out.println("\t\t- Growth Rate (%): This is the growth rate for the simulation will have. The");
                    System.out.println("\t\tgrowth rate will dictate how fast a water crystal will grow.");
                    System.out.println("\t\t\tThe growth rate must be a number.");
                    //nuclear rate
                    System.out.println("\t\t- Nuclear Rate (%): This is the nuclear rate for simulation will have. The");
                    System.out.println("\t\tnuclear rate will dictate the likelihood a water will crystallize randomly.");
                    System.out.println("\t\t\tThe nuclear rate must be a number.");
                    //grid size
                    System.out.println("\t\t- Grid Size (no units): This is the grid size of the simulation. The grid size is the length");
                    System.out.println("\t\tof one size of a 3D box. The box will have equal sizes.");
                    //grid size has to be less than 150 because otherwise Java will run out of memory.
                    System.out.println("\t\t\tThe grid size must be between 5 and 150.");
                    //tick amount
                    System.out.println("\t\t- Tick Amount (no units): This is how long the simulation will run for. One tick is one unit");
                    System.out.println("\t\tof processing time.");
                    System.out.println("\t\t\tTick has to be greater than 0.");

                    System.out.println("3. After you answer the questions, the simulation will run with the parameter you");
                    System.out.println("put in.");
                    System.out.println("4. Once the simulation is complete, a graph will be displayed on screen so you can");
                    System.out.println("see the final results.");

                    System.out.print("Type anything to continue...");
                    scanner.next();
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                } else if (input == 3) {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Made By: Omar M. Radwan");
                    System.out.print("Type anything to continue...");
                    scanner.next();
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                } else {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
            }

        } else {
            for(int i = 0; i < args.length; i++) {
                //gridSize    -size
                //Tempucure   -tmp
                //Growth rate -g
                //nuclear     -n
                //tick count  -tick
                //            --help
                switch (args[i]) {
                    case "--help":
                        System.out.println("Program Arguments:");
                        System.out.println("\t--help: Prints out this page");
                        System.out.println("\t-tmp <double>: Sets the temperature of the simulation");
                        System.out.println("\t-g <double>: Sets the growth rate of the simulation");
                        System.out.println("\t-size <int>: Sets the grid size");
                        System.out.println("\t-n <double>: Sets the nuclear rate of the simulation");
                        System.out.println("\t-tick <int>: Sets the tick amount for the simulation");

                        System.exit(0);
                    case "-tmp":
                        i++;
                        try {
                            double input = Double.parseDouble(args[i]);
                            tmp = input;
                            filled[0] = true;
                            break;
                        } catch (NumberFormatException ex) {
                            System.out.println("Error: Enter a number!");
                        }
                        break;
                    case "-size":
                        i++;
                        try {
                            int input = Integer.parseInt(args[i]);
                            if (input > 150 || input < 5) {
                                System.out.println("Error: Number must be between 5 and 150!");
                            } else {
                                gridSize = input;
                                filled[1] = true;
                                break;
                            }
                        } catch (NumberFormatException ex) {
                            System.out.println("Error: Enter a number!");
                        }
                        break;
                    case "-g":
                        i++;
                        try {
                            double input = Double.parseDouble(args[i]);
                            growth = input;
                            filled[2] = true;
                            break;
                        } catch (NumberFormatException ex) {
                            System.out.println("Error: Enter a number!");
                        }
                        break;
                    case "-n":
                        i++;
                        try {
                            double input = Double.parseDouble(args[i]);
                            nucleation = input;
                            filled[3] = true;
                            break;
                        } catch (NumberFormatException ex) {
                            System.out.println("Error: Enter a number!");
                        }
                        break;
                    case "-tick":
                        i++;
                        try {
                            int input = Integer.parseInt(args[i]);
                            if (input < 1) {
                                System.out.println("Error: Number must be greater than 1!");
                            } else {
                                tickCount = input;
                                filled[4] = true;
                                break;
                            }
                        } catch (NumberFormatException ex) {
                            System.out.println("Error: Enter a number!");
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        double input;
        Scanner scanner = new Scanner(System.in);

        if (!(filled[0])) {
            System.out.println("What temperature do you want to simulation to run at?");
            while (true) {
                System.out.print(">>>");
                try {
                    input = Double.parseDouble(scanner.nextLine());

                    break;
                } catch (NumberFormatException ex) {
                    System.out.println("Error: Enter a number!");
                }
            }
            tmp = input;
        }

        if (!(filled[2])) {
            System.out.println("What growth rate do you want to simulation to run at?");
            while (true) {
                System.out.print(">>>");
                try {
                    input = Double.parseDouble(scanner.nextLine());

                    break;
                } catch (NumberFormatException ex) {
                    System.out.println("Error: Enter a number!");
                }
            }
            growth = input;
        }

        if (!(filled[3])) {
            System.out.println("What nuclear rate do you want to simulation to run at?");
            while (true) {
                System.out.print(">>>");
                try {
                    input = Double.parseDouble(scanner.nextLine());

                    break;
                } catch (NumberFormatException ex) {
                    System.out.println("Error: Enter a number!");
                }
            }
            nucleation = input;
        }

        if(!(filled[1])) {
            System.out.println("How big do you want the grid");
            while (true) {
                System.out.print(">>>");
                try {
                    input = Integer.parseInt(scanner.nextLine());
                    if (input > 150 || input < 5) {
                        System.out.println("Error: Number must be between 5 and 150!");
                    } else {
                        break;
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Error: Enter a number!");
                }
            }
            gridSize = (int) input;
        }

        if (!(filled[4])) {
            System.out.println("How many ticks do you want the simulation to run for?");
            while (true) {
                System.out.print(">>>");
                try {
                    input = Integer.parseInt(scanner.nextLine());
                    if (input < 1) {
                        System.out.println("Error: Number must be greater than 1!");
                    } else {
                        break;
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Error: Enter a number!");
                }
            }
            tickCount = (int) input;
        }

        scanner.close();
        
        Simulate sim = new Simulate(tmp,growth, nucleation,gridSize);
        sim.run(tickCount);

        crystal = sim.genCrystallizedArray();
        launch(args);
    }

    /**
     * Sets up the JavaFX scene
     * @return - a set up JavaFX subScene
     */
    public Parent createContent() {
        Group root = new Group();
        int boxSize = 1000/gridSize;
        for (int x = 0; x < crystal.length; x++) {
            for (int y = 0; y < crystal.length; y++) {
                for (int z = 0; z < crystal.length; z++) {
                    if (crystal[x][y][z]) {
                        Box temp = new Box(boxSize, boxSize, boxSize);
                        temp.setMaterial(new PhongMaterial(Color.RED));

                        temp.setTranslateX(x * boxSize);
                        temp.setTranslateY(y * boxSize);
                        temp.setTranslateZ(z * boxSize);
                        //temp.setCullFace(CullFace.NONE);
                        //temp.setDrawMode(DrawMode.FILL);
                        root.getChildren().add(temp);
                    }
                }
            }
        }

        Translate pivot = new Translate();
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(pivot, yRotate, new Rotate(-20, Rotate.X_AXIS), new Translate(0, 0, -50));
        camera.setNearClip(0.00001);
        camera.setFarClip(1000000.0);
        camera.setFieldOfView(35);
        camera.setTranslateX((Screen.getPrimary().getBounds().getWidth()/2)-500);
        camera.setTranslateY(-300);
        camera.setTranslateZ(-2000);

        root.getChildren().add(camera);

        PointLight light = new PointLight();
        light.setColor(Color.SKYBLUE);
        light.setTranslateX((Screen.getPrimary().getBounds().getWidth()/2)-500);
        light.setTranslateY(-300);
        light.setTranslateZ(-2000);

        root.getChildren().add(light);

        SubScene subScene = new SubScene(root, Screen.getPrimary().getBounds().getWidth()-100, Screen.getPrimary().getBounds().getHeight()-100, false, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.ALICEBLUE);
        subScene.setCamera(camera);
        Group group = new Group();
        group.getChildren().add(subScene);

        return group;
    }

    /**
     * This is called when JavaFX launches.
     * @param stage - The stage to launch with
     */
    @Override
    public void start(Stage stage) {
        stage.setResizable(false);
        stage.setTitle("3D Graph");
        Scene scene = new Scene(createContent());
        stage.setScene(scene);
        stage.show();
    }
}
