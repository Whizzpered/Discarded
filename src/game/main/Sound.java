
package game.main;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;


public class Sound {
    static HashMap<String, Audio> sounds = new HashMap<String, Audio>();
    
    
    public void setSounds() {
        try {
            String files[] = new File("res/sounds").list();
            for (String adr : files) {
                if (adr.contains(".ogg")) {
                    sounds.put(adr.substring(0, adr.indexOf(".ogg")),// get every ogg sound and write into list under name of file
                            AudioLoader.getAudio("OGG", new FileInputStream(new File("res/sounds/" + adr))));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void playSound(String index, int gain, int pitch, boolean repeat) {
        sounds.get(index).playAsSoundEffect(gain, pitch, repeat);
    }

    public void playSound(String index, int gain, int pitch, boolean repeat, float x, float y, float z) {
        sounds.get(index).playAsSoundEffect(gain, pitch, repeat, x, y, z);
    }
}
