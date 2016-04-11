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
 * {@link DecoupledPlatformImpl} provides a separate interface to {@link PlatformImpl}
 * to allow decoupling and testing.
 */
public class DecoupledPlatformImpl {
   
   private static PlatformDecoupler decoupler;
   
   /**
    * Method to set the instance to work with. This will provide the method of running items
    * on {@link PlatformImpl} or alternative.
    * @param decoupler the {@link PlatformDecoupler}.
    */
   public static void setInstance( PlatformDecoupler decoupler ) {
      DecoupledPlatformImpl.decoupler = decoupler;
   }//End Method

   /**
    * Method to replace {@link PlatformImpl#runLater(Runnable)}.
    * @param runnable the {@link Runnable} to run.
    */
   public static void runLater( Runnable runnable ) {
      decoupler.run( runnable );
   }//End Method

}//End Class
