package com.hecvd19.pasdept.home;

import com.hecvd19.pasdept.models.Post;

public class Operation {
    public Post post;
    public int type;

    public Operation(Post post, int type) {
        this.post = post;
        this.type = type;
    }
}
