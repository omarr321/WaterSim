module ChemWaterSimFX {
    requires javafx.graphics;
    requires  javafx.controls;
    requires  javafx.fxml;
    requires  javafx.base;

    opens ChemWaterSim;
    opens ChemWaterSim.SimulateGen;
}