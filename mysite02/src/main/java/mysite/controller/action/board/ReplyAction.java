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

public class ReplyAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		String contents = request.getParameter("content");
		long groupNo = Long.parseLong(request.getParameter("groupNo"));
		long orderNo = Long.parseLong(request.getParameter("orderNo"));
		long depth = Long.parseLong(request.getParameter("depth"));
		
		// get userId
		HttpSession session = request.getSession(false);
		UserVo userVo = (UserVo) session.getAttribute("authUser");
		long userId = userVo.getId();
		
		BoardVo vo = new BoardVo();
		vo.setTitle(title);
		vo.setContents(contents);
		vo.setGroupNo(groupNo);
		vo.setOrderNo(orderNo + 1);
		vo.setDepth(depth + 1);
		vo.setUserId(userId);
		
		new BoardDao().insertReply(vo);
		
		response.sendRedirect(request.getContextPath() + "/board");
	}

}
