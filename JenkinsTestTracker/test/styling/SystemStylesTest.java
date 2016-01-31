/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package styling;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javafx.scene.Parent;
import model.jobs.BuildResultStatus;
import utility.TestCommon;

/**
 * {@link SystemStyles} test.
 */
public class SystemStylesTest {
   
   private SystemStyles systemUnderTest;
   
   /** Simple extension for {@link Parent} because mocking isn't enough. **/
   private class TestableParent extends Parent {}
   
   @Before public void initialieSystemUnderTest(){
      systemUnderTest = new SystemStyles();
   }//End Method

   @Test public void shouldFindBuildWallProgressBarStyleSheets() {
      for ( BuildWallStyles style : BuildWallStyles.values() ) {
         Assert.assertTrue( systemUnderTest.hasSheet( style ) );
         Assert.assertTrue( systemUnderTest.hasLabel( style ) );
      }
   }//End Method
   
   @Test public void shouldHaveStylesPresentInSheet(){
      for ( BuildWallStyles style : BuildWallStyles.values() ) {
            Assert.assertTrue( 
                  TestCommon.readFileIntoString( getClass(), systemUnderTest.getSheet( style ) )
                     .contains( systemUnderTest.getLabel( style ) ) 
            );
      }
   }//End Method
   
   @Test public void shouldApplyStyleToParent(){
      TestableParent parent = new TestableParent();
      systemUnderTest.applyStyle( BuildWallStyles.ProgressBarSuccess, parent );
      Assert.assertTrue( parent.getStylesheets().contains( systemUnderTest.getSheet( BuildWallStyles.ProgressBarSuccess ) ) );
      Assert.assertTrue( parent.getStyleClass().contains( systemUnderTest.getLabel( BuildWallStyles.ProgressBarSuccess ) ) );
   }//End Method
   
   @Test public void shouldSwapStylesInParent(){
      TestableParent parent = new TestableParent();
      systemUnderTest.applyStyle( BuildWallStyles.ProgressBarSuccess, parent );
      Assert.assertEquals( 1, parent.getStylesheets().size() );
      Assert.assertTrue( parent.getStylesheets().contains( systemUnderTest.getSheet( BuildWallStyles.ProgressBarSuccess ) ) );
      Assert.assertTrue( parent.getStyleClass().contains( systemUnderTest.getLabel( BuildWallStyles.ProgressBarSuccess ) ) );
      
      systemUnderTest.applyStyle( BuildWallStyles.ProgressBarFailed, parent );
      Assert.assertEquals( 1, parent.getStylesheets().size() );
      Assert.assertTrue( parent.getStylesheets().contains( systemUnderTest.getSheet( BuildWallStyles.ProgressBarFailed ) ) );
      Assert.assertTrue( parent.getStyleClass().contains( systemUnderTest.getLabel( BuildWallStyles.ProgressBarFailed ) ) );
   }//End Method
   
   @Test public void shouldIgnoreMissingSheetAndStyle(){
      Parent parent = Mockito.mock( Parent.class );
      systemUnderTest.applyStyle( BuildResultStatus.ABORTED, parent );
      Mockito.verifyZeroInteractions( parent );
   }//End Method

}//End Class
