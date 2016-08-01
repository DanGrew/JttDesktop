/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.javafx.tree;

import javafx.scene.control.TreeTableView;
import javafx.scene.layout.Pane;

/**
 * The {@link ColumnHeaderHider} is responsible for hiding the column headers on {@link TreeTableView}s.
 */
public class ColumnHeaderHider {
   
   static final double ZERO_DIMENSION = 0.0;
   static final String TABLE_HEADER_ROW = "TableHeaderRow";

   /**
    * Method to hide the {@link TreeTableView} column headers when the width changes.
    * @param tableView the {@link TreeTableView} to hide on.
    */
   public void hideColumnHeaders( TreeTableView< ? > tableView ){
      tableView.widthProperty().addListener( ( source, old, updated ) -> {
         Pane header = ( Pane ) tableView.lookup( TABLE_HEADER_ROW );
         if ( header != null && header.isVisible() ) {
            header.setMaxHeight( ZERO_DIMENSION );
            header.setMinHeight( ZERO_DIMENSION );
            header.setPrefHeight( ZERO_DIMENSION );
            header.setVisible( false );
            header.setManaged( false );
         }
      } );
   }//End Method

}//End Class
