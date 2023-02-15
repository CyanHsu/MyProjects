package com.example.synthesizer2;

import javafx.scene.layout.AnchorPane;

public class SineWaveWidget extends AudioComponentWidgetBase {


    SineWaveWidget(AudioComponent ac, AnchorPane parent, String name) {
        super(ac, parent, name);
        inputWidgets = null;
        baseLayout.getChildren().remove(leftSide);



    }
}
