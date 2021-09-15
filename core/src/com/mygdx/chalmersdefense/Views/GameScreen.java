package com.mygdx.chalmersdefense.Views;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.chalmersdefense.ChalmersDefense;
import com.mygdx.chalmersdefense.Model.Virus;

public class GameScreen implements Screen {

    private final ChalmersDefense game;
    Viewport viewport;
    Batch batch;

    Virus virus;

    public GameScreen(ChalmersDefense game, Batch batch, Viewport viewport){
        this.game = game;
        this.viewport = viewport;
        this.batch = batch;

        virus = new Virus(1, new Sprite(new Texture("corona_virus_low.png")));
        //virus.setPosition(-300, -150);	// This needs to be fixed with later sprites
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        virus.update();
        virus.getSprite().draw(batch);

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            virus = new Virus(1, new Sprite(new Texture("corona_virus_low.png")));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}