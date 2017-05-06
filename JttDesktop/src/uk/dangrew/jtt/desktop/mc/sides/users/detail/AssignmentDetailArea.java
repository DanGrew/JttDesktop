/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.sides.users.detail;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import uk.dangrew.jtt.desktop.mc.resources.ManagementConsoleImages;
import uk.dangrew.jtt.desktop.mc.sides.users.UserAssignment;
import uk.dangrew.jtt.desktop.mc.view.ManagementConsoleStyle;

/**
 * The {@link AssignmentDetailArea} provides a {@link TextArea} for editting the detail
 * of a {@link UserAssignment}.
 */
public class AssignmentDetailArea extends BorderPane {
   
   static final String NOT_SELECTED = "Select a single assignment to view the detail associated.";
   static final String LOCK_TOOL_TIP = "Unlock to edit, lock to save.";
   
   private final ManagementConsoleStyle styling;
   private final ManagementConsoleImages images;
   
   private final TextArea detailArea;
   private final HBox toolBox;
   private final ToggleButton lockButton;
   
   private UserAssignment assignment;
   private ChangeListener< String > detailListener;
   
   /**
    * Constructs a new {@link AssignmentDetailArea}.
    */
   public AssignmentDetailArea(){
      this( new ManagementConsoleStyle(), new ManagementConsoleImages() );
   }//End Constructor
   
   /**
    * Constructs a new {@link AssignmentDetailArea}.
    * @param styling the associated {@link ManagementConsoleStyle}.
    * @param images the {@link ManagementConsoleImages}.
    */
   AssignmentDetailArea( ManagementConsoleStyle styling, ManagementConsoleImages images ) {
      this.styling = styling;
      this.images = images;
      
      this.detailArea = new TextArea( NOT_SELECTED );
      setCenter( detailArea );
      
      this.lockButton = new ToggleButton();
      this.lockButton.setTooltip( new Tooltip( LOCK_TOOL_TIP ) );
      
      this.toolBox = new HBox();
      this.toolBox.setPadding( new Insets( 2 ) );
      this.toolBox.setAlignment( Pos.CENTER_RIGHT );
      this.toolBox.getChildren().add( lockButton );
      setTop( toolBox );
      this.styling.styleButtonSize( lockButton, images.constuctLockImage() );
      
      detailListener = this::detailChanged;
      
      lockButton.selectedProperty().addListener( this::handleLocking );
      lockButton.setSelected( true );
      lockButton.setDisable( true );
   }//End Constructor
   
   /**
    * Method to set the current {@link UserAssignment} being displayed.
    * @param assignment the {@link UserAssignment} to set and replace the previous.
    */
   public void setAssignment( UserAssignment assignment ) {
      if ( this.assignment != null ) {
         this.assignment.detailProperty().removeListener( detailListener );
      }
      
      this.assignment = assignment;
      if ( assignment == null ) {
         detailArea.setText( NOT_SELECTED );
         lockButton.setDisable( true );
      } else {
         assignment.detailProperty().addListener( detailListener );
         detailArea.setText( assignment.detailProperty().get() );
         lockButton.setDisable( false );
      }
      
      lockButton.setSelected( true );
   }//End Method
   
   /**
    * Method to handle the locking of the area given the change in the lock button selection.
    * @param property the {@link ObservableValue} changed, the selection of the lock button.
    * @param old the old value.
    * @param locked the new value of the lock status.
    */
   private void handleLocking( ObservableValue< ? extends Boolean > property, Boolean old, Boolean locked ) {
      detailArea.setEditable( !locked );
      
      if ( assignment == null ) {
         return;
      }
      
      if ( locked ) {
         assignment.detailProperty().set( detailArea.getText() );
      }
   }//End Method
   
   /**
    * Method to handle the changing of the detail associated with the {@link UserAssignment}.
    * @param property the {@link ObservableValue} changed, the detail property.
    * @param old the old detail.
    * @param updated the updated detail.
    */
   private void detailChanged( ObservableValue< ? extends String > property, String old, String updated ) {
      if ( lockButton.isSelected() ) {
         detailArea.setText( updated );
      }
   }//End Method

   TextArea detailArea() {
      return detailArea;
   }//End Method

   ToggleButton lockButton() {
      return lockButton;
   }//End Method

   Pane toolBox() {
      return toolBox;
   }//End Method

}//End Class
