package com.example.demo16.myssm.myspringmvc;

import com.example.demo16.myssm.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet{
    private Map<String, Object> BeanMap = new HashMap<>();
    public DispatcherServlet() throws ParserConfigurationException, IOException, SAXException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        //InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("applicationContext.xml");
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\20179\\IdeaProjects\\demo16\\src\\applicationContext.xml");
        // 创建documentBuilderFactory对象
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        // 创建documentBuilder对象
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        // 获取document对象
        Document document = documentBuilder.parse(fileInputStream);
        // 获取所有的bean节点
        NodeList beanNodeList = document.getElementsByTagName("bean");
        for(int i = 0;i < beanNodeList.getLength();i ++) {
            Node beannode = beanNodeList.item(i);
            if (beannode.getNodeType() == Node.ELEMENT_NODE) {
                Element beanElement = (Element) beannode;
                String BeanId = beanElement.getAttribute("id");
                String ClassName = beanElement.getAttribute("class");
                Object o = Class.forName(ClassName).newInstance();
                Field servletContext = Class.forName(ClassName).getDeclaredField("servletContext");
                servletContext.setAccessible(true);
                servletContext.set(o,this.getServletContext());
                BeanMap.put(BeanId,o);
            }
        }
    }
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
        Object controllerobj = BeanMap.get(substring1);
        String oper = req.getParameter("oper");
        if (StringUtil.isEmpty(oper)) {
            oper = "index";
        }
        try {
            Method declaredMethod = controllerobj.getClass().getDeclaredMethod(oper, HttpServletRequest.class, HttpServletResponse.class);
            if (declaredMethod != null) {
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(controllerobj,req,resp);
            } else {
                throw new RuntimeException("oper error");
            }

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
        }
    }
}
