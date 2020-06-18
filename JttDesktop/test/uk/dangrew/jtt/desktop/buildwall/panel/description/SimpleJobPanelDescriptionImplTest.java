/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.panel.description;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import uk.dangrew.kode.TestCommon;
import uk.dangrew.kode.launch.TestApplication;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * {@link SimpleJobPanelDescriptionImpl} test.
 */
public class SimpleJobPanelDescriptionImplTest extends JobPanelDescriptionBaseImplTest {

   @Override protected JobPanelDescriptionBaseImpl constructSut() {
      return new SimpleJobPanelDescriptionImpl( configuration, theme, job );
   }//End Method
   
   @Ignore //For manual inspection.
   @Test public void manualInspection() throws InterruptedException {
      TestApplication.launch( () -> {
         configuration.buildNumberColour().set( Color.BLACK );
         configuration.jobNameColour().set( Color.BLACK );
         configuration.completionEstimateColour().set( Color.BLACK );
         return new SimpleJobPanelDescriptionImpl( configuration, theme, job ); 
      } );
      Thread.sleep( 100000 );
   }//End Method
   
   @Test public void propertiesGridShouldBeCenter(){
      assertThat( systemUnderTest.getCenter(), is( systemUnderTest.propertiesPane() ) );
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
   
   @Test public void propertiesShouldBeCentred(){
      assertThat( systemUnderTest.propertiesPane().getAlignment(), is( Pos.CENTER ) );
   }//End Method
   
}//End Class
