package com.sharkeva.pressball.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gc.materialdesign.views.ProgressBarIndeterminateDeterminate;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.sharkeva.pressball.R;
import com.sharkeva.pressball.dao.CategoryDao;
import com.sharkeva.pressball.dao.imp.CategoryDaoImp;
import com.sharkeva.pressball.entities.Category;
import com.sharkeva.pressball.entities.Comment;
import com.sharkeva.pressball.entities.CommentData;
import com.sharkeva.pressball.entities.News;
import com.sharkeva.pressball.entities.Newsflash;
import com.sharkeva.pressball.entities.Online;
import com.sharkeva.pressball.entities.OnlineData;
import com.sharkeva.pressball.entities.news.ImageElement;
import com.sharkeva.pressball.entities.news.NewsElement;
import com.sharkeva.pressball.entities.news.OnlineElement;
import com.sharkeva.pressball.entities.news.TextElement;
import com.sharkeva.pressball.entities.news.VideoElement;
import com.sharkeva.pressball.services.CommentService;
import com.sharkeva.pressball.services.NewsService;
import com.sharkeva.pressball.services.OnlineService;
import com.sharkeva.pressball.ui.DividerItemDecoration;
import com.sharkeva.pressball.ui.DownloadImageTask;
import com.sharkeva.pressball.ui.MutableAdapter;
import com.sharkeva.pressball.ui.MyLinearLayoutManager;
import com.sharkeva.pressball.ui.adapter.CommentAdapter;
import com.sharkeva.pressball.ui.adapter.NavigationAdapter;
import com.sharkeva.pressball.ui.adapter.OnlineAdapter;
import com.sharkeva.pressball.utils.PressballUtils;
import com.sharkeva.pressball.utils.ToolbarUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mListView;
    private LinearLayout mContent;
    private LinearLayout mMainContent;
    private ProgressBarCircularIndeterminate mSpinner;
    private ProgressBarCircularIndeterminate mSpinnerSmall;
    private TextView mTitle;
    private ButtonFlat mCommentsUpdate;
    private ProgressBarIndeterminateDeterminate mCommentsProgressBar;
    private com.github.ksoichiro.android.observablescrollview.ObservableScrollView mScrollView;
    private TextView mCommentCount;

    private RecyclerView mComments;

    private CategoryDao categoryDao;

    private NewsService newsService;
    private CommentService commentService;

    public static final String KEY_NEWS_FLASH = "newsFlash";
    public static final String KEY_NEWS = "news";

    public static final String KEY_FIRST_START = "firstStart";

    public static final String KEY_COMMENT_SERVICE_CURRENT_PAGE = "csCommentPage";
    public static final String KEY_COMMENT_SERVICE_FIRST_START = "csFirstStart";
    private boolean csFirstStart;
    private int csCurrentPage;


    private boolean firstStart;
    private Newsflash newsflash;
    private News mainNews;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        if(getIntent().getExtras() != null){
            newsflash = (Newsflash)getIntent().getExtras().getSerializable(KEY_NEWS_FLASH);
        }

        restoreInstanceState(savedInstanceState);

//        //---------
//        // test
//        newsflash = new Newsflash();
//        newsflash.setTitle("");
//        newsflash.setClassify("http://www.pressball.by/pbonline/hockey/80695");
////        newsflash.setClassify("http://www.pressball.by/news/biathlon/194297");
//        //---------

        mToolbar = (Toolbar)findViewById(R.id.pressball_toolbar);
        mToolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(mToolbar);

        initialize(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i("LENGTH", String.valueOf(mainNews.getComments().getCommentCount()));
        mainNews.getComments().setComments(((CommentAdapter)mComments.getAdapter()).getItems());

        outState.putBoolean(KEY_FIRST_START, false);
        outState.putSerializable(KEY_NEWS, mainNews);
        outState.putSerializable(KEY_NEWS_FLASH, newsflash);

        outState.putBoolean(KEY_COMMENT_SERVICE_FIRST_START, commentService.isFirstStart());
        outState.putInt(KEY_COMMENT_SERVICE_CURRENT_PAGE, commentService.getCurrentPage());
    }

    protected void restoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            firstStart = savedInstanceState.getBoolean(KEY_FIRST_START, true);
            mainNews = (News)savedInstanceState.getSerializable(KEY_NEWS);
            newsflash = (Newsflash)savedInstanceState.getSerializable(KEY_NEWS_FLASH);
            csCurrentPage = savedInstanceState.getInt(KEY_COMMENT_SERVICE_CURRENT_PAGE);
            csFirstStart = savedInstanceState.getBoolean(KEY_COMMENT_SERVICE_FIRST_START);
        }
    }

    private Activity getActivity(){
        return this;
    }

    public void initialize(Bundle savedInstanceState){
        categoryDao = new CategoryDaoImp(this);
        newsService = new NewsService(newsflash);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mListView = (ListView)findViewById(R.id.navigation_drawer);
        mContent = (LinearLayout)findViewById(R.id.main_content);
        mMainContent = (LinearLayout)findViewById(R.id.main);
        mSpinner = (ProgressBarCircularIndeterminate)findViewById(R.id.spinner);
        mTitle = (TextView)findViewById(R.id.news_title);
        mComments = (RecyclerView)findViewById(R.id.news_comments);
        mCommentsUpdate = (ButtonFlat)findViewById(R.id.comments_update);
        mCommentsProgressBar = (ProgressBarIndeterminateDeterminate) findViewById(R.id.comments_progress_bar);
        mScrollView = (com.github.ksoichiro.android.observablescrollview.ObservableScrollView) findViewById(R.id.scrollview_news);
        mCommentCount = (TextView) findViewById(R.id.comments_count);
        mSpinnerSmall = (ProgressBarCircularIndeterminate) findViewById(R.id.spinner_small);

        initializeToolbar();
        initializeNavigation();
        initializeComments();
        initializeNews();
    }

    private void initializeComments() {
        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mComments.setLayoutManager(linearLayoutManager);
        mComments.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        mComments.setAdapter(new CommentAdapter(new ArrayList<Comment>(), getBaseContext()));

        mScrollView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {

            private ScrollState currentScrollState = ScrollState.DOWN;
            private boolean changedState = true;
            private int changedPosition = 0;
            private int currentPosition = 0;

            @Override
            public void onScrollChanged(int y, boolean b, boolean b2) {
                currentPosition = y;
                if(changedState){
                    int deltaY = currentPosition - changedPosition;
                    if(deltaY < ToolbarUtils.UP_DELTA_Y){
                        ToolbarUtils.show(mToolbar);
                        changedState = false;
                    }
                    if(deltaY > ToolbarUtils.DOWN_DELTA_Y){
                        ToolbarUtils.hide(mToolbar);
                        changedState = false;
                    }
                }

                if(mScrollView.getChildAt(0).getHeight() - mScrollView.getHeight() <= mScrollView.getCurrentScrollY()){
                    Log.i("comm_load", "----------------");
                    loadMoreComments();
                }
            }

            @Override
            public void onDownMotionEvent() {

            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {

                if(scrollState != currentScrollState && scrollState != ScrollState.STOP){
                    currentScrollState = scrollState;
                    changedState = true;
                    changedPosition = currentPosition;
                }
            }
        });

        mCommentsUpdate.setOnClickListener(new ButtonFlat.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommentsProgressBar.setVisibility(View.VISIBLE);
                mCommentsUpdate.setEnabled(false);
                new AsyncTask<Void, Void, CommentData>() {
                    private Exception exception = null;

                    @Override
                    protected CommentData doInBackground(Void... params) {
                        CommentData comments = null;
                        try {
                            comments = commentService.update();
                        } catch (IOException e) {
                            exception = e;
                        }
                        return comments;
                    }

                    @Override
                    protected void onPostExecute(CommentData commentData) {
                        if(exception == null){
                            ((MutableAdapter<Comment>)mComments.getAdapter()).update(commentData.getComments());
                            mCommentCount.setText(getResources().getString(R.string.comments) + "(" + commentData.getCommentCount() + ")");
                        }
                        mCommentsProgressBar.setVisibility(View.INVISIBLE);
                        mCommentsUpdate.setEnabled(true);
                    }
                }.execute();
            }
        });
    }

    private void loadMoreComments() {
        mSpinnerSmall.setVisibility(View.VISIBLE);

        if(!commentService.Busy()){
            new AsyncTask<Void, Void, List<Comment>>() {
                private Exception exception;
                @Override
                protected List<Comment> doInBackground(Void... params) {
                    List<Comment> comments = null;
                    try {
                        comments = commentService.getMore();
                    } catch (IOException e) {
                        exception = e;
                    }
                    return comments;
                }

                @Override
                protected void onPostExecute(List<Comment> comments) {
                    if(exception == null){
                        if(comments != null){
                            MutableAdapter<Comment> adapter = ((MutableAdapter<Comment>)mComments.getAdapter());
                            Log.i("ADD", String.valueOf(comments.size()));
                            for (Comment comment : comments){
                                adapter.add(comment);
                            }
                        }
                    }
                    mSpinnerSmall.setVisibility(View.GONE);
                }

            }.execute();
        }
    }


    private void initializeNews() {
        mTitle.setText(newsflash.getTitle());

        if(mainNews == null){
            mMainContent.setVisibility(View.INVISIBLE);
            mSpinner.setVisibility(View.VISIBLE);
            initializeNewsContentAsync();
        } else {
            initializeNewsContent(mainNews, csFirstStart, csCurrentPage);
        }

    }

    private void initializeNewsContent(News news, boolean csFirstStart, int csCurrentPage){
        mainNews = news;
        commentService = new CommentService(news.getCommentPage(), csFirstStart, csCurrentPage);
        mTitle.setText(news.getTitle());
        if(news.getComments().getComments() != null){
            ((MutableAdapter<Comment>)mComments.getAdapter()).update(news.getComments().getComments());
//            Log.i("34", String.valueOf(news.getComments().getComments().size()));
        }

        mCommentCount.setText(getResources().getString(R.string.comments) + "(" + news.getComments().getCommentCount() + ")");

        for (NewsElement element : news.getNewsElements()){
            if(element instanceof TextElement){
                initializeTextElement((TextElement)element);
            } else if (element instanceof ImageElement){
                initializeImageElement((ImageElement) element);
            }else if (element instanceof VideoElement){
                initializeVideoElement1((VideoElement) element);
            }else if (element instanceof OnlineElement){
                initializeOnlineElement((OnlineElement) element);
            }
        }
    }

    private void initializeNewsContentAsync(){
        new AsyncTask<Void, Void, News>() {
            @Override
            protected News doInBackground(Void... params) {
                return newsService.load();
            }

            @Override
            protected void onPostExecute(News news) {

                //first time
                initializeNewsContent(news, true, -1);

                mMainContent.setVisibility(View.VISIBLE);
                mSpinner.setVisibility(View.INVISIBLE);

            }
        }.execute();
    }

    private void initializeOnlineElement(OnlineElement element) {
        LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.online_element, null);
        ButtonFlat updateButton = (ButtonFlat)view.findViewById(R.id.online_update_button);
        final RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.view_online);
        final SwipeRefreshLayout swipeRefreshLayout =(SwipeRefreshLayout)view.findViewById(R.id.refresh_online);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        final Online data = (Online)element.getData();
        recyclerView.setAdapter(new OnlineAdapter(data.getDataList().toArray(new OnlineData[0]),
                getBaseContext()));
        recyclerView.setOnTouchListener(new RecyclerView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

       recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "3435", Toast.LENGTH_SHORT).show();
            }
        });

        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setColorSchemeResources(R.color.updateColor1,
                R.color.updateColor2,
                R.color.updateColor3);

        final OnlineService service = new OnlineService();
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout.setRefreshing(true);
                getAsyncTaskForOnline(service, recyclerView, swipeRefreshLayout).execute(data.getId());
            }
        });

        if(data.getDataList().size() == 0){
            swipeRefreshLayout.setRefreshing(true);
            getAsyncTaskForOnline(service, recyclerView, swipeRefreshLayout).execute(data.getId());
        }

        mContent.addView(view);

    }
    private AsyncTask <Integer, Void, Online> getAsyncTaskForOnline(
            final OnlineService service, final RecyclerView recyclerView, final SwipeRefreshLayout refreshLayout){
        return new AsyncTask<Integer, Void, Online>() {
            @Override
            protected Online doInBackground(Integer... id) {
                Online obj = null;
                try {
                    obj = service.load(id[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return obj;
            }

            @Override
            protected void onPostExecute(Online online) {
                recyclerView.setAdapter(new OnlineAdapter(online.getDataList().toArray(new OnlineData[0]),
                        getBaseContext()));
                refreshLayout.setRefreshing(false);
            }
        };
    }

    private void initializeVideoElement(VideoElement element) {
        String content = (String)element.getData();
        WebView webView = new WebView(this);
        webView.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT)
        );
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)webView.getLayoutParams();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setSupportMultipleWindows(false);
        webView.getSettings().setSupportZoom(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setScrollContainer(false);
        webView.loadData(content, "text/html; charset=utf-8", "UTF-8");
        mContent.addView(webView);
    }
    private void initializeVideoElement1(VideoElement element) {
        String content = (String)element.getData();
        TextView textView = new TextView(this);
        textView.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        200)
        );
        textView.setGravity(Gravity.CENTER);
        textView.setText("Video coming soon");
        textView.setBackgroundResource(R.color.lightPressballColor);
        mContent.addView(textView);
    }
    private void initializeImageElement(ImageElement element) {
        String href = (String)element.getData();
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
        );
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mContent.addView(imageView);
        new DownloadImageTask(imageView)
                .execute(href);
    }
    private void initializeImageElement1(ImageElement element) {
        String href = (String)element.getData();
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT)
        );

        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mContent.addView(imageView);
        new DownloadImageTask(imageView)
                .execute(href);
    }
    private void initializeTextElement(TextElement element){
        String content = (String)element.getData();
        WebView webView = new WebView(this);
        webView.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT)
        );
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)webView.getLayoutParams();
        marginLayoutParams.leftMargin  = 15;
        marginLayoutParams.rightMargin = 15;
        webView.setLayoutParams(marginLayoutParams);
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setScrollContainer(false);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(PressballUtils.isPressballUrl(url)){
                    Newsflash newsflash1 = new Newsflash();
                    newsflash1.setTitle("");
                    newsflash1.setClassify(url);
                    newsflash1.setCategory(newsflash.getCategory());
                    Intent intent = new Intent(getActivity(), NewsActivity.class);
                    intent.putExtra(NewsActivity.KEY_NEWS_FLASH, newsflash1);
                    startActivity(intent);
                } else{
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
                return true;
            }
        });
        webView.loadData(content, "text/html; charset=utf-8", "UTF-8");
        mContent.addView(webView);
    }

    private void initializeToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case android.R.id.home:
                        NavUtils.navigateUpFromSameTask(getActivity());
                        return true;
                }
                return true;
            }
        });
    }

    private void initializeNavigation() {
        mListView.setAdapter(new NavigationAdapter(this,
                R.layout.navigation_item,
                new ArrayList<Category>(categoryDao.getAll())));

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
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
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                SharedPreferences.Editor editor = getSharedPreferences(MainActivity.SHARED_PREF, MODE_PRIVATE).edit();
                editor.putInt(MainActivity.KEY_BASECATEGORY, position);
                editor.apply();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
