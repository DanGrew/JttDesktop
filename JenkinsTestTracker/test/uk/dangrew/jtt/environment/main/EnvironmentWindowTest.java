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
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.environment.launch.LaunchOptions;
import uk.dangrew.jtt.environment.layout.CenterScreenWrapper;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.sd.viewer.basic.DigestViewer;

/**
 * {@link EnvironmentWindow} test.
 */
public class EnvironmentWindowTest {

   private SystemConfiguration configuration;
   @Mock private JenkinsDatabase database;
   @Mock private DigestViewer digest;
   private EnvironmentWindow systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      MockitoAnnotations.initMocks( this );
      configuration = new SystemConfiguration();
      systemUnderTest = new EnvironmentWindow( configuration, database, digest );
   }//End Method
   
   @Ignore
   @Test public void maunal() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> new EnvironmentWindow(configuration, null, null) );
      
      Thread.sleep( 1000000 );
   }//End Method
   
   @Test public void shouldProvideHiddenSidesPaneToHideMenuBarWhenNotInUse(){
      assertThat( systemUnderTest.hiddenSidesPane(), is( systemUnderTest.getCenter() ) );
   }//End Method
   
   @Test public void shouldProvideMenuBarInTopOfHiddenPane(){
      assertThat( systemUnderTest.hiddenSidesPane().getTop(), is( instanceOf( EnvironmentMenuBar.class ) ) );
   }//End Method
   
   @Test public void shouldUseLaunchOptionsDisplayedInCenter(){
      assertThat( systemUnderTest.hiddenSidesPane().getContent(), is( instanceOf( CenterScreenWrapper.class ) ) );
      CenterScreenWrapper wrapper = ( CenterScreenWrapper ) systemUnderTest.hiddenSidesPane().getContent();
      assertThat( wrapper.getCenter(), is( instanceOf( LaunchOptions.class ) ) );
   }//End Method
   
   @Test public void shouldHavePreferenceOpener(){
      assertThat( systemUnderTest.preferenceOpener(), is( notNullValue() ) );
      assertThat( systemUnderTest.preferenceOpener().usesConfiguration( configuration ), is( true ) );
   }//End Method
   
}//End Class
