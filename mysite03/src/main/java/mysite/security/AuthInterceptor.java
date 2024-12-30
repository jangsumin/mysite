package mysite.security;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mysite.vo.UserVo;

public class AuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// 1. Handler 종류 확인
		if (!(handler instanceof HandlerMethod)) {
			// DefualtServletRequestHandler 타입인 경우 
			// DefaultServletHandler가 처리하는 경우(정적 자원, /assets/**, mapping이 안되어 있는 URL)
			return true;
		}
 		
		// 2. casting
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		// 3. Handler에서 @Auth 가져오기
		Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
		
		// 4. Handler Method에서 @Auth가 없으면 클래스(타입)에 @Auth
		// 과제
//		if (auth == null) {
//			
//		}
		
		// 5. @Auth가 없으면
		if (auth == null) {
			return true;
		}
		
		// role에 따라 다른 처리가 필요하다.(Authorization)
		String role = auth.role();
		
		// 
		
		// 6. @Auth가 붙어 있어서 인증(Authentication) 여부 확인
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		
		if (authUser == null) {
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}
		
		// 7. @Auth가 붙어 있고 인증도 된 경우
		return true;
	}
	
}
