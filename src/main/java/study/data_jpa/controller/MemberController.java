package study.data_jpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;
import study.data_jpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;


    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}") //pathvariable로 id만 입력해도 Member로 받을 수 있도록 해줌 / 조회용으로만 사용하기
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }


//    @GetMapping("/members")
//    public Page<MemberDto> list(Pageable pageable) {
//        Page<Member> page = memberRepository.findAll(pageable);
//        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), member.getTeam().getName()));
//        return map;
//    }

    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5)  Pageable pageable) {
        PageRequest request = PageRequest.of(1, 2);

        return memberRepository.findAll(pageable)
                .map(MemberDto::new);

    }


//    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
