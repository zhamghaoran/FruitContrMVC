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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@WebServlet("/fruit.do")
public class FruitServlet extends ViewBaseServlet {
    FruitDAO fruit = new FruitDAOImpl();
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String oper = req.getParameter("oper");
        if (StringUtil.isEmpty(oper)) {
            oper = "index";
        }
        Method[] Methods = this.getClass().getDeclaredMethods(); // 获取当前类中的所有方法
        for( Method m : Methods) {
            String name = m.getName();
            if (oper.equals(name)) {  // 找到和oper同名的方法
                try {
                    m.invoke(this,req,resp);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        switch (oper) {
            case "index":
                index(req, resp);
                break;
            case "add" :
                add(req, resp);
                break;
            case "delete":
                delete(req, resp);
                break;
            case "edit":
                edit(req, resp);
                break;
            case "update":
                update(req, resp);
                break;
            default:
                System.out.println(oper);
        }
    }
    private void index(HttpServletRequest request , HttpServletResponse response)throws IOException, ServletException {
        int page = 1;
        String oper = request.getParameter("oper");
        // 如果oper不为空则是通过表单的查询按钮过来的,如果oper为空则不是通过按键过来的
        String keyword = null;
        HttpSession session = request.getSession();
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
    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String fname = req.getParameter("fname");
        String price = req.getParameter("price");
        String fcount = req.getParameter("fcount");
        String remark = req.getParameter("remark");
        int pri = Integer.parseInt(price);
        int count = Integer.parseInt(fcount);
        fruit.addFruit(new Fruit(0,fname,pri,count,remark));
        resp.sendRedirect("fruit.do");
    }
    private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fid = req.getParameter("fid");
        if (StringUtil.isNotEmpty(fid)) {
            int fid1 = Integer.parseInt(fid);
            fruit.delFruit(fid1);
            resp.sendRedirect("fruit.do");
        }
    }
    private void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fid = req.getParameter("fid");
        if (StringUtil.isNotEmpty(fid)) {
            int fid1 = Integer.parseInt(fid);
            Fruit fruitByFid = fruit.getFruitByFid(fid1);
            req.setAttribute("fruit",fruitByFid);
            super.processTemplate("edit",req,resp);
        }
    }
    private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置编码
        req.setCharacterEncoding("utf-8");
        // 获取参数
        String fname = req.getParameter("fname");
        String price = req.getParameter("price");
        int price1 = Integer.parseInt(price);
        String fcount = req.getParameter("fcount");
        int count = Integer.parseInt(fcount);
        String remark = req.getParameter("remark");
        String fid = req.getParameter("fid");
        int fid1 = Integer.parseInt(fid);
        // 执行更新
        fruit.UpdataFruit(new Fruit(fid1,fname,price1,count,remark));
        // 资源跳转

        //super.processTemplate("index",req,resp);
        // req.getDispatchar("index.html").forward(req,resp);
        // 此处需要重定向，目的是重新给IndexServlet发请求，重新获取FruitList，然后覆盖到Session中，这样首页上显示的session中的数据才是最新的
        resp.sendRedirect("fruit.do");
    }
}
