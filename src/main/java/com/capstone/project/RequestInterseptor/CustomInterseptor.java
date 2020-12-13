package com.capstone.project.RequestInterseptor;
/**
 * @author Rohan Patel
 */
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomInterseptor extends HandlerInterceptorAdapter {
    private final String issuerId = "462948404D635166546A576E5A7234753778214125442A472D4B614E645267556B58703273357638792F423F4528482B4D6251655368566D597133743677397A";

    /**
     * <p>
     *     This method handles every incoming requests to the server and checks for the presence
     *     of of JWT token with a particular id. If there is a match, then the request continues otherwise
     *     it's blocked.
     * </p>
     * @param request
     * @param response
     * @param handler
     */
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {
        String token = request.getHeader("Authorization");
        System.out.println(token);
        try {
            System.out.println("Decoding JWT");
            DecodedJWT jwt = JWT.decode(token);
            System.out.println("Decode Done");
            if (jwt.getIssuer().equals(issuerId)) {
                System.out.println("Request granted");
                return true;
            } else {
                return false;
            }
        } catch (Exception exception) {
            //Invalid token
            System.out.println("Cannot decode secret key");
            return true;
        }
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) {

    }
}
