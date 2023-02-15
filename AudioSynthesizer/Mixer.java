package com.example.synthesizer2;

import java.util.ArrayList;

public class Mixer implements AudioComponent{

    ArrayList<AudioClip> inputs = new ArrayList<>();


    //collect inputs


//    public double clamping(double input){
//        double clamping = 0;
//        if (input > Short.MAX_VALUE){
//            clamping = Short.MAX_VALUE;
//        }
//        else if(input < Short.MIN_VALUE){
//            clamping = Short.MIN_VALUE;
//        }
//        else {
//            clamping = input;
//        }
//        return clamping;
//    }

    //generate the sum of inputs
    @Override
    public AudioClip getClip() {
        AudioClip mix = new AudioClip();
        for(int i = 0; i < inputs.size(); i++) {
            for (int j = 0; j < mix.getData().length/2; j++) {
                mix.setSample(j, (short) (clamping(mix.getSample(j) + inputs.get(i).getSample(j))));
            }
        }
        return mix;
    }

    @Override
    public boolean hasInput() {
        return false;
    }

    @Override
    public void connectInput(AudioComponent input) {
        AudioClip member = input.getClip();
        inputs.add(member);
    }




}
