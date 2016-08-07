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
import uk.dangrew.jtt.core.JenkinsTestTrackerCoreImpl;
import uk.dangrew.jtt.core.JttSystemCoreImpl;
import uk.dangrew.jtt.credentials.login.JenkinsLogin;
import uk.dangrew.jtt.environment.main.EnvironmentWindow;
import uk.dangrew.jtt.main.digest.SystemDigestController;

/**
 * The {@link JttSceneConstructor} is responsible for constructing the {@link Scene}
 * outside of the {@link JenkinsTestTracker}, to help decouple it.
 */
public class JttSceneConstructor {
   
   private final JttApplicationController controller;
   private final SystemDigestController digestController;
   
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
      ExternalApi api = new JenkinsApiImpl( new ClientHandler() );
      
      if ( !controller.login( new JenkinsLogin( api, digestController.getDigestViewer() ) ) ) {
         return null;
      }
      
      JenkinsTestTrackerCoreImpl core = new JttSystemCoreImpl( api );
      EnvironmentWindow window = new EnvironmentWindow( core.getJenkinsDatabase(), digestController.getDigestViewer() );
      return new Scene( window );
   }//End Method
   
}//End Class
