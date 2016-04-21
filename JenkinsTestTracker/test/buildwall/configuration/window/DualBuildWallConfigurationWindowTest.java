/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration.window;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import buildwall.configuration.BuildWallConfiguration;
import buildwall.configuration.BuildWallConfigurationImpl;
import buildwall.configuration.BuildWallJobPolicy;
import buildwall.effects.flasher.ImageFlasherProperties;
import buildwall.effects.flasher.ImageFlasherPropertiesImpl;
import buildwall.effects.flasher.configuration.ImageFlasherConfigurationPanel;
import graphics.DecoupledPlatformImpl;
import graphics.JavaFxInitializer;
import graphics.TestPlatformDecouplerImpl;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import model.jobs.JenkinsJobImpl;

/**
 * {@link DualBuildWallConfigurationWindow} test.
 */
public class DualBuildWallConfigurationWindowTest {

   private BuildWallConfiguration rightConfig;
   private ImageFlasherProperties imageConfig;
   private BuildWallConfiguration leftConfig;
   private DualBuildWallConfigurationWindow systemUnderTest;
   
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
      systemUnderTest = new DualBuildWallConfigurationWindow( leftConfig, imageConfig, rightConfig );
   }//End Method
   
   @Ignore //For manual inspection.
   @Test public void manualInspection() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> { return systemUnderTest; } );
      Thread.sleep( 100000 );
   }//End Method
   
   @Test public void shouldSplitConfigurationPanelsEvenly() {
      assertThat( systemUnderTest.getColumnConstraints(), hasSize( 3 ) );
      for ( ColumnConstraints constraint : systemUnderTest.getColumnConstraints() ) {
         assertThat( constraint.getPercentWidth(), is( DualBuildWallConfigurationWindow.CONFIG_PANEL_WIDTH_PERCENT ) );
      }
   }//End Method
   
   @Test public void shouldProvideBuildWallConfigurationInScrollers(){
      assertThat( systemUnderTest.leftScroller().getContent(), is( systemUnderTest.leftConfigPanel() ) );
      assertThat( systemUnderTest.rightScroller().getContent(), is( systemUnderTest.rightConfigPanel() ) );
   }//End Method
   
   @Test public void shouldDisplayPanelsInCorrectOrder() {
      ScrollPane leftScroller = systemUnderTest.leftScroller();
      assertThat( GridPane.getColumnIndex( leftScroller ), is( 0 ) );
      assertThat( GridPane.getRowIndex( leftScroller ), is( 0 ) );
      
      ImageFlasherConfigurationPanel imageConfig = systemUnderTest.imageConfigPanel();
      assertThat( GridPane.getColumnIndex( imageConfig ), is( 1 ) );
      assertThat( GridPane.getRowIndex( imageConfig ), is( 0 ) );
      
      ScrollPane rightScroller = systemUnderTest.rightScroller();
      assertThat( GridPane.getColumnIndex( rightScroller ), is( 2 ) );
      assertThat( GridPane.getRowIndex( rightScroller ), is( 0 ) );
   }//End Method
}//End Class
