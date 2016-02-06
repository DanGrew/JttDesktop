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
import synchronisation.testresults.JobMonitorImpl;
import synchronisation.time.TimeKeeper;

/**
 * The {@link JenkinsTestTrackerCoreImpl} provides the core structure for maintaining a 
 * {@link JenkinsDatabase} from an {@link ExternalApi}.
 */
public class JenkinsTestTrackerCoreImpl {

   private final JenkinsDatabase database;
   private final TimeKeeper timeKeeper;
   
   /**
    * Constructs a new {@link JenkinsTestTrackerCoreImpl}.
    * @param api the {@link ExternalApi} to connect to.
    */
   public JenkinsTestTrackerCoreImpl( ExternalApi api ) {
      database = new JenkinsDatabaseImpl();
      JenkinsFetcher fetcher = new JenkinsFetcherImpl( database, api );
      timeKeeper = new TimeKeeper( fetcher, 1000l );
      new JobMonitorImpl( database, fetcher );
   }//End Constructor
   
   /**
    * Getter for the {@link JenkinsDatabase}.
    * @return the {@link JenkinsDatabase}.
    */
   public JenkinsDatabase getDatabase() {
      return database;
   }//End Method

   /**
    * Getter for the {@link TimeKeeper}.
    * @return the {@link TimeKeeper}.
    */
   public TimeKeeper getTimeKeeper() {
      return timeKeeper;
   }//End Method

}//End Class
