package be.niob.asciicamera;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

/**
 * 
 * @author Sander Versluys
 * @version 0.1 2012/05/10
 *
 */
public class CameraActivity extends Activity {
	
    private static final String TAG = Util.TAG+":CameraActivity";

    private MenuItem mItemPreviewRGBA;
    private MenuItem mItemPreviewGray;
    private MenuItem mItemPreviewAscii;
    private CameraView mView;


    public CameraActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mView = new CameraView(this);
        setContentView(mView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu");
        mItemPreviewRGBA = menu.add("Preview RGBA");
        mItemPreviewGray = menu.add("Preview GRAY");
        mItemPreviewAscii = menu.add("Preview ASCII");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "Menu Item selected " + item);
        if (item == mItemPreviewRGBA)
        	mView.setViewMode(CameraView.VIEW_MODE_RGBA);
        else if (item == mItemPreviewGray)
        	mView.setViewMode(CameraView.VIEW_MODE_GRAY);
        else if (item == mItemPreviewAscii)
        	mView.setViewMode(CameraView.VIEW_MODE_ASCII);
        return true;
    }
}
