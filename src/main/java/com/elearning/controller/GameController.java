package com.elearning.controller;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.model.GameInteractRequest;
import com.elearning.model.GameRequest;
import com.elearning.util.Util;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class GameController {

	private static final String KAFKA_GAME_TOPIC = "kafka-game";
	private static final String KAFKA_GAME_INTERACT_TOPIC = "kafka-game-interact";

	private static final String REDIS_GAMEROOM_PREFIX = "gameroom-";

	private static final String REDIS_CHATROOM_PATTERN = REDIS_GAMEROOM_PREFIX + "*";

	@Autowired
	private KafkaTemplate<Integer, String> kafkaTemplate;
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Autowired
	private Util util;
	
	@RequestMapping(value="/game/send-request", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public HttpEntity<GameRequest> sendRequest(@RequestBody GameRequest body) {

		try {
			
			kafkaTemplate.send(KAFKA_GAME_TOPIC, util.objectToJSON(body)).get();
			
			String redisKey = REDIS_GAMEROOM_PREFIX + body.getGameRoomKey();
			
			redisTemplate.opsForSet().add(redisKey, body.getHostName());
			redisTemplate.expire(redisKey, 10, TimeUnit.MINUTES);
			
			
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
		
		return new HttpEntity<>(body);
		
	}
	
	@RequestMapping(value="/game/interact", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public HttpEntity<GameInteractRequest> interact(@RequestBody GameInteractRequest body) {

		try {
			
			kafkaTemplate.send(KAFKA_GAME_INTERACT_TOPIC, util.objectToJSON(body)).get();
			
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
		
		return new HttpEntity<>(body);
		
	}
	
	@RequestMapping(value="/game/{gameRoomKey}", method=RequestMethod.GET, produces="application/json")
	public List<GameRequest> getGameroom(@PathVariable String gameRoomKey) {
		
		Consumer<Integer, String> consumer = new KafkaConsumer<>(consumerConfigs(), null, new JsonDeserializer<>(String.class));
		
		consumer.subscribe(Arrays.asList(KAFKA_GAME_TOPIC));
		
		ConsumerRecords<Integer, String> records = consumer.poll(1000);
		Iterator<ConsumerRecord<Integer, String>> iter = records.iterator();
		
		System.out.println("received "+ records.count() + " messages ");
		
		List<GameRequest> result = Lists.newArrayListWithExpectedSize(records.count());
		
		while (iter.hasNext()) {
			ConsumerRecord<Integer,String> next = iter.next();
			String json = next.value();
			GameRequest msg = util.jsonToObject(json, GameRequest.class);
			if (gameRoomKey.equals(msg.getGameRoomKey())) {
				result.add(0, msg);
			}
		}
		
		consumer.close();
		
		return result;
	}
	
	private Map<String, Object> consumerConfigs() {

		return ImmutableMap.<String, Object>builder()
					.put("bootstrap.servers", "localhost:29092")
					.put("key.deserializer", IntegerDeserializer.class)
					.put("value.deserializer", JsonDeserializer.class)
					.put("auto.offset.reset", "earliest")
					.put("max.partition.fetch.bytes", 2097152) // important, but why???
					.put("group.id", UUID.randomUUID().toString()) 

				.build();
	}
	
	
	
}
