package me.preciouso.lobbynickcheck.MojangWrapper;

import com.ning.http.client.AsyncHandler;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;

import java.io.ByteArrayOutputStream;

// TODO -Xlint:unchecked
public class RequestHandler<T extends ResponseHandler> implements AsyncHandler<T> {
    private final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    private int statusCode = -1;

    @Override
    public void onThrowable(Throwable t) {

    }

    @Override
    public STATE onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
        bytes.write(bodyPart.getBodyPartBytes());
        return STATE.CONTINUE;
    }

    @Override
    public STATE onStatusReceived(HttpResponseStatus responseStatus) {
        statusCode = responseStatus.getStatusCode();
        if (statusCode >= 400) {
            return STATE.ABORT;
        }

        return STATE.CONTINUE;
    }

    @Override
    public STATE onHeadersReceived(HttpResponseHeaders headers) {
        return STATE.CONTINUE;
    }

    @Override
    public T onCompleted() throws Exception {
        ResponseHandler finalResp = new ResponseHandler(bytes.toString("UTF-8"), statusCode);
        return (T) finalResp;
    }
}
