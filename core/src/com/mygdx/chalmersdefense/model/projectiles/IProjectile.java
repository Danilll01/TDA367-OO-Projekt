package com.mygdx.chalmersdefense.model.projectiles;

import com.mygdx.chalmersdefense.model.IMapObject;

/**
 * @author Elin Forsberg
 * Interface for projectiles
 */
public interface IProjectile extends IMapObject {

    /**
     * Moves the projectile in calculated direction
     */
    void update(boolean hitVirus, int haveHit, float angle);

    /**
     * Return if current projectile can be removed
     *
     * @return if can be removed
     */
    boolean canRemove();

    /**
     * Return if the virus with given hashcode has been hit before
     *
     * @param hashCode of the virus
     * @return if virus have been hit before
     */
    boolean haveHitBefore(int hashCode);
}
