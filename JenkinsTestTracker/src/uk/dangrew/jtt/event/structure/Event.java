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
 */
public class Event< SourceT, ValueT > {

   private final SourceT source;
   private final ValueT value;
   
   /**
    * Constructs a new {@link Event}.
    * @param source the source of the event.
    * @param value the new value associated with the type of event.
    */
   public Event( SourceT source, ValueT value ) {
      this.source = source;
      this.value = value;
   }//End Constructor
   
   /**
    * Getter for the source;
    * @return the source.
    */
   public SourceT getSource() {
      return source;
   }//End Method

   /**
    * Getter for the value.
    * @return the value.
    */
   public ValueT getValue() {
      return value;
   }//End Method
   
}//End Class
