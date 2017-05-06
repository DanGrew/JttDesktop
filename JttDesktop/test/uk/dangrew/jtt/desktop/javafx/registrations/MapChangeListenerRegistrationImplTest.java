/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.registrations;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.util.Pair;
import uk.dangrew.jtt.desktop.javafx.registrations.MapChangeListenerRegistrationImpl;

/**
 * {@link MapChangeListenerRegistrationImpl} test.
 */
public class MapChangeListenerRegistrationImplTest {
   
   private static final Object OBJECT1 = new Object();
   private static final Object OBJECT2 = new Object();
   private static final Object OBJECT3 = new Object();
   private static final Object OBJECT4 = new Object();
   
   private ObservableMap< String, Object > observableMap;
   private List< Pair< String, Object > > addedResultsList;
   private List< Pair< String, Object > > removedResultsList;
   private MapChangeListenerRegistrationImpl< String, Object > systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      addedResultsList = new ArrayList<>();
      removedResultsList = new ArrayList<>();
      observableMap = FXCollections.observableHashMap();
      
      systemUnderTest = new MapChangeListenerRegistrationImpl<>(
               observableMap, 
               ( MapChangeListener.Change< ? extends String, ? extends Object > change ) -> {
                  if ( !observableMap.containsKey( change.getKey() ) ) {
                     removedResultsList.add( new Pair<>( change.getKey(), change.getValueRemoved() ) );
                  } else {
                     addedResultsList.add( new Pair<>( change.getKey(), change.getValueAdded() ) );
                  }
               }
      );
   }//End Method
   
   @Test public void shouldAddListenerAndRecordAdditions() {
      systemUnderTest.register();
      
      assertThat( addedResultsList, hasSize( 0 ) );
      assertThat( removedResultsList, hasSize( 0 ) );

      final String firstValue = "something to go first";
      
      observableMap.put( firstValue, OBJECT1 );
      
      assertValuesRecorded( addedResultsList, new Pair<>( firstValue, OBJECT1 ) );
      assertValuesRecorded( removedResultsList );
      
      final String secondValue = "second";
      final String thirdValue = "another value after second";
      
      Map< String, Object > mapToPut = new LinkedHashMap<>();
      mapToPut.put( secondValue, OBJECT2 );
      mapToPut.put( thirdValue, OBJECT3 );
      observableMap.putAll( mapToPut );
      
      assertValuesRecorded( 
               addedResultsList, 
               new Pair<>( firstValue, OBJECT1 ),
               new Pair<>( secondValue, OBJECT2 ),
               new Pair<>( thirdValue, OBJECT3 )
      );
      assertValuesRecorded( removedResultsList );
   }//End Method
   
   @Test public void shouldAddListenerAndRecordRemovals() {
      systemUnderTest.register();
      
      assertThat( addedResultsList, hasSize( 0 ) );
      assertThat( removedResultsList, hasSize( 0 ) );

      List< String > testValues = Arrays.asList( "one", "value", "after", "another" );
      
      Map< String, Object > mapToPut = new LinkedHashMap<>();
      mapToPut.put( testValues.get( 0 ), OBJECT4 );
      mapToPut.put( testValues.get( 1 ), OBJECT3 );
      mapToPut.put( testValues.get( 2 ), OBJECT2 );
      mapToPut.put( testValues.get( 3 ), OBJECT1 );
      observableMap.putAll( mapToPut );
      
      assertValuesRecorded( 
               addedResultsList, 
               new Pair<>( testValues.get( 0 ), OBJECT4 ),
               new Pair<>( testValues.get( 1 ), OBJECT3 ),
               new Pair<>( testValues.get( 2 ), OBJECT2 ),
               new Pair<>( testValues.get( 3 ), OBJECT1 )
      );
      assertValuesRecorded( removedResultsList );

      observableMap.remove( testValues.get( 1 ) );
      
      assertValuesRecorded( 
               addedResultsList, 
               new Pair<>( testValues.get( 0 ), OBJECT4 ),
               new Pair<>( testValues.get( 1 ), OBJECT3 ),
               new Pair<>( testValues.get( 2 ), OBJECT2 ),
               new Pair<>( testValues.get( 3 ), OBJECT1 )
      );
      assertValuesRecorded( 
               removedResultsList, 
               new Pair<>( testValues.get( 1 ), OBJECT3 )
      );
   }//End Method
   
   @Test public void shouldRemoveListener() {
      systemUnderTest.register();
      systemUnderTest.unregister();

      Map< String, Object > mapToPut = new HashMap<>();
      mapToPut.put( "some", OBJECT1 );
      mapToPut.put( "thing", OBJECT2 );
      mapToPut.put( "being", OBJECT3 );
      mapToPut.put( "added", OBJECT4 );
      
      observableMap.putAll( mapToPut );
      
      assertValuesRecorded( addedResultsList );
      assertValuesRecorded( removedResultsList );
      
      observableMap.remove( "thing" );
      observableMap.remove( "being" );
      observableMap.remove( "added" );
      
      assertValuesRecorded( addedResultsList );
      assertValuesRecorded( removedResultsList );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotRegisterMultipleTimes(){
      systemUnderTest.register();
      systemUnderTest.register();
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotUnregisterIfNotRegistered(){
      systemUnderTest.unregister();
   }//End Method
   
   /**
    * Method to assert that the correct values have been recorded in the appropriate {@link List}.
    * @param listRecordedIn the {@link List} to assert on.
    * @param expected the expected recorded results since beginning of case.
    */
   @SafeVarargs private final void assertValuesRecorded( 
            List< Pair< String, Object > > listRecordedIn, 
            Pair< String, Object >... expected 
   ) {
      assertThat( listRecordedIn, hasSize( expected.length ) );
      for ( int i = 0; i < expected.length; i++ ) {
         assertThat( listRecordedIn.get( i ), is( expected[ i ] ) );
      }
   }//End Method

}//End Class
