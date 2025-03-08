package devblackholemax.easychattingroom.service.impl;

import devblackholemax.easychattingroom.dao.UserRepository;
import devblackholemax.easychattingroom.domain.User;
import devblackholemax.easychattingroom.service.UserService;
import devblackholemax.easychattingroom.untils.Md5Util;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserByName(String username) {
        return userRepository.getUserByUsername(username);
    }

    @Override
    public void register(String username, String password) {
        String md5String = Md5Util.getMD5String(password);
        userRepository.save(new User(username, md5String));
    }
}