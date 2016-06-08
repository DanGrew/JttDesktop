/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility.conversion;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javafx.scene.paint.Color;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

/**
 * {@link ColorConverter} test.
 */
@RunWith( JUnitParamsRunner.class )
public class ColorConverterTest {

   private ColorConverter systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new ColorConverter();
   }//End Method
   
   /**
    * Method to provide the {@link Color} parameters to {@link #shouldConvertStandardColorToHexAndBack(Color)}.
    * @return the parameters.
    */
   public static final Object[] provideColours(){
      return new Object[]{
               new Object[]{ Color.RED },
               new Object[]{ Color.ANTIQUEWHITE },
               new Object[]{ Color.BLACK },
               new Object[]{ Color.WHITE },
               new Object[]{ Color.BEIGE },
               new Object[]{ Color.DARKTURQUOISE },
               new Object[]{ new Color( 0, 0, 0, 0 ) },
               new Object[]{ new Color( 1, 1, 1, 0 ) },
               new Object[]{ new Color( 0.2, 0.5, 0.6, 0 ) },
               new Object[]{ new Color( 0.4, 0.4, 0.4, 0.5 ) },
               new Object[]{ new Color( 0.4, 0.4, 0.4, 1 ) }
      };
   }//End Method
   
   @Parameters( method = "provideColours" )
   @Test public void shouldConvertStandardColorToHexAndBack( Color colour ) {
      Color red = Color.RED;
      
      String hex = systemUnderTest.colorToHex( red );
      Color converted = systemUnderTest.hexToColor( hex );
      
      assertThat( converted, is( red ) );
   }//End Method
   
   @Test public void shouldIgnoreNullColorToHex(){
      assertThat( systemUnderTest.colorToHex( null ), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldIgnoreNullHexToColor(){
      assertThat( systemUnderTest.hexToColor( null ), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldHandleInappropriateHexToColor(){
      assertThat( systemUnderTest.hexToColor( "anything" ), is( nullValue() ) );
   }//End Method

}//End Class
