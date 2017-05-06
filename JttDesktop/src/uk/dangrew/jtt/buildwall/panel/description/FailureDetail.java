/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.panel.description;

import java.util.LinkedHashSet;
import java.util.Set;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.javafx.registrations.ChangeListenerRegistrationImpl;
import uk.dangrew.jtt.javafx.registrations.ListChangeListenerRegistrationImpl;
import uk.dangrew.jtt.javafx.registrations.RegisteredComponent;
import uk.dangrew.jtt.javafx.registrations.RegistrationManager;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.nodes.JenkinsNode;
import uk.dangrew.jtt.model.tests.TestClass;
import uk.dangrew.jtt.model.utility.observable.FunctionListChangeListenerImpl;

/**
 * {@link FailureDetail} provides a sub component for showing more detail about
 * the failure of a build.
 */
public class FailureDetail extends GridPane implements RegisteredComponent {
   
   static final String UNKNOWN_NODE = "Unknown";
   static final String BUILT_ON_PREFIX = "Built on: ";
   static final String CULPRIT_PREFIX = "Suspect: ";
   static final String CULPRITS_PREFIX = "Suspects: ";
   static final String NO_CULPRITS = "No Suspects.";
   
   static final String NO_FAILING_TESTS = "No Failures.";
   static final double CULPRITS_ROW_PERCENT = 25.0;
   static final double BUILT_ON_ROW_PERCENT = 10.0;
   static final double FAILURES_ROW_PERCENT = 65.0;
   
   static final String ABORTED_DESCRIPTION = "ABORTED: Manual, Build Timeout, etc.";
   static final String FAILURE_DESCRIPTION = "FAILURE: Compilation Problem, Partial Checkout, etc.";
   
   private final BuildWallConfiguration configuration;
   private final JenkinsJob jenkinsJob;
   
   private RegistrationManager registrations;
   private Label culpritsLabel;
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
      
      provideCulprits();
      provideLastBuiltOnNode();
      provideFailingTestCases();
      
      updateDetailColour();
      updateDetailFont();
      
      applyRegistrations();
      applyRowConstraints();
   }//End Constructor
   
   /**
    * Method to provide the culprits on the component as a {@link Label}.
    */
   private void provideCulprits(){  
      culpritsLabel = new Label();
      updateCulpritText();
      culpritsLabel.setWrapText( true );
      add( culpritsLabel, 0, 0 );
   }//End Method
   
   /**
    * Method to provide the {@link JenkinsNode} the {@link JenkinsNode} was last
    * built on.
    */
   private void provideLastBuiltOnNode(){  
      lastBuiltOnLabel = new Label();
      updateLastBuiltOnText();
      lastBuiltOnLabel.setWrapText( true );
      add( lastBuiltOnLabel, 0, 1 );
   }//End Method

   /**
    * Method to construct the list of culprits as a {@link StringBuilder}.
    * @return the {@link StringBuilder} for further changes if needed.
    */
   private StringBuilder constructCulpritsList() {
      StringBuilder culprits = new StringBuilder();
      
      if ( jenkinsJob.culprits().isEmpty() ) {
         culprits.append( NO_CULPRITS );
         return culprits;
      }
      
      if ( jenkinsJob.culprits().size() == 1 ) {
         culprits.append( CULPRIT_PREFIX );
      } else {
         culprits.append( CULPRITS_PREFIX );
      }
      jenkinsJob.culprits().forEach( culprit -> {
         culprits.append( culprit.nameProperty().get() );
         culprits.append( ", " );
      } );
      if ( culprits.length() > 0 ) {
         culprits.setLength( culprits.length() - 1 );
         culprits.setCharAt( culprits.length() - 1, '.' );
      }
      return culprits;
   }//End Method
   
   /**
    * Method to provide the culprits on the component as a {@link Label}.
    */
   private void provideFailingTestCases(){  
      failingLabel = new Label();
      failingLabel.setLineSpacing( 0.0 );
      updateFailuresText();
      failingLabel.setWrapText( true );
      add( failingLabel, 0, 2 );
   }//End Method

   /**
    * Method to construct the list of culprits as a {@link StringBuilder}.
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
      RowConstraints culpritsRow = new RowConstraints();
      culpritsRow.setPercentHeight( CULPRITS_ROW_PERCENT );
      culpritsRow.setMaxHeight( Double.MAX_VALUE );
      
      RowConstraints builtOnRow = new RowConstraints();
      builtOnRow.setPercentHeight( BUILT_ON_ROW_PERCENT );
      builtOnRow.setMaxHeight( Double.MAX_VALUE );
      
      RowConstraints failuresRow = new RowConstraints();
      failuresRow.setPercentHeight( FAILURES_ROW_PERCENT );
      failuresRow.setMaxHeight( Double.MAX_VALUE );
      
      getRowConstraints().addAll( culpritsRow, builtOnRow, failuresRow );
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
               jenkinsJob.culprits(), 
               new FunctionListChangeListenerImpl<>( 
                        added -> updateCulpritText(), removed -> updateCulpritText()
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
    * Method to update the culprits {@link Font} in line with the {@link BuildWallConfiguration}.
    */
   private void updateDetailFont(){
      culpritsLabel.fontProperty().set( configuration.detailFont().get() );
      lastBuiltOnLabel.fontProperty().set( configuration.detailFont().get() );
      failingLabel.fontProperty().set( configuration.detailFont().get() );
   }//End Method
   
   /**
    * Method to update the culprits {@link Color} in line with the {@link BuildWallConfiguration}.
    */
   private void updateDetailColour(){
      culpritsLabel.textFillProperty().set( configuration.detailColour().get() );
      lastBuiltOnLabel.textFillProperty().set( configuration.detailColour().get() );
      failingLabel.textFillProperty().set( configuration.detailColour().get() );
   }//End Method
   
   /**
    * Method to update the culprit text displayed in the {@link Label}.
    */
   private void updateCulpritText(){
      StringBuilder culprits = constructCulpritsList();
      DecoupledPlatformImpl.runLater( () -> {
         culpritsLabel.setText( culprits.toString() );
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
   
   Label culpritsLabel() {
      return culpritsLabel;
   }//End Method

   Label lastBuiltOnLabel() {
      return lastBuiltOnLabel;
   }//End Method
   
   Label failuresLabel() {
      return failingLabel;
   }//End Method
   
}//End Class
