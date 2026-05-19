package com.example.projektkalkulationgruppe6.utility;

import com.example.projektkalkulationgruppe6.exception.NotLoggedInException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class SessionHelper {


    public Integer getEmployeeId(HttpSession session){
        return (Integer) session.getAttribute("employeeId");

    }

    public void isLoggedIn(HttpSession session){
        Integer employeeId = getEmployeeId(session);
        if(employeeId == null){
            throw new NotLoggedInException();
        }
    }


}
