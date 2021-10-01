package com.mygdx.chalmersdefense.model.towers;

import com.mygdx.chalmersdefense.model.projectiles.IProjectile;
import com.mygdx.chalmersdefense.model.targetMode.ITargetMode;
import com.mygdx.chalmersdefense.model.viruses.IVirus;


import java.util.ArrayList;
import java.util.List;

/**
 * @author Elin Forsberg
 * Class representing the MechTower
 */
public class MechTower extends Tower {


    private final List<ITower> miniTowers = new ArrayList<>();


    private int attackSpeed;
    private int range;
    private List<ITargetMode> targetModes;
    List<ITower> towersToAddList;

    public MechTower(float x, float y, String name, int attackSpeed, int cost, int range, List<ITargetMode> targetModes, List<ITower> towersToAddList) {
        super(x, y, name, attackSpeed, cost, range, targetModes);
        this.attackSpeed = attackSpeed;
        this.targetModes = targetModes;
        this.range = range;
        this.towersToAddList = towersToAddList;
    }

    private List<ITower> createMiniTowers(){
            ITower miniTower1 = new MechMiniTower(this.getX() + 100,this.getY() - 100, attackSpeed, range, targetModes);
            ITower miniTower2 = new MechMiniTower(this.getX() - 100,this.getY() - 100, attackSpeed, range, targetModes);

            miniTowers.add(miniTower1);
            miniTowers.add(miniTower2);
            return miniTowers;
    }

    @Override
    void createProjectile(List<IProjectile> projectileList) {
        // Empty for now, maybe move around robot towers here later?
    }

    @Override
    public void update(List<IProjectile> projectilesList,  float newAngle, boolean hasTarget, List<IVirus> viruses){
        if(this.isPlaced() && miniTowers.isEmpty()){
            List<ITower> miniTowers = createMiniTowers();
            for (ITower miniTower: miniTowers) {
                miniTower.placeTower();
                miniTower.setRectangle();
                miniTower.setGotButton(true);
            }
            towersToAddList.addAll(miniTowers);
        }
        this.setAngle(0);
    }

}
