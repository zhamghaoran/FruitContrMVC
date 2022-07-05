package com.example.demo16.fruit.servlets;

import com.example.demo16.fruit.dao.FruitDAO;
import com.example.demo16.fruit.dao.impl.FruitDAOImpl;
import com.example.demo16.fruit.pojo.Fruit;
import com.example.demo16.myssm.myspringmvc.ViewBaseServlet;
import com.example.demo16.myssm.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

//Servlet从3.0版本开始支持注解方式的注册
@WebServlet("/index")
public class IndexServlet extends ViewBaseServlet {
    @Override

    public void doGet(HttpServletRequest request , HttpServletResponse response)throws IOException, ServletException {
        request.setCharacterEncoding("utf-8");
        HttpSession session = request.getSession();
        int page = 1;
        String oper = request.getParameter("oper");
        // 如果oper不为空则是通过表单的查询按钮过来的,如果oper为空则不是通过按键过来的
        String keyword = null;
        if (StringUtil.isNotEmpty(oper) && "search".equals(oper)) {  // 说明我是通过表单过来的，pageno变为1，keyword从请求参数中获取
            page = 1;
            keyword = request.getParameter("keyword");
            if (StringUtil.isEmpty(keyword))
                keyword = "";
            session.setAttribute("keyword",keyword);
        } else {
            // 说明此处不是点击表单查询发送过来的请求，此时keyword应从session中获取
            String pageNo = request.getParameter("pageNo");
            if (StringUtil.isNotEmpty(pageNo)) {
                page = Integer.parseInt(pageNo);
            }
            Object keyword1 = session.getAttribute("keyword");
            if (keyword1 != null) {
                keyword = (String) keyword1;
            } else {
                keyword = "";
            }
        }
        session.setAttribute("pageNo",page);
        FruitDAOImpl fruitDAO = new FruitDAOImpl();
        List<Fruit> fruitList = fruitDAO.getFruitList(keyword,page);
        Long fruitCount = fruitDAO.getFruitCount(keyword);
        Long pageCount = (fruitCount + 5 - 1) / 5;
        session.setAttribute("pageCount",pageCount);
        //保存到session作用域
        session.setAttribute("fruitList",fruitList);
        //此处的视图名称是 index
        //那么thymeleaf会将这个 逻辑视图名称 对应到 物理视图 名称上去
        //逻辑视图名称 ：   index
        //物理视图名称 ：   view-prefix + 逻辑视图名称 + view-suffix
        //所以真实的视图名称是：      /       index       .html
        super.processTemplate("index",request,response);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
