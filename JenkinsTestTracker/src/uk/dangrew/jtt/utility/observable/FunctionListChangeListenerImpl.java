/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility.observable;

import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.collections.ListChangeListener;

/**
 * The {@link FunctionListChangeListenerImpl} provides a {@link ListChangeListener} that accepts
 * two {@link Consumer}s to delete actions to when the {@link List} is changed.
 * @param <TypeT> the type listened for.
 */
public class FunctionListChangeListenerImpl< TypeT > implements ListChangeListener< TypeT > {
   
   private final Consumer< TypeT > addFunction;
   private final Consumer< TypeT > removeFunction;

   /**
    * Constructs a new {@link FunctionListChangeListenerImpl}.
    * @param addFunction the {@link Consumer} to invoke when something is added.
    * @param removeFunction the {@link Consumer} to invoke when something is removed.
    */
   public FunctionListChangeListenerImpl( Consumer< TypeT > addFunction, Consumer< TypeT > removeFunction ) {
      this.addFunction = addFunction;
      this.removeFunction = removeFunction;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void onChanged( Change< ? extends TypeT > change ) {
      while ( change.next() ) {
         if ( change.wasAdded() ) {
            new ArrayList<>( change.getAddedSubList() ).forEach( addFunction::accept );
         }
         if ( change.wasRemoved() ) {
            change.getRemoved().forEach( removeFunction::accept );
         }
      }
   }//End Method

}//End Class
