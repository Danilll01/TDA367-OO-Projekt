package com.mygdx.chalmersdefense.model.towers;

import com.mygdx.chalmersdefense.model.projectiles.IProjectile;
import com.mygdx.chalmersdefense.model.projectiles.ProjectileFactory;
import com.mygdx.chalmersdefense.model.targetMode.ITargetMode;
import com.mygdx.chalmersdefense.utilities.Calculate;
import com.mygdx.chalmersdefense.utilities.CountDownTimer;
import com.mygdx.chalmersdefense.utilities.PathRectangle;

import java.util.List;

/**
 * @author Elin Forsberg
 * Class representing the MechTower
 *
 * 2021-10-14 Modified by Joel Båtsman Hilmersson: MechTower now only spawns MinimechTowers and do not remove them <br>
 */
final class MechTower extends Tower {

    private final int reloadSpeed;      // Reload speed
    private final List<ITower> towersToAddList;       // The list to add towers to get them to show up on the map
    private final List<ITower> allTowers;           // The list of all towers on the map
    private final List<PathRectangle> pathRectangles;  // The list of all pathRectangles on current map

    private final CountDownTimer robotCoolDownTimer = new CountDownTimer(1500,0); // Timer for robot spawning cooldown


    MechTower(float x, float y, String name, int reloadSpeed, int cost, int range, List<ITower> towersToAddList, List<ITower> allTowers,  List<PathRectangle> pathRectangles) {
        super(x, y, name, reloadSpeed, cost, range);
        this.reloadSpeed = reloadSpeed;
        this.towersToAddList = towersToAddList;

        this.allTowers = allTowers;
        this.pathRectangles = pathRectangles;
    }


    @Override
    void createProjectile(List<IProjectile> projectileList){
        projectileList.add(ProjectileFactory.createWrenchProjectile(super.getX(), super.getY(), getAngle()));

    }

    @Override
    public void update(List<IProjectile> projectilesList, float newAngle, boolean hasTarget) {

        spawnMiniTowers();

        if(getUpgradeLevel() == 3){
            super.update(projectilesList, newAngle, hasTarget);
        }
        else {
            this.setAngle(0);
        }

    }

    private void spawnMiniTowers(){
        if (this.isPlaced() && robotCoolDownTimer.haveReachedZero()){
            createMiniTowers();
        }
    }

    private void createMiniTowers() {
        float[] point1 = checkPointCollision();
        float[] point2 = checkPointCollision();

        if(getUpgradeLevel() == 1 || getUpgradeLevel() == 3){
            createOneTower(point1);
        }

        if(getUpgradeLevel() == 2){
            createTwoTowers(point1, point2);
        }
    }

    private void createOneTower(float[] point1) {
        if(point1[0] != -1){
            ITower miniTower1 = new MechMiniTower(point1[0], point1[1], reloadSpeed, getRange(), getCurrentTargetModeIndex(), super.getUpgradeLevel());

            towersToAddList.add(miniTower1);

            miniTower1.placeTower();
        }
    }

    private void createTwoTowers(float[] point1, float[] point2) {
        if(point1[0] != -1 && point2[0] != -1){
            ITower miniTower1 = new MechMiniTower(point1[0], point1[1], reloadSpeed, getRange(), getCurrentTargetModeIndex(), super.getUpgradeLevel());
            ITower miniTower2 = new MechMiniTower(point2[0], point2[1], reloadSpeed, getRange(), getCurrentTargetModeIndex(), super.getUpgradeLevel());

            towersToAddList.add(miniTower1);
            towersToAddList.add(miniTower2);

            miniTower1.placeTower();
            miniTower2.placeTower();
        }
    }



    private float[] checkPointCollision() {
        float[] point = Calculate.randPoint(getX(), getY(), getRange());
        for (int i = 0; i < 100; i++) {
            if(pathCollision(this.getWidth(),this.getHeight(), point[0],point[1]) || towerCollision(this.getWidth(),this.getHeight(), point[0],point[1]) || checkIfOutOfBounds(point[0],point[1])){
                point = Calculate.randPoint(getX(), getY(), getRange());
            }
            else{
                return point;
            }
        }

        return new float[]{-1, -1};
    }

    private boolean pathCollision(double width, double height,double x, double y){
        for (PathRectangle rectangle : pathRectangles) {
            if (Calculate.calculateIntersects(width , height, rectangle.getWidth(), rectangle.getHeight(), x, y, rectangle.getX(),rectangle.getY())) {
                return true;
            }
        }
        return false;
    }

    private boolean towerCollision(double width, double height,double x, double y){
        for (ITower tower : allTowers) {
            if (Calculate.calculateIntersects(width , height, tower.getWidth(), tower.getHeight(), x, y, tower.getX(),tower.getY())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfOutOfBounds(float y, float x) {
        if (y > 1130 || -50 > y) {
            return true;
        }
        return x > 1970 || -50 > x;
    }

}
