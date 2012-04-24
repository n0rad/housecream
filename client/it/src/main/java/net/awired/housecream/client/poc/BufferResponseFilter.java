package net.awired.housecream.client.poc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ContainerResponseWriter;

public class BufferResponseFilter implements ContainerResponseFilter {

    private final class Adapter implements ContainerResponseWriter {
        private final ContainerResponseWriter crw;

        private ContainerResponse response;

        private ByteArrayOutputStream baos;

        Adapter(ContainerResponseWriter crw) {
            this.crw = crw;
        }

        @Override
        public OutputStream writeStatusAndHeaders(long contentLength, ContainerResponse response) throws IOException {
            this.response = response;
            return this.baos = new ByteArrayOutputStream();
        }

        @Override
        public void finish() throws IOException {
            OutputStream out = crw.writeStatusAndHeaders(baos.size(), response);
            out.write(baos.toByteArray());
            crw.finish();
        }
    }

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        response.setContainerResponseWriter(new Adapter(response.getContainerResponseWriter()));
        return response;
    }
}
