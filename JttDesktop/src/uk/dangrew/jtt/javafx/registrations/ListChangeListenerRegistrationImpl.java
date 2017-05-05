/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.javafx.registrations;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * {@link ListChangeListenerRegistrationImpl} provides a {@link RegistrationImpl} that can handle
 * the registration of a {@link ListChangeListener}.
 * @param <ListTypeT> the {@link ListChangeListener} type.
 */
public class ListChangeListenerRegistrationImpl< ListTypeT > extends RegistrationImpl {
   
   private final ObservableList< ListTypeT > list;
   private final ListChangeListener< ListTypeT > listener;
   private boolean registered = false;
   
   /**
    * Constructs a new {@link ListChangeListenerRegistrationImpl}.
    * @param observableList the {@link ObservableList} associated.
    * @param action the {@link ListChangeListener} associated with the {@link ObservableList}.
    */
   public ListChangeListenerRegistrationImpl( 
            ObservableList< ListTypeT > observableList, 
            ListChangeListener< ListTypeT > action 
   ) {
      this.list = observableList;
      this.listener = action;
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override protected void register() {
      if ( registered ) throw new IllegalStateException( "Registered multiple times which is not supported." );
      
      registered = true;
      list.addListener( listener );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override protected void unregister() {
      if ( !registered ) throw new IllegalStateException( "Unregistering something that was never registered." );
      
      list.removeListener( listener );
   }//End Method

}//End Class
