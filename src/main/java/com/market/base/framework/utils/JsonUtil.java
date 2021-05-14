package com.market.base.framework.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.json.JSONException;
import org.json.XML;

@Slf4j
public class JsonUtil {

    private static Gson gson = new GsonBuilder().create(); //null bean转json null值不输出
    private static Gson gsonNull = new GsonBuilder().serializeNulls().create(); //bean转json时null值会输出

    public static Gson getGson() {
        return gson;
    }

    public static Gson getGsonNull() {
        return gsonNull;
    }


    /**
     * 把单个json字符串转换为java bean对像
     * {"user":"0","arg":"1"}
     *
     * @param jsonStr
     */
    public static <T> T json2Bean(String jsonStr, Class<T> clazz) {
        T t = null;
        try {
            t = JsonUtil.getGson().fromJson(jsonStr, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error:json string cannot convert to java bean " + clazz.getSimpleName());
        }
        return t;
    }

    /**
     * json字符串转换为document对像格式 :{"a":1,"b":"2"}
     *
     * @param json
     * @return
     */
    public static Document json2doc(String json) {
        return Document.parse(json.trim());
    }

    /**
     * json字符串数组转换为List<Document>对像
     * 格式:[{1},{2},{3}]
     *
     * @param json
     * @return
     */
    public static List<Document> jsonArray2docs(String json) {
        if (json == null) {
            return null;
        }
        ArrayList<Document> docList = new ArrayList<Document>();
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();
        for (JsonElement el : jsonArray) {
            Document doc = Document.parse(el.toString());
            docList.add(doc);
        }

        return docList;
    }

    /**
     * Json数组字符串转换为 List<java bean>对像
     *
     * @param json 格式为：[{"A":1},{"B":2}...]
     * @param clazz 格式为：jsonToList(jsonStr,MyClass.class)
     * @return
     * @throws Exception
     */
    public static <T> List<T> jsonArray2BeanArray(String json, Class<T> clazz) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = JsonUtil.getGson().fromJson(json, type);
        List<T> arrayList = new ArrayList<T>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(JsonUtil.getGson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

    /**
     * Json数组字符串转换为 List<java bean>对像
     *
     * @param json 格式为：[{"A":1},{"B":2}...]
     * @param clazz 格式为：jsonToList(jsonStr,MyClass[].class)
     * @return
     */
    public static <T> List<T> jsonArray2Beans(String json, Class<T[]> clazz) {
        T[] array = JsonUtil.getGson().fromJson(json, clazz);
        return Arrays.asList(array);
    }

    /**
     * JSON字符串转为HashMap<String,String>对像
     * Json字符串为{"field":"001","field2":"002"}则转为map对像时可用 map.get("field")获取001
     *
     * @param jsonStr {"field":"001","field2":"002"}
     * @return
     * @throws Exception
     */
    public static HashMap<String, String> json2map(String jsonStr) throws Exception {
        try {
            HashMap<String, String> map = JsonUtil.getGson()
                    .fromJson(jsonStr, new TypeToken<HashMap<String, String>>() {
                    }.getType());
            return map;
        } catch (Exception e) {
            throw new Exception("JSON string cannot be converted to map String " + jsonStr);
        }
    }

    public static HashMap<String, Object> json2mapobj(String jsonStr) throws Exception {
        try {
            HashMap<String, Object> map = JsonUtil.getGson()
                    .fromJson(jsonStr, new TypeToken<HashMap<String, Object>>() {
                    }.getType());
            return map;
        } catch (Exception e) {
            throw new Exception("JSON string cannot be converted to map object " + jsonStr);
        }
    }

    /**
     * 把List<Map<String,String>>对像转换为json字符串
     *
     * @return
     */
    public static String list2json(List<Map<String, String>> list) {
        return JsonUtil.getGson().toJson(list);
    }

    /**
     * 把Set<Object>对像转换为json字符串
     *
     * @return
     */
    public static String set2json(Set<Object> set) {
        return JsonUtil.getGson().toJson(set);
    }

    /**
     * 把map对像转换为json字符串
     *
     * @return
     */
    public static String map2json(Map<String, ?> map) {
        return JsonUtil.getGson().toJson(map);
    }

    /**
     * java bean转换为json对像如果属性值为null时不会输出
     *
     * @param obj
     * @return
     */
    public static String bean2Json(Object obj) {
        return JsonUtil.getGson().toJson(obj);
    }

    /**
     * java bean转换为json对像null属性值也会输出
     *
     * @param obj
     * @return
     * @serializeNull true表示null值也输出
     */
    public static String bean2Json(Object obj, boolean serializeNull) {
        if (serializeNull) {
            return JsonUtil.getGsonNull().toJson(obj);
        } else {
            return JsonUtil.getGson().toJson(obj);
        }
    }

    /**
     * 把class的所有字段转换为json对像
     *
     * @param
     * @return
     */
    public static String class2Json(Class<?> cls) {
        StringBuilder jsonStr = new StringBuilder();
//		Document doc=new Document();
        List<Field> fields = BeanUtil.getAllFields(cls);
        for (Field field : fields) {
            if (jsonStr.length() > 0) {
                jsonStr.append(",");
            }
            String fieldName = field.getName();
            String classType = field.getType().getSimpleName();
            if (classType.equalsIgnoreCase("int") || classType.equalsIgnoreCase("long") || classType
                    .equalsIgnoreCase("Double") || classType.equalsIgnoreCase("float")) {
                jsonStr.append("\"" + fieldName + "\":0");
            } else if (classType.equalsIgnoreCase("boolean")) {
                jsonStr.append("\"" + fieldName + "\":true");
            } else {
                jsonStr.append("\"" + fieldName + "\":\"\"");
            }
        }
        jsonStr.insert(0, "{").append("}");
//		PrintUtil.o(jsonStr.toString());
        return jsonStr.toString();
    }

    /**
     * 多个List集合的java bean转换为json对像
     *
     * @param list
     * @return
     */
    public static String beans2Json(List<?> list) {
        return JsonUtil.getGson().toJson(list);
    }

    /**
     * 多个List集合的java bean转换为json对像
     *
     * @param list
     * @param serializeNull
     * @return
     * @serializeNull true表示null值也输出
     */
    public static String beans2Json(List<?> list, boolean serializeNull) {
        if (serializeNull) {
            return JsonUtil.getGsonNull().toJson(list);
        } else {
            return JsonUtil.getGson().toJson(list);
        }
    }

    /**
     * List转字符串
     *
     * @param list
     * @param Separator
     * @return
     */
    public static String join(List<String> list, String Separator) {
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            if (sb.length() > 0) {
                sb.append(Separator);
            }
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * List转字符串
     *
     * @param objs
     * @param Separator
     * @return
     */
    public static String ArrayToString(Object[] objs, String Separator) {
        if (objs == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object obj : objs) {
            if (obj != null) {
                if (sb.length() > 0) {
                    sb.append(Separator);
                }
                sb.append(obj.toString());
            }
        }
        return sb.toString();
    }

    /**
     * List<Document>文档集合转换为json字符串
     * 注意：document中的_id会被转换为id在json中输出
     *
     * @return
     * @throws Exception
     * @serializeNull true表示null值也输出
     */
    public static String doc2Json(Document document, boolean serializeNull) throws Exception {
        if (document == null) {
            return "";
        }
        String id = DocumentUtil.getDocumentId(document);
        if (StringUtils.isNotBlank(id)) {
            document.put("id", id); //只有id为空时才增加id
        }
        DocumentUtil.removeDocumentObjectId(document);
        if (serializeNull) {
            return JsonUtil.getGsonNull().toJson(document); //输出null字段
        } else {
            return JsonUtil.getGson().toJson(document);
        }
    }

    /**
     * List<Document>文档集合转换为json字符串
     * 注意：document中的_id会被转换为id在json中输出
     *
     * @param documents
     * @return
     * @throws Exception
     * @serializeNull true表示null值也输出
     */
    public static String docs2Json(List<Document> documents, boolean serializeNull) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean isFirst = true;
        for (Document doc : documents) {
            if (isFirst == false) {
                sb.append(",");
            }
            sb.append(doc2Json(doc, serializeNull));
            isFirst = false;
        }
        sb.append("]");
        String jsonStr = sb.toString();
        return jsonStr;
    }

    /**
     * List<Document>文档集合转换为json字符串
     * 注意：document中的_id会被转换为id在json中输出
     *
     * @param document 如果为null时，将返回空字符串
     * @return
     * @throws Exception
     */
    public static String doc2Json(Document document) throws Exception {
        if (document == null) {
            return "";
        }
        String id = DocumentUtil.getDocumentId(document);
        if (StringUtils.isNotBlank(id)) {
            document.put("id", id); //只有id为空时才增加id
        }
        DocumentUtil.removeDocumentObjectId(document);
        return JsonUtil.getGson().toJson(document);
    }

    /**
     * List<Document>文档集合转换为json字符串
     * 注意：document中的_id会被转换为id在json中输出
     *
     * @param documents
     * @return
     * @throws Exception
     */
    public static String docs2Json(List<Document> documents) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean isFirst = true;
        for (Document doc : documents) {
            if (isFirst == false) {
                sb.append(",");
            }
            sb.append(doc2Json(doc));
            isFirst = false;
        }
        sb.append("]");
        String jsonStr = sb.toString();
        return jsonStr;
    }

    /**
     * json字符串转为xml字符串
     *
     * @param json 字符串
     * @return 返回xml字符串
     * @throws JSONException
     */
    public static String json2xml(String json) throws JSONException {
        org.json.JSONObject jsonObj = new org.json.JSONObject(json);
        return XML.toString(jsonObj);
    }


    /**
     * json字符串转为xml字符串
     *
     * @param xml 字符串格式为：<xml><userId></userId></xml>
     * @return 返回json字符串
     * @throws JSONException
     */
    public static String xml2json(String xml) throws JSONException {
        org.json.JSONObject xmlJSONObj = XML.toJSONObject(xml.replace("<xml>", "").replace("</xml>", ""));
        return xmlJSONObj.toString();
    }

    public static String xml2json(String xml, String subNode, String key) throws JSONException {
        return xml2json(xml, subNode.split(key));
    }

    public static String xml2json(String xml, String subNode) throws JSONException {
        return xml2json(xml, subNode.split(","));
    }

    /**
     * 根据xml的子节点返回json
     *
     * @param xml
     * @param subNodeArray
     * @return
     * @throws JSONException
     */
    public static String xml2json(String xml, String[] subNodeArray) throws JSONException {
        org.json.JSONObject xmlJSONObj = XML.toJSONObject(xml.replace("<xml>", "").replace("</xml>", ""));
        for (String key : subNodeArray) {
            if (xmlJSONObj.get(key) == null) {
                log.info(key + "节点在xml结果不存在");
            }
            if (xmlJSONObj.get(key) instanceof String) {
                return xmlJSONObj.getString(key);
            } else if (xmlJSONObj.get(key) instanceof org.json.JSONObject) {
                xmlJSONObj = xmlJSONObj.getJSONObject(key);
            } else if (xmlJSONObj.get(key) instanceof org.json.JSONArray) {
                return xmlJSONObj.getJSONArray(key).toString();
            }

        }
        return xmlJSONObj.toString();
    }
}