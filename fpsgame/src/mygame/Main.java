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

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
  
        /*
        | 1 | 1 | 1 | 1 | 1 |
        | 2 | 0 | 2 | 0 | 2 |
        | 2 | 0 | 2 | 0 | 2 |
        | 2 | 0 | 0 | 0 | 2 |
        | 1 | 1 | 1 | 1 | 1 |
        */
        int matriz[][] = {{1, 1, 1, 1, 1}, {2, 0, 2, 0, 2}, {2, 0, 2, 0, 2}, {2, 0, 0, 0, 2}, {1, 1, 1, 1, 1}};
        
        /*
        | 1 | 1 | 1 | 1 | 1 | 1 |
        | 2 | 0 | 2 | 0 | 0 | 2 |
        | 2 | 0 | 2 | 1 | 0 | 2 |
        | 2 | 0 | 0 | 0 | 0 | 2 |
        | 1 | 1 | 1 | 1 | 1 | 1 |
        */
        
        int matriz2[][] = {{1, 1, 1, 1, 1, 1}, {2, 0, 2, 0, 0, 2}, {2, 0, 2, 1, 0, 2}, {2, 0, 0, 0, 0, 2}, {1, 1, 1, 1, 1, 1}};
        
        initMaterial();
        initFloor(5, 5);
        initWall();
        desenhaCena(matriz);
        
    }
    
    /*
    | 1 | 1 | 1 | 1 | 1 |
    | 2 | 0 | 2 | 0 | 2 |
    | 2 | 0 | 2 | 0 | 2 |
    | 2 | 0 | 0 | 0 | 2 |
    | 1 | 1 | 1 | 1 | 1 |
    */
        
    /*
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
        
        //x = 4
        //y = 4
        
    }
    
    public void initWall () {
        
        Box boxParede = new Box(Vector3f.ZERO, 0f, 1f, 1f);
        Spatial paredeHor = new Geometry("Box", boxParede);
        boxParede.scaleTextureCoordinates(new Vector2f(3f, 2f));

        paredeHor.setMaterial(matWall);

        //paredeHor.setLocalTranslation(-1 + x * 2, 0, -1 + z * 2);
        paredeHor.setLocalTranslation(-1, 1, -1);
        rootNode.attachChild(paredeHor);
        
    }
   
    
    public void desenhaCena(int matriz[][]) {
        
        float x = (float) matriz.length;
        float z = (float) matriz[0].length;
  
        
        //TO DO: Através do i e j, criar o chão e as paredes que cercam o labirinto
        
        /*
        //Box boxChao = new Box(Vector3f.ZERO, 5f, 0f, 5f);
        Box boxChao = new Box(10f, 0.1f, 5f);
        Spatial chao = new Geometry("Box", boxChao);
        boxChao.scaleTextureCoordinates(new Vector2f(3f, 6f));

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/splat/grass.jpg"));
        chao.setMaterial(mat);
        chao.setLocalTranslation(1.0f, -1f, 6.2f);      
        */
        
        
        /* CUBO PAREDE ANTIGA
        
        Box boxParede = new Box(Vector3f.ZERO, x, 1, z);
        Spatial paredes = new Geometry("Box", boxParede);
        boxParede.scaleTextureCoordinates(new Vector2f(x/2, z/2));
        
        paredes.setMaterial(matWall);
        paredes.setLocalTranslation(1.0f, 0f, 6.2f);
        rootNode.attachChild(paredes);
        
        */
        
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
