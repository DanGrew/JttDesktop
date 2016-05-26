package uk.dangrew.jtt.main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uk.dangrew.jtt.api.sources.ClientHandler;
import uk.dangrew.jtt.api.sources.ExternalApi;
import uk.dangrew.jtt.api.sources.JenkinsApiImpl;
import uk.dangrew.jtt.core.JenkinsTestTrackerCoreImpl;
import uk.dangrew.jtt.core.JttSystemCoreImpl;
import uk.dangrew.jtt.credentials.login.JenkinsLogin;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.PlatformDecouplerImpl;
import uk.dangrew.jtt.javafx.platform.PlatformLifecycle;
import uk.dangrew.jtt.main.selector.ToolSelector;
import uk.dangrew.jtt.styling.SystemStyling;
import uk.dangrew.jtt.system.properties.DateAndTimes;
import viewer.basic.DigestViewer;

/**
 * The {@link JenkinsTestTracker} is the launcher for the application.
 */
public class JenkinsTestTracker extends Application {
   
   private JttApplicationController controller;
   
   /**
    * Constructs a new {@link JenkinsTestTracker}.
    */
   public JenkinsTestTracker() {
      this( new JttApplicationController() );
   }//End Constructor
   
   /**
    * Constructs a new {@link JenkinsTestTracker}.
    * @param controller the {@link JttApplicationController} to support the launching
    * of the {@link JenkinsTestTracker}.
    */
   public JenkinsTestTracker( JttApplicationController controller ) {
      this.controller = controller;
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public void start(Stage stage) throws Exception {
      ExternalApi api = new JenkinsApiImpl( new ClientHandler() );
      DigestViewer digest = new DigestViewer( 600, 200 );
      
      if ( !controller.login( new JenkinsLogin( api, digest ) ) ) {
         return;
      }
      
      ToolSelector selector = new ToolSelector();
      if ( !controller.selectTool( selector ) ) {
         return;
      }
      
      JenkinsTestTrackerCoreImpl core = new JttSystemCoreImpl( api );
      Scene scene = selector.getSelectedTool().construct( core.getJenkinsDatabase(), digest );
      
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
