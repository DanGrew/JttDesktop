/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.storage.structure;

import javafx.collections.ObservableList;

/**
 * The {@link ObjectStoreManager} provides an interface for a mangaer that holds
 * objects and provides access to them.
 * @param <KeyTypeT> the type of the key for referencing the object.
 * @param <ObjectTypeT> the type of object stored. 
 */
public interface ObjectStoreManager< KeyTypeT, ObjectTypeT > {

   /**
    * Determines whether there are any objects present in the manager.
    * @return true if none, false if any.
    */
   public boolean isEmpty();

   /**
    * Method to store the given object against its key.
    * @param key the key.
    * @param object the object to store.
    */
   public void store( KeyTypeT key, ObjectTypeT object );

   /**
    * Getter for the object associated with the given key.
    * @param key the key for the object.
    * @return the associated object, can be null.
    */
   public ObjectTypeT get( KeyTypeT key );

   /**
    * Method to determine if an object is held against the given key.
    * @param key the key referencing the object.
    * @return true if a mapping is held.
    */
   public boolean has( KeyTypeT key );

   /**
    * Method to remove the object associated with the given key.
    * @param key the key referencing the object.
    * @return the object removed, or null if no mapping for key.
    */
   public ObjectTypeT remove( KeyTypeT key );

   /**
    * Provides the {@link ObservableList} of objects held.
    * @return the {@link ObservableList}.
    */
   public ObservableList< ObjectTypeT > objectList();

}//End Interface
