package com.armadillo.game.desktop;

import com.armadillo.game.controller.ArmadilloController;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		/**
		 * TODO: weird thing with the exit code.
		 * an exit code of -1 seemed to indicate a memory leak. However, when I generated a clean
		 * project it still returned this exit code. Considering that a clean project does not
		 * autogenerate a project with memory leaks, I do not believe there is a memory leak.
		 *
		 * referencing: https://www.reddit.com/r/libgdx/comments/49zsvl/how_to_properly_close_a_libgdx_application/
		 * It appears that a default config has forceExit set to True. If forceExit is true, it will
		 * return an exit code of -1. So I'm setting it to false below.
		 */
		config.forceExit = false;
		new LwjglApplication(new ArmadilloController(), config);
	}
}
