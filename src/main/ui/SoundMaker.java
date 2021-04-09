package ui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

// Code below is attributed to stackoverflow suggestion:
// https://stackoverflow.com/questions/9470148/how-do-you-play-a-long-audioclip

public class SoundMaker {

    private static final String DELETE_SOUND = "./data/delete.wav";
    private static final String ADD_SOUND = "./data/add.wav";
    private static final String PAY_SOUND = "./data/pay.wav";
    private static final String SAVE_SOUND = "./data/save.wav";

    // EFFECTS: Plays the sound from the file destination in the parameter
    private void playSound(File file) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: Plays a ding success sound
    public void playAddSound() {
        File addSound = new File(ADD_SOUND);
        playSound(addSound);
    }

    // EFFECTS: Plays a trash delete sound
    public void playDeleteSound() {
        File deleteSound = new File(DELETE_SOUND);
        playSound(deleteSound);
    }

    // EFFECTS: Plays a money sound
    public void playPaidSound() {
        File deleteSound = new File(PAY_SOUND);
        playSound(deleteSound);
    }

    // EFFECTS: Plays a saved sound
    public void playSaveSound() {
        File deleteSound = new File(SAVE_SOUND);
        playSound(deleteSound);
    }
}