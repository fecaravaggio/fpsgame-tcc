package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import static com.jme3.bullet.PhysicsSpace.getPhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    
    Material mat;
    Material matWall;
    Material matFloor;
    Box boxParede;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
  
        boxParede = new Box(Vector3f.ZERO, 0f, 0.5f, 1f);
        boxParede.scaleTextureCoordinates(new Vector2f(2f, 1f));
        
        int matriz[][] = {{1, 1, 1, 1, 1}, {2, 0, 2, 0, 2}, {2, 0, 2, 0, 2}, {2, 0, 0, 0, 2}, {1, 1, 1, 1, 1}};
            
        int matriz2[][] = {{1, 1, 1, 1, 1, 1}, {2, 0, 2, 0, 0, 2}, {2, 0, 2, 1, 0, 2}, {2, 0, 0, 0, 0, 2}, {1, 1, 1, 1, 1, 1}};
        
        int x = matriz.length;
        int y = matriz[0].length;
  
        initMaterial();
        initFloor(x, y);
        limiteLabirinto (x, y);
        initCrossHairs();
        //desenhaCena(matriz);
        
    }
    
    /* matriz1
    | 1 | 1 | 1 | 1 | 1 |
    | 2 | 0 | 2 | 0 | 2 |
    | 2 | 0 | 2 | 0 | 2 |
    | 2 | 0 | 0 | 0 | 2 |
    | 1 | 1 | 1 | 1 | 1 |
    */
        
    /* matriz2
    | 1 | 1 | 1 | 1 | 1 | 1 |
    | 2 | 0 | 2 | 0 | 0 | 2 |
 EU | 2 | 0 | 2 | 1 | 0 | 2 |
    | 2 | 0 | 0 | 0 | 0 | 2 |
    | 1 | 1 | 1 | 1 | 1 | 1 |
    */  
    // 0 = espaço livre
    // 1 = parede "horizontal"
    // 2 = parede "vertical"
    
    public void initMaterial () {
        
        matFloor = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey keyFloor = new TextureKey("Textures/Terrain/splat/grass.jpg");
        keyFloor.setGenerateMips(true);
        Texture texFloor = assetManager.loadTexture(keyFloor);
        texFloor.setWrap(Texture.WrapMode.Repeat);
        matFloor.setTexture("ColorMap", texFloor);
        
        matWall = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey keyWall = new TextureKey("Textures/Terrain/BrickWall/BrickWall.jpg");
        keyWall.setGenerateMips(true);
        Texture texWall = assetManager.loadTexture(keyWall);
        texWall.setWrap(Texture.WrapMode.Repeat);
        matWall.setTexture("ColorMap", texWall);      
       // matWall.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        
    }
    
    public void initFloor (int i, int j) {
          
        Box boxChao = new Box((float) i, 0.1f, (float) j);
        Spatial chao = new Geometry("Box", boxChao);
        boxChao.scaleTextureCoordinates(new Vector2f((float) i/3, (float) j));
               
        chao.setMaterial(matFloor);
        chao.setShadowMode(ShadowMode.Receive);
        chao.setLocalTranslation(0, -0.1f, 0);
        chao.addControl(new RigidBodyControl(new BoxCollisionShape(new Vector3f((float) i, 0.1f, (float) j)), 0));
             
        rootNode.attachChild(chao);  
    }
    
    public void limiteLabirinto (int x, int y) {
        
        float ang = 0f;
        //float a = 0, b = 0, c = 0;
        int j = 0, i = 0;
        
        while (j < y) {
            Vector3f vt = new Vector3f(0 - y, 0.5f, y-1 - j * 2);
            Vector3f vt2 = new Vector3f(0 + y, 0.5f, y-1 - j * 2);
            initWall(vt, ang);
            initWall(vt2, ang);
            j++;  
        }
        ang = (float) 1.5708;
         while (i < x) {
            Vector3f vt = new Vector3f(x-1 - i * 2, 0.5f, 0 - x);
            Vector3f vt2 = new Vector3f(x-1 - i * 2, 0.5f, 0 + x);
            initWall(vt, ang);
            initWall(vt2, ang);
            i++;  
        }
        
    }
    
    public void initWall (Vector3f ori, float angulo) {
        
        Spatial parede = new Geometry("Box", boxParede);
        parede.setMaterial(matWall);
        parede.setLocalTranslation(ori);
        parede.rotate(0, angulo, 0);
        rootNode.attachChild(parede);
        
    }
    
    protected void initCrossHairs() {
        
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        ch.setLocalTranslation( // center
                settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
                settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
        
    }
   
    
    public void desenhaCena(int matriz[][]) {
        
        float x = (float) matriz.length;
        float z = (float) matriz[0].length;

        //TO DO: Depois de criado os limites do labirinto, criar as paredes internas
        
        /*
        for (int x = 0; x < matriz.length; x++) {
            for (int z = 0; z < matriz[x].length; z++) {
                System.out.print(matriz[x][z] + " ");
                if (matriz[x][z] == 1) {
                    //desenhar muro horizontal
                    Box boxParede = new Box(Vector3f.ZERO, 0f, 1f, 1f);
                    Spatial paredeHor = new Geometry("Box", boxParede);

                    Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                    mat2.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
                    paredeHor.setMaterial(mat2);

                    //paredeHor.setLocalTranslation(-1 + x * 2, 0, 9 - z * 2);
                    paredeHor.setLocalTranslation(-1 + x*2, 0, -1 + z*2);
                    rootNode.attachChild(paredeHor);
                }
                else {
                    if(matriz[x][z] == 2) {
                        //desenhar muro vertical
                        Box boxParede2 = new Box(Vector3f.ZERO, 1f, 1f, 0f);
                        Spatial paredeVert= new Geometry("Box", boxParede2);

                        Material mat4 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                        mat4.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
                        paredeVert.setMaterial(mat4);

                        //paredeVert.setLocalTranslation(-2 + x * 2, 0, 10 - z * 2);
                        paredeVert.setLocalTranslation(-2 + x * 2, 0, z * 2);
                        rootNode.attachChild(paredeVert);
                    }
                }
            }
        }   
        */
    }
  

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
