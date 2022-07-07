package com.example.demo16.fruit.controllers;

import com.example.demo16.fruit.dao.FruitDAO;
import com.example.demo16.fruit.dao.impl.FruitDAOImpl;
import com.example.demo16.fruit.pojo.Fruit;
import com.example.demo16.myssm.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class FruitController  {
    FruitDAO fruit = new FruitDAOImpl();
    private String index(String oper, String keyword, Integer pageNo, HttpServletRequest request ) {
        HttpSession session = request.getSession();
        if (pageNo == null)
            pageNo = 1;
        if (StringUtil.isNotEmpty(oper) && "search".equals(oper)) {
            pageNo = 1;
            if (StringUtil.isEmpty(keyword))
                keyword = "";
            session.setAttribute("keyword",keyword);
            }
        else {
            Object keyword1 = session.getAttribute("keyword");
            if (keyword1 != null) {
                keyword = (String) keyword1;
            } else {
                keyword = "";
            }
        }
        session.setAttribute("keyword",keyword);
        session.setAttribute("pageNo",pageNo);
        FruitDAOImpl fruitDAO = new FruitDAOImpl();
        List<Fruit> fruitList = fruitDAO.getFruitList(keyword,pageNo);
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
        return "index";
    }
    private String add(String fname,Integer price ,Integer fcount ,String remark)  {
        fruit.addFruit(new Fruit(0,fname,price,fcount,remark));
        return "redirect:fruit.do";
    }
    private String del(Integer fid)  {
        if (fid != null) {
            fruit.delFruit(fid);
            return "redirect:fruit.do";
        }
        return "error";
    }
    private String edit(Integer fid ,HttpServletRequest request) {
        if (fid != null) {
            Fruit fruitByFid = fruit.getFruitByFid(fid);
            request.setAttribute("fruit",fruitByFid);
            return "edit";
        }
        return "error";
    }
    private String update(Integer fid,String fname ,Integer price ,Integer fcount ,String remark) {
        // 执行更新
        fruit.UpdataFruit(new Fruit(fid,fname,price,fcount,remark));
        // 此处需要重定向，目的是重新给IndexServlet发请求，重新获取FruitList，然后覆盖到Session中，这样首页上显示的session中的数据才是最新的
        return "redirect:fruit.do";
    }
}
