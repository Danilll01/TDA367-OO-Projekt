package com.mygdx.chalmersdefense.utilities;

import com.mygdx.chalmersdefense.model.IMapObject;

import java.awt.*;


/**
 * @author Joel Båtsman Hilmersson
 * <p>
 * Class for wrapping a java rectangle
 */
public final class PathRectangle implements IMapObject {
    private final Rectangle rectangle = new Rectangle();    // The wrapped rectangle object

    public PathRectangle(float x, float y, float width, float height) {
        rectangle.setRect(x, y, width, height);
    }


    @Override
    public float getX() {
        return (float) rectangle.getX();
    }

    @Override
    public float getY() {
        return (float) rectangle.getY();
    }

    @Override
    public float getHeight() {
        return (float) rectangle.getHeight();
    }

    @Override
    public float getWidth() {
        return (float) rectangle.getWidth();
    }

    @Override
    public String getSpriteKey() {
        throw new IllegalMethodCallException("getSpriteKey should not be called from pathRectangles");
    }

    @Override
    public float getAngle() {
        throw new IllegalMethodCallException("getAngle should not be called from pathRectangles");
    }
}
