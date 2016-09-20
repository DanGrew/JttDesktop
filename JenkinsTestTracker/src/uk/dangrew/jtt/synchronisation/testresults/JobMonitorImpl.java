/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.synchronisation.testresults;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.util.Pair;
import uk.dangrew.jtt.api.handling.JenkinsFetcher;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * The {@link JobMonitorImpl} monitors the state of the {@link JenkinsJob#lastBuildProperty()} in order
 * to determine when to update the {@link JenkinsDatabase} with the new test results.
 */
public class JobMonitorImpl implements ListChangeListener< JenkinsJob > {

   private JenkinsFetcher fetcher;
   private Map< JenkinsJob, ChangeListener< Pair< Integer, BuildResultStatus > > > lastBuildNumberListeners;
   
   /**
    * Constructs a new {@link JobMonitorImpl}.
    * @param database the {@link JenkinsDatabase} to update tests in.
    * @param fetcher the {@link JenkinsFetcher} to retrieve the results.
    */
   public JobMonitorImpl( JenkinsDatabase database, JenkinsFetcher fetcher ) {
      lastBuildNumberListeners = new HashMap<>();
      database.jenkinsJobs().addListener( this );
      this.fetcher = fetcher;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void onChanged( Change< ? extends JenkinsJob > c ) {
      while ( c.next() ) {
         if ( c.wasAdded() ) {
            c.getAddedSubList().forEach( job -> handleNewJob( job ) );
         }
         if ( c.wasRemoved() ) {
            c.getRemoved().forEach( job -> handleRemovedJob( job ) );
         }
      }
   }//End Method
   
   /**
    * Method to handle the addition of a {@link JenkinsJob} into the {@link JenkinsDatabase}.
    * @param job the {@link JenkinsJob} added.
    */
   private void handleNewJob( JenkinsJob job ) {
      ChangeListener< Pair< Integer, BuildResultStatus > > lastBuildNumberListener = ( source, old, updated ) -> {
         fetcher.updateTestResults( job );
      };
      lastBuildNumberListeners.put( job, lastBuildNumberListener );
      job.lastBuildProperty().addListener( lastBuildNumberListener );
      fetcher.updateTestResults( job );
   }//End Method
   
   /**
    * Method to handle the removing of a {@link JenkinsJob} from the {@link JenkinsDatabase}.
    * @param job the {@link JenkinsJob} removed.
    */
   private void handleRemovedJob( JenkinsJob job ) {
      ChangeListener< Pair< Integer, BuildResultStatus > > listener = lastBuildNumberListeners.get( job );
      job.lastBuildProperty().removeListener( listener );
   }//End Method
   
}//End Class
