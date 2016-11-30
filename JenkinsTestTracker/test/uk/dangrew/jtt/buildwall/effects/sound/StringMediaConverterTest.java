/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.effects.sound;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import javafx.scene.media.MediaPlayer;
import uk.dangrew.jtt.graphics.JavaFxInitializer;

public class StringMediaConverterTest {
   
   private StringMediaConverter systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      JavaFxInitializer.startPlatform();
      systemUnderTest = new StringMediaConverter();
   }//End Method

   @Test public void shouldHandleNullValue() {
      assertThat( systemUnderTest.apply( null ), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldHandleEmptyString() {
      assertThat( systemUnderTest.apply( "    " ), is( nullValue() ) );
   }//End Method

   @Test public void shouldHandleMissingMediaFile() {
      assertThat( systemUnderTest.apply( "missing-media-file.txt" ), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldHandleInvalidMediaFile() {
      String fileName = getClass().getResource( "invalid-media-file.txt" ).getPath();
      assertThat( systemUnderTest.apply( fileName ), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldHandleValidMediaFile() {
      String fileName = getClass().getResource( "valid-media-file.m4a" ).getPath();
      assertThat( systemUnderTest.apply( fileName ), is( notNullValue() ) );
      assertThat( systemUnderTest.apply( fileName ), is( instanceOf( MediaPlayer.class ) ) );
   }//End Method
}//End Class
