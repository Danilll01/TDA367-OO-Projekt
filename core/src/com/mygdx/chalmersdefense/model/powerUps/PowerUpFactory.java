package com.mygdx.chalmersdefense.model.powerUps;


import com.mygdx.chalmersdefense.model.viruses.IVirus;

import java.util.List;

/**
 * @author Elin Forsberg
 * Factory for power-ups
 * <p>
 * 2021-10-15 Modified by Joel Båtsman Hilmersson: Factory now only returns a list <br>
 */
public abstract class PowerUpFactory {

    /**
     * Create a powerUp of the type MaskedUp
     *
     * @return new MaskedUp object
     */
    private static IPowerUp createMaskedUpPowerUp() {
        return new MaskedUp();
    }

    /**
     * Create a powerUp of the type CleanHands
     *
     * @return new CleanHands object
     */
    private static IPowerUp createCleanHandsPowerUp() {
        return new CleanHands();
    }

    /**
     * Create a powerUp of the type Vaccinated
     *
     * @param viruses viruses to hurt
     * @return new Vaccinated object
     */
    private static IPowerUp createVaccinatedPowerUp(List<IVirus> viruses) {
        return new Vaccinated(viruses);
    }

    /**
     * Creates a list of all different power-ups
     *
     * @param viruses viruses to hurt
     * @return the list with all power-ups
     */
    public static List<IPowerUp> createPowerUps(List<IVirus> viruses) {
        return List.of(createCleanHandsPowerUp(), createMaskedUpPowerUp(), createVaccinatedPowerUp(viruses));
    }

}
