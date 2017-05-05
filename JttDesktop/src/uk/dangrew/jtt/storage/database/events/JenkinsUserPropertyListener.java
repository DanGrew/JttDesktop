/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.storage.database.events;

import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * The {@link JenkinsUserPropertyListener} is responsible for providing global registrations for
 * particular properties on the {@link JenkinsUser}s in the associated {@link JenkinsDatabase}.
 */
public class JenkinsUserPropertyListener {

   private GlobalPropertyListenerImpl< JenkinsUser, String > namePropertyListener;
   
   /**
    * Constructs a new {@link JenkinsUserPropertyListener}.
    * @param database the associated {@link JenkinsDatabase}.
    */
   public JenkinsUserPropertyListener( JenkinsDatabase database ) {
      namePropertyListener = new GlobalPropertyListenerImpl<>(
               database.jenkinsUsers(), 
               job -> job.nameProperty()
      );
   }//End Constructor

   /**
    * Method to add a {@link JttChangeListener} as a listener for changes in {@link JenkinsUser}s name.
    * @param namePropertyListener the {@link JttChangeListener} listener.
    */
   public void addNamePropertyListener( JttChangeListener< JenkinsUser, String > namePropertyListener ) {
      this.namePropertyListener.addListener( namePropertyListener );
   }//End Method

}//End Class
