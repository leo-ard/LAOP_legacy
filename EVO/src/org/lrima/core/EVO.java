package org.lrima.core;

import org.lrima.Interface.home.HomeFrame;

public class EVO {

	public static void main(String[] args) {
		start();

	}

	public static void restart() {
        start();
    }

    private static void start(){
        HomeFrame homeFrame = new HomeFrame();
        homeFrame.setVisible(true);

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
