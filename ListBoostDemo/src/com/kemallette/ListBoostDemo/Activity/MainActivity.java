package com.kemallette.ListBoostDemo.Activity;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.kemallette.ListBoostDemo.R;
import com.kemallette.ListBoostDemo.Fragment.BoostExpandableListFragment;
import com.kemallette.ListBoostDemo.Fragment.BoostListFragment;
import com.kemallette.ListBoostDemo.Fragment.BuilderFrag;
import com.kemallette.ListBoostDemo.Fragment.InfoFrag;

public class MainActivity extends ActionBarActivity implements DemoBuilderListener{

	private static final String	TAG	          = "MainActivity";
	private static final String	INFO_FRAG	  = "infoFrag";
	private static final String	EXP_LIST_FRAG = "elvFrag";
	private static final String	LIST_FRAG	  = "lvFrag";

	public static final String	SWIPE	      = "swipe";
	public static final String	SLIDE	      = "slide";
	public static final String	DRAGDROP	  = "dragdrop";
	public static final String	MULTICHOICE	  = "multichoice";

	public enum ListType {
		LISTVIEW,
		EXPANDABLE_LISTVIEW
	}

	private DrawerLayout	            mDrawerLayout;
	private ActionBarDrawerToggle	    mDrawerToggle;
	private ViewGroup	                mLeftDrawerView;
	private ViewGroup	                mRightDrawerView;

	private InfoFrag	                mInfoFrag;
	private BoostExpandableListFragment	mExpListFrag;
	private BoostListFragment	        mListFrag;


	@Override
	protected void onCreate(final Bundle saveInstanceState){

		super.onCreate(saveInstanceState);

		setContentView(R.layout.main_activity);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		if (saveInstanceState == null)
			addInfoFrag();
		else{
			mInfoFrag = (InfoFrag) getSupportFragmentManager().findFragmentByTag(INFO_FRAG);
			mExpListFrag = (BoostExpandableListFragment) getSupportFragmentManager().findFragmentByTag(EXP_LIST_FRAG);
			mListFrag = (BoostListFragment) getSupportFragmentManager().findFragmentByTag(LIST_FRAG);
		}
	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState){

		super.onPostCreate(savedInstanceState);

		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}


	@Override
	protected void onStart(){

		super.onStart();

		if (mDrawerLayout == null || mLeftDrawerView == null || mRightDrawerView == null || mDrawerToggle == null){
			// Configure navigation drawer
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			mLeftDrawerView = (ViewGroup) findViewById(R.id.left_nav_drawer_content);
			mRightDrawerView = (ViewGroup) findViewById(R.id.right_nav_drawer_content);
			mDrawerToggle = new ActionBarDrawerToggle(this,
			                                          mDrawerLayout,
			                                          R.drawable.ic_drawer,
			                                          R.string.app_name,
			                                          R.string.welcome_and_instructions){

				/** Called when a drawer has settled in a completely closed state. */
				public void onDrawerClosed(View drawerView){

					if (drawerView.equals(mLeftDrawerView)){
						getSupportActionBar().setTitle(getTitle());
						supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
						mDrawerToggle.syncState();
					}
				}


				/** Called when a drawer has settled in a completely open state. */
				public void onDrawerOpened(View drawerView){

					if (drawerView.equals(mLeftDrawerView)){
						getSupportActionBar().setTitle(getString(R.string.app_name));
						supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
						mDrawerToggle.syncState();
					}
				}


				@Override
				public void onDrawerSlide(View drawerView, float slideOffset){

					// Avoid normal indicator glyph behaviour. This is to avoid glyph movement when
					// opening the right drawer
					// super.onDrawerSlide(drawerView, slideOffset);
				}
			};

			mDrawerLayout.setDrawerListener(mDrawerToggle); // Set the drawer toggle as the
															// DrawerListener
		}
	}


	@Override
	public boolean onPrepareOptionsMenu(Menu menu){

		// If the nav drawer is open, hide action items related to the content view
		for (int i = 0; i < menu.size(); i++)
			menu.getItem(i).setVisible(!mDrawerLayout.isDrawerOpen(mLeftDrawerView));

		return super.onPrepareOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item){

		switch(item.getItemId()){
			case android.R.id.home:
				mDrawerToggle.onOptionsItemSelected(item);

				if (mDrawerLayout.isDrawerOpen(mRightDrawerView))
					mDrawerLayout.closeDrawer(mRightDrawerView);

				return true;
		}

		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onConfigurationChanged(Configuration newConfig){

		super.onConfigurationChanged(newConfig);

		mDrawerToggle.onConfigurationChanged(newConfig);
	}


	@Override
	public void onStartDemo(final ListType listType, final Bundle features){

		if (listType == ListType.LISTVIEW){
			showListFrag(features);
		}else{
			showExpandableListFrag(features);
		}
	}


	private void addInfoFrag(){

		if(mInfoFrag == null)
			mInfoFrag = InfoFrag.newInstance();

		final FragmentTransaction mTransaction = getSupportFragmentManager()
			.beginTransaction()
            .add(R.id.container, mInfoFrag, INFO_FRAG)
            .setTransition(FragmentTransaction.TRANSIT_NONE);

		mTransaction.commit();
	}


	private void showExpandableListFrag(final Bundle features){

		if (mExpListFrag == null)
			mExpListFrag = BoostExpandableListFragment.newInstance();

		mExpListFrag.onEnableFeatures(features);

		final FragmentTransaction mTransaction = getSupportFragmentManager()
			.beginTransaction()
		    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
		    .replace(R.id.container, mExpListFrag, EXP_LIST_FRAG)
		    .addToBackStack(null);

		mTransaction.commit();
	}


	private void showListFrag(final Bundle features){

		if (mListFrag == null)
			mListFrag = BoostListFragment.newInstance(features);

		mListFrag.onEnableFeatures(features);

		final FragmentTransaction mTransaction = getSupportFragmentManager()
			.beginTransaction()
		    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
		    .replace(R.id.container, mListFrag, LIST_FRAG)
		    .addToBackStack(null);
		
		mTransaction.commit();
	}

}
