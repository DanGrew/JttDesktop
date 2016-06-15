/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility.synchronisation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import com.sun.javafx.collections.ObservableMapWrapper;

/**
 * The {@link SynchronizedObservableMap} provides an extension to {@link ObservableMapWrapper}
 * that attempts to resolve a weakness in the Synchronized Map in {@link javafx.collections.FXCollections} which
 * does not synchronize the forEach method so that it can conflict with put commands.
 */
public class SynchronizedObservableMap< K, V > extends ObservableMapWrapper< K, V >{

   private final Object lock;
   
   /**
    * Constructs a new {@link SynchronizedObservableMap} with a {@link HashMap}.
    */
   public SynchronizedObservableMap(){
      this( new HashMap<>() );
   }//End Constructor
   
   /**
    * Constructs a new {@link SynchronizedObservableMap} wrapping the given backing map.
    * @param map the backing {@link Map}.
    */
   public SynchronizedObservableMap( Map< K, V > map ) {
      super( map );
      this.lock = new Object();
   }//End Constructor
   
   /**
    * {@inheritDoc}
    * Synchronizes the instruction.
    */
   @Override public V put( K key, V value ) {
      synchronized ( lock ) {
         return super.put( key, value );
      }
   }//End Method
   
   /**
    * {@inheritDoc}
    * Synchronizes the instruction.
    */
   @Override public void forEach( BiConsumer<? super K, ? super V> action) {
      synchronized ( lock ) {
         super.forEach( action );
      }
   }//End Method
   
}//End Class
