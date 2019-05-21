package com.kafka.demo.producer.controller;

import com.alibaba.fastjson.JSON;
import com.gemantic.springcloud.model.Response;
import com.gemantic.springcloud.model.ResponseMessage;
import com.kafka.demo.util.ConverUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.CollectionUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/kafka")
public class KafkaProducerController {
    private static Logger log = LoggerFactory.getLogger(KafkaProducerController.class);

    public static final Integer BATCH_SEND_SIZE = 50;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @ApiOperation(value = "kafka生产者发送数据", notes = "kafka生产者发送数据")
    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public Response<Boolean> sendKafka(@ApiParam(value = "message") @RequestParam String message) {
        log.info("kafka的消息={}", message);
        if(StringUtils.isEmpty(message)){
            log.info("send is error!params is empty!");
            return new Response<>(
                    ResponseMessage.error(-1, "参数错误!"), Boolean.FALSE);
        }
        try {
            kafkaTemplate.send("test1", "key", message);
            log.info("发送kafka成功.");
            return Response.ok(Boolean.TRUE);
        } catch (Exception e) {
            log.error("发送kafka失败", e);
            return new Response<>(
                    ResponseMessage.error(-1, "发送kafka失败!"), Boolean.FALSE);
        }
    }
    @ApiOperation(value = "kafka生产者发送数据有回调", notes = "kafka生产者发送数据有回调")
    @RequestMapping(value = "/sendCollback", method = RequestMethod.GET)
    public Response<Boolean> sendKafkaCollback(@ApiParam(value = "message") @RequestParam String message) {
        log.info("kafka的消息={}", message);
        if(StringUtils.isEmpty(message)){
            log.info("send is error!params is empty!");
            return new Response<>(
                    ResponseMessage.error(-1, "参数错误!"), Boolean.FALSE);
        }
        try {
            ProducerRecord<String, String> record = new ProducerRecord("test1", JSON.toJSONString(message));
            ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send( record);
            future.addCallback((r) -> {
                if (log.isDebugEnabled()) {
                    log.debug(String
                            .format("sendToKafka %s %d %s", record.topic(),record.partition(),record.key()));
                }
            }, (ex) -> log.error(String
                    .format("sendToKafka %s %d error %s", record.topic(),record.partition(), record.key()), ex));
            log.info("发送kafka成功.");
            return Response.ok(Boolean.TRUE);
        } catch (Exception e) {
            log.error("发送kafka失败", e);
            return new Response<>(
                    ResponseMessage.error(-1, "发送kafka失败!"), Boolean.FALSE);
        }
    }
    @ApiOperation(value = "kafka生产者发送数据有回调", notes = "kafka生产者发送数据有回调")
    @RequestMapping(value = "/sendCollback", method = RequestMethod.GET)
    public void sendBatch(List<String> scores, String topic,Integer batchSize) throws Exception {
        if (CollectionUtils.isEmpty(scores)) {
            return;
        }

        List<List<String>> batchList = ConverUtil.convert2BatchList(scores, batchSize);
        batchList.forEach(b->{
            final ProducerRecord<String, String> record = new ProducerRecord(topic, JSON.toJSONString(b));
            sendkafka(record);
        });
    }

    private void sendkafka(ProducerRecord<String, String> record){
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(record);
        future.addCallback((r) -> {
            if (log.isDebugEnabled()) {
                log.debug(String
                        .format("sendToKafka %s %d %s", record.topic(),record.partition(),record.key()));
            }
        }, (ex) -> log.error(String
                .format("sendToKafka %s %d error %s", record.topic(),record.partition(), record.key()), ex));
    }
}
