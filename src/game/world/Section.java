package game.world;

import game.creature.NPC;
import java.util.Random;

public class Section {
    
    public Section [] sections = new Section[20];
    public int width = 8, height = 5, location;
    Random r = new Random();
    
    
    public Section(int x, int y, Room room){
        if(r.nextBoolean()){
            for(int i = x; i<x+width+1;i++){
                room.set(i, y+1, 2, (byte)25); 
            }
        }
        for(int i = x; i<x+width+1;i++){
           room.set(i, y+height, 1, (byte)1); 
           room.set(i, y+height-1, 1, (byte) (r.nextInt(3)+22));
        }
       
       if(r.nextBoolean())room.npcies.add(new NPC(x*64 + r.nextInt(8)*64,(y+height-1)*64,1));
    }
}
