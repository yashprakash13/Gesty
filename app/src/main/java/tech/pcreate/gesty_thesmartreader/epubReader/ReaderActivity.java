/*
 * Copyright (c) @ 2019 Yash Prakash
 */

package tech.pcreate.gesty_thesmartreader.epubReader;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import tech.pcreate.gesty_thesmartreader.MainActivity; //Copyright (c) @ 2019 Yash Prakash
import tech.pcreate.gesty_thesmartreader.R;
import tech.pcreate.gesty_thesmartreader.gestyMode.TurnDetector;
import tech.pcreate.gesty_thesmartreader.utils.AppConstants;
import tech.pcreate.gesty_thesmartreader.utils.SwipeDetector;

import static com.github.florent37.runtimepermission.RuntimePermission.askPermission;
import static tech.pcreate.gesty_thesmartreader.utils.AppConstants.FAST;
import static tech.pcreate.gesty_thesmartreader.utils.AppConstants.PRETTY_FAST;
import static tech.pcreate.gesty_thesmartreader.utils.AppConstants.SLOW;

public class ReaderActivity extends AppCompatActivity implements
                            ScrollingOptionsBottomSheetFragment.BottomSheetClick,
                            ScrollingOptionsBottomSheetFragment.CheckedChangeListener{

    private static final String DEBUG_TAG = ReaderActivity.class.getSimpleName();
    private static String mFileLocation;
    private static WebView webView;
    private Toolbar mToolbar;
    private boolean isImmersiveModeEnabled;
    private boolean isGestyModeEnabled = false;

    private static int mCurrentPageNumber = 1;
    private boolean isAccepted = false;

    private TurnDetector mTurnDetector;
    private static Timer timer  = new Timer();

    private static boolean firstTimeGesty = true;
    public static boolean isScrolling = false;

    private ScrollingOptionsBottomSheetFragment fragment;
    private SharedPreferences sharedPreferences;

    private static int readSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTurnDetector = new TurnDetector(this);

        mFileLocation = getIntent().getStringExtra("loc");
        Log.e("loc got is= ", mFileLocation);

        webView = findViewById(R.id.webviewBook);
        showPage(mCurrentPageNumber);

        new SwipeDetector(webView).setOnSwipeListener((v, swipeType) -> {
            if(swipeType==SwipeDetector.SwipeTypeEnum.LEFT_TO_RIGHT) {
                //Toast.makeText(getApplicationContext(), "Previous", Toast.LENGTH_SHORT).show();
                showPage(mCurrentPageNumber - 1);
            }
            else if( swipeType == SwipeDetector.SwipeTypeEnum.RIGHT_TO_LEFT) {
                //Toast.makeText(getApplicationContext(), "Next", Toast.LENGTH_SHORT).show();
                showPage(mCurrentPageNumber + 1);
            }
        });

        sharedPreferences = getSharedPreferences(getString(R.string.scrollMode), MODE_PRIVATE);
        getReadSpeed();
    }

    private void getReadSpeed() {
        readSpeed = getSharedPreferences(getString(R.string.scroll_mode_integer), MODE_PRIVATE).getInt(getString(R.string.scroll_mode_integer), SLOW);
        //Log.e("read Speed=  ", String.valueOf(readSpeed));
    }//Copyright (c) @ 2019 Yash Prakash

    @Override
    protected void onResume() {
        super.onResume();
        getReadSpeed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private static void showPage(int currentPageNumber) {
        Book book;
        try {
            book = (new EpubReader()).readEpub(new FileInputStream(mFileLocation));
            String data = new String(book.getContents().get(currentPageNumber).getData());

            mCurrentPageNumber = currentPageNumber;
            webView.loadDataWithBaseURL(mFileLocation, data, "text/html", "UTF-8", null);
            //webView.scrollTo(0, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toggleFullScreenMode() {

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;

        isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);

        if (isImmersiveModeEnabled) {
            mToolbar.setVisibility(View.VISIBLE);
        } else {
            mToolbar.setVisibility(View.GONE);
        }

        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    private boolean isFullScreen(){
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        return ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        super.dispatchTouchEvent(ev);
        return webView.onTouchEvent(ev);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reader_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.full_screen_mode){
            toggleFullScreenMode();
        }else if (id == R.id.gesty_mode){
            if (!isGestyModeEnabled) {
                showBottomSheetOrNot();
            }
            else enableGestyMode(false);
        }

        return true;
    }

    private void showBottomSheetOrNot() {
        boolean b = sharedPreferences.getBoolean(getString(R.string.remember_scroll), false);
        if (b){
            enableGestyMode(true);
        }else{

            fragment = new ScrollingOptionsBottomSheetFragment();
            fragment.show(getSupportFragmentManager(), "SCROLL_OPTIONS");
            fragment.setBottomSheetClickListener(this);
            fragment.setChangeListener(this);
        }

    }

    private void enableGestyMode(boolean b) {

        if (b){
            if(checkPermissions()){
                isGestyModeEnabled = true;
                Toast.makeText(this, getString(R.string.g_mode_on), Toast.LENGTH_SHORT).show();
                /*Copyright (c) @ 2019 Yash Prakash
                 */
                mTurnDetector.startDetector();
                //startScrolling(true);
            }

        }else {
            isGestyModeEnabled = false;
            Toast.makeText(this, getString(R.string.g_mode_off), Toast.LENGTH_SHORT).show();
            mTurnDetector.stopDetector();
            startScrolling(false);
            firstTimeGesty = true;
        }
    }

    @Override
    public void onBackPressed() {
        //Works the opposite ;)
        if (isFullScreen() ) toggleFullScreenMode();
        else{
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public static void turnPage(int i){
        if (i == AppConstants.NEXT) showPage( mCurrentPageNumber + 1);
        else if (mCurrentPageNumber > 1 && !firstTimeGesty) showPage(mCurrentPageNumber - 1);
        firstTimeGesty = false;
        startScrolling(false);
        /*Copyright (c) @ 2019 Yash Prakash
         */
        webView.scrollTo(0, 0);
        new Handler().postDelayed(() -> startScrolling(true), 3000);

    }

    public static void startScrolling(boolean b){
        if (b ){
            timer = new Timer();
            isScrolling = true;
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    webView.scrollBy(0,1);
                }
            },0, readSpeed);
        }else{
            timer.cancel();
            timer.purge();
            isScrolling = false;
        }
    }

    private boolean checkPermissions() {
        askPermission(this)
                .request(Manifest.permission.CAMERA)
                .onAccepted((result) -> {
                    isAccepted = true;
                })
                .onDenied((result) -> {
                    Snackbar.make(webView, getString(R.string.permissions_for_gesty_mode_needed), Snackbar.LENGTH_SHORT).show();

                    new AlertDialog.Builder(this)
                            .setMessage(getString(R.string.give_camera_permission))
                            .setPositiveButton(getString(R.string.okay_string), (dialog, which) -> result.askAgain()) // ask again
                            .setNegativeButton(getString(R.string.no_string), (dialog, which) -> dialog.dismiss())
                            .show();

                })
                .onForeverDenied((result) -> {

                    Snackbar.make(webView, getString(R.string.permissions_for_gesty_mode_needed), Snackbar.LENGTH_SHORT)
                            .setAction(getString(R.string.go_to_settings), view -> result.goToSettings()).show();

                })
                .ask();

        return isAccepted;
    }

    @Override
    public void onBottomSheetClick(int i) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (i == 0) { editor.putString(getString(R.string.scrollMode), String.valueOf(SLOW));  readSpeed = SLOW; }
        else if (i == 1) { editor.putString(getString(R.string.scrollMode), String.valueOf(AppConstants.FAST));  readSpeed = FAST;  }
        else { editor.putString(getString(R.string.scrollMode), String.valueOf(AppConstants.PRETTY_FAST)); readSpeed = PRETTY_FAST; }

        editor.apply();

        enableGestyMode(true);

    }

    @Override
    public void onChecked(boolean b) {
        if (b){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(getString(R.string.remember_scroll), true);
            editor.apply();
            fragment.dismissBottomSheet();
        }
    }
}
