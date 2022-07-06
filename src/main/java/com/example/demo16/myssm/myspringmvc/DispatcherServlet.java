package com.example.demo16.myssm.myspringmvc;

import com.example.demo16.myssm.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet{
    private Map<String, Object> BeanMap = new HashMap<>();
    public void init() {
        //InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("applicationContext.xml");
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("C:\\Users\\20179\\IdeaProjects\\demo16\\src\\applicationContext.xml");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        // 创建documentBuilderFactory对象
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        // 创建documentBuilder对象
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        // 获取document对象
        Document document = null;
        try {
            document = documentBuilder.parse(fileInputStream);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 获取所有的bean节点
        NodeList beanNodeList = document.getElementsByTagName("bean");
        for(int i = 0;i < beanNodeList.getLength();i ++) {
            Node beannode = beanNodeList.item(i);
            if (beannode.getNodeType() == Node.ELEMENT_NODE) {
                Element beanElement = (Element) beannode;
                String BeanId = beanElement.getAttribute("id");
                String ClassName = beanElement.getAttribute("class");
                Object o = null;
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
                Object Methodretuern = declaredMethod.invoke(controllerobj,req,resp);
                String methodreturn = (String) Methodretuern;
                if (methodreturn.startsWith("redirect:")) {
                    String redirectStr = methodreturn.substring("redirect:".length());
                    resp.sendRedirect(redirectStr);
                }
            } else {
                throw new RuntimeException("oper error");
            }

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
        }
    }
}
