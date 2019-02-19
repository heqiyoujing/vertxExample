package io.example.Future;

import io.vertx.ext.web.RoutingContext;

import java.util.List;
//import java.util.concurrent.Future;
import io.vertx.core.Future;

/**
 * @author: yiqq
 * @date: 2018/10/25
 * @description:
 */
public class FutureAll {

    private void start(RoutingContext rc){
        Future<List<User>> future = Future.future();
        future.complete();//异步调用
        //异步返回结果
        future.setHandler(res->{
            if (res.succeeded()) {
                List<User> list= res.result();
                rc.put("list", list);
            }else {
                rc.fail(400);
            }
        });
    }
}
