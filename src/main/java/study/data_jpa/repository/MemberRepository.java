package study.data_jpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.jar.Attributes;

public interface MemberRepository extends JpaRepository<Member, Long> {


    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    //spring data jpa에서 named query 호출
//    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);


    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);


    @Query("select m.username from Member m")
    List<String> findUsernameList();


    @Query("select new study.data_jpa.dto.MemberDto (m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();


    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);


    // find + 반환타입 명시 by + 조건절
    List<Member> findListByUsername(String username); //컬렉션
    Member findMemberByUsername(String username); //단건
    Optional<Member> findOptionalByUsername(String username); // 단건 Optional


    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m") //엔티티 조회시 outer join이 필요할 때, count는 join 없이 count하도록 해야 함.
    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true) // update연산 실행 후 영속성 컨텍스트를 자동으로 clear해서 DB와 영속성 컨텍스트의 싱크가 안맞을 일 없도록 예방할 수 있음.
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();


    @Override
    @EntityGraph(attributePaths = {"team"}) // fetch 조인을 어노테이션으로 구현
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph("Member.all")
//    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true")) //변경감지를 위한 스냅샷을 만들지 않음
    Member findReadOnlyByUsername(String username);

    //select for update
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

}
