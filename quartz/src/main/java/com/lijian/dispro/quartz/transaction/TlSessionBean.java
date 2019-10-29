package com.lijian.dispro.quartz.transaction;

import com.mysql.cj.Session;
import com.mysql.cj.xdevapi.SessionFactory;
import lombok.Data;
import org.springframework.context.annotation.Description;

import java.beans.beancontext.BeanContext;
import java.io.Serializable;

@Data
public class TlSessionBean implements Serializable {

    private Boolean custom;
    private Session session;
}

