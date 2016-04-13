/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.dual;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * {@link DualBuildWallAutoHider} test.
 */
public class DualBuildWallAutoHiderTest {
   
   private BooleanProperty rightEmpty;
   private BooleanProperty leftEmpty;
   @Mock private DualBuildWallDisplayImpl display;
   private DualBuildWallAutoHider systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      rightEmpty = new SimpleBooleanProperty( false );
      leftEmpty = new SimpleBooleanProperty( false );
      systemUnderTest = new DualBuildWallAutoHider( display, leftEmpty, rightEmpty );
   }//End Method

   @Test public void leftEmptyShouldHideLeftWall() {
      leftEmpty.set( true );
      verify( display ).hideLeftWall();
   }//End Method
   
   @Test public void leftPopulatedShouldShowLeftWall() {
      leftEmptyShouldHideLeftWall();
      
      leftEmpty.set( false );
      verify( display, times( 2 ) ).showLeftWall();
   }//End Method
   
   @Test public void rightEmptyShouldHideRightWall() {
      rightEmpty.set( true );
      verify( display ).hideRightWall();
   }//End Method
   
   @Test public void rightPopulatedShouldShowRightWall() {
      rightEmptyShouldHideRightWall();
      
      rightEmpty.set( false );
      verify( display, times( 2 ) ).showRightWall();
   }//End Method
   
   @Test public void shouldHideIfInitiallyEmpty(){
      rightEmpty = new SimpleBooleanProperty( true );
      leftEmpty = new SimpleBooleanProperty( true );
      systemUnderTest = new DualBuildWallAutoHider( display, leftEmpty, rightEmpty );
      
      verify( display ).hideRightWall();
      verify( display ).hideLeftWall();
   }//End Method
   
}//End Class
