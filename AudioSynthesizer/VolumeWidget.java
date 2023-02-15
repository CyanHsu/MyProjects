package com.example.synthesizer2;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class VolumeWidget extends AudioComponentWidgetBase{


    VolumeWidget(AudioComponent ac, AnchorPane parent, String name) {
        super(ac, parent, name);
        //reset slider
        slider.setMax(3);
        slider.setMin(0);
        slider.setValue(1);
        title.setText(name+"(1.00)");
        audioComponent_= new Filter(1);
    }

    @Override
    void handleSlide(MouseEvent e, Slider slider, Label title) {
        double value = slider.getValue();
        NumberFormat formatter = new DecimalFormat("#0.00");
        title.setText("Volume(" + formatter.format(value) + ")");
        AudioComponent filter = new Filter(value);
        audioComponent_=filter;
        if(inputWidgets.size() !=0 ){
            filter.connectInput(inputWidgets.get(0).audioComponent_);
        }
    }
}
