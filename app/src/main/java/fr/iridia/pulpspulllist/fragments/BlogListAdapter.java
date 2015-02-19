package fr.iridia.pulpspulllist.fragments;

import android.content.Context;
import android.database.DataSetObserver;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import fr.iridia.pulpspulllist.R;
import fr.iridia.pulpspulllist.data.Feed;

public class BlogListAdapter implements ListAdapter{

    public static final String TAG = "BlogListAdapter";

    protected Feed feed;
    protected Context context;

    public  BlogListAdapter(Context context, Feed feed) {
        this.feed = feed;
        this.context = context;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return feed.items.size();
    }

    @Override
    public Object getItem(int position) {
        return feed.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //if (null == convertView) {
            convertView = View.inflate(context, R.layout.blog_cell, null);
        //}
        TextView tvTitle = (TextView) convertView.findViewById(R.id.articleTitle);
        TextView tvContent = (TextView) convertView.findViewById(R.id.articleContent);

        tvTitle.setText(feed.items.get(position).title);
        //tvContent.setText(Html.fromHtml(feed.items.get(position).content));

        String content = feed.items.get(position).content.replaceAll("\\<[^>]*>","");
        if (content.length() > 120)
            content = content.substring(0, 120) + "...";
        tvContent.setText(content);

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return feed.items.isEmpty();
    }
}
