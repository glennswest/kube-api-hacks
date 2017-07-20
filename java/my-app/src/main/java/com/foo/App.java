package com.foo;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * simple kube api call using
 * https://github.com/fabric8io/fabric8/tree/master/components/kubernetes-api
 */
public class App {
    public static void main(String[] args) throws IOException {
        String accountToken = "5UyzEQXm__XkAO-wh3sVmMstisUjpt4odEWDeU9Q2ws";
        Config config = new ConfigBuilder().withOauthToken(accountToken).build();
        config.setMasterUrl("https://192.168.137.2:8443");
        config.setNamespace("default");

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new MyHandler(config));
        server.start();
    }

    static class MyHandler implements HttpHandler {

        private Config config;
        private KubernetesClient kube;

        public MyHandler(Config config) {
            this.config = config;
            kube = new DefaultKubernetesClient(config);
        }

        @Override
        public void finalize() {
            kube.close();
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            PodList pods = kube.pods().list();
            StringBuffer response = new StringBuffer();
            for (Pod pod : pods.getItems()) {
                String name = pod.getMetadata().getName();
                String ip = pod.getStatus().getPodIP();
                response.append("<h2>name: " + name + "</h2>");
            }
            t.getResponseHeaders().set("Content-Type", "text/html");
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        }
    }
}
