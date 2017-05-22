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

import uk.dangrew.jtt.connection.api.handling.live.LiveStateFetcher;
import uk.dangrew.jtt.connection.api.sources.ExternalApi;
import uk.dangrew.jtt.connection.synchronisation.time.BuildProgressor;
import uk.dangrew.jtt.connection.synchronisation.time.JobUpdater;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.SystemWideJenkinsDatabaseImpl;

/**
 * The {@link JttInitializer} is responsible for initializing the Jtt, pre-processing to avoid
 * unnecessary notifications and redrawing.
 */
@Deprecated //should be replaced with connection management
public class JttCoreInitializer {
   
   static final long PROGRESS_DELAY = 1000L;
   static final long UPDATE_DELAY = 5000L;
   static final double BAR_WIDTH = 500;
   
   private final Function< Runnable, Thread > threadSupplier;
   private final JenkinsDatabase database;
   private final JttSystemInitialization systemInitializer;
   
   private BuildProgressor buildProgressor;
   
   /**
    * Constructs a new {@link JttInitializer}.
    * @param systemInitializer the {@link JttSystemInitialization}.
    */
   public JttCoreInitializer( 
            JttSystemInitialization systemInitializer
   ) {
      this( 
               r -> new Thread( r ), 
               new SystemWideJenkinsDatabaseImpl().get(),
               systemInitializer
      );
   }//End Constructor
   
   /**
    * Constructs a new {@link JttInitializer}.
    * @param threadSupplier the {@link Function} for supplying the {@link Thread}.
    * @param database the {@link JenkinsDatabase}.
    * @param systemInitializer the {@link JttSystemInitialization}.
    */
   JttCoreInitializer( 
            Function< Runnable, Thread > threadSupplier,
            JenkinsDatabase database,
            JttSystemInitialization systemInitializer
   ) {
      this.threadSupplier = threadSupplier;
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
      buildProgressor = new BuildProgressor( database, new Timer(), PROGRESS_DELAY );
   }//End Method
   
   BuildProgressor buildProgressor(){
      return buildProgressor;
   }//End Method
}//End Class
