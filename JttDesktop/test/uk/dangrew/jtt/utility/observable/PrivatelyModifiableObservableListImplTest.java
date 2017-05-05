/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility.observable;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import uk.dangrew.jtt.utility.observable.PrivatelyModifiableObservableListImpl;

/**
 * {@link PrivatelyModifiableObservableListImpl} test.
 */
public class PrivatelyModifiableObservableListImplTest {
   
   private ObservableList< String > backingList;
   private ObservableList< String > systemUnderTest;
   
   @SuppressWarnings("unchecked") //Simply mocking genericized objects. 
   @Before public void initialiseSystemUnderTest(){
      backingList = Mockito.mock( ObservableList.class );
      systemUnderTest = new PrivatelyModifiableObservableListImpl< String >( backingList );
   }//End Method

   @Test public void safe_addListener_invalidation() {
      InvalidationListener listener = Mockito.mock( InvalidationListener.class );
      systemUnderTest.addListener( listener );
      Mockito.verify( backingList ).addListener( listener );
      Mockito.verifyNoMoreInteractions( backingList );
   }//End Method

   @Test public void safe_addListener_listchange() {
      @SuppressWarnings("unchecked") //Simply mocking genericized objects.
      ListChangeListener< String > listener = Mockito.mock( ListChangeListener.class );
      systemUnderTest.addListener( listener );
      Mockito.verify( backingList ).addListener( listener );
      Mockito.verifyNoMoreInteractions( backingList );
   }//End Method
   
   @Test public void safe_contains() {
      String value = "anything";
      systemUnderTest.contains( value );
      Mockito.verify( backingList ).contains( value );
      Mockito.verifyNoMoreInteractions( backingList );
   }//End Method

   @Test public void safe_containsAll() {
      @SuppressWarnings("unchecked") //Simply mocking genericized objects. 
      Collection< String > collection = Mockito.mock( Collection.class );
      systemUnderTest.containsAll( collection );
      Mockito.verify( backingList ).containsAll( collection );
      Mockito.verifyNoMoreInteractions( backingList );
   }//End Method

   @Test public void safe_get() {
      int value = 45;
      systemUnderTest.get( value );
      Mockito.verify( backingList ).get( value );
      Mockito.verifyNoMoreInteractions( backingList );
   }//End Method

   @Test public void safe_indexOf() {
      String value = "anything";
      systemUnderTest.indexOf( value );
      Mockito.verify( backingList ).indexOf( value );
      Mockito.verifyNoMoreInteractions( backingList );
   }//End Method

   @Test public void safe_isEmpty() {
      systemUnderTest.isEmpty();
      Mockito.verify( backingList ).isEmpty();
      Mockito.verifyNoMoreInteractions( backingList );
   }//End Method

   @Test public void safe_iterator() {
      systemUnderTest.iterator();
      Mockito.verify( backingList ).iterator();
      Mockito.verifyNoMoreInteractions( backingList );
   }//End Method

   @Test public void safe_lastIndexOf() {
      String value = "anything";
      systemUnderTest.lastIndexOf( value );
      Mockito.verify( backingList ).lastIndexOf( value );
      Mockito.verifyNoMoreInteractions( backingList );
   }//End Method

   @Test public void safe_listIterator() {
      systemUnderTest.listIterator();
      Mockito.verify( backingList ).listIterator();
      Mockito.verifyNoMoreInteractions( backingList );
   }//End Method

   @Test public void safe_listIterator_int() {
      int value = 98;
      systemUnderTest.listIterator( value );
      Mockito.verify( backingList ).listIterator( value );
      Mockito.verifyNoMoreInteractions( backingList );
   }//End Method

   @Test public void safe_removeListener_invalidation() {
      InvalidationListener listener = Mockito.mock( InvalidationListener.class );
      systemUnderTest.removeListener( listener );
      Mockito.verify( backingList ).removeListener( listener );
      Mockito.verifyNoMoreInteractions( backingList );
   }//End Method

   @Test public void safe_removeListener_listchange() {
      @SuppressWarnings("unchecked") //Simply mocking genericized objects. 
      ListChangeListener< String > listener = Mockito.mock( ListChangeListener.class );
      systemUnderTest.removeListener( listener );
      Mockito.verify( backingList ).removeListener( listener );
      Mockito.verifyNoMoreInteractions( backingList );
   }//End Method

   @Test public void safe_size() {
      systemUnderTest.size();
      Mockito.verify( backingList ).size();
      Mockito.verifyNoMoreInteractions( backingList );
   }//End Method

   @Test public void safe_subList() {
      int value1 = 3;
      int value2 = 8;
      systemUnderTest.subList( value1, value2 );
      Mockito.verify( backingList ).subList( value1, value2 );
      Mockito.verifyNoMoreInteractions( backingList );
   }//End Method

   @Test public void safe_toArray() {
      systemUnderTest.toArray();
      Mockito.verify( backingList ).toArray();
      Mockito.verifyNoMoreInteractions( backingList );
   }//End Method
   
   @Test public void safe_toArray_array() {
      String[] value = new String[ 0 ];
      systemUnderTest.toArray( value );
      Mockito.verify( backingList ).toArray( value );
      Mockito.verifyNoMoreInteractions( backingList );
   }//End Method
   
   @Test public void safe_foreach() {
      @SuppressWarnings("unchecked") Consumer< String > consumer = mock( Consumer.class );
      
      systemUnderTest.forEach( consumer );
      Mockito.verify( backingList ).forEach( consumer );
      Mockito.verifyNoMoreInteractions( backingList );
   }//End Method
   
   @Test( expected = UnsupportedOperationException.class ) public void unsafe_add() {
      systemUnderTest.add( "anything" );
   }//End Method

   @Test( expected = UnsupportedOperationException.class ) public void unsafe_add_position() {
      systemUnderTest.add( 0, "anything" );
   }//End Method

   @Test( expected = UnsupportedOperationException.class ) public void unsafe_addAll_collection() {
      systemUnderTest.addAll( new ArrayList<>() );
   }//End Method

   @Test( expected = UnsupportedOperationException.class ) public void unsafe_addAll_position() {
      systemUnderTest.addAll( 0, new ArrayList<>() );
   }//End Method

   @Test( expected = UnsupportedOperationException.class ) public void unsafe_addAll_variable() {
      systemUnderTest.addAll( "anything", "to", "add" );
   }//End Method

   @Test( expected = UnsupportedOperationException.class ) public void unsafe_clear() {
      systemUnderTest.clear();
   }//End Method
   
   @Test( expected = UnsupportedOperationException.class ) public void unsafe_remove_position() {
      systemUnderTest.remove( 0 );
   }//End Method

   @Test( expected = UnsupportedOperationException.class ) public void unsafe_remove_between() {
      systemUnderTest.remove( 0, 1 );
   }//End Method

   @Test( expected = UnsupportedOperationException.class ) public void unsafe_remove_object() {
      systemUnderTest.remove( "anything" );
   }//End Method

   @Test( expected = UnsupportedOperationException.class ) public void unsafe_removeAll_collection() {
      systemUnderTest.removeAll( new ArrayList<>() );
   }//End Method

   @Test( expected = UnsupportedOperationException.class ) public void unsafe_removeAll_variable() {
      systemUnderTest.removeAll( "anything" );
   }//End Method

   @Test( expected = UnsupportedOperationException.class ) public void unsafe_retainAll_collection() {
      systemUnderTest.retainAll( new ArrayList<>() );
   }//End Method

   @Test( expected = UnsupportedOperationException.class ) public void unsafe_retainAll_variable() {
      systemUnderTest.retainAll( "anything" );
   }//End Method

   @Test( expected = UnsupportedOperationException.class ) public void unsafe_set() {
      systemUnderTest.set( 0, "anything" );
   }//End Method

   @Test( expected = UnsupportedOperationException.class ) public void unsafe_setAll_collection() {
      systemUnderTest.setAll( new ArrayList<>() );
   }//End Method

   @Test( expected = UnsupportedOperationException.class ) public void unsafe_setAll_variable() {
      systemUnderTest.setAll( "anything" );
   }//End Method

}//End Class
