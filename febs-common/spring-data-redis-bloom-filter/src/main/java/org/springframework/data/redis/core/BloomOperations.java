package org.springframework.data.redis.core;

import java.util.List;
import java.util.Objects;


/**
 * 布隆过滤器
 *
 * @author duzou
 */

public interface BloomOperations<K, V> {

    /**
     * 创建一个布隆过滤器
     *
     * @param key          名称
     * @param errorRate    错误率
     * @param initCapacity 容器大小
     */
    void createFilter(K key, double errorRate, long initCapacity);

    /**
     * 将一个元素添加至容器
     *
     * @param key   名称
     * @param value 元素
     * @return 成功true 反之 false
     */
    Boolean add(K key, V value);

    /**
     * 批量添加元素至容器
     *
     * @param key    名称
     * @param values 元素
     * @return 成功true 反之 false
     */
    Boolean[] addMulti(K key, List<V> values);

    /**
     * 检查一个元素是否存在容器中
     *
     * @param key   名称
     * @param value 元素
     * @return 存在true 反之 false
     */
    Boolean exists(K key, V value);

    /**
     * 批量检查元素是否存在容器中
     *
     * @param key    名称
     * @param values 元素
     * @return 存在true 反之 false
     */
    Boolean[] existsMulti(K key, List<V> values);

    /**
     * 删除一个容器
     *
     * @param key 名称
     * @return 成功true 反之 false
     */
    Boolean delete(K key);

    /**
     * 验证容器是否存在
     * @param key 名称
     * @return 成功true 反之 false
     */
    Boolean hasKey(K key);
}
