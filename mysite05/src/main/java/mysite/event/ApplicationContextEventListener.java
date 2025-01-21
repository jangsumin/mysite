package mysite.event;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import mysite.service.SiteService;
import mysite.vo.SiteVo;

public class ApplicationContextEventListener {
	@Autowired
	private ApplicationContext applicationContext;
	
	@EventListener({ContextRefreshedEvent.class})
	public void handlerContextRefreshEvent() {
		System.out.println("-- Context Refreshed Event Received --");
		
		SiteService siteService = applicationContext.getBean(SiteService.class);
		// 여기서 어플리케이션 스코프가 시작되므로 여기서 SiteVo를 관리하라는 의미 같음
		SiteVo vo = siteService.getSite();
		
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("title", vo.getTitle());
		propertyValues.add("welcome", vo.getWelcome());
		propertyValues.add("profile", vo.getProfile());
		propertyValues.add("description", vo.getDescription());
		
		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClass(SiteVo.class);
		beanDefinition.setPropertyValues(propertyValues);
		
		BeanDefinitionRegistry registry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
		registry.registerBeanDefinition("site", beanDefinition);
	}
}
