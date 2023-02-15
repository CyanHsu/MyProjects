package com.example.synthesizer2;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class SynthesizeApplication extends Application {
    private AnchorPane mainCanvas_;

    public static ArrayList<AudioComponentWidgetBase> widgets_ = new ArrayList<>();

    public static ArrayList<AudioComponentWidgetBase> outputWidgets_ = new ArrayList<>();

    public static Circle speakerCircle;

    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(SynthesizeApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 1000,600);


        //center panel

        mainCanvas_ = new AnchorPane();
        mainCanvas_.setStyle("-fx-background-color: lightyellow");

//create a circle
        speakerCircle = new Circle(800,300,15);
        speakerCircle.setFill(Color.DODGERBLUE);
        mainCanvas_.getChildren().add(speakerCircle);

        //Bottom panel
        HBox botPanel = new HBox();
        Button playbtn = new Button("Play");
        playbtn.setOnAction(e-> playClips());

        botPanel.getChildren().add(playbtn);
//        botPanel.getChildren().add(test);
        botPanel.setAlignment(Pos.CENTER);
//        botPanel.setSpacing(50);
        botPanel.setPadding(new Insets(10));
        botPanel.setStyle("-fx-background-color: gray");

        VBox rightPanel = new VBox();
        Button sineWavebtn = new Button("SineWave");
        sineWavebtn.setOnAction(e -> createSineWave(e,mainCanvas_ ));
        Button VFSineWavebtn = new Button("VFSineWave");
        VFSineWavebtn.setOnAction(e -> createVFSineWave(e,mainCanvas_ ));
        Button volumebtn = new Button("Volume");
        volumebtn.setOnAction(e -> createVolumeWidget(e,mainCanvas_));
        Button mixerbtn = new Button("Mixer");
        mixerbtn.setOnAction(e -> createMixerWidget(e,mainCanvas_));


        rightPanel.getChildren().add(sineWavebtn);
        rightPanel.getChildren().add(VFSineWavebtn);
        rightPanel.getChildren().add(volumebtn);
        rightPanel.getChildren().add(mixerbtn);
        rightPanel.setSpacing(30);
        rightPanel.setPadding(new Insets(10));
        rightPanel.setStyle("-fx-background-color: bisque");



//        Label test = new Label("test");



        root.setBottom(botPanel);
        root.setRight(rightPanel);
        root.setCenter(mainCanvas_);

        stage.setTitle("Synthesizer");
        stage.setScene(scene);
        stage.show();
    }

    private void createMixerWidget(ActionEvent e, AnchorPane mainCanvas_) {
        AudioComponent mixer = new Mixer();
        AudioComponentWidgetBase mxw = new MixerWidget(mixer, mainCanvas_, "Mixer");
        widgets_.add(mxw);
    }

    private void createVolumeWidget(ActionEvent e, AnchorPane mainCanvas_) {
        AudioComponent volume = new Filter(1);
        AudioComponentWidgetBase vw = new VolumeWidget(volume, mainCanvas_, "Volume");
        widgets_.add(vw);
    }

    private void createVFSineWave(ActionEvent e, AnchorPane mainCanvas) {
        AudioComponent rl = new LinearRamp(50,5000);
        AudioComponent vfs = new VFSineWave();
        vfs.connectInput(rl);
        AudioComponentWidgetBase vfsww = new VFSineWaveWidget(vfs, mainCanvas_, "VF Sine Wave");
    }

        private void createSineWave(ActionEvent e, AnchorPane mainCanvas) {

        AudioComponent sinewave = new SineWave(440);
        AudioComponentWidgetBase sww = new SineWaveWidget(sinewave, mainCanvas_, "Sine Wave");


//        Label title = new Label();
//        title.setText("Sine Wave(440 Hz)");
//        Slider slider = new Slider(220, 880, 440);
//        VBox sineWaveWidget = new VBox();
//
//        sineWaveWidget.getChildren().add(title);
//        sineWaveWidget.getChildren().add(slider);
//        sineWaveWidget.relocate(50, 100);
//        sineWaveWidget.setStyle("-fx-background-color: lightblue");
//
//        slider.setOnMouseDragged(e1 -> handleSlide(e1, slider,title ));
//
//        mainCanvas.getChildren().add(sineWaveWidget);

    }

    private void handleSlide(MouseEvent e1, Slider slider, Label title) {
        int value = (int) slider.getValue();
        title.setText("Sine Wave(" + value + " Hz)");

    }


    private void playClips()  {



        if(outputWidgets_.size()==0){
            return;
        }
        else {
            try {


                Clip c = AudioSystem.getClip();
                AudioListener listener = new AudioListener(c);
                AudioFormat format16 = new AudioFormat(44100, 16, 1, true, false);

                for (AudioComponentWidgetBase acwb : widgets_){
                    if(acwb.inputWidgets.size() > 1) {
                        for (int i = 0; i < acwb.inputWidgets.size(); i++) {
                            acwb.audioComponent_.connectInput(acwb.inputWidgets.get(i).audioComponent_);
                        }
                    }
                }

//                Mixer mixer = new Mixer();
//                for (AudioComponentWidgetBase acwb : outputWidgets_) {
//                    mixer.connectInput(acwb.getAudioComponent());
//                }

                AudioClip clip = outputWidgets_.get(0).audioComponent_.getClip();
                c.open(format16, clip.getData(), 0, clip.getData().length);
                System.out.println("About to play...");

                c.start();
                c.addLineListener(listener);


            } catch (LineUnavailableException e) {
                System.out.println("No sound can be played");
            }
        }

    }

    public static void main(String[] args) {

        launch();
    }
}