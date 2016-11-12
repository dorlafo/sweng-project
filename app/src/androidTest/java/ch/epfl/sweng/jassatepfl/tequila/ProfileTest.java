package ch.epfl.sweng.jassatepfl.tequila;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

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
        assertTrue(p.sciper.equals(sciper));
        assertTrue(p.gaspar.equals(gaspar));
        assertTrue(p.email.equals(email));
        assertTrue(p.firstNames.equals(firstNames));
        assertTrue(p.lastNames.equals(lastNames));
    }

}
