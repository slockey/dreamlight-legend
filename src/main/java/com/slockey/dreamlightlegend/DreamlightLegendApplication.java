package com.slockey.dreamlightlegend;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.slockey.dreamlightlegend.tui.AppController;

@SpringBootApplication
public class DreamlightLegendApplication {

    public static void main(String[] args) throws Exception {

        AppController controller = new AppController();
        controller.start();

    }
}