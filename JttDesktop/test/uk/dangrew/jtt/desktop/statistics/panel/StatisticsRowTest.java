/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.statistics.panel;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.geometry.HPos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;
import uk.dangrew.jtt.desktop.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.desktop.statistics.panel.StatisticsRow;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;

public class StatisticsRowTest {
   
   @Spy private JavaFxStyle styling;
   private StatisticsConfiguration configuration;
   private JenkinsDatabase database;
   private StatisticsRow systemUnderTest;
   
   @Before public void initialiseSystemUnderTest() {
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      database = new TestJenkinsDatabaseImpl();
      configuration = new StatisticsConfiguration();
      systemUnderTest = new StatisticsRow( styling, database, configuration );
   }//End Method

   @Ignore
   @Test public void manual() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> new StatisticsRow( database, configuration ) );
      
      Thread.sleep( 1000000 );
   }//End Method

   @Test public void shouldHaveTotalSuccessStatistic() {
      assertThat( systemUnderTest.totalSuccessStatistic().isAssociatedWith( configuration ), is( true ) );
      assertThat( systemUnderTest.totalSuccessStatistic().isAssociatedWith( database ), is( true ) );
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.totalSuccessStatistic() ), is( true ) );
      assertThat( GridPane.getHalignment( systemUnderTest.totalSuccessStatistic() ), is( HPos.CENTER ) );
   }//End Method
   
   @Test public void shouldHaveTotalPassingTestsStatistic() {
      assertThat( systemUnderTest.totalPassingTestsStatistic().isAssociatedWith( configuration ), is( true ) );
      assertThat( systemUnderTest.totalPassingTestsStatistic().isAssociatedWith( database ), is( true ) );
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.totalPassingTestsStatistic() ), is( true ) );
      assertThat( GridPane.getHalignment( systemUnderTest.totalPassingTestsStatistic() ), is( HPos.CENTER ) );
   }//End Method
   
   @Test public void shouldHaveNodesInUseStatistic() {
      assertThat( systemUnderTest.nodeInUseStatistic().isAssociatedWith( configuration ), is( true ) );
      assertThat( systemUnderTest.nodeInUseStatistic().isAssociatedWith( database ), is( true ) );
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.nodeInUseStatistic() ), is( true ) );
      assertThat( GridPane.getHalignment( systemUnderTest.nodeInUseStatistic() ), is( HPos.CENTER ) );
   }//End Method
   
   @Test public void shouldHaveLastBuildStartedStatistic() {
      assertThat( systemUnderTest.lastBuildStartedStatistic().isAssociatedWith( configuration ), is( true ) );
      assertThat( systemUnderTest.lastBuildStartedStatistic().isAssociatedWith( database ), is( true ) );
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.lastBuildStartedStatistic() ), is( true ) );
      assertThat( GridPane.getHalignment( systemUnderTest.lastBuildStartedStatistic() ), is( HPos.CENTER ) );
   }//End Method
   
   @Test public void shouldProvideBackgroundOfDualWall(){
      assertThat( systemUnderTest.getBackground().getFills().get( 0 ).getFill(), is( Color.BLACK ) );
   }//End Method
   
   @Test public void shouldProvideEvenColumnSplit(){
      verify( styling ).configureConstraintsForEvenColumns( systemUnderTest, 4 );
   }//End Method
   
   @Test public void shouldBeAssociatedWithConfiguration(){
      assertThat( systemUnderTest.isAssociatedWith( configuration ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( new StatisticsConfiguration() ), is( false ) );
   }//End Method
   
   @Test public void shouldBeAssociatedWithDatabase(){
      assertThat( systemUnderTest.isAssociatedWith( database ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( new TestJenkinsDatabaseImpl() ), is( false ) );
   }//End Method
}//End Class
