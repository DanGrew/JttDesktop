/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.javafx.registrations;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import uk.dangrew.jtt.javafx.registrations.ListChangeListenerRegistrationImpl;
import uk.dangrew.jtt.utility.observable.FunctionListChangeListenerImpl;

/**
 * {@link ListChangeListenerRegistrationImpl} test.
 */
public class ListChangeListenerRegistrationImplTest {
   
   private ObservableList< String > observableList;
   private List< String > addedResultsList;
   private List< String > removedResultsList;
   private ListChangeListenerRegistrationImpl< String > systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      addedResultsList = new ArrayList<>();
      removedResultsList = new ArrayList<>();
      observableList = FXCollections.observableArrayList();
      
      systemUnderTest = new ListChangeListenerRegistrationImpl<>(
               observableList, 
               new FunctionListChangeListenerImpl<>( 
                        value -> addedResultsList.add( value ), 
                        value -> removedResultsList.add( value )
               )
      );
   }//End Method
   
   @Test public void shouldAddListenerAndRecordAdditions() {
      systemUnderTest.register();
      
      assertThat( addedResultsList, hasSize( 0 ) );
      assertThat( removedResultsList, hasSize( 0 ) );

      final String firstValue = "something to go first";
      observableList.add( firstValue );
      
      assertValuesRecorded( addedResultsList, firstValue );
      assertValuesRecorded( removedResultsList );
      
      final String secondValue = "second";
      final String thirdValue = "another value after second";
      observableList.addAll( secondValue, thirdValue );
      
      assertValuesRecorded( addedResultsList, firstValue, secondValue, thirdValue );
      assertValuesRecorded( removedResultsList );
   }//End Method
   
   @Test public void shouldAddListenerAndRecordRemovals() {
      systemUnderTest.register();
      
      assertThat( addedResultsList, hasSize( 0 ) );
      assertThat( removedResultsList, hasSize( 0 ) );

      List< String > testValues = Arrays.asList( "one", "value", "after", "another", "for", "testing" );
      
      observableList.addAll( testValues );
      
      assertValuesRecorded( addedResultsList, testValues.toArray( new String[ 6 ] ) );
      assertValuesRecorded( removedResultsList );

      observableList.remove( 1 );
      
      assertValuesRecorded( 
               addedResultsList, 
               testValues.get( 0 ), testValues.get( 1 ), testValues.get( 2 ), 
               testValues.get( 3 ), testValues.get( 4 ), testValues.get( 5 ) 
      );
      assertValuesRecorded( 
               removedResultsList, 
               testValues.get( 1 )
      );
      
      observableList.removeAll( testValues.get( 2 ), testValues.get( 4 ) );
      
      assertValuesRecorded( 
               addedResultsList, 
               testValues.get( 0 ), testValues.get( 1 ), testValues.get( 2 ), 
               testValues.get( 3 ), testValues.get( 4 ), testValues.get( 5 ) 
      );
      assertValuesRecorded( 
               removedResultsList,
               testValues.get( 1 ),
               testValues.get( 2 ), 
               testValues.get( 4 )  
      );
   }//End Method
   
   @Test public void shouldRemoveListener() {
      systemUnderTest.register();
      systemUnderTest.unregister();

      observableList.addAll( "some", "thing", "being", "added" );
      
      assertValuesRecorded( addedResultsList );
      assertValuesRecorded( removedResultsList );
      
      observableList.removeAll( "thing", "being", "added" );
      
      assertValuesRecorded( addedResultsList );
      assertValuesRecorded( removedResultsList );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotRegisterMultipleTimes(){
      systemUnderTest.register();
      systemUnderTest.register();
   }//End Method
   
   /**
    * Method to assert that the correct values have been recorded in the appropriate {@link List}.
    * @param listRecordedIn the {@link List} to assert on.
    * @param expected the expected recorded results since beginning of case.
    */
   private void assertValuesRecorded( List< String > listRecordedIn, String... expected ) {
      assertThat( listRecordedIn, hasSize( expected.length ) );
      for ( int i = 0; i < expected.length; i++ ) {
         assertThat( listRecordedIn.get( i ), is( expected[ i ] ) );
      }
   }//End Method

}//End Class
