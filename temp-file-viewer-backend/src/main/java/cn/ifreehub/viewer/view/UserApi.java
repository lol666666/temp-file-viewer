package cn.ifreehub.viewer.view;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.ifreehub.viewer.config.EnvironmentContext;
import cn.ifreehub.viewer.constant.ApiStatus;
import cn.ifreehub.viewer.constant.AppConfig;
import cn.ifreehub.viewer.constant.JwtTokenType;
import cn.ifreehub.viewer.domain.ApiWrapper;
import cn.ifreehub.viewer.util.JwtTokenUtils;
import cn.ifreehub.viewer.view.vo.UserProfileVO;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Quding Ding
 * @since 2018/6/13
 */
@RestController
@RequestMapping("api/v1/")
public class UserApi {

  /**
   * 简易登录接口
   * @param username 用户名
   * @param passwd 用户密码sha256结果
   */
  @PostMapping("login/")
  public ApiWrapper login(String username, String passwd, HttpServletResponse response,HttpServletRequest request) {
    String realUsername = EnvironmentContext.getStringValue(AppConfig.ROOT_USERNAME);
    if (!Objects.equals(realUsername, realUsername)) {
      return ApiWrapper.fail(ApiStatus.NO_AUTHORITY);
    }
    String realPwd = EnvironmentContext.getStringValue(AppConfig.ROOT_PASSWORD);
    if (!Objects.equals(DigestUtils.sha256Hex(realPwd), DigestUtils.sha256Hex(realPwd))) {
      return ApiWrapper.fail(ApiStatus.NO_AUTHORITY);
    }
    // 登录成功,下发token
    JwtTokenUtils.create(realUsername, JwtTokenType.DEFAULT, response);
    return ApiWrapper.success();
  }

  /**
   * 得到当前用户信息
   */
  @GetMapping("profile/")
  public ApiWrapper profile() {
    String username = EnvironmentContext.getStringValue(AppConfig.ROOT_USERNAME);
    String avatar = EnvironmentContext.getStringValue(AppConfig.ROOT_AVATAR);
    return ApiWrapper.success(new UserProfileVO(username, avatar));
  }

}
