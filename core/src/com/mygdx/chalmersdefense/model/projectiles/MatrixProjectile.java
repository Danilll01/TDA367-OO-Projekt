package com.mygdx.chalmersdefense.model.projectiles;

import java.util.List;

/**
 * @author Joel Båtsman Hilmersson
 *
 * Class representing Hackerman / HackerTowers shooting projectile
 */
class MatrixProjectile extends Projectile{

    private final List<IProjectile> projectileList; // The list to add the MatrixArea to

    MatrixProjectile(float x, float y, float angle, int upgradeLevel, List<IProjectile> projectileList) {
        super(7, "hackerProjectile" + upgradeLevel, x, y, angle, Math.min(upgradeLevel - 1, 1));
        this.projectileList = projectileList;
    }

    @Override
    public void virusIsHit(int haveHit, float angle) {
        // -50 because the matrix area is around 100 x 100 in size
        int upgradeLevel = Character.getNumericValue(getSpriteKey().charAt(getSpriteKey().length() - 1));
        projectileList.add(new MatrixArea(getX() - 50, getY() - 50, upgradeLevel));
        super.virusIsHit(haveHit, angle);
    }
}
