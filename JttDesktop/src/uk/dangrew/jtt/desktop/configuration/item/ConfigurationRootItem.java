/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.item;

/**
 * The {@link ConfigurationRootItem} provides a basic implementation
 * that can be used for the root of a {@link javafx.scene.control.TreeView}.
 */
public class ConfigurationRootItem implements ConfigurationItem {

   /**
    * {@inheritDoc}
    */
   @Override public void handleBeingSelected() {
      //do nothing
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean isAssociatedWith( Object object ) {
      return false;
   }//End Method

}//End Class
