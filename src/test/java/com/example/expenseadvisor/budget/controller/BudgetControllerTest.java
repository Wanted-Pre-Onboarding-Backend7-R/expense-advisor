package com.example.expenseadvisor.budget.controller;

import com.example.expenseadvisor.budget.domain.Budget;
import com.example.expenseadvisor.budget.dto.BudgetByCategory;
import com.example.expenseadvisor.budget.dto.BudgetCreateRequest;
import com.example.expenseadvisor.budget.repository.BudgetRepository;
import com.example.expenseadvisor.category.domain.Category;
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
    void budgetByCategory() throws Exception {
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

//    @DisplayName("예산 설정 요청에는 모든 카테고리에 대한 예산이 들어 있어야 한다. 아니라면 놓친 카테고리에 대한 에러 응답 메시지를 보내야 한다.")
//    @WithUserDetails(value = testEmail, setupBefore = TestExecutionEvent.TEST_EXECUTION)
//    @Test
//    void test() throws Exception {
//        // given - 카테고리 3번 누락
//        List<BudgetByCategory> budgetByCategories = List.of(
//                new BudgetByCategory("FOOD", 10000),
//                new BudgetByCategory("HOUSING", 20000)
//                //new BudgetByCategory("HOBBY", 30000)
//        );
//        BudgetCreateRequest request = new BudgetCreateRequest(budgetByCategories);
//
//        // when
//        mockMvc.perform(post("/api/budgets")
//                        .contentType(APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                )
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//
//        // then
//    }

}
