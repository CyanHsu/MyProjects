package com.example.synthesizer2;

public class VFSineWave implements AudioComponent{

    private AudioClip input_;
    private AudioClip output_;
    private int frequency;

    VFSineWave(){
       output_=new AudioClip();

    }




    @Override
    public AudioClip getClip() {

        double   phase = 0;
        for(int i = 0 ; i< AudioClip.TotalSamples; i ++){
        phase += 2 * Math.PI * input_.getSample(i) / AudioClip.samplerate;
        output_.setSample(i, (short) clamping(Short.MAX_VALUE*Math.sin(phase)));
        }

        return output_;
    }

    @Override
    public boolean hasInput() {
        return false;
    }

    @Override
    public void connectInput(AudioComponent input) {
        input_ = input.getClip();

    }




}
