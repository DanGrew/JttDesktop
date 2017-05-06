/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.credentials.login;

import uk.dangrew.sd.core.category.Categories;
import uk.dangrew.sd.core.message.Messages;
import uk.dangrew.sd.core.progress.Progresses;
import uk.dangrew.sd.core.source.SourceImpl;
import uk.dangrew.sd.digest.object.ObjectDigestImpl;

/**
 * The {@link JenkinsLoginDigest} provides an {@link ObjectDigest} for the
 * {@link JenkinsLogin}.
 */
public class JenkinsLoginDigest extends ObjectDigestImpl {

   static final String JENKINS_LOGIN_NAME = "User Login";
   static final String WAITING_FOR_LOG_IN = "Waiting for log in...";
   static final String CREDENTIALS_FORMAT_ACCEPTED = "Credentials format accepted";
   static final String CONNECTING_TO_JENKINS = "Connecting to Jenkins...";
   static final String LOG_IN_FAILED = "Log in failed. Check credentials.";
   static final String LOGIN_PROCESS_HAS_COMPLETED = "Login process has completed.";
   static final String SUCCESSFULLY_LOGGED_IN = "Successfully logged in!";
   static final String LOGIN_PROCESS_TIMED_OUT = "Log in attempt timed out.";
   
   static final double CREDENTIALS_FORMAT_ACCEPTED_PROGRESS = 50;

   /**
    * Constructs a new {@link JenkinsLoginDigest}.
    */
   JenkinsLoginDigest() {}
   
   /**
    * Method to attach the {@link JenkinsLogin} as the {@link Source} for the {@link ObjectDigest}.
    * @param source the {@link JenkinsLogin} source.
    */
   void attachSource( JenkinsLogin source ) {
      super.attachSource( new SourceImpl( source, JENKINS_LOGIN_NAME ) );
   }//End Method
   
   /**
    * Method to reset the login progress when failed.
    */
   void resetLoginProgress(){
      progress( Progresses.simpleProgress( 0 ), Messages.simpleMessage( WAITING_FOR_LOG_IN ) );
   }//End Method

   /**
    * Method to provide a validation error and reset the login progress.
    * @param error the description of the error.
    */
   void validationError( String error ) {
      resetLoginProgress();
      log( Categories.error(), Messages.simpleMessage( error ) );
   }//End Method

   /**
    * Method to handle the acceptance of credentials format.
    */
   void acceptCredentials() {
      log( Categories.status(), Messages.simpleMessage( CREDENTIALS_FORMAT_ACCEPTED ) );
      progress( Progresses.simpleProgress( CREDENTIALS_FORMAT_ACCEPTED_PROGRESS ), Messages.simpleMessage( CONNECTING_TO_JENKINS ) );
   }//End Method

   /**
    * Method to handle when a login has failed.
    */
   void loginFailed() {
      resetLoginProgress();
      log( Categories.status(), Messages.simpleMessage( LOG_IN_FAILED ) );
   }//End Method

   /**
    * Method to handle when a login has been successful.
    */
   void loginSuccessful() {
      progress( Progresses.complete(), Messages.simpleMessage( LOGIN_PROCESS_HAS_COMPLETED ) );
      log( Categories.status(), Messages.simpleMessage( SUCCESSFULLY_LOGGED_IN ) );
   }//End Method

   /**
    * Method to handle when a login has timed out.
    */
   void loginTimedOut() {
      resetLoginProgress();
      log( Categories.status(), Messages.simpleMessage( LOGIN_PROCESS_TIMED_OUT ) );
   }//End Method

}//End Class
