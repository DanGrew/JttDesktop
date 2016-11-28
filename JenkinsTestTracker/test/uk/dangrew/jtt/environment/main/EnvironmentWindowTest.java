/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.environment.main;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.environment.layout.CenterScreenWrapper;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.TestPlatformDecouplerImpl;

/**
 * {@link EnvironmentWindow} test.
 */
public class EnvironmentWindowTest {

   @Spy private CenterScreenWrapper wrapper;
   private SystemConfiguration configuration;
   private EnvironmentWindow systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      MockitoAnnotations.initMocks( this );
      configuration = new SystemConfiguration();
      systemUnderTest = new EnvironmentWindow( wrapper, configuration );
   }//End Method
   
   @Ignore
   @Test public void maunal() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> systemUnderTest = new EnvironmentWindow( configuration ) );
      Thread.sleep( 1000000 );
   }//End Method
   
   @Test public void shouldProvideMenuBarInTopOfPane(){
      assertThat( systemUnderTest.getTop(), is( instanceOf( EnvironmentMenuBar.class ) ) );
   }//End Method
   
   @Test public void shouldNotHaveAnythingDisplayedInCenterInitially(){
      assertThat( systemUnderTest.getCenter(), is( instanceOf( CenterScreenWrapper.class ) ) );
      CenterScreenWrapper wrapper = ( CenterScreenWrapper ) systemUnderTest.getCenter();
      assertThat( wrapper.getCenter(), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldHavePreferenceOpener(){
      assertThat( systemUnderTest.preferenceOpener(), is( notNullValue() ) );
      assertThat( systemUnderTest.preferenceOpener().usesConfiguration( configuration ), is( true ) );
   }//End Method
   
   @Test public void shouldSetCenterUsingWrapper(){
      Node content = new GridPane();
      systemUnderTest.setContent( content );
      verify( wrapper ).setCenter( content );
   }//End Method
   
   @Test public void shouldAccountForMenuBarInDimensionBinding(){
      EnvironmentMenuBar menuBar = ( EnvironmentMenuBar ) systemUnderTest.getTop();
      menuBar.setUseSystemMenuBar( false );
      menuBar.setMaxHeight( 27 );
      menuBar.setMinHeight( 27 );
      systemUnderTest.setTop( menuBar );
      JavaFxInitializer.launchInWindow( () -> systemUnderTest );
      
      assertThat( menuBar.getHeight(), is( 27.0 ) );
      
      BorderPane pane = new BorderPane();
      systemUnderTest.bindDimensions( pane );
      assertThat( pane.getMaxHeight(), is( systemUnderTest.getHeight() - menuBar.getHeight() ) );
      assertThat( pane.getMinHeight(), is( systemUnderTest.getHeight() - menuBar.getHeight() ) );
      assertThat( pane.getMaxWidth(), is( systemUnderTest.getWidth() ) );
      assertThat( pane.getMinWidth(), is( systemUnderTest.getWidth() ) );
   }//End Method
   
}//End Class
