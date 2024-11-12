package com.github.ps.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.ps.common.vo.PageParam;
import com.github.ps.common.vo.user.dto.FollowUserInfoDTO;
import com.github.ps.user.domain.entity.UserRelationDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户关系mapper接口
 *
 * @author louzai
 * @date 2022-07-18
 */
public interface UserRelationMapper extends BaseMapper<UserRelationDO> {

    /**
     * 我关注的用户
     * @param followUserId
     * @param pageParam
     * @return
     */
    List<FollowUserInfoDTO> queryUserFollowList(@Param("followUserId") Long followUserId, @Param("pageParam") PageParam pageParam);

    /**
     * 关注我的粉丝
     * @param userId
     * @param pageParam
     * @return
     */
    List<FollowUserInfoDTO> queryUserFansList(@Param("userId") Long userId, @Param("pageParam") PageParam pageParam);
}