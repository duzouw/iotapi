package org.springframework.data.redis.core;

import java.util.List;
import java.util.Objects;


/**
 * @author duzou
 */
public class DefaultBloomOperations<K, V> extends AbstractOperations<K, V> implements BloomOperations<K, V> {

    public DefaultBloomOperations(RedisTemplate<K, V> template) {
        super(template);
    }

    @Override
    public void createFilter(K key, double errorRate, long initCapacity) {
        byte[] rawKey = rawKey(key);
        byte[] rawErrorRate = rawString(String.valueOf(errorRate));
        byte[] rawInitCapacity = rawString(String.valueOf(initCapacity));
        execute(connection -> {
            connection.execute(BloomCommand.RESERVE.getCommand(), rawKey, rawErrorRate, rawInitCapacity);
            return null;
        }, true);
    }

    @Override
    public Boolean add(K key, V value) {
        byte[] rawKey = rawKey(key);
        byte[] rawValue = rawValue(value);
        return Objects.requireNonNull(execute(connection -> {
            Long l = (Long) connection.execute(BloomCommand.ADD.getCommand(), rawKey, rawValue);
            return Objects.equals(l, 1L);
        }, true));
    }

    @Override
    public Boolean[] addMulti(K key, List<V> values) {
        byte[][] rawArgs = rawArgs(key, values);
        return Objects.requireNonNull(execute(connection -> {
            List<Long> ls = (List<Long>) connection.execute(BloomCommand.MADD.getCommand(), rawArgs);
            return ls.stream().map(l -> Objects.equals(l, 1L)).toArray(Boolean[]::new);
        }, true));
    }

    @Override
    public Boolean exists(K key, V value) {
        byte[] rawKey = rawKey(key);
        byte[] rawValue = rawValue(value);
        return Objects.requireNonNull(execute(connection -> {
            Long l = (Long) connection.execute(BloomCommand.EXISTS.getCommand(), rawKey, rawValue);
            return Objects.equals(l, 1L);
        }, true));
    }

    @Override
    public Boolean[] existsMulti(K key, List<V> values) {
        byte[][] rawArgs = rawArgs(key, values);
        return Objects.requireNonNull(execute(connection -> {
            List<Long> ls = (List<Long>) connection.execute(BloomCommand.MEXISTS.getCommand(), rawArgs);
            return ls.stream().map(l -> Objects.equals(l, 1L)).toArray(Boolean[]::new);
        }, true));
    }

    @Override
    public Boolean delete(K key) {
        return template.delete(key);
    }

    @Override
    public Boolean hasKey(K key) {
        try {
            return template.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private byte[][] rawArgs(Object key, List<V> values) {
        byte[][] rawArgs = new byte[1 + values.size()][];

        int i = 0;
        rawArgs[i++] = rawKey(key);

        for (Object value : values) {
            rawArgs[i++] = rawValue(value);
        }

        return rawArgs;
    }
}
