/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.friendly.controlsfx;

import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import javafx.stage.Modality;

/**
 * The {@link FriendlyAlert} provides a friendly interface for testing on 
 * top of the {@link Alert} which is practically untestable. It has been decided
 * that this approach it better than using something like PowerMock.
 */
public class FriendlyAlert extends Alert {

   /**
    * Constructs a new {@link FriendlyAlert}.
    * @param alertType the {@link uk.dangrew.jtt.javafx.scene.control.Alert.AlertType}.
    */
   public FriendlyAlert( AlertType alertType ) {
      super( alertType );
   }//End Class
   
   /**
    * {@link Alert#setAlertType(AlertType)}.
    * @param type the {@link uk.dangrew.jtt.javafx.scene.control.Alert.AlertType}.
    */
   public void friendly_setAlertType( AlertType type ){
      setAlertType( type );
   }//End Method

   /**
    * {@link Alert#getButtonTypes()}.
    * @return the {@link ObservableList}.
    */
   public ObservableList< ButtonType > friendly_getButtonTypes() {
      return getButtonTypes();
   }//End Method

   /**
    * {@link Alert#getDialogPane()}{@link DialogPane#setContent(Node)}.
    * @param content the {@link Node}.
    */
   public void friendly_dialogSetContent( Node content ) {
      getDialogPane().setContent( content );
   }//End Method
   
   /**
    * {@link Alert#getDialogPane()}{@link DialogPane#lookupButton(ButtonType)}.
    * @param buttonType the {@link ButtonType} to look up.
    * @return the {@link Node} found.
    */
   public Node friendly_dialogLookup( ButtonType buttonType ) {
      return getDialogPane().lookupButton( buttonType );
   }//End Method
   
   /**
    * {@link Alert#setOnCloseRequest(EventHandler)}.
    * @param handler the {@link EventHandler}.
    */
   public void friendly_setOnCloseRequest( EventHandler< DialogEvent > handler ) {
      setOnCloseRequest( handler );
   }//End Method

   /**
    * {@link Alert#setTitle(String)}.
    * @param title the title.
    */
   public void friendly_setTitle( String title ) {
      setTitle( title );
   }//End Method

   /**
    * {@link Alert#setHeaderText(String)}.
    * @param headerText the header text.
    */
   public void friendly_setHeaderText( String headerText ) {
      setHeaderText( headerText );
   }//End Method

   /**
    * {@link Alert#initModality(Modality)}.
    * @param modality the {@link Modality}
    */
   public void friendly_initModality( Modality modality ) {
      initModality( modality );
   }//End Method

   /**
    * {@link Alert#getResult()}.
    * @return the {@link ButtonType}.
    */
   public ButtonType friendly_getResult() {
      return getResult();
   }//End Method

   /**
    * {@link Alert#showAndWait()}.
    * @return the {@link Optional} {@link ButtonType}.
    */
   public Optional< ButtonType > friendly_showAndWait() {
      return showAndWait();
   }//End Method
   
   /**
    * {@link Alert#close()}.
    */
   public void friendly_close(){
      close();
   }//End Method
   
   /** Method to indicate the end of a piece of processing related to splitting threads.**/
   public void friendly_separateThreadProcessingComplete(){}

}//End Class
