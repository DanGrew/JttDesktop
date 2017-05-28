/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.api;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import uk.dangrew.sd.table.model.DigestTable;
import uk.dangrew.sd.table.presentation.DigestTableRowLimit;

/**
 * The {@link ApiConfigurationPanel} provides the root panel of the {@link uk.dangrew.jtt.connection.api.sources.JenkinsConnection}
 * configuration.
 */
public class ApiConfigurationPanel extends BorderPane {
   
   static final double INSETS = 20;
   static final double PREF_WIDTH = 100;
   static final double PREF_HEIGHT = 200;
   
   /**
    * Constructs a new {@link ApiConfigurationPanel}.
    */
   public ApiConfigurationPanel() {
      this( new ApiConfigurationController() );
   }//End Constructor
   
   /**
    * Constructs a new {@link ApiConfigurationPanel}.
    * @param controller the {@link ApiConfigurationController}.
    */
   private ApiConfigurationPanel( ApiConfigurationController controller ) {
      this( 
               controller, 
               new JenkinsConnectionTable(), 
               new JenkinsConnectionControls( controller ),
               new DigestTable()
      );
   }//End Constructor
   
   /**
    * Constructs a new {@link ApiConfigurationPanel}.
    * @param controller the {@link ApiConfigurationController}.
    * @param table the {@link JenkinsConnectionTable}.
    * @param controls the {@link JenkinsConnectionControls}.
    * @param digest the {@link DigestTable}.
    */
   ApiConfigurationPanel( 
            ApiConfigurationController controller, 
            JenkinsConnectionTable table, 
            JenkinsConnectionControls controls,
            DigestTable digest
  ) {
      this.setTop( table );
      this.setCenter( controls );
      this.setBottom( digest );
      this.setPadding( new Insets( INSETS ) );
      
      controller.control( table, controls );
      
      digest.setRowLimit( DigestTableRowLimit.OneHundred );
      digest.setPrefSize( 100, 200 );
      table.setPrefSize( 100, 200 );
      controls.setPadding( new Insets( INSETS ) );
   }//End Constructor

}//End Class
