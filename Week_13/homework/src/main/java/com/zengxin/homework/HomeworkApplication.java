package com.zengxin.homework;

import com.zengxin.homework.kafka.KafkaUtil;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HomeworkApplication {

	public static void main(String[] args) {
		String servers = "localhost:9092,localhost:9093,localhost:9094";
		String topic = "TestTopic";
		String message = "test";

		KafkaProducer<String, String> producer = KafkaUtil.createProducer(servers);
		KafkaUtil.send(producer, topic, message);

		KafkaConsumer<String, String> consumer = KafkaUtil.createConsumer(servers, topic);
		KafkaUtil.readMessage(consumer, 100);
		SpringApplication.run(HomeworkApplication.class, args);
	}

}
