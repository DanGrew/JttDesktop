/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.main;

import javafx.scene.Scene;
import uk.dangrew.jtt.connection.api.sources.ExternalApi;
import uk.dangrew.jtt.desktop.buildwall.configuration.persistence.buildwall.BuildWallConfigurationSessions;
import uk.dangrew.jtt.desktop.buildwall.configuration.persistence.dualwall.DualWallConfigurationSessions;
import uk.dangrew.jtt.desktop.buildwall.configuration.persistence.sound.SoundConfigurationSessions;
import uk.dangrew.jtt.desktop.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.desktop.core.JenkinsApiConnector;
import uk.dangrew.jtt.desktop.core.JttCoreInitializer;
import uk.dangrew.jtt.desktop.core.JttUiInitializer;
import uk.dangrew.jtt.desktop.environment.main.EnvironmentWindow;
import uk.dangrew.jtt.desktop.main.digest.SystemDigestController;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.SystemWideJenkinsDatabaseImpl;

/**
 * The {@link JttSceneConstructor} is responsible for constructing the {@link Scene}
 * outside of the {@link JenkinsTestTracker}, to help decouple it.
 */
public class JttSceneConstructor {
   
   private final JenkinsApiConnector apiConnector;
   private final SystemDigestController digestController;
   
   private JttCoreInitializer initializer;
   private JenkinsDatabase database;
   private SystemConfiguration configuration;
   private BuildWallConfigurationSessions buildWallSessions;
   private DualWallConfigurationSessions dualWallSessions;
   private SoundConfigurationSessions soundSessions;
   
   /**
    * Constructs a new {@link JttSceneConstructor}.
    */
   public JttSceneConstructor() {
      this( new SystemDigestController(), new JenkinsApiConnector() );
   }//End Constructor
   
   /**
    * Constructs a new {@link JttSceneConstructor}.
    * @param digestController the {@link SystemDigestController} for managing the digest.
    * @param apiConnector the {@link JenkinsApiConnector}.
    */
   JttSceneConstructor( SystemDigestController digestController, JenkinsApiConnector apiConnector ){
      this.digestController = digestController;
      this.apiConnector = apiConnector;
   }//End Constructor

   /**
    * Method to make the {@link Scene} for the {@link JenkinsTestTracker}.
    * @return the {@link Scene} constructed, or null if the user backs out.
    */
   public Scene makeScene() {
      if ( configuration != null ) {
         throw new IllegalStateException( "Can only call once." );
      }
      
      ExternalApi api = apiConnector.connect( digestController.getDigestViewer() );
      if ( api == null ) {
         return null;
      }
      
      configuration = new SystemConfiguration();
      database = new SystemWideJenkinsDatabaseImpl().get();
      
      buildWallSessions = new BuildWallConfigurationSessions( 
               database, 
               configuration.getLeftConfiguration(), 
               configuration.getRightConfiguration() 
      );
      dualWallSessions = new DualWallConfigurationSessions( configuration.getDualConfiguration() );
      soundSessions = new SoundConfigurationSessions( configuration.getSoundConfiguration(), database );
      
      EnvironmentWindow window = new EnvironmentWindow( configuration, database );
      
      initializer = new JttCoreInitializer( 
               api, 
               new JttUiInitializer( 
                        database, 
                        window, 
                        digestController.getDigestViewer(), 
                        configuration 
               ) 
      );
      return new Scene( window );
   }//End Method

   JttCoreInitializer initializer(){
      return initializer;
   }//End Method
   
   JenkinsDatabase database(){
      return database;
   }//End Method
   
   SystemConfiguration configuration(){
      return configuration;
   }//End Method
   
   BuildWallConfigurationSessions buildWallSessions() {
      return buildWallSessions;
   }//End Method
   
   DualWallConfigurationSessions dualWallSessions() {
      return dualWallSessions;
   }//End Method

   SoundConfigurationSessions soundSessions() {
      return soundSessions;
   }//End Method
   
}//End Class
