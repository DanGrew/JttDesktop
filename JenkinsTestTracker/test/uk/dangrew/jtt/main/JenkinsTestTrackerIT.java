/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.main;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.PlatformDecouplerImpl;
import uk.dangrew.jtt.styling.BuildWallStyles;
import uk.dangrew.jtt.styling.SystemStyling;
import uk.dangrew.jupa.javafx.platform.PlatformLifecycle;
import uk.dangrew.jupa.javafx.platform.PlatformLifecycleImpl;

/**
 * {@link JenkinsTestTracker} test.
 */
public class JenkinsTestTrackerIT {
   
   @Mock private JttSceneConstructor constructor;
   private Scene scene;
   private Stage stage;
   
   @Before public void initialseApplication() throws Exception {
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      SystemStyling.initialise();
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
   }//End Method
   
   /**
    * Method to launch the application once mocking is complete.
    */
   private void launchApplication(){
      JavaFxInitializer.runAndWait( () -> {
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
      PlatformLifecycleImpl lifecycle = mock( PlatformLifecycleImpl.class );
      PlatformLifecycle.setInstance( lifecycle );
      
      constructorWillProvideScene();
      launchApplication();
      
      stage.getOnCloseRequest().handle( new WindowEvent( stage, WindowEvent.WINDOW_CLOSE_REQUEST ) );
      verify( lifecycle ).shutdownPlatform();
   }//End Method
   
   @Test public void shouldReturnIfControllerProvidesNoScene(){
      launchApplication();
      assertThat( stage.isShowing(), is( false ) );
   }//End Method
   
   @Test public void shouldHaveInitialisedSystemStylings() {
      //Expect no exception.
      SystemStyling.applyStyle( BuildWallStyles.ProgressBarFailed, new Group() );
   }//End Method
   
   @Test public void shouldHaveInitialisedPlatform() {
      //Expect no exception.
      DecoupledPlatformImpl.runLater( () -> {} );
   }//End Method
   
}//End Class
