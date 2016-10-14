package ch.epfl.sweng.project;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.project.model.GPSPoint;
import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.model.Player;


/**
 * Unit tests!
 */
public class DatabaseInteractionTest extends ActivityTestRule<CreateMatchActivity> {
    public DatabaseInteractionTest() {
        super(CreateMatchActivity.class);
    }

    /*
    Match m1;
    Match m2;
    Player vinz;
    Player colin;
    Player nicolas;
    GPSPoint geneva;
    GPSPoint lausanne;

    private void populate() {
        vinz = new Player(new Player.PlayerID(777777), "Combes", "Amaury", new Player.PlayerRank(1));
        colin = new Player(new Player.PlayerID(999999), "Laforest", "Dorian", new Player.PlayerRank(2));
        nicolas = new Player(new Player.PlayerID(666666), "Montavon", "Alexis", new Player.PlayerRank(3));
        lausanne = new GPSPoint(46.5196535, 6.632273400000031);
        geneva = new GPSPoint(46.2043907, 6.143157699999961);
        List<Player> e1 = new ArrayList<>(4);
        List<Player> e2 = new ArrayList<>(4);
        e1.add(vinz);
        e1.add(colin);
        e2.add(nicolas);

        m1 = new Match(e1, lausanne, "test match 1", new Match.MatchRank(1), false, Match.GameVariant.CLASSIC,
                (new Date(2016, 12, 20)).getTime());
        m2 = new Match(e2, geneva, "test match 2", new Match.MatchRank(2), false, Match.GameVariant.CLASSIC,
                (new Date(2016, 12, 10)).getTime());
        /*
        DatabaseReference myDB = FirebaseDatabase.getInstance().getReference().getRoot().child("testDB");
        Task<Void> t1 = myDB.push().setValue(m1);
        Task<Void> t2 = myDB.push().setValue(m2);
    }*/

    @Test
    public void MainActiviyCanReadData() {
        Activity a = getActivity();


    }


}
