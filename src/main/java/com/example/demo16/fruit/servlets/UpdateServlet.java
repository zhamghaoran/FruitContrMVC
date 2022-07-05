package com.example.demo16.fruit.servlets;

import com.example.demo16.fruit.dao.FruitDAO;
import com.example.demo16.fruit.dao.impl.FruitDAOImpl;
import com.example.demo16.fruit.pojo.Fruit;
import com.example.demo16.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet("/update.do")
public class UpdateServlet extends ViewBaseServlet {
    private FruitDAO fruitDAO = new FruitDAOImpl();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
        fruitDAO.UpdataFruit(new Fruit(fid1,fname,price1,count,remark));
        // 资源跳转

        //super.processTemplate("index",req,resp);
        // req.getDispatchar("index.html").forward(req,resp);
        // 此处需要重定向，目的是重新给IndexServlet发请求，重新获取FruitList，然后覆盖到Session中，这样首页上显示的session中的数据才是最新的
        resp.sendRedirect("index");
    }

}
