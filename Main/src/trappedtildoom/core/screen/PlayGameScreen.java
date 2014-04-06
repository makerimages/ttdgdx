package trappedtildoom.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import trappedtildoom.core.TrappedTilDoomGame;
import trappedtildoom.gamelogic.gameobject.Player;
import trappedtildoom.gamelogic.potion.PotionType;
import trappedtildoom.graphics.TerrainRenderer;
import trappedtildoom.ui.*;
import trappedtildoom.core.input.PlayGameInputProcessor;
import trappedtildoom.core.serialization.GameData;
import trappedtildoom.gamelogic.terrain.*;
import trappedtildoom.core.util.MathUtils;

import java.util.Random;

import trappedtildoom.graphics.PlayerRenderer;
import trappedtildoom.core.service.SoundManager;
import trappedtildoom.core.thread.AutosaverThread;
import trappedtildoom.core.thread.FoodDecreaserThread;
import trappedtildoom.core.util.ClassUtil;
import trappedtildoom.gamelogic.potion.Potion;
import trappedtildoom.gamelogic.world.GameWorld;
import trappedtildoom.gamelogic.world.GameWorldRenderer;

public class PlayGameScreen extends TrappedTilDoomScreen {
    
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final SpriteBatch spriteBatch = new SpriteBatch();

    private Skin skin = new Skin(Gdx.files.internal("res/ui/uiskin.json"));

    private static final BitmapFont font = new FreeTypeFontGenerator(Gdx.files.internal("res/fonts/bavarg.ttf")).generateFont(14);

    private GameWorld gameWorld;
    private Player player;

    private final PlayerRenderer playerRenderer = new PlayerRenderer(spriteBatch);
    private final TerrainRenderer terrainRenderer = new TerrainRenderer();

    private Terrain terrain;
    private GameWorldRenderer gameWorldRenderer;

    private OrthographicCamera camera;
    private GuiWorldgem worldGem;
    public static  World physicalWorld;
    private Thread foodThread;
    private Thread saveThread;
    boolean showGemGui=false;
    private ParticleEffect particleEffect = new ParticleEffect();

    private final Stage stage = new Stage();
    private final ResourceBar foodBar;
    private final ResourceBar healthBar;
    private final ResourceBar xpBar;

    public static InventoryWindow inventoryWindow;

    private TerrainCoordinate selectedBox = null;
    private boolean selectedBoxInRange = false;
    private Color selectedBoxColor;
    
    private float displayAutosave = 0;

    private final InputMultiplexer inputMultiplexer = new InputMultiplexer();

    public PlayGameScreen(TrappedTilDoomGame trappedTilDoomGame) {
        super(trappedTilDoomGame);

        inputMultiplexer.addProcessor(commonInputProcessor);
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new PlayGameInputProcessor(game, this));

        particleEffect.load(Gdx.files.internal("res/particles/test.p"), Gdx.files.internal("res/particles/"));
        particleEffect.setPosition(100, 100);
        
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth * 0.5f, camera.viewportHeight * 0.5f, 0.0f);


        foodBar = new ResourceBar(
                new Texture(Gdx.files.internal("res/textures/guis/FoodON.png")),
                new Texture(Gdx.files.internal("res/textures/guis/FoodOFF.png"))
        );
        foodBar.inverse = true;
        foodBar.setPosition(Gdx.graphics.getWidth() / 2 + 20, 42 + 16);

        healthBar = new ResourceBar(
                new Texture(Gdx.files.internal("res/textures/guis/HealthON.png")),
                new Texture(Gdx.files.internal("res/textures/guis/HealthOFF.png"))
        );
        healthBar.setPosition(Gdx.graphics.getWidth() / 2 - 180, 42 + 16);

        xpBar = new ResourceBar(
                new Texture(Gdx.files.internal("res/textures/guis/xpON.png")),
                new Texture(Gdx.files.internal("res/textures/guis/xpOFF.png"))
        );
        xpBar.maximumValue = 20;
        xpBar.setPosition(Gdx.graphics.getWidth() / 2 - 164, 42);

        stage.addActor(foodBar);
        stage.addActor(healthBar);
        stage.addActor(xpBar);
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.position.set(camera.viewportWidth * 0.5f, camera.viewportHeight * 0.5f, 0.0f);

        stage.setViewport(width, height, true);
    }
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);

        GameData gameData = game.gameSlot.load();
        if (gameData == null) {
            generateNewTerrain();
        } else {
            terrain = gameData.terrain;
            setUpTerrain(terrain);
            gameWorld = gameData.gameWorld;
            player = gameData.player;
        }

        if (gameWorld == null) {
            gameWorld = new GameWorld();
        }

        if (player == null) {
            player = new Player(Gdx.graphics.getWidth() / 2, 751);
            player.hairTextureId = game.bodyPodBeginningScreen.selectedHair.value;
            player.headTextureId = game.bodyPodBeginningScreen.selectedHead.value;
            player.bodyTextureId = game.bodyPodBeginningScreen.selectedBody.value;
            player.legTextureId = game.bodyPodBeginningScreen.selectedLeg.value;
            player.shoeTextureId = game.bodyPodBeginningScreen.selectedShoe.value;
           
        }
        inventoryWindow = new InventoryWindow(skin,player.inventory);

        player.setRenderer(playerRenderer);

        player.initializePlayerModel(physicalWorld);


        //player.addItemHotBar(new PorkChopItem());
       // player.addItemHotBar(new WoodenAxeItem());
     //   player.addItemInventory(new PorkChopItem());
        player.applyPotion(new Potion(particleEffect,4,Gdx.files.internal("res/particles/speedPotion.p"), PotionType.SpeedPotion));

        playerRenderer.setHairTexture(game.bodyPodBeginningScreen.hairTextures[player.hairTextureId]);
        playerRenderer.setHeadTexture(game.bodyPodBeginningScreen.headTextures[player.headTextureId]);
        playerRenderer.setBodyTexture(game.bodyPodBeginningScreen.bodyTextures[player.bodyTextureId]);
        playerRenderer.setLegTexture(game.bodyPodBeginningScreen.legTextures[player.legTextureId]);
        playerRenderer.setShoeTexture(game.bodyPodBeginningScreen.shoeTextures[player.shoeTextureId]);
        
        worldGem = new GuiWorldgem("wGem", 273, 353, gameWorld);

        ClassUtil.dispose(gameWorldRenderer);
        gameWorldRenderer = new GameWorldRenderer(gameWorld, this);

        foodThread = new Thread(new FoodDecreaserThread(this));
        foodThread.start();
        
        saveThread = new Thread(new AutosaverThread(this, game.gameSlot));
        saveThread.start();


        
    }
    
    @Override
    public void hide() {
        foodThread.interrupt();
        foodThread = null;
        
        saveThread.interrupt();
        saveThread = null;
    }

    @Override
    protected void update(float delta) {
        stage.act(delta);

        gameWorldRenderer.update(delta);

        if (selectedBox != null) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && selectedBoxInRange) {
                Voxel brokenBlock = breakBlock(delta);
                if (brokenBlock != null) {
                    if(brokenBlock.type == TerrainType.CoalOre) {
                        player.increaseExperience(3);
                        getSoundManager().play(SoundManager.TrappedTilDoomSound.XP, 3, 0.5);
                    }
                }
            }
        }

        player.getPlayerModel().update();
        physicalWorld.step(1 / 60.0f, 6, 2);

        camera.update();
        player.update();
        displayAutosave = Math.max(displayAutosave - delta, 0);

        updateSelectedBox();
        
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
        {
             if(showGemGui)
                    {
                        showGemGui=false;
                    }
        }

        if (player.health < 20 && healthBar.getActions().size == 0) {
            healthBar.addAction(
                    Actions.repeat(RepeatAction.FOREVER, Actions.sequence(
                            Actions.rotateTo(15f, 0.1f),
                            Actions.rotateTo(-15f, 0.1f)
                    ))
            );
        }

        if (player.health >= 20 && healthBar.getActions().size > 0) {
           healthBar.clearActions();
        }
        
    }

    private Voxel breakBlock(float delta) {
        Voxel voxel = terrain.terrain[selectedBox.x][selectedBox.y];
        if (voxel == null || !voxel.type.breakable) {
            return null;
        }

        particleEffect.setPosition(selectedBox.x * 32 + 16, selectedBox.y * 32 + 16);

        float healthBefore = voxel.health;
//        if(voxel.type == TerrainType.Wood && player.getCurrentItem().type == ItemType.TOOL) {
//            voxel.health -= 60f * delta;
  //      }
        voxel.health -= 50f * delta;

        if (voxel.health > 0) {
            if (healthBefore > 99 && voxel.health < 99 ||
                healthBefore > 66 && voxel.health < 66 ||
                healthBefore > 33 && voxel.health < 33) {
                particleEffect.start();
            }
            return null;
        }

        terrain.terrain[selectedBox.x][selectedBox.y] = null;
        if (voxel.body != null) {
            player.getPlayerModel().removeContact(voxel.body);
            physicalWorld.destroyBody(voxel.body);
        }

        return voxel;
    }

    @Override
    protected void draw(float delta) {
        gameWorldRenderer.drawBackground();
        gameWorldRenderer.draw(camera);

        spriteBatch.begin();
        spriteBatch.setProjectionMatrix(camera.combined);

        terrain.draw();

        particleEffect.draw(spriteBatch, delta);
        player.draw(spriteBatch,delta);

        spriteBatch.end();

        if (!showGemGui) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Rectangle);
            shapeRenderer.setColor(selectedBoxColor);
            shapeRenderer.rect(selectedBox.x * 32f, selectedBox.y * 32f, 32f, 32f);
            shapeRenderer.end();
        }

        drawHUD();

//        debugRenderer.render(physicalWorld, camera.combined);
    }

    private void drawHUD() {
        foodBar.currentValue = player.food * foodBar.maximumValue / Player.MAX_FOOD;
        healthBar.currentValue = player.health * healthBar.maximumValue / Player.MAX_HEALTH;
        xpBar.currentValue = player.xp * xpBar.maximumValue / Player.MAX_XP;

        stage.draw();

        spriteBatch.begin();

        if(showGemGui) {
            worldGem.draw(spriteBatch, Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 - 100);
        }

        font.draw(spriteBatch, "" + player.level, Gdx.graphics.getWidth() / 2 , 42+ 32);
        if (displayAutosave > 0) {
            font.draw(spriteBatch, "Autosaving ...", 10, Gdx.graphics.getHeight() - 10);
        }
        inventoryWindow.draw(spriteBatch);
        spriteBatch.end();
    }
    
    

    @Override
    public void dispose() {
        super.dispose();
      
        ClassUtil.dispose(playerRenderer);
        ClassUtil.dispose(terrainRenderer);
        ClassUtil.dispose(gameWorldRenderer);
        
        font.dispose();
        spriteBatch.dispose();
        shapeRenderer.dispose();

        healthBar.dispose();
        foodBar.dispose();
        xpBar.dispose();
        skin.dispose();

        if (inventoryWindow != null) {
            inventoryWindow.dispose();
        }

        stage.dispose();
    }

    private void setUpPhysicalWorld() {
        for (int y = 0; y < terrain.height; y++) {
            for (int x = 0; x < terrain.width; x++) {
                Voxel voxel = terrain.terrain[x][y];
                if (voxel != null && voxel.type != TerrainType.Wood) {
                    voxel.body = createBody(physicalWorld, x, y);
                }
            }
        }

        setBoundingBox();
    }

    private void setBoundingBox() {
        Vector2 bottomLeftCorner = new Vector2(0, 0);
        Vector2 bottomRightCorner = new Vector2(terrain.width, 0);
        Vector2 topLeftCorner = new Vector2(0, terrain.height);
        Vector2 topRightCorner = new Vector2(terrain.width, terrain.height);

        BodyDef screenBorderDef = new BodyDef();
        screenBorderDef.position.set(0, 0);

        Body screenBorderBody = physicalWorld.createBody(screenBorderDef);

        EdgeShape screenBorderShape = new EdgeShape();
        screenBorderShape.set(bottomLeftCorner, bottomRightCorner);
        screenBorderBody.createFixture(screenBorderShape, 0);
        screenBorderShape.set(bottomRightCorner, topRightCorner);
        screenBorderBody.createFixture(screenBorderShape, 0);
        screenBorderShape.set(topRightCorner, topLeftCorner);
        screenBorderBody.createFixture(screenBorderShape, 0);
        screenBorderShape.set(topLeftCorner, bottomLeftCorner);
        screenBorderBody.createFixture(screenBorderShape, 0);
    }

    private static Body createBody(World boxWorld, float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        Body body = boxWorld.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.friction = 0;
        body.createFixture(fixtureDef);
        shape.dispose();
        return body;
    }

    public void setUpTerrain(Terrain terrain) {
        this.terrain = terrain;

        // Fix old saves by adding a world gem if missing
        boolean hasWorldGem = false;
        for (Voxel[] voxels : terrain.terrain) {
            for (Voxel voxel : voxels) {
                if (voxel != null && voxel.type == TerrainType.WorldGem) {
                    hasWorldGem = true;
                }
            }
        }

        if (!hasWorldGem) {
            int x = new Random().nextInt(terrain.width);
            int y = (int)(0.7f * terrain.height) - 1;
            terrain.terrain[x][y] = new Voxel(TerrainType.WorldGem);
        }

        terrain.setRenderer(terrainRenderer);

        if (physicalWorld != null) {
            physicalWorld.dispose();
        }

        physicalWorld = new World(new Vector2(0, -10), true);
        setUpPhysicalWorld();
    }

    public void generateNewTerrain() {
        int terrainWidth = Gdx.graphics.getWidth() / 32 + 1;
        int terrainHeight = Gdx.graphics.getHeight() / 32 + 1;

        terrain = new Terrain(terrainWidth, terrainHeight);
        terrain.generate();

        setUpTerrain(terrain);
    }

    private void updateSelectedBox() {
        Vector2 playerPosition = player.getPosition();
        Vector2 cursorPosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

        selectedBox = new TerrainCoordinate((int)cursorPosition.x / 32, (int)cursorPosition.y / 32);

        float distance = MathUtils.distancePointRect(playerPosition, new Rectangle(
                selectedBox.x * 32f, selectedBox.y * 32f, 32f, 32f
        ));

        selectedBoxInRange = distance <= 4 * 32;

        if (selectedBoxInRange) {
            if (terrain.terrain[selectedBox.x][selectedBox.y] != null) {
                selectedBoxColor = Color.YELLOW;
            } else {
                selectedBoxColor = Color.GREEN;
            }
        } else {
            selectedBoxColor = Color.RED;
        }
    }
    
    public void displayAutosaveMessage() {
        displayAutosave = 3; // Display autosaving message for 3 seconds ...
    }
    
    public Player getPlayer() {
        return player;
    }

    public GameData getData() {
        GameData gameData = new GameData();
        gameData.terrain = terrain;
        gameData.gameWorld = gameWorld;
        gameData.player = player;
        return gameData;
    }

    public void resetLightning() {
        gameWorldRenderer.resetLightning();
    }

    public void onAction() {


        if (!selectedBoxInRange) {
            return;
        }

        Voxel voxel = terrain.terrain[selectedBox.x][selectedBox.y];
        if (voxel != null && voxel.type == TerrainType.WorldGem) {
            showGemGui = true;
            return;
        }

       if (player.useCurrentItem()) {
            return;
        }

        if (voxel == null) {
            voxel = new Voxel(TerrainType.Dirt);
            terrain.terrain[selectedBox.x][selectedBox.y] = voxel;
            voxel.body = createBody(physicalWorld, selectedBox.x, selectedBox.y);
        }
    }
}
