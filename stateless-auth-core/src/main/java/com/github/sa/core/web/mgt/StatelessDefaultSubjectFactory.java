package com.github.sa.core.web.mgt;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * 禁用SubjectFactory中的Session
 * 
 * @author wh
 * @lastModified 2016-6-7 16:03:33
 */
public class StatelessDefaultSubjectFactory extends DefaultWebSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {
        context.setSessionCreationEnabled(false); // 设置false之后, 再调用Subject.getSession(), 将抛出DisabledSessionException
        return super.createSubject(context);
    }
}