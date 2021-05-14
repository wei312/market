package com.market.base.framework.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.market.modules.api.model.entity.Api;
import com.market.modules.apiparam.model.entity.ApiParam;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.bson.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.XML;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author wei
 * @Date 2021/2/22 18:24
 **/
@Slf4j
public class Tools {


    public static void main(String[] args) {

        System.out.println("aliyun".equalsIgnoreCase("aliyun"));
    }

    /**
     * 获取当前工程IP和端口拼成 HTTP地址
     *
     * @return
     * @throws Exception
     */
    public static String getUrl() throws Exception {
        return "http://" + WebToolsUtils.getLocalIP() + ":9007";
    }

    /**
     * 获取UUID 替换-为空
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String now() {
        return now("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取当前 时间
     *
     * @param formart
     * @return
     */
    public static String now(String formart) {
        SimpleDateFormat sdf = new SimpleDateFormat(formart);
        return sdf.format(new Date());
    }

    /**
     * 获取全局ServletRequestAttributes
     *
     * @return
     */
    public static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
    }

    /**
     * 获取request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        return getServletRequestAttributes().getRequest();
    }

    /**
     * 获取response
     *
     * @return
     */
    public static HttpServletResponse getResponse() {
        return getServletRequestAttributes().getResponse();
    }

    /**
     * 格式化参数值
     *
     * @param object
     * @return
     */
    public static Object parseParam(Object object) {
        return (Object) String.valueOf(object).replaceAll("'", "''");
    }

    /**
     * 获取请求参数
     *
     * @param key
     * @return
     */
    public static String getParameter(String key) {
        HttpServletRequest request = getRequest();
        return request.getParameter(key);
    }

    /**
     * 校验请求参数是否必输
     *
     * @param list
     * @return
     */
    public static String parseParameters(List<ApiParam> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            boolean flag = false;
            String msg = "";
            for (ApiParam apiParam : list) {
                if ("true".equalsIgnoreCase(apiParam.getIsMust()) && StringUtils
                        .isEmpty(getParameter(apiParam.getKeyName()))) {
                    flag = true;
                    msg = ModelAndView.error(400, "参数" + apiParam.getKeyName() + "不能为空.").toJson();
                    break;
                }
            }

            if (flag) {
                log.info(" 参数校验异常 msg :{},状态:{}", msg, flag);
                return msg;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 获取REQUEST请求参数,设置到Map中
     *
     * @param list
     * @return
     */
    public static Map<String, String> getParameters(List<ApiParam> list) {
        Map<String, String> queryMap = new HashMap<>();
        list.stream().forEach(apiParam -> {
            queryMap.put(apiParam.getKeyName(), getParameter(apiParam.getKeyName()));
        });
        return queryMap;
    }

    /**
     * 调用ALI市场接口
     *
     * @param api
     * @param apiParamList
     * @return
     */
    public static String sendHttpConnection(Api api, List<ApiParam> apiParamList) {
        try {
            Map<String, String> headerMap = new HashMap();
            if (api.getApiService().equalsIgnoreCase("EntrAICDataQry")) {
                headerMap.put("User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; QQWubi 133; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; CIBA; InfoPath.2)");
            }
            headerMap.put("Authorization", "APPCODE " + api.getRunCode());
            // 组装请求参数,封装到MAP中
            Map<String, String> queryMap = Tools.getParameters(apiParamList);
            log.info("当前参数 host:{} path:{} method:{} headerMap:{} queryMap:{}", api.getHost(), api.getPath()
                    , api.getMethod(), headerMap, queryMap);
            HttpResponse response = null;
            if ("GET".equalsIgnoreCase(api.getMethod())) {
                log.info(" -> GET 请求");
                response = HttpUtils.doGet(api.getHost(), api.getPath(), api.getMethod(), headerMap, queryMap);
            } else if ("POST".equalsIgnoreCase(api.getMethod())) {
                log.info(" -> POST 请求");
//                String responseStr = HttpUtils
//                        .httpURLConnection(api.getHost(), api.getPath(), api.getMethod(), api.getRunCode(), queryMap);
//                return responseStr;
                response = HttpUtils
                        .doPost(api.getHost(), api.getPath(), api.getMethod(), headerMap, queryMap, queryMap);
            } else if ("ANY".equalsIgnoreCase(api.getMethod())) {
                log.info(" -> ANY 请求(用GET方式)");
                response = HttpUtils.doGet(api.getHost(), api.getPath(), api.getMethod(), headerMap, queryMap);
            }
            log.info("{}", response);
            StatusLine statusLine = response.getStatusLine();
            int httpCode = statusLine.getStatusCode();
            if (httpCode == 200) {
                String responseString = EntityUtils.toString(response.getEntity());
                if (StringUtils.isNotEmpty(responseString)) {
                    return responseString;
                } else {
                    return parseHeaders(response);
                }
            } else {
                String responseString = parseHeaders(response);
                if (StringUtils.isEmpty(responseString)) {
                    responseString = "{\"code\":" + httpCode + ",\"msg\":\"\"}";
                }
                return responseString;
            }
        } catch (Exception e) {
            log.error("调用接口失败 ,{}", e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 调用7M接口
     *
     * @return
     */
    public static String send7MHttpConnection(Api api, List<ApiParam> apiParamList) throws Exception {
        try {
            //查询坐席状态接口
            String account = getParameter("account");
            String time = now("yyyyMMddHHmmss");
            String sig = "";
            String auth = "";

            if (!account.startsWith("N")) {
                HttpResponse response = HttpUtils.doGet("https://register.7moor.com",
                        "/dingzhi/register/webSiteQueryAccountInfo/" + getParameter("account") + "/" + getParameter(
                                "secret"),
                        "GET", new HashMap<>(), new HashMap<>());
                String json = EntityUtils.toString(response.getEntity());
                Document doc = JsonUtil.json2doc(json);
                if (doc.getBoolean("success") != null && doc.getBoolean("success")) {
                    Document accountData = doc.get("accountData", Document.class);
                    account = accountData.getString("account");
                    sig = md5(account + accountData.getString("APISecret") + time);
                    auth = base64(account + ":" + time);
                }
                log.info("doc ->{}", doc);

            } else {
                sig = md5(parseMd5Url(apiParamList, new String[]{"account", "secret"}) + time);
                auth = base64(account + ":" + time);
            }

            Map<String, String> headerMap = new HashMap();
            getRequest().setCharacterEncoding(HTTP.UTF_8);
            headerMap.put("Accept", getRequest().getHeader("Accept"));
            headerMap.put("Content-Type", StringUtils.isNotEmpty(getRequest().getHeader("Content-Type")) ? getRequest()
                    .getHeader("Content-Type") : "application/json;charset=UTF-8");
            headerMap.put("Authorization", auth);
            String path = api.getPath() + account + "?sig=" + sig;
            // 组装请求参数,封装到MAP中
            Map<String, String> queryMap = Tools.getParameters(apiParamList);
            String responseString = "";
            log.info("当前参数 host:{} path:{} method:{} headerMap:{} queryMap:{}", api.getHost(), path
                    , api.getMethod(), headerMap, queryMap);
            getResponse().setCharacterEncoding(HTTP.UTF_8);
            if ("POST".equalsIgnoreCase(api.getMethod())) {
                HttpResponse response = HttpUtils
                        .doPost2(api.getHost(), path, api.getMethod(), headerMap, new HashMap<>(), queryMap);
                log.info("{}", response);
                // responseString = send7MPost(api, apiParamList);
                // 须加码传输,请求和返回结果都要加统一编码
                String charset = HTTP.UTF_8;
                String accept = getRequest().getHeader("Accept");
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(accept)) {
                    String[] accepts = accept.split(";");
                    if (accepts.length == 2) {
                        charset = accepts[1];
                    }
                }
                System.out.println(charset);
                responseString = EntityUtils.toString(response.getEntity(), charset);
            }

            log.info("{}", responseString);
            return responseString;
        } catch (Exception e) {
            log.error("调用接口失败 ,{}", e.getMessage());
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 发送7MPOST请求
     *
     * @param api
     * @param apiParamList
     * @return
     */
    public static String send7MPost(Api api, List<ApiParam> apiParamList) {
//        try {
//            String time = now("yyyyMMddHHmmss");
//            String sig = md5(parseMd5Url(apiParamList, new String[]{"account", "secret"}) + time);
//            //查询坐席状态接口
//            String account = getParameter("account");
//            String url = api.getHost() + api.getPath() + account + "?sig=" + sig;
//            String auth = base64(account + ":" + time);
//            HttpClientBuilder builder = HttpClientBuilder.create();
//            CloseableHttpClient client = builder.build();
//            HttpPost post = new HttpPost(url);
//            post.addHeader("Accept", "application/json");
//            post.addHeader("Content-Type", "application/json;charset=utf-8");
//            post.addHeader("Authorization", auth);
//            StringEntity requestEntity = null;
//            //根据需要发送的数据做相应替换
//            if (apiParamList != null) {
//                Map<String, String> bodys = getParameters(apiParamList);
//                StringBuilder stringBuilder = new StringBuilder();
//                for (String key : bodys.keySet()) {
//                    if ("exten".equalsIgnoreCase(key)) {
//                        stringBuilder.append("{").append("\"").append(key).append("\"").append(":")
//                                .append("\"").append(getParameter(key)).append("\"").append("}");
//                    }
//                }
//                requestEntity = new StringEntity(stringBuilder.toString(), "UTF-8");
//            }
//            CloseableHttpResponse response = null;
//            try {
//                response = client.execute(post);
//                HttpEntity entity = response.getEntity();
//                log.info("the response is : {}", EntityUtils.toString(entity, "utf8"));
//                return EntityUtils.toString(entity);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "";
//            } finally {
//                if (response != null) {
//                    try {
//                        response.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return "";
//            }
//        } catch (Exception e) {
//            log.error("调用接口失败 ,{}", e.getMessage());
//            e.printStackTrace();
//            return "";
//        }
        return "";
    }

    /**
     * BASE64加密
     *
     * @param text
     * @return
     */
    public static String base64(String text) {
        byte[] b = text.getBytes();
        Base64 base64 = new Base64();
        b = base64.encode(b);
        String s = new String(b);
        return s;
    }


    /**
     * 拼MD5加密字符串
     *
     * @param apiParamList
     * @param keys
     * @return
     */
    public static String parseMd5Url(List<ApiParam> apiParamList, String[] keys) {
        String value = "";
        for (String key : keys) {
            for (ApiParam apiParam : apiParamList) {
                if (key.equalsIgnoreCase(apiParam.getKeyName())) {
                    value += getParameter(apiParam.getKeyName());
                }
            }
        }
        return value;
    }

    public static String appendXML(String xmlJson) {
        if (!xmlJson.startsWith("<?xml")) {
            xmlJson = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root>" + xmlJson + "</root>";
        }
        return xmlJson;
    }

    /**
     * MDS加密
     *
     * @param text
     * @return
     */
    public static String md5(String text) {
        return DigestUtils.md5Hex(text).toUpperCase();
    }

    /**
     * JSON转XML
     *
     * @param json
     * @return
     */
    public static String jsontoxml(String json) {
        org.json.JSONObject jsonObject = new org.json.JSONObject(json);
        String xml = XML.toString(jsonObject);
        xml = appendXML(xml);
        log.info(" xml,{}", xml);
        return xml;
    }

    /**
     * XML转JSON
     *
     * @param xml
     * @return
     */
    public static String xmltojson(String xml) {
        org.json.JSONObject json = XML.toJSONObject(xml);
        return json.toString();
    }

    /**
     * 循环解析XML NODE节点
     *
     * @param xmlJson
     * @param nodes
     * @return
     */
    public static String parseXMLForeach(String xmlJson, String nodes) {
        xmlJson = parseXML(appendXML(xmlJson), "root");
        log.info(" root -->{}", xmlJson);
        return parseXMLForeach(xmlJson, Arrays.asList(nodes.split(",")));
    }

    /**
     * 循环解析XML NODE节点
     *
     * @param xmlJson
     * @param nodes
     * @return
     */
    public static String parseXMLForeach(String xmlJson, List<String> nodes) {
        String string = xmlJson;
        for (String node : nodes) {
            string = parseXML(appendXML(string), node);
            log.info(" -->,{}", string);
        }
        return string;
    }

    /**
     * 将JSON转成DOC解析
     *
     * @param json
     * @param keys
     * @return
     * @throws Exception
     */
    public static String parseDocument(String json, String keys) throws Exception {
        if (StringUtils.isEmpty(keys)) {
            return json;
        } else {
            return JsonUtil.xml2json(JsonUtil.json2xml(json), keys);
        }
    }


    /**
     * 把字符串切分为List<String>类型
     *
     * @param str
     * @return
     */
    public static List<String> splitAsList(String str) {
        return Arrays.asList(StringUtils.split(str, ","));
    }

    /**
     * 解析XML文件
     *
     * @param xmlJson
     * @param parentNode
     * @return
     */
    public static String parseXML(String xmlJson, String parentNode) {
        try {
            org.dom4j.Document document = DocumentHelper.parseText(xmlJson);
            Element root = document.getRootElement();
            List nodes = root.selectNodes(parentNode);
            log.info("--->{}", nodes.toString());
            return nodes.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

//    public static JSONArray parseJson(String json) {
//        String responseXML = parseXMLForeach(jsontoxml(json), "showapi_res_body,list");
//        return new JSONArray();
//    }

    /**
     * 解析固定JSON格式
     *
     * @param json
     * @return //
     */
    public static JSONArray parseJson(String json, String keys) {
        try {
            log.info("返回结果 ,{}", json);
            JSONObject jsonObject = JSONObject.parseObject(json);
            JSONArray jsonArray = new JSONArray();
            if (StringUtils.isEmpty(keys)) {
                jsonArray.add(jsonObject);
                return jsonArray;
            } else {
                String[] keyStr = keys.split(",");
                JSONObject parseObject = new JSONObject(jsonObject);
                for (int i = 0; i < keyStr.length; i++) {
                    JSONArray parseJSONArray = parseJSONKey(keyStr[i], parseObject);
                    if (keyStr.length == i) {
                        jsonArray = parseJSONArray;
                    } else {
                        parseObject = parseJSONArray.getObject(0, JSONObject.class);
                    }
                    log.info("keys->{},parseObject ->{}", keys, parseObject);
                }
                jsonArray.add(parseObject);
                return jsonArray;
            }
//            // 匹配天气,交通车辆违章等接口
//            if (jsonObject.getJSONObject("showapi_res_body") != null) {
//                JSONObject bodyJSONObject = jsonObject.getJSONObject("showapi_res_body");
//                if (bodyJSONObject.getJSONArray("list") != null) {
//                    jsonArray = bodyJSONObject.getJSONArray("list");
//                    return jsonArray;
//                }
//                if (bodyJSONObject.getJSONArray("records") != null) {
//                    jsonArray = bodyJSONObject.getJSONArray("records");
//                    return jsonArray;
//                }
//                if (bodyJSONObject.getJSONArray("data") != null) {
//                    jsonArray = bodyJSONObject.getJSONArray("data");
//                    return jsonArray;
//                }
//                // 匹配银行接口
//                jsonArray.add(jsonObject.getJSONObject("showapi_res_body"));
//                return jsonArray;
//            }
//            // 匹配身份证接口等
//            String jsonResult = jsonObject.getString("result");
//            Object jjson = JSON.toJSON(jsonResult);
//            if (jjson instanceof JSONObject) {
//                jsonArray.add(jsonObject.getJSONObject("result"));
//                return jsonArray;
//            } else if (jjson instanceof JSONArray) {
//                return jsonObject.getJSONArray("result");
//            }
//            // 企业关键字查询接口
//            if (jsonObject.getJSONObject("Result") != null) {
//                jsonArray.add(jsonObject.getJSONObject("Result"));
//                return jsonArray;
//            }
//            jsonArray.add(jsonObject);
//            return jsonArray;
        } catch (Exception e) {
            log.error("解析接口失败 ,异常:{} ,参数:{}", e.getMessage(), json);
            return new JSONArray();
        }
    }

    /**
     * 按照KEY解析JSONObject
     *
     * @param key
     * @param jsonObject
     * @return
     */
    public static JSONArray parseJSONKey(String key, JSONObject jsonObject) throws Exception {
        String jsonResult = jsonObject.getString(key);
        Object jjson = JSON.toJSON(jsonResult);
        JSONArray jsonArray = new JSONArray();
        if (jjson instanceof JSONObject) {
            jsonArray.add(jsonObject.getJSONObject(key));
        } else if (jjson instanceof JSONArray) {
            jsonArray = jsonObject.getJSONArray(key);
        }
        return jsonArray;
    }

    /**
     * 解析ALI 返回非200的异常Header
     *
     * @param response
     * @return
     */
    public static String parseHeaders(HttpResponse response) {
        Header header = response.getFirstHeader("X-Ca-Error-Message");
        String msg = "";
        if (header != null) {
            String error = header.getValue();
            if (error.equals("Invalid AppCode")) {
                msg = "AppCode错误 ";
            } else if (error.equals("Invalid Url")) {
                msg = "请求的 Method、Path 或者环境错误";
            } else if (error.equals("Invalid Param Location")) {
                msg = "参数错误";
            } else if (error.equals("Unauthorized")) {
                msg = "服务未被授权（或URL和Path不正确）";
            } else if (error.equals("Quota Exhausted")) {
                msg = "套餐包次数用完";
            } else {
                msg = "参数名错误 或 其他错误";
            }
        }
        return msg;
    }


    /**
     * 拼接URL,加码转义
     *
     * @param apiParamList
     * @param apiService
     * @return
     * @throws Exception
     */
    public static String assemblyParameterUrl(List<ApiParam> apiParamList, String apiService, String userName)
            throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getUrl())
                .append("/api").append("/").append(apiService).append("?userName=").append(userName);
        apiParamList.stream().forEach(apiParam -> {
            try {
                stringBuilder.append("&").append(apiParam.getKeyName()).append("=")
                        .append(URLEncoder.encode(apiParam.getExampleValue(), "utf-8"));
            } catch (Exception e) {
                e.printStackTrace();
                log.error("参数设置异常 ->{}", e.getMessage());
            }
        });
        return stringBuilder.toString();
    }

    /**
     * 解析源系统类型
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String parseSourceType(String url) throws Exception {
        if (StringUtils.isEmpty(url)) {
            return "aliyun";
        }
        String type = "aliyun";
        if ("aliyun".contains(url)) {
            type = "aliyun";
        } else if ("tencent".contains(url)) {
            type = "tencent";
        } else if ("jisuapi".contains(url)) {
            type = "jisuapi";
        } else if ("7moor".contains(url)) {
            type = "7moor";
        }
        return type;
    }


}
