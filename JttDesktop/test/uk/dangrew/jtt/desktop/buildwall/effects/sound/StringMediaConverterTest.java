/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.effects.sound;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.desktop.friendly.javafx.FriendlyMediaPlayer;
import uk.dangrew.sd.graphics.launch.TestApplication;

public class StringMediaConverterTest {
   
   private StringMediaConverter systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      TestApplication.startPlatform();
      systemUnderTest = new StringMediaConverter();
   }//End Method

   @Test public void shouldHandleNullValue() {
      assertThat( systemUnderTest.convert( null ), is( nullValue() ) );
      assertThat( systemUnderTest.isValidMedia( null ), is( false ) );
   }//End Method
   
   @Test public void shouldHandleEmptyString() {
      assertThat( systemUnderTest.convert( "    " ), is( nullValue() ) );
      assertThat( systemUnderTest.isValidMedia( "    " ), is( false ) );
   }//End Method

   @Test public void shouldHandleMissingMediaFile() {
      assertThat( systemUnderTest.convert( "missing-media-file.txt" ), is( nullValue() ) );
      assertThat( systemUnderTest.isValidMedia( "missing-media-file.txt" ), is( false ) );
   }//End Method
   
   @Test public void shouldHandleInvalidMediaFile() {
      String fileName = getClass().getResource( "invalid-media-file.txt" ).getPath();
      assertThat( systemUnderTest.convert( fileName ), is( nullValue() ) );
      assertThat( systemUnderTest.isValidMedia( fileName ), is( false ) );
   }//End Method
   
   @Test public void shouldHandleValidMediaFile() {
      String fileName = getClass().getResource( "valid-media-file.m4a" ).getPath();
      assertThat( systemUnderTest.convert( fileName ), is( notNullValue() ) );
      assertThat( systemUnderTest.convert( fileName ), is( instanceOf( FriendlyMediaPlayer.class ) ) );
      assertThat( systemUnderTest.isValidMedia( fileName ), is( true ) );
   }//End Method
}//End Class
