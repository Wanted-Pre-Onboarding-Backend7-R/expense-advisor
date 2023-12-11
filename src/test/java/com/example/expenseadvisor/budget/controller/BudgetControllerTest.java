package com.example.expenseadvisor.budget.controller;

import com.example.expenseadvisor.budget.domain.Budget;
import com.example.expenseadvisor.budget.dto.common.BudgetByCategory;
import com.example.expenseadvisor.budget.dto.request.BudgetCreateRequest;
import com.example.expenseadvisor.budget.dto.request.BudgetPatchRequest;
import com.example.expenseadvisor.budget.repository.BudgetRepository;
import com.example.expenseadvisor.category.domain.Category;
import com.example.expenseadvisor.exception.domain.ErrorCode;
import com.example.expenseadvisor.member.domain.Member;
import com.example.expenseadvisor.member.dto.MemberCreateRequest;
import com.example.expenseadvisor.member.repository.MemberRepository;
import com.example.expenseadvisor.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class BudgetControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BudgetRepository budgetRepository;

    private final String testEmail = "abc@gmail.com";
    private final String testPassword = "12345";

    @BeforeEach
    void beforeEach() {
        MemberCreateRequest memberCreateRequest = MemberCreateRequest.builder()
                .email(testEmail)
                .password(testPassword)
                .build();
        memberService.createMember(memberCreateRequest);
    }

    @AfterEach
    void afterEach() {
        budgetRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("카테고리 별 예산을 등록할 수 있다.")
    @WithUserDetails(value = testEmail, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void createBudget() throws Exception {
        // given
        List<BudgetByCategory> budgetByCategories = List.of(
                new BudgetByCategory(Category.FOOD.name(), 10000),
                new BudgetByCategory(Category.HOUSING.name(), 20000),
                new BudgetByCategory(Category.HOBBY.name(), 30000)
        );
        BudgetCreateRequest request = new BudgetCreateRequest(budgetByCategories);

        // when
        mockMvc.perform(post("/api/budgets")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk());

        // then
        Member member = memberRepository.findMemberByEmail(testEmail).orElseThrow(() -> new Exception("테스트 멤버가 검색되어야 합니다."));
        List<Budget> all = budgetRepository.findAll();
        assertThat(all).hasSize(3).extracting("amount", "member.id", "category")
                .contains(
                        Tuple.tuple(10000, member.getId(), Category.FOOD),
                        Tuple.tuple(20000, member.getId(), Category.HOUSING),
                        Tuple.tuple(30000, member.getId(), Category.HOBBY)
                );
    }

    @DisplayName("등록된 카테고리별 예산을 수정할 수 있다.")
    @WithUserDetails(value = testEmail, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void patchBudget() throws Exception {
        // given - 3개 카테고리에 대한 예산 등록
        List<BudgetByCategory> budgetByCategories = List.of(
                new BudgetByCategory(Category.FOOD.name(), 10000),
                new BudgetByCategory(Category.HOUSING.name(), 20000),
                new BudgetByCategory(Category.HOBBY.name(), 30000)
        );
        BudgetCreateRequest request = new BudgetCreateRequest(budgetByCategories);

        mockMvc.perform(post("/api/budgets")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        assertThat(budgetRepository.findAll().size()).isEqualTo(budgetByCategories.size());

        // given - 3개 카테고리에 대한 수정 예산 준비
        List<BudgetByCategory> patchBudgetByCategories = List.of(
                new BudgetByCategory(Category.FOOD.name(), 20000),
                new BudgetByCategory(Category.HOUSING.name(), 30000),
                new BudgetByCategory(Category.HOBBY.name(), 40000)
        );

        BudgetPatchRequest budgetPatchRequest = new BudgetPatchRequest(patchBudgetByCategories);

        // when - 예산 수정 요청
        mockMvc.perform(patch("/api/budgets")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budgetPatchRequest))
                )
                .andDo(print())
                .andExpect(status().isOk());

        // then - 수정된 예산 확인
        List<Budget> budgets = budgetRepository.findAll();
        assertThat(budgets).hasSize(3)
                .extracting("amount", "category")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(20000, Category.FOOD),
                        Tuple.tuple(30000, Category.HOUSING),
                        Tuple.tuple(40000, Category.HOBBY)
                );
    }

    @DisplayName("예산 수정 요청이 비어 있다면 에러 메시지로 응답한다.")
    @WithUserDetails(value = testEmail, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void patchBudget_givenNull_thenErrorMessage() throws Exception {
        // given - 비어 있는 수정 요청 field
        BudgetPatchRequest budgetPatchRequest = new BudgetPatchRequest(null);

        // when, then
        mockMvc.perform(patch("/api/budgets")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budgetPatchRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.GEN_NOT_VALID_ARGUMENTS.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.GEN_NOT_VALID_ARGUMENTS.getMessage()))
                .andExpect(jsonPath("$.errors.budgetByCategories[0]").value("수정 예산 리스트는 비어 있으면 안됩니다."));
    }

}
