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

/**
 * {@link ChangeListenerBindingImpl} test.
 */
public class ChangeListenerBindingImplTest {

   private ObjectProperty< String > propertyA;
   private ObjectProperty< String > propertyB;
   private ChangeListenerBindingImpl< String > systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      propertyA = new SimpleObjectProperty< String >( null );
      propertyB = new SimpleObjectProperty< String >( null );
      systemUnderTest = new ChangeListenerBindingImpl<>( propertyA, propertyB );
   }//End Method
   
   @Test public void shouldConfigureSecondPropertyToFirstValueWhenFirstIsSet() {
      final String value = "something specific";
      propertyA.set( value );
      assertThat( propertyA.get(), is( value ) );
      assertThat( propertyB.get(), not( value ) );
      
      systemUnderTest.register();
      assertThat( propertyA.get(), is( value ) );
      assertThat( propertyB.get(), is( value ) );
   }//End Method
   
   @Test public void shouldConfigureSecondPropertyToFirstValueWhenSecondIsSet() {
      final String value = "something specific";
      propertyB.set( value );
      assertThat( propertyA.get(), not( value ) );
      assertThat( propertyB.get(), is( value ) );
      
      systemUnderTest.register();
      assertThat( propertyA.get(), not( value ) );
      assertThat( propertyB.get(), not( value ) );
   }//End Method
   
   @Test public void shouldUpdateSecondWhenFirstChanges() {
      systemUnderTest.register();
      
      propertyA.set( "anything you can think of" );
      assertThat( propertyB.get(), is( propertyA.get() ) );
   }//End Method
   
   @Test public void shouldUpdateFirstWhenSecondChanges() {
      systemUnderTest.register();
      
      propertyB.set( "anything you can think of" );
      assertThat( propertyA.get(), is( propertyB.get() ) );
   }//End Method
   
   @Test public void shouldUnregister() {
      systemUnderTest.register();
      systemUnderTest.unregister();
      
      propertyA.set( "anything you can think of" );
      assertThat( propertyB.get(), not( propertyA.get() ) );
      
      propertyB.set( "anything else you can think of" );
      assertThat( propertyA.get(), not( propertyB.get() ) );
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
