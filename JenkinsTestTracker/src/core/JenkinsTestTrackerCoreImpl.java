/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package core;

import api.handling.JenkinsFetcher;
import api.handling.JenkinsFetcherImpl;
import api.sources.ExternalApi;
import storage.database.JenkinsDatabase;
import storage.database.JenkinsDatabaseImpl;
import synchronisation.model.TimeKeeper;
import synchronisation.testresults.JobMonitorImpl;

/**
 * The {@link JenkinsTestTrackerCoreImpl} provides the core structure for maintaining a 
 * {@link JenkinsDatabase} from an {@link ExternalApi}.
 */
public abstract class JenkinsTestTrackerCoreImpl {

   private final JenkinsDatabase database;
   private final JenkinsFetcher fetcher; 
   private TimeKeeper jobUpdater;
   private TimeKeeper buildProgressor;
   
   /**
    * Constructs a new {@link JenkinsTestTrackerCoreImpl}.
    * @param api the {@link ExternalApi} to connect to.
    */
   public JenkinsTestTrackerCoreImpl( ExternalApi api ) {
      database = new JenkinsDatabaseImpl();
      fetcher = new JenkinsFetcherImpl( database, api );
      new JobMonitorImpl( database, fetcher );
      initialiseTimeKeepers();
   }//End Constructor
   
   /**
    * Method to initialise the {@link TimeKeeper}s for polling changes.
    */
   abstract void initialiseTimeKeepers();
   
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
    * Getter for the {@link JenkinsFetcher}.
    * @return the {@link JenkinsFetcher}.
    */
   protected JenkinsFetcher getJenkinsFetcher(){
      return fetcher;
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
