/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.system;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import uk.dangrew.kode.javafx.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.friendly.javafx.FriendlyDesktop;
import uk.dangrew.jtt.desktop.versioning.Versioning;

/**
 * the {@link SystemVersionPanel} provides a simple panel that describes what the
 * version of the system is.
 */
public class SystemVersionPanel extends GridPane {
   
   static final String FIRST_SENTENCE = 
            "Jenkins Test Tracker: ";
   static final String SECOND_PARAGRAPH = 
            "Designed, developed and produced by Dan Grew. This project started as a way "
            + "to learn more about creating a product, about developing something with "
            + "good principles from it's first lines to full releases, automating as much "
            + "of the process as possible and understanding technical debt and how to manage "
            + "it.";
   static final String CHECK_FOR_UPDATES = "Check For Updates";
   static final String ICON_CREDIT_DESCRIPTION = "Icons kindly provided by:";
   
   static final String ICON_CREDIT = "https://icons8.com/";

   private final FriendlyDesktop desktop;
   private final Label firstSentence;
   private final Label firstGap;
   private final Label secondParagraph;
   private final Label secondGap;
   private final Button checkForUpdatesButton;
   private final Label thirdGap;
   private final Label thirdParagraph;
   private final Hyperlink iconCreditLink;
   
   /**
    * Constructs a new {@link SystemVersionPanel}.
    */
   public SystemVersionPanel(){
      this( new CheckForUpdates(), new FriendlyDesktop() );
   }//End Constructor
   
   /**
    * Constructs a new {@link SystemVersionPanel}.
    * @param updates the {@link CheckForUpdates} to use to find new releases.
    * @param desktop the {@link FriendlyDesktop} to browse to the link.
    */
   SystemVersionPanel( CheckForUpdates updates, FriendlyDesktop desktop ) {
      this.desktop = desktop;
      JavaFxStyle styling = new JavaFxStyle();
      Versioning versionig = new Versioning();
      
      add( 
               firstSentence = styling.createWrappedTextLabel( FIRST_SENTENCE + versionig.getVersionNumber() ), 
      0, 0 );
      
      add( 
               firstGap = new Label(), 
      0, 1 );
      
      add( 
               secondParagraph = styling.createWrappedTextLabel( SECOND_PARAGRAPH ), 
      0, 2 );
      
      add( 
               secondGap = new Label(), 
      0, 3 );
      
      checkForUpdatesButton = new Button( CHECK_FOR_UPDATES );
      checkForUpdatesButton.setMaxWidth( Double.MAX_VALUE );
      checkForUpdatesButton.setOnAction( event -> updates.checkForUpdates() );
      add( checkForUpdatesButton, 0, 4 );
      
      add( 
               thirdGap = new Label(), 
      0, 5 );
      
      add( 
               thirdParagraph = styling.createWrappedTextLabel( ICON_CREDIT_DESCRIPTION ), 
      0, 6 );
      
      iconCreditLink = new Hyperlink( ICON_CREDIT );
      iconCreditLink.setOnAction( event -> browseToIconWebsite() );
      iconCreditLink.setMaxWidth( Double.MAX_VALUE );
      iconCreditLink.setAlignment( Pos.CENTER );
      add( 
               iconCreditLink, 
      0, 7 );
   }//End Constructor
   
   /**
    * Method to browse to the icon website.
    */
   private void browseToIconWebsite(){
      try {
         desktop.getDesktop().browse( new URI( ICON_CREDIT ) );
      } catch ( IOException e ) {
         throw new IllegalStateException( "Unable to find default web browser, or not able to launch web browser." );
      } catch ( URISyntaxException e ) {
         throw new IllegalArgumentException( "Invalid website location provided." );
      }
   }//End Method
   
   Label firstSentence() {
      return firstSentence;
   }//End Method

   Label firstGap() {
      return firstGap;
   }//End Method
   
   Label secondParagraph() {
      return secondParagraph;
   }//End Method
   
   Label secondGap() {
      return secondGap;
   }//End Method
   
   Button checkForUpdatesButton(){
      return checkForUpdatesButton;
   }//End Method
   
   Label thirdParagraph() {
      return thirdParagraph;
   }//End Method
   
   Hyperlink iconCreditLink() {
      return iconCreditLink;
   }//End Method
   
   Label thirdGap() {
      return thirdGap;
   }//End Method
}//End Class
