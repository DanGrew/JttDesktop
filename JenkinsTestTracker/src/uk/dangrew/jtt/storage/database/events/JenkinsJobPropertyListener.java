/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.storage.database.events;

import java.util.function.BiConsumer;

import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * The {@link JenkinsJobPropertyListener} is responsible for providing global registrations for
 * particular properties on the {@link JenkinsJob}s in the associated {@link JenkinsDatabase}.
 */
public class JenkinsJobPropertyListener {

   private GlobalPropertyListenerImpl< JenkinsJob, BuildResultStatus > buildResultStatusPropertyListener;
   
   /**
    * Constructs a new {@link JenkinsJobPropertyListener}.
    * @param database the associated {@link JenkinsDatabase}.
    */
   public JenkinsJobPropertyListener( JenkinsDatabase database ) {
      buildResultStatusPropertyListener = new GlobalPropertyListenerImpl<>(
               database.jenkinsJobs(), 
               job -> job.lastBuildStatusProperty()
      );
   }//End Constructor

   /**
    * Method to add a {@link BiConsumer} as a listener for changes in {@link JenkinsJob}s {@link BuildResultStatus}.
    * @param buildResultListener the {@link BiConsumer} listener.
    */
   public void addBuildResultStatusListener( BiConsumer< JenkinsJob, BuildResultStatus > buildResultListener ) {
      buildResultStatusPropertyListener.addListener( buildResultListener );
   }//End Method

}//End Class
