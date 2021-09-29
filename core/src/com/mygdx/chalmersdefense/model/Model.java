package com.mygdx.chalmersdefense.model;


import com.mygdx.chalmersdefense.ChalmersDefense;
import com.mygdx.chalmersdefense.model.projectiles.Projectile;
import com.mygdx.chalmersdefense.model.towers.*;
import com.mygdx.chalmersdefense.model.towers.Upgrades;
import com.mygdx.chalmersdefense.utilities.Calculate;
import com.mygdx.chalmersdefense.model.towers.Tower;
import com.mygdx.chalmersdefense.model.towers.TowerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author Joel Båtsman Hilmmersson
 * @author Elin Forsberg
 * @author Daniel Persson
 * @author Jenny Carlsson
 *
 * Class handeling all the models in the game.
 *
 * 2021-09-20 Modified by Elin Forsberg: Added methods to handle towers + collisions
 * 2021-09-20 Modified by Joel Båtsman Hilmersson: Made updateVirus loop syncronized
 * 2021-09-22 Modified by Daniel Persson: Added support for storing a clicked tower and added algorithm for finding what tower is being clicked.
 * 2021-09-24 Modified by Elin Forsberg: Added methods to handle projectiles
 * 2021-09-25 Modified by Joel Båtsman Hilmersson: Added support for round system
 * 2021-09-27 Modified by Elin Forsberg: Added methods to handle different attacks from towers
 * 2021-09-27 Modified by Daniel Persson: Added delegation getters for upgrade title, description and price.
 * 2021-09-28 Modified by Everyone: Moved methods to Map class
 */

public class Model {
    private final ChalmersDefense game;
    private final List<Projectile> projectilesList = Collections.synchronizedList(new ArrayList<>());
    private final Map map = new Map();
    private final SpawnViruses virusSpawner = new SpawnViruses(map.getViruses());
    private final Rounds round = new Rounds(10);    // 10 is temporary

    private final List<Tower> towersList = new ArrayList<>();

    private Tower newTower;
    private Tower clickedTower;


    private final Player player = new Player(100, 3000); //Change staring capital later. Just used for testing right now
    private final Upgrades upgrades = new Upgrades();

    private final List<Virus> allViruses = Collections.synchronizedList(new ArrayList<>());



    /**
     * Constructor of the model class
     * @param game current game session
     */
    public Model(ChalmersDefense game) {
        this.game = game;

    }

    /**
     * Update all the model components
     */
    public void updateModel() {
        map.updateMap();
        checkRoundCompleted();
    }

    private void checkRoundCompleted() {
        if (map.getViruses().isEmpty() && !virusSpawner.isSpawning()) {
            if (game.isUpdating()) {
                player.increaseMoney((100 * (round.getCurrentRound()/2)));
            }
            stopGameUpdate();
            projectilesList.clear();
        }
    }



    private void stopGameUpdate() {
        game.stopModelUpdate();
    }

    private void startGameUpdate() {
        game.startModelUpdate();
    }


    /**
     * Starts spawning viruses based on which is the current round
     */
    public void startRoundPressed() {
        if (!virusSpawner.isSpawning() && map.getViruses().isEmpty()) {
            startGameUpdate();
            round.incrementToNextRound();
            virusSpawner.spawnRound(round.getCurrentRound());
        } else {

            game.changeUpdateSpeed();

        }
    }


    /**
     * Creates a new tower when user starts dragging from a tower button.
     * @param towerName the name of the tower
     * @param x the X-position of the button
     * @param y the Y-position of the button
     */
    public void dragStart(String towerName, int x, int y) {
       map.dragStart(towerName, x, y);
    }

    /**
     * Handles a tower being dragged.
     * Updates the towers position after mouse and check for collision
     * @param buttonWidth The width of the button dragged from
     * @param buttonHeight The height of the button dragged from
     * @param x The X-position of the mouse
     * @param y The Y-position of the mouse
     * @param windowHeight The height of the window
     * @param windowWidth  The width of the window
     */

    public void onDrag(int buttonWidth, int buttonHeight, int x, int y, int windowHeight, int windowWidth) {
        map.onDrag(buttonWidth, buttonHeight, x, y, windowHeight, windowWidth);

    }


    /**
     * Handles when the tower is let go.
     * Checks if tower can be placed on current position.
     * If not: tower is removed
     * if valid: place the tower
     * @param buttonWidth The width of the button dragged from
     * @param buttonHeight The height of the button dragged from
     * @param x The X-position of the mouse
     * @param y The Y-position of the mouse
     * @param windowHeight The height of the window
     */

    public void dragEnd(int buttonWidth, int buttonHeight, int x, int y, int windowHeight) {
        map.dragEnd(buttonWidth, buttonHeight, x, y, windowHeight);
    }


    public void towerClicked(float x, float y) {

        // Algorithm for finding which tower is clicked
        for (Tower tower : towersList) {
            float towerCenterX = tower.getPosX() + tower.getWidth()/2;
            float towerCenterY = tower.getPosY() + tower.getHeight()/2;
            if (Math.sqrt(Math.pow(towerCenterX - x, 2) + Math.pow(towerCenterY - y, 2)) <= tower.getWidth()) {
                clickedTower = tower;
            }
        }
    }

    // Maybe temporary because it sets the object to null.
    public void towerNotClicked() {
        clickedTower = null;
    }

    /**
     * A delegation for getting title of a tower upgrade.
     * @param tower used to get tower name
     * @param upgradeLevel what upgrade to get title of
     * @return a String with towers upgrade title depending on upgrade level.
     */
    public String getTowerUpgradeTitle(Tower tower, int upgradeLevel) {
        return upgrades.getTowerUpgradeTitle(tower, upgradeLevel);
    }


    /**
     * Return the list of projectiles
     * @return list of projectiles
     */
    public List<Projectile> getProjectilesList() {
        return map.getProjectilesList();
    }

    /**
     * A delegation for getting description of a tower upgrade.
     * @param tower used to get tower name
     * @param upgradeLevel what upgrade to get description of
     * @return a String with towers upgrade description depending on upgrade level.
     */
    public String getTowerUpgradeDesc(Tower tower, int upgradeLevel) {
        return upgrades.getTowerUpgradeDesc(tower, upgradeLevel);
    }

    /**
     * A delegation for getting price of a tower upgrade.
     * @param tower used to get tower name
     * @param upgradeLevel what upgrade to get price of
     * @return a String with towers upgrade price depending on upgrade level.
     */
    public Long getTowerUpgradePrice(Tower tower, int upgradeLevel) {
        return upgrades.getTowerUpgradePrice(tower, upgradeLevel);
    }

    /**
     * Delegates upgrade method to upgrade class.
     */
    public void upgradeClickedTower() {
        upgrades.upgradeTower(clickedTower);
    /**
     * Return the current money value
     * @return the money value
     */
    public int getMoney() { return player.getMoney(); }

    /**
     * Returns the lives left of player
     * @return lives left
     */
    public int getLivesLeft() { return player.getLives(); }

    /**
     * Returns the current round
     * @return current round
     */
    public int getCurrentRound() { return round.getCurrentRound(); }

    /**
     * Return the list of towers on map
     * @return The list of towers
     */
    public List<Tower> getTowers() {
        return map.getTowers();
    }

    /**
     * Return the list of viruses on path
     * @return the list of viruses
     */
    public List<Virus> getViruses() {
        return map.getViruses();
    }
}
