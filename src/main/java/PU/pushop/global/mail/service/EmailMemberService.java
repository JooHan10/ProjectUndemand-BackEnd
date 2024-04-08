package PU.pushop.global.mail.service;

import PU.pushop.global.mail.entity.MailMemberDto;
import PU.pushop.members.entity.Member;
import PU.pushop.members.repository.MemberRepositoryV1;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmailMemberService {

    private final JavaMailSender mailSender;
    private final MemberRepositoryV1 memberRepositoryV1;

    @Transactional
    public void add(Member member) throws Exception {


        String receiverMail = member.getEmail();
        MimeMessage message = mailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, receiverMail); // 받는 분 메일 추가
        message.setSubject("PU Shopping mall 회원가입 이메일 인증입니다. "); // 제목

        String body = "<div>"
                + "<h1> 안녕하세요. PU Shopping mall 입니다.!</h1>"
                + "<br>"
                + "<p>저희 쇼핑몰을 이용해주셔서 감사합니다. .<p>"
                + "<p>아래 링크를 클릭하면 이메일 인증이 완료됩니다.<p>"
                + "<a href='http://localhost:8080/auth/verify?token=" + member.getToken() + "'>인증 링크</a>"
                + "<p>즐거운 쇼핑 되세요.!<p>"
                + "</div>";
        message.setText(body, "utf-8", "html");// 내용, charset 타입, subtype
        message.setFrom(new InternetAddress("gokorea1214@naver.com", "PU_ADMIN")); // 보내는 사람
        mailSender.send(message);
    }

    @Transactional
    public Member updateByVerifyToken(String token) {
        Member member = memberRepositoryV1.findByToken(token);
        System.out.println("member = " + member.getToken());

        // 검색된 회원이 있을 경우, 업데이트 수행
        if (member != null) {
            // 회원 정보를 업데이트합니다.
            member.updateMemberByToken(token);
            member.certifyByEmail();
            memberRepositoryV1.save(member);
        }
        // 업데이트된 또는 검색된 회원을 반환합니다.
        return member;
    }
}