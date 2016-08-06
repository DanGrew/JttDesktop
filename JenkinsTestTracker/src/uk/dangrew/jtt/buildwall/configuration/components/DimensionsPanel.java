/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.components;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.buildwall.panel.type.JobPanelDescriptionProviders;
import uk.dangrew.jtt.javafx.spinner.IntegerPropertySpinner;

/**
 * The {@link DimensionsPanel} provides a {@link GridPane} for configuring the dimension
 * properties of a {@link uk.dangrew.jtt.buildwall.layout.GridWallImpl}.
 */
public class DimensionsPanel extends GridPane {
   
   static final int MINIMUM_COLUMNS = 1;
   static final int MAXIMUM_COLUMNS = 1000;
   
   private final BuildWallConfiguration configuration;
   
   private final Label columnsLabel;
   private final IntegerPropertySpinner columnsSpinner;
   
   private final Label descriptionTypeLabel;
   private final RadioButton simpleDescriptionButton;
   private final RadioButton defaultDescriptionButton;
   private final RadioButton detailedDescriptionButton;
   
   /**
    * Constructs a new {@link DimensionsPanel}.
    * @param configuration the {@link BuildWallConfiguration} associated, to configure.
    */
   public DimensionsPanel( BuildWallConfiguration configuration ) {
      this( configuration, new JavaFxStyle() );
   }//End Constructor
   
   /**
    * Constructs a new {@link DimensionsPanel}.
    * @param configuration the {@link BuildWallConfiguration} associated, to configure.
    * @param styling the {@link BuildWallConfigurationStyle} to apply.
    */
   DimensionsPanel( BuildWallConfiguration configuration, JavaFxStyle styling ) {
      this.configuration = configuration;
      
      columnsLabel = styling.createBoldLabel( "Columns" );
      add( columnsLabel, 0, 0 );
      
      columnsSpinner = new IntegerPropertySpinner();  
      styling.configureIntegerSpinner( columnsSpinner, configuration.numberOfColumns(), 1, 1000, 1 );
      add( columnsSpinner, 1, 0 );
      
      descriptionTypeLabel = styling.createBoldLabel( "Description Type" );
      add( descriptionTypeLabel, 0, 1 );
      
      simpleDescriptionButton = configureRadioButton( "Simple Description", JobPanelDescriptionProviders.Simple );
      add( simpleDescriptionButton, 1, 1 );
      
      defaultDescriptionButton = configureRadioButton( "Default Description", JobPanelDescriptionProviders.Default );
      add( defaultDescriptionButton, 1, 2 );
      
      detailedDescriptionButton = configureRadioButton( "Detailed Description", JobPanelDescriptionProviders.Detailed );
      add( detailedDescriptionButton, 1, 3 );
      
      updateDescriptionTypeButton( configuration.jobPanelDescriptionProvider().get() );
      configuration.jobPanelDescriptionProvider().addListener( 
               ( source, old, updated ) -> updateDescriptionTypeButton( updated ) 
      );

      styling.configureColumnConstraints( this );
   }//End Constructor
   
   /**
    * Method to configure the {@link RadioButton}s for this panel.
    * @param name the name on the button.
    * @param provider the {@link JobPanelDescriptionProviders} the button is for.
    * @return the {@link RadioButton} constructed.
    */
   private RadioButton configureRadioButton( String name, JobPanelDescriptionProviders provider ) {
      RadioButton descriptionButton = new RadioButton( name );
      descriptionButton.setOnAction( 
               event -> configuration.jobPanelDescriptionProvider().set( provider ) 
      );
      return descriptionButton;
   }//End Method
   
   
   /**
    * Method to update the {@link RadioButton}s for the {@link JobPanelDescriptionProviders}.
    * @param provider the {@link JobPanelDescriptionProviders} set in the {@link BuildWallConfiguration}.
    */
   private void updateDescriptionTypeButton( JobPanelDescriptionProviders provider ){
      simpleDescriptionButton.setSelected( JobPanelDescriptionProviders.Simple.equals( provider )  );
      defaultDescriptionButton.setSelected( JobPanelDescriptionProviders.Default.equals( provider )  );
      detailedDescriptionButton.setSelected( JobPanelDescriptionProviders.Detailed.equals( provider )  );
   }//End Method
   
   /**
    * Method to determine whether the given {@link BuildWallConfiguration} is associated.
    * @param configuration the {@link BuildWallConfiguration} in question.
    * @return true if associated.
    */
   public boolean hasConfiguration( BuildWallConfiguration configuration ) {
      return this.configuration.equals( configuration );
   }//End Method

   Spinner< Integer > columnsSpinner() {
      return columnsSpinner;
   }//End Method

   Label columnsSpinnerLabel() {
      return columnsLabel;
   }//End Method

   RadioButton simpleDescriptionButton() {
      return simpleDescriptionButton;
   }//End Method

   RadioButton defaultDescriptionButton() {
      return defaultDescriptionButton;
   }//End Method
   
   RadioButton detailedDescriptionButton() {
      return detailedDescriptionButton;
   }//End Method

   Label descriptionTypeLabel() {
      return descriptionTypeLabel;
   }//End Method
}//End Class
