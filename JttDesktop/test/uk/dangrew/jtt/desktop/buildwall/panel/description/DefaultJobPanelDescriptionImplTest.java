/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.panel.description;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.model.utility.TestCommon;
import uk.dangrew.sd.graphics.launch.TestApplication;

/**
 * {@link DefaultJobPanelDescriptionImpl} test.
 */
public class DefaultJobPanelDescriptionImplTest extends JobPanelDescriptionBaseImplTest {

   @Override protected JobPanelDescriptionBaseImpl constructSut() {
      return new DefaultJobPanelDescriptionImpl( configuration, theme, job );
   }//End Method
   
   @Ignore //For manual inspection.
   @Test public void manualInspection() throws InterruptedException {
      TestApplication.launch( () -> { return new DefaultJobPanelDescriptionImpl( configuration, theme, job ); } );
      Thread.sleep( 100000 );
   }//End Method
   
   @Test public void propertiesShouldBeEvenlySplit(){
      GridPane properties = systemUnderTest.propertiesPane();
      ObservableList< ColumnConstraints > constraints = properties.getColumnConstraints();
      Assert.assertEquals( 2, constraints.size() );
      Assert.assertEquals( DefaultJobPanelDescriptionImpl.BUILD_PROPERTY_PERCENTAGE, constraints.get( 0 ).getPercentWidth(), TestCommon.precision() );
      Assert.assertEquals( DefaultJobPanelDescriptionImpl.COMPLETION_ESTIMATE_PERCENTAGE, constraints.get( 1 ).getPercentWidth(), TestCommon.precision() );
   }//End Method

   @Test public void jobNameShouldBeInCenter(){
      Assert.assertEquals( systemUnderTest.jobName(), systemUnderTest.getCenter() );
   }//End Method
   
   @Test public void buildNumberShouldBeBottomLeft(){
      Assert.assertEquals( 0, GridPane.getRowIndex( systemUnderTest.buildNumber() ).intValue() );
      Assert.assertEquals( 0, GridPane.getColumnIndex( systemUnderTest.buildNumber() ).intValue() );
      
      ObservableList< ColumnConstraints > constraints = systemUnderTest.propertiesPane().getColumnConstraints();
      Assert.assertEquals( HPos.LEFT, constraints.get( 0 ).getHalignment() );
   }//End Method
   
   @Test public void completionEstimateShouldBeBottomRight(){
      Assert.assertEquals( 0, GridPane.getRowIndex( systemUnderTest.completionEstimate() ).intValue() );
      Assert.assertEquals( 1, GridPane.getColumnIndex( systemUnderTest.completionEstimate() ).intValue() );
      
      ObservableList< ColumnConstraints > constraints = systemUnderTest.propertiesPane().getColumnConstraints();
      Assert.assertEquals( HPos.RIGHT, constraints.get( 1 ).getHalignment() );
   }//End Method
   
}//End Class
