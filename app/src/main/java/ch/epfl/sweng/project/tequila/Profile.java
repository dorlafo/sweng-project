package ch.epfl.sweng.project.tequila;

/**
 * Tequila user profile information.
 *
 * @author Alexis Montavon
 *
 * This code was taken from the example of Solal Pirelli:
 *   https://github.com/sweng-epfl/tequila-sample/tree/master/src/main/java/ch/epfl/sweng/tequila
 */

public final class Profile {

    /**
     * This is the user ID, it is guaranteed to be unique.
     */
    public final String sciper;

    /**
     * This is probably unique, but you shouldn't depend on it.
     */
    public final String gaspar;

    /**
     * Don't spam your users! Use this carefully.
     */
    public final String email;

    /**
     * Do not assume anything about what exactly this contains.
     * Some people have one name, some have multiple, some have honorary prefixes, ...
     */
    public final String firstNames;

    /**
     * Same remark as `firstNames`.
     */
    public final String lastNames;

    public Profile(String sciper, String gaspar, String email, String firstNames, String lastNames) {
        this.sciper = sciper;
        this.gaspar = gaspar;
        this.email = email;
        this.firstNames = firstNames;
        this.lastNames = lastNames;
    }
}