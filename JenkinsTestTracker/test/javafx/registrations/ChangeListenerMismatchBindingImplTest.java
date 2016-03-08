/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package javafx.registrations;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * {@link ChangeListenerMismatchBindingImpl} test.
 */
public class ChangeListenerMismatchBindingImplTest {
   
   protected ObjectProperty< Color > colourProperty;
   protected ObjectProperty< Paint > paintProperty;
   protected ChangeListenerMismatchBindingImpl< Color, Paint > systemUnderTest;

   @Before public void initialiseSystemUnderTest(){
      colourProperty = new SimpleObjectProperty< Color >( null );
      paintProperty = new SimpleObjectProperty< Paint >( null );
      systemUnderTest = new ChangeListenerMismatchBindingImpl< Color, Paint >( 
               colourProperty, 
               paintProperty, 
               ( Paint color ) -> { return ( Color )color; },
               ( Color paint ) -> { return paint; } 
      );
   }//End Method
   
   @Test public void shouldConfigureSecondPropertyToFirstValueWhenFirstIsSet() {
      final Color value = Color.AQUA;
      colourProperty.set( value );
      assertThat( colourProperty.get(), is( value ) );
      assertThat( paintProperty.get(), not( value ) );
      
      systemUnderTest.register();
      assertThat( colourProperty.get(), is( value ) );
      assertThat( paintProperty.get(), is( value ) );
   }//End Method
   
   @Test public void shouldConfigureSecondPropertyToFirstValueWhenSecondIsSet() {
      final Paint value = Color.AQUA;
      paintProperty.set( value );
      assertThat( colourProperty.get(), not( value ) );
      assertThat( paintProperty.get(), is( value ) );
      
      systemUnderTest.register();
      assertThat( colourProperty.get(), not( value ) );
      assertThat( paintProperty.get(), not( value ) );
   }//End Method
   
   @Test public void shouldUpdateSecondWhenFirstChanges() {
      systemUnderTest.register();
      
      colourProperty.set( Color.AZURE );
      assertThat( paintProperty.get(), is( colourProperty.get() ) );
   }//End Method
   
   @Test public void shouldUpdateFirstWhenSecondChanges() {
      systemUnderTest.register();
      
      paintProperty.set( Color.AZURE );
      assertThat( colourProperty.get(), is( paintProperty.get() ) );
   }//End Method
   
   @Test public void shouldUnregister() {
      systemUnderTest.register();
      systemUnderTest.unregister();
      
      colourProperty.set( Color.BLANCHEDALMOND );
      assertThat( paintProperty.get(), not( colourProperty.get() ) );
      
      paintProperty.set( Color.DARKGOLDENROD );
      assertThat( colourProperty.get(), not( paintProperty.get() ) );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAllowMultipleRegistrations(){
      systemUnderTest.register();
      systemUnderTest.register();
   }//End Method
   
   @Test public void shouldClearRegistrationsOnUnregister() {
      systemUnderTest.register();
      systemUnderTest.unregister();
      systemUnderTest.register();
   }//End Method
}//End Class
