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
import uk.dangrew.jtt.api.handling.BuildState;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * The {@link JenkinsJobPropertyListener} is responsible for providing global registrations for
 * particular properties on the {@link JenkinsJob}s in the associated {@link JenkinsDatabase}.
 */
public class JenkinsJobPropertyListener {

   private GlobalPropertyListenerImpl< JenkinsJob, Pair< Integer, BuildResultStatus > > buildResultStatusPropertyListener;
   private GlobalPropertyListenerImpl< JenkinsJob, BuildState > buildStatePropertyListener;
   
   /**
    * Constructs a new {@link JenkinsJobPropertyListener}.
    * @param database the associated {@link JenkinsDatabase}.
    */
   public JenkinsJobPropertyListener( JenkinsDatabase database ) {
      buildResultStatusPropertyListener = new GlobalPropertyListenerImpl<>(
               database.jenkinsJobs(), 
               job -> job.lastBuildProperty()
      );
      buildStatePropertyListener = new GlobalPropertyListenerImpl<>(
               database.jenkinsJobs(), 
               job -> job.buildStateProperty()
      );
   }//End Constructor

   /**
    * Method to add a {@link JttChangeListener} as a listener for changes in {@link JenkinsJob}s {@link BuildResultStatus}.
    * @param buildResultListener the {@link JttChangeListener} listener.
    */
   public void addBuildResultStatusListener( JttChangeListener< JenkinsJob, Pair< Integer, BuildResultStatus > > buildResultListener ) {
      buildResultStatusPropertyListener.addListener( buildResultListener );
   }//End Method
   
   /**
    * Method to add a {@link JttChangeListener} as a listener for changes in {@link JenkinsJob}s {@link BuildState}.
    * @param buildStateListener the {@link JttChangeListener} listener.
    */
   public void addBuildStateListener( JttChangeListener< JenkinsJob, BuildState > buildStateListener ) {
      buildStatePropertyListener.addListener( buildStateListener );
   }//End Method

}//End Class
