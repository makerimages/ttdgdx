package trappedtildoom.gamelogic.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import trappedtildoom.gamelogic.gameobject.Player;

import java.util.ArrayList;
import java.util.List;
import trappedtildoom.core.service.SoundManager;

public class PlayerModel 
{

    public static final float BOX_TO_WORLD = 32f;
    private static final float WORLD_TO_BOX = 1 / BOX_TO_WORLD;
    private static final float MAX_VELOCITY = 1.5f;
  
    private final Body body;
    private final Fixture bodyFixture;
    private final Fixture footSensorFixture;
    private final List<Fixture> contacts = new ArrayList<>();

    public PlayerModel(World world, float x, float y, Player player)
    {
        body = createBody(world, x, y);
        bodyFixture = createBodyFixture(body);
        footSensorFixture = createFootSensorFixture(body);
        world.setContactListener(new MyContactListener());

        bodyFixture.setUserData(player);
    }

    public void update() 
    {
        Vector2 velocity = body.getLinearVelocity();
        Vector2 position = body.getPosition();

        if (Math.abs(velocity.x) > MAX_VELOCITY) 
        {
            velocity.x = Math.signum(velocity.x) * MAX_VELOCITY;
            body.setLinearVelocity(velocity);
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) 
        {
            body.setLinearVelocity(velocity.x * 0.1f, velocity.y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && velocity.x > -MAX_VELOCITY) 
        {
            body.applyLinearImpulse(-1, 0, position.x, position.y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) && velocity.x < MAX_VELOCITY)
        {
            body.applyLinearImpulse(1, 0, position.x, position.y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) && contacts.size() > 0) 
        {
            body.applyLinearImpulse(0, 1, position.x, position.y);
        }

        body.getFixtureList().get(0).setFriction(contacts.size() > 0 ? 0.3f : 0);
    }

    private static Body createBody(World b2World, float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(convertToBox(x), convertToBox(y));
        bodyDef.fixedRotation = true;

        return b2World.createBody(bodyDef);
    }

    private static Fixture createBodyFixture(Body body) {
        PolygonShape dynamicBox = new PolygonShape();
        dynamicBox.setAsBox(0.25f, 0.48f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = dynamicBox;
        fixtureDef.density = 1;
        fixtureDef.friction = 0.3f;

        Fixture fixture = body.createFixture(fixtureDef);

        dynamicBox.dispose();

        return fixture;
    }

    private static Fixture createFootSensorFixture(Body body) 
    {
        CircleShape dynamicCircle = new CircleShape();
        dynamicCircle.setRadius(0.25f);
        dynamicCircle.setPosition(new Vector2(0, -0.5f));

        Fixture footSensorFixture = body.createFixture(dynamicCircle, 0);
        footSensorFixture.setSensor(true);
        dynamicCircle.dispose();

        return footSensorFixture;
    }

    public void removeContact(Body x)
    {
        for (Fixture f : x.getFixtureList()) 
        {
            if (contacts.contains(f)) 
            {
                contacts.remove(f);
            }
        }
    }

    public float getPositionX() 
    {
        return convertToWorld(body.getPosition().x);
    }

    public float getPositionY() 
    {
        return convertToWorld(body.getPosition().y);
    }

    private static float convertToBox(float worldCoordinate)
    {
        return worldCoordinate * WORLD_TO_BOX;
    }

    public static float convertToWorld(float boxCoordinate) 
    {
        return boxCoordinate * BOX_TO_WORLD;
    }

    private class MyContactListener implements ContactListener
    {
        @Override
        public void beginContact(Contact contact) 
        {
            if (contact.getFixtureA() == footSensorFixture) 
            {
                contacts.add(contact.getFixtureB());
            }
            if (contact.getFixtureB() == footSensorFixture)
            {
                contacts.add(contact.getFixtureA());
            }
        }

        @Override
        public void endContact(Contact contact)
        {
            if (contact.getFixtureA() == footSensorFixture) 
            {
                if (contacts.contains(contact.getFixtureB())) 
                {
                    contacts.remove(contact.getFixtureB());
                }
            }
            if (contact.getFixtureB() == footSensorFixture)
            {
                if (contacts.contains(contact.getFixtureA())) 
                {
                    contacts.remove(contact.getFixtureA());
                }
            }
        }

        @Override
        public void preSolve(Contact contact, Manifold manifold)
        {

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse contactImpulse) {
            if (contactImpulse.getNormalImpulses()[0] > 4.0) {
                if (contact.getFixtureA() == bodyFixture || contact.getFixtureB() == bodyFixture) {
                    Player player = (Player)bodyFixture.getUserData();
                    player.decreaseHealth((int)((contactImpulse.getNormalImpulses()[0] - 4.0) * 10));
                    SoundManager sm=new SoundManager();
                    sm.play(SoundManager.TrappedTilDoomSound.HURT);
                }
            }
        }
        
    }
   
}
