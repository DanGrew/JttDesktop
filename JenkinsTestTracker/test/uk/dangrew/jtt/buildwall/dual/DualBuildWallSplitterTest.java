/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.dual;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.scene.control.SplitPane;
import uk.dangrew.jtt.buildwall.configuration.BuildWallConfigurationImpl;
import uk.dangrew.jtt.buildwall.dual.DualBuildWallSplitter;
import uk.dangrew.jtt.buildwall.layout.GridWallImpl;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.PlatformDecouplerImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;

/**
 * {@link DualBuildWallSplitter} test.
 */
public class DualBuildWallSplitterTest {

   private GridWallImpl right;
   private GridWallImpl left;
   private DualBuildWallSplitter systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      JavaFxInitializer.startPlatform();
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      JenkinsDatabase database = new JenkinsDatabaseImpl();
      right = new GridWallImpl( new BuildWallConfigurationImpl(), database );
      left = new GridWallImpl( new BuildWallConfigurationImpl(), database );
      systemUnderTest = new DualBuildWallSplitter( left, right );
   }//End Method
   
   /**
    * Method to assert that the given walls are present.
    * @param expectedWalls the {@link GridWallImpl}s expected, in the expected order.
    */
   private void assertThatWallsArePresent( GridWallImpl... expectedWalls ) {
      assertThat( systemUnderTest.getItems(), hasSize( expectedWalls.length ) );
      for ( int i = 0; i < expectedWalls.length; i++ ) {
         assertThat( systemUnderTest.getItems().get( i ), is( expectedWalls[ i ] ) );
      }
   }//End Method
   
   @Test public void shouldInitiallyHaveGridsInItems(){
      assertThatWallsArePresent( left, right );
   }//End Method
   
   @Test public void shouldHideAndShowRightWall() {
      systemUnderTest.hideRightWall();
      assertThatWallsArePresent( left );
      
      systemUnderTest.showRightWall();
      assertThatWallsArePresent( left, right );
   }//End Method
   
   @Test public void shouldHideAndShowLeftWall() {
      systemUnderTest.hideLeftWall();
      assertThatWallsArePresent( right );
      
      systemUnderTest.showLeftWall();
      assertThatWallsArePresent( left, right );
   }//End Method
   
   @Test public void multipleShowRightShouldNotHaveAnyEffect() {
      systemUnderTest.showRightWall();
      systemUnderTest.showRightWall();
      systemUnderTest.showRightWall();
      systemUnderTest.showRightWall();
      
      assertThatWallsArePresent( left, right );
   }//End Method
   
   @Test public void multipleShowLeftShouldNotHaveAnyEffect() {
      systemUnderTest.showLeftWall();
      systemUnderTest.showLeftWall();
      systemUnderTest.showLeftWall();
      systemUnderTest.showLeftWall();
      
      assertThatWallsArePresent( left, right );
   }//End Method
   
   @Test public void multipleHideRightShouldNotHaveAnyEffect() {
      systemUnderTest.hideRightWall();
      systemUnderTest.hideRightWall();
      systemUnderTest.hideRightWall();
      systemUnderTest.hideRightWall();
      
      assertThatWallsArePresent( left );
   }//End Method
   
   @Test public void multipleHideLeftShouldNotHaveAnyEffect() {
      systemUnderTest.hideLeftWall();
      systemUnderTest.hideLeftWall();
      systemUnderTest.hideLeftWall();
      systemUnderTest.hideLeftWall();
      
      assertThatWallsArePresent( right );
   }//End Method

   /**
    * Method to assert that the {@link DualBuildWallSplitter} has dividers in the 
    * given positions.
    */
   private void assertDividerPosition( double... expected ) {
      assertThat( systemUnderTest.getDividerPositions(), is( expected ) );
   }//End Method
   
   @Test public void shouldRetainSplitPositionWhenRightIsRemoved(){
      final double dividerPosition = 0.7;
      systemUnderTest.setDividerPositions( dividerPosition );
      assertDividerPosition( dividerPosition );

      systemUnderTest.hideRightWall();
      assertDividerPosition();
      
      systemUnderTest.showRightWall();
      assertDividerPosition( dividerPosition );
   }//End Method
   
   @Test public void shouldRetainSplitPositionWhenLeftIsRemoved(){
      final double dividerPosition = 0.7;
      systemUnderTest.setDividerPositions( dividerPosition );
      assertDividerPosition( dividerPosition );

      systemUnderTest.hideLeftWall();
      assertDividerPosition();
      
      systemUnderTest.showLeftWall();
      assertDividerPosition( dividerPosition );
   }//End Method
   
   @Test public void shouldRetainSplitPositionWhenRightThenLeftRemoved(){
      final double dividerPosition = 0.7;
      systemUnderTest.setDividerPositions( dividerPosition );
      assertDividerPosition( dividerPosition );

      systemUnderTest.hideRightWall();
      systemUnderTest.hideLeftWall();
      assertDividerPosition();
      
      systemUnderTest.showRightWall();
      systemUnderTest.showLeftWall();
      assertDividerPosition( dividerPosition );
   }//End Method
   
   @Test public void shouldRetainSplitPositionWhenLeftThenRightRemoved(){
      final double dividerPosition = 0.7;
      systemUnderTest.setDividerPositions( dividerPosition );
      assertDividerPosition( dividerPosition );

      systemUnderTest.hideLeftWall();
      systemUnderTest.hideRightWall();
      assertDividerPosition();
      
      systemUnderTest.showLeftWall();
      systemUnderTest.showRightWall();
      assertDividerPosition( dividerPosition );
   }//End Method
   
   @Test public void shouldRetainDefaultDividerPositionWhenNotChanged(){
      systemUnderTest.hideRightWall();
      assertDividerPosition();
      
      systemUnderTest.showRightWall();
      assertDividerPosition( DualBuildWallSplitter.DEFAULT_DIVIDER );
   }//End Method
   
   @Test public void shouldNotAllowWallsToResizeWithSplitPaneToRetainDividerPosition(){
      assertThat( SplitPane.isResizableWithParent( systemUnderTest.leftGridWall() ), is( false ) );
      assertThat( SplitPane.isResizableWithParent( systemUnderTest.rightGridWall() ), is( false ) );
   }//End Method
   
}//End Class
