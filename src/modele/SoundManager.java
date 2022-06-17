package modele;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import java.io.File;

import server.Client;

public class SoundManager extends Thread{
    
    private String musicName;
    private boolean loop;
    private boolean muted = false;
    private Clip clip;
    private int type; // 0 = musique, 1 = sound effect

    public SoundManager(String musicName, boolean loop, int type){
        this.musicName = musicName;
        this.loop = loop;
        this.type = type;
    }

    public void run(){
        try {
            clip = AudioSystem.getClip();
            Images i = new Images();
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(i.getPath() + musicName).getAbsoluteFile()); 
            clip.open(audioStream);
            if(loop){
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }            
            //clip.start();
            int vol = 20;
            if(type == 1){
                vol = 100;
            }
            setVolume(vol, this.type);
        } catch (Exception e) {
            //System.err.println(e.getMessage());
        }
    }

    public void muteUnmute() {
        if(muted){
            clip.start();
            muted = false;
        }else{
            clip.stop();
            muted = true;
        }
    }

    public void setVolume(float volume, int type) {
        if(type == this.type){
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);        
            gainControl.setValue(20f * (float) Math.log10(volume));
        }
    }
}
