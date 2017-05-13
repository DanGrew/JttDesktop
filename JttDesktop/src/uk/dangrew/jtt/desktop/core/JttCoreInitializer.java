/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.core;

import java.util.Timer;
import java.util.function.Function;

import uk.dangrew.jtt.connection.api.sources.ExternalApi;
import uk.dangrew.jtt.desktop.api.handling.live.LiveStateFetcher;
import uk.dangrew.jtt.desktop.synchronisation.time.BuildProgressor;
import uk.dangrew.jtt.desktop.synchronisation.time.JobUpdater;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.SystemWideJenkinsDatabaseImpl;

/**
 * The {@link JttInitializer} is responsible for initializing the Jtt, pre-processing to avoid
 * unnecessary notifications and redrawing.
 */
public class JttCoreInitializer {
   
   static final long PROGRESS_DELAY = 1000L;
   static final long UPDATE_DELAY = 5000L;
   static final double BAR_WIDTH = 500;
   
   private final Function< Runnable, Thread > threadSupplier;
   private final JenkinsDatabase database;
   private final LiveStateFetcher fetcher;
   private final JttSystemInitialization systemInitializer;
   
   private JobUpdater jobUpdater;
   private BuildProgressor buildProgressor;
   
   /**
    * Constructs a new {@link JttInitializer}.
    * @param api the {@link ExternalApi}.
    * @param database the {@link JenkinsDatabase}.
    * @param systemInitializer the {@link JttSystemInitialization}.
    */
   public JttCoreInitializer( 
            ExternalApi api, 
            JttSystemInitialization systemInitializer
   ) {
      this( 
               r -> new Thread( r ), 
               new LiveStateFetcher( api ),
               new SystemWideJenkinsDatabaseImpl().get(),
               systemInitializer
      );
   }//End Constructor
   
   /**
    * Constructs a new {@link JttInitializer}.
    * @param styling the {@link JavaFxStyle}.
    * @param threadSupplier the {@link Function} for supplying the {@link Thread}.
    * @param fetcher the {@link LiveStateFetcher}.
    * @param database the {@link JenkinsDatabase}.
    * @param systemInitializer the {@link JttSystemInitialization}.
    */
   JttCoreInitializer( 
            Function< Runnable, Thread > threadSupplier,
            LiveStateFetcher fetcher,
            JenkinsDatabase database,
            JttSystemInitialization systemInitializer
   ) {
      this.threadSupplier = threadSupplier;
      this.fetcher = fetcher;
      this.database = database;
      this.systemInitializer = systemInitializer;
      
      initialise();
   }//End Constructor
   
   /**
    * Method to perform the initialisation.
    */
   private void initialise() {
      systemInitializer.beginInitializing();
      
      Thread thread = threadSupplier.apply( () -> {
         fetcher.loadLastCompletedBuild();
         systemReady();
      } );
         
      thread.start();
   }//End Method
   
   /**
    * To be called when the system is ready to start.
    */
   void systemReady(){
      startPolling();
      systemInitializer.systemReady();
   }//End Method
   
   /**
    * Method to start the polling for the system.
    */
   private void startPolling(){
      jobUpdater = new JobUpdater( fetcher, new Timer(), UPDATE_DELAY );
      buildProgressor = new BuildProgressor( database, new Timer(), PROGRESS_DELAY );
   }//End Method
   
   JobUpdater jobUpdater(){
      return jobUpdater;
   }//End Method
   
   BuildProgressor buildProgressor(){
      return buildProgressor;
   }//End Method
}//End Class
