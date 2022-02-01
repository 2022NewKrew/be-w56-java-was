package app.model;

import java.util.List;

public class Posts {
    List<Post> posts;

    public Posts(List<Post> posts){
        this.posts = posts;
    }

    public List<Post> getPosts() {
        return posts;
    }
}
