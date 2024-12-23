package mysite.controller.action.board;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mysite.controller.ActionServlet.Action;
import mysite.dao.BoardDao;
import mysite.vo.BoardVo;

public class ReplyFormAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("id"));
		BoardVo vo = new BoardVo();
		vo.setId(id);
		
		new BoardDao().getParentPostInfo(vo);
		request.setAttribute("info", vo);
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/reply.jsp");
		rd.forward(request, response);
	}

}
