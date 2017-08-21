package cn.com.dj.controller;

import cn.com.dj.service.DeviceService;
import cn.com.dj.task.DetectService;
import cn.com.dj.task.model.Task;
import cn.com.inhand.common.model.Device;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping()
public class TaskController {

    @Autowired
    private DetectService detectService;

    @Autowired
    private DeviceService deviceService;

    @Value("${config.detect.oid}")
    private String oId;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * 验证机器是否正常运行
     * @param request api/task/listcheck
     * @return
     */
    @RequestMapping(value = { "api/task/listcheck", "api/task/listcheck.json" }, method = RequestMethod.GET)
    public Boolean listcheck(HttpServletRequest request) {
        return true;
    }

    /**
     * 执行定时任务
     */
    /**
     * api/task/execTasks
     * @param task
     * @return
     */
    @RequestMapping(value={"api/task/execTasks"}, method={RequestMethod.POST})
    @ResponseBody
    public Boolean execTasks(Task task) {
        if (task == null) {
            return false;
        }
        final ObjectId oId = new ObjectId(this.oId);
        List<ObjectId> tasks = task.getTasks();//30个元素
        for (final ObjectId deviceId : tasks) {
            executorService.submit(new Runnable() {
                @Override public void run() {
                    Device onlineDevice = deviceService.getDeviceById(oId, deviceId);
                    detectService.detectFaults(onlineDevice);
                }
            });
        }

        return true;
    }

}
