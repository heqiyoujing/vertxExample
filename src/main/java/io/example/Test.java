package io.example;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

/**
 * @author: yiqq
 * @date: 2018/9/30
 * @description:
 */
public class Test {
    public static void main(String[] args) {
        // 注意要添加对应的集群管理器依赖，详情见集群管理器章节
        VertxOptions options = new VertxOptions();
        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                Vertx vertx = res.result(); // 获取到了集群模式下的 Vertx 对象
                // 做一些其他的事情
            } else {
                // 获取失败，可能是集群管理器出现了问题
            }
        });

        //----------------------------------------------------------------------------
        //VertxOptions对象有很多配置，包括集群、高可用、池大小等。在Javadoc中描述了所有配置的细节。
        Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));

        //----------------------------------------------------------------------------
        vertx.setPeriodic(1000, id -> {
            // This handler will get called every second
            // 这个处理器将会每隔一秒被调用一次
            System.out.println("timer fired!");
        });

        //----------------------------------------------------------------------------
        vertx.executeBlocking(future -> {
            // 调用一些需要耗费显著执行时间返回结果的阻塞式API
            String result = "hello";
            future.complete(result);
        }, res -> {
            System.out.println("The result is: " + res.result());
        });

        //----------------------------------------------------------------------------
        WorkerExecutor executor = vertx.createSharedWorkerExecutor("my-worker-pool");
        executor.executeBlocking(future -> {
            // 调用一些需要耗费显著执行时间返回结果的阻塞式API
            String result = "hello";
            future.complete(result);
        }, res -> {
            System.out.println("The result is: " + res.result());
        });
        //Worker Executor 在不需要的时候必须被关闭
        executor.close();

        //----------------------------------------------------------------------------
        FileSystem fs = vertx.fileSystem();
        Future<Void> startFuture = Future.future();

        Future<Void> fut1 = Future.future();
        fs.createFile("/foo", fut1.completer());

        fut1.compose(v -> {
            // fut1中文件创建完成后执行
            Future<Void> fut2 = Future.future();
            fs.writeFile("/foo", Buffer.buffer(), fut2.completer());
            return fut2;
        }).compose(v -> {
                    // fut2文件写入完成后执行
                    fs.move("/foo", "/bar", startFuture.completer());
                },
                // 如果任何一步失败，将startFuture标记成failed
                startFuture);
        //----------------------------------------------------------------------------
    }

    public static void create(RoutingContext rc){
        HttpServerRequest request = rc.request();
        //1
        request.response().putHeader("Content-Type", "text/plain").write("some text").end();
        //2
        HttpServerResponse response = request.response();
        response.putHeader("Content-Type", "text/plain");
        response.write("some text");
        response.end();
    }
}
