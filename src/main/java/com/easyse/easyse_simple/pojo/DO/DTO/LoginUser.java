package com.easyse.easyse_simple.pojo.DO.DTO;

import com.alibaba.fastjson.annotation.JSONField;
import com.easyse.easyse_simple.pojo.DO.task.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * warn：不要继承UserDetails
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails {

    // 用来接收用户信息
    private User user;

    private List<String> permissions;

    @JSONField(serialize = false)
    // 名称必须使用authorities， 因为无论什么名字在redis序列化时，都是序列化的这个名称（因为继承了UserDetails）
    private List<SimpleGrantedAuthority> authorities;

    public LoginUser(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }


    /**
     * 此方法用于获取该用户的权限
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //把permissions中的权限信息封装成 SimpleGrantedAuthority 对象
        /*
        List<GrantedAuthority> authorityList = new ArrayList<>();
        for (String permission : permissions) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(permission);
            authorityList.add(simpleGrantedAuthority);
        }
        */
        // 函数式编写
        if(authorities!=null){
            return authorities;
        }
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        for (String permission : permissions) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(permission);
            list.add(simpleGrantedAuthority);
        }
        authorities = list;
        return authorities;
    }

    @Override
    public String getPassword() {

        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
