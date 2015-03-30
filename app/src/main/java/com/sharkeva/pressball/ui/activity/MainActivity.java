package com.sharkeva.pressball.ui.activity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.sharkeva.pressball.R;
import com.sharkeva.pressball.dao.CategoryDao;
import com.sharkeva.pressball.dao.imp.CategoryDaoImp;
import com.sharkeva.pressball.entities.Category;
import com.sharkeva.pressball.services.Listener;
import com.sharkeva.pressball.services.NFUpdateCenter;
import com.sharkeva.pressball.ui.OnFragmentCreatedListener;
import com.sharkeva.pressball.ui.ToolbarActivity;
import com.sharkeva.pressball.ui.adapter.NavigationAdapter;
import com.sharkeva.pressball.ui.adapter.NewsPagerAdapter;
import com.sharkeva.pressball.ui.fragment.BaseNewsFlashFragment;

import org.jsoup.nodes.Document;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements OnFragmentCreatedListener, ToolbarActivity {
    //Views
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView listView;
    private ViewPager pager;
    private PagerSlidingTabStrip tabs;
    private NewsPagerAdapter newsPagerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBarCircularIndeterminate spinner;
    private View errorView;
    private ButtonFlat errorButton;

    //Dao
    private CategoryDao categoryDAO;
    private Category baseCategory;

    //service
    private NFUpdateCenter updateCenter;

    private boolean firstStart;
    private boolean error;
    private static final String KEY_FIRSTSTART = "firstStart";
    private static final String KEY_ERROR = "error";
    public static final String KEY_BASECATEGORY = "baseCategory";

    private int createdFragments = 0;
    private final int fragmentCount = 2;

    public static final String SHARED_PREF = "com.sharkeva.pressball";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            firstStart = savedInstanceState.getBoolean(KEY_FIRSTSTART, true);
            error = savedInstanceState.getBoolean(KEY_ERROR, false);
        }else {
            error = false;
            firstStart = true;
        }

        toolbar = (Toolbar)findViewById(R.id.pressball_toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(toolbar);

        initialize(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_FIRSTSTART, false);
        outState.putSerializable(KEY_BASECATEGORY, baseCategory);
        outState.putBoolean(KEY_ERROR, error);
    }

    private void initialize(Bundle saveInstanceState){
        categoryDAO = new CategoryDaoImp(this);
        updateCenter = new NFUpdateCenter();

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        listView = (ListView)findViewById(R.id.navigation_drawer);
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        spinner = (ProgressBarCircularIndeterminate)findViewById(R.id.spinner);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh_news);
        errorView = findViewById(R.id.error_layout);
        errorButton = (ButtonFlat)findViewById(R.id.repeat_load);

        initializeCategory();
        initializeNavigation();
        initializeToolbar();
        initializePager();
        initializeUpdateCenter();
        initializeErrorHadling();

    }

    private void initializeErrorHadling() {
        errorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorButton.setEnabled(false);
                updateCenter.updateAsync();
            }
        });
    }

    private void initializeUpdateCenter() {
        updateCenter.addOnStartUpdate(new Listener<Void>() {
            @Override
            public void onExecute(Void context) {
                if(updateCenter.isFirstLoad() && !error){
                    spinner.setVisibility(View.VISIBLE);
                }
                errorButton.setEnabled(false);
            }
        });

        updateCenter.addOnEndUpdate(new Listener<Document>() {
            @Override
            public void onExecute(Document context) {
                spinner.setVisibility(View.GONE);
                errorView.setVisibility(View.INVISIBLE);
                errorButton.setEnabled(true);
                swipeRefreshLayout.setRefreshing(false);
                error = false;
            }
        });

        updateCenter.addOnError(new Listener<Void>() {
            @Override
            public void onExecute(Void context) {
                if(updateCenter.isFirstLoad() || error){
                    spinner.setVisibility(View.GONE);
                    errorView.setVisibility(View.VISIBLE);
                }
                swipeRefreshLayout.setRefreshing(false);
                errorButton.setEnabled(true);
                error = true;
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.updateColor1,
                R.color.updateColor2,
                R.color.updateColor3);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateCenter.updateAsync();
            }
        });
    }
    private void initializeCategory(){
        SharedPreferences preferences = getSharedPreferences(MainActivity.SHARED_PREF, MODE_PRIVATE);
        int categoryId = preferences.getInt(KEY_BASECATEGORY ,0);
        baseCategory = categoryDAO.getById(categoryId);
    }
    private void initializePager() {
        newsPagerAdapter = new NewsPagerAdapter(getSupportFragmentManager()
                , getResources()
                , updateCenter);
        pager.setAdapter(newsPagerAdapter);

        tabs.setShouldExpand(true);
        tabs.setViewPager(pager);

        tabs.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if(newsPagerAdapter.getRegisteredFragment(position) == null){
                    return;
                }
                LinearLayoutManager layoutManager =((BaseNewsFlashFragment) newsPagerAdapter.getRegisteredFragment(position))
                        .getLinearLayoutManager();
                if(layoutManager.findFirstCompletelyVisibleItemPosition() == 0){
                    swipeRefreshLayout.setEnabled(true);
                }else{
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });
    }
    private void initializeToolbar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    private void initializeNavigation(){
        listView.setAdapter(new NavigationAdapter(this,
                R.layout.navigation_item,
                new ArrayList<Category>(categoryDAO.getAll())));

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.app_name,
                R.string.app_name
        ) {

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                SharedPreferences.Editor editor = getSharedPreferences(MainActivity.SHARED_PREF, MODE_PRIVATE).edit();
                editor.putInt(KEY_BASECATEGORY, position);
                editor.apply();

                drawerLayout.closeDrawers();
                updateCenter.initialize((Category) listView.getAdapter().getItem(position));
                updateCenter.updateAsync();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

        @Override
        protected void onPostCreate(Bundle savedInstanceState) {
            super.onPostCreate(savedInstanceState);
            drawerToggle.syncState();
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            drawerToggle.onConfigurationChanged(newConfig);
        }



    @Override
    public void onFragmentCreated(Fragment fragment) {
        createdFragments++;
        if(fragment instanceof BaseNewsFlashFragment){
            ((BaseNewsFlashFragment)fragment)
                    .setRecyclerViewOnScrollListener(swipeRefreshLayout);
            ((BaseNewsFlashFragment)fragment)
                    .initializeNFCenter(updateCenter);
        }
        if(fragmentCount == createdFragments){
            updateCenter.initialize(baseCategory, firstStart);
            if(firstStart || error) {
                updateCenter.updateAsync();
            }
        }
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }
}
