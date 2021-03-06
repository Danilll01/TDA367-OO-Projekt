package com.mygdx.chalmersdefense.model.path;

import com.mygdx.chalmersdefense.model.modelUtilities.PathRectangle;
import com.mygdx.chalmersdefense.model.modelUtilities.PositionVector;

import java.util.List;

/**
 * @author Elin Forsberg
 * Interface for paths
 */
public interface IPath {

    /**
     * Returns the background image path to the image
     *
     * @return the image path
     */
    String getImagePath();

    /**
     * Returns waypoint of given index
     *
     * @param index to get waypoint of
     * @return waypoint
     */
    PositionVector getWaypoint(int index);

    /**
     * Gets the list of rectangles used for collision
     *
     * @return list of rectangles
     */
    List<PathRectangle> getCollisionRectangles();
}
