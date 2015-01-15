package ie.itcarlow.loadlevelfromxmlexample;

import java.util.Vector;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.SAXUtils;
import org.andengine.util.level.IEntityLoader;
import org.andengine.util.level.LevelLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.xml.sax.Attributes;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

public class Main extends BaseGameActivity implements IUpdateHandler {
	//dimensions of camera
	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;
	//tags for the level file
	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_WIDTH = "width";
	private static final String TAG_ENTITY_ATTRIBUTE_HEIGHT = "height";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM = "platform";
	//scene
	private Scene mScene;
	//our list of all the platforms in the level
	private Vector<Platform> listOfPlatforms;

	SmoothCamera mSmoothCamera;
	private final Context c = this;
	

	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		//we create our smooth camera. Parameters are: x,y,width,height,x velocity,y velocity,maxZoomfactorchange
		//the velocity parameters can be used to create a side scrolling game
		mSmoothCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT,
				100, 0, 1.0f);
		//Tell the engine we are using a smooth camera
				EngineOptions engine = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				mSmoothCamera);

				return engine;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		// TODO Auto-generated method stub
		listOfPlatforms = new Vector<Platform>();
	
		loadGfx();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}
	private void loadGfx()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

	}
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		// TODO Auto-generated method stub
		this.mScene = new Scene();
		
		final LevelLoader levelLoader = new LevelLoader();
		levelLoader.setAssetBasePath("level/");
		
		levelLoader.registerEntityLoader(LevelConstants.TAG_LEVEL,
				new IEntityLoader()
				{
			//load the dimensions of the level. Not used in this tutorial
					@Override
					public IEntity onLoadEntity(final String pEntityName,
					final Attributes pAttributes)
					{
						final int width = SAXUtils.getIntAttributeOrThrow(pAttributes,
								LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
						final int height = SAXUtils.getIntAttributeOrThrow(pAttributes,
								LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
						Main.this.runOnUiThread(new Runnable()
					{
					@Override
					public void run()
					{
						Toast.makeText(Main.this,
						"Loaded level with a width of "+width + "and a height of "+height,
						Toast.LENGTH_LONG).show();
					}
					});
						return Main.this.mScene;
					}
				});
		
		
					levelLoader.registerEntityLoader(TAG_ENTITY, new IEntityLoader()
				{


				@Override
				public IEntity onLoadEntity(String pEntityName,
						Attributes pAttributes) {
					// TODO Auto-generated method stub
					//tags we are looking for
					final int x = SAXUtils.getIntAttributeOrThrow(pAttributes,TAG_ENTITY_ATTRIBUTE_X);
					final int y = SAXUtils.getIntAttributeOrThrow(pAttributes,TAG_ENTITY_ATTRIBUTE_Y);
					final int width = SAXUtils.getIntAttributeOrThrow(pAttributes,TAG_ENTITY_ATTRIBUTE_WIDTH);
					final int height = SAXUtils.getIntAttributeOrThrow(pAttributes,TAG_ENTITY_ATTRIBUTE_HEIGHT);
					final String type = SAXUtils.getAttributeOrThrow(pAttributes,TAG_ENTITY_ATTRIBUTE_TYPE);
					//create a platform. This will be added to our list
					final Platform spr;

					//if the type of the entity we have found is of type platform
					if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM))
					{
						spr = new Platform(c, getTextureManager(), x, y, width,height);
						listOfPlatforms.add(spr);
						return spr.getSprite();
					} 
					else
					{
						return null;
					}
				}
				});
					
				levelLoader.loadLevelFromAsset(this.getAssets(), "level1.lvl");
				populatePlatforms();

				pOnCreateSceneCallback.onCreateSceneFinished(this.mScene);
	}
	//calls the populate method for each platform which sets up its sprite
	public void populatePlatforms()
	{
		int size = listOfPlatforms.size();
		for (int i = 0; i < size; i++)
		{
			listOfPlatforms.get(i).Populate(this.mEngine, mScene);
		}
	}
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		// TODO Auto-generated method stub
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}
