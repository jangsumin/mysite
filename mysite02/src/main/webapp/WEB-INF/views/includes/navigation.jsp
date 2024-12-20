<%@ page import="mysite.vo.UserVo" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="navigation">
	<ul>
		<c:choose>
			<c:when test="${empty authUser}">
				<li><a href="${pageContext.request.contextPath}">홈</a></li>		
			</c:when>
			
			<c:otherwise>
				<li><a href="${pageContext.request.contextPath}">${authUser.name}</a></li>	
			</c:otherwise>
		</c:choose>
		<li><a href="${pageContext.request.contextPath}/guestbook">방명록</a></li>
		<li><a href="${pageContext.request.contextPath}/board">게시판</a></li>
	</ul>
</div>