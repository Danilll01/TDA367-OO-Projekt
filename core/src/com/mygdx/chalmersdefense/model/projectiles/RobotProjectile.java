package com.mygdx.chalmersdefense.model.projectiles;

/**
 * @author Elin Forsberg
 * Class representing a robot projectile
 */
class RobotProjectile extends Projectile{

    RobotProjectile(float x, float y, float angle, int upgradeLevel) {
        super(5 , "mechaProjectile" + upgradeLevel, x, y, angle);
    }



//    @Override
//    public IProjectile createProjectile(int speed, float x, float y, float angle) {
//        return new RobotProjectile(x, y, angle);
//    }
}
