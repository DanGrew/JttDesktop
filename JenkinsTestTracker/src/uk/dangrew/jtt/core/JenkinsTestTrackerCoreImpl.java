/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.core;

import uk.dangrew.jtt.api.handling.JenkinsFetcher;
import uk.dangrew.jtt.api.handling.JenkinsFetcherImpl;
import uk.dangrew.jtt.api.handling.JenkinsProcessing;
import uk.dangrew.jtt.api.handling.JenkinsProcessingImpl;
import uk.dangrew.jtt.api.sources.ExternalApi;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;
import uk.dangrew.jtt.synchronisation.model.TimeKeeper;

/**
 * The {@link JenkinsTestTrackerCoreImpl} provides the core structure for maintaining a 
 * {@link JenkinsDatabase} from an {@link ExternalApi}.
 */
public abstract class JenkinsTestTrackerCoreImpl {

   private final JenkinsDatabase database;
   private final JenkinsProcessing jenkinsProcessing; 
   private TimeKeeper jobUpdater;
   private TimeKeeper buildProgressor;
   
   /**
    * Constructs a new {@link JenkinsTestTrackerCoreImpl}.
    * @param api the {@link ExternalApi} to connect to.
    */
   public JenkinsTestTrackerCoreImpl( ExternalApi api ) {
      database = new JenkinsDatabaseImpl();
      JenkinsFetcher fetcher = new JenkinsFetcherImpl( database, api );
      jenkinsProcessing = new JenkinsProcessingImpl( database, fetcher );
//      new JobMonitorImpl( database, fetcher );
   }//End Constructor
   
   /**
    * Method to initialise the {@link TimeKeeper}s for polling changes. To be called
    * manually for synchronization issues.
    */
   public abstract void initialiseTimeKeepers();
   
   /**
    * Setter for the {@link TimeKeeper}s created with specific configurations for the different environments.
    * @param jobUpdater the {@link TimeKeeper} for the {@link JobUpdater}.
    * @param buildProgressor the {@link TimeKeeper} for the {@link BuildProgressor}.
    */
   protected void setTimeKeepers( TimeKeeper jobUpdater, TimeKeeper buildProgressor ) {
      if ( this.jobUpdater != null || this.buildProgressor != null ) throw new IllegalArgumentException( "Already initialised." );
      
      this.jobUpdater = jobUpdater;
      this.buildProgressor = buildProgressor;
   }//End Method
   
   /**
    * Getter for the {@link JenkinsDatabase}.
    * @return the {@link JenkinsDatabase}.
    */
   public JenkinsDatabase getJenkinsDatabase() {
      return database;
   }//End Method
   
   /**
    * Getter for the {@link JenkinsProcessing}.
    * @return the {@link JenkinsProcessing}.
    */
   protected JenkinsProcessing getJenkinsProcessing(){
      return jenkinsProcessing;
   }//End Method

   /**
    * Getter for the {@link TimeKeeper}.
    * @return the {@link TimeKeeper}.
    */
   public TimeKeeper getJobUpdater() {
      return jobUpdater;
   }//End Method
   
   /**
    * Getter for the {@link BuildProgressor}.
    * @return the {@link BuildProgressor}.
    */
   protected TimeKeeper getBuildProgressor() {
      return buildProgressor;
   }//End Method

}//End Class
