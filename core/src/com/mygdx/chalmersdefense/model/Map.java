package com.mygdx.chalmersdefense.model;

import com.mygdx.chalmersdefense.model.genericMapObjects.IGenericMapObject;
import com.mygdx.chalmersdefense.model.path.IPath;
import com.mygdx.chalmersdefense.model.path.PathFactory;
import com.mygdx.chalmersdefense.model.powerUps.*;
import com.mygdx.chalmersdefense.model.projectiles.IProjectile;
import com.mygdx.chalmersdefense.model.towers.ITower;
import com.mygdx.chalmersdefense.model.towers.TowerFactory;
import com.mygdx.chalmersdefense.model.viruses.IVirus;
import com.mygdx.chalmersdefense.model.modelUtilities.Calculate;
import com.mygdx.chalmersdefense.utilities.RangeCircle;
import com.mygdx.chalmersdefense.model.modelUtilities.PathRectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Joel Båtsman Hilmmersson
 * @author Elin Forsberg
 * @author Daniel Persson
 * @author Jenny Carlsson
 * <p>
 * Class handeling all objects and methods on Map.
 *
 * 2021-10-15 Modified by Elin Forsberg and Joel Båtsman Hilmmersson: Added methods for powerUps
 * 2021-10-22 Modified by Joel Båtsman Hilmmersson: Split big methods into smaller ones
 */
final class Map {
    private ITower newTower;            // Temp helper for when new tower is added
    private ITower clickedTower;        // The current clicked tower
    private final List<ITower> towersList = new ArrayList<>();              // The main tower list
    private final List<IProjectile> projectilesList = new ArrayList<>();    // The main projectile list
    private final List<IVirus> virusesList = new ArrayList<>();             // The main virus list
    private final List<IGenericMapObject> genericObjectsList = new ArrayList<>();    // The main genericObjects list
    private final List<IPowerUp> powerUpList = PowerUpFactory.createPowerUps(towersList, virusesList); // List containing all power-ups

    private final List<ITower> towersToAddList = new ArrayList<>();             // Temporary list for object adding towers to the main list (To avoid concurrent modification issues)
    private final List<IProjectile> projectilesToAddList = new ArrayList<>();   // Temporary list for object adding projectiles to the main list (To avoid concurrent modification issues)
    private final List<IVirus> virusToAddList = new ArrayList<>();             // Temporary list for object adding virus to the main list (To avoid concurrent modification issues)

    private final Player player;                                   // A reference to the Player object in the game
    private final IPath path = PathFactory.createClassicPath();     // Current path

    private boolean isGameLost = false;     // Boolean if game is lost

    private final RangeCircle rangeCircle = new RangeCircle(0,0,0);     // Helper class for showing gray range circle

    Map(Player player) { this.player = player; }

    /**
     * Update all map components
     */
    void updateMap() {
        updateVirus();
        updateTowers();
        updateProjectiles();
        updateRangeCircle();
        updatePowerUps();
        updateGenericObjects();
        addTempListsToMainLists();
    }


    /**
     * Resets all of maps variables
     */
    void resetMap() {
        towersList.clear();
        projectilesList.clear();
        virusesList.clear();
        genericObjectsList.clear();

        resetAllPowerUps();

        clickedTower = null;
        isGameLost = false;

        // Removes range circle
        rangeCircle.setEnumColor(RangeCircle.Color.NONE);
    }

    //Resets all powerUps
    private void resetAllPowerUps() {
        for (IPowerUp powerUp : powerUpList) {
            powerUp.resetPowerUp();
        }
    }

    //Add all temporary list to the mainlist
    private void addTempListsToMainLists() {
        towersList.addAll(towersToAddList);
        projectilesList.addAll(projectilesToAddList);
        virusesList.addAll(virusToAddList);

        towersToAddList.clear();
        projectilesToAddList.clear();
        virusToAddList.clear();
    }

    //Update all the towers
    private void updateTowers() {
        List<ITower> removeTowers = new ArrayList<>();

        for (ITower tower : towersList) {

            List<IVirus> virusInRange = Calculate.getVirusesInRange(tower.getX(), tower.getY(), tower.getRange(), virusesList);

            // Standard values for when virus is out of range
            float newAngle = -1;
            boolean towerHasTarget = false;

            // If there are virus in range, update the new values accordingly
            if (virusInRange.size() > 0) {
                IVirus targetVirus = tower.getCurrentTargetMode().getCorrectVirus(virusInRange, tower.getX(), tower.getY());
                newAngle = Calculate.angleDeg(targetVirus.getX(), targetVirus.getY(), tower.getX(), tower.getY());
                towerHasTarget = true;
            }

            tower.update(projectilesList, newAngle, towerHasTarget);

            if (tower.canRemove() && !tower.equals(newTower)) { removeTowers.add(tower); }
        }

        towersList.removeAll(removeTowers);
    }


    //Update all the viruses
    private void updateVirus() {

        List<IVirus> virusToRemove = new ArrayList<>();

        for (IVirus virus : virusesList) {
            if (virus.getY() > 1130 || virus.isDead()) {
                virusToRemove.add(virus);
            }
            virus.update();
        }

        removeDeadVirusesHandler(virusToRemove);
    }

    private void removeDeadVirusesHandler(List<IVirus> virusToRemove) {
        for (IVirus virus : virusToRemove) {
            try {
                player.decreaseLivesBy(virus.getLifeDecreaseAmount());
            } catch (PlayerLostAllLifeException ignore) {
                isGameLost = true;
            }
            virusesList.remove(virus);
        }
    }

    //Update the projectiles
    private void updateProjectiles() {
        List<IProjectile> removeProjectiles = new ArrayList<>();

        for (IProjectile projectile : projectilesList) {
            projectileUpdateHandler(projectile);
            if (projectile.canRemove() || checkIfOutOfBounds(projectile.getY(), projectile.getX())) {
                removeProjectiles.add(projectile);
            }
        }

        projectilesList.removeAll(removeProjectiles);
    }

    private void projectileUpdateHandler(IProjectile projectile) {
        List<IVirus> virusThatWasHit = new ArrayList<>();

        if (checkCollisionOfProjectiles(projectile, virusThatWasHit)) {
            float angle = getAngleToVirus(projectile, virusThatWasHit);
            projectile.update(true, virusThatWasHit.get(0).hashCode(), angle);
        } else {
            projectile.update(false, -1, -1);
        }
    }

    //Checks if projectile collided with path, then virus
    private boolean checkCollisionOfProjectiles(IProjectile projectile, List<IVirus> virusThatWasHit) {
        for (PathRectangle rectangle : path.getCollisionRectangles()) {
            if (Calculate.objectsIntersects(projectile, rectangle)) {
                return checkVirusAndProjectileCollision(projectile, virusThatWasHit);
            }
        }
        return false;
    }

    //Helper method for collision between virus and projectile
    private boolean checkVirusAndProjectileCollision(IProjectile projectile, List<IVirus> virusThatWasHit) {

        for (IVirus virus : virusesList) {
            if (Calculate.objectsIntersects(projectile, virus) && !projectile.haveHitBefore(virus.hashCode())) {
                virusAndProjectileHitHandler(projectile, virusThatWasHit, virus);
                return true;
            }
        }

        return false;
    }

    private void virusAndProjectileHitHandler(IProjectile projectile, List<IVirus> virusThatWasHit, IVirus virus) {
        int virusHealthBefore = virus.getLifeDecreaseAmount();

        virus.decreaseHealth(projectile.getDamageAmount());
        player.increaseMoney(virusHealthBefore - virus.getLifeDecreaseAmount());    // This will add the correct amount of money to the player relative to the amount of damage done
        virusThatWasHit.add(virus);
    }

    //Get angle to virus in range of projectile
    private float getAngleToVirus(IProjectile projectile, List<IVirus> removeList) {
        List<IVirus> virusInRange = getTargetableViruses(projectile,removeList);

        if (virusInRange.size() > 0) {
            IVirus virus = virusInRange.get(0);
            return Calculate.angleDeg(virus.getX(), virus.getY(), projectile.getX(), projectile.getY());
        }
        return -1;
    }

    //Get the targetable viruses in range on projectile
    private List<IVirus> getTargetableViruses(IProjectile projectile, List<IVirus> removeList){
        List<IVirus> virusInRange = Calculate.getVirusesInRange(projectile.getX(), projectile.getY(), 250, virusesList);

        for (IVirus virus : virusInRange) {
            if (projectile.haveHitBefore(virus.hashCode())) {
                removeList.add(virus);
            }
        }

        virusInRange.removeAll(removeList);
        return virusInRange;
    }

    //Updates the rangeCircle
    private void updateRangeCircle() {
        if(clickedTower != null){
            rangeCircle.updatePos(clickedTower.getX() + clickedTower.getWidth()/2,clickedTower.getY() + clickedTower.getHeight()/2,clickedTower.getRange());
        }
    }

    //Update all the genericObjects
    private void updateGenericObjects(){
        List<IGenericMapObject> removeList = new ArrayList<>();

        for (IGenericMapObject object : genericObjectsList) {
            object.update();
            if(object.canRemove()){ removeList.add(object); }
        }

        genericObjectsList.removeAll(removeList);
    }

    //Updates the powerUps
    private void updatePowerUps() {
        for (IPowerUp powerUp : powerUpList){
            powerUp.decreaseTimer();
        }

        if (powerUpList.get(0).getIsActive()){
            for (int i = 0; i < 3; i++) { updateTowers(); }
        }
    }


    /**
     * Returns the timers for all power-ups
     * @return Array with all timers in it
     */
    int[] getPowerUpTimers(){
        int[] timers = new int[powerUpList.size()];

        for (int i = 0; i < powerUpList.size(); i++){
            timers[i] = powerUpList.get(i).getTimer();
        }

        return timers;
    }

    /**
     * Returns a list with the active status of all power-ups
     * @return Array with current active status of power-ups
     */
    boolean[] getPowerUpActiveStatus(){
        boolean[] powerUpsActive = new boolean[powerUpList.size()];

        for (int i = 0; i < powerUpList.size(); i++){
            powerUpsActive[i] = powerUpList.get(i).getIsActive();
        }

        return powerUpsActive;
    }


    //Check if coordinates are outside the screen
    private boolean checkIfOutOfBounds(float y, float x) {
        if (y > 1130 || -50 > y) {
            return true;
        }
        return x > 1970 || -50 > x;
    }


    //Checks if a tower collides with path
    private boolean checkMapAndTowerCollision(ITower tower) {
        for (PathRectangle rect : path.getCollisionRectangles()) {
            if (Calculate.objectsIntersects(tower, rect)) {
                return true;
            }
        }
        return false;
    }

    //Checks if towers collide with anything
    private boolean checkCollisionOfTower(ITower tower, int windowHeight, int windowWidth) {
        for (ITower checkTower : towersList) {
            //Check if tower collides with a placed tower
            if (Calculate.objectsIntersects(tower, checkTower) && !(checkTower.hashCode() == tower.hashCode())) {
                return true;
            }
            //Check if tower out of bound on X
            else if (!(0 <= (tower.getX())) || (windowWidth - 340 < (tower.getX() + tower.getWidth() / 2))) {
                return true;
            }
            //Check if tower out of bound on Y
            else if (!(windowHeight - 950 < (tower.getY() - tower.getHeight() / 2)) || (windowHeight < (tower.getY()) + tower.getHeight())) {
                return true;
            }
            //check if tower collide with path
            else if (checkMapAndTowerCollision(tower)) {
                return true;
            }

        }
        return false;

    }

    /**
     * Creates a new tower when user starts dragging from a tower button.
     *
     * @param towerName the name of the tower
     * @param x         the X-position of the button
     * @param y         the Y-position of the button
     */
    void dragStart(String towerName, float x, float y) {
        switch (towerName) {
            case "smurf" -> newTower = TowerFactory.createSmurf(x, y);
            case "chemist" -> newTower = TowerFactory.createChemist(x, y, projectilesToAddList);
            case "electro" -> newTower = TowerFactory.createElectro(x, y);
            case "hacker" -> newTower = TowerFactory.createHacker(x, y, projectilesToAddList);
            case "mech" -> newTower = TowerFactory.createMech(x, y, towersToAddList, Collections.unmodifiableList(towersList), path.getCollisionRectangles());
            case "eco" -> newTower = TowerFactory.createEco(x, y, player);
            default -> {
                return;
            }
        }

        towersList.add(newTower);
        clickedTower = newTower;

    }


    /**
     * Handles a tower being dragged.
     * Updates the towers position after mouse and check for collision
     *  @param x            The X-position of the mouse
     * @param y            The Y-position of the mouse
     * @param windowHeight The height of the window
     * @param windowWidth  The width of the window
     */
    void onDrag(float x, float y, int windowHeight, int windowWidth) {

        newTower.setPos(x - newTower.getWidth() / 2f, y - newTower.getHeight() / 2f);

        if (!checkCollisionOfTower(newTower, windowHeight, windowWidth) && (player.getMoney() >= newTower.getCost())) {
            newTower.setIfCanRemove(false);
            rangeCircle.updatePos(newTower.getX() + newTower.getWidth() / 2, newTower.getY() + newTower.getHeight() / 2, newTower.getRange());
            rangeCircle.setEnumColor(RangeCircle.Color.GRAY);

        } else {
            newTower.setIfCanRemove(true);
            rangeCircle.updatePos(newTower.getX() + newTower.getWidth() / 2, newTower.getY() + newTower.getHeight() / 2, newTower.getRange());
            rangeCircle.setEnumColor(RangeCircle.Color.RED);
        }
    }


    /**
     * Handles when the tower is let go.
     * Checks if tower can be placed on current position.
     * If not: tower is removed
     * if valid: place the tower
     *  @param x            The X-position of the mouse
     * @param y            The Y-position of the mouse
     */
    void dragEnd(float x, float y) {
        if (!newTower.canRemove()) {
            newTower.placeTower();
            newTower.setPos(x - newTower.getWidth() / 2f, y - newTower.getHeight() / 2f);
            player.decreaseMoney(newTower.getCost());
        } else {
            towersList.remove(newTower);
            rangeCircle.setEnumColor(RangeCircle.Color.NONE);
            clickedTower = null;
        }
    }


    /**
     * Handles when a placed tower is clicked
     */
    void checkIfTowerClicked(float x, float y) {
        // Algorithm for finding which tower is clicked
        ITower towerWasClicked = null;
        for (ITower tower : towersList) {
            float towerCenterX = tower.getX() + tower.getWidth() / 2;
            float towerCenterY = tower.getY() + tower.getHeight() / 2;


            if (Math.sqrt(Math.pow(towerCenterX - x, 2) + Math.pow(towerCenterY - y, 2)) <= tower.getWidth()) {
                towerWasClicked = tower;
                rangeCircle.updatePos(towerCenterX, towerCenterY, tower.getRange());
                rangeCircle.setEnumColor(RangeCircle.Color.GRAY);
            }
        }

        if (towerWasClicked == null) {
            rangeCircle.setEnumColor(RangeCircle.Color.NONE);
        }
        clickedTower = towerWasClicked;

    }

    /**
     * Upgrades clicked tower if player has enough money
     */
    void upgradeClickedTower() {
        // If upgrade is applied decrease player money
        if (Upgrades.upgradeTower(clickedTower)) {
            player.decreaseMoney(Upgrades.getTowerUpgradePrice(clickedTower.getName(), clickedTower.getUpgradeLevel() - 1));

            rangeCircle.updatePos(clickedTower.getX() + getClickedTower().getWidth()/2, clickedTower.getY() + getClickedTower().getHeight()/2, clickedTower.getRange());
        }
    }

    /**
     * Sell the clicked tower
     * Also gives the player some money back
     *
     * @param cost cost of tower sold
     */
    void sellClickedTower(int cost) {
        towersList.remove(clickedTower);
        player.increaseMoney(cost);
        clickedTower = null;
        rangeCircle.setEnumColor(RangeCircle.Color.NONE);
    }

    /**
     * Change the targetMode of the clicked tower
     */
    void changeTargetMode(boolean goRight){
        clickedTower.changeTargetMode(goRight);
    }


    /**
     * Return the circle used for rendering range
     * @return the circle
     */
    RangeCircle getRangeCircle() {
        return rangeCircle;
    }

    /**
     * Returns currently clicked tower
     *
     * @return tower object of clicked tower
     */
    ITower getClickedTower() {
        return clickedTower;
    }

    /**
     * Returns currently clicked towers target mode
     *
     * @return target mode of clicked tower
     */
    String getClickedTowerTargetMode() {
        String[] targetModeNameSplit = clickedTower.getCurrentTargetMode().getClass().getName().split("[.]");
        return targetModeNameSplit[targetModeNameSplit.length - 1];
    }

    /**
     * Returns if game has been lost
     *
     * @return a boolean for game lost status
     */
    boolean getIsGameLost() {
        return isGameLost;
    }


    /**
     * Returns the list to add viruses to
     * @return list of viruses
     */
    List<IVirus> getVirusesToAddList() {
        return virusToAddList;
    }

    //TODO Remove when not needed
    List<IVirus> getViruses() {
        return virusesList;
    }
    /**
     * Return the list of objects on map
     *
     * @return the list of objects
     */
    List<IMapObject> getAllMapObjects() {
        List<IMapObject> allMapObjects = new ArrayList<>();
        allMapObjects.addAll(towersList);
        allMapObjects.addAll(virusesList);
        allMapObjects.addAll(projectilesList);
        allMapObjects.addAll(genericObjectsList);
        return allMapObjects;
    }

    /**
     * Returns if virus list is empty
     *
     * @return true - if all viruses are cleared, false - if there are viruses left
     */
    boolean isVirusCleared() {
        return virusesList.isEmpty();
    }

    /**
     * Returns the background image path to the image
     */
    String getMapImagePath(){ return path.getImagePath(); }

    /**
     * Method to call when round is cleared, makes map ready for next round
     */
    void roundClear() {
        projectilesList.clear();
        genericObjectsList.clear();
        resetAllPowerUps();
        updateRangeCircle();
    }


    /**
     * Method to handle a powerUp button being clicked. Also checks if player have enough cost to buy powerup-
     * @param powerUpName name of the button that was clicked
     */
    void powerUpClicked(String powerUpName) {
        IPowerUp powerUp = switch (powerUpName) {
            case "cleanHands" -> powerUpList.get(0);
            case "maskedUp"   -> powerUpList.get(1);
            case "vaccinated" -> powerUpList.get(2);
            default -> throw new IllegalStateException(); // TODO Custom exception???
        };

        handlePowerUpClicked(powerUp);
    }

    private void handlePowerUpClicked(IPowerUp powerUp) {
        if ((player.getMoney() >= powerUp.getCost()) && !powerUp.getIsActive() && powerUp.getTimer() == -1) {
            powerUp.powerUpClicked(genericObjectsList);
            player.decreaseMoney(powerUp.getCost());
        }
    }


}
