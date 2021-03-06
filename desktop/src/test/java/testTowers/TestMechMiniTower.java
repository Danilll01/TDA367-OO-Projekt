package testTowers;

import com.mygdx.chalmersdefense.model.path.IPath;
import com.mygdx.chalmersdefense.model.path.PathFactory;
import com.mygdx.chalmersdefense.model.projectiles.IProjectile;
import com.mygdx.chalmersdefense.model.towers.ITower;
import com.mygdx.chalmersdefense.model.towers.TowerFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Joel Båtsman Hilmersson
 * <p>
 * 2021-10-11 Modified by Elin Forsberg: Changed methods to work with new MechoMan class and added tests
 */
public class TestMechMiniTower {
    IPath path = PathFactory.createClassicPath();
    List<ITower> addToList = new ArrayList<>();
    List<ITower> towersList = new ArrayList<>();

    @Test
    public void testCreateProjectile() {
        List<ITower> addToList = new ArrayList<>();
        ITower mech = TowerFactory.createMech(0, 0, addToList, towersList, path.getCollisionRectangles());
        List<IProjectile> pList = new ArrayList<>();


        mech.placeTower();

        while (addToList.size() == 0) {
            mech.update(new ArrayList<>(), 10, true);
        }

        for (ITower tower : addToList) {
            tower.update(pList, 10, true);
        }

        assertTrue(pList.size() > 0);
    }

    @Test
    public void testUpgradeTower() {
        HashMap<String, Double> upgrades = new HashMap<>();
        upgrades.put("attackSpeedMul", 0.2);
        upgrades.put("attackRangeMul", 2.0);
        ITower mech = TowerFactory.createMech(0, 0, addToList, towersList, path.getCollisionRectangles());

        mech.placeTower();
        mech.update(new ArrayList<>(), 10, true);

        for (ITower tower : addToList) {
            tower.upgradeTower(upgrades);
            assertEquals(2, tower.getUpgradeLevel());
        }


    }
}
