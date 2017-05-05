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

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

/**
 * The {@link FunctionMapChangeListenerImpl} provides a {@link MapChangeListener} that accepts
 * two {@link BiConsumer}s to take action when the {@link Map} is changed.
 * @param <KeyTypeT> the key type of the map.
 * @param <ValueTypeT> the value type of the map.
 */
public class FunctionMapChangeListenerImpl< KeyTypeT, ValueTypeT > implements MapChangeListener< KeyTypeT, ValueTypeT > {
   
   private final ObservableMap< KeyTypeT, ValueTypeT > map;
   private final BiConsumer< KeyTypeT, ValueTypeT > addFunction;
   private final BiConsumer< KeyTypeT, ValueTypeT > removeFunction;

   /**
    * Constructs a new {@link FunctionMapChangeListenerImpl}.
    * @param addFunction the {@link BiConsumer} to invoke when something is added.
    * @param removeFunction the {@link BiConsumer} to invoke when something is removed.
    */
   public FunctionMapChangeListenerImpl( 
            ObservableMap< KeyTypeT, ValueTypeT > map, 
            BiConsumer< KeyTypeT, ValueTypeT > addFunction, 
            BiConsumer< KeyTypeT, ValueTypeT > removeFunction 
   ) {
      this.map = map;
      this.addFunction = addFunction;
      this.removeFunction = removeFunction;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void onChanged( MapChangeListener.Change< ? extends KeyTypeT, ? extends ValueTypeT > change ) {
      if ( !map.containsKey( change.getKey() ) ) {
         removeFunction.accept( change.getKey(), change.getValueRemoved() );
      } else {
         addFunction.accept( change.getKey(), change.getValueAdded() );
      }
   }//End Method

}//End Class
