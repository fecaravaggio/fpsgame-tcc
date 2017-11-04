package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        Box boxParede = new Box(Vector3f.ZERO, 0f, 1f, 1f);
        Box boxChao = new Box(Vector3f.ZERO, 5f, 0f, 5f);
        Spatial chao = new Geometry("Box", boxChao);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/splat/grass.jpg"));
        chao.setMaterial(mat);
        chao.setLocalTranslation(1.0f, -1f, 5.0f);      

        rootNode.attachChild(chao);
        
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
        
        desenhaCena(matriz2);
        
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
    
    public void desenhaCena(int matriz[][]) {
        
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
