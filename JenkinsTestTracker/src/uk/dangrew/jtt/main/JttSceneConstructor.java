/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.main;

import javafx.scene.Scene;
import uk.dangrew.jtt.api.sources.ClientHandler;
import uk.dangrew.jtt.api.sources.ExternalApi;
import uk.dangrew.jtt.api.sources.JenkinsApiImpl;
import uk.dangrew.jtt.buildwall.configuration.persistence.buildwall.BuildWallConfigurationSessions;
import uk.dangrew.jtt.buildwall.configuration.persistence.dualwall.DualWallConfigurationSessions;
import uk.dangrew.jtt.buildwall.configuration.persistence.sound.SoundConfigurationSessions;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.core.JttCoreInitializer;
import uk.dangrew.jtt.core.JttUiInitializer;
import uk.dangrew.jtt.credentials.login.JenkinsLogin;
import uk.dangrew.jtt.environment.main.EnvironmentWindow;
import uk.dangrew.jtt.main.digest.SystemDigestController;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;

/**
 * The {@link JttSceneConstructor} is responsible for constructing the {@link Scene}
 * outside of the {@link JenkinsTestTracker}, to help decouple it.
 */
public class JttSceneConstructor {
   
   private final JttApplicationController controller;
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
      this( new JttApplicationController(), new SystemDigestController() );
   }//End Constructor
   
   /**
    * Constructs a new {@link JttSceneConstructor}.
    * @param controller the {@link JttApplicationController} to support construction.
    * @param digestController the {@link SystemDigestController} for managing the digest.
    */
   JttSceneConstructor( JttApplicationController controller, SystemDigestController digestController ){
      this.controller = controller;
      this.digestController = digestController;
   }//End Constructor

   /**
    * Method to make the {@link Scene} for the {@link JenkinsTestTracker}.
    * @return the {@link Scene} constructed, or null if the user backs out.
    */
   public Scene makeScene() {
      if ( configuration != null ) {
         throw new IllegalStateException( "Can only call once." );
      }
      
      ExternalApi api = new JenkinsApiImpl( new ClientHandler() );
      
      if ( !controller.login( new JenkinsLogin( api, digestController.getDigestViewer() ) ) ) {
         return null;
      }
      
      configuration = new SystemConfiguration();
      database = new JenkinsDatabaseImpl();
      
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
               database, 
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
