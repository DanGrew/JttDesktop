package uk.dangrew.jtt.desktop.main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uk.dangrew.jtt.desktop.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.desktop.graphics.PlatformDecouplerImpl;
import uk.dangrew.jtt.desktop.styling.SystemStyling;
import uk.dangrew.jtt.desktop.system.properties.DateAndTimes;
import uk.dangrew.jupa.javafx.platform.PlatformLifecycle;

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
   JenkinsTestTracker( JttSceneConstructor constructor ) {
      this.sceneConstructor = constructor;
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public void start(Stage stage) throws Exception {
      Scene scene = sceneConstructor.makeScene();
      if ( scene == null ) {
         PlatformLifecycle.shutdown();
         return;
      }
      
      stage.setOnCloseRequest( event -> PlatformLifecycle.shutdown() );
      stage.setScene( scene );
      stage.setMaximized( true );
      stage.show();
   }//End Method
   
   public static void main( String[] args ) {
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      SystemStyling.initialise();
      DateAndTimes.initialise();
      launch();
   }//End Method

}//End Class
