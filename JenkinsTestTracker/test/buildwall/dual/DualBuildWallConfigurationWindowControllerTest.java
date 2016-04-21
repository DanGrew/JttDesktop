/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.dual;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.javafx.application.PlatformImpl;

import buildwall.configuration.BuildWallConfiguration;
import buildwall.configuration.BuildWallConfigurationImpl;
import buildwall.configuration.BuildWallJobPolicy;
import buildwall.configuration.window.DualBuildWallConfigurationWindow;
import buildwall.effects.flasher.ImageFlasherProperties;
import buildwall.effects.flasher.ImageFlasherPropertiesImpl;
import graphics.DecoupledPlatformImpl;
import graphics.JavaFxInitializer;
import graphics.TestPlatformDecouplerImpl;
import model.jobs.JenkinsJobImpl;

/**
 * {@link DualBuildWallConfigurationWindowController} test.
 */
public class DualBuildWallConfigurationWindowControllerTest {
   
   private BuildWallConfiguration rightConfig;
   private ImageFlasherProperties imageConfig;
   private BuildWallConfiguration leftConfig;
   private DualBuildWallConfigurationWindowController systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      rightConfig = new BuildWallConfigurationImpl();
      imageConfig = new ImageFlasherPropertiesImpl();
      leftConfig = new BuildWallConfigurationImpl();
      
      for ( int i = 0; i < 10; i++ ) {
         rightConfig.jobPolicies().put( new JenkinsJobImpl( "job " + i ), BuildWallJobPolicy.values()[ i % 3 ] );
      }
      leftConfig.jobPolicies().putAll( rightConfig.jobPolicies() );
      
      JavaFxInitializer.startPlatform();
      systemUnderTest = new DualBuildWallConfigurationWindowController();
      systemUnderTest.associateWithConfiguration( leftConfig, imageConfig, rightConfig );
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
      assertThat( systemUnderTest.stage().getScene().getRoot(), instanceOf( DualBuildWallConfigurationWindow.class ) );
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

}//End Class
