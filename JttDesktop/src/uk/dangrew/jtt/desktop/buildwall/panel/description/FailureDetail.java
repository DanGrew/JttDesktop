/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.panel.description;

import java.util.LinkedHashSet;
import java.util.Set;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.desktop.javafx.registrations.ChangeListenerRegistrationImpl;
import uk.dangrew.jtt.desktop.javafx.registrations.ListChangeListenerRegistrationImpl;
import uk.dangrew.jtt.desktop.javafx.registrations.RegisteredComponent;
import uk.dangrew.jtt.desktop.javafx.registrations.RegistrationManager;
import uk.dangrew.jtt.model.commit.Commit;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.nodes.JenkinsNode;
import uk.dangrew.jtt.model.tests.TestClass;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.utility.observable.FunctionListChangeListenerImpl;

/**
 * {@link FailureDetail} provides a sub component for showing more detail about
 * the failure of a build.
 */
public class FailureDetail extends GridPane implements RegisteredComponent {
   
   static final String UNKNOWN_NODE = "Unknown";
   static final String BUILT_ON_PREFIX = "Built on: ";
   static final String NO_COMMITTERS = "Unaware of any committers.";
   static final String PASSING = "Build stable.";
   
   static final String NEW_COMMITTERS_PREFIX = "New Committers: ";
   static final String SINCE_LAST_FAILURE_PREFIX = "Since Last Failure: ";
   static final String NO_FAILING_TESTS = "No Failures.";
   static final double SINCE_FAILURE_COMMITTERS_ROW_PERCENT = 20.0;
   static final double NEW_COMMITTERS_ROW_PERCENT = 15.0;
   static final double BUILT_ON_ROW_PERCENT = 10.0;
   static final double FAILURES_ROW_PERCENT = 55.0;
   
   static final String ABORTED_DESCRIPTION = "ABORTED: Manual, Build Timeout, etc.";
   static final String FAILURE_DESCRIPTION = "FAILURE: Compilation Problem, Partial Checkout, etc.";
   
   private final BuildWallConfiguration configuration;
   private final JenkinsJob jenkinsJob;
   
   private RegistrationManager registrations;
   private Label sinceLastFailureLabel;
   private Label newCommittersLabel;
   private Label lastBuiltOnLabel;
   private Label failingLabel;
   
   /**
    * Constructs a new {@link FailureDetail}.
    * @param jenkinsJob the {@link JenkinsJob} associated.
    * @param configuration the {@link BuildWallConfiguration} for configuring the components.
    */
   public FailureDetail( JenkinsJob jenkinsJob, BuildWallConfiguration configuration ) {
      this.jenkinsJob = jenkinsJob;
      this.configuration = configuration;
      
      provideCommitters();
      provideLastBuiltOnNode();
      provideFailingTestCases();
      
      updateDetailColour();
      updateDetailFont();
      
      applyRegistrations();
      applyRowConstraints();
   }//End Constructor
   
   /**
    * Method to provide the committers on the component as a {@link Label}.
    */
   private void provideCommitters(){  
      sinceLastFailureLabel = new Label();
      sinceLastFailureLabel.setWrapText( true );
      newCommittersLabel = new Label();
      newCommittersLabel.setWrapText( true );
      updateCommittersText();
      add( sinceLastFailureLabel, 0, 0 );
      add( newCommittersLabel, 0, 1 );
   }//End Method
   
   /**
    * Method to provide the {@link JenkinsNode} the {@link JenkinsNode} was last
    * built on.
    */
   private void provideLastBuiltOnNode(){  
      lastBuiltOnLabel = new Label();
      updateLastBuiltOnText();
      lastBuiltOnLabel.setWrapText( true );
      add( lastBuiltOnLabel, 0, 2 );
   }//End Method

   /**
    * Method to construct the list of committers as a {@link StringBuilder}.
    * @return the {@link StringBuilder} for further changes if needed.
    */
   private StringBuilder constructCommittersList( ObservableList< Commit > commits ) {
      StringBuilder committers = new StringBuilder();
      
      Set< JenkinsUser > committerUsers = new LinkedHashSet<>();
      commits.forEach( c -> committerUsers.add( c.user() ) );
      
      if ( committerUsers.isEmpty() ) {
         committers.append( NO_COMMITTERS );
         return committers;
      }
      
      committerUsers.forEach( committer -> {
         committers.append( committer.nameProperty().get() );
         committers.append( ", " );
      } );
      if ( committers.length() > 0 ) {
         committers.setLength( committers.length() - 1 );
         committers.setCharAt( committers.length() - 1, '.' );
      }
      return committers;
   }//End Method
   
   /**
    * Method to provide the failing test cases on the component as a {@link Label}.
    */
   private void provideFailingTestCases(){  
      failingLabel = new Label();
      failingLabel.setLineSpacing( 0.0 );
      updateFailuresText();
      failingLabel.setWrapText( true );
      add( failingLabel, 0, 3 );
   }//End Method

   /**
    * Method to construct the list of failing test cases as a {@link StringBuilder}.
    * @return the {@link StringBuilder} for further changes if needed.
    */
   private StringBuilder constructFailingTestCasesList() {
      StringBuilder failingTests = new StringBuilder();
      
      BuildResultStatus status = jenkinsJob.buildProperty().get().getValue();
      if ( status == BuildResultStatus.ABORTED ) {
         return failingTests.append( ABORTED_DESCRIPTION );
      } else if ( status == BuildResultStatus.FAILURE ) {
         return failingTests.append( FAILURE_DESCRIPTION );
      }
      
      Set< TestClass > uniqueTestClasses = new LinkedHashSet<>();
      jenkinsJob.failingTestCases().forEach( testCase -> uniqueTestClasses.add( testCase.testClassProperty().get() ) );
      
      if ( uniqueTestClasses.isEmpty() ) {
         failingTests.append( NO_FAILING_TESTS );
         return failingTests;
      }
      
      failingTests.append( jenkinsJob.testFailureCount().get() ).append( "/" )
         .append( jenkinsJob.testTotalCount().get() )
         .append( " Failed:" ).append( "\n" );
      
      uniqueTestClasses.forEach( test -> {
         failingTests.append( test.nameProperty().get() );
         failingTests.append( "\n" );
      } );
      if ( failingTests.length() > 0 ) {
         failingTests.setLength( failingTests.length() - 1 );
      }
      return failingTests;
   }//End Method
   
   /**
    * Method to apply the {@link RowConstraints} needed.
    */
   private void applyRowConstraints() {
      RowConstraints sinceFailureRow = new RowConstraints();
      sinceFailureRow.setPercentHeight( SINCE_FAILURE_COMMITTERS_ROW_PERCENT );
      sinceFailureRow.setMaxHeight( Double.MAX_VALUE );
      
      RowConstraints newCommittersRow = new RowConstraints();
      newCommittersRow.setPercentHeight( NEW_COMMITTERS_ROW_PERCENT );
      newCommittersRow.setMaxHeight( Double.MAX_VALUE );
      
      RowConstraints builtOnRow = new RowConstraints();
      builtOnRow.setPercentHeight( BUILT_ON_ROW_PERCENT );
      builtOnRow.setMaxHeight( Double.MAX_VALUE );
      
      RowConstraints failuresRow = new RowConstraints();
      failuresRow.setPercentHeight( FAILURES_ROW_PERCENT );
      failuresRow.setMaxHeight( Double.MAX_VALUE );
      
      getRowConstraints().addAll( sinceFailureRow, newCommittersRow, builtOnRow, failuresRow );
   }//End Method
   
   /**
    * Method to apply the {@link RegistrationImpl}s needed to keep the component up to date.
    */
   private void applyRegistrations(){
      registrations = new RegistrationManager();
      
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               configuration.detailFont(), 
               ( source, old, updated ) -> updateDetailFont() 
      ) );
      
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               configuration.detailColour(), 
               ( source, old, updated ) -> updateDetailColour() 
      ) );
      
      registrations.apply( new ListChangeListenerRegistrationImpl<>(
               jenkinsJob.commits(), 
               new FunctionListChangeListenerImpl<>( 
                        added -> updateCommittersText(), removed -> updateCommittersText()
               )
      ) );
      
      registrations.apply( new ListChangeListenerRegistrationImpl<>(
               jenkinsJob.supplements().commitsSinceLastFailure(), 
               new FunctionListChangeListenerImpl<>( 
                        added -> updateCommittersText(), removed -> updateCommittersText()
               )
      ) );
      
      registrations.apply( new ListChangeListenerRegistrationImpl<>(
               jenkinsJob.failingTestCases(), 
               new FunctionListChangeListenerImpl<>( 
                        added -> updateFailuresText(), removed -> updateFailuresText()
               )
      ) );
      
      registrations.apply( new ChangeListenerRegistrationImpl<>(
               jenkinsJob.testFailureCount(), ( s, o, n ) -> updateFailuresText() 
      ) );
      registrations.apply( new ChangeListenerRegistrationImpl<>(
               jenkinsJob.testTotalCount(), ( s, o, n ) -> updateFailuresText() 
      ) );
      
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               jenkinsJob.builtOnProperty(), 
               ( source, old, updated ) -> updateLastBuiltOnText() 
      ) );
      
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               jenkinsJob.buildProperty(), 
               ( source, old, updated ) -> updateFailuresText() 
      ) );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void detachFromSystem(){
      registrations.shutdown();
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean isDetached() {
      return registrations.isEmpty();
   }//End Method
   
   /**
    * Method to update the committers {@link Font} in line with the {@link BuildWallConfiguration}.
    */
   private void updateDetailFont(){
      sinceLastFailureLabel.fontProperty().set( configuration.detailFont().get() );
      newCommittersLabel.fontProperty().set( configuration.detailFont().get() );
      lastBuiltOnLabel.fontProperty().set( configuration.detailFont().get() );
      failingLabel.fontProperty().set( configuration.detailFont().get() );
   }//End Method
   
   /**
    * Method to update the committers {@link Color} in line with the {@link BuildWallConfiguration}.
    */
   private void updateDetailColour(){
      sinceLastFailureLabel.textFillProperty().set( configuration.detailColour().get() );
      newCommittersLabel.textFillProperty().set( configuration.detailColour().get() );
      lastBuiltOnLabel.textFillProperty().set( configuration.detailColour().get() );
      failingLabel.textFillProperty().set( configuration.detailColour().get() );
   }//End Method
   
   /**
    * Method to update the committers text displayed in the {@link Label}.
    */
   private void updateCommittersText(){
      StringBuilder sinceLastFailureText;
      if ( jenkinsJob.getBuildStatus() == BuildResultStatus.SUCCESS ) {
         sinceLastFailureText = new StringBuilder( PASSING );
      } else { 
         sinceLastFailureText = constructCommittersList( jenkinsJob.supplements().commitsSinceLastFailure() );
      }
      StringBuilder newCommittersText = constructCommittersList( jenkinsJob.commits() );
      DecoupledPlatformImpl.runLater( () -> {
         sinceLastFailureLabel.setText( SINCE_LAST_FAILURE_PREFIX + sinceLastFailureText.toString() );
         newCommittersLabel.setText( NEW_COMMITTERS_PREFIX + newCommittersText.toString() );
      } );
   }//End Method
   
   /**
    * Method to update the node text displayed in the {@link Label}.
    */
   private void updateLastBuiltOnText(){
      JenkinsNode builtOn = jenkinsJob.builtOnProperty().get();
      String builtOnText = BUILT_ON_PREFIX + ( builtOn == null ? UNKNOWN_NODE : builtOn.nameProperty().get() );
      DecoupledPlatformImpl.runLater( () -> {
         lastBuiltOnLabel.setText( builtOnText );
      } );
   }//End Method
   
   /**
    * Method to update the culprit text displayed in the {@link Label}.
    */
   private void updateFailuresText(){
      StringBuilder failures = constructFailingTestCasesList();
      DecoupledPlatformImpl.runLater( () -> {
         failingLabel.setText( failures.toString() );
      } );
   }//End Method
   
   Label sinceLastFailureLabel() {
      return sinceLastFailureLabel;
   }//End Method
   
   Label newCommittersLabel() {
      return newCommittersLabel;
   }//End Method

   Label lastBuiltOnLabel() {
      return lastBuiltOnLabel;
   }//End Method
   
   Label failuresLabel() {
      return failingLabel;
   }//End Method
   
}//End Class
