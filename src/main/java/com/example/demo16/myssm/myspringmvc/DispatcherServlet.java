package com.example.demo16.myssm.myspringmvc;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet{
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        // 假设url是 ：http://localhost:8080/hello.do
        // 那么ServletPath是：/hello.do
        // 我的思路是第一步/hello.do --> hello
        String servletPath = req.getServletPath();
        String substring = servletPath.substring(1);
        int i = substring.lastIndexOf(".do");
        String substring1 = substring.substring(0, i);
        //System.out.println(substring1);

    }
}
