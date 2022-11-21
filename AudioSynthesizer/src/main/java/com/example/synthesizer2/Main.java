package com.example.synthesizer2;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class Main {
    public static void main(String[] args) throws LineUnavailableException {
//        SineWave sw = new SineWave(440);
//        AudioClip test = sw.getClip();
//        System.out.println(test.getSample(1));

        Clip c = AudioSystem.getClip();
        AudioFormat format16 = new AudioFormat( 44100, 16, 1, true, false );
//        AudioComponent gen = new SineWave(20000);
        AudioComponent gen2 = new SineWave(440);
        AudioComponent gen3 = new SineWave(660);

        //Mixer
//        Mixer mix = new Mixer();
//        mix.connectInput(gen3);
//        mix.connectInput(gen2);
//
//        AudioClip clip =mix.getClip();



        //filter
//        Filter filter = new Filter(2);
//        filter.connectInput(mix);

        LinearRamp lr = new LinearRamp(50,10000);

        VFSineWave VFsinewave = new VFSineWave();
        VFsinewave.connectInput(lr);


         AudioClip clip = VFsinewave.getClip();



        c.open( format16, clip.getData(), 0, clip.getData().length );
        System.out.println( "About to play..." );

        c.start();
        c.loop( 2 );
        while( c.getFramePosition() < (clip.sampleDatas.length )|| c.isActive() || c.isRunning() ){
            // Do nothing while we wait for the note to play.
//            System.out.println(c.getFramePosition());
        }

        System.out.println( "Done." );
        c.close();

    }
}
