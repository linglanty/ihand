package cn.com.dj.task;

import cn.com.dj.task.model.Task;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by dujiang02 on 17/7/23.
 */
@Service
public class RpcClient {

    private static final RestTemplate template = new RestTemplate();

    public boolean checkMachine(String ip) {
        try{
            String url = ip +":8080/api/task/listcheck";
            Boolean object = template.getForObject(url, Boolean.class);
            if (object == true) {
                return true;
            }
        }catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean sendTasks(String ip, List<ObjectId> objectIds) {
        try {
            String url = ip +":8080/api/task/execTasks";
            Task task =new Task(objectIds);
            Boolean acceptTask = template.postForObject(url, task, Boolean.class);
            if (true == acceptTask) {
                return true;
            }
        } catch (Exception e) {
            return  false;
        }
        return false;

    }

}
