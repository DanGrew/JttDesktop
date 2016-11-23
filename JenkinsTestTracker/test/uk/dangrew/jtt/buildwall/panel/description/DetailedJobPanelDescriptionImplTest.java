/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.panel.description;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;
import uk.dangrew.jtt.utility.TestCommon;

/**
 * {@link DetailedJobPanelDescriptionImpl} test.
 */
public class DetailedJobPanelDescriptionImplTest extends JobPanelDescriptionBaseImplTest {

   @Before @Override public void initialiseSystemUnderTest(){
      super.initialiseSystemUnderTest();
      
      JenkinsUser rick = new JenkinsUserImpl( "Rick" );
      JenkinsUser daryl = new JenkinsUserImpl( "Daryl" );
      JenkinsUser carl = new JenkinsUserImpl( "Carl" );
      JenkinsUser michonne = new JenkinsUserImpl( "Michonne" );
      JenkinsUser abraham = new JenkinsUserImpl( "Abraham" );
      JenkinsUser maggie = new JenkinsUserImpl( "Maggie" );
      JenkinsUser glenn = new JenkinsUserImpl( "Glenn" );
      JenkinsUser eugene = new JenkinsUserImpl( "Eugene" );
      JenkinsUser jesus = new JenkinsUserImpl( "Jesus" );
      JenkinsUser carol = new JenkinsUserImpl( "Carol" );

      job.culprits().addAll( rick, daryl, carl, michonne, abraham, maggie, glenn, eugene, jesus, carol );
      systemUnderTest = new DetailedJobPanelDescriptionImpl( configuration, job );
   }//End Method
   
   @Ignore //For manual inspection.
   @Test public void manualInspection() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> { 
         configuration.buildNumberColour().set( Color.BLACK );
         configuration.jobNameColour().set( Color.BLACK );
         configuration.completionEstimateColour().set( Color.BLACK );
         configuration.detailColour().set( Color.BLACK );
         return new DetailedJobPanelDescriptionImpl( configuration, job ); 
      } );
      Thread.sleep( 100000 );
   }//End Method
   
   @Test public void propertiesGridShouldBeBottom(){
      assertThat( systemUnderTest.getBottom(), is( systemUnderTest.propertiesPane() ) );
   }//End Method
   
   @Test public void propertiesShouldBeEvenlySplit(){
      ObservableList< ColumnConstraints > constraints = systemUnderTest.propertiesPane().getColumnConstraints();
      Assert.assertEquals( 2, constraints.size() );
      Assert.assertEquals( DetailedJobPanelDescriptionImpl.BUILD_PROPERTY_PERCENTAGE, constraints.get( 0 ).getPercentWidth(), TestCommon.precision() );
      Assert.assertEquals( DetailedJobPanelDescriptionImpl.COMPLETION_ESTIMATE_PERCENTAGE, constraints.get( 1 ).getPercentWidth(), TestCommon.precision() );
   }//End Method

   @Test public void buildNumberShouldBeBottomLeft(){
      Assert.assertEquals( 0, GridPane.getRowIndex( systemUnderTest.buildNumber() ).intValue() );
      Assert.assertEquals( 0, GridPane.getColumnIndex( systemUnderTest.buildNumber() ).intValue() );
      
      ObservableList< ColumnConstraints > constraints = systemUnderTest.propertiesPane().getColumnConstraints();
      Assert.assertEquals( HPos.LEFT, constraints.get( 0 ).getHalignment() );
   }//End Method
   
   @Test public void jobNameShouldBeAtTopInCenter(){
      assertThat( systemUnderTest.getTop(), is( systemUnderTest.jobName() ) );
      assertThat( systemUnderTest.jobName().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.jobName().getAlignment(), is( Pos.CENTER ) );
   }//End Method
   
   @Test public void completionEstimateShouldBeBottomRight(){
      Assert.assertEquals( 0, GridPane.getRowIndex( systemUnderTest.completionEstimate() ).intValue() );
      Assert.assertEquals( 1, GridPane.getColumnIndex( systemUnderTest.completionEstimate() ).intValue() );
      
      ObservableList< ColumnConstraints > constraints = systemUnderTest.propertiesPane().getColumnConstraints();
      Assert.assertEquals( HPos.RIGHT, constraints.get( 1 ).getHalignment() );
   }//End Method
   
   @Test public void failureDetailShouldBeInCenter(){
      assertThat( systemUnderTest.getCenter(), instanceOf( FailureDetail.class ) );
   }//End Method
   
}//End Class
