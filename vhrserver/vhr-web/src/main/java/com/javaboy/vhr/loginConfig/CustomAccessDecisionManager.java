package com.javaboy.vhr.loginConfig;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection< ConfigAttribute > configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        Collection< ? extends GrantedAuthority > authorities = authentication.getAuthorities();
        for (ConfigAttribute configAttribute : configAttributes) {
            if("ROLE_LOGIN".equals(configAttribute.getAttribute())){
                if(authentication instanceof AnonymousAuthenticationToken){
                    throw new AccessDeniedException("离线状态，请您登录");
                }else {
                    return;
                }
            }

            for(GrantedAuthority grantedAuthority : authorities){
                if(configAttribute.getAttribute().equals(grantedAuthority.getAuthority())){
                    return;
                }
            }
        }

        throw new AccessDeniedException("权限不足，请联系管理员");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class< ? > clazz) {
        return true;
    }
}