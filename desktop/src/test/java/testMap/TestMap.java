package testMap;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.mygdx.chalmersdefense.ChalmersDefense;
import com.mygdx.chalmersdefense.model.Model;
import com.mygdx.chalmersdefense.model.modelUtilities.events.ModelEvents;
import com.mygdx.chalmersdefense.utilities.Preferences;
import com.mygdx.chalmersdefense.utilities.event.EventBus;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Elin Forsberg
 * <p>
 * Test class for map
 */
public class TestMap {
    LwjglApplication app = new LwjglApplication(new ChalmersDefense());
    Preferences preferences = new Preferences();
    Model model = new Model(preferences);
    EventBus eventBus = new EventBus();

    @Before
    public void setUp() {
        eventBus.listenFor(ModelEvents.class, model);
    }

    @Test
    public void testMapProjectileCollision() {
        eventBus.listenFor(ModelEvents.class, model);
        model.dragStart("electro", 0, 0);
        model.dragEnd(190, 640);       // Creates and places smurf
        assertTrue(model.getAllMapObjects().size() > 0);        // To verify Smurf is on the map
        model.startRoundPressed();                                      // Begins to spawn viruses

        while (model.getAllMapObjects().size() > 1) {                     // Loops until one projectile has hit the first virus so the tower is
            eventBus.emit(new ModelEvents(ModelEvents.EventType.UPDATEMODEL)); // the only object left on map
        }

        assertEquals(1, model.getAllMapObjects().size());       // To see if it can kill on virus

        int moneyBeforeMoreSpawn = model.getMoney();

        for (int i = 0; i < 10000; i++) {                                // To test more lines in map
            eventBus.emit(new ModelEvents(ModelEvents.EventType.UPDATEMODEL)); // Loops so the virus can move and tower shoot more
        }

        assertTrue(model.getMoney() > moneyBeforeMoreSpawn);    // To see if tower has hit more virus in the update loop

    }

    @Test
    public void testSellTower() {
        model.dragStart("smurf", 300, 300); // Creates tower
        model.dragEnd(300, 300);

        assertEquals(1, model.getAllMapObjects().size());
        model.sellClickedTower();
        assertEquals(0, model.getAllMapObjects().size());
    }

    @Test
    public void testSellUpgradedTower() {
        model.dragStart("smurf", 300, 300); // Creates tower
        model.dragEnd(300, 300);
        model.upgradeClickedTower();

        assertEquals(1, model.getAllMapObjects().size());
        model.sellClickedTower();
        assertEquals(0, model.getAllMapObjects().size());
    }
}
