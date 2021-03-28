import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class KVStoreHttpHandler implements HttpHandler {
    private KVStore kvStore = new KVStore();

    @Override
    public void handle(HttpExchange httpExchange) {
        if("GET".equals(httpExchange.getRequestMethod())) {
            handleGetRequest(httpExchange);
        } else if ("PUT".equals(httpExchange.getRequestMethod())) {
            handlePutRequest(httpExchange);
        } else if ("DELETE".equals(httpExchange.getRequestMethod())) {
            handleDeleteRequest(httpExchange);
        }
    }

    private void handleDeleteRequest(HttpExchange httpExchange) {
        String key = getRequestParameter(httpExchange);
        kvStore.delete(key);
        handleResponse(httpExchange);
    }

    private void handlePutRequest(HttpExchange httpExchange) {
        String key = getRequestParameter(httpExchange);
        String value = getRequestBody(httpExchange);
        kvStore.put(key, value);
        handleResponse(httpExchange);
    }

    private void handleGetRequest(HttpExchange httpExchange) {
        String key = getRequestParameter(httpExchange);
        String value = kvStore.get(key);
        handleResponse(httpExchange, value);
    }

    private void handleResponse(HttpExchange httpExchange, String response) {
        try {
            if (response != null) {
                // Key exists in store
                httpExchange.sendResponseHeaders(200, response.getBytes().length);
                setResponseBody(httpExchange, response);
            } else {
                // Key doesn't exist in store
                httpExchange.sendResponseHeaders(404, -1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleResponse(HttpExchange httpExchange) {
        try {
            httpExchange.sendResponseHeaders(204, -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setResponseBody(HttpExchange httpExchange, String response) throws IOException {
        // Set content of response body
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.flush();
        os.close();
    }

    private String getRequestParameter(HttpExchange httpExchange) {
        // Get parameter from URI
        return httpExchange
                .getRequestURI()
                .toString()
                .split("/")[1];
    }

    private String getRequestBody(HttpExchange httpExchange) {
        int i;
        StringBuilder sb = new StringBuilder();
        InputStream ios = httpExchange.getRequestBody();

        // Get content from request body
        try {
            while ((i = ios.read()) != -1) {
                sb.append((char) i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
