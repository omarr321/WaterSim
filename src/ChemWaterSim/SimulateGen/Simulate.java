package ChemWaterSim.SimulateGen;

import ChemWaterSim.Molecule.MOLECULE_DIR;
import ChemWaterSim.Molecule.Molecule;
import ChemWaterSim.SimulateGen.SimulateHelper;
import ChemWaterSim.SimulateGen.SimulateInitHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This is the simulate class that simulate how water will crystallize in a cube.
 * @author Omar Radwan
 */
public class Simulate{
    private int threadCount;
    private SimulateInitHelper[] threads;

    private double temperature;
    final private double growthRate;
    final private double nucleationRate;
    final private int gridSize;
    final private Molecule[][][] grid;
    private double currentETA = 0;
    private double tickTime = 0;
    private ArrayList<Molecule> crystals = new ArrayList<>();
    private ArrayList<Molecule> cleaned = new ArrayList<>();
    /**
     * This is the constructor for the WaterSim.Simulate class.
     * @param tmp - The temperature that the water will be at.
     * @param growth - How fast a water crystal will grow.
     * @param nucleation - The chance of a water molecule crystallize.
     * @param gridSize - the size of the container.
     */
    public Simulate(double tmp, double growth, double nucleation, int gridSize, int threadCount){
        System.out.printf("Starting Values:\n\tTemperature: %.2f\n\tGrowth Rate: %.2f\n\tNucleation Rate: %.2f\n\tGrid Size:%d\n", tmp, growth, nucleation, gridSize);
        System.out.print("Initializing...\n");

        this.threadCount = threadCount;
        threads = new SimulateInitHelper[this.threadCount];

        this.temperature = tmp;
        this.growthRate = growth;
        this.nucleationRate = nucleation;
        this.gridSize = gridSize;
        this.grid = new Molecule[this.gridSize][this.gridSize][this.gridSize];

        for (int i = 0; i < this.threadCount; i++){
            SimulateInitHelper temp = new SimulateInitHelper("Thread " + i, i+1, this.threadCount, this.grid);
            temp.start();
            this.threads[i] = temp;
        }

        for (int i = 0; i < this.threadCount; i++){
            while(this.threads[i].getThread().isAlive());
        }

        System.out.println("DONE!");
    }

    /**
     * This will run the simulation.
     * @param tickCount - The number of ticks to go before stopping
     */
    public void run(int tickCount){
        System.out.printf("Starting Simulation for %d ticks...\n", tickCount);
        for (int i = 0; i < tickCount; i++) {
            final long startTime = System.currentTimeMillis();
            this.tick();
            final long endTime = System.currentTimeMillis();
            this.tickTime = endTime - startTime;
            if (i != 0) {
                this.currentETA = (currentETA + tickTime) / 2;
            } else {
                this.currentETA = (currentETA + tickTime);
            }

            System.out.println("ETA to completion (DAY:HOUR:MIN:SEC:MILLI): " + this.millToStr(this.currentETA*(tickCount-(i+1))));
        }
    }

    /**
     * This will move the simulation one tick;
     */
    public void tick() {
        System.out.println("Running tick...");

        System.out.print("\tChanging Temperature...");
        //change temp
        this.temperature = this.temperature + this.randomNum(-1, 1, 5);
        System.out.println("DONE!");

        for (int i = 0; i < this.threadCount; i++){
            SimulateHelper temp = new SimulateHelper("Thread " + i, i+1, this.threadCount, this.grid, this.getTemperatureChance(), this.getNucleationChance(), this.getGrowthChance(), this.crystals, this.cleaned);
            temp.start();
            this.threads[i] = temp;
        }

        for (int i = 0; i < this.threadCount; i++){
            while(this.threads[i].getThread().isAlive());
        }
        crystals.clear();
        crystals.addAll(cleaned);

        //Removes duplicates from the ArrayList.
        Set<Molecule> set = new LinkedHashSet<>(this.crystals);
        crystals.clear();
        crystals.addAll(set);
    }

    /**
     * This will generate a boolean array of if a water molecule has been crystallized or not.
     * @return - a boolean array of water molecule that have been crystallized.
     */
    public boolean[][][] genCrystallizedArray() {
        boolean[][][] temp = new boolean[this.gridSize][this.gridSize][this.gridSize];
        for (int x = 0; x < this.gridSize; x++) {
            for (int y = 0; y < this.gridSize; y++) {
                for (int z = 0; z < this.gridSize; z++) {
                    temp[x][y][z] = this.getPos(x,y,z).isCrystallized();
                }
            }
        }
        return temp;
    }

    /**
     * This will return the WaterSim.Molecule at the position given.
     * @param x - the x position.
     * @param y - the y position.
     * @param z - the z position.
     * @return - the WaterSim.Molecule at that position. Will return Null if position is invalid.
     */
    private Molecule getPos(int x, int y, int z) {
        try {
            return this.grid[x][y][z];
        } catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }

    private double getTemperatureChance() {
        double temp;
        if (this.temperature <= 0) {
            temp = 1;
        } else if (this.temperature >= 273.15) {
            temp = 0;
        } else {
            temp = Math.abs(this.temperature/273.15);
        }

        return temp;
    }

    /**
     * This will calculate the nucleationChance which is based on NuclearRate and temperature.
     * @return - a double between 0 and 1 for the chance of nuclear rate.
     */
    private double getNucleationChance() {
        return (this.nucleationRate/100) * this.getTemperatureChance();
    }

    /**
     * This will calculate the growthChance which is based on GrowthRate and temperature.
     * @return - a double between 0 and 1 for the chance of growth rate.
     */
    private double getGrowthChance() {
        double temp = this.getTemperatureChance();
        if (temp == 1) {
            return 1;
        } else if (temp == 0){
            return 0;
        } else {
         return (this.growthRate/100);
        }
    }

    /**
     * Generates a random number between min and max with the precision precision.
     * @param min - min number
     * @param max - max number
     * @param precision - the amount of precision
     * @return - a random number between the min and max.
     */
    protected static double randomNum(double min, double max, int precision) {
        double temp = Math.random()*(max-min)+min;
        DecimalFormat df = new DecimalFormat("#." + "#".repeat(Math.max(0, precision)));
        return Double.parseDouble(df.format(temp));
    }

    public static String millToStr(double time) {
        double temp = time;
        int day = (int) (temp/86400000);
        temp = temp % 86400000;

        int hour = (int) (temp/3600000);
        temp = temp % 3600000;

        int min = (int) (temp/60000);
        temp = temp % 60000;

        int second = (int) (temp/1000);
        temp = temp % 1000;

        String results = day + ":" + hour + ":" + min + ":" + second + ":" + (int)temp;
        return results;
    }
}