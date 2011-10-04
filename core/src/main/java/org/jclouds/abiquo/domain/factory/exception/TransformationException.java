package org.jclouds.abiquo.domain.factory.exception;


/**
 * Exception thrown during model to client object transformation.
 * 
 * @author Francesc Montserrat
 */
public class TransformationException extends RuntimeException
{
    private static final long serialVersionUID = -3093923238120960853L;

    String source;

    String target;

    public TransformationException(final String source, final String target)
    {
        this.source = source;
        this.target = target;
    }

    public TransformationException(final String source, final String target, final String message)
    {
        super(message);
        this.source = source;
        this.target = target;
    }

    @Override
    public String getMessage()
    {
        String msg = "Could not transform source " + source + " to target " + target + ": ";
        return msg + super.getMessage();
    }
}
