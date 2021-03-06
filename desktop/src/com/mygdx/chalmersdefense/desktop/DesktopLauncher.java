package com.mygdx.chalmersdefense.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.chalmersdefense.ChalmersDefense;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1920;
        config.height = 1080;
        config.foregroundFPS = 60;
        config.fullscreen = true;
        new LwjglApplication(new ChalmersDefense(), config);
    }
}
