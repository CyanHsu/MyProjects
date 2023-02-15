package com.example.synthesizer2;


import java.util.Arrays;

public class AudioClip {
    protected static final double duration = 2.0;

    protected static final int samplerate = 44100;
    protected static final double TotalSamples = duration*samplerate;
    public byte[] sampleDatas;

    AudioClip(){
        int totalbyte = (int) (samplerate * duration * 2);
        sampleDatas = new byte[totalbyte];
    }
    AudioClip(String test){
        int totalbyte = (int) (samplerate * duration * 2);
        sampleDatas = new byte[totalbyte];
        sampleDatas[0] = (byte) 0x01;
        sampleDatas[1] = (byte) 0x02;
        sampleDatas[2] = (byte) 0xA1;
        sampleDatas[3] = (byte) 0xFF;
    }

    public int getSample( int index ){
        int lower = Byte.toUnsignedInt(sampleDatas[2*index]);
        int upper = sampleDatas[2*index+1] << 8;

        return upper | lower;
    }
    public void setSample( int index, short value ){
        sampleDatas[2*index] = (byte) (value & 0xFF);
        sampleDatas[2*index+1] = (byte) (value >> 8);
    }

    public byte[] getData(){
        byte[] copy ;
        copy = Arrays.copyOf(sampleDatas, sampleDatas.length);
        return copy;


    }


}
