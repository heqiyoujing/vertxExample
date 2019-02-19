package io.example.mainVerticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;

/**
 * @author: yiqq
 * @date: 2018/10/8
 * @description: **********重点是这个类**********
 */
public class FirstMain extends AbstractVerticle {

    private static Logger logger = LoggerFactory.getLogger(FirstMain.class);

    public void start() {

        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.get("/hang/some").handler(routingContext -> { //指定get方法

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
//		router.route("/static/*").handler(StaticHandler.create());
        //处理请求并调用下一个处理器
        router.route(HttpMethod.POST,"/hang/add").handler(BodyHandler::getStr_1);//OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT, PATCH, OTHER
        router.route("/hang/add").handler(BodyHandler::getStr_2);
        router.route("/hang/add").handler(BodyHandler::getStr_3);

        router.route("/hello").blockingHandler(BodyHandler.bodyHandler()::getStr_4, false);
        router.route("/hello_4s").blockingHandler(BodyHandler.bodyHandler()::getStr_4s, false);
        router.route("/helloWorld").blockingHandler(BodyHandler.bodyHandler()::getStr_5, false);

        server.requestHandler(router::accept).listen(8088);
    }


    public static void main(String[] args) {
//        Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));
//        vertx.deployVerticle(FirstMain.class.getName());
//        System.out.println("vertx......启动");
        Vertx.clusteredVertx(new VertxOptions(), res->{
            if (res.succeeded()) {
                res.result().deployVerticle(FirstMain.class.getName());
                logger.info("success start!" );
                System.out.println("success start!" );
            } else {
                logger.info("Failed: " + res.cause());
            }
        });

    }
}
