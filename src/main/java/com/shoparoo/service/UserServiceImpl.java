package com.shoparoo.service;

import com.shoparoo.dto.AddressDto;
import com.shoparoo.dto.AdminUserUpdateDto;
import com.shoparoo.dto.UserDto;
import com.shoparoo.dto.UserUpdateDto;
import com.shoparoo.entity.*;
import com.shoparoo.exception.*;
import com.shoparoo.mapper.MapStructMapper;
import com.shoparoo.mapper.MapStructMapperUpdate;
import com.shoparoo.repository.UserRepository;
import com.shoparoo.specification.UserSpecification;
import com.shoparoo.util.FileNamingUtil;
import com.shoparoo.util.FileUploadUtil;
import com.shoparoo.util.PasswordUtil;
import com.shoparoo.util.TokenGeneratorUtil;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class UserServiceImpl implements UserService {
    private final MapStructMapper mapStructMapper;
    private final MapStructMapperUpdate mapStructMapperUpdate;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final AddressService addressService;
    private final EmailVerifyTokenService emailVerifyTokenService;
    private final ResetPasswordTokenService resetPasswordTokenService;
    private final EmailService emailService;
    private final PasswordUtil passwordUtil;
    private final Environment environment;

    public UserServiceImpl(MapStructMapper mapStructMapper,
                           MapStructMapperUpdate mapStructMapperUpdate,
                           UserRepository userRepository,
                           RoleService roleService,
                           AddressService addressService,
                           EmailVerifyTokenService emailVerifyTokenService,
                           ResetPasswordTokenService resetPasswordTokenService,
                           EmailService emailService,
                           PasswordUtil passwordUtil,
                           Environment environment) {
        this.mapStructMapper = mapStructMapper;
        this.mapStructMapperUpdate = mapStructMapperUpdate;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.addressService = addressService;
        this.emailVerifyTokenService = emailVerifyTokenService;
        this.resetPasswordTokenService = resetPasswordTokenService;
        this.emailService = emailService;
        this.passwordUtil = passwordUtil;
        this.environment = environment;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> getAllUsersPageable(Integer pageNumber,
                                      Integer usersPerPage,
                                      String sort,
                                      String dir,
                                      String gender,
                                      String role,
                                      Optional<Boolean> enabled,
                                      String search,
                                      String key) {
        if (pageNumber <= 0) {
            return Page.empty();
        }

        Sort.Direction direction = dir.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber - 1, usersPerPage, Sort.by(direction, sort));

        if (search.equals("id")) {
            try {
                int id = Integer.parseInt(key);
                return userRepository.findAll(UserSpecification.idIs(id), pageable);
            } catch (Exception e) {
                // sKey(id) is not an integer, return empty page
                return Page.empty();
            }
        } else {
            Specification<User> searchSpec = UserSpecification.noFilter();

            switch (search) {
                case "email":
                    searchSpec = UserSpecification.emailLike(key);
                    break;
                case "firstName":
                    searchSpec = UserSpecification.firstNameLike(key);
                    break;
                case "lastName":
                    searchSpec = UserSpecification.lastNameLike(key);
                    break;
                default:
                    break;
            }

            return userRepository.findAll(
                    where(searchSpec
                            .and(UserSpecification.genderIs(gender)))
                            .and(UserSpecification.hasRole(role))
                            .and(UserSpecification.isEnabled(enabled)),
                    pageable);
        }
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    @Override
    @Transactional
    public User saveNewUser(UserDto userDto) {
        try {
            User user = getUserByEmail(userDto.getEmail());
            if (user != null) {
                throw new EmailExistsException();
            }
        } catch (UserNotFoundException e) {
            Role roleCustomer = roleService.getRoleByName("CUSTOMER");
            Set<Role> roles = new HashSet<>();
            roles.add(roleCustomer);

            Address savedAddress = addressService.saveNewAddress(new AddressDto());

            userDto.setEnabled(true);
            userDto.setAccountVerified(false);
            userDto.setEmailVerified(false);
            userDto.setRoles(roles);
            userDto.setAddress(savedAddress);

            User userToSave = mapStructMapper.userDtoToUser(userDto);
            userToSave.setPassword(passwordUtil.encodePassword(userToSave.getPassword()));
            User savedUser = userRepository.save(userToSave);
            buildAndSendNewEmailVerifyToken(savedUser);
            return savedUser;
        }

        return null;
    }

    private void buildAndSendNewEmailVerifyToken(User user) {
        try {
            String token = TokenGeneratorUtil.generateToken();
            EmailVerifyToken emailVerifyToken = new EmailVerifyToken();
            emailVerifyToken.setToken(token);
            emailVerifyToken.setUser(user);
            emailVerifyToken.setExpireAt(LocalDateTime.now().plusHours(1));
            emailVerifyToken.setCreatedAt(LocalDateTime.now());
            emailVerifyTokenService.saveToken(emailVerifyToken);

            String mail = emailService.buildAccountConfirmationMail(emailVerifyToken.getToken());
            emailService.send(user.getEmail(), "Confirm Your Email", mail);
        } catch (NoSuchAlgorithmException ignored) {
            throw new RuntimeException();
        }
    }

    @Override
    @Transactional
    public void sendVerifyEmailToken(Long id) {
        User user = getUserById(id);
        buildAndSendNewEmailVerifyToken(user);
    }

    @Override
    @Transactional
    public void verifyUserEmail(String token) {
        EmailVerifyToken emailVerifyToken = emailVerifyTokenService.getByToken(token);
        if (emailVerifyToken != null) {
            boolean tokenExpired = LocalDateTime.now().isAfter(emailVerifyToken.getExpireAt());
            if (tokenExpired) {
                throw new TokenExpiredException();
            } else {
                User user = emailVerifyToken.getUser();
                user.setEmailVerified(true);
                user.setAccountVerified(true);
                userRepository.save(user);

                emailVerifyTokenService.deleteAllByUser(user);
            }
        } else {
          throw new TokenNotFoundException();
        }
    }

    @Override
    public void sendResetPasswordMail(String email) {
        try {
            User user = getUserByEmail(email);

            String token = TokenGeneratorUtil.generateToken();
            ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
            resetPasswordToken.setToken(token);
            resetPasswordToken.setUser(user);
            resetPasswordToken.setExpireAt(LocalDateTime.now().plusHours(1));
            resetPasswordToken.setCreatedAt(LocalDateTime.now());
            resetPasswordTokenService.saveToken(resetPasswordToken);

            String mail = emailService.buildResetPasswordMail(resetPasswordToken.getToken());
            emailService.send(user.getEmail(), "Reset Password", mail);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (NoSuchAlgorithmException ignored) {}
    }

    @Override
    public void checkResetPasswordTokenValidity(String token) {
        try {
            ResetPasswordToken resetPasswordToken = resetPasswordTokenService.getByToken(token);
            boolean tokenExpired = LocalDateTime.now().isAfter(resetPasswordToken.getExpireAt());
            if (tokenExpired) {
                throw new TokenExpiredException();
            }
        } catch (TokenNotFoundException e) {
            throw new TokenNotFoundException();
        }
    }

    @Override
    @Transactional
    public void resetUserPassword(String token, UserDto userDto) {
        try {
            ResetPasswordToken resetPasswordToken = resetPasswordTokenService.getByToken(token);
            boolean tokenExpired = LocalDateTime.now().isAfter(resetPasswordToken.getExpireAt());
            if (tokenExpired) {
                throw new TokenExpiredException();
            } else {
                User user = resetPasswordToken.getUser();
                user.setPassword(passwordUtil.encodePassword(userDto.getPassword()));
                userRepository.save(user);

                resetPasswordTokenService.deleteAllByUser(user);
            }
        } catch (TokenNotFoundException e) {
            throw new TokenNotFoundException();
        }
    }

    @Override
    public User addAuthUserToModel(Authentication authentication, Model model) {
        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            try {
                User authUser = getUserByEmail(userDetails.getUsername());
                UserDto authUserDto = mapStructMapper.userToUserDto(authUser);

                model.addAttribute("authUser", authUserDto);

                return authUser;
            } catch (UserNotFoundException e) {
                throw new UserNotFoundException();
            }
        } else {
            model.addAttribute("authUser", null);
        }

        return null;
    }

    @Override
    @Transactional
    public void updateUserInfo(String email, UserUpdateDto userUpdateDto, MultipartFile userPhoto) {
        User user = getUserByEmail(email);

        if (userPhoto != null) {
            String dpUploadDir = environment.getProperty("photo.upload.user");
            userUpdateDto.setPhotoUrl(FileNamingUtil.nameFile(userPhoto));
            try {
                if (user.getPhotoUrl() == null) {
                    FileUploadUtil.saveNewFile(dpUploadDir, userUpdateDto.getPhotoUrl(), userPhoto);
                } else {
                    FileUploadUtil.updateFile(dpUploadDir, user.getPhotoUrl(),
                            userUpdateDto.getPhotoUrl(), userPhoto);
                }
            } catch (IOException ignored) {
                throw new RuntimeException();
            }
        } else {
            if (user.getPhotoUrl() != null) {
                userUpdateDto.setPhotoUrl(user.getPhotoUrl());
            } else {
                userUpdateDto.setPhotoUrl(null);
            }
        }

        addressService.updateAddressInfo(user.getAddress().getId(), userUpdateDto);
        UserDto userDto = mapStructMapper.userUpdateDtoToUserDto(userUpdateDto);
        mapStructMapperUpdate.updateUserFromUserDto(userDto, user);

        userRepository.save(user);
    }

    @Override
    public void updateUserEmail(String email, UserDto userDto) {
        User user = getUserByEmail(email);

        if (passwordUtil.matchPassword(userDto.getPassword(), user.getPassword())) {
            if (user.getEmail().equals(userDto.getEmail())) {
                throw new UpdateWithCurrentEmailException();
            } else {
                Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
                if (optionalUser.isPresent()) {
                    throw new EmailExistsException();
                } else {
                    user.setEmail(userDto.getEmail());
                    user.setEmailVerified(false);
                    userRepository.save(user);
                }
            }
        } else {
            throw new WrongPasswordException();
        }
    }

    @Override
    public void updateUserPassword(String email, UserDto userDto, String oldPassword) {
        User user = getUserByEmail(email);

        if (passwordUtil.matchPassword(oldPassword, user.getPassword())) {
            if (passwordUtil.matchPassword(userDto.getPassword(), user.getPassword())) {
                throw new UpdateWithCurrentPasswordException();
            } else {
                user.setPassword(passwordUtil.encodePassword(userDto.getPassword()));
                userRepository.save(user);
            }
        } else {
            throw new WrongPasswordException();
        }
    }

    @Override
    public void deleteUserById(Long id) {
        try {
            User user = getUserById(id);
            if (user != null) {
                String photo = user.getPhotoUrl();
                // delete user dp from filesystem if user uploaded dp previously
                if (photo != null && photo.length() > 0) {
                    String dpUploadDir = environment.getProperty("photo.upload.user");
                    try {
                        FileUploadUtil.deleteFile(dpUploadDir, photo);
                    } catch (IOException e) {
                        throw new RuntimeException();}
                }

                userRepository.deleteById(id);
            }
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }
    }

    @Override
    @Transactional
    public void adminUpdateUser(Long id, AdminUserUpdateDto adminUserUpdateDto, MultipartFile userPhoto) {
        try {
            User user = getUserById(id);

            if (userPhoto != null) {
                String dpUploadDir = environment.getProperty("photo.upload.user");
                adminUserUpdateDto.setPhotoUrl(FileNamingUtil.nameFile(userPhoto));
                try {
                    if (user.getPhotoUrl() == null) {
                        FileUploadUtil.saveNewFile(dpUploadDir, adminUserUpdateDto.getPhotoUrl(), userPhoto);
                    } else {
                        FileUploadUtil.updateFile(dpUploadDir, user.getPhotoUrl(), adminUserUpdateDto.getPhotoUrl(), userPhoto);
                    }
                } catch (IOException ignored) {
                }
            } else {
                adminUserUpdateDto.setPhotoUrl(user.getPhotoUrl());
            }

            addressService.updateAddressInfo(user.getAddress().getId(), adminUserUpdateDto);
            UserDto userDto = mapStructMapper.adminUserUpdateDtoToUserDto(adminUserUpdateDto);
            mapStructMapperUpdate.adminUpdateUserFromUserDto(userDto, user);

            userRepository.save(user);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }
    }
}
