/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility.synchronisation;

import java.util.List;
import java.util.function.Consumer;

import com.sun.javafx.collections.ObservableListWrapper;

import javafx.collections.FXCollections;

/**
 * {@link SynchronizedObservableList} provides an {@link ObservableListWrapper}
 * that synchronizes certain calls to avoid concurrency issues.
 */
public class SynchronizedObservableList< TypeT > extends ObservableListWrapper< TypeT > {

   private final Object lock;
   
   /**
    * Constructs a new {@link SynchronizedObservableList} with a {@link FXCollections#observableArrayList()}.
    */
   public SynchronizedObservableList(){
      this( FXCollections.observableArrayList() );
   }//End Constructor
   
   /**
    * Constructs a new {@link SynchronizedObservableList} wrapping the given backing list.
    * @param backingList the backing {@link List}.
    */
   public SynchronizedObservableList( List< TypeT > backingList ) {
      super( backingList );
      this.lock = new Object();
   }//End Constructor
   
   /**
    * {@inheritDoc}
    * Synchronizes the instruction.
    */
   @Override public boolean add( TypeT item ) {
      synchronized ( lock ) {
         return super.add( item );
      }
   }//End Method
   
   /**
    * {@inheritDoc}
    * Synchronizes the instruction.
    */
   @Override public void forEach( Consumer< ? super TypeT > action) {
      synchronized ( lock ) {
         super.forEach( action );
      }
   }//End Method
}//End Class
