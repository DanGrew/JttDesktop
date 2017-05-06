/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.environment.preferences;

import uk.dangrew.jtt.desktop.configuration.tree.ConfigurationTreeItems;

/**
 * {@link PreferenceBehaviour} defines the parameters for the behavioural response of
 * the {@link PreferenceController} to the event.
 */
public class PreferenceBehaviour {

   private final WindowPolicy policy;
   private final ConfigurationTreeItems selection;
   
   /**
    * Constructs a new {@link PreferenceBehaviour}.
    * @param policy the {@link WindowPolicy} to perform.
    * @param selection the {@link ConfigurationTreeItems} to select, can be null.
    */
   public PreferenceBehaviour( WindowPolicy policy, ConfigurationTreeItems selection ) {
      if ( policy == null ) {
         throw new IllegalArgumentException( "Must not provide null WindowPolicy." );
      }
      this.policy = policy;
      this.selection = selection;
   }//End Constructor

   /**
    * Getter for the {@link WindowPolicy} to perform.
    * @return the {@link WindowPolicy}.
    */
   public WindowPolicy getWindowPolicy() {
      return policy;
   }//End Method

   /**
    * Getter for the selection requested.
    * @return the {@link ConfigurationTreeItems} selection, can be null.
    */
   public ConfigurationTreeItems getSelection() {
      return selection;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ( ( policy == null ) ? 0 : policy.hashCode() );
      result = prime * result + ( ( selection == null ) ? 0 : selection.hashCode() );
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
      if ( !( obj instanceof PreferenceBehaviour ) ) {
         return false;
      }
      PreferenceBehaviour other = ( PreferenceBehaviour ) obj;
      if ( policy != other.policy ) {
         return false;
      }
      if ( selection != other.selection ) {
         return false;
      }
      return true;
   }//End Method
   
}//End Class
