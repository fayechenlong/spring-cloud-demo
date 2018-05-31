package com.beeplay;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenlongfei
 */
@Component
public class MyFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(MyFilter.class);
    private static final String PARAM_TOKEN = "token";
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String token=request.getParameter("token");
//        Map<String,List<String>> map=new HashMap<String,List<String>>();
//        List<String> list=new ArrayList<String>();
//        list.add("chenlongfei");
//        map.put("username",list);
//        context.setRequestQueryParams(map);
        if(StringUtils.isEmpty(token)){
                context.setResponseBody("token is empty!");
                context.setSendZuulResponse(false);

        }
        return null;
    }
}