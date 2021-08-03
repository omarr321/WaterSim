package ChemWaterSim.SimulateGen;

import ChemWaterSim.Molecule.MOLECULE_DIR;
import ChemWaterSim.Molecule.Molecule;

import java.util.*;

/**
 * This class is used as a way to multithread the simulation.
 * @author Omar Radwan
 */
public class SimulateHelper extends SimulateInitHelper {
    private double temperature;
    private double nucleationChance;
    private double growChance;
    private ArrayList<Molecule> crystals;
    private ArrayList<Molecule> crystalsTemp;

    /**
     * This is the constructor for the Sim Helper class.
     * @param name - Thread name
     * @param id - Thread id
     * @param threadCount - Number of threads
     * @param data - The data that need to be worked on
     * @param temperature - The temperature of the simulation
     * @param nucleationChance - The nuclear chance of the simulation
     * @param growChance - Thr grow chance of the simulation
     * @param crystals - The Arraylist of crystals
     * @param crystalsTemp - The temp Arraylist for the new crystal array
     */
    public SimulateHelper(String name, int id, int threadCount, Molecule[][][] data, double temperature, double nucleationChance, double growChance, ArrayList<Molecule> crystals, ArrayList<Molecule> crystalsTemp) {
        super(name, id, threadCount, data);
        this.temperature = temperature;
        this.nucleationChance = nucleationChance;
        this.growChance = growChance;
        this.crystals= crystals;
        this.crystalsTemp = crystalsTemp;
    }

    /**
     * This method goes thought the crystal Arraylist and first remove duplacates and then removes any crystals that can
     * not crystalize anymore water.
     */
    private Molecule[] purgeCrystalsList(Molecule[] arr) {
        ArrayList<Molecule> results = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            Molecule curr = arr[i];
            int[] cords = curr.getLocation();

            boolean flag = true;

            if ((this.getPos(cords[0] + 1, cords[1], cords[2]) != null) && !(this.getPos(cords[0] + 1, cords[1], cords[2]).isCrystallized())) {
                flag = false;
            } else if ((this.getPos(cords[0] - 1, cords[1], cords[2]) != null) && !(this.getPos(cords[0] - 1, cords[1], cords[2]).isCrystallized())) {
                flag = false;
            } else if ((this.getPos(cords[0], cords[1] + 1, cords[2]) != null) && !(this.getPos(cords[0], cords[1] + 1, cords[2]).isCrystallized())) {
                flag = false;
            } else if ((this.getPos(cords[0], cords[1] - 1, cords[2]) != null) && !(this.getPos(cords[0], cords[1] - 1, cords[2]).isCrystallized())) {
                flag = false;
            } else if ((this.getPos(cords[0], cords[1], cords[2] + 1) != null) && !(this.getPos(cords[0], cords[1], cords[2] + 1).isCrystallized())) {
                flag = false;
            } else if ((this.getPos(cords[0], cords[1], cords[2] - 1) != null) && !(this.getPos(cords[0], cords[1], cords[2] - 1).isCrystallized())) {
                flag = false;
            }

            if (!flag) {
                results.add(curr);
            }
        }

        return results.toArray(new Molecule[0]);
    }

    /**
     * Gets the molecule at that position.
     * @param x - X pos
     * @param y - Y pos
     * @param z - Z pos
     * @return - The Molecule at that position or null if it is a invalid position
     */
    private Molecule getPos(int x, int y, int z) {
        try {
            return this.data[x][y][z];
        } catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     *This runs whens the thread is launched.
     */
    @Override
    public void run() {
        int N = this.data.length / this.threadCount;
        int Nr = this.data.length % this.threadCount;

        int currID = this.id;

        int myStart = N * (currID - 1);
        int myEnd;
        if (currID != this.threadCount) {
            myEnd = myStart + N;
        } else {
            myEnd = myStart + N + Nr;
        }

        System.out.print("\tRandomizing the directions of Molecules...\n");
        for (int x = 0; x < this.data.length; x++) {
            for (int y = 0; y < this.data.length; y++) {
                for (int z = myStart; z < myEnd; z++) {
                    //random rotation
                    if (!(this.data[x][y][z].isCrystallized())) {
                        if (Math.random() > this.temperature) {
                            int temp = (int) Simulate.randomNum(1, 3, 0);
                            this.data[x][y][z].randomizeDir(temp);
                        }
                    }
                }
            }
        }

        System.out.print("\tSpawning new Crystals...\n");
        //random nucleationRate
        int count = (int)(Math.sqrt(this.data.length*this.data.length*this.data.length)/this.threadCount);
        for (int i = 0; i < count; i ++) {
            if (Math.random() < this.nucleationChance) {
                int x = (int) Simulate.randomNum(0, this.data.length-1, 0);
                int y = (int) Simulate.randomNum(0, this.data.length-1, 0);
                int z = (int) Simulate.randomNum(0, this.data.length-1, 0);

                this.data[x][y][z].crystallize();
                this.crystals.add(this.data[x][y][z]);
            }
        }

        Molecule[] moleArray = crystals.toArray(new Molecule[0]);

        System.out.print("\tGrowing Crystals...\n");

        ArrayList<int[]> temp = new ArrayList<>();

        N = moleArray.length/this.threadCount;
        Nr = moleArray.length%this.threadCount;

        myStart = N * (currID - 1);
        if (currID != this.threadCount) {
            myEnd = myStart + N;
        } else {
            myEnd = myStart + N + Nr;
        }

        for(int i = myStart; i < myEnd; i++) {
            Molecule curr = moleArray[i];
            int[] currPos = curr.getLocation();
            if (Math.random() <= this.growChance) {
                MOLECULE_DIR dir;
                int[] offset = new int[3];
                offset[0] = 0;
                offset[1] = 0;
                offset[2] = 0;

                int num = (int) Simulate.randomNum(0,5, 0);
                switch(num) {
                    case 0:
                        dir = MOLECULE_DIR.FRONT;
                        offset[2] = -1;
                        break;
                    case 1:
                        dir = MOLECULE_DIR.BACK;
                        offset[2] = 1;
                        break;
                    case 2:
                        dir = MOLECULE_DIR.UP;
                        offset[1] = 1;
                        break;
                    case 3:
                        dir = MOLECULE_DIR.DOWN;
                        offset[1] = -1;
                        break;
                    case 4:
                        dir = MOLECULE_DIR.LEFT;
                        offset[0] = -1;
                        break;
                    case 5:
                        dir = MOLECULE_DIR.RIGHT;
                        offset[0] = 1;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + num);
                }

                Molecule effected = this.getPos(currPos[0]+offset[0],currPos[1]+offset[1],currPos[2]+offset[2]);
                if (effected != null) {
                    if (curr.canBond(effected, dir)){

                        effected.crystallize();
                        temp.add(effected.getLocation());
                    }
                }
            }
        }

        Iterator<int[]> list = temp.iterator();
        while (list.hasNext()) {
            int[] cords = list.next();
            this.crystals.add(this.getPos(cords[0], cords[1], cords[2]));
        }


        System.out.print("\tCleaning Crystal List...\n");
        //purge cryistalList
        if (crystals.toArray(new Molecule[0]).length != 0 && myEnd != 0) {
            Molecule[] cleaned = this.purgeCrystalsList(Arrays.copyOfRange(moleArray, myStart, myEnd - 1));

            for (int i = 0; i < cleaned.length; i++) {
                this.crystalsTemp.add(cleaned[i]);
            }
        }
    }
}
