package com.example.synthesizer2;

import javafx.scene.layout.AnchorPane;

public class MixerWidget extends AudioComponentWidgetBase{
    MixerWidget(AudioComponent ac, AnchorPane parent, String name) {
        super(ac, parent, name);

        title.setText(name);
        center.getChildren().remove(slider);
        slider =null;


    }

//    public void process(){
//        audioComponent_ = new Mixer();
//        for (AudioComponentWidgetBase acwb : inputWidgets) {
//            audioComponent_.connectInput(acwb.getAudioComponent());
//        }
//
//    }
}
