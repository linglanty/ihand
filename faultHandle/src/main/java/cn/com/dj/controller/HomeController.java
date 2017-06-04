package cn.com.dj.controller;
 
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.dj.detectfault.DetectService;
import cn.com.inhand.common.bean.ServerInfoBean;


@Controller
public class HomeController {

	@Autowired
	DetectService detectService;
	
	
	@RequestMapping(value = "/dj", method = RequestMethod.GET)
    public Object home(Locale locale) {
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
        String formattedDate = dateFormat.format(date);
        detectService.detectFaults();

        return new ServerInfoBean("template", formattedDate);
    }
	
   // @RequestMapping(value = "/dj", method = RequestMethod.GET)
	/*@RequestMapping(value = "/home", method = RequestMethod.GET)
	@ResponseBody
    public Object home() {
        //@RequestParam(required=false, defaultValue="1231") String time
        System.out.println("Get here!!");
        detectService.detectFaults();
        return new Date().getTime();
    }*/

}
