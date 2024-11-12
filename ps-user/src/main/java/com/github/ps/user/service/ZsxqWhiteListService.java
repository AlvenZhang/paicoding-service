package com.github.ps.user.service;



import com.github.ps.common.enums.user.UserAIStatEnum;
import com.github.ps.common.vo.PageVo;
import com.github.ps.common.vo.user.SearchZsxqUserReq;
import com.github.ps.common.vo.user.ZsxqUserPostReq;
import com.github.ps.common.vo.user.dto.ZsxqUserInfoDTO;

import java.util.List;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 6/29/23
 */
public interface ZsxqWhiteListService {
    PageVo<ZsxqUserInfoDTO> getList(SearchZsxqUserReq req);

    void operate(Long id, UserAIStatEnum operate);

    void update(ZsxqUserPostReq req);

    void batchOperate(List<Long> ids, UserAIStatEnum operate);

    void reset(Integer authorId);
}
