/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.storage.database.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import uk.dangrew.jtt.utility.observable.FunctionListChangeListenerImpl;

/**
 * The {@link GlobalPropertyListenerImpl} is responsible for listening to the same {@link ObjectProperty} change
 * across all objects associated with the {@link JenkinsDatabase}.
 * @param <ObjectTypeT> the type of object being changed.
 * @param <PropertyTypeT> the type of property being changed.
 */
public class GlobalPropertyListenerImpl< ObjectTypeT, PropertyTypeT > {
   
   private Function< ObjectTypeT, ObjectProperty< PropertyTypeT > > propertyGetterFunction;
   private Map< ObjectTypeT, ChangeListener< PropertyTypeT > > propertyListeners;
   private List< JttChangeListener< ObjectTypeT, PropertyTypeT > > listeners;

   /**
    * Constructs a new {@link GlobalPropertyListenerImpl}.
    * @param databaseJobs the {@link ObservableList} of objects to monitor.
    * @param propertyGetterFunction the {@link Function} for getting the {@link ObjectProperty} being listened to. 
    */
   public GlobalPropertyListenerImpl( 
            ObservableList< ObjectTypeT > databaseJobs,
            Function< ObjectTypeT, ObjectProperty< PropertyTypeT > > propertyGetterFunction 
   ) {
      listeners = new ArrayList<>();
      this.propertyListeners = new HashMap<>();
      this.propertyGetterFunction = propertyGetterFunction;
      
      databaseJobs.addListener( new FunctionListChangeListenerImpl<>( 
               this::listenForJobPropertyChange,
               this::removeObjectListeners
      ) );
      databaseJobs.forEach( this::listenForJobPropertyChange );
   }//End Constructor
   
   /**
    * Method to attach a {@link ChangeListener} to the given object after retrieving its property.
    * @param object the object in question.
    */
   private void listenForJobPropertyChange( ObjectTypeT object ) {
      if ( propertyListeners.containsKey( object ) ) {
         return;
      }
      
      ChangeListener< PropertyTypeT > changeListener = ( source, old, updated ) -> {
         listeners.forEach( listener -> listener.changed( object, old, updated ) );
      };
      propertyListeners.put( object, changeListener );
      propertyGetterFunction.apply( object ).addListener( changeListener );
   }//End Method
   
   /**
    * Method to remove the listeners associated with the given object.
    * @param object the object in question.
    */
   private void removeObjectListeners( ObjectTypeT object ) {
      ChangeListener< PropertyTypeT > listener = propertyListeners.get( object );
      propertyGetterFunction.apply( object ).removeListener( listener );
   }//End Method
   
   /**
    * Method to add a {@link JttChangeListener} as a listener to the associated {@link ObjectProperty} change.
    * @param listener the {@link JttChangeListener} to add as a listener.
    */
   void addListener( JttChangeListener< ObjectTypeT, PropertyTypeT > listener ) {
      if ( listeners.contains( listener ) ) {
         return;
      }
      
      listeners.add( listener );
   }//End Method

   /**
    * Method to remove the given listener from this.
    * @param listener the {@link JttChangeListener} to remove.
    */
   void removeListener( JttChangeListener< ObjectTypeT, PropertyTypeT > listener ) {
      listeners.remove( listener );
   }//End Method

}//End Class
