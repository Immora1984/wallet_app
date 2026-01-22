package ru.wallettz;

import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.wallettz.controllers.WalletController;
import ru.wallettz.entity.Wallet;
import ru.wallettz.repository.AuthRepository;
import ru.wallettz.repository.UserRepository;
import ru.wallettz.repository.WalletRepository;
import ru.wallettz.service.impl.WalletServiceImpl;

@Slf4j
@WebMvcTest(
        controllers = {WalletController.class}
)
@Import({WalletServiceImpl.class})
public class WalletControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    UserRepository userRepository;
    @MockitoBean
    WalletRepository walletRepository;
    @MockitoBean
    AuthRepository authRepository;

    @Test
    @WithMockUser(
            username = "ff07701e-0cf8-4cc1-8869-9e33b64eba10",
            authorities = {"USER"}
    )
    void successfulGetBalance() throws Exception {
        Mockito.when(this.walletRepository.findById((UUID)Mockito.any(UUID.class))).thenReturn(Optional.of(WalletUtils.generateWallet()));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/wallets/{walletId}", new Object[]{UUID.randomUUID().toString()}).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(
            username = "ff07701e-0cf8-4cc1-8869-9e33b64eba10",
            authorities = {"USER"}
    )
    void unsuccessfulGetBalance() throws Exception {
        Mockito.when(this.walletRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/wallets/{walletId}",
                new Object[]{UUID.randomUUID().toString()})
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @WithMockUser(
            username = "ff07701e-0cf8-4cc1-8869-9e33b64eba10",
            authorities = {"USER"}
    )
    void successfulAction() throws Exception {
        Mockito.when(this.walletRepository.findById((UUID)Mockito.any(UUID.class))).thenReturn(Optional.of(WalletUtils.generateWallet()));
        Mockito.when(walletRepository.save(Mockito.any(Wallet.class))).thenReturn(WalletUtils.generateWallet());
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallet", new Object[0]).with(SecurityMockMvcRequestPostProcessors.csrf()).content(WalletUtils.getActionRequestBody()).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(
            username = "ff07701e-0cf8-4cc1-8869-9e33b64eba10",
            authorities = {"USER"}
    )
    void unsuccessfulAction() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallet", new Object[0]).with(SecurityMockMvcRequestPostProcessors.csrf()).content(WalletUtils.getActionRequestBody()).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}
