package io.example;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: yiqq
 * @date: 2018/9/30
 * @description:
 */
public class PublicClass {

    private static Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));

    public static void main(String[] args) {
//        json();
//        jsonArray();
//        buffer();

    }

    /**
     * JSON对象
     */
    public static void json(){
        //创建 JSON 对象方式一
        String jsonString = "{\"foo\":\"bar\"}";
        JsonObject object = new JsonObject(jsonString);
        System.out.println(object);

        //创建 JSON 对象方式二
        Map<String, Object> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("xyz", 3);
        JsonObject object_2 = new JsonObject(map);
        System.out.println(object_2);

        //将键值对放入 JSON 对象
        JsonObject object_3 = new JsonObject();
        object_3.put("foo", "bar").put("num", 123).put("mybool", true);
        System.out.println(object_3);

        //从 JSON 对象获取值
        String val = object_2.getString("foo-key");
        int intVal = object_2.getInteger("xyz");
        System.out.println("foo-key: " + val +  "，xyz： " + intVal);
    }

    /**
     * JSON数组
     */
    public static void jsonArray(){
        //创建 JSON 数组
        String jsonString = "[\"foo\",\"bar\"]";
        JsonArray array = new JsonArray(jsonString);
        System.out.println(array);

        //将数组项添加到JSON数组
        JsonArray array_1 = new JsonArray();
        array_1.add("foo").add(123).add(false);
        System.out.println(array_1);

        //从 JSON 数组中获取值
        String val = array_1.getString(0);
        Integer intVal = array_1.getInteger(1);
        Boolean boolVal = array_1.getBoolean(2);
        System.out.println(val + "， " + intVal + "， " + boolVal);
    }

    /**
     * Buffer
     */
    public static void buffer(){
        //创建buffer
        Buffer buff = Buffer.buffer("some string"/*, "UTF-8"*/);
        System.out.println(buff);

        //从字节数组 byte[] 创建 Buffer
        byte[] bytes = new byte[] {1, 3, 5};
        Buffer buff_1 = Buffer.buffer(bytes);
        System.out.println(buff_1);

        //数据追加到Buffer
        Buffer buff_2 = Buffer.buffer();
        buff_2.appendInt(123).appendString("hello\n");
        System.out.println(buff_2);

        //随机访问写Buffer
        Buffer buff_3 = Buffer.buffer();
        buff_3.setInt(1000, 123);
        buff_3.setString(0, "hello");
        System.out.println(buff_3);
    }

    /**
     * 创建tcp客户端和服务端
     */
    public static void tcp(){
        //创建 TCP 服务端
        NetServerOptions options = new NetServerOptions().setPort(4321);
        NetServer server = vertx.createNetServer(options);

        //服务端监听
        server.listen(1234, "localhost", res -> {
            if (res.succeeded()) {
                System.out.println("Server is now listening!");
            } else {
                System.out.println("Failed to bind!");
            }
        });

        //监听随机端口
        server.listen(0, "localhost", res -> {
            if (res.succeeded()) {
                System.out.println("Server is now listening on actual port: " + server.actualPort());
            } else {
                System.out.println("Failed to bind!");
            }
        });

        //接收传入连接的通知
        server.connectHandler(socket -> {
            // 在这里处理传入连接
        });

        //从Socket读取数据
        server.connectHandler(socket -> {
            socket.handler(buffer -> {
                System.out.println("I received some bytes: " + buffer.length());
            });

            //关闭处理器
            socket.closeHandler(v -> {
                System.out.println("The socket has been closed");
            });
        });

        //关闭 TCP 服务端
        server.close(res -> {
            if (res.succeeded()) {
                System.out.println("Server is now closed");
            } else {
                System.out.println("close failed");
            }
        });

        //创建配置 TCP 客户端
        NetClientOptions option = new NetClientOptions().setConnectTimeout(10000);
        NetClient client = vertx.createNetClient(option);
        //创建连接
        client.connect(4321, "localhost", res -> {
            if (res.succeeded()) {
                System.out.println("Connected!");
                NetSocket socket = res.result();
            } else {
                System.out.println("Failed to connect: " + res.cause().getMessage());
            }
        });

        //配置连接重试
        option.setReconnectAttempts(10).setReconnectInterval(500);


    }



}
