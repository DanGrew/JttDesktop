/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration;

import buildwall.panel.type.JobPanelDescriptionProvider;
import buildwall.panel.type.JobPanelDescriptionProviders;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.jobs.JenkinsJob;

/**
 * The {@link BuildWallConfigurationImpl} provides an implementation of the {@link BuildWallConfiguration}.
 */
public class BuildWallConfigurationImpl implements BuildWallConfiguration {
   
   static final Font DEFAULT_PROPERTIES_FONT = new Font( 18 );
   static final Font DEFAULT_JOB_NAME_FONT = new Font( 30 );
   
   static final Color DEFAULT_TEXT_COLOUR = Color.WHITE;
   static final int DEFAULT_NUMBER_OF_COLUMNS = 2;
   
   private ObjectProperty< Font > buildNumberFont;
   private ObjectProperty< Color > buildNumberColour;
   
   private ObjectProperty< Font > completionEstimateFont;
   private ObjectProperty< Color > completionEstimateColour;
   
   private ObjectProperty< Font > jobNameFont;
   private ObjectProperty< Color > jobNameColour;
   
   private IntegerProperty numberOfColumns;
   
   private ObservableMap< JenkinsJob, BuildWallJobPolicy > jobPolicies;
   private ObjectProperty< JobPanelDescriptionProvider > jobPanelDescriptionProvider;
   
   private ObjectProperty< Font > culpritsFont;
   private ObjectProperty< Color > culpritsColour;
   
   /**
    * Constructs a new {@link BuildWallConfigurationImpl}.
    */
   public BuildWallConfigurationImpl() {
      buildNumberColour = new SimpleObjectProperty<>( DEFAULT_TEXT_COLOUR );
      buildNumberFont = new SimpleObjectProperty<>( DEFAULT_PROPERTIES_FONT );
      
      completionEstimateColour = new SimpleObjectProperty<>( DEFAULT_TEXT_COLOUR );
      completionEstimateFont = new SimpleObjectProperty<>( DEFAULT_PROPERTIES_FONT );
      
      jobNameColour = new SimpleObjectProperty<>( DEFAULT_TEXT_COLOUR );
      jobNameFont = new SimpleObjectProperty<>( DEFAULT_JOB_NAME_FONT );
      
      numberOfColumns = new SimpleIntegerProperty( DEFAULT_NUMBER_OF_COLUMNS );
      
      jobPolicies = FXCollections.observableHashMap();
      jobPanelDescriptionProvider = new SimpleObjectProperty<>( JobPanelDescriptionProviders.Default );
      
      culpritsColour = new SimpleObjectProperty<>( DEFAULT_TEXT_COLOUR );
      culpritsFont = new SimpleObjectProperty<>( DEFAULT_PROPERTIES_FONT );
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Font > jobNameFont() {
      return jobNameFont;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Color > jobNameColour() {
      return jobNameColour;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public IntegerProperty numberOfColumns() {
      return numberOfColumns;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Font > buildNumberFont() {
      return buildNumberFont;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Color > buildNumberColour() {
      return buildNumberColour;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Font > completionEstimateFont() {
      return completionEstimateFont;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Color > completionEstimateColour() {
      return completionEstimateColour;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObservableMap< JenkinsJob, BuildWallJobPolicy > jobPolicies() {
      return jobPolicies;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< JobPanelDescriptionProvider > jobPanelDescriptionProvider() {
      return jobPanelDescriptionProvider;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Font > culpritsFont() {
      return culpritsFont;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Color > culpritsColour() {
      return culpritsColour;
   }//End Method

}//End Class
