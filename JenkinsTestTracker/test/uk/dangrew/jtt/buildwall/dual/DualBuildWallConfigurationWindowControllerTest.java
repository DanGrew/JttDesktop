/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.dual;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.javafx.application.PlatformImpl;

import uk.dangrew.jtt.buildwall.configuration.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.BuildWallConfigurationImpl;
import uk.dangrew.jtt.buildwall.configuration.BuildWallJobPolicy;
import uk.dangrew.jtt.buildwall.configuration.window.DualBuildWallConfigurationWindow;
import uk.dangrew.jtt.buildwall.dual.DualBuildWallConfigurationWindowController;
import uk.dangrew.jtt.buildwall.effects.flasher.ImageFlasherProperties;
import uk.dangrew.jtt.buildwall.effects.flasher.ImageFlasherPropertiesImpl;
import uk.dangrew.jtt.configuration.tree.ConfigurationTreePane;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;

/**
 * {@link DualBuildWallConfigurationWindowController} test.
 */
public class DualBuildWallConfigurationWindowControllerTest {
   
   private BuildWallConfiguration rightConfig;
   private BuildWallConfiguration leftConfig;
   private DualBuildWallConfigurationWindowController systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      rightConfig = new BuildWallConfigurationImpl();
      leftConfig = new BuildWallConfigurationImpl();
      
      for ( int i = 0; i < 10; i++ ) {
         rightConfig.jobPolicies().put( new JenkinsJobImpl( "job " + i ), BuildWallJobPolicy.values()[ i % 3 ] );
      }
      leftConfig.jobPolicies().putAll( rightConfig.jobPolicies() );
      
      JavaFxInitializer.startPlatform();
      systemUnderTest = new DualBuildWallConfigurationWindowController();
      systemUnderTest.associateWithConfiguration( leftConfig, rightConfig );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAllowShowIfNotAssociated(){
      systemUnderTest = new DualBuildWallConfigurationWindowController();
      systemUnderTest.showConfigurationWindow();
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAllowHideIfNotAssociated(){
      systemUnderTest = new DualBuildWallConfigurationWindowController();
      systemUnderTest.hideConfigurationWindow();
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAllowIsShowingCheckIfNotAssociated(){
      systemUnderTest = new DualBuildWallConfigurationWindowController();
      systemUnderTest.isConfigurationWindowShowing();
   }//End Method
   
   @Test public void shouldHaveInitialisedStage() {
      assertThat( systemUnderTest.stage(), is( notNullValue() ) );
   }//End Method
   
   @Test public void stageShouldHaveConfigurationWindowWithinScene(){
      assertThat( systemUnderTest.stage().getScene(), is( notNullValue() ) );
      assertThat( systemUnderTest.stage().getScene().getRoot(), is( notNullValue() ) );
      assertThat( systemUnderTest.stage().getScene().getRoot(), instanceOf( ConfigurationTreePane.class ) );
   }//End Method
   
   @Test public void stageShouldBeHiddenInitially(){
      assertThat( systemUnderTest.stage().isShowing(), is( false ) );
   }//End Method
   
   @Test public void stageShouldShowWhenTold(){
      assertThat( systemUnderTest.stage().isShowing(), is( false ) );
      systemUnderTest.showConfigurationWindow();
      PlatformImpl.runAndWait( () -> {} );
      assertThat( systemUnderTest.stage().isShowing(), is( true ) );
      assertThat( systemUnderTest.isConfigurationWindowShowing(), is( true ) );
   }//End Method
   
   @Test public void stageShouldHideWhenTold(){
      stageShouldShowWhenTold();
      assertThat( systemUnderTest.stage().isShowing(), is( true ) );
      systemUnderTest.hideConfigurationWindow();
      PlatformImpl.runAndWait( () -> {} );
      assertThat( systemUnderTest.stage().isShowing(), is( false ) );
      assertThat( systemUnderTest.isConfigurationWindowShowing(), is( false ) );
   }//End Method

   @Test public void shouldSizeStageAndMakeNotFullScreen(){
      assertThat( systemUnderTest.stage().isFullScreen(), is( false ) );
      assertThat( systemUnderTest.stage().widthProperty().get(), is( DualBuildWallConfigurationWindowController.WIDTH ) );
      assertThat( systemUnderTest.stage().heightProperty().get(), is( DualBuildWallConfigurationWindowController.HEIGHT ) );
   }//End Method
}//End Class
