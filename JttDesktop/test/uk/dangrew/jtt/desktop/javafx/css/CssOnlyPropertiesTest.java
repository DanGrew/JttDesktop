/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.css;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class CssOnlyPropertiesTest {

   private CssOnlyProperties systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      systemUnderTest = new CssOnlyProperties();
   }//End Method

   @Test public void shouldProvideBackgroundProperty() {
      assertThat( systemUnderTest.backgroundProperty(), is( CssOnlyProperties.BACKGROUND_COLOUR_FX_PROPERTY ) );
   }//End Method
   
   @Test public void shouldProvideCssSuffix() {
      assertThat( systemUnderTest.propertySuffix(), is( CssOnlyProperties.FX_PROPERTY_SUFFIX ) );
   }//End Method
   
   @Test public void shouldProvideTransparentKeyword() {
      assertThat( systemUnderTest.transparent(), is( CssOnlyProperties.TRANSPARENT ) );
   }//End Method

}//End Class
