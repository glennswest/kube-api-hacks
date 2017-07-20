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
 *
 * to test:
 *   mvn exec:java
 *   curl localhost:8080
 */
public class App {
    public static void main(String[] args) throws IOException {
        // oc whoami -t, also see doco for env variables configuration
        String accountToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZWZhdWx0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6InN1cGVyLWRlbW8tdXNlci10b2tlbi1jOWYxdCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJzdXBlci1kZW1vLXVzZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiI5MjVjOGVkMi02Y2ZmLTExZTctYmQyMS01MjU0MDBiMzdkMWEiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6ZGVmYXVsdDpzdXBlci1kZW1vLXVzZXIifQ.GCUB3KPmTSVUVKBMJPKA-zsz-Bnpq4-LgW5lzaSyb-shB-OZUa0QlBOgCd8BQrpP_Cp5VT52hYKIxsAf6agWffsKClowACQ_0Y_Qoe1fAexVh8jRRjPPz5jJPYZrJ4Gpr-7vVMkHAhw0ZFZShmWA0kvORIwBSCwE-ZROlaXDqQb86xgKa8OOaZBo2TeXfsMlHd_eQuFu4WgB6EVNFOj99x9QmTUAc-pgjepUMdAcLVUYe21o2XankXNxb3ZIPUzhfaQ1iY3_WGqZ8-C8Q2ds5Xq9g8t9Ejt9DsnXKwpiNfvOTM1lgs80lxUgiPVsCAYuiAPgO_GdNazTR3TYOfA-Yw";
        Config config = new ConfigBuilder().withOauthToken(accountToken).build();
        config.setMasterUrl("https://192.168.137.2:8443");
        config.setNamespace("default");

        // simple web server
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
