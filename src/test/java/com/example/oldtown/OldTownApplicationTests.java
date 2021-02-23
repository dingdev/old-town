package com.example.oldtown;

import cn.hutool.core.collection.CollectionUtil;
import com.example.oldtown.common.api.CommonResult;
import com.example.oldtown.dto.StringIntegerDTO;
import com.example.oldtown.modules.trf.mapper.TrfYachtMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

@SpringBootTest
class OldTownApplicationTests {

	@Autowired
	RedisTemplate redisTemplate;
	@Resource
	TrfYachtMapper trfYachtMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void sth() {

		// Stack<Integer> stack = new Stack<>();
		// stack.push(3);
		// stack.push(4);
		// stack.push(5);
		// System.out.println(stack.peek());

		// Long timestamp = System.currentTimeMillis()/1000;
		// HashOperations<String, String, Integer> hashOperations = redisTemplate.opsForHash();
		//
		// List<StringIntegerDTO> yachtList = trfYachtMapper.getAllGpsCode(timestamp.intValue());
		// Map<String, Integer> yachtMap = yachtList.stream().collect(Collectors.toMap(StringIntegerDTO::getKey, StringIntegerDTO::getValue));
		// hashOperations.delete("trfYachtGPS",hashOperations.keys("trfYachtGPS").toArray());
		// hashOperations.putAll("trfYachtGPS",yachtMap);
	}


}
