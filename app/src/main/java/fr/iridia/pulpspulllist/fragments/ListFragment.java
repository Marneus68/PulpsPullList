package fr.iridia.pulpspulllist.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import java.util.HashSet;
import java.util.jar.Manifest;

import fr.iridia.pulpspulllist.MainActivity;
import fr.iridia.pulpspulllist.R;

public class ListFragment extends Fragment {

    public static final String TAG = "ListFragment";

    protected ArrayAdapter arrayAdapter;

    protected String key;

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    protected ListView listView;

    HashSet<String> hs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);

        listView = (ListView) view.findViewById(android.R.id.list);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.button);

        hs = (HashSet<String>) MainActivity.sharedPreferences.getStringSet(key, new HashSet<String>());
        //arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, hs.toArray(new String[0]));
        updateListView();

        fab.attachToListView(listView);
        fab.setShadow(true);
        fab.show(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Button clicked");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                final EditText editText = new EditText(getActivity());

                builder.setMessage(R.string.add_watchlist_desc)
                        .setTitle(R.string.add_watchlist_title)
                        .setCancelable(true)
                        .setView(editText)
                        .setPositiveButton(R.string.add_watchlist_add, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (0 != editText.getText().toString().length()) {
                                    SharedPreferences.Editor ed = MainActivity.sharedPreferences.edit();
                                    HashSet<String> s = new HashSet<String>();
                                    s.add(editText.getText().toString());
                                    hs.add(editText.getText().toString());
                                    ed.putStringSet(key, s);
                                    ed.commit();
                                    updateListView();
                                }
                            }
                        });
                builder.create().show();
            }
        });

        return view;
    }

    public void updateListView() {
        listView.setAdapter(new ArrayAdapter<String>(MainActivity.context, android.R.layout.simple_list_item_1, hs.toArray(new String[hs.size()])));
    }
}
