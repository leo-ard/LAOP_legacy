package org.lrima.core;

import org.lrima.Interface.home.HomeFrameManager;

import java.awt.*;

public class EVO {

	public static void main(String[] args) {
		start();

	}

	public static void restart() {
        start();
    }

    private static void start(){
        EventQueue.invokeLater(() -> {
            try {
                HomeFrameManager homeFrameManager = new HomeFrameManager();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
