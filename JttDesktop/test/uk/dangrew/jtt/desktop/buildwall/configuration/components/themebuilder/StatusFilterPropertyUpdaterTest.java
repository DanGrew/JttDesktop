/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components.themebuilder;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.desktop.buildwall.configuration.components.themebuilder.StatusFilterPropertyUpdater;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;

public class StatusFilterPropertyUpdaterTest {

   private ObservableMap< BuildResultStatus, Color > map;
   private ObjectProperty< Color > property;
   private StatusFilterPropertyUpdater systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      map = FXCollections.observableHashMap();
      property = new SimpleObjectProperty<>();
      systemUnderTest = new StatusFilterPropertyUpdater( map, BuildResultStatus.FAILURE, property );
      map.addListener( systemUnderTest );
   }//End Method

   @Test public void shouldUpdatePropertyWhenSetInMap() {
      map.put( BuildResultStatus.FAILURE, Color.RED );
      assertThat( property.get(), is( Color.RED ) );
      
      map.put( BuildResultStatus.UNSTABLE, Color.BLUE );
      assertThat( property.get(), is( Color.RED ) );
      
      map.put( BuildResultStatus.FAILURE, null );
      assertThat( property.get(), is( nullValue() ) );
   }//End Method

}//End Class
