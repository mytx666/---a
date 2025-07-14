package com.data.agricultural_material_management_system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.data.agricultural_material_management_system.common.Result;
import com.data.agricultural_material_management_system.dto.LoginDTO;
import com.data.agricultural_material_management_system.dto.LoginResponseDTO;
import com.data.agricultural_material_management_system.dto.UserDTO;
import com.data.agricultural_material_management_system.entity.User;
import com.data.agricultural_material_management_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/page")
    public Result<IPage<User>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<User> page = userService.page(new Page<>(pageNum, pageSize));
        return Result.success(page);
    }

    @GetMapping
    public Result list() {
        List<UserDTO> userDTOs = userService.listWithRoles();
        return Result.success(userDTOs);
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody User user) {
        return Result.success(userService.save(user));
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody User user) {
        user.setUserId(id);
        return Result.success(userService.updateById(user));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(userService.removeById(id));
    }

    @PutMapping("/{userId}/roles")
    public Result updateUserRoles(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        userService.updateUserRoles(userId, roleIds);
        return Result.success();
    }

    // 登录
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO) {
        LoginResponseDTO loginResponse = userService.login(loginDTO);
        if (loginResponse == null) {
            return Result.error("用户名或密码错误");
        }
        return Result.success(loginResponse);
    }

    // 注册
    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody User user) {
        // 校验用户名唯一
        if (userService.lambdaQuery().eq(User::getUsername, user.getUsername()).count() > 0) {
            return Result.error("用户名已存在");
        }
        user.setStatus(1); // 默认启用

        boolean saved = userService.save(user);
        return saved ? Result.success(true) : Result.error("注册失败");
    }

    // 修改密码
    @PutMapping("/{userId}/password")
    public Result<Boolean> updatePassword(@PathVariable Long userId, @RequestBody String newPassword) {
        User user = userService.getById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return null;
    }

    // 临时方法：更新所有用户的密码为加密后的密码
    @PostMapping("/update-passwords")
    public Result<String> updateAllPasswords() {
        try {
            userService.updateAllPasswordsToEncrypted();
            return Result.success("所有用户密码已更新为加密格式");
        } catch (Exception e) {
            return Result.error("更新密码失败: " + e.getMessage());
        }
    }
}