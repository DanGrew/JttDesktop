/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.environment.main;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.environment.layout.CenterScreenWrapper;
import uk.dangrew.jtt.environment.preferences.PreferenceController;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;

/**
 * The {@link EnvironmentWindow} is responsible for providing an overall frame to the 
 * application that hosts multiple tools and systems.
 */
public class EnvironmentWindow extends BorderPane {

   static final String NEW_VERSIONS_MESSAGE = "New versions of JTT are available. Click here to install...";
   
   private final CenterScreenWrapper centerWrapper;
   private final PreferenceController preferenceOpener;
   private final EnvironmentMenuBar menuBar;
   
   /**
    * Constructs a new {@link EnvironmentWindow}.
    * @param configuration the {@link SystemConfiguration}.
    * @param database the {@link JenkinsDatabase}.
    */
   public EnvironmentWindow( SystemConfiguration configuration, JenkinsDatabase database ) {
      this( new CenterScreenWrapper(), configuration, database );
   }//End Constructor
   
   /**
    * Constructs a new {@link EnvironmentWindow}.
    * @param wrapper the {@link CenterScreenWrapper}.
    * @param configuration the {@link SystemConfiguration}.
    * @param database the {@link JenkinsDatabase}.
    */
   EnvironmentWindow( CenterScreenWrapper wrapper, SystemConfiguration configuration, JenkinsDatabase database ) {
      this.preferenceOpener = new PreferenceController( configuration, database );
      
      this.menuBar = new EnvironmentMenuBar();
      this.setTop( menuBar );
      this.centerWrapper = wrapper;
      
      this.setCenter( centerWrapper );
   }//End Constructor
   
   /**
    * Method to set the content of the window, replacing the current content.
    * @param content the {@link Node} content.
    */
   public void setContent( Node content ){
      centerWrapper.setCenter( content );
   }//End Method
   
   /**
    * Method to bind the dimensions of the given {@link Region} to the content area of this
    * {@link EnvironmentWindow}.
    * @param node the {@link Region} to bind dimensions to.
    */
   public void bindDimensions( Region node ) {
      node.maxWidthProperty().bind( widthProperty() );
      node.minWidthProperty().bind( widthProperty() );
      node.maxHeightProperty().bind( heightProperty().subtract( menuBar.heightProperty() ) );
      node.minHeightProperty().bind( heightProperty().subtract( menuBar.heightProperty() ) );
   }//End Method
   
   PreferenceController preferenceOpener(){
      return preferenceOpener;
   }//End Method

}//End Class
