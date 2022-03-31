package net.skhu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import net.skhu.entity.User;
import net.skhu.model.Pagination;
import net.skhu.model.UserSignUp;
import net.skhu.repository.UserRepository;

@Service
public class UserService {

    @Autowired UserRepository userRepository;
    @Autowired PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean hasErrors(UserSignUp userSignUp, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return true;
        if (userSignUp.getPasswd1().equals(userSignUp.getPasswd2()) == false) {
            bindingResult.rejectValue("passwd2", null, "비밀번호가 일치하지 않습니다.");
            return true;
        }
        User user = userRepository.findByLoginName(userSignUp.getLoginName());
        if (user != null) {
            bindingResult.rejectValue("loginName", null, "사용자 아이디가 중복됩니다.");
            return true;
        }
        return false;
    }

    public User createEntity(UserSignUp userSignUp) {
        User user = new User();
        user.setLoginName(userSignUp.getLoginName());
        user.setPassword(passwordEncoder.encode(userSignUp.getPasswd1()));
        user.setName(userSignUp.getName());
        user.setEmail(userSignUp.getEmail());
        user.setEnabled(true);
        return user;
    }

    public void save(UserSignUp userSignUp) {
        User user = createEntity(userSignUp);
        userRepository.save(user);
    }

    private static Sort[] orderBy = new Sort[] {
        Sort.by(Sort.Direction.DESC, "id"),
        Sort.by(Sort.Direction.DESC, "id"),
        Sort.by(Sort.Direction.ASC, "loginName"),
        Sort.by(Sort.Direction.ASC, "name")
    };

    public List<User> findAll(Pagination pagination) {
        int pg = pagination.getPg() - 1, sz = pagination.getSz(),
            si = pagination.getSi(), od = pagination.getOd();
        String st = pagination.getSt();
        Page<User> page = null;
        if (si == 1)
            page = userRepository.findByLoginNameStartsWith(st, PageRequest.of(pg, sz, orderBy[od]));
        else if (si == 2)
            page = userRepository.findByNameStartsWith(st, PageRequest.of(pg, sz, orderBy[od]));
        else
            page = userRepository.findAll(PageRequest.of(pg, sz, orderBy[od]));
        pagination.setRecordCount((int)page.getTotalElements());
        return page.getContent();
    }

}
