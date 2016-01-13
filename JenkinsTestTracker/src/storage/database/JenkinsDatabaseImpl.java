/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package storage.database;

import javafx.collections.ObservableList;
import model.jobs.JenkinsJob;
import model.tests.TestClass;
import storage.structure.MappedObservableStoreManagerImpl;
import storage.structure.ObjectStoreManager;

/**
 * Basic implementation of the {@link JenkinsDatabase}.
 */
public class JenkinsDatabaseImpl implements JenkinsDatabase {

   private ObjectStoreManager< TestClassKey, TestClass > testClasses;
   private ObjectStoreManager< String, JenkinsJob > jenkinsJobs;

   /**
    * Constructs a new {@link JenkinsDatabaseImpl}.
    */
   public JenkinsDatabaseImpl() {
      testClasses = new MappedObservableStoreManagerImpl<>();
      jenkinsJobs = new MappedObservableStoreManagerImpl<>();
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean hasNoTestClasses() {
      return testClasses.isEmpty();
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean hasNoJenkinsJobs() {
      return jenkinsJobs.isEmpty();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public boolean hasTestClass( TestClassKey testClassKey ) {
      return testClasses.has( testClassKey );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean hasJenkinsJob( String key ) {
      return jenkinsJobs.has( key );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void store( TestClass testClass ) {
      if ( testClass == null ) return;
      
      TestClassKey representativeKey = new TestClassKeyImpl( 
               testClass.nameProperty().get(), 
               testClass.locationProperty().get() 
      );
      testClasses.store( representativeKey, testClass );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void store( JenkinsJob jenkinsJob ) {
      if ( jenkinsJob == null ) return;
      if ( jenkinsJob.nameProperty().get() == null ) return;
      
      jenkinsJobs.store( jenkinsJob.nameProperty().get(), jenkinsJob );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public TestClass getTestClass( TestClassKey testClassKey ) {
      return testClasses.get( testClassKey );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public JenkinsJob getJenkinsJob( String key ) {
      return jenkinsJobs.get( key );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public TestClass removeTestClass( TestClassKey testClassKey ) {
      return testClasses.remove( testClassKey );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public JenkinsJob removeJenkinsJob( String key ) {
      return jenkinsJobs.remove( key );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObservableList< TestClass > testClasses() {
      return testClasses.objectList();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObservableList< JenkinsJob > jenkinsJobs() {
      return jenkinsJobs.objectList();
   }//End Method

}//End Class
