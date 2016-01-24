/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
import java.util.Optional;

import api.sources.ClientHandler;
import api.sources.ExternalApi;
import api.sources.JenkinsApiImpl;
import core.JenkinsTestTrackerCoreImpl;
import credentials.login.JenkinsLogin;
import friendly.controlsfx.FriendlyAlert;
import graphics.DecoupledPlatformImpl;
import graphics.PlatformDecouplerImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import view.table.TestTableView;

/**
 * The {@link JenkinsTestTracker} is the launcher for the application.
 */
public class JenkinsTestTracker extends Application {

   /**
    * {@inheritDoc}
    */
   @Override public void start(Stage stage) throws Exception {
      ExternalApi api = new JenkinsApiImpl( new ClientHandler() );
      JenkinsLogin login = new JenkinsLogin( api );
      FriendlyAlert alert = new FriendlyAlert( AlertType.INFORMATION );
      login.configureAlert( alert );
      Optional< ButtonType > result = alert.showAndWait();
      if ( !login.isLoginResult( result.get() ) ) {
         return;
      }
      
      JenkinsTestTrackerCoreImpl core = new JenkinsTestTrackerCoreImpl( api );
      Scene scene = new Scene( new TestTableView( core.getDatabase() ) );
      stage.setScene( scene );
      stage.show();
   }//End Method
   
   public static void main( String[] args ) {
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      launch();
   }//End Method

}//End Class
