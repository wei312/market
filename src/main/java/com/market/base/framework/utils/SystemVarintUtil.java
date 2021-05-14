package com.market.base.framework.utils;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;

/**
 * 系统变量替换公共类
 * 本类在ESBSystemVarintUtil中还复制了一个专门针对ESB使用的类
 *
 * @author Thinkpad
 */
public class SystemVarintUtil {

    /**
     * MongoDbFilters类中使用
     * 对字SQL条件中的字符串中的{ParamsName}变量进行替换,与DocumentUtil.replaceParams()方法一致
     * 主要是防止sql注入，会把变量中的单引号替换为''
     *
     * @param doc 文档对像
     * @param str 带有 {字段名}的字符串
     * @return 返回字符串
     * @throws Exception
     */
    public static String replaceFiltersByDoc(String str, Document doc) throws Exception {
        boolean isRdbFlag = true; //是否是关系数据库条件
        if (StringUtils.isBlank(str)) {
            return "";
        }
        if (str.toLowerCase().indexOf("filters.") != -1) {
            isRdbFlag = false;
        } //说明是mongodb条件
        String startCode = "{";
        String endCode = "}";
        int spos = str.indexOf(startCode);
        if (spos == -1) {
            return str;
        } // 没有{符号直接返回
        StringBuilder reString = new StringBuilder(str.length());
        while (spos != -1) {
            int epos = str.indexOf(endCode);
            String fdName = str.substring(spos + 1, epos);
            String fdValue = "";
            if (fdName.startsWith("$")) {
                fdValue = prarserFieldValue(doc, fdName);
            } else {
                //正常字段
                Object v = doc.get(fdName);
                if (v != null) {
                    fdValue = v.toString();
                }
            }
            if (isRdbFlag) {
                fdValue = RdbUtil.formatSqlValue(fdValue); //如果是关系数据库要进行编码，避免SQL注入问题
            }
            String lStr = str.substring(0, spos);
            str = str.substring(epos + 1, str.length());
            reString.append(lStr);
            reString.append(fdValue);
            spos = str.indexOf(startCode);
        }
        reString.append(str);
        return reString.toString();
    }


    /**
     * 对字符串中的{$ParamsName}变量进行替换,${$config.配置id} 可替换为配置中的值
     *
     * @param doc 文档对像
     * @param str 带有 {字段名}的字符串
     * @return 返回字符串
     * @throws Exception
     */
    public static String replaceDocumentParams(String str, Document doc) throws Exception {
        return replaceStringParams(str, doc, false); //与sql变量进行合并计算2019-04-07
    }


    /**
     * 替换sql语句变量专用
     *
     * @param sql
     * @param doc
     * @return
     * @throws Exception
     */
    public static String replaceSqlParams(String sql, Document doc) throws Exception {
        return replaceStringParams(sql, doc, true);
    }

    /**
     * 对sql语句的${}变量进行解析并替换为字符串
     * ${$config.配置id} 可替换为配置中的值
     *
     * @param doc 存变量值的文档对像
     * @param sql 带有 ${变量}的sql字符串
     * @param formatFlag true表示要进行防sql注入的格式化，false表示否
     * @return 返回解析后的SQL语句
     * @throws Exception
     */
    public static String replaceStringParams(String sql, Document doc, boolean formatFlag) throws Exception {
        if (StringUtils.isBlank(sql)) {
            return "";
        }
        String startCode = "${";
        String endCode = "}";
        int spos = sql.indexOf(startCode);
        if (spos == -1) {
            return sql;
        } // 没有{符号直接返回
        StringBuilder reString = new StringBuilder(sql.length());
        while (spos != -1) {
            int epos = sql.indexOf(endCode);
            String fdName = sql.substring(spos + 2, epos);
            String fdValue = "";
            if (fdName.startsWith("$") || fdName.startsWith("#")) {
                fdValue = prarserFieldValue(doc, fdName);
            } else {
                //正常字段
                Object v = doc.get(fdName);
                if (v != null) {
                    if (v instanceof Document) {
                        //是一个json doc
                        fdValue = ((Document) v).toJson();
                    } else if (v instanceof List<?>) {
                        //说明是一个json数组
                        fdValue = JsonUtil.docs2Json((List<Document>) v);
                    } else {
                        //普通字符串
                        fdValue = v.toString();
                    }
                }
            }
            String lStr = sql.substring(0, spos);
            sql = sql.substring(epos + 1, sql.length());
            reString.append(lStr);
            if (formatFlag) {
                reString.append(RdbUtil.formatSqlValue(fdValue)); //防止sql注入
            } else {
                reString.append(fdValue);
            }
            spos = sql.indexOf(startCode);
        }
        reString.append(sql);
        return reString.toString();
    }

    /**
     * 统一分析系统可替换的变量字段
     *
     * @param doc
     * @param fdName
     * @return
     * @throws Exception
     */
    public static String prarserFieldValue(Document doc, String fdName) throws Exception {
//        //系统变量
        String fdValue = "";
//        if (fdName.startsWith("$config.") || fdName.startsWith("#config.")) { //应用参数配置
//            int tempspos = fdName.indexOf(".");
//            fdName = fdName.substring(tempspos + 1, fdName.length());
//            fdValue = ConfigUtil.getConfig(fdName);
//        } else if (fdName.equalsIgnoreCase("$id") || fdName.equalsIgnoreCase("#id")) {
//            fdValue = DocumentUtil.getNewDocumentId();//自动生成一个唯一id
//        } else if (fdName.equalsIgnoreCase("$date") || fdName.equalsIgnoreCase("#date")) {
//            fdValue = DateTimeUtil.getNow("yyyy-MM-dd");
//        } else if (fdName.equalsIgnoreCase("$dateTime") || fdName.equalsIgnoreCase("#dateTime")) {
//            fdValue = DateTimeUtil.getNow();
//        } else if (fdName.equalsIgnoreCase("$userId") || fdName.equalsIgnoreCase("#userId")) {
//            fdValue = AppContext.getUserId();//用户id
//        } else if (fdName.equalsIgnoreCase("$userName") || fdName.equalsIgnoreCase("#userName")) {
//            fdValue = AppContext.getUserName(); //用户名
//        } else if (fdName.equalsIgnoreCase("$roleCode") || fdName.equalsIgnoreCase("#roleCode")) {
//            fdValue = StringUtils.join(AppContext.getUserContext().getRolesCode(), ",");//角色id
//        } else if (fdName.equalsIgnoreCase("$deptCode") || fdName.equalsIgnoreCase("#deptCode")) {
//            fdValue = AppContext.getUserContext().getDepartmentCode();//替换部门id
//        } else if (fdName.equalsIgnoreCase("$permissionId") || fdName.equalsIgnoreCase("#permissionId")) {
//            fdValue = StringUtils.join(AppContext.getUserContext().getPermissionId(), ",");//替换权限id
//        } else if (fdName.startsWith("$header.") || fdName.startsWith("#header.")) {
//            fdName = StringUtils.substringAfter(fdName, "."); //取头中的字段id
//            fdValue = RequestUtil.getHeaders().get(fdName); //取http头中的数据
//        } else if (fdName.toLowerCase().startsWith("$bean.") || fdName.toLowerCase()
//                .startsWith("#bean.")) { //格式 ${$bean.WeixinConnector.getAccessToken} 执行java bean的方法获取一个变量
//            String[] configArray = StringUtils.split(fdName, ".");
//            if (configArray.length == 3) {
//                String beanId = configArray[1];
//                String methodName = configArray[2];
//                fdValue = (String) BeanEngine.call(beanId, methodName);
//            }
//        } else {
//            //正常字段
//            Object v = doc.get(fdName);
//            if (v != null) {
//                fdValue = v.toString();
//            }
//        }
        return fdValue;
    }

}
