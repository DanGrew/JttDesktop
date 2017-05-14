/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.main;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sun.javafx.application.PlatformImpl;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import uk.dangrew.jtt.desktop.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.desktop.graphics.PlatformDecouplerImpl;
import uk.dangrew.jtt.desktop.styling.BuildWallStyles;
import uk.dangrew.jtt.desktop.styling.BuildWallThemes;
import uk.dangrew.jtt.desktop.styling.SystemStyling;
import uk.dangrew.jupa.javafx.platform.PlatformLifecycle;
import uk.dangrew.jupa.javafx.platform.PlatformLifecycleImpl;
import uk.dangrew.sd.graphics.launch.TestApplication;

/**
 * {@link JenkinsTestTracker} test.
 */
public class JenkinsTestTrackerIT {
   
   @Mock private JttSceneConstructor constructor;
   @Mock private PlatformLifecycleImpl lifecycle;
   private Scene scene;
   private Stage stage;
   
   @Before public void initialseApplication() throws Exception {
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      
      SystemStyling.initialise();
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      PlatformLifecycle.setInstance( lifecycle );
   }//End Method
   
   @After public void teardown(){
      PlatformLifecycle.setInstance( new PlatformLifecycleImpl() );
   }//End Method
   
   /**
    * Method to launch the application once mocking is complete.
    */
   private void launchApplication(){
      TestApplication.startPlatform();
      PlatformImpl.runAndWait( () -> {
         stage = spy( new Stage() );
         JenkinsTestTracker main = new JenkinsTestTracker( constructor );
         try {
            main.start( stage );
         } catch ( Exception e ) {
            fail( "Something went wrong in launch." );
            e.printStackTrace();
         }
      } );
   }//End Method
   
   /**
    * Method to setup the conditions for making the {@link JttSceneConstructor} provide
    * a real {@link Scene}.
    */
   private void constructorWillProvideScene(){
      scene = new Scene( new Group() );
      when( constructor.makeScene() ).thenReturn( scene );
   }
   
   @Test public void shouldRequestControllerMakeScene(){
      constructorWillProvideScene();
      launchApplication();
      
      assertThat( stage.getScene(), is( scene ) );
      assertThat( stage.isShowing(), is( true ) );
   }//End Method
   
   @Test public void shouldApplyShutdownHandler(){
      constructorWillProvideScene();
      launchApplication();
      
      stage.getOnCloseRequest().handle( new WindowEvent( stage, WindowEvent.WINDOW_CLOSE_REQUEST ) );
      verify( lifecycle ).shutdownPlatform();
   }//End Method
   
   @Test public void shouldReturnIfControllerProvidesNoScene(){
      launchApplication();
      assertThat( stage.isShowing(), is( false ) );
      verify( lifecycle ).shutdownPlatform();
   }//End Method
   
   @Test public void shouldHaveInitialisedSystemStylings() {
      //Expect no exception.
      SystemStyling.applyStyle( BuildWallStyles.ProgressBarFailed, BuildWallThemes.Standard, new Group() );
   }//End Method
   
   @Test public void shouldHaveInitialisedPlatform() {
      //Expect no exception.
      DecoupledPlatformImpl.runLater( () -> {} );
   }//End Method
   
   @Test public void stageShouldBeMaximised(){
      constructorWillProvideScene();
      launchApplication();
      assertThat( stage.isMaximized(), is( true ) );
   }//End Method
   
}//End Class
