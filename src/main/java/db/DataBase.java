package db;

import com.google.common.collect.Maps;
import model.Post;
import model.User;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class DataBase {
    private static Map<String, User> users = Maps.newHashMap();
    private static Map<Long, Post> posts = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAllUsers() {
        return users.values();
    }

    public static void addPost(Post post) {
        posts.put(post.getId(), post);
    }

    public static Post findPostById(Long id) {
        return posts.get(id);
    }

    public static Collection<Post> findAllPosts() {
        return posts.values().stream().sorted(Comparator.comparing(Post::getPostTime).reversed()).collect(Collectors.toList());
    }
}
