/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.storage.database.events;

import javafx.util.Pair;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * The {@link JenkinsJobPropertyListener} is responsible for providing global registrations for
 * particular properties on the {@link JenkinsJob}s in the associated {@link JenkinsDatabase}.
 */
public class JenkinsJobPropertyListener {

   private GlobalPropertyListenerImpl< JenkinsJob, Pair< Integer, BuildResultStatus > > buildResultStatusPropertyListener;
   
   /**
    * Constructs a new {@link JenkinsJobPropertyListener}.
    * @param database the associated {@link JenkinsDatabase}.
    */
   public JenkinsJobPropertyListener( JenkinsDatabase database ) {
      buildResultStatusPropertyListener = new GlobalPropertyListenerImpl<>(
               database.jenkinsJobs(), 
               job -> job.lastBuildProperty()
      );
   }//End Constructor

   /**
    * Method to add a {@link JttChangeListener} as a listener for changes in {@link JenkinsJob}s {@link BuildResultStatus}.
    * @param buildResultListener the {@link JttChangeListener} listener.
    */
   public void addBuildResultStatusListener( JttChangeListener< JenkinsJob, Pair< Integer, BuildResultStatus > > buildResultListener ) {
      buildResultStatusPropertyListener.addListener( buildResultListener );
   }//End Method

}//End Class
