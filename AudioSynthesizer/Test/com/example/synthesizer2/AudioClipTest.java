package com.example.synthesizer2;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AudioClipTest {

    @Test
    void getSample() {

            AudioClip test = new AudioClip("test");
            Assertions.assertEquals(test.getSample(0), 0x0201);
            Assertions.assertEquals(test.getSample(1), 0xFFA1);
    }

    @Test
    void setSample() {
        AudioClip test = new AudioClip();
        test.sampleDatas = new byte[5];
        test.setSample(0, (short) (-20005));
        System.out.println(test.getSample(0));


    }

    @Test
    void getData() {
    }
}