package org.lrima.core;

import org.lrima.Interface.HomeDialog;
import org.lrima.Interface.FrameManager;

public class EVO {

	public static void main(String[] args) {
        UserPrefs.load();

		start();

	}

	public static void restart() {
        UserPrefs.load();
        start();
    }

    private static void start(){
        HomeDialog homeDialog = new HomeDialog();
        homeDialog.setVisible(true);

        /*EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frame = new FrameManager();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/
    }

}
