/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package credentials.login;

/**
 * The {@link CredentialsVerifier} is responsible for connecting to Jenkins and logging in.
 */
public interface CredentialsVerifier {

   /**
    * Method to attempt a login.
    * @param jenkinsLocation the location of Jenkins.
    * @param user the user name.
    * @param password the password.
    */
   public boolean attemptLogin( String jenkinsLocation, String user, String password );

}//End Interface
