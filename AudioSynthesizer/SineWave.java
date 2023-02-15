package com.example.synthesizer2;

public class SineWave implements AudioComponent{

    private AudioClip sineWave;
    private int frequency;

    SineWave(int pitch){
        frequency = pitch;
        sineWave = new AudioClip();

    }

    @Override
    public AudioClip getClip() {


        for(int i =0 ; i < AudioClip.samplerate * AudioClip.duration  ; i++ ){
            sineWave.setSample(i, (short) (Short.MAX_VALUE * Math.sin(2 * Math.PI * frequency * i /AudioClip.samplerate)));
        }

//       =sample[ i ] = maxValue * sine( 2*pi*frequency * i / sampleRate );
        return sineWave;
    }

    @Override
    public boolean hasInput() {
        return false;
    }

    @Override
    public void connectInput(AudioComponent input) {

    }



}
