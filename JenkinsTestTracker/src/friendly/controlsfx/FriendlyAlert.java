/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package friendly.controlsfx;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;

/**
 * The {@link FriendlyAlert} provides a friendly interface for testing on 
 * top of the {@link Alert} which is practically untestable. It has been decided
 * that this approach it better than using something like PowerMock.
 */
public class FriendlyAlert extends Alert {

   /**
    * Constructs a new {@link FriendlyAlert}.
    * @param alertType the {@link AlertType}.
    */
   public FriendlyAlert( AlertType alertType ) {
      super( alertType );
   }//End Class
   
   /**
    * {@link Alert#setAlertType(AlertType)}.
    */
   public void friendly_setAlertType( AlertType type ){
      setAlertType( type );
   }//End Method

   /**
    * {@link Alert#getButtonTypes()}.
    */
   public ObservableList< ButtonType > friendly_getButtonTypes() {
      return getButtonTypes();
   }//End Method

   /**
    * {@link Alert#getDialogPane()}{@link DialogPane#setContent()}.
    */
   public void friendly_dialogSetContent( Node content ) {
      getDialogPane().setContent( content );
   }//End Method
   
   /**
    * {@link Alert#setOnCloseRequest(EventHandler)}.
    */
   public void friendly_setOnCloseRequest( EventHandler< DialogEvent > handler ) {
      setOnCloseRequest( handler );
   }//End Method

   /**
    * {@link Alert#setTitle(String)}.
    */
   public void friendly_setTitle( String title ) {
      setTitle( title );
   }//End Method

   /**
    * {@link Alert#setHeaderText(String)}.
    */
   public void friendly_setHeaderText( String headerText ) {
      setHeaderText( headerText );
   }//End Method

   /**
    * {@link Alert#initModality(Modality)}.
    */
   public void friendly_initModality( Modality modality ) {
      initModality( modality );
   }//End Method

   /**
    * {@link Alert#getResult()}.
    */
   public ButtonType friendly_getResult() {
      return getResult();
   }//End Method

}//End Class
