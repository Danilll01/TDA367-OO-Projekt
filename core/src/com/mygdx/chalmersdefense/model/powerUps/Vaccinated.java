package com.mygdx.chalmersdefense.model.powerUps;

import com.mygdx.chalmersdefense.model.viruses.IVirus;
import com.mygdx.chalmersdefense.utilities.CountDownTimer;

import java.util.List;

/**
 * @author Joel Båtsman Hilmersson
 *
 * Class representing Vaccinated powerup
 */
public class Vaccinated {

    private final CountDownTimer cooldownTimer = new CountDownTimer(1000);  // Cooldown timer
    private final CountDownTimer activeTimer = new CountDownTimer(380);     // Active timer


    private boolean canBeUsed = true;   // If this power-up can be used at the moment
    private boolean activated = false;   // If this power-up is activated at the moment

    private boolean hurtVirus = false;
    private List<IVirus> viruses;

    /**
     * Activates the power-up if the power-up can be used
     */
    public void activatePowerUp(List<IVirus> allViruses){
        if (canBeUsed) {
            canBeUsed = false;
            activated = true;
            viruses = allViruses;
        }
    }

    private void decreaseLife(List<IVirus> allViruses){
        for (IVirus virus : allViruses){
            virus.decreaseHealth(1);
        }
    }

    /**
     * Decreases the power-up timer cooldowns
     */
    public void decreaseTimer(){

        if (activated && activeTimer.haveReachedZero()){
            activated = false;
            hurtVirus = false;
        }

        if (!canBeUsed && cooldownTimer.haveReachedZero()){
            canBeUsed = true;
        }

        if(activated && activeTimer.getCurrentCountTime() <= 325 && !hurtVirus){
            decreaseLife(viruses);
            hurtVirus = true;

        }

    }


    /**
     * Return the active timer amount
     * @return active timer
     */
    public int getTimer() {
        if(activated && !canBeUsed){
            return (activeTimer.getCurrentCountTime() * 5) / 1000;
        }
        else if(activated){
            return -1;
        }
        else if(!canBeUsed){
            return (cooldownTimer.getCurrentCountTime() * 5) / 1000;
        }
        else{
            return -1;
        }
    }

    /**
     * Returns if the power-up is active at the moment
     * @return Status of power-up
     */
    public boolean isActive() {
        return activated;
    }
}
