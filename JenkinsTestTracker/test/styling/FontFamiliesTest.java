/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package styling;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import javafx.scene.text.Font;

/**
 * {@link FontFamilies} test.
 */
public class FontFamiliesTest {

   @Test public void shouldProvideUsableFonts() {
      List< String > families = FontFamilies.getUsableFontFamilies();
      
      assertThat( families.isEmpty(), is( false ) );
      families.forEach( family -> assertThat( Font.font( family ).getFamily(), not( containsString( FontFamilies.SYSTEM_FONT_INDICATOR ) ) ) );
   }//End Method
   
   @Test public void shouldIdentifyUnusableFonts() {
      List< String > families = FontFamilies.getUnusableFontFamilies();
      
      assertThat( families.isEmpty(), is( false ) );
      families.forEach( family -> assertThat( Font.font( family ).getFamily(), containsString( FontFamilies.SYSTEM_FONT_INDICATOR ) ) );
   }//End Method

}//End Class
