/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.effects.sound;

import uk.dangrew.jtt.model.jobs.BuildResultStatus;

/**
 * The {@link BuildResultStatusChange} describes a change of {@link BuildResultStatus}.
 */
public class BuildResultStatusChange {

   private final BuildResultStatus previous;
   private final BuildResultStatus current;
   
   /**
    * Constructs a new {@link BuildResultStatusChange}.
    * @param previous the previous {@link BuildResultStatus}.
    * @param current the current {@link BuildResultStatus}.
    */
   public BuildResultStatusChange( BuildResultStatus previous, BuildResultStatus current ) {
      if ( previous == null || current == null ) {
         throw new IllegalArgumentException( "Must provide non null parameters." );
      }
      this.previous = previous;
      this.current = current;
   }//End Constructor

   /**
    * Getter for the previous {@link BuildResultStatus}.
    * @return the {@link BuildResultStatus}.
    */
   public BuildResultStatus getPreviousStatus() {
      return previous;
   }//End Method

   /**
    * Getter for the current {@link BuildResultStatus}.
    * @return the {@link BuildResultStatus}.
    */
   public BuildResultStatus getCurrentStatus() {
      return current;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ( ( current == null ) ? 0 : current.hashCode() );
      result = prime * result + ( ( previous == null ) ? 0 : previous.hashCode() );
      return result;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public boolean equals( Object obj ) {
      if ( this == obj ) {
         return true;
      }
      if ( obj == null ) {
         return false;
      }
      if ( getClass() != obj.getClass() ) {
         return false;
      }
      BuildResultStatusChange other = ( BuildResultStatusChange ) obj;
      if ( current != other.current ) {
         return false;
      }
      if ( previous != other.previous ) {
         return false;
      }
      return true;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public String toString() {
      return previous.name() + " -> " + current.name();
   }//End Method
   
}//End Class
