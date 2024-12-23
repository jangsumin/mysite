package mysite.controller.action.board;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mysite.controller.ActionServlet.Action;
import mysite.dao.BoardDao;
import mysite.vo.BoardVo;
import mysite.vo.UserVo;

public class WriteAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		String contents = request.getParameter("content");
		
		// get userId
		HttpSession session = request.getSession(false);
		UserVo userVo = (UserVo) session.getAttribute("authUser");
		long userId = userVo.getId();
			
		BoardVo boardVo = new BoardVo();
		boardVo.setTitle(title);
		boardVo.setContents(contents);
		boardVo.setOrderNo(1);
		boardVo.setDepth(0);
		boardVo.setUserId(userId);
		
		new BoardDao().insert(boardVo);
		
		response.sendRedirect(request.getContextPath() + "/board");
	}

}
