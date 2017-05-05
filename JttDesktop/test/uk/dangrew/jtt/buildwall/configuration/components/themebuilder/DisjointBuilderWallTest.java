/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.components.themebuilder;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static uk.dangrew.jtt.buildwall.configuration.components.themebuilder.DisjointBuilderWall.COLUMN_WIDTH;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.scene.layout.GridPane;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.buildwall.configuration.theme.BuildWallThemeImpl;
import uk.dangrew.jtt.buildwall.panel.JobPanelImpl;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.styling.SystemStyling;

@RunWith( JUnitParamsRunner.class )
public class DisjointBuilderWallTest {

   @Spy private JavaFxStyle styling;
   private BuildWallConfiguration configuration; 
   private BuildWallTheme theme;
   private DisjointBuilderWall systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      JavaFxInitializer.startPlatform();
      SystemStyling.initialise();
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      MockitoAnnotations.initMocks( this );
      
      configuration = new BuildWallConfigurationImpl();
      theme = new BuildWallThemeImpl( "Test" );
      systemUnderTest = new DisjointBuilderWall( styling, configuration, theme );
   }//End Method
   
   @Test public void shouldReduceTextSizeForLayout() {
      assertThat( configuration.buildNumberFont().get().getSize(), is( DisjointBuilderWall.REDUCED_TEXT_SIZE ) );
      assertThat( configuration.completionEstimateFont().get().getSize(), is( DisjointBuilderWall.REDUCED_TEXT_SIZE ) );
   }//End Method

   @Test public void shouldConfigureColumnConstraints() {
      verify( styling ).configureConstraintsForColumnPercentages( 
               systemUnderTest, COLUMN_WIDTH, COLUMN_WIDTH, COLUMN_WIDTH 
      );
   }//End Method
   
   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldProvideJobPanelPerStatus( BuildResultStatus status ) {
      assertThat( systemUnderTest.panelFor( status ), is( notNullValue() ) );
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.panelFor( status ) ), is( true ) );
   }//End Method
   
   @Test public void shouldArrangePanelsInColumns() {
      for ( BuildResultStatus status : BuildResultStatus.values() ) {
         JobPanelImpl panel = systemUnderTest.panelFor( status );
         assertThat( GridPane.getRowIndex( panel ), is( status.ordinal() / DisjointBuilderWall.COLUMN_COUNT ) );
         assertThat( GridPane.getColumnIndex( panel ), is( status.ordinal() % DisjointBuilderWall.COLUMN_COUNT ) );
      }
   }//End Method
   
   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldSetStatusAndBuildTimesOnJobs( BuildResultStatus status ) {
      JobPanelImpl panel = systemUnderTest.panelFor( status );
      assertThat( panel.getJenkinsJob().getBuildStatus(), is( status ) );
      assertThat( panel.getJenkinsJob().buildTimestampProperty().get(), is( DisjointBuilderWall.JOB_TIMESTAMP ) );
      assertThat( panel.getJenkinsJob().currentBuildTimeProperty().get(), is( DisjointBuilderWall.JOB_PROGRESS ) );
      assertThat( panel.getJenkinsJob().expectedBuildTimeProperty().get(), is( DisjointBuilderWall.JOB_EXPECTED_BUILD_TIME ) );
   }//End Method
   
   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldAssociatePanelsWithTheme( BuildResultStatus status ) {
      assertThat( systemUnderTest.panelFor( status ).isAssociatedWith( theme ), is( true ) );
   }//End Method
   
   @Test public void shouldBeAssociatedWithTheme(){
      assertThat( systemUnderTest.isAssociatedWith( theme ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( new BuildWallThemeImpl( "anything" ) ), is( false ) );
   }//End Method

}//End Class
