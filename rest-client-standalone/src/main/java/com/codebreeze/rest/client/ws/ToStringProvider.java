package com.codebreeze.rest.client.ws;

import org.apache.commons.io.IOUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.util.ObjectUtils.nullSafeToString;

@Produces( "text/plain" )
@Consumes( "text/plain" )
@Provider
public class ToStringProvider implements MessageBodyReader<Object>,
        MessageBodyWriter<Object> {

    @Override
    public boolean isWriteable ( Class<?> type, Type genericType,
                                 Annotation[] annotations, MediaType mediaType ) {
        System.out.println(mediaType);
        return MediaType.TEXT_PLAIN_TYPE.equals ( mediaType );
//                && type.equals ( Sometype.class );
    }

    @Override
    public long getSize ( Object t, Class<?> type, Type genericType,
                          Annotation[] annotations, MediaType mediaType ) {
        // I'm being lazy - should compute the actual size
        return -1;
    }

    @Override
    public void writeTo ( Object t, Class<?> type, Type genericType,
                          Annotation[] annotations, MediaType mediaType,
                          MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream )
            throws IOException, WebApplicationException {
        entityStream.write(nullSafeToString(t).getBytes(UTF_8));
    }

    @Override
    public boolean isReadable ( Class<?> type, Type genericType,
                                Annotation[] annotations, MediaType mediaType ) {
        return MediaType.TEXT_PLAIN_TYPE.equals ( mediaType );
//                && type.equals ( Sometype.class );
    }

    @Override
    public Object readFrom ( Class<Object> type, Type genericType,
                             Annotation[] annotations, MediaType mediaType,
                             MultivaluedMap<String, String> httpHeaders, InputStream entityStream )
            throws IOException, WebApplicationException {
        // add error handling, etc.
        return IOUtils.toString(entityStream);
    }
}
