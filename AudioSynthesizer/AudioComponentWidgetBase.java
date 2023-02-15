package com.example.synthesizer2;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class AudioComponentWidgetBase extends Pane {

    protected ArrayList<AudioComponentWidgetBase> inputWidgets = new ArrayList<>();
    protected AudioComponent audioComponent_;
    private AnchorPane parent_;
    private String name_;

    protected HBox baseLayout;

    protected VBox leftSide, center, rightSide;

    private double mouseStartDragX_;
    private double mouseStartDragY_;
    private double widgetStartDragX_;
    private double widgetStartDragY_;

    private Line line_ ;

    protected Circle output;

    protected Circle input;

    protected Slider slider;

    protected  Label title;

    protected boolean hasInput = false;
    protected boolean hasConnection = false;

    private AudioComponentWidgetBase connectionWidget;

    AudioComponentWidgetBase(AudioComponent ac, AnchorPane parent, String name){
        audioComponent_ = ac;
        parent_ = parent;
        name_ = name;

        baseLayout = new HBox();


        //right side
        rightSide = new VBox();
        Button closeBtn = new Button("x");
        closeBtn.setOnAction(e -> closeWidget());
        output = new Circle(10);
        output.setFill(Color.DODGERBLUE);
        output.setOnMousePressed(e -> startConnection(e));
        output.setOnMouseDragged(e -> moveConnection(e));
        output.setOnMouseReleased(e -> endConnection(e));

        rightSide.getChildren().add(closeBtn);
        rightSide.getChildren().add(output);
        rightSide.setAlignment(Pos.CENTER);
        rightSide.setSpacing(5);
        rightSide.setPadding(new Insets(5));

        //left side
        leftSide = new VBox();
        input = new Circle(10);
        input.setFill(Color.ORANGE);
        leftSide.setSpacing(10);
        leftSide.setPadding(new Insets(5));
        leftSide.getChildren().add(input);


        //center
        center = new VBox();
        title = new Label();
        title.setText(name+"(440 Hz)");
        slider = new Slider(220, 880, 440);

        center.getChildren().add(title);
        center.getChildren().add(slider);


        slider.setOnMouseDragged(e -> handleSlide(e, slider,title ));

        center.setAlignment(Pos.CENTER);
        center.setSpacing(10);
        center.setPadding(new Insets(5));

        center.setOnMousePressed(e -> startDrag(e));
        center.setOnMouseDragged(e -> handleDrag(e));



        baseLayout.getChildren().add(leftSide);
        baseLayout.getChildren().add(center);
        baseLayout.getChildren().add(rightSide);
        baseLayout.setStyle("-fx-background-color: lightblue;-fx-border-color: black");


        this.getChildren().add(baseLayout);
        this.setLayoutX(50 + SynthesizeApplication.widgets_.size() * 10);
        this.setLayoutY(100 + SynthesizeApplication.widgets_.size() * 10);
        parent_.getChildren().add(this);



    }



    private void startConnection(MouseEvent e) {

        if(line_ != null){
            parent_.getChildren().remove(line_);
            hasConnection = false;
            connectionWidget.inputWidgets.remove(this);
        }

        Bounds parentBounds = parent_.getBoundsInParent();
        Bounds bounds = output.localToScene(output.getBoundsInLocal());

        line_= new Line();
        line_.setStrokeWidth(4);
        line_.setStartX(bounds.getCenterX() - parentBounds.getMinX());
        line_.setStartY(bounds.getCenterY()- parentBounds.getMinY());
        line_.setEndX(e.getSceneX());
        line_.setEndY(e.getSceneY());

        parent_.getChildren().add(line_);

    }

    private void moveConnection(MouseEvent e) {
        Bounds parentBounds = parent_.getBoundsInParent();

        line_.setEndX(e.getSceneX()-parentBounds.getMinX());
        line_.setEndY(e.getSceneY()-parentBounds.getMinY());

    }

    private void endConnection(MouseEvent e) {
        Circle speaker = SynthesizeApplication.speakerCircle;
        Bounds speakerBounds = speaker.localToScene(speaker.getBoundsInLocal());

//        Bounds volumeBounds = VolumeWidget.input.localToScene(input.getBoundsInLocal());

        double distanceToSpeaker = Math.sqrt(Math.pow(speakerBounds.getCenterX()-e.getSceneX(),2)+Math.pow(speakerBounds.getCenterY()-e.getSceneY(),2));
//        double distanceToVolume = Math.sqrt(Math.pow(volumeBounds.getCenterX()-e.getSceneX(),2)+Math.pow(volumeBounds.getCenterY()-e.getSceneY(),2));

        if (distanceToSpeaker < 10){
            line_.setEndX(speakerBounds.getCenterX());
            line_.setEndY(speakerBounds.getCenterY());
            SynthesizeApplication.outputWidgets_.add(this);
            hasConnection = true;
        }
        else {
            for ( AudioComponentWidgetBase x : SynthesizeApplication.widgets_) {
                Bounds volumeBounds = x.input.localToScene(x.input.getBoundsInLocal());
                double distanceToVolume = Math.sqrt(Math.pow(volumeBounds.getCenterX()-e.getSceneX(),2)+Math.pow(volumeBounds.getCenterY()-e.getSceneY(),2));
                if (distanceToVolume < 5 && !x.equals(this)) {
                    line_.setEndX(volumeBounds.getCenterX());
                    line_.setEndY(volumeBounds.getCenterY());
                    x.inputWidgets.add(this);
//                    x.process();
                    x.audioComponent_.connectInput(this.audioComponent_);
//                    connectionWidget = x;
                    x.hasInput = true;
                    hasConnection = true;
                    connectionWidget = x;
                }
            }
        }
        if(!hasConnection){
            parent_.getChildren().remove(line_);
            line_=null;
        }

//        else {
//            parent_.getChildren().remove(line_);
//            line_=null;
//        }
    }

    private void handleDrag(MouseEvent e) {
        double mouseDeltaX = e.getSceneX() - mouseStartDragX_;
        double mouseDeltaY = e.getSceneY() - mouseStartDragY_;
        this.relocate(widgetStartDragX_+mouseDeltaX, widgetStartDragY_ + mouseDeltaY);
        Bounds bounds = output.localToScene(output.getBoundsInLocal());
        Bounds boundsInput = this.input.localToScene(this.input.getBoundsInLocal());
        if(line_ !=null ) {
            line_.setStartX(bounds.getCenterX());
            line_.setStartY(bounds.getCenterY());
            if(inputWidgets != null) {
                for (AudioComponentWidgetBase acw : inputWidgets) {
                    acw.line_.setEndX(boundsInput.getCenterX());
                    acw.line_.setEndY(boundsInput.getCenterY());
                }
            }
        }
        else if(inputWidgets != null){
            for (AudioComponentWidgetBase acw : inputWidgets) {
                    acw.line_.setEndX(boundsInput.getCenterX());
                    acw.line_.setEndY(boundsInput.getCenterY());
            }
        }

    }

    private void startDrag(MouseEvent e) {
        mouseStartDragX_ = e.getSceneX();
        mouseStartDragY_ = e.getSceneY();

        widgetStartDragX_ = this.getLayoutX();
        widgetStartDragY_ = this.getLayoutY();
    }

    void handleSlide(MouseEvent e, Slider slider, Label title) {
        int value = (int) slider.getValue();
        AudioComponent newSineWave = new SineWave(value);
        audioComponent_= newSineWave;
        title.setText("Sine Wave(" + value + " Hz)");

    }

    private void closeWidget() {
        parent_.getChildren().remove(this);
        parent_.getChildren().remove(line_);
        line_=null;
        SynthesizeApplication.widgets_.remove(this);
        SynthesizeApplication.outputWidgets_.remove(this);
        hasConnection = false;


    }

    public void process( ){
       return;


    }

    public AudioComponent getAudioComponent() {
        return audioComponent_;
    }

}
