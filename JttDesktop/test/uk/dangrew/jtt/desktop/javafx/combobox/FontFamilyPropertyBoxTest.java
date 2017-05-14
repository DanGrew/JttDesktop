/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.combobox;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static uk.dangrew.jtt.model.utility.TestCommon.assertThatFontIsBold;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sun.javafx.application.PlatformImpl;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.text.Font;
import uk.dangrew.jtt.desktop.styling.FontFamilies;
import uk.dangrew.sd.graphics.launch.TestApplication;

/**
 * {@link FontFamilyPropertyBox} test.
 */
public class FontFamilyPropertyBoxTest {

   private static final String COMMON_FONT = "SansSerif";
   private static List< String > FONT_FAMILY_CHOICES = FontFamilies.getUsableFontFamilies();
   private static final int COMMON_FONT_POSITION = FONT_FAMILY_CHOICES.indexOf( COMMON_FONT );
   private ObjectProperty< Font > property;
   private FontFamilyPropertyBox systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      property = new SimpleObjectProperty<>();
   }//End Method
   
   /**
    * Method to launch the {@link FontFamilyPropertyBox} on the JavaFx {@link Thread}.
    */
   private void launchBox(){
      TestApplication.startPlatform();
      PlatformImpl.runAndWait( () -> {
         systemUnderTest = new FontFamilyPropertyBox( property );
      } );
   }//End Method
   
   @Test public void shouldHandleNoInitialFontFamily() {
      property.set( null );
      launchBox();
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), notNullValue() );
   }//End Method
   
   @Test public void shouldSelectInitialFontFamily() {
      property.set( Font.font( FONT_FAMILY_CHOICES.get( COMMON_FONT_POSITION ) ) );
      launchBox();
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( FONT_FAMILY_CHOICES.get( COMMON_FONT_POSITION ) ) );
   }//End Method
   
   @Test public void shouldHandleNoFontPreviously(){
      property.set( null );
      launchBox();
      systemUnderTest.getSelectionModel().select( FONT_FAMILY_CHOICES.get( COMMON_FONT_POSITION ) );
      assertThat( property.get(), notNullValue() );
      assertThat( property.get().getFamily(), is( FONT_FAMILY_CHOICES.get( COMMON_FONT_POSITION ) ) );
      assertThatFontIsBold( property.get() );
   }//End Method
   
   @Test public void shouldUpdatePropertyWhenSelectionIsChangedAndKeepSize() {
      property.set( Font.font( FONT_FAMILY_CHOICES.get( 3 ), 40 ) );
      launchBox();
      systemUnderTest.getSelectionModel().select( COMMON_FONT_POSITION );
      assertThat( property.get(), notNullValue() );
      assertThat( property.get().getFamily(), is( systemUnderTest.getSelectionModel().getSelectedItem() ) );
      assertThat( property.get().getSize(), is( 40.0 ) );
      assertThatFontIsBold( property.get() );
   }//End Method
   
   @Test public void shouldUpdateSelectionWhenFontChanges() {
      property.set( Font.font( FONT_FAMILY_CHOICES.get( COMMON_FONT_POSITION ) ) );
      launchBox();
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( FONT_FAMILY_CHOICES.get( COMMON_FONT_POSITION ) ) );
      
      property.set( Font.font( FONT_FAMILY_CHOICES.get( 10 ) ) );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( FONT_FAMILY_CHOICES.get( 10 ) ) );
   }//End Method
   
   @Test public void shouldHandleNoFontWhenFamilyIsSelected() {
      property.set( Font.font( FONT_FAMILY_CHOICES.get( 10 ) ) );
      launchBox();
      systemUnderTest.getSelectionModel().select( COMMON_FONT_POSITION );
      assertThat( property.get(), notNullValue() );
      assertThat( property.get().getFamily(), is( systemUnderTest.getSelectionModel().getSelectedItem() ) );
      assertThatFontIsBold( property.get() );
   }//End Method

}//End Class
