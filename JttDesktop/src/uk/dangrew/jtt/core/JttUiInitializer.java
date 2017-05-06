/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.core;

import com.sun.javafx.application.PlatformImpl;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.environment.launch.LaunchOptions;
import uk.dangrew.jtt.environment.main.EnvironmentWindow;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.sd.viewer.basic.DigestViewer;

/**
 * The {@link JttUiInitializer} is a {@link JttSystemInitialization} for the desktop UI.
 */
public class JttUiInitializer implements JttSystemInitialization {
   
   static final String LOADING_JENKINS_JOBS = "Loading Jenkins Jobs...";
   
   private final JavaFxStyle styling;
   private final EnvironmentWindow window;
   private final DigestViewer viewer;
   private final SystemConfiguration configuration;
   private final JenkinsDatabase database;
   
   /**
    * Constructs a new {@link JttUiInitializer}.
    * @param database the {@link JenkinsDatabase}.
    * @param window the {@link EnvironmentWindow}.
    * @param viewer the {@link DigestViewer}.
    * @param configuration the {@link SystemConfiguration}.
    */
   public JttUiInitializer( JenkinsDatabase database, EnvironmentWindow window, DigestViewer viewer, SystemConfiguration configuration ) {
      this( new JavaFxStyle(), database, window, viewer, configuration );
   }//End Constructor
   
   /**
    * Constructs a new {@link JttUiInitializer}.
    * @param database the {@link JenkinsDatabase}.
    * @param window the {@link EnvironmentWindow}.
    * @param viewer the {@link DigestViewer}.
    * @param configuration the {@link SystemConfiguration}.
    */
   JttUiInitializer( JavaFxStyle styling, JenkinsDatabase database, EnvironmentWindow window, DigestViewer viewer, SystemConfiguration configuration ) {
      this.styling = styling;
      this.database = database;
      this.window = window;
      this.viewer = viewer;
      this.configuration = configuration;
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public void beginInitializing(){
      BorderPane content = new BorderPane( new ProgressIndicator() );
      content.setBottom( styling.createBoldLabel( LOADING_JENKINS_JOBS ) );
      
      window.setContent( content );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void systemReady(){
      PlatformImpl.runAndWait( () -> window.setContent( 
               new LaunchOptions( window, configuration, database, viewer ) 
      ) );
   }//End Method

}//End Class
