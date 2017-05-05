/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.storage.database;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class SystemWideJenkinsDatabaseImplTest {

   private SystemWideJenkinsDatabaseImpl systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      systemUnderTest = new SystemWideJenkinsDatabaseImpl();
   }//End Method

   @Test public void shouldProvideSameDatabaseForEachInstantiation() {
      assertThat( systemUnderTest.get(), is( new SystemWideJenkinsDatabaseImpl().get() ) );
      assertThat( systemUnderTest.get(), is( notNullValue() ) );
   }//End Method

}//End Class
