package ChemWaterSim.Molecule;

import ChemWaterSim.Molecule.SubAtomic.ATOM_TYPE;
import ChemWaterSim.Molecule.SubAtomic.SubAtomic;

/**
 * This class holds atoms together in a molecule and makes it easy to do things with said molecule.
 * @author Omar Radwan
 */
public class Molecule {
    private String name;
    private SubAtomic[][][] atomGrid;
    private boolean crystallized;
    final private int[] location = new int[3];

    /**
     * Constructor that creates an empty WaterSim.Molecule.
     * @param name - name of molecule.
     */
    public Molecule(String name, int x, int y, int z) {
        this.name = name;
        this.location[0] = x;
        this.location[1] = y;
        this.location[2] = z;
        this.crystallized = false;
        this.atomGrid = initArr();
    }

    /**
     * This will try to add an atom as the centralAtom but will fail if there is an atom already there.
     * @param atom - the atom to add.
     * @return - will return true if the atom was added successfully, false if not.
     */
    public boolean addCentralAtom(SubAtomic atom) {
        if (!(this.atomGrid[1][1][1].getType().equals(ATOM_TYPE.NONE))){
            return false;
        }

        return setCentralAtom(atom);
    }

    /**
     * This is like add atom but will force the atom to change and overwrite what is there.
     * @see #addCentralAtom(SubAtomic).
     * @param atom - the atom to add.
     * @return - Will always return true.
     */
    public boolean setCentralAtom(SubAtomic atom) {
        this.atomGrid[1][1][1] = atom;
        return true;
    }

    /**
     * This will take the atom and add it to the molecule on the face given.
     * @param atom - the atom to add.
     * @param dir - the face to add it to.
     * @return - will return true if the atom was added successfully, false if not.
     */
    public boolean addAtom(SubAtomic atom, MOLECULE_DIR dir){
        if (!(getAtom(dir).getType().equals(ATOM_TYPE.NONE))){
            return false;
        }

        return setAtom(atom, dir);
    }

    /**
     * This is like add atom but will force the atom to change and overwrite what is there.
     * @param atom - the atom to add.
     * @param dir - the face toa dd it to.
     * @return - will always return true.
     */
    public boolean setAtom(SubAtomic atom, MOLECULE_DIR dir){
        int[] temp = dirToCord(dir);
        this.atomGrid[temp[0]][temp[1]][temp[2]] = atom;
        return true;
    }

    /**
     * This will return the atom of the face given.
     * @param dir - the face that you want the atom from.
     * @return - the atom found.
     */
    public SubAtomic getAtom(MOLECULE_DIR dir){
        int[] temp = dirToCord(dir);
        return this.atomGrid[temp[0]][temp[1]][temp[2]];
    }

    /**
     * Like getAtom but is for the central atom.
     * @return - the central atom.
     */
    public SubAtomic getCentralAtom() {
        return this.atomGrid[1][1][1];
    }

    /**
     * This method will rotate the atom 8 times to a new postion.
     */

    public int[] getLocation() {
        return this.location;
    }

    public void randomizeDir(){
        this.randomizeDir(7);
    }

    /**
     * This method will rotate the atom <count> number of time(s) to a new postion.
     * @param count the number of rotates to do.
     */
    public void randomizeDir(int count){
        for (int i = 0; i < count; i++) {
            double randomNum = (Math.random()*100);

            if (randomNum > 0 && randomNum <= 16.67) {
                rotate(MOLECULE_DIR.UP);
            } else if (randomNum > 16.67 && randomNum <= 33.34) {
                rotate(MOLECULE_DIR.DOWN);
            } else if (randomNum > 33.34 && randomNum <= 50.01) {
                rotate(MOLECULE_DIR.LEFT);
            } else if (randomNum > 50.01 && randomNum <= 66.68) {
                rotate(MOLECULE_DIR.RIGHT);
            } else if (randomNum > 66.68 && randomNum <= 83.35) {
                rotate(MOLECULE_DIR.FRONT);
            } else {
                rotate(MOLECULE_DIR.BACK);
            }
        }
    }

    /**
     * This will test to see if this molecule can bond with another one.
     * @param molecule - the molecule to see if bonding is possible.
     * @param dir - the direction the molecule is relative to this molecule.
     * @return - Will return true if bonding is possible, false if not.
     */
    public boolean canBond(Molecule molecule, MOLECULE_DIR dir) {
        switch (dir) {
            case UP -> {
                if (this.getAtom(dir).getType().equals(ATOM_TYPE.HYDROGEN) && molecule.getAtom(MOLECULE_DIR.DOWN).getType().equals(ATOM_TYPE.ELETRON_PAIR)) {
                    return true;
                } else return this.getAtom(dir).getType().equals(ATOM_TYPE.ELETRON_PAIR) && molecule.getAtom(MOLECULE_DIR.DOWN).getType().equals(ATOM_TYPE.HYDROGEN);
            }
            case DOWN -> {
                if (this.getAtom(dir).getType().equals(ATOM_TYPE.HYDROGEN) && molecule.getAtom(MOLECULE_DIR.UP).getType().equals(ATOM_TYPE.ELETRON_PAIR)) {
                    return true;
                } else return this.getAtom(dir).getType().equals(ATOM_TYPE.ELETRON_PAIR) && molecule.getAtom(MOLECULE_DIR.UP).getType().equals(ATOM_TYPE.HYDROGEN);
            }
            case LEFT -> {
                if (this.getAtom(dir).getType().equals(ATOM_TYPE.HYDROGEN) && molecule.getAtom(MOLECULE_DIR.RIGHT).getType().equals(ATOM_TYPE.ELETRON_PAIR)) {
                    return true;
                } else return this.getAtom(dir).getType().equals(ATOM_TYPE.ELETRON_PAIR) && molecule.getAtom(MOLECULE_DIR.RIGHT).getType().equals(ATOM_TYPE.HYDROGEN);
            }
            case RIGHT -> {
                if (this.getAtom(dir).getType().equals(ATOM_TYPE.HYDROGEN) && molecule.getAtom(MOLECULE_DIR.LEFT).getType().equals(ATOM_TYPE.ELETRON_PAIR)) {
                    return true;
                } else return this.getAtom(dir).getType().equals(ATOM_TYPE.ELETRON_PAIR) && molecule.getAtom(MOLECULE_DIR.LEFT).getType().equals(ATOM_TYPE.HYDROGEN);
            }
            case FRONT -> {
                if (this.getAtom(dir).getType().equals(ATOM_TYPE.HYDROGEN) && molecule.getAtom(MOLECULE_DIR.BACK).getType().equals(ATOM_TYPE.ELETRON_PAIR)) {
                    return true;
                } else return this.getAtom(dir).getType().equals(ATOM_TYPE.ELETRON_PAIR) && molecule.getAtom(MOLECULE_DIR.BACK).getType().equals(ATOM_TYPE.HYDROGEN);
            }
            case BACK -> {
                if (this.getAtom(dir).getType().equals(ATOM_TYPE.HYDROGEN) && molecule.getAtom(MOLECULE_DIR.FRONT).getType().equals(ATOM_TYPE.ELETRON_PAIR)) {
                    return true;
                } else return this.getAtom(dir).getType().equals(ATOM_TYPE.ELETRON_PAIR) && molecule.getAtom(MOLECULE_DIR.FRONT).getType().equals(ATOM_TYPE.HYDROGEN);
            }
        }
        return false;
    }

    /**
     * This method returns if the molecule is crystallized.
     * @return - Will return true if this molecule is a crystallized, false if not.
     */
    public boolean isCrystallized() {
        return this.crystallized;
    }

    /**
     * This method will make the molecule into a crystal.
     */
    public void crystallize() {
        this.crystallized = true;
    }

    /**
     * This method will rotate the molecule in that direction. I use the MOLECULE_DIR for rotation so the names do not line up but it works.
     * @param dir -  the direction to rotate.
     */
    private void rotate(MOLECULE_DIR dir) {
        switch (dir) {
            case UP -> {
                SubAtomic[][][] temp = initArr();

                rotateHelper(temp, MOLECULE_DIR.FRONT, MOLECULE_DIR.UP);
                rotateHelper(temp, MOLECULE_DIR.UP, MOLECULE_DIR.BACK);
                rotateHelper(temp, MOLECULE_DIR.BACK, MOLECULE_DIR.DOWN);
                rotateHelper(temp, MOLECULE_DIR.DOWN, MOLECULE_DIR.FRONT);
                rotateHelper(temp, MOLECULE_DIR.LEFT, MOLECULE_DIR.LEFT);
                rotateHelper(temp, MOLECULE_DIR.RIGHT, MOLECULE_DIR.RIGHT);
                temp[1][1][1] = getCentralAtom();

                this.atomGrid = temp;
            }
            case DOWN -> {
                SubAtomic[][][] temp = initArr();

                rotateHelper(temp, MOLECULE_DIR.UP, MOLECULE_DIR.FRONT);
                rotateHelper(temp, MOLECULE_DIR.BACK, MOLECULE_DIR.UP);
                rotateHelper(temp, MOLECULE_DIR.DOWN, MOLECULE_DIR.BACK);
                rotateHelper(temp, MOLECULE_DIR.FRONT, MOLECULE_DIR.DOWN);
                rotateHelper(temp, MOLECULE_DIR.LEFT, MOLECULE_DIR.LEFT);
                rotateHelper(temp, MOLECULE_DIR.RIGHT, MOLECULE_DIR.RIGHT);
                temp[1][1][1] = getCentralAtom();

                this.atomGrid = temp;
            }

            case RIGHT -> {
                SubAtomic[][][] temp = initArr();

                rotateHelper(temp, MOLECULE_DIR.FRONT, MOLECULE_DIR.RIGHT);
                rotateHelper(temp, MOLECULE_DIR.RIGHT, MOLECULE_DIR.BACK);
                rotateHelper(temp, MOLECULE_DIR.BACK, MOLECULE_DIR.LEFT);
                rotateHelper(temp, MOLECULE_DIR.LEFT, MOLECULE_DIR.FRONT);
                rotateHelper(temp, MOLECULE_DIR.UP, MOLECULE_DIR.UP);
                rotateHelper(temp, MOLECULE_DIR.DOWN, MOLECULE_DIR.DOWN);
                temp[1][1][1] = getCentralAtom();

                this.atomGrid = temp;
            }

            case LEFT -> {
                SubAtomic[][][] temp = initArr();

                rotateHelper(temp, MOLECULE_DIR.RIGHT, MOLECULE_DIR.FRONT);
                rotateHelper(temp, MOLECULE_DIR.BACK, MOLECULE_DIR.RIGHT);
                rotateHelper(temp, MOLECULE_DIR.LEFT, MOLECULE_DIR.BACK);
                rotateHelper(temp, MOLECULE_DIR.FRONT, MOLECULE_DIR.LEFT);
                rotateHelper(temp, MOLECULE_DIR.UP, MOLECULE_DIR.UP);
                rotateHelper(temp, MOLECULE_DIR.DOWN, MOLECULE_DIR.DOWN);
                temp[1][1][1] = getCentralAtom();

                this.atomGrid = temp;
            }

            case FRONT -> {
                SubAtomic[][][] temp = initArr();

                rotateHelper(temp, MOLECULE_DIR.UP, MOLECULE_DIR.RIGHT);
                rotateHelper(temp, MOLECULE_DIR.RIGHT, MOLECULE_DIR.DOWN);
                rotateHelper(temp, MOLECULE_DIR.DOWN, MOLECULE_DIR.LEFT);
                rotateHelper(temp, MOLECULE_DIR.LEFT, MOLECULE_DIR.UP);
                rotateHelper(temp, MOLECULE_DIR.FRONT, MOLECULE_DIR.FRONT);
                rotateHelper(temp, MOLECULE_DIR.BACK, MOLECULE_DIR.BACK);
                temp[1][1][1] = getCentralAtom();

                this.atomGrid = temp;
            }

            case BACK -> {
                SubAtomic[][][] temp = initArr();

                rotateHelper(temp, MOLECULE_DIR.RIGHT, MOLECULE_DIR.UP);
                rotateHelper(temp, MOLECULE_DIR.DOWN, MOLECULE_DIR.RIGHT);
                rotateHelper(temp, MOLECULE_DIR.LEFT, MOLECULE_DIR.DOWN);
                rotateHelper(temp, MOLECULE_DIR.UP, MOLECULE_DIR.LEFT);
                rotateHelper(temp, MOLECULE_DIR.FRONT, MOLECULE_DIR.FRONT);
                rotateHelper(temp, MOLECULE_DIR.BACK, MOLECULE_DIR.BACK);
                temp[1][1][1] = getCentralAtom();

                this.atomGrid = temp;
            }
        }
    }

    /**
     * This method moves the atom from atomGrid to the working array.
     * @param arr - The working array.
     * @param start - The atom direction from atomGrid.
     * @param end - The face to put the atom on the working array.
     */
    private void rotateHelper(SubAtomic[][][] arr, MOLECULE_DIR start, MOLECULE_DIR end) {
        int[] startPos = dirToCord(start);
        int[] endPos = dirToCord(end);
        arr[endPos[0]][endPos[1]][endPos[2]] = this.atomGrid[startPos[0]][startPos[1]][startPos[2]];
    }

    /**
     * This method gens a new SubAtomic 3x3x3 array and sets all the atoms to none.
     * @return - the gened array.
     */
    private SubAtomic[][][] initArr() {
        SubAtomic[][][] temp = new SubAtomic[3][3][3];
        for(int x = 0; x < 3; x++) {
            for(int y = 0; y < 3; y++) {
                for(int z = 0; z < 3; z++) {
                    temp[x][y][z] = new SubAtomic("Empty", ATOM_TYPE.NONE);
                }
            }
        }
        return temp;
    }

    /**
     * converts a direction to cords reltive to the central atom.
     * @param dir - The direction you wants the cords for.
     * @return - an Array of the x,y,z in that order.
     */
    private int[] dirToCord(MOLECULE_DIR dir) {
        switch (dir) {
            case UP -> {
                return new int[]{1, 0, 1};
            }
            case DOWN -> {
                return new int[]{1, 2, 1};
            }
            case FRONT -> {
                return new int[]{1, 1, 0};
            }
            case BACK -> {
                return new int[]{1, 1, 2};
            }
            case LEFT -> {
                return new int[]{0, 1, 1};
            }
            case RIGHT -> {
                return new int[]{2, 1, 1};
            }
        }

        return new int[]{-1,-1,-1};
    }

    /**
     * This method takes all the data in the molecule and puts it into a sting formatted nicely.
     * @return - The String formatted nicely.
     */
    @Override
    public String toString() {
        return "WaterSim.Molecule{\n" +
                "\tname=" + this.name + "\n" +
                "\tcrystallized=" + crystallized + "\n" +
                "\tatomGrid=" + "\n" +
                "\t\tFRONT:" + "\n" +
                "\t\t\t" + this.atomGrid[0][0][0].toString() + ", " + this.atomGrid[1][0][0].toString() + ", " + this.atomGrid[2][0][0].toString() + "\n" +
                "\t\t\t" + this.atomGrid[0][1][0].toString() + ", " + this.atomGrid[1][1][0].toString() + ", " + this.atomGrid[2][1][0].toString() + "\n" +
                "\t\t\t" + this.atomGrid[0][2][0].toString() + ", " + this.atomGrid[1][2][0].toString() + ", " + this.atomGrid[2][2][0].toString() + "\n" +
                "\t\tMIDDLE:" + "\n" +
                "\t\t\t" + this.atomGrid[0][0][1].toString() + ", " + this.atomGrid[1][0][1].toString() + ", " + this.atomGrid[2][0][1].toString() + "\n" +
                "\t\t\t" + this.atomGrid[0][1][1].toString() + ", " + this.atomGrid[1][1][1].toString() + ", " + this.atomGrid[2][1][1].toString() + "\n" +
                "\t\t\t" + this.atomGrid[0][2][1].toString() + ", " + this.atomGrid[1][2][1].toString() + ", " + this.atomGrid[2][2][1].toString() + "\n" +
                "\t\tBACK:" + "\n" +
                "\t\t\t" + this.atomGrid[0][0][2].toString() + ", " + this.atomGrid[1][0][2].toString() + ", " + this.atomGrid[2][0][2].toString() + "\n" +
                "\t\t\t" + this.atomGrid[0][1][2].toString() + ", " + this.atomGrid[1][1][2].toString() + ", " + this.atomGrid[2][1][2].toString() + "\n" +
                "\t\t\t" + this.atomGrid[0][2][2].toString() + ", " + this.atomGrid[1][2][2].toString() + ", " + this.atomGrid[2][2][2].toString() + "\n" +
                "\n}\n";
    }
}
