package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import static com.jme3.bullet.PhysicsSpace.getPhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener {

    Material mat;
    Material matWall;
    Material matFloor;
    Material mat2;
    Box boxParede2;
    Vector3f coordPlayer;
    ArrayList<Tile> tileArray = new ArrayList<Tile>();
    int iMap, jMap;
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
    private CharacterControl player;
    private boolean left = false, right = false, up = false, down = false;
    private Vector3f walkDirection = new Vector3f();
    private static Sphere bullet;
    private static SphereCollisionShape bulletCollisionShape;
    private boolean isRunning = true;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        /**
         * Set up Physics
         */
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.setDebugEnabled(true);

        boxParede2 = new Box(Vector3f.ZERO, 1f, 1f, 1f);
        boxParede2.scaleTextureCoordinates(new Vector2f(1.5f, 1.5f));
        bullet = new Sphere(16, 16, 0.1f, true, false);
        bullet.setTextureMode(Sphere.TextureMode.Projected);
        bulletCollisionShape = new SphereCollisionShape(0.4f);

        setUpKeys();
        initMaterial();
        initCrossHairs();
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        flyCam.setMoveSpeed(1);

        try {
            loadMap("src\\mygame\\maps\\map1.txt");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        initFloor(iMap, jMap);
        coordPlayer = coordInicio();

        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(0.2f, 0.9f, 1);
        player = new CharacterControl(capsuleShape, 0.05f);
        player.setJumpSpeed(10);
        player.setFallSpeed(10);
        player.setGravity(20);
        player.setPhysicsLocation(coordPlayer);
        

        bulletAppState.getPhysicsSpace().add(player);

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
    public void initMaterial() {

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

        mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key2 = new TextureKey("Textures/Terrain/Rock/Rock.PNG");
        key2.setGenerateMips(true);
        Texture tex2 = assetManager.loadTexture(key2);
        mat2.setTexture("ColorMap", tex2);

    }

    public void initFloor(int i, int j) {

        Box boxChao = new Box((float) i, 0.1f, (float) j);
        Spatial chao = new Geometry("Box", boxChao);
        boxChao.scaleTextureCoordinates(new Vector2f((float) i, (float) j));

        chao.setMaterial(matFloor);
        chao.setShadowMode(ShadowMode.Receive);
        chao.setLocalTranslation(i, -0.1f, j);
        // chao.addControl(new RigidBodyControl(new BoxCollisionShape(new Vector3f((float) i, 0.1f, (float) j)), 0));

        CollisionShape sceneShape
                = CollisionShapeFactory.createMeshShape(chao);
        //landscape = new RigidBodyControl(sceneShape, 0);
        landscape = new RigidBodyControl(0);
        chao.addControl(landscape);

        rootNode.attachChild(chao);
        bulletAppState.getPhysicsSpace().add(landscape);
    }

    public void initWall(Vector3f ori) {

        Spatial paredeBox = new Geometry("Box", boxParede2);
        paredeBox.setMaterial(matWall);
        paredeBox.setLocalTranslation(ori);

        CollisionShape sceneShape
                = CollisionShapeFactory.createMeshShape(paredeBox);
        landscape = new RigidBodyControl(sceneShape, 0);
        paredeBox.addControl(landscape);

        bulletAppState.getPhysicsSpace().add(landscape);

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

    public Vector3f coordInicio() {
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
                vt = new Vector3f(1 + t.getTileX() * 2, 2f, 1 + t.getTileY() * 2);
                break;
            }
        }
        return vt;
    }

    public void anguloInicio() {
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

    private void setUpKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Jump");
        inputManager.addListener(this, "Pause");

        inputManager.addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "shoot");
        inputManager.addMapping("gc", new KeyTrigger(KeyInput.KEY_X));
        inputManager.addListener(this, "gc");
    }
    
    
    public void textfinishLevel () {    
        /**
         * Write text on the screen (HUD)
         */
        guiNode.detachAllChildren();

        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");

        BitmapText helloText = new BitmapText(guiFont, false);

        helloText.setSize(guiFont.getCharSet().getRenderedSize());

        helloText.setText("Missão Completa");

        helloText.setLocalTranslation(300, helloText.getLineHeight(), 0);

        guiNode.attachChild(helloText);
        
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        Vector3f camDir = cam.getDirection().clone().multLocal(0.05f);
        Vector3f camLeft = cam.getLeft().clone().multLocal(0.03f);
        walkDirection.set(0, 0, 0);

        System.out.println("o isrunning é: " + isRunning);
        
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }
        player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());

    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    private PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }

    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
        if (name.equals("Pause") && !keyPressed) {
            isRunning = !isRunning;
        }
        if (isRunning) {
            if (name.equals("shoot") && !keyPressed) {
                Geometry bulletg = new Geometry("bullet", bullet);
                bulletg.setMaterial(mat2);
                bulletg.setShadowMode(ShadowMode.CastAndReceive);
                bulletg.setLocalTranslation(cam.getLocation());
                //bulletg.setLocalTranslation(player.getPhysicsLocation());

                SphereCollisionShape bulletCollisionShape = new SphereCollisionShape(0.4f);
                //RigidBodyControl bulletNode = new BombControl(assetManager, bulletCollisionShape, 1);
                RigidBodyControl bulletNode = new RigidBodyControl(bulletCollisionShape, 1);
                bulletNode.setLinearVelocity(cam.getDirection().mult(50));
                bulletg.addControl(bulletNode);
                rootNode.attachChild(bulletg);
                getPhysicsSpace().add(bulletNode);
            } else if (name.equals("gc") && !keyPressed) {
                System.gc();
            } else if (name.equals("Left")) {
                left = keyPressed;
            } else if (name.equals("Right")) {
                right = keyPressed;
            } else if (name.equals("Up")) {
                up = keyPressed;
            } else if (name.equals("Down")) {
                down = keyPressed;
            } else if (name.equals("Jump")) {
                player.jump();
            }
        }
    }
}