package com.mygdx.chalmersdefense.views;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;


import java.util.HashMap;

/**
 * @author Daniel Persson
 * Class respresenting an AbstractScreen
 * <p>
 * 2021-09-23 Modified by Joel Båtsman Hilmersson: Created Hashmap with sprites <br>
 * 2021-10-19 Modified by Joel Båtsman Hilmersson: Added input multiplexer support <br>
 */
public abstract class AbstractScreen extends Stage implements Screen {

    final HashMap<String, Sprite> spriteMap = new HashMap<>();       // HashMap for containing all game sprites
    final HashMap<String, Sprite> largeSpriteMap = new HashMap<>();  // HashMap for containing all large game sprites

    final Batch batch = new SpriteBatch();                           // SpriteBatch to use when rendering sprites

    private final InputMultiplexer multiplexer = new InputMultiplexer(); // Used to get inputs from multiple different stages

    /**
     * Creates the foundation for a screen
     */
    AbstractScreen() {
        super(new FitViewport(1920, 1080, new OrthographicCamera(1920, 1080)));
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        createSprites();
        createLargeSprites();
    }

    /**
     * Adds processor to multiplexer
     *
     * @param newProcessor supplied processor
     */
    void addToMultiplexer(InputProcessor newProcessor) {
        multiplexer.addProcessor(newProcessor);
    }

    /**
     * Sets background image of screen
     */
    abstract void setBackgroundImage();

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(this.getCamera().combined); // Renders based on window pixels and not screen pixels.

        super.act(delta);
        super.draw();

        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }


    //Put all sprites in a HashMap to optimize render time
    private void createSprites() {
        spriteMap.put("IT-Smurf1", new Sprite(new Texture("towers/IT-Smurf/IT-Smurf1.png")));
        spriteMap.put("Chemist1", new Sprite(new Texture("towers/Chemist/Chemist1.png")));
        spriteMap.put("Hackerman1", new Sprite(new Texture("towers/Hackerman/Hackerman1.png")));
        spriteMap.put("Electroman1", new Sprite(new Texture("towers/Electroman/Electroman1.png")));
        spriteMap.put("Mechoman1", new Sprite(new Texture("towers/Mechoman/Mechoman1.png")));
        spriteMap.put("Economist1", new Sprite(new Texture("towers/Economist/Economist1.png")));


        spriteMap.put("IT-Smurf2", new Sprite(new Texture("towers/IT-Smurf/IT-Smurf2.png")));
        spriteMap.put("Chemist2", new Sprite(new Texture("towers/Chemist/Chemist2.png")));
        spriteMap.put("Hackerman2", new Sprite(new Texture("towers/Hackerman/Hackerman2.png")));
        spriteMap.put("Electroman2", new Sprite(new Texture("towers/Electroman/Electroman2.png")));
        spriteMap.put("Mechoman2", new Sprite(new Texture("towers/Mechoman/Mechoman1.png")));
        spriteMap.put("Economist2", new Sprite(new Texture("towers/Economist/Economist2.png")));
        spriteMap.put("MechMini2", new Sprite(new Texture("towers/MechMini/MechMini1.png")));

        spriteMap.put("IT-Smurf3", new Sprite(new Texture("towers/IT-Smurf/IT-Smurf3.png")));
        spriteMap.put("Chemist3", new Sprite(new Texture("towers/Chemist/Chemist3.png")));
        spriteMap.put("Hackerman3", new Sprite(new Texture("towers/Hackerman/Hackerman3.png")));
        spriteMap.put("Electroman3", new Sprite(new Texture("towers/Electroman/Electroman3.png")));
        spriteMap.put("Mechoman3", new Sprite(new Texture("towers/Mechoman/Mechoman1.png")));
        spriteMap.put("Economist3", new Sprite(new Texture("towers/Economist/Economist3.png")));
        spriteMap.put("MechMini3", new Sprite(new Texture("towers/MechMini/MechMini3.png")));

        spriteMap.put("virus1", new Sprite(new Texture("viruses/virus1Hp.png")));
        spriteMap.put("virus2", new Sprite(new Texture("viruses/virus2Hp.png")));
        spriteMap.put("virus3", new Sprite(new Texture("viruses/virus3Hp.png")));
        spriteMap.put("virus4", new Sprite(new Texture("viruses/virus4Hp.png")));
        spriteMap.put("virus5", new Sprite(new Texture("viruses/virus5Hp.png")));
        spriteMap.put("virus50", new Sprite(new Texture("viruses/virus50Hp.png")));


        spriteMap.put("vaccinationStorm", new Sprite(new Texture("genericMapObjects/vaccinationStorm.png")));
        spriteMap.put("bubbles", new Sprite(new Texture("genericMapObjects/bubbles.png")));
        spriteMap.put("maskedUpSmurf", new Sprite(new Texture("genericMapObjects/maskedUpSmurf.png")));
        spriteMap.put("happyMask", new Sprite(new Texture("genericMapObjects/happyMask.png")));


        spriteMap.put("smurfProjectile1", new Sprite(new Texture("projectiles/smurfProjectile1.png")));
        spriteMap.put("smurfProjectile2", new Sprite(new Texture("projectiles/smurfProjectile2.png")));
        spriteMap.put("smurfProjectile3", new Sprite(new Texture("projectiles/smurfProjectile3.png")));
        spriteMap.put("hackerProjectile1", new Sprite(new Texture("projectiles/hackerProjectile1.png")));
        spriteMap.put("hackerProjectile2", new Sprite(new Texture("projectiles/hackerProjectile2.png")));
        spriteMap.put("hackerProjectile3", new Sprite(new Texture("projectiles/hackerProjectile3.png")));
        spriteMap.put("hackerArea1", new Sprite(new Texture("projectiles/hackerArea1.png")));
        spriteMap.put("hackerArea2", new Sprite(new Texture("projectiles/hackerArea2.png")));
        spriteMap.put("hackerArea3", new Sprite(new Texture("projectiles/hackerArea3.png")));
        spriteMap.put("electroProjectile1", new Sprite(new Texture("projectiles/electroProjectile1.png")));
        spriteMap.put("electroProjectile2", new Sprite(new Texture("projectiles/electroProjectile2.png")));
        spriteMap.put("electroProjectile3", new Sprite(new Texture("projectiles/electroProjectile3.png")));
        spriteMap.put("chemistProjectile1", new Sprite(new Texture("projectiles/chemistProjectile1.png")));
        spriteMap.put("chemistProjectile2", new Sprite(new Texture("projectiles/chemistProjectile2.png")));
        spriteMap.put("chemistProjectile3", new Sprite(new Texture("projectiles/chemistProjectile3.png")));
        spriteMap.put("chemistAcid1", new Sprite(new Texture("projectiles/chemistAcid1.png")));
        spriteMap.put("chemistAcid2", new Sprite(new Texture("projectiles/chemistAcid2.png")));
        spriteMap.put("chemistAcid3", new Sprite(new Texture("projectiles/chemistAcid3.png")));
        spriteMap.put("mechaProjectile1", new Sprite(new Texture("projectiles/mechaProjectile1.png")));
        spriteMap.put("mechaProjectile2", new Sprite(new Texture("projectiles/mechaProjectile2.png")));
        spriteMap.put("mechaProjectile3", new Sprite(new Texture("projectiles/mechaProjectile3.png")));
        spriteMap.put("wrenchProjectile", new Sprite(new Texture("projectiles/wrenchProjectile.png")));
        spriteMap.put("money1", new Sprite(new Texture("projectiles/money1.png")));
        spriteMap.put("money2", new Sprite(new Texture("projectiles/money2.png")));
        spriteMap.put("money3", new Sprite(new Texture("projectiles/money3.png")));
    }


    //Put all sprites in a HashMap to optimize render time
    private void createLargeSprites() {
        largeSpriteMap.put("IT-Smurf1Large", new Sprite(new Texture("towers/IT-Smurf/IT-Smurf1Large.png")));
        largeSpriteMap.put("Chemist1Large", new Sprite(new Texture("towers/Chemist/Chemist1Large.png")));
        largeSpriteMap.put("Hackerman1Large", new Sprite(new Texture("towers/Hackerman/Hackerman1Large.png")));
        largeSpriteMap.put("Electroman1Large", new Sprite(new Texture("towers/Electroman/Electroman1Large.png")));
        largeSpriteMap.put("Mechoman1Large", new Sprite(new Texture("towers/Mechoman/Mechoman1Large.png")));
        largeSpriteMap.put("MechMini1Large", new Sprite(new Texture("towers/MechMini/MechMini1Large.png")));
        largeSpriteMap.put("Economist1Large", new Sprite(new Texture("towers/Economist/Economist1Large.png")));

        largeSpriteMap.put("IT-Smurf2Large", new Sprite(new Texture("towers/IT-Smurf/IT-Smurf2Large.png")));
        largeSpriteMap.put("Chemist2Large", new Sprite(new Texture("towers/Chemist/Chemist2Large.png")));
        largeSpriteMap.put("Hackerman2Large", new Sprite(new Texture("towers/Hackerman/Hackerman2Large.png")));
        largeSpriteMap.put("Electroman2Large", new Sprite(new Texture("towers/Electroman/Electroman2Large.png")));
        largeSpriteMap.put("Mechoman2Large", new Sprite(new Texture("towers/Mechoman/Mechoman2Large.png")));
        largeSpriteMap.put("MechMini2Large", new Sprite(new Texture("towers/MechMini/MechMini1Large.png")));
        largeSpriteMap.put("Economist2Large", new Sprite(new Texture("towers/Economist/Economist2Large.png")));

        largeSpriteMap.put("IT-Smurf3Large", new Sprite(new Texture("towers/IT-Smurf/IT-Smurf3Large.png")));
        largeSpriteMap.put("Chemist3Large", new Sprite(new Texture("towers/Chemist/Chemist3Large.png")));
        largeSpriteMap.put("Hackerman3Large", new Sprite(new Texture("towers/Hackerman/Hackerman3Large.png")));
        largeSpriteMap.put("Electroman3Large", new Sprite(new Texture("towers/Electroman/Electroman3Large.png")));
        largeSpriteMap.put("Mechoman3Large", new Sprite(new Texture("towers/Mechoman/Mechoman3Large.png")));
        largeSpriteMap.put("MechMini3Large", new Sprite(new Texture("towers/MechMini/MechMini3Large.png")));
        largeSpriteMap.put("Economist3Large", new Sprite(new Texture("towers/Economist/Economist3Large.png")));
    }
}

