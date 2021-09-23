package com.mygdx.chalmersdefense.Model;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.chalmersdefense.Model.Path.GamePaths.ClassicPath;
import com.mygdx.chalmersdefense.Model.Path.Path;


/**
 * @author Joel Båtsman Hilmersson
 * A factory class for creating different viruses
 */
public abstract class VirusFactory {

    private static final Path path = new ClassicPath();

    static public synchronized Virus createVirusOne(){
        return new Virus(1, path);
    }

    static public synchronized Virus createVirusTwo(){
        return new Virus(2, path);
    }

    static public synchronized Virus createVirusThree(){
        return new Virus(3, path);
    }

    static public synchronized Virus createVirusFour(){
        return new Virus(4, path);
    }

    static public synchronized Virus createVirusFive(){
        return new Virus(5, path);
    }
}
