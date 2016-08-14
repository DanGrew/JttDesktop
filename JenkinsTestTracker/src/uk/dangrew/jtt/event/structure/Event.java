/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.event.structure;

/**
 * An {@link Event} represents a single value change from a single source.
 * @param <ValueT> the type of object associated with the event.
 */
public class Event< ValueT > {

   private final ValueT value;
   
   /**
    * Constructs a new {@link Event}.
    * @param value the new value associated with the type of event.
    */
   public Event( ValueT value ) {
      this.value = value;
   }//End Constructor
   
   /**
    * Getter for the value.
    * @return the value.
    */
   public ValueT getValue() {
      return value;
   }//End Method
   
}//End Class
