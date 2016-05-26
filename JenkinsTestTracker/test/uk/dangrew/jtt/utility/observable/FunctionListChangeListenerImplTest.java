/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility.observable;

import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.sun.javafx.collections.NonIterableChange.SimpleAddChange;
import com.sun.javafx.collections.NonIterableChange.SimpleRemovedChange;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import uk.dangrew.jtt.utility.observable.FunctionListChangeListenerImpl;
import javafx.collections.ObservableList;

/**
 * {@link FunctionListChangeListenerImpl} test.
 */
public class FunctionListChangeListenerImplTest {

   private ObservableList< String > testList;
   private Consumer< String > addFunction;
   private Consumer< String > removeFunction;
   private ListChangeListener< String > systemUnderTest;
   
   @SuppressWarnings("unchecked") //Mocking, safe. 
   @Before public void initialiseSystemUnderTest(){
      testList = FXCollections.observableArrayList( 
               "something", "in", "this", "list", "for", "testing", "purposes" 
      );
      addFunction = Mockito.mock( Consumer.class );
      removeFunction = Mockito.mock( Consumer.class );
      systemUnderTest = new FunctionListChangeListenerImpl<>( addFunction, removeFunction );
   }//End Method
   
   @Test public void shouldCallFunctionWhenAdded() {
      Change< String > change = new SimpleAddChange<>( 0, testList.size(), testList );
      systemUnderTest.onChanged( change );
      for ( int i = 0; i < testList.size(); i++ ) {
         Mockito.verify( addFunction ).accept( testList.get( i ) );
      }
      Mockito.verifyNoMoreInteractions( addFunction, removeFunction );
   }//End Method
   
   @Test public void shouldCallFunctionWhenRemoved() {
      Change< String > change = new SimpleRemovedChange<>( 0, 0, testList.get( 3 ), testList );
      systemUnderTest.onChanged( change );
      Mockito.verify( removeFunction ).accept( testList.get( 3 ) );
      Mockito.verifyNoMoreInteractions( addFunction, removeFunction );
   }//End Method

   @Test public void shouldCallFunctionWhenAddedAndRemoved() {
      Change< String > change = new SimpleRemovedChange<>( 0, 3, testList.get( 3 ), testList );
      systemUnderTest.onChanged( change );
      for ( int i = 0; i < 3; i++ ) {
         Mockito.verify( addFunction ).accept( testList.get( i ) );
      }
      Mockito.verify( removeFunction ).accept( testList.get( 3 ) );
      Mockito.verifyNoMoreInteractions( addFunction, removeFunction );
   }//End Method
   
   @Test public void shouldAvoidAddConcurrencyException(){
      systemUnderTest = new FunctionListChangeListenerImpl<>( item -> testList.add( item ), removeFunction );
      Change< String > change = new SimpleRemovedChange<>( 0, 3, testList.get( 3 ), testList );
      systemUnderTest.onChanged( change );
      Assert.assertEquals( 10, testList.size() );
   }
   
   @Test public void shouldAvoidRemoveConcurrencyException(){
      systemUnderTest = new FunctionListChangeListenerImpl<>( addFunction, item -> testList.remove( item ) );
      Change< String > change = new SimpleRemovedChange<>( 0, 0, testList.get( 3 ), testList );
      systemUnderTest.onChanged( change );
      Assert.assertEquals( 6, testList.size() );
   }
}//End Class
