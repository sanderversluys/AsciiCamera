package be.niob.asciicamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * 
 * @author Sander Versluys
 * @version 0.1 2012/05/10
 *
 */
class CameraView extends CameraViewBase {
	
	private static final String TAG = Util.TAG+":CameraViewBase";
	
	int mSize;
	int[] mRGBA;
	private Bitmap mBitmap;
    private int mViewMode;
    private Paint mPaint;
    
    public static final int     VIEW_MODE_RGBA = 0;
    public static final int     VIEW_MODE_GRAY = 1;
    public static final int		VIEW_MODE_ASCII = 2;
    
    private static final int SIDE = 20;
    
    private static final char[] ascii = " .`-_':,;^=+/\"|)\\<>)iv%xclrs{*}I?!][1taeo7zjLunT#JCwfy325Fp6mqSghVd4EgXPGZbYkOA&8U$@KHDBWNMR0Q".toCharArray();
    
    public CameraView(Context context) {
        super(context);
        mSize = 0;
        mViewMode = VIEW_MODE_ASCII;
        
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
		mPaint.setTypeface(Typeface.MONOSPACE);
		mPaint.setTextSize(SIDE);
    }
    
    @Override
    protected Bitmap processFrame(byte[] data) {
        int frameSize = getFrameWidth() * getFrameHeight();
        
        int[] rgba = mRGBA;

        final int view_mode = mViewMode;
        if (view_mode == VIEW_MODE_GRAY) {
        	
        	for (int i = 0; i < frameSize; i++) {
                int y = (0xff & ((int) data[i]));
                rgba[i] = 0xff000000 + (y << 16) + (y << 8) + y;
            }
        	
        	/*int min = 255;
        	int max = 0;
        	
        	for (int i = 0; i < frameSize; i++) {
                int y = (0xff & ((int) data[i]));
                if (Math.abs(y)<min) min = y;
                if (Math.abs(y)>min) max = y;
        	}
        	
            for (int i = 0; i < frameSize; i++) {
            	
                int y = (0xff & ((int) data[i]));
                
                int diff = max - min;
                
                diff = contrast;
                
                //if (diff > 100)
                	y = (int)(((double)(y) / 256) * diff);
                
                
                //y = (int)((double)(y) - min / (double)(max - min));

                rgba[i] = 0xff000000 + (y << 16) + (y << 8) + y;
            }*/
            
            /*Log.d(Util.TAG, "max:"+h);
            Log.d(Util.TAG, "min:"+l);*/
            
            mBitmap.setPixels(rgba, 0/* offset */, getFrameWidth() /* stride */, 0, 0, getFrameWidth(), getFrameHeight());
        } else if (view_mode == VIEW_MODE_RGBA) {
            for (int i = 0; i < getFrameHeight(); i++) {
                for (int j = 0; j < getFrameWidth(); j++) {
                	int index = i * getFrameWidth() + j;
                	int supply_index = frameSize + (i >> 1) * getFrameWidth() + (j & ~1);
                    int y = (0xff & ((int) data[index]));
                    int u = (0xff & ((int) data[supply_index + 0]));
                    int v = (0xff & ((int) data[supply_index + 1]));
                    y = y < 16 ? 16 : y;
                    
                    float y_conv = 1.164f * (y - 16);
                    int r = Math.round(y_conv + 1.596f * (v - 128));
                    int g = Math.round(y_conv - 0.813f * (v - 128) - 0.391f * (u - 128));
                    int b = Math.round(y_conv + 2.018f * (u - 128));

                    r = r < 0 ? 0 : (r > 255 ? 255 : r);
                    g = g < 0 ? 0 : (g > 255 ? 255 : g);
                    b = b < 0 ? 0 : (b > 255 ? 255 : b);

                    rgba[i * getFrameWidth() + j] = 0xff000000 + (b << 16) + (g << 8) + r;
                }
            }
            mBitmap.setPixels(rgba, 0/* offset */, getFrameWidth() /* stride */, 0, 0, getFrameWidth(), getFrameHeight());
        } else if (view_mode == VIEW_MODE_ASCII) {
        	
        	Canvas canvas = new Canvas(mBitmap);
        	canvas.drawColor(Color.BLACK);
        	
        	for (int i = 0; i < getFrameHeight(); i+=SIDE) {
                for (int j = 0; j < getFrameWidth(); j+=SIDE) {
                	int index = i * getFrameWidth() + j;
                	//int supply_index = frameSize + (i >> 1) * getFrameWidth() + (j & ~1);
                    int y = (0xff & ((int) data[index]));
                    y = y < 16 ? 16 : y;
                    rgba[i * getFrameWidth() + j] = 0xff000000 + (y << 16) + (y << 8) + y;
                    
                    int pos = (int)(((double)(y) / 256) * ascii.length);
                    canvas.drawText(Character.toString(ascii[pos]), j, i, mPaint);
                  
                    //Log.d(Util.TAG, "char: "+ Character.toString(ascii[pos]));
                }
        	}
        	
        }
        
        return mBitmap;
    }

	@Override
	protected void onPreviewStared(int previewWidth, int previewHeight) {
		/* Create a bitmap that will be used through to calculate the image to */
        mBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888);
    	mRGBA = new int[previewWidth * previewHeight];
	}

	@Override
	protected void onPreviewStopped() {
		mBitmap.recycle();
		mBitmap = null;
		mRGBA = null;
	}

	public void setViewMode(int viewMode) {
		mViewMode = viewMode;
	}
}