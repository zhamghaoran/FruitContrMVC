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
import java.io.IOException;
@WebServlet("/edit.do")
public class EditServlet extends ViewBaseServlet {
    private FruitDAO fruitDAO = new FruitDAOImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fid = req.getParameter("fid");
        if (StringUtil.isNotEmpty(fid)) {
            int fid1 = Integer.parseInt(fid);
            Fruit fruitByFid = fruitDAO.getFruitByFid(fid1);
            req.setAttribute("fruit",fruitByFid);
            super.processTemplate("edit",req,resp);
        }
    }
}
