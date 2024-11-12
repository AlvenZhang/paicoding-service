package com.github.ps.user.domain;


import com.github.ps.common.enums.FollowSelectEnum;
import com.github.ps.common.vo.PageListVo;
import com.github.ps.common.vo.article.dto.ArticleDTO;
import com.github.ps.common.vo.article.dto.TagSelectDTO;
import com.github.ps.common.vo.user.dto.FollowUserInfoDTO;
import com.github.ps.common.vo.user.dto.UserPayCodeDTO;
import com.github.ps.common.vo.user.dto.UserStatisticInfoDTO;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author YiHui
 * @date 2022/9/2
 */
@Data
public class UserHomeVo {
    private String homeSelectType;
    private List<TagSelectDTO> homeSelectTags;
    /**
     * 关注列表/粉丝列表
     */
    private PageListVo<FollowUserInfoDTO> followList;
    /**
     * @see FollowSelectEnum#getCode()
     */
    private String followSelectType;
    private List<TagSelectDTO> followSelectTags;
    private UserStatisticInfoDTO userHome;

    /**
     * 文章列表
     */
    private PageListVo<ArticleDTO> homeSelectList;

    /**
     * 收款二维码
     */
    private Map<String, UserPayCodeDTO> payQrCodes;
}
