/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.api;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.connection.api.sources.JenkinsConnection;
import uk.dangrew.jtt.connection.login.JenkinsLoginDetails;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.configuration.api.ButtonConfigurer.StateTranslator;

/**
 * {@link JenkinsConnectionControls} provides controls for creating and changing {@link JenkinsConnection}s.
 */
public class JenkinsConnectionControls extends BorderPane {

   public static final String NEW_TEXT = "New";
   public static final String SAVE_TEXT = "Save";
   public static final String CANCEL_TEXT = "Cancel";
   public static final String CONNECTED_TEXT = "Connect";
   public static final String DISCONNECTED_TEXT = "Disconnect";
   public static final String FORGET_TEXT = "Forget";
   
   private final ApiConfigurationController controller;
   private final JenkinsLoginDetails details;
   private final StateTranslator stateTranslator;
   
   private final GridPane controls;
   private final Button firstButton;
   private final Button secondButton;
   private final Button thirdButton;
   
   private JenkinsConnection currentConnection;
   
   /**
    * Constructs a new {@link JenkinsConnectionControls}.
    * @param controller the {@link ApiConfigurationController}.
    */
   JenkinsConnectionControls( ApiConfigurationController controller ) {
      this( new JavaFxStyle(), new StateTranslator(), controller );
   }//End Constructor
   
   /**
    * Constructs a new {@link JenkinsConnectionControls}.
    * @param styling the {@link JavaFxStyle}.
    * @param stateTranslator the {@link uk.dangrew.jtt.desktop.configuration.api.ButtonConfigurer.StateTranslator}.
    * @param controller the {@link ApiConfigurationController}.
    */
   JenkinsConnectionControls( JavaFxStyle styling, StateTranslator stateTranslator, ApiConfigurationController controller ) {
      this.controller = controller;
      this.stateTranslator = stateTranslator;
      this.setCenter( details = new JenkinsLoginDetails() );
      
      this.controls = new GridPane();
      styling.configureConstraintsForEvenColumns( controls, 3 );
      
      controls.add( firstButton = new Button(), 0, 0 );
      controls.add( secondButton = new Button(), 1, 0 );
      controls.add( thirdButton = new Button(), 2, 0 );
      setBottom( controls );
      
      firstButton.setPrefWidth( Double.MAX_VALUE );
      secondButton.setPrefWidth( Double.MAX_VALUE );
      thirdButton.setPrefWidth( Double.MAX_VALUE );
      
      cancelNew();
   }//End Constructor
   
   /**
    * Method to apply the given {@link JenkinsControlButtonStates}.
    * @param state the {@link JenkinsControlButtonStates} to apply.
    */
   private void applyState( JenkinsControlButtonStates state ) {
      stateTranslator.apply( state ).configure( firstButton, secondButton, thirdButton, this );
   }//End Method
   
   /**
    * Method to configure all text fields disabled property.
    * @param enable whether enabled.
    */
   private void enabledTextFields( boolean enable ) {
      details.locationField().setDisable( !enable );
      details.usernameField().setDisable( !enable );
      details.passwordField().setDisable( !enable );
   }//End Method
   
   /**
    * Method to reset the text in the details based on the current {@link JenkinsConnection}.
    */
   private void resetText() {
      details.locationField().setText( currentConnection == null ? null : currentConnection.location() );
      details.usernameField().setText( currentConnection == null ? null : currentConnection.username() );
      details.passwordField().setText( currentConnection == null ? null : currentConnection.password() );
   }//End Method
   
   /**
    * Method to show the given {@link JenkinsConnection} in the controls.
    * @param connection the {@link JenkinsConnection} selected.
    * @param connected whether it is currently connected or not.
    */
   void showConnection( JenkinsConnection connection, boolean connected ) {
      currentConnection = connection;
      
      enabledTextFields( false );
      resetText();
      if ( connected ) {
         applyState( JenkinsControlButtonStates.SelectedConnected );
      } else {
         applyState( JenkinsControlButtonStates.SelectedDisconnected );
      }
   }//End Method

   /**
    * Method to provide controls for creating a new {@link JenkinsConnection}.
    */
   void createNew() {
      currentConnection = null;
      controller.clearSelection();
      
      enabledTextFields( true );
      applyState( JenkinsControlButtonStates.EnteringNew );
   }//End Method

   /**
    * Method to submit the entered data and provide resulting controls.
    */
   void saveConnection() {
      currentConnection = controller.connect( 
               details.locationField().getText(),
               details.usernameField().getText(),
               details.passwordField().getText()
      );
      if ( currentConnection == null ) {
         enabledTextFields( true );
         return;
      }
      enabledTextFields( false );
      applyState( JenkinsControlButtonStates.SelectedDisconnected );
   }//End Method

   /**
    * Method to cancel the current input and reset controls.
    */
   void cancelNew() {
      enabledTextFields( false );
      applyState( JenkinsControlButtonStates.NoSelection );
      resetText();
   }//End Method

   /**
    * Method to connect the given {@link JenkinsConnection} and provide resulting controls.
    */
   void connect() {
      enabledTextFields( false );
      applyState( JenkinsControlButtonStates.SelectedConnected );
      controller.connect( currentConnection );
   }//End Method

   /**
    * Method to disconnect the given {@link JenkinsConnection} and provide resulting controls.
    */
   void disconnect() {
      enabledTextFields( false );
      applyState( JenkinsControlButtonStates.SelectedDisconnected );
      controller.disconnect( currentConnection );
   }//End Method

   /**
    * Method to forget the given {@link JenkinsConnection} and provide resulting controls.
    */
   void forget() {
      enabledTextFields( false );
      applyState( JenkinsControlButtonStates.NoSelection );
      
      controller.forget( currentConnection );
      currentConnection = null;
      resetText();
   }//End Method
   
   JenkinsLoginDetails details(){
      return details;
   }//End Method
   
   GridPane controls(){
      return controls;
   }//End Method
   
   Button firstButton(){
      return firstButton;
   }//End Method
   
   Button secondButton(){
      return secondButton;
   }//End Method
   
   Button thirdButton(){
      return thirdButton;
   }//End Method
   
}//End Class
