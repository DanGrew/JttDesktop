/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility.synchronisation;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.utility.TestCommon;

/**
 * {@link SynchronizedObservableMap} test.
 */
public class SynchronizedObservableMapTest {
   
   @Mock private Map< String, String > backingMap;
   private SynchronizedObservableMap< String, String > systemUnderTest;
   
   @Before public void initialiseSystemUnderTes(){
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new SynchronizedObservableMap<>( backingMap );
   }//End Method

   @Test public void putAndForEachShouldBeSynchronizedAndNotCauseConcurrencyIssues() throws InterruptedException {
      systemUnderTest = new SynchronizedObservableMap<>();
      
      TestCommon.assertConcurrencyIsNotAnIssue( 
               i -> systemUnderTest.put( "" + i , "anything" ), 
               i -> systemUnderTest.forEach( ( key, value ) -> {} ),
               1000
      );
   }//End Method
   
   @Test public void putShouldCallThroughToBackingMap(){
      verifyZeroInteractions( backingMap );
      
      final String key = "some key";
      final String value = "some value";
      systemUnderTest.put( key, value );
      
      verify( backingMap ).put( key, value );
   }//End Method
   
   @Test public void forEachShouldCallForEachEntry(){
      backingMap = new LinkedHashMap<>();
      systemUnderTest = new SynchronizedObservableMap<>( backingMap );
      
      backingMap.put( "a", "b" );
      backingMap.put( "c", "d" );
      backingMap.put( "f", "g" );
      backingMap.put( "x", "z" );
      
      List< String > results = new ArrayList<>();
      final BiConsumer< String, String > consumer = ( k, v ) -> { results.add( k ); results.add( v ); };
      systemUnderTest.forEach( consumer );
      
      assertThat( results, contains( "a", "b", "c", "d", "f", "g", "x", "z" ) );
   }//End Method
   
   @Test public void shouldConstructWithBasicHashMap(){
      systemUnderTest = new SynchronizedObservableMap<>();
      systemUnderTest.put( "something", "else" );
      assertThat( systemUnderTest.get( "something" ), is( "else" ) );
   }//End Method

}//End Class
