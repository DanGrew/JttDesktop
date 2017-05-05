/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.storage.database.events;

/**
 * The {@link JttChangeListener} provides a custom {@link javafx.beans.value.ChangeListener}
 * for {@link GlobalPropertyListenerImpl}.
 * @param <ObjectTypeT> the source object type.
 * @param <PropertyTypeT> the property value type.
 */
@FunctionalInterface
public interface JttChangeListener< ObjectTypeT, PropertyTypeT > {
   
   /**
    * Method to be trigger when the property has changed.
    * @param source the source of the event.
    * @param old the old value.
    * @param updated the new value.
    */
   public void changed( ObjectTypeT source, PropertyTypeT old, PropertyTypeT updated );

}//End Interface
