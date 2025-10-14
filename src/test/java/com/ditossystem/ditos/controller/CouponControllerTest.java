//package com.ditossystem.ditos.coupon;
//
//import com.ditossystem.ditos.coupon.dto.CouponCreateRequest;
//import com.ditossystem.ditos.coupon.dto.CouponPrivateResponse;
//import com.ditossystem.ditos.coupon.model.DiscountType;
//import com.ditossystem.ditos.device.DeviceService;
//import com.ditossystem.ditos.security.SecurityUtils;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.OffsetDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(CouponController.class)
//class CouponControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private CouponService couponService;
//
//    @MockBean
//    private DeviceService deviceService;
//
//    @MockBean
//    private SecurityUtils securityUtils;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private CouponPrivateResponse testCoupon;
//
//    @BeforeEach
//    void setUp() {
//        objectMapper.registerModule(new JavaTimeModule());
//        testCoupon = new CouponPrivateResponse(
//                "1",
//                "CODE123",
//                "Descrição do cupom",
//                10.0,
//                DiscountType.PERCENTAGE,
//                50.0,
//                100.0,
//                100,
//                OffsetDateTime.parse("2024-01-01T00:00:00Z"),
//                500,
//                true,
//                OffsetDateTime.parse("2023-12-01T00:00:00Z"),
//                "admin@ditossystem.com",
//                150L,
//                List.of("LOJA123", "LOJA456")
//        );
//    }
//
//    @Test
//    void createCoupon_ShouldReturnCreated() throws Exception {
//        CouponCreateRequest request = new CouponCreateRequest(
//                "CODE123", "Desconto", 10,
//                DiscountType.FIXED, 100, 25, 15, OffsetDateTime.now(),
//                10, true, List.of("LOJA123", "LOJA456")
//        );
//
//        when(couponService.saveCoupon(any())).thenReturn(testCoupon);
//
//        mockMvc.perform(post("/coupons")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value("1"))
//                .andExpect(jsonPath("$.code").value("CODE123"));
//
//        verify(couponService, times(1)).saveCoupon(any());
//    }
//
//    @Test
//    void increaseCouponClicks_ShouldReturnOk() throws Exception {
//        doNothing().when(couponService).clickCoupon(anyString());
//
//        mockMvc.perform(post("/coupons/click/1"))
//                .andExpect(status().isOk());
//
//        verify(couponService, times(1)).clickCoupon("1");
//    }
//
//    @Test
//    void getAllCoupons_ShouldReturnCouponsList() throws Exception {
//        List<CouponPrivateResponse> coupons = Collections.singletonList(testCoupon);
//        when(couponService.getAllCoupons()).thenReturn(coupons);
//
//        mockMvc.perform(get("/coupons"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value("1"));
//    }
//
//    @Test
//    void getAllCouponsByStoreId_Authenticated_ShouldReturnAllCoupons() throws Exception {
//        when(securityUtils.isAuthenticated()).thenReturn(true);
//        List<CouponPrivateResponse> coupons = Collections.singletonList(testCoupon);
//
//        when(couponService.getAllCouponsByStoreId("LOJA123")).thenReturn(coupons);
//
//        mockMvc.perform(get("/coupons/store")
//                        .param("storeId", "LOJA123"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].storeId").value("LOJA123"));
//
//        verify(deviceService, never()).updateDate(any());
//    }
//
//    @Test
//    void getAllCouponsByStoreId_NotAuthenticated_ShouldReturnActiveCoupons() throws Exception {
//        when(securityUtils.isAuthenticated()).thenReturn(false);
//        List<CouponPrivateResponse> coupons = Collections.singletonList(testCoupon);
//
//        when(couponService.getActiveCoupons("LOJA123")).thenReturn(coupons);
//
//        mockMvc.perform(get("/coupons/store")
//                        .param("storeId", "LOJA123")
//                        .param("deviceId", "DEVICE456"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].active").value(true));
//
//        verify(deviceService, times(1)).updateDate("DEVICE456");
//    }
//
//    @Test
//    void getCouponsByCode_ExistingCode_ShouldReturnCoupons() throws Exception {
//        List<CouponPrivateResponse> coupons = Collections.singletonList(testCoupon);
//        when(couponService.getCouponByCode("CODE123")).thenReturn(coupons);
//
//        mockMvc.perform(get("/coupons/search")
//                        .param("code", "CODE123"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].code").value("CODE123"));
//    }
//
//    @Test
//    void getCouponsByCode_NonExistingCode_ShouldReturnNotFound() throws Exception {
//        when(couponService.getCouponByCode("INVALID")).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/coupons/search")
//                        .param("code", "INVALID"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void getCouponById_ExistingId_ShouldReturnCoupon() throws Exception {
//        when(couponService.getCouponById("1")).thenReturn(Optional.of(testCoupon));
//
//        mockMvc.perform(get("/coupons/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value("1"));
//    }
//
//    @Test
//    void getCouponById_NonExistingId_ShouldReturnNotFound() throws Exception {
//        when(couponService.getCouponById("999")).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/coupons/999"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void updateCoupon_ExistingId_ShouldReturnUpdatedCoupon() throws Exception {
//        CouponCreateRequest request = new CouponCreateRequest(
//                "CODE123", "Desconto Atualizado", "Nova Descrição",
//                15.0, 200, null, null, "LOJA123"
//        );
//
//        CouponPrivateResponse updated = new CouponPrivateResponse(
//                "1", "CODE123", "Desconto Atualizado", "Nova Descrição",
//                15.0, 200, 50, null, null, true, "LOJA123"
//        );
//
//        when(couponService.updateCoupon("1", request)).thenReturn(Optional.of(updated));
//
//        mockMvc.perform(put("/coupons/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("Desconto Atualizado"));
//    }
//
//    @Test
//    void deleteCoupon_ExistingId_ShouldReturnOk() throws Exception {
//        when(couponService.deleteCoupon("1")).thenReturn(true);
//
//        mockMvc.perform(delete("/coupons/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Cupom deletado com sucesso!"));
//    }
//
//    @Test
//    void deleteCoupon_NonExistingId_ShouldReturnNotFound() throws Exception {
//        when(couponService.deleteCoupon("999")).thenReturn(false);
//
//        mockMvc.perform(delete("/coupons/999"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Cupom não encontrado!"));
//    }
//}