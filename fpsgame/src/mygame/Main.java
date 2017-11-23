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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
      
    Material mat;
    Material matWall;
    Material matFloor;
    Box boxParede2;
    Vector3f coordPlayer;
    ArrayList<Tile> tileArray = new ArrayList<Tile>();
    int iMap, jMap;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
  
        
        boxParede2 = new Box(Vector3f.ZERO, 1f, 1f, 1f);
        boxParede2.scaleTextureCoordinates(new Vector2f(1.5f, 1.5f));

        initMaterial();
        initCrossHairs();
        
        try {
            loadMap("src\\mygame\\maps\\map1.txt");
        } 
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        initFloor(iMap, jMap);
        coordPlayer = coordInicio();

        this.cam.setLocation(coordPlayer);
        //cam.lookAt(Vector3f.ZERO, new Vector3f(0, 1, 0));
        //cam.setFrustumFar(15);
        
        //desenhaCena(matriz);
        
    }
    
    /* 
    map2.txt
    
    11111
    1i101
    10101
    10001
    11111
    
    map1.txt
    
    1111111111111111111111111111111111111111
    1i00000000000000Q00000000000100010000001
    1010101010110101011111101010001010101101
    101011111011010100000010101010000000000S
    1010001010111101011110000000110101011101
    1011111100000110100000011110110101010101
    1011111111111101011111111110110101110101
    101000000000000Q0000000000100000Q0000001
    1011011010101010101010101010111010101101
    1010001000100001000010000010101000101101
    1010111111111111111111111110101110111101
    100000000000001Q000000100000100000000001
    1111111111111111111111111111111111111111
    
    */
    
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
        boxChao.scaleTextureCoordinates(new Vector2f((float) i, (float) j));
               
        chao.setMaterial(matFloor);
        chao.setShadowMode(ShadowMode.Receive);
        chao.setLocalTranslation(i, -0.1f, j);
        chao.addControl(new RigidBodyControl(new BoxCollisionShape(new Vector3f((float) i, 0.1f, (float) j)), 0));
             
        rootNode.attachChild(chao);  
    }
    
 
    public void initWall (Vector3f ori) {
        
        Spatial paredeBox = new Geometry("Box", boxParede2);
        paredeBox.setMaterial(matWall);
        paredeBox.setLocalTranslation(ori);
        rootNode.attachChild(paredeBox);
        
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
    
    public Vector3f coordInicio () {
        //percorrer a matriz e encontrar o "i" de início do personagem
        
        String aux = "";
        Vector3f vt = new Vector3f(0, 0, 0);
        Tile t;

        for (int i = 0; i < tileArray.size(); i++) {
            t = tileArray.get(i);
            //System.out.println ("oh o type do t: " + t.getType());
             if (t.getType() == 'i') {
                 System.out.println("achei o i"); 
                 System.out.println("x (i) = " + t.getTileX() + " e y (j) = " + t.getTileY());
                 vt = new Vector3f(1 + t.getTileX() * 2, 0.5f,1 + t.getTileY() * 2);
                 break;
             }         
        }
        return vt;
    }
    
    public void anguloInicio () {
        //procurar os elementos vizinhos
        //encontrar 0
        //para saber o angulo inicial
        
        // n sei se isso vale a pena, mais facil ser carregado junto com o labirinto
        
    }
    
    private void loadMap(String fileName) throws IOException {
        ArrayList lines = new ArrayList();
        int coluna = 0;
        int linha = 0;

        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        
        while (true) {
            String line = reader.readLine();
            // no more lines to read
            if (line == null) {
                reader.close();
                break;
            }

            if (!line.startsWith("!")) {
                lines.add(line);
                coluna = Math.max(coluna, line.length());
                jMap = coluna;

            }
        }
        linha = lines.size();
        iMap = linha;
        
        System.out.println("width/j tem: " + coluna + " e height/i tem: " + linha);
        
        for (int i = 0; i < linha; i++) {
            String line = (String) lines.get(i);
            System.out.println(line);
            for (int j = 0; j < coluna; j++) {
                char type = line.charAt(j);
                Tile t = new Tile(i, j, type);
                tileArray.add(t);
                
                //t.tileType();
                if (type == '1') {
                    System.out.println("i = " + i + " j = " + j);
                    Vector3f vt = new Vector3f(+1 + i * 2, 0.5f, +1 + j * 2);
                    initWall(vt);
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
