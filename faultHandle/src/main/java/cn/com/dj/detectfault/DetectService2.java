package cn.com.dj.detectfault;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DetectService2 {

	@Value("${config.detect.url}")
	private String url;
	
	public void testDetecting() {
		
		//System.out.println("Detecting start!"+new Date().getTime());
		System.out.println(new RestTemplate().getForEntity(url, String.class));
		//System.out.println("Detecting end!"+new Date().getTime());
	}
}
