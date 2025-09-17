package com.eplugger.business.key.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.eplugger.business.key.mapper.KeyMappler;
import com.eplugger.business.key.model.Key;
import com.eplugger.business.key.service.KeyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 秘钥服务实现类
 * 提供秘钥的保存、比较等功能
 */
@Service
public class KeyServiceImpl implements KeyService {

    private final KeyMappler mapper;

    @Autowired
    public KeyServiceImpl(KeyMappler mapper) {
        this.mapper = mapper;
    }

    /**
     * 保存秘钥（新增或更新）
     *
     * @return 保存后的秘钥对象
     */
    @Override
    @Transactional
    public Key generate() {
        List<Key> keys = mapper.findAll();
        if (CollectionUtil.isEmpty(keys)) {
            Key key = new Key();
            key.setKey(generateKey());
            mapper.insert(key);
            return key;
        } else {
            Key key = keys.get(0);
            key.setKey(generateKey());
            mapper.update(key);
            return key;
        }
    }

    /**
     * 生成秘钥ID
     * 参考前端生成方式：ai-scenarios-{timestamp}-{randomPart}
     */
    private String generateKey() {
        long timestamp = System.currentTimeMillis();
        String randomPart = generateRandomString(6);
        return String.format("ai-scenarios-%d-%s", timestamp, randomPart);
    }

    /**
     * 生成随机字符串
     *
     * @param length 长度
     * @return 随机字符串
     */
    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            result.append(characters.charAt(index));
        }

        return result.toString();
    }

    /**
     * 比较秘钥是否存在
     *
     * @param key 秘钥对象
     * @return 秘钥是否存在
     */
    @Override
    public Boolean compare(Key key) {
        if (key == null || StringUtils.isBlank(key.getKey())) {
            return false;
        }
        return mapper.findByKey(key.getKey()) != null;
    }
}
