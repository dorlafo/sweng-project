package ch.epfl.sweng.jassatepfl.tequila;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Test for Profile class
 *
 * @author Alexis Montavon
 */

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
