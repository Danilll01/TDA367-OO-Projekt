package com.mygdx.chalmersdefense.model.viruses;


import com.mygdx.chalmersdefense.model.path.Path;
import com.mygdx.chalmersdefense.model.path.PathFactory;


/**
 * @author Joel Båtsman Hilmersson
 * A factory class for creating different viruses
 */
public abstract class VirusFactory {

    private static final Path path = PathFactory.createClassicPath(); // Pointer to the path object

    /**
     * Creates a Virus with one health
     *
     * @return The new Virus object
     */
    static public IVirus createVirusOne() {
        return new StandardVirus(1, path);
    }

    /**
     * Creates a Virus with two health
     *
     * @return The new Virus object
     */
    static public IVirus createVirusTwo() {
        return new StandardVirus(2, path);
    }

    /**
     * Creates a Virus with three health
     *
     * @return The new Virus object
     */
    static public IVirus createVirusThree() {
        return new StandardVirus(3, path);
    }

    /**
     * Creates a Virus with four health
     *
     * @return The new Virus object
     */
    static public IVirus createVirusFour() {
        return new StandardVirus(4, path);
    }

    /**
     * Creates a Virus with five health
     *
     * @return The new Virus object
     */
    static public IVirus createVirusFive() {
        return new StandardVirus(5, path);
    }

    /**
     * Creates a BossVirus
     *
     * @return The new BossVirus object
     */
    static public IVirus createBossVirus(){return new BossVirus(path);}
}
