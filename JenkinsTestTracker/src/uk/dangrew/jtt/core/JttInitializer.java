/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.core;

import java.util.Timer;
import java.util.function.Function;

import com.sun.javafx.application.PlatformImpl;

import uk.dangrew.jtt.api.handling.JenkinsFetcherImpl;
import uk.dangrew.jtt.api.handling.JenkinsProcessing;
import uk.dangrew.jtt.api.handling.JenkinsProcessingImpl;
import uk.dangrew.jtt.api.sources.ExternalApi;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.environment.launch.LaunchOptions;
import uk.dangrew.jtt.environment.main.EnvironmentWindow;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.synchronisation.time.BuildProgressor;
import uk.dangrew.jtt.synchronisation.time.JobUpdater;
import uk.dangrew.sd.core.lockdown.DigestProgressReceiverImpl;
import uk.dangrew.sd.core.lockdown.DigestReceiverConnection;
import uk.dangrew.sd.core.source.SourceImpl;
import uk.dangrew.sd.progressbar.model.DigestProgressBar;
import uk.dangrew.sd.viewer.basic.DigestViewer;

/**
 * The {@link JttInitializer} is responsible for initializing the Jtt, pre-processing to avoid
 * unnecessary notifications and redrawing.
 */
public class JttInitializer {
   
   static final long PROGRESS_DELAY = 1000L;
   static final long UPDATE_DELAY = 5000L;
   static final double BAR_WIDTH = 500;
   
   private final Function< Runnable, Thread > threadSupplier;
   private final JenkinsDatabase database;
   private final JenkinsProcessing processing;
   private final EnvironmentWindow window;
   private final SystemConfiguration configuration;
   private final DigestViewer digest;
   
   private JobUpdater jobUpdater;
   private BuildProgressor buildProgressor;
   private DigestReceiverConnection digestConnection;
   
   /**
    * Constructs a new {@link JttInitializer}.
    * @param api the {@link ExternalApi}.
    * @param database the {@link JenkinsDatabase}.
    * @param window the {@link EnvironmentWindow}.
    * @param configuration the {@link SystemConfiguration}.
    * @param viewer the {@link DigestViewer}.
    */
   public JttInitializer( 
            ExternalApi api, 
            JenkinsDatabase database, 
            EnvironmentWindow window, 
            SystemConfiguration configuration, 
            DigestViewer viewer 
   ) {
      this( 
               r -> new Thread( r ), 
               new JenkinsProcessingImpl( database, new JenkinsFetcherImpl( database, api ) ),
               database, window, configuration, viewer 
      );
   }//End Constructor
   
   /**
    * Constructs a new {@link JttInitializer}.
    * @param threadSupplier the {@link Function} for supplying the {@link Thread}.
    * @param processing the {@link JenkinsProcessing}.
    * @param database the {@link JenkinsDatabase}.
    * @param window the {@link EnvironmentWindow}.
    * @param configuration the {@link SystemConfiguration}.
    * @param viewer the {@link DigestViewer}.
    */
   JttInitializer( 
            Function< Runnable, Thread > threadSupplier,
            JenkinsProcessing processing,
            JenkinsDatabase database, 
            EnvironmentWindow window, 
            SystemConfiguration configuration, 
            DigestViewer viewer 
   ) {
      this.threadSupplier = threadSupplier;
      this.processing = processing;
      this.window = window;
      this.database = database;
      this.configuration = configuration;
      this.digest = viewer;
      
      initialise();
   }//End Constructor
   
   /**
    * Method to perform the initialisation.
    */
   private void initialise() {
      DigestProgressBar bar = new DigestProgressBar( new SourceImpl( processing ) );
      bar.setMinWidth( BAR_WIDTH );
      window.setContent( bar );
      
      digestConnection = new DigestProgressReceiverImpl( new InitialSynchronizationReceiver( this, bar ) );
      
      Thread thread = threadSupplier.apply( () -> processing.fetchJobsAndUpdateDetails() );
      thread.start();
   }//End Method
   
   /**
    * To be called when the system is ready to start.
    */
   void systemReady(){
      digestConnection.disconnect();
      startPolling();
      provideOptions();
   }//End Method
   
   /**
    * Method to start the polling for the system.
    */
   private void startPolling(){
      jobUpdater = new JobUpdater( processing, new Timer(), UPDATE_DELAY );
      buildProgressor = new BuildProgressor( database, new Timer(), PROGRESS_DELAY );
   }//End Method
   
   /**
    * Method to provide the options for choosing which tool to run.
    */
   private void provideOptions(){
      PlatformImpl.runAndWait( () -> window.setContent( 
               new LaunchOptions( window, configuration, database, digest ) 
      ) );
   }//End Method

   DigestReceiverConnection digestConnection(){
      return digestConnection;
   }//End Method
   
   JobUpdater jobUpdater(){
      return jobUpdater;
   }//End Method
   
   BuildProgressor buildProgressor(){
      return buildProgressor;
   }//End Method
}//End Class
