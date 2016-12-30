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
import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.nodes.JenkinsNode;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * The {@link JenkinsJobPropertyListener} is responsible for providing global registrations for
 * particular properties on the {@link JenkinsJob}s in the associated {@link JenkinsDatabase}.
 */
public class JenkinsJobPropertyListener {

   private GlobalPropertyListenerImpl< JenkinsJob, Pair< Integer, BuildResultStatus > > buildResultStatusPropertyListener;
   private GlobalPropertyListenerImpl< JenkinsJob, BuildState > buildStatePropertyListener;
   private GlobalPropertyListenerImpl< JenkinsJob, Integer > testTotalCountPropertyListener;
   private GlobalPropertyListenerImpl< JenkinsJob, Integer > testFailureCountPropertyListener;
   private GlobalPropertyListenerImpl< JenkinsJob, JenkinsNode > builtOnPropertyListener;
   private GlobalPropertyListenerImpl< JenkinsJob, Long > timestampPropertyListener;
   
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
      testFailureCountPropertyListener = new GlobalPropertyListenerImpl<>(
               database.jenkinsJobs(), 
               job -> job.testFailureCount()
      );
      testTotalCountPropertyListener = new GlobalPropertyListenerImpl<>(
               database.jenkinsJobs(), 
               job -> job.testTotalCount()
      );
      builtOnPropertyListener = new GlobalPropertyListenerImpl<>(
               database.jenkinsJobs(), 
               job -> job.lastBuiltOnProperty()
      );
      timestampPropertyListener = new GlobalPropertyListenerImpl<>(
               database.jenkinsJobs(), 
               job -> job.currentBuildTimestampProperty()
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

   /**
    * Method to add a {@link JttChangeListener} as a listener for changes in {@link JenkinsJob#testTotalCount()}.
    * @param testTotalListener the {@link JttChangeListener}.
    */
   public void addTestTotalCountListener( JttChangeListener< JenkinsJob, Integer > testTotalListener ) {
      testTotalCountPropertyListener.addListener( testTotalListener );
   }//End Method

   /**
    * Method to add a {@link JttChangeListener} as a listener for changes in {@link JenkinsJob#testFailureCount()}.
    * @param testTotalListener the {@link JttChangeListener}.
    */
   public void addTestFailureCountListener( JttChangeListener< JenkinsJob, Integer > testFailuresListener ) {
      testFailureCountPropertyListener.addListener( testFailuresListener );
   }//End Method

   /**
    * Method to add a {@link JttChangeListener} as a listener for changes in {@link JenkinsJob#lastBuiltOnProperty()}.
    * @param builtOnListener the {@link JttChangeListener}.
    */
   public void addBuiltOnListener( JttChangeListener< JenkinsJob, JenkinsNode > builtOnListener ) {
      builtOnPropertyListener.addListener( builtOnListener );
   }//End Method

   /**
    * Method to add a {@link JttChangeListener} as a listener for changes in {@link JenkinsJob#currentBuildTimestampProperty()}.
    * @param timestampListener the {@link JttChangeListener}.
    */
   public void addTimestampListener( JttChangeListener< JenkinsJob, Long > timestampListener ) {
      timestampPropertyListener.addListener( timestampListener );
   }//End Method

}//End Class
