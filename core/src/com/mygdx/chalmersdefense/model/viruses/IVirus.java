package com.mygdx.chalmersdefense.model.viruses;

import com.mygdx.chalmersdefense.model.IMapObject;

/**
 * @author Joel Båtsman Hilmersson
 */
public interface IVirus extends IMapObject {

    void  update();

    boolean isDead();

    int getLifeDecreaseAmount();

    float getTotalDistanceTraveled();

    void decreaseHealth();

}
