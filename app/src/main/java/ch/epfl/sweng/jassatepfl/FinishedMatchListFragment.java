package ch.epfl.sweng.jassatepfl;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

import ch.epfl.sweng.jassatepfl.database.helpers.DBReferenceWrapper;
import ch.epfl.sweng.jassatepfl.database.helpers.QueryWrapper;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinishedMatchListFragment extends BaseMatchListFragment {

    private static final String TAG = FinishedMatchListFragment.class.getSimpleName();

    public FinishedMatchListFragment() {
        // Required empty public constructor
    }

    @Override
    public int getListID() {
        return R.id.my_finished_matches_list;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_finished_match_list;
    }

    @Override
    public QueryWrapper getQuery(DBReferenceWrapper dbRefWrapped) {
        return dbRefWrapped.child(DatabaseUtils.DATABASE_STATS)
                .child(DatabaseUtils.DATABASE_STATS_MATCH_ARCHIVE)
                .orderByChild(DatabaseUtils.DATABASE_MATCHES_EXPIRATION_TIME);
    }

    @Override
    public String getEmptyListMessage() {
        return getString(R.string.main_empty_finished_list);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Match match = getAdapter().getItem(position);
        Intent moveToGameActivity = new Intent(getActivity(), GameActivity.class);
        moveToGameActivity.putExtra("match_Id", match.getMatchID());
        startActivity(moveToGameActivity);
    }
}
