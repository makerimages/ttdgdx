package trappedtildoom.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import java.io.FileNotFoundException;
import trappedtildoom.core.TrappedTilDoomGame;
import trappedtildoom.core.input.MainMenuInputProcessor;
import trappedtildoom.core.service.SoundManager;
import trappedtildoom.core.util.MutableInteger;

public class BodyPodBeginningScreen extends TrappedTilDoomScreen {
    
    private final InputMultiplexer inputMultiplexer = new InputMultiplexer();
    
    private final Stage stage = new Stage();
    private final Skin skin = new Skin(Gdx.files.internal("res/ui/uiskin.json"));
    private final Label title;
    
    public final Texture[] hairTextures = new Texture[] {
        new Texture(Gdx.files.internal("res/textures/characters/BodyPod/Hair/Hair1.png")),
        new Texture(Gdx.files.internal("res/textures/characters/BodyPod/Hair/Hair2.png")),
        new Texture(Gdx.files.internal("res/textures/characters/BodyPod/Hair/Hair3.png")),
        new Texture(Gdx.files.internal("res/textures/characters/BodyPod/Hair/Hair4.png"))
    };

    public final Texture[] headTextures = new Texture[] {
        new Texture(Gdx.files.internal("res/textures/characters/BodyPod/Heads/Head1.png")),
        new Texture(Gdx.files.internal("res/textures/characters/BodyPod/Heads/Head2.png")),
        new Texture(Gdx.files.internal("res/textures/characters/BodyPod/Heads/Head3.png"))
    };

    public final Texture[] bodyTextures = new Texture[] {
        new Texture(Gdx.files.internal("res/textures/characters/BodyPod/Body/Body1.png")),
        new Texture(Gdx.files.internal("res/textures/characters/BodyPod/Body/Body2.png"))
    };

    public final Texture[] legTextures = new Texture[] {
        new Texture(Gdx.files.internal("res/textures/characters/BodyPod/Legs/legs1.png")),
        new Texture(Gdx.files.internal("res/textures/characters/BodyPod/Legs/legs2.png")),
        new Texture(Gdx.files.internal("res/textures/characters/BodyPod/Legs/legs3.png")),
        new Texture(Gdx.files.internal("res/textures/characters/BodyPod/Legs/legs4.png"))
    };

    public final Texture[] shoeTextures = new Texture[] {
        new Texture(Gdx.files.internal("res/textures/characters/BodyPod/Shoes/shoe1.png")),
        new Texture(Gdx.files.internal("res/textures/characters/BodyPod/Shoes/shoe2.png")),
        new Texture(Gdx.files.internal("res/textures/characters/BodyPod/Shoes/shoe3.png")),
        new Texture(Gdx.files.internal("res/textures/characters/BodyPod/Shoes/shoe4.png"))
    };
    
    public final MutableInteger selectedHair = new MutableInteger(0);
    public final MutableInteger selectedHead = new MutableInteger(0);
    public final MutableInteger selectedBody = new MutableInteger(0);
    public final MutableInteger selectedLeg = new MutableInteger(0);
    public final MutableInteger selectedShoe = new MutableInteger(0);
    
    public BodyPodBeginningScreen(TrappedTilDoomGame trappedTilDoomGame) throws FileNotFoundException {
        super(trappedTilDoomGame);

        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new MainMenuInputProcessor(trappedTilDoomGame));
        
        title = new Label("", skin);
        title.setFontScale(2);
        
        Label hairText = new Label("Hair", skin);
        Label headText = new Label("Head", skin);
        Label bodyText = new Label("Body", skin);
        Label legsText = new Label("Legs", skin);
        Label shoesText = new Label("Shoes", skin);
        Label hairText1 = new Label("Hair", skin);
        Label headText1 = new Label("Head", skin);
        Label bodyText1 = new Label("Body", skin);
        Label legsText1 = new Label("Legs", skin);
        Label shoesText1 = new Label("Shoes", skin);

        TextButton previousHairButton = new TextButton("<", skin);
        previousHairButton.addListener(new BodyPartSelectionListener(selectedHair, hairTextures.length, -1));

        TextButton previousHeadButton = new TextButton("<", skin);
        previousHeadButton.addListener(new BodyPartSelectionListener(selectedHead, headTextures.length, -1));

        TextButton previousBodyButton = new TextButton("<", skin);
        previousBodyButton.addListener(new BodyPartSelectionListener(selectedBody, bodyTextures.length, -1));

        TextButton previousLegsButton = new TextButton("<", skin);
        previousLegsButton.addListener(new BodyPartSelectionListener(selectedLeg, legTextures.length, -1));
        
        TextButton previousShoesButton = new TextButton("<", skin);
        previousShoesButton.addListener(new BodyPartSelectionListener(selectedShoe, shoeTextures.length, -1));
        
        TextButton nextHairButton = new TextButton(">", skin);
        nextHairButton.addListener(new BodyPartSelectionListener(selectedHair, hairTextures.length, +1));

        TextButton nextHeadButton = new TextButton(">", skin);
        nextHeadButton.addListener(new BodyPartSelectionListener(selectedHead, headTextures.length, +1));

        TextButton nextBodyButton = new TextButton(">", skin);
        nextBodyButton.addListener(new BodyPartSelectionListener(selectedBody, bodyTextures.length, +1));

        TextButton nextLegsButton = new TextButton(">", skin);
        nextLegsButton.addListener(new BodyPartSelectionListener(selectedLeg, legTextures.length, +1));
        
        TextButton nextShoesButton = new TextButton(">", skin);
        nextShoesButton.addListener(new BodyPartSelectionListener(selectedShoe, shoeTextures.length, +1));
        
        TextButton doneButton = new TextButton("Done", skin);
        doneButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                getSoundManager().play(SoundManager.TrappedTilDoomSound.CLICK);
                game.setScreen(game.playGameScreen);
            }
        });
        
        Table table=new Table();
        table.setFillParent(true);
        table.add(title);
        table.layout();
        table.align(Align.top+Align.center);
        
        stage.addActor(table);
        
        Table table1 = new Table();
        table1.setFillParent(true);

        table1.add(hairText1);
        table1.add(previousHairButton);
        table1.row();
        table1.add(headText1);
        table1.add(previousHeadButton);
        table1.row();
        table1.add(bodyText1);
        table1.add(previousBodyButton);
        table1.row();
        table1.add(legsText1);
        table1.add(previousLegsButton);
        table1.row();
        table1.add(shoesText1);
        table1.add(previousShoesButton);
        table1.layout();
        table1.align(Align.left);
        table1.setPosition(500f,60f);

        stage.addActor(table1);
        
        Table table2 = new Table();
        table2.setFillParent(true);

        table2.add(nextHairButton);
        table2.add(hairText);
        table2.row();
        table2.add(nextHeadButton);
        table2.add(headText);
        table2.row();
        table2.add(nextBodyButton);
        table2.add(bodyText);
        table2.row();
        table2.add(nextLegsButton);
        table2.add(legsText);
        table2.row();
        table2.add(nextShoesButton);
        table2.add(shoesText);
        table2.layout();
        table2.align(Align.left);
        table2.setPosition(800f,60f);
        stage.addActor(table2);
        Table table3=new Table();
        table3.setFillParent(true);
        table3.add(doneButton);
        table3.layout();
        table3.align(Align.left);
        table3.setPosition(650f,-20f);
        stage.addActor(table3);
    }

    @Override
    public void resize(int width, int height) {
        stage.setViewport(width, height, true);
    }
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
        title.setText("BodyPod character creation for: " + game.gameSlot.fileName);
    }

    @Override
    protected void update(float delta) {
        stage.act(delta);
    }

    @Override
    protected void draw(float delta) {
        SpriteBatch batch = new SpriteBatch();
        
        Gdx.gl.glClearColor(0, 0.7f, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        batch.draw(hairTextures[selectedHair.value], Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 64, 64);
        batch.draw(headTextures[selectedHead.value], Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 64, 64);
        batch.draw(bodyTextures[selectedBody.value], Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 64, 64);
        batch.draw(legTextures[selectedLeg.value], Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 64, 64);
        batch.draw(shoeTextures[selectedShoe.value], Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 64, 64);
        batch.end();

        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        
        stage.dispose();
        skin.dispose();
        
        disposeTextures(hairTextures);
        disposeTextures(headTextures);
        disposeTextures(bodyTextures);
        disposeTextures(legTextures);
        disposeTextures(shoeTextures);
    }
    
    private static void disposeTextures(Texture[] textures) {
        for (Texture texture : textures) {
            texture.dispose();
        }
    }
    
    private class BodyPartSelectionListener extends ChangeListener {
        
        private final int partCount;
        private final int direction;
        
        private MutableInteger selectedPart;
        
        public BodyPartSelectionListener(MutableInteger selectedPart, int partCount, int direction) {
            this.partCount = partCount;
            this.direction = direction;
            this.selectedPart = selectedPart;
        }
        
        @Override
        public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            getSoundManager().play(SoundManager.TrappedTilDoomSound.CLICK);
            
            int newSelection = selectedPart.value += direction;
            if (newSelection < 0) {
                newSelection = partCount - 1;
            }
            if (newSelection >= partCount) {
                newSelection = 0;
            }
            
            selectedPart.value = newSelection;
        }
    }
}
