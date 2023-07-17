package com.bitacademy.mysite.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bitacademy.mysite.dao.BoardDao;
import com.bitacademy.mysite.vo.BoardVo;
import com.bitacademy.mysite.vo.UserVo;

public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		String actionName = request.getParameter("a");
		if("writeform".equals(actionName)) {
			// 접근제어, Access Control
			//////////////////////////////////////////////////////////////////
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			if(authUser == null) {
				response.sendRedirect(request.getContextPath());
				return;
			}
			//////////////////////////////////////////////////////////////////
			
			request.getRequestDispatcher("/WEB-INF/views/board/writeform.jsp")
			.forward(request, response);
		} else if("write".equals(actionName)) {
			// 접근제어, Access Control
			//////////////////////////////////////////////////////////////////
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			if(authUser == null) {
				response.sendRedirect(request.getContextPath());
				return;
			}
			//////////////////////////////////////////////////////////////////
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			BoardVo boardVo = new BoardVo();
			boardVo.setTitle(title);
			boardVo.setContent(content);
			boardVo.setWriter(authUser.getName());
			boardVo.setWriterNo(authUser.getNo());
			
			new BoardDao().writeContent(boardVo);
			
			response.sendRedirect(request.getContextPath()+ "/board");
		} else if("replyform".equals(actionName)) {
			// 접근제어, Access Control
			//////////////////////////////////////////////////////////////////
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			if(authUser == null) {
				response.sendRedirect(request.getContextPath());
				return;
			}
			//////////////////////////////////////////////////////////////////
			
			request.getRequestDispatcher("/WEB-INF/views/board/replyform.jsp")
			.forward(request, response);
		} else if("reply".equals(actionName)) {
			// 접근제어, Access Control
			//////////////////////////////////////////////////////////////////
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			if(authUser == null) {
				response.sendRedirect(request.getContextPath());
				return;
			}
			//////////////////////////////////////////////////////////////////
			// 기존 글 정보 가져오기
			String boardNo = request.getParameter("no");
			BoardVo viewVo = new BoardDao().findByBoardNo(Long.parseLong(boardNo));
			// 답변 
			String title = request.getParameter("title");
			String content = request.getParameter("content");			
			// insert
			BoardVo boardVo = new BoardVo();
			boardVo.setTitle(title);
			boardVo.setContent(content);
			boardVo.setGroupNo(viewVo.getGroupNo());
			boardVo.setOrderNo(viewVo.getOrderNo());
			boardVo.setDepth(viewVo.getDepth());
			boardVo.setWriter(authUser.getName());
			boardVo.setWriterNo(authUser.getNo());
			
			new BoardDao().replyContent(boardVo);
			
			response.sendRedirect(request.getContextPath()+ "/board");
		} else if("view".equals(actionName)) {
			String boardNo = request.getParameter("no");
			BoardVo viewVo = new BoardDao().findByBoardNo(Long.parseLong(boardNo)); // 여기에 글제목,내용, 글쓴유저 번호

			request.setAttribute("viewvo", viewVo);
			
			request.getRequestDispatcher("/WEB-INF/views/board/view.jsp")
			.forward(request, response);
		} else if("updateform".equals(actionName)) {
			
			
			request.getRequestDispatcher("/WEB-INF/views/board/updateform.jsp")
			.forward(request, response);
		} else if("update".equals(actionName)) {
			
		} else if("deleteform".equals(actionName)) {
			
			request.getRequestDispatcher("/WEB-INF/views/board/deleteform.jsp")
			.forward(request, response);
		} else {
			// default action
			BoardDao dao = new BoardDao();
			List<BoardVo> list = new ArrayList<>();
			
			list = dao.findAll();
			
			request.setAttribute("list", list);
			
			request.getRequestDispatcher("/WEB-INF/views/board/list.jsp")
			.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}
	
}
