/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package main;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import buildwall.layout.BuildWallDisplayImpl;
import categories.IntegrationTest;
import graphics.DecoupledPlatformImpl;
import graphics.JavaFxInitializer;
import graphics.PlatformDecouplerImpl;
import javafx.platform.PlatformLifecycle;
import javafx.platform.PlatformLifecycleImpl;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.selector.ToolSelector;
import main.selector.Tools;
import styling.BuildWallStyles;
import styling.SystemStyling;
import view.table.TestTableView;

/**
 * {@link JenkinsTestTracker} test.
 */
@Category( IntegrationTest.class )
public class JenkinsTestTrackerIT {
   
   @Captor private ArgumentCaptor< ToolSelector > selectorCaptor;
   @Mock private JttApplicationController controller;
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
         JenkinsTestTracker main = new JenkinsTestTracker( controller );
         try {
            main.start( stage );
         } catch ( Exception e ) {
            fail( "Something went wrong in launch." );
            e.printStackTrace();
         }
      } );
   }//End Method
   
   @Test public void shouldHaveInitialisedSystemStylings() {
      //Expect no exception.
      SystemStyling.applyStyle( BuildWallStyles.ProgressBarFailed, new Group() );
   }//End Method
   
   @Test public void shouldHaveInitialisedPlatform() {
      //Expect no exception.
      DecoupledPlatformImpl.runLater( () -> {} );
   }//End Method
   
   @Test public void shouldReturnIfCantLogin(){
      when( controller.login( Mockito.any() ) ).thenReturn( false );
      launchApplication();
      verify( controller, times( 1 ) ).login( Mockito.any() );
      verify( controller, times( 0 ) ).selectTool( Mockito.any() );
   }//End Method
   
   @Test public void shouldReturnIfNotLaunchedCorrectly(){
      when( controller.login( Mockito.any() ) ).thenReturn( true );
      when( controller.selectTool( Mockito.any() ) ).thenReturn( false );
      launchApplication();
      verify( controller, times( 1 ) ).login( Mockito.any() );
      verify( controller, times( 1 ) ).selectTool( Mockito.any() );
      assertThat( stage.isShowing(), is( false ) );
      assertThat( stage.getScene(), nullValue() );
   }//End Method
   
   @Test public void shouldUseBuildWallDisplay(){
      when( controller.login( Mockito.any() ) ).thenReturn( true );
      when( controller.selectTool( Mockito.any() ) ).thenReturn( true );
      when( controller.selectTool( selectorCaptor.capture() ) ).then( invocation -> {
         selectorCaptor.getValue().select( Tools.BuildWall );
         return true;
      } );
      launchApplication();
      verify( controller, times( 1 ) ).login( Mockito.any() );
      verify( controller, times( 1 ) ).selectTool( Mockito.any() );
      
      assertThat( stage.isShowing(), is( true ) );
      assertThat( stage.getScene(), notNullValue() );
      
      assertThat( stage.getScene().getRoot(), instanceOf( BuildWallDisplayImpl.class ) );
      assertThat( stage.getScene().getAccelerators().isEmpty(), is( false ) );
   }//End Method
   
   @Test public void shouldUseTestTableView(){
      when( controller.login( Mockito.any() ) ).thenReturn( true );
      when( controller.selectTool( Mockito.any() ) ).thenReturn( true );
      when( controller.selectTool( selectorCaptor.capture() ) ).then( invocation -> {
         selectorCaptor.getValue().select( Tools.TestTable );
         return true;
      } );
      launchApplication();
      verify( controller, times( 1 ) ).login( Mockito.any() );
      verify( controller, times( 1 ) ).selectTool( Mockito.any() );
      
      assertThat( stage.isShowing(), is( true ) );
      assertThat( stage.getScene(), notNullValue() );
      
      assertThat( stage.getScene().getRoot(), instanceOf( TestTableView.class ) );
      assertThat( stage.getScene().getAccelerators().isEmpty(), is( true ) );
   }//End Method
   
   @Test public void shouldShutdownPlatformOnExit(){
      PlatformLifecycleImpl lifecycle = mock( PlatformLifecycleImpl.class );
      PlatformLifecycle.setInstance( lifecycle );
      
      when( controller.login( Mockito.any() ) ).thenReturn( true );
      when( controller.selectTool( Mockito.any() ) ).thenReturn( true );
      when( controller.selectTool( selectorCaptor.capture() ) ).then( invocation -> {
         selectorCaptor.getValue().select( Tools.TestTable );
         return true;
      } );
      launchApplication();
      stage.getOnCloseRequest().handle( new WindowEvent( stage, WindowEvent.WINDOW_CLOSE_REQUEST ) );
      verify( lifecycle ).shutdownPlatform();
   }//End Method

}//End Class
