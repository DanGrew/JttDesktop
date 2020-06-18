/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.DualWallConfiguration;
import uk.dangrew.kode.javafx.spinner.DoublePropertySpinner;
import uk.dangrew.kode.javafx.style.JavaFxStyle;

/**
 * The {@link DualPropertiesPanel} provides a {@link GridPane} for configuring the dual
 * wall properties of a {@link uk.dangrew.jtt.desktop.buildwall.dual.DualBuildWallDisplayImpl}.
 */
public class DualPropertiesPanel extends GridPane {
   
   static final double MINIMUM_POSITION = 0;
   static final double MAXIMUM_POSITION = 1;
   static final double POSITION_INTERVAL = 0.005;
   
   private final DualWallConfiguration configuration;
   
   private final Label positionLabel;
   private final DoublePropertySpinner positionSpinner;
   
   private final Label orientationLabel;
   private final RadioButton horizontalButton;
   private final RadioButton verticalButton;
   
   /**
    * Constructs a new {@link DualPropertiesPanel}.
    * @param configuration the {@link DualWallConfiguration} associated, to configure.
    */
   public DualPropertiesPanel( DualWallConfiguration configuration ) {
      this( configuration, new JavaFxStyle(), new ConfigurationPanelDefaults() );
   }//End Constructor
   
   /**
    * Constructs a new {@link DualPropertiesPanel}.
    * @param configuration the {@link DualWallConfiguration} associated, to configure.
    * @param styling the {@link JavaFxStyle} to apply.
    * @param defaults the {@link ConfigurationPanelDefaults}.
    */
   DualPropertiesPanel( DualWallConfiguration configuration, JavaFxStyle styling, ConfigurationPanelDefaults defaults ) {
      this.configuration = configuration;
      
      positionLabel = styling.createBoldLabel( "Divider Position" );
      add( positionLabel, 0, 0 );
      
      positionSpinner = new DoublePropertySpinner();
      styling.configureDoubleSpinner( 
               positionSpinner, 
               configuration.dividerPositionProperty(), 
               MINIMUM_POSITION, 
               MAXIMUM_POSITION, 
               POSITION_INTERVAL 
      );
      add( positionSpinner, 1, 0 );
      
      orientationLabel = styling.createBoldLabel( "Orientation" );
      add( orientationLabel, 0, 1 );
      
      horizontalButton = configureRadioButton( "Horizontal", Orientation.HORIZONTAL );
      add( horizontalButton, 1, 1 );
      
      verticalButton = configureRadioButton( "Vertical", Orientation.VERTICAL );
      add( verticalButton, 1, 2 );
      
      updateOrientationButton( configuration.dividerOrientationProperty().get() );
      configuration.dividerOrientationProperty().addListener( 
               ( source, old, updated ) -> updateOrientationButton( updated ) 
      );

      defaults.configureColumnConstraints( this );
   }//End Constructor
   
   /**
    * Method to configure the {@link RadioButton}s for this panel.
    * @param name the name on the button.
    * @param orientation the {@link Orientation} the button is for.
    * @return the {@link RadioButton} constructed.
    */
   private RadioButton configureRadioButton( String name, Orientation orientation ) {
      RadioButton button = new RadioButton( name );
      button.setOnAction( 
               event -> configuration.dividerOrientationProperty().set( orientation ) 
      );
      return button;
   }//End Method
   
   /**
    * Method to update the {@link RadioButton}s for the {@link Orientation}.
    * @param value the {@link Orientation} set in the {@link DualWallConfiguration}.
    */
   private void updateOrientationButton( Orientation value ){
      horizontalButton.setSelected( Orientation.HORIZONTAL.equals( value )  );
      verticalButton.setSelected( Orientation.VERTICAL.equals( value )  );
   }//End Method
   
   /**
    * Method to determine whether the given {@link DualWallConfiguration} is associated.
    * @param configuration the {@link DualWallConfiguration} in question.
    * @return true if associated.
    */
   public boolean hasConfiguration( DualWallConfiguration configuration ) {
      return this.configuration.equals( configuration );
   }//End Method

   Spinner< Double > positionSpinner() {
      return positionSpinner;
   }//End Method

   Label positionLabel() {
      return positionLabel;
   }//End Method

   RadioButton verticalButton() {
      return verticalButton;
   }//End Method

   RadioButton horizontalButton() {
      return horizontalButton;
   }//End Method
   
   Label orientationLabel() {
      return orientationLabel;
   }//End Method
}//End Class
