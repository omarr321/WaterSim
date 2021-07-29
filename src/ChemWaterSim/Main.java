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

public class Main extends Application {
    public static boolean[][][] crystal;
    public static int gridSize = 150;
    public static void main(String[] args){
        double tmp = 40;
        double growth = 90;
        double nucleation = 1;
        int tickCount = 60;
        Simulate test = new Simulate(tmp,growth, nucleation,gridSize);
        test.run(tickCount);

        crystal = test.genCrystallizedArray();
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
