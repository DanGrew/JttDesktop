package uk.dangrew.jtt.main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.PlatformDecouplerImpl;
import uk.dangrew.jtt.javafx.platform.PlatformLifecycle;
import uk.dangrew.jtt.styling.SystemStyling;
import uk.dangrew.jtt.system.properties.DateAndTimes;

/**
 * The {@link JenkinsTestTracker} is the launcher for the application.
 */
public class JenkinsTestTracker extends Application {
   
   private final JttSceneConstructor sceneConstructor;
   
   /**
    * Constructs a new {@link JenkinsTestTracker}.
    */
   public JenkinsTestTracker() {
      this( new JttSceneConstructor() );
   }//End Constructor
   
   /**
    * Constructs a new {@link JenkinsTestTracker}.
    * @param controller the {@link JttSceneConstructor} to support the launching
    * of the {@link JenkinsTestTracker}.
    */
   public JenkinsTestTracker( JttSceneConstructor constructor ) {
      this.sceneConstructor = constructor;
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public void start(Stage stage) throws Exception {
      Scene scene = sceneConstructor.makeScene();
      if ( scene == null ) {
         return;
      }
      
      stage.setOnCloseRequest( event -> PlatformLifecycle.shutdown() );
      stage.setScene( scene );
      stage.show();
   }//End Method
   
   public static void main( String[] args ) {
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      SystemStyling.initialise();
      DateAndTimes.initialise();
      launch();
   }//End Method

}//End Class
