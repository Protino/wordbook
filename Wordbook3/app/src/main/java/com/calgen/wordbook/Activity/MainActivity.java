package com.calgen.wordbook.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.calgen.wordbook.Fragments.Home;
import com.calgen.wordbook.Fragments.Mastered;
import com.calgen.wordbook.Fragments.NewWord;
import com.calgen.wordbook.Interfaces.NavigationDrawerCallbacks;
import com.calgen.wordbook.Fragments.NavigationDrawerFragment;
import com.calgen.wordbook.Models.Word;
import com.calgen.wordbook.R;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks, NewWord.addNewWordListener {

    private static String TAG = "MainActivity";
    public static String PACKAGE_NAME;

    private Word newWord;
    private static boolean isAdded = false;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;

    private FragmentManager.OnBackStackChangedListener
            mOnBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {

            syncActionBarArrowState();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
//        mToolbar.setNavigationOnClickListener();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        //Set OnBackStackChangedListener to handle navigation drawer enabler
        getSupportFragmentManager().addOnBackStackChangedListener(mOnBackStackChangedListener);
        //Set ToolbarNavigationClickListener to listen clicks on up-caret in lower level fragments.
        mNavigationDrawerFragment.getActionBarDrawerToggle().setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        });
        // populate the navigation drawer
        //mNavigationDrawerFragment.setUserData("John Doe", "johndoe@doe.com", BitmapFactory.decodeResource(getResources(), R.drawable.avatar));
        Intent intent = getIntent();
        //Get the action of the intent
        String action = intent.getAction();
        //Get the type of intent
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null)
            if ("text/plain".equals(type))
                handleSentText(intent);

        //set PACKAGE_NAME
        PACKAGE_NAME = getApplicationContext().getPackageName();
    }

    private void handleSentText(Intent intent) {
        Bundle bundle = new Bundle();
        bundle.putString("newword", intent.getStringExtra(Intent.EXTRA_TEXT));
        Fragment newFragment = new NewWord();
        newFragment.setArguments(bundle);
        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, newFragment).addToBackStack(null).commit();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG,"AddToBAckStack");
            try {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, newFragment).commit();
            } catch (Exception f) {
                e.printStackTrace();
                Log.d(TAG, "Cannot start the fragment");
            }
        }

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
       Fragment fragment = null;
        switch (position) {

            case 0:
                fragment = new Home();
                break;
            case 1:
                fragment = new Mastered();
                break;
            default:
                break;

        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStackImmediate();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d(TAG, "onOptionsItemSelected");
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        getSupportFragmentManager().removeOnBackStackChangedListener(mOnBackStackChangedListener);
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mNavigationDrawerFragment.getActionBarDrawerToggle().syncState();
    }

    private void syncActionBarArrowState() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            mNavigationDrawerFragment.disableNavigationDrawer(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mNavigationDrawerFragment.disableNavigationDrawer(false);
        }
    }

    public Word getNewWord() {
        if (isAdded) {
            isAdded = false;
            return this.newWord;
        } else
            return null;
    }

    @Override
    public void addNewWord(Word word) {
        this.newWord = word;
        isAdded = true;
    }
}
