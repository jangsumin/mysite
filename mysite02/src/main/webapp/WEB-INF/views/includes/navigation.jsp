<%@ page import="mysite.vo.UserVo" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	UserVo authUser = (UserVo) session.getAttribute("authUser");
%>
<div id="navigation">
	<ul>
		<% if (authUser == null) { %>
			<li><a href="<%=request.getContextPath()%>">홈</a></li>
		<% } else { %>
			<li><a href="<%=request.getContextPath()%>"><%= authUser.getName() %></a></li>
		<% } %>
		<li><a href="<%=request.getContextPath()%>/guestbook">방명록</a></li>
		<li><a href="<%=request.getContextPath()%>/board">게시판</a></li>
	</ul>
</div>