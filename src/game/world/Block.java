/*
 * Here we are
 */
package game.world;

import java.io.File;
import java.util.HashMap;
import java.util.Random;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public  class Block {

    public static Block[] block;
    public boolean solid = false, transparent = false, destroyable = false, stairs = false, doorl = false, doorr = false;
    static HashMap<String, Image> images = new HashMap<>();
    Image sprite;
    Random r = new Random();

    public static void setBlocks() {
        block = new Block[256];
        for (int i = 0; i < block.length; i++) {
            block[i] = new Block();
        }
        try {
            initSprites();
        } catch (Exception ex) {
            System.out.print("Error in Block: cannot load images");
        }
        
        correction();
        setPropetries();
        setSprites();
    }

    public static void correction() {
        block[23] = new Torch();
    }

    public static void setPropetries() {
        block[1].solid = true;
        block[1].destroyable = true;

        block[2].solid = true;
        block[2].destroyable = true;

        block[21].stairs = true;

        block[100].doorl = true;
        block[101].doorr = true;
    }

    public static void setSprites() {
        block[1].sprite = images.get("Front-brick");
        block[2].sprite = images.get("brick_wall");
        block[21].sprite = images.get("ladder");
        block[22].sprite = images.get("brush").getScaledCopy(2);
        block[24].sprite = images.get("wheel").getScaledCopy(2);
        block[25].sprite = images.get("window").getScaledCopy(2);
        block[51].sprite = images.get("brick_back");
        block[100].sprite = images.get("door");
        block[101].sprite = images.get("door");
        block[199].sprite = images.get("sky");
        
    }

    public static void initSprites() throws SlickException {
        File[] fList;
        File F = new File("res/room/");
        String path = "res/room/";
        fList = F.listFiles();
        for (int i = 0; i < fList.length; i++) {
            String wut = (path + fList[i].getName()).substring(path.length(), (path + fList[i].getName()).length() - 4);
            images.put(wut, new Image(path + fList[i].getName()));
            images.get(wut).setFilter(GL11.GL_NEAREST);
        }
    }

    public void render(Graphics g, int x, int y, int size) {
        if (sprite != null) {
            sprite.draw(x * size, y * size);
        }
    }

    
    private static class Torch extends Block{
        Image[] sprites = new Image[4];
        int s = 0;
        
        public Torch() {
                for (int i = 0; i < 4; i++) {
                    sprites[i] = images.get("torch_" + i);
                }
            }

            @Override
            public void render(Graphics g, int x, int y, int size) {
                sprites[r.nextInt(sprites.length)].draw(x * size, y * size);
            }
    }
    
}
