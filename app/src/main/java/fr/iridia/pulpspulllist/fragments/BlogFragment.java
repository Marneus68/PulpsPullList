package fr.iridia.pulpspulllist.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.net.URL;

import fr.iridia.pulpspulllist.MainActivity;
import fr.iridia.pulpspulllist.R;
import fr.iridia.pulpspulllist.data.Feed;
import fr.iridia.pulpspulllist.service.RSSDownloaderAsyncTask;
import fr.iridia.pulpspulllist.utils.RSSParser;

public class BlogFragment extends Fragment {

    public static final String TAG = "BlogFragment";

    protected Context context;

    protected SwipeRefreshLayout swipeRefreshLayout = null;
    protected ListView listView = null;
    protected BlogListAdapter blogListAdapter = null;

    protected String localFile = null;

    protected URL url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.blog_fragment, container, false);

        context = getActivity().getApplicationContext();

        localFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.pulps_rss_folder) + "/" + getString(R.string.pulps_rss_local_file);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        listView = (ListView) view.findViewById(R.id.mainListView);

        if (null == blogListAdapter) {
            blogListAdapter = new BlogListAdapter(MainActivity.context, RSSParser.getFeed(localFile));
        }
        listView.setAdapter(blogListAdapter);

        try {
            url = new URL(getString(R.string.pulps_rss_url));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "Refresh");

                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new RSSDownloaderAsyncTask(context, url, localFile) {
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
        return view;
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
