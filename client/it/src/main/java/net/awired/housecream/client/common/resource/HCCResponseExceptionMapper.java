package net.awired.housecream.client.common.resource;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import net.awired.housecream.client.common.domain.HccError;
import org.apache.cxf.jaxrs.client.ResponseExceptionMapper;
import com.google.common.base.Strings;

public class HCCResponseExceptionMapper implements ResponseExceptionMapper<Exception> {

    @Override
    public Exception fromResponse(Response r) {
        try {
            JAXBContext jc = JAXBContext.newInstance(new Class[] { HccError.class });
            Unmarshaller u = jc.createUnmarshaller();
            HccError hccError = (HccError) u.unmarshal((InputStream) r.getEntity());
            Class<?> loadClass = getClass().getClassLoader().loadClass(hccError.getErrorClass());
            Exception newInstance = (Exception) loadClass.getConstructor(String.class).newInstance(
                    "From server: " + Strings.nullToEmpty(hccError.getMessage()));
            return newInstance;
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
