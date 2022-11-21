package com.example.synthesizer2;

public interface AudioComponent {
    AudioClip getClip();
    boolean hasInput();
    void connectInput( AudioComponent input);



    default public double clamping(double input){
        double clamping = 0;
        if (input > Short.MAX_VALUE){
            clamping = Short.MAX_VALUE;
        }
        else if(input < Short.MIN_VALUE){
            clamping = Short.MIN_VALUE;
        }
        else {
            clamping = input;
        }
        return clamping;
    }
}
