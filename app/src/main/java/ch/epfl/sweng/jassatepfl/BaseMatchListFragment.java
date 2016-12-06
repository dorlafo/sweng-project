package ch.epfl.sweng.jassatepfl;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ch.epfl.sweng.jassatepfl.database.helpers.DBReferenceWrapper;
import ch.epfl.sweng.jassatepfl.database.helpers.QueryWrapper;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.tools.MatchListAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseMatchListFragment extends Fragment implements AdapterView.OnItemClickListener {
    @Inject
    public DBReferenceWrapper dbRefWrapped;
    @Inject
    public FirebaseAuth fAuth;

    private static final String TAG = BaseMatchListFragment.class.getSimpleName();

    private ListView listView;
    private List<Match> matches;
    private MatchListAdapter adapter;
    private ChildEventListener matchListener;

    public BaseMatchListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().graph().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_base_match_list, container, false);

        TextView emptyList = new TextView(rootView.getContext());
        emptyList.setText(getEmptyListMessage());
        emptyList.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        emptyList.setTextColor(Color.BLACK);

        matches = new ArrayList<>();

        listView = (ListView) rootView.findViewById(R.id.my_matches_list);
        ((ViewGroup) listView.getParent()).addView(emptyList);
        listView.setEmptyView(emptyList);
        
        listView.setOnItemClickListener(this);
        adapter = new MatchListAdapter(rootView.getContext(), R.layout.match_list_row, new ArrayList<Match>());
        listView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        removeListener();
        matches.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        contactFirebase();
    }

    public void removeListener() {
        if(matchListener != null) {
            getQuery(dbRefWrapped).removeEventListener(matchListener);
        }
    }

    public abstract String getEmptyListMessage();

    private void contactFirebase() {
        matchListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded:dataSnapshot:" + dataSnapshot.toString());
                Match match = dataSnapshot.getValue(Match.class);
                //Add match to the list if we are in it
                if(match.hasParticipantWithID(new Player.PlayerID(getUserSciper()))) {
                    matches.add(match);
                }
                modifyListAdapter();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged:dataSnapshot:" + dataSnapshot.toString());
                Match match = dataSnapshot.getValue(Match.class);
                int matchIndex = matches.indexOf(match);
                //If the match is in the list (ie we were in it)
                if(matchIndex != -1) {
                    //If we now are not in it, remove it from the list, otherwise modify it
                    if(!match.hasParticipantWithID(new Player.PlayerID(getUserSciper()))) {
                        matches.remove(match);
                    }
                    else {
                        matches.set(matchIndex, match);
                    }
                }
                //The match was not in the list
                else {
                    //Add match if we are in it
                    if(match.hasParticipantWithID(new Player.PlayerID(getUserSciper()))) {
                        matches.add(match);
                    }
                }
                modifyListAdapter();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:dataSnapshot:" + dataSnapshot.toString());
                Match match = dataSnapshot.getValue(Match.class);
                matches.remove(match);
                modifyListAdapter();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        getQuery(dbRefWrapped)
                .addChildEventListener(matchListener);
    }

    private void modifyListAdapter() {
        adapter.refreshData(matches);
    }

    public abstract QueryWrapper getQuery(DBReferenceWrapper dbRefWrapped);

    public String getUserSciper() {
        return fAuth.getCurrentUser().getDisplayName();
    }

    public MatchListAdapter getAdapter() {
        return adapter;
    }

    public ChildEventListener getMatchListener() {
        return matchListener;
    }
}
