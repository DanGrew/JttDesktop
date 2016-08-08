/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.environment.preferences;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.javafx.application.PlatformImpl;

import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallJobPolicy;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.configuration.tree.ConfigurationTreePane;
import uk.dangrew.jtt.environment.preferences.ConfigurationWindowController;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;

/**
 * {@link ConfigurationWindowController} test.
 */
public class ConfigurationWindowControllerTest {
   
   private SystemConfiguration systemConfiguration;
   private ConfigurationWindowController systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      systemConfiguration = new SystemConfiguration();
      
      for ( int i = 0; i < 10; i++ ) {
         systemConfiguration.getRightConfiguration().jobPolicies().put( new JenkinsJobImpl( "job " + i ), BuildWallJobPolicy.values()[ i % 3 ] );
      }
      systemConfiguration.getLeftConfiguration().jobPolicies().putAll( systemConfiguration.getRightConfiguration().jobPolicies() );
      
      JavaFxInitializer.startPlatform();
      systemUnderTest = new ConfigurationWindowController();
      systemUnderTest.associateWithConfiguration( systemConfiguration );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAllowShowIfNotAssociated(){
      systemUnderTest = new ConfigurationWindowController();
      systemUnderTest.showConfigurationWindow();
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAllowHideIfNotAssociated(){
      systemUnderTest = new ConfigurationWindowController();
      systemUnderTest.hideConfigurationWindow();
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAllowIsShowingCheckIfNotAssociated(){
      systemUnderTest = new ConfigurationWindowController();
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
      assertThat( systemUnderTest.stage().widthProperty().get(), is( ConfigurationWindowController.WIDTH ) );
      assertThat( systemUnderTest.stage().heightProperty().get(), is( ConfigurationWindowController.HEIGHT ) );
   }//End Method
}//End Class
