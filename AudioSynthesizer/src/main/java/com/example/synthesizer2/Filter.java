package com.example.synthesizer2;

public class Filter implements AudioComponent{

    AudioComponent input_;


    AudioClip output;
    double volume;

    Filter(double v){
        this.volume = v;
//        this.input_ = inputData;

    }

    public void setVolume(double input){
        volume = input;
    }

    @Override
    public AudioClip getClip() {
//        AudioClip original = input.getClip();
//        AudioClip result = // Some modification of the original clip.
//        AudioClip output = new AudioClip();
//        for(int i = 0; i < input.getData().length/2; i ++) {
//            output.setSample(i, (short) (input.getSample(i)*volume));
//        }
        AudioClip original = input_.getClip();
        output = new AudioClip();
        for(int i = 0; i < AudioClip.samplerate*AudioClip.duration; i ++) {
            output.setSample(i, (short) clamping(original.getSample(i)*volume));
        }
        return output;
    }

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

    @Override
    public boolean hasInput() {
        return false;
    }

    @Override
    public void connectInput(AudioComponent input) {
        input_ = input;


    }


}
