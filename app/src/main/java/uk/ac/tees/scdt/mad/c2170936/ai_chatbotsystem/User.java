package uk.ac.tees.scdt.mad.c2170936.ai_chatbotsystem;

import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

public class User implements IUser {

    //variables
    String id,name,avatar;


    //Initializing the constructor
    public User(String id, String name, String avatar){
        this.id = id;
        this.name = name;
        this.avatar = avatar;

    }
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }
}
