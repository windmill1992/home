package com.redis.utils;

import java.io.Closeable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SerializeTranscoder {

	Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

	public abstract byte[] serialize(Object value);

	public abstract Object deserialize(byte[] in);

	public void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				logger.info("Unable to close " + closeable, e);
			}
		}
	}
}
