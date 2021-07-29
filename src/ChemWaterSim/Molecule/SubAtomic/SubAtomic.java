package ChemWaterSim.Molecule.SubAtomic;

/**
 * This is a class Wrapper for ATOM_TYPE.
 * @author Omar Radwan
 */
public class SubAtomic {
    private String name;
    private ATOM_TYPE type;

    public SubAtomic(String name, ATOM_TYPE type) {
        this.name = name;
        this.type = type;
    }

    public ATOM_TYPE getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "" + type;
    }
}
