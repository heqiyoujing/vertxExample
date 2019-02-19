package io.example.mainVerticle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.Future.User;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: yiqq
 * @date: 2018/10/8
 * @description:
 */
public class BodyHandler {
    //    private Vertx vertx;
    private static BodyHandler bodyHandler = new BodyHandler();
    public Logger logger = LoggerFactory.getLogger(BodyHandler.class);

    public static BodyHandler bodyHandler() {
        return bodyHandler;
    }

    public static void getStr(RoutingContext rc){
        rc.response().putHeader("Content-type", "application/json; charset=UTF-8");
        rc.response().end("Hello world! 我!");
    }

    public static void getStr_1(RoutingContext rc){
        HttpServerResponse response = rc.response();
        // 由于我们会在不同的处理器里写入响应，因此需要启用分块传输,仅当需要通过多个处理器输出响应时才需要
        response.setChunked(true);
        response.write("我");
        rc.next();
        // 5 秒后调用下一个处理器
//        rc.vertx().setTimer(5000, tid ->  rc.next());
    }

    public static void getStr_2(RoutingContext rc){
        //*********获取参数*************
//        JsonObject reqBody = rc.getBodyAsJson();
//        int userId = reqBody.getValue("userId") instanceof Integer ? reqBody.getInteger("userId") : 0;
//        System.out.println(userId);
        //*********获取参数*************
        HttpServerResponse response = rc.response();
        response.write("和");
        rc.next();
    }

    public static void getStr_3(RoutingContext rc){
        HttpServerResponse response = rc.response();
        response.write("你");
        //**********添加结果*********
        JsonObject cartJson = new JsonObject();
        cartJson.put("hello", "我是一只程序员！");
        responseEndJson(rc, cartJson);
        //**********添加结果*********
//        rc.response().end();
    }

    private static void responseEndJson(RoutingContext context, JsonObject jsonObject) {
        context.response().setStatusCode(200)
                .putHeader("content-type", "application/json; charset=UTF-8")
                .end(jsonObject.encodePrettily());
    }

    public void getStr_4(RoutingContext rc){
        ObjectMapper om = new ObjectMapper();
        if ("1".equals("1")) {
            getOrder("1", res -> {
                if (res.succeeded()) {
                    try {
                        rc.response().putHeader("Content-type", "application/json; charset=UTF-8");
                        rc.response().end(om.writeValueAsString(res.result()));
                        System.out.println("我是第四个打印!");
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }else{
                    rc.response().setStatusCode(500)
                            .putHeader("Content-type", "application/json; charset=UTF-8")
                            .end((Buffer) new JsonObject().put("status", 0).put("des", "fali"));
                }
            });
            System.out.println("我是第一个打印!");
        }
    }

    /**另外一种方法getStr_4s*/
    public void getStr_4s(RoutingContext rc){
        ObjectMapper om = new ObjectMapper();
        if ("1".equals("1")) {
            Future<String> future = Future.future();
            getOrder("1",future.completer());
            future.setHandler(res->{
                if (res.succeeded()) {
                    try {
                        rc.response().putHeader("Content-type", "application/json; charset=UTF-8");
                        rc.response().end(om.writeValueAsString(res.result()));
                        System.out.println("我是第四个打印!");
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }else{
                    rc.response().setStatusCode(500)
                            .putHeader("Content-type", "application/json; charset=UTF-8")
                            .end((Buffer) new JsonObject().put("status", 0).put("des", "fali"));
                }
            });
            System.out.println("我是第一个打印!");
        }
    }


    private void getOrder(String orderId, Handler<AsyncResult<String>> handler){
        Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));
        vertx.<String>executeBlocking(future -> {
            try {
                Thread.sleep(1);
                System.out.println("我是第二个打印!");
                future.complete("成功！");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },false, res->{
            if (res.succeeded()) {
                System.out.println("我是第三个打印!");
                handler.handle(Future.succeededFuture(res.result()));
            } else {
                handler.handle(Future.failedFuture(res.cause()));
            }
        });
    }

    public void getStr_5(RoutingContext rc){
        Map<String, Object> map = new HashMap<>();
        final String[] name = {""};
        Future<Void> startFuture = Future.future();

        Future<Void> fut1 = Future.future();

        fut1.compose(v->{
            Future<Void> fut2 = Future.future();
            map.put("jj", "hello");
            System.out.println("map已经put");
            return fut2;
        }).compose(v->{
            name[0] = map.get("jj").toString();
            System.out.println("name已经取值");
        },
                startFuture );
        rc.response().end("success");
    }
}
