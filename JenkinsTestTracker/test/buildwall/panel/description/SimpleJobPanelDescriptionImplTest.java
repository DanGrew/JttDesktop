/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.panel.description;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import buildwall.configuration.BuildWallConfiguration;
import buildwall.configuration.BuildWallConfigurationImpl;
import graphics.DecoupledPlatformImpl;
import graphics.JavaFxInitializer;
import graphics.TestPlatformDecouplerImpl;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;
import system.properties.DateAndTimes;
import utility.TestCommon;

/**
 * {@link SimpleJobPanelDescriptionImpl} test.
 */
public class SimpleJobPanelDescriptionImplTest extends JobPanelDescriptionBaseImplTest {

   @Before @Override public void initialiseSystemUnderTest(){
      super.initialiseSystemUnderTest();
      systemUnderTest = new SimpleJobPanelDescriptionImpl( configuration, job );
   }//End Method
   
   @Ignore //For manual inspection.
   @Test @Override public void manualInspection() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> { 
         configuration.buildNumberColour().set( Color.BLACK );
         configuration.jobNameColour().set( Color.BLACK );
         configuration.completionEstimateColour().set( Color.BLACK );
         return new SimpleJobPanelDescriptionImpl( configuration, job ); 
      } );
      Thread.sleep( 100000 );
   }//End Method
   
   @Test public void propertiesShouldBeEvenlySplit(){
      ObservableList< ColumnConstraints > constraints = systemUnderTest.propertiesPane().getColumnConstraints();
      Assert.assertEquals( 3, constraints.size() );
      Assert.assertEquals( SimpleJobPanelDescriptionImpl.BUILD_PROPERTY_PERCENTAGE, constraints.get( 0 ).getPercentWidth(), TestCommon.precision() );
      Assert.assertEquals( SimpleJobPanelDescriptionImpl.JOB_NAME_PERCENTAGE, constraints.get( 1 ).getPercentWidth(), TestCommon.precision() );
      Assert.assertEquals( SimpleJobPanelDescriptionImpl.COMPLETION_ESTIMATE_PERCENTAGE, constraints.get( 2 ).getPercentWidth(), TestCommon.precision() );
   }//End Method

   @Test public void buildNumberShouldBeBottomLeft(){
      Assert.assertEquals( 0, GridPane.getRowIndex( systemUnderTest.buildNumber() ).intValue() );
      Assert.assertEquals( 0, GridPane.getColumnIndex( systemUnderTest.buildNumber() ).intValue() );
      
      ObservableList< ColumnConstraints > constraints = systemUnderTest.propertiesPane().getColumnConstraints();
      Assert.assertEquals( HPos.LEFT, constraints.get( 0 ).getHalignment() );
   }//End Method
   
   @Test public void jobNameShouldBeInCenter(){
      assertThat( GridPane.getRowIndex( systemUnderTest.jobName() ), is( 0 ) );
      assertThat( GridPane.getColumnIndex( systemUnderTest.jobName() ), is( 1 ) );
      
      ObservableList< ColumnConstraints > constraints = systemUnderTest.propertiesPane().getColumnConstraints();
      Assert.assertEquals( HPos.CENTER, constraints.get( 1 ).getHalignment() );
   }//End Method
   
   @Test public void completionEstimateShouldBeBottomRight(){
      Assert.assertEquals( 0, GridPane.getRowIndex( systemUnderTest.completionEstimate() ).intValue() );
      Assert.assertEquals( 2, GridPane.getColumnIndex( systemUnderTest.completionEstimate() ).intValue() );
      
      ObservableList< ColumnConstraints > constraints = systemUnderTest.propertiesPane().getColumnConstraints();
      Assert.assertEquals( HPos.RIGHT, constraints.get( 2 ).getHalignment() );
   }//End Method
   
}//End Class
