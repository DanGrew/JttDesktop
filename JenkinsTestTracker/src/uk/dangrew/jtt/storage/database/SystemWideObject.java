/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.storage.database;

/**
 * The {@link SystemWideObject} provides a wrapper for an object that can be used to provide
 * a system wide resource to be assumed conceptually static.
 * @param <ObjectTypeT> the type of the sytem wide object.
 */
public class SystemWideObject< ObjectTypeT > {

   private final ObjectTypeT systemObject;
   
   /**
    * Constructs a new {@link SystemWideObject}.
    * @param object the system wide object.
    */
   protected SystemWideObject( ObjectTypeT object ) {
      this.systemObject = object;
   }//End Constructor
   
   /**
    * Access to the system wide object.
    * @return the system wide object.
    */
   public ObjectTypeT get() {
      return systemObject;
   }//End Method

}//End Class
