package com.atguigu.gmall0513.logger.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import comatguigu.gmall0513.common.constants.GmallConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class loggerController {

    //kafkaTemplate是接口,帮我们实例化kafka对象
    //误判，
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;
    @PostMapping("/log")
    @ResponseBody
    //@ResponseBody 表示该方法的返回结果直接写入 HTTP response body 中
    //使用 @RequestMapping后，返回值通常解析为跳转路径，但是加上 @ResponseBody 后返回结果不会被解析为跳转路径，而是直接写入 HTTP response body 中。
    public  String log(@RequestParam("logString") String logString){
        JSONObject jsonObject = JSON.parseObject(logString);

        jsonObject.put("ts",System.currentTimeMillis());
        log.info(jsonObject.toJSONString());
        //添加时间戳，先转成json对象


        // 发送kafka
        if("startup".equals(jsonObject.getString("type")) ){
            kafkaTemplate.send(GmallConstant.KAFKA_TOPIC_STARTUP,jsonObject.toJSONString());
        }else{
            kafkaTemplate.send(GmallConstant.KAFKA_TOPIC_EVENT,jsonObject.toJSONString());
        }

       // System.out.println(logString);
        return "success";
    }
}
