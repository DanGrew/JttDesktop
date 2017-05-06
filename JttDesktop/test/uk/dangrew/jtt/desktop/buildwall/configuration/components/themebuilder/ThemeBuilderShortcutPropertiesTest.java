/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components.themebuilder;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.desktop.buildwall.configuration.components.themebuilder.ThemeBuilderShortcutProperties;

public class ThemeBuilderShortcutPropertiesTest {

   private ThemeBuilderShortcutProperties systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      systemUnderTest = new ThemeBuilderShortcutProperties();
   }//End Method

   @Test public void shouldProvideShortcutColorProperty() {
      assertThat( systemUnderTest.shortcutColorProperty(), is( notNullValue() ) );
      assertThat( systemUnderTest.shortcutColorProperty(), is( systemUnderTest.shortcutColorProperty() ) );
   }//End Method

}//End Class
