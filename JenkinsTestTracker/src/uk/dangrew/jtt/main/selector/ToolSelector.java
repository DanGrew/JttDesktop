/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.main.selector;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import uk.dangrew.jtt.friendly.controlsfx.FriendlyAlert;

/**
 * {@link ToolSelector} is responsible for giving the user to option to select which tool
 * to run.
 */
public class ToolSelector {
   
   static final String HEADER_TEXT = "Please select the tool you wish to launch. \n"
                                   + "Future tools and configuration may appear here, \n"
                                   + "for now, lets keep it simple!";
   static final String TITLE = "Tool Selector";
   private ButtonType launch;
   private GridPane content;
   private ComboBox< Tools > toolsChoices;
   
   /**
    * Constructs a new {@link ToolSelector}.
    */
   public ToolSelector() {
      launch = new ButtonType( "Launch" );
      initialiseContent();
   }//End Constructor
   
   /**
    * Method to configure the {@link FriendlyAlert} to display the choices.
    * @param alert the {@link FriendlyAlert} to display.
    */
   public void configureAlert( FriendlyAlert alert ) {
      alert.friendly_setAlertType( Alert.AlertType.NONE );
      alert.friendly_setTitle( TITLE );
      alert.friendly_setHeaderText( HEADER_TEXT );
      alert.friendly_initModality( Modality.NONE );
      alert.friendly_getButtonTypes().setAll( launch );
      alert.friendly_dialogSetContent( content );
   }//End Method
   
   /**
    * Method to initialise the content of the {@link Alert}.
    */
   private void initialiseContent(){
      content = new GridPane();

      toolsChoices = new ComboBox<>();
      toolsChoices.getItems().addAll( Tools.values() );
      toolsChoices.getSelectionModel().select( Tools.BuildWall );
      toolsChoices.setMaxWidth( Double.MAX_VALUE );
      
      content.add( toolsChoices, 0, 0 );
      GridPane.setHgrow( toolsChoices, Priority.ALWAYS );
      GridPane.setHgrow( content, Priority.ALWAYS );
   }//End Method
   
   /**
    * Getter for the selected tool.
    * @return the {@link Tools}.
    */
   public Tools getSelectedTool() {
      return toolsChoices.getValue();
   }//End Method

   /**
    * Method to determine whether the given {@link ButtonType} indicates the launching
    * of the application.
    * @param button the {@link ButtonType} in question.
    * @return true if it is the launch button.
    */
   public boolean isLaunchResult( ButtonType button ) {
      if ( button == null ) return false;
      return button.equals( launch );
   }//End Method
   
   /**
    * Method to select the given {@link Tools} programmatically.
    * @param choice the {@link Tools} chosen.
    */
   public void select( Tools choice ) {
      toolsChoices.getSelectionModel().select( choice );
   }//End Method
   
   /**
    * Getter for the {@link ButtonType} for the launch button.
    * @return the {@link ButtonType}.
    */
   ButtonType launchButton() {
      return launch;
   }//End Method

   /**
    * Getter for the {@link ComboBox} of choices.
    * @return the {@link ComboBox} of {@link Tools}.
    */
   ComboBox< Tools > toolsChoices() {
      return toolsChoices;
   }//End Method

   /**
    * Getter for the content of the alert.
    * @return the {@link GridPane} content.
    */
   GridPane content() {
      return content;
   }//End Method

}//End Class
