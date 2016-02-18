/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package javafx.combobox;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import graphics.JavaFxInitializer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.text.Font;

/**
 * {@link FontFamilyPropertyBox} test.
 */
public class FontFamilyPropertyBoxTest {

   private static List< String > FONT_FAMILY_CHOICES = Font.getFamilies();
   private ObjectProperty< Font > property;
   private FontFamilyPropertyBox systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      property = new SimpleObjectProperty<>();
   }//End Method
   
   /**
    * Method to launch the {@link FontFamilyPropertyBox} on the JavaFx {@link Thread}.
    */
   private void launchBox(){
      JavaFxInitializer.runAndWait( () -> {
         systemUnderTest = new FontFamilyPropertyBox( property );
      } );
   }//End Method
   
   @Test public void shouldHandleNoInitialFontFamily() {
      property.set( null );
      launchBox();
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), notNullValue() );
   }//End Method
   
   @Test public void shouldSelectInitialFontFamily() {
      property.set( Font.font( FONT_FAMILY_CHOICES.get( 5 ) ) );
      launchBox();
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( FONT_FAMILY_CHOICES.get( 5 ) ) );
   }//End Method
   
   @Test public void shouldUpdatePropertyWhenSelectionIsChangedAndKeepSize() {
      property.set( Font.font( FONT_FAMILY_CHOICES.get( 3 ), 40 ) );
      launchBox();
      systemUnderTest.getSelectionModel().select( 4 );
      assertThat( property.get(), notNullValue() );
      assertThat( property.get().getFamily(), is( systemUnderTest.getSelectionModel().getSelectedItem() ) );
      assertThat( property.get().getSize(), is( 40.0 ) );
   }//End Method
   
   @Test public void shouldUpdateSelectionWhenFontChanges() {
      property.set( Font.font( FONT_FAMILY_CHOICES.get( 5 ) ) );
      launchBox();
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( FONT_FAMILY_CHOICES.get( 5 ) ) );
      
      property.set( Font.font( FONT_FAMILY_CHOICES.get( 10 ) ) );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( FONT_FAMILY_CHOICES.get( 10 ) ) );
   }//End Method
   
   @Test public void shouldHandleNoFontWhenFamilyIsSelected() {
      for ( int i = 0; i < FONT_FAMILY_CHOICES.size(); i++ ) {
         System.out.println( FONT_FAMILY_CHOICES.get( i ) );
         System.out.println( Font.font( FONT_FAMILY_CHOICES.get( i ) ).getFamily() );
   }
      property.set( Font.font( FONT_FAMILY_CHOICES.get( 5 ) ) );
      launchBox();
      systemUnderTest.getSelectionModel().select( 10 );
      assertThat( property.get(), notNullValue() );
      assertThat( property.get().getFamily(), is( systemUnderTest.getSelectionModel().getSelectedItem() ) );
   }//End Method

}//End Class