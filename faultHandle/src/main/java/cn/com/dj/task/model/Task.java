package cn.com.dj.task.model;

import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by dujiang02 on 17/7/23.
 */
public class Task {

    public Task() {
    }

    public Task(List<ObjectId> tasks) {
        this.tasks = tasks;
    }

    private List<ObjectId> tasks;

    public List<ObjectId> getTasks() {
        return tasks;
    }

    public void setTasks(List<ObjectId> tasks) {
        this.tasks = tasks;
    }
}
