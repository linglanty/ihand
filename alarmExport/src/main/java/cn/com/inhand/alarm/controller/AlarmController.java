package cn.com.inhand.alarm.controller;

import cn.com.inhand.alarm.dao.AlarmDao;
import cn.com.inhand.alarm.dto.AlarmBean; 
import cn.com.inhand.alarm.view.ExportExcel;
import cn.com.inhand.common.dto.TokenValidateResult;
import cn.com.inhand.common.oauth2.TokenClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"api/alarmExport"})
public class AlarmController {

    @Autowired
    TokenClient tokenClient;
    
    @Autowired
    private ExportExcel viewExcel;
    
    @Autowired
    private AlarmDao alarmService;
     
     
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView exportAlarm(
            @RequestParam(value = "access_token", required = true) String access_token,
            @RequestParam(value="oid_user", required=false) ObjectId oId,
            @RequestParam(value = "params", required = false) String params,
            HttpServletRequest request,
            HttpServletResponse response) {
 
        TokenValidateResult tokenDto = this.tokenClient.validateAccessToken(access_token, "api/alarmExport/export", "post", Double.valueOf(16).intValue(), Double.valueOf(0).intValue(), oId);
        oId = tokenDto.getOid();
        
        List<AlarmBean> alarmList = alarmService.getAlarmData(params, oId); 
//        params.setoId(oId);
        Map model = new HashMap(); 
        model.put("alarmList", alarmList);
        ModelAndView mav = new ModelAndView(viewExcel, model);
        return mav;
    }

}
