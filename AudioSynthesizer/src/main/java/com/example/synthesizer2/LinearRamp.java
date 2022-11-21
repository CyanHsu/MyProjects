package com.example.synthesizer2;

public class LinearRamp implements AudioComponent{

    private AudioClip linearRamp;

    private AudioComponent origin;
    private float start_;
    private float stop_;

    LinearRamp(float start, float stop){
        start_ = start;
        stop_ = stop;
       linearRamp = new AudioClip();

    }
    @Override
    public AudioClip getClip() {

        for (int i = 0; i < AudioClip.TotalSamples; i ++ ){
            linearRamp.setSample(i, (short) clamping((start_*(AudioClip.TotalSamples - i)+stop_*i)/AudioClip.TotalSamples));

        }

        return linearRamp;
    }

    @Override
    public boolean hasInput() {
        return false;
    }

    @Override
    public void connectInput(AudioComponent input) {
        origin = input;


    }




}
