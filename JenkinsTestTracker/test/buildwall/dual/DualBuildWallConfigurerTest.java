/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.dual;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import buildwall.configuration.BuildWallConfiguration;
import buildwall.configuration.BuildWallConfigurationImpl;
import graphics.JavaFxInitializer;
import javafx.scene.layout.BorderPane;

/**
 * {@link DualBuildWallConfigurer} test.
 */
public class DualBuildWallConfigurerTest {
   
   private BorderPane display;
   private BuildWallConfiguration leftConfiguration;
   private BuildWallConfiguration rightConfiguration;
   private DualBuildWallConfigurer systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      JavaFxInitializer.startPlatform();
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      display = new BorderPane();
      leftConfiguration = new BuildWallConfigurationImpl();
      rightConfiguration = new BuildWallConfigurationImpl();
      systemUnderTest = new DualBuildWallConfigurer( display, leftConfiguration, rightConfiguration );
   }//End Method

   @Test public void displayIntiallyShouldHaveNoConfiguration() {
      assertThat( systemUnderTest.isConfigurationShowing(), is( false ) );
      assertThat( display.getRight(), is( nullValue() ) );
   }//End Method
   
   @Test public void showRightShouldAddConfigToRight() {
      systemUnderTest.showRightConfiguration();
      assertThat( systemUnderTest.isConfigurationShowing(), is( true ) );
      assertThat( display.getRight(), is( notNullValue() ) );
      assertThat( display.getRight(), is( systemUnderTest.scroller() ) );
      assertThat( systemUnderTest.scroller().getContent(), is( systemUnderTest.rightConfigurationPanel() ) );
   }//End Method
   
   @Test public void showLeftShouldAddConfigToRight() {
      systemUnderTest.showLeftConfiguration();
      assertThat( systemUnderTest.isConfigurationShowing(), is( true ) );
      assertThat( display.getRight(), is( notNullValue() ) );
      assertThat( display.getRight(), is( systemUnderTest.scroller() ) );
      assertThat( systemUnderTest.scroller().getContent(), is( systemUnderTest.leftConfigurationPanel() ) );
   }//End Method
   
   @Test public void showRightWhenLeftShownShouldReplace() {
      showLeftShouldAddConfigToRight();
      showRightShouldAddConfigToRight();
   }//End Method
   
   @Test public void showLeftWhenRightShownShouldReplace() {
      showRightShouldAddConfigToRight();
      showLeftShouldAddConfigToRight();
   }//End Method
   
   @Test public void hideShouldHideRight() {
      showRightShouldAddConfigToRight();
      assertThat( systemUnderTest.isConfigurationShowing(), is( true ) );
      systemUnderTest.hideConfiguration();
      assertThat( systemUnderTest.isConfigurationShowing(), is( false ) );
   }//End Method
   
   @Test public void hideShouldHideLeft() {
      showLeftShouldAddConfigToRight();
      assertThat( systemUnderTest.isConfigurationShowing(), is( true ) );
      systemUnderTest.hideConfiguration();
      assertThat( systemUnderTest.isConfigurationShowing(), is( false ) );
   }//End Method
   
   @Test public void hideRightWallWhenLeftShownShouldDoNothing() {
      showLeftShouldAddConfigToRight();
      systemUnderTest.hideRightWall();
      assertThat( systemUnderTest.isConfigurationShowing(), is( true ) );
   }//End Method
   
   @Test public void hideLeftWallWhenRightShownShouldDoNothing() {
      showRightShouldAddConfigToRight();
      systemUnderTest.hideLeftWall();
      assertThat( systemUnderTest.isConfigurationShowing(), is( true ) );
   }//End Method
   
   @Test public void hideRightWallWhenRightShownShouldHideConfiguration() {
      showRightShouldAddConfigToRight();
      systemUnderTest.hideRightWall();
      assertThat( systemUnderTest.isConfigurationShowing(), is( false ) );
   }//End Method
   
   @Test public void hideLeftWallWhenLeftShownShouldHideConfiguration() {
      showLeftShouldAddConfigToRight();
      systemUnderTest.hideLeftWall();
      assertThat( systemUnderTest.isConfigurationShowing(), is( false ) );
   }//End Method

}//End Class
