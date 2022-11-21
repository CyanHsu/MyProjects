package com.example.synthesizer2;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class VFSineWaveWidget extends AudioComponentWidgetBase{
    VFSineWaveWidget(AudioComponent ac, AnchorPane parent, String name) {
        super(ac, parent, name);

        title.setText(name + "(Max 5000 Hz)");
        inputWidgets = null;
        baseLayout.getChildren().remove(leftSide);
        slider.setMax(10000);
        slider.setMin(500);
        slider.setValue(5000);
    }

    void handleSlide(MouseEvent e, Slider slider, Label title) {
        int value = (int) slider.getValue();
        AudioComponent newLinearRamp = new LinearRamp(50, value);
        AudioComponent vfs = new VFSineWave();
        vfs.connectInput(newLinearRamp);
        audioComponent_ = vfs;
        title.setText("VF Sine Wave(Max " + value + " Hz)");

    }


}
