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

/**
 * The {@link JenkinsControlButtonStates} provides {@link ButtonConfigurer}s for the different
 * types of control states of the {@link JenkinsConnectionControls}.
 */
enum JenkinsControlButtonStates implements ButtonConfigurer {
   
   NoSelection( ( a, b, c, d ) -> {
      a.setText( JenkinsConnectionControls.NEW_TEXT );
      b.setText( JenkinsConnectionControls.SAVE_TEXT );
      c.setText( JenkinsConnectionControls.CANCEL_TEXT );
      
      a.setDisable( false );
      b.setDisable( true );
      c.setDisable( true );
      
      a.setOnAction( e -> d.createNew() );
      b.setOnAction( null );
      c.setOnAction( null );
   } ),
   EnteringNew( ( a, b, c, d ) -> {
      a.setText( JenkinsConnectionControls.NEW_TEXT );
      b.setText( JenkinsConnectionControls.SAVE_TEXT );
      c.setText( JenkinsConnectionControls.CANCEL_TEXT );
      
      a.setDisable( true );
      b.setDisable( false );
      c.setDisable( false );
      
      a.setOnAction( null );
      b.setOnAction( e -> d.saveConnection() );
      c.setOnAction( e -> d.cancelNew() );
   } ),
   SelectedDisconnected( ( a, b, c, d ) -> {
      a.setText( JenkinsConnectionControls.NEW_TEXT );
      b.setText( JenkinsConnectionControls.CONNECTED_TEXT );
      c.setText( JenkinsConnectionControls.FORGET_TEXT );
      
      a.setDisable( false );
      b.setDisable( false );
      c.setDisable( false );
      
      a.setOnAction( e -> d.createNew() );
      b.setOnAction( e -> d.connect() );
      c.setOnAction( e -> d.forget() );
   } ),
   SelectedConnected( ( a, b, c, d ) -> {
      a.setText( JenkinsConnectionControls.NEW_TEXT );
      b.setText( JenkinsConnectionControls.DISCONNECTED_TEXT );
      c.setText( JenkinsConnectionControls.FORGET_TEXT );
      
      a.setDisable( false );
      b.setDisable( false );
      c.setDisable( true );
      
      a.setOnAction( e -> d.createNew() );
      b.setOnAction( e -> d.disconnect() );
      c.setOnAction( null );
   } );
   
   private final ButtonConfigurer configuration;
   
   /**
    * Constructs a new {@link JenkinsControlButtonStates}.
    * @param configuration the {@link ButtonConfigurer}.
    */
   private JenkinsControlButtonStates( ButtonConfigurer configuration ) {
      this.configuration = configuration;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void configure( Button first, Button second, Button third, JenkinsConnectionControls controls ) {
      configuration.configure( first, second, third, controls );
   }//End Method
   
}//End Enum
