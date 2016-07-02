/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility.mockito;

import static org.junit.Assert.fail;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * The {@link ParameterReturner} is responsible for defining an {@link Answer} that provides
 * a parameter back as a response.
 * @param <TypeT> the parameter type.
 * 
 * @deprecated use that provided by JUPA.
 */
@Deprecated
public class ParameterReturner< TypeT > implements Answer< TypeT >{
   
   private final Class< TypeT > classType;
   private final int parameterIndex;
   
   /**
    * Constructs a new {@link ParameterReturner}.
    * @param classType the {@link Class} of the parameter to return.
    * @param parameterIndex the index of the parameter to return.
    */
   public ParameterReturner( Class< TypeT > classType, int parameterIndex ) {
      this.classType = classType;
      this.parameterIndex = parameterIndex;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public TypeT answer( InvocationOnMock invocation ) throws Throwable {
      Object[] arguments = invocation.getArguments();
      
      if ( arguments.length <= parameterIndex ) {
         fail( "Return parameter index is larger than the number of parameters provided." );
         return null;
      }
      
      Object argument = arguments[ parameterIndex ];
      if ( !argument.getClass().equals( classType ) ) {
         fail( "Return parameter class type is not the correct type." );
         return null;
      }
      return classType.cast( argument );
   }//End Method

}//End Class
