package mysite.controller.action.user;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mysite.controller.ActionServlet.Action;
import mysite.dao.UserDao;
import mysite.vo.UserVo;

public class UpdateAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String gender = request.getParameter("gender");
		
		UserVo vo = new UserVo();
		vo.setId(Long.parseLong(id));
		vo.setName(name);
		vo.setPassword(password);
		vo.setGender(gender);
		
		new UserDao().update(vo);
		
		HttpSession session = request.getSession(true); // 세션 다시 만들기
		vo.setPassword(null);
		vo.setGender(null);
		session.setAttribute("authUser", vo);
		
		response.sendRedirect(request.getContextPath());
	}

}
