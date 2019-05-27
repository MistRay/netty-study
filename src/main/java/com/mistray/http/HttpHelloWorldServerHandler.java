package com.mistray.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mistray.http.pojo.User;
import com.mistray.http.serialize.impl.JSONSerializer;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author MistRay
 * @Project netty-study
 * @Package com.mistray.http
 * @create 2019年05月24日 16:34
 * @Desc 执行
 */
public class HttpHelloWorldServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    public static final Logger LOGGER = LoggerFactory.getLogger(HttpHelloWorldServerHandler.class);

    private HttpHeaders headers;
    private HttpRequest request;
    private FullHttpRequest fullRequest;

    private static final String FAVICON_ICO = "/favicon.ico";
    private static final AsciiString CONTENT_TYPE = AsciiString.cached("Content-Type");
    private static final AsciiString CONTENT_LENGTH = AsciiString.cached("Content-Length");
    private static final AsciiString CONNECTION = AsciiString.cached("Connection");
    private static final AsciiString KEEP_ALIVE = AsciiString.cached("keep-alive");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        User user = new User();
        user.setDate(new Date());
        user.setUserName("MistRay");
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;
            headers = request.headers();
            String uri = request.uri();
            LOGGER.info("http uri : " + uri);
            if (uri.equals(FAVICON_ICO)) {
                return;
            }
            HttpMethod method = request.method();
            if (method.equals(HttpMethod.GET)) {
                QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri, Charsets.toCharset(CharEncoding.UTF_8));
                // 获取到请求参数 k = v 形式
                Map<String, List<String>> parameters = queryStringDecoder.parameters();
                for (Map.Entry<String, List<String>> attr : parameters.entrySet()) {
                    for (String attrVal : attr.getValue()) {
                        LOGGER.info(attr.getKey() + " = " + attrVal);
                    }
                }
                user.setMethod("get");
            } else if (method.equals(HttpMethod.POST)) {
                // Post请求需要从消息体重取数据,所以需要把msg换成FullHttpRequest
                fullRequest = (FullHttpRequest) msg;
                // 根据不同的Content_Type处理body数据
                dealWithContentType();
                user.setMethod("post");
            }
            JSONSerializer jsonSerializer = new JSONSerializer();
            byte[] content = jsonSerializer.serialize(user);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(content));
            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
            boolean keepAlive = HttpUtil.isKeepAlive(request);
            if (!keepAlive) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(CONNECTION, KEEP_ALIVE);
                ctx.write(response);
            }

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void dealWithContentType() {
        String contentType = getContentType();
        if (contentType.equals("application/json")) {
            String jsonStr = fullRequest.content().toString(Charsets.toCharset(CharEncoding.UTF_8));
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            for (Map.Entry<String, Object> item : jsonObject.entrySet()) {
                LOGGER.info(item.getKey() + "=" + item.getValue().toString());
            }

        } else if (contentType.equals("applicatin/x-www-form-urlencoded")) {
            // 方式一:使用 QueryStringDecoder
            String jsonStr = fullRequest.content().toString(Charsets.toCharset(CharEncoding.UTF_8));
            QueryStringDecoder queryDecoder = new QueryStringDecoder(jsonStr, false);
            Map<String, List<String>> uriAttributes = queryDecoder.parameters();
            for (Map.Entry<String, List<String>> attr : uriAttributes.entrySet()) {
                for (String attrVal : attr.getValue()) {
                    LOGGER.info(attr.getKey() + "=" + attrVal);

                }
            }
        } else if (contentType.equals("multipart/form-data")) {
            // 用于大文件上传
        }
    }

    private String getContentType() {
        String typeStr = headers.get("Content-Type").toString();
        String[] list = typeStr.split(";");
        return list[0];
    }
}
