/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.styling;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javafx.scene.Parent;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.utility.TestCommon;
import uk.dangrew.jtt.utility.javafx.TestableParent;

/**
 * {@link SystemStyles} test.
 */
public class SystemStylesTest {
   
   private SystemStyles systemUnderTest;
   
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
