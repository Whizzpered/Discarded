package game.world;

import game.creature.NPC;
import game.object.Wearing;
import java.util.Random;

public class Section {
    
    public Section [] sections = new Section[20];
    public int width = 8, height = 10, location;
    Random r = new Random();
    
    
    public Section(int x, int y, Room room){
        if(r.nextBoolean()){
            for(int i = x; i<x+width+1;i++){
                room.set(i, y+1, 2, (byte)25); 
            }
        }
        for(int i = x; i<x+width+1;i++){
           room.set(i, y+height, 1, (byte)1); 
        }
       
        if(r.nextBoolean())room.set(x+r.nextInt(width), y+height-2, 1, (byte) 23);
           
           if(r.nextInt(10)>4){
               for(int i = x+2;i<x+width+1;i++){
                   room.set(i, y+height/2, 1, (byte)3); 
               }
               room.set(x+1,y+height/2,1,(byte)21);
              if(r.nextBoolean())room.objects.add(new Wearing((x+width-3)*64,(y+height/2-2)*64,5));
              if(r.nextBoolean()){
                  for(int i = 1; i < 5; i++){
                      room.set(x+width-(int)(i/2), y+height/2+i, 1, (byte)3); 
                  }
              }
           }
       if(r.nextBoolean()){
           int g = r.nextInt(3);
           if(g==0)g++;
           room.npcies.add(new NPC(x*64 + r.nextInt(8)*64,(y+height/2-1)*64,g,room.locate));
       }
       
    }
}
