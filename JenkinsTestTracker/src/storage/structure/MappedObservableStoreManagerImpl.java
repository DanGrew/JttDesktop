/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package storage.structure;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The {@link MappedObservableStoreManagerImpl} provides an implementation of the {@link ObjectStoreManager} that
 * provides a mapping for quick look up and an {@link ObservableList} for event management.
 */
public class MappedObservableStoreManagerImpl< KeyTypeT, ObjectTypeT > implements ObjectStoreManager< KeyTypeT, ObjectTypeT > {

   private Map< KeyTypeT, ObjectTypeT > objectMap;
   private ObservableList< ObjectTypeT > objectList;
   
   /**
    * Constructs a new {@link MappedObservableStoreManagerImpl}.
    */
   public MappedObservableStoreManagerImpl() {
      objectMap = new HashMap<>();
      objectList = FXCollections.observableArrayList();
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean isEmpty() {
      return objectMap.isEmpty();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void store( KeyTypeT key, ObjectTypeT object ) {
      if ( key == null ) return;

      if ( objectMap.containsKey( key ) ) {
         ObjectTypeT currentObject = objectMap.remove( key );
         objectList.remove( currentObject );
      }
      
      objectMap.put( key, object );
      objectList.add( object );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectTypeT get( KeyTypeT key ) {
      if ( key == null ) return null;
      
      return objectMap.get( key );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public boolean has( KeyTypeT key ) {
      if ( key == null ) return false;
      
      return objectMap.containsKey( key );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectTypeT remove( KeyTypeT key ) {
      if ( key == null ) return null;
      
      ObjectTypeT object = objectMap.remove( key );
      objectList.remove( object );
      return object;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObservableList< ObjectTypeT > objectList() {
      return objectList;
   }//End Method

}//End Class
