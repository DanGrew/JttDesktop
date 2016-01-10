/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package view.table;

import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.TreeTableView;

/**
 * The {@link TestTableItem} provides an interface for defining how describers of rows
 * in a {@link TreeTableView} should provide cell entries.
 */
public interface TestTableItem {

   /**
    * Getter for the associated object with the item.
    * @return the association.
    */
   public Object getSubject();

   /**
    * Getter for the {@link StringProperty} associated with the given column reference
    * index. This property will be used to keep the cells up to date.
    * @param columnReference the index of the column in the {@link TreeTableView}.
    * @return the {@link StringProperty} being updated with the cell value.
    */
   public StringProperty getColumnProperty( int columnReference );

   /**
    * Getter for the {@link Node} graphic describing the status visually.
    * @return the {@link Node} representing the status.
    */
   public Node getStatusGraphic();

}//End Interface
