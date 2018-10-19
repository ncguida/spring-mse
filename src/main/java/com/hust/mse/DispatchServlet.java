package com.hust.mse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import com.hust.mse.bean.Data;
import com.hust.mse.bean.Handler;
import com.hust.mse.bean.Param;
import com.hust.mse.bean.View;
import com.hust.mse.helper.BeanHelper;
import com.hust.mse.helper.ConfigHelper;
import com.hust.mse.helper.ControllerHelper;
import com.hust.mse.utils.CodeUtil;
import com.hust.mse.utils.ReflectionUtil;
import com.hust.mse.utils.StreamUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 类的用处
 *
 * @author ncguida
 * @date 2018/10/13
 */
public class DispatchServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        //初始化相关helper类
        HelperLoader.init();

        ServletContext servletContext = config.getServletContext();
        //注册jsp的servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
        //注册静态资源的默认Servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestMethod = req.getMethod().toLowerCase();
        String requestPath = req.getPathInfo();

        Handler handler = ControllerHelper.getHandler(requestPath, requestMethod);

        if (handler != null) {
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getByType(controllerClass);
            Param param = parseParamFromReq(req);
            Method actionMethod = handler.getActionMethod();

            Object result = ReflectionUtil.invokeMethod(actionMethod, controllerBean, param);

            if (result instanceof View) {
                handleViewResult((View)result, req, resp);
            } else if (result instanceof Data) {
                handleDataResult((Data)result, req, resp);
            }

        }

    }

    private void handleDataResult(Data data, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Object model = data.getModel();
        if (model != null) {
            writeJson(model, req, resp);
        }
    }

    private void writeJson(Object model, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        writer.write(JSON.toJSONString(model));
        writer.flush();
        writer.close();
    }

    private void handleViewResult(View view, HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        String path = view.getPath();
        if (path.startsWith("/")) {
            resp.sendRedirect(req.getContextPath() + path);
        } else {
            Map<String, Object> modelMap = view.getModel();
            for (Entry<String, Object> entry : modelMap.entrySet()) {
                req.setAttribute(entry.getKey(), entry.getValue());
            }
            req.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(req, resp);
        }
    }

    private Param parseParamFromReq(HttpServletRequest req) throws IOException {
        Map<String, Object> paramMap = new HashMap<>();
        //解析
        Enumeration<String> parameterNames = req.getParameterNames();
        if (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = req.getParameter(paramName);
            paramMap.put(paramName, paramValue);
        }

        ServletInputStream is = req.getInputStream();

        String body = CodeUtil.decodeUrl(StreamUtil.getString(is));
        if (StringUtils.isNotBlank(body)) {
            String[] params = StringUtils.split(body, "&");

            if (ArrayUtils.isNotEmpty(params)) {
                for (String param : params) {
                    String[] paramArr = StringUtils.split(param, "=");
                    if (ArrayUtils.isNotEmpty(paramArr) && paramArr.length == 2) {
                        paramMap.put(paramArr[0], paramArr[1]);
                    }
                }
            }

        }
        return new Param(paramMap);
    }
}
