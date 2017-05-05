/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.dual;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.buildwall.effects.flasher.ImageFlasherProperties;
import uk.dangrew.jtt.buildwall.effects.flasher.ImageFlasherPropertiesImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;

/**
 * {@link DualBuildWallConfigurer} test.
 */
public class DualBuildWallConfigurerTest {
   
   private BorderPane display;
   private ImageFlasherProperties imageFlasherProperties;
   private DualBuildWallConfigurer systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      JavaFxInitializer.startPlatform();
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      display = new BorderPane();
      imageFlasherProperties = new ImageFlasherPropertiesImpl();
      systemUnderTest = new DualBuildWallConfigurer( display, imageFlasherProperties );
   }//End Method

   @Test public void displayIntiallyShouldHaveNoConfiguration() {
      assertThat( systemUnderTest.isConfigurationShowing(), is( false ) );
      assertThat( display.getRight(), is( nullValue() ) );
   }//End Method
   
   @Test public void showFlasherConfigShouldShowPanelOnRight(){
      assertThat( systemUnderTest.isConfigurationShowing(), is( false ) );
      assertThat( display.getRight(), is( nullValue() ) );
      systemUnderTest.showImageFlasherConfiguration();
      assertThat( systemUnderTest.isConfigurationShowing(), is( true ) );
      assertThat( display.getRight(), is( systemUnderTest.scroller() ) );
      assertThat( systemUnderTest.scroller().getContent(), is( systemUnderTest.imageFlasherConfigurationPanel() ) );
   }//End Method
   
   @Test public void hideConfigShouldHidePanelOnRight(){
      showFlasherConfigShouldShowPanelOnRight();
      assertThat( systemUnderTest.isConfigurationShowing(), is( true ) );
      systemUnderTest.hideConfiguration();
      assertThat( systemUnderTest.isConfigurationShowing(), is ( false ) );
      assertThat( display.getRight(), is( nullValue() ) );
   }//End Method

}//End Class
