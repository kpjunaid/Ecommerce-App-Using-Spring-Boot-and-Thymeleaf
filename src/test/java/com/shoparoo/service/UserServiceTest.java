package com.shoparoo.service;

import com.shoparoo.dto.AdminUserUpdateDto;
import com.shoparoo.dto.UserDto;
import com.shoparoo.dto.UserUpdateDto;
import com.shoparoo.entity.Address;
import com.shoparoo.entity.EmailVerifyToken;
import com.shoparoo.entity.ResetPasswordToken;
import com.shoparoo.entity.User;
import com.shoparoo.mapper.MapStructMapper;
import com.shoparoo.mapper.MapStructMapperUpdate;
import com.shoparoo.repository.UserRepository;
import com.shoparoo.util.PasswordUtil;
import com.shoparoo.util.TokenGeneratorUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.env.Environment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DataJpaTest
class UserServiceTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleService roleService;

    @Mock
    AddressService addressService;

    @Mock
    EmailService emailService;

    @Mock
    EmailVerifyTokenService emailVerifyTokenService;

    @Mock
    ResetPasswordTokenService resetPasswordTokenService;

    @Mock
    Environment environment;

    @Mock
    PasswordUtil passwordUtil;

    @Spy
    MapStructMapper mapStructMapper = Mappers.getMapper(MapStructMapper.class);

    @Spy
    MapStructMapperUpdate mapStructMapperUpdate = Mappers.getMapper(MapStructMapperUpdate.class);

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldReturnListOfAllUsers() {
        User user1 = new User(
                1L,
                "johndoe@dom.com",
                "@P4ssword",
                true,
                false,
                false
        );
        User user2 = new User(
                1L,
                "johndoe@dom.com",
                "@P4ssword",
                true,
                false,
                false
        );

        List<User> actualUserList = List.of(user1, user2);
        when(userRepository.findAll()).thenReturn(actualUserList);

        List<User> returnedList = userService.getAllUsers();

        assertThat(returnedList).isNotNull();
        assertThat(returnedList.size()).isGreaterThan(0);
        assertThat(returnedList.size()).isEqualTo(2);
    }

    @Test
    void shouldReturnUser_whenIdIsGiven() {
        Long id = 1L;
        User actualUser = new User(
                1L,
                "johndoe@dom.com",
                "@P4ssword",
                true,
                false,
                false
        );

        when(userRepository.findById(id)).thenReturn(Optional.of(actualUser));

        User returnedUser = userService.getUserById(id);

        assertThat(returnedUser).isNotNull();
        assertThat(returnedUser).isEqualTo(actualUser);
    }

    @Test
    void shouldReturnUser_whenEmailIsGiven() {
        String email = "johndoe@dom.com";
        User actualUser = new User(
                1L,
                "johndoe@dom.com",
                "@P4ssword",
                true,
                false,
                false
        );

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(actualUser));

        User returnedUser = userService.getUserByEmail(email);

        assertThat(returnedUser).isNotNull();
        assertThat(returnedUser).isEqualTo(actualUser);
    }

    @Test
    void shouldSaveNewUser_whenValidInputsAreGiven() {
        UserDto userDto = new UserDto(
                "johndoe@dom.com",
                "@P4ssword",
                "@P4ssword",
                true,
                false,
                false
        );
        User user = mapStructMapper.userDtoToUser(userDto);

        when(userRepository.save(any(User.class))).thenReturn(user);
        User savedUser = userService.saveNewUser(userDto);

        verify(userRepository).save(any(User.class));
        assertThat(savedUser).isNotNull();
    }

    @Test
    void shouldUpdateUserInfo_whenValidInputsAreGiven() {
        String email = "johndoe@dom.com";
        User userToUpdate = new User(
                1L,
                "johndoe@dom.com",
                "@P4ssword",
                true,
                false,
                false
        );
        userToUpdate.setAddress(new Address(1L));

        UserUpdateDto userUpdateDto = new UserUpdateDto();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userToUpdate));
        userService.updateUserInfo(email, userUpdateDto, null);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldUpdateUserEmail_whenValidInputsAreGiven() {
        String email = "johndoe@dom.com";
        User userToUpdate = new User(
                1L,
                "johndoe@dom.com",
                "@P4ssword",
                true,
                false,
                false
        );

        UserDto userDto = new UserDto();
        userDto.setEmail("johndoe_2@dom.com");
        userDto.setPassword("@P4ssword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userToUpdate));
        when(passwordUtil.matchPassword(anyString(), anyString())).thenReturn(Boolean.TRUE);
        userService.updateUserEmail(email, userDto);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldUpdateUserPassword_whenValidInputsAreGiven() {
        String email = "johndoe@dom.com";
        String oldPassword = "@P4ssword";
        User userToUpdate = new User(
                1L,
                "johndoe@dom.com",
                "@P4ssword",
                true,
                false,
                false
        );

        String newPassword = "@_P4ssword";
        UserDto userDto = new UserDto();
        userDto.setPassword(newPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userToUpdate));
        when(passwordUtil.matchPassword(anyString(), anyString())).thenReturn(Boolean.TRUE, Boolean.FALSE);
        when(passwordUtil.encodePassword(anyString())).thenReturn(newPassword);
        userService.updateUserPassword(email, userDto, oldPassword);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldDeleteUser_whenValidIdIsGiven() {
        Long id = 1L;
        User userToDelete = new User(
                1L,
                "johndoe@dom.com",
                "@P4ssword",
                true,
                false,
                false
        );

        when(userRepository.findById(id)).thenReturn(Optional.of(userToDelete));

        userService.deleteUserById(id);

        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void shouldSendVerifyEmailToken_whenSignupSuccessful() {
        Long id = 1L;
        User user = new User(
                1L,
                "johndoe@dom.com",
                "@P4ssword",
                true,
                false,
                false
        );

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(emailService.buildAccountConfirmationMail(anyString())).thenReturn(anyString());

        userService.sendVerifyEmailToken(id);

        verify(emailService, times(1)).send(anyString(), anyString(), anyString());
    }

    @Test
    void shouldVerifyUserEmail_whenVerifyEmailTokenIsValid() {
        User user = new User(
                1L,
                "johndoe@dom.com",
                "@P4ssword",
                true,
                false,
                false
        );

        String token = "sample_token";
        EmailVerifyToken emailVerifyToken = new EmailVerifyToken(
                1L,
                token,
                LocalDateTime.now().plusHours(1)
        );
        emailVerifyToken.setUser(user);

        when(emailVerifyTokenService.getByToken(anyString())).thenReturn(emailVerifyToken);
        userService.verifyUserEmail(anyString());

        verify(userRepository).save(any(User.class));
        verify(emailVerifyTokenService, times(1)).deleteAllByUser(any(User.class));
    }

    @Test
    void shouldSendResetPasswordMail() {
        String email = "johndoe@dom.com";
        User user = new User(
                1L,
                "johndoe@dom.com",
                "@P4ssword",
                true,
                false,
                false
        );

        String token = "sample_token";
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(
                1L,
                token,
                LocalDateTime.now().plusHours(1)
        );
        resetPasswordToken.setUser(user);

        try (MockedStatic<TokenGeneratorUtil> mockedStatic = Mockito.mockStatic(TokenGeneratorUtil.class)) {
            mockedStatic.when(TokenGeneratorUtil::generateToken).thenReturn(token);
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
            when(emailService.buildResetPasswordMail(anyString())).thenReturn(anyString());

            userService.sendResetPasswordMail(email);

            verify(emailService, times(1)).send(anyString(), anyString(), anyString());
        }
    }

    @Test
    void shouldResetPassword_whenValidInputsAreGiven() {
        User user = new User(
                1L,
                "johndoe@dom.com",
                "@P4ssword",
                true,
                false,
                false
        );

        String token = "sample_token";
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(
                1L,
                token,
                LocalDateTime.now().plusHours(1)
        );
        resetPasswordToken.setUser(user);

        UserDto userDto = new UserDto();
        userDto.setPassword("@P4ssword");

        when(resetPasswordTokenService.getByToken(anyString())).thenReturn(resetPasswordToken);

        userService.resetUserPassword(anyString(), userDto);

        verify(userRepository).save(any(User.class));
        verify(resetPasswordTokenService, times(1)).deleteAllByUser(any(User.class));
    }

    @Test
    void shouldAdminUpdateUser_whenValidInputsAreGiven() {
        Long id = 1L;
        User userToUpdate = new User(
                1L,
                "johndoe@dom.com",
                "@P4ssword",
                true,
                false,
                false
        );
        userToUpdate.setAddress(new Address(1L));

        AdminUserUpdateDto adminUserUpdateDto = new AdminUserUpdateDto();

        when(userRepository.findById(id)).thenReturn(Optional.of(userToUpdate));
        userService.adminUpdateUser(id, adminUserUpdateDto, null);

        verify(userRepository).save(any(User.class));
    }
}