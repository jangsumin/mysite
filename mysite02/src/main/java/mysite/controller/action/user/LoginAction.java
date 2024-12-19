package mysite.controller.action.user;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mysite.controller.ActionServlet.Action;
import mysite.dao.UserDao;
import mysite.vo.UserVo;

public class LoginAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		UserVo vo = new UserDao().findByEmailAndPassword(email, password);
		
		// 로그인 실패
		if (vo == null) {
			// response.sendRedirect(request.getContextPath() + "/user?a=loginform&result=fail&email=" + email);
			
			request.setAttribute("result", "fail");
			request.setAttribute("email", email);
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/loginform.jsp");
			rd.forward(request, response);
			
			return;
		}
		
		// 로그인 처리
		HttpSession session = request.getSession(true); // 없을 때는 만들어주므로, 로그인 시에는 true를 보내야 한다.
		session.setAttribute("authUser", vo);
		
		response.sendRedirect(request.getContextPath());
	}

}
