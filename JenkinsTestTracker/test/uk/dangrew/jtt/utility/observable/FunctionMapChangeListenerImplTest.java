/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility.observable;

import java.util.function.BiConsumer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sun.javafx.binding.MapExpressionHelper.SimpleChange;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.MapChangeListener.Change;
import javafx.collections.ObservableMap;

/**
 * {@link FunctionMapChangeListenerImpl} test.
 */
public class FunctionMapChangeListenerImplTest {

   private static final Object OBJECT1 = new Object();
   private static final Object OBJECT2 = new Object();
   private static final Object OBJECT3 = new Object();
   private static final Object OBJECT4 = new Object();
   
   private static final String STRING1 = "something";
   private static final String STRING2 = "int";
   private static final String STRING3 = "this";
   private static final String STRING4 = "map";
   
   private ObservableMap< String, Object > testMap;
   @Mock private BiConsumer< String, Object > addFunction;
   @Mock private BiConsumer< String, Object > removeFunction;
   private MapChangeListener< String, Object > systemUnderTest;
   
   @SuppressWarnings("unchecked") //Mocking, safe. 
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      
      testMap = FXCollections.observableHashMap();
      testMap.put( STRING1,  OBJECT1 );
      testMap.put( STRING2, OBJECT2 );
      testMap.put( STRING3, OBJECT3 );
      testMap.put( STRING4, OBJECT4 ); 
      
      systemUnderTest = new FunctionMapChangeListenerImpl<>( testMap, addFunction, removeFunction );
   }//End Method
   
   @Test public void shouldCallFunctionWhenAdded() {
      Change< String, Object > change = new SimpleChange<>( testMap ).setAdded( STRING2, OBJECT2 );
      systemUnderTest.onChanged( change );
      Mockito.verify( addFunction ).accept( STRING2, OBJECT2 );
      Mockito.verifyNoMoreInteractions( addFunction, removeFunction );
   }//End Method
   
   @Test public void shouldCallFunctionWhenRemoved() {
      testMap.clear();
      Change< String, Object > change = new SimpleChange<>( testMap ).setRemoved( STRING2, OBJECT2 );
      systemUnderTest.onChanged( change );
      Mockito.verify( removeFunction ).accept( STRING2, OBJECT2 );
      Mockito.verifyNoMoreInteractions( addFunction, removeFunction );
   }//End Method

   @Test public void shouldCallFunctionWhenChanged() {
      Change< String, Object > change = new SimpleChange<>( testMap ).setPut( STRING2, OBJECT2, OBJECT3 );
      systemUnderTest.onChanged( change );
      Mockito.verify( addFunction ).accept( STRING2, OBJECT3 );
      Mockito.verifyNoMoreInteractions( addFunction, removeFunction );
   }//End Method
}//End Class
