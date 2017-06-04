/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class ContentAreaColoursTest {

   private ContentArea area;
   private ContentAreaColours systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      area = new ContentArea( 
               100, 100, 
               0, 0,  
               100, 100 
      );
      systemUnderTest = new ContentAreaColours();
   }//End Method

   @Test public void shouldApplySelectedColours() {
      systemUnderTest.applySelectedColours( area );
      assertThat( area.getFill(), is( ContentAreaColours.SELECTED_BACKGROUND_COLOUR ) );
      assertThat( area.getStroke(), is( ContentAreaColours.SELECTED_BORDER_COLOUR ) );
      assertThat( area.getStrokeWidth(), is( ContentAreaColours.STROKE_THICKNESS ) );
   }//End Method
   
   @Test public void shouldApplyUnselectedColours() {
      systemUnderTest.applyUnselectedColours( area );
      assertThat( area.getFill(), is( ContentAreaColours.UNSELECTED_BACKGROUND_COLOUR ) );
      assertThat( area.getStroke(), is( ContentAreaColours.UNSELECTED_BORDER_COLOUR ) );
      assertThat( area.getStrokeWidth(), is( ContentAreaColours.STROKE_THICKNESS ) );
   }//End Method
   
}//End Class
