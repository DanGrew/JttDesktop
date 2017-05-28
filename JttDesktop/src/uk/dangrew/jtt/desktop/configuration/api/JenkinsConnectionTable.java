/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
 package uk.dangrew.jtt.desktop.configuration.api;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;

/**
 * The {@link JenkinsConnectionTable} provides a simple {@link TableView} that displays {@link uk.dangrew.jtt.connection.api.sources.JenkinsConnection}s.
 */
public class JenkinsConnectionTable extends TableView< JenkinsConnectionTableRow > {

   static final int LOCATION_PROPORTION_WIDTH = 2;
   static final int USER_PROPORTION_WIDTH = 3;
   static final int CONNECTED_PROPORTION_WIDTH = 6;
   
   static final String COLUMN_TITLE_LOCATION = "Location";
   static final String COLUMN_TITLE_USER = "User";
   static final String COLUMN_TITLE_CONNECTED = "On/Off";
   
   /**
    * Constructs a new {@link JenkinsConnectionTable}.
    */
   public JenkinsConnectionTable() {
      getSelectionModel().setSelectionMode( SelectionMode.SINGLE );
      initialiseColumns();
   }//End Constructor
   
   /**
    * Method to get the rows in the {@link JenkinsConnectionTable}. Should be used over {@link #getItems()} because
    * this method is tested.
    * @return the {@link ObservableList} of {@link JenkinsConnectionTableRow}, {@link #getItems()}.
    */
   public ObservableList< JenkinsConnectionTableRow > getRows(){
      return getItems();
   }//End Method
   
   /**
    * Method to initialise the {@link TableColumn}s and their associated constraints.
    */
   private void initialiseColumns(){
      TableColumn< JenkinsConnectionTableRow, String > locationColumn = new TableColumn<>( COLUMN_TITLE_LOCATION );
      locationColumn.prefWidthProperty().bind( widthProperty().divide( LOCATION_PROPORTION_WIDTH ) );
      locationColumn.setCellValueFactory( object -> new SimpleObjectProperty<>( object.getValue().getLocation() ) );
      getColumns().add( locationColumn );
      
      TableColumn< JenkinsConnectionTableRow, String > userColumn = new TableColumn<>( COLUMN_TITLE_USER );
      userColumn.prefWidthProperty().bind( widthProperty().divide( USER_PROPORTION_WIDTH ) );
      userColumn.setCellValueFactory( object -> new SimpleStringProperty( object.getValue().getUser() ) );
      getColumns().add( userColumn );
      
      TableColumn< JenkinsConnectionTableRow, Boolean > connectedColumn = new TableColumn<>( COLUMN_TITLE_CONNECTED );
      connectedColumn.prefWidthProperty().bind( widthProperty().divide( CONNECTED_PROPORTION_WIDTH ) );
      connectedColumn.setCellValueFactory( object -> object.getValue().connected() );
      connectedColumn.setCellFactory( tc -> new CheckBoxTableCell<>() );
      getColumns().add( connectedColumn );
   }//End Method

}//End Class
