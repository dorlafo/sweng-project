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
public class ActiveMatchListFragment extends BaseMatchListFragment {

    private static final String TAG = ActiveMatchListFragment.class.getSimpleName();

    public ActiveMatchListFragment() {
        // Required empty public constructor
    }

    @Override
    public int getListID() {
        return R.id.my_active_matches_list;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_active_match_list;
    }

    @Override
    public QueryWrapper getQuery(DBReferenceWrapper dbRefWrapped) {
        return dbRefWrapped.child(DatabaseUtils.DATABASE_MATCHES)
                .orderByChild(DatabaseUtils.DATABASE_MATCHES_MATCH_STATUS)
                .equalTo(Match.MatchStatus.ACTIVE.toString());
    }

    @Override
    public String getEmptyListMessage() {
        return getString(R.string.main_empty_active_list);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Match match = getAdapter().getItem(position);
        Intent moveToGameActivity = new Intent(getActivity(), GameActivity.class);
        moveToGameActivity.putExtra("match_Id", match.getMatchID());
        startActivity(moveToGameActivity);
    }
}
