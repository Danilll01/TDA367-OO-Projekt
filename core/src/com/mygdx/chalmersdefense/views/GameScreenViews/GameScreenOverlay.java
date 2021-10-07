package com.mygdx.chalmersdefense.views.GameScreenViews;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.chalmersdefense.controllers.GameScreenController;

import static com.badlogic.gdx.graphics.GL20.*;

/**
 * @author Daniel Persson
 */
public abstract class GameScreenOverlay {
    protected final Stage stage;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    protected GameScreenController gameScreenController;

    public GameScreenOverlay(GameScreenController gameScreenController, Stage stage) {
        this.gameScreenController = gameScreenController;
        this.stage = new Stage(stage.getViewport());
    }

    abstract void initialize();

    /**
     * Renders overlay panel
     */
    public abstract void render();

    /**
     * Hides overlay
     */
    public abstract void hideOverlay();

    public Stage getStage() {
        return stage;
    }

    // Generate gray transparent overlay background
    protected void drawTransparentBackground() {
        Gdx.gl.glEnable(GL_BLEND);
        Gdx.gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(20 / 255F, 20 / 255F, 20 / 255F, 0.6F));
        shapeRenderer.rect(0, 0, stage.getWidth(), stage.getHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL_BLEND);
    }

    protected void createButtons(Image backgroundImage, Button button, Label buttonLabel, int buttonNr, String originPanel) {
        // Offset used to place button in center of left or right part.
        float offsetMulX;
        if (buttonNr == 1) {
            offsetMulX = 1 / 4f;
            gameScreenController.addMainMenuClickListener(button);
        } else {
            offsetMulX = 3 / 4f;
            if (originPanel.equals("WinPanel")) {
                gameScreenController.addWinPanelContinueClickListener(button);
            } else {
                gameScreenController.addLostPanelTryAgainClickListener(button);
            }

        }

        button.setPosition(
                backgroundImage.getX() + (backgroundImage.getWidth() * offsetMulX) - button.getWidth() / 2,
                backgroundImage.getY() + 65);
        buttonLabel.setPosition(button.getWidth() / 2 - buttonLabel.getWidth() / 2, button.getHeight() / 2 - buttonLabel.getHeight() / 2 + 5);
    }
}
