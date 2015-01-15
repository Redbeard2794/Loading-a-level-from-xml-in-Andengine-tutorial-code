package ie.itcarlow.loadlevelfromxmlexample;
//import statements
import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.content.Context;

public class Platform {
//variables
private final float platformX;//x coordinate
private final float platformY;//y coordinate
private ITextureRegion platformTextureRegion;
private Sprite platformSprite;
private final float w;//width
private final float h;//height


public Platform(Context c, TextureManager t, int x, int y, int width, int height)
{
	loadGFX(c, t);//pass the context and texture manager to loadGFX
	platformX = x;
	platformY = y;
	w=width;
	h=height;
}
private void loadGFX(Context c, TextureManager t)
{
	BitmapTextureAtlas platformTexture = new BitmapTextureAtlas(t, 120, 60);
	platformTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(platformTexture,
	c.getAssets(), "plat2.png",0,0);
	platformTexture.load();
}
public void Populate(Engine c, Scene s)
{
	platformSprite = new Sprite(platformX, platformY, w, h, platformTextureRegion,
	c.getVertexBufferObjectManager());
	s.attachChild(platformSprite);
}
public void Update()
{
}
//gets
public Sprite getSprite(){return platformSprite;}
//sets
public void setSprite(Sprite sp){platformSprite = sp;}
}
