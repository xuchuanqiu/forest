package org.forest.backend.httpclient;

import org.forest.backend.httpclient.executor.*;
import org.forest.backend.httpclient.handler.DefaultHttpclientResultHandler;
import org.forest.config.ForestConfiguration;
import org.forest.exceptions.ForestRuntimeException;
import org.forest.backend.HttpExecutor;
import org.forest.backend.HttpBackend;
import org.forest.backend.httpclient.conn.HttpclientConnectionManager;
import org.forest.backend.httpclient.request.AsyncHttpclientRequestSender;
import org.forest.backend.httpclient.request.HttpclientRequestSender;
import org.forest.backend.httpclient.request.SyncHttpclientRequestSender;
import org.forest.backend.httpclient.response.HttpclientResponseHandler;
import org.forest.handler.ResponseHandler;
import org.forest.handler.ResultHandler;
import org.forest.http.ForestRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gongjun[jun.gong@thebeastshop.com]
 * @since 2017-04-20 18:23
 */
public class HttpclientBackend implements HttpBackend {

    private final static Map<String, HttpExecutorCreator> executorCreatorMap = new HashMap<>();

    static {
        executorCreatorMap.put("GET", new HttpExecutorCreator() {
            @Override
            public HttpExecutor createExecutor(HttpclientConnectionManager connectionManager, ForestRequest request, ResponseHandler responseHandler) {
                return new HttpclientGetExecutor(request,
                        getHttpclientResponseHandler(request, responseHandler),
                        getRequestSender(connectionManager, request));
            }
        });

        executorCreatorMap.put("HEAD", new HttpExecutorCreator() {
            @Override
            public HttpExecutor createExecutor(HttpclientConnectionManager connectionManager, ForestRequest request, ResponseHandler responseHandler) {
                return new HttpclientHeadExecutor(request,
                        getHttpclientResponseHandler(request, responseHandler),
                        getRequestSender(connectionManager, request));
            }
        });

        executorCreatorMap.put("DELETE", new HttpExecutorCreator() {
            @Override
            public HttpExecutor createExecutor(HttpclientConnectionManager connectionManager, ForestRequest request, ResponseHandler responseHandler) {
                return new HttpclientDeleteExecutor(request,
                        getHttpclientResponseHandler(request, responseHandler),
                        getRequestSender(connectionManager, request));
            }
        });


        executorCreatorMap.put("OPTIONS", new HttpExecutorCreator() {
            @Override
            public HttpExecutor createExecutor(HttpclientConnectionManager connectionManager, ForestRequest request, ResponseHandler responseHandler) {
                return new HttpclientOptionsExecutor(request,
                        getHttpclientResponseHandler(request, responseHandler),
                        getRequestSender(connectionManager, request));
            }
        });

        executorCreatorMap.put("TRACE", new HttpExecutorCreator() {
            @Override
            public HttpExecutor createExecutor(HttpclientConnectionManager connectionManager, ForestRequest request, ResponseHandler responseHandler) {
                return new HttpclientTraceExecutor(request,
                        getHttpclientResponseHandler(request, responseHandler),
                        getRequestSender(connectionManager, request));
            }
        });

        executorCreatorMap.put("POST", new HttpExecutorCreator() {
            @Override
            public HttpExecutor createExecutor(HttpclientConnectionManager connectionManager, ForestRequest request, ResponseHandler responseHandler) {
                return new HttpclientPostExecutor(request,
                        getHttpclientResponseHandler(request, responseHandler),
                        getRequestSender(connectionManager, request));
            }
        });

        executorCreatorMap.put("PUT", new HttpExecutorCreator() {
            @Override
            public HttpExecutor createExecutor(HttpclientConnectionManager connectionManager, ForestRequest request, ResponseHandler responseHandler) {
                return new HttpclientPutExecutor(request,
                        getHttpclientResponseHandler(request, responseHandler),
                        getRequestSender(connectionManager, request));
            }
        });

        executorCreatorMap.put("PATCH", new HttpExecutorCreator() {
            @Override
            public HttpExecutor createExecutor(HttpclientConnectionManager connectionManager, ForestRequest request, ResponseHandler responseHandler) {
                return new HttpclientPatchExecutor(request,
                        getHttpclientResponseHandler(request, responseHandler),
                        getRequestSender(connectionManager, request));
            }
        });
    }


    private static HttpclientResponseHandler getHttpclientResponseHandler(ForestRequest request, ResponseHandler responseHandler) {
        return new HttpclientResponseHandler(request, responseHandler);
    }

    @SuppressWarnings("deprecation")
    private static HttpclientRequestSender getRequestSender(HttpclientConnectionManager connectionManager, ForestRequest request) {
        if (request.isAsync()) {
            return new AsyncHttpclientRequestSender(connectionManager, request);
        }
        return new SyncHttpclientRequestSender(connectionManager, request);
    }


    private final HttpclientConnectionManager connectionManager;

    public HttpclientBackend(ForestConfiguration configuration) {
        this.connectionManager = new HttpclientConnectionManager(configuration);
    }


    @Override
    public String getName() {
        return "HttpClient";
    }

    @Override
    public ResultHandler getDefaultResultHandler() {
        return new DefaultHttpclientResultHandler();
    }

    @Override
    public HttpExecutor createExecutor(ForestRequest request, ResponseHandler responseHandler) {
        String key = request.getType().toUpperCase();
        HttpExecutorCreator httpExecutorCreator = executorCreatorMap.get(key);
        if (httpExecutorCreator == null) {
            throw new ForestRuntimeException("Http request type\"" + key + "\" is not be supported.");
        }
        HttpExecutor executor = httpExecutorCreator.createExecutor(connectionManager, request, responseHandler);
        return executor;
    }

    private interface HttpExecutorCreator {
        HttpExecutor createExecutor(HttpclientConnectionManager connectionManager, ForestRequest request, ResponseHandler responseHandler);
    }
}