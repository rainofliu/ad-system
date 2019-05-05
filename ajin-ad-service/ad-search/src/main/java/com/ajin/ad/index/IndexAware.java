package com.ajin.ad.index;

/**
 * @Auther: ajin
 * @Date: 2019/4/12 11:58
 */
public interface IndexAware<K, V> {
    /**
     * 获取索引
     */
    V get(K key);

    /**
     * 添加索引
     */
    void add(K key, V value);

    /**
     * 更新索引
     */
    void update(K key, V value);

    /**
     * 删除索引
     */
    void delete(K key, V value);

}
