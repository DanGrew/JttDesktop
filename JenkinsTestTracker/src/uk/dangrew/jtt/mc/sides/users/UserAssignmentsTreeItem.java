/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.users;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import uk.dangrew.jtt.javafx.tree.structure.TreeItemValue;
import uk.dangrew.jtt.model.users.JenkinsUser;

/**
 * The {@link UserAssignmentsTreeItem} provides an interface for all items within a
 * {@link UserAssignmentsTree}.
 */
public interface UserAssignmentsTreeItem extends TreeItemValue {

   /**
    * Getter for the {@link JenkinsUser} associated with this item.
    * @return the {@link JenkinsUser}.
    */
   public JenkinsUser getJenkinsUser();
   
   /**
    * Method to determine whether the given {@link UserAssignment} is associated with
    * this {@link UserAssignmentsTreeItem}.
    * @param assignment the {@link UserAssignment} in question.
    * @return true if associated.
    */
   public boolean isAssociatedWithUserAssignment( UserAssignment assignment );
   
   /**
    * {@link ObjectProperty} providing a {@link Node} to display in the tree.
    * @return the property for the first column.
    */
   public ObjectProperty< Node > firstColumnProperty();
   
   /**
    * {@link ObjectProperty} providing a {@link Node} to display in the tree.
    * @return the property for the second column.
    */
   public ObjectProperty< Node > secondColumnProperty();
   
   /**
    * {@link ObjectProperty} providing a {@link Node} to display in the detail of the tree.
    * @return the property for the detail area.
    */
   public ObjectProperty< Node > detailProperty();
   
   /**
    * Method to detach all listeners and registrations from the system.
    */
   public void detachFromSystem();
   
   /**
    * Method to determine whether the item is detached and holds no registrations.
    * @return true if holds no registrations.
    */
   public boolean isDetachedFromSystem();
}//End Interface
