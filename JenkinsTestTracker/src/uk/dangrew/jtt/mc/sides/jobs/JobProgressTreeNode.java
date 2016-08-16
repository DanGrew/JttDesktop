/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.jobs;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.buildwall.panel.JobProgressImpl;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link JobProgressTreeNode} is the graphic for a {@link JobProgressTree} item.
 */
public class JobProgressTreeNode extends GridPane {

   static final double NAME_PROPORTION = 40.0;
   static final double PROGRESS_PROPORTION = 60.0;
   static final double MINIMUM_JOB_NAME_WIDTH = 100;
   
   private final JenkinsJob job;
   private final Label jobName;
   private final BorderPane jobProgress;
   
   /**
    * Constructs a new {@link JobProgressTreeNode}.
    * @param job the {@link JenkinsJob} associated.
    */
   public JobProgressTreeNode( JenkinsJob job ) {
      this( job, new JavaFxStyle() );
   }//End Constructor
   
   /**
    * Constructs a new {@link JobProgressTreeNode}.
    * @param job the {@link JenkinsJob} associated.
    * @param styling the {@link JavaFxStyle} for styling components.
    */
   JobProgressTreeNode( JenkinsJob job, JavaFxStyle styling ) {
      this.job = job;
      add( jobName = styling.createBoldLabel( job.nameProperty().get() ), 0, 0 );
      add( jobProgress = new BorderPane( new JobProgressImpl( job ) ), 1, 0 );
      
      ColumnConstraints constraintA = new ColumnConstraints();
      constraintA.setPercentWidth( NAME_PROPORTION );
      constraintA.setMinWidth( MINIMUM_JOB_NAME_WIDTH );
      ColumnConstraints constraintB = new ColumnConstraints();
      constraintB.setPercentWidth( PROGRESS_PROPORTION );
      getColumnConstraints().addAll( constraintA, constraintB );
   }//End Constructor
   
   /**
    * Method to determine whether the given {@link JenkinsJob} is associated with this.
    * @param job the {@link JenkinsJob} in question.
    * @return true if associated.
    */
   public boolean isAssociatedWith( JenkinsJob job ) {
      return this.job == job;
   }//End Method
   
   Label jobName(){
      return jobName;
   }//End Method
   
   BorderPane jobProgress(){
      return jobProgress;
   }//End Method
   
}//End Class
