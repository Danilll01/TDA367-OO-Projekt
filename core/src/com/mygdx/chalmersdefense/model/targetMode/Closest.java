package com.mygdx.chalmersdefense.model.targetMode;

import com.mygdx.chalmersdefense.model.viruses.IVirus;
import com.mygdx.chalmersdefense.utilities.Calculate;

import java.util.List;

/**
 * @author Elin Forsberg
 * Finds the virus that is the nearest to the tower
 *
 * 2021-09-24 Modified by Joel Båtsman Hilmersson: Changed class to use ITargetMode interface
 *
 *
 */
class Closest implements ITargetMode{

    @Override
    public IVirus getRightVirus(List<IVirus> virusInRange, float towerX, float towerY) {
        IVirus closestVirus = virusInRange.get(0);   // Need to hold the closes virus
        double closestDistance = Calculate.disBetweenPoints(towerX, towerY, closestVirus.getX(), closestVirus.getY());

        for (IVirus virus : virusInRange){
            double rangeToVirus = Calculate.disBetweenPoints(towerX, towerY, virus.getX(), virus.getY());
            if (rangeToVirus < closestDistance){
                closestDistance = rangeToVirus;
                closestVirus = virus;
            }
        }

        return closestVirus;
    }
}
