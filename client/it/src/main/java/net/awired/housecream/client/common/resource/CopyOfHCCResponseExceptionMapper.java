package net.awired.housecream.client.common.resource;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.cxf.jaxrs.client.ResponseExceptionMapper;
import com.sun.xml.bind.api.JAXBRIContext;

public class CopyOfHCCResponseExceptionMapper implements ResponseExceptionMapper<HccUpdateException> {

    @Override
    public HccUpdateException fromResponse(Response r) {
        try {
            Map<String, Object> jaxbConfig = new HashMap<String, Object>();
            TransientAnnotationReader reader = new TransientAnnotationReader();
            try {
                reader.addTransientField(Throwable.class.getDeclaredField("stackTrace"));
                reader.addTransientMethod(Throwable.class.getDeclaredMethod("getStackTrace"));
            } catch (SecurityException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
            jaxbConfig.put(JAXBRIContext.ANNOTATION_READER, reader);
            JAXBContext jc = JAXBContext.newInstance(new Class[] { HccUpdateException.class }, jaxbConfig);
            Unmarshaller u = jc.createUnmarshaller();
            HccUpdateException unmarshal = (HccUpdateException) u.unmarshal((InputStream) r.getEntity());
            return unmarshal;
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
