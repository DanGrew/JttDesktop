/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.registrations;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * {@link ChangeListenerRegistrationImpl} test.
 */
public class ChangeListenerRegistrationImplTest {
   
   private static final String INITIAL_VALUE = "anything to start with";
   private ObjectProperty< String > property;
   private ObjectProperty< String > assertion;
   private ChangeListenerRegistrationImpl< String > systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      assertion = new SimpleObjectProperty<>();
      property = new SimpleObjectProperty<>();
      
      systemUnderTest = new ChangeListenerRegistrationImpl<>(
               property, 
               INITIAL_VALUE,
               ( source, old, update ) -> assertion.set( update )
      );
   }//End Method
   
   @Test public void propertyShouldTakeInitialValue(){
      assertThat( property.get(), is( INITIAL_VALUE ) );
   }//End Method
   
   @Test public void shouldAddListener() {
      systemUnderTest.register();
      
      assertThat( assertion.get(), nullValue() );

      String objectToSet = "aything";
      property.set( objectToSet );
      
      assertThat( assertion.get(), is( objectToSet ) );
   }//End Method
   
   @Test public void shouldRemoveListener() {
      systemUnderTest.register();
      assertThat( assertion.get(), nullValue() );

      String objectToSet = "aything";
      property.set( objectToSet );
      assertThat( assertion.get(), is( objectToSet ) );
      
      systemUnderTest.unregister();
      
      objectToSet = "next value";
      property.set( objectToSet );
      assertThat( assertion.get(), not( objectToSet ) );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotRegisterMultipleTimes(){
      systemUnderTest.register();
      systemUnderTest.register();
   }//End Method

}//End Class
