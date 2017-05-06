/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.components.sound;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.buildwall.configuration.components.sound.applier.FailFailApplier;
import uk.dangrew.jtt.buildwall.configuration.components.sound.applier.FailPassApplier;
import uk.dangrew.jtt.buildwall.configuration.components.sound.applier.IndividualChangeApplier;
import uk.dangrew.jtt.buildwall.configuration.components.sound.applier.PassFailApplier;
import uk.dangrew.jtt.buildwall.configuration.components.sound.applier.PassPassApplier;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.buildwall.effects.sound.BuildResultStatusChange;
import uk.dangrew.jtt.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.utility.observable.FunctionMapChangeListenerImpl;

/**
 * The {@link SoundConfigurationPanel} provides a configuration panel for the {@link SoundConfiguration}
 * so that the user can associated sounds with triggers.
 */
public class SoundConfigurationPanel extends GridPane {

   static final String FAIL_FAIL_TEXT = "Fail -> Fail";
   static final String PASS_PASS_TEXT = "Pass -> Pass";
   static final String FAIL_PASS_TEXT = "Fail -> Pass";
   static final String PASS_FAIL_TEXT = "Pass -> Fail";
   static final String SIMPLE_TEXT = "Simple";
   static final String ADVANCED_TEXT = "Advanced";

   static final double PADDING = 10;
   
   private final SoundConfiguration configuration;
   private final JavaFxStyle styling;
   
   private TitledPane simplePane;
   private TitledPane advancedPane;
   
   private SoundConfigurationRow passFailRow;
   private SoundConfigurationRow passPassRow;
   private SoundConfigurationRow faillFailRow;
   private SoundConfigurationRow failPassRow;
   private final Map< BuildResultStatusChange, SoundConfigurationRow > rows;
   
   /**
    * Constructs a new {@link SoundConfigurationPanel}.
    * @param configuration the {@link SoundConfiguration} to configure.
    */
   public SoundConfigurationPanel( SoundConfiguration configuration ) {
      this( new JavaFxStyle(), configuration );
   }//End Constructor

   /**
    * Constructs a new {@link SoundConfigurationPanel}.
    * @param styling the {@link JavaFxStyle}.
    * @param configuration the {@link SoundConfiguration} to configure.
    */
   SoundConfigurationPanel( JavaFxStyle styling, SoundConfiguration configuration ) {
      this.styling = styling;
      this.configuration = configuration;
      this.rows = new HashMap<>();
      
      this.configuration.statusChangeSounds().addListener( new FunctionMapChangeListenerImpl<>( 
               configuration.statusChangeSounds(), 
               ( k, v ) -> rows.get( k ).updateSelectedFile( v ), 
               ( k, v ) -> rows.get( k ).updateSelectedFile( null ) 
      ) );
      
      setPadding( new Insets( 10 ) );
      styling.configureFullWidthConstraints( this );
      
      constructSimplePane();
      constructAdvancedPane();
      
      this.configuration.statusChangeSounds().forEach( ( c, f ) -> rows.get( c ).updateSelectedFile( f ) );
   }//End Constructor
      
   /**
    * Method to construct the simple configuration options.
    */
   private void constructSimplePane(){
      GridPane simpleNode = new GridPane();
      styling.configureFullWidthConstraints( simpleNode );
      simplePane = new TitledPane( SIMPLE_TEXT, simpleNode );
      add( simplePane, 0, 0 );
      
      passFailRow = new SoundConfigurationRow( PASS_FAIL_TEXT, new PassFailApplier( configuration ) );
      simpleNode.add( passFailRow, 0, 0 );
      failPassRow = new SoundConfigurationRow( FAIL_PASS_TEXT, new FailPassApplier( configuration ) );
      simpleNode.add( failPassRow, 0, 1 );
      passPassRow = new SoundConfigurationRow( PASS_PASS_TEXT, new PassPassApplier( configuration ) );
      simpleNode.add( passPassRow, 0, 2 );
      faillFailRow = new SoundConfigurationRow( FAIL_FAIL_TEXT, new FailFailApplier( configuration ) );
      simpleNode.add( faillFailRow, 0, 3 );
   }//End Method
   
   /**
    * Method to construct the advanced configuration options.
    */
   private void constructAdvancedPane(){
      GridPane advancedNode = new GridPane();
      styling.configureFullWidthConstraints( advancedNode );
      advancedPane = new TitledPane( ADVANCED_TEXT, advancedNode );
      add( advancedPane, 0, 1 );

      int rowNumber = 0;
      for ( BuildResultStatus from : BuildResultStatus.values() ) {
         for ( BuildResultStatus to : BuildResultStatus.values() ) {
            SoundConfigurationRow row = new SoundConfigurationRow( from.name() + " -> " + to.name(), new IndividualChangeApplier( configuration, from, to ) );
            rowNumber++;
            
            advancedNode.add( row, 0, rowNumber );
            rows.put( new BuildResultStatusChange( from, to ), row );
         }
      }
   }//End Method
   
   /**
    * Method to determine whether the given is associated.
    * @param configuration the {@link SoundConfiguration} in question.
    * @return true if identical.
    */
   public boolean isAssociatedWith( SoundConfiguration configuration ) {
      return this.configuration == configuration;
   }//End Method
   
   TitledPane simple(){
      return simplePane;
   }//End Method
   
   TitledPane advanced(){
      return advancedPane;
   }//End Method
   
   SoundConfigurationRow passFailRow(){
      return passFailRow;
   }//End Method
   
   SoundConfigurationRow passPassRow(){
      return passPassRow;
   }//End Method
   
   SoundConfigurationRow failFailRow(){
      return faillFailRow;
   }//End Method
   
   SoundConfigurationRow failPassRow(){
      return failPassRow;
   }//End Method
   
   SoundConfigurationRow rowFor( BuildResultStatus from, BuildResultStatus to ) {
      return rows.get( new BuildResultStatusChange( from, to ) );
   }//End Method
}//End Class
