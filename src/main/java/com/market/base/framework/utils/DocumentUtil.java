package com.market.base.framework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;

/*
 * 注意事项： document中具有_id字段才可以更新原有的Document不能是id，而java bean中的id的名称可以是id，而不能是_id
  	因为DocumentUtil中的 doc2bean会把_id转成java bean的id属性，而bean2doc会把java bean的id转成文档的_id
 * 将mongo的文档转化为对象将对象转化为mongo文档
 * @author:maybo
 * @data:2016-2-1
 */
public class DocumentUtil {

    /**
     * 获取文档的字段的值对像
     *
     * @param doc 要操作的文档对像
     * @param key 字段的id不区分大小写
     * @return 如果不存在则返回null，存在则返回object对像
     */
    public static Object getIgnoreCase(Document doc, String key) {
        for (String fieldId : doc.keySet()) {
            if (fieldId.equalsIgnoreCase(key)) {
                return doc.get(fieldId);
            }
        }
        return null;
    }

    /**
     * 获取文档的字段内容，按字符串返回
     *
     * @param doc 要操作的文档对像
     * @param key 字段id区分大小写
     * @return 返回字段key的字符串值
     */
    public static String getString(Document doc, String key) {
        Object vobj = doc.get(key);
        if (vobj != null) {
            return vobj.toString();
        } else {
            return "";
        }
    }

    /**
     * 获取文档的字段内容，按字符串返回
     *
     * @param doc 要操作的文档对像
     * @param keys 多个字段的list集合,区分大写小
     * @return 返回多个字段用逗号分隔的值
     */
    public static String getString(Document doc, List<String> keys) {
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(getString(doc, key));
        }
        return sb.toString();
    }

    /**
     * 获取文档的字段内容，按字符串返回
     *
     * @param doc 要操作的文档对像
     * @param keys 多个字段的list集合,不区分大小写
     * @return 返回多个字段用逗号分隔的值
     */
    public static String getStringIgnoreCase(Document doc, List<String> keys) {
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(getStringIgnoreCase(doc, key));
        }
        return sb.toString();
    }

    /**
     * 获取文档多个字段的值，按字符串逗号分隔返回
     *
     * @param doc 要操作的文档对像
     * @param keys 多个字段用多逗号分隔
     * @return 返回多个字段用逗号分隔的值
     */
    public static String getStringByKeys(Document doc, String keys) {
        StringBuilder sb = new StringBuilder();
        String[] keyArray = keys.split(",");
        for (String key : keyArray) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(getString(doc, key));
        }
        return sb.toString();
    }

    /**
     * 获取文档多个字段的值，按字符串逗号分隔返回
     *
     * @param doc 要操作的文档对像
     * @param keys 多个字段用多逗号分隔
     * @return 返回多个字段用逗号分隔的值
     */
    public static String getStringByKeysIgnoreCase(Document doc, String keys) {
        StringBuilder sb = new StringBuilder();
        String[] keyArray = keys.split(",");
        for (String key : keyArray) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(getStringIgnoreCase(doc, key));
        }
        return sb.toString();
    }

    /**
     * 根据字段id自动转整数返回，如果不存在返回0
     *
     * @param doc 要获取的文档对像
     * @param key 关键字
     * @return
     */
    public static int getInteger(Document doc, String key) {
        Object vobj = doc.get(key);
        if (vobj != null) {
            return Integer.parseInt(vobj.toString());
        } else {
            return 0;
        }
    }

    /**
     * 根据字段id自动转double返回
     *
     * @param doc 要获取的文档对像
     * @param key 关键字
     * @return
     */
    public static double getDouble(Document doc, String key) {
        Object vobj = doc.get(key);
        if (vobj != null && StringUtils.isNotBlank(vobj.toString())) {
            return Double.parseDouble(vobj.toString());
        } else {
            return 0;
        }
    }

    /**
     * 根据字段id自动转double返回
     *
     * @param doc 要获取的文档对像
     * @param key 关键字
     * @return
     */
    public static double getDoubleIgnoreCase(Document doc, String key) {
        String value = getStringIgnoreCase(doc, key);
        if (StringUtils.isNotBlank(value)) {
            return Double.parseDouble(value);
        } else {
            return 0;
        }
    }

    /**
     * 不区分大小写获取字段数据
     *
     * @param doc 文档对像
     * @param key 字段id
     * @return 如果不存在则返回空字符串
     */
    public static String getStringIgnoreCase(Document doc, String key) {
        for (String fieldId : doc.keySet()) {
            if (fieldId.equalsIgnoreCase(key)) {
                Object vobj = doc.get(fieldId);
                if (vobj == null) {
                    return "";
                }
                return vobj.toString();
            }
        }
        return "";
    }


    /**
     * 获取文档的字段内容，按字符串返回
     *
     * @param doc 要操作的文档对像
     * @param key 字段id
     * @param defaultValue 没有取到值时返回的默认值
     * @return 返回字段key的字符串值
     */
    public static String getString(Document doc, String key, String defaultValue) {
        Object vobj = doc.get(key);
        String value = "";
        if (vobj != null) {
            value = vobj.toString();
        }
        if (StringUtils.isBlank(value)) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * 设定系统的默认字段
     *
     * @param doc
     * @param isNewDoc 是否insert新文档标识
     */
    public static void setDocCoreDefaultValue(Document doc, boolean isNewDoc) {
//        //说明不用保存核心字段类容，直接退出
//        if (DocumentUtil.getString(doc, "P_NOCOREDEFAULTVALUE").equals("1")) {
//            doc.remove("P_NOCOREDEFAULTVALUE");
//            return;
//        }
//
//        if (isNewDoc) {
//            //只有是新文档时才执行
//            if (StringUtils.isBlank(doc.getString("createTime"))) {
//                doc.put("createTime", DateTimeUtil.getNow());
//            }
//            if (StringUtils.isBlank(doc.getString("creator"))) {
//                doc.put("creator", getUserId());
//                doc.put("creatorName", getUserName());
//            }
//        }
//
//        //更新字段每次保存必须重新给值
//        if (StringUtils.isBlank(doc.getString("appId"))) {
//            doc.put("appId", getAppId());
//        }
//
//        doc.put("editTime", DateTimeUtil.getNow());
//        doc.put("editor", AppContext.getUserId());
//        doc.put("editorName", AppContext.getUserName());
    }

    /**
     * 对字符串中的${ParamsName}变量进行替换,${userId}固定替换为当前登录的用户id,${userName}固定替换为当前登录的用户名
     * appconfig.变量替换为应用的配置值，sysconfig.变量替换为系统的配置值
     *
     * @param doc 文档对像
     * @param str 带有 {字段名}的字符串
     * @return 返回字符串
     * @throws Exception
     */
    public static String replaceParams(String str, Document doc) throws Exception {
        return SystemVarintUtil.replaceDocumentParams(str, doc);
    }

    /**
     * List<Document>对像集合转换为List<Object>对像集合
     *
     * @param documents 要转换的List<Document>
     * @param clazz 类
     * @return
     * @throws Exception
     */
    public static <T> List<T> docs2Beans(List<Document> documents, Class<T> clazz) throws Exception {
        List<T> list = new ArrayList<T>();
        for (Document doc : documents) {
            list.add(doc2Bean(doc, clazz));
        }
        return list;
    }


    /**
     * List<Object>对像集合转换为List<Document>集合
     *
     * @param objs 对像集合
     * @return List<Document>对像集合
     * @throws Exception
     */
    public static List<Document> beans2Docs(List<?> objs) throws Exception {
        List<Document> documents = new ArrayList<Document>();
        for (int i = 0; null != objs && i < objs.size(); i++) {
            documents.add(bean2Doc(objs.get(i)));
        }
        return documents;
    }

    /*
     * 将Document转化为对象Model Bean,doc对像中的字段和java bean中的属性不区分大小写转换
     * @param:Document文档对像
     * @param:obj JavaBean POJO类
     * @param:返回对象
     */
    public static <T> T doc2Bean(Document doc, T obj) throws Exception {
        Document upperDoc = DocumentUtil.documentFieldsToUpperCase(doc);//转换为大写
        List<Field> fields = BeanUtil.getAllFields(obj.getClass());
        for (Field field : fields) {
            String fieldName = field.getName();
            //1.先取文档中的值
            Object columnValue = null;
            if ("id".equals(fieldName)) {
                columnValue = getDocumentId(doc); //如果字段是id要进行特别处理
            } else {
                columnValue = upperDoc.get(fieldName.toUpperCase()); //统一转换为大写再取值，这样文档对像的大小写了java bean的属性的大小写可以兼容
            }
            //2.根据值的类型对数据进行转换
            if (columnValue != null && columnValue instanceof Document) {// 如果字段是文档则要进行递归调用
                if (field.getType() != Document.class) {
                    //如果类的字段类型本身是document的情况下不用再进行转换
                    columnValue = doc2Bean((Document) columnValue, field.getType());
                }
            } else if (columnValue != null) {// 如果字段是文档集合调用colTOList方法
                columnValue = mongoCollectionToList(columnValue, field);
            } else if (field.getType().equals(List.class)) {
                //3.根据field的不同类型对值进行转换List<?>这种类型,field.getGenericType()得到的字符串为java.util.List<cn.restcloud.example.model.UserModel>
                if (columnValue != null && StringUtils.isNotBlank(columnValue.toString())) {
                    Type genericType = field.getGenericType();
                    // 如果是泛型参数的类型
                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) genericType;
                        //得到泛型里的class类型对象
                        Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                        //PrintUtil.o("genericClazz====="+genericClazz.getName());
                        if (BeanUtil.isBaseDataType(genericClazz)) {
//		       				PrintUtil.o(fieldName+"是字符串");
                            columnValue = Tools
                                    .splitAsList(columnValue.toString()); //转换为list<String>字符串list,设置到属性中时会自动转换类型
                        } else if (genericClazz.getName().equalsIgnoreCase("org.bson.Document")) {
                            //是文档类型List<Document>,不用作转换
                        } else {
                            //List<Object> java bean类型
                            columnValue = JsonUtil.jsonArray2BeanArray(JsonUtil.beans2Json((List<?>) columnValue),
                                    genericClazz); //转为java bean对像,传入进来的应该是一个json array的字符串
                        }
                    }
                } else {
                    columnValue = null; //如果没有传入字符串值则设为空对像List<?>
                }
            } else if (columnValue != null && field.getGenericType().equals(java.util.Date.class)) {
                //4.转换日期对像
                String dateValue = columnValue.toString();
                if (StringUtils.isNotBlank(dateValue)) {
                    dateValue = dateValue.replace("/", "-"); //2018/10/02格式转换为2018-10-02这种
                    if (dateValue.length() == 19) {
                        columnValue = DateTimeUtil
                                .string2Date(columnValue.toString(), ""); //日期字符串转换为日期对像yyyy-MM-dd HH:mm:ss
                    } else if (dateValue.length() == 16) {
                        columnValue = DateTimeUtil
                                .string2Date(columnValue.toString(), "yyyy-MM-dd HH:mm"); //日期字符串转换为日期对像yyyy-MM-dd hh:mm
                    } else {
                        columnValue = DateTimeUtil.string2Date(columnValue.toString(), "yyyy-MM-dd"); //日期字符串转换为日期对像
                    }
                }
            }

            //如果值不为null则把值设置到对像属性中去
            if (columnValue != null) {
                BeanUtil.setPropertyValue(obj, fieldName, columnValue);
            }
        }
        return obj;
    }

    /**
     * 把文档的所有字段转换为大写字母,这样方便在取值时全部统一用大写来取，避免出现大小写取不到值的问题
     *
     * @param doc 要转换的文档对像
     * @return 全部为大写字母字段的新文档
     */
    public static Document documentFieldsToUpperCase(Document doc) {
        //先复制所有字段列表，避免在多线程时发生并发问题
        List<String> allFieldIds = new ArrayList<String>();
        allFieldIds.addAll(doc.keySet());
        Document newDoc = new Document();
        for (String key : allFieldIds) {
            //把所有字段全部转换为大写
            if (key.equals("_id")) {
                //如果_id不存在时才读取id
                newDoc.put("_ID", DocumentUtil.getDocumentObjectId(newDoc)); //统一转换为字符串
            } else {
                newDoc.put(key.toUpperCase(), doc.get(key));
            }
        }
        return newDoc;
    }

    /*
     * 将Document转化为对象Model Bean
     * @param:Bson文档
     * @param:类Model 类
     * @param:返回对象
     */
    public static <T> T doc2Bean(Document doc, Class<T> clazz) throws Exception {
        T obj = clazz.newInstance();// 声明一个对象
        return doc2Bean(doc, obj);
    }

    /*
     * Model Bean对象的所有字段属性全部转化为Document文档对像，不管字段属性中是否有NoStore注解标识
     * @param:Bean对象
     * @return:Document文档对像
     */
    public static Document bean2Doc(Object obj) throws Exception {
        return bean2Doc(obj, true);
    }

    /*
     * Model Bean对象转化为Document文档对像<br>
     * Model Bean的字段属性目前支持的基本类型为:String,Integer,Byte,Long,Double,Float,Character,Short,BigDecimal,BigInteger,Boolean,Date<br>
     * 同时还支持<br>
     * List<Document>类型直接转换到Document中  <br>
     * List<基本类型>直接转换到Document中 <br>
     * List<ModelBean> ModelBean会被再转换为List<Document>加入到Document对像中
     * @param 要转换的jave Bean对象
     * @param allField表示bean中的所有字段全部转找到doc对像中去,false表示标识为NoStore注解的字段不转换
     * @return:Document文档对像
     */
    public static Document bean2Doc(Object obj, boolean allField) throws Exception {
        if (null == obj) {
            return null;
        }
        String jsonStr = JsonUtil.bean2Json(obj);
        return JsonUtil.json2doc(jsonStr);
    }

    /*
     * 将数据库的文档集合转化为List列表
     * @param:文档集合
     * @param:属性类型
     * @return:返回列表
     */
    private static List<Object> mongoCollectionToList(Object mc, Field field) throws Exception {
        ParameterizedType pt = (ParameterizedType) field.getGenericType();// 获取列表的类型
        List<Object> objs = new ArrayList<Object>();
//        @SuppressWarnings("unchecked")
//        MongoCollection<Document> cols = (MongoCollection<Document>) mc;
//        MongoCursor<Document> cursor = cols.find().iterator();
//        while (cursor.hasNext()) {
//            Document child = cursor.next();
//            @SuppressWarnings("rawtypes")
//            Class clz = (Class) pt.getActualTypeArguments()[0];// 获取元素类型
//            @SuppressWarnings("unchecked")
//            Object obj = doc2Bean(child, clz);
//            objs.add(obj);
//
//        }
        return objs;
    }

    /**
     * 清除Document对像中所有的id字段，方便拷贝对像
     *
     * @param doc
     * @return
     */
    public static void removeDocumentId(Document doc) {
        doc.remove("_id"); //必须删除掉_id，因为_id不能更新
        doc.remove("id");//存盘时删除此字段，id不用存盘也不能更新
        doc.remove("ID");//存盘时删除此字段，id不用存盘也不能更新
    }

    /**
     * 清除_id字段
     *
     * @param doc
     */
    public static void removeDocumentObjectId(Document doc) {
        doc.remove("_id"); //必须删除掉_id，因为_id不能更新
    }

    /**
     * 不区分大小写获取字段数据
     *
     * @param doc 文档对像
     * @param key 字段id
     * @return
     */
    public static void removeIgnoreCase(Document doc, String key) {
        if (key.equals("id")) {
            //大写的在DocToStatement中传入的不能计算在内,否则在getDocumentId()中得不到大写ID的值
            doc.remove("id");
        } else if (key.equalsIgnoreCase("ID")) {
            doc.remove("ID");
        } else if (key.equalsIgnoreCase("_id")) {
            //大写的在DocToStatement中传入的不能计算在内,否则在getDocumentId()中得不到大写ID的值
            removeDocumentObjectId(doc);
        } else {
            for (String fdName : doc.keySet()) {
                if (fdName.equalsIgnoreCase(key)) {
                    doc.remove(fdName);
                    break;
                }
            }
        }
    }

    /**
     * 获得一个新的DocId
     *
     * @return
     */
    public static String getNewDocumentId() {
        return ObjectId.get().toString();
    }

    /**
     * 获得Document对像的id值,以文档中的id字段为优先，只有没有id字段的情况下才读取_id对像
     *
     * @param doc
     * @return
     */
    public static String getDocumentId(Document doc) {
        if (doc.containsKey("id")) {
            Object obj = doc.get("id");
            if (obj instanceof String) {
                return obj.toString();
            } else {
                return String.valueOf(obj); //id是小写的情况下
            }
        } else if (doc.containsKey("ID")) {
            Object obj = doc.get("ID");
            if (obj instanceof String) {
                return obj.toString();
            } else {
                return String.valueOf(obj); //id是大写的情况下
            }
        }
        return ""; //这里一定要返回空值,不能返回_id的值，因为每个文档存盘必须要存一个id字段
    }

    /**
     * 获得Document对像的id值,以文档中的id字段为优先，只有没有id字段的情况下才读取_id对像
     *
     * @param doc
     * @return
     */
    public static Object getDocumentObjectId(Document doc) {
        if (doc.containsKey("id")) {
            Object obj = doc.get("id");
            return obj;
        } else if (doc.containsKey("ID")) {
            Object obj = doc.get("ID");
            return obj;
        } else {
            //没有id字段的情况下才读取_id对像
            Object objId = doc.get("_id");
            if (objId != null) {
                return objId.toString();
            }
        }
        return "";
    }

}