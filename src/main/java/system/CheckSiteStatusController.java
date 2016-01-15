package system;

import system.controller.CustomBackOff;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandler;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.springframework.stereotype.Controller;

import java.util.logging.Logger;

/**
 * CheckSiteStatusController.
 */
@Controller
public class CheckSiteStatusController {

    /**
     * WEB_CLIENT.
     */
    private static final HttpRequestFactory WEB_CLIENT;

    static {
        WEB_CLIENT = new NetHttpTransport()
                .createRequestFactory(new HttpRequestInitializer() {

                private final int timeout = 45000;

                @Override
                public void initialize(final HttpRequest request) {
                    request.setThrowExceptionOnExecuteError(false);
                    request.setReadTimeout(timeout);
                    request.setConnectTimeout(timeout);
                }
            }
        );
    }

    /**
     * checkSiteStatus.
     * @param strUrl value.
     * @return test results.
     */
    public final boolean checkSiteStatus(final String strUrl) {
        final int goodStatusCode = 200;
        try {
            HttpRequest request = WEB_CLIENT
                    .buildGetRequest(new GenericUrl(strUrl));
            configure(request);
            final HttpResponse response = request.execute();
            Logger.getGlobal().info(String.valueOf(response.getStatusCode()));
            return  response.getStatusCode() == goodStatusCode;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * configure.
     * @param request HttpRequest.
     */
    protected final void configure(final HttpRequest request) {
        request.setUnsuccessfulResponseHandler(new
                HttpBackOffUnsuccessfulResponseHandler(new CustomBackOff()));
    }
}
