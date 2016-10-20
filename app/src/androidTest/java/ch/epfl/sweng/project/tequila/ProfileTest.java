package ch.epfl.sweng.project.tequila;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test for Profile class
 *
 * @author Alexis Montavon
 */

@RunWith(JUnit4.class)
public class ProfileTest {

    @Test
    public void profileConstructorTest() {
        String sciper = "245789";
        String gaspar = "zgorode";
        String email = "yipii@epfl.ch";
        String firstNames = "JeanLouisEric";
        String lastNames = "BoboBubu";
        Profile p = new Profile(sciper, gaspar, email, firstNames, lastNames);
        Assert.assertTrue(p.sciper.equals(sciper));
        Assert.assertTrue(p.gaspar.equals(gaspar));
        Assert.assertTrue(p.email.equals(email));
        Assert.assertTrue(p.firstNames.equals(firstNames));
        Assert.assertTrue(p.lastNames.equals(lastNames));
    }

}
