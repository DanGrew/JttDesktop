/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.storage.database;

/**
 * {@link TestJenkinsDatabaseImpl} provides a {@link JenkinsDatabaseImpl} that can be
 * constructed purely for testing.
 */
public class TestJenkinsDatabaseImpl extends JenkinsDatabaseImpl {

   /**
    * Constructor to expose the {@link JenkinsDatabaseImpl} construction.
    */
   public TestJenkinsDatabaseImpl(){
      super();
   }//End Constructor
}//End Class
