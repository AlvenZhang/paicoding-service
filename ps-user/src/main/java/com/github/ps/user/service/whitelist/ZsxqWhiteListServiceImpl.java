package com.github.ps.user.service.whitelist;

import com.github.ps.common.enums.user.LoginTypeEnum;
import com.github.ps.common.enums.user.UserAIStatEnum;
import com.github.ps.common.exception.ExceptionUtil;
import com.github.ps.common.vo.PageVo;
import com.github.ps.common.vo.constants.StatusEnum;
import com.github.ps.common.vo.user.SearchZsxqUserReq;
import com.github.ps.common.vo.user.ZsxqUserPostReq;
import com.github.ps.common.vo.user.dto.ZsxqUserInfoDTO;
import com.github.ps.user.dao.UserAiDao;
import com.github.ps.user.dao.UserDao;
import com.github.ps.user.domain.entity.UserAiDO;
import com.github.ps.user.domain.entity.UserDO;
import com.github.ps.user.domain.entity.UserInfoDO;
import com.github.ps.user.params.SearchZsxqWhiteParams;
import com.github.ps.user.service.ZsxqWhiteListService;
import com.github.ps.user.service.converter.UserAiConverter;
import com.github.ps.user.service.converter.UserStructMapper;
import com.github.ps.user.service.help.UserPwdEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 6/29/23
 */
@Service
public class ZsxqWhiteListServiceImpl implements ZsxqWhiteListService {
    @Autowired
    private UserAiDao userAiDao;
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserPwdEncoder userPwdEncoder;

    @Override
    public PageVo<ZsxqUserInfoDTO> getList(SearchZsxqUserReq req) {
        SearchZsxqWhiteParams params = UserStructMapper.INSTANCE.toSearchParams(req);
        // 查询知识星球用户
        List<ZsxqUserInfoDTO> zsxqUserInfoDTOs = userAiDao.listZsxqUsersByParams(params);
        Long totalCount = userAiDao.countZsxqUserByParams(params);
        return PageVo.build(zsxqUserInfoDTOs, req.getPageSize(), req.getPageNumber(), totalCount);
    }

    @Override
    public void operate(Long id, UserAIStatEnum operate) {
        // 根据id获取用户信息
        UserAiDO userAiDO = userAiDao.getById(id);
        // 为空则抛出异常
        if (userAiDO == null) {
            throw ExceptionUtil.of(StatusEnum.USER_NOT_EXISTS, id, "用户不存在");
        }

        // 更新用户状态
        userAiDO.setState(operate.getCode());

        // 审核通过的时候调整用户的策略
        userAiDao.updateById(userAiDO);
    }

    @Override
    // 加事务
    @Transactional(rollbackFor = Exception.class)
    public void update(ZsxqUserPostReq req) {
        // 根据id获取用户信息
        UserAiDO userAiDO = userAiDao.getById(req.getId());
        // 为空则抛出异常
        if (userAiDO == null) {
            throw ExceptionUtil.of(StatusEnum.USER_NOT_EXISTS, req.getId(), "用户不存在");
        }

        // 星球编号不能重复
        UserAiDO userAiDOByStarNumber = userAiDao.getByStarNumber(req.getStarNumber());
        if (userAiDOByStarNumber != null && !userAiDOByStarNumber.getId().equals(req.getId())) {
            throw ExceptionUtil.of(StatusEnum.USER_STAR_REPEAT, req.getStarNumber(), "星球编号已存在");
        }

        // 用户登录名不能重复
        UserDO userDO = userDao.getUserByUserName(req.getUserCode());
        if (userDO != null && !userDO.getId().equals(userAiDO.getUserId())) {
            throw ExceptionUtil.of(StatusEnum.USER_LOGIN_NAME_REPEAT, req.getUserCode(), "用户登录名已存在");
        }

        // 更新用户登录名
        userDO = new UserDO();
        userDO.setId(userAiDO.getUserId());
        userDO.setUserName(req.getUserCode());
        userDao.updateUser(userDO);

        // 更新用户昵称
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setId(userAiDO.getUserId());
        userInfoDO.setUserName(req.getName());
        userDao.updateById(userInfoDO);

        // 更新星球编号
        userAiDO.setStarNumber(req.getStarNumber());
        // 更新 AI 策略
        userAiDO.setStrategy(req.getStrategy());

        userAiDao.updateById(userAiDO);
    }

    @Override
    public void batchOperate(List<Long> ids, UserAIStatEnum operate) {
        // 批量更新用户状态
        userAiDao.batchUpdateState(ids, operate.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reset(Integer authorId) {
        // 根据id获取用户信息
        UserAiDO userAiDO = userAiDao.getById(authorId);
        // 为空则抛出异常
        if (userAiDO == null) {
            throw ExceptionUtil.of(StatusEnum.USER_NOT_EXISTS, authorId, "该星球用户不存在");
        }

        // 获取用户，看是微信还是用户名密码注册用户
        UserDO userDO = userDao.getUserByUserId(userAiDO.getUserId());
        if (userDO == null) {
            throw ExceptionUtil.of(StatusEnum.USER_NOT_EXISTS, userAiDO.getUserId(), "该用户不存在");
        }

        // 不能直接删除，要初始化用户的 AI 信息
        UserAiDO initUserAiDO = UserAiConverter.initAi(userAiDO.getUserId());
        initUserAiDO.setId(userAiDO.getId());
        userAiDao.updateById(initUserAiDO);

        UserDO user = new UserDO();
        user.setId(userAiDO.getUserId());
        // 如果是微信注册用户
        if (LoginTypeEnum.WECHAT.getType() == userDO.getLoginType()) {
            // 用户登录名也重置
            user.setUserName("");
        }

        // 密码重置为
        user.setPassword(userPwdEncoder.encPwd("paicoding"));
        userDao.saveUser(user);
    }
}
