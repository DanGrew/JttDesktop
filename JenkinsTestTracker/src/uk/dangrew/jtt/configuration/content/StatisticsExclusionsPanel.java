/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.configuration.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.utility.comparator.Comparators;
import uk.dangrew.jtt.utility.observable.FunctionListChangeListenerImpl;

/**
 * The {@link StatisticsExclusionsPanel} provides a configuration panel for excluding jobs from the 
 * statistics views and calculations.
 */
public class StatisticsExclusionsPanel extends GridPane {
   
   static final String SELECT_DESELECT_ALL = "Select/Deselect All";
   static final double PADDING = 10;
   
   private final StatisticsConfiguration configuration;
   private final JenkinsDatabase database;
   
   private final CheckBox selectAll;
   private final Map< JenkinsJob, CheckBox > boxes;
   
   /**
    * Constructs a new {@link StatisticsExclusionsPanel}.
    * @param database the {@link JenkinsDatabase}.
    * @param confgiuration the {@link StatisticsConfiguration}.
    */
   public StatisticsExclusionsPanel( JenkinsDatabase database, StatisticsConfiguration confgiuration ) {
      this( new JavaFxStyle(), database, confgiuration );
   }//End Constructor
   
   /**
    * Constructs a new {@link StatisticsExclusionsPanel}.
    * @param styling the {@link JavaFxStyle}.
    * @param database the {@link JenkinsDatabase}.
    * @param confgiuration the {@link StatisticsConfiguration}.
    */
   StatisticsExclusionsPanel( JavaFxStyle styling, JenkinsDatabase database, StatisticsConfiguration confgiuration ) {
      this.database = database;
      this.configuration = confgiuration;
      this.boxes = new HashMap<>();
      
      styling.configureFullWidthConstraints( this );
      
      selectAll = new CheckBox( SELECT_DESELECT_ALL );
      selectAll.setSelected( true );
      selectAll.selectedProperty().addListener( ( s, o, n ) -> {
          boxes.values().forEach( box -> box.setSelected( n ) );
      } );
      resetCheckBoxesForJobs();
      
      setPadding( new Insets( PADDING ) );
      
      database.jenkinsJobs().addListener( new FunctionListChangeListenerImpl<>( 
               j -> resetCheckBoxesForJobs(), j -> resetCheckBoxesForJobs() 
      ) );
      confgiuration.excludedJobs().addListener( new FunctionListChangeListenerImpl<>( 
               j -> boxes.get( j ).setSelected( false ), j -> boxes.get( j ).setSelected( true ) 
      ) );
   }//End Constructor
   
   /**
    * Method to reset the {@link CheckBox}es on the panel, clearing existing and recreating new.
    */
   private void resetCheckBoxesForJobs(){
      getChildren().clear();
      boxes.clear();
      
      add( selectAll, 0, 0 );
      
      List< JenkinsJob > jobs = new ArrayList<>( database.jenkinsJobs() );
      jobs.sort( Comparators.stringExtractionComparater( j -> j.nameProperty().get() ) );
      for ( int i = 0; i < jobs.size(); i++ ) {
         addJobCheckBox( jobs.get( i ), i + 1 );
      }
   }//End Method
   
   /**
    * Method to add a {@link CheckBox} for the given {@link JenkinsJob}, on the row.
    * @param job the {@link JenkinsJob} to add for.
    * @param row the row to add the box into.
    */
   private void addJobCheckBox( JenkinsJob job, int row ){
      CheckBox box = new CheckBox( job.nameProperty().get() );
      box.setSelected( true );
      box.selectedProperty().addListener( ( s, o, n ) -> {
          if ( n ) {
             configuration.excludedJobs().remove( job );
          } else {
             configuration.excludedJobs().add( job );
          }
      } );
      add( box, 0, row );
      boxes.put( job, box );
   }//End Method
   
   /**
    * Method to determine whether the given is associated with this.
    * @param configuration the {@link StatisticsConfiguration} in question.
    * @return true if identical.
    */
   public boolean isAssociatedWith( StatisticsConfiguration configuration ) {
      return this.configuration == configuration;
   }//End Method

   CheckBox selectAll(){
      return selectAll;
   }//End Method
   
   CheckBox checkBoxFor( JenkinsJob job ) {
      return boxes.get( job );
   }//End Method

}//End Class
