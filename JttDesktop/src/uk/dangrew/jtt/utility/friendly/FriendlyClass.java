/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility.friendly;

import java.io.InputStream;

/**
 * The {@link FriendlyClass} provides a mockable interface to a {@link Class}.
 * This is important for verifying some interactions.
 */
public class FriendlyClass< ClassTypeT > {

   private final Class< ? extends ClassTypeT > subject;
   
   /**
    * Constructs a new {@link FriendlyClass}.
    * @param subject the {@link Class} to wrap.
    */
   public FriendlyClass( Class< ? extends ClassTypeT > subject ) {
      this.subject = subject;
   }//End Constructor
   
   /**
    * Method to simply forward on to {@link Class#getResourceAsStream(String)}.
    */
   public InputStream getResourceAsStream( String path ) {
      return subject.getResourceAsStream( path );
   }//End Method

}//End Class
