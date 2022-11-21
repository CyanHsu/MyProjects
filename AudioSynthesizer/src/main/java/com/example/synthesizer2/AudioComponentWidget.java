//package com.example.synthesizer2;
//
//import javafx.geometry.Bounds;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.control.Cell;
//import javafx.scene.control.Label;
//import javafx.scene.control.Slider;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.HBox;
//
//import javafx.scene.layout.Pane;
//import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Circle;
//import javafx.scene.control.Button;
//import javafx.scene.shape.Line;
//
//import java.awt.*;
//import java.util.ArrayList;
//
//public class AudioComponentWidget extends Pane {
//    private AudioComponent audioComponent_;
//    private AnchorPane parent_;
//    private String name_;
//
//    private HBox baseLayout;
//
//    private double mouseStartDragX_;
//    private double mouseStartDragY_;
//    private double widgetStartDragX_;
//    private double widgetStartDragY_;
//
//    private Line line_ = null;
//
//    private Circle output;
//
//    AudioComponentWidget(AudioComponent ac, AnchorPane parent, String name){
//        audioComponent_ = ac;
//        parent_ = parent;
//        name_ = name;
//
//        baseLayout = new HBox();
//
//
//        //right side
//        VBox rightSide = new VBox();
//        Button closeBtn = new Button("x");
//        closeBtn.setOnAction(e -> closeWidget());
//        output = new Circle(10);
//        output.setFill(Color.DODGERBLUE);
//        output.setOnMousePressed(e -> startConnection(e));
//        output.setOnMouseDragged(e -> moveConnection(e));
//        output.setOnMouseReleased(e -> endConnection(e));
//
//        rightSide.getChildren().add(closeBtn);
//        rightSide.getChildren().add(output);
//        rightSide.setAlignment(Pos.CENTER);
//        rightSide.setSpacing(5);
//        rightSide.setPadding(new Insets(5));
//
//        //left side
//        VBox leftSide = new VBox();
//        Label title = new Label();
//        title.setText(name+"(440 Hz)");
//        Slider slider = new Slider(220, 880, 440);
//        slider.setOnMouseDragged(e -> handleSlide(e, slider,title ));
//        leftSide.getChildren().add(title);
//        leftSide.getChildren().add(slider);
//        leftSide.setAlignment(Pos.CENTER);
//        leftSide.setSpacing(10);
//        leftSide.setPadding(new Insets(5));
//
//        leftSide.setOnMousePressed(e -> startDrag(e));
//        leftSide.setOnMouseDragged(e -> handleDrag(e));
//
//
//
//        baseLayout.getChildren().add(leftSide);
//        baseLayout.getChildren().add(rightSide);
//        baseLayout.setStyle("-fx-background-color: lightblue;-fx-border-color: black");
//
//
//        this.getChildren().add(baseLayout);
//        this.setLayoutX(50 + SynthesizeApplication.widgets_.size() * 10);
//        this.setLayoutY(100 + SynthesizeApplication.widgets_.size() * 10);
//        parent_.getChildren().add(this);
//
//
//
//    }
//
//    private void endConnection(MouseEvent e) {
//        Circle speaker = SynthesizeApplication.speakerCircle;
//        Bounds speakerBounds = speaker.localToScene(speaker.getBoundsInLocal());
//        double distance = Math.sqrt(Math.pow(speakerBounds.getCenterX()-e.getSceneX(),2)+Math.pow(speakerBounds.getCenterY()-e.getSceneY(),2));
//
//        if (distance < 10){
//            line_.setEndX(speakerBounds.getCenterX());
//            line_.setEndY(speakerBounds.getCenterY());
//            SynthesizeApplication.widgets_.add(this);
//        }
//        else {
//            parent_.getChildren().remove(line_);
//            line_=null;
//
//        }
//
//
//
//    }
//
//    private void startConnection(MouseEvent e) {
//
//        if(line_ != null){
//            parent_.getChildren().remove(line_);
//        }
//
//        Bounds parentBounds = parent_.getBoundsInParent();
//        Bounds bounds = output.localToScene(output.getBoundsInLocal());
//
//        line_= new Line();
//        line_.setStrokeWidth(3);
//        line_.setStartX(bounds.getCenterX() - parentBounds.getMinX());
//        line_.setStartY(bounds.getCenterY()- parentBounds.getMinY());
//        line_.setEndX(e.getSceneX());
//        line_.setEndY(e.getSceneY());
//
//        parent_.getChildren().add(line_);
//
//    }
//
//    private void moveConnection(MouseEvent e) {
//        Bounds parentBounds = parent_.getBoundsInParent();
//
//        line_.setEndX(e.getSceneX()-parentBounds.getMinX());
//        line_.setEndY(e.getSceneY()-parentBounds.getMinY());
//
//    }
//
//    private void handleDrag(MouseEvent e) {
//        double mouseDeltaX = e.getSceneX() - mouseStartDragX_;
//        double mouseDeltaY = e.getSceneY() - mouseStartDragY_;
//        this.relocate(widgetStartDragX_+mouseDeltaX, widgetStartDragY_ + mouseDeltaY);
//        if(line_ !=null) {
//            Bounds bounds = output.localToScene(output.getBoundsInLocal());
//            line_.setStartX(bounds.getCenterX());
//            line_.setStartY(bounds.getCenterY());
//        }
//
//    }
//
//    private void startDrag(MouseEvent e) {
//        mouseStartDragX_ = e.getSceneX();
//        mouseStartDragY_ = e.getSceneY();
//
//        widgetStartDragX_ = this.getLayoutX();
//        widgetStartDragY_ = this.getLayoutY();
//    }
//
//    private void handleSlide(MouseEvent e, Slider slider, Label title) {
//        int value = (int) slider.getValue();
//        AudioComponent newSineWave = new SineWave(value);
//        audioComponent_= newSineWave;
//        title.setText("Sine Wave(" + value + " Hz)");
//    }
//
//    private void closeWidget() {
//        parent_.getChildren().remove(this);
//        parent_.getChildren().remove(line_);
//        line_=null;
//        SynthesizeApplication.widgets_.remove(this);
//
//
//    }
//
//
//    public AudioComponent getAudioConponent() {
//        return audioComponent_;
//    }
//}
