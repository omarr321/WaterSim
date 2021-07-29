package ChemWaterSim.SimulateGen;

import ChemWaterSim.Molecule.MOLECULE_DIR;
import ChemWaterSim.Molecule.Molecule;
import ChemWaterSim.Molecule.SubAtomic.ATOM_TYPE;
import ChemWaterSim.Molecule.SubAtomic.SubAtomic;

public class SimulateInitHelper implements Runnable{
    protected Thread t;
    protected String name;
    protected int id;
    protected int threadCount;
    protected Molecule[][][] data;

    public SimulateInitHelper(String name, int id, int threadCount, Molecule[][][] data) {
        this.name = name;
        this.id = id;
        this.data = data;
        this.threadCount = threadCount;
    }

    @Override
    public void run() {
        System.out.printf("%s: Initing...\n", t.getName());
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
        System.out.printf("%s: INIT DONE!\n", t.getName());
        System.out.printf("%s: Starting work on range x[%d-%d], y[%d-%d], z[%d-%d]...\n", t.getName(), 0, this.data.length - 1, 0, this.data.length - 1, myStart, myEnd);

        for (int x = 0; x < this.data.length; x++) {
            for (int y = 0; y < this.data.length; y++) {
                for (int z = myStart; z < myEnd; z++) {
                    Molecule mole = new Molecule("H2O", x, y, z);
                    mole.setCentralAtom(new SubAtomic("O", ATOM_TYPE.OXYGEN));
                    mole.setAtom(new SubAtomic("H", ATOM_TYPE.HYDROGEN), MOLECULE_DIR.UP);
                    mole.setAtom(new SubAtomic("H", ATOM_TYPE.HYDROGEN), MOLECULE_DIR.FRONT);
                    mole.setAtom(new SubAtomic("ELETRON", ATOM_TYPE.ELETRON_PAIR), MOLECULE_DIR.LEFT);
                    mole.setAtom(new SubAtomic("ELETRON", ATOM_TYPE.ELETRON_PAIR), MOLECULE_DIR.DOWN);
                    mole.randomizeDir();
                    this.data[x][y][z] = mole;
                }
            }
        }

        System.out.printf("%s: WORK DONE!\n", this.name);
    }

    public void start() {
        System.out.println("Starting " + this.name + "...");
        if (t == null) {
            t = new Thread(this, this.name);
            t.start();
        }
    }

    public Thread getThread() {
        return this.t;
    }
}
