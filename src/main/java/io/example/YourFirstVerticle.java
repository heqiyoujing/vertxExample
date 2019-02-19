package io.example;

import io.example.mainVerticle.BodyHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;


/**
 * @author: yiqq
 * @date: 2018/10/8
 * @description:
 */
public class YourFirstVerticle extends AbstractVerticle {
    public void start() {

        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.get("/hang/some").handler(routingContext -> {

            // 所有的请求都会调用这个处理器处理
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain");

            // 写入响应并结束处理
            response.end("Hello World from Vert.x-Web!");
        });
        router.route("/hang/all").handler(routingContext -> {

            // 所有的请求都会调用这个处理器处理
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain");

            // 写入响应并结束处理
            response.end("Hello World !");
        });
		router.route("/hang/put").handler(BodyHandler::getStr);

		router.route("/hang/add").handler(BodyHandler::getStr_1);
		router.route("/hang/add").handler(BodyHandler::getStr_2);
		router.route("/hang/add").handler(BodyHandler::getStr_3);

//		router.route("/static/*").handler(StaticHandler.create());

        server.requestHandler(router::accept).listen(8080);
    }

}
