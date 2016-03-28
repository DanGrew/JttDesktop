/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration.style;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import graphics.JavaFxInitializer;
import javafx.scene.control.Label;
import javafx.scene.text.FontWeight;
import utility.TestCommon;

/**
 * {@link BuildWallConfigurationStyle} test.
 */
public class BuildWallConfigurationStyleTest {

   private BuildWallConfigurationStyle systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      JavaFxInitializer.startPlatform();
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new BuildWallConfigurationStyle();
   }//End Method
   
   @Test public void shouldProvideLabelWithBoldFont() {
      final String text = "anything";
      Label label = systemUnderTest.createBoldLabel( text );
      
      assertThat( label.getText(), is( text ) );
      assertThat( FontWeight.findByName( label.getFont().getStyle() ), is( FontWeight.BOLD ) );
   }//End Method
   
   @Test public void shouldProvideLabelWithBoldFontAndSize() {
      final String text = "anything";
      final double size = 34;
      Label label = systemUnderTest.createBoldLabel( text, size );
      
      assertThat( label.getText(), is( text ) );
      assertThat( FontWeight.findByName( label.getFont().getStyle() ), is( FontWeight.BOLD ) );
      assertThat( label.getFont().getSize(), closeTo( size, TestCommon.precision() ) );
   }//End Method

}//End Class
