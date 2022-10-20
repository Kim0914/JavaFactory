package com.example.core.order;

import com.example.core.discount.DiscountPolicy;
import com.example.core.member.Member;
import com.example.core.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService{
   
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    // @Autowired // 자동으로 의존 관계 대입 (생성자 주입)
    public OrderServiceImpl(MemberRepository memberRepository, @Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    /** 필드 주입 -> 권장 X
     * @Autowired private final MemberRepository memberRepository;
     * @Autowired private final DiscountPolicy discountPolicy
     */

    /** 수정자 주입
     * @Autowired
     public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }
    **/
    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        // 주문이 들어오면, 먼저 회원정보를 검색하고 할인정책을 거친 후 주문 생성
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);
        // 이렇게 되면 할인을 고치고 싶으면 할인만 건들면 된다.

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    // 테스트 용도
    public MemberRepository getMemberRepository(){
        return memberRepository;
    }

}
