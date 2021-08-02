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

public class Main extends Application {
    public static boolean[][][] crystal;
    public static int gridSize = 5;
    public static void main(String[] args){
        double tmp;
        double growth;
        double nucleation;
        int tickCount;
        while (true) {
            System.out.println("Welcome to");
            System.out.println("__      __       _                    ___  _        ");
            System.out.println("\\ \\    / / __ _ | |_  ___  _ _       / __|(_) _ __  ");
            System.out.println(" \\ \\/\\/ / / _` ||  _|/ -_)| '_|      \\__ \\| || '  \\ ");
            System.out.println("  \\_/\\_/  \\__/_| \\__|\\___||_|        |___/|_||_|_|_|");
            System.out.println("                                               ___  ___   ___  ");
            System.out.println("                                              | _ \\| _ \\ / _ \\ ");
            System.out.println("                                              |  _/|   /| (_) |");
            System.out.println("                                              |_|  |_|_\\ \\___/ ");

            System.out.println();


            System.out.println("What would you like to do?");
            System.out.println("1 | Start");
            System.out.println("2 | Credits");
            System.out.println("3 | Quit");

            Scanner scanner = new Scanner(System.in);
            int input;
            while (true) {
                System.out.print(">>>");
                try {
                    input = Integer.parseInt(scanner.nextLine());

                    if (input < 1 || input > 3) {
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
                System.out.println("Made By: Omar M. Radwan");
                System.out.print("Type anything to continue...");
                scanner.next();
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            } else {
                System.out.println("Goodbye!");
                System.exit(0);
            }
        }

        //double tmp;
        //double growth;
        //double nucleation;
        //int tickCount;

        System.out.println("What temperature do you want to simulation to run at?");
        double input;
        Scanner scanner = new Scanner(System.in);
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

        System.out.println("How big do you want the grid");
        while (true) {
            System.out.print(">>>");
            try {
                input = Integer.parseInt(scanner.nextLine());

                break;
            } catch (NumberFormatException ex) {
                System.out.println("Error: Enter a number!");
            }
        }
        gridSize = (int)input;

        System.out.println("How many ticks do you want the simulation to run for?");
        while (true) {
            System.out.print(">>>");
            try {
                input = Integer.parseInt(scanner.nextLine());

                break;
            } catch (NumberFormatException ex) {
                System.out.println("Error: Enter a number!");
            }
        }
        tickCount = (int)input;

        Simulate sim = new Simulate(tmp,growth, nucleation,gridSize);
        sim.run(tickCount);

        crystal = sim.genCrystallizedArray();
        launch(args);
    }

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
                        temp.setCullFace(CullFace.NONE);
                        temp.setDrawMode(DrawMode.FILL);
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

        SubScene subScene = new SubScene(root, Screen.getPrimary().getBounds().getWidth()-100, Screen.getPrimary().getBounds().getHeight()-100, false, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.ALICEBLUE);
        subScene.setCamera(camera);
        Group group = new Group();
        group.getChildren().add(subScene);

        return group;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.setTitle("3D Graph");
        Scene scene = new Scene(createContent());
        stage.setScene(scene);
        stage.show();
    }
}
