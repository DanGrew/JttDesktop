/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package graphics;

/**
 * Implementation of {@link PlatformDecoupler} to simply run the {@link Runnable}
 * without the {@link PlatformImpl} thread. This is used for testing.
 */
public class TestPlatformDecouplerImpl implements PlatformDecoupler {
   
   private final Runnable recorder;
   
   /**
    * Constructs a new {@link TestPlatformDecouplerImpl}.
    */
   public TestPlatformDecouplerImpl() {
      this( null );
   }//End Constructor
   
   /**
    * Constructs a new {@link TestPlatformDecouplerImpl}.
    * @param recorder the {@link Runnable} to call when this is invoked.
    */
   public TestPlatformDecouplerImpl( Runnable recorder ) {
      this.recorder = recorder;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void run( Runnable runnable ) {
      runnable.run();
      
      if ( recorder != null ) recorder.run();
   }//End Method

}//End Class
