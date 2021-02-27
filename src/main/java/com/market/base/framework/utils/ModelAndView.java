package com.market.base.framework.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

/**
 * 输出模型JSON
 *
 * @Author wei
 * @Date 2021/2/22 18:00
 **/
@Data
public class ModelAndView {

    private int code;
    private String msg;
    private Object data;
    private long totalCount = 0;
    private long totalPage = 0;
    private long size = 10;
    private long current = 1;

    public ModelAndView() {
        super();
    }

    public ModelAndView(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ModelAndView(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * SUCCESS 返回
     *
     * @return
     */
    public static ModelAndView success(String msg) {
        return new ModelAndView(200, msg, "");
    }

    /**
     * SUCCESS 返回
     *
     * @param msg
     * @return
     */
    public static ModelAndView success(int code, String msg) {
        return new ModelAndView(code, msg, new JSONArray());
    }

    /**
     * 正常信息集合返回
     *
     * @param object
     * @return
     */
    public static ModelAndView successData(Object object) {
        return new ModelAndView(200, " ", object);
    }


    /**
     * error 返回
     *
     * @param msg
     * @return
     */
    public static ModelAndView error(String msg) {
        return new ModelAndView(400, msg, new JSONArray());
    }

    /**
     * error 返回
     *
     * @param code
     * @param msg
     * @return
     */
    public static ModelAndView error(int code, String msg) {
        return new ModelAndView(code, msg, new JSONArray());
    }


    /**
     * 错误信息集合返回
     *
     * @param object
     * @return
     */
    public static String errorData(Object object) {
        return toJSONString(object);
    }

    public static ModelAndView ipage(IPage ipage) {
        ModelAndView modelAndView = new ModelAndView(200, " ");
        modelAndView.setData(ipage.getRecords());
        modelAndView.setTotalCount(ipage.getTotal());
        modelAndView.setCurrent(ipage.getCurrent());
        modelAndView.setSize(ipage.getSize());
        modelAndView.setTotalPage((int) Math.ceil((double) ipage.getTotal() / ipage.getSize()));
        return modelAndView;
    }

    /**
     * 使用JSONObject格式化JSON输出
     *
     * @param object
     * @return
     */
    public static String toJSONString(Object object) {
        return JSONObject.toJSONString(object);
    }


    /**
     * 转换成JSON格式输出
     *
     * @return
     */
    public String toJson() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{").append("\"").append("code")
                .append("\"").append(":").append(this.code)
                .append(",").append("\"").append("msg").append("\"")
                .append(":").append("\"").append(this.msg).append("\"")
                .append(",").append("\"").append("data").append("\"")
                .append(":").append(this.data).append("}");
        return stringBuilder.toString();
    }


}
