package main;
import api.sources.ClientHandler;
import api.sources.ExternalApi;
import api.sources.JenkinsApiImpl;
import core.JenkinsTestTrackerCoreImpl;
import core.JttSystemCoreImpl;
import credentials.login.JenkinsLogin;
import graphics.DecoupledPlatformImpl;
import graphics.PlatformDecouplerImpl;
import javafx.application.Application;
import javafx.platform.PlatformLifecycle;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.selector.ToolSelector;
import styling.SystemStyling;

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
      
      if ( !controller.login( new JenkinsLogin( api ) ) ) {
         return;
      }
      
      ToolSelector selector = new ToolSelector();
      if ( !controller.selectTool( selector ) ) {
         return;
      }
      
      JenkinsTestTrackerCoreImpl core = new JttSystemCoreImpl( api );
      Scene scene = selector.getSelectedTool().construct( core.getJenkinsDatabase() );
      
      stage.setOnCloseRequest( event -> PlatformLifecycle.shutdown() );
      stage.setScene( scene );
      stage.show();
   }//End Method
   
   public static void main( String[] args ) {
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      SystemStyling.initialise();
      launch();
   }//End Method

}//End Class
