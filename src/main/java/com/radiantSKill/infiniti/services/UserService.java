package com.radiantSKill.infiniti.services;

import com.radiantSKill.infiniti.dao.UserDAO;
import com.radiantSKill.infiniti.dto.RegisterRequestDTO;
import com.radiantSKill.infiniti.dto.UserResponseDTO;
import com.radiantSKill.infiniti.entity.AppUser;
import com.radiantSKill.infiniti.entity.Role;
import com.radiantSKill.infiniti.entity.StudentSubmissionStore;
import com.radiantSKill.infiniti.repository.AppUserRepository;
import com.radiantSKill.infiniti.repository.RoleRepository;
import com.radiantSKill.infiniti.repository.StudentSubmissionStoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final StudentSubmissionStoreRepository studentSubmissionRepository;
    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserDAO userDAO;

    @Transactional
    public void registerUser(RegisterRequestDTO request) {

        Role role = roleRepository.findByName(request.getRole().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        AppUser user = new AppUser();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus("ACTIVE");
        user.setRoles(Set.of(role));

        AppUser savedUser = userRepository.save(user);

        // AUTO CREATE STUDENT SUBMISSION
        if ("STUDENT".equalsIgnoreCase(role.getName())) {
            StudentSubmissionStore studentSubmissionStore = new StudentSubmissionStore(); // ✅ create instance
            studentSubmissionStore.setStudent(savedUser);
            studentSubmissionStore.setSubmissionStatus("DRAFT");
            studentSubmissionRepository.save(studentSubmissionStore);
        }
    }

    public UserResponseDTO getUserByEmail(String email) {

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String role = userDAO.findRoleByUserId(user.getId());

        UserResponseDTO dto = new UserResponseDTO();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setStatus(user.getStatus());
        dto.setRole(role);

        return dto;
    }
}


