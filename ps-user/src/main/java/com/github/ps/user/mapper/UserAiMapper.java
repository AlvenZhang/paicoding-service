package com.github.ps.user.mapper;


import com.github.ps.user.domain.entity.UserAiDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ai用户登录mapper接口
 *
 * @author ygl
 * @date 2022-07-18
 */
public interface UserAiMapper extends BaseMapper<UserAiDO> {

    Long countZsxqUsersByParams(@Param("searchParams") SearchZsxqWhiteParams params);

    List<ZsxqUserInfoDTO> listZsxqUsersByParams(@Param("searchParams") SearchZsxqWhiteParams params,
                                                @Param("pageParam") PageParam newPageInstance);
}
