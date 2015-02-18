package fr.iridia.pulpspulllist;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.net.URL;

import fr.iridia.pulpspulllist.fragments.BlogFragment;
import fr.iridia.pulpspulllist.fragments.WatchListsFragment;
import fr.iridia.pulpspulllist.service.RSSDownloaderAsyncTask;

public class MainActivity extends ActionBarActivity {

    public static final String TAG = "MainActivity";

    protected enum Location {
        ON_BLOG,
        ON_WATCHLIST
    }

    protected ActionBar actionBar;
    protected FragmentManager fragmentManager;

    protected ProgressDialog progressDialog;

    protected URL url = null;
    protected String localFile = null;

    protected Location location = Location.ON_BLOG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        fragmentManager = getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.mainFrame, new BlogFragment());
        fragmentTransaction.commit();

        try {
            url = new URL(getString(R.string.pulps_rss_url));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        localFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.pulps_rss_folder) + "/" + getString(R.string.pulps_rss_local_file);

        actionBar = getSupportActionBar();

        if (null!=actionBar) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.drawable.ic_launcher);
            actionBar.setTitle(R.string.blog_title);
        }

        if (!localRSSFileExists()) {
            Log.i(TAG, "No local copy of the RSS feed. Copying.");
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.fetching));
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            new RSSDownloaderAsyncTask(getApplicationContext(), url, localFile) {
                @Override
                protected void onPreExecute() {
                    progressDialog.show();
                    super.onPreExecute();
                }
                @Override
                protected void onPostExecute(Integer integer) {
                    progressDialog.hide();
                    super.onPostExecute(integer);
                }
            }.execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_activity_menu_preferences:
                openPreferenceActivity();
                return true;
            case R.id.main_activity_menu_watchlist:
                toggleWatchList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (location != Location.ON_BLOG) {
            navigateToBlogView();
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (location != Location.ON_BLOG) {
            navigateToBlogView();
        }
        else super.onBackPressed();
    }

    public void toggleWatchList() {
        if (location == Location.ON_BLOG) {
            if (null!=actionBar) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(R.string.watchlist_title);
                actionBar.setIcon(null);
            }
            location = Location.ON_WATCHLIST;

            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.animator.slide_up, R.animator.slide_down, R.animator.slide_up, R.animator.slide_down)
                    //.setCustomAnimations(R.animator.slide_up, R.animator.slide_down)
                    .replace(R.id.mainFrame, new WatchListsFragment())
                    .addToBackStack(null)
                    .commit();
        } else {
            navigateToBlogView();
        }
    }

    public void navigateToBlogView() {
        if (null!=actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(R.string.blog_title);
            actionBar.setIcon(R.drawable.ic_launcher);
        }
        location = Location.ON_BLOG;

        fragmentManager.popBackStack();
    }

    public void openPreferenceActivity() {
        Intent i = new Intent(this, PulpsPreferencesActivity.class);
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
}
