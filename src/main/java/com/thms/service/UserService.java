package com.thms.service;

import com.thms.dto.UserDTO;
import com.thms.model.User;
import com.thms.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public UserDTO registerUser(UserDTO userDTO) {
        // Check if username or email already exists
        String username = (String) readField(userDTO, "username");
        String email = (String) readField(userDTO, "email");
        String password = (String) readField(userDTO, "password");
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username is already taken!");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already in use!");
        }

        // Create new user
        User user = new User();
        writeField(user, "username", username);
        writeField(user, "email", email);
        writeField(user, "password", passwordEncoder.encode(password));
        writeField(user, "firstName", readField(userDTO, "firstName"));
        writeField(user, "lastName", readField(userDTO, "lastName"));
        writeField(user, "phoneNumber", readField(userDTO, "phoneNumber"));
        // Default role for new registrations
        try {
            Object roleEnum = getUserRoleEnum(user.getClass(), "ROLE_USER");
            writeField(user, "role", roleEnum);
        } catch (Exception e) {
            // ignore if role enum is not available
        }

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        // Check if username or email already exists
        String username = (String) readField(userDTO, "username");
        String email = (String) readField(userDTO, "email");
        String password = (String) readField(userDTO, "password");
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username is already taken!");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already in use!");
        }

        // Create new user (similar to registerUser but allows setting custom roles)
        User user = new User();
        writeField(user, "username", username);
        writeField(user, "email", email);
        writeField(user, "password", passwordEncoder.encode(password));
        writeField(user, "firstName", readField(userDTO, "firstName"));
        writeField(user, "lastName", readField(userDTO, "lastName"));
        writeField(user, "phoneNumber", readField(userDTO, "phoneNumber"));

        // Use the role from userDTO or default to ROLE_USER
        Object dtoRole = readField(userDTO, "role");
        if (dtoRole != null) {
            writeField(user, "role", dtoRole);
        } else {
            try {
                Object roleEnum = getUserRoleEnum(user.getClass(), "ROLE_USER");
                writeField(user, "role", roleEnum);
            } catch (Exception e) {
                // ignore
            }
        }

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(this::convertToDTO);
    }

    @Transactional
    public Optional<UserDTO> updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(id).map(user -> {
            // Update username if provided and different
            String newUsername = (String) readField(userDTO, "username");
            String existingUsername = (String) readField(user, "username");
            if (newUsername != null && !newUsername.trim().isEmpty() &&
                    (existingUsername == null || !existingUsername.equals(newUsername))) {

                // Check if new username is already taken
                if (userRepository.existsByUsername(newUsername)) {
                    throw new RuntimeException("Username is already taken!");
                }
                writeField(user, "username", newUsername);
            }

            // Update email if provided and different
            String newEmail = (String) readField(userDTO, "email");
            String existingEmail = (String) readField(user, "email");
            if (newEmail != null && !newEmail.trim().isEmpty() &&
                    (existingEmail == null || !existingEmail.equals(newEmail))) {

                // Check if new email is already taken
                if (userRepository.existsByEmail(newEmail)) {
                    throw new RuntimeException("Email is already in use!");
                }
                writeField(user, "email", newEmail);
            }

            // Update other fields
            String newFirst = (String) readField(userDTO, "firstName");
            if (newFirst != null && !newFirst.trim().isEmpty()) {
                writeField(user, "firstName", newFirst);
            }

            String newLast = (String) readField(userDTO, "lastName");
            if (newLast != null && !newLast.trim().isEmpty()) {
                writeField(user, "lastName", newLast);
            }

            Object newPhone = readField(userDTO, "phoneNumber");
            if (newPhone != null) {
                writeField(user, "phoneNumber", newPhone);
            }

            // Update role if provided
            Object newRole = readField(userDTO, "role");
            if (newRole != null) {
                writeField(user, "role", newRole);
            }

            // Only update password if provided
            String newPassword = (String) readField(userDTO, "password");
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                writeField(user, "password", passwordEncoder.encode(newPassword));
            }

            return convertToDTO(userRepository.save(user));
        });
    }

    @Transactional
    public Optional<UserDTO> updateUserRole(Long id, User.Role role) {
        return userRepository.findById(id).map(user -> {
            writeField(user, "role", role);
            return convertToDTO(userRepository.save(user));
        });
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        writeField(dto, "id", readField(user, "id"));
        writeField(dto, "username", readField(user, "username"));
        writeField(dto, "email", readField(user, "email"));
        writeField(dto, "firstName", readField(user, "firstName"));
        writeField(dto, "lastName", readField(user, "lastName"));
        writeField(dto, "phoneNumber", readField(user, "phoneNumber"));
        writeField(dto, "role", readField(user, "role"));
        // Don't set password in DTO for security reasons
        return dto;
    }

    // Reflection helpers to avoid relying on Lombok-generated accessors
    private Object readField(Object obj, String fieldName) {
        if (obj == null || fieldName == null) return null;
        Class<?> cls = obj.getClass();
        while (cls != null) {
            try {
                java.lang.reflect.Field f = cls.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f.get(obj);
            } catch (NoSuchFieldException e) {
                cls = cls.getSuperclass();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        // try getter
        try {
            java.lang.reflect.Method m = obj.getClass().getMethod("get" + capitalize(fieldName));
            return m.invoke(obj);
        } catch (Exception ignored) {
        }
        return null;
    }

    private void writeField(Object obj, String fieldName, Object value) {
        if (obj == null || fieldName == null) return;
        Class<?> cls = obj.getClass();
        while (cls != null) {
            try {
                java.lang.reflect.Field f = cls.getDeclaredField(fieldName);
                f.setAccessible(true);
                f.set(obj, value);
                return;
            } catch (NoSuchFieldException e) {
                cls = cls.getSuperclass();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        // try setter
        String setterName = "set" + capitalize(fieldName);
        try {
            // try to find matching setter by name and single parameter
            for (java.lang.reflect.Method m : obj.getClass().getMethods()) {
                if (m.getName().equals(setterName) && m.getParameterCount() == 1) {
                    m.invoke(obj, value);
                    return;
                }
            }
        } catch (Exception ignored) {
        }
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private Object getUserRoleEnum(Class<?> userClass, String enumName) {
        // Try to find nested Role enum inside User class and return the enum constant if found
        for (Class<?> nested : userClass.getDeclaredClasses()) {
            if (nested.getSimpleName().equals("Role") && nested.isEnum()) {
                for (Object constant : nested.getEnumConstants()) {
                    if (constant != null && constant.toString().equals(enumName)) {
                        return constant;
                    }
                }
            }
        }
        return null;
    }
}