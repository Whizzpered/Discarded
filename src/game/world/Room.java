/*
 * Here we are
 */
package game.world;

import game.creature.NPC;
import game.creature.Player;
import game.main.Game;
import static game.main.Game.sSizeX;
import game.object.Bullet;
import game.object.Objects;
import java.util.ArrayList;
import java.util.Random;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Room {
    
    public Section[][] sections = new Section[5][4];
    public int width=50, height=30,depth=3, size = 64, blockTim,locate,y;
    public byte [][][] room = new byte [width][height][depth];
    Random r = new Random();
    
    public ArrayList<NPC> npcies;
    public NPC[] npcArr;
    public ArrayList<Bullet> bullets;
    public Bullet[] bullArr;
    public ArrayList<Objects> objects ;
    public Objects[] objArr;
    
    public NPC[] getNPCArr(ArrayList<NPC> enemies) {
        npcArr = new NPC[enemies.size()]; 
        npcArr = enemies.toArray(npcArr);
        return npcArr;
    }
    
    public Bullet[] getBullArr(ArrayList<Bullet> bullets) {
        bullArr = new Bullet[bullets.size()];
        bullArr = bullets.toArray(bullArr);
        return bullArr;
    }
    
    public Objects[] getObjArr(ArrayList<Objects> enemies) {
        objArr = new Objects[objects.size()]; 
        objArr = objects.toArray(objArr);
        return objArr;
    }
    /* 
        from 1 to 20 is a floors
        from 20 to 50 is a walls
        from 50 to 100 is a backgrounds
        from 100 to 200 reserved blocks
        from 200 to ... animated blocks
    */
    
    
    public Room(int locate, int y) throws SlickException{
        npcies = new ArrayList<>();
        bullets = new ArrayList<>();
        objects = new ArrayList<>();
        this.locate = locate;
        this.y = y;
        generate();
    }
    
    public void generate() {
        //-----------------------------------Skelet-----------------------------
        for(int x = 0; x<5*8+1; x++){
            for(int y = 0; y<4*5+1 ; y++){
                set(x,y, 2,(byte)51);
            }
            }
        for(int x = 0; x<5; x++){
            for(int y = 0; y<4 ; y++){
               sections[x][y] = new Section(x*8, y*5, this);
            }}
        for(int y = 0;y<4*(sections[1][1].height)+1;y++){
            set(0,y, 1,(byte)2);
        }
        for(int y = 0;y<4*(sections[1][1].height)+1;y++){
            set(5*(sections[1][1].width)+1,y, 1,(byte)2);
        }
        for(int x = 0;x<5*sections[1][1].width+1;x++){
            set(x,0, 1,(byte)2);
        }
        
        //---------------------------------Vhod-Vyhod---------------------------
        int i = r.nextInt(4);
        if(i==0)i++;
        if(i==1){set(5*(sections[1][1].width)+1, 4*i, 1, (byte)101);
                 set(5*(sections[1][1].width)+1, 5*i, 1, (byte)1);
                 set(5*(sections[1][1].width)+2, 5*i, 1, (byte)1);
        } else {
                set(5*(sections[1][1].width)+1, 5*i-1, 1, (byte)101);
                set(5*(sections[1][1].width)+1, 5*i, 1, (byte)1);
                set(5*(sections[1][1].width)+2, 5*i, 1, (byte)1);
        }
        if(locate>1){
            set(0, y, 1, (byte)100);
        }else {
            i = r.nextInt(4);
            if(i==0)i++;
            if(i==1){set(0, 4*i, 1, (byte)100);
                     set(0, 4*i+1, 1, (byte)1);
            } else {
                set(0, 5*i-1, 1, (byte)100); 
                set(0, 5*i+1, 1, (byte)1);              
        }}
        //---------------------------------Lesenki------------------------------
        for(int y = 1; y<4 ; y++){
            set(r.nextInt(40)+1,y*5, 1,(byte)21);
        }
        
    }
    
    public void set(int x, int y,int c, byte type) {
        if(type<0)type = 0;
        if(x>=0 && x<width && y>=0 && y<height){
          room[x][y][c] = type;
        if(type == 21){
            int i = 1;
            while (get(x, y+i, 1)==0){
                room[x][y+i][1] = type;
                i++;
            }
        }    
        
    }}
    
    public int get(int x, int y, int c) {
        if(x>=0 && x<width && y>=0 && y<height)
            return(room[x][y][c]);
        else return 0;
    }
    
    public void destroy(int x, int y) {
        if(x>=0 && x<width && y>=0 && y<height){
            set(x, y, 1,(byte)200);
            blockTim = 200;
        }
    }
    
    
    public void render(Graphics g, int camx, int camy, int ssizex, int ssizey, Player player, Game game) {
        int h = game.app.getHeight();
        for (double i = -camx / 3 - game.app.getWidth() / 2.0 * 3.0; i < camx / 3 + game.app.getWidth() / 2.0 * 3.0; i += h) {
             Block.block[199].sprite.draw((int)i, 0, h, h);
        }

        GL11.glTranslatef(-camx-32, -camy-32, 0);

        for (int x = camx/size-ssizex/size-1; x<camx/size+ssizex/size+2; x++) {
        for (int y = camy/size+ssizey/size+2; y>camy/size-ssizey/size-1; y--) {
            
            if(Block.block[get(x,y,1)].sprite!=null)Block.block[get(x,y,1)].sprite.setImageColor(0.5f, 0.5f, 0.5f);
            Block.block[get(x,y,2)].render(g, x, y, size);
            
            if(Block.block[get(x,y,1)].sprite!=null)Block.block[get(x,y,1)].sprite.setImageColor(2f, 2f, 2f);
            Block.block[get(x,y,1)].render(g, x, y, size);
        }}
        
        player.render(g, camx, camy);
        for(NPC en : getNPCArr(npcies)){
            en.render(g, camx, camy, sSizeX, sSizeX,game);
        }
                
        for(Bullet bul : getBullArr(bullets)){
            bul.render(g, camx, camy, sSizeX, sSizeX,game);
        }
        
        for(Objects obj : getObjArr(objects)){
            obj.render(g, camx, camy, sSizeX, sSizeX,game);
        }
        GL11.glTranslatef(camx+32, camy+32, 0);
    }
}

