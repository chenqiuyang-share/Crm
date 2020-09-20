package com.share.crm.settings.web.controller;

import com.share.crm.settings.domain.User;
import com.share.crm.settings.service.UserService;
import com.share.crm.settings.service.impl.UserServiceImpl;
import com.share.crm.utils.MD5Util;
import com.share.crm.utils.PrintJson;
import com.share.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到用户控制器");
        /**
         * getServletPath():获取能够与“url-pattern”中匹配的路径，
         * 注意是完全匹配的部分，*的部分不包括。
         */
        String path = request.getServletPath();
        if("settings/user/login.do".equals(path)){
            login(request,response);
        }else if("settings/user/xxx.do".equals(path)){
            //xxx(request,response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response){
        System.out.println("进入到验证登录的操作");
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        //将密码明文形式转化为MD5的密文格式
        loginPwd = MD5Util.getMD5(loginPwd);
        System.out.println(loginPwd);
        //接受浏览器端的ip地址
        String remoteAddr = request.getRemoteAddr();
        System.out.println("ip地址=====" + remoteAddr);
        //未来业务层开发，统一使用代理类形态的接口对象,走事物,非常重要
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try{
            //返回一个User对象
            User user = userService.login(loginAct,loginPwd);
            request.getSession().setAttribute("user",user);
            //如果程序执行到此处，说明业务层没有为controller抛出任何异常
            //表示登录成功，为前台提供下面的信息
            /**
             * {"success":true}
             */
            PrintJson.printJsonFlag(response,true);



        }catch (Exception e){
            e.printStackTrace();
            //一旦程序执行到catch块的信息，说明业务层为我们验证登录失败，为controller抛出异常
            //表示登录失败，为前台提供下面的信息
            /**
             * {"success":false,"msg":?}
             */
            String msg = e.getMessage();//获取异常的信息
            /**
             * 现在我们作为controller层，需要为ajax提供多项信息
             * 可以有两种手段来处理:
             * (1)将多项信息打包成map,将map解析成json串
             * (2)创建一个vo
             *      private boolean success;
             *      private String msg;
             *    如果对于展现的信息将来还会大量的使用，我们创建一个vo类，使用方便
             *    如果对于展现的信息只有在这个需求中能够使用，我们使用map就行了
             *
             */

            Map<String,Object> map = new HashMap<String,Object>();
            map.put("success",false);
            map.put("msg",msg);
            //将map解析成json串，并传递给前台
            PrintJson.printJsonObj(response,map);


        }


    }
}
