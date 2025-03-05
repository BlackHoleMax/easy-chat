package devblackholemax.easychattingroom.service;

import devblackholemax.easychattingroom.domain.User;

import java.util.List;

public interface UserService {
    public List<User> getAllUsers();

    public void saveUser(User user);

    public User getUserByName(String username);

    public void register(String username, String password);
}
