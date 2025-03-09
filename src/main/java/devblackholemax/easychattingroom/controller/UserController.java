package devblackholemax.easychattingroom.controller;

import devblackholemax.easychattingroom.domain.InviteCode;
import devblackholemax.easychattingroom.domain.Result;
import devblackholemax.easychattingroom.domain.User;
import devblackholemax.easychattingroom.service.InviteCodeService;
import devblackholemax.easychattingroom.service.UserService;
import devblackholemax.easychattingroom.untils.JwtUtil;
import devblackholemax.easychattingroom.untils.Md5Util;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat/user")
public class UserController {
    @Resource
    UserService userService;

    @Resource
    InviteCodeService inviteCodeService;

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/register")
    public Result register(String username, String password, String code) {
        User user = userService.getUserByName(username);
        InviteCode inviteCode = inviteCodeService.getInviteCodeByCode(code);
        if (user == null && inviteCode != null) {
            userService.register(username, password);
            return Result.success();
        } else {
            return Result.error("用户名已被占用");
        }
    }

    @PostMapping("/login")
    public Result login(String username, String password) {
        User loginUser = userService.getUserByName(username);
        if (loginUser == null) {
            return Result.error("用户名错误");
        }

        String encryptedPassword = Md5Util.getMD5String(password);
        if (!encryptedPassword.equals(loginUser.getPassword())) {
            return Result.error("密码错误");
        }

        String token = JwtUtil.generateToken(loginUser.getId(), loginUser.getUsername());

        return Result.success(token);
    }
}
