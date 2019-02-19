package io.example;
import io.vertx.core.AbstractVerticle;
/**
 * @author: yiqq
 * @date: 2018/9/25
 * @description:
 */
public class MyFirstVerticle extends AbstractVerticle {
    private String str = "Hello World! My Dream!";
    // Called when verticle is deployed
    // Verticle部署时调用
    public void start() {
        vertx.createHttpServer().requestHandler(req -> {
            req.response()
                    .putHeader("content-type", "text/plain")
                    .end(str);
//         	HttpServerResponse response = req.response();
//        	response.putHeader("Content-Type", "text/plain");
//        	response.write("Hello World!");
//        	response.end();
        }).listen(8080);

        vertx.createHttpServer().requestHandler(req -> {
            req.response()
                    .putHeader("content-type", "text/plain")
                    .end("some text");
        }).listen(8081);

    }

    // Optional - called when verticle is undeployed
    // 可选 - Verticle撤销时调用
    public void stop() {
    }
}
