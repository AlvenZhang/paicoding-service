package com.github.ps.user.dao;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.ps.user.domain.entity.UserAiHistoryDO;
import com.github.ps.user.mapper.UserAiHistoryMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class UserAiHistoryDao extends ServiceImpl<UserAiHistoryMapper, UserAiHistoryDO> {

    @Resource
    private UserAiHistoryMapper userAiHistoryMapper;
}

