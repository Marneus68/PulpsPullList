package fr.iridia.pulpspulllist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import java.io.File;
import java.net.URL;

import fr.iridia.pulpspulllist.data.Feed;
import fr.iridia.pulpspulllist.service.RSSDownloaderAsyncTask;
import fr.iridia.pulpspulllist.utils.RSSParser;

public class MainActivity extends ActionBarActivity {

    public static final String TAG = "MainActivity";

    protected enum Location {
        ON_BLOG,
        ON_WATCHLIST
    }

    protected ActionBar actionBar;

    protected SwipeRefreshLayout swipeRefreshLayout = null;
    protected ListView listView = null;

    protected URL url = null;
    protected String localFile = null;
    protected String loadedVersion = "";

    protected Location location = Location.ON_BLOG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //setContentView(R.layout.blog_fragment);

        try {
            url = new URL(getString(R.string.pulps_rss_url));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        localFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.pulps_rss_folder) + "/" + getString(R.string.pulps_rss_local_file);

        actionBar = getSupportActionBar();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        listView = (ListView) findViewById(R.id.mainListView);

        if (null!=actionBar) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.drawable.ic_launcher);
            actionBar.setTitle(R.string.blog_title);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "Refresh");

                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new RSSDownloaderAsyncTask(getApplicationContext(), url, localFile) {
                            @Override
                            protected void onPostExecute(Integer integer) {
                                super.onPostExecute(integer);
                                swipeRefreshLayout.setRefreshing(false);
                                updateListViewContent();
                            }
                        }.execute();
                    }
                }, 3000);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0)
                    swipeRefreshLayout.setEnabled(true);
                else
                    swipeRefreshLayout.setEnabled(false);
            }
        });

        if (!localRSSFileExists()) {
            swipeRefreshLayout.setRefreshing(true);
            Log.i(TAG, "No local copy of the RSS feed. Copying.");
            new RSSDownloaderAsyncTask(getApplicationContext(), url, localFile) {
                @Override
                protected void onPostExecute(Integer integer) {
                    super.onPostExecute(integer);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }.execute();
        } else {
            updateListViewContent();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (location == Location.ON_BLOG)
            getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        else if (location == Location.ON_WATCHLIST)
            getMenuInflater().inflate(R.menu.main_activity_watchlist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_activity_menu_preferences:
                openPreferenceActivity();
                return true;
            case R.id.main_activity_menu_watchlist:
                navigateToWatchList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void navigateToWatchList() {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.watchlist_title);
        actionBar.setIcon(null);
        location = Location.ON_WATCHLIST;
        invalidateOptionsMenu();
    }

    public void navigateToBlogView() {
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(R.string.blog_title);
        actionBar.setIcon(R.drawable.ic_launcher);
        location = Location.ON_BLOG;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onSupportNavigateUp() {
        switch (location) {
            case ON_BLOG:
                return true;
            case ON_WATCHLIST:
                navigateToBlogView();
                return true;
            default:
                return super.onSupportNavigateUp();
        }
    }

    public void openPreferenceActivity() {
        Intent i = new Intent(this, PreferencesActivity.class);
        startActivity(i);
    }

    public boolean localRSSFileExists() {
        File file = new File(localFile);
        boolean out = file.exists();
        if (out) return out;

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.pulps_rss_folder));
        try {
            folder.mkdir();
            file.createNewFile();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return out;
    }

    public void updateListViewContent() {
        Feed feed = RSSParser.getFeed(localFile);
        /*
        if (loadedVersion != feed.lastEdited) {

            loadedVersion = feed.lastEdited;
        }
        */
    }
}
