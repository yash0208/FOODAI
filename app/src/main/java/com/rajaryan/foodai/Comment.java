package com.rajaryan.foodai;

public class Comment {
    String Comment,User,Pic,Name;

    public Comment(String comment, String user, String pic, String name) {
        Comment = comment;
        User = user;
        Pic = pic;
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public Comment() {
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }
}
