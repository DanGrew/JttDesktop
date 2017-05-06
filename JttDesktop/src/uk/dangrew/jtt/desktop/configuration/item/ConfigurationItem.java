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
 * The {@link ConfigurationItem} describes an item in the {@link uk.dangrew.jtt.configuration.tree.ConfigurationTree}
 * describing an item or collection of items that can be configured. 
 */
public interface ConfigurationItem {

   /**
    * General purpose method for checking the associations of this {@link ConfigurationItem} with other {@link Object}s.
    * @param object the {@link Object} to check for association.
    * @return true if associated.
    */
   public boolean isAssociatedWith( Object object );
   
   /**
    * Method to call when the item in the {@link uk.dangrew.jtt.configuration.tree.ConfigurationTree} has been clicked
    * or selected.
    */
   public void handleBeingSelected();

}//End Interface
