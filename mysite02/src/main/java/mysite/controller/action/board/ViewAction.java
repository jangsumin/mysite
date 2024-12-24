package mysite.controller.action.board;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mysite.controller.ActionServlet.Action;
import mysite.dao.BoardDao;
import mysite.vo.BoardVo;

public class ViewAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("id"));
		
		BoardDao dao = new BoardDao();
		
		Cookie[] cookies = request.getCookies();
		boolean hasViewed = false;
		
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("viewed_" + id)) {
					hasViewed = true;
					break;
				}
			}
		}
		
		if (!hasViewed) {
			dao.updatePostViewCount(id);
			Cookie cookie = new Cookie("viewed_" + id, "true");
			cookie.setMaxAge(60 * 30); // expired period: 30min
			cookie.setPath(request.getContextPath());
			response.addCookie(cookie);
		}
		
		BoardVo vo = dao.findPostById(id);
		request.setAttribute("post", vo);
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/view.jsp");
		rd.forward(request, response);
	}

}
