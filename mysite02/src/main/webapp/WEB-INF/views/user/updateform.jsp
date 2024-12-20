<%@ page import="mysite.vo.UserVo" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	UserVo vo = (UserVo) request.getAttribute("vo");
	Long id = vo.getId();
	String name = vo.getName();
	String email = vo.getEmail();
	String gender = vo.getGender();
%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="<%=request.getContextPath() %>/assets/css/user.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="user">

				<form id="join-form" name="joinForm" method="post" action="/mysite02/user">
					<input type="hidden" name="a" value="update">
					<input type="hidden" name="id" value="<%= id %>">
					
					<label class="block-label" for="name">이름</label>
					<input id="name" name="name" type="text" value="<%= name %>">

					<label class="block-label" for="email">이메일</label>
					<h4><%= email %></h4>
					
					<label class="block-label">패스워드</label>
					<input name="password" type="password" value="">
					
					<fieldset>
						<legend>성별</legend>
						<% if ("female".equals(gender)) { %>
							<label>여</label> <input type="radio" name="gender" value="female" checked>
							<label>남</label> <input type="radio" name="gender" value="male">
						<% } else { %>
							<label>여</label> <input type="radio" name="gender" value="female">
							<label>남</label> <input type="radio" name="gender" value="male" checked>
						<% } %>
					</fieldset>
					
					<input type="submit" value="수정하기">
					
				</form>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>