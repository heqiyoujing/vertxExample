package io.example;

import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

/**
 * @author: yiqq
 * @date: 2018/9/25
 * @description:
 */
public class Main {
    public static void main(String[] args){

        //VertxOptions对象有很多配置，包括集群、高可用、池大小等。在Javadoc中描述了所有配置的细节。
        Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));

        JsonObject config = new JsonObject().put("name", "tim").put("directory", "/blah");
        DeploymentOptions options = new DeploymentOptions().setConfig(config);

        vertx.deployVerticle(MyFirstVerticle.class.getName(), options);
        System.out.println("Configuration: " + config.getString("name"));

        System.getProperty("prop");
        System.getenv("HOME");

        //Context 对象
        Context context = vertx.getOrCreateContext();
        if (context.isEventLoopContext()) {
            System.out.println("Context attached to Event Loop");
        } else if (context.isWorkerContext()) {
            System.out.println("Context attached to Worker Thread");
        } else if (context.isMultiThreadedWorkerContext()) {
            System.out.println("Context attached to Worker Thread - multi threaded worker");
        } else if (! Context.isOnVertxThread()) {
            System.out.println("Context not attached to a thread managed by vert.x");
        }
        vertx.getOrCreateContext().runOnContext(v -> {
            System.out.println("This will be executed asynchronously in the same context");
        });

        final Context contexts = vertx.getOrCreateContext();
        contexts.put("data", "hello");
        contexts.runOnContext((v) -> {
            String hello = contexts.get("data");
            System.out.println(hello);
        });

        //一次性计时器
        long timerID = vertx.setTimer(1000, id -> {
            System.out.println("And one second later this is printed");
        });

        System.out.println("First this is printed");

        //周期性计时器
        long timerIDs = vertx.setPeriodic(1000, id -> {
            System.out.println("And every second this is printed");
        });

        System.out.println("First this is printed");

        //取消计时器
        vertx.cancelTimer(timerIDs);

        //注册处理器
        EventBus eb = vertx.eventBus();

        eb.consumer("news.uk.sport", message -> {
            System.out.println("I have received a message: " + message.body());
        });

        MessageConsumer<String> consumer = eb.consumer("news.uk.sport");
        consumer.handler(message -> {
            System.out.println("I have received a message: " + message.body());
        });

        //下一个

       /* vertx.deployVerticle("io.example.MyFirstVerticle", res -> {
            if (res.succeeded()) {
                System.out.println("Deployment id is: " + res.result());
            } else {
                System.out.println("Deployment failed!");
            }
        });*/
    }
}
